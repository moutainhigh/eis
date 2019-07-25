package com.maicard.mb.service.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessWsMessageOperate;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.StringTools;
import com.maicard.mb.domain.EsSession;
import com.maicard.mb.service.EsMessageListener;
import com.maicard.mb.service.EsSessionService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateCode;

@Service
public class EisWebSocketHandler extends TextWebSocketHandler {

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	private ApplicationContextService applicationContextService;

	@Autowired
	private EsSessionService wsSessionService;
	
	ObjectMapper om = JsonUtils.getNoDefaultValueInstance();


	/**
	 * 动作与处理该动作的所有bean的一个集合<br>
	 * 当收到某个动作时，即调用对应Set中的所有bean处理具体业务
	 */
	private static ConcurrentHashMap<String, Set<EsMessageListener>> handlerMap = null;



	/*public EisWebSocketHandler(SimpMessagingTemplate template){

	}*/


	/*	@OnMessage
	public void onMessage(Session session, String message) throws Exception {
		if(logger.isDebugEnabled()){
			if(logger.isDebugEnabled())logger.debug("获取到WS消息:" +  message);
		}
		Map<String,String> params = HttpUtils.getRequestDataMap(message);
		String action = params.get("action");
		if(action == null){
			logger.error("消息中未提交操作代码，消息正文:" + message);
			session.getBasicRemote().sendText(om.writeValueAsString(new EisMessage(EisError.requiredDataNotFound.id,"请提交操作代码")));		
			//session.close();
		}

		// template.convertAndSend("/topic/getLog", text); // 这里用于广播
	}*/




	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message){
		
		String text = message.getPayload() != null ? message.getPayload().trim() : null;
		if(logger.isDebugEnabled()){
			if(logger.isDebugEnabled())logger.debug("获取到WS客户端:" + session.getId() + ",来自:" + session.getLocalAddress().getHostString() +  "的消息:" +  message.getPayload());
			//siteDomainRelationService.getByHostName(hostName);
		}
		//session.
		/*for(String key : session.getAttributes().keySet()){
			if(logger.isDebugEnabled())logger.debug("当前session属性:" + key + "=>" + session.getAttributes().get(key));
		}*/
		//if(logger.isDebugEnabled())logger.debug("当前线程数:" + applicationContextService.getThreadCount());

		Map<String,String> params = HttpUtils.getRequestDataMap(text);
		
		EsSession esSession = null;
		Object esSessionId = session.getAttributes().get("esSessionId");
		if(esSessionId != null){
			 esSession = wsSessionService.get(esSessionId.toString());
		}
		if(esSession == null){
			Object o = session.getAttributes().get("ownerId");
			if(logger.isDebugEnabled())logger.debug("从附加属性中获取ownerId=" + o);
			long ownerId = 0;
			if(o != null && o instanceof Long){
				ownerId = (long)o;
			}
			o = session.getAttributes().get("serverId");
			if(logger.isDebugEnabled())logger.debug("从附加属性中获取serverId=" + o);
			int serverId = 0;
			if(o != null && o instanceof Integer){
				serverId = (int)o;
			}
			String newEsSessionId = esSessionId == null ? UUID.randomUUID().toString() : esSessionId.toString();
			esSession = new EsSession(newEsSessionId, session.getId(),ownerId);
			if(esSessionId == null){
				session.getAttributes().put("esSessionId", newEsSessionId);
			}
			esSession.setServerId(serverId);
			
		}
		Object o = session.getAttributes().get("clientIp");
		if(o != null){
			esSession.setClientIp(o.toString());
		}
		o = session.getAttributes().get("serverURL");
		if(logger.isDebugEnabled())logger.debug("从附加属性中获取serverId=" + o);
		if(o != null){
			esSession.getAttributes().put("serverURL", o.toString());
		}
		if(esSession.getLastSaveTime() == null || (new Date().getTime() - esSession.getLastSaveTime().getTime()) / 1000 > CommonStandard.SESSION_SAVE_INTERVAL){
			esSession.setLastSaveTime(new Date());
			wsSessionService.put(esSession,session);
			if(logger.isDebugEnabled())logger.debug("向系统中放入EsSession对象:" + esSession.getEsSessionId() + ",nativeSessionId=" + session.getId() + ",serverId=" + esSession.getServerId());

		} else {
			logger.debug("EsSession对象的上次存储时间是:{},本次忽略",StringTools.getFormattedTime(esSession.getLastSaveTime()));
		}


		ModelMap map = new ModelMap();
		map.put("operate", OperateCode.NOTIFY.toString());
		
		String action = params.get("action");
		if(action == null){
			logger.error("消息中未提交操作代码，消息正文:" + message);
			map.put("message",new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交操作代码"));
			synchronized(session){
				try {
					session.sendMessage(new TextMessage(om.writeValueAsString(map)));
				} catch (IOException e) {
					logger.error("无法向客户端发送消息因为出现错误:" + e.getMessage());
				}
			}
			return;
		}
		
		action = action.trim();
		
		if(action.equalsIgnoreCase("ping")){
			logger.debug("进入到了连接测试=====>心跳机制,text=" + text + ",当前连接是否连通:" + session.isOpen());
			if(session.isOpen()){
				int sn = NumericUtils.parseInt(params.get("sn"));
				int savedSn = (int)esSession.getLongExtraValue("sn");
				//if(savedSn == 0){
				//}
				if(logger.isDebugEnabled())logger.debug("收到session[" + session.getId() + "]的ping测试,sn=" + sn + "，本地sn=" + savedSn + ",返回pong,sn=" + (sn+1));
				savedSn = sn;
				esSession.setExtraValue("sn", String.valueOf(savedSn));
				esSession.setExtraValue("lastPingTime", String.valueOf(System.currentTimeMillis()));
				synchronized(session){
					try {
						long oldTime = System.currentTimeMillis();
						logger.debug("当前时间戳====>"+oldTime);
						session.sendMessage(new TextMessage("action=pong&sn=" + (sn+1)+"&timeStamp=" + oldTime));
						map.clear();
						long newTime = System.currentTimeMillis();
						logger.debug("======>ws消息发送经过了{}ms",newTime - oldTime);
					} catch (IOException e) {
						logger.error("无法向客户端发送消息因为出现错误:" + e.getMessage());
					}
				}
			} else {
				if(logger.isDebugEnabled())logger.debug("收到session[" + session.getId() + "]的ping测试，但会话已关闭，无法返回pong");
			}
			return;
		}
		Date beginTime = new Date();

		if(handlerMap == null){
			initHandlerMap();
		}
		if(handlerMap == null || handlerMap.size()  < 1){
			logger.error("系统中没有类型为[" + EsMessageListener.class.getName() + "]的bean");
			return;
		}
		if(handlerMap.get(action) == null){
			logger.warn("当前系统中没有注册任何处理action=" + action + "的bean");
			return;
		}
		if(logger.isDebugEnabled()){
			if(logger.isDebugEnabled())logger.debug("系统注册了" + handlerMap.get(action).size() + "个服务来处理action=" + action);
		}
		for(EsMessageListener bean : handlerMap.get(action)){
			if(logger.isDebugEnabled())logger.debug("把消息交给类[" + bean.getClass().getName() + "]处理WS操作:" + action);
			try {
				bean.onWsMessage(esSession, params, map);
			} catch (Exception e) {
				logger.error("服务[" + bean.getClass().getName() + "]无法处理WS消息，服务抛出异常:" + e.getMessage());
				map.put("message", new EisMessage(EisError.systemException.id,"系统异常"));
				e.printStackTrace();
			}
		}

		if(map.isEmpty()){
			if(logger.isDebugEnabled())logger.debug("S消息[" + text + "]处理完成，耗时:" + (new Date().getTime() - beginTime.getTime()) + "毫秒,但返回数据MAP为空，不发送数据");
		} else if(!session.isOpen()){
			if(logger.isDebugEnabled())logger.debug("消息[" + text + "]处理完成，耗时:" + (new Date().getTime() - beginTime.getTime()) + "毫秒,但Session已关闭");
		} else {
			if(logger.isDebugEnabled())logger.debug("S消息[" + text + "]处理完成，耗时:" + (new Date().getTime() - beginTime.getTime()) + "毫秒,向Session发送消息");
			synchronized(session){
				try {
					session.sendMessage(new TextMessage(om.writeValueAsString(map)));
					if(logger.isDebugEnabled())logger.debug("ES消息[" + text + "]处理完成，耗时:" + (new Date().getTime() - beginTime.getTime()) + "毫秒,已完成向Session发送消息");
				} catch (IOException e) {
					logger.error("无法向客户端发送消息因为出现错误:" + e.getMessage());
				}
			}
			//wsSessionService.sendMessage(map,session.getId());
		} 
		
		//FIXME 为啥每次要放入呢？
		//wsSessionService.put(esSession,session);

		return;


	}

	private synchronized void initHandlerMap() {
		ApplicationContext applicationContext = applicationContextService.getApplicationContext();

		Map<String,EsMessageListener>map  = applicationContext.getBeansOfType(EsMessageListener.class);
		if(map == null || map.size() < 1){
			logger.error("系统中没有类型为[" + EsMessageListener.class.getName() + "]的bean");
			return;
		}
		for(String beanName : map.keySet()){

			ProcessWsMessageOperate processObjectAnnotation = applicationContext.findAnnotationOnBean(beanName, ProcessWsMessageOperate.class);

			if(processObjectAnnotation == null){			
				if(logger.isDebugEnabled()){
					if(logger.isDebugEnabled())logger.debug(beanName + "未声明ProcessWsMessageOperate注解");
				}
				continue;
			}
			if(processObjectAnnotation.value() == null || processObjectAnnotation.value().length < 1){
				if(logger.isDebugEnabled()){
					if(logger.isDebugEnabled())logger.debug(beanName + "的ProcessWsMessageOperate注解内容为空");
				}
				continue;
			}
			if(handlerMap == null){
				handlerMap = new ConcurrentHashMap<String, Set<EsMessageListener>>();
			}

			for(String value : processObjectAnnotation.value()){
				if(StringUtils.isBlank(value)){
					if(logger.isDebugEnabled())logger.debug("忽略[" + beanName + "]类的注解ProcessWsMessageOperate中的空指令");
					continue;
				}
				if(handlerMap.get(value) == null){
					handlerMap.put(value, new HashSet<EsMessageListener>());
				}
				handlerMap.get(value).add(map.get(beanName));
				/*if(logger.isDebugEnabled()){
					if(logger.isDebugEnabled())logger.debug("把bean" + beanName + "作为操作[" + value + "]的处理者");
				}*/
			}

		}
		logger.info("系统WS处理器初始化完成，共注册:" + handlerMap.size() + "个指令");


	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		if(logger.isDebugEnabled())logger.debug("WS session[" + session.getId() + "]连接已关闭，向系统消息总线发送DISCONNECT消息");
		//发送用户已断线的消息
		this.handleTextMessage(session, new TextMessage("action=DISCONNECT"));
		Object o = session.getAttributes().get("esSessionId");
		if(o != null){
			wsSessionService.remove(o.toString());
		} else {
			if(logger.isDebugEnabled())logger.debug("WS session[" + session.getId() + "]连接已为空或找不到其中的esSessionId扩展数据，不执行删除");
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		if(logger.isDebugEnabled())logger.debug("WS session[" + session.getId() + "]建立了连接");

		super.afterConnectionEstablished(session);
	}


}

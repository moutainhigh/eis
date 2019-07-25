package com.maicard.mb.service.rabbitmq;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.annotation.ProcessMessageOperate;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.JmsObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.EsMessageListener;
import com.maicard.mb.service.JmsDataReplicateService;
import com.maicard.mb.service.JmsDataSyncService;
import com.maicard.mb.service.MessageService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.Operate;
import com.maicard.standard.MessageStandard.MessageLevel;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 基于RabbitMQ的全局消息消费者实现
 * 1、接收JMS队列中的消息
 * 2、调用容器中的其他服务的onMessage接口
 * 3、提供对队列的数据查询
 * 
 * 
 * @author NetSnake
 * @date 2012-10-09
 */

@Service
public class MessageServiceImpl extends BaseService implements MessageService,MessageListener,ApplicationContextAware{


	@Resource
	private ConfigService configService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private RabbitTemplate rabbitTemplate ;
	@Resource
	private JmsDataSyncService jmsDataSyncService;
	@Resource
	private JmsDataReplicateService jmsDataReplicateService;
	@Resource
	private MessagePostProcessor eisMessagePostProcessor;
	
	@Value("${MQ_ENABLED:true}")
	private boolean mqEnabled;

	private String appId = null;


	private int messageQueueSize = 500;

	private String jmsNodeId = null;

	private int jmsDefaultTtl = 0;

	private ApplicationContext applicationContext;

	private boolean isSiteReplicationSlaver;

	private final ObjectMapper om = new ObjectMapper();

	private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	
	private final SimpleMessageConverter converter = new SimpleMessageConverter();

	/**
	 * 动作与处理该动作的所有bean的一个集合<br>
	 * 当收到某个动作时，即调用对应Set中的所有bean处理具体业务
	 */
	private static ConcurrentHashMap<String, Set<String>> handlerMap = null;

	@PostConstruct
	public void init(){
		messageQueueSize = configService.getIntValue(DataName.messageQueueSize.toString(),0);
		if(messageQueueSize == 0){
			messageQueueSize = 500;
		}
		jmsDefaultTtl = configService.getIntValue(DataName.jmsDefaultTtl.toString(),0);
		if(jmsDefaultTtl == 0){
			jmsDefaultTtl = 24 * 3600 * 1000;
		}
		jmsNodeId = configService.getSystemCode() + "." + configService.getServerId();

		isSiteReplicationSlaver = configService.getBooleanValue(DataName.isSiteReplactionSlaver.toString(),0)
				&& configService.getBooleanValue(DataName.handlerSiteReplicationSlaver.toString(),0);

		om.setDateFormat(sdf);
		appId = configService.getSystemCode() + "." + configService.getServerId();

	}

	@Override
	public void reply(String destination, String queue, String correlationId, Serializable m) {
		logger.debug("尝试回复消息到回复队列:" + queue + ",correlationId:" + correlationId);
		try{
			rabbitTemplate.setQueue(queue);		
			//CorrelationData correlationData = new CorrelationData(correlationId);
			//rabbitTemplate.convertAndSend(null,m, correlationData);
			//rabbitTemplate.convertAndSend(null,m, eisMessagePostProcessor, correlationData);

			Message message = createMessage(m, new MessageProperties());
			message.getMessageProperties().setAppId(appId);
			message.getMessageProperties().setCorrelationId( correlationId.getBytes());
			message.getMessageProperties().setMessageId(message.getMessageProperties().getAppId() + "-" + UUID.randomUUID().toString());
			//rabbitTemplate.(object, correlationData)
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
	}
	protected Message createMessage(Object objectToConvert,
			MessageProperties messageProperties) {
		byte[] bytes = null;
		try {
			String jsonString = om.writeValueAsString(objectToConvert);
			bytes = jsonString.getBytes("UTF-8");
		} catch (Exception  e) {
			e.printStackTrace();
			throw new MessageConversionException(
					"Failed to convert Message content", e);
		} 
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding("UTF-8");
		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		return new Message(bytes, messageProperties);

	}

	@Override
	@Async
	public String send(String destination, Serializable m){
		/*if(StringUtils.isBlank(destination)){
			if(logger.isWarnEnabled()){
				logger.warn("消息目的地为空，停止发送");
			}
			return null;
		}*/
		if(!mqEnabled){
			logger.debug("系统已关闭MQ功能");
			return null;
		}
		if(m == null){
			if(logger.isWarnEnabled()){
				logger.warn("消息对象为空，停止发送");
			}
			return null;
		}
		try{
			logger.debug("尝试发送消息至[" + destination + ",类型:" + m.getClass().getName() + "]");
			rabbitTemplate.convertAndSend(m, eisMessagePostProcessor);
			return null;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Async
	public Object sendAndReceive(String destination, Serializable m){

		if(m == null){
			if(logger.isWarnEnabled()){
				logger.warn("消息对象为空，停止发送");
			}
			return null;
		}
		try{
			Object object = rabbitTemplate.convertSendAndReceive(m, eisMessagePostProcessor);
			logger.debug("尝试发送等待返回消息至[" + destination + ",类型:" + m.getClass().getName() + "]，结果:" + object);

		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public void onMessage(Message message){
		if(message == null){
			return;
		}

		try{

			String correlationId = null;
			if(message.getMessageProperties().getCorrelationId() != null){
				correlationId = new String(message.getMessageProperties().getCorrelationId());
			}
			if(logger.isDebugEnabled()){

				logger.debug("全局消息驱动处理器收到消息[messageId=" + message.getMessageProperties().getMessageId() + ",length=" + message.getBody().length + "correlationId=" + correlationId + "]");		
			}
			if(StringUtils.isBlank(message.getMessageProperties().getAppId()) && StringUtils.isBlank(message.getMessageProperties().getReplyTo())){
				if(logger.isDebugEnabled()){
					logger.debug("忽略未知APPID且不是需回复的消息[" + message.getMessageProperties().getMessageId() + "]");
				}
				return;
			}
			if(message.getMessageProperties().getAppId() != null && message.getMessageProperties().getAppId().equals(jmsNodeId)){
				if(logger.isDebugEnabled()){
					logger.debug("忽略本地消息[" + message.getMessageProperties().getMessageId() + "]");
				}
				return;
			}
			//byte[] correlationId = message.getMessageProperties().getCorrelationId();
			/*System.out.println("XX:correlationId=>" + message.getMessageProperties().getCorrelationId());
			System.out.println("XX:replyToAddress=>" + message.getMessageProperties().getReplyToAddress());
			for(String head : message.getMessageProperties().getHeaders().keySet()){
				System.out.println("XXXXXXXX>>>>" + head + "===>" + message.getMessageProperties().getHeaders().get(head));
			}*/

			Object object = null;
			try{
				object = converter.fromMessage(message);
			}catch(Exception e){
				logger.error("无法解析消息:{},异常:{}",message, e.getMessage());
			}
			
			if(object == null){
				return;
			}
			
			/*JsonMessageConverter converter = new JsonMessageConverter();
			Object object = converter.fromMessage(message);*/
			//Object object = om.readValue(message.getBody(),EisMessage.class);

			logger.debug("消息处理器收到的消息是:" + ( object == null ? "空" : object.getClass().getName()));
			//converter.fromMessage(message);
			if(object instanceof EisMessage){
				EisMessage eisMessage = (EisMessage)object;
				if(StringUtils.isNotBlank(message.getMessageProperties().getReplyTo())){
					logger.debug("[" + message.getMessageProperties().getMessageId() + "]是一条需要回复的消息,回复queue=" + message.getMessageProperties().getReplyTo() + ",receiveRoutingKey=" + message.getMessageProperties().getReceivedRoutingKey());
					//eisMessage.setReceiverName(message.getMessageProperties().getReplyTo());
					eisMessage.setReplyMessageId(new String(message.getMessageProperties().getCorrelationId()));
				} else {
					eisMessage.setMessageId(message.getMessageProperties().getMessageId());
				}
				eisMessage.setMessageId(message.getMessageProperties().getMessageId());

				operate(eisMessage);
				
			} else {
				logger.error("消息处理器收到的消息类型不是EisMessage，是:" + ( object == null ? "空" : object.getClass().getName()) + ",无法处理");

			}
			object = null;
			//converter = null;

		}catch(Exception e){
			e.printStackTrace();
		}		
		message = null;

	}

	/*
	 * 查找当前环境中标记为EisMessageListener接口的所有bean
	 * 并提供了ListenEisMessage注解
	 * 同时比对ListenEisMessage注解的值：
	 * 1、如果objectType与消息中的objectType一致才是有效的接收者
	 * 2、如果同时注解了operate，那么还要比较operate值是否与消息中的operate一致
	 */
	private void operate(EisMessage eisMessage) throws Exception{
		long beginTime = new Date().getTime();
		if(eisMessage.getOperateCode() == Operate.JmsDataSync.getId()){
			if(isSiteReplicationSlaver){
				jmsDataReplicateService.operate(eisMessage);
				try{
					if(jmsDataReplicateService.getValidSiteReplicationObject(eisMessage.getAttachment().get("updateSlaveParamaters")) != null){
						if(logger.isDebugEnabled()){
							logger.debug("转发收到的站点复制消息");
						}
						send(null, eisMessage);
					} else {
						if(logger.isDebugEnabled()){
							logger.debug("从站点复制不支持JMS数据复制对象的类型，不再转发");
						}
					}
				}catch(Exception e){
					logger.warn("在JMS数据复制消息中未找到合法的附件[updateSlaveParamaters]");
				}

				return;
			}
			jmsDataSyncService.operate(eisMessage);
			return;
		}

		if(handlerMap == null){
			initHandlerMap();
		}
		if(handlerMap == null || handlerMap.size()  < 1){
			logger.error("系统中没有类型为[" + EsMessageListener.class.getName() + "]的bean");
			return;
		}



		if(eisMessage.getObjectType() == null || eisMessage.getObjectType().equals("")){
			logger.warn("消息[" + eisMessage.getMessageId() + "]未定义处理对象");

			return;
		}
		if(logger.isDebugEnabled()){
			logger.debug("处理消息[对象类型=" + eisMessage.getObjectType() + ",操作:" + eisMessage.getOperateCode() + "]");
		}
		Set<String> objectTypeProcessorName = handlerMap.get(eisMessage.getObjectType());
		if(objectTypeProcessorName == null || objectTypeProcessorName.size() < 1){
			logger.warn("系统中没有处理对象[" + eisMessage.getObjectType() + "]的任何bean");
			return;
		}



		for(String beanName : objectTypeProcessorName){
			if(logger.isDebugEnabled()){
				logger.debug("尝试匹配服务[" + beanName + "]");
			}

			if(isProcessOperate(beanName, eisMessage.getOperateCode()) == 0){//定义了操作模式但不合法
				if(logger.isDebugEnabled()){
					logger.debug(beanName + "定义了操作模式但不相符");
				}
				continue;
			}
			if(logger.isDebugEnabled()){
				logger.debug("由[" + beanName + "]处理该消息");
			}
			EisMessageListener processor = applicationContextService.getBeanGeneric(beanName);
			processor.onMessage(eisMessage);

		}


		if(logger.isDebugEnabled())logger.debug("消息[" + eisMessage.getMessageId() + "]处理完成,收到消息的时间是:" + sdf.format(beginTime) + ",处理耗时" + (new Date().getTime() - beginTime) + "毫秒");
		return;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}


	@Override
	public EisMessage syncSend(String destination, String replyQueueName, Serializable m){
		String uuid = UUID.randomUUID().toString();
		try{
			logger.debug("尝试发送并等待回复[" + uuid + "]...");
			Object object = rabbitTemplate.convertSendAndReceive(m);
			logger.debug("得到了[" + uuid + "]的回复消息:" + object);
			return (EisMessage)object;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		logger.error("未能得到[" + uuid + "]的回复消息");
		return null;
	}




	private int isProcessOperate(String beanName, int operateCode){
		if(operateCode == 0){
			return 2;
		}
		ProcessMessageOperate processOperateAnnotation = applicationContext.findAnnotationOnBean(beanName, ProcessMessageOperate.class);
		if(processOperateAnnotation != null){
			if(processOperateAnnotation.value()[0].equals("*")){
				if(logger.isDebugEnabled()){
					logger.debug("bean[" + beanName + "]注解处理所有操作[*]");
				}
				return 1;
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("检查bean[" + beanName + "]的处理操作");
				}
				for(String targetOperate : processOperateAnnotation.value()){
					boolean isValid = false;
					for(Operate op : Operate.values()){
						if(op.getCode().toString().equals(targetOperate)){
							isValid = true;
							break;
						}
					}
					Operate op = Operate.close.findByCode(targetOperate);
					if(op != null && operateCode == op.getId() && isValid){
						if(logger.isDebugEnabled()){
							logger.debug("bean[" + beanName + "]注解处理[" + targetOperate + "]操作的对象");
						}
						return 1;
					}


				}
			}
			return 0;
		} else {
			return 2;
		}

	}






	@Override
	public void sendJmsDataSyncMessage(String destination, String beanName,
			String methodName, Object... objects) {
		if(!mqEnabled){
			logger.info("系统已关闭MQ功能");
			return;
		}
		if(objects != null && objects.length > 0){
			for(Object object : objects){
				if(object instanceof JmsObject){
					JmsObject jObject = (JmsObject)object;
					if(jObject.getSyncFlag() == 1){
						logger.warn("参数中有一个对象[" + object + "]的同步标记为1，停止发送同步消息");
						return;
					}
					jObject.setSyncFlag(1);
				}		
			}
		}
		/*JmsObject jObject = null;
		if(object instanceof JmsObject){
			jObject = (JmsObject)object;
		}
		if(jObject != null){
			if(jObject.getSyncFlag() == 1){
				logger.warn("对象[" + object + "]的同步标记为1，停止发送同步消息");
				return;
			}
			jObject.setSyncFlag(1);
		}*/
		EisMessage syncMessage = new EisMessage();
		syncMessage.setOperateCode(Operate.JmsDataSync.getId());
		syncMessage.setMessageLevel(MessageLevel.system.getCode());
		syncMessage.setAttachment(new HashMap<String,Object>());
		//syncMessage.getAttachment().put("updateSlaveParamaters", jObject == null ? object : new Object[]{jObject});
		syncMessage.getAttachment().put("updateSlaveParamaters", objects);
		syncMessage.getAttachment().put("updateSlaveBeanName", beanName);	
		syncMessage.getAttachment().put("updateSlaveMethodName", methodName);	
		syncMessage.setNeedReply(false);
		send(destination, syncMessage);
		syncMessage = null;

	}

	private synchronized void initHandlerMap() {
		ApplicationContext applicationContext = applicationContextService.getApplicationContext();

		Map<String,EisMessageListener>map  = applicationContext.getBeansOfType(EisMessageListener.class);
		if(map == null || map.size() < 1){
			logger.error("系统中没有类型为[" + EisMessageListener.class.getName() + "]的bean");
			return;
		}
		if(handlerMap == null){
			handlerMap = new ConcurrentHashMap<String, Set<String>>();
		}
		for(String beanName : map.keySet()){

			ProcessMessageObject processObjectAnnotation = applicationContext.findAnnotationOnBean(beanName, ProcessMessageObject.class);

			if(processObjectAnnotation == null){			
				if(logger.isDebugEnabled()){
					logger.debug(beanName + "未声明ProcessWsMessageOperate注解");
				}
				continue;
			}
			if(processObjectAnnotation.value() == null || processObjectAnnotation.value().length < 1){
				if(logger.isDebugEnabled()){
					logger.debug(beanName + "的ProcessWsMessageOperate注解内容为空");
				}
				continue;
			}


			for(String value : processObjectAnnotation.value()){
				if(StringUtils.isBlank(value)){
					logger.debug("忽略[" + beanName + "]类的注解ProcessMessageObject中的空指令");
					continue;
				}
				if(handlerMap.get(value) == null){
					handlerMap.put(value, new HashSet<String>());
				}
				handlerMap.get(value).add(beanName);
				if(logger.isDebugEnabled()){
					logger.debug("把bean" + beanName + "作为MDB对象[" + value + "]的处理者");
				}
			}

		}
		logger.info("MDB消息处理器缓存初始化完毕，系统共注册了" + map.size() + "个EisMessageListener处理器，当前有" + handlerMap.size() + "种处理对象"); 

	}




}

package com.maicard.mb.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.criteria.UserSubscribeMessageRelationCriteria;
import com.maicard.mb.criteria.UserSubscribeStatusCriteria;
import com.maicard.mb.dao.UserMessageDao;
import com.maicard.mb.domain.EisTopic;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.domain.UserMessageRelation;
import com.maicard.mb.domain.UserSubscribeStatus;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.EisTopicService;
import com.maicard.mb.service.MessageGatewayService;
import com.maicard.mb.service.MessageService;
import com.maicard.mb.service.SmsService;
import com.maicard.mb.service.UserMessageService;
import com.maicard.mb.service.UserSubscribeMessageRelationService;
import com.maicard.mb.service.UserSubscribeStatusService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.dao.FrontUserDao;
import com.maicard.security.dao.PartnerDao;
import com.maicard.security.dao.SysUserDao;
import com.maicard.security.domain.User;
import com.maicard.security.service.UserDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.MessageStandard;
import com.maicard.standard.Operate;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionType;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@ProcessMessageObject("userMessage")
public class UserMessageServiceImpl extends BaseService implements UserMessageService,EisMessageListener {

	@Resource
	private UserMessageDao userMessageDao;

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private EisTopicService eisTopicService;
	@Resource
	private MessageService messageService;
	@Resource
	private FrontUserDao frontUserDao;
	@Resource
	private PartnerDao partnerDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private UserDataService userDataService;
	@Resource
	private UserSubscribeStatusService userSubscribeStatusService;
	@Resource
	private UserSubscribeMessageRelationService userSubscribeMessageRelationService;

	private boolean handlerUserMessageUpdate = false;
	private String[] defaultSendMode;

	private String beanEmailGateway;

	private int frontUserStartUuid;

	@PostConstruct
	public void init(){
		handlerUserMessageUpdate = configService.getBooleanValue(DataName.handlerUserMessageUpdate.toString(),0);
		
		String sendArray = configService.getValue(DataName.userMessageDefaultSendMode.toString(),0);
		if(StringUtils.isBlank(sendArray)){
			defaultSendMode = new String[]{MessageStandard.UserMessageSendMethod.site.toString()};
		} else {
			defaultSendMode = sendArray.split(",");

		}
		beanEmailGateway = configService.getValue(DataName.beanEmailGateway.toString(),0);
		frontUserStartUuid = configService.getIntValue(DataName.frontUserStartUuid.toString(),0);
	}

	@Override
	public int insert(UserMessage userMessage) {
		if(userMessage == null){
			return -1;
		}
		if(StringUtils.isBlank(userMessage.getMessageId())){
			generateMessageId(userMessage);
		}
		return insertLocal(userMessage);

		/*if(handlerUserMessageUpdate){
			return insertLocal(userMessage);

		} else {
			return insertRemote(userMessage);
		}*/

	}



	@Override
	public int insertLocal(UserMessage userMessage){
		if(userMessage == null){
			return -1;
		}
		if(userMessage.getSenderStatus() == MessageStatus.queue.id){
			userMessage.setSenderStatus(MessageStatus.sent.id);
		}
		try{
			if( userMessageDao.insert(userMessage) == 1 ){
				return 1;
			}
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法插入数据库:" + e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			logger.error("无法插入消息[" + userMessage.getContent() + "]数据库:" + ExceptionUtils.getFullStackTrace(e));
		}
		return -1;
	}

	/*private int insertRemote(UserMessage userMessage) {
		if(userMessage == null){
			return 0;
		}
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.create.getId());
		m.setMessageLevel(MessageLevel.user.getCode());
		m.setObjectType(ObjectType.userMessage.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("userMessage", userMessage);	
		m.setNeedReply(false);
		try{
			messageService.send(messageBusName, m);
			m = null;
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;		
	}
*/
	@Override
	public int delete(String messageId) {
		
		int rs = deleteLocal(messageId);
		if(rs == 1){
			messageService.sendJmsDataSyncMessage(null, "userMessageService", "delete", messageId);
		}
		return rs;
		/*if(handlerUserMessageUpdate){
			return deleteLocal(messageId);
		} else {
			return deleteRemote(messageId);
		}*/
	}

	private int deleteLocal(String messageId){
		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setMessageId(messageId);
		List<UserMessage> msgList = list(messageCriteria);
		logger.debug("进入deleteLocal,msgList为"+msgList);
		if(msgList == null || msgList.size() != 1){
			return -1;
		}
		return userMessageDao.delete(messageId);
	}

	/*private int deleteRemote(String messageId) {
		if(messageId == null){
			return 0;
		}
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.delete.getId());
		m.setObjectType(ObjectType.userMessage.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("userMessage", messageId);	
		m.setNeedReply(false);
		try{
			messageService.send(messageBusName, m);
			m = null;
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;

	}
*/
	@Override
	public UserMessage select(String messageId) {
		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setMessageId(messageId);
		List<UserMessage> msgList = list(messageCriteria);
		if(msgList == null || msgList.size() != 1){
			return null;
		}
		UserMessage userMessage = msgList.get(0);
		afterFetch(userMessage);
		return userMessage;

	}

	@Override
	public List<UserMessage> list(MessageCriteria messageCriteria) {
		List<UserMessage> userMessageList= userMessageDao.list(messageCriteria);
		if(userMessageList == null || userMessageList.size() < 1){
			return Collections.emptyList();
		}	
		for(int i =0; i < userMessageList.size(); i++){
			userMessageList.get(i).setIndex(i+1);
			afterFetch(userMessageList.get(i));
		}
		return userMessageList;
	}

	@Override
	public List<UserMessage> listOnPage(MessageCriteria messageCriteria) {
		List<UserMessage> userMessageList =  userMessageDao.listOnPage(messageCriteria);
		if(userMessageList == null || userMessageList.size() < 1){
			return Collections.emptyList();
		}
		for(int i =0; i < userMessageList.size(); i++){
			userMessageList.get(i).setIndex(i+1);
			afterFetch(userMessageList.get(i));
		}
		return userMessageList;
	}

	@Override
	public int update(UserMessage eisMessage) {
		if(eisMessage == null){
			return -1;
		}
		
		return updateLocal(eisMessage);

		/*if(handlerUserMessageUpdate){
			return updateLocal(eisMessage);
		} else {
			return updateRemote(eisMessage);
		}	*/	
	}

	private int updateLocal(UserMessage eisMessage){
		try{
			return userMessageDao.update(eisMessage);
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法更新数据库:" + e.getMessage());
			e.printStackTrace();
		}
		return -1;

	}

	/*private int updateRemote(UserMessage userMessage) {
		if(userMessage == null){
			return 0;
		}
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.update.getId());
		m.setObjectType(ObjectType.userMessage.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("userMessage", userMessage);	
		m.setNeedReply(false);
		try{
			messageService.send(messageBusName, m);
			m = null;
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;		
	}
*/

	@Override
	public  int count(MessageCriteria messageCriteria){
		return userMessageDao.count(messageCriteria);
	}



	@Override
	public int send(UserMessage userMessage){	
		if(StringUtils.isBlank(userMessage.getMessageId())){
			generateMessageId(userMessage);
		}
		if(userMessage.getTopicId() > 0){
			return sendTopic(userMessage);
		} else {
			return sendMsg(userMessage);
		}
		/*if(handlerUserMessageUpdate){
			if( sendLocal(userMessage) == 1){
				messageService.sendJmsDataSyncMessage(messageBusName, "userMessageService", "insertLocal", userMessage);
				return 1;
			}
			return 0;
		} else {
			return sendRemote(userMessage);
		}*/
	}


/*
	private int sendRemote(UserMessage userMessage){
		if(userMessage == null){
			return 0;
		}
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.sendNotify.getId());
		m.setMessageLevel(MessageLevel.user.getCode());
		m.setObjectType(ObjectType.userMessage.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("userMessage", userMessage);	
		m.setNeedReply(false);
		try{
			messageService.send(messageBusName, m);
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;		
	}*/



	private int sendMsg(UserMessage eisMessage){

		boolean sendSmsUseAllChannel = true;

		String[] sendMode;
		if(eisMessage.getPerferMethod() == null || eisMessage.getPerferMethod().length < 1){
			sendMode = defaultSendMode;
		} else {
			sendMode= eisMessage.getPerferMethod();
		}
		int sendCount = 0;
		for(String sendMethod : sendMode){
			if(logger.isDebugEnabled()){
				logger.debug("检查发送方式:" + sendMethod);
			}
			if(sendMethod.equals(UserMessageSendMethod.site.toString())){
				if(insert(eisMessage) == 1){
					sendCount++;
				}
			}
			if(sendMethod.equals(UserMessageSendMethod.email.toString())){
				if(StringUtils.isBlank(beanEmailGateway)){
					logger.error("系统未配置短信网关服务");
					continue;
				}
				MessageGatewayService emailGatewayService = null;
				try{
					emailGatewayService = (MessageGatewayService)applicationContextService.getBean(beanEmailGateway);
				}catch(Exception e){
				}	
				if(emailGatewayService == null){
					logger.error("系统未部署邮件网关[" + beanEmailGateway + "]");
					continue;
				}
				try{
					emailGatewayService.send(eisMessage);
					sendCount++;
				}catch(Exception e){
					e.printStackTrace();
					logger.error("在使用邮件网关[" + beanEmailGateway + "]发送消息时出现异常:" + e.getMessage());
				}
			}
			if(sendMethod.equals(UserMessageSendMethod.sms.toString())){
				String[] beanNames = applicationContextService.getBeanNamesForType(SmsService.class);
				if(beanNames == null || beanNames.length < 1){
					logger.error("系统未配置任何一个短信网关服务");
					continue;
				}
				logger.debug("系统有" + beanNames.length + "个短信网关,是否使用多个网关发送:" + sendSmsUseAllChannel);
				int i = 1;
				for(String smsGatewayName : beanNames){
					SmsService smsGatewayService = null;
					try{
						smsGatewayService = (SmsService)applicationContextService.getBean(smsGatewayName);
					}catch(Exception e){
					}	
					if(smsGatewayService == null){
						logger.error("系统未部署短信网关[" + smsGatewayName + "]");
						continue;
					}
					logger.debug("准备使用第" + i + "个短信网关" + smsGatewayName + "发送短信");
					try{
						boolean sendSuccess = smsGatewayService.send(eisMessage);
						logger.debug("第" + i + "个短信网关" + smsGatewayName + "发送短信结果:" + sendSuccess);
				
						if(sendSuccess){
							sendCount++;
						}
						if(!sendSmsUseAllChannel && sendSuccess){
							logger.debug("第" + i + "个短信网关" + smsGatewayName + "短信发送完成，并且未设置多重发送，返回");
							break;
						}
					}catch(Exception e){
						logger.error("在使用短信网关[" + smsGatewayName + "]发送消息时出现异常:" + e.getMessage());
					}
					i++;
				}
			}

		}
		return sendCount;
	}



	private int sendTopic(UserMessage userMessage){
		
		boolean sendSmsUseAllChannel = true;

		if(userMessage == null){
			logger.error("尝试发送到Topic的用户消息为空");
			return -1;
		}
		if(userMessage.getTopicId() <= 0){
			logger.error("尝试发送到Topic的用户消息，TopicID=0");
			return -1;
		}
		EisTopic eisTopic = eisTopicService.select(userMessage.getTopicId());
		if(eisTopic == null){
			logger.error("找不到发送目标Topic[" + userMessage.getTopicId() + "]");
			return -1;
		}
		if(eisTopic.getCurrentStatus() != BasicStatus.normal.getId()){
			logger.error("发送目标Topic[" + userMessage.getTopicId() + "]的状态[" + eisTopic.getCurrentStatus() + "]异常，不能发送");
			return -1;
		}
		//插入消息
		userMessage.setReceiverId(0);
		userMessage.setReceiverName(null);
		if(insert(userMessage) != 1){
			logger.error("无法插入Topic消息:" + userMessage.getContent());
			//return 0;
		}
		//查找该订阅主题所有的订阅者
		UserSubscribeStatusCriteria userSubscribeStatusCriteria = new UserSubscribeStatusCriteria();
		userSubscribeStatusCriteria.setTopicId(userMessage.getTopicId());
		userSubscribeStatusCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<UserSubscribeStatus> userSubscribeStatusList = userSubscribeStatusService.list(userSubscribeStatusCriteria);
		if(userSubscribeStatusList == null || userSubscribeStatusList.size() < 1){
			logger.warn("订阅主题[" + eisTopic.getTopicId() + "/" + eisTopic.getTopicCode() + "]没有有效订阅者");
			return 1;
		}
		for(UserSubscribeStatus userSubscribeStatus : userSubscribeStatusList){
			String[] sendMode;
			if(userSubscribeStatus.getPerferMethod() == null || userSubscribeStatus.getPerferMethod().length < 1){
				sendMode = defaultSendMode;
			} else {
				sendMode= userSubscribeStatus.getPerferMethod();
			}
			//插入消息与订阅者关联
			UserMessageRelation userMessageRelation = new UserMessageRelation(); 
			userMessageRelation.setUuid(userSubscribeStatus.getUuid());
			userMessageRelation.setMessageId(userMessage.getMessageId());
			userMessageRelation.setCurrentStatus(MessageStatus.unread.id);				
			int rs = userSubscribeMessageRelationService.insert(userMessageRelation);
			if(logger.isDebugEnabled()){
				logger.debug("新增消息[" + userMessage.getMessageId() + "]与订阅者[" + userSubscribeStatus.getUuid() + "]的关系:" + rs);
			}
			User subscriber = null;
			subscriber = findReciver(userSubscribeStatus.getUuid());
			for(String sendMethod : sendMode){
				if(logger.isDebugEnabled()){
					logger.debug("检查发送方式:" + sendMethod);
				}
				String email = null;
				if(sendMethod.equals(UserMessageSendMethod.email.toString())){
					if(StringUtils.isNotBlank(userMessage.getReceiverName()) && userMessage.getReceiverName().matches(CommonStandard.emailPattern) && userMessage.getReceiverId() <= 0){
						logger.debug("尝试直接发送邮件至消息接收者[" + userMessage.getReceiverName() + "]");
						email = userMessage.getReceiverName();
					} else {
						logger.debug("尝试查找用户[" + userSubscribeStatus.getUuid() + "]的邮件地址");
						if(subscriber == null){
							logger.warn("找不到收件人[" + userSubscribeStatus.getUuid() + "],中止发送");
							continue;							
						}
						try{
							email = subscriber.getUserConfigMap().get(DataName.userBindMailBox.toString()).getDataValue();
						}catch(Exception e){}
						if(StringUtils.isBlank(email)){
							logger.warn("收件人[" + userSubscribeStatus.getUuid() + "]未定义绑定邮箱，无法发送邮件");
							continue;
						}
						logger.debug("将消息的收件邮箱设置为[" + email + "]");

					}
					if(StringUtils.isBlank(beanEmailGateway)){
						logger.error("系统未配置短信网关服务");
						continue;
					}
					MessageGatewayService emailGatewayService = null;
					try{
						emailGatewayService = (MessageGatewayService)applicationContextService.getBean(beanEmailGateway);
					}catch(Exception e){
					}	
					if(emailGatewayService == null){
						logger.error("系统未部署邮件网关[" + beanEmailGateway + "]");
						continue;
					}
					try{
						emailGatewayService.send(email, userMessage);
					}catch(Exception e){
						logger.error("在使用邮件网关[" + beanEmailGateway + "]发送消息时出现异常:" + e.getMessage());
					}
				}
				if(sendMethod.equals(UserMessageSendMethod.sms.toString())){
					String[] beanNames = applicationContextService.getBeanNamesForType(SmsService.class);
					if(beanNames == null || beanNames.length < 1){
						logger.error("系统未配置任何一个短信网关服务");
						continue;
					}
					for(String smsGatewayName : beanNames){
						SmsService smsGatewayService = null;
						try{
							smsGatewayService = (SmsService)applicationContextService.getBean(smsGatewayName);
						}catch(Exception e){
						}	
						if(smsGatewayService == null){
							logger.error("系统未部署短信网关[" + smsGatewayName + "]");
							continue;
						}
						try{
							boolean sendSuccess = smsGatewayService.send(userMessage);
							if(!sendSmsUseAllChannel && sendSuccess){
								break;
							}
						}catch(Exception e){
							logger.error("在使用短信网关[" + smsGatewayName + "]发送消息时出现异常:" + e.getMessage());
						}
					}
				}

			}
		}


		return 1;
	}



	private User findReciver(long uuid) {
		User user = null;
		if(uuid > frontUserStartUuid){
			user =  frontUserDao.select(uuid);
			if(user != null){
				UserDataCriteria userDataCriteria = new UserDataCriteria();
				userDataCriteria.setUserTypeId(UserTypes.frontUser.getId());
				userDataCriteria.setUuid(uuid);
				user.setUserConfigMap(userDataService.map(userDataCriteria));
				return user;
			}
		}
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUuids(uuid);
		if(sysUserDao.count(userCriteria) > 0){
			user = sysUserDao.select(uuid);
			if(user != null){
				UserDataCriteria userDataCriteria = new UserDataCriteria();
				userDataCriteria.setUserTypeId(UserTypes.sysUser.getId());
				userDataCriteria.setUuid(uuid);
				user.setUserConfigMap(userDataService.map(userDataCriteria));
				return user;
			}
		} else if(partnerDao.count(userCriteria) > 0){
			user =  partnerDao.select(uuid);
			if(user != null){
				UserDataCriteria userDataCriteria = new UserDataCriteria();
				userDataCriteria.setUserTypeId(UserTypes.partner.getId());
				userDataCriteria.setUuid(uuid);
				user.setUserConfigMap(userDataService.map(userDataCriteria));
				return user;
			}
		}
		logger.warn("找不到收件人:" + uuid);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.maicard.mb.service.EisMessageListener#onMessage(com.maicard.mb.domain.EisMessage)
	 */
	@Override
	@Async
	public void onMessage(EisMessage eisMessage) {
		if(!handlerUserMessageUpdate){
			if(logger.isDebugEnabled()){
				logger.debug("本节点不负责处理用户数据更新，也不负责从数据更新忽略消息[" + eisMessage.getMessageId() + "].");
			}
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == 0){
			if(logger.isDebugEnabled()){
				logger.debug("消息操作码为空，忽略消息[" + eisMessage.getMessageId() + "].");
			}
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == Operate.create.getId()
				|| eisMessage.getOperateCode() == Operate.delete.getId()
				|| eisMessage.getOperateCode() == Operate.update.getId()
				||  eisMessage.getOperateCode() == Operate.notify.getId()
				){
			try{
				operate(eisMessage);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} else {			 
			if(logger.isDebugEnabled()){
				logger.debug("消息操作码非法[" + eisMessage.getOperateCode() + "]，忽略消息[" + eisMessage.getMessageId() + "].");
			}
			eisMessage = null;
			return;
		}
		eisMessage = null;		
	}

	private void operate(EisMessage eisMessage) throws Exception{

		UserMessage userMessage = null;

		if(eisMessage.getOperateCode() == Operate.create.getId()){
			try{
				Object object = eisMessage.getAttachment().get("userMessage");
				if(object == null){
					return;
				}
				if(object instanceof UserMessage){
					userMessage = (UserMessage)object;
				}
				if(object instanceof LinkedHashMap){
					ObjectMapper om = new ObjectMapper();
					om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
					String textData = null;
					textData = om.writeValueAsString(object);
					userMessage = om.readValue(textData, UserMessage.class);
					if(userMessage == null){
						logger.warn("无法将请求执行的对象转换为UserMessage对象");
						return;
					}

				}

				if(userMessage == null){
					logger.error("得不到消息中的对象[userMessage]");
					return;
				}
				insertLocal(userMessage);
			}catch(Exception e){
				e.printStackTrace();
			}
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == Operate.update.getId()){
			try{
				Object object = eisMessage.getAttachment().get("userMessage");
				if(object == null){
					return;
				}
				if(object instanceof UserMessage){
					userMessage = (UserMessage)object;
				}
				if(object instanceof LinkedHashMap){
					ObjectMapper om = new ObjectMapper();
					om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
					String textData = null;
					textData = om.writeValueAsString(object);
					userMessage = om.readValue(textData, UserMessage.class);
					if(userMessage == null){
						logger.warn("无法将请求执行的对象转换为UserMessage对象");
						return;
					}

				}

				if(userMessage == null){
					logger.error("得不到消息中的对象[userMessage]");
					return;
				}
				updateLocal(userMessage);
			}catch(Exception e){
				e.printStackTrace();
			}
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == Operate.delete.getId()){
			try{
				String messageId = (String)eisMessage.getAttachment().get("userMessage");
				deleteLocal(messageId);
			}catch(Exception e){
				e.printStackTrace();
			}
			return;
		}
		if(eisMessage.getOperateCode() == Operate.notify.getId()){
			try{
				Object object = eisMessage.getAttachment().get("userMessage");
				if(object == null){
					return;
				}
				if(object instanceof UserMessage){
					userMessage = (UserMessage)object;
				}
				if(object instanceof LinkedHashMap){
					ObjectMapper om = new ObjectMapper();
					om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
					String textData = null;
					textData = om.writeValueAsString(object);
					userMessage = om.readValue(textData, UserMessage.class);
					if(userMessage == null){
						logger.warn("无法将请求执行的对象转换为UserMessage对象");
						return;
					}

				}

				if(userMessage == null){
					logger.error("得不到消息中的对象[userMessage]");
					return;
				}
				send(userMessage);
			}catch(Exception e){
				e.printStackTrace();
			}
			eisMessage = null;
			return;
		}
		eisMessage = null;
	}

	private void afterFetch(UserMessage userMessage){
		/*try{
			userMessage.setSenderStatusName(MessageStatus.deleted.findById(userMessage.getSenderStatus()).getName());
		}catch(Exception e){}
		try{
			userMessage.setReceiverStatusName(MessageStatus.deleted.findById(userMessage.getReceiverStatus()).getName());
		}catch(Exception e){}*/
	}

	private void generateMessageId(UserMessage userMessage) {
		if(userMessage.getTopicId() > 0){
			userMessage.setMessageId("1" + globalOrderIdService.generate(TransactionType.userMessage.getId()));
		} else {
			userMessage.setMessageId("0" + globalOrderIdService.generate(TransactionType.userMessage.getId()));
		}
	}

	@Override
	public List<String> getUniqueIdentify(long ownerId) {
		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setOwnerId(ownerId);
		return userMessageDao.getUniqueIdentify(messageCriteria);
	}
	
	@Override
	public List<UserMessage> popBroadcastMessage(MessageCriteria userMessageCriteria){
		
		if(userMessageCriteria.getPaging() == null){
			Paging paging = new Paging(10);
			paging.setCurrentPage(1);
			userMessageCriteria.setPaging(paging);
		}
		Assert.isTrue(userMessageCriteria.getReceiverId() > 0 , "读取广播的uuid不能为0");
		long receiver = userMessageCriteria.getReceiverId();
		//读取广播不能有接收者，这个接收者用来比较他是不是读取了这条广播
		userMessageCriteria.setReceiverId(0);
		List<UserMessage> messageList = userMessageDao.listOnPage(userMessageCriteria);
		if(messageList == null || messageList.size() < 1){
			return Collections.emptyList();
		} 
		List<UserMessage> newMessageList = new ArrayList<UserMessage>();
		
		for(UserMessage um : messageList){
			UserSubscribeMessageRelationCriteria relationCriteria = new UserSubscribeMessageRelationCriteria();
			relationCriteria.setMessageId(um.getMessageId());
			relationCriteria.setUuid(receiver);
			int count = userSubscribeMessageRelationService.count(relationCriteria);
			if(count > 0){
				//已经读取了
			} else {
				//没有读取过，那么新增一条记录
				UserMessageRelation relation = new UserMessageRelation(um.getMessageId(), relationCriteria.getUuid(), userMessageCriteria.getOwnerId());
				int rs = userSubscribeMessageRelationService.insert(relation);
				if(rs == 1){
					newMessageList.add(um);
				}
			}
			

		}
		logger.info("用户#{}共提取了{}条未读广播", userMessageCriteria.getReceiverId(),newMessageList.size() );
		return newMessageList;
		
		
		
		
		
		
		
	}

	@Override
	public int deleteSubscribe(MessageCriteria userMessageCriteria) {
		return userMessageDao.deleteSubscribe(userMessageCriteria);
	}
}

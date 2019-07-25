package com.maicard.security.service.impl;


import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.service.UuidService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.UserUtils;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.dao.FrontUserDao;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.FrontRoleService;
import com.maicard.security.service.FrontUserExtraService;
import com.maicard.security.service.FrontUserRelationService;
import com.maicard.security.service.FrontUserRoleRelationService;
import com.maicard.security.service.UserDataService;
import com.maicard.security.service.UserDynamicDataService;
import com.maicard.security.service.UserLevelProjectService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DataSource;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.ObjectUid;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;
import com.maicard.standard.SecurityStandard.UserTypes;

@Service
public class FrontUserExtraServiceImpl extends BaseService implements FrontUserExtraService {

	@Resource
	private FrontUserDao frontUserDao;	

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private FrontUserRelationService frontUserRelationService;
	@Resource
	private FrontUserRoleRelationService frontUserRoleRelationService;
	@Resource
	private FrontRoleService frontRoleService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private UserDataService userDataService;
	@Resource
	private UserLevelProjectService userLevelProjectService;
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private UuidService uuidService;

	@Resource
	private UserDynamicDataService userDynamicDataService;


	private boolean handlerUserDataUpdate;
	private boolean handlerJmsDataSyncToLocal;



	private boolean sendWelcomeMessage;
	private boolean sendWelcomeEmail;
	private boolean sendWelcomeSms;

	private String welcomeMessage; //欢迎站短
	private String welcomeEmailTemplate; //欢迎邮件的模版
	private String welcomeSms; //欢迎短信内容

	private String systemName;
	private String siteUrl;

	private int dataWriteIdelIntervalMs;	//在每次查询是否写入数据之间的间隔毫秒
	private int dataWriteMaxWaitingCount;	//数据写入的最长等待次数
	private int dataWriteIdleForWaitingCount;	//当数据库连接池中有多少个等待连接时，暂停写入数据
	private int dataWriteIdelForActiveCount;	//当数据库连接池中有多少个活动连接时，暂停写入数据
	private final String cacheName = CommonStandard.cacheNameUser;

	@PostConstruct
	public void init(){
		handlerUserDataUpdate = configService.getBooleanValue(DataName.handlerUserDataUpdate.toString(),0);
		handlerJmsDataSyncToLocal = configService.getBooleanValue(DataName.handlerJmsDataSyncToLocal.toString(),0);

		sendWelcomeMessage = configService.getBooleanValue(DataName.sendWelcomeMessageAfterRegister.toString(),0);
		sendWelcomeEmail = configService.getBooleanValue(DataName.sendWelcomeEmailAfterRegister.toString(),0);
		sendWelcomeSms = configService.getBooleanValue(DataName.sendWelcomeSmsAfterRegister.toString(),0);	

		welcomeSms = configService.getValue(DataName.userRegisterWelcomeSms.toString(),0);
		welcomeEmailTemplate = configService.getValue(DataName.userRegisterWelcomeMailTemplate.toString(),0);
		welcomeMessage = configService.getValue(DataName.userRegisterWelcomeMessage.toString(),0);

		if(sendWelcomeMessage && StringUtils.isBlank(welcomeMessage)){
			if(logger.isWarnEnabled()){
				logger.warn("系统配置了发送欢迎站短但未配置信息内容");
			}
			sendWelcomeMessage = false;
		}
		if(sendWelcomeEmail && StringUtils.isBlank(welcomeEmailTemplate)){
			if(logger.isWarnEnabled()){
				logger.warn("系统配置了发送欢迎邮件但未配置邮件模版");
			}
			sendWelcomeEmail = false;
		}

		String emailTemplate = null;
		try{
			emailTemplate = FileUtils.readFileToString(new File(applicationContextService.getServletContext().getRealPath(welcomeEmailTemplate)));
		}catch(Exception e){
			logger.error("无法读取欢迎邮件模版:" + e.getMessage());
		}
		if(StringUtils.isBlank(emailTemplate)){
			logger.error("无法读取欢迎邮件模版:" + welcomeEmailTemplate);
			welcomeEmailTemplate = null;
		} else {
			welcomeEmailTemplate = emailTemplate;
		}
		if(sendWelcomeSms && StringUtils.isBlank(welcomeSms)){
			logger.error("系统配置了发送欢迎短信但未配置短信内容");
			sendWelcomeSms = false;
		}


	}







	public int insertLocal(User frontUser) throws Exception {
		if(frontUser == null){
			return -1;
		}
		Date startTime = new Date();
		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		String inviteByCode = frontUser.getExtraValue(DataName.userInviteByCode.toString());
		if(StringUtils.isNotBlank(inviteByCode)){
			inviteByCode = inviteByCode.trim();
			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria = new UserDataCriteria();
			userDataCriteria.setUserTypeId(UserTypes.partner.getId());
			userDataCriteria.setDataCode(DataName.userInviteCode.toString());
			userDataCriteria.setDataValue(inviteByCode);
			userDataCriteria.setCorrectWithDynamicData(false);
			List<UserData> userDataList = userDataService.list(userDataCriteria);
			if(userDataList != null && userDataList.size() == 1 ){
				logger.info("根据邀请码[" + inviteByCode + "]查找对应的推广ID:" + userDataList.get(0).getUuid());
				long inviteUuid = userDataList.get(0).getUuid();
				frontUser.setInviter(inviteUuid);
			} else {
				logger.info("根据邀请码[" + inviteByCode + "]找不到对应的推广ID.");
			}
		}
		frontUser.setCreateTime(new Date());
		if(frontUser.getUserPassword().length() != 64){//防止二次加密
			//将密码改为加密形式
			//String plainPassword = frontUser.getUserPassword();
			frontUser.setUserPassword(Crypt.passwordEncode(frontUser.getUserPassword()));
			//logger.info("新增用户密码是:" + plainPassword + "/" + frontUser.getUserPassword());
		}
		for(int i = 0; i < dataWriteMaxWaitingCount; i++){
			int waitingCount = applicationContextService.getDatabaseWaiting(DataSource.user.toString());
			int activeCount = applicationContextService.getDatabaseActive(DataSource.user.toString());
			if(logger.isDebugEnabled()){
				logger.debug("在写入数据前检查数据库状态[等待数:" + waitingCount + ",系统禁止写入的最大等待数:" + dataWriteIdleForWaitingCount + ",活动连接:" + activeCount + ",系统禁止写入的最大活动数:" + dataWriteIdelForActiveCount + ",当前等待次数:" + (i+1) + ",系统定义的最大等待次数:" + dataWriteMaxWaitingCount);
			}
			if(waitingCount <= dataWriteIdleForWaitingCount){
				if(activeCount <= dataWriteIdelForActiveCount) {
					break;
				}
			}
			try {
				Thread.sleep(dataWriteIdelIntervalMs + (i*1000));
			} catch (InterruptedException e) {}	

		}
		if(frontUser.getUuid() < 1){
			//获取本地UUID
			long minId = getStrippedMaxId() + 1;
			long uuid = uuidService.createById(NumericUtils.parseLong(ObjectUid.FRONT_USER.id + "" + configService.getServerId()), minId, -1);
			
			long newUuid = UserUtils.formatFrontUuid(configService.getServerId(), uuid);
			logger.debug("为新增用户:{},设置UUID[本地最小UID={},生成后的本地UID={},最终的新UUID={}]",frontUser.getUsername(), minId, uuid, newUuid);
			frontUser.setUuid(newUuid);
			
		}


		int rs = 0;
		try{
			rs = frontUserDao.insert(frontUser);
		}catch(Exception e){
			logger.error("无法创建新用户:" + e.getMessage());
			return 0;
		}
		if(rs != 1){
			logger.error("无法创建新用户,数据操作未返回1[" + rs + "]");
		}
		long mainInsertTime = (new Date().getTime() - startTime.getTime());

		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(frontUser.getUuid());
		if(frontUser.getUserConfigMap() != null){
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setObjectType(ObjectType.user.name());
			dataDefineCriteria.setObjectId(UserTypes.frontUser.getId());
			DataDefine dd = null;
			for(String key: frontUser.getUserConfigMap().keySet()){
				dataDefineCriteria.setDataCode(key);
				dd = dataDefineService.select(dataDefineCriteria);
				if(dd == null){									
						logger.error("找不到[" + key + "]的数据定义，无法完成用户扩展数据插入:" + key + "=" +frontUser.getUserConfigMap().get(key).getDataValue() );
						continue;
				}
				UserData userData = new UserData();
				/*if(frontUser.getUserConfigMap().get(key) != null && frontUser.getUserConfigMap().get("key").getUserDataId() > 0){
					userData.setUserDataId(frontUser.getUserConfigMap().get("key").getUserDataId());
				}*/
				if(frontUser.getUserConfigMap().get(key).getUserDataId() > 0){
					userData.setUserDataId(frontUser.getUserConfigMap().get(key).getUserDataId());
				}
				userData.setUuid(frontUser.getUuid());
				userData.setDataDefineId(dd.getDataDefineId());
				userData.setDataCode(key);
				userData.setDataValue(frontUser.getUserConfigMap().get(key).getDataValue());
				//userData.setCurrentStatus(dd.getCurrentStatus());
				userDataService.insert(userData);
			}

			dataDefineCriteria = null;
			dd = null;
		}
		userDataCriteria = null;		
		long totalTime = (new Date().getTime() -  startTime.getTime() - mainInsertTime);

		logger.debug("成功创建新用户[" + frontUser.getUuid() + "]，耗时" + mainInsertTime + "毫秒，创建扩展数据耗时:" + totalTime + "毫秒");

		return rs;
	}



	private long getStrippedMaxId() {
		long id = frontUserDao.getMaxId();
		return id;
	}







	@SuppressWarnings("unused")
	private void sendWelcomeMessage(User frontUser){
		//向用户发送欢迎信息
		if(logger.isDebugEnabled()){
			logger.debug("尝试向新用户[" + frontUser.getUuid() + "]发送欢迎信息");
		}
		if(logger.isDebugEnabled()){
			logger.debug("是否向新注册用户[" + frontUser.getUuid() + "]发送站短:" + sendWelcomeMessage);
		}
		if(sendWelcomeMessage){
			if(!StringUtils.isBlank(welcomeMessage)){
				String content = welcomeMessage;
				content = content.replaceAll("\\$\\{systemName\\}", systemName);
				content = content.replaceAll("\\$\\{username\\}", ( StringUtils.isBlank(frontUser.getNickName()) ? frontUser.getUsername() : frontUser.getNickName() ));
				UserMessage siteMsg = new UserMessage();
				siteMsg.setSenderName(systemName);
				siteMsg.setContent(content);
				siteMsg.setReceiverId(frontUser.getUuid());
				siteMsg.setReceiverName( ( StringUtils.isBlank(frontUser.getNickName()) ? frontUser.getUsername() : frontUser.getNickName() ) );
				siteMsg.setSenderStatus(MessageStatus.queue.id);
				siteMsg.setPerferMethod(UserMessageSendMethod.site.toString());
				if(logger.isDebugEnabled()){
					logger.debug("站短发送结果:" + userMessageService.send(siteMsg));
				}
			}	else {
				if(logger.isWarnEnabled()){
					logger.warn("向新注册用户发送站短的内容为空，停止发送");
				}
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("是否向新注册用户发送邮件:" + sendWelcomeEmail);
		}
		if(sendWelcomeEmail){
			if(!StringUtils.isBlank(welcomeEmailTemplate)){
				String emailAddress = null;
				try{
					emailAddress = frontUser.getUserConfigMap().get(DataName.userBindMailBox.toString()).getDataValue();
				}catch(Exception e){}
				if(StringUtils.isBlank(emailAddress)){
					if(frontUser.getUsername().matches(CommonStandard.emailPattern)){
						emailAddress = frontUser.getUsername();
					}
				}
				if(!StringUtils.isBlank(emailAddress)){
					//发送欢迎邮件
					String content = welcomeEmailTemplate;
					content = content.replaceAll("\\$\\{systemName\\}", systemName);
					content = content.replaceAll("\\$\\{username\\}", ( StringUtils.isBlank(frontUser.getNickName()) ? frontUser.getUsername() : frontUser.getNickName() ));
					UserMessage siteMsg = new UserMessage();
					siteMsg.setReceiverId(frontUser.getUuid());
					siteMsg.setReceiverName( emailAddress );
					siteMsg.setContent(content);
					siteMsg.setSenderStatus(MessageStatus.queue.id);
					siteMsg.setPerferMethod(UserMessageSendMethod.email.toString());
					if(logger.isDebugEnabled()){
						logger.debug("邮件发送结果:" + userMessageService.send(siteMsg));
					}
				} else {
					if(logger.isWarnEnabled()){
						logger.warn("无法解析用户邮箱[" + frontUser.getUuid());
					}
				}

			} else {
				if(logger.isWarnEnabled()){
					logger.warn("向新注册用户发送邮件模版为空，停止发送");
				}
			}

		}
		if(logger.isDebugEnabled()){
			logger.debug("是否向新注册用户发送短信:" + sendWelcomeSms);
		}
		if(sendWelcomeSms){
			if(!StringUtils.isBlank(welcomeSms)){
				String phone = null;
				String userPhoneBindSign = null;
				try{
					phone = frontUser.getUserConfigMap().get(DataName.userBindPhoneNumber.toString()).getDataValue();
				}catch(Exception e){}
				try{
					userPhoneBindSign = frontUser.getUserConfigMap().get(DataName.userPhoneBindSign.toString()).getDataValue();
				}catch(Exception e){}
				if(StringUtils.isBlank(userPhoneBindSign)){
					logger.warn("找不到用户的手机绑定验证码[" + frontUser.getUuid() + "]");
				} 
				if(StringUtils.isBlank(phone)){
					if(frontUser.getUsername().matches("^\\d{11}$")){
						phone = frontUser.getUsername();
					}
				}
				if(!StringUtils.isBlank(phone)){
					//发送欢迎短信
					String content = welcomeSms;
					content = content.replaceAll("\\$\\{systemName\\}", systemName);
					content = content.replaceAll("\\$\\{siteDomain\\}", siteUrl);
					if(!StringUtils.isBlank(userPhoneBindSign)){
						content = content.replaceAll("\\$\\{userPhoneBindSign\\}", userPhoneBindSign);
					}
					UserMessage siteMsg = new UserMessage();
					siteMsg.setReceiverId(frontUser.getUuid());
					siteMsg.setReceiverName( phone );
					siteMsg.setContent(content);
					siteMsg.setSenderStatus(MessageStatus.queue.id);
					siteMsg.setPerferMethod(UserMessageSendMethod.sms.toString());
					if(logger.isDebugEnabled()){
						logger.debug("短信发送结果:" + userMessageService.send(siteMsg));
					}
				} else {
					if(logger.isWarnEnabled()){
						logger.warn("无法解析用户手机号[" + frontUser.getUuid() + "]");
					}
				}


			}  else {
				if(logger.isWarnEnabled()){
					logger.warn("向新注册用户发送短信模版为空，停止发送");
				}
			}

		}
	}

	@Override
	@Transactional
	public int deleteLocalByUuid(long uuid) {

		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(uuid);
		userDataService.delete(userDataCriteria);	
		userDataCriteria = null;

		frontUserRoleRelationService.deleteByFuuid(uuid);

		int rs = 0;
		try{
			rs = frontUserDao.delete(uuid);
		}catch(Exception e){
			logger.error("删除用户失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("删除用户失败，删除操作返回[" + rs + "]");
			return rs;

		}

		//userConfigService.deleteByUser(fuuid);
		//messageService.sendMessage(Constants.operateDel, _oldFrontUser);
		return 1;
	}

	@Override
	@Transactional
	public int deleteLocalByName(UserCriteria userCriteria) {
		Assert.notNull(userCriteria,"尝试本地删除的userCriteria不能为空");
		Assert.isTrue(userCriteria.getOwnerId() > 0,"尝试本地删除的frontUser其ownerId不能为0");
		Assert.notNull(userCriteria.getUsername(),"尝试本地删除的frontUser用户名不能为空");

		UserCriteria frontUserCriteria = new UserCriteria(userCriteria.getOwnerId());
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		frontUserCriteria.setUsername(userCriteria.getUsername());
		List<User> userList = frontUserDao.list(frontUserCriteria);
		if(userList == null || userList.size() < 1){
			logger.info("根据用户名[" + userCriteria.getUsername() + "],ownerId=" + userCriteria.getOwnerId() + "找不到任何用户");
			return 1;
		}
		for(User user : userList){
			deleteLocalByUuid(user.getUuid());
		}
		return 1;
	}






	//更新本地数据库
	@Override
	public int updateLocal(User frontUser) throws Exception{	
		//FIXME
		/*if(frontUser.getEmail() == null || frontUser.getEmail().equals("")){
			frontUser.setEmail(frontUser.getUsername());
		}*/
		//  logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!,"+frontUser.getUuid()+",!!!!"+ frontUser.getUserConfigMap().get("userBindMailBox").getDataValue()+"!!!!");		
		frontUser.setUserTypeId(UserTypes.frontUser.getId());

		long uuid = frontUser.getUuid();	

		User _oldFrontUser = frontUserDao.select(uuid);

		if (_oldFrontUser != null) {
			if(frontUser.getUserPassword() == null || frontUser.getUserPassword().equals("")){
				//未修改密码
				frontUser.setUserPassword(_oldFrontUser.getUserPassword());
			} else {
				//与原密码不一致
				if(!frontUser.getUserPassword().equals(_oldFrontUser.getUserPassword())){
					if(frontUser.getUserPassword().length() != 64){//防止二次加密
						//将密码改为加密形式
						//String plainPassword = frontUser.getUserPassword();
						frontUser.setUserPassword(Crypt.passwordEncode(frontUser.getUserPassword()));
					}
				}
			}
			//logger.info("更新用户，密码是:" + frontUser.getUserPassword());
			try{
				frontUserDao.update(frontUser);
			}catch(Exception e){
				logger.error("更新用户失败:" + e.getMessage());
				if(e.getMessage() != null && e.getMessage().indexOf("Duplicate entry") > 0){
					return -EisError.dataDuplicate.id;
				}
				return -1;
			} 

		} else {
			return 0;
		}

		//更新用户角色
		/*frontUserRoleRelationService.deleteByFuuid(fuuid);
		if(frontUser.getRelatedRoleList() != null){
			List<Role> relatedFrontRoleList = frontUser.getRelatedRoleList(); 
			for(int i = 0; i < relatedFrontRoleList.size(); i++){
				UserRoleRelation userRoleRelation = new  UserRoleRelation();
				userRoleRelation.setCurrentStatus(Constants.BasicStatus.normal.getId());
				userRoleRelation.setUuid(fuuid);
				userRoleRelation.setRoleId(relatedFrontRoleList.get(i).getRoleId());
				frontUserRoleRelationService.insert(userRoleRelation);
			}
		} else {
			logger.warn("修改的前台用户未包含对应角色" + fuuid);
		}*/

		//如果当前是用户更新模式，且当前不存在任何普通配置，将不进行任何更新以防止误删除
		/*if(frontUser.getUpdateMode().equals(Constants.InputLevel.user.toString())){
			if(frontUser.getUserConfigMap() != null && frontUser.getUserConfigMap().size() > 0){
				int normalConfigCount = 0;
				for(String key : frontUser.getUserConfigMap().keySet()){
					if(frontUser.getUserConfigMap().get(key).getCurrentStatus() == Constants.BasicStatus.normal.getId()){
						normalConfigCount++;
					}
				}
				//只有当提交配置中有至少一个普通配置时才进行更新
				if(normalConfigCount > 0){
					userDataService.processUserConfig(frontUser);
				}
			} 
		} else {
			userDataService.processUserConfig(frontUser);
		}*/
		userDataService.processUserConfig(frontUser);

		//sendMessage(Constants.operateEdit, frontUser);
		return 1;
	}


	@Override
	@Cacheable(value=cacheName, key = "'FrontUser#' + #uuid")
	public User select(long uuid) {
		if(logger.isInfoEnabled()){
			logger.info("从数据库选择用户[" + uuid + "]");
		}
		User frontUser =  frontUserDao.select(uuid);
		if(frontUser == null){
			logger.debug("找不到用户[" + uuid + "]");	
			return null;
		} 

		afterFetch(frontUser);

		return frontUser;	
	}
	private void afterFetch(User frontUser){
		if(frontUser == null){
			return;
		}
		//frontUser.setId(frontUser.getUuid());
		if(frontUser.getNickName() == null || frontUser.getNickName().equals("")){
			frontUser.setNickName(frontUser.getUsername());
		}
		
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(frontUser.getUuid());
		userDataCriteria.setUserTypeId(UserTypes.frontUser.getId());
		frontUser.setUserConfigMap(userDataService.map(userDataCriteria));
		
		/*String headPic = frontUser.getExtraValue(DataName.userHeadPic.toString());
		if(headPic != null){
			if(headPic.startsWith("/") || headPic.indexOf("://") > 0){
				;
			} else {
				String userUploadDir = configService.getValue(DataName.userHeadPic.toString(), frontUser.getOwnerId());
				if(userUploadDir != null){
					headPic = userUploadDir.replaceAll(File.pathSeparator, "") + File.pathSeparator + headPic;
					frontUser.getUserConfigMap().get(DataName.userHeadPic.toString()).setDataValue(headPic);
				}
			}
		}*/
		//frontUser.setUserLevelProject(userLevelProjectService.selectByLevel(frontUser.getLevel()));
		//userDynamicDataService.applyToUser(frontUser);

	}







	@Override
	public void changeUuid(User frontUser) {
		if(handlerUserDataUpdate || handlerJmsDataSyncToLocal){
			logger.info("尝试强制修改用户[" + frontUser.getUsername() + "]的UUID为[" + frontUser.getUuid());
			frontUserDao.changeUuid(frontUser);
		}

	}

}


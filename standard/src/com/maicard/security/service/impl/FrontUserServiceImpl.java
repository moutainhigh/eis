package com.maicard.security.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.util.Crypt;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.domain.Money;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.criteria.UserLevelConditionCriteria;
import com.maicard.security.dao.FrontUserDao;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.domain.UserLevelCondition;
import com.maicard.security.service.FrontUserExtraService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserDataService;
import com.maicard.security.service.UserLevelConditionService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserLevelPromotion;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

@Service
public class FrontUserServiceImpl extends BaseService implements FrontUserService {

	@Resource
	private FrontUserDao frontUserDao;	

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserExtraService frontUserExtraService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private MessageService messageService;
	@Resource
	private UserDataService userDataService;
	@Resource
	private UserLevelConditionService userLevelConditionService;




	private int baseTotalUser;
	private int baseOnlineUser;

	private final String cacheName = CommonStandard.cacheNameUser;


	@PostConstruct
	public void init(){
		baseTotalUser = configService.getIntValue(DataName.baseTotalUser.toString(),0);
		if(baseTotalUser == 0){
			baseTotalUser = 5800000;
		}
		baseOnlineUser = configService.getIntValue(DataName.baseOnlineUser.toString(),0);
		if(baseOnlineUser == 0){
			baseOnlineUser = 1200;
		}
	}



	@Override
	public int insert(User frontUser) throws Exception {
		if(frontUser == null){
			return -1;
		}
		if(globalUniqueService.exist(new GlobalUnique(frontUser.getUsername(),frontUser.getOwnerId()))){
			logger.error("无法创建前台用户，因为全局数据不唯一[" + frontUser.getUsername() + "]");
			return EisError.dataUniqueConflict.id;
		}
		if(!globalUniqueService.create(new GlobalUnique(frontUser.getUsername(),frontUser.getOwnerId()))){
			logger.error("无法创建前台用户，因为无法创建全局唯一数据[" + frontUser.getUsername() + "]");
			return EisError.dataUniqueConflict.id;
		}
		int rs = frontUserExtraService.insertLocal(frontUser);
		logger.debug("创建新用户结果:" + rs);
		return rs;

	}


	public List<User> list(UserCriteria frontUserCriteria) {
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		List<Long> pkList = frontUserDao.listPk(frontUserCriteria);
		if(pkList == null || pkList.size() < 1){
			return Collections.emptyList();
		}
		List<User> frontUserList = new ArrayList<User>();
		logger.debug("得到[" + (pkList == null ? -1 : pkList.size()) + "]个前台用户");

		for(int i = 0; i < pkList.size(); i++){
			User user = frontUserExtraService.select(pkList.get(i));
			if(user != null){
				user.setIndex(i+1);
				frontUserList.add(user);

			} else {
				logger.error("根据主键[" + pkList.get(i) + "]未找到用户");
			}
		}
		return frontUserList;
	}

	public List<User> listByPrepayment(long uuid){
		return frontUserDao.listByPrepayment(uuid);
	}
	public List<User> listOnPage(UserCriteria frontUserCriteria) {
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		List<Long> pkList = frontUserDao.listPkOnPage(frontUserCriteria);
		if(pkList == null || pkList.size() < 1){
			return null;
		}
		List<User> frontUserList = new ArrayList<User>();
		logger.debug("得到[" + (pkList == null ? -1 : pkList.size()) + "]个前台用户");

		for(int i = 0; i < pkList.size(); i++){
			User user = frontUserExtraService.select(pkList.get(i));
			if(user != null){
				user.setIndex(i+1);
				frontUserList.add(user);

			} else {
				logger.error("根据主键[" + pkList.get(i) + "]未找到用户");
			}
		}
		return frontUserList;
	}

	public int count(UserCriteria frontUserCriteria){
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		return frontUserDao.count(frontUserCriteria);
	}

	public 	User login(UserCriteria frontUserCriteria){
		if(frontUserCriteria.getUsername() == null || frontUserCriteria.getUsername().equals("")){
			return null;
		}
		if(frontUserCriteria.getUserPassword() == null || frontUserCriteria.getUserPassword().equals("")){
			return null;
		}
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		//将密码改为加密形式
		//logger.info("加密后的密码：" + Crypt.passwordEncode(frontUserCriteria.getUserPassword()));
		frontUserCriteria.setUserPassword(Crypt.passwordEncode(frontUserCriteria.getUserPassword()));
		//强制设置为返回正常状态的用户
		frontUserCriteria.setCurrentStatus(UserStatus.normal.getId());
		List<User> frontUserList = list(frontUserCriteria);
		if(frontUserList == null){
			return null;
		}
		if(frontUserList.size() != 1){
			return null;
		}
		if(logger.isDebugEnabled()){
			logger.debug("前台用户[" + frontUserList.get(0).getUuid() + "/" + frontUserList.get(0).getUsername() + "]成功登录");
		}
		return frontUserList.get(0);

	}





	@Override
	@CacheEvict(value=cacheName, key = "'FrontUser#' + #frontUser.uuid")
	public EisMessage update(User frontUser) throws Exception {
		if(frontUser == null){
			throw new RequiredObjectIsNullException("更新的前台用户是空");
		}
		if(frontUser.getUuid() < 1){
			throw new RequiredAttributeIsNullException("更新的前台用户UUID异常");
		}
		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		int rs = frontUserExtraService.updateLocal(frontUser);
		if(rs  == 1){
			return new EisMessage(OperateResult.success.getId(), "更新成功");
		}
		return new EisMessage(rs, "更新用户[" + frontUser.getUuid() + "]失败");


	}
	
	@Override
	@Async
	@CacheEvict(value=cacheName, key = "'FrontUser#' + #frontUser.uuid")
	public void updateAsync(User frontUser){
		try {
			this.update(frontUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@CacheEvict(value=cacheName, key = "'FrontUser#' + #frontUser.uuid")
	public int updateNoNull(User frontUser) {
		if(frontUser == null){
			throw new RequiredObjectIsNullException("更新的前台用户是空");
		}
		if(frontUser.getUuid() < 1){
			throw new RequiredAttributeIsNullException("更新的前台用户UUID异常");
		}
		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		int rs = frontUserDao.updateNoNull(frontUser);
		//XXX 不可设置syncFlag=0并调用同步请求，会造成消息死循环,NetSnake,2016-09-18
		/*if(rs == 1){
			frontUser.setSyncFlag(0);
			messageService.sendJmsDataSyncMessage(null, "frontUserService", "updateNoNull", frontUser);
		}*/
		return rs;


	}





	@Override
	@CacheEvict(value=cacheName, key = "'FrontUser#' + #frontUser.uuid")
	public EisMessage delete(User frontUser) {
		if(frontUser == null){
			return new EisMessage(OperateResult.failed.getId(), "前台对象为空");
		}
		String username = null;
		if(frontUser.getUuid() > 0 && StringUtils.isBlank(frontUser.getUsername())){
			User _oldUser = frontUserDao.select(frontUser.getUuid());
			if(_oldUser == null){
				logger.error("根据UUID[" + frontUser.getUuid() + "]找不到前端用户");
				return new EisMessage(OperateResult.failed.getId(), "更新用户[" + frontUser.getUuid() + "]失败");
			}
			username = _oldUser.getUsername();
		} else {
			username = frontUser.getUsername();
		}
		globalUniqueService.delete(new GlobalUnique(username, frontUser.getOwnerId()));

		int rs = 0;
		if(username != null){
			UserCriteria userCriteria = new UserCriteria(frontUser.getOwnerId());
			userCriteria.setUsername(username);
			rs = frontUserExtraService.deleteLocalByName(userCriteria);
		} else if(frontUser.getUuid() > 0){
			rs = frontUserExtraService.deleteLocalByUuid(frontUser.getUuid());
		} else {
			return new EisMessage(OperateResult.failed.getId(), "无法更新用户，既没有用户名也没有UUID");
		}
		if(rs == 1){
			return new EisMessage(OperateResult.success.getId(),"成功删除前台用户[" + frontUser.getUuid() + "]");
		}

		return new EisMessage(OperateResult.failed.getId(), "更新用户[" + frontUser.getUuid() + "]失败");

	}

	/*//对于收到的用户数据处理消息进行处理
	@Override
	public void onMessage(EisMessage eisMessage) {
		if(!handlerUserDataUpdate){
			if(logger.isDebugEnabled()){
				logger.debug("本节点不负责处理用户数据更新，忽略消息[" + eisMessage.getMessageId() + "].");
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
				|| eisMessage.getOperateCode() == Operate.update.getId()
				|| eisMessage.getOperateCode() == Operate.updateLogin.getId()
				|| eisMessage.getOperateCode() == Operate.findByUuid.getId()
				|| eisMessage.getOperateCode() == Operate.findByUsername.getId()

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
		long start = new Date().getTime();
		EisMessage replyMessage = new EisMessage();
		replyMessage.setAttachment(new HashMap<String,Object>());
		User frontUser = null;
		Object object = eisMessage.getAttachment().get("frontUser");
		if(object instanceof NotifyLog){
			frontUser = (User)object;
		} else if(object instanceof LinkedHashMap){
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
			String textData = null;
			try{
				textData = om.writeValueAsString(object);
				frontUser = om.readValue(textData, User.class);
			}catch(Exception e){}

		}
		if(frontUser == null){
			replyMessage.setOperateCode(OperateResult.failed.getId());
			if(eisMessage.isNeedReply()){
				messageService.reply(replyQueueName, eisMessage.getMessageId(), eisMessage.getReplyMessageId(),replyMessage);
			}
			eisMessage = null;
			return;
		}

		if(eisMessage.getOperateCode() == Operate.create.getId()){
			if(frontUserExtraService.insertLocal(frontUser) == 1){
				frontUserExtraService.afterInsert(frontUser);
				replyMessage.setOperateCode(OperateResult.success.getId());
				replyMessage.setMessage("用户注册成功");
			} else {
				replyMessage.setOperateCode(OperateResult.failed.getId());
				replyMessage.setMessage("用户注册失败");

			}
			replyMessage.getAttachment().put("frontUser", frontUser);
			//logger.debug("回复消息中放入新增用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]");	
			long middle = new Date().getTime() - start;
			if(eisMessage.isNeedReply()){
				messageService.reply(replyQueueName, eisMessage.getMessageId(), eisMessage.getReplyMessageId(),replyMessage);
			}
			logger.debug("创建用户耗时:" + middle + "毫秒,+发送消息耗时" + (new Date().getTime() - start) + "毫秒");
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == CommonStandard.Operate.update.getId()){
			if(frontUserExtraService.updateLocal(frontUser) == 1){
				replyMessage.setOperateCode(OperateResult.success.getId());
				replyMessage.setMessage("用户更新成功");
			} else {
				replyMessage.setOperateCode(OperateResult.failed.getId());
				replyMessage.setMessage("用户更新失败");
			}
			replyMessage.getAttachment().put("frontUser", frontUser);
			if(eisMessage.isNeedReply()){
				messageService.reply(replyQueueName, eisMessage.getMessageId(), eisMessage.getReplyMessageId(),replyMessage);	
			}
			eisMessage = null;
			return;
		}

		if(eisMessage.getOperateCode() == CommonStandard.Operate.delete.getId()){
			if(frontUserExtraService.deleteLocal(frontUser.getUuid()) == 1){
				replyMessage.setOperateCode(OperateResult.success.getId());
				replyMessage.setMessage("用户删除成功");
			} else {
				replyMessage.setOperateCode(OperateResult.failed.getId());
				replyMessage.setMessage("用户删除失败");
			}
			replyMessage.getAttachment().put("frontUser", frontUser);
			if(eisMessage.isNeedReply()){
				messageService.reply(replyQueueName, eisMessage.getMessageId(), eisMessage.getReplyMessageId(),replyMessage);	
			}
			eisMessage = null;
			return;
		} 
		if(eisMessage.getOperateCode() == Operate.findByUuid.getId()){
			int systemServerId = configService.getServerId();
			if(String.valueOf(frontUser.getUuid()).startsWith(String.valueOf(systemServerId))){
				logger.info("需要被查找的前端用户[" + frontUser.getUuid() + "]与本服务器ID[" + systemServerId + "]一致，尝试查找并重新创建该用户");
				User reCreateUser = select(frontUser.getUuid());
				if(reCreateUser == null){
					logger.error("找不到正在查找的用户[" + frontUser.getUuid());
				} else {
					logger.info("找到了丢失的前端用户[" + reCreateUser.getUuid() + "/" + reCreateUser.getUsername() + "]，发送消息以重新创建该用户");
					messageService.sendJmsDataSyncMessage(null, "frontUserExtraService", "syncFromJms", reCreateUser);
				}
			} else {
				logger.info("需要被查找的前端用户[" + frontUser.getUuid() + "]与本服务器ID[" + systemServerId + "]不一致，忽略该查找请求");

			}
			return;
		} 

		if(eisMessage.getOperateCode() == Operate.findByUsername.getId()){
			UserCriteria userCriteria = new UserCriteria();
			userCriteria.setUsername(frontUser.getUsername());
			int count =  count(userCriteria);
			if(count == 0){
				logger.info("系统中没找到名叫[" + frontUser.getUsername() + "]的用户");
			} else {
				logger.info("系统中找到了名叫[" + frontUser.getUsername() + "]的用户，尝试将其UUID改为:" + frontUser.getUuid());
				frontUserExtraService.changeUuid(frontUser);
			}

			return;
		} 

		logger.debug("忽略操作[" + eisMessage.getOperateCode() );

		eisMessage = null;


	}

	 */


	@Override
	public void correctUserConfig(User frontUser){
		if(frontUser == null){
			throw new RequiredObjectIsNullException("尝试检查的用户对象为空");
		}
		if(frontUser.getUuid() < 1){
			throw new RequiredAttributeIsNullException("尝试检查的用户对象UUID为空");
		}
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(frontUser.getUuid());
		userDataCriteria.setUserTypeId(UserTypes.frontUser.getId());
		HashMap<String, UserData> _oldUserDataMap = null;
		try {
			_oldUserDataMap = userDataService.map(userDataCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//与旧的数据进行比较
		logger.debug("旧配置数据有:" + (_oldUserDataMap == null ? -1 : _oldUserDataMap.size()) + "条");
		if(_oldUserDataMap == null || frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String, UserData>());
		}
		for(UserData _oldUserData : _oldUserDataMap.values()){
			if(_oldUserData.getInputLevel() != null && !_oldUserData.getInputLevel().equals(InputLevel.user.getCode())){
				//不是用户可编辑属性
				logger.debug("恢复非用户编辑数据[" + _oldUserData.getDataCode() + "].");
				frontUser.getUserConfigMap().put(_oldUserData.getDataCode(), _oldUserData);
			}
		}

	}

	@Override
	public void correctUserAttributes(User frontUser){
		if(frontUser == null){
			throw new RequiredObjectIsNullException("尝试检查的用户对象为空");
		}
		if(frontUser.getUuid() < 1){
			throw new RequiredAttributeIsNullException("尝试检查的用户对象UUID为空");

		}
		User _oldUser = frontUserDao.select(frontUser.getUuid());
		//将不允许自行修改的项目设置为旧的
		frontUser.setOwnerId(_oldUser.getOwnerId());
		frontUser.setUsername(_oldUser.getUsername());
		//frontUser.setAuthType(_oldUser.getAuthType());
		frontUser.setCreateTime(_oldUser.getCreateTime());
		frontUser.setInviter(_oldUser.getInviter());
		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		/*if(_oldUser.getFromUser() != null){
			frontUser.setFromUser(_oldUser.getFromUser());
		} else {
			frontUser.setFromUser(null);
		}*/
		frontUser.setLevel(_oldUser.getLevel());
		frontUser.setCurrentStatus(_oldUser.getCurrentStatus());
	}

	@Override
	public void processBindEmailData(User frontUser, String bindMailBox) throws Exception{
		if(frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String,UserData>());
		}
		String mailBindSign = Crypt.passwordEncode(CommonStandard.mailBindKey + frontUser.getUuid());
		if(frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String,UserData>());
		}
		UserData bindMailConfig = new UserData();
		bindMailConfig.setDataCode(DataName.userMailBindSign.toString());
		bindMailConfig.setDataValue(mailBindSign);
		//bindMailConfig.set

		UserData bindMailAddressConfig = new UserData();
		bindMailAddressConfig.setDataCode(DataName.userBindMailBox.toString());
		bindMailAddressConfig.setDataDescription("绑定的邮箱");
		bindMailAddressConfig.setDataValue(bindMailBox);

		frontUser.getUserConfigMap().put(DataName.userMailBindSign.toString(),bindMailConfig);
		frontUser.getUserConfigMap().put(DataName.userBindMailBox.toString(),bindMailAddressConfig);
	}

	@Override
	public String processBindPhoneData(User frontUser, String phone) throws Exception{	
		if(frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String,UserData>());
		}
		String phoneBindSign = "" + (new java.util.Random().nextInt(888888) + 100000);
		UserData bindPhoneConfig = new UserData();
		bindPhoneConfig.setDataCode(DataName.userPhoneBindSign.toString());
		bindPhoneConfig.setDataValue(phoneBindSign);
		bindPhoneConfig.setUuid(frontUser.getUuid());
		UserData bindPhoneNumberConfig = new UserData();
		bindPhoneNumberConfig.setDataCode(DataName.userBindPhoneNumber.toString());
		bindPhoneNumberConfig.setDataValue(phone);
		bindPhoneNumberConfig.setUuid(frontUser.getUuid());
		frontUser.getUserConfigMap().put(DataName.userPhoneBindSign.toString(),bindPhoneConfig);
		frontUser.getUserConfigMap().put(DataName.userBindPhoneNumber.toString(),bindPhoneNumberConfig);
		return phoneBindSign;

	}

	//检查用户是否有资金变化后的优惠
	@Override
	public void calculateUserPromotion(User user, Money money){
		logger.debug("计算用户优惠政策");
		if(user == null){
			return;
		}
		if(money == null){
			return;
		}
		if(user.getUserLevelProject() == null){
			logger.debug("用户[" + user.getUuid() + "级别/" + user.getLevel() + "]没有方案级别");
			return;
		}
		UserLevelConditionCriteria userLevelConditionCriteria = new UserLevelConditionCriteria();
		userLevelConditionCriteria.setUserLevelProjectId(user.getUserLevelProject().getUserLevelProjectId());
		userLevelConditionCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<UserLevelCondition> userLevelConditionList = userLevelConditionService.list(userLevelConditionCriteria);

		logger.debug("用户[" + user.getUuid() + "级别/" + user.getLevel() + "]的方案有[" + (userLevelConditionList == null ? -1 :userLevelConditionList.size()) + "]个条件");
		for(UserLevelCondition userLevelCondition : userLevelConditionList){
			logger.debug("比对级别方案条件:" + userLevelCondition.getUserLevelConditionName());
			//充值赠送
			if(userLevelCondition.getUserLevelConditionName().equals(UserLevelPromotion.givePointForPay.toString())){				
				try{
					int point = Math.round(money.getChargeMoney()) * Integer.parseInt(userLevelCondition.getUserLevelConditionValue());
					money.setPoint(money.getPoint() + point);
				}catch(Exception e){}
			}
			//消费赠送
			if(userLevelCondition.getUserLevelConditionName().equals(UserLevelPromotion.givePointForBuy.toString())){				
				try{
					//消费是扣除操作，因此这里用负数，当扣除时，扣除负数就是增加积分
					int point = -1 * Math.round(money.getFrozenMoney()) * Integer.parseInt(userLevelCondition.getUserLevelConditionValue());
					money.setPoint(money.getPoint() + point);
				}catch(Exception e){}
			}
		}


	}


	@Override
	public void calculateUserPromotion(long uuid, Money money) {
		if(uuid == 0){
			return;
		}
		if(money == null){
			return;
		}
		User frontUser = select(uuid);
		if(frontUser == null){
			return;
		}
		calculateUserPromotion(frontUser, money);

	}



	@Override
	public User getUnLoginedUser() {	
		return new User();
	}



	@Override
	public String getTotalUser() {
		long totalUser = baseTotalUser + count(new UserCriteria());
		DecimalFormat nf = new DecimalFormat("###,###,###,###,###");
		return nf.format(totalUser);
	}


	@Override
	public String searchInviter(long uuid){
		return frontUserDao.search_Inviter(uuid);	
	}



	@Override
	public User select(long uuid) {
		logger.debug("BINX 执行...");
		return frontUserExtraService.select(uuid);
	}

	@Override
	public User lockUser(UserCriteria userCriteria) {
		if(userCriteria == null){
			logger.error("尝试锁定外部用户的条件为空");
			return null;
		}
		if(userCriteria.getLockGlobalUniqueId() == null){
			userCriteria.setLockGlobalUniqueId(java.util.UUID.randomUUID().toString());
		}
		return frontUserDao.lockUser(userCriteria);
	}


	//重新同步未能正确在后端创建的用户
	@Override
	public void reSyncLostUser(long uuid) {
		User frontUser = new User();
		frontUser.setUuid(uuid);
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.findByUuid.getId());
		m.setObjectType(ObjectType.frontUser.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("frontUser", frontUser);	
		logger.info("将查找用户[" + uuid + "]的消息发往系统总线");


	}

	//重新同步前后端UUID不一致的用户
	@Override
	public void reSyncUuidNotMatchUser(User user) {

		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.findByUsername.getId());
		m.setObjectType(ObjectType.frontUser.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("frontUser", user);	
		logger.info("将修正用户[" + user.getUuid() + "/" + user.getUsername() + "]UUID的消息发往系统总线");

	}
	@Override
	public String downloadNewAccountCsv(int num) {
		try {
			return frontUserDao.downloadNewAccountCsv(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String downloadbalanceCsv(int num) {
		try {
			return frontUserDao.downloadBalanceCsv(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String makeCollection(String custdata){
		try{
			return frontUserDao.makeCollection(custdata);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "-1";
	}


	/**
	 * 更新用户头像文件
	 */
	@Override
	public void updateUserHeadPic(User frontUser, String newImageUrl) {
		Assert.notNull(frontUser);
		Assert.notNull(newImageUrl);
		
		

	}




}


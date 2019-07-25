package com.maicard.money.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.GiftCardCriteria;
import com.maicard.money.dao.GiftCardDao;
import com.maicard.money.domain.GiftCard;
import com.maicard.money.domain.Money;
import com.maicard.money.service.GiftCardService;
import com.maicard.money.service.MoneyService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;

import org.springframework.stereotype.Service;

@Service
@ProcessMessageObject("giftCard")
public class GiftCardServiceImpl extends BaseService implements GiftCardService,EisMessageListener {

	@Resource
	private GiftCardDao giftCardDao;
	@Resource
	private ConfigService configService;
	@Resource
	private CenterDataService centerDataService;

	@Resource
	private MessageService messageService;
	@Resource MoneyService moneyService;
	private boolean handlerGiftCardUpdate;
	private String messageBusName;

	private final int LOCK_BY_REDIS_TRY_COUNT = 3;

	@PostConstruct
	public void init(){
		handlerGiftCardUpdate = configService.getBooleanValue(DataName.handlerGiftCardUpdate.toString(),0);
		messageBusName = configService.getValue(DataName.messageBusUser.toString(),0);
	}

	@Override
	public int insert(GiftCard giftCard){
		return giftCardDao.insert(giftCard);
	}

	public int update(GiftCard giftCard) {
		try{
			return  giftCardDao.update(giftCard);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public GiftCard fetchWithLock(GiftCardCriteria giftCardCriteria) {

		for(int i = 0; i < LOCK_BY_REDIS_TRY_COUNT; i++){
			GiftCard giftCard = _lockByRedis(giftCardCriteria);
			if(giftCard != null){
				return giftCard;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		if(handlerGiftCardUpdate){
			return _lockLocal(giftCardCriteria);
		}
		return _lockRemote(giftCardCriteria);

	}
	private GiftCard _lockRemote(GiftCardCriteria giftCardCriteria){
		if(giftCardCriteria == null){
			return null;
		}
		if(StringUtils.isBlank(giftCardCriteria.getLockGlobalUniqueId())){
			giftCardCriteria.setLockGlobalUniqueId(UUID.randomUUID().toString());
		}
		giftCardCriteria.setPaging(null);
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.lock.getId());
		m.setObjectType(ObjectType.giftCard.toString());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("giftCardCriteria", giftCardCriteria);	
		m.setNeedReply(false);
		logger.info("请求远程锁定礼品卡[" + giftCardCriteria.getLockGlobalUniqueId() + "]");
		messageService.send(messageBusName, m);		
		/*ObjectMapper om = new ObjectMapper();
		try{
			logger.info("本地读取条件:" + om.writeValueAsString(giftCardCriteria));
		}catch(Exception e){}*/
		GiftCardCriteria giftCardCriteria2 = null;
		try {
			giftCardCriteria2 = giftCardCriteria.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		//等待从本地数据库中读取对应的数据
		for(int i = 0; i < giftCardCriteria2.getMaxWaiting(); i++){
			int count = giftCardDao.count(giftCardCriteria2);
			if(logger.isDebugEnabled()){
				logger.debug("尝试第" + (i+1) + "次从本地读取礼品卡[" + giftCardCriteria2.getLockGlobalUniqueId() + "]");
			}
			if(count > 0){
				if(logger.isDebugEnabled()){
					logger.debug("成功从本地读取礼品卡[" + giftCardCriteria2.getLockGlobalUniqueId() + "]");
				}
				List<GiftCard> giftCardList = list(giftCardCriteria2);
				if(giftCardList != null && giftCardList.size() > 0){
					return giftCardList.get(0);
				} else {
					return null;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		giftCardCriteria2 = null;
		return null;
	}

	private GiftCard _lockLocal(GiftCardCriteria giftCardCriteria){	
		logger.info("收到任务消息！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		String oldLockId = null;
		if(StringUtils.isNotBlank(giftCardCriteria.getLockGlobalUniqueId())){
			oldLockId = giftCardCriteria.getLockGlobalUniqueId();
			giftCardCriteria.setLockGlobalUniqueId(null);
		}
		if(!giftCardCriteria.isForceLock()){
			List<GiftCard> giftCardList = list(giftCardCriteria);
			if(giftCardList != null){

				if(giftCardList.size() > 0){//该用户对应于该业务，已存在新手卡，即已经使用过，直接返回该卡
					if(giftCardList.get(0).getCurrentStatus() == BasicStatus.relation.getId()){
						if(logger.isDebugEnabled()){
							logger.debug("该用户[" + giftCardCriteria.getUsedByUuid() + "]已领取对应服务的新手卡，返回该卡[" + giftCardList.get(0).getGiftCardId() + "]");
						}
						return giftCardList.get(0);
					}
					return null;

				}
			}
		}
		if(StringUtils.isNotBlank(oldLockId)){
			giftCardCriteria.setLockGlobalUniqueId(oldLockId);		
		} else {
			giftCardCriteria.setLockGlobalUniqueId(UUID.randomUUID().toString());
		}
		GiftCard giftCard =  giftCardDao.fetchWithLock(giftCardCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("根据锁定ID[" + giftCardCriteria.getLockGlobalUniqueId() + "]锁定的新手卡是[" + giftCard + "]");
		}
		if(giftCard != null){

			messageService.sendJmsDataSyncMessage(messageBusName, "giftCardService", "update", giftCard);
		}
		return giftCard;

	}

	private GiftCard _lockLocal1(GiftCardCriteria giftCardCriteria){	
		logger.info("收到任务消息！！！！！！！！！！！！！！！！！！！！！！！！！！！！！"+giftCardCriteria.getCardNumber());
		giftCardCriteria.setNewStatus(100003);
		GiftCard giftCard =  giftCardDao.fetchWithLock(giftCardCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("根据锁定ID[" + giftCardCriteria.getLockGlobalUniqueId() + "]锁定的新手卡是[" + giftCard + "]");
		}
		if(giftCard != null){

			messageService.sendJmsDataSyncMessage(messageBusName, "giftCardService", "update", giftCard);
		}
		return giftCard;
	}
	public GiftCard get(GiftCardCriteria giftCardCriteria) {
		//logger.info("start get.");
		if(giftCardCriteria.getObjectType() == null ){
			logger.info("未指定获取新手卡的对应业务");
			return null;
		}
		if(giftCardCriteria.getObjectIds() == null || giftCardCriteria.getObjectIds().length < 1){
			logger.info("未指定获取新手卡的对应业务服务");
			return null;
		}
		if(giftCardCriteria.getUsedByUuid() == 0){
			logger.info("未指定获取新手卡的对应用户");
			return null;
		}
		if(!giftCardCriteria.isForceLock()){
			List<GiftCard> giftCardList = list(giftCardCriteria);
			if(giftCardList != null){			
				if(giftCardList.size() > 0){//该用户对应于该业务，已存在新手卡，即已经使用过，直接返回该卡
					if(giftCardList.get(0).getCurrentStatus() == BasicStatus.relation.getId()){
						if(logger.isDebugEnabled()){
							logger.debug("该用户[" + giftCardCriteria.getUsedByUuid() + "]已领取对应服务的新手卡，返回该卡[" + giftCardList.get(0).getGiftCardId() + "]");
						}
						return giftCardList.get(0);
					}
					return null;

				}
			}
		}
		//获取新的新手卡
		logger.info("尝试为用户获取新的新手卡");
		giftCardCriteria.setCurrentStatus(BasicStatus.normal.getId());
		giftCardCriteria.setNewStatus(BasicStatus.relation.getId());
		GiftCard giftCard = fetchWithLock(giftCardCriteria);
		if(giftCard == null ){
			logger.error("找不到符合条件的新手卡.");
			return null;
		}		
		return giftCard;
	}

	public List<GiftCard> list(GiftCardCriteria giftCardCriteria) {
		return giftCardDao.list(giftCardCriteria);
	}

	public List<GiftCard> listOnPage(GiftCardCriteria giftCardCriteria) {
		return giftCardDao.listOnPage(giftCardCriteria);
	}


	@Override
	public void onMessage(EisMessage eisMessage) {
		if(!handlerGiftCardUpdate){
			logger.debug("本节点不负责处理礼品卡数据更新，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == 0){
			logger.debug("消息操作码为空，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == Operate.lock.getId()){
			try{
				operate(eisMessage);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} else {			 
			logger.debug("消息操作码非法[" + eisMessage.getOperateCode() + "]，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}
		eisMessage = null;

	}

	private void operate(EisMessage eisMessage) throws Exception{

		GiftCardCriteria giftCardCriteria = null;
		Object object = eisMessage.getAttachment().get("giftCardCriteria");
		if(object == null){
			logger.error("从附件中找不到名称为[giftCardCriteria]的对象");
			return;
		}
		if(object instanceof GiftCardCriteria){
			giftCardCriteria = (GiftCardCriteria)object;
		} else if(object instanceof LinkedHashMap){
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
			String textData = null;
			try{
				textData = om.writeValueAsString(object);
				giftCardCriteria = om.readValue(textData, GiftCardCriteria.class);
			}catch(Exception e){
				e.printStackTrace();
			}
			om = null;
			textData = null;
		}


		if(giftCardCriteria == null){
			logger.error("消息中的giftCardCriteria为空");
			return;
		}

		if(giftCardCriteria.getCardNumber()==null || giftCardCriteria.getCardNumber().equals("") && eisMessage.getOperateCode() == Operate.lock.getId()){
			_lockLocal(giftCardCriteria);			
		}
		if(giftCardCriteria.getCardNumber()!=null && !giftCardCriteria.getCardNumber().equals("") && eisMessage.getOperateCode() == Operate.lock.getId()){
			_lockLocal1(giftCardCriteria);			
		}
		giftCardCriteria = null;

	}

	@Override
	public HashMap<String,String> generate(GiftCardCriteria giftCardCriteria){
		if(giftCardCriteria == null){
			return null;
		}
		if(StringUtils.isBlank(giftCardCriteria.getObjectType())){
			throw new RequiredObjectIsNullException("生成礼品卡的要求中没有objectType");			
		}
		if(giftCardCriteria.getCount() < 1){
			throw new RequiredObjectIsNullException("生成礼品卡的要求中没有指定数量count");		

		}
		HashMap<String,String> cards = new HashMap<String,String>();
		int status = 0;
		if(giftCardCriteria.getCurrentStatus() == null || giftCardCriteria.getCurrentStatus().length < 1){
			status = BasicStatus.normal.getId();
		} else {
			status = giftCardCriteria.getCurrentStatus()[0];
		}
		for(int i = 0; i < giftCardCriteria.getCount(); i++){
			GiftCard giftCard = new GiftCard();
			giftCard.setObjectType(giftCardCriteria.getObjectType());
			giftCard.setObjectId(giftCardCriteria.getObjectIds()[0]);
			giftCard.setObjectExtraId(giftCardCriteria.getObjectExtraId());
			giftCard.setCurrentStatus(status);
			giftCard.setLabelMoney(giftCardCriteria.getLabelMoney());
			giftCard.setRequestMoney(giftCardCriteria.getRequestMoney());
			giftCard.setMoneyTypeId(giftCardCriteria.getMoneyTypeId());
			giftCard.setCardNumber("S" + UUID.randomUUID().toString().replaceAll("-", "").substring(1, 19));
			giftCard.setCardPassword("P" + UUID.randomUUID().toString().replaceAll("-", "").substring(1, 19));
			if(insert(giftCard) == 1){
				cards.put(giftCard.getCardNumber(), giftCard.getCardPassword());
			}
		}
		return cards;

	}

	@Override
	public GiftCard select(int giftCardId) {
		return giftCardDao.select(giftCardId);
	}
	@Override
	public GiftCard check(GiftCardCriteria giftCardCriteria){
		List<GiftCard> l=giftCardDao.list(giftCardCriteria);
		if (l!=null){
			if (l.size()>0){
				return l.get(0);
			}
		}
		return null;
	}
	@Override
	public float charge(Long uuid,String cardNumber){
		GiftCard result=null;
		float labelMoney=0F;
		int currentStatus;
		GiftCardCriteria giftCardCriteria=new GiftCardCriteria();
		giftCardCriteria.setCardNumber(cardNumber);
		result=check(giftCardCriteria);
		if (result!=null){
			labelMoney=result.getLabelMoney();
			currentStatus=result.getCurrentStatus();
			logger.info("卡状态是"+currentStatus);
			if  (currentStatus==100001) {
				giftCardCriteria.setCardNumber(cardNumber);
				giftCardCriteria.setUsedByUuid(uuid);
				giftCardCriteria.setCurrentStatus(new int[]{100003});
				result=fetchWithLock(giftCardCriteria);
				if (result!=null){
					Money money=new Money();
					money.setGiftMoney(result.getLabelMoney());
					money.setUuid(uuid);
					EisMessage message=moneyService.plus(money);
					logger.info("XXXXXXXXXXXXXXXXXX"+message.getOperateCode()+"!!!!!!!!!!!!!!!!!!!!!!");
					if (message.getOperateCode()==OperateResult.accept.getId()){
						logger.info("返回卡面值为"+labelMoney+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						return labelMoney;
					}
				}
			}
			else {
				return currentStatus;
			}
		}
		else{
			return -1;
		}
		return -1;
	}

	/**
	 * 直接在前端执行锁定数据库中的数据，然后在REDIS中锁定该卡
	 * 如果锁定成功才发送更新消息
	 * 
	 * @param giftCardCriteria
	 * @return
	 */
	private GiftCard _lockByRedis(GiftCardCriteria giftCardCriteria){	

		giftCardCriteria.setLockGlobalUniqueId(UUID.randomUUID().toString());

		GiftCard giftCard =  giftCardDao.fetchWithLock(giftCardCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("根据锁定ID[" + giftCardCriteria.getLockGlobalUniqueId() + "]锁定的新手卡是[" + giftCard + "]");
		}
		if(giftCard == null){
			return null;
		}
		final String key = KeyConstants.GIFTCARD_LOCK_PREFIX + "#" + giftCard.getGiftCardId();
		giftCard.setVersion(giftCard.getVersion() + 1);
		boolean lockSuccess = centerDataService.setIfNotExist(key, String.valueOf(giftCard.getVersion()), CommonStandard.DISTRIBUTED_DEFAULT_LOCK_SEC);

		if(lockSuccess){
			messageService.sendJmsDataSyncMessage(messageBusName, "giftCardService", "update", giftCard);
		}
		return null;

	}
}

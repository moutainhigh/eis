package com.maicard.money.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.JsonUtils;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.MoneyCriteria;
import com.maicard.money.dao.MoneyDao;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.MessageStandard.MessageLevel;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@ProcessMessageObject("money")
public class MoneyServiceImpl extends BaseService implements MoneyService,EisMessageListener {

	@Resource
	private MoneyDao moneyDao;

	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;
	@Resource
	private CenterDataService centerDataService;

	private boolean handlerMoney;
	private String messageBusName;

	private final int LOCK_MAX_RETRY = 5;
	private final int LOCK_INTERVAL = 200;	//200毫秒
	
	private final String setBalance = "D";


	@PostConstruct
	public void init(){
		handlerMoney = configService.getBooleanValue(DataName.handlerMoney.toString(),0);
		messageBusName = configService.getValue(DataName.messageBusSystem.toString(),0);
	}

	public EisMessage insert(Money money) {
		if(money == null){
			return new EisMessage(OperateResult.failed.getId(), "无法插入新的资金账户,money对象为空");
		}
		if(money.getUuid() < 1){
			return new EisMessage(OperateResult.failed.getId(), "无法插入新的资金账户,money对象中的uuid是0");
		}
		if(select(money.getUuid(), money.getUuid()) != null){//已存在记录
			return new EisMessage(OperateResult.failed.getId(), "无法插入新的资金账户,账户已存在");
		}

		if(!lockForUpdate(money)){
			logger.error("无法在新增资金账户前进行锁定");
			return new EisMessage(OperateResult.failed.getId(), "无法插入新的资金账户,未知错误");
		}
		logger.debug("向数据库中插入新的资金账户:" + money);
		moneyDao.insert(money);
		if(selectLocal(money.getUuid()) != null){
			//插入资金变化记录
			unlockForUpdate(money);
			return new EisMessage(OperateResult.success.getId(), "" + money.getUuid());
		}
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();
		String value = null;
		try {
			value = JsonUtils.getNoDefaultValueInstance().writeValueAsString(money);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		boolean rs = centerDataService.setIfNotExist(key, value, -1);
		unlockForUpdate(money);
		if(!rs){
			return new EisMessage(OperateResult.failed.getId(), "无法插入新的资金账户,未知错误");
		}
		return new EisMessage(OperateResult.success.getId(), "" + money.getUuid());

	}

	private boolean lockForUpdate(Money money) {
		boolean rs = false;
		String key = KeyConstants.MONEY_LOCK_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();
		for(int i = 0; i < LOCK_MAX_RETRY; i++){
			rs = centerDataService.setIfNotExist(key, key, CommonStandard.DISTRIBUTED_DEFAULT_LOCK_SEC);
			logger.debug("第" + (i+1) + "次锁定资金数据[" + key + "]的结果:" + rs);
			if(rs){
				break;
			}
			try {
				Thread.sleep(LOCK_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.debug("最终锁定资金数据[" + key + "]的结果:" + rs);
		return rs;
	}

	private boolean unlockForUpdate(Money money) {
		String key = KeyConstants.MONEY_LOCK_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();
		centerDataService.delete(key);
		return true;

	}

	public int update(Money money) {
		int actualRowsAffected = 0;

		long uuid = money.getUuid();

		Money _oldMoney = moneyDao.select(uuid);

		if(_oldMoney == null){
			logger.error("找不到要更新的资金帐户:" + uuid);
			return -EisError.moneyAccountNotExist.id;
		}
		if(!lockForUpdate(money)){
			logger.error("无法在新增资金账户前进行锁定");
			return -EisError.distributedLockFail.id;
		}

		updateLocal(money);
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();
		String value = null;
		try {
			value = JsonUtils.getNoDefaultValueInstance().writeValueAsString(money);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		centerDataService.setForce(key, value, -1);

		unlockForUpdate(money);

		return actualRowsAffected;
	}

	@Override
	public int updateLocal(Money money){
		try{
			return moneyDao.update(money);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return 0;
	}

	public int delete(long uuid) {
		try{
			return   moneyDao.delete(uuid);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;


	}

	@Override
	public Money selectLocal(long uuid) {		
		return moneyDao.select(uuid);
	}

	public Money select(long uuid, long ownerId) {
		return select(uuid, ownerId, true);

		
	}
	@Override 
	public Money select(long uuid, long ownerId, boolean needLock) {
		Assert.isTrue(ownerId > 0,"尝试获取的资金账户ownerId为空");
		Assert.isTrue(uuid != 0,"尝试获取的资金账户UUID为空");

		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		Money money = null;
		//先从REDIS获取
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + ownerId + "#" + uuid;

		String value = centerDataService.get(key);
		if(value != null){
			try {
				money = om.readValue(value,Money.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(money == null){
			logger.warn("无法从REDIS读取Money实例:" + key + ",准备从本地读取Money数据");
			money =  moneyDao.select(uuid);
			if(money == null){
				return null;
			}
			if(needLock && !lockForUpdate(money)){
				logger.error("无法在写入资金账户到REDIS前进行锁定");
				return null;
			}
			try {
				value = om.writeValueAsString(money);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			centerDataService.setForce(key, value, -1);
			if(needLock){
				unlockForUpdate(money);
			}
		}
		return money;
	}

	/*public void chargeMoneyLocal(Money money){
		moneyDao.charge(money);
	}
	public int chargeMoney(Money money){
		try{
			chargeMoneyLocal(money);
		}
		catch(Exception e)
		{
			return 0;
		}
		try{
			messageService.sendJmsDataSyncMessage(messageBusName, "moneyService", "chargeMoneyLocal", money);
		}
		catch(Exception e)
		{
			return 0;
		}
		return 1; 
	}*/
	public List<Money> list(MoneyCriteria moneyCriteria) {
		List<Long> pkList = moneyDao.listPk(moneyCriteria);
		if(pkList == null){
			return Collections.emptyList();
		} 
		List<Money> moneyList = new ArrayList<Money>();
		for(Long uuid : pkList){
			Money money = select(uuid,moneyCriteria.getOwnerId());
			if(money != null){
				moneyList.add(money);
			}
		}
		return moneyList;
		
		
		
	}

	public List<Money> listOnPage(MoneyCriteria moneyCriteria) {
		List<Long> pkList = moneyDao.listPkOnPage(moneyCriteria);
		if(pkList == null){
			return Collections.emptyList();
		} 
		List<Money> moneyList = new ArrayList<Money>();
		for(Long uuid : pkList){
			Money money = select(uuid,moneyCriteria.getOwnerId());
			if(money != null){
				moneyList.add(money);
			}
		}
		return moneyList;
	}

	public List<Money> listByPartner() {
		return moneyDao.listByPartner();
	}

	public 	EisMessage plus(Money money){

		Assert.notNull(money,"尝试加款的资金账户为空");
		Assert.isTrue(money.getOwnerId() > 0,"尝试加款的资金账户ownerId为空");
		Assert.isTrue(money.getUuid() != 0,"尝试加款的资金账户UUID为0");

		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		//先从REDIS获取
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();

		String value = null;
		
		/*XXX 必须先加锁，再读取数据，否则在高并发情况下，可能造成两笔资金操作不一致，都是从最开始的资金开始相加 */
		if(!lockForUpdate(money)){
			logger.error("无法在写入资金账户到REDIS前进行锁定");
			return new EisMessage(EisError.distributedLockFail.getId(), "扣除账户[" + money.getUuid() + "]资金失败，无法锁定资金");
		}
		
		
		Money _oldMoney = select(money.getUuid(), money.getOwnerId(), false);
		if(_oldMoney == null){
			logger.warn("无法从数据库中读取Money实例:" + key + ",直接新增账户");
			moneyDao.insert(money);
			try {
				value = om.writeValueAsString(money);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			centerDataService.setForce(key, value, -1);
			//设置后再次获取比对
			Money freshMoney = select(money.getUuid(), money.getOwnerId(), false);
			
			unlockForUpdate(money);
		
			logger.info("执行资金账户添加及请求增加金额:" + money + ",增加资金后的当前资金:" + money + ",最新缓存资金是:" + freshMoney);
			
			moneyDao.update(money);				
			messageService.sendJmsDataSyncMessage(messageBusName, "moneyService", "updateLocal", money);
			return new EisMessage(OperateResult.success.getId(), "增加账户[" + money.getUuid() + "]资金成功");
		}

		if(setBalance != null){
			if(setBalance.equalsIgnoreCase("D")){
				this.snapBalance(_oldMoney, new SimpleDateFormat("yyyyMMdd").format(new Date()), 48 * 3600);
			}
		}
		//进行资金增加操作
		logger.debug("执行资金增加操作前，原有资金:" + _oldMoney + ",请求增加金额:" + money);
		if(money.getChargeMoney() > 0){
			_oldMoney.setChargeMoney(_oldMoney.getChargeMoney() + money.getChargeMoney());
		}
		if(money.getIncomingMoney() > 0){
			_oldMoney.setIncomingMoney(_oldMoney.getIncomingMoney() + money.getIncomingMoney());
		}
		if(money.getTransitMoney() > 0){
			_oldMoney.setTransitMoney(_oldMoney.getTransitMoney() + money.getTransitMoney());
		}
		if(money.getCoin() > 0){
			_oldMoney.setCoin(_oldMoney.getCoin() + money.getCoin());
		}
		if(money.getPoint() > 0){
			_oldMoney.setPoint(_oldMoney.getPoint() + money.getPoint());
		}
		if(money.getScore() > 0){
			_oldMoney.setScore(_oldMoney.getScore() + money.getScore());
		}
		if(money.getFrozenMoney() > 0){
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() + money.getFrozenMoney());
		}
		try {
			value = om.writeValueAsString(_oldMoney);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		centerDataService.setForce(key, value, -1);
		
		Money freshMoney = select(money.getUuid(), money.getOwnerId());
		
		unlockForUpdate(money);

		logger.info("请求增加金额:" + money + ",增加资金后的当前资金:" + _oldMoney + ",最新缓存资金是:" + freshMoney);

		moneyDao.update(_oldMoney);		
		messageService.sendJmsDataSyncMessage(null, "moneyService", "updateLocal", _oldMoney);

		BeanUtils.copyProperties(_oldMoney, money);
		return new EisMessage(OperateResult.success.getId(), "增加账户[" + money.getUuid() + "]资金成功");

	}

	@Override
	public EisMessage minus(Money money){
		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		
		/*XXX 必须先加锁，再读取数据，否则在高并发情况下，可能造成两笔资金操作不一致，都是从最开始的资金开始相加 */
		if(!lockForUpdate(money)){
			logger.error("无法在写入资金账户到REDIS前进行锁定");
			return new EisMessage(EisError.distributedLockFail.getId(), "扣除账户[" + money.getUuid() + "]资金失败，无法锁定资金");
		}
		
		Money _oldMoney = select(money.getUuid(), money.getOwnerId(), false);

		if(_oldMoney == null){
			logger.warn("无法获取用户[" + money.getUuid() + "]的资金账户，无法扣款");
			unlockForUpdate(money);
			return new EisMessage(-EisError.moneyAccountNotExist.getId(), "扣除账户[" + money.getUuid() + "]资金失败，找不到指定的资金账户");
		}

		
		//进行资金扣减操作
		logger.debug("准备执行资金扣减操作，原有资金:" + _oldMoney + ",扣减金额:" + money);
		if(money.getChargeMoney() > 0){
			if(_oldMoney.getChargeMoney() < money.getChargeMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getChargeMoney() + "]充值资金,但余额只有:" + _oldMoney.getChargeMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，充值资金不足");
			}
			_oldMoney.setChargeMoney(_oldMoney.getChargeMoney() - money.getChargeMoney());
		}
		if(money.getIncomingMoney() > 0){
			if(_oldMoney.getIncomingMoney() < money.getIncomingMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getIncomingMoney() + "]收入资金,但余额只有:" + _oldMoney.getIncomingMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，收入资金不足");
			}
			_oldMoney.setIncomingMoney(_oldMoney.getIncomingMoney() - money.getIncomingMoney());
		}
		if(money.getTransitMoney() > 0){
			if(_oldMoney.getTransitMoney() < money.getTransitMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getTransitMoney() + "]充值资金,但余额只有:" + _oldMoney.getTransitMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，在途或可提现资金不足");
			}
			_oldMoney.setTransitMoney(_oldMoney.getTransitMoney() - money.getTransitMoney());
		}
		if(money.getCoin() > 0){
			if(_oldMoney.getCoin() < money.getCoin()){
				logger.error("无法扣除资金，需要扣除[" + money.getCoin() + "]coin,但余额只有:" + _oldMoney.getCoin());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，coin不足");
			}
			_oldMoney.setCoin(_oldMoney.getCoin() - money.getCoin());
		}
		if(money.getPoint() > 0){
			if(_oldMoney.getPoint() < money.getPoint()){
				logger.error("无法扣除资金，需要扣除[" + money.getPoint() + "]point,但余额只有:" + _oldMoney.getPoint());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，point不足");
			}
			_oldMoney.setPoint(_oldMoney.getPoint() - money.getPoint());
		}
		if(money.getScore() > 0){
			if(_oldMoney.getScore() < money.getScore()){
				logger.error("无法扣除资金，需要扣除[" + money.getScore() + "]score,但余额只有:" + _oldMoney.getScore());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，score不足");
			}
			_oldMoney.setScore(_oldMoney.getScore() - money.getScore());
		}
		if(money.getFrozenMoney() > 0){
			if(_oldMoney.getFrozenMoney() < money.getFrozenMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getFrozenMoney() + "]冻结资金,但余额只有:" + _oldMoney.getFrozenMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，冻结资金不足");
			}
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() - money.getFrozenMoney());
		}
		String value = null;
		try {
			value = om.writeValueAsString(_oldMoney);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();

		centerDataService.setForce(key, value, -1);

		
		Money freshMoney = select(money.getUuid(), money.getOwnerId());


		unlockForUpdate(money);
		
		logger.info("执行资金扣减:" + money + ",扣减后的当前资金:" + _oldMoney + ",最新缓存资金是:" + freshMoney);

		moneyDao.update(_oldMoney);		
		messageService.sendJmsDataSyncMessage(null, "moneyService", "updateLocal", _oldMoney);

		BeanUtils.copyProperties(_oldMoney, money);
		return new EisMessage(OperateResult.success.getId(), "扣除账户[" + money.getUuid() + "]资金成功");


	}


/*	@Override
	public EisMessage minus(Money money , String code){
		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		Money _oldMoney = select(money.getUuid(), money.getOwnerId());

		if(_oldMoney == null){
			logger.warn("无法获取用户[" + money.getUuid() + "]的资金账户，无法扣款");
			return new EisMessage(-EisError.moneyAccountNotExist.getId(), "扣除账户[" + money.getUuid() + "]资金失败，找不到指定的资金账户");
		}

		if(!lockForUpdate(money)){
			logger.error("无法在写入资金账户到REDIS前进行锁定");
			return new EisMessage(EisError.distributedLockFail.getId(), "扣除账户[" + money.getUuid() + "]资金失败，无法锁定资金");
		}
		//进行资金扣减操作
		logger.debug("准备执行资金扣减操作，原有资金:" + _oldMoney + ",扣减金额:" + money);
		if(money.getChargeMoney() > 0){
			if(_oldMoney.getChargeMoney() < money.getChargeMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getChargeMoney() + "]充值资金,但余额只有:" + _oldMoney.getChargeMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，充值资金不足");
			}
			_oldMoney.setChargeMoney(_oldMoney.getChargeMoney() - money.getChargeMoney());
		}
		if(money.getIncomingMoney() > 0){
			if(_oldMoney.getIncomingMoney() < money.getIncomingMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getIncomingMoney() + "]收入资金,但余额只有:" + _oldMoney.getIncomingMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，收入资金不足");
			}
			_oldMoney.setIncomingMoney(_oldMoney.getIncomingMoney() - money.getIncomingMoney());
		}
		if(money.getTransitMoney() > 0){
			if(_oldMoney.getTransitMoney() < money.getTransitMoney()){
				logger.error("无法扣除资金，需要扣除[" + money.getTransitMoney() + "]充值资金,但余额只有:" + _oldMoney.getTransitMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，在途或可提现资金不足");
			}
			_oldMoney.setTransitMoney(_oldMoney.getTransitMoney() - money.getTransitMoney());
		}
		if(money.getCoin() > 0){
			if(_oldMoney.getCoin() < money.getCoin()){
				logger.error("无法扣除资金，需要扣除[" + money.getCoin() + "]coin,但余额只有:" + _oldMoney.getCoin());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，coin不足");
			}
			_oldMoney.setCoin(_oldMoney.getCoin() - money.getCoin());
		}
		if(money.getPoint() > 0){
			if(_oldMoney.getPoint() < money.getPoint()){
				logger.error("无法扣除资金，需要扣除[" + money.getPoint() + "]point,但余额只有:" + _oldMoney.getPoint());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，point不足");
			}
			_oldMoney.setPoint(_oldMoney.getPoint() - money.getPoint());
		}
		if(money.getScore() > 0){
			//如果是麻将   积分可以为负数
			if(_oldMoney.getScore() < money.getScore()){
				logger.error("无法扣除资金，需要扣除[" + money.getScore() + "]score,但余额只有:" + _oldMoney.getScore());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，score不足");
			}
			_oldMoney.setScore(_oldMoney.getScore() - money.getScore());
		}
		if(money.getFrozenMoney() > 0){
			if(_oldMoney.getScore() < money.getScore()){
				logger.error("无法扣除资金，需要扣除[" + money.getScore() + "]冻结资金,但余额只有:" + _oldMoney.getFrozenMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，冻结资金不足");
			}
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() - money.getFrozenMoney());
		}
		String value = null;
		try {
			value = om.writeValueAsString(_oldMoney);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();

		centerDataService.setForce(key, value, -1);

		
		Money freshMoney = select(money.getUuid(), money.getOwnerId());


		unlockForUpdate(money);
		
		logger.info("执行资金扣减:" + money + ",扣减后的当前资金:" + _oldMoney + ",最新缓存资金是:" + freshMoney);

		moneyDao.update(_oldMoney);		
		messageService.sendJmsDataSyncMessage(null, "moneyService", "updateLocal", _oldMoney);

		BeanUtils.copyProperties(_oldMoney, money);
		return new EisMessage(OperateResult.success.getId(), "扣除账户[" + money.getUuid() + "]资金成功");


	}*/


	/*@Override
	public int minusLocal(Money money){
		logger.debug("执行本地minus(" + money + ")操作");
		return moneyDao.minus(money);
	}*/



	public int count(MoneyCriteria moneyCriteria) {
		return moneyDao.count(moneyCriteria);
	}

	/* 
	 * 锁定成功后，返回的money对象是最新余额对象
	 */
	@Override
	public EisMessage lock(Money money) {

		Assert.notNull(money,"尝试冻钱的资金账户为空");
		Assert.isTrue(money.getOwnerId() > 0,"尝试冻钱的资金账户ownerId为空");
		Assert.isTrue(money.getUuid() > 0,"尝试冻钱的资金账户UUID为空");

		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		Money _oldMoney = null;
		//先从REDIS获取
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();

		String value = centerDataService.get(key);
		if(value != null){
			try {
				_oldMoney = om.readValue(value,Money.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(_oldMoney == null){
			logger.warn("无法从REDIS读取Money实例:" + key + "，尝试从数据库读取");
			_oldMoney =  moneyDao.select(money.getUuid());
			if(_oldMoney == null){
				logger.warn("无法从数据库中读取Money实例:" + key);
				_oldMoney = new Money(money.getOwnerId());
				_oldMoney.setOwnerId(money.getUuid());

			}
		}
		if(!lockForUpdate(money)){
			logger.error("无法在写入资金账户到REDIS前进行锁定");
			return new EisMessage(EisError.distributedLockFail.id,"无法锁定资金帐号");
		}
		logger.debug("准备执行资金冻结操作，原有资金:" + _oldMoney + ", 请求冻结金额:" + money);
		if(money.getChargeMoney() > 0){
			if(_oldMoney.getChargeMoney() < money.getChargeMoney()){
				logger.error("无法冻结资金，需要冻结[" + money.getChargeMoney() + "]充值资金,但余额只有:" + _oldMoney.getChargeMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "锁定账户[" + money.getUuid() + "]资金失败，充值资金不足");
			}
			_oldMoney.setChargeMoney(_oldMoney.getChargeMoney() - money.getChargeMoney());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() + money.getChargeMoney());
		}
		if(money.getCoin() > 0){
			if(_oldMoney.getCoin() < money.getCoin()){
				logger.error("冻结资金，需要冻结[" + money.getCoin() + "]coin,但余额只有:" + _oldMoney.getCoin());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，coin不足");
			}
			_oldMoney.setCoin(_oldMoney.getCoin() - money.getCoin());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() + money.getCoin());
		}
		if(money.getPoint() > 0){
			if(_oldMoney.getPoint() < money.getPoint()){
				logger.error("无法冻结资金，需要冻结[" + money.getPoint() + "]point,但余额只有:" + _oldMoney.getPoint());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，point不足");
			}
			_oldMoney.setPoint(_oldMoney.getPoint() - money.getPoint());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() + money.getPoint());
		}
		if(money.getScore() > 0){
			if(_oldMoney.getScore() < money.getScore()){
				logger.error("无法冻结资金，需要冻结[" + money.getScore() + "]score,但余额只有:" + _oldMoney.getScore());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，score不足");
			}
			_oldMoney.setScore(_oldMoney.getScore() - money.getScore());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() + money.getScore());
		}
		try {
			value = om.writeValueAsString(_oldMoney);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		centerDataService.setForce(key, value, -1);
		unlockForUpdate(money);

		logger.debug("执行资金冻结操作完成后，当前资金:" + _oldMoney + ",请求冻结金额:" + money);

		moneyDao.update(_oldMoney);		
		messageService.sendJmsDataSyncMessage(null, "moneyService", "updateLocal", _oldMoney);

		BeanUtils.copyProperties(_oldMoney, money);
		return new EisMessage(OperateResult.success.getId(), "冻结账户[" + money.getUuid() + "]资金成功");
	}



	/*@Override
	public	int lockLocal(Money money) {
		if(money == null){
			logger.error("资金锁定对象money为空");
			return 0;
		}
		if(money.getUuid() < 1){
			logger.debug("资金锁定条件中没有账户ID");
			return 0;
		}
		if(money.getChargeMoney() <=0 
				&& money.getIncomingMoney() <= 0 
				&& money.getMarginMoney() <= 0 
				&& money.getGiftMoney() <= 0
				&& money.getCoin() <= 0
				&& money.getPoint() <= 0
				&& money.getScore() <= 0){
			logger.debug("资金锁定条件中chargeMoney、incomingMoney、marginMoney、giftMoney、coin、point、score都为0");
			return 0;
		}
		logger.debug("尝试锁定账户资金:" + money);
		int rs = 0;
		try{
			rs = moneyDao.lock(money);
			//	logger.info("执行DAO之后:" + money + "=>" + money.getIncomingMoney());
		}catch(JdbcUpdateAffectedIncorrectNumberOfRowsException e){

		}
		return rs;
	}*/

	@Override
	public EisMessage unLock(Money money) {

		Assert.notNull(money,"尝试解冻的资金账户为空");
		Assert.isTrue(money.getOwnerId() > 0,"尝试解冻的资金账户ownerId为空");
		Assert.isTrue(money.getUuid() > 0,"尝试解冻的资金账户UUID为空");

		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		Money _oldMoney = null;
		//先从REDIS获取
		String key = KeyConstants.MONEY_INSTANCE_PREFIX + "#" + money.getOwnerId() + "#" + money.getUuid();

		String value = centerDataService.get(key);
		if(value != null){
			try {
				_oldMoney = om.readValue(value,Money.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(_oldMoney == null){
			logger.warn("无法从REDIS读取Money实例:" + key + "，尝试从数据库读取");
			_oldMoney =  moneyDao.select(money.getUuid());
			if(_oldMoney == null){
				logger.warn("无法从数据库中读取Money实例:" + key);
				return new EisMessage(-EisError.moneyAccountNotExist.getId(), "扣除账户[" + money.getUuid() + "]资金失败，找不到指定的资金账户");
			}
		}
		if(!lockForUpdate(money)){
			logger.error("无法在写入资金账户到REDIS前进行锁定");
			return null;
		}
		//进行资金扣减操作
		logger.debug("准备执行资金冻结操作，原有资金:" + _oldMoney + ",扣减金额:" + money);
		if(money.getChargeMoney() > 0){
			if(_oldMoney.getFrozenMoney() < money.getChargeMoney()){
				logger.error("无法解冻资金，需要解冻[" + money.getChargeMoney() + "]充值资金,但余额只有:" + _oldMoney.getFrozenMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "锁定账户[" + money.getUuid() + "]资金失败，充值资金不足");
			}
			_oldMoney.setChargeMoney(_oldMoney.getChargeMoney() + money.getChargeMoney());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() - money.getChargeMoney());
		}
		if(money.getCoin() > 0){
			if(_oldMoney.getFrozenMoney() < money.getCoin()){
				logger.error("无法解冻资金，需要解冻[" + money.getCoin() + "]coin,但余额只有:" + _oldMoney.getFrozenMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，coin不足");
			}
			_oldMoney.setCoin(_oldMoney.getCoin() - money.getCoin());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() - money.getCoin());
		}
		if(money.getPoint() > 0){
			if(_oldMoney.getFrozenMoney() < money.getPoint()){
				logger.error("无法冻结资金，需要冻结[" + money.getPoint() + "]point,但余额只有:" + _oldMoney.getFrozenMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，point不足");
			}
			_oldMoney.setPoint(_oldMoney.getPoint() + money.getPoint());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() - money.getPoint());
		}
		if(money.getScore() > 0){
			if(_oldMoney.getFrozenMoney() < money.getScore()){
				logger.error("无法冻结资金，需要冻结[" + money.getScore() + "]score,但余额只有:" + _oldMoney.getFrozenMoney());
				unlockForUpdate(money);
				return new EisMessage(-EisError.moneyNotEnough.getId(), "扣除账户[" + money.getUuid() + "]资金失败，score不足");
			}
			_oldMoney.setScore(_oldMoney.getScore() + money.getScore());
			_oldMoney.setFrozenMoney(_oldMoney.getFrozenMoney() - money.getScore());
		}
		try {
			value = om.writeValueAsString(_oldMoney);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		centerDataService.setForce(key, value, -1);
		unlockForUpdate(money);

		logger.debug("执行资金解冻操作完成后，当前资金:" + _oldMoney + ",请求解冻金额:" + money);

		moneyDao.update(_oldMoney);		
		messageService.sendJmsDataSyncMessage(null, "moneyService", "updateLocal", _oldMoney);

		BeanUtils.copyProperties(_oldMoney, money);
		return new EisMessage(OperateResult.success.getId(), "解冻账户[" + money.getUuid() + "]资金成功");
	}

	/*private EisMessage _unLockRemote(Money money){
		EisMessage message = new EisMessage(MessageLevel.system.getCode(), ObjectType.money.toString(),  Operate.unLock.getId());
		message.setAttachment(new HashMap<String, Object>());
		message.getAttachment().put("money", money);
		messageService.send(messageBusName, message);
		message = null;
		return new EisMessage(OperateResult.accept.getId(), "资金解锁需求已提交");		
	}

	@Override
	public int unLockLocal(Money money) {
		if(money == null){
			logger.debug("资金解锁条件moneyCriteria为空");
			return 0;
		}
		if(money.getUuid() < 1){
			logger.debug("资金解锁条件中没有账户ID");
			return 0;
		}
		if(money.getChargeMoney() <=0 
				&& money.getIncomingMoney() <= 0 
				&& money.getMarginMoney() <= 0 
				&& money.getGiftMoney() <= 0
				&& money.getCoin() <= 0
				&& money.getPoint() <= 0
				&& money.getScore() <= 0){
			logger.debug("资金解锁条件中chargeMoney、incomingMoney、marginMoney、giftMoney、coin、point和score都为0");
			return 0;
		}


		StringBuffer message = new StringBuffer();
		message.append("尝试解锁账户[" + money.getUuid() + "]资金[");
		if(money.getChargeMoney() > 0){
			message.append(" chargeMoney:" + money.getChargeMoney());
		}
		if(money.getIncomingMoney() > 0){
			message.append(" incomingMoney:" + money.getIncomingMoney());
		}
		if(money.getMarginMoney() > 0){
			message.append(" marginMoney:" + money.getMarginMoney());
		}
		if(money.getGiftMoney() > 0){
			message.append(" giftMoney:" + money.getGiftMoney());
		}
		if(money.getCoin() > 0){
			message.append(" coin:" + money.getCoin());
		}
		if(money.getPoint() > 0){
			message.append(" point:" + money.getPoint());
		}
		if(money.getScore() > 0){
			message.append(" score:" + money.getScore());
		}
		message.append("]");
		logger.debug(message.toString());
		int rs = 0;
		try{
			rs = moneyDao.unLock(money);
		}catch(JdbcUpdateAffectedIncorrectNumberOfRowsException e){

		}
		return rs;
	}*/


	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(EisMessage eisMessage) {
		if(!handlerMoney){
			logger.debug("本节点不负责处理资金数据更新，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}	
		if(eisMessage.getOperateCode() == 0){
			eisMessage = null;
			return;
		}
		if(eisMessage.getAttachment() == null || eisMessage.getAttachment().get("money") == null){
			logger.info("money消息中不包含money附件");
			eisMessage = null;
			return;
		}
		Money money  = null;
		Object object = eisMessage.getAttachment().get("money");
		if(object instanceof Money){
			money = (Money)object;
		} else if(object instanceof LinkedHashMap){
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
			String textData = null;
			try{
				textData = om.writeValueAsString(object);
				money = om.readValue(textData, Money.class);
			}catch(Exception e){}
		}
		if(money == null){
			logger.error("无法解析消息附件中的money数据");
			eisMessage = null;
			return;
		}
		EisMessage replyMessage = new EisMessage(MessageLevel.system.getCode(), ObjectType.money.toString(),0);
		//复制附件map
		try{
			replyMessage.setAttachment((HashMap<String, Object>)eisMessage.getAttachment().clone());			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("无法克隆消息附件");
			eisMessage = null;
			replyMessage = null;
			return;
		}
		replyMessage.setReplyMessageId(eisMessage.getMessageId());

		if(eisMessage.getOperateCode() == Operate.lock.getId()){
			lock(money);
		}
		if(eisMessage.getOperateCode() == Operate.unLock.getId()){
			unLock(money);
		}
		if(eisMessage.getOperateCode() == Operate.plus.getId()){
			plus(money);
		}
		if(eisMessage.getOperateCode() == Operate.minus.getId()){
			minus(money);
		}
		//if(eisMessage.isNeedReply()){
		//logger.info("发送资金处理结果的消息到消息总线[operateCode:" + replyMessage.getOperateCode() + ", operateResult:" + replyMessage.getOperateResult() + "，queue:" + eisMessage.getReceiverName() + " ]");
		//messageService.reply(null, eisMessage.getReceiverName(), eisMessage.getReplyMessageId(), replyMessage);
		//messageService.reply(messageBusName, eisMessage.getMessageId(), replyMessage);
		//	}
		money  = null;
		eisMessage = null;

	}

	@Override
	public boolean haveEnoughMoney(Money money) {
		if(money.getUuid() < 1){
			logger.error("未指定检查资金的UUID");
			return false;
		}
		Money _oldMoney = select(money.getUuid(), money.getOwnerId());
		if(_oldMoney == null){
			logger.debug("未找到指定用户[" + money.getUuid() + "]的资金帐户");
			return false;
		}
		if(money.getChargeMoney() > _oldMoney.getChargeMoney()){
			logger.debug("用户[" + money.getUuid() + "]的资金帐户充值资金[" + _oldMoney.getChargeMoney() + "]少于指定金额:" + money.getChargeMoney());
			return false;
		}
		if(money.getCoin() > _oldMoney.getCoin()){
			logger.debug("用户[" + money.getUuid() + "]的资金帐户coin[" + _oldMoney.getCoin() + "]少于指定金额:" + money.getCoin());
			return false;
		}
		if(money.getPoint() > _oldMoney.getPoint()){
			logger.debug("用户[" + money.getUuid() + "]的资金帐户point[" + _oldMoney.getPoint() + "]少于指定金额:" + money.getPoint());
			return false;
		}
		if(money.getScore() > _oldMoney.getScore()){
			logger.debug("用户[" + money.getUuid() + "]的资金帐户score[" + _oldMoney.getScore() + "]少于指定金额:" + money.getScore());
			return false;
		}


		return true;

	}

	@Override
	public boolean snapBalance(Money money, String balanceTime, long liveSec) {
		if(money == null){
			return false;
		}
		final String key = new StringBuffer().append("MONEY_BALANCE_SNAP#").append( money.getUuid()).append("#").append(balanceTime).toString();
		return centerDataService.setIfNotExist(key, JsonUtils.toStringFull(money), liveSec);
	}
	
	@Override
	public Money getSnapBalance(long uuid, String balanceTime) {
		
		final String key = new StringBuffer().append("MONEY_BALANCE_SNAP#").append(uuid).append("#").append(balanceTime).toString();
		String text = centerDataService.get(key);
		if(StringUtils.isBlank(text)){
			return null;
		}
		try{
			return JsonUtils.getInstance().readValue(text, Money.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	
}

package com.maicard.money.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.MoneyExchangeRuleCriteria;
import com.maicard.money.dao.MoneyExchangeRuleDao;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.MoneyExchangeRule;
import com.maicard.money.domain.PointExchange;
import com.maicard.money.service.MoneyExchangeRuleService;
import com.maicard.money.service.MoneyService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.MessageStandard.MessageLevel;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@ProcessMessageObject("moneyExchangeRule")
public class MoneyExchangeRuleServiceImpl extends BaseService implements MoneyExchangeRuleService,EisMessageListener{

	@Resource
	private MoneyExchangeRuleDao moneyExchangeRuleDao;

	@Resource
	private ConfigService configService;

	@Resource
	private MoneyService moneyService;

	@Resource
	private MessageService messageService;

	private boolean handlerMoney;

	@PostConstruct
	public void init(){
		handlerMoney = configService.getBooleanValue(DataName.handlerMoney.toString(),0);
	}

	public int insert(MoneyExchangeRule moneyExchangeRule) {
		try{
			return moneyExchangeRuleDao.insert(moneyExchangeRule);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'MoneyExchangeRule#' + #moneyExchangeRule.moneyExchangeRuleId")
	public int update(MoneyExchangeRule moneyExchangeRule) {
		try{
			return  moneyExchangeRuleDao.update(moneyExchangeRule);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'MoneyExchangeRule#' + #moneyExchangeRuleId")
	public int delete(int moneyExchangeRuleId) {
		try{
			return  moneyExchangeRuleDao.delete(moneyExchangeRuleId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'MoneyExchangeRule#' + #moneyExchangeRuleId")
	public MoneyExchangeRule select(int moneyExchangeRuleId) {
		return moneyExchangeRuleDao.select(moneyExchangeRuleId);
	}

	public List<MoneyExchangeRule> list(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) {
		return moneyExchangeRuleDao.list(moneyExchangeRuleCriteria);
	}

	public List<MoneyExchangeRule> listOnPage(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) {
		return moneyExchangeRuleDao.listOnPage(moneyExchangeRuleCriteria);
	}

	public int count(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) {
		return moneyExchangeRuleDao.count(moneyExchangeRuleCriteria);
	}

	@Override
	public EisMessage exchange(MoneyExchangeRule moneyExchangeRule) {
		if(handlerMoney){
			return _exchangeLocal(moneyExchangeRule);
		}
		return _exchangeRemote(moneyExchangeRule);
	}

	private EisMessage _exchangeLocal(MoneyExchangeRule moneyExchangeRule){
		if(moneyExchangeRule == null){
			logger.error("尝试进行兑换的moneyExchangeRule为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"尝试进行兑换的规则为空");
		}
		if(moneyExchangeRule.getAmount() <= 0){
			logger.error("尝试进行兑换的moneyExchangeRule金额为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"尝试进行兑换的规则金额为空");
		}
		if(moneyExchangeRule.getRate() <= 0){
			logger.error("尝试进行兑换的moneyExchangeRule兑换比例为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"尝试进行兑换的规则兑换比例异常");
		}
		if(moneyExchangeRule.getRate() > 1){
			logger.error("尝试进行兑换的moneyExchangeRule兑换比例大于1:" + moneyExchangeRule.getRate());
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"尝试进行兑换的规则兑换比例异常");
		}
		Money sourceMoney = new Money();
		sourceMoney.setUuid(moneyExchangeRule.getUuid());
		Money destMoney = new Money();
		destMoney.setUuid(moneyExchangeRule.getUuid());
		if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.money.getCode())){
			sourceMoney.setChargeMoney(moneyExchangeRule.getAmount());
		} else if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.coin.getCode())){
			sourceMoney.setCoin(moneyExchangeRule.getAmount());
		} else if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.point.getCode())){
			sourceMoney.setPoint(moneyExchangeRule.getAmount());
		} else if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.score.getCode())){
			sourceMoney.setScore((long)moneyExchangeRule.getAmount());
		} else {
			logger.error("不支持的源兑换类型:" + moneyExchangeRule.getSourceMoneyType());
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"不支持的源兑换类型:" + moneyExchangeRule.getSourceMoneyType());
		}
		float destMoneyAmount = moneyExchangeRule.getAmount() * moneyExchangeRule.getRate();
		if(moneyExchangeRule.getDestMoneyType().toLowerCase().equals(MoneyType.money.getCode())){
			destMoney.setChargeMoney(destMoneyAmount);
		} else if(moneyExchangeRule.getDestMoneyType().toLowerCase().equals(MoneyType.coin.getCode())){
			destMoney.setCoin(destMoneyAmount);
		} else if(moneyExchangeRule.getDestMoneyType().toLowerCase().equals(MoneyType.point.getCode())){
			destMoney.setPoint(destMoneyAmount);
		} else if(moneyExchangeRule.getDestMoneyType().toLowerCase().equals(MoneyType.score.getCode())){
			destMoney.setScore((long)destMoneyAmount);
		} else {
			logger.error("不支持的目标兑换类型:" + moneyExchangeRule.getSourceMoneyType());
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"不支持的目标兑换类型:" + moneyExchangeRule.getSourceMoneyType());
		}

		EisMessage minusResult = moneyService.minus(sourceMoney);
		logger.info("扣除兑换源资金[类型=" + moneyExchangeRule.getSourceMoneyType() + ",数量:" + moneyExchangeRule.getAmount() + "]结果:" + (minusResult == null ? "空" : minusResult.getOperateCode()) );
		if(minusResult != null && minusResult.getOperateCode() == OperateResult.success.getId()){
			EisMessage plusResult = moneyService.plus(destMoney);
			logger.info("增加兑换目标资金[类型=" + moneyExchangeRule.getDestMoneyType() + ",数量:" + destMoney + "]结果:" + (plusResult == null ? "空" : plusResult.getOperateCode()) );
			return plusResult;
		}
		return null;
	}

	private EisMessage _exchangeRemote(MoneyExchangeRule moneyExchangeRule){
		int rs = preCheck(moneyExchangeRule);
		logger.info("货币交换预检查结果:" + rs);
		if(rs != OperateResult.success.getId()){
			logger.error("无法通过货币交换预检查");
			return new EisMessage(rs, "无法进行货币兑换");
		}
		EisMessage message = new EisMessage(MessageLevel.system.getCode(), ObjectType.moneyExchangeRule.toString(),  Operate.exchange.getId());
		message.setAttachment(new HashMap<String, Object>());
		message.getAttachment().put("moneyExchangeRule", moneyExchangeRule);
		messageService.send(null, message);
		message = null;
		return new EisMessage(OperateResult.accept.getId(), "货币兑换请求已提交");		
	}

	@Override
	public int preCheck(MoneyExchangeRule moneyExchangeRule) {
		if(moneyExchangeRule == null){
			logger.error("尝试进行兑换的moneyExchangeRule为空");
			return EisError.OBJECT_IS_NULL.getId();
		}
		if(moneyExchangeRule.getAmount() <= 0){
			logger.error("尝试进行兑换的moneyExchangeRule金额为空");
			return EisError.OBJECT_IS_NULL.getId();
		}
		if(moneyExchangeRule.getRate() <= 0){
			logger.error("尝试进行兑换的moneyExchangeRule兑换比例为空");
			return EisError.dataIllegal.getId();
		}
		if(moneyExchangeRule.getRate() > 1){
			logger.error("尝试进行兑换的moneyExchangeRule兑换比例大于1:" + moneyExchangeRule.getRate());
			return EisError.dataIllegal.getId();
		}
		Money money = moneyService.select(moneyExchangeRule.getUuid(),moneyExchangeRule.getOwnerId() );
		if(money == null){
			logger.error("找不到尝试兑换的资金账户:" + moneyExchangeRule.getUuid());
			return EisError.moneyAccountNotExist.getId();
		}
		if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.money.getCode())){
			if(money.getChargeMoney() < moneyExchangeRule.getAmount()){
				logger.info("货币兑换源是money，但资金账户chargeMoney[" + money.getChargeMoney() + "]不足以兑换:" + moneyExchangeRule.getAmount() );
			} else {
				logger.info("货币兑换源是money，资金账户chargeMoney[" + money.getChargeMoney() + "]足以兑换:" + moneyExchangeRule.getAmount() );
				return OperateResult.success.getId();
			}
		} else if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.coin.getCode())){
			if(money.getCoin() < moneyExchangeRule.getAmount()){
				logger.info("货币兑换源是coin，但资金账户coin[" + money.getCoin() + "]不足以兑换:" + moneyExchangeRule.getAmount() );
			}	else {
				logger.info("货币兑换源是coin，资金账户coin[" + money.getCoin() + "]足以兑换:" + moneyExchangeRule.getAmount() );
				return OperateResult.success.getId();
			}	
		} else if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.point.getCode())){
			if(money.getPoint() < moneyExchangeRule.getAmount()){
				logger.info("货币兑换源是point，但资金账户point[" + money.getPoint() + "]不足以兑换:" + moneyExchangeRule.getAmount() );
			}	else {
				logger.info("货币兑换源是point，资金账户point[" + money.getPoint() + "]足以兑换:" + moneyExchangeRule.getAmount() );
				return OperateResult.success.getId();
			}	
		} else if(moneyExchangeRule.getSourceMoneyType().toLowerCase().equals(MoneyType.score.getCode())){
			if(money.getScore() < moneyExchangeRule.getAmount()){
				logger.info("货币兑换源是score，但资金账户score[" + money.getScore() + "]不足以兑换:" + moneyExchangeRule.getAmount() );
			} else {
				logger.info("货币兑换源是score，资金账户score[" + money.getScore() + "]足以兑换:" + moneyExchangeRule.getAmount() );
				return OperateResult.success.getId();
			}	
		} 
		logger.error("不支持的源兑换类型:" + moneyExchangeRule.getSourceMoneyType());
		return EisError.unsupportedMoneyType.getId();
	}

	@Override
	public void onMessage(EisMessage eisMessage) {
		if(handlerMoney){

			logger.debug("货币兑换服务收到消息");
			if(eisMessage == null){
				logger.error("得到的消息是空");
				return;
			}
			if(eisMessage.getObjectType() == null || !eisMessage.getObjectType().equals(ObjectType.moneyExchangeRule.toString())){
				eisMessage = null;
				return;
			}
			if(eisMessage.getAttachment() == null){
				logger.debug("消息中没有附件");
				eisMessage = null;
				return;
			}
			MoneyExchangeRule moneyExchangeRule = null;
			Object object = eisMessage.getAttachment().get("moneyExchangeRule");
			if(object instanceof PointExchange){
				moneyExchangeRule = (MoneyExchangeRule)object;
			} else if(object instanceof LinkedHashMap){
				ObjectMapper om = new ObjectMapper();
				om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
				String textData = null;
				try{
					textData = om.writeValueAsString(object);
					moneyExchangeRule = om.readValue(textData, MoneyExchangeRule.class);
				}catch(Exception e){}
			}
			if(moneyExchangeRule == null){
				logger.debug("消息中没有找到需要的对象pointExchange");
				eisMessage = null;
				return;
			}
			if(logger.isDebugEnabled()){
				logger.debug("消息指定的操作是[" + eisMessage.getOperateCode() + "/" + Operate.unknown.findById(eisMessage.getOperateCode()).getName() + ",syncFlag=" + moneyExchangeRule.getSyncFlag() + "]");
			}
			if(eisMessage.getOperateCode() == Operate.exchange.getId()){
				EisMessage rs = _exchangeLocal(moneyExchangeRule);
				logger.debug("积分兑换结果:" + rs );
			}

		}
		eisMessage = null;				
	}

}

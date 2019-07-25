package com.maicard.money.service.pp.misc;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 使用账户内资金付款购买其他商品
 * @author NetSnake
 * @date 2012-8-4
 */
@Service
public class AccountPayProcessor extends BaseService implements PayProcessor {

	
	@Resource
	private PayService payService;	
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private MoneyService moneyService;

	@Override
	public EisMessage onPay(Pay pay){
		User frontUser = null;
		try{
			frontUser = frontUserService.select(pay.getPayFromAccount());
		}catch(Exception e){}
		if(frontUser == null){
			logger.error("找不到支付订单:{{对应的账户:{}", pay.getTransactionId(), pay.getPayFromAccount());
			return new EisMessage(TransactionStatus.failed.getId(),"帐号[" + pay.getPayFromAccount() + "]不存在");
		}
		
		//return new EisMessage(OperateResult.success.id,"账户正常");//OperateResult.
		//FIXME 从官网支付订单时需要扣除资金，但其他情况貌似只需要返回正常即可
		
		Money minusMoney = new Money(pay.getPayFromAccount(), pay.getOwnerId());
		minusMoney.setChargeMoney(pay.getFaceMoney());
		EisMessage minusResult = moneyService.minus(minusMoney);
		if(minusResult.getOperateCode() == OperateResult.success.id) {
			pay.setRealMoney(pay.getFaceMoney());
			pay.setCurrentStatus(TransactionStatus.success.id);
		}
		logger.info("账户内扣除资金:{},扣除结果:{}",pay.getRealMoney(), minusResult);
		return minusResult;

	}

	@Override
	public EisMessage onQuery(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pay onResult(String resultString) {
		return null;
	}

	@Override
	public EisMessage onRefund(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "账户内资金充值处理器" + this.getClass().getSimpleName();
	}
	

}

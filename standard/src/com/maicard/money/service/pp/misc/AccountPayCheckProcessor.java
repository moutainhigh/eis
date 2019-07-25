package com.maicard.money.service.pp.misc;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.MoneyService;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 直接返回正常
 * @author NetSnake
 * @date 2019-01-01
 */
@Service
public class AccountPayCheckProcessor extends BaseService implements PayProcessor {

	
	//@Resource
	//private PayService payService;	
	//@Resource
	//private FrontUserService frontUserService;
	@Resource
	private MoneyService moneyService;

	@Override
	public EisMessage onPay(Pay pay){
		/*User frontUser = null;
		try{
			frontUser = frontUserService.select(pay.getPayFromAccount());
		}catch(Exception e){}
		if(frontUser == null){
			logger.error("找不到支付订单:{{对应的账户:{}", pay.getTransactionId(), pay.getPayFromAccount());
			return new EisMessage(TransactionStatus.failed.getId(),"帐号[" + pay.getPayFromAccount() + "]不存在");
		}*/
		Money money = moneyService.select(pay.getPayFromAccount(), pay.getOwnerId());
		if(money == null) {
			logger.error("支付订单:{}对应的付款资金账户:{}不存在", pay.getTransactionId(), pay.getPayFromAccount());
			return new EisMessage(EisError.moneyAccountNotExist.id,"付款资金账户不存在");			
		}
		if(money.getChargeMoney() < pay.getFaceMoney()) {
			logger.error("支付订单:{}对应的付款资金账户:{}余额:{}不足以支付订单金额:{}", pay.getTransactionId(), pay.getPayFromAccount(), money, pay.getFaceMoney());
			return new EisMessage(EisError.moneyNotEnough.id,"付款资金账户不足");			
		}
		//pay.setRealMoney(pay.getFaceMoney());
		pay.setCurrentStatus(TransactionStatus.inProcess.id);
		logger.debug("资金账户:{}支付检查返回正常，设置支付订单:{}的成功金额为:{}", pay.getPayFromAccount(), pay.getTransactionId(), pay.getRealMoney());
		
		//返回forceClose，让后续逻辑直接进行成功处理
		return new EisMessage(TransactionStatus.forceClose.id,"账户正常");//OperateResult.
		

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

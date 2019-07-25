package com.maicard.money.service.pp.misc;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 使用内部生成的Coupon进行充值，就是将coupon消费后转换为指定的coin或Point
 *
 *
 * @author NetSnake
 * @date 2017年3月11日
 *
 */
@Service
public class CouponPayProcessor extends BaseService implements PayProcessor {

	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	
	@Resource
	private CouponModelService couponModelService;

	@Resource
	private ApplicationContextService applicationContextService;


	private final String DEFAULT_COUPON_CODE = "DEFAULT_PAY_COUPON";


	@Override
	public EisMessage onPay(Pay pay){
		if(pay == null ){
			throw new RequiredObjectIsNullException("支付对象为空");
		}
		if(pay.getTransactionId() == null){
			throw new RequiredObjectIsNullException("支付对象的订单号为空");
		}
		logger.debug("开始兑换码支付接口..");
		
		String sn = pay.getCardSerialNumber();
		String password = pay.getCardPassword();
		
		String combineNumber = null;
		if(StringUtils.isBlank(sn) && StringUtils.isBlank(password)){
			logger.error("尝试支付的兑换码请求未提供卡号和密码");
			return new EisMessage(EisError.serialNumberError.id,"请输入卡号或密码");
		} 
		if(StringUtils.isBlank(sn) || StringUtils.isBlank(password)){
			//对于前端一起输入了卡号密码的情况，按照输入的长度一半，转换为卡号和密码
			if(sn != null){
				combineNumber = sn.trim();
			}
			if(password != null){
				combineNumber = password.trim();
			}
			int size = combineNumber.length() / 2;
			sn = combineNumber.substring(0, size);
			password = combineNumber.substring(size);
			
		}
		
		String couponCode = pay.getExtraValue("productCode");
		if(couponCode == null){
			couponCode = DEFAULT_COUPON_CODE;
		}
		CouponModel couponModel = couponModelService.select(couponCode, pay.getOwnerId());
		if(couponModel == null){
			logger.error("找不到指定的卡券产品:" + couponCode);
			return new EisMessage(EisError.couponProductNotExist.id,"找不到指定的卡券产品");

		}
		CouponProcessor couponProcessor = applicationContextService.getBeanGeneric(couponModel.getProcessor());
		if(couponProcessor == null){
			logger.error("找不到指定的卡券处理器:" + couponModel.getProcessor());
			return new EisMessage(EisError.couponProcessorIsNull.id,"找不到指定的卡券处理器");
		}
		Coupon coupon = new Coupon(pay.getOwnerId());
		coupon.setCouponCode(couponCode);
		coupon.setCouponSerialNumber(sn);
		coupon.setCouponPassword(password);
		coupon.setUuid(pay.getPayFromAccount());
		int useResult = couponProcessor.consume(coupon);
		if(useResult != OperateResult.success.getId()){
			logger.error("无法使用卡券[" + coupon + "]，返回结果是:" + useResult);
			return new EisMessage(useResult,"无法使用消费码");
		} 
		Money money = coupon.getGiftMoney();
		if(money.getChargeMoney() > 0){
			pay.setMoneyTypeId(MoneyType.chargeMoney.getId());
			pay.setRealMoney(money.getChargeMoney());
		} else if(money.getCoin() > 0){
			pay.setMoneyTypeId(MoneyType.coin.getId());
			pay.setRealMoney(money.getCoin());
		} else if(money.getPoint() > 0){
			pay.setMoneyTypeId(MoneyType.point.getId());
			pay.setRealMoney(money.getPoint());
		}
		logger.debug("卡券[" + coupon + "]消费成功，金额:" + coupon.getGiftMoney() + ",支付订单[" + pay.getTransactionId() + "]的成功类型是:" + pay.getMoneyTypeId() + ",金额是:" + pay.getRealMoney());

		if(useResult == OperateResult.success.id){
			pay.setCurrentStatus(TransactionStatus.success.id);
		} else {
			pay.setCurrentStatus(useResult);
		}
		return new EisMessage(pay.getCurrentStatus(),"交易成功");
	}



	@Override
	public Pay onResult(String resultString){
		return null;

	}

	@Override
	public EisMessage onQuery(Pay pay){
		return null;
	}


	@Override
	public EisMessage onRefund(Pay pay) {
		return null;
	}



	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "内部赠券处理器" + this.getClass().getSimpleName();
	}


}


package com.maicard.money.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.exception.ServiceNotFoundException;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.money.service.BankAccountService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.WithdrawMethodService;
import com.maicard.money.service.WithdrawProcessor;
import com.maicard.money.service.WithdrawService;
import com.maicard.money.service.WithdrawTxService;
import com.maicard.money.service.WithdrawTypeService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyMemory;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

@Service
@ProcessMessageObject("withdraw")
public class WithdrawTxServiceImpl extends BaseService implements WithdrawTxService,EisMessageListener{
	
	

	@Resource
	private ApplicationContextService applicationContextService;	

	@Resource
	private BankAccountService bankAccountService;
	@Resource
	private WithdrawTypeService withdrawTypeService;

	@Resource
	private WithdrawMethodService withdrawMethodService;
	@Resource
	private ConfigService configService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private MessageService messageService;
	@Resource
	private MoneyService moneyService;

	@Resource
	private NotifyService notifyService;
	@Resource
	private PartnerService partnerService;

	@Resource
	private WithdrawService withdrawService;
	
	
	private boolean handlerWithdraw = false;


	private String messageBusName;


	long DEMO_UUID = 8100014;

	@PostConstruct
	public void init(){
		handlerWithdraw = configService.getBooleanValue(DataName.handlerWithdraw.toString(),0);
		messageBusName = configService.getValue(DataName.messageBusSystem.toString(),0);

	}

	@Override
	//本地更新数据
	public EisMessage end(Withdraw withdraw) throws Exception{
		if(!handlerWithdraw){
			return null;
		}
		Withdraw _oldWithdraw = withdrawService.select(withdraw.getTransactionId());
		if(_oldWithdraw == null){
			logger.error("数据库中找不到批付订单[" + withdraw.getTransactionId());
			return new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在");			
		}
		if(_oldWithdraw.getCurrentStatus() == withdraw.getCurrentStatus()){
			logger.debug("批付订单[" + withdraw.getTransactionId() + "]状态未改变");
			return null;//FIXME		
		}
		//_oldWithdraw.setRealMoney(withdraw.getRealMoney());
		_oldWithdraw.setOutOrderId(withdraw.getOutOrderId());
		_oldWithdraw.setEndTime(new Date());
		//只有已存在的批付的状态是处理中时，才判断新传入的withdraw对象状态是否是成功
		if(_oldWithdraw.getCurrentStatus() == TransactionStatus.inProcess.getId()){
			_oldWithdraw.setCurrentStatus(withdraw.getCurrentStatus());
			withdrawService.update(_oldWithdraw);
			return new EisMessage(OperateResult.success.getId(), "操作成功");

		} else {	
			_oldWithdraw.setCurrentStatus(withdraw.getCurrentStatus());

			if(withdrawService.update(_oldWithdraw)  <= 0){
				logger.error("无法更新批付订单[" + _oldWithdraw.getTransactionId() + "].");
				return new EisMessage(EisError.BILL_UPDATE_FAIL.getId(), "无法更新订单");
			}
			messageService.sendJmsDataSyncMessage(messageBusName, "withdrawService", "update", _oldWithdraw);
			logger.debug("批付订单[" + _oldWithdraw.getTransactionId() + "]已更新，并发送同步请求[withdrawService.update(item[" + withdraw.getTransactionId() + "]");

			return new EisMessage(OperateResult.failed.getId(), "批付失败");
		}



	}


	
	@Override
	public EisMessage end(int withdrawMethodId, String resultString, Object params){
		WithdrawMethod withdrawMethod = withdrawMethodService.select(withdrawMethodId);
		if(withdrawMethod == null){
			logger.error("找不到尝试结束的批付方法:" + withdrawMethodId + ",结束字符串:" + resultString);
			return new EisMessage(EisError.UNKNOWN_ERROR.getId(), "");

		}
		WithdrawProcessor withdrawProcessor = applicationContextService.getBeanGeneric(withdrawMethod.getProcessClass());
		if(withdrawProcessor == null){
			throw new ServiceNotFoundException("找不到对应的批付处理器[" + withdrawMethod.getProcessClass());
		}
		Withdraw withdraw = withdrawProcessor.onResult(resultString);

		if(withdraw == null){
			if(logger.isDebugEnabled()){
				logger.debug("批付处理器[" + withdrawMethod.getProcessClass() + "]返回的批付对象是空");
			}
			return new EisMessage(EisError.UNKNOWN_ERROR.getId(), "success");
		}
		if(withdraw.getTransactionId() == null){
			if(logger.isDebugEnabled()){
				logger.debug("批付处理器[" + withdrawMethod.getProcessClass() + "]返回的批付订单是空");
			}
			return new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在");			
		}
		Withdraw _oldWithdraw = withdrawService.select(withdraw.getTransactionId());
		if(_oldWithdraw == null){
			if(logger.isDebugEnabled()){
				logger.debug("根据批付订单[" + withdraw.getTransactionId() + "]找不到存在的批付记录");
			}
			return new EisMessage(EisError.BILL_NOT_EXIST.getId(), null);		
		}
		if(_oldWithdraw.getCurrentStatus() != TransactionStatus.inProcess.getId()){
			if(logger.isDebugEnabled()){
				logger.debug("根据批付订单[" + withdraw.getTransactionId() + "]找到的批付记录状态不是正在处理中，是[" + _oldWithdraw.getCurrentStatus() + "]");
			}
			return new EisMessage(_oldWithdraw.getCurrentStatus(), "");
		}
		_oldWithdraw.setEndTime(new Date());
		_oldWithdraw.setCurrentStatus(withdraw.getCurrentStatus());
		_oldWithdraw.setExtraValue("rawNotify",resultString);
		withdraw.setParentTransactionId(_oldWithdraw.getParentTransactionId());
		withdraw.setExtraValue("rawNotify",resultString);

		
		try {
			withdrawService.update(_oldWithdraw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果批付失败，那么把对应的资金加回用户账户
		if(withdraw.getCurrentStatus() == TransactionStatus.failed.id){

			Money refundMoney = new Money(withdraw.getUuid(), withdraw.getOwnerId());
			refundMoney.setTransitMoney(withdraw.getWithdrawMoney());
			refundMoney.setMemo(MoneyMemory.出款失败退款.toString());

			logger.info("批付订单[" + withdraw.getTransactionId() + "]失败，返还用户资金:" + refundMoney);

			EisMessage plusResult = moneyService.plus(refundMoney);
			if(plusResult.getOperateCode() == OperateResult.success.id){

				if(withdrawMethod.getReferUuid() > 0){
					//还要返还资金到系统虚拟账户
					Money systemRefundMoney = new Money(withdrawMethod.getReferUuid(), withdraw.getOwnerId());
					logger.info("批付订单[" + withdraw.getTransactionId() + "]对应的通道有虚拟资金账户，向该账户返还资金:" + systemRefundMoney);
					systemRefundMoney.setTransitMoney(withdraw.getWithdrawMoney());
					systemRefundMoney.setMemo(MoneyMemory.出款失败退款.toString());
					moneyService.plus(systemRefundMoney);
					
				}
			}
			
		}
		//将withdraw发送到消息总线
		if(logger.isDebugEnabled()){
			logger.debug("将批付订单[" + withdraw.getTransactionId() + "]发送到消息总线，批付状态:" + withdraw.getCurrentStatus());
		}
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.close.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("withdraw", withdraw);	

		m.setObjectType(ObjectType.withdraw.toString());
		messageService.send(messageBusName, m);
		m = null;
		EisMessage result = new EisMessage();
		result.setOperateCode(OperateResult.success.getId());
		result.setAttachment(new HashMap<String,Object>());
		result.getAttachment().put("withdraw", withdraw);	
		result.setObjectType(ObjectType.withdraw.toString());

		if(withdraw.getParentTransactionId() != null){
			//这是个子订单
			Withdraw parentWithdraw = withdrawService.select(withdraw.getParentTransactionId());
			if(parentWithdraw == null){
				logger.error("找不到批付子订单[" + withdraw.getTransactionId() + "]的父订单:" + withdraw.getParentTransactionId());
			} else {
				if(withdraw.getCurrentStatus() == TransactionStatus.success.id){
					parentWithdraw.setSuccessRequest(parentWithdraw.getSuccessRequest()+1);
				} else {
					parentWithdraw.setFailRequest(parentWithdraw.getFailRequest()+1);
				}
				logger.info("批付子订单[" + withdraw.getTransactionId() + "]的父订单:" + withdraw.getParentTransactionId() + ",总请求数:" + parentWithdraw.getTotalRequest() + ",成功" + parentWithdraw.getSuccessRequest() + "单，失败:" + parentWithdraw.getFailRequest());
				boolean parentClosed = false;
				if(parentWithdraw.getSuccessRequest() >= parentWithdraw.getTotalRequest()){
					//父订单成功
					parentWithdraw.setCurrentStatus(TransactionStatus.success.id);
					parentClosed = true;
				} else if(parentWithdraw.getFailRequest() >= parentWithdraw.getTotalRequest()){
					//父订单失败
					parentWithdraw.setCurrentStatus(TransactionStatus.failed.id);
					parentClosed = true;
				} else if(parentWithdraw.getSuccessRequest() + parentWithdraw.getFailRequest() >= parentWithdraw.getTotalRequest()){
					//父订单成功了部分
					parentWithdraw.setCurrentStatus(TransactionStatus.halfComplete.id);
					parentClosed = true;
				} else {
					//父订单还没结束
				}
				int updateResult = 0;
				if(parentClosed){
					parentWithdraw.setEndTime(new Date());
				}
				try {
					updateResult = withdrawService.update(parentWithdraw);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if(parentClosed){
					if(updateResult == 1){
						//发送JMS同步
						parentWithdraw.setSyncFlag(0);
						messageService.sendJmsDataSyncMessage(null, "withdrawService", "update", parentWithdraw);
					}
					//发送父订单的更新
					String inNotifyUrl = parentWithdraw.getExtraValue("inNotifyUrl");
					if(StringUtils.isNotBlank(inNotifyUrl)){
						//发送通知
						notifyService.sendNotify(parentWithdraw);
					}
				}

			}
		}
		return result;



	}

	@Override
	public void onMessage(EisMessage eisMessage) {

		if(handlerWithdraw){

			logger.debug("后台批付服务收到消息");
			if(eisMessage == null){
				logger.error("得到的消息是空");
				return;
			}
			if(eisMessage.getObjectType() == null || !eisMessage.getObjectType().equals(ObjectType.withdraw.toString())){
				eisMessage = null;
				return;
			}
			if(eisMessage.getAttachment() == null){
				logger.debug("消息中没有附件");
				eisMessage = null;
				return;
			}
			Withdraw withdraw = null;
			Object object = eisMessage.getAttachment().get("withdraw");
			if(object instanceof Withdraw){
				withdraw = (Withdraw)object;
			} else if(object instanceof LinkedHashMap){
				ObjectMapper om = new ObjectMapper();
				om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
				String textData = null;
				try{
					textData = om.writeValueAsString(object);
					withdraw = om.readValue(textData, Withdraw.class);
				}catch(Exception e){}
			}
			if(withdraw == null){
				logger.debug("消息中没有找到需要的对象withdraw");
				eisMessage = null;
				return;
			}
			if(logger.isDebugEnabled()){
				logger.debug("消息指定的操作是[" + eisMessage.getOperateCode() + "/" + Operate.unknown.findById(eisMessage.getOperateCode()).getName() + ",syncFlag=" + withdraw.getSyncFlag() + "]");
			}
			EisMessage replyMessage = null;
			int rs = 0;
			if(eisMessage.getOperateCode() == Operate.create.getId()){
				try {
					rs = withdrawService.begin(withdraw,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(rs == 1){
					if(logger.isDebugEnabled()){
						logger.debug("批付订单[" + withdraw.getTransactionId() + "]创建成功，发送批付订单同步请求[withdrawService.insert(" + withdraw + ")]");
					}
					messageService.sendJmsDataSyncMessage(messageBusName, "withdrawService", "insert", withdraw);
				} else{
					logger.error("批付订单[" + withdraw.getTransactionId() + "]创建失败，返回值:" + rs);
				}
			}
			if(eisMessage.getOperateCode() == Operate.close.getId()){
				try{
					replyMessage = this.end(withdraw);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(eisMessage.isNeedReply()){
				messageService.reply(messageBusName, eisMessage.getMessageId(), eisMessage.getReplyMessageId(), replyMessage);
			}
			withdraw = null;
		}
		eisMessage = null;

	}


}

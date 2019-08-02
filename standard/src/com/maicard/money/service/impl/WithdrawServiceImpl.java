package com.maicard.money.service.impl;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.JsonUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.criteria.WithdrawMethodCriteria;
import com.maicard.money.dao.WithdrawDao;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.money.domain.WithdrawType;
import com.maicard.money.service.*;
import com.maicard.product.service.NotifyService;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.*;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WithdrawServiceImpl extends BaseService implements WithdrawService {

	@Resource
	private WithdrawDao withdrawDao;

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


	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	/**
	 * 是否自动设置
	 */
	private boolean autoSetWithdrawMethod = true;

	/**
	 * 是否允许各个商户使用同一张银行卡提现<br>
	 * 如果为false，则每个商户只能使用自己的，当提交一个其他商户使用过的银行卡时则不能提交
	 */
	private boolean BANK_ACCOUNT_SHARED = true;


	long DEMO_UUID = 8100014;

	@PostConstruct
	public void init(){

		BANK_ACCOUNT_SHARED = configService.getBooleanValue(DataName.BANK_ACCOUNT_SHARED.toString(),0);
	}



	@Override
	public int insert(Withdraw withdraw) {
		if(withdraw.getCurrentStatus() == 0){
			withdraw.setCurrentStatus(TransactionStatus.newOrder.getId());
		}
		if(withdraw.getBeginTime() == null){
			withdraw.setBeginTime(new Date());
		}
		return withdrawDao.insert(withdraw);
	}


	@Override
	public int update(Withdraw withdraw) throws Exception {
		return withdrawDao.update(withdraw);		
	}

	/**
	 * 根据渠道请求号获取支付订单
	 * @param channelReqNo
	 * @return
	 */
	@Override
	public Withdraw queryByChannelRequestNo(String channelReqNo) {
		return withdrawDao.queryByChannelRequestNo(channelReqNo);
	}

	@Override
	public int delete(String transactionId) {
		int actualRowsAffected = 0;

		Withdraw _oldWithdraw = withdrawDao.select(transactionId);

		if (_oldWithdraw != null) {
			actualRowsAffected = withdrawDao.delete(transactionId);
		}

		return actualRowsAffected;
	}

	@Override
	public Withdraw select(String transactionId) {
		return withdrawDao.select(transactionId);
	}


	public List<Withdraw> list(WithdrawCriteria withdrawCriteria) {
		List<Withdraw> list =  withdrawDao.list(withdrawCriteria);
		if(list == null){
			return Collections.emptyList();
		}
		return list;
	}

	public List<Withdraw> listOnPage(WithdrawCriteria withdrawCriteria) {
		if(withdrawCriteria.getBeginTimeBegin() == null){
			//设置为本月开始
			try{
				withdrawCriteria.setBeginTimeBegin(    
						DateUtils.truncate(new Date(), Calendar.MONTH));

			}catch(Exception e){
				e.printStackTrace();
			}
		}

		List<Withdraw> withdrawList = withdrawDao.listOnPage(withdrawCriteria);
		if(withdrawList == null){
			return null;
		}
		for(int i = 0; i < withdrawList.size(); i++){
			withdrawList.get(i).setIndex(i+1);
		}
		return withdrawList;
	}



	public int count(WithdrawCriteria withdrawCriteria) {
		return withdrawDao.count(withdrawCriteria);
	}



	


	/*
	 * 1、负责检查各项参数
	 * 2、调用相应的批付处理器为用户完成下一步操作
	 * 3、发送批付对象至消息总线，由对应的节点进行处理

	private EisMessage _startWithdrawRemote(Withdraw withdraw){	

		//全局交易ID
		withdraw.setTransactionId(globalOrderIdService.generate(TransactionType.withdraw.getId()));

		WithdrawType withdrawType = withdrawTypeService.select(withdraw.getWithdrawTypeId());
		if(withdrawType == null){
			return new EisMessage(EisError.objectIsNull.getId(), "找不到指定的批付类型[" + withdraw.getWithdrawTypeId() + "]");			
		}
		logger.info("批付订单[" + withdraw.getTransactionId() + "]的批付方式的手续费费率是:" + withdrawType.getCommission() + "，手续费方式是:" + withdrawType.getCommissionType());
		withdraw.setCurrentStatus(TransactionStatus.newOrder.getId());
		withdraw.setCommissionType(withdrawType.getCommissionType());
		if(withdraw.getCommissionType().equalsIgnoreCase(WithdrawType.COMMISSION_TYPE_FIXED)){
			withdraw.setCommission(withdrawType.getCommission());
			logger.info("批付订单[" + withdraw.getTransactionId() + "]的批付类型[" + withdraw.getWithdrawTypeId() + "]手续费是固定，应扣除手续费:" + withdraw.getCommission());
		} else 	if(withdraw.getCommissionType().equalsIgnoreCase(WithdrawType.COMMISSION_TYPE_RATE)){
			withdraw.setCommission(withdrawType.getCommission() * withdraw.getWithdrawMoney());
			logger.info("批付订单[" + withdraw.getTransactionId() + "]的批付类型[" + withdraw.getWithdrawTypeId() + "]手续费是按比例，应扣除手续费:" + withdraw.getCommission());
		} else {
			logger.error("批付订单[" + withdraw.getTransactionId() + "]的批付类型[" + withdraw.getWithdrawTypeId() + "]手续费类型异常");
			return new EisMessage(EisError.moneyRangeError.getId(), "当前批付类型[" + withdraw.getWithdrawTypeId() + "]手续费类型异常");			

		}
		WithdrawProcessor withdrawProcessor = null;
		try{
			withdrawProcessor = (WithdrawProcessor)applicationContextService.getBean(withdrawType.getProcessor());
		}catch(Exception e){}
		if(withdrawProcessor == null){
			logger.error("批付订单[" + withdraw.getTransactionId() + "]找不到指定的批付处理器[" + withdrawType.getProcessor() + "]" );
			return new EisMessage(EisError.objectIsNull.getId(), "系统异常");
		}

		EisMessage msg = withdrawProcessor.onWithdraw(withdraw);
		//将withdraw发送到消息总线
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.create.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("withdraw", withdraw);	
		m.setObjectType(ObjectType.withdraw.toString());
		try{
			messageService.send(messageBusName, m);
			m = null;
		}catch(Exception e){
			logger.error("消息总线异常:" + e.getMessage());
		}
		return new EisMessage(OperateResult.accept.getId(),"批付请求已提交");
	}*/

	@Override
	public int begin(Withdraw withdraw, List<Withdraw> subWithdrawList) throws Exception{
		Assert.notNull(withdraw,"开始批付的对象不能为空");
		Assert.isTrue(withdraw.getOwnerId() > 0,"开始批付的对象ownerId不能为空");
		Assert.isTrue(withdraw.getUuid() != 0,"开始批付的对象uuid不能为空");
		int isValid = this.isValidWithdraw(withdraw);
		if(isValid != OperateResult.success.getId()){
			//不允许批付
			logger.error("当前不允许批付，检查返回:" + isValid);
			return isValid;
		}
		if(withdraw.getBankAccountId() == 0 && withdraw.getBankAccount() != null){
			withdraw.setBankAccountId(withdraw.getBankAccount().getBankAccountId());
		}

		withdraw.setCurrentStatus(TransactionStatus.newOrder.id);
		if(withdraw.getTransactionId() == null){
			withdraw.setTransactionId(globalOrderIdService.generate(TransactionType.withdraw.getId()));
		}
		WithdrawType withdrawType = withdrawTypeService.select(withdraw.getWithdrawTypeId());
		if(withdrawType == null){
			logger.error("找不到指定的批付类型:" + withdraw.getWithdrawTypeId());
			return EisError.WITHDRAW_TYPE_NOT_FOUND.id;
		}

		List<WithdrawMethod> withdrawMethodList = getWithdrawMethod(withdraw, partnerService.select(withdraw.getUuid()));
		if(withdrawMethodList.size() < 1){
			logger.error("当前未返回任何批付方法，批付订单[" + withdraw.getTransactionId() + "]直接失败");
			int errorCode = EisError.noWithdrawMethod.id;
			withdraw.setCurrentStatus(errorCode);
			return errorCode;
		}
		withdraw.setCommissionType(withdrawType.getCommissionType());
		withdraw.setCommissionChargeType(withdrawType.getCommissionChargeType());
		if(withdraw.getBeginTime() == null){
			withdraw.setBeginTime(new Date());
		}
		/*
		 * commissonPerOrder是每一笔付款请求要扣除的资金，
		 * 如果是单笔请求，则直接扣除该资金
		 * 如果是一次多笔的批付，则应扣除该资金X批付数量
		 */
		float commissonPerOrder = 0f;
		if(withdrawType.getCommission() > 0){
			if(withdrawType.getCommissionType().equals(CommissionType.COMMISSION_TYPE_FIXED)){
				//固定金额手续费，比如说1笔10元
				commissonPerOrder = withdrawType.getCommission();
			} else {
				if(withdrawType.getCommission() >= 1){
					logger.error("批付类型[" + withdrawType.getWithdrawTypeId() + "]的手续费类型是按比例，但是比例异常:" + withdrawType.getCommission());
					return EisError.systemDataError.id;
				} else {
					commissonPerOrder = withdrawType.getCommission() * withdraw.getWithdrawMoney();
				}
			}
		}
		
		//commissonTotal是这一次批付会扣除的总手续费
		float commissonTotal = 0;
		if(subWithdrawList != null && subWithdrawList.size() > 0){
			commissonTotal = commissonPerOrder * subWithdrawList.size();
		} else {
			commissonTotal = commissonPerOrder;
		}
		logger.info("批付类型#{}的手续费类型是:{},,单笔费率/金额是:{},本次总金额是:{}", withdrawType.getWithdrawTypeId(), withdrawType.getCommissionType(), withdrawType.getCommission(), commissonTotal);

		Money money = moneyService.select(withdraw.getUuid(),withdraw.getOwnerId() );
		if(money == null){
			logger.error("批付订单[" + withdraw.getTransactionId() + "]对应用户[" + withdraw.getUuid() + "]没有资金账户，无法批付");
			return EisError.userNotFoundInSystem.getId();
		}
		withdraw.setMoneyBeforeWithdraw(money.getTransitMoney());
		float needMinus = 0f;
		if(commissonTotal > 0){
			withdraw.setCommission(commissonTotal);
			//如果手续费收取方式是从批付资金中扣除，那么这里到账金额要剪掉手续费
			if(withdrawType.getCommissionChargeType().equals(WithdrawType.COMMISSION_CHARGE_TYPE_IN_WITHDRAW)){
				withdraw.setArriveMoney(withdraw.getWithdrawMoney() - commissonTotal);
				needMinus = withdraw.getWithdrawMoney();				
			} else {
				//如果手续费收取方式是从剩下的资金中扣除，那么这里到要从账户中扣除加上手续费后的总费用
				withdraw.setArriveMoney(withdraw.getWithdrawMoney());
				needMinus = withdraw.getWithdrawMoney() + commissonTotal;
			}
		} else {
			needMinus = withdraw.getWithdrawMoney();
			withdraw.setArriveMoney(withdraw.getWithdrawMoney());
		}
		/*
		 * money.transitMoney是可批付金额
		 * money.incomingMoney是账户余额
		 * 批付时，将同时从这两个余额中扣除资金
		 * 对于T+0的用户而言，可批付金额就是账户余额，但是对于T+1或其他周期的用户而言，transitMoney可能是用户截至到当日0点的收入资金，
		 * 当天的资金不计入transitMoney，这样就不能直接批付当日资金
		 * 
		 */
		if(money.getTransitMoney() < needMinus){
			logger.error("批付订单[" + withdraw.getTransactionId() + "]用户[" + withdraw.getUuid() + "]在途资金是[" + money.getTransitMoney() + "]，不够批付金额[" + withdraw.getWithdrawMoney() + "]和手续费[" + withdraw.getCommission() + "]，无法批付");
			return EisError.moneyNotEnough.id;
		}
		/*if(money.getIncomingMoney() < needMinus){
			logger.error("批付订单[" + withdraw.getTransactionId() + "]用户[" + withdraw.getUuid() + "]收入资金是[" + money.getIncomingMoney() + "]，不够批付金额[" + withdraw.getWithdrawMoney() + "]和手续费[" + withdraw.getCommission() + "]，无法批付");
			return EisError.moneyNotEnough.getId();
		}*/
		Money minusMoney = new Money(withdraw.getUuid(),withdraw.getOwnerId());
		//minusMoney.setIncomingMoney(needMinus);
		minusMoney.setTransitMoney(needMinus);

		/**
		 * refundMoney是用来在自动批付失败时返还资金
		 */
		Money refundMoney = minusMoney.clone();
		//systemMinusMoney从系统账户中扣除的钱
		Money systemMinusMoney = minusMoney.clone();
		//systemMinusMoney是如果付款失败向系统账户中回滚的钱
		systemMinusMoney.setUuid(0);
		Money systemRefundMoney = systemMinusMoney.clone();
		boolean systemMinusSuccess = false;

		minusMoney.setMemo(MoneyMemory.出款支出.toString());
		refundMoney.setMemo(MoneyMemory.出款失败退款.toString());
		final String currentUrl = withdraw.getExtraValue("currentUrl");
		if(currentUrl == null){
			logger.error("批付订单:" + withdraw.getTransactionId() + "没有必须的扩展数据currentUrl");
			return EisError.systemDataError.getId();
		}


		withdraw.setCurrentStatus(TransactionStatus.newOrder.getId());
		withdraw.setMoneyAfterWithdraw(minusMoney.getTransitMoney());
		int rs = insert(withdraw);
		if(rs != 1){
			//插入失败
			return EisError.BILL_CREATE_FAIL.id;
		}
		EisMessage moneyMinusResult = moneyService.minus(minusMoney);
		if(moneyMinusResult.getOperateCode() != OperateResult.success.getId()){
			return moneyMinusResult.getOperateCode();
		}
		logger.info("批付订单[" + withdraw.getTransactionId() + "]完成扣款，锁定用户[" + withdraw.getUuid() + "]收入资金[" + withdraw.getWithdrawMoney() + "+" + withdraw.getCommission() + "]共计:" + refundMoney.getTransitMoney() + ",锁定后用户剩余可批付资金:" + withdraw.getMoneyAfterWithdraw());

		if(subWithdrawList != null && subWithdrawList.size() > 0){
			//是批量批付，需要循环处理子订单
			for(Withdraw subWithdraw : subWithdrawList){
				insert(subWithdraw);
				EisMessage 	finalResult = new EisMessage(EisError.noWithdrawMethod.id,"");


				for(WithdrawMethod withdrawMethod : withdrawMethodList){
					subWithdraw.setWithdrawMethod(withdrawMethod);
					subWithdraw.setWithdrawMethodId(withdrawMethod.getWithdrawMethodId());
					String notifyUrl = currentUrl.replaceAll("\\.shtml$",".json").replaceAll("/batch.json", "/create.json").replaceAll("/withdraw/create.json", "/withdraw/notify/" + subWithdraw.getWithdrawMethodId() + ".json");
					subWithdraw.setExtraValue("notifyUrl",notifyUrl);
	
					if(StringUtils.isBlank(withdrawMethod.getProcessClass())){
						logger.info("批付方式[" + JsonUtils.toStringFull(withdrawMethod) + "]未指定批付处理器");
						finalResult = new EisMessage(EisError.processorIsNull.id,"找不到处理程序");
						continue;
					}
					if(!BANK_ACCOUNT_SHARED && subWithdraw.getBankAccount().getWithdrawMethodId() > 0 && subWithdraw.getBankAccount().getWithdrawMethodId() != withdrawMethod.getWithdrawMethodId()){
						//当前帐号设置了提现方法的ID，说明这个帐号只能从指定的提现方法走，如果跟当前不匹配，则不能走
						logger.info("当前银行帐号:" + JsonUtils.toStringFull(subWithdraw.getBankAccount()) + "指定的与当前批付方式[" + withdrawMethod.getWithdrawMethodId() + "]不一致");
						finalResult = new EisMessage(EisError.withdrawMethodNotMatch.id,"批付方法不匹配");
						continue;
					}

					WithdrawProcessor withdrawProcessor = applicationContextService.getBeanGeneric(withdrawMethod.getProcessClass());
					if(withdrawProcessor == null){
						logger.warn("找不到批付类型[" + withdrawType.getWithdrawTypeId() + "]指定的处理器:" + withdrawMethod.getProcessClass());
						finalResult = new EisMessage(EisError.processorIsNull.id,"找不到处理程序");
						continue;
					}

					if(withdrawMethod.getReferUuid() > 0){
						//必须全新new一个Money对象
						systemMinusMoney = new Money(withdrawMethod.getReferUuid(), withdrawMethod.getOwnerId());
						systemMinusMoney.setTransitMoney(subWithdraw.getWithdrawMoney());
						
						EisMessage systemMinusResult = moneyService.minus(systemMinusMoney);
						if(systemMinusResult.getOperateCode() != OperateResult.success.id){
							logger.error("当执行出款时，无法从系统资金账户:" + systemMinusMoney.getUuid() + "中扣款:" + systemRefundMoney.getTransitMoney() + ",返回结果是:" + systemMinusResult + ",跳过该处理");
							//跳过该通道
							finalResult = new EisMessage(EisError.systemMoneyNotEnough.id,"系统虚拟账户资金不足");
							continue;
						} else {
							systemMinusSuccess = true;
						}
					}

					if(logger.isDebugEnabled())logger.debug("准备由批付方法:" + JsonUtils.toStringFull(withdrawMethod) + ",执行子批付:" + subWithdraw.getTransactionId() + ",批付帐号:" + JsonUtils.toStringFull(subWithdraw.getBankAccount()));
					subWithdraw.setCurrentStatus(TransactionStatus.newOrder.getId());
					if(subWithdraw.getUuid() == DEMO_UUID){
						finalResult = new EisMessage(OperateResult.success.id,"DEMO成功");
					} else {
						finalResult = withdrawProcessor.onWithdraw(subWithdraw);
					}

					logger.debug("批付处理器[" + withdrawMethod.getProcessClass() + "]返回的批付处理结果是:" + finalResult);
					if(finalResult.getOperateCode() == OperateResult.success.getId()){
						//XXX 仅为提交成功，不代表批付成功，因此不增加父订单的成功次数
						break;
					} else {
						if(finalResult.getOperateCode() == EisError.withdrawMethodNotMatch.id || finalResult.getOperateCode() == EisError.systemMoneyNotEnough.id || finalResult.getOperateCode() == EisError.channelAccountQuotaExceed.id){
							//这个通道的钱不够了，尝试下一个通道批付
							logger.debug("批付处理器[" + withdrawMethod.getProcessClass() + "]返回的批付处理结果是:" + finalResult + ",尝试下一个批付方式");
							if(systemMinusSuccess){
								//还要退还资金到系统账户，必须全新new一个Money对象
								systemRefundMoney = new Money(withdrawMethod.getReferUuid(), withdrawMethod.getOwnerId());
								systemRefundMoney.setTransitMoney(subWithdraw.getWithdrawMoney());
								logger.info("批付子订单[" + subWithdraw.getTransactionId() + "]由通道:" + withdrawMethod.getWithdrawMethodId() + "处理结果是:" + finalResult.getOperateCode() + ",退还资金:" +  systemRefundMoney + "到系统资金账户");
								moneyService.plus(systemRefundMoney);
							}
							continue;
						} else {

							logger.debug("批付处理器[" + withdrawMethod.getProcessClass() + "]返回的批付处理结果是:" + finalResult + ",本批总请求数是" + withdraw.getTotalRequest() + "，成功次数是:" + withdraw.getSuccessRequest() + ",失败次数是:" + withdraw.getFailRequest());

							//该订单处理完成，以退款处理下一个批付子订单
							break;
						}
					}
				}


				logger.info("批付子订单[" + subWithdraw.getTransactionId() + "]的最终处理结果是:" + finalResult.getOperateCode());
				if(finalResult.getOperateCode() != OperateResult.success.id) {
					Money subRefoundMoney = new Money(subWithdraw.getUuid(), subWithdraw.getOwnerId());
					subRefoundMoney.setTransitMoney(subWithdraw.getWithdrawMoney() + commissonPerOrder);
					moneyService.plus(subRefoundMoney);
					subWithdraw.setCurrentStatus(finalResult.getOperateCode());
					withdraw.setFailRequest(withdraw.getFailRequest()+1);
					logger.info("批付子订单[" + subWithdraw.getTransactionId() + "]的最终处理结果是:" + finalResult.getOperateCode() + ",退还批付资金到账户，退款后资金:" + subRefoundMoney);


				} else {
					//提交成功，给对应银行账号设置成功的withdrawMethodId
					subWithdraw.setBankAccountId(subWithdraw.getBankAccount().getBankAccountId());
					if(autoSetWithdrawMethod){
						//if(subWithdraw.getBankAccount().getWithdrawMethodId() == 0){
							subWithdraw.getBankAccount().setWithdrawMethodId(subWithdraw.getWithdrawMethodId());
							bankAccountService.update(subWithdraw.getBankAccount());
						//}

					}
				}
				update(subWithdraw);
				messageService.sendJmsDataSyncMessage(null, "withdrawService", "insert", subWithdraw);
			}
		} else {
			//单笔批付处理
			EisMessage 	finalResult = new EisMessage(EisError.noWithdrawMethod.id,"");

			for(WithdrawMethod withdrawMethod : withdrawMethodList){

				withdraw.setWithdrawMethod(withdrawMethod);
				withdraw.setWithdrawMethodId(withdrawMethod.getWithdrawMethodId());
				String notifyUrl = currentUrl.replaceAll("\\.shtml$",".json").replaceAll("/batch.json", "/create.json").replaceAll("/withdraw/create.json", "/withdraw/notify/" + withdraw.getWithdrawMethodId() + ".json");
				withdraw.setExtraValue("notifyUrl",notifyUrl);
				
				if(StringUtils.isBlank(withdrawMethod.getProcessClass())){
					logger.info("批付方式[" + JsonUtils.toStringFull(withdrawMethod) + "]未指定批付处理器");
					finalResult = new EisMessage(EisError.processorIsNull.id,"找不到处理程序");
					continue;
				}
				WithdrawProcessor withdrawProcessor = applicationContextService.getBeanGeneric(withdrawMethod.getProcessClass());
				if(withdrawProcessor == null){
					logger.warn("找不到批付类型[" + withdrawType.getWithdrawTypeId() + "]指定的处理器:" + withdrawMethod.getProcessClass());
					finalResult = new EisMessage(EisError.processorIsNull.id,"找不到处理程序");
					continue;
				}
				if(withdrawMethod.getReferUuid() > 0){
					systemMinusMoney.setUuid(withdrawMethod.getReferUuid());
					systemRefundMoney.setUuid(withdrawMethod.getReferUuid());
					EisMessage systemMinusResult = moneyService.minus(systemMinusMoney);
					if(systemMinusResult.getOperateCode() != OperateResult.success.id){
						logger.error("当执行出款时，无法从系统资金账户:" + systemMinusMoney.getUuid() + "中扣款:" + systemRefundMoney.getTransitMoney() + ",返回结果是:" + systemMinusResult + ",跳过该处理");
						//跳过该通道
						finalResult = new EisMessage(EisError.systemMoneyNotEnough.id,"系统虚拟账户资金不足");
						continue;
					} else {
						systemMinusSuccess = true;
					}
				}
				if(!BANK_ACCOUNT_SHARED && withdraw.getBankAccount().getWithdrawMethodId() > 0 && withdraw.getBankAccount().getWithdrawMethodId() != withdrawMethod.getWithdrawMethodId()){
					//当前帐号设置了提现方法的ID，说明这个帐号只能从指定的提现方法走，如果跟当前不匹配，则不能走
					logger.info("当前银行帐号:" + JsonUtils.toStringFull(withdraw.getBankAccount()) + "指定的与当前批付方式[" + withdrawMethod.getWithdrawMethodId() + "]不一致");
					finalResult = new EisMessage(EisError.withdrawMethodNotMatch.id,"批付方法不匹配");
					continue;
				}
				

				if(logger.isDebugEnabled())logger.debug("准备由批付方法:" + JsonUtils.toStringFull(withdrawMethod) + ",执行批付:" + withdraw.getTransactionId());
				if(withdraw.getUuid() == DEMO_UUID){
					finalResult = new EisMessage(OperateResult.success.id,"DEMO成功");
					break;
				}
				finalResult = withdrawProcessor.onWithdraw(withdraw);
				logger.debug("批付处理器[" + withdrawMethod + "]返回的批付处理结果是:" + finalResult);
				if(finalResult.getOperateCode() == OperateResult.success.getId()){
					//批付申请成功
					break;
				} else {
					if(finalResult.getOperateCode() == EisError.systemMoneyNotEnough.id || finalResult.getOperateCode() == EisError.channelAccountQuotaExceed.id){
						//这个通道的钱不够了，尝试下一个通道批付
						logger.debug("批付处理器[" + withdrawMethod + "]返回的批付处理结果是:" + finalResult + ",尝试下一个批付方式");
						continue;
					} else {
						logger.debug("批付处理器[" + withdrawMethod + "]返回的批付处理结果是:" + finalResult + ",不再尝试下一个批付方式");

						break;
					}
				}
			}

			logger.info("批付订单[" + withdraw.getTransactionId() + "]的最终处理结果是:" + finalResult.getOperateCode());

			if(finalResult.getOperateCode() != OperateResult.success.id) {
				withdraw.setCurrentStatus(finalResult.getOperateCode());
				logger.info("批付订单[" + withdraw.getTransactionId() + "]的最终处理结果是:" + finalResult.getOperateCode() + ",退还批付资金到账户，应退还资金:" + refundMoney);
				moneyService.plus(refundMoney);
				if(systemMinusSuccess){
					//还要退还资金到系统账户
					logger.info("批付子订单[" + withdraw.getTransactionId() + "]的最终处理结果是:" + finalResult.getOperateCode() + ",退还批付资金到账户，退款后资金:" + refundMoney + "并退还资金:" +  systemRefundMoney + "到系统资金账户");
					moneyService.plus(systemRefundMoney);
				}
			} else {
				//提交成功，给对应银行账号设置成功的withdrawMethodId
				if(autoSetWithdrawMethod){
					//if(withdraw.getBankAccount().getWithdrawMethodId() == 0){
						withdraw.getBankAccount().setWithdrawMethodId(withdraw.getWithdrawMethodId());
						
						bankAccountService.update(withdraw.getBankAccount());
					//}

				}
			}

		}
		if(withdraw.getTotalRequest() > 0 ){
			if(withdraw.getFailRequest() == withdraw.getTotalRequest()){
				withdraw.setCurrentStatus(TransactionStatus.failed.id);
				logger.info("批付订单[" + withdraw.getTransactionId() + "]提交时已全部返回错误，把该订单状态改为失败");
				notifyService.sendNotify(withdraw);
			} else {
				withdraw.setCurrentStatus(TransactionStatus.inProcess.id);

			}
		}
		rs = this.update(withdraw);
		if(rs != 1){
			logger.error("无法更新批付订单:" + withdraw.getTransactionId() + ",返回结果:" + rs);
			return rs;
		}
		messageService.sendJmsDataSyncMessage(null, "withdrawService", "insert", withdraw);
		return withdraw.getCurrentStatus();




	}

	/**
	 * 校验一个批付申请是否合规
	 */
	@Override
	public int isValidWithdraw(Withdraw withdraw) {
		int withdrawTypeId = withdraw.getWithdrawTypeId();
		if(withdrawTypeId < 1){
			logger.info("申请批付类型为0，使用第一个批付类型1");
			withdrawTypeId = 1;
		}

		WithdrawType withdrawType = withdrawTypeService.select(withdrawTypeId);
		if(withdrawType == null){
			logger.error("找不到指定的批付类型:" + withdrawTypeId);
			return EisError.REQUIRED_PARAMETER.id;
		}
		Calendar instance = Calendar.getInstance();
		Calendar beginTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		float perAmount = 0;
		if(withdraw.getTotalRequest() == 0){
			perAmount = withdraw.getWithdrawMoney();
		} else {
			perAmount = withdraw.getWithdrawMoney() / withdraw.getTotalRequest();

		}
		if(withdrawType.getMaxWithdrawAmountPerCount() > 0 && withdrawType.getMaxWithdrawAmountPerCount() < perAmount){
			logger.error("本次批付金额:" + perAmount + "大于批付类型[" + withdrawType.getWithdrawTypeId() + "]设置的单次批付最大金额:" + withdrawType.getMaxWithdrawAmountPerCount());
			return EisError.moneyRangeError.id;
		}
		if(withdrawType.getMinWithdrawAmountPerCount() > 0 && withdrawType.getMinWithdrawAmountPerCount() > withdraw.getWithdrawMoney()){
			logger.error("本次批付金额:" + withdraw.getWithdrawMoney() + "小于批付类型[" + withdrawType.getWithdrawTypeId() + "]设置的单次批付最小金额:" + withdrawType.getMinWithdrawAmountPerCount());
			return EisError.moneyRangeError.id;
		}

		if(withdrawType.getWithdrawBeginTime() != null){
			//只比较设定时间的时分秒			
			beginTime.setTime( withdrawType.getWithdrawBeginTime());
			beginTime.set(Calendar.YEAR, instance.get(Calendar.YEAR));
			beginTime.set(Calendar.MONTH, instance.get(Calendar.MONTH));
			beginTime.set(Calendar.DAY_OF_MONTH, instance.get(Calendar.DAY_OF_MONTH));
			logger.error("批付类型[" + withdrawType.getWithdrawTypeId() + "]设置的批付起始时间是:" + sdf.format(withdrawType.getWithdrawBeginTime()));
			if(beginTime.after(instance)){
				logger.error("批付类型[" + withdrawType.getWithdrawTypeId() + "]设置的批付起始时间是:" + sdf.format(withdrawType.getWithdrawBeginTime()) + ",现在不能批付");
				return EisError.timePeriodError.id;
			}
		}
		if(withdrawType.getWithdrawEndTime() != null){
			endTime.setTime( withdrawType.getWithdrawEndTime());
			endTime.set(Calendar.YEAR, instance.get(Calendar.YEAR));
			endTime.set(Calendar.MONTH, instance.get(Calendar.MONTH));
			endTime.set(Calendar.DAY_OF_MONTH, instance.get(Calendar.DAY_OF_MONTH));
			logger.error("批付类型[" + withdrawType.getWithdrawTypeId() + "]设置的批付结束时间是:" + sdf.format(withdrawType.getWithdrawEndTime()));
			if(endTime.before(instance)){
				logger.error("批付类型[" + withdrawType.getWithdrawTypeId() + "]设置的批付结束时间是:" + sdf.format(withdrawType.getWithdrawEndTime()) + ",现在不能批付");
				return EisError.timePeriodError.id;
			}

		}
		WithdrawCriteria withdrawCriteria = new WithdrawCriteria(withdraw.getOwnerId());
		withdrawCriteria.setBeginTimeBegin(beginTime.getTime());
		withdrawCriteria.setBeginTimeEnd(endTime.getTime());
		withdrawCriteria.setUuid(withdraw.getUuid());

		if(withdrawType.getMaxWithdrawCountInPeriod() > 0){
			//检查用户在一个批付周期内的批付次数

			int existCount = count(withdrawCriteria);
			logger.debug("用户[" + withdrawCriteria.getUuid() + "]在批付周期[" + sdf.format(withdrawCriteria.getBeginTimeBegin()) + "=>" + sdf.format(withdrawCriteria.getBeginTimeEnd() ) + "]内的申请批付次数是:" + existCount + ",周期内允许批付次数上限是:" + withdrawType.getMaxWithdrawCountInPeriod());
			if(existCount >= withdrawType.getMaxWithdrawCountInPeriod()){
				logger.info("用户[" + withdrawCriteria.getUuid() + "]在批付周期[" + sdf.format(withdrawCriteria.getBeginTimeBegin()) + "=>" + sdf.format(withdrawCriteria.getBeginTimeEnd() ) + "]内的申请批付次数是:" + existCount + ",已超过周期内允许批付次数上限:" + withdrawType.getMaxWithdrawCountInPeriod() );
				return EisError.COUNT_LIMIT_EXCEED.id;
			}
		}
		if(withdrawType.getMaxWithdrawAmountInPeriod() > 0){
			//检查用户在一个批付周期内的批付金额

			List<Withdraw> existWithdraw = list(withdrawCriteria);
			if(existWithdraw == null || existWithdraw.size() < 0){
				logger.debug("用户[" + withdrawCriteria.getUuid() + "]在批付周期[" + sdf.format(withdrawCriteria.getBeginTimeBegin()) + "=>" + sdf.format(withdrawCriteria.getBeginTimeEnd() ) + "]内的没有申请批付");
			} else {
				float totalMoney = 0f;
				for(Withdraw w2 : existWithdraw){
					totalMoney += w2.getWithdrawMoney();
				}
				logger.info("用户[" + withdrawCriteria.getUuid() + "]在批付周期[" + sdf.format(withdrawCriteria.getBeginTimeBegin()) + "=>" + sdf.format(withdrawCriteria.getBeginTimeEnd() ) + "]内的申请批付总金额是:" + totalMoney + ",周期内允许批付金额上限:" + withdrawType.getMaxWithdrawAmountInPeriod() );


				if(totalMoney >= withdrawType.getMaxWithdrawAmountInPeriod()){
					logger.info("用户[" + withdrawCriteria.getUuid() + "]在批付周期[" + sdf.format(withdrawCriteria.getBeginTimeBegin()) + "=>" + sdf.format(withdrawCriteria.getBeginTimeEnd() ) + "]内的申请批付总金额是:" + totalMoney + ",已超过周期内允许批付金额上限:" + withdrawType.getMaxWithdrawAmountInPeriod() );
					return EisError.moneyRangeError.id;
				}
			}
		}


		return OperateResult.success.getId();
	}

	@Override
	public List<WithdrawMethod> getWithdrawMethod(Withdraw withdraw, User partner){

		List<WithdrawMethod> returnWithdrawMethodList = new ArrayList<WithdrawMethod>();
		if(withdraw.getWithdrawMethodId() > 0){
			logger.debug("批付订单[" + withdraw.getTransactionId() + "]指定了withdraMethodId:" + withdraw.getWithdrawMethodId() + ",返回该支付方式");
			returnWithdrawMethodList.add(withdrawMethodService.select(withdraw.getWithdrawMethodId()));
			return returnWithdrawMethodList;
		}

		boolean internal = withdraw.getBooleanExtraValue("internal");

		WithdrawMethodCriteria withdrawMethodCriteria = new WithdrawMethodCriteria(withdraw.getOwnerId());
		withdrawMethodCriteria.setWithdrawTypeId(withdraw.getWithdrawTypeId());

		if(internal){
			withdrawMethodCriteria.setCurrentStatus(BasicStatus.hidden.getId());
		} else {
			withdrawMethodCriteria.setCurrentStatus(BasicStatus.normal.getId());
		}


		int[] excludePayMethods = null;
		String exclude = withdraw.getExtraValue("excludeWithdrawMethod");
		if(StringUtils.isNotBlank(exclude)){
			String[] data =  exclude.split(",");
			excludePayMethods = new int[data.length];
			for(int i = 0; i < data.length; i++){
				excludePayMethods[i] = Integer.parseInt(data[i].trim());
			}
		}
		List<WithdrawMethod> withdrawMethodList = withdrawMethodService.list(withdrawMethodCriteria);
		if(withdrawMethodList == null || withdrawMethodList.size() < 1){
			logger.error("根据批付类型:" + withdraw.getWithdrawTypeId() + "找不到任何批付方式");
			return returnWithdrawMethodList;
		}

		//该商户是否只允许从绑定的通道批付
		boolean limitWithdrawToChannelOnly = partner.getBooleanExtraValue("limitWithdrawToChannelOnly");

		//该商户是否有优先走的批付通道，这个channel不是withdrawMethodId，是channelId
		int perferChannelId = (int)partner.getLongExtraValue("perferChannelId");



		/*WithdrawMethod perferWithdrawMethod = null;
		if(perferChannelId > 0){
			//如果商户有优先选择的通道，查找该通道是否可用
			for(WithdrawMethod withdrawMethod : withdrawMethodList){
				if(withdrawMethod.getChannelId() == perferChannelId){
					logger.debug("找到商户[" + partner.getUuid() + "]配置中优先的channelId=" + perferChannelId + "的批付方法:" + perferWithdrawMethod);
					returnWithdrawMethodList.add(perferWithdrawMethod);
					break;
				}
			}
		}*/

		for(WithdrawMethod withdrawMethod : withdrawMethodList){
			if(excludePayMethods != null && excludePayMethods.length > 0){
				boolean shouldExclude =false;
				for(int ex2 : excludePayMethods){
					if(withdrawMethod.getWithdrawMethodId() == ex2){
						logger.debug("支付方式[" + withdrawMethod + "]被参数excludePayMethod设置为跳过");
						shouldExclude = true;
						break;
					}
				}
				if(shouldExclude){
					continue;
				}
			}
			if(perferChannelId > 0){
				//如果该用户被限制为只能走指定通道，那么跳过不是属于该指定通道的那些通道
				if(limitWithdrawToChannelOnly && withdrawMethod.getChannelId() != perferChannelId){
					continue;
				} else {
					returnWithdrawMethodList.add(withdrawMethod);
				}
			} else {
				//没有通道限制
				returnWithdrawMethodList.add(withdrawMethod);
			}

		}		
		logger.info("筛选后可用的批付方法有" + returnWithdrawMethodList.size() + "种，用户[" + withdraw.getUuid() + "]优选channel=" + perferChannelId + ",是否只能使用优选通道:" + limitWithdrawToChannelOnly) ;
		if(returnWithdrawMethodList.size() < 1){
			logger.warn("用户[" + withdraw.getUuid() + "]优选channel=" + perferChannelId + ",是否只能使用优选通道:" + limitWithdrawToChannelOnly + "，筛选后没有可用的批付方法") ;
			return returnWithdrawMethodList;
		}
		//如果有优选channleId，那么应当放在前面
		if(perferChannelId > 0){
			List<WithdrawMethod> perferWithdrawMethodList = new ArrayList<WithdrawMethod>();
			List<WithdrawMethod> otherWithdrawMethodList = new ArrayList<WithdrawMethod>();
			//把与指定的perferChannelId一样的批付通道放到一个列表，把其他的放到另一个列表，二者分开排序
			for(WithdrawMethod payMethod: returnWithdrawMethodList){
				if(payMethod.getChannelId() == perferChannelId){
					perferWithdrawMethodList.add(payMethod);
				} else {
					otherWithdrawMethodList.add(payMethod);
				}
			}
			returnWithdrawMethodList.clear();
			if(perferWithdrawMethodList.size() > 0){
				Collections.sort(perferWithdrawMethodList, new Comparator<WithdrawMethod>(){

					@Override
					public int compare(WithdrawMethod p1, WithdrawMethod p2) {
						if(p1.getWeight() > p2.getWeight()){
							return -1;
						} else {
							return 1;
						} 
					}});
				returnWithdrawMethodList.addAll(perferWithdrawMethodList);

			}
			if(otherWithdrawMethodList.size() > 0){

				Collections.sort(otherWithdrawMethodList, new Comparator<WithdrawMethod>(){

					@Override
					public int compare(WithdrawMethod p1, WithdrawMethod p2) {
						if(p1.getWeight() > p2.getWeight()){
							return -1;
						} else {
							return 1;
						} 
					}});
				returnWithdrawMethodList.addAll(otherWithdrawMethodList);
			}
			return	returnWithdrawMethodList;

		}
		//按优先级排序
		Collections.sort(returnWithdrawMethodList, new Comparator<WithdrawMethod>(){

			@Override
			public int compare(WithdrawMethod p1, WithdrawMethod p2) {
				if(p1.getWeight() > p2.getWeight()){
					return -1;
				} else {
					return 1;
				} 
			}});
		return	returnWithdrawMethodList;




	}
	/**
	 * 人工操作修改订单状态
	 * @param map
	 */
    @Override
    public int updateWithdrawForManualOperate(Map map) {
		return  withdrawDao.updateWithdrawForManualOperate(map);
	}

	@Override
	public int save(Withdraw withdraw) {
		if(withdraw.getCurrentStatus() == 0){
			withdraw.setCurrentStatus(TransactionStatus.newOrder.getId());
		}
		if(withdraw.getBeginTime() == null){
			withdraw.setBeginTime(new Date());
		}
		return withdrawDao.insert(withdraw);
	}

}

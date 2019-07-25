package com.maicard.money.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.ServiceNotFoundException;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.dao.PayDao;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.domain.PayType;
import com.maicard.money.domain.WithdrawType;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.ChannelRobinService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.money.service.WithdrawTypeService;
import com.maicard.product.domain.TransPlan;
import com.maicard.product.service.TransPlanService;
import com.maicard.product.service.TransactionExecutor;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyMemory;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PayCardTypeEnum;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;

@Service
@ProcessMessageObject("pay")
public class PayServiceImpl extends BaseService implements PayService,EisMessageListener {

	@Resource
	private PayDao payDao;

	@Resource
	private ApplicationContextService applicationContextService;	
	

    @Resource
    private ChannelRobinService channelRobinService;

	@Resource
	private TransPlanService transPlanService;
	@Resource
	private ConfigService configService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private MessageService messageService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayMethodService payMethodService;
	@Resource
	private PayTypeService payTypeService;

	@Resource
	private ShareConfigService shareConfigService;

	@Resource
	private WithdrawTypeService withdrawTypeService;

	private boolean handlerPay = false;
	private int MONEY_SHARE_MODE = 0;
	private String messageBusName;
	
	@Value("${MQ_ENABLED}")
	private boolean mqEnabled;

	final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	/**
	 * 未指定任何支付方式时的默认支付方式
	 */
	public static final int DEFULAT_PAY_TYPE = 1;

	@PostConstruct
	public void init(){
		handlerPay = configService.getBooleanValue(DataName.handlerPay.toString(),0);
		messageBusName = configService.getValue(DataName.messageBusSystem.toString(),0);
		MONEY_SHARE_MODE = configService.getIntValue(DataName.MONEY_SHARE_MODE.toString(), 0);
		//transactionCachePolicy = configService.getIntValue(DataName.transactionCachePolicy.toString(), 0);
	}


	@Override
	public int insert(Pay pay) {
		if(pay.getCurrentStatus() == 0){
			pay.setCurrentStatus(TransactionStatus.newOrder.getId());
		}
		if(StringUtils.isBlank(pay.getTransactionId())){
			pay.setTransactionId(globalOrderIdService.generate(TransactionType.pay.getId()));
			logger.info("尝试插入的支付订单没有订单号，产生一个新的订单号:{}", pay.getTransactionId());
		}
		Pay _oldPay = payDao.select(pay.getTransactionId());
		if(_oldPay != null){
			logger.info("支付订单[" + pay.getTransactionId() + "]已存在，停止插入");
			return 0;
		}
		 // 默认值
        if (StringUtils.isEmpty(pay.getPayCardType())) {
            pay.setPayCardType(PayCardTypeEnum.UNKNOWN.getCode());
        }
		int rs = payDao.insert(pay);
		logger.debug("向系统中插入新的支付订单[" + pay + "]，结果:" + rs);
		if(rs != 1){
			return rs;
		}
		messageService.sendJmsDataSyncMessage(null, "payService", "insert", pay);
		return rs;
	}



	public int update(Pay pay) throws Exception {
		int rs = payDao.update(pay);
		logger.debug("更新系统中的支付订单[" + pay + "]，结果:" + rs);
		if(rs != 1){
			return rs;
		}
		messageService.sendJmsDataSyncMessage(null, "payService", "update", pay);
		return rs;
	}

	/*@Transactional
	public int changeMoney(Pay pay){
		logger.debug("开始进入修改金钱过程" + pay.getRealMoney());
		if(pay.getTransactionId() == null || pay.getTransactionId().equals("")){
			logger.error("无订单号");
			return Constants.Error.objectIsNull.getId();
		}
		if(pay.getPayToAccount() < 1){
			logger.error("无入账帐号");
			return Constants.Error.chargeToAccountNotExist.getId();
		}

		//logger.info("插入资金变化记录成功，目标账户[" + moneyChangeLog.getToAccount() + "],资金[" + moneyChangeLog.getMoney() + "],关联对象为[" + moneyChangeLog.getRefObjectTypeId() + "/" + moneyChangeLog.getRefObjectId() + "]" );
		if(pay.getRefObjectId() > 0){
			//向业务充值，则此项工作结束，直接返回
			logger.info("向业务充值，返回");
			return Constants.Error.dataError.getId();

		}
		Money money = new Money();
		money.setUuid(pay.getPayToAccount());
		money.setChargeMoney(pay.getRealMoney());
		User frontUser = frontUserService.select(money.getUuid());
		checkUserPromotion(frontUser, money);
		moneyService.add(money);
		return Constants.Error.billUpdateFailed.getId();


	}*/

	public int delete(String transactionId) {
		int actualRowsAffected = 0;

		Pay _oldPay = payDao.select(transactionId);

		if (_oldPay != null) {
			actualRowsAffected = payDao.delete(transactionId);
		}

		return actualRowsAffected;
	}

	public Pay select(String transactionId) {
		return payDao.select(transactionId);
	}


	public List<Pay> list(PayCriteria payCriteria) {
		List<Pay> payList =  payDao.list(payCriteria);
		if(payList == null){
			return Collections.emptyList();
		} else {
			return payList;
		}
	}

	public List<Pay> listOnPage(PayCriteria payCriteria) {
		if(payCriteria.getStartTimeBegin() == null && payCriteria.getEndTimeBegin() == null){
			//	if(payCriteria.getStartTimeBegin() == null && payCriteria.getStartTime() == null){
			//设置为本月开始
			payCriteria.setStartTimeBegin(DateUtils.truncate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), Calendar.MONTH));

			payCriteria.setStartTime(new SimpleDateFormat(CommonStandard.defaultDateFormat).format(payCriteria.getStartTimeBegin()));


		}

		List<Pay> payList = payDao.listOnPage(payCriteria);
		if(payList == null){
			return Collections.emptyList();
		}
		/*for(int i = 0; i < payList.size(); i++){
			payList.get(i).setIndex(i+1);
			afterFetch(payList.get(i));
		}*/
		return payList;
	}
	public List<Pay> listOnPageByDay(PayCriteria payCriteria) {
		if(payCriteria.getStartTime() == null){
			//设置为本月开始
			try{
				payCriteria.setStartTime(      
						//	new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(
						new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01 00:00:00");

			}catch(Exception e){
				e.printStackTrace();
			}
		}

		List<Pay> payList = payDao.listOnPageByday(payCriteria);
		if(payList == null){
			return null;
		}
		for(int i = 0; i < payList.size(); i++){
			payList.get(i).setIndex(i+1);
			afterFetch(payList.get(i));
		}
		return payList;
	}
	@Override
	public List<Pay> listOnPageByPartner(PayCriteria payCriteria) {
		generateChildCondition(payCriteria);
		List<Pay> payList = payDao.listOnPageByPartner(payCriteria);
		if(payList == null){
			return null;
		}
		for(int i = 0; i < payList.size(); i++){
			afterFetch(payList.get(i));
		}
		return payList;
	}



	public int count(PayCriteria payCriteria) {
		return payDao.count(payCriteria);
	}

	@Override
	public int countByPartner(PayCriteria payCriteria) {
		return payDao.countByPartner(payCriteria);
	}

	@Override
	public EisMessage end(int payMethodId, String resultString) throws Exception{
		PayMethod payMethod = payMethodService.select(payMethodId);
		if(payMethod == null){
			logger.error("找不到指定的支付方式:" + payMethodId);
		}
		String[] payProcessConfig = null;
		if(payMethod.getProcessClass() != null){
			payProcessConfig = payMethod.getProcessClass().split(",");
		} else {
			logger.error("支付方式[" + payMethod.getProcessClass() + "]没有配置处理器");
		}
		String payProcessBeanName = null;
		if(payProcessConfig != null && payProcessConfig.length > 1){
			payProcessBeanName = payProcessConfig[1];
		} else {
			payProcessBeanName = payMethod.getProcessClass();
		}
		PayProcessor payProcessor = applicationContextService.getBeanGeneric(payProcessBeanName);
		if(payProcessor == null){
			throw new ServiceNotFoundException("找不到对应的支付处理器:" + payProcessBeanName);
		}
		Pay pay = payProcessor.onResult(resultString);

		if(pay == null){
			if(logger.isDebugEnabled()){
				logger.debug("支付处理器[" + payProcessBeanName + "]返回的支付对象是空");
			}
			return new EisMessage(EisError.UNKNOWN_ERROR.getId(), "success");
		}
		if(pay.getTransactionId() == null){
			if(logger.isDebugEnabled()){
				logger.debug("支付处理器[" + payMethod.getProcessClass() + "]返回的支付订单是空");
			}
			return new EisMessage(EisError.BILL_NOT_EXIST.getId(), pay.getPayResultMessage());			
		}
		Pay _oldPay = select(pay.getTransactionId());
		if(_oldPay == null){
			if(logger.isDebugEnabled()){
				logger.debug("根据支付订单[" + pay.getTransactionId() + "]找不到存在的支付记录");
			}
			return new EisMessage(EisError.BILL_NOT_EXIST.getId(), null);		
		}
		if(_oldPay.getCurrentStatus() != TransactionStatus.inProcess.getId()){
			if(logger.isDebugEnabled()){
				logger.debug("根据支付订单[" + pay.getTransactionId() + "]找到的支付记录状态不是正在处理中，是[" + _oldPay.getCurrentStatus() + "]，返回字符串为:" + pay.getPayResultMessage());
			}
			EisMessage result = new EisMessage();
			result.setOperateCode(OperateResult.success.getId());
			result.setAttachment(new HashMap<String,Object>());
			result.getAttachment().put("pay", pay);	
			result.setObjectType(ObjectType.pay.toString());
			return result;
		}
		if(pay.getCurrentStatus() == TransactionStatus.inProcess.id){
			logger.debug("支付订单[" + pay.getTransactionId() + "]由支付处理器[" + payProcessor.getDesc() + "]返回了处理中，返回字符串为:" + pay.getPayResultMessage());
			//不做任何处理
		} else {
			
			if(mqEnabled) {
				//消息总线方式，把消息发送到总线，由其他节点完成支付处理
				//先将本地pay订单改为对应状态
				_oldPay.setCurrentStatus(pay.getCurrentStatus());
				if(pay.getCurrentStatus() == TransactionStatus.success.getId()){
					_oldPay.setRealMoney(pay.getRealMoney());
				}
				_oldPay.setSyncFlag(1);
				update(_oldPay);
				
				_oldPay.setSyncFlag(0);
				//将pay发送到消息总线
				if(logger.isDebugEnabled()){
					logger.debug("将支付订单[" + pay.getTransactionId() + "]发送到消息总线，支付状态:" + pay.getCurrentStatus());
				}
				EisMessage m = new EisMessage();
				m.setOperateCode(Operate.close.getId());
				m.setAttachment(new HashMap<String,Object>());
				m.getAttachment().put("pay", pay);	
				m.setObjectType(ObjectType.pay.toString());
				messageService.send(messageBusName, m);
				m = null;
			} else {
				//非总线模式，本节点直接完成支付处理
				this.end(pay);
			}
		}

		EisMessage result = new EisMessage();
		result.setOperateCode(OperateResult.success.getId());
		result.setAttachment(new HashMap<String,Object>());
		result.getAttachment().put("pay", pay);	
		result.setObjectType(ObjectType.pay.toString());
		return result;
	}


	private void generateChildCondition(PayCriteria payCriteria){
		List<User> childrenList = new ArrayList<User>();
		ArrayList<Long> inviteUuid = new ArrayList<Long>();
		ArrayList<Long> inviteUuid2 = new ArrayList<Long>();
		String inviteUuids = "";
		String otherCondition;

		//非系统用户或超级用户
		if(!payCriteria.isSuperMode()){
			//找出该partner所拥有的所有子账户ID
			partnerService.listAllChildren(childrenList, payCriteria.getInviteByUuid());
			for(int i = 0; i < childrenList.size(); i++){
				inviteUuid.add(childrenList.get(i).getUuid());
			}
			//也包含自己发展的用户
			inviteUuid.add(payCriteria.getInviteByUuid());
			//如果条件中指定了子账户，那么检查这些指定的账户是否与合法子账户一致
			if(payCriteria.getUuidRange() != null &&  !payCriteria.getUuidRange().equals("")){

				String[] specialPartnerIds = payCriteria.getUuidRange().split(",");
				if(specialPartnerIds != null && specialPartnerIds.length > 0){
					for(int i = 0; i < specialPartnerIds.length; i++){
						for(int j = 0; j < inviteUuid.size(); j++){
							try{
								if(Integer.parseInt(specialPartnerIds[i]) == inviteUuid.get(j)){
									inviteUuid2.add(inviteUuid.get(j));
									break;
								}
							}catch(Exception e){}
						}
					}

				}
				logger.info("当前查询条件指定了[" + inviteUuid2.size() + "]个子账户[" + payCriteria.getUuidRange() + "]");
			}
			//如果指定了子账户，则只查询（合法）的子账户，否则，查询当前partner及其所拥有的所有子账户
			if(inviteUuid2.size() > 0){
				for(int i = 0; i < inviteUuid2.size(); i++){
					inviteUuids += inviteUuid2.get(i) + ",";
				}
			} else {
				for(int i = 0; i < inviteUuid.size(); i++){
					inviteUuids += inviteUuid.get(i) + ",";
				}
			}
		} else {
			//如果条件中指定了子账户，那么在超级模式下，只查询指定的子账户及其子子账户
			if(payCriteria.getUuidRange() != null &&  !payCriteria.getUuidRange().equals("")){
				String[] specialPartnerIds = payCriteria.getUuidRange().split(",");
				if(specialPartnerIds != null && specialPartnerIds.length > 0){
					for(int i = 0; i < specialPartnerIds.length; i++){
						try{
							inviteUuid2.add(Long.parseLong(specialPartnerIds[i]));
						}catch(Exception e){}
					}

				}
				logger.info("当前查询条件指定了[" + inviteUuid2.size() + "]个子账户[" + payCriteria.getUuidRange() + "]");

				//为每个账户添加其子账户
				for(int  i = 0; i < inviteUuid2.size(); i++){
					partnerService.listAllChildren(childrenList, inviteUuid2.get(i));
				}
				logger.info("当前查询条件指定了[" + inviteUuid2.size() + "]个子账户，所有子账户数量为[" + childrenList.size() + "][" + payCriteria.getUuidRange() + "]");
				for(int i = 0; i < childrenList.size(); i++){
					inviteUuids += childrenList.get(i).getUuid() + ",";

				}
			}
		}

		inviteUuids = inviteUuids.replaceAll(",$", "");
		otherCondition = "b.invite_by_uuid in (" + inviteUuids + ")";

		if(!payCriteria.isSuperMode()){
			logger.info("当前partner[uuid=" + payCriteria.getInviteByUuid() + "]共有 " + childrenList.size() + " 个子账户,查询条件为" + otherCondition );	
			payCriteria.setUuidRange(otherCondition);
		}else {
			if(inviteUuid2 != null && inviteUuid2.size() > 0){
				payCriteria.setUuidRange(otherCondition);
				logger.info("当前为系统用户，但指定了[" + inviteUuid2.size() + "]个查询子账户:" + otherCondition);
			} else {
				logger.info("当前为系统用户，且未指定查询子账户，不考虑当前合作伙伴ID");
			}
		}
		childrenList = null;
		inviteUuid = null;
		inviteUuid2 = null;
		inviteUuids = null;
	}

	private void afterFetch(Pay pay){
		/*

		if(pay.getEndTime() != null && pay.getEndTime().getTime() > pay.getStartTime().getTime()){
			pay.setProcessTime((int)(pay.getEndTime().getTime() - pay.getStartTime().getTime()) /1000);
		}*/

	}


	@Override
	public EisMessage begin(Pay pay){
		return _startPayRemote(pay);

	}

	/*@Override
	public EisMessage createOrder(Pay pay){
		return _createOrderRemote(pay);
	}*/
	 /*
	@Override
	public PayMethod getPayMethod(Pay pay, User partner){

		if(pay.getPayMethodId() > 0){
			logger.debug("支付订单[" + pay.getTransactionId() + "]指定了payMethodId:" + pay.getPayMethodId() + ",返回该支付方式");
			return payMethodService.select(pay.getPayMethodId());
		}


		
		
       
        return channelRobinService.getPayMethod(pay, partner);

       
		int rand = RandomUtils.nextInt(100);
		logger.debug("当前比例随机数是:" + rand);
		//按照优先级、占比进行排序，把优先级高的放前面，相同优先级的把占比最小的放在前面，这样可以通过rand处理比例
		Collections.sort(payMethodList2, new Comparator<PayMethod>(){

			@Override
			public int compare(PayMethod p1, PayMethod p2) {
				if(p1.getWeight() > p2.getWeight()){
					return -1;
				} else if(p1.getWeight() < p2.getWeight()){
					return 1;
				} else {
					if(p1.getPercent() > p2.getPercent()){
						return 1;
					}
					return -1;
				}
			}});

		//如果有绑定channleId，那么应当放在前面
		if(perferChannelId > 0){
			PayMethod firstPayMethod = null;
			for(PayMethod payMethod: payMethodList2){
				if(payMethod.getPayChannelId() == perferChannelId){
					if(firstPayMethod == null){
						firstPayMethod = payMethod;
					}
					if(rand <= payMethod.getPercent() || payMethod.getPercent() == 100){
						logger.debug("为支付订单:" + pay.getTransactionId() + ",根据[支付类型:" + pay.getPayTypeId() + ",contextType:" + pay.getContextType() + "]返回符合绑定channelId=" + perferChannelId + "的支付方式:" + payMethod + ",当前随机数是:" + rand + ",该支付方式的占比:" + payMethod.getPercent());
						return payMethod;
					}
				}
			}
			if(firstPayMethod != null){
				logger.debug("为支付订单:" + pay.getTransactionId() + ",根据[支付类型:" + pay.getPayTypeId() + ",contextType:" + pay.getContextType() + "]返回符合绑定channelId=" + perferChannelId + "的第一个支付方式:" + firstPayMethod + ",当前随机数是:" + rand + ",该支付方式的占比:" + firstPayMethod.getPercent());
				return firstPayMethod;
			}
		}
		//第一个符合最高优先级的方式
		PayMethod firstHighestPayMethod = null;
		for(PayMethod payMethod: payMethodList2){
			if(payMethod.getWeight() < highestWeight){
				logger.debug("支付方式:" + payMethod.getPayMethodId() + "的优先级:" + payMethod.getWeight() + "比当前最高优先级:" + highestWeight + "低，忽略");
				continue;
			}
			if(firstHighestPayMethod == null){
				firstHighestPayMethod = payMethod;
			}
			if(rand <= payMethod.getPercent() || payMethod.getPercent() == 100){
				logger.debug("为支付订单:" + pay.getTransactionId() + ",根据[支付类型:" + pay.getPayTypeId() + ",contextType:" + pay.getContextType() + "]返回支付方式:" + payMethod + ",当前随机数是:" + rand + ",支付方式占比是:" + payMethod.getPercent());
				return payMethod;
			}
		}
		if(firstHighestPayMethod != null){
			logger.debug("为支付订单:" + pay.getTransactionId() + "根据[支付类型:" + pay.getPayTypeId() + ",contextType:" + pay.getContextType() + "]返回符合最高优先级:" + highestWeight + "的第一个支付方式:" + firstHighestPayMethod);
			return firstHighestPayMethod;
		}

		//
		logger.debug("为支付订单:" + pay.getTransactionId() + "根据[支付类型:" + pay.getPayTypeId() + ",contextType:" + pay.getContextType() + "]未找到匹配的支付方式，返回第一个支付方式:" + payMethodList.get(0));
		return payMethodList.get(0);
	}
*/


	/*private EisMessage _createOrderRemote(Pay pay) {
		PayMethod payMethod = getPayMethod(pay, partnerService.select(pay.getPayFromAccount()));
		if(payMethod == null){
			return new EisMessage(EisError.payMethodIsNull.getId(), "找不到支付方式payMethod");
		}
		PayType payType = payTypeService.select(pay.getPayTypeId());
		if(payType == null){
			return new EisMessage(EisError.payTypeIsNull.getId(), "找不到指定的支付类型payType");			
		}
		logger.debug("当前支付方式的费率是:" + payType.getPublicRate());
		//logger.info("当前支付方式的费率是:" + currentPayTypeMethodRelation.getRate());
		pay.setRate(payType.getPublicRate());
		pay.setPayMethodId(payMethod.getPayMethodId());
		pay.setCurrentStatus(TransactionStatus.newOrder.getId());
		logger.info("为用户选择的支付类型[" + pay.getPayTypeId() + "]自动选择支付方法[" + payMethod.getPayMethodId() + "]");

		//全局交易ID
		pay.setTransactionId(globalOrderIdService.generate(TransactionType.pay.getId()));

		//将pay发送到消息总线
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.create.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("pay", pay);	
		m.setObjectType(ObjectType.pay.toString());
		try{
			messageService.send(messageBusName, m);
			m = null;
		}catch(Exception e){
			logger.error("消息总线异常:" + e.getMessage());
		}
		EisMessage msg = new EisMessage(OperateResult.success.getId(), "订单创建成功");		
		msg.setContent(pay.getTransactionId());
		return msg;
	}*/


	/*
	 * 1、负责检查各项参数
	 * 2、调用相应的支付处理器为用户完成下一步操作
	 * 3、发送支付对象至消息总线，由对应的节点进行处理
	 */
	private EisMessage _startPayRemote(Pay pay){
		if(pay.getTransactionId() == null){
			pay.setTransactionId(globalOrderIdService.generate(TransactionType.pay.getId()));
		}

		User payUser = null;
		if(pay.getPayFromAccountType() == UserTypes.partner.getId()){
			payUser = partnerService.select(pay.getPayFromAccount());
			if(payUser != null){
				//商户模式，放入商户的一些配置参数
				if(payUser.getBooleanExtraValue(DataName.forceReplacePayNameByProductName.toString())){
					pay.setExtraValue(DataName.forceReplacePayNameByProductName.toString(), "true");
				}
				if(payUser.getBooleanExtraValue(DataName.useOrderIdSuffixToPayName.toString())){
					pay.setExtraValue(DataName.useOrderIdSuffixToPayName.toString(), "true");
				}
			}
		} else {
			payUser = frontUserService.select(pay.getPayFromAccount());
		}
		
		if(pay.getPayTypeId() < 1){
			pay.setPayTypeId(DEFULAT_PAY_TYPE);
		}
		PayType payType = payTypeService.select(pay.getPayTypeId());
		if(payType == null){
			logger.error("找不到指定的支付方式:" + pay.getPayTypeId());
			return new EisMessage(EisError.payTypeIsNull.getId(), "找不到指定的支付类型");			
		}
		PayMethod payMethod = pay.getPayMethod();
		//如果没有支付方法，把支付方式的实例放入供后续使用
		if(payMethod == null) {
			payMethod = channelRobinService.getPayMethod(pay, payUser);
			if(payMethod == null){
				logger.error("处理支付订单[" + pay.getTransactionId() + "]时找不到指定的支付方式payMethod");
				return new EisMessage(EisError.payMethodIsNull.getId(), "找不到指定的支付方式");
			}
			pay.setPayMethod(payMethod.clone());
		}
		pay.setExtraValue("payTypeName", payType.getName());
		if (StringUtils.isBlank(pay.getExtraValue("openId"))) {
			if (payUser!=null && payUser.getAuthKey()!=null) {
				pay.setExtraValue("openId", payUser.getAuthKey());
			}
		}
		logger.debug("支付订单[" + pay.getTransactionId() + "]当前支付方式的费率是:" + payType.getPublicRate());
		//logger.info("当前支付方式的费率是:" + currentPayTypeMethodRelation.getRate());
		pay.setRate(payType.getPublicRate());
		pay.setPayMethodId(payMethod.getPayMethodId());
		pay.setCurrentStatus(TransactionStatus.newOrder.getId());
		if(pay.getNotifyUrl() != null){
			pay.setNotifyUrl(pay.getNotifyUrl().replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId())));
		}
		logger.info("处理支付订单[" + pay.getTransactionId() + "]，为用户[uuid=" + pay.getPayFromAccount() + ",用户类型=" + pay.getPayFromAccountType() + "]选择的支付类型[" + pay.getPayTypeId() + "]自动选择支付方法[" + payMethod.getPayMethodId() + "],回调URL:" + pay.getNotifyUrl());

		String payProcessBeanName  = payMethod.getProcessClass();		

		PayProcessor payProcessor = applicationContextService.getBeanGeneric(payProcessBeanName.trim());
		if(payProcessor == null){
			logger.error("处理支付订单[" + pay.getTransactionId() + "]时，找不到指定的支付处理器[" + payProcessBeanName + "]" );
			return new EisMessage(EisError.payProcessorIsNull.getId(), "系统异常");
		}

		long ts = System.currentTimeMillis();
		EisMessage msg = payProcessor.onPay(pay);
		long time = System.currentTimeMillis() - ts;
		logger.info("支付订单:{}提交耗时是{}ms", pay.getTransactionId(), time);
		if(mqEnabled) {

			//将pay发送到消息总线
			EisMessage m = new EisMessage();
			m.setOperateCode(Operate.create.getId());
			m.setAttachment(new HashMap<String,Object>());
			m.getAttachment().put("pay", pay);	
			m.setObjectType(ObjectType.pay.toString());
			logger.debug("将支付订单[" + pay + "]发送到消息总线");
			try{
				messageService.send(messageBusName, m);
				m = null;
			}catch(Exception e){
				logger.error("消息总线异常:" + e.getMessage());
			}
		} else {
			this._createPay(pay);

		}
		//	pay.setCurrentStatus(oldStatus);


		return msg;
	}

	@Override
	public PayProcessor getProcessor(Pay pay){
		Assert.notNull(pay,"尝试获取处理器的Pay对象不能为空");

		int payMethodId = 0;
		PayMethod payMethod =  null;
		if(pay.getPayMethodId() > 0){
			payMethodId = pay.getPayMethodId();
			payMethod = payMethodService.select(pay.getPayMethodId());
		} else {
			payMethod = channelRobinService.getPayMethod(pay, partnerService.select(pay.getPayFromAccount()));			
		}
		if(payMethod == null){
			logger.error("找不到指定的支付类型:" + payMethodId);
			return null;
		}

		String	payProcessBeanName = payMethod.getProcessClass();

		PayProcessor payProcessor = applicationContextService.getBeanGeneric(payProcessBeanName.trim());

		if(payProcessor == null){
			logger.error("找不到指定的支付处理器[" + payProcessBeanName + "]" );
		}
		return payProcessor;
	}

	@Override
	public void onMessage(EisMessage eisMessage) {

		if(handlerPay){

			logger.debug("后台支付服务收到消息");
			if(eisMessage == null){
				logger.error("得到的消息是空");
				return;
			}
			if(eisMessage.getObjectType() == null || !eisMessage.getObjectType().equals(ObjectType.pay.toString())){
				eisMessage = null;
				return;
			}
			if(eisMessage.getAttachment() == null){
				logger.debug("消息中没有附件");
				eisMessage = null;
				return;
			}
			Pay pay = null;
			Object object = eisMessage.getAttachment().get("pay");
			if(object instanceof Pay){
				pay = (Pay)object;
			} else if(object instanceof LinkedHashMap){
				ObjectMapper om = JsonUtils.getInstance();
				String textData = null;
				try{
					textData = om.writeValueAsString(object);
					pay = om.readValue(textData, Pay.class);
				}catch(Exception e){}
			}
			if(pay == null){
				logger.debug("消息中没有找到需要的对象pay");
				eisMessage = null;
				return;
			}
			if(logger.isDebugEnabled()){
				logger.debug("消息指定的操作是[" + eisMessage.getOperateCode() + "/" + Operate.unknown.findById(eisMessage.getOperateCode()).getName() + ",syncFlag=" + pay.getSyncFlag() + "]");
			}
			EisMessage replyMessage = null;
			if(eisMessage.getOperateCode() == Operate.create.getId()){
				this._createPay(pay);
				
			}
			if(eisMessage.getOperateCode() == Operate.close.getId()){
				try{
					replyMessage = this.end(pay);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(eisMessage.isNeedReply()){
				messageService.reply(messageBusName, eisMessage.getMessageId(), eisMessage.getReplyMessageId(), replyMessage);
			}
			pay = null;
		}
		eisMessage = null;

	}

	private void _createPay(Pay pay) {
		if(pay.getCurrentStatus() == Operate.jump.getId()){
			pay.setCurrentStatus(TransactionStatus.inProcess.getId());
		}

		int rs = insert(pay);
		if(rs == 1){
			if(logger.isDebugEnabled()){
				logger.debug("支付订单[" + pay.getTransactionId() + "]创建成功，发送支付订单同步请求[payService.insert(" + pay + ")],syncFlag=" + pay.getSyncFlag());
			}
			pay.setSyncFlag(0);
			messageService.sendJmsDataSyncMessage(messageBusName, "payService", "insert", pay);
		} else{
			logger.error("支付订单[" + pay.getTransactionId() + "]创建失败，返回值:" + rs);
		}
		if(pay.getCurrentStatus() == TransactionStatus.success.id){
			//订单直接成功，调用成功后处理逻辑
			try {
				this._postPay(pay, pay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}


	@Override
	//本地更新数据
	public EisMessage end(Pay pay) throws Exception{
		if(!handlerPay){
			if(mqEnabled) {
			logger.info("1当前节点不负责处理支付订单，消息服务器启用:{}也没有禁用，不处理订单:{}", mqEnabled, pay.getTransactionId());
			return null;
			}
		}
		Pay _oldPay = select(pay.getTransactionId());
		boolean directInsert = false;
		if(_oldPay == null){
			logger.error("数据库中找不到支付订单[" + pay.getTransactionId() + ",仍然按照结束订单处理,先插入该订单");
			insert(pay);
			directInsert = true;
			_oldPay = pay;
			//return new EisMessage(EisError.billNotExist.getId(), pay.getPayResultMessage());			
		} 
		if(_oldPay.getBalance() > 0){
			logger.error("支付订单[" + pay.getTransactionId() + "已经进行了支付成功处理(balance > 0)，不再处理");
			return null;
		}


		int payUserTypeId = pay.getPayFromAccountType();
		logger.debug("支付订单[" + pay.getTransactionId() + "]的付款人[" + pay.getPayFromAccount() + "]类型是:" + payUserTypeId);

		if(!directInsert || (payUserTypeId != UserTypes.partner.getId())){
			if(_oldPay.getCurrentStatus() == pay.getCurrentStatus()){
				logger.debug("支付订单:{}状态未改变:{}", pay.getTransactionId(), pay.getCurrentStatus());
				return null;//FIXME
			}
		}
		_oldPay.setRealMoney(pay.getRealMoney());
		_oldPay.setOutOrderId(pay.getOutOrderId());
		_oldPay.setEndTime(new Date());
		logger.debug("尝试结束的支付订单[" + pay.getTransactionId() + "]请求结束状态是:" + pay.getCurrentStatus() + ",系统中旧的状态是:" + _oldPay.getCurrentStatus() + ",是否刚即时新增:" + directInsert);
		//只有已存在的支付的状态是处理中时，或者是支付订单的付款人是商户时，才判断新传入的pay对象状态是否是成功
		if( directInsert || (payUserTypeId == UserTypes.partner.getId() && pay.getCurrentStatus() == TransactionStatus.success.id) || (_oldPay.getCurrentStatus() == TransactionStatus.inProcess.getId() && pay.getCurrentStatus() == TransactionStatus.success.getId())){// && changeMoney(pay) == Constants.OperateResult.success.getId()){
			return _postPay(pay,_oldPay);

		} else {	
			_oldPay.setCurrentStatus(pay.getCurrentStatus());

			if(update(_oldPay)  <= 0){
				logger.error("无法更新支付订单[" + _oldPay.getTransactionId() + "].");
				return new EisMessage(EisError.BILL_UPDATE_FAIL.getId(), pay.getPayResultMessage());
			}
			messageService.sendJmsDataSyncMessage(messageBusName, "payService", "update", _oldPay);
			logger.debug("支付订单[" + _oldPay.getTransactionId() + "]已更新，并发送同步请求[payService.update(item[" + pay.getTransactionId() + "]");

			return new EisMessage(OperateResult.failed.getId(), pay.getPayResultMessage());
		}



	}

	/**
	 * 确认支付成功后的业务逻辑
	 * 包括由异步通知返回的支付成功close消息，和内部卡券直接成功create消息
	 * 当一次性就成功时，即create消息，那么pay和_oldPay应当是同一个对象
	 * @param pay
	 * @param _oldPay
	 * @return
	 * @throws Exception
	 */
	private EisMessage _postPay(Pay pay, Pay _oldPay) throws Exception {
		if(_oldPay.getBalance() > 0){
			logger.error("尝试执行成功后处理操作的支付订单:" + _oldPay.getTransactionId() + "已经有成功后账户余额balance=" + _oldPay.getBalance() + "，不再进行成功后处理");
			return new EisMessage(EisError.BILL_ALREADY_EXIST.getId(), "已有balance的支付订单不能再次进行成功后处理");
		}
		Money money = new Money(_oldPay.getOwnerId());
		if(_oldPay.getPayToAccount() > 0) {
			money.setUuid(_oldPay.getPayToAccount());
		} else {
			money.setUuid(_oldPay.getPayFromAccount());
		}
		float plusMoney = 0f;
		float rate = 0f;
		
		


		/* 
		 * 检查系统是否配置了资金分成
		 * 如果没有分成配置，那么将付款金额作为chargeMoney
		 * 如果是分成配置，那么付款金额将计算分成比例后放入incomingMoney
		 */

		boolean moneyShared = false;
		Money partnerMoney = null;
		
		long shareConfigId = 0;

		if(MONEY_SHARE_MODE > 0){
			
			//如何计算分成由各自的shareConfigService实现
			ShareConfig shareConfig = shareConfigService.calculateShare(pay, null);
			
				

				
			
			if(shareConfig != null){
				shareConfigId = shareConfig.getShareConfigId();
				pay.setExtraValue("shareConfigId", String.valueOf(shareConfig.getShareConfigId()));
				rate = shareConfig.getSharePercent();
				
				float commission = 0f;
				if(rate > 1){
					logger.info("分成配置[" + shareConfig + "]的分成比例大于，异常的分成比例");
					return new EisMessage(EisError.moneyRangeError.id,"分成比例异常");
				} else {
					//先使用四舍五入计算手续费，否则如果先计算了真实收入，可能会因为真实收入四舍五入来减少了手续费
					commission = (float)NumericUtils.round(pay.getRealMoney() * (1-rate));
					plusMoney = pay.getRealMoney() - commission;
					//plusMoney = (float)NumericUtils.round(pay.getRealMoney() * rate);
				}

				//计算扣除的手续费
				//float commission = pay.getRealMoney() - plusMoney;
				pay.setCommission(commission);

				moneyShared = true;
				partnerMoney = new Money(shareConfig.getShareUuid(), pay.getOwnerId());
				logger.info("为支付交易[" + pay.getTransactionId() + "]根据条件[objectType=pay,shareUuid=" + shareConfig.getShareUuid() + ",currentStatus=" + BasicStatus.normal.getId() + "]得到的特定配置shareConfig是" + shareConfig.getShareConfigId() + ",默认配置:" + shareConfig.isDefaultConfig() + ",分成比例是:" + rate + ",实际分成金额是:" + plusMoney);
				if(MONEY_SHARE_MODE == ShareConfigCriteria.MONEY_SHARE_MODE_TO_CHANNEL){
					//C端分成给商户
					//给上级分润到多少级
					int moneyShareUpLevel = (int)shareConfig.getLongExtraValue(DataName.MONEY_SHARE_UP_LEVEL.toString());
					long upLevelUuid = shareConfig.getShareUuid();
					float upLevelTotalSharedMoney = 0;
					if(moneyShareUpLevel > 0){
						float totalSharePercent = 0;
						for(int i = 1; i <= moneyShareUpLevel; i++){
							float shareForUpLevelRate = shareConfig.getFloatExtraValue(DataName.MONEY_SHARE_UP_LEVEL.toString() + "_" + i);
							if(totalSharePercent + shareForUpLevelRate > rate){
								logger.error("多级分润级别[" + i + "]的分成比例:" + shareForUpLevelRate + "加上已分配比例:" + totalSharePercent + ",已超过可分配最大比例:" + rate);
								break;
							}

							User partner = partnerService.select(upLevelUuid);
							if(partner == null){
								logger.error("在进行第" + i + "级分润时找不到指定的用户:" + upLevelUuid);
								break;
							} 
							if(partner.getParentUuid() == 0){
								logger.info("在进行第" + i + "级分润时，该用户:" + upLevelUuid + "已经没有上级商户");
								break;
							}
							upLevelUuid = partner.getParentUuid();
							Money upMoney = new Money(upLevelUuid, pay.getOwnerId());
							if(shareForUpLevelRate > 1){
								upMoney.setIncomingMoney(shareForUpLevelRate);
							} else {
								upMoney.setIncomingMoney(pay.getRealMoney() * shareForUpLevelRate);
							}
							upLevelTotalSharedMoney += upMoney.getIncomingMoney();
							upMoney.setMemo(MoneyMemory.支付分成收入.toString());
							logger.info("进行第" + i + "级分润,为分润用户[" + upLevelUuid + "]分润:" + upMoney.getIncomingMoney() + ",上级总分润:" + upLevelTotalSharedMoney);
							moneyService.plus(upMoney);
						}
					}

					partnerMoney.setIncomingMoney(plusMoney - upLevelTotalSharedMoney);
					partnerMoney.setMemo(MoneyMemory.支付分成收入.toString());

					logger.info("交易订单[" + pay.getTransactionId() + "]共进行" + moneyShareUpLevel + "级分润,上级总分润:" + upLevelTotalSharedMoney + ",当前经销商分成:" + partnerMoney.getIncomingMoney());


				} else {
					//	pay.setShareConfigId(shareConfig.getShareConfigId());	
					money.setIncomingMoney(plusMoney);
					//使用money中的transitMoney作为当前可提现金额
				}

				int withdrawTypeId = 0;
				User partner = null;
				if(pay.getPayFromAccountType() == UserTypes.partner.getId()){
					partner = partnerService.select(pay.getPayFromAccount());
					if(partner == null){
						logger.warn("找不到支付交易:" + pay.getTransactionId() + "对应的商户:" + pay.getPayFromAccount());
					} else {
						withdrawTypeId = (int)partner.getLongExtraValue(DataName.withdrawType.toString());
					}
				}
				if(withdrawTypeId > 0){
					WithdrawType withdrawType = withdrawTypeService.select(withdrawTypeId);
					if(withdrawType == null){
						logger.info("支付订单:" + pay.getTransactionId() + "找不到对应商户:" + partner.getUuid() + "的提现类型配置withdrawType");
					} else {
						//提现周期

						String arraivePeriod = withdrawType.getArrivePeriod();
						if(StringUtils.isBlank(arraivePeriod)){
							arraivePeriod = WithdrawType.DEFAULT_WITHDRAW_ARRIVE_PERIOD;
							logger.info("分成配置[" + shareConfig.getShareConfigId() + "]提现的提现到账期是空，使用默认到账周期:" + arraivePeriod);
						}
						logger.info("分成配置[" + shareConfig.getShareConfigId() + "]提现的提现到账期:" + arraivePeriod);
						if(arraivePeriod.equalsIgnoreCase("d0")){
							//T+0，可以提现当天的资金
							if(MONEY_SHARE_MODE == ShareConfigCriteria.MONEY_SHARE_MODE_TO_CHANNEL){
								partnerMoney.setIncomingMoney(plusMoney);
							} else {
								money.setTransitMoney(plusMoney);
							}
						}

					}
				}


			} 
		} 

		money.setMemo(MoneyMemory.支付收入.toString());

		if(!moneyShared || MONEY_SHARE_MODE != ShareConfigCriteria.MONEY_SHARE_MODE_TO_USER){
			rate = 1;
			plusMoney = pay.getRealMoney();
			if(pay.getMoneyTypeId() == MoneyType.coin.getId()){
				money.setCoin(plusMoney);
				logger.info("支付交易[" + pay.getTransactionId() + "]不需要分成，或系统为经销商分成模式，将资金[" + pay.getRealMoney() + "]放入coin资金");
			} else if(pay.getMoneyTypeId() == MoneyType.point.getId()){
				money.setPoint(plusMoney);
				logger.info("支付交易[" + pay.getTransactionId() + "]不需要分成，或系统为经销商分成模式，将资金[" + pay.getRealMoney() + "]放入point资金");
			} else {
				money.setChargeMoney(plusMoney);
				logger.info("支付交易[" + pay.getTransactionId() + "]不需要分成，或系统为经销商分成模式，将资金[" + pay.getRealMoney() + "]放入chargeMoney资金");
			}
		}

		if(pay.getEndTime() == null){
			pay.setEndTime(new Date());
		}





		if(pay.getCurrentStatus() != _oldPay.getCurrentStatus()){
			//在异步模式下更新订单信息，如果直接返回了成功，则不需要
			//XXX 必须以锁定形式更新，才能确保订单不会被重复处理
			_oldPay.setCurrentStatus(pay.getCurrentStatus());
			_oldPay.setLockStatus(TransactionStatus.inProcess.getId());
			_oldPay.setRealMoney(pay.getRealMoney());
			_oldPay.setRate(rate);
			_oldPay.setCommission(pay.getCommission());
			_oldPay.setBalance(pay.getBalance());
			_oldPay.setExtraValue("shareConfigId", String.valueOf(shareConfigId));
			if(update(_oldPay) != 1){
				logger.error("无法更新支付订单[" + _oldPay.getTransactionId() + "].");
				return new EisMessage(EisError.BILL_UPDATE_FAIL.getId(), pay.getPayResultMessage());
			} 

			//更新成功才能加钱
			writeStat(pay);

			if(MONEY_SHARE_MODE == ShareConfigCriteria.MONEY_SHARE_MODE_TO_CHANNEL){
				if(moneyShared){
					if(partnerMoney == null){
						logger.error("当前分成模式是2，但是parnterMoney为空");
					} else {
						moneyService.plus(partnerMoney);
						logger.info("为账户[" + money.getUuid() + "]的渠道[" + partnerMoney.getUuid() + "]增加收入资金,付款面值：" + pay.getRealMoney() + "，增加后渠道资金账户余额:" + partnerMoney.getIncomingMoney());
					}
				}
			} 
			moneyService.plus(money);
			if(moneyShared && MONEY_SHARE_MODE == ShareConfigCriteria.MONEY_SHARE_MODE_TO_USER){
				logger.info("为账户[" + money.getUuid() + "]增加收入资金,付款面值：" + pay.getRealMoney() + "，实际增加" + plusMoney + "，增加后账户收入资金inComingMoney余额:" + money.getIncomingMoney());
				_oldPay.setBalance(money.getIncomingMoney());
			} else {
				logger.info("为账户[" + money.getUuid() + "]增加充值资金,付款面值：" + pay.getRealMoney() + "，实际增加" + plusMoney + "，增加后账户充值资金chargeMoney余额:" + money.getChargeMoney());
				_oldPay.setBalance(money.getChargeMoney());
			}
			//无条件更新一遍
			_oldPay.setLockStatus(0);
			_oldPay.setSyncFlag(0);
			update(_oldPay);
			
			if(_oldPay.getRefBuyTransactionId() != null){
				//有对应的购买订单，发送请求完成此交易
				logger.debug("支付订单[" + _oldPay.getTransactionId() + "]有对应的购买订单[" + _oldPay.getRefBuyTransactionId() + "]，尝试继续购买交易");
				TransPlan tp = transPlanService.select(0);

				TransactionExecutor transactionExecutor = applicationContextService.getBeanGeneric(tp.getProcessClass());

				if(transactionExecutor == null){
					logger.error("找不到指定的交易处理器:" + tp.getProcessClass());
					return new EisMessage(EisError.transactionProcessorNotFound.getId(), "找不到指定的交易处理器");
				}
				if(logger.isDebugEnabled())logger.debug("尝试由交易处理器["  + transactionExecutor.toString() + "]处理支付交易[orderId=" + pay.getTransactionId() + "].");

				try {
					transactionExecutor.begin(_oldPay);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				//如果没有对应的购买订单，那么才发送资金同步请求
				messageService.sendJmsDataSyncMessage(messageBusName, "moneyService", "plusLocal", money);
			}
			//update本身已经有这个步骤
			//messageService.sendJmsDataSyncMessage(messageBusName, "payService", "update", _oldPay);
			//logger.debug("支付订单[" + _oldPay.getTransactionId() + "]已更新，并发送同步请求[payService.update(" + pay.getTransactionId() + ")");

		}

		


		return new EisMessage(OperateResult.success.getId(), pay.getPayResultMessage());
	}


	private void writeStat(Pay pay) {
		//放入当前支付渠道这一小时的成功金额
		String hour = new SimpleDateFormat("HH").format(new Date());
		String key = "PayMethod#SuccessMoney#" + hour + "#" + pay.getPayMethodId();
		int setMoney = new Float(pay.getRealMoney()).intValue();
		centerDataService.increaseBy(key, setMoney, setMoney, 3600);		

		//放入当前支付渠道当天的成功金额
		String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
		key = "PayMethod#SuccessMoney#" + day + "#" + pay.getPayMethodId();
		centerDataService.increaseBy(key, setMoney, setMoney, 3600 * 24);		
	}


	@Override
	/**
	 * 在前端结束一个支付订单
	 * 并将结果发送到远程
	 * @param pay
	 * @return
	 * @throws Exception
	 */
	public int endFront(Pay pay) throws Exception{

		Pay _oldPay = select(pay.getTransactionId());
		if(_oldPay == null){
			logger.error("数据库中找不到支付订单[" + pay.getTransactionId());
			return -EisError.BILL_NOT_EXIST.getId();			
		}
		if(_oldPay.getCurrentStatus() == pay.getCurrentStatus()){
			logger.debug("支付订单[" + pay.getTransactionId() + "]状态未改变");
			return 0;		
		}
		if(pay.getRealMoney() <= 0){
			logger.error("尝试结束的支付订单realMoney为0");
			return -EisError.moneyRangeError.id;
		}




		_oldPay.setRealMoney(pay.getRealMoney());
		_oldPay.setOutOrderId(pay.getOutOrderId());
		_oldPay.setEndTime(new Date());
		//只有已存在的支付的状态是处理中时，才判断新传入的pay对象状态是否是成功
		if(_oldPay.getCurrentStatus() == TransactionStatus.inProcess.getId() && pay.getCurrentStatus() == TransactionStatus.success.getId()){// && changeMoney(pay) == Constants.OperateResult.success.getId()){
			float rate = 0f;
			if(_oldPay.getRate() > 1){
				rate = _oldPay.getRate() / 100;
			} else if(_oldPay.getRate() <= 1){
				rate = _oldPay.getRate();
			} else {
				logger.error("异常的费率:" + _oldPay.getRate());
			}
			float resultMoney = pay.getRealMoney();
			if(pay.getRealMoney() > pay.getFaceMoney() && pay.getFaceMoney() > 0){
				//XXX 如果支付结果，实际金额>面额，那么按照面额计算;
				resultMoney = pay.getFaceMoney();
			}

			float realValue = (float)NumericUtils.round(resultMoney * rate);

			logger.debug("当前支付订单的费率是:" + rate + ",支付完成金额是:" + resultMoney + "，经四舍五入后实际增加金额是:" + realValue);
			//XXX 必须以锁定形式更新，才能确保订单不会被重复处理
			_oldPay.setCurrentStatus(pay.getCurrentStatus());
			_oldPay.setLockStatus(TransactionStatus.inProcess.getId());
			_oldPay.setRealMoney(realValue);
			if(update(_oldPay) != 1){
				logger.error("无法更新支付订单[" + _oldPay.getTransactionId() + "].");
				return -EisError.BILL_UPDATE_FAIL.getId();
			} else {
				messageService.sendJmsDataSyncMessage(messageBusName, "payService", "update", _oldPay);
				logger.debug("支付订单[" + _oldPay.getTransactionId() + "]已更新，并发送同步请求[payService.update(item[" + pay.getTransactionId() + "]");
			}


			Money money = new Money();
			money.setUuid(_oldPay.getPayToAccount());
			money.setChargeMoney(realValue);
			User frontUser = frontUserService.select(money.getUuid());
			if(frontUser == null){

			}
			//计算用户是否有完成支付后的优惠
			//moneyService.plus(money);
			//logger.info("为账户[" + money.getUuid() + "]增加资金[付款面值：" + pay.getRealMoney() + "，实际增加：" + money.getChargeMoney() + "]");
			if(_oldPay.getRefBuyTransactionId() != null){
				//有对应的购买订单，发送请求完成此交易
				if(handlerPay){

					//有对应的购买订单，发送请求完成此交易
					logger.debug("支付订单[" + _oldPay.getTransactionId() + "]有对应的购买订单[" + _oldPay.getRefBuyTransactionId() + "]，尝试继续购买交易");
					TransPlan tp = transPlanService.select(0);

					TransactionExecutor transactionExecutor = applicationContextService.getBeanGeneric(tp.getProcessClass());

					if(transactionExecutor == null){
						logger.error("找不到指定的交易处理器:" + tp.getProcessClass());
						return -EisError.transactionProcessorNotFound.getId();
					}
					if(logger.isDebugEnabled()){
						logger.debug("尝试由交易处理器["  + transactionExecutor.toString() + "]处理支付交易[orderId=" + pay.getTransactionId() + "].");
					}
					try {
						transactionExecutor.begin(_oldPay);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {
				//如果没有对应的购买订单，那么才发送资金同步请求
				messageService.sendJmsDataSyncMessage(messageBusName, "moneyService", "plusLocal", money);
			}
			return OperateResult.success.getId();

		} else {	
			_oldPay.setCurrentStatus(pay.getCurrentStatus());

			if(update(_oldPay)  <= 0){
				logger.error("无法更新支付订单[" + _oldPay.getTransactionId() + "].");
				return -EisError.BILL_UPDATE_FAIL.getId();
			}
			messageService.sendJmsDataSyncMessage(messageBusName, "payService", "update", _oldPay);
			logger.debug("支付订单[" + _oldPay.getTransactionId() + "]已更新，并发送同步请求[payService.update(item[" + pay.getTransactionId() + "]");

			return OperateResult.failed.getId();
		}



	}





	@Override
	public int refund(Pay pay) {
		pay.setCurrentStatus(TransactionStatus.refunding.id);
		int rs = 0;
		try {
			rs = update(pay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rs != 1){
			logger.error("无法在退款前修改支付订单[" + pay.getTransactionId() + "]状态为退款中:" + rs);
			return -rs;
		}
		PayProcessor payProcessor = getProcessor(pay);
		if(payProcessor == null){
			logger.error("无法退款[" + pay.getTransactionId() + "]，根据支付方式[" + pay.getPayTypeId() + "]找不到指定的退款处理器");
			return -EisError.payProcessorIsNull.id;

		}
		EisMessage e = payProcessor.onRefund(pay);
		if(e == null){
			logger.error("无法退款[" + pay.getTransactionId() + ",，退款处理器[" + payProcessor + "]返回空");
			return -EisError.OBJECT_IS_NULL.id;
		}
		if(e.getOperateCode() != TransactionStatus.refunded.getId()){
			logger.error("无法退款[" + pay.getTransactionId() + ",，退款处理器[" + payProcessor + "]返回不是退款成功，而是:" + e.getOperateCode());
			return e.getOperateCode();
		}
		try {
			update(pay);
		} catch (Exception e1) {
			e1.printStackTrace();
			return -EisError.DATA_UPDATE_FAIL.id;
		}
		return 1;

	}


	@Override
	public Map<String, String> generateClientResponseMap(Pay pay) {
		final DecimalFormat df=new DecimalFormat("0.00"); 

		Map<String,String> param = new HashMap<String,String>();
		param.put("transactionId",pay.getTransactionId());
		param.put("orderId",pay.getInOrderId());
		param.put("requestMoney", df.format(pay.getFaceMoney()));
		param.put("successMoney", df.format(pay.getRealMoney()));
		param.put("result", String.valueOf(pay.getCurrentStatus()));
		param.put("timestamp", String.valueOf(new Date().getTime()));
		User partner = null;
		long forceUseSystemAccount = pay.getLongExtraValue("FORCE_USE_INTERNAL_PAY_FROM_ACCOUNT");
		if(forceUseSystemAccount > 0) {
			partner = partnerService.select(forceUseSystemAccount);
		} else {
			partner = partnerService.select(pay.getPayFromAccount());
		}
		if(partner == null){
			logger.error("根据UUID[" + pay.getPayFromAccount() + "]找不到合作伙伴");
			return null;
		} 

		if(partner.getCurrentStatus() != UserStatus.normal.getId()){
			logger.error("用户[" + pay.getPayFromAccount() + "]状态异常[" + partner.getCurrentStatus() + "]");
			return null;
		}

		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierLoginKey");
			return null;
		}	

		List<String> keys = new ArrayList<String>(param.keySet());
		Collections.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			String value = param.get(key);
			if(StringUtils.isBlank(value)){
				continue;
			}
			sb.append(key);
			sb.append('=');
			sb.append(value);
			sb.append('&');		
		}

		String signString = sb.toString().replaceAll("&$", "");		

		signString += "&key=" + loginKey;
		String sign = DigestUtils.md5Hex(signString);
		logger.debug("用源[" + signString + "]生成签名:" + sign);
		param.put("sign", sign);

		//以下为不参与签名的参数,NetSnake,2017-06-20
		param.put("beginTime", sdf.format(pay.getStartTime()));
		if(pay.getEndTime() != null){
			param.put("endTime", sdf.format(pay.getEndTime()));
		} else {
			param.put("endTime", "");
		}
		if(StringUtils.isNotBlank(pay.getPayCardType())){
			param.put("payCardType", pay.getPayCardType());
		}
		return param;

	}
	
	


}

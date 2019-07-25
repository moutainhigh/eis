package com.maicard.wpt.controller.common;

import static com.maicard.standard.CommonStandard.frontMessageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.annotation.IgnoreWeixinCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.domain.BankAccount;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.service.BankAccountService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.WithdrawService;
import com.maicard.money.service.WithdrawTypeService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;
/**
 * 提现接口<br>
 *
 * 增加了提现时检查IP白名单的限制,NetSnake,2017-06-13<br>
 * 
 * 
 * @author NetSnake
 * @date 2016年11月27日
 *
 */
@Controller
@RequestMapping("/withdraw")
public class WithdrawController extends BaseController {

	@Resource
	private AuthorizeService authorizeService;

	@Resource
	private BankAccountService bankAccountService;
	

	@Resource
	private ConfigService configService;

	@Resource
	private CertifyService certifyService;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private GlobalOrderIdService globalOrderIdService;

	@Resource
	private MoneyService moneyService;

	@Resource
	private NotifyService notifyService;

	@Resource
	private PartnerService partnerService;

	@Resource
	private WithdrawTypeService withdrawTypeService;

	@Resource
	private WithdrawService withdrawService;

	private final DecimalFormat df= new DecimalFormat("0.00"); 

	private final SimpleDateFormat csvFileSdf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);

	private long DEMO_ACCOUNT = 8100014;


	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	
	/**
	 * 是否把域名替换为IP地址
	 */
	boolean replaceHostNameToIp = false;
	
	/**
	 * 是否把IP替换为域名，如果是则这个不能为空
	 */
	String replaceIpToHost = null;
	
	
	@PostConstruct
	public void init(){
		replaceIpToHost = configService.getValue("replaceIpToHost",0);
		if(replaceIpToHost != null){
			replaceIpToHost = replaceIpToHost.trim();
			logger.info("当前系统配置了将IP地址替换为主机:" + replaceIpToHost);
		}
	}

	/**
	 * 申请提现、批付
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)		
	@IgnoreLoginCheck
	@IgnoreWeixinCheck	
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;		
		}

		map.clear();

		long withdrawAccount = ServletRequestUtils.getLongParameter(request, "withdrawAccount", 0);
		if(withdrawAccount == 0){
			logger.error("订单未提交必须的参数[withdrawAccount]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[withdrawAccount]"));
			return frontMessageView;		
		}
		User partner = partnerService.select(withdrawAccount);
		if(partner == null){
			logger.error("找不到指定的用户:" + withdrawAccount);
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "参数错误","找不到指定的用户"));
			return frontMessageView;		
		}

		if(partner.getUuid() != DEMO_ACCOUNT){
			String ip = IpUtils.getClientIp(request);
			String ipWhiteList = partner.getExtraValue("ipWhiteList");
			if(StringUtils.isBlank(ipWhiteList)){
				logger.error("商户[" + partner.getUuid() + "]未配置IP白名单，禁止提现");
				map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
				return frontMessageView;		
			}
			String[] whiteIps = ipWhiteList.split(",|;| ");
			boolean ipIsValid = false;
			for(String whiteIp : whiteIps){
				if(whiteIp.equals(ip)){
					ipIsValid = true;
					break;
				}
			}
			if(!ipIsValid){
				logger.error("当前IP:" + ip + "]不在商户[" + partner.getUuid() + "]的IP白名单中:" + ipWhiteList + "，禁止提现");
				map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
				return frontMessageView;	
			}
		}
		Withdraw withdraw = new Withdraw();
		withdraw.setUuid(partner.getUuid());
		withdraw.setOwnerId(ownerId);
		withdraw.setWithdrawMoney(ServletRequestUtils.getFloatParameter(request, "money", 0f));

		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierLoginKey");
			map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
			return frontMessageView;		
		}	

		String inOrderId = ServletRequestUtils.getStringParameter(request, "orderId",null);
		if(StringUtils.isBlank(inOrderId)){
			logger.error("用户[" + partner.getUuid() + "]未提交提现订单号");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[orderId]"));
			return frontMessageView;		
		} 
		withdraw.setInOrderId(inOrderId);
		String key = KeyConstants.WITHDRAW_IN_ORDER_LOCK_PREFIX + "#" + withdraw.getUuid() + "#" + withdraw.getInOrderId();
		//设置24小时内订单号不重复
		boolean inOrderOk = centerDataService.setIfNotExist(key, withdraw.getInOrderId(), 3600 * 24);
		if(!inOrderOk){
			logger.error("发起的订单号已存在:" + key);
			map.put("message", new EisMessage(EisError.billDuplicate.getId(), "数据错误","订单号重复"));
			return frontMessageView;		
		}




		String timestamp = ServletRequestUtils.getStringParameter(request, "timestamp",null);
		if(!NumericUtils.isNumeric(timestamp)){
			logger.error("订单未提交必须的参数[timestamp]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[timestamp]"));
			return frontMessageView;		
		}

		String sign = ServletRequestUtils.getStringParameter(request, "sign",null);
		if(StringUtils.isBlank(sign)){
			logger.error("订单未提交必须的参数[sign]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[sign]"));
			return frontMessageView;		
		}
		boolean allowNewWithdrawBankAccount = partner.getBooleanExtraValue(DataName.allowNewWithdrawBankAccount.toString());
		BankAccount bankAccount = null;

		boolean isCreateBankAccount = false;

		if(allowNewWithdrawBankAccount){
			bankAccount = new BankAccount();
			ClassUtils.bindBeanFromMap(bankAccount, HttpUtils.getRequestDataMap(request));
			bankAccount.setOwnerId(ownerId);
			bankAccount.setUuid(partner.getUuid());
			bankAccount.setCurrentStatus(BasicStatus.normal.getId());

			logger.info("用户[" + partner.getUuid() + "]允许自行提交新提现/批付帐号，提交帐号是:" + bankAccount);
			if(StringUtils.isBlank(bankAccount.getBankName())){
				logger.info("当前帐号[" + partner.getUuid() + "]允许自行提交新提现/批付帐号，但未提交正确的银行帐号:" + bankAccount);				
			} else {
				logger.debug("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号:" + bankAccount);
				if(StringUtils.isBlank(bankAccount.getBankAccountType())){
					logger.warn("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号但是银行账户类型为空:" + bankAccount);
					map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误"));
					return frontMessageView;
				}

				if(!bankAccount.getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BANK_ACCOUNT_TYPE_COMPANY) && !bankAccount.getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BAKC_ACCOUNT_TYPE_PERSONAL)){
					logger.warn("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号但是银行账户类型不合法:" + bankAccount.getBankAccountType());
					map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误"));
					return frontMessageView;
				}
				BankAccount b2 = bankAccount.clone();
				int createBankRs = bankAccountService.createIfNotExist(b2);
				if(createBankRs == EisError.ACCOUNT_ERROR.id){
					logger.error("无法创建提交的银行帐号数据:" + bankAccount);
					map.put("message", new EisMessage(EisError.ACCOUNT_ERROR.getId(), "无法创建银行帐号"));
					return frontMessageView;		
				}
				bankAccount.setBankAccountId(b2.getBankAccountId());
				isCreateBankAccount = true;
				withdraw.setBankAccount(bankAccount);
			}
		} 

		if(withdraw.getBankAccount() == null){
			//查找对应账户的银行提现帐号
			BankAccountCriteria bankAccountCriteria = new BankAccountCriteria();
			bankAccountCriteria.setUuid(partner.getUuid());
			List<BankAccount> bankAccountList = bankAccountService.list(bankAccountCriteria);
			logger.debug("用户[" + partner.getUuid() + "]拥有的提现帐号数量是:" + (bankAccountList == null ? "空" : bankAccountList.size()));
			if(bankAccountList == null || bankAccountList.size() < 1){
				logger.error("用户[" + partner.getUuid() + "]没有任何提现帐号，无法提现");
				map.put("message", new EisMessage(EisError.accountNotExist.getId(),"没有可用的提现帐号"));
				return CommonStandard.frontMessageView;	
			} 
			if(bankAccountList.size() == 1){
				bankAccount = bankAccountList.get(0);
				logger.info("用户[" + partner.getUuid() + "]只有一个提现帐号，直接使用:" + bankAccount);
			} else {
				int useBankAccountId = ServletRequestUtils.getIntParameter(request, "bankAccountId", 0);
				logger.info("用户[" + partner.getUuid() + "]有多个提现帐号，申请使用的提现帐号ID是:" + useBankAccountId);
				if(useBankAccountId == 0){
					bankAccount = bankAccountList.get(0);
				} else {
					for(BankAccount ba : bankAccountList ){
						if(ba.getBankAccountId() == useBankAccountId){
							bankAccount = ba;
							break;
						}
					}
					if(bankAccount == null){
						logger.info("用户[" + partner.getUuid() + "]申请使用提现帐号:" + useBankAccountId + ",但是找不到指定的提现帐号");
						map.put("message", new EisMessage(EisError.ACCOUNT_ERROR.getId(),"找不到指定的提现帐号"));
						return CommonStandard.frontMessageView;	
					}
				}
			}
		}
		boolean signIsOk = false;
		if(isCreateBankAccount){
			signIsOk = checkSign(withdraw, sign, loginKey, timestamp, bankAccount);
		} else {
			signIsOk = checkSign(withdraw, sign, loginKey, timestamp, null);
		}
		if(!signIsOk){
			logger.error("订单签名校验失败");
			map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "校验失败","订单签名校验失败"));
			return frontMessageView;
		}
		withdraw.setBankAccount(bankAccount);

		String inNotifyUrl = ServletRequestUtils.getStringParameter(request, "inNotifyUrl", null);
		if(StringUtils.isNotBlank(inNotifyUrl)){
			withdraw.setExtraValue("inNotifyUrl", inNotifyUrl);
		}
		int withdrawTypeId = (int)partner.getLongExtraValue(DataName.withdrawType.toString());
		logger.debug("用户[" + partner.getUuid() + "]配置的提现类型是:" + withdrawTypeId);
		if(withdrawTypeId == 0){
			logger.warn("用户[" + partner.getUuid() + "]未配置任何提现类型，无法支持提现");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"帐号不支持提现"));
			return CommonStandard.frontMessageView;		
		}
		withdraw.setWithdrawTypeId(withdrawTypeId);
		withdraw.setOwnerId(ownerId);
		withdraw.setUuid(partner.getUuid());
		if(withdraw.getWithdrawMoney() <= 0){
			logger.error("申请提现金额为0");
			map.put("message", new EisMessage(EisError.moneyRangeError.getId(),"提现金额不能为0"));
			return CommonStandard.partnerMessageView;		
		}



		int valid = withdrawService.isValidWithdraw(withdraw);
		if(valid != OperateResult.success.getId()){
			map.put("message", new EisMessage(valid,"提现申请不合规"));
			return CommonStandard.partnerMessageView;		
		}

		StringBuffer sb2 = new StringBuffer();
		sb2.append(request.getProtocol().toLowerCase().startsWith("https") ? "https" : "http").append("://");
		if(StringUtils.isNotBlank(replaceIpToHost)){
			sb2.append(replaceIpToHost);
		} else {
			sb2.append(request.getServerName());
		}
		sb2.append(":").append(request.getServerPort()).append(request.getRequestURI());
		String currentUrl = sb2.toString();
		String hostname = request.getServerName();
		String ip = IpUtils.getIpFromHost(hostname);
		if(replaceHostNameToIp){
			currentUrl = currentUrl.replaceAll(hostname, ip);
		}
		withdraw.setExtraValue("currentUrl", currentUrl);

		/*StringBuffer sb = new StringBuffer();
		sb.append(request.getProtocol().toLowerCase().startsWith("https") ? "https" : "http").append("://");
		if(StringUtils.isNotBlank(replaceIpToHost)){
			sb.append(replaceIpToHost);
		} else {
			sb.append(request.getServerName());
		}
		sb.append(":").append(request.getServerPort()).append(request.getRequestURI());
		String currentUrl = sb.toString();
		
		String hostname = request.getServerName();
		String ip = IpUtils.getIpFromHost(hostname);
		String notifyUrl = currentUrl.replaceAll("/create.shtml","/create.json").replaceAll("/withdraw/create.json", "/withdraw/notify/" + withdraw.getWithdrawMethodId() + ".json");
		
		if(replaceHostNameToIp){
			notifyUrl = notifyUrl.replaceAll(hostname, ip);
		}
		
		withdraw.setExtraValue("notifyUrl", notifyUrl);
*/
		EisMessage message = null;
		try{
			int rs = withdrawService.begin(withdraw,null);
			if(rs == TransactionStatus.inProcess.getId()){
				message = new EisMessage(OperateResult.success.getId(),"提现申请成功");		
				map.put("remainWithdrawMoney", withdraw.getMoneyAfterWithdraw());
			} else {
				message = new EisMessage(rs,"提现申请失败");				

			}

		}catch(Exception e){
			e.printStackTrace();
			String m = "数据操作失败" + e.getMessage();			
			logger.error(m);
			throw new DataWriteErrorException(m);
		}

		map.put("message", message);
		map.put("orderId", inOrderId);
		map.put("transactionId", withdraw.getTransactionId());
		return CommonStandard.partnerMessageView;
	}


	/**
	 * 一次性提交多个提现数据
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/batch", method=RequestMethod.POST)		
	@IgnoreLoginCheck
	@IgnoreWeixinCheck	
	public String batch(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;		
		}

		map.clear();

		long withdrawAccount = ServletRequestUtils.getLongParameter(request, "withdrawAccount", 0);
		if(withdrawAccount == 0){
			logger.error("订单未提交必须的参数[withdrawAccount]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[withdrawAccount]"));
			return frontMessageView;		
		}
		User partner = partnerService.select(withdrawAccount);
		if(partner == null){
			logger.error("找不到指定的用户:" + withdrawAccount);
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "参数错误","找不到指定的用户"));
			return frontMessageView;		
		}

		if(partner.getUuid() != DEMO_ACCOUNT){
			String ip = IpUtils.getClientIp(request);
			String ipWhiteList = partner.getExtraValue("ipWhiteList");
			if(StringUtils.isBlank(ipWhiteList)){
				logger.error("商户[" + partner.getUuid() + "]未配置IP白名单，禁止提现");
				map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
				return frontMessageView;		
			}
			String[] whiteIps = ipWhiteList.split(",|;| ");
			boolean ipIsValid = false;
			for(String whiteIp : whiteIps){
				if(whiteIp.equals(ip)){
					ipIsValid = true;
					break;
				}
			}
			if(!ipIsValid){
				logger.error("当前IP:" + ip + "]不在商户[" + partner.getUuid() + "]的IP白名单中:" + ipWhiteList + "，禁止提现");
				map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
				return frontMessageView;	
			}
		}
		Withdraw withdraw = new Withdraw();
		withdraw.setUuid(partner.getUuid());
		withdraw.setOwnerId(ownerId);
		withdraw.setTransactionId(globalOrderIdService.generate(TransactionType.withdraw.getId()));
		withdraw.setBeginTime(new Date());
		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierLoginKey");
			map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
			return frontMessageView;		
		}	

		//批量提交必须使用加密
		String cryptKey = partner.getExtraValue(DataName.supplierChargeKey.toString());
		if(StringUtils.isBlank(cryptKey)){
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierChargeKey");
			map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
			return frontMessageView;		
		}	

		String inOrderId = ServletRequestUtils.getStringParameter(request, "orderId",null);
		if(StringUtils.isBlank(inOrderId)){
			logger.error("用户[" + partner.getUuid() + "]未提交提现订单号");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[orderId]"));
			return frontMessageView;		
		}
		withdraw.setInOrderId(inOrderId);
		String orderKey = KeyConstants.WITHDRAW_IN_ORDER_LOCK_PREFIX + "#" + withdraw.getUuid() + "#" + withdraw.getInOrderId();
		//设置24小时内订单号不重复
		boolean inOrderOk = centerDataService.setIfNotExist(orderKey, withdraw.getInOrderId(), 3600 * 24);
		if(!inOrderOk){
			logger.error("发起的订单号已存在:" + orderKey);
			map.put("message", new EisMessage(EisError.billDuplicate.getId(), "数据错误","订单号重复"));
			return frontMessageView;		
		}


		String timestamp = ServletRequestUtils.getStringParameter(request, "timestamp",null);
		if(!NumericUtils.isNumeric(timestamp)){
			logger.error("订单未提交必须的参数[timestamp]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[timestamp]"));
			return frontMessageView;		
		}

		String sign = ServletRequestUtils.getStringParameter(request, "sign",null);
		if(StringUtils.isBlank(sign)){
			logger.error("订单未提交必须的参数[sign]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[sign]"));
			return frontMessageView;		
		}
		boolean allowNewWithdrawBankAccount = partner.getBooleanExtraValue(DataName.allowNewWithdrawBankAccount.toString());


		if(!allowNewWithdrawBankAccount){
			logger.error("当前商户不允许提交银行帐号，无法使用批量提现");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "功能受限"));
			return frontMessageView;		
		}

		String encryptData = ServletRequestUtils.getStringParameter(request,"batchData");
		if(StringUtils.isBlank(encryptData)){
			logger.error("当前商户未提交批量数据batchData");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[batchData]"));
			return frontMessageView;		
		}

		Crypt crypt = new Crypt();
		crypt.setDes3Key(cryptKey);
		String clearData = null;
		try{
			clearData = crypt.des3Decrypt(encryptData);
		}catch(Exception e){
			logger.error("解密失败:" + e.getMessage());
		}
		if(clearData == null){
			logger.error("商户[" + partner.getUuid() + "]提交的批量数据无法解密，商户密钥是:" + cryptKey + ",密文:" + encryptData);
			map.put("message", new EisMessage(EisError.decryptError.getId(), "解密失败"));
			return frontMessageView;		
		}
		logger.debug("商户[" + partner.getUuid() + "]提交的数据[" + encryptData + "]解密后:" + clearData);
		int batchCount = ServletRequestUtils.getIntParameter(request, "batchCount", 0);

		String[] dataLevel1 = clearData.split("\\|");
		if(dataLevel1.length != batchCount){
			logger.error("当前商户[" + partner.getUuid() + "]提交的批量数据数量:" + batchCount + "，与其提交的帐号数量:" + dataLevel1.length + "不一致");
			map.put("message", new EisMessage(EisError.countNotEnough.getId(), "帐号数量错误"));
			return frontMessageView;	
		}

		float totalMoney = ServletRequestUtils.getFloatParameter(request, "totalMoney", 0f);
		if(totalMoney <= 0){
			logger.error("当前商户[" + partner.getUuid() + "]未提交批量数据totalMoney");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[totalMoney]"));
			return frontMessageView;		
		}
		withdraw.setWithdrawMoney(totalMoney);

		String inNotifyUrl = ServletRequestUtils.getStringParameter(request, "inNotifyUrl", null);
		if(StringUtils.isNotBlank(inNotifyUrl)){
			withdraw.setExtraValue("inNotifyUrl", inNotifyUrl);
		}
		int withdrawTypeId = (int)partner.getLongExtraValue(DataName.withdrawType.toString());
		logger.debug("用户[" + partner.getUuid() + "]配置的提现类型是:" + withdrawTypeId);
		if(withdrawTypeId == 0){
			logger.warn("用户[" + partner.getUuid() + "]未配置任何提现类型，无法支持提现");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"帐号不支持提现"));
			return CommonStandard.frontMessageView;		
		}
		withdraw.setWithdrawTypeId(withdrawTypeId);

		boolean signIsOk = false;

		//检查签名
		Map<String,String> param = new HashMap<String, String>();
		param.put("withdrawAccount", String.valueOf(withdraw.getUuid()));
		param.put("timestamp", timestamp);
		param.put("batchData", clearData);
		param.put("orderId", inOrderId);
		param.put("batchCount", String.valueOf(batchCount));
		param.put("totalMoney", df.format(totalMoney));
		param.put("inNotifyUrl", inNotifyUrl);

		ArrayList<String> list = new ArrayList<String>();
		for (String key : param.keySet()) {
			list.add(key);
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
			sb.append('=');
			sb.append(param.get(arrayToSort[i]));
			sb.append('&');
		}
		sb.append("key=").append(loginKey);
		String signSource = sb.toString();
		String ourSign = DigestUtils.md5Hex(signSource);
		logger.debug("我方对源[" + signSource + "]签名结果:" + ourSign + ",对方传递的sign:" + sign);

		signIsOk = ourSign.equalsIgnoreCase(sign);
		if(!signIsOk){
			logger.error("订单签名校验失败");
			map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "校验失败","订单签名校验失败"));
			return frontMessageView;
		}
		
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append(request.getProtocol().toLowerCase().startsWith("https") ? "https" : "http").append("://");
		if(StringUtils.isNotBlank(replaceIpToHost)){
			sb2.append(replaceIpToHost);
		} else {
			sb2.append(request.getServerName());
		}
		sb2.append(":").append(request.getServerPort()).append(request.getRequestURI());
		String currentUrl = sb2.toString();
		String hostname = request.getServerName();
		String ip = IpUtils.getIpFromHost(hostname);
		if(replaceHostNameToIp){
			currentUrl = currentUrl.replaceAll(hostname, ip);
		}
		withdraw.setExtraValue("currentUrl", currentUrl);
		
		
		//String notifyUrl = currentUrl.replaceAll("/batch.shtml","/batch.json").replaceAll("/withdraw/batch.json", "/withdraw/notify/" + withdrawId() + ".json").replaceAll(hostname, ip);
		
		
		
		/*String currentUrl = request.getRequestURL().toString();
		String hostname = request.getServerName();
		String ip = IpUtils.getIpFromHost(hostname);
		String notifyUrl = currentUrl.replaceAll("/batch.shtml","/batch.json").replaceAll("/withdraw/batch.json", "/withdraw/notify/" + withdraw.getWithdrawTypeId() + ".json").replaceAll(hostname, ip);
	*/	
		
		withdraw.setExtraValue("currentUrl", currentUrl);

		int subOrderCount = 0;
		float subOrderMoney = 0f;
		List<Withdraw> subWithdrawList = new ArrayList<Withdraw>();
		for(String data : dataLevel1){

			if(StringUtils.isBlank(data)){
				continue;
			}
			BankAccount bankAccount = new BankAccount(ownerId);

			bankAccount.setOwnerId(ownerId);
			bankAccount.setUuid(partner.getUuid());
			bankAccount.setCurrentStatus(BasicStatus.normal.getId());

			Withdraw subWithdraw = withdraw.clone();
			subWithdraw.setParentTransactionId(withdraw.getTransactionId());
			logger.info("用户[" + partner.getUuid() + "]批量付款，提交帐号是:" + bankAccount);

			bindSubData(subWithdraw, bankAccount,data);

			if(StringUtils.isBlank(bankAccount.getBankName())){
				logger.info("当前帐号[" + partner.getUuid() + "]允许自行提交新提现/批付帐号，但未提交正确的银行帐号:" + bankAccount);	
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "银行帐号参数错误"));
				return frontMessageView;
			} 
			if(subWithdraw.getWithdrawMoney() <= 0){
				logger.info("当前帐号[" + partner.getUuid() + "]的子提现请求没有金额:" + subWithdraw);	
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "银行帐号参数错误"));
				return frontMessageView;
			}
			logger.debug("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号:" + bankAccount);
			if(StringUtils.isBlank(bankAccount.getBankAccountType())){
				logger.warn("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号但是银行账户类型为空:" + bankAccount);
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误"));
				return frontMessageView;
			}
			if(!bankAccount.getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BANK_ACCOUNT_TYPE_COMPANY) && !bankAccount.getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BAKC_ACCOUNT_TYPE_PERSONAL)){
				logger.warn("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号但是银行账户类型不合法:" + bankAccount.getBankAccountType());
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误"));
				return frontMessageView;
			}
			int createBankRs = bankAccountService.createIfNotExist(bankAccount);
			if(createBankRs == EisError.ACCOUNT_ERROR.id){
				logger.error("无法创建提交的银行帐号数据:" + bankAccount);
				map.put("message", new EisMessage(EisError.ACCOUNT_ERROR.getId(), "无法创建银行帐号"));
				return frontMessageView;		
			}
			subWithdraw.setBankAccount(bankAccount);
			subOrderCount++;
			subOrderMoney += subWithdraw.getWithdrawMoney();
			subWithdrawList.add(subWithdraw);


		}
		logger.debug("共生成" + subOrderCount + "个子提现订单，商户描述的数量是:" + batchCount);
		if(subOrderCount != batchCount){
			map.put("message", new EisMessage(EisError.countNotEnough.getId(), "银行帐号数量错误"));
			return frontMessageView;
		}
		if(subOrderMoney != totalMoney){
			map.put("message", new EisMessage(EisError.moneyRangeError.getId(), "提现金额不匹配"));
			return frontMessageView;
		}

		withdraw.setTotalRequest(batchCount);






		int valid = withdrawService.isValidWithdraw(withdraw);
		if(valid != OperateResult.success.getId()){
			map.put("message", new EisMessage(valid,"提现申请不合规"));
			return CommonStandard.partnerMessageView;		
		}



		EisMessage message = null;
		try{
			int rs = withdrawService.begin(withdraw,subWithdrawList);
			logger.debug("付款[" + withdraw.getTransactionId() + "]申请结果:" + rs);
			if(rs == TransactionStatus.inProcess.getId()){
				message = new EisMessage(OperateResult.success.getId(),"提现申请成功");		
				map.put("remainWithdrawMoney", withdraw.getMoneyAfterWithdraw());
			} else {
				message = new EisMessage(rs,"提现申请失败");				

			}

		}catch(Exception e){
			e.printStackTrace();
			message = new EisMessage(EisError.systemException.id,"系统异常");
		}

		map.put("message", message);
		map.put("orderId", inOrderId);
		map.put("transactionId", withdraw.getTransactionId());
		return CommonStandard.partnerMessageView;
	}

	/**
	 * 把文本数据转换为要提现的银行帐号，和提现金额<br>
	 * 文本格式:收款人帐号^收款人姓名^付款金额^付款理由^银行名称^省份^城市^开户行名称^对公对私
	 * @param subWithdraw
	 * @param bankAccount
	 * @param data
	 */
	private void bindSubData(Withdraw subWithdraw, BankAccount bankAccount, String data) {
		String[] subData = data.split("\\^");
		if(subData.length < 10){
			logger.error("错误的批量提现数据:" + data);
			return;
		}
		subWithdraw.setTransactionId(subWithdraw.getTransactionId() + "-" + subData[0].trim());
		bankAccount.setBankAccountNumber(subData[1].trim());
		bankAccount.setBankAccountName(subData[2].trim());
		subWithdraw.setWithdrawMoney(NumericUtils.parseFloat(subData[3]));
		subWithdraw.setArriveMoney(subWithdraw.getWithdrawMoney());
		subWithdraw.setReason(subData[4]);
		bankAccount.setBankName(subData[5].trim());
		bankAccount.setProvince(subData[6].trim());
		bankAccount.setCity(subData[7].trim());
		bankAccount.setIssueBank(subData[8]);
		String bankAccountType = subData[9];
		if(bankAccountType == null || bankAccountType.toUpperCase().equals(BankAccountCriteria.BAKC_ACCOUNT_TYPE_PERSONAL)){
			bankAccount.setBankAccountType(BankAccountCriteria.BAKC_ACCOUNT_TYPE_PERSONAL);
		} else {
			bankAccount.setBankAccountType(BankAccountCriteria.BANK_ACCOUNT_TYPE_COMPANY);
		}


	}


	/**
	 * 查询用商户的账户余额和可提现资金
	 */
	@RequestMapping(value="/balance")		
	@IgnoreLoginCheck
	@IgnoreWeixinCheck	
	public String balance(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		map.clear();
		long withdrawAccount = ServletRequestUtils.getLongParameter(request, "withdrawAccount", 0);
		if(withdrawAccount == 0){
			logger.error("查询余额未提交必须的参数[withdrawAccount]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[withdrawAccount]"));
			return frontMessageView;		
		}
		User partner = partnerService.select(withdrawAccount);
		if(partner == null){
			logger.error("找不到指定的用户:" + withdrawAccount);
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "参数错误","找不到指定的用户"));
			return frontMessageView;		
		}

		if(partner.getUuid() != DEMO_ACCOUNT){
			String ip = IpUtils.getClientIp(request);
			String ipWhiteList = partner.getExtraValue("ipWhiteList");
			if(StringUtils.isBlank(ipWhiteList)){
				logger.error("商户[" + partner.getUuid() + "]未配置IP白名单，禁止查询余额");
				map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
				return frontMessageView;		
			}
			String[] whiteIps = ipWhiteList.split(",|;| ");
			boolean ipIsValid = false;
			for(String whiteIp : whiteIps){
				if(whiteIp.equals(ip)){
					ipIsValid = true;
					break;
				}
			}
			if(!ipIsValid){
				logger.error("当前IP:" + ip + "]不在商户[" + partner.getUuid() + "]的IP白名单中:" + ipWhiteList + "，禁止查询余额");
				map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
				return frontMessageView;	
			}
		}

		long ts = ServletRequestUtils.getLongParameter(request, "timestamp",0);
		if(ts == 0){
			logger.error("查询余额未提交必须的参数[timestamp]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[timestamp]"));
			return frontMessageView;		
		}
		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierLoginKey");
			map.put("message", new EisMessage(EisError.dataError.getId(), "数据错误","指定的用户配置错误"));
			return frontMessageView;		
		}	

		String sign = ServletRequestUtils.getStringParameter(request, "sign",null);
		if(StringUtils.isBlank(sign)){
			logger.error("订单未提交必须的参数[sign]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[sign]"));
			return frontMessageView;		
		}

		String signSource = "timestamp=" + ts + "&withdrawAccount=" + withdrawAccount + "&key=" + loginKey;

		String ourSign = DigestUtils.md5Hex(signSource);

		if(!ourSign.equalsIgnoreCase(sign)){
			logger.error("我方对源[" + signSource + "]签名后是:" + ourSign + ",对方提供的签名是:" + sign + ",不一致");
			map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "参数错误","签名校验失败"));
			return frontMessageView;		
		}

		Money money = moneyService.select(withdrawAccount, ownerId);
		if(money == null){
			money = new Money(withdrawAccount,ownerId);
		}

		map.put("message", new EisMessage(OperateResult.success.id,null));


		long ourTs = new Date().getTime();
		String balance = df.format(money.getIncomingMoney());
		String avaiable = df.format(money.getTransitMoney());

		signSource = "avaiable=" + avaiable + "&balance=" + balance + "&timestamp=" + ourTs + "&withdrawAccount=" + withdrawAccount + "&key=" + loginKey;

		ourSign = DigestUtils.md5Hex(signSource);
		logger.info("我方对源[" + signSource + "]签名后是:" + ourSign);
		map.put("sign", ourSign);
		map.put("balanceMoney", balance);
		map.put("avaiableMoney", avaiable);
		return frontMessageView;		
	}


	private boolean checkSign(Withdraw withdraw, String inSign, String signKey, String timestamp, BankAccount bankAccount) {


		Map<String,String> param = new HashMap<String, String>();
		param.put("withdrawAccount", String.valueOf(withdraw.getUuid()));
		param.put("money", String.valueOf(df.format(withdraw.getWithdrawMoney())));
		param.put("timestamp", timestamp);
		if(StringUtils.isNotBlank(withdraw.getInOrderId())){
			param.put("orderId", withdraw.getInOrderId());
		}
		if(bankAccount != null){
			/*if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankAccountNumber())){
				param.put("bankAccountType", withdraw.getBankAccount().getBankAccountType());
			}*/
			if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankName())){
				param.put("bankName", withdraw.getBankAccount().getBankName());
			}
			if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankAccountName())){
				param.put("bankAccountName", withdraw.getBankAccount().getBankAccountName());
			}
			if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankAccountNumber())){
				param.put("bankAccountNumber", withdraw.getBankAccount().getBankAccountNumber());
			}
			if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankAccountNumber())){
				param.put("province", withdraw.getBankAccount().getProvince());
			}
			if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankAccountNumber())){
				param.put("city", withdraw.getBankAccount().getCity());
			}/*
			if(StringUtils.isNotBlank(withdraw.getBankAccount().getBankAccountNumber())){
				param.put("issueBank", withdraw.getBankAccount().getIssueBank());
			}*/
		}

		ArrayList<String> list = new ArrayList<String>();
		for (String key : param.keySet()) {
			list.add(key);
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
			sb.append('=');
			sb.append(param.get(arrayToSort[i]));
			sb.append('&');
		}
		sb.append("key=").append(signKey);
		String signSource = sb.toString();
		String ourSign = DigestUtils.md5Hex(signSource);
		logger.debug("我方对查询余额的请求签名检查，源[" + signSource + "]签名结果:" + ourSign + ",对方传递的sign:" + inSign);
		if(ourSign.equalsIgnoreCase(inSign)){
			return true;
		}
		return false;
	}

	/*
	 * 提现完成后的回调通知处理
	 * 调用对应的withdrawProcessor的onResult接口方法，并得到EisMessage结果
	 * 处理完成后仅向支付商返回success等字符
	 */
	@RequestMapping("/notify/{withdrawMethodId}")
	@IgnoreLoginCheck
	public ResponseEntity<String>   onNotify(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("withdrawMethodId") Integer withdrawMethodId) throws Exception {
		String resultString = HttpUtils.generateRequestString(request);
		BufferedReader input = request.getReader();
		String rawContent = null;
		if(input != null){
			StringBuilder sb = new StringBuilder(); 
			String line = null;
			while( (line = input.readLine()) != null){
				sb.append(line);
			}
			rawContent = sb.toString();
			logger.debug("对方提交了数据流:" + rawContent);
			if(StringUtils.isNotBlank(rawContent)){
				resultString += "&rawContent=" + rawContent;
			}
		} else {
			logger.debug("对方没提交任何POST数据");
		}

		EisMessage notifyResult = withdrawService.end(withdrawMethodId, resultString);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "text/html; charset=UTF-8");
		Withdraw withdraw = null;
		if(notifyResult != null){
			try{
				withdraw = (Withdraw) notifyResult.getAttachment().get("withdraw");
			}catch(Exception e){
				logger.error("无法解析提现处理器返回的withdraw对象:" + e.getMessage());
				e.printStackTrace();
			}
		}
		if(withdraw == null){
			logger.error("提现处理器未返回withdraw对象");
			return new ResponseEntity<String>(null, responseHeaders, HttpStatus.OK);	
		}
		if(withdraw.getParentTransactionId() == null){
			logger.info("提现订单[" + withdraw.getTransactionId() + "]是一个独立或父订单，发送通知");
			String inNotifyUrl = withdraw.getExtraValue("inNotifyUrl");
			if(StringUtils.isNotBlank(inNotifyUrl)){
				//发送通知
				notifyService.sendNotify(withdraw);
			}
		} else {
			logger.info("提现订单[" + withdraw.getTransactionId() + "]是[" + withdraw.getParentTransactionId() + "]的子订单，不发送通知");

		}

		String responseText = withdraw.getExtraValue("notifyResponse");
		logger.info("提现订单[" + withdraw.getTransactionId() + "]响应字符串:" + responseText );
		return new ResponseEntity<String>(responseText, responseHeaders, HttpStatus.OK);	
	}

	//查询指定交易ID的支付状态
	@RequestMapping(value="/query")
	@IgnoreLoginCheck
	@IgnoreWeixinCheck
	public String query(HttpServletRequest request, HttpServletResponse response, ModelMap map)  {

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		long withdrawAccount = ServletRequestUtils.getLongParameter(request, "withdrawAccount", 0);
		if(withdrawAccount == 0){
			logger.error("订单未提交必须的参数[withdrawAccount]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单未提交必须的参数[withdrawAccount]"));
			return frontMessageView;		
		}
		User partner = partnerService.select(withdrawAccount);
		if(partner == null){
			logger.error("找不到指定的用户:" + withdrawAccount);
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "参数错误","找不到指定的用户"));
			return frontMessageView;		
		}
		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("找不到指定的商户[" + withdrawAccount + "]的密钥");
			map.put("message",  new EisMessage(EisError.dataError.id,"找不到指定的用户数据"));
			return frontMessageView;
		}	
		String timestamp = ServletRequestUtils.getStringParameter(request, "timestamp",null);
		if(!NumericUtils.isNumeric(timestamp)){
			logger.error("订单查询未提交必须的参数[timestamp]");
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "参数错误","订单查询未提交必须的参数[timestamp]"));
			return frontMessageView;		
		}

		String sign = ServletRequestUtils.getStringParameter(request, "sign", null);
		if(StringUtils.isBlank(sign)){
			logger.error("查询未提交签名sign");
			map.put("message",  new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交正确的参数:sign"));
			return frontMessageView;
		}
		String inOrderId = ServletRequestUtils.getStringParameter(request, "orderId", null);
		if(StringUtils.isBlank(inOrderId)){
			logger.error("查询未提交订单号参数orderId");
			map.put("message",  new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交正确的参数:orderId"));
			return frontMessageView;
		}


		String signSource = "orderId=" + inOrderId + "&timestamp=" + timestamp +  "&withdrawAccount=" + withdrawAccount + "&key=" + loginKey;
		String ourSign = DigestUtils.md5Hex(signSource);
		if(!ourSign.equalsIgnoreCase(sign)){
			map.put("message",  new EisMessage(EisError.VERIFY_ERROR.id,"校验失败"));
			return frontMessageView;

		}

		logger.debug("查询客户支付订单[" + inOrderId + "]");
		WithdrawCriteria withdrawCriteria = new WithdrawCriteria(ownerId);
		withdrawCriteria.setInOrderId(inOrderId);
		withdrawCriteria.setUuid(withdrawAccount);
		List<Withdraw> withdrawList = withdrawService.list(withdrawCriteria);
		if(withdrawList == null || withdrawList.size() < 1){
			logger.warn("根据条件[" + withdrawCriteria + "]找不到任何订单");
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在"));
			return CommonStandard.frontMessageView;
		} 
		Withdraw withdraw = withdrawList.get(0);
		logger.warn("根据条件[" + withdrawCriteria + "]找到的订单是:" + withdraw);

		String resultTs = String.valueOf(new Date().getTime());
		map.put("result", withdraw.getCurrentStatus());
		map.put("transactionId", withdraw.getTransactionId());
		map.put("orderId", withdraw.getInOrderId());
		map.put("timestamp", resultTs);
		String resultSignSource = "orderId=" + inOrderId + "&timestamp=" + resultTs + "&transactionId=" + withdraw.getTransactionId() + "&withdrawAccount=" + withdrawAccount +  "&key=" + loginKey;
		String resultSign = DigestUtils.md5Hex(resultSignSource);
		logger.debug("对查询结果返回进行签名，签名源[" + resultSignSource + "]，签名:" + resultSign);
		map.put("sign", resultSign);
		return CommonStandard.frontMessageView;
	}

	//查询指定条件的批量订单
	@RequestMapping(value="/list",method=RequestMethod.GET )
	@IgnoreLoginCheck
	@IgnoreWeixinCheck
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws IOException  {
		final long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		final int MAX_QUERY_PERIOD = 7;
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		map.clear();

		long uuid = ServletRequestUtils.getLongParameter(request, "uuid", 0);
		if(uuid < 1){
			logger.error("查询未提交参数uuid");
			map.put("message",  new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交正确的参数:uuid"));
			return frontMessageView;
		}		

		User partner = partnerService.select(uuid);
		if(partner == null){
			logger.error("找不到指定的商户:" + uuid);
			map.put("message",  new EisMessage(EisError.userNotFoundInSystem.id,"找不到指定的用户"));
			return frontMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);


		WithdrawCriteria withdrawCriteria = new WithdrawCriteria(ownerId);
		withdrawCriteria.setUuid(uuid);
		if(!isPlatformGenericPartner){
			try {
				partnerService.setSubPartner(withdrawCriteria, partner);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("message",  new EisMessage(EisError.systemDataError.id,"系统参数错误"));
				return frontMessageView;
			}

		}
		String beginTime = ServletRequestUtils.getStringParameter(request,"beginTime",null);

		if(StringUtils.isBlank(beginTime)){
			logger.error("批量查询未提交开始时间");
			map.put("message",  new EisMessage(EisError.timeError.id,"未提交开始时间"));
			return frontMessageView;
		}

		Date queryBeginTime = null;
		try{
			queryBeginTime = sdf.parse(beginTime);
		}catch(Exception e){
			logger.error("无法解析查询开始时间:" + beginTime);
		}

		if(queryBeginTime == null){
			logger.error("无法解析查询开始时间:" + beginTime);
			map.put("message",  new EisMessage(EisError.timeError.id,"错误的开始时间"));
			return frontMessageView;
		}

		String endTime = ServletRequestUtils.getStringParameter(request,"endTime",null);

		if(StringUtils.isBlank(endTime)){
			logger.error("批量查询未提交结束时间");
			map.put("message",  new EisMessage(EisError.timeError.id,"未提交结束时间"));
			return frontMessageView;
		}

		Date queryEndTime = null;
		try{
			queryEndTime = sdf.parse(endTime);
		}catch(Exception e){
			logger.error("无法解析查询结束时间:" + endTime);
		}

		if(queryEndTime == null){
			logger.error("无法解析查询开始时间:" + endTime);
			map.put("message",  new EisMessage(EisError.timeError.id,"错误的结束时间"));
			return frontMessageView;
		}
		logger.info("查询起止时间是[" + beginTime + "=>" + endTime + "]");
		if(DateUtils.addDays(queryBeginTime, MAX_QUERY_PERIOD).before(queryEndTime)){
			logger.error("查询起止时间是[" + beginTime + "=>" + endTime + "]，查询时间过长，最长天数是:" + MAX_QUERY_PERIOD);
			map.put("message",  new EisMessage(EisError.timePeriodError.id,"错误的结束时间"));
		}
		withdrawCriteria.setBeginTimeBegin(queryBeginTime);
		withdrawCriteria.setBeginTimeEnd(queryEndTime);
		withdrawCriteria.setIncludeSubOrder(WithdrawCriteria.NO_SUB_ORDER);
		int queryStatus = ServletRequestUtils.getIntParameter(request,"status",0);
		if(queryStatus > 0){
			withdrawCriteria.setCurrentStatus(queryStatus);
		}
		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("找不到指定的商户[" + uuid + "]的密钥");
			map.put("message",  new EisMessage(EisError.dataError.id,"找不到指定的用户数据"));
			return frontMessageView;
		}	
		String timestamp = ServletRequestUtils.getStringParameter(request, "timestamp",null);
		if(!NumericUtils.isNumeric(timestamp)){
			logger.error("订单查询未提交必须的参数[timestamp]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "参数错误","订单查询未提交必须的参数[timestamp]"));
			return frontMessageView;		
		}

		String sign = ServletRequestUtils.getStringParameter(request, "sign", null);
		if(StringUtils.isBlank(sign)){
			logger.error("查询未提交签名sign");
			map.put("message",  new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交正确的参数:sign"));
			return frontMessageView;
		}
		Map<String,String> param = new HashMap<String,String>();
		param.put("uuid", String.valueOf(withdrawCriteria.getUuid()));
		param.put("beginTime",beginTime);
		param.put("endTime",endTime);
		param.put("timestamp",timestamp);


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
		sb.append("key=").append(loginKey);
		String signSource = sb.toString();

		String ourSign = DigestUtils.md5Hex(signSource);
		if(!ourSign.equalsIgnoreCase(sign)){
			map.put("message",  new EisMessage(EisError.VERIFY_ERROR.id,"校验失败"));
			return frontMessageView;

		}


		List<Withdraw> withdrawList = withdrawService.list(withdrawCriteria);
		if(withdrawList == null || withdrawList.size() < 1){
			logger.warn("根据条件[" + withdrawCriteria + "]找不到任何提现订单");
			return CommonStandard.frontMessageView;
		} 
		logger.warn("根据条件[" + withdrawCriteria + "]找到的订单是数量是:" + withdrawList.size());

		String fileName = csvFileSdf.format(new Date()) + (RandomUtils.nextInt(10000000) + 10000000) + ".csv";

		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentType("application/oct-stream");
		PrintWriter output = response.getWriter();
		sb.setLength(0);


		sb.append("系统订单号, 入口订单号,");
		if(isPlatformGenericPartner){
			sb.append("出口订单号,");
		}
		sb.append("渠道, 提现金额, 实际到账金额, 申请笔数, 成功笔数, 失败笔数, 手续费, 手续费类型, 发起时间, 结束时间, 状态\n");
		for (Withdraw p : withdrawList) {
			sb.append(p.getTransactionId().trim().toString()).append(",").append(p.getInOrderId()).append(",");
			if(isPlatformGenericPartner){
				sb.append(p.getOutOrderId()).append(",");
			}
			sb.append(p.getUuid()).append(",").append(df.format(p.getWithdrawMoney())).append(",").append(df.format(p.getArriveMoney())).append(',');
			if(p.getParentTransactionId() == null){
				//总笔数
				sb.append("1,");
				//成功笔数
				if(p.getCurrentStatus() == TransactionStatus.success.id){
					sb.append("1,");
				} else {
					sb.append("0,");
				}
				//失败笔数
				if(p.getCurrentStatus() == TransactionStatus.failed.id){
					sb.append("1,");
				} else {
					sb.append("0,");
				}
			} else {
				sb.append(p.getTotalRequest()).append(',').append(p.getSuccessRequest()).append(',').append(p.getFailRequest()).append(',');
			}
			sb.append(df.format(p.getCommission())).append(',').append(p.getCommissionType()).append(',').append(df.format(p.getArriveMoney())).append(",").append(sdf.format(p.getBeginTime())).append(",").append(p.getEndTime() == null ? "空" : sdf.format(p.getEndTime())).append(",");
			if (p.getCurrentStatus() == TransactionStatus.success.id) {
				sb.append(TransactionStatus.success.getName());
			} else if(p.getCurrentStatus() == TransactionStatus.inProcess.id){
				sb.append(TransactionStatus.inProcess.getName());
			} else if(p.getCurrentStatus() == TransactionStatus.newOrder.id){
				sb.append(TransactionStatus.newOrder.getName());
			} else if(p.getCurrentStatus() == TransactionStatus.failed.id){
				sb.append(TransactionStatus.failed.getName());
			} else {
				sb.append("错误");
			}
			sb.append("\n");
		}
		String csv = sb.toString();
		if(csv.length() > 200){
			logger.debug("输出CSV:" + csv.substring(200) + "...");
		} else {
			logger.debug("输出CSV:" + csv);

		}
		output.write(csv);
		output.close();
		withdrawList = null;
		return null;
	}

}

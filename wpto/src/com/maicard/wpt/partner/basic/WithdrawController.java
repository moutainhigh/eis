package com.maicard.wpt.partner.basic;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.annotation.StrictAuthorize;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.*;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.domain.*;
import com.maicard.money.service.*;
import com.maicard.product.service.NotifyService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.*;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.maicard.standard.CommonStandard.frontMessageView;

/**
 * 提现管理
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
	private ApplicationContextService applicationContextService;

	@Resource
	private AuthorizeService authorizeService;

	@Resource
	private BankAccountService bankAccountService;

	@Resource
	private CertifyService certifyService;

	@Resource
	private MoneyService moneyService;

	@Resource
	private PartnerService partnerService;

	@Resource
	private NotifyService notifyService;

	@Resource
	private WithdrawTypeService withdrawTypeService;

	@Resource
	private WithdrawService withdrawService;

	@Resource
	private WithdrawMethodService withdrawMethodService;

	private int rowsPerPage = 10;

	private final SimpleDateFormat csvFileSdf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			WithdrawCriteria withdrawCriteria) throws Exception {

		final String view = "common/withdraw/index";

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		withdrawCriteria.setOwnerId(ownerId);
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);



		if(authorizeService.havePrivilege(partner, ObjectType.withdraw.name(), Operate.download.code)){
			map.put("CSVDownload", "download" );
		}
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(withdrawCriteria, partner);
		}

		/*//收款人名称
		String fuzzyBankAccountName = ServletRequestUtils.getStringParameter(request, "fuzzyBankAccountName");
		String fuzzyBankAccountNumber = ServletRequestUtils.getStringParameter(request, "fuzzyBankAccountNumber");

		if(StringUtils.isNotBlank(fuzzyBankAccountName) || StringUtils.isNotBlank(fuzzyBankAccountNumber)){
			//查找对应的收款人
			BankAccountCriteria bankAccountCriteria = new BankAccountCriteria(ownerId);
			if(!isPlatformGenericPartner){
				partnerService.setSubPartner(withdrawCriteria, partner);
			}
			if(StringUtils.isNotBlank(fuzzyBankAccountNumber)){
				bankAccountCriteria.setBankAccountNumber(fuzzyBankAccountNumber.trim());
			} 
			if(StringUtils.isNotBlank(fuzzyBankAccountName)){
				bankAccountCriteria.setBankAccountName(fuzzyBankAccountName.trim());
			} 

			List<BankAccount> bankAccountList = bankAccountService.list(bankAccountCriteria);
			if(bankAccountList.size() > 0){
				List<Integer> bankAccountIdList = new ArrayList<Integer>();
				for(BankAccount bankAccount : bankAccountList){
					bankAccountIdList.add(bankAccount.getBankAccountId());
				}
				withdrawCriteria.setBankAccountIds(NumericUtils.list2Array(bankAccountIdList));
			}
		}*/

		int includeSubOrder = ServletRequestUtils.getIntParameter(request, "includeSubOrder",1);
		withdrawCriteria.setIncludeSubOrder(includeSubOrder);

		map.put("isPlatformGenericPartner",isPlatformGenericPartner);
		Money money = moneyService.select(partner.getUuid(), ownerId);
		//判断当前用户是否可以看到新增结算单按钮
		boolean canCreateWithdraw = authorizeService.havePrivilege(partner, ObjectType.withdraw.name(), Operate.create.code);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (canCreateWithdraw ? "可以" : "不能") + "手工提现");
/*
		if(canCreateWithdraw){
			map.put("isShow", "show" );
		}*/
		map.put("money", money);
		if(money != null && canCreateWithdraw && (money.getIncomingMoney() > 0 || money.getTransitMoney() > 0)){
			map.put("addUrl", "./withdraw/create.shtml");
		}

		UserCriteria partnerCriteria = new UserCriteria();
		partnerCriteria.setOwnerId(ownerId);
		partnerCriteria.setMerchant(true);
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(partnerCriteria, partner);
		}
		List<User> list = partnerService.list(partnerCriteria);
		map.put("inviterList", list);

		map.put("statusList",StatusUtils.getAllStatusValue(new String[]{}));


		int totalRows = withdrawService.count(withdrawCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据行数是0");
			return view;
		}
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		withdrawCriteria.setPaging(paging);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));



		int downloadMode = ServletRequestUtils.getIntParameter(request, "download", 0);
		List<Withdraw> withdrawList = null;
		if(downloadMode == 2){
			//下载所有数据
			withdrawList = withdrawService.list(withdrawCriteria);
		} else {
			withdrawList = withdrawService.listOnPage(withdrawCriteria);
		}

		map.put("requireSummary", withdrawCriteria.isRequireSummary());
		float totalWithdrawMoney = 0;
		float totalArriveMoney = 0;
		float totalCommission = 0;
		if(downloadMode < 2 && withdrawList != null && withdrawList.size() >0){
			for(Withdraw withdraw : withdrawList){
				//if(withdrawCriteria.isRequireSummary()){		
				//计算本页小计
				totalWithdrawMoney += withdraw.getWithdrawMoney();
				totalArriveMoney += withdraw.getArriveMoney();
				totalCommission += withdraw.getCommission();
				//}
				if(isPlatformGenericPartner){

					User withdrawUser = partnerService.select(withdraw.getUuid());
					if(withdrawUser == null){
						withdraw.setExtraValue("withdrawUserName", "未知");
					} else {
						withdraw.setExtraValue("withdrawUserName", withdrawUser.getNickName());
					}
				}
			}
		}
		map.put("totalWithdrawMoney", totalWithdrawMoney);
		map.put("totalArriveMoney", totalArriveMoney);
		map.put("totalCommission", totalCommission);

		map.put("total", totalRows);
		for (Withdraw withdraw : withdrawList) {
			withdraw.getOperate().put("get","./withdraw/get.shtml?transactionId="+withdraw.getTransactionId());
			withdraw.getOperate().put("update","./withdraw/update.json");
		}
		map.put("rows",withdrawList);
		if(downloadMode > 0 && withdrawList != null){
			//下载CSV
			String fileName = csvFileSdf.format(new Date()) + (RandomUtils.nextInt(10000000) + 10000000) + ".csv";
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			response.setContentType("application/oct-stream");
			PrintWriter output = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("商户号,商户订单号,平台订单号,开始时间,完成时间,提现金额,到账金额,手续费,状态,银行名称,开户人,银行账号\n");
			for(Withdraw withdraw : withdrawList){
				sb.append(withdraw.getUuid()).append(',').append(withdraw.getInOrderId()).append(',').append(withdraw.getTransactionId()).append(',').append(sdf.format(withdraw.getBeginTime())).append(',');
				if(withdraw.getEndTime() != null){
					sb.append(sdf.format(withdraw.getEndTime()));
				}
				sb.append(',');
				sb.append(withdraw.getWithdrawMoney()).append(',').append(withdraw.getArriveMoney()).append(',').append(withdraw.getCommission()).append(',');
				if(withdraw.getCurrentStatus() == TransactionStatus.success.id){
					sb.append("成功");
				} else if(withdraw.getCurrentStatus() == TransactionStatus.inProcess.id){
					sb.append("处理中");
				} else if(withdraw.getCurrentStatus() == TransactionStatus.newOrder.id){
					sb.append("新订单");
				} else if(withdraw.getCurrentStatus() == TransactionStatus.failed.id){
					sb.append("失败");
				} else {
					sb.append("错误");
				}
				if(withdraw.getBankAccount() != null){
					sb.append(',').append(withdraw.getBankAccount().getBankName()).append(',').append(withdraw.getBankAccount().getBankAccountName()).append(',').append(withdraw.getBankAccount().getBankAccountNumber());
				}
				sb.append("\n");


			}
			String csv = sb.toString();
			//logger.debug("输出CSV:" + csv);
			output.write(csv);
			output.close();
			return null;

		} else {
			//显示用户帐号中的所有银行类型
			BankAccountCriteria bankAccountCriteria = new BankAccountCriteria(ownerId);
			if(!isPlatformGenericPartner){
				bankAccountCriteria.setUuid(partner.getUuid());
			}
			bankAccountCriteria.setCurrentStatus(BasicStatus.normal.getId(), BasicStatus.relation.getId());
			List<BankAccount> bankAccountList = bankAccountService.list(bankAccountCriteria);
			Set<String> bankNames = new HashSet<String>();
			if(bankAccountList != null && bankAccountList.size() > 0){
				for(BankAccount ba : bankAccountList){
					bankNames.add(ba.getBankName());
				}
			}
			logger.debug("当前用户[" + partner.getUuid() + "]银行帐号共有" + bankNames.size() + "个不同的银行");
			map.put("bankNames",bankNames);
		}
		return view;
	}

	@RequestMapping(value = "/notify")
	@AllowJsonOutput
	public String notify(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, String transactionId
			)
					throws Exception {
		if(transactionId != null && transactionId.equals("batch")){
			String tid = ServletRequestUtils.getStringParameter(request, "transactionId");
			String[] tid2 = tid.split(",");
			int sendCount = 0;
			for(String tid1 : tid2){
				Withdraw withdraw = withdrawService.select(tid1.trim());
				if (withdraw == null) {
					logger.error("找不到指定的出款订单:" + tid1);
					continue;
				}
				sendCount++;
				notifyService.syncSendNotify(withdraw);



			}
			map.put("message", new EisMessage(OperateResult.success.getId(), "批量发送" + sendCount + "条通知"));
			return CommonStandard.partnerMessageView;

		} else {
			Withdraw i = withdrawService.select(transactionId);
			if (i == null) {

				logger.error("找不到指定的出款订单:" + transactionId);
				map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到指定的出款订单:" + transactionId));
				return CommonStandard.partnerMessageView;
			}
			String result = notifyService.syncSendNotify(i);
			map.put("message", new EisMessage(OperateResult.success.getId(), "发送结果:" + result));
		}

		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	@StrictAuthorize(token="withdraw", authType="payPassword", ttl=3600)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());

		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		//判断当前用户是否可以手工提现
		if(authorizeService.havePrivilege(partner, ObjectType.withdraw.name(), Operate.create.code)){
			logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴，对withdraw有create权限，可以新增提现");
			//对于内部用户才放入所有合作伙伴
			UserCriteria partnerCriteria = new UserCriteria(ownerId);
			partnerCriteria.setUserExtraTypeId(2);
			partnerCriteria.setCurrentStatus(UserStatus.normal.id);
			List<User> partnerList = partnerService.list(partnerCriteria);
			if(partnerList.size() > 0){
				map.put("partnerList", partnerList);
			}
			//long uuid = ServletRequestUtils.getLongParameter(request, "uuid", 0);

		} else {
			logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴，对withdraw没有create权限，不允许新增提现");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"无权提现","请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;		
		}

		//用户只能从指定IP访问才能使用手工提现功能
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
			map.put("message", new EisMessage(EisError.dataError.getId(), "IP地址错误","指定的用户配置错误"));
			return frontMessageView;	
		}

		boolean allowNewWithdrawBankAccount = partner.getBooleanExtraValue(DataName.allowNewWithdrawBankAccount.toString());
		map.put("allowNewWithdrawBankAccount", allowNewWithdrawBankAccount);
		Withdraw withdraw = new Withdraw();

		Money money = moneyService.select(partner.getUuid(), partner.getOwnerId());
		map.put("money", money);
		BankAccountCriteria bankAccountCriteria = new BankAccountCriteria();
		bankAccountCriteria.setOwnerId(ownerId);
		bankAccountCriteria.setCurrentStatus(BasicStatus.normal.getId(), BasicStatus.relation.getId());
		if(!isPlatformGenericPartner){
			bankAccountCriteria.setUuid(partner.getUuid());
		}
		List<BankAccount> bankAccountList = bankAccountService.list(bankAccountCriteria);

		int defaultWithdrawType = (int)partner.getLongExtraValue("withdrawType");
		boolean defaultWithdrawTypeIncluded = false;
		List<WithdrawType> withdrawTypeList = new ArrayList<WithdrawType>();

		String allowWithdrawType = partner.getExtraValue(DataName.allowWithdrawType.toString());
		if(StringUtils.isNotBlank(allowWithdrawType)){
			Set<Integer> allowWithdrawTypes = StringTools.getIntSetFromString(allowWithdrawType,",");
			if(allowWithdrawTypes != null && allowWithdrawTypes.size() > 0){
				for(int withdrawTypeId : allowWithdrawTypes){
					WithdrawType withdrawType = withdrawTypeService.select(withdrawTypeId);
					if(withdrawType != null){
						logger.debug("把用户[" + partner.getUuid() + "]可使用的提现类型[" + withdrawTypeId + "]放入");
						withdrawTypeList.add(withdrawType);
						if(withdrawTypeId == defaultWithdrawType){
							defaultWithdrawTypeIncluded = true;
						}
					}
				}
			}
		}

		if(!defaultWithdrawTypeIncluded){
			WithdrawType withdrawType = withdrawTypeService.select(defaultWithdrawType);
			if(withdrawType != null){
				logger.debug("把用户[" + partner.getUuid() + "]默认提现类型[" + defaultWithdrawType + "]放入");
				withdrawTypeList.add(withdrawType);
			}

		}

		map.put("withdrawTypeList", withdrawTypeList);

		map.put("bankAccountList",bankAccountList);
		map.put("withdraw",withdraw);
		return "common/withdraw/create";
	}


	@RequestMapping(value="/create", method=RequestMethod.POST)		
	@StrictAuthorize(token="withdraw", authType="payPassword", ttl=3600)
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			Withdraw withdraw) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());

		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		if(authorizeService.havePrivilege(partner, ObjectType.withdraw.name(), Operate.create.code)){
			logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴，对withdraw有create权限，可以新增提现");
		} else {
			logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴，对withdraw没有create权限，不允许新增提现");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"无权提现","请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;	
		}

		/*boolean allowNewWithdrawBankAccount = isPlatformGenericPartner && partner.getBooleanExtraValue(DataName.allowNewWithdrawBankAccount.toString());
		BankAccount bankAccount = null;

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
					map.put("message", new EisMessage(EisError.requiredDataNotFound.getId(), "参数错误"));
					return frontMessageView;
				}

				if(!bankAccount.getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BANK_ACCOUNT_TYPE_COMPANY) && !bankAccount.getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BAKC_ACCOUNT_TYPE_PERSONAL)){
					logger.warn("用户[" + partner.getUuid() + "]提交了提现申请的对应帐号但是银行账户类型不合法:" + bankAccount.getBankAccountType());
					map.put("message", new EisMessage(EisError.requiredDataNotFound.getId(), "参数错误"));
					return frontMessageView;
				}
				int createBankRs = bankAccountService.createIfNotExist(bankAccount);
				if(createBankRs == EisError.accountError.id){
					logger.error("无法创建提交的银行帐号数据:" + bankAccount);
					map.put("message", new EisMessage(EisError.accountError.getId(), "无法创建银行帐号"));
					return frontMessageView;		
				}
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
						map.put("message", new EisMessage(EisError.accountError.getId(),"找不到指定的提现帐号"));
						return CommonStandard.frontMessageView;	
					}
				}
			}
		}*/

		if(withdraw.getBankAccountId() > 0){
			//查找对应的提现帐号
			BankAccount bankAccount = bankAccountService.select(withdraw.getBankAccountId());
			if(bankAccount == null){
				logger.error("找不到提现请求中的银行帐号:" + withdraw.getBankAccountId());
				map.put("message", new EisMessage(EisError.ACCOUNT_ERROR.getId(),"找不到指定的银行帐号"));
				return CommonStandard.partnerMessageView;		
			}
			if(bankAccount.getUuid() != partner.getUuid()){
				logger.warn("提现请求中的银行帐号:" + withdraw.getBankAccountId() + ",其所有者[" + bankAccount.getUuid() + "]与当前用户[" + partner.getUuid() + "]不一致");
				//内部用户可以使用任意的银行账号
				if(!isPlatformGenericPartner){
					map.put("message", new EisMessage(EisError.ACCOUNT_ERROR.getId(),"找不到指定的银行帐号"));
					return CommonStandard.partnerMessageView;		
				}
			}				
			withdraw.setBankAccount(bankAccount);
		} else {
			logger.error("提现请求中未提交银行帐号ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"请选择银行帐号"));
			return CommonStandard.partnerMessageView;	
		}

		withdraw.setOwnerId(ownerId);
		withdraw.setUuid(partner.getUuid());
		if(withdraw.getWithdrawMoney() <= 0){
			logger.error("申请提现金额为0");
			map.put("message", new EisMessage(EisError.moneyRangeError.getId(),"提现金额不能为0"));
			return CommonStandard.partnerMessageView;		
		}
		if(withdraw.getWithdrawTypeId() < 1){
			logger.error("申请提现类型未选择");
			map.put("message", new EisMessage(EisError.moneyRangeError.getId(),"请选择提现类型"));
			return CommonStandard.partnerMessageView;		
		}
		boolean isValidWithdrawType = false;

		int defaultWithdrawType = (int)partner.getLongExtraValue("withdrawType");
		if(defaultWithdrawType > 0 && defaultWithdrawType == withdraw.getWithdrawTypeId()){
			isValidWithdrawType = true;
		} else {
			String allowWithdrawType = partner.getExtraValue(DataName.allowWithdrawType.toString());
			if(StringUtils.isNotBlank(allowWithdrawType)){
				Set<Integer> allowWithdrawTypes = StringTools.getIntSetFromString(allowWithdrawType,",");
				if(allowWithdrawTypes != null && allowWithdrawTypes.size() > 0){
					for(int withdrawTypeId : allowWithdrawTypes){
						if(withdrawTypeId == withdraw.getWithdrawTypeId()){
							isValidWithdrawType = true;
							break;
						}
					}
				}
			}
		}
		if(!isValidWithdrawType){
			logger.error("不合法的提现类型:" + withdraw.getWithdrawTypeId());
			map.put("message", new EisMessage(EisError.moneyRangeError.getId(),"不支持的提现类型"));
			return CommonStandard.partnerMessageView;		
		}



		int valid = withdrawService.isValidWithdraw(withdraw);
		if(valid != OperateResult.success.getId()){
			map.put("message", new EisMessage(valid,"提现申请失败"));
			return CommonStandard.partnerMessageView;		
		}

		StringBuffer sb2 = new StringBuffer();
		sb2.append(request.getProtocol().toLowerCase().startsWith("https") ? "https" : "http").append("://");

		sb2.append(request.getServerName());

		sb2.append(":").append(request.getServerPort()).append(request.getRequestURI());
		String currentUrl = sb2.toString();
		withdraw.setExtraValue("currentUrl", currentUrl);


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
			map.put("message", new EisMessage(EisError.DATA_UPDATE_FAIL.id,"数据操作失败"));
			return CommonStandard.partnerMessageView;
		}

		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}

	/*
	 * 完成结算后，审批某个结算单
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)		
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String transactionId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());

		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"无权访问","请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;	
		}
		Withdraw withdraw = withdrawService.select(transactionId);
		if(withdraw == null){
			logger.error("找不到要审批的结算单:" + transactionId);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"对象错误","找不到指定的结算单"));
			return CommonStandard.partnerMessageView;	
		}
		if(withdraw.getOwnerId() != ownerId){
			logger.error("要审批的结算单:" + transactionId + ",其ownerId=" + withdraw.getOwnerId() + "与系统当前的:" + ownerId + "不一致");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"对象错误","找不到指定的结算单"));
			return CommonStandard.partnerMessageView;	
		}
		if(withdraw.getCurrentStatus() == TransactionStatus.newOrder.id){
			withdraw.setCurrentStatus(TransactionStatus.inProcess.id);
		} else {
			map.put("message", new EisMessage(EisError.statusAbnormal.id,"状态错误","指定的结算单状态不对"));
			return CommonStandard.partnerMessageView;	
		}

		EisMessage message = null;
		int rs = withdrawService.update(withdraw);
		if(rs == OperateResult.success.getId()){
			message = new EisMessage(OperateResult.success.getId(),"提现操作成功");			
		} else {
			message = new EisMessage(rs,"提现操作失败");				
		}



		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}



	/*
	 * 提现完成后的回调通知处理
	 * 调用对应的withdrawProcessor的onResult接口方法，并得到EisMessage结果
	 * 处理完成后仅向支付商返回success等字符
	 */
	@RequestMapping("/notify/{withdrawMethodId}")
	@IgnoreLoginCheck
	@AllowJsonOutput
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


	@RequestMapping(value="/get")		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			WithdrawCriteria withdrawCriteria) throws Exception {
		final String view = "common/withdraw/get";

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		withdrawCriteria.setOwnerId(ownerId);
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(withdrawCriteria, partner);
		}

		Withdraw withdraw = withdrawService.select(withdrawCriteria.getTransactionId());
		int withdrawTypeId = withdraw.getWithdrawTypeId();
		WithdrawType withdrawType = withdrawTypeService.select(withdrawTypeId);
		if (withdrawType != null) {
			withdraw.setExtraValue("withdrawTpe", withdrawType.getWithdrawTypeName());
		}
		WithdrawMethod withdrawMethod = withdrawMethodService.select(withdraw.getWithdrawMethodId());
		if (withdrawMethod != null) {
			withdraw.setExtraValue("withdrawMethod", withdrawMethod.getName());
		}
		User user = partnerService.select(withdraw.getUuid());
		logger.debug("提现用户的uuid为"+withdraw.getUuid());
		if (user != null) {
			withdraw.setExtraValue("withdrawUserName", user.getUsername());
		}
		//判断当前用户是否可以看到新增结算单按钮
		if(isPlatformGenericPartner){
			map.put("isPlatformGenericPartner", "true" );
		}
		map.put("withdraw", withdraw);
		withdrawCriteria.setTransactionId(null);
		withdrawCriteria.setParentTransactionId(withdraw.getTransactionId());
		List<Withdraw> subWithdrawList = withdrawService.list(withdrawCriteria);
		if (subWithdrawList.size() > 0) {
			for(Withdraw subWithdraw : subWithdrawList){
				WithdrawMethod withdrawMethod2 = withdrawMethodService.select(subWithdraw.getWithdrawMethodId());
				if (withdrawMethod2 != null) {
					subWithdraw.setExtraValue("withdrawMethod", withdrawMethod2.getName());
				}
			}
			map.put("childTransaction", subWithdrawList);
		}
		return view;
	}

	@RequestMapping(value = "/confirm")
	@AllowJsonOutput
	public String reconfirm(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,String confirmTransactionId)
					throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean force = true;
		if(force){
			WithdrawCriteria payCriteria = new WithdrawCriteria(ownerId);
			payCriteria.setCurrentStatus(TransactionStatus.inProcess.id);
			//通过时间段查询
			String beginTime = ServletRequestUtils.getStringParameter(request, "beginTime");
			if(StringUtils.isNotEmpty(beginTime)){
				payCriteria.setBeginTimeBegin(sdf.parse(beginTime));
			}
			String endTime = ServletRequestUtils.getStringParameter(request, "endTime");
			if(StringUtils.isNotEmpty(endTime)){
				payCriteria.setBeginTimeEnd(DateUtils.addMinutes(payCriteria.getBeginTimeBegin(), 60));
				endTime = sdf.format(payCriteria.getBeginTimeEnd());
			}
			if(StringUtils.isNotEmpty(confirmTransactionId)){
				payCriteria.setTransactionId(confirmTransactionId);
			}
			List<Withdraw> payList = withdrawService.list(payCriteria);
			if(null==payList || payList.size()<1){
				logger.error("当前时间段[" + beginTime + "=>" + endTime + "]内没有处理中的支付订单");
			}
			for(Withdraw withdraw : payList){
				Thread.sleep(1000);
				WithdrawMethod withdrawMethod = withdrawMethodService.select(withdraw.getWithdrawMethodId());
				if(withdrawMethod == null){
					logger.error("找不到批付订单[" + withdraw.getTransactionId() + "]的批付方法:" + withdraw.getWithdrawMethodId());
					continue;
				}

				Withdraw queryWithdraw = withdraw.clone();

				WithdrawProcessor withdrawProcessor = applicationContextService.getBeanGeneric(withdrawMethod.getProcessClass());
				EisMessage queryResult = withdrawProcessor.onQuery(queryWithdraw);

				logger.info("批付订单[" + withdraw.getTransactionId() + "]的查询结果是:" + queryResult + ",查询后的批付订单是:" + queryWithdraw);

				if(queryResult.getOperateCode() == OperateResult.success.id){
					withdraw.setCurrentStatus(queryWithdraw.getCurrentStatus());
					withdrawService.update(withdraw);
				}
			}

			return CommonStandard.partnerMessageView;

		}


		String[] tid = ServletRequestUtils.getStringParameters(request, "transactionId");
		if(tid.length < 1){
			WithdrawCriteria payCriteria = new WithdrawCriteria(ownerId);
			payCriteria.setCurrentStatus(TransactionStatus.inProcess.id);
			//通过时间段查询
			String beginTime = ServletRequestUtils.getStringParameter(request, "beginTime");
			if(StringUtils.isBlank(beginTime)){
				logger.error("再次确认请求中既没有订单号列表也没有开始时间");
			}
			payCriteria.setBeginTimeBegin(sdf.parse(beginTime));

			String endTime = ServletRequestUtils.getStringParameter(request, "endTime");
			if(StringUtils.isBlank(endTime)){
				payCriteria.setBeginTimeEnd(DateUtils.addMinutes(payCriteria.getBeginTimeBegin(), 60));
				endTime = sdf.format(payCriteria.getBeginTimeEnd());
			}

			List<Withdraw> payList = withdrawService.list(payCriteria);
			if(payList.size() < 1){
				logger.error("当前时间段[" + beginTime + "=>" + endTime + "]内没有处理中的批付订单");
				map.put("message", new EisMessage(EisError.dataNotFoundInSystem.getId(), "当前时间段[" + beginTime + "=>" + endTime + "]内没有处理中的批付订单"));
				return CommonStandard.partnerMessageView;
			}
			tid = new String[ payList.size() ];
			for(Withdraw withdraw : payList){
				WithdrawMethod withdrawMethod = withdrawMethodService.select(withdraw.getWithdrawMethodId());
				if(withdrawMethod == null){
					logger.error("找不到批付订单[" + withdraw.getTransactionId() + "]的批付方法:" + withdraw.getWithdrawMethodId());
				}

				Withdraw queryWithdraw = withdraw.clone();

				WithdrawProcessor withdrawProcessor = applicationContextService.getBeanGeneric(withdrawMethod.getProcessClass());
				EisMessage queryResult = withdrawProcessor.onQuery(queryWithdraw);

				logger.info("批付订单[" + withdraw.getTransactionId() + "]的查询结果是:" + queryResult + ",查询后的批付订单是:" + queryWithdraw);

				if(queryResult.getOperateCode() == OperateResult.success.id && queryWithdraw.getCurrentStatus() == TransactionStatus.success.id && queryWithdraw.getArriveMoney() > 0){
					//把老订单状态修改并发送通知
					withdraw.setCurrentStatus(queryWithdraw.getCurrentStatus());
					withdraw.setArriveMoney(queryWithdraw.getArriveMoney());
					withdrawService.update(withdraw);

					ModelMap resultMap = new ModelMap();
					resultMap.put("currentStatus",withdraw.getCurrentStatus());
					resultMap.put("realMoney", withdraw.getArriveMoney());
					String result = notifyService.syncSendNotify(withdraw);
					resultMap.put("notifyResult", result);
					map.put(withdraw.getTransactionId(), resultMap);

				} 
			}


		} else {


			for(String transactionId : tid){
				Withdraw withdraw = withdrawService.select(transactionId);
				if (withdraw == null) {
					logger.error("找不到指定的批付订单:" + transactionId);
					map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到指定的订单:" + transactionId));
					return CommonStandard.partnerMessageView;
				}

				WithdrawMethod withdrawMethod = withdrawMethodService.select(withdraw.getWithdrawMethodId());
				if(withdrawMethod == null){
					logger.error("找不到批付订单[" + withdraw.getTransactionId() + "]的批付方法:" + withdraw.getWithdrawMethodId());
				}

				Withdraw queryPay = withdraw.clone();

				WithdrawProcessor payProcessor = applicationContextService.getBeanGeneric(withdrawMethod.getProcessClass());
				EisMessage queryResult = payProcessor.onQuery(queryPay);

				logger.info("批付订单[" + withdraw.getTransactionId() + "]的查询结果是:" + queryResult + ",查询后的批付订单是:" + queryPay);

				if (queryResult != null) {
					if(queryResult.getOperateCode() == OperateResult.success.id && queryPay.getCurrentStatus() == TransactionStatus.success.id && queryPay.getArriveMoney() > 0){
						//把老订单状态修改并发送通知
						withdraw.setCurrentStatus(queryPay.getCurrentStatus());
						withdraw.setArriveMoney(queryPay.getArriveMoney());
						withdrawService.update(withdraw);

						ModelMap resultMap = new ModelMap();
						resultMap.put("currentStatus",withdraw.getCurrentStatus());
						resultMap.put("realMoney", withdraw.getArriveMoney());
						String result = notifyService.syncSendNotify(withdraw);
						resultMap.put("notifyResult", result);
						map.put(transactionId, resultMap);
					} 
				}else {
					map.put("message", new EisMessage(OperateResult.failed.getId(), "批付订单[" + withdraw.getTransactionId() + "]的查询结果是:null"));
					return CommonStandard.partnerMessageView;
				}

			}
		}
		return CommonStandard.partnerMessageView;
	}


}

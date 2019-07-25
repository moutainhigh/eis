package com.maicard.wpt.partner.controller;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.billing.criteria.BillingCriteria;
import com.maicard.billing.domain.Billing;
import com.maicard.billing.service.BillingService;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.StatusUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.ts.SettlementStatus;

/**
 * 结算单列表
 * 
 * @author hailong
 * @date 2017-6-27
 * 
 */
@Controller
@RequestMapping("/billing")
public class BillingController extends BaseController {
	private final SimpleDateFormat sdf = new SimpleDateFormat(
			CommonStandard.defaultDateFormat);
	private final SimpleDateFormat csvFileSdf = new SimpleDateFormat(
			CommonStandard.orderIdDateFormat);
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private BillingService billingService;
	@Resource
	private MoneyService moneyService;

	@Resource
	private PayMethodService payMethodService;


	final private DecimalFormat df = new DecimalFormat("0.00");
	private int rowsPerPage = 10;

	@PostConstruct
	public void init() {
		rowsPerPage = configService.getIntValue(
				DataName.partnerRowsPerPage.toString(), 0);
		if (rowsPerPage < 1) {
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, BillingCriteria billingCriteria, boolean requireSummary)
					throws Exception {
		final String view = "common/billing/index";
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////

		if (!isPlatformGenericPartner) {
			partnerService.setSubPartner(billingCriteria, partner);
			logger.debug("***************************************************************************************************");
		}
		//判断当前用户是否可以看到新增结算单按钮
		if(isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.billing.name(), "w")){
			map.put("isShow", "show" );
		}
		if(isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.billing.name(), "download")){
			map.put("CSVDownload", "download" );
		}
		//状态码
		List<Integer> statusList = StatusUtils.getStatusList("SettelmentStatus");
		statusList.add(EisError.moneyNotEnough.id);
		
		map.put("statusList", statusList);
		// 分页功能
		int rows = ServletRequestUtils.getIntParameter(request, "rows",
				rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);



		String username = ServletRequestUtils.getStringParameter(request,
				"username");
/*		String currentStatus = ServletRequestUtils.getStringParameter(request,
				"currentStatus");
		int[] arr = new int[statusList.size()];
		for (int i = 0; i < statusList.size(); i++) {
			arr[i]=statusList.get(i);
		}
		if (StringUtils.isBlank(currentStatus) || Integer.parseInt(currentStatus)==0) {
			billingCriteria.setCurrentStatus(arr);
		}*/

		if (StringUtils.isNotBlank(username)) {
			/*UserCriteria partnerCriteria = new UserCriteria();
			partnerCriteria.setUsername(username);
			List<User> list = partnerService.list(partnerCriteria);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				billingCriteria.setUuid(user.getUuid());
			}*/
			if(StringUtils.isNumeric(username.trim())){
				User frontUser = partnerService.select(Long.parseLong(username.trim()));
				if(frontUser == null){
					logger.warn("找不到UUID=" + username + "的商户");
					return view;
				}
				if(frontUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + username + "对应的商户，其ownerid[" + frontUser.getOwnerId() + "]与指定的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				billingCriteria.setUuid(Integer.parseInt(username));
				logger.info("查询的商户是:" + username + ",UUID=" + username.trim());
			} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(username.trim());
				List<User> frontUserList = partnerService.list(frontUserCriteria);
				if(frontUserList == null || frontUserList.size() < 1){
					logger.warn("找不到昵称=" + partner + "商户");
					frontUserCriteria.setUsername(username.trim());
					frontUserList = partnerService.list(frontUserCriteria);
					if(frontUserList == null || frontUserList.size() < 1){
						logger.warn("找不到用户名=" + username + "商户");
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if(frontUserList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + username + "对应的商户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与指定的ownerId[" + username + "]不匹配");
					return view;
				}
				billingCriteria.setUuid(frontUserList.get(0).getUuid());
				logger.info("查询的发件人名是:" + username + ",UUID=" + frontUserList.get(0).getUuid());

			}
		}

		billingCriteria.setOwnerId(ownerId);

		int totalRows = billingService.count(billingCriteria);
		map.put("total", totalRows);
		Paging paging = new Paging(rows);
		billingCriteria.setPaging(paging);
		billingCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 "
				+ page + " 页。");

		int downloadMode = ServletRequestUtils.getIntParameter(request,
				"download", 0);
		List<Billing> list = null;
		if (downloadMode == 2) {
			// 下载所有数据
			list = billingService.list(billingCriteria);
		} else {
			list = billingService.listOnPage(billingCriteria);
		}

		authorizeService.writeOperate(partner, list);

		if (list != null && list.size() > 0) {
			for (Billing billing : list) {
				if(billing.getUuid() > 0){
					User user = partnerService.select(billing.getUuid());
					if (user != null) {
						billing.setOperateValue("username", user.getNickName()== null ? user.getUsername():user.getNickName()+"/"+user.getUuid());
					}else {
						logger.debug("找不到结算单所对应的商户:"+billing.getUuid());
						billing.setOperateValue("username", "未知/"+billing.getUuid());
					}
				} else if(billing.getObjectId() > 0){
					if(billing.getObjectType() != null && billing.getObjectType().equalsIgnoreCase(ObjectType.payMethod.toString())){
						PayMethod payMethod = payMethodService.select((int)billing.getObjectId());
						if(payMethod != null){
							billing.setOperateValue("username", payMethod.getName() + "/" + billing.getObjectId());
						}
					} else {
						billing.setOperateValue("username", billing.getObjectType() + "/" + billing.getObjectId());
					}
				}
			}
		}

		map.put("billingList", list);


		double totalFaceMoney = 0;
		double totalRealMoney = 0;
		double totalCommission = 0;
		int count = 0;
		if (downloadMode < 2 && list != null && list.size() > 0) {
			for (Billing billing : list) {
				count++;
				totalFaceMoney += billing.getFaceMoney();
				totalRealMoney += billing.getRealMoney();
				totalCommission += billing.getCommission();
			}

		}

		map.put("totalFaceMoney", totalFaceMoney);
		map.put("totalRealMoney", totalRealMoney);
		if (count > 0) {
			map.put("totalCommission", df.format(totalCommission/count));
		}
		map.put("requireSummary", requireSummary);
		logger.debug("下载：" + downloadMode);
		if (downloadMode > 0 && list != null && 
				authorizeService.havePrivilege(partner, ObjectType.billing.name(), "download")) {
			// 下载CSV
			logger.debug("下载CSV");
			String fileName = csvFileSdf.format(new Date())
					+ (RandomUtils.nextInt(10000000) + 10000000) + ".csv";
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName);
			response.setContentType("application/oct-stream");
			PrintWriter output = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("结算单ID,对应用户,开始时间,结束时间,交易金额(元),结算金额(元),实际完成金额(元),佣金(元), 状态\r\n"
					.trim().toString());
			for (Billing billing : list) {
				sb.append(billing.getBillingId()).append(",")
				.append(billing.getOperate().get("username").trim().toString())
				.append(",")
				.append(sdf.format(billing.getBillingBeginTime()))
				.append(",")
				.append(sdf.format(billing.getBillingEndTime()))
				.append(",").append(billing.getFaceMoney()).append(",")
				.append(billing.getRealMoney()).append(",")
				.append(billing.getCommission()).append(",");
				if (billing.getCurrentStatus() == SettlementStatus.ackedByClient.id) {
					sb.append(SettlementStatus.ackedByClient.name.trim().toString());
				} else if (billing.getCurrentStatus() == SettlementStatus.unknown.id) {
					sb.append(SettlementStatus.unknown.name.trim().toString());
				} else if (billing.getCurrentStatus() == SettlementStatus.ackedByFinance.id) {
					sb.append(SettlementStatus.ackedByFinance.name.trim().toString());
				} else if (billing.getCurrentStatus() == SettlementStatus.billed.id) {
					sb.append(SettlementStatus.billed.name.trim().toString());
				} else if (billing.getCurrentStatus() == SettlementStatus.settled.id) {
					sb.append(SettlementStatus.settled.name.trim().toString());
				} else if (billing.getCurrentStatus() == SettlementStatus.submitToBank.id) {
					sb.append(SettlementStatus.submitToBank.name.trim().toString());
				} else if (billing.getCurrentStatus() == SettlementStatus.generated.id) {
					sb.append(SettlementStatus.generated.name.trim().toString());
				} else {
					sb.append("错误");
				}
				sb.append("\r\n");
			}
			String csv = sb.toString();
			logger.debug("输出CSV:" + csv);
			output.write(csv);
			output.close();
			return null;
		} else {
			authorizeService.writeOperate(partner, list);
			map.put("billingList", list);
		}

		if (isPlatformGenericPartner) {
			UserCriteria partnerCriteria = new UserCriteria();
			partnerCriteria.setOwnerId(ownerId);
			partnerCriteria.setUserTypeId(121002);
			List<User> partnerList = partnerService.list(partnerCriteria );
			map.put("partner", partnerList);
		}
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			Billing billing) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		EisMessage message = null;
		if (!isPlatformGenericPartner) {
			message=new EisMessage(OperateResult.failed.getId(), "没有访问权限");
			map.put("message", message);
			return view;
		}
		if (!authorizeService.havePrivilege(partner, ObjectType.billing.name(), "w")) {
			message=new EisMessage(OperateResult.failed.getId(), "没有访问权限");
			map.put("message", message);
			return view;
		}


		billing.setOwnerId(ownerId);
		billing.setObjectType(ObjectType.pay.toString());
		billing.setCreateTime(new Date(System.currentTimeMillis()));
		billing.setCurrentStatus(SettlementStatus.billed.id);
		billing.setBillingHandlerTime(new Date(System.currentTimeMillis()));
		billing.setOperator(partner.getUuid());



		logger.debug(billing.getBillingBeginTime()+"*****************"+billing.getBillingEndTime()+"*****"+billing.getUuid()+"******"+billing.getBillingHandlerTime()+billing.getFaceMoney()+"*****"+billing.getRealMoney());
		logger.debug(billing.getObjectType()+"***********+++++++++++++++++++++++++++++++++++");
		//补结算     除了新增一条记录之外，还需要给对应商户的资金做处理
		String supplement = ServletRequestUtils.getStringParameter(request, "supplement");

		//该补结算资金是否要从商户的未结算资金（incomingMoney）中扣除，如果是1，则先从incomingMoney扣除，否则直接增加资金
		String fromIncomingMoney = ServletRequestUtils.getStringParameter(request, "fromIncomingMoney");
		logger.info("fromIncomingMoney=" + fromIncomingMoney);
		if (StringUtils.isNotBlank(supplement)) {
			if(fromIncomingMoney != null && billing.getRealMoney() > 0){
				logger.info("对商户:" + billing.getUuid() + "的补结算操作要求先从未结算资金中扣款，扣款金额是:" + billing.getRealMoney());
				Money minusMoney = new  Money(billing.getUuid(),ownerId);
				minusMoney.setIncomingMoney(billing.getRealMoney());
				EisMessage minusResult = moneyService.minus(minusMoney);
				if(minusResult.getOperateCode() != OperateResult.success.id){
					logger.error("先从资金账户:" + minusMoney.getUuid() + "扣款:" + billing.getRealMoney() + "失败，返回是:" + minusResult);
					map.put("message",  new EisMessage(OperateResult.failed.getId(), "先扣款操作失败"));
					return view;
				}

			}
			Money plusMoney = new Money(billing.getUuid(),ownerId);
			plusMoney.setTransitMoney(billing.getRealMoney());
			if(billing.getRealMoney() > 0){
				moneyService.plus(plusMoney);
			} else {
				//结算资金小于0的情况，是扣款
				plusMoney.setTransitMoney(Math.abs(plusMoney.getTransitMoney()));
				moneyService.minus(plusMoney);
			}
			billing.setExtraValue("memory", supplement);
		}
		if (billingService.insert(billing) == 1) {
			message = new EisMessage(OperateResult.success.getId(), "操作完成");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "操作失败");
		}
		map.put("message", message);
		return view;
	}


	@RequestMapping(value="/get" + "/{billingId}")		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("billingId") int billingId) throws Exception {
		final String view = "common/billing/get";
		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			throw new UserNotFoundInRequestException();
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
		//////////////////////// 结束标准流程 ///////////////////////
		Billing billing = billingService.select(billingId);
		if (!isPlatformGenericPartner) {
			//不是内部用户，检查是不是访问自己或下级账户
			if(partner.getUuid() == billing.getUuid() || partnerService.isValidSubUser(partner.getUuid(), billing.getUuid())){
				//用户合法访问
			} else {
				map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
				return CommonStandard.partnerMessageView;
			}
		}

		HashMap<String, String> operate = new HashMap<>();
		//操作人
		User user = partnerService.select(billing.getOperator());
		//对应的商户
		User user2 = partnerService.select(billing.getUuid());		
		if (user != null) {
			operate.put("operator", user.getUsername());
		}
		if (user2 != null) {
			operate.put("username", user2.getUsername());
		}
		billing.setOperate(operate);
		map.put("billing", billing);
		return view;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		EisMessage message = null;
		if (!isPlatformGenericPartner) {
			message=new EisMessage(OperateResult.failed.getId(), "没有访问权限");
			map.put("message", message);
			return view;
		}

		String mode = ServletRequestUtils.getStringParameter(request, "mode");
		if(mode.equalsIgnoreCase("arrive")){
			int billingId = ServletRequestUtils.getIntParameter(request, "billingId", 0);
			float arriveMoney = ServletRequestUtils.getFloatParameter(request, "arriveMoney", 0);
			Billing billing = billingService.select(billingId);
			if(billing == null){
				logger.error("找不到指定的账单:" + billingId);
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"找不到指定的账单"));
				return CommonStandard.partnerMessageView;
			}
			if(billing.getCurrentStatus() != SettlementStatus.generated.id){
				logger.error("指定的账单:" + billingId + "状态是:" + billing.getCurrentStatus() + ",不能确认");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"该账单不允许确认操作"));
				return CommonStandard.partnerMessageView;
			}
			billing.setArriveMoney(arriveMoney);

			billing.setCurrentStatus(SettlementStatus.billed.id);
			billing.setBillingHandlerTime(new Date());
			billing.setOperator(partner.getUuid());


			if (billingService.update(billing) == 1) {
				message = new EisMessage(OperateResult.success.getId(), "操作完成");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "操作失败");
			}
			map.put("message", message);
		}
		return view;
	}

}

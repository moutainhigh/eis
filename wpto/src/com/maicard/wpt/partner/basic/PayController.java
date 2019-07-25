package com.maicard.wpt.partner.basic;


import static com.maicard.standard.CommonStandard.partnerMessageView;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.StatusUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.domain.PayType;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.service.PayStatService;



@Controller
@RequestMapping("/pay")
public class PayController extends BaseController{

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	
	@Resource
	private MessageService messageService;
	
	@Resource
	private NotifyService notifyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayService payService;

	@Resource
	private PayMethodService payMethodService;

	@Resource
	private PayTypeService payTypeService;
	
	@Resource
	private PayStatService payStatService;
	
	@Resource
	private AuthorizeService authorizeService;

	private int rowsPerPage = 10;
	private final SimpleDateFormat csvFileSdf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			PayCriteria payCriteria) throws Exception {
		final String view = "common/pay/list";
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
		payCriteria.setOwnerId(partner.getOwnerId());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1); 

		String payFromUserName = null;
		payFromUserName = ServletRequestUtils.getStringParameter(request, "payFromUserName");

		if(StringUtils.isNotBlank(payFromUserName)){
			if(StringUtils.isNumeric(payFromUserName.trim())){
				User payFromUser = null;
				if(payCriteria.getPayFromAccountType() == UserTypes.frontUser.getId()){
					payFromUser = frontUserService.select(Long.parseLong(payFromUserName.trim()));
				} else {
					payFromUser = partnerService.select(Long.parseLong(payFromUserName.trim()));

				}
				if(payFromUser == null){
					logger.warn("找不到UUID=" + payFromUserName + "的用户，查询用户类型是:" + payCriteria.getPayFromAccountType());
					return view;
				}
				if(payFromUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + payFromUserName + "对应的用户，其ownerid[" + payFromUser.getOwnerId() + "]与指定的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				payCriteria.setPayFromAccount(Long.parseLong(payFromUserName.trim()));
				logger.info("查询的用户名是:" + payFromUserName + ",UUID=" + payFromUserName.trim());
			} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(payFromUserName.trim());
				
				List<User> userList = null;
				if(payCriteria.getPayFromAccountType() == UserTypes.partner.getId()){
					userList = partnerService.list(frontUserCriteria);
				} else {
					userList = frontUserService.list(frontUserCriteria);

				}
				if(userList == null || userList.size() < 1){
					logger.warn("找不到昵称=" + payFromUserName + "的用户,查询用户类型:" + payCriteria.getPayFromAccountType());
					UserCriteria frontUserCriteria1 = new UserCriteria();
					frontUserCriteria1.setUsername(payFromUserName.trim());
					userList = frontUserService.list(frontUserCriteria1 );
					if(userList == null || userList.size() < 1){
						logger.warn("找不到用户名=" + payFromUserName + "的用户,查询用户类型:" + payCriteria.getPayFromAccountType());
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if(userList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + payFromUserName + "对应的用户，其ownerid[" + userList.get(0).getOwnerId() + "]与指定的ownerId[" + payFromUserName + "]不匹配");
					return view;
				}
				payCriteria.setPayFromAccount(userList.get(0).getUuid());
				logger.info("查询的人名是:" + payFromUserName + ",UUID=" + userList.get(0).getUuid());

			}
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(payCriteria, partner);
		}
		
		if(authorizeService.havePrivilege(partner, ObjectType.pay.name(), "download")){
			map.put("CSVDownload", "download" );
		}
/*
		if (payCriteria.getRefBuyTransactionId()!= null && payCriteria.getRefBuyTransactionId().equals("")) {
			payCriteria.setRefBuyTransactionId(null);
		}*/
		int totalRows = payService.count(payCriteria);
		map.put("total", totalRows);
		
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria(ownerId);
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		map.put("payTypeList", payTypeList);

		map.put("statusList",StatusUtils.getAllStatusValue(new String[]{}));
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}

		Paging paging = new Paging(rows);
		payCriteria.setPaging(paging);
		payCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");

		int downloadMode = ServletRequestUtils.getIntParameter(request, "download", 0);
		List<Pay> payList = null;
		if(downloadMode == 2){
			/*Calendar calendar=Calendar.getInstance();   
			calendar.setTime(new Date()); 	//当前时间
			int today = calendar.get(Calendar.DAY_OF_MONTH); //当前日期
			calendar.set(Calendar.DAY_OF_MONTH, today - 2);//让日期加-2   两天之前的日期
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			payCriteria.setStartTime(sdf.format(calendar.getTime()));*/

			//下载所有数据
			payList = payService.list(payCriteria);
		} else {
			payList = payService.listOnPage(payCriteria);
		}

		authorizeService.writeOperate(partner, payList);
		List<Pay> payList2 = new ArrayList<Pay>();
		if(payList != null && payList.size() > 0){
			for(Pay pay : payList){
				Pay pay2 = pay.clone();
				if(!isPlatformGenericPartner){
					pay2.setOutOrderId(null);
				}

				//如果用户是渠道，则“不显示入口订单号、出口订单号，增加一个渠道字段”
				User user = partnerService.select(pay.getInviter());
				if (user != null) {
					if (user.getUserExtraTypeId() > 0) {
						//						pay2.setInOrderId(null);
						pay2.setOutOrderId(null);
						pay2.setDescription(user.getNickName());
					}
				}
				/*User payUser = null;
				if(pay.getPayFromAccount() > 0){
					if(pay.getPayFromAccountType() == UserTypes.partner.getId()){
						payUser = partnerService.select(pay.getPayFromAccount());
					} else {
						payUser = frontUserService.select(pay.getPayFromAccount());
					}
				} else if(pay.getPayToAccount() > 0){
					if(pay.getPayToAccountType() == UserTypes.partner.getId()){
						payUser = partnerService.select(pay.getPayToAccount());
					} else {
						payUser = frontUserService.select(pay.getPayToAccount());
					}
				}
				if(payUser != null){
					String payUserString = payUser.getUuid() + "#";
					if(payUser.getNickName() == null){
						payUserString += payUser.getUsername();
					} else {
						payUserString += payUser.getNickName();
					}
					pay.setExtraValue("payUser", payUserString);
				}*/
				payList2.add(pay2);
				logger.debug("pay2"+pay2.getName()+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		if (downloadMode < 1 && isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.pay.name(), "w")) {
			for (Pay pay : payList2) {
				pay.setOperateValue("isShow", "show");
			}
			//对所有成功订单增加补帐功能展示
			for(Pay pay : payList2){
				if(TransactionStandard.TransactionStatus.success.id == pay.getCurrentStatus()){
					pay.setOperateValue("book", "book");
				}
			}
		}
		logger.debug("payList2的长度为"+payList2.size()+"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		map.put("rows",payList2);
		logger.debug("下载：" + downloadMode);
		if(downloadMode > 0 && payList2 != null && authorizeService.havePrivilege(partner, ObjectType.pay.name(), "download")){
			//下载CSV
			logger.debug("下载CSV");
			String fileName = csvFileSdf.format(new Date()) + (RandomUtils.nextInt(10000000) + 10000000) + ".csv";
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			response.setContentType("application/oct-stream");
			PrintWriter output = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("系统订单号, 入口订单号, 出口订单号, 渠道, 对应购买交易, 付款人, 订单金额, 成功金额, 发起时间, 结束时间, 状态\r\n");
			for (Pay p : payList2) {
				sb.append(p.getTransactionId().trim().toString()).append(",").append(p.getInOrderId()).append(",").append(p.getOutOrderId()).append(",").append(p.getDescription()).append(",").append(p.getRefBuyTransactionId()).append(",").append(p.getPayFromAccount()).append(",").append(p.getFaceMoney()).append(",").append(p.getRealMoney()).append(",").append(sdf.format(p.getStartTime())).append(",").append(p.getEndTime() == null ? "空" : sdf.format(p.getEndTime())).append(",");
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
				sb.append("\r\n");
			}
			String csv = sb.toString();
			logger.debug("输出CSV:" + csv);
			output.write(csv);
			output.close();
			return null;
		} else {
			authorizeService.writeOperate(partner, payList2);
			map.put("rows",payList2);

		}
		return view;
	}

	@RequestMapping(value = "/notify/" + "{transactionId}")
	@AllowJsonOutput
	public String notify(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId)
					throws Exception {
		if(transactionId != null && transactionId.equals("batch")){
			String tid = ServletRequestUtils.getStringParameter(request, "transactionId");
			String[] tid2 = tid.split(",");
			int sendCount = 0;
			for(String tid1 : tid2){
				Pay pay = payService.select(tid1.trim());
				if (pay == null) {
					logger.error("找不到指定的支付订单:" + tid1);
					continue;
				}
				sendCount++;
				notifyService.syncSendNotify(pay);

				

			}
			map.put("message", new EisMessage(OperateResult.success.getId(), "批量发送" + sendCount + "条通知"));
			return CommonStandard.partnerMessageView;

		} else {
			Pay i = payService.select(transactionId);
			if (i == null) {

				logger.error("找不到指定的支付订单:" + transactionId);
				map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到指定的订单:" + transactionId));
				return CommonStandard.partnerMessageView;
			}
			String result = notifyService.syncSendNotify(i);
			map.put("message", new EisMessage(OperateResult.success.getId(), "发送结果:" + result));
		}

		return CommonStandard.partnerMessageView;
	}

	//支付订单退款
	@RequestMapping("/refund")
	@AllowJsonOutput
	public String refund(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String transactionId) throws Exception {

		long ownerId = (long)map.get("ownerId");

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return partnerMessageView;		
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return partnerMessageView;
		}
		Pay pay = payService.select(transactionId);
		if(pay == null){
			logger.error("找不到请求退款的支付订单:" + transactionId);
			map.put("message",  new EisMessage(-EisError.BILL_NOT_EXIST.id, "找不到请求退款的支付订单:" + transactionId));
			return CommonStandard.partnerMessageView;
		}
		if(pay.getOwnerId() != ownerId){
			logger.error("支付订单[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message",  new EisMessage(-EisError.BILL_NOT_EXIST.id, "找不到请求退款的支付订单:" + transactionId));
			return partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			boolean isBelongUser = authorizeService.isBelongUser(pay, partner);
			logger.debug("当前支付订单[" + pay.getTransactionId() + "]" + (isBelongUser ? "是" : "不") + "属于合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]");
			if(!isBelongUser){
				map.put("message",  new EisMessage(-EisError.BILL_NOT_EXIST.id, "找不到请求退款的支付订单:" + transactionId));
				return partnerMessageView;
			}
		}


		boolean autoRefund = configService.getBooleanValue(DataName.autoRefund.toString(), ownerId);
		logger.debug("当前系统配置自动退款为:" + autoRefund);
		if(autoRefund){
			PayProcessor payProcessor = payService.getProcessor(pay);
			if(payProcessor == null){
				logger.error("找不到请求退款的支付订单:" + transactionId + "的处理器");
				map.put("message",  new EisMessage(-EisError.BILL_NOT_EXIST.id, "找不到请求退款的支付订单:" + transactionId) + "的支付处理器");
				return CommonStandard.partnerMessageView;
			}
			EisMessage refundResult = payProcessor.onRefund(pay);
			logger.error("支付订单:" + transactionId + "交给处理器[" + payProcessor.getDesc() + "]的退款结果:" + refundResult);

			map.put("message",  refundResult);
		} else {
			//不支持自动退款，仅将支付订单修改为请求退款
			pay.setCurrentStatus(TransactionStatus.refunding.id);
			payService.update(pay);
			map.put("message",  new EisMessage(OperateResult.accept.getId(),"退款申请已提交"));

		}
		return CommonStandard.partnerMessageView;

	}
	@RequestMapping(value = "/confirm")
	@AllowJsonOutput
	public String reconfirm(HttpServletRequest request,
			HttpServletResponse response, ModelMap map)
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



		String[] tid = ServletRequestUtils.getStringParameters(request, "transactionId");
		if(tid.length < 1){
			PayCriteria payCriteria = new PayCriteria(ownerId);
			payCriteria.setCurrentStatus(TransactionStatus.inProcess.id);
			//通过时间段查询
			String beginTime = ServletRequestUtils.getStringParameter(request, "beginTime");
			if(StringUtils.isBlank(beginTime)){
				logger.error("再次确认请求中既没有订单号列表也没有开始时间");
			}
			payCriteria.setStartTimeBegin(sdf.parse(beginTime));

			String endTime = ServletRequestUtils.getStringParameter(request, "endTime");
			if(StringUtils.isBlank(endTime)){
				payCriteria.setStartTimeEnd(DateUtils.addMinutes(payCriteria.getStartTimeBegin(), 60));
				endTime = sdf.format(payCriteria.getStartTimeEnd());
			}

			List<Pay> payList = payService.list(payCriteria);
			if(payList.size() < 1){
				logger.error("当前时间段[" + beginTime + "=>" + endTime + "]内没有处理中的支付订单");
				map.put("message", new EisMessage(EisError.dataNotFoundInSystem.getId(), "当前时间段[" + beginTime + "=>" + endTime + "]内没有处理中的支付订单"));
				return CommonStandard.partnerMessageView;
			}
			tid = new String[ payList.size() ];
			for(Pay pay : payList){
				PayMethod payMethod = payMethodService.select(pay.getPayMethodId());
				if(payMethod == null){
					logger.error("找不到支付订单[" + pay.getTransactionId() + "]的支付方法:" + pay.getPayMethodId());
				}

				Pay queryPay = pay.clone();

				PayProcessor payProcessor = applicationContextService.getBeanGeneric(payMethod.getProcessClass());
				EisMessage queryResult = payProcessor.onQuery(queryPay);

				logger.info("支付订单[" + pay.getTransactionId() + "]的查询结果是:" + queryResult + ",查询后的支付订单是:" + queryPay);

				if(queryResult.getOperateCode() == OperateResult.success.id && queryPay.getCurrentStatus() == TransactionStatus.success.id && queryPay.getRealMoney() > 0){
					processReconfirmOrder(pay,queryPay);
					ModelMap resultMap = new ModelMap();
					resultMap.put("currentStatus",pay.getCurrentStatus());
					resultMap.put("realMoney", pay.getRealMoney());
					String result = notifyService.syncSendNotify(pay);
					resultMap.put("notifyResult", result);
					map.put(pay.getTransactionId(), resultMap);

				} 
			}
			map.put("message", new EisMessage(OperateResult.success.id, "查询完成，请刷新后查看"));



		} else {


			for(String transactionId : tid){
				Pay pay = payService.select(transactionId);
				if (pay == null) {
					logger.error("找不到指定的支付订单:" + transactionId);
					map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到指定的订单:" + transactionId));
					return CommonStandard.partnerMessageView;
				}

				PayMethod payMethod = payMethodService.select(pay.getPayMethodId());
				if(payMethod == null){
					logger.error("找不到支付订单[" + pay.getTransactionId() + "]的支付方法:" + pay.getPayMethodId());
				}

				Pay queryPay = pay.clone();

				PayProcessor payProcessor = applicationContextService.getBeanGeneric(payMethod.getProcessClass());
				EisMessage queryResult = payProcessor.onQuery(queryPay);

				logger.info("支付订单[" + pay.getTransactionId() + "]的查询结果是:" + queryResult + ",查询后的支付订单是:" + queryPay);
				
				if (queryResult != null) {
					if(queryResult.getOperateCode() == OperateResult.success.id && queryPay.getCurrentStatus() == TransactionStatus.success.id && queryPay.getRealMoney() > 0){
						processReconfirmOrder(pay, queryPay);

						
						ModelMap resultMap = new ModelMap();
						resultMap.put("currentStatus",pay.getCurrentStatus());
						resultMap.put("realMoney", pay.getRealMoney());
						String result = notifyService.syncSendNotify(pay);
						resultMap.put("notifyResult", result);
						map.put(transactionId, resultMap);
					} 
				}else {
					map.put("message", new EisMessage(OperateResult.failed.getId(), "支付订单[" + pay.getTransactionId() + "]的查询结果是:null"));
					return CommonStandard.partnerMessageView;
				}

			}
			map.put("message", new EisMessage(OperateResult.success.id, "查询完成，请刷新后查看"));

		}
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/get/"+"{transactionId}")
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map ,@PathVariable("transactionId") String transactionId)
					throws Exception {
		
		final String view = "common/pay/get";
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
		
		map.put("isPlatformGenericPartner", isPlatformGenericPartner);
		Pay pay = payService.select(transactionId);
		if (pay == null) {
			logger.debug("找不到订单号为:"+transactionId+"的支付订单");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到指定的订单:" + transactionId));
			return CommonStandard.partnerMessageView;
		}
		Pay pay2 = pay.clone();
	
		if(!isPlatformGenericPartner){
			pay2.setOutOrderId(null);
		} else {
			PayMethod payMethod = payMethodService.select(pay2.getPayMethodId());
			pay2.setOperateValue("payMethod", payMethod.getName());
		}

		//如果用户是渠道，则“不显示入口订单号、出口订单号，增加一个渠道字段”
		User user = partnerService.select(pay.getInviter());
		if (user != null) {
			if (user.getUserExtraTypeId() > 0) {
				pay2.setOutOrderId(null);
				pay2.setDescription(user.getNickName());
			}
		}
		
		
		List<Map<String, Object>> userTypeList = new ArrayList<>();
		for (UserTypes userTypes : UserTypes.values()) {
			Map<String, Object> userType = new HashMap<String, Object>();
			userType.put("userTypeId", userTypes.getId());
			userType.put("userTypeName", userTypes.getName());
			userTypeList.add(userType);
		}
		map.put("userType", userTypeList);
		map.put("pay", pay2);
		return view;
	}

	/**
	 * 更新补单操作信息 转换
	 * @param pay
	 * @param queryPayResult
	 * @throws Exception
	 */
	private void processReconfirmOrder(Pay pay, Pay queryPayResult) throws Exception{
		int oldStatus = pay.getCurrentStatus();
		//把老订单状态修改并发送通知
		pay.setCurrentStatus(queryPayResult.getCurrentStatus());
		pay.setRealMoney(queryPayResult.getRealMoney());
		if(!StringUtils.isNotEmpty(pay.getPayCardType()) && !pay.getPayCardType().equals(queryPayResult.getPayCardType())){
			pay.setPayCardType(queryPayResult.getPayCardType());
		}
		if(pay.getBalance() == 0 && oldStatus != TransactionStatus.success.id){
			pay.setPayCardType(queryPayResult.getPayCardType());
			pay.setOutOrderId(queryPayResult.getOutOrderId());
			if(pay.getEndTime() == null){
				pay.setEndTime(new Date());
			}
			logger.info("支付订单[" + pay.getTransactionId() + "]的查询结果是:" + queryPayResult.getCurrentStatus() + ",该订单未做结算，发送到消息总线进行支付成功后处理逻辑");
			pay.setExtraValue("reStatistic", "true");
			//进行支付成功后处理比如结算
			pay.setSyncFlag(0);
			EisMessage m = new EisMessage();
			m.setOperateCode(Operate.close.getId());
			m.setAttachment(new HashMap<String,Object>());
			m.getAttachment().put("pay", pay);	
			m.setObjectType(ObjectType.pay.toString());
			messageService.send(null, m);
			m = null;
			Thread.sleep(1000);
			//重新跑一次统计，因为可能这笔订单那个小时的统计已结束
			PayStatCriteria payStatCriteria = new PayStatCriteria(pay.getOwnerId());
			payStatCriteria.setQueryBeginTime(pay.getEndTime());
			payStatCriteria.setQueryEndTime(pay.getEndTime());
			payStatService.statistic(payStatCriteria);
			
		} else {
			payService.update(pay);
		}
	}

}

package com.maicard.wpt.partner.controller;

import static com.maicard.standard.CommonStandard.partnerMessageView;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.criteria.WithdrawTypeCriteria;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.domain.PayType;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.domain.WithdrawType;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.BankAccountService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.WithdrawService;
import com.maicard.money.service.WithdrawTxService;
import com.maicard.money.service.WithdrawTypeService;
import com.maicard.product.service.NotifyService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.domain.Tag;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.BillingPeriod;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;



/**
 * 提现类型即提现策略的管理
 *
 *
 * @author NetSnake
 * @date 2017-06-13
 *
 */
@Controller
@RequestMapping("/withdrawType")
public class WithdrawTypeController extends BaseController {

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
	private WithdrawTxService withdrawTxService;
	
	@Resource
	private ApplicationContextService applicationContextService;
	
	
	
	@Resource
	private DataDefineService dataDefineService;

	private int rowsPerPage = 10;

	//private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			WithdrawTypeCriteria withdrawTypeCriteria) throws Exception {

		final String view = "common/withdrawType/index";

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

		withdrawTypeCriteria.setOwnerId(ownerId);
		
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		
		map.put("isPlatformGenericPartner",isPlatformGenericPartner);
		

		int totalRows = withdrawTypeService.count(withdrawTypeCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据行数是0");
			return view;
		}
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		withdrawTypeCriteria.setPaging(paging);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));		

		List<WithdrawType> withdrawTypeList = withdrawTypeService.listOnPage(withdrawTypeCriteria);
		map.put("total", totalRows);
		map.put("rows",withdrawTypeList);
		return view;
	}


	/**
	 * 新建支付类型初始获取扩展信息
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 * @author zjx
	 */
	@RequestMapping(value="/create", method=RequestMethod.GET)
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
		
		//获取支付类型相关属性所需得初始值
		//结算类型||到帐周期信息列表
		map.put("billingPeriodList", BillingPeriod.values());
		//处理器列表加载
		String[] beanNamesForType = applicationContextService.getBeanNamesForType(PayProcessor.class);
		logger.debug("beanNames的长度是" + beanNamesForType.length
				+ "***********************************");
		List<HashMap<String, Object>> processList = new ArrayList<>();
		for (String beanName : beanNamesForType) {
			HashMap<String, Object> processMap = new HashMap<>();
			logger.debug("beanName是" + beanName
					+ "+++++++++++++++++++++++++++++++++++++++++++++");
			processMap.put("processName", beanName);
			PayProcessor payProcessor = applicationContextService
					.getBeanGeneric(beanName);
			processMap.put("processDesc", payProcessor.getDesc());
			processList.add(processMap);
		}
		map.put("process", processList);
		//查询扩展数据
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(partner.getOwnerId());
		dataDefineCriteria.setObjectType("withdrawType");
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		map.put("dataDefine", dataDefineList);
		return "common/withdrawType/create";
	}

	/**
	 * 新增提现类型
	 */	
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map ,WithdrawType withdrawType) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return partnerMessageView;
		}

		
		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		//获取是否是内部用户
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		if(!isPlatformGenericPartner){
			//不是平台内部用户
			logger.error("不是平台内部用户，无权限进行新增支付类型操作。");
			return partnerMessageView;
		}
		//验证支付类型名称是否重复
		WithdrawTypeCriteria withdrawTypeCriteria = new WithdrawTypeCriteria();
		withdrawTypeCriteria.setOwnerId(ownerId);
		withdrawTypeCriteria.setWithdrawTypeName(withdrawType.getWithdrawTypeName());
		List<WithdrawType> list = withdrawTypeService.list(withdrawTypeCriteria);
		if(list != null && list.size() > 0){
			//名称重复
			logger.error("支付类型名称重复");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"支付类型名称重复"));
			return partnerMessageView;
		}
		
		//下面进行真正得数据录入
		withdrawType.setCurrentStatus(BasicStatus.normal.getId());
		withdrawType.setOwnerId(ownerId);
		//下面对时间特殊处理
		String withdrawBeginTime = ServletRequestUtils.getStringParameter(request, "withdrawBeginTime");
		String withdrawEndTime = ServletRequestUtils.getStringParameter(request, "withdrawEndTime");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date beginTime = sdf.parse(withdrawBeginTime);
		Date endTime = sdf.parse(withdrawEndTime);
		withdrawType.setWithdrawBeginTime(beginTime);
		withdrawType.setWithdrawEndTime(endTime);
		
		//下面进行扩展数据录入
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
		dataDefineCriteria.setObjectType("withdrawType");
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList != null && dataDefineList.size() > 0){
			for(DataDefine dataDefine : dataDefineList){
				//获取扩展数据值
				logger.debug("dataCode的值 是" + dataDefine.getDataCode()
						+ "***************************");
				String dataValue = ServletRequestUtils.getStringParameter(
						request, dataDefine.getDataCode());
				if (StringUtils.isNotBlank(dataValue)) {
					logger.debug("dataValue的值是" + dataValue
							+ "+++++++++++++++++");
					withdrawType.setExtraValue(dataDefine.getDataCode(), dataValue);
				}
			}
		}
		
		EisMessage message = null;
		try{
			if(withdrawTypeService.insert(withdrawType) > 0){
				message = new EisMessage(OperateResult.success.getId(), "支付类型添加成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "支付类型添加失败");				
			}

		}catch(Exception e){
			map.put("message", new EisMessage(EisError.dataError.id, "无法新增支付类型"));
			return partnerMessageView;	
		}
		map.put("message", message);
		return partnerMessageView;	
	}

	/**
	 * 查询指定提现类型
	 * @param request
	 * @param response
	 * @param map
	 * @param withdrawTypeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get" + "/{withdrawTypeId}", method = RequestMethod.GET)
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("withdrawTypeId") int withdrawTypeId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException(
					"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		//boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		
		WithdrawType withdrawType = withdrawTypeService.select(withdrawTypeId);
		if (withdrawType == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + withdrawTypeId
					+ "的支付通道对象");
		}
		if (withdrawType.getOwnerId() != partner.getOwnerId()) {
			throw new ObjectNotFoundByIdException("找不到ID=" + withdrawTypeId
					+ "的支付通道对象");
		}
		
		
		List<Tag> listTag = new ArrayList<Tag>();
		Tag tag = new Tag();
		if (authorizeService.havePrivilege(partner, "withdrawType", "w")
				|| authorizeService.havePrivilege(partner, "withdrawType", "*")) {
			tag = new Tag();
			tag.setTagCode("edit");
			tag.setTagName("edit");
			listTag.add(tag);
		}
		for (Tag t : listTag) {
			logger.debug("权限码返回：" + t.getTagCode());
		}
		map.put("listTag", listTag);
		if (!authorizeService.havePrivilege(partner, "withdrawType", "get")) {
			logger.error("用户" + partner.getNickName() + "没有查看权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有查看权限"));
			return CommonStandard.partnerMessageView;
		}
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//下面进行扩展数据获取
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setOwnerId(withdrawType.getOwnerId());
		dataDefineCriteria.setObjectType("withdrawType");
		List<DataDefine> dataDefineList = dataDefineService
				.list(dataDefineCriteria);
		if (dataDefineList == null || dataDefineList.size() < 1) {
			logger.warn("提现类型没有可选自定义字段");
		} else {
			List<HashMap<String, Object>> list = new ArrayList<>();
			for (DataDefine dataDefine : dataDefineList) {
				HashMap<String, Object> map2 = new HashMap<>();
				
				String value = withdrawType.getExtraValue(dataDefine.getDataCode());
				map2.put("dataCode", dataDefine.getDataCode());
				if (value != null) {
					map2.put("dataValue", value);
				} else {
					map2.put("dataValue", "");
				}
				map2.put("withdrawTypeId", withdrawTypeId);
				map2.put("dataName", dataDefine.getDataName());
				map2.put("dataDefineId", dataDefine.getDataDefineId());
				map2.put("dataType", dataDefine.getDataType());
				if(!isPlatformGenericPartner && dataDefine.getInputLevel() != null && (dataDefine.getInputLevel().equals(InputLevel.system.name()) || dataDefine.getInputLevel().equals(InputLevel.platform.name()))){
					map2.put("readonly",true);
				}else{
					map2.put("readonly",false);
				}
				list.add(map2);
			}
			map.put("configMap", list);
			dataDefineList = null;
		}
		
		//结算类型||到帐周期信息列表
		map.put("billingPeriodList", BillingPeriod.values());
		map.put("withdrawType", withdrawType);
		return "common/withdrawType/get";
	}
	
	/*
	 * 修改提现类型信息 因为每次只能修改一次信息
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)		
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			int withdrawTypeId) throws Exception {
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
		WithdrawType withdrawType = withdrawTypeService.select(withdrawTypeId);
		if(withdrawType == null){
			logger.error("找不到要修改得提现类型:" + withdrawType.getWithdrawTypeId());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"对象错误","找不到指定的提现类型"));
			return CommonStandard.partnerMessageView;	
		}
		if(withdrawType.getOwnerId() != ownerId){
			logger.error("要修改得提现类型:" + withdrawType.getWithdrawTypeId() + ",其ownerId=" + withdrawType.getOwnerId() + "与系统当前的:" + ownerId + "不一致");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"对象错误","找不到指定的提现类型"));
			return CommonStandard.partnerMessageView;	
		}
		//下面判断是扩展数据更新还是基本数据更新 定义如果updateMode参数为空得话 则默认为是扩展数据更新 
		String updateMode = ServletRequestUtils.getStringParameter(request, "updateMode");
		boolean updateFlag = false;
		if(StringUtils.isNotBlank(updateMode)){
			//更新非扩展得数据
			String dataValue = ServletRequestUtils.getStringParameter(request,
					updateMode);
			if(updateMode.equals("withdrawTypeName")){
				//更新提现类型名称 验证信息合法性
				if(StringUtils.isNotBlank(dataValue)){
					withdrawType.setWithdrawTypeName(dataValue);
					updateFlag = true;
				}
			}else if(updateMode.equals("arrivePeriod")){
				//更新到账周期
				if(StringUtils.isNotBlank(dataValue)){
					withdrawType.setArrivePeriod(dataValue);
					updateFlag = true;
				}
				
			}else if(updateMode.equals("commissionType")){
				//更新手续费类型
				if(StringUtils.isNotBlank(dataValue)){
					withdrawType.setCommissionType(dataValue);
					updateFlag = true;
				}
			}else if(updateMode.equals("commission")){
				//更新手续费比例或者数值
				if(StringUtils.isNotBlank(dataValue) && StringUtils.isNumeric(dataValue)){
					withdrawType.setCommission(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("commissionChargeType")){
				//更新手续费扣除来源
				if(StringUtils.isNotBlank(dataValue)){
					withdrawType.setCommissionChargeType(dataValue);
					updateFlag = true;
				}
			}else if(updateMode.equals("withdrawBeginTime")){
				//更新开始时间
				if(StringUtils.isNotBlank(dataValue)){
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					withdrawType.setWithdrawBeginTime(sdf.parse(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("withdrawEndTime")){
				//更新结束时间
				if(StringUtils.isNotBlank(dataValue)){
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					withdrawType.setWithdrawEndTime(sdf.parse(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("maxWithdrawCountInPeriod")){
				//更新可提现次数
				if(StringUtils.isNotBlank(dataValue) && StringUtils.isNumeric(dataValue)){
					withdrawType.setMaxWithdrawCountInPeriod(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("maxWithdrawAmountInPeriod")){
				//更新周期内可提现总金额
				if(StringUtils.isNotBlank(dataValue) && StringUtils.isNumeric(dataValue)){
					withdrawType.setMaxWithdrawAmountInPeriod(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("maxWithdrawAmountPerCount")){
				//更新每笔提现最大金额
				if(StringUtils.isNotBlank(dataValue) && StringUtils.isNumeric(dataValue)){
					withdrawType.setMaxWithdrawAmountPerCount(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("minWithdrawAmountPerCount")){
				//更新每笔提现最小金额
				if(StringUtils.isNotBlank(dataValue) && StringUtils.isNumeric(dataValue)){
					withdrawType.setMinWithdrawAmountPerCount(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("currentStatus")){
				//更新每笔提现最小金额
				if(StringUtils.isNotBlank(dataValue)){
					withdrawType.setCurrentStatus(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}
		}else{
			String updateDataCode = ServletRequestUtils.getStringParameter(request,
					"updateDataCode");
			//更新扩展数据
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(
					ownerId);
			dataDefineCriteria.setDataCode(updateDataCode);
			dataDefineCriteria.setObjectType("withdrawType");
			DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
			if (dataDefine == null) {
				logger.error("找不到要更新的提现类型扩展数据定义:" + updateDataCode);
				map.put("message", new EisMessage(EisError.dataError.getId(),
						"找不到要更新的数据" + updateDataCode));
				return CommonStandard.partnerMessageView;
			}
			if (dataDefine.getInputLevel() != null) {
				if (dataDefine.getInputLevel().equals(
						InputLevel.system.toString())) {
					logger.error("不允许更新system级别的扩展数据:" + updateDataCode);
					map.put("message",
							new EisMessage(EisError.dataError.getId(),
									"找不到要更新的数据"));
					return CommonStandard.partnerMessageView;
				}
				if (!isPlatformGenericPartner
						&& dataDefine.getInputLevel().equals(
								InputLevel.platform.toString())) {
					logger.error("当前不是内部用户，不允许更新platform级别的扩展数据:"
							+ updateDataCode);
					map.put("message",
							new EisMessage(EisError.dataError.getId(),
									"找不到要更新的数据"));
					return CommonStandard.partnerMessageView;
				}
			}
			String dataValue = ServletRequestUtils.getStringParameter(request,
					updateDataCode);
			if (StringUtils.isNotBlank(dataValue)) {
					withdrawType.setExtraValue(updateDataCode, dataValue);
				
				updateFlag = true;
			}
		}
		EisMessage message = null;
		if(updateFlag){
			int rs = withdrawTypeService.update(withdrawType);
			if(rs == OperateResult.success.getId()){
				message = new EisMessage(OperateResult.success.getId(),"提现类型修改成功");			
			} else {
				message = new EisMessage(rs,"提现类型修改失败");				
			}
		}else{
			message = new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"提现类型修改失败,参数错误");
		}
		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}


	/*
	 * 提现完成后的回调通知处理
	 * 调用对应的withdrawProcessor的onResult接口方法，并得到EisMessage结果
	 * 处理完成后仅向支付商返回success等字符
	 */
	@RequestMapping("/notify/{withdrawTypeId}")
	@IgnoreLoginCheck
	public ResponseEntity<String>   onNotify(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("withdrawTypeId") Integer withdrawTypeId) throws Exception {
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

		EisMessage notifyResult = withdrawTxService.end(withdrawTypeId, resultString);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "text/html; charset=UTF-8");
		Withdraw withdraw = null;
		if(notifyResult != null){
			try{
				withdraw = (Withdraw) notifyResult.getAttachment().get("withdraw");
			}catch(Exception e){
				logger.error("无法解析支付处理器返回的pay对象:" + e.getMessage());
				e.printStackTrace();
			}
		}
		if(withdraw == null){
			logger.error("支付处理器未返回withdraw对象");
			return new ResponseEntity<String>(null, responseHeaders, HttpStatus.OK);	
		}
		String inNotifyUrl = withdraw.getExtraValue("inNotifyUrl");
		if(StringUtils.isNotBlank(inNotifyUrl)){
			//发送通知
			notifyService.sendNotify(withdraw);
		}

		String responseText = withdraw.getExtraValue("notifyResponse");
		logger.info("支付订单[" + withdraw.getTransactionId() + "]响应字符串:" + responseText );
		return new ResponseEntity<String>(responseText, responseHeaders, HttpStatus.OK);	
	}
}

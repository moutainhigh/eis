package com.maicard.wpt.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.domain.PayType;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.domain.Tag;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 管理和控制支付通道PayMethod
 * 
 * 
 * 
 * @author NetSnake
 * @date 2016年11月4日
 * 
 */
@Controller
@RequestMapping("/payMethod")
public class PayMethodController extends BaseController {

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private PayMethodService payMethodService;
	@Resource
	private PayTypeService payTypeService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private DataDefineService dataDefineService;

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
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			PayMethodCriteria payMethodCriteria) throws Exception {
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException(
					"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		final String view = "common/payMethod/index";
		payMethodCriteria.setOwnerId(partner.getOwnerId());

		int totalRows = payMethodService.count(payMethodCriteria);
		map.put("total", totalRows);

		if (totalRows < 1) {
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		String hour = new SimpleDateFormat("HH").format(new Date());
		String day = new SimpleDateFormat("yyyyMMdd").format(new Date());

		int rows = ServletRequestUtils.getIntParameter(request, "rows",
				rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		map.put("contentPaging",
				PagingUtils.generateContentPaging(totalRows, rows, page));

		Paging paging = new Paging(rows);
		payMethodCriteria.setPaging(paging);
		payMethodCriteria.getPaging().setCurrentPage(page);
		List<PayMethod> payMethodList = payMethodService
				.listOnPage(payMethodCriteria);

		authorizeService.writeOperate(partner, payMethodList);

		if (payMethodList != null && payMethodList.size() > 0) {
			for (PayMethod payMethod : payMethodList) {
				String key = "PayMethod#SuccessMoney#" + hour + "#"
						+ payMethod.getPayMethodId();
				String value = centerDataService.get(key);
				if (value != null) {
					payMethod.setOperateValue("currentSuccessMoneyInHour",
							value);
				}
				key = "PayMethod#SuccessMoney#" + day + "#"
						+ payMethod.getPayMethodId();
				value = centerDataService.get(key);
				if (value != null) {
					payMethod
							.setOperateValue("currentSuccessMoneyInDay", value);
				}
				logger.info("支付方式:" + payMethod.getPayMethodId() + "当天成功金额是:"
						+ value);
				if (payMethod.getPayTypeId() > 0) {
					PayType payType = payTypeService.select(payMethod
							.getPayTypeId());
					if (payType != null) {
						payMethod.setOperateValue("payTypeName",
								payType.getName());
					}
				}
			}
		}
		List<Tag> listTag = new ArrayList<Tag>();
		Tag tag = new Tag();
		if (authorizeService.havePrivilege(partner, "payMethod", "get")) {
			tag.setTagCode("browse");
			tag.setTagName("browse");
			listTag.add(tag);
		}
		if (authorizeService.havePrivilege(partner, "payMethod", "switch")) {
			tag = new Tag();
			tag.setTagCode("switch");
			tag.setTagName("switch");
			listTag.add(tag);
		}
		if (authorizeService.havePrivilege(partner, "payMethod", "w")
				|| authorizeService.havePrivilege(partner, "payMethod", "*")) {
			tag = new Tag();
			tag.setTagCode("edit");
			tag.setTagName("edit");
			listTag.add(tag);
		}
		for (Tag t : listTag) {
			logger.debug("权限码返回：" + t.getTagCode());
		}
		map.put("listTag", listTag);
		map.put("rows", payMethodList);
		return view;
	}

	@RequestMapping(value = "/get" + "/{payMethodId}", method = RequestMethod.GET)
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("payMethodId") int payMethodId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException(
					"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		PayMethod payMethod = payMethodService.select(payMethodId);
		if (payMethod == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + payMethodId
					+ "的支付通道对象");
		}
		if (payMethod.getOwnerId() != partner.getOwnerId()) {
			throw new ObjectNotFoundByIdException("找不到ID=" + payMethodId
					+ "的支付通道对象");
		}
		List<Tag> listTag = new ArrayList<Tag>();
		Tag tag = new Tag();
		if (authorizeService.havePrivilege(partner, "payMethod", "w")
				|| authorizeService.havePrivilege(partner, "payMethod", "*")) {
			tag = new Tag();
			tag.setTagCode("edit");
			tag.setTagName("edit");
			listTag.add(tag);
		}
		for (Tag t : listTag) {
			logger.debug("权限码返回：" + t.getTagCode());
		}
		map.put("listTag", listTag);
		if (!authorizeService.havePrivilege(partner, "payMethod", "get")) {
			logger.error("用户" + partner.getNickName() + "没有查看权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有查看权限"));
			return CommonStandard.partnerMessageView;
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setOwnerId(payMethod.getOwnerId());
		dataDefineCriteria.setObjectType(ObjectType.payMethod.name());
		List<DataDefine> dataDefineList = dataDefineService
				.list(dataDefineCriteria);
		if (dataDefineList == null || dataDefineList.size() < 1) {
			logger.warn("支付方法没有可选自定义字段");

		} else {
			List<HashMap<String, Object>> list = new ArrayList<>();
			for (DataDefine dataDefine : dataDefineList) {
				HashMap<String, Object> map2 = new HashMap<>();
				map2.put("dataCode", dataDefine.getDataCode());
				String dataValue = payMethod.getExtraValue(dataDefine.getDataCode());
				if (dataValue != null) {
					map2.put("dataValue", dataValue);
				} else {
					map2.put("dataValue", "");
				}
				map2.put("payMethodId", payMethodId);
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

		// process_class
		String[] beanNamesForType = applicationContextService
				.getBeanNamesForType(PayProcessor.class);
		logger.debug("beanNames的长度是" + beanNamesForType.length
				+ "***********************************");
		List<HashMap<String, Object>> processList = new ArrayList<>();
		for (String beanName : beanNamesForType) {
			HashMap<String, Object> processMap = new HashMap<>();
			logger.debug("beanName是" + beanName
					+ "+++++++++++++++++++++++++++++++++++++++++++++");
			processMap.put("processName", beanName);
			/*
			 * beanName="com.maicard.money.service.payProcessor."+beanName.
			 * replaceFirst(beanName.substring(0,1),
			 * beanName.substring(0,1).toUpperCase()); Method[] methods =
			 * Class.forName(beanName).getMethods(); for (Method method :
			 * methods) { if ("getDesc".equals(method.getName())) { Object desc
			 * = method.invoke(Class.forName(beanName).newInstance());
			 * processMap.put("processDesc", desc); } }
			 */
			PayProcessor payProcessor = applicationContextService
					.getBeanGeneric(beanName);
			processMap.put("processDesc", payProcessor.getDesc());
			processList.add(processMap);
		}
		map.put("process", processList);
		// 支付方式
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria();
		payTypeCriteria.setOwnerId(payMethod.getOwnerId());
		List<PayType> list = payTypeService.list(payTypeCriteria);
		map.put("payType", list);
		map.put("payMethod", payMethod);
		return "common/payMethod/get";
	}

	@RequestMapping(value = "/update/{payMethodId}", method = RequestMethod.GET)
	public String getUpdate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("payMethodId") int payMethodId) throws Exception {
		long ownerId = 0;

		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}

		PayMethod node = payMethodService.select(payMethodId);
		if (node == null) {
			throw new ParentObjectNotFoundException("找不到指定支付通道[" + payMethodId
					+ "]");
		}
		if (node.getOwnerId() != ownerId) {
			throw new ParentObjectNotFoundException("找不到指定支付通道[" + payMethodId
					+ "]");
		}
		return "common/node/update";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, PayMethod payMethod)
			throws Exception {
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException(
					"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		if (!authorizeService.havePrivilege(partner, "payMethod", "w")) {
			logger.error("用户" + partner.getNickName() + "没有更新权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有更新权限"));
			return CommonStandard.partnerMessageView;
		}
		PayMethod _oldPayMethod = payMethodService.select(payMethod
				.getPayMethodId());
		if (_oldPayMethod == null) {
			logger.error("找不到指定的支付通道:" + payMethod.getPayMethodId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,
					"找不到指定的支付通道"));
			return CommonStandard.partnerMessageView;
		}
		//下面判断是扩展数据更新还是基本数据更新 定义如果updateMode参数为空得话 则默认为是扩展数据更新 
		String updateMode = ServletRequestUtils.getStringParameter(
				request, "updateMode");
		boolean updateFlag = false;//更新结果标识
		if(StringUtils.isBlank(updateMode)){
			//扩展数据更新
			String updateDataCode = ServletRequestUtils.getStringParameter(request,
					"updateDataCode");
			if (StringUtils.isNotBlank(updateDataCode)) {
				DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(
						ownerId);
				dataDefineCriteria.setDataCode(updateDataCode);
				dataDefineCriteria.setObjectType("payMethod");
				DataDefine dataDefine = dataDefineService
						.select(dataDefineCriteria);
				if (dataDefine == null) {
					logger.error("找不到要更新的商户扩展数据定义:" + updateDataCode);
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
					_oldPayMethod.setExtraValue(updateDataCode, dataValue);
					updateFlag = true;
				}
			}
		}else{
			//基本数据更新  判断基础数据更新类型
			
			String dataValue = ServletRequestUtils.getStringParameter(request,
					updateMode);
			if(updateMode.equals("payChannelId")){
				//支付通道更新
				if(!StringUtils.isNotBlank(dataValue)){
					dataValue = "0";
				}
				if(StringUtils.isNumeric(dataValue) 
						&& dataValue.indexOf(".") < 0){
					//判断数字合法性
					_oldPayMethod.setPayChannelId(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("name")){
				//名称更新
				if(StringUtils.isNotBlank(dataValue)){
					_oldPayMethod.setName(dataValue);
					updateFlag = true;
				}
			}else if(updateMode.equals("payTypeId")){
				//支付类型更新
				if(!StringUtils.isNotBlank(dataValue)){
					dataValue = "0";
				}
				if(StringUtils.isNumeric(dataValue) 
						&& dataValue.indexOf(".") < 0){
					_oldPayMethod.setPayTypeId(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("processClass")){
				//处理器更新
				if(StringUtils.isNotBlank(dataValue)){
					_oldPayMethod.setProcessClass(dataValue);
					updateFlag = true;
				}
			}else if(updateMode.equals("commissionType")){
				//通道收费费类型更新
				if(StringUtils.isNotBlank(dataValue)){
					_oldPayMethod.setCommissionType(dataValue);
					updateFlag = true;
				}
			}else if(updateMode.equals("commission")){
				//通道手续费数值更新
				float commission = 0;
				if(StringUtils.isNotBlank(dataValue)){
					commission = Float.parseFloat(dataValue);
				}
				_oldPayMethod.setCommission(commission);
				updateFlag = true;
			}else if(updateMode.equals("referUuid")){
				//系统资金账户id更新
				if(StringUtils.isNotBlank(dataValue)){
					_oldPayMethod.setReferUuid(Long.parseLong(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("weight")){
				//权重更新
				if(!StringUtils.isNotBlank(dataValue)){
					dataValue = "0";
				}
				if(StringUtils.isNumeric(dataValue) 
						&& dataValue.indexOf(".") < 0){
					_oldPayMethod.setWeight(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("percent")){
				//权重更新
				if(!StringUtils.isNotBlank(dataValue)){
					dataValue = "0";
				}
				if(StringUtils.isNumeric(dataValue)
						&& dataValue.indexOf(".") < 0){
					_oldPayMethod.setPercent(Integer.parseInt(dataValue));
					updateFlag = true;
				}
			}else if(updateMode.equals("currentStatus")){
				if (StringUtils.isNotBlank(dataValue)) {
					if (_oldPayMethod.getCurrentStatus() == Integer.parseInt(dataValue)) {
						logger.debug("系统中的支付通道状态与提交修改状态一致，都是:"
								+ payMethod.getCurrentStatus());
						map.put("message", new EisMessage(OperateResult.failed.getId(),
								"状态未变，不需要修改"));
						return CommonStandard.partnerMessageView;
					}
					if (Integer.parseInt(dataValue) == BasicStatus.normal.getId()
							|| payMethod.getCurrentStatus() == BasicStatus.disable
									.getId()) {

						_oldPayMethod.setCurrentStatus(payMethod.getCurrentStatus());
						logger.debug("将支付通道[" + _oldPayMethod.getPayMethodId()
								+ "]状态改为:" + _oldPayMethod.getCurrentStatus());
						updateFlag = true;
					} else {
						logger.debug("未知或不允许提交的状态:" + payMethod.getCurrentStatus());
						map.put("message", new EisMessage(OperateResult.failed.getId(),
								"不允许修改为指定状态:" + payMethod.getCurrentStatus()));
						return CommonStandard.partnerMessageView;
					}
				}
			}
			
		}
		//测试代码 希望成功
		EisMessage message = null;
		if(updateFlag){
			if (payMethodService.update(_oldPayMethod) > 0) {
				message = new EisMessage(OperateResult.success.getId(), "支付通道修改成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "支付通道修改失败");
			}
		}else{
			message = new EisMessage(OperateResult.failed.getId(), "支付通道修改失败");
		}
		map.put("message", message);
		return CommonStandard.partnerMessageView;
    }

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		final String view = "common/payMethod/create";
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
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有权限"));
			return CommonStandard.partnerMessageView;
		}

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
		dataDefineCriteria.setObjectType(ObjectType.payMethod.name());
		List<DataDefine> dataDefineList = dataDefineService
				.list(dataDefineCriteria);
		map.put("dataDefine", dataDefineList);
		// process_class
		String[] beanNamesForType = applicationContextService
				.getBeanNamesForType(PayProcessor.class);
		logger.debug("beanNames的长度是" + beanNamesForType.length
				+ "***********************************");
		List<HashMap<String, Object>> processList = new ArrayList<>();
		for (String beanName : beanNamesForType) {
			HashMap<String, Object> processMap = new HashMap<>();
			logger.debug("beanName是" + beanName
					+ "+++++++++++++++++++++++++++++++++++++++++++++");
			processMap.put("processName", beanName);
			/*
			 * beanName="com.maicard.money.service.payProcessor."+beanName.
			 * replaceFirst(beanName.substring(0,1),
			 * beanName.substring(0,1).toUpperCase()); Method[] methods =
			 * Class.forName(beanName).getMethods(); for (Method method :
			 * methods) { if ("getDesc".equals(method.getName())) { Object desc
			 * = method.invoke(Class.forName(beanName).newInstance());
			 * processMap.put("processDesc", desc); } }
			 */
			PayProcessor payProcessor = applicationContextService
					.getBeanGeneric(beanName);
			processMap.put("processDesc", payProcessor.getDesc());
			processList.add(processMap);
		}
		map.put("process", processList);

		// 支付方式
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria();
		payTypeCriteria.setOwnerId(ownerId);
		List<PayType> list = payTypeService.list(payTypeCriteria);
		map.put("payType", list);
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, PayMethod payMethod)
			throws Exception {
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
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有权限"));
			return CommonStandard.partnerMessageView;
		}
		if (!authorizeService.havePrivilege(partner, "payMethod", "w")
				|| !authorizeService.havePrivilege(partner, "payMethod", "*")) {
			logger.error("用户" + partner.getNickName() + "没有新增权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有新增权限"));
			return CommonStandard.partnerMessageView;
		}

		payMethod.setCurrentStatus(BasicStatus.normal.getId());
		payMethod.setOwnerId(ownerId);
		// payMethod.setCommissionType("COMMISSION_TYPE_RATE");

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setOwnerId(ownerId);
		dataDefineCriteria.setObjectType("payMethod");
		List<DataDefine> list = dataDefineService.list(dataDefineCriteria);
		// logger.debug("dataDefineList的长度为"+list.size()+"***********************");
		if (list != null && list.size() > 0) {
			for (DataDefine dataDefine : list) {
				logger.debug("dataCode的值 是" + dataDefine.getDataCode()
						+ "***************************");
				String dataValue = ServletRequestUtils.getStringParameter(
						request, dataDefine.getDataCode());
				if (StringUtils.isNotBlank(dataValue)) {
					logger.debug("dataValue的值是" + dataValue
							+ "+++++++++++++++++");
					payMethod.setExtraValue(dataDefine.getDataCode(), dataValue);
				}
			}
		}
		EisMessage message = null;
		if (payMethodService.insert(payMethod) == 1) {
			message = new EisMessage(OperateResult.success.getId(), "操作完成");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "操作失败");
		}
		map.put("message", message);
		return view;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@AllowJsonOutput
	public String delete(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, PayMethod payMethod)
			throws Exception {
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
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有权限"));
			return view;
		}
		if (!authorizeService.havePrivilege(partner, "payMethod", "w")
				|| !authorizeService.havePrivilege(partner, "payMethod", "*")) {
			logger.error("用户" + partner.getNickName() + "没有删除权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有删除权限"));
			return CommonStandard.partnerMessageView;
		}
		PayMethod oldPayMethod = payMethodService.select(payMethod
				.getPayMethodId());

		String updateDataCode = ServletRequestUtils.getStringParameter(request,
				"updateDataCode");
		/*if (updateDataCode != null) {
			Map<String, String> data = oldPayMethod.getData();
			if (data != null && data.size() > 0) {
				data.put(updateDataCode, "");
			}
		}*/

		if (payMethod.getName() != null) {
			oldPayMethod.setName(null);
		}
		if (payMethod.getPayTypeId() != 0) {
			oldPayMethod.setPayTypeId(0);
		}
		if (payMethod.getProcessClass() != null) {
			oldPayMethod.setProcessClass(null);
		}
		if (payMethod.getWeight() != 0) {
			oldPayMethod.setWeight(0);
		}
		if (payMethod.getPayChannelId() != 0) {
			oldPayMethod.setPayChannelId(0);
		}
		if (payMethod.getPercent() != 0) {
			oldPayMethod.setPercent(0);
		}
		EisMessage message = null;
		if (payMethodService.update(oldPayMethod) > 0) {
			message = new EisMessage(OperateResult.success.getId(), "删除成功");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "删除失败");
		}

		map.put("message", message);
		return view;
	}
}

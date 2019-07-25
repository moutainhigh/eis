package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.domain.ProductType;
import com.maicard.product.service.ProductTypeService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DisplayLevel;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentTypes;

/**
 * 扩展数据的配置
 * @author hailong
 * @date 2017-7-14
 */
@Controller
@RequestMapping("/dataDefine")
public class DataDefineController extends BaseController{
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private ProductTypeService productTypeService;
	
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
	public String list(HttpServletRequest request,HttpServletResponse response, ModelMap map,
		@ModelAttribute("dataDefineCriteria")DataDefineCriteria dataDefineCriteria)
			throws Exception {
			final String view = "common/dataDefine/index";
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
			
			if (!isPlatformGenericPartner) {
				map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"非内部用户,没有权限访问"));
				return CommonStandard.partnerMessageView;
			}
			
			if (StringUtils.isBlank(dataDefineCriteria.getDataCode())) {
				dataDefineCriteria.setDataCode(null);
			}
			if (StringUtils.isBlank(dataDefineCriteria.getObjectType())) {
				dataDefineCriteria.setObjectType(null);
			}
			dataDefineCriteria.setOwnerId(ownerId);
			
			int rows = ServletRequestUtils.getIntParameter(request, "rows",rowsPerPage);
			int page = ServletRequestUtils.getIntParameter(request, "page", 1);
			Paging paging = new Paging(rows);
			dataDefineCriteria.setPaging(paging);
			dataDefineCriteria.getPaging().setCurrentPage(page);
			
			int totalRows = dataDefineService.count(dataDefineCriteria);
			map.put("total", totalRows);
			logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 "+ page + " 页。");
			
			List<DataDefine> dataDefineList = dataDefineService.listOnPage(dataDefineCriteria);
			
			List<Integer> statusList = new ArrayList<>();
			for (BasicStatus basicStatus : BasicStatus.values()) {
				statusList.add(basicStatus.id);
			}
			List<String> inputLevelList = new ArrayList<>();
			for (InputLevel inputLevel : InputLevel.values()) {
				inputLevelList.add(inputLevel.name());
			}
			List<Map<String, Object>> documentTypeList = new ArrayList<>();
			for (DocumentTypes documentTypes : DocumentTypes.values()) {
				Map<String, Object> documentMap = new HashMap<String, Object>();
				documentMap.put("documentTypeId", documentTypes.getId());
				documentMap.put("documentTypeName", documentTypes.getName());
				documentTypeList.add(documentMap);
			}
			List<Map<String, Object>> userTypeList = new ArrayList<>();
			for (UserTypes userTypes : UserTypes.values()) {
				Map<String, Object> userType = new HashMap<String, Object>();
				userType.put("userTypeId", userTypes.getId());
				userType.put("userTypeName", userTypes.getName());
				userTypeList.add(userType);
			}
			
			ProductTypeCriteria productTypeCriteria = new ProductTypeCriteria();
			productTypeCriteria.setOwnerId(ownerId);
			List<ProductType> productTypes = productTypeService.list(productTypeCriteria );
			map.put("productType", productTypes);
			map.put("userType", userTypeList);
			map.put("documentType", documentTypeList);
			String[] arrs = {"payMethod","withdrawMethod","document","user","product","business","activity"};
			map.put("objectType", arrs);
			map.put("displayLevel", DisplayLevel.values());
			map.put("inputLevel", inputLevelList);
			map.put("statusList", statusList);
			map.put("rows", dataDefineList);
			String[] types = {"String","int","boolean","float"};
			map.put("dataType", types);
			
			//计算并放入分页
			map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
			return view;
	}
	
	@RequestMapping(value="/get" + "/{dataDefineId}")		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("dataDefineId") int dataDefineId) throws Exception {
		final String view = "common/dataDefine/get";
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
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"非内部用户,没有权限访问"));
			return CommonStandard.partnerMessageView;
		}
		
		DataDefine dataDefine = dataDefineService.select(dataDefineId);
		if (dataDefine==null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + dataDefineId + "的dataDefine对象");
		}
		List<Integer> statusList = new ArrayList<>();
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.id);
		}
		List<String> inputLevelList = new ArrayList<>();
		for (InputLevel inputLevel : InputLevel.values()) {
			inputLevelList.add(inputLevel.name());
		}
		List<Map<String, Object>> documentTypeList = new ArrayList<>();
		for (DocumentTypes documentTypes : DocumentTypes.values()) {
			Map<String, Object> documentMap = new HashMap<String, Object>();
			documentMap.put("documentTypeId", documentTypes.getId());
			documentMap.put("documentTypeName", documentTypes.getName());
			documentTypeList.add(documentMap);
		}
		List<Map<String, Object>> userTypeList = new ArrayList<>();
		for (UserTypes userTypes : UserTypes.values()) {
			Map<String, Object> userType = new HashMap<String, Object>();
			userType.put("userTypeId", userTypes.getId());
			userType.put("userTypeName", userTypes.getName());
			userTypeList.add(userType);
		}
		ProductTypeCriteria productTypeCriteria = new ProductTypeCriteria();
		productTypeCriteria.setOwnerId(ownerId);
		List<ProductType> productTypes = productTypeService.list(productTypeCriteria );
		map.put("productType", productTypes);
		map.put("userType", userTypeList);
		map.put("documentType", documentTypeList);
		String[] arrs = {"payMethod","withdrawMethod","document","user","product","business","activity"};
		map.put("objectType", arrs);
		map.put("displayLevel", DisplayLevel.values());
		map.put("inputLevel", inputLevelList);
		map.put("statusList", statusList);
		map.put("dataDefine", dataDefine);
		String[] types = {"String","int","boolean","float"};
		map.put("dataType", types);
		return view;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, DataDefine dataDefine)
			throws Exception {
		final String view = CommonStandard.partnerMessageView;
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
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"非内部用户,没有权限访问"));
			return CommonStandard.partnerMessageView;
		}
		
		DataDefine oldDataDefine = dataDefineService.select(dataDefine.getDataDefineId());
		if (oldDataDefine == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + dataDefine.getDataDefineId() + "的dataDefine对象");
		}
		if (StringUtils.isNotBlank(dataDefine.getDataCode())) {
			oldDataDefine.setDataCode(dataDefine.getDataCode());
		}
		if (StringUtils.isNotBlank(dataDefine.getDataType())) {
			oldDataDefine.setDataType(dataDefine.getDataType());
		}
		if (StringUtils.isNotBlank(dataDefine.getDataName())) {
			oldDataDefine.setDataName(dataDefine.getDataName());
		}
		if (StringUtils.isNotBlank(dataDefine.getDataDescription())) {
			oldDataDefine.setDataDescription(dataDefine.getDataDescription());
		}
		if (StringUtils.isNotBlank(dataDefine.getDisplayLevel())) {
			oldDataDefine.setDisplayLevel(dataDefine.getDisplayLevel());
		}
		if (StringUtils.isNotBlank(dataDefine.getInputMethod())) {
			oldDataDefine.setInputMethod(dataDefine.getInputMethod());
		}
		if (StringUtils.isNotBlank(dataDefine.getInputLevel())) {
			oldDataDefine.setInputLevel(dataDefine.getInputLevel());
		}
		if (StringUtils.isNotBlank(dataDefine.getObjectType())) {
			oldDataDefine.setObjectType(dataDefine.getObjectType());
		}
		if (dataDefine.getObjectId()>0) {
			oldDataDefine.setObjectId(dataDefine.getObjectId());
		}
		if (StringUtils.isNotBlank(dataDefine.getCompareMode())) {
			oldDataDefine.setCompareMode(dataDefine.getCompareMode());
		}
		if (dataDefine.getCurrentStatus()>0) {
			oldDataDefine.setCurrentStatus(dataDefine.getCurrentStatus());
		}
		
		if (dataDefineService.update(oldDataDefine)>0) {
			map.put("message", new EisMessage(OperateResult.success.getId(), "操作完成"));
		}else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "操作失败"));
		}
		return view;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			DataDefine dataDefine) throws Exception {
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
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"非内部用户,没有权限访问"));
			return CommonStandard.partnerMessageView;
		}
		dataDefine.setOwnerId(ownerId);
		if (dataDefineService.insert(dataDefine)>0) {
			map.put("message", new EisMessage(OperateResult.success.getId(), "新增扩展字段完成"));
		}else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "新增扩展字段失败"));
		}
		return view;
	}
}

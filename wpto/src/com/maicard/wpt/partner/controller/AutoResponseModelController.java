package com.maicard.wpt.partner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.domain.PayMethod;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.criteria.AutoResponseModelCriteria;
import com.maicard.wpt.domain.AutoResponseModel;
import com.maicard.wpt.service.AutoResponseModelService;

/**
 * 自动回复配置的管理
 * 
 *
 *
 * @author NetSnake
 * @date 2018-05-07
 */
@Controller
@RequestMapping("/autoResponseModel")
public class AutoResponseModelController extends BaseController {

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private AutoResponseModelService autoResponseModelService;
	
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private AuthorizeService authorizeService;
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
			AutoResponseModelCriteria autoResponseModelCriteria) throws Exception {
		
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
		final String view = "common/autoResponseModel/index";
		autoResponseModelCriteria.setOwnerId(partner.getOwnerId());

		int totalRows = autoResponseModelService.count(autoResponseModelCriteria);
		map.put("total", totalRows);

		if (totalRows < 1) {
			logger.debug("当前返回的数据数量是0");
			return view;
		}
	
		int rows = ServletRequestUtils.getIntParameter(request, "rows",
				rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		map.put("contentPaging",
				PagingUtils.generateContentPaging(totalRows, rows, page));

		Paging paging = new Paging(rows);
		autoResponseModelCriteria.setPaging(paging);
		autoResponseModelCriteria.getPaging().setCurrentPage(page);
		List<AutoResponseModel> autoResponseModelList = autoResponseModelService.listOnPage(autoResponseModelCriteria);

		authorizeService.writeOperate(partner, autoResponseModelList);


		map.put("rows", autoResponseModelList);
		return view;
	}

	@RequestMapping(value = "/get" + "/{autoResponseModelId}", method = RequestMethod.GET)
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("payMethodId") int payMethodId) throws Exception {
		final String view =  "wpt/autoResponseModel/detail";
		
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException(
					"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		AutoResponseModel payMethod = autoResponseModelService.select(payMethodId);
		if (payMethod == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + payMethodId
					+ "的支付通道对象");
		}
		if (payMethod.getOwnerId() != partner.getOwnerId()) {
			throw new ObjectNotFoundByIdException("找不到ID=" + payMethodId
					+ "的支付通道对象");
		}
		
		if (!authorizeService.havePrivilege(partner, "payMethod", "get")) {
			logger.error("用户" + partner.getNickName() + "没有查看权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),
					"没有查看权限"));
			return CommonStandard.partnerMessageView;
		}
		

		map.put("payMethod", payMethod);
		return view;
	}

	@RequestMapping(value = "/update/{autoResponseModelId}", method = RequestMethod.GET)
	public String getUpdate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("autoResponseModelId") int autoResponseModelId) throws Exception {
		final String view =  "wpt/autoResponseModel/update";
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

		AutoResponseModel autoResponseModel = autoResponseModelService.select(autoResponseModelId);
		if (autoResponseModel == null) {
			throw new ParentObjectNotFoundException("找不到指定的自动回复配置:" + autoResponseModelId);
		}
		if (autoResponseModel.getOwnerId() != ownerId) {
			throw new ParentObjectNotFoundException("找不到指定的自动回复配置:" + autoResponseModelId);
		}
		return view;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, AutoResponseModel autoResponseModel)
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
		AutoResponseModel _oldAutoResponseModel = autoResponseModelService.select(autoResponseModel.getAutoResponseModelId());
		if (_oldAutoResponseModel == null) {
			logger.error("找不到指定的自动回复配置:" + autoResponseModel.getAutoResponseModelId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,					"找不到指定的自动回复配置"));
			return CommonStandard.partnerMessageView;
		}
		//下面判断是扩展数据更新还是基本数据更新 定义如果updateMode参数为空得话 则默认为是扩展数据更新 
		
		EisMessage message = null;
		if (autoResponseModelService.update(_oldAutoResponseModel) > 0) {
			message = new EisMessage(OperateResult.success.getId(), "支付通道修改成功");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "支付通道修改失败");
		}
		
		map.put("message", message);
		return CommonStandard.partnerMessageView;
    }

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		final String view = "wpt/autoResponseModel/create";
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
		
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, AutoResponseModel autoResponseModel)
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

		autoResponseModel.setCurrentStatus(BasicStatus.normal.getId());
		autoResponseModel.setOwnerId(ownerId);
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
					autoResponseModel.setExtraValue(dataDefine.getDataCode(), dataValue);
				}
			}
		}
		EisMessage message = null;
		if (autoResponseModelService.insert(autoResponseModel) == 1) {
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
		AutoResponseModel _oldAutoResponseModel = autoResponseModelService.select(payMethod
				.getPayMethodId());

		String updateDataCode = ServletRequestUtils.getStringParameter(request,
				"updateDataCode");
		if (updateDataCode != null) {
			
		}

		
		EisMessage message = null;
		if (autoResponseModelService.update(_oldAutoResponseModel) > 0) {
			message = new EisMessage(OperateResult.success.getId(), "删除成功");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "删除失败");
		}

		map.put("message", message);
		return view;
	}
}

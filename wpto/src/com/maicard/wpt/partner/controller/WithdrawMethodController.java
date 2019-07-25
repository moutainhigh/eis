package com.maicard.wpt.partner.controller;

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
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.WithdrawMethodCriteria;
import com.maicard.money.criteria.WithdrawTypeCriteria;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.money.domain.WithdrawType;
import com.maicard.money.service.WithdrawMethodService;
import com.maicard.money.service.WithdrawProcessor;
import com.maicard.money.service.WithdrawTypeService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.domain.Tag;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 管理和控制提现通道WithdrawMethod
 * 
 *
 *
 * @author hailong
 * @date 2017年7月4日
 *
 */
@Controller
@RequestMapping("/withdrawMethod")
public class WithdrawMethodController extends BaseController{


	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private WithdrawMethodService withdrawMethodService;
	@Resource
	private WithdrawTypeService withdrawTypeService;
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
	
	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, WithdrawMethodCriteria withdrawMethodCriteria) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		final String view = "common/withdrawMethod/index";
		withdrawMethodCriteria.setOwnerId(partner.getOwnerId());

		int totalRows = withdrawMethodService.count(withdrawMethodCriteria);
		map.put("total", totalRows);

		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		String hour = new SimpleDateFormat("HH").format(new Date());
		int rows = ServletRequestUtils.getIntParameter(request, "rows",
				rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		withdrawMethodCriteria.setPaging(paging);
		withdrawMethodCriteria.getPaging().setCurrentPage(page);
		List<WithdrawMethod> withdrawMethodList = withdrawMethodService.listOnPage(withdrawMethodCriteria);

		authorizeService.writeOperate(partner, withdrawMethodList);

		if(withdrawMethodList != null && withdrawMethodList.size() > 0){
			for(WithdrawMethod withdrawMethod : withdrawMethodList){
				String key = "WithdrawMethod#SuccessMoney#" + hour + "#" + withdrawMethod.getWithdrawMethodId();
				String value = centerDataService.get(key);
				if(value != null){
					withdrawMethod.setOperateValue("currentSuccessMoneyInHour", value);
				}
				if(withdrawMethod.getWithdrawTypeId() > 0){
					WithdrawType withdrawType = withdrawTypeService.select(withdrawMethod.getWithdrawTypeId());
					if(withdrawType != null){
						withdrawMethod.setOperateValue("withdrawTypeName", withdrawType.getWithdrawTypeName());
					}
				}
			}
		}
		List<Tag> listTag = new ArrayList<Tag>();
		Tag tag = new Tag();
		if(authorizeService.havePrivilege(partner, "withdrawMethod", "get")){
			tag.setTagCode("browse");
			tag.setTagName("browse");
			listTag.add(tag);
		}
		if(authorizeService.havePrivilege(partner, "withdrawMethod", "switch")){
			tag = new Tag();
			tag.setTagCode("switch");
			tag.setTagName("switch");
			listTag.add(tag);
		}
		if(authorizeService.havePrivilege(partner, "withdrawMethod", "w") || authorizeService.havePrivilege(partner, "withdrawMethod", "*")){
			tag = new Tag();
			tag.setTagCode("edit");
			tag.setTagName("edit");
			listTag.add(tag);
		}
		for(Tag t :listTag){
			logger.debug("权限码返回："+t.getTagCode());
		}
		map.put("listTag", listTag);
		map.put("rows",withdrawMethodList);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		return view;
	}


	@RequestMapping(value="/get" + "/{withdrawMethodId}", method=RequestMethod.GET )			
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("withdrawMethodId") int withdrawMethodId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		WithdrawMethod withdrawMethod = withdrawMethodService.select(withdrawMethodId);
		if(withdrawMethod == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + withdrawMethodId + "的提现通道对象");			
		}
		if(withdrawMethod.getOwnerId() != partner.getOwnerId()){
			throw new ObjectNotFoundByIdException("找不到ID=" + withdrawMethodId + "的提现通道对象");			
		}
		List<Tag> listTag = new ArrayList<Tag>();
		Tag tag = new Tag();
		if(authorizeService.havePrivilege(partner, "withdrawMethod", "w") || authorizeService.havePrivilege(partner, "withdrawMethod", "*")){
			tag = new Tag();
			tag.setTagCode("edit");
			tag.setTagName("edit");
			listTag.add(tag);
		}
		for(Tag t :listTag){
			logger.debug("权限码返回："+t.getTagCode());
		}
		map.put("listTag", listTag);
		if(!authorizeService.havePrivilege(partner, "withdrawMethod", "get")){
			logger.error("用户"+partner.getNickName()+"没有查看权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "没有查看权限"));
			return CommonStandard.partnerMessageView;
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setOwnerId(withdrawMethod.getOwnerId());
		dataDefineCriteria.setObjectType("withdrawMethod");
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria );
		if (dataDefineList == null || dataDefineList.size() < 1) {
			logger.warn("合作伙伴类型没有可选自定义字段");

		} else {
			List<HashMap<String, Object>> list = new ArrayList<>();
			for (DataDefine dataDefine : dataDefineList) {
				HashMap<String, Object> map2 = new HashMap<>();
				map2.put("dataCode", dataDefine.getDataCode());
					map2.put("dataValue", withdrawMethod.getExtraValue(dataDefine.getDataCode()));
				
				map2.put("withdrawMethodId", withdrawMethodId);
				map2.put("dataName", dataDefine.getDataName());
				map2.put("dataDefineId", dataDefine.getDataDefineId());
				list.add(map2);
			}
			map.put("configMap", list);
			dataDefineList = null;
		}
		
		//process_class
		String[] beanNamesForType = applicationContextService.getBeanNamesForType(WithdrawProcessor.class);
		logger.debug("beanNames的长度是"+beanNamesForType.length+"***********************************");
		List<HashMap<String, Object>> processList = new ArrayList<>();
		for (String beanName : beanNamesForType) {
			HashMap<String, Object> processMap = new HashMap<>();
			logger.debug("beanName是"+beanName+"+++++++++++++++++++++++++++++++++++++++++++++");
			processMap.put("processName", beanName);
			WithdrawProcessor withdrawProcessor=applicationContextService.getBeanGeneric(beanName);
			processMap.put("processDesc",withdrawProcessor.getDesc());
			processList.add(processMap);
		}
		map.put("process", processList);
		//提现方式
		WithdrawTypeCriteria withdrawTypeCriteria = new WithdrawTypeCriteria();
		withdrawTypeCriteria.setOwnerId(withdrawMethod.getOwnerId());
		List<WithdrawType> list = withdrawTypeService.list(withdrawTypeCriteria);
		map.put("withdrawType", list);
		map.put("withdrawMethod", withdrawMethod);
		return "common/withdrawMethod/get";
	}


	@RequestMapping(value="/update/{payMethodId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@PathVariable("payMethodId") int payMethodId) throws Exception {
		long ownerId = 0;

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;		
		}

		WithdrawMethod node = withdrawMethodService.select(payMethodId);
		if(node == null){
			throw new ParentObjectNotFoundException("找不到指定支付通道[" + payMethodId + "]");				
		}
		if(node.getOwnerId() != ownerId){
			throw new ParentObjectNotFoundException("找不到指定支付通道[" + payMethodId + "]");				
		}


		return "common/node/update";
	}


	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, WithdrawMethod withdrawMethod)
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
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		if(!authorizeService.havePrivilege(partner, "withdrawMethod", "w")){
			logger.error("用户"+partner.getNickName()+"没有更新权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "没有更新权限"));
			return CommonStandard.partnerMessageView;
		}
		// 仅支持修改状态
		WithdrawMethod _oldWithdrawMethod = withdrawMethodService.select(withdrawMethod.getWithdrawMethodId());
		if (_oldWithdrawMethod == null) {
			logger.error("找不到指定的提现通道:" + withdrawMethod.getWithdrawMethodId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,
					"找不到指定的提现通道"));
			return CommonStandard.partnerMessageView;
		}
		
		String updateCurrentStatus = ServletRequestUtils.getStringParameter(request, "updateCurrentStatus");
		if (StringUtils.isNotBlank(updateCurrentStatus)) {
			if (_oldWithdrawMethod.getCurrentStatus() == withdrawMethod.getCurrentStatus()) {
				logger.debug("系统中的支付通道状态与提交修改状态一致，都是:"
						+ withdrawMethod.getCurrentStatus());
				map.put("message", new EisMessage(OperateResult.failed.getId(),
						"状态未变，不需要修改"));
				return CommonStandard.partnerMessageView;
			}
			if (withdrawMethod.getCurrentStatus() == BasicStatus.normal.getId()
					|| withdrawMethod.getCurrentStatus() == BasicStatus.disable.getId()) {
				
				_oldWithdrawMethod.setCurrentStatus(withdrawMethod.getCurrentStatus());
				logger.debug("将支付通道[" + _oldWithdrawMethod.getWithdrawMethodId() + "]状态改为:"
						+ _oldWithdrawMethod.getCurrentStatus());
				
			} else {
				logger.debug("未知或不允许提交的状态:" + withdrawMethod.getCurrentStatus());
				map.put("message", new EisMessage(OperateResult.failed.getId(),
						"不允许修改为指定状态:" + withdrawMethod.getCurrentStatus()));
				return CommonStandard.partnerMessageView;
			}
		}
		
		
		
		String updateDataCode = ServletRequestUtils.getStringParameter(request, "updateDataCode");
		if (StringUtils.isNotBlank(updateDataCode)) {
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
			dataDefineCriteria.setDataCode(updateDataCode);
			dataDefineCriteria.setObjectType("withdrawMethod");
			DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
			if(dataDefine == null){
				logger.error("找不到要更新的商户扩展数据定义:" + updateDataCode);
				map.put("message", new EisMessage(EisError.dataError.getId(), "找不到要更新的数据"+updateDataCode));
				return CommonStandard.partnerMessageView;
			}
			if(dataDefine.getInputLevel() != null){
				if(dataDefine.getInputLevel().equals(InputLevel.system.toString())){
					logger.error("不允许更新system级别的扩展数据:" + updateDataCode);
					map.put("message", new EisMessage(EisError.dataError.getId(), "找不到要更新的数据"));
					return CommonStandard.partnerMessageView;
				}
				if(!isPlatformGenericPartner && dataDefine.getInputLevel().equals(InputLevel.platform.toString())){
					logger.error("当前不是内部用户，不允许更新platform级别的扩展数据:" + updateDataCode);
					map.put("message", new EisMessage(EisError.dataError.getId(), "找不到要更新的数据"));
					return CommonStandard.partnerMessageView;
				}
			}
		}
		String dataValue = ServletRequestUtils.getStringParameter(request, updateDataCode);
		if (StringUtils.isNotBlank(dataValue)) {
			
			
			_oldWithdrawMethod.setExtraValue(updateDataCode, dataValue);

		}
		
		//更新基本数据
		if (StringUtils.isNotBlank(withdrawMethod.getName())) {
			_oldWithdrawMethod.setName(withdrawMethod.getName());
		}
		if (withdrawMethod.getWithdrawTypeId()!=0) {
			_oldWithdrawMethod.setWithdrawTypeId(withdrawMethod.getWithdrawTypeId());
		}
		if (withdrawMethod.getWeight()!=0) {
			_oldWithdrawMethod.setWeight(withdrawMethod.getWeight());
		}
		if (withdrawMethod.getReferUuid()!=0) {
			_oldWithdrawMethod.setReferUuid(withdrawMethod.getReferUuid());
		}
		if (withdrawMethod.getPercent()!=0) {
			_oldWithdrawMethod.setPercent(withdrawMethod.getPercent());
		}
		if (withdrawMethod.getChannelId()!=0) {
			_oldWithdrawMethod.setChannelId(withdrawMethod.getChannelId());
		}
		if (StringUtils.isNotBlank(withdrawMethod.getProcessClass())) {
			_oldWithdrawMethod.setProcessClass(withdrawMethod.getProcessClass());
		}
		
		EisMessage message = null;
		if (withdrawMethodService.update(_oldWithdrawMethod) > 0) {
			message = new EisMessage(OperateResult.success.getId(), "支付通道修改成功");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "支付通道修改失败");
		}

		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception{
		final String view = "common/withdrawMethod/create";
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
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "没有权限"));
			return CommonStandard.partnerMessageView;
		}
		
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
		dataDefineCriteria.setObjectType("withdrawMethod");
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria );
		map.put("dataDefine", dataDefineList);
		
		//process_class
		String[] beanNamesForType = applicationContextService.getBeanNamesForType(WithdrawProcessor.class);
		logger.debug("beanNames的长度是"+beanNamesForType.length+"***********************************");
		List<HashMap<String, Object>> processList = new ArrayList<>();
		for (String beanName : beanNamesForType) {
			HashMap<String, Object> processMap = new HashMap<>();
			logger.debug("beanName是"+beanName+"+++++++++++++++++++++++++++++++++++++++++++++");
			processMap.put("processName", beanName);
			/*beanName="com.maicard.money.service.payProcessor."+beanName.replaceFirst(beanName.substring(0,1), beanName.substring(0,1).toUpperCase());
			Method[] methods = Class.forName(beanName).getMethods();
			for (Method method : methods) {
				if ("getDesc".equals(method.getName())) {
					Object desc = method.invoke(Class.forName(beanName).newInstance());
					processMap.put("processDesc", desc);
				}
			}*/
			WithdrawProcessor withdrawProcessor=applicationContextService.getBeanGeneric(beanName);
			processMap.put("processDesc",withdrawProcessor.getDesc());
			processList.add(processMap);
		}
		map.put("process", processList);
		// 支付方式
		WithdrawTypeCriteria withdrawTypeCriteria = new WithdrawTypeCriteria();
		withdrawTypeCriteria.setOwnerId(ownerId);
		List<WithdrawType> list = withdrawTypeService.list(withdrawTypeCriteria);
		map.put("withdrawType", list);
		return view;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map, WithdrawMethod withdrawMethod ) throws Exception{
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
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"没有权限"));
			return CommonStandard.partnerMessageView;
		}
		if(!authorizeService.havePrivilege(partner, "withdrawMethod", "w") || !authorizeService.havePrivilege(partner, "withdrawMethod", "*")){
			logger.error("用户"+partner.getNickName()+"没有新增权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "没有新增权限"));
			return CommonStandard.partnerMessageView;
		}
		
		
		withdrawMethod.setCurrentStatus(BasicStatus.normal.getId());
		withdrawMethod.setOwnerId(ownerId);
		//payMethod.setCommissionType("COMMISSION_TYPE_RATE");
		
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setOwnerId(ownerId);
		dataDefineCriteria.setObjectType("withdrawMethod");
		List<DataDefine> list = dataDefineService.list(dataDefineCriteria);
		logger.debug("dataDefineList的长度为"+list.size()+"***********************");
		if (list != null && list.size()>0) {
			for (DataDefine dataDefine : list) {
				logger.debug("dataCode的值 是"+dataDefine.getDataCode()+"***************************");
				String dataValue = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
				if (StringUtils.isNotBlank(dataValue)) {
					logger.debug("dataValue的值是"+dataValue+"+++++++++++++++++");
					withdrawMethod.setExtraValue(dataDefine.getDataCode(), dataValue);
					
				}
			}
		}
		EisMessage message = null;
		if (withdrawMethodService.insert(withdrawMethod) == 1) {
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
			HttpServletResponse response, ModelMap map, WithdrawMethod withdrawMethod ) throws Exception{
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
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"没有权限"));
			return view;
		}
		if(!authorizeService.havePrivilege(partner, "withdrawMethod", "w") || !authorizeService.havePrivilege(partner, "withdrawMethod", "*")){
			logger.error("用户"+partner.getNickName()+"没有删除权限");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "没有删除权限"));
			return CommonStandard.partnerMessageView;
		}
		WithdrawMethod oldWithdrawMethod = withdrawMethodService.select(withdrawMethod.getWithdrawMethodId());
		if (oldWithdrawMethod == null) {
			logger.error("找不到指定的提现通道:" + withdrawMethod.getWithdrawMethodId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,
					"找不到指定的提现通道"));
			return CommonStandard.partnerMessageView;
		}
		String updateDataCode = ServletRequestUtils.getStringParameter(request, "updateDataCode");
		/*if (updateDataCode != null) {
			Map<String, String> data = oldWithdrawMethod.getData();
			if (data != null && data.size() > 0) {
				data.put(updateDataCode, "");
			}
		}*/
		
		if(withdrawMethod.getName() != null){
			oldWithdrawMethod.setName(null);
		}
		if(withdrawMethod.getWithdrawTypeId() != 0){
			oldWithdrawMethod.setWithdrawTypeId(0);
		}
		if(withdrawMethod.getWeight() != 0){
			oldWithdrawMethod.setWeight(0);
		}
		if(withdrawMethod.getPercent() != 0){
			oldWithdrawMethod.setPercent(0);
		}
		if(withdrawMethod.getChannelId() != 0){
			oldWithdrawMethod.setChannelId(0);
		}
		if(withdrawMethod.getProcessClass()!=null) {
			oldWithdrawMethod.setProcessClass(null);
		}

		EisMessage message = null;
		if (withdrawMethodService.update(oldWithdrawMethod) > 0) {
			message = new EisMessage(OperateResult.success.getId(), "删除成功");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "删除失败");
		}

		map.put("message", message);
		return view;
	}
}

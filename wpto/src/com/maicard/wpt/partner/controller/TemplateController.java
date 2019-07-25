package com.maicard.wpt.partner.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Language;
import com.maicard.common.service.LanguageService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.criteria.TemplateCriteria;
import com.maicard.site.domain.Template;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/template")
public class TemplateController extends BaseController  {

	@Resource
	private CertifyService certifyService;

	@Resource
	private TemplateService templateService;

	@Resource
	private LanguageService languageService;
	
	@Resource
	private AuthorizeService authorizeService;

	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			TemplateCriteria templateCriteria) throws Exception {
		final String view = "common/template/list";


		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		if (isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.template.name(), "w")) {
			map.put("addUrl", "./template/" + "create");
		}
		
		templateCriteria.setOwnerId(partner.getOwnerId());

		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = templateService.count(templateCriteria);
		map.put("total", totalRows);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回的模版数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		templateCriteria.setPaging(paging);

		templateCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<Template> templateList = templateService.listOnPage(templateCriteria);
		for(Template template:templateList){
			template.setOperate(new HashMap<String,String>());
			template.getOperate().put("get", "./template/" + "get" + "/" + template.getTemplateId());
			template.getOperate().put("del", "./template/" + "delete");		
			template.getOperate().put("update", "./template/" + "update" + "/" + template.getTemplateId());		
		}		
		map.put("rows",templateList);
		return view;

	}

	@RequestMapping(value="/get" + "/{templateId}", method=RequestMethod.GET )		
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("templateId") Integer templateId) throws Exception {
		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		if(templateId == 0){
			logger.error("请求中找不到必须的参数[templateId]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"未提供要查看的模版ID"));
			return CommonStandard.partnerMessageView;
		}
		Template template = templateService.select(templateId);
		if(template == null){
			logger.error("找不到ID=" + templateId + "的template对象");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"找不到ID=" + templateId + "的模版"));
			return CommonStandard.partnerMessageView;
		}	
		
		if(template.getOwnerId() != partner.getOwnerId()){
			logger.error("模版[" + template.getTemplateId() + "]的ownerId[" + template.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"找不到ID=" + templateId + "的模版"));
			return CommonStandard.partnerMessageView;		}
		map.put("template", template);
		map.put("title", "查看系统模版");

		return "common/template/" + "get";
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)		
	@AllowJsonOutput
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Template template = templateService.select(deleteId);
			if(template == null){
				logger.warn("找不到要删除的模版，ID=" + deleteId);
				continue;
			}
			if(template.getOwnerId() != partner.getOwnerId()){
				logger.warn("要删除的模版，ownerId[" + template.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(templateService.delete(deleteId) > 0){

					successDeleteCount++;
				}
			}catch(DataIntegrityViolationException forignKeyException ){
				String error  = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}
		}

		String messageContent = "成功删除[" + successDeleteCount + "]个.";
		if(!errors.equals("")){
			messageContent += errors;
		}
		logger.info(messageContent);
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)	
	public String getCreate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("templateCriteria") TemplateCriteria templateCriteria) throws Exception {

		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Language> languageList = languageService.list(languageCriteria);
		map.put("title", "创建系统模版");

		map.put("templateCriteria", templateCriteria);
		map.put("statusCodeList", BasicStatus.values());
		map.put("languageList", languageList);
		map.put("template", new Template());
		return "common/template/" + "create";
	}


	@RequestMapping(value="/create", method=RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("template") Template template) throws Exception {

		final String view = CommonStandard.partnerMessageView;
		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		template.setOwnerId(partner.getOwnerId());
		try{
			templateService.insert(template);
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));			
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
			String m = e.getMessage();
			if(m != null && m.indexOf("Duplicate entry") > 0){
				map.put("message", new EisMessage(EisError.dataDuplicate.id, "数据重复，请检查输入"));
				return view;		
			}
			map.put("message", new EisMessage(EisError.dataError.id, "无法新增模版"));
			return view;	
		}
		return view;
	}

	@RequestMapping(value="/update" + "/{templateId}", method=RequestMethod.GET)	
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("templateCriteria") TemplateCriteria templateCriteria,
			@PathVariable("templateId") int templateId) throws Exception {

		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Language> languageList = languageService.list(languageCriteria);
		User loginedSysUser = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(loginedSysUser == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		map.put("title", "编辑系统模版");
		map.put("templateCriteria", templateCriteria);
		map.put("statusCodeList", BasicStatus.values());
		map.put("languageList", languageList);
		Template template = null;
		if(templateId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[templateId]");
		}
		template = templateService.select(templateId);
		if(template == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + templateId + "的template对象");			
		}
		if(template.getOwnerId() != loginedSysUser.getOwnerId()){
			logger.error("模版[" + template.getTemplateId() + "]的ownerId[" + template.getOwnerId() + "]不属于当前ownerId:" + loginedSysUser.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + templateId + "的template对象");			
		}
		map.put("template", template);
		return "common/template/" + "update";
	}


	@RequestMapping(value="/update", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("template") Template template) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		Template _oldTemplate = templateService.select(template.getTemplateId());
		if(_oldTemplate == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + template.getTemplateId() + "的模版对象");			
		}
		if(_oldTemplate.getOwnerId() != partner.getOwnerId()){
			logger.error("模版[" + _oldTemplate.getTemplateId() + "]的ownerId[" + _oldTemplate.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + _oldTemplate.getTemplateId() + "的模版对象");			
		}
		template.setOwnerId(partner.getOwnerId());
		try{
			templateService.update(template);
			map.put("message", new EisMessage(OperateResult.success.getId(),"修改成功"));
		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();	
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return CommonStandard.partnerMessageView;
	}


}

package com.maicard.wpt.partner.controller;

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

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SiteThemeRelation;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.SiteThemeRelationService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.common.criteria.ThemeCriteria;
import com.maicard.common.domain.Theme;
import com.maicard.common.service.ThemeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

import static com.maicard.standard.CommonStandard.partnerMessageView;


@Controller
@RequestMapping("/theme")
public class ThemeController extends BaseController  {

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;

	@Resource
	private ThemeService themeService;

	@Resource
	private DataDefineService dataDefineService;

	@Resource
	private PartnerService partnerService;

	@Resource
	private SiteThemeRelationService siteThemeRelationService;


	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			ThemeCriteria themeCriteria) throws Exception {
		final String view = "common/theme/index";


		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		themeCriteria.setOwnerId(partner.getOwnerId());

		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = themeService.count(themeCriteria);
		map.put("total", totalRows);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回的主题数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		themeCriteria.setPaging(paging);

		themeCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<Theme> themeList = themeService.listOnPage(themeCriteria);
		if(themeList == null || themeList.size() < 1){

		} else {
			authorizeService.writeOperate(partner, themeList);
			//获取用户当前使用的主题
			Theme useTheme = themeService.selectByUser(partner.getUuid());
			if(useTheme != null){
				map.put(DataName.frontTheme.toString(), useTheme.getThemeCode());
			}
				
		}
		map.put("rows",themeList);
		return view;

	}

	@RequestMapping(value="/get" + "/{themeId}", method=RequestMethod.GET )		
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("themeId") Integer themeId) throws Exception {
		if(themeId == 0){
			logger.error("请求中找不到必须的参数[themeId]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"未提供要查看的主题ID"));
			return partnerMessageView;
		}
		Theme theme = themeService.select(themeId);
		if(theme == null){
			logger.error("找不到ID=" + themeId + "的theme对象");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"找不到ID=" + themeId + "的主题"));
			return partnerMessageView;
		}	
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(theme.getOwnerId() != partner.getOwnerId()){
			logger.error("主题[" + theme.getThemeId() + "]的ownerId[" + theme.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"找不到ID=" + themeId + "的主题"));
			return partnerMessageView;		}
		map.put("theme", theme);
		map.put("title", "查看主题");

		return "common/theme/" + "get";
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)		
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
		if(!authorizeService.isPlatformGenericPartner(partner)){
			//非内部用户不允许删除
			logger.error("非内部用户[" + partner.getUuid() + "]不允许删除主题");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"您无权删除主题"));
			return partnerMessageView;
			
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Theme theme = themeService.select(deleteId);
			if(theme == null){
				logger.warn("找不到要删除的主题，ID=" + deleteId);
				continue;
			}
			if(theme.getOwnerId() != partner.getOwnerId()){
				logger.warn("要删除的主题，ownerId[" + theme.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(themeService.delete(deleteId) > 0){

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
		return partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)	
	public String getCreate(HttpServletRequest request, HttpServletResponse response,ModelMap map) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(!authorizeService.isPlatformGenericPartner(partner)){
			//非内部用户不允许删除
			logger.error("非内部用户[" + partner.getUuid() + "]不允许新建主题");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"您无权新建主题"));
			return partnerMessageView;
			
		}
		map.put("statusCodeList", BasicStatus.values());
		map.put("theme", new Theme());
		return "common/theme/" + "create";
	}


	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("theme") Theme theme) throws Exception {

		final String view = partnerMessageView;
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(!authorizeService.isPlatformGenericPartner(partner)){
			//非内部用户不允许删除
			logger.error("非内部用户[" + partner.getUuid() + "]不允许新建主题");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"您无权新建主题"));
			return partnerMessageView;
			
		}
		theme.setOwnerId(partner.getOwnerId());
		try{
			themeService.insert(theme);
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));			
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
			String m = e.getMessage();
			if(m != null && m.indexOf("Duplicate entry") > 0){
				map.put("message", new EisMessage(EisError.dataDuplicate.id, "数据重复，请检查输入"));
				return view;		
			}
			map.put("message", new EisMessage(EisError.dataError.id, "无法新增主题"));
			return view;	
		}
		return view;
	}

	@RequestMapping(value="/update" + "/{themeId}", method=RequestMethod.GET)	
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("themeCriteria") ThemeCriteria themeCriteria,
			@PathVariable("themeId") int themeId) throws Exception {

		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		User loginedSysUser = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(loginedSysUser == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		map.put("title", "编辑主题");
		map.put("themeCriteria", themeCriteria);
		map.put("statusCodeList", BasicStatus.values());
		Theme theme = null;
		if(themeId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[themeId]");
		}
		theme = themeService.select(themeId);
		if(theme == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + themeId + "的theme对象");			
		}
		if(theme.getOwnerId() != loginedSysUser.getOwnerId()){
			logger.error("主题[" + theme.getThemeId() + "]的ownerId[" + theme.getOwnerId() + "]不属于当前ownerId:" + loginedSysUser.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + themeId + "的theme对象");			
		}
		map.put("theme", theme);
		return "common/theme/" + "update";
	}


	@RequestMapping(value="/update", method=RequestMethod.POST)	
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("theme") Theme theme) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Theme _oldTheme = themeService.select(theme.getThemeId());
		if(_oldTheme == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + theme.getThemeId() + "的主题对象");			
		}
		if(_oldTheme.getOwnerId() != partner.getOwnerId()){
			logger.error("主题[" + _oldTheme.getThemeId() + "]的ownerId[" + _oldTheme.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + _oldTheme.getThemeId() + "的主题对象");			
		}
		theme.setOwnerId(partner.getOwnerId());
		try{
			themeService.update(theme);
			map.put("message", new EisMessage(OperateResult.success.getId(),"修改成功"));
		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();	
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return partnerMessageView;
	}
	//用户确认选择了某个主题
	@RequestMapping("/relate")
	public String submitSelect(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}


		int themeId = (int)ServletRequestUtils.getLongParameter(request, "themeId", 0);
		if(themeId <= 0){
			logger.error("用户未选择要使用的主题");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要使用的主题"));
			return partnerMessageView;		
		}

		Theme theme = themeService.select(themeId);
		if(theme == null){
			logger.error("找不到用户选择的主题:" + themeId);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到指定的主题"));
			return partnerMessageView;	
		}

		SiteThemeRelation sitemThemeRelation = new SiteThemeRelation();
		sitemThemeRelation.setUuid(partner.getUuid());
		sitemThemeRelation.setThemeId(themeId);
		sitemThemeRelation.setCurrentStatus(BasicStatus.normal.getId());
		sitemThemeRelation.setHostCode(partner.getUsername());
		siteThemeRelationService.updateForUuid(sitemThemeRelation);

		return partnerMessageView;

	}


}

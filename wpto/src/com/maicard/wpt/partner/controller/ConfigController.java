package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.ConfigCriteria;
import com.maicard.common.domain.Config;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Node;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.ConfigCategory;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController{

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private NodeService nodeService;
	@Resource
	private PartnerRoleService partnerRoleService;
	
	
	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ConfigCriteria configCriteria) throws Exception {
		final String view = "common/config/list";
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(),"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。"));
			return CommonStandard.partnerMessageView;
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
		//configCriteria.setCategory(ServletRequestUtils.getStringParameters(request, "category"));

		configCriteria.setOwnerId(partner.getOwnerId());
		configCriteria.setAllConfig(true);
		if(configCriteria.getCategory() == null || configCriteria.getCategory().length < 1){
			logger.debug("当前查询条件未指定任何分类，使用默认分类");
			configCriteria.setCategory(CommonStandard.partnerViewConfigCategory);
		} else {
			List<String> validCategory = new ArrayList<String>();
			for(String ct : configCriteria.getCategory()){
				if(ct.equalsIgnoreCase(ConfigCategory.personal.name())){
					logger.debug("当前查询条件指定了查询personal分类");
					validCategory.add(ct);
				} else {
					boolean isValid = false;
					for(String vct : CommonStandard.partnerViewConfigCategory){
						if(vct.equalsIgnoreCase(ct)){
							isValid = true;
							logger.debug("当前查询条件指定了查询合法分类:" + ct);
							break;
						}
					}
					if(isValid){
						validCategory.add(ct);
					}
				}
			}		
			String[] validCategoryArray = new String[validCategory.size()];
			configCriteria.setCategory(validCategory.toArray(validCategoryArray));
		}
		if(configCriteria.getCategory() == null || configCriteria.getCategory().length < 1){
			logger.debug("当前查询条件未指定任何分类，使用默认分类");
			configCriteria.setCategory(CommonStandard.partnerViewConfigCategory);
		}
		StringBuffer sb = new StringBuffer();
		for(String ct : configCriteria.getCategory()){
			sb.append(ct).append(',');
		}
		logger.debug("当前查询的系统配置分类:" + sb.toString().replaceAll(",$", ""));
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 100);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = configService.count(configCriteria);

		map.put("total", totalRows);

		if(totalRows < 1){
			logger.debug("配置数据totalRows=0");
			return view;
		}
		Paging paging = new Paging(rows);
		configCriteria.setPaging(paging);
		configCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");

		String[] beanNames = applicationContextService.getBeanNamesForType(NodeProcessor.class);
		map.put("siteNodeProcessorList", beanNames);
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setOwnerId(ownerId);
		List<Node> nodeList = nodeService.list(nodeCriteria );
		List<Integer> statusList = new ArrayList<>();
		map.put("displayNodeInFirstPageConfig", nodeList);
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.id);
		}
		map.put("statusList", statusList);
		
		RoleCriteria partnerRoleCriteria = new RoleCriteria();
		partnerRoleCriteria.setOwnerId(ownerId);
		partnerRoleCriteria.setCurrentStatus(BasicStatus.normal.id);
		List<Role> roleList = partnerRoleService.list(partnerRoleCriteria );
		map.put("partnerDefaultRoleid", roleList);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

		List<Config> configList = configService.listOnPage(configCriteria);
		for(Config config:configList){
			config.setCategoryDescription(ConfigCategory.site.findByCode(config.getCategory()).getDescription());
		}
		Collections.sort(configList, new Comparator<Config>(){

			@Override
			public int compare(Config o1, Config o2) {
				if(o1.getOwnerId() > o2.getOwnerId()){
					return 1;
				}
				return -1;
			}

		});
		map.put("rows",configList);
		map.put("categoryList", configCriteria.getCategory());
		return view;
	}




	@RequestMapping(value="/get/{configId}")	
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map, ConfigCriteria configCriteria,
			@PathVariable("configId") Integer configId) throws Exception {

		if(configId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[configId]");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Config config = configService.select(configId);
		if(config == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + configId + "的config对象");			
		}
		if(config.getOwnerId() != partner.getOwnerId()){
			throw new ObjectNotFoundByIdException("找不到ID=" + configId + "的config对象");			
		}
		try{
			config.setCategoryDescription(ConfigCategory.site.findByCode(config.getCategory()).getDescription());
		}catch(Exception e){}
		map.put("config", config);

		map.put("configCriteria", configCriteria);
		return "common/config/get";
	}

	@RequestMapping(value="/delete")	
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map, String idList) throws Exception {		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		String[] ids = idList.split("-");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Config config = configService.select(deleteId);
			if(config == null){
				throw new ObjectNotFoundByIdException("找不到ID=" + deleteId + "的config对象");			
			}
			if(config.getOwnerId() != partner.getOwnerId()){
				throw new ObjectNotFoundByIdException("找不到ID=" + deleteId + "的config对象");			
			}
			try{
				if(configService.delete(deleteId) > 0){
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
		dataMap.put("message", new EisMessage(OperateResult.success.getId(),messageContent));
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map, ConfigCriteria configCriteria) throws Exception {
		Config config = new Config();		
		map.put("config", config);
		map.put("configCriteria", configCriteria);
		map.put("statusCodeList", BasicStatus.values());
		map.put("categoryCodeList", ConfigCategory.values());
		return "common/config/create";
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, Config config) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		config.setOwnerId(partner.getOwnerId());
		try{
			configService.insert(config);
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));
		}catch(Exception e){
			String message = "数据操作失败" + e.getMessage();			
			logger.error(message);
			throw new DataWriteErrorException(message);
		}
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/update/{configId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map, ConfigCriteria configCriteria,
			@PathVariable("configId") Integer configId
			) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		Config config = null;
		if(configId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[configId]");
		}
		config = configService.select(configId);
		if(config == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + configId + "的config对象");			
		}
		if(config.getOwnerId() != partner.getOwnerId()){
			throw new ObjectNotFoundByIdException("找不到ID=" + configId + "的config对象");			
		}
		map.put("config", config);
		map.put("configCriteria", configCriteria);
		map.put("statusCodeList", BasicStatus.values());
		map.put("categoryCodeList", ConfigCategory.values());
		return "common/config/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map, Config config) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(config.getConfigId() < 1){
			map.put("message", new EisMessage(EisError.dataError.getId(),"错误的config对象"));
			return CommonStandard.partnerMessageView;
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

		config.setOwnerId(partner.getOwnerId());
		try{
			configService.update(config);
			map.put("message", new EisMessage(OperateResult.success.getId(),"更新成功"));
		}catch(Exception e){
			String message = "数据操作失败" + e.getMessage();			
			logger.error(message);
			throw new DataWriteErrorException(message);
		}
		return CommonStandard.partnerMessageView;
	}
}

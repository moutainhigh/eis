package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.PrivilegeRoleRelation;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerPrivilegeRoleRelationService;
import com.maicard.security.service.PartnerPrivilegeService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/partnerPrivilege")
public class PartnerPrivilegeController extends BaseController {
	@Resource
	private CertifyService certifyService;
	@Resource
	private PartnerPrivilegeService partnerPrivilegeService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ConfigService configService;
	@Resource
	private PartnerRoleService partnerRoleService;
	@Resource
	private PartnerPrivilegeRoleRelationService partnerPrivilegeRoleRelationService;
	
	
	private int rowsPerPage = 10;
	
	


	@PostConstruct
	public void init() {
		rowsPerPage = configService.getIntValue(
				DataName.partnerRowsPerPage.toString(), 0);
		if (rowsPerPage < 1) {
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, PrivilegeCriteria privilegeCriteria) throws Exception {

		//map.put("addUrl", "./partnerPrivilege/" + "create");
		final String view = "common/partnerPrivilege/index";
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,UserTypes.partner.getId());
		if (partner == null) {
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

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "没有访问权限"));
			return CommonStandard.partnerMessageView;
		}
		
		if(isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.partnerPrivilege.name(), "w")){
			map.put("isShow", "show" );
		}
		
		privilegeCriteria.setOwnerId(ownerId);
		int rows = ServletRequestUtils.getIntParameter(request, "rows",rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		Paging paging = new Paging(rows);
		privilegeCriteria.setPaging(paging);
		privilegeCriteria.getPaging().setCurrentPage(page);
		int totalRows = partnerPrivilegeService.count(privilegeCriteria);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		
		
		List<Privilege> partnerPrivilegeList = partnerPrivilegeService.listOnPage(privilegeCriteria);
		/*for(Privilege partnerPrivilege:partnerPrivilegeList){
			partnerPrivilege.setOperate(new HashMap<String,String>());
			partnerPrivilege.getOperate().put("get", "./partnerPrivilege/" + "get" + "/" + partnerPrivilege.getPrivilegeId());
			partnerPrivilege.getOperate().put("del", "./partnerPrivilege/delete");				
			partnerPrivilege.getOperate().put("update", "./partnerPrivilege/update/"+ partnerPrivilege.getPrivilegeId());
		}*/
		
		//角色列表
		RoleCriteria partnerRoleCriteria = new RoleCriteria();
		partnerRoleCriteria.setOwnerId(ownerId);
		List<Role> roleList = partnerRoleService.list(partnerRoleCriteria);
		map.put("operateCode", Operate.values());
		map.put("objectTypes", ObjectType.values());
		map.put("statusCodeList",BasicStatus.values());
		map.put("role", roleList);
		map.put("total", totalRows);
		map.put("rows",partnerPrivilegeList);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		return view;

	}

	@RequestMapping(value="/get" + "/{privilegeId}", method=RequestMethod.GET )	
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("partnerPrivilegeCriteria") PrivilegeCriteria privilegeCriteria,
			@PathVariable("privilegeId") Integer privilegeId) throws Exception {
		final String view = "common/partnerPrivilege/get";
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,UserTypes.partner.getId());
		if (partner == null) {
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

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "没有访问权限"));
			return CommonStandard.partnerMessageView;
		}
		if(privilegeId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[privilegeId]");
		}
		Privilege partnerPrivilege = partnerPrivilegeService.select(privilegeId);
		if(partnerPrivilege == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + privilegeId + "的partnerPrivilege对象");			
		}		
		if(partnerPrivilege.getOwnerId() != partner.getOwnerId()){
			logger.error("系统权限[" + partnerPrivilege.getPrivilegeId() + "]的ownerId[" + partnerPrivilege.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + privilegeId + "的partnerPrivilege对象");			
		}
		map.put("operateCode", Operate.values());
		map.put("objectTypes", ObjectType.values());
		List<Integer> statusList = new ArrayList<>();
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.getId());
		}
		//获得角色列表
		PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria = new PrivilegeRoleRelationCriteria();
		privilegeRoleRelationCriteria.setPrivilegeId(privilegeId);
		List<PrivilegeRoleRelation> list = partnerPrivilegeRoleRelationService.list(privilegeRoleRelationCriteria);
		List<Role> roles = new ArrayList<>(); 
		if (list != null && list.size() > 0) {
			for (PrivilegeRoleRelation privilegeRoleRelation : list) {
				Role role = partnerRoleService.select(privilegeRoleRelation.getRoleId());
				if (role != null) {
					roles.add(role);
				}
			}
		}
		RoleCriteria partnerRoleCriteria = new RoleCriteria();
		partnerRoleCriteria.setOwnerId(ownerId);
		List<Role> roleList = partnerRoleService.list(partnerRoleCriteria);
		map.put("roleList", roleList);
		map.put("roles", roles);
		map.put("statusList", statusList);
		map.put("partnerPrivilege", partnerPrivilege);
		map.put("partnerPrivilegeCriteria", privilegeCriteria);
		return view;
	}
	
	/*@RequestMapping(value="/delete", method=RequestMethod.GET)	
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
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Privilege partnerPrivilege = partnerPrivilegeService.select(deleteId);
			if(partnerPrivilege == null){
				logger.warn("找不到要删除的系统权限，ID=" + deleteId);
				continue;
			}

			if(partnerPrivilege.getOwnerId() != partner.getOwnerId()){
				logger.warn("要删除的系统权限，ownerId[" + partnerPrivilege.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(partnerPrivilegeService.delete(Integer.parseInt(ids[i])) > 0){
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
*/
	@RequestMapping(value="/delete" + "/{privilegeId}")	
	@AllowJsonOutput
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("privilegeId") Integer privilegeId) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,UserTypes.partner.getId());
		if (partner == null) {
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

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "没有访问权限"));
			return CommonStandard.partnerMessageView;
		}
		Privilege partnerPrivilege = partnerPrivilegeService.select(privilegeId);
		if(partnerPrivilege == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + privilegeId + "的partnerPrivilege对象");			
		}
		PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria = new PrivilegeRoleRelationCriteria();
		privilegeRoleRelationCriteria.setPrivilegeId(privilegeId);
		List<PrivilegeRoleRelation> list = partnerPrivilegeRoleRelationService.list(privilegeRoleRelationCriteria );
		if (list != null && list.size() > 0) {
			for (PrivilegeRoleRelation privilegeRoleRelation : list) {
				int status = partnerPrivilegeRoleRelationService.delete(privilegeRoleRelation.getPrivilegeRoleRelationId());
				if(status != 1){
					map.put("message", new EisMessage(OperateResult.failed.getId(), "操作失败"));
				}
			}
		}
		if (partnerPrivilegeService.delete(privilegeId)==1) {
			map.put("message", new EisMessage(OperateResult.success.getId(), "操作完成"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "操作失败"));
		}
		return view;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)	
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {


		map.put("statusCodeList",BasicStatus.values());
		map.put("operators", Operate.values());
		map.put("objectTypes", ObjectType.values());
		map.put("partnerPrivilege", new Privilege());
		return "partnerPrivilege/" + "create";
	}

	@RequestMapping(value="/create",method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("partnerPrivilege") Privilege partnerPrivilege) throws Exception {
		
		logger.info("尝试新增系统权限:操作代码:" + partnerPrivilege.getOperateCode());
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

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "没有访问权限"));
			return CommonStandard.partnerMessageView;
		}
		partnerPrivilege.setOwnerId(partner.getOwnerId());
		partnerPrivilege.setCurrentStatus(BasicStatus.normal.getId());
		String[] roleIds = request.getParameterValues("role");
		if(partnerPrivilegeService.insert(partnerPrivilege) > 0){
			if (roleIds != null && roleIds.length > 0) {
				for (String roleId : roleIds) {
					PrivilegeRoleRelation privilegeGroupRelation = new PrivilegeRoleRelation();
					privilegeGroupRelation.setRoleId(Integer.parseInt(roleId));
					privilegeGroupRelation.setCurrentStatus(BasicStatus.normal.getId());
					privilegeGroupRelation.setPrivilegeId(partnerPrivilege.getPrivilegeId());
					privilegeGroupRelation.setOwnerId(ownerId);
					partnerPrivilegeRoleRelationService.insert(privilegeGroupRelation);
				}
			}
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"添加失败"));
		}
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/update" + "/{privilegeId}", method=RequestMethod.GET)	
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("privilegeId") Integer privilegeId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		map.put("statusCodeList", BasicStatus.values());
		map.put("operators", Operate.values());
		map.put("objectTypes", ObjectType.values());
		if(privilegeId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[privilegeId]");
		}
		Privilege partnerPrivilege = partnerPrivilegeService.select(privilegeId);
		if(partnerPrivilege == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + privilegeId + "的partnerPrivilege对象");			
		}	
		if(partnerPrivilege.getOwnerId() != partner.getOwnerId()){
			logger.error("系统权限[" + partnerPrivilege.getPrivilegeId() + "]的ownerId[" + partnerPrivilege.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + privilegeId + "的partnerPrivilege对象");			
		}
		map.put("partnerPrivilege", partnerPrivilege);
		return "partnerPrivilege/" + "update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("partnerPrivilege") Privilege partnerPrivilege) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(partnerPrivilege.getPrivilegeId() < 1){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[privilegeId]");
		}
		/*if(partnerPrivilege.getOwnerId() != partner.getOwnerId()){
			logger.error("系统权限[" + partnerPrivilege.getPrivilegeId() + "]的ownerId[" + partnerPrivilege.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + partnerPrivilege.getPrivilegeId() + "的partnerPrivilege对象");			
		}*/
		try{
			Privilege old = partnerPrivilegeService.select(partnerPrivilege.getPrivilegeId());
			if (old!=null) {
				if (StringUtils.isNotBlank(partnerPrivilege.getObjectTypeCode())) {
					old.setObjectTypeCode(partnerPrivilege.getObjectTypeCode());
				}
				if (StringUtils.isNotBlank(partnerPrivilege.getPrivilegeDesc())) {
					old.setPrivilegeDesc(partnerPrivilege.getPrivilegeDesc());
				}
				if (StringUtils.isNotBlank(partnerPrivilege.getPrivilegeName())) {
					old.setPrivilegeName(partnerPrivilege.getPrivilegeName());
				}
				if (StringUtils.isNotBlank(partnerPrivilege.getOperateCode())) {
					old.setOperateCode(partnerPrivilege.getOperateCode());
				}
				if (StringUtils.isNotBlank(partnerPrivilege.getObjectList())) {
					old.setObjectList(partnerPrivilege.getObjectList());
				}
				if (StringUtils.isNotBlank(partnerPrivilege.getObjectAttributePattern())) {
					old.setObjectAttributePattern(partnerPrivilege.getObjectAttributePattern());
				}
				if (partnerPrivilege.getCurrentStatus() > 0) {
					old.setCurrentStatus(partnerPrivilege.getCurrentStatus());
				}
				
				String roleIds = ServletRequestUtils.getStringParameter(request, "roleIds");
				if (StringUtils.isNotBlank(roleIds)) {
					PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria = new PrivilegeRoleRelationCriteria();
					privilegeRoleRelationCriteria.setPrivilegeId(partnerPrivilege.getPrivilegeId());
					List<PrivilegeRoleRelation> list = partnerPrivilegeRoleRelationService.list(privilegeRoleRelationCriteria );
					if (list != null && list.size() > 0) {
						for (PrivilegeRoleRelation privilegeRoleRelation : list) {
							int status = partnerPrivilegeRoleRelationService.delete(privilegeRoleRelation.getPrivilegeRoleRelationId());
							if(status != 1){
								map.put("message", new EisMessage(OperateResult.failed.getId(), "操作失败"));
							}
						}
					}
					String[] split = roleIds.split(",");
					for (String roleId : split) {
						PrivilegeRoleRelation privilegeGroupRelation = new PrivilegeRoleRelation();
						privilegeGroupRelation.setRoleId(Integer.parseInt(roleId));
						privilegeGroupRelation.setCurrentStatus(BasicStatus.normal.getId());
						privilegeGroupRelation.setPrivilegeId(partnerPrivilege.getPrivilegeId());
						privilegeGroupRelation.setOwnerId(partner.getOwnerId());
						partnerPrivilegeRoleRelationService.insert(privilegeGroupRelation);
					}
				}else {
					partnerPrivilegeService.update(old);
				}
				map.put("message", new EisMessage(OperateResult.success.getId(),"修改成功"));
			}else {
				map.put("message", new EisMessage(OperateResult.failed.getId(),"找不到要修改的对象"));
			}
		}catch(Exception e){
			throw new DataWriteErrorException("数据操作失败" + e.getMessage());
		}
		//map.put("message", message);
		return CommonStandard.partnerMessageView;
	}
}

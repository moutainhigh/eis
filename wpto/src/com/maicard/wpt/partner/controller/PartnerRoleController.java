package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.*;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.PartnerPrivilegeRoleRelationDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.PrivilegeRoleRelation;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerPrivilegeService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/partnerRole")
public class PartnerRoleController extends BaseController {
	@Resource
	private CertifyService certifyService;
	@Resource
	private PartnerRoleService partnerRoleService;
	@Resource
	private PartnerPrivilegeService partnerPrivilegeService;
	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;
	@Resource
	private PartnerPrivilegeRoleRelationDao partnerPrivilegeRoleRelationDao;
	@Resource
	private PartnerService partnerService;


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, RoleCriteria roleCriteria) throws Exception {
		int totalRows = partnerRoleService.count(roleCriteria);
		map.put("addUrl", "./partnerRole/create");
		map.put("title","角色列表");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Paging paging = new Paging(100);
		roleCriteria.setPaging(paging);
		roleCriteria.getPaging().setCurrentPage(1);
		roleCriteria.setOwnerId(partner.getOwnerId());
		List<Role> partnerRoleList = partnerRoleService.list(roleCriteria);
		for(Role partnerRole:partnerRoleList){
			partnerRole.setOperate(new HashMap<String,String>());
			partnerRole.getOperate().put("get", "./partnerRole/" + "get" + "/"+ partnerRole.getRoleId() + CommonStandard.DEFAULT_PAGE_SUFFIX);
			partnerRole.getOperate().put("del", "./partnerRole/" + "delete" + CommonStandard.DEFAULT_PAGE_SUFFIX);				
			partnerRole.getOperate().put("update", "./partnerRole/" + "update" + "/" + partnerRole.getRoleId() + CommonStandard.DEFAULT_PAGE_SUFFIX);
			if(partnerRole.getRoleType() == 1){//组织架构单元而非岗位
				partnerRole.getOperate().put("add", "./partnerRole/" + "create" + "/" + partnerRole.getRoleId() + CommonStandard.DEFAULT_PAGE_SUFFIX);
			} else if(partnerRole.getRoleType() == 2){
				partnerRole.getOperate().put("menuEdit", "./sysMenuRoleRelation/" + "update" + "/" + partnerRole.getRoleId() + CommonStandard.DEFAULT_PAGE_SUFFIX);
			}
		}
		map.put("total", totalRows);
		map.put("rows",partnerRoleList);
		if(partnerRoleList != null){
			ObjectMapper mapper = new ObjectMapper();
			String partnerRoleListString = "{\"rows\":" +mapper.writeValueAsString(partnerRoleList) + "}";
			partnerRoleListString = partnerRoleListString.replaceAll("\"parentRoleId\"", "\"_parentId\"");
			partnerRoleListString = partnerRoleListString.replaceAll("\"roleName\"", "\"name\"");
			map.put("tree", partnerRoleListString);
		}

		return "common/partnerRole/list";

	}

	@RequestMapping(value="/get" + "/{roleId}", method=RequestMethod.GET )		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("partnerRoleCriteria") RoleCriteria roleCriteria,
			@PathVariable("roleId")Integer roleId) throws Exception {
		if(roleId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[roleId]");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Role partnerRole = partnerRoleService.select(roleId);
		if(partnerRole == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + roleId + "的partnerRole对象");			
		}	
		if(partnerRole.getOwnerId() != partner.getOwnerId()){
			logger.error("PartnerRole[" + partnerRole.getRoleId() + "]的ownerId[" + partnerRole.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + roleId + "的PartnerRole对象");			
		}
		//获取与角色关联的权限
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria(partner.getOwnerId());
		privilegeCriteria.setRoleIds(roleId);
		List<Privilege> partnerPrivilegeList = partnerPrivilegeService.listByRole(privilegeCriteria);
		logger.debug("与" + roleId + "#角色对应的权限有:" + (partnerPrivilegeList == null ? "空" : partnerPrivilegeList.size()));
		//获取与角色关联的用户
		UserRoleRelationCriteria partnerRoleRelationCriteria = new UserRoleRelationCriteria(partner.getOwnerId());
		partnerRoleRelationCriteria.setRoleId(roleId);
		List<UserRoleRelation> userRoleRelationList = partnerRoleRelationService.list(partnerRoleRelationCriteria);
		if(userRoleRelationList == null || userRoleRelationList.size() < 1){
			logger.info("没有任何用户关联到角色:" + roleId);
		} else {
			List<User> partnerList = new ArrayList<User>();
			for(UserRoleRelation userRoleRelation : userRoleRelationList){
				User p = partnerService.select(userRoleRelation.getUuid());
				if(p == null){
					logger.error("找不到与" + roleId + "#角色关联的parnter:" + userRoleRelation.getUuid());
					continue;
				}
				if(p.getOwnerId() != partner.getOwnerId()){
					logger.error("找与" + roleId + "#角色关联的parnter:" + userRoleRelation.getUuid() + "，其ownerId与当前用户[" + partner.getOwnerId() + "]的不一致");
					continue;
				}
				partnerList.add(p);
			}
			map.put("partnerList", partnerList);

		}
		map.put("partnerRole", partnerRole);
		map.put("partnerRoleCriteria", roleCriteria);
		map.put("partnerPrivilegeList",partnerPrivilegeList);

		return "common/partnerRole/" + "get";
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

		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Role partnerRole = partnerRoleService.select(deleteId);
			if(partnerRole == null){
				logger.warn("找不到要删除的PartnerRole，ID=" + deleteId);
				continue;
			}

			if(partnerRole.getOwnerId() != partner.getOwnerId()){
				logger.warn("要删除的PartnerRole，ownerId[" + partnerRole.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(partnerRoleService.delete(Integer.parseInt(ids[i])) > 0){
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

	@RequestMapping(value="/create"+"/{parentRoleId}", method=RequestMethod.GET)	
	public String getCreate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("partnerRoleCriteria") RoleCriteria roleCriteria,
			@PathVariable("parentRoleId") int parentRoleId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Role parentRole  = partnerRoleService.select(parentRoleId);
		if(parentRole == null){

			logger.error("找不到父角色:" + parentRoleId);
			return CommonStandard.partnerMessageView;		
		}
		if(parentRole.getOwnerId() != partner.getOwnerId()){
			logger.error("找不到父角色:" + parentRoleId);
			return CommonStandard.partnerMessageView;		
		}
		roleCriteria.setOwnerId(partner.getOwnerId());
		RoleCriteria partnerRoleCriteria2 = new RoleCriteria();
		partnerRoleCriteria2.setRoleType(RoleCriteria.ROLE_TYPE_DEPARTMENT);
		partnerRoleCriteria2.setCurrentStatus(BasicStatus.normal.getId());
		partnerRoleCriteria2.setOwnerId(partner.getOwnerId());
		//类型为1即为组织单元的组，而非2岗位
		List<Role> unitRoleList = partnerRoleService.list(partnerRoleCriteria2);
		List<Privilege> partnerPrivilegeList = new ArrayList<Privilege>();


		map.put("partnerRoleCriteria", roleCriteria);
		//map.put("maxRoleId", partnerRoleService.maxId()+1);
		map.put("unitRoleList", unitRoleList);
		map.put("statusCodeList", BasicStatus.values());
		Role partnerRole = null;

		partnerRole = new Role();
		//	int parentRoleId = ServletRequestUtils.getIntParameter(request, "parentRoleId", 0);
		partnerRole.setParentRoleId(parentRoleId);
		logger.info("设置新增组父ID=" + parentRoleId);		
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria();
		privilegeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		partnerPrivilegeList = partnerPrivilegeService.list(privilegeCriteria);

		map.put("partnerPrivilegeList", partnerPrivilegeList);
		map.put("partnerRole", partnerRole);
		return "common/partnerRole/" + "create";
	}



	@RequestMapping(value="/create",method=RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("partnerRole") Role partnerRole) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		partnerRole.setOwnerId(partner.getOwnerId());
		//获取额外的权限关系
		int[] enabledPrivilege = ServletRequestUtils.getIntParameters(request, "partnerPrivilege");
		EisMessage message = null;
		try{
			int rs = partnerRoleService.insert(partnerRole);
			if(rs != 1){
				map.put("message", new EisMessage(EisError.dataError.id,"无法添加"));
				return CommonStandard.partnerMessageView;
			}
			if(partnerRole.getRoleType() == RoleCriteria.ROLE_TYPE_USER){
				for(int i = 0; i < enabledPrivilege.length; i++){
					PrivilegeRoleRelation privilegeRoleRelation = new PrivilegeRoleRelation();
					privilegeRoleRelation.setOwnerId(partner.getOwnerId());
					privilegeRoleRelation.setPrivilegeId(enabledPrivilege[i]);
					privilegeRoleRelation.setRoleId(partnerRole.getRoleId());
					partnerPrivilegeRoleRelationDao.insert(privilegeRoleRelation);
				}
				
			} 
		}catch(Exception e){
			throw new DataWriteErrorException("数据操作失败" + e.getMessage());
		}
		map.put("message",message);
		return CommonStandard.partnerMessageView;

	}

	@RequestMapping(value="/update" + "/{roleId}", method=RequestMethod.GET)	
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("partnerRoleCriteria") RoleCriteria roleCriteria,
			@PathVariable("roleId") Integer roleId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		RoleCriteria partnerRoleCriteria2 = new RoleCriteria();
		partnerRoleCriteria2.setRoleType(RoleCriteria.ROLE_TYPE_DEPARTMENT);
		partnerRoleCriteria2.setOwnerId(partner.getOwnerId());
		partnerRoleCriteria2.setCurrentStatus(BasicStatus.normal.getId());
		//类型为1即为组织单元的组，而非2岗位
		List<Role> unitRoleList = partnerRoleService.list(partnerRoleCriteria2);

		//获取与角色关联的权限
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria(partner.getOwnerId());
		privilegeCriteria.setRoleIds(roleId);
		List<Privilege> partnerPrivilegeList = partnerPrivilegeService.listByRole(privilegeCriteria);


		map.put("partnerRoleCriteria", roleCriteria);
		//map.put("maxRoleId", partnerRoleService.maxId()+1);
		map.put("unitRoleList", unitRoleList);
		map.put("statusCodeList", BasicStatus.values());
		Role partnerRole = null;
		if(roleId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[roleId]");
		}	
		partnerRole = partnerRoleService.select(roleId);
		if(partnerRole == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + roleId + "的partnerRole对象");			
		}	
		if(partnerRole.getOwnerId() != partner.getOwnerId()){
			logger.error("PartnerRole[" + partnerRole.getRoleId() + "]的ownerId[" + partnerRole.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + roleId + "的PartnerRole对象");			
		}


		map.put("partnerPrivilegeList", partnerPrivilegeList);
		map.put("partnerRole", partnerRole);
		return "common/partnerRole/" + "update";
	}



	@RequestMapping(value="/update", method=RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("partnerRole") Role partnerRole) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Role _oldPartnerRole = partnerRoleService.select(partnerRole.getRoleId());
		if(_oldPartnerRole == null){
			logger.error("找不到要修改的PartnerRole，ID=" + partnerRole.getRoleId());
			return CommonStandard.partnerMessageView;		
		}
		if(_oldPartnerRole.getOwnerId() != partner.getOwnerId()){
			logger.warn("要修改的PartnerRole，ownerId[" + _oldPartnerRole.getOwnerId() + "]与系统会话中的ownerId不一致:" + partner.getOwnerId());
			return CommonStandard.partnerMessageView;		
		}
		partnerRole.setOwnerId(_oldPartnerRole.getOwnerId());
		//获取额外的权限关系
		int[] enabledPrivilege = ServletRequestUtils.getIntParameters(request, "partnerPrivilege");
		EisMessage message = null;
		try{
			int rs = partnerRoleService.update(partnerRole);
			if(rs != 1){
				map.put("message", new EisMessage(EisError.dataError.id,"无法更新"));
				return CommonStandard.partnerMessageView;
			}
			if(partnerRole.getRoleType() == RoleCriteria.ROLE_TYPE_USER){
				for(int i = 0; i < enabledPrivilege.length; i++){
					PrivilegeRoleRelation privilegeRoleRelation = new PrivilegeRoleRelation();
					privilegeRoleRelation.setOwnerId(partner.getOwnerId());
					privilegeRoleRelation.setPrivilegeId(enabledPrivilege[i]);
					privilegeRoleRelation.setRoleId(partnerRole.getRoleId());
					partnerPrivilegeRoleRelationDao.insert(privilegeRoleRelation);
				}
				
			} 
		}catch(Exception e){
			throw new DataWriteErrorException("数据操作失败" + e.getMessage());
		}
		map.put("message",message);
		return CommonStandard.partnerMessageView;

	}



}

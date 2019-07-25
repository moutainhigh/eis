package com.maicard.wpt.partner.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.Menu;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerMenuRoleRelationService;
import com.maicard.security.service.PartnerMenuService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.SecurityStandard;

/**
 * 确定一个partner所能访问的菜单
 *
 * @author NetSnake
 * @date 2015年12月12日
 * 
 */
@Controller
@RequestMapping("/partnerMenuRoleRelation")
public class PartnerMenuRoleRelationController extends BaseController  {

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;
	@Resource
	private PartnerMenuService partnerMenuService;
	@Resource
	private PartnerMenuRoleRelationService partnerMenuRoleRelationService;

	//为当前用户生成菜单
	@RequestMapping
	@AllowJsonOutput
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User selfPartner = certifyService.getLoginedUser(request, response, SecurityStandard.UserTypes.partner.getId());
		if(selfPartner == null){
			//无权访问
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		long currentUuid;
		if(selfPartner.getHeadUuid()>0){
			currentUuid=selfPartner.getHeadUuid();
		}else{
			currentUuid=selfPartner.getUuid();
		}
		UserRoleRelationCriteria userRoleRelationCriteria = new UserRoleRelationCriteria();
		userRoleRelationCriteria.setOwnerId(selfPartner.getOwnerId());
		userRoleRelationCriteria.setUuid(currentUuid);
		userRoleRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<UserRoleRelation> partnerRoleRelationList = partnerRoleRelationService.list(userRoleRelationCriteria);

		if(partnerRoleRelationList == null || partnerRoleRelationList.size() < 1){
			logger.warn("用户[" + selfPartner.getUuid() + "]没有任何岗位");
			return CommonStandard.partnerMessageView;
		} 
		logger.info("用户[" + selfPartner.getUuid() + "]关联了" + partnerRoleRelationList.size() + "个岗位");
		int[] roleIds = new int[partnerRoleRelationList.size()];
		for(int i = 0 ; i < partnerRoleRelationList.size(); i++){
			roleIds[i] = partnerRoleRelationList.get(i).getRoleId();
		}
		

		MenuRoleRelationCriteria menuRoleRelationCriteria = new MenuRoleRelationCriteria();
		menuRoleRelationCriteria.setOwnerId(selfPartner.getOwnerId());
		menuRoleRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		menuRoleRelationCriteria.setRoleIds(roleIds);

		List<Menu> menuList = partnerMenuRoleRelationService.listInTree(menuRoleRelationCriteria);
		logger.debug("xxxxxxxxxxxxxxxxxxxxxx"+menuList.size()+"");

		if(menuList != null){
			map.put("partnerMenuList", menuList);
			/*ObjectMapper mapper = new ObjectMapper(); 
			String sysMenuString = mapper.writeValueAsString(menuList);
			sysMenuString = sysMenuString.replaceAll("^\\{\"sysMenu\":", "");
			sysMenuString = sysMenuString.replaceAll("\\}$", "");
			sysMenuString = sysMenuString.replaceAll("menuName", "text");
			sysMenuString = sysMenuString.replaceAll("subMenuList", "children");
			sysMenuString = sysMenuString.replaceAll("\"id\":\\d+,", "");
			sysMenuString = sysMenuString.replaceAll("\"menuUrl\":\"#\"", "\"menuUrl\":\"0\"");
			sysMenuString = sysMenuString.replaceAll("menuUrl", "id");
			logger.debug(sysMenuString);
			map.put("tree", sysMenuString);*/
		}
		return CommonStandard.partnerMessageView;

	}


}

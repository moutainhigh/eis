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
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Menu;
import com.maicard.security.domain.MenuRoleRelation;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerMenuRoleRelationService;
import com.maicard.security.service.PartnerMenuService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 后台系统菜单的管理
 * @author hailong
 *
 */
@Controller
@RequestMapping("/partnerMenu")
public class PartnerMenuController extends BaseController{
	@Resource
	private CertifyService certifyService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PartnerMenuService partnerMenuService;
	@Resource
	private ConfigService configService;
	@Resource
	private PartnerRoleService partnerRoleService;
	@Resource
	private PartnerMenuRoleRelationService partnerMenuRoleRelationService;
	
	
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
			HttpServletResponse response, ModelMap map)
			throws Exception {
		final String view = "common/menu/index";
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
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		int rows = ServletRequestUtils.getIntParameter(request, "rows",
				rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		
		MenuCriteria partnerMenuCriteria = new MenuCriteria();
		partnerMenuCriteria.setOwnerId(ownerId);
		List<Menu> list1 = partnerMenuService.list(partnerMenuCriteria );
		
		int totalRows = 0;
		if (list1 != null && list1.size()>0) {
			totalRows = list1.size();
		}else {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"菜单为空"));
			return CommonStandard.partnerMessageView;
		}
		map.put("total", totalRows);
		Paging paging = new Paging(rows);
		partnerMenuCriteria.setPaging(paging);
		partnerMenuCriteria.getPaging().setCurrentPage(page);
		
		List<Menu> list = partnerMenuService.listOnPage(partnerMenuCriteria);
		if (list != null && list.size()>0) {
			for (Menu menu : list) {
				if (menu.getParentMenuId()==0) {
					menu.setParentMenuName("无");
				}else {
					Menu fatherMenu = partnerMenuService.select(menu.getParentMenuId());
					if (fatherMenu!=null) {
						menu.setParentMenuName(fatherMenu.getMenuName());
					}
				}
			}
		}
		List<Integer> statusList = new ArrayList<>();
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.id);
		}
		
		RoleCriteria partnerRoleCriteria = new RoleCriteria();
		partnerRoleCriteria.setOwnerId(ownerId);
		List<Role> roleList = partnerRoleService.list(partnerRoleCriteria );
		map.put("roleList", roleList);
		map.put("statusList", statusList);
		map.put("menu", list);
		map.put("menuList", list1);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		return view;
	}
	
	@RequestMapping(value="/get" + "/{menuId}")
	public String get(HttpServletRequest request,
			HttpServletResponse response, ModelMap map ,@PathVariable("menuId")int menuId)
			throws Exception {
		final String view = "common/menu/get";
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
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		
		Menu menu = partnerMenuService.select(menuId);
		if (menu == null) {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"未找到Id为："+menuId+"的菜单项"));
			return CommonStandard.partnerMessageView;
		}
		
		if (menu.getParentMenuId()==0) {
			menu.setParentMenuName("无");
		}else {
			Menu fatherMenu = partnerMenuService.select(menu.getParentMenuId());
			if (fatherMenu!=null) {
				menu.setParentMenuName(fatherMenu.getMenuName());
			}
		}
		
		List<Integer> statusList = new ArrayList<>();
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.id);
		}
		
		MenuCriteria partnerMenuCriteria = new MenuCriteria();
		partnerMenuCriteria.setOwnerId(ownerId);
		List<Menu> list = partnerMenuService.list(partnerMenuCriteria );
		
		MenuRoleRelationCriteria partnerMenuRoleRelationCriteria = new MenuRoleRelationCriteria();
		partnerMenuRoleRelationCriteria.setOwnerId(ownerId);
		List<MenuRoleRelation> relations = partnerMenuRoleRelationService.list(partnerMenuRoleRelationCriteria);
		List<Role> list2 = new ArrayList<>();
		if (relations != null && relations.size()>0) {
			for (MenuRoleRelation menuRoleRelation : relations) {
				if (menuRoleRelation.getMenuId()==menuId) {
					Role role = partnerRoleService.select(menuRoleRelation.getRoleId());
					if (role!=null) {
						list2.add(role);
					}
				}
			}
		}
		RoleCriteria partnerRoleCriteria = new RoleCriteria();
		partnerRoleCriteria.setOwnerId(ownerId);
		List<Role> roleList = partnerRoleService.list(partnerRoleCriteria );
		map.put("roleList", roleList);
		map.put("menuRole", list2);
		map.put("menuList", list);
		map.put("statusList", statusList);
		map.put("menu", menu);
		return view;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map ,Menu menu)
			throws Exception {
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
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		Boolean isTop = ServletRequestUtils.getBooleanParameter(request, "isTop");
		if (isTop) {
			menu.setParentMenuId(0);
			menu.setMenuLevel(1);
		}else {
			Menu parentMenu = partnerMenuService.select(menu.getParentMenuId());
			if (parentMenu != null) {
				menu.setMenuLevel(parentMenu.getMenuLevel()+1);
			}
		}
		menu.setOwnerId(ownerId);
		String roleIds = ServletRequestUtils.getStringParameter(request, "roleIds");
		if (partnerMenuService.insert(menu) == 1) {
			if (StringUtils.isNotBlank(roleIds)) {
				String[] split = roleIds.split(",");
				for (String roleId : split) {
					MenuRoleRelation partnerMenuRoleRelation = new MenuRoleRelation();
					partnerMenuRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
					partnerMenuRoleRelation.setOwnerId(ownerId);
					partnerMenuRoleRelation.setMenuId(menu.getMenuId());
					partnerMenuRoleRelation.setRoleId(Integer.parseInt(roleId));
					partnerMenuRoleRelationService.insert(partnerMenuRoleRelation);
				}
			}
			map.put("message", new EisMessage(OperateResult.success.getId(), "操作完成")) ;
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "操作失败"));
		}
		return CommonStandard.partnerMessageView;
	}
	

	@RequestMapping(value="/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request,
			HttpServletResponse response, ModelMap map ,Menu menu)
			throws Exception {
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
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		
		
		Menu oldMenu = partnerMenuService.select(menu.getMenuId());
		if (oldMenu == null) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "找不到 ID为 :"+menu.getMenuId()+"的菜单"));
			return CommonStandard.partnerMessageView;
		}
		
		
		String roleIds = ServletRequestUtils.getStringParameter(request, "roleIds");
		if (StringUtils.isNotBlank(roleIds)) {
			MenuRoleRelationCriteria partnerMenuRoleRelationCriteria = new MenuRoleRelationCriteria();
			partnerMenuRoleRelationCriteria.setOwnerId(ownerId);
			List<MenuRoleRelation> relations = partnerMenuRoleRelationService.list(partnerMenuRoleRelationCriteria);
			if (relations != null && relations.size()>0) {
				for (MenuRoleRelation menuRoleRelation : relations) {
					if (menuRoleRelation.getMenuId()==menu.getMenuId()) {
						partnerMenuRoleRelationService.delete(menuRoleRelation.getMenuRoleRelationId());
					}
				}
			}
			
			String[] split = roleIds.split(",");
			for (String roleId : split) {
				MenuRoleRelation partnerMenuRoleRelation = new MenuRoleRelation();
				partnerMenuRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				partnerMenuRoleRelation.setOwnerId(ownerId);
				partnerMenuRoleRelation.setMenuId(menu.getMenuId());
				partnerMenuRoleRelation.setRoleId(Integer.parseInt(roleId));
				partnerMenuRoleRelationService.insert(partnerMenuRoleRelation);
			}
			
			return CommonStandard.partnerMessageView;
		}
		if (StringUtils.isNotBlank(menu.getMenuName())) {
			oldMenu.setMenuName(menu.getMenuName());
		}
		
		if (menu.getParentMenuId() > 0) {
			oldMenu.setParentMenuId(menu.getParentMenuId());
			Menu parentMenu = partnerMenuService.select(menu.getParentMenuId());
			if (parentMenu != null) {
				oldMenu.setMenuLevel(parentMenu.getMenuLevel()+1);
			}
		}
		
		if (StringUtils.isNotBlank(menu.getResourceId())) {
			oldMenu.setResourceId(menu.getResourceId());
		}
		if (partnerMenuService.update(oldMenu) == 1) {
			map.put("message", new EisMessage(OperateResult.success.getId(), "操作完成")) ;
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "操作失败"));
		}
		return CommonStandard.partnerMessageView;
	}
}

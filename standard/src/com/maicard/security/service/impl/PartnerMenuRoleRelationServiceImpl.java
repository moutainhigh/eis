package com.maicard.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.dao.PartnerMenuRoleRelationDao;
import com.maicard.security.domain.Menu;
import com.maicard.security.domain.MenuRoleRelation;
import com.maicard.security.service.PartnerMenuRoleRelationService;
import com.maicard.security.service.PartnerMenuService;
import com.maicard.standard.BasicStatus;

@Service
public class PartnerMenuRoleRelationServiceImpl extends BaseService implements PartnerMenuRoleRelationService {
	@Resource
	private PartnerMenuRoleRelationDao partnerMenuRoleRelationDao;
	@Resource
	private PartnerMenuService partnerMenuService;



	public int insert(MenuRoleRelation partnerMenuRoleRelation) {
		try{
			return partnerMenuRoleRelationDao.insert(partnerMenuRoleRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(MenuRoleRelation partnerMenuRoleRelation) {
		try{
			return partnerMenuRoleRelationDao.update(partnerMenuRoleRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int partnerMenuRoleRelationId) {
		try{
			return partnerMenuRoleRelationDao.delete(partnerMenuRoleRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	
	}

	public void deleteByGroupId(int groupId){
		partnerMenuRoleRelationDao.deleteByGroupId(groupId);
	}

	public MenuRoleRelation select(int partnerMenuRoleRelationId) {
		return partnerMenuRoleRelationDao.select(partnerMenuRoleRelationId);
	}

	public List<MenuRoleRelation> list(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) {
		return partnerMenuRoleRelationDao.list(partnerMenuRoleRelationCriteria);
	}

	public List<MenuRoleRelation> listOnPage(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) {
		return partnerMenuRoleRelationDao.listOnPage(partnerMenuRoleRelationCriteria);
	}

	/*以树形结构列出菜单
	 * 是否返回所有菜单，取决于PartnerMenuRoleRelationCriteria中是否存在roleId
	 */
	public List<Menu> listInTree(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) {

		MenuCriteria menuCriteria = new MenuCriteria();
		menuCriteria.setCurrentStatus(BasicStatus.normal.getId());
		menuCriteria.setOwnerId(partnerMenuRoleRelationCriteria.getOwnerId());
		List<Menu> plateSysMenuList = partnerMenuService.list(menuCriteria);
		logger.debug("ownerId=" + menuCriteria.getOwnerId() + "的全部菜单是" + (plateSysMenuList == null ? "空" : plateSysMenuList.size()));
		if(plateSysMenuList == null){
			return null;
		}
		ArrayList<Menu> userMenuList = new ArrayList<Menu>();
		if(partnerMenuRoleRelationCriteria.getRoleIds() != null && partnerMenuRoleRelationCriteria.getRoleIds().length > 0){
			List<MenuRoleRelation> sysMenuPositionRelationList = partnerMenuRoleRelationDao.list(partnerMenuRoleRelationCriteria);
			for(int i = 0; i < plateSysMenuList.size(); i++){
				for(int j = 0; j<sysMenuPositionRelationList.size(); j++){
					if(plateSysMenuList.get(i).getMenuId() == sysMenuPositionRelationList.get(j).getMenuId()){
						Menu currentMenu = plateSysMenuList.get(i);
						if(userMenuList.contains(currentMenu)){
							logger.debug("忽略重复菜单:" + currentMenu);
						} else {
							userMenuList.add(currentMenu);
						}
						break;
					}
				}
			}
		} else {
			userMenuList = (ArrayList<Menu>)plateSysMenuList;
		}
		logger.info("过滤后的菜单是:" + (userMenuList == null ? "空" : userMenuList.size()));
		/*
		 * 由于前台菜单空间不能控制部分选择的情况，所以这里要对没有父菜单的菜单，进行修补
		 */
		//ArrayList<Integer> needPathMenuId = new ArrayList<Integer>();
		for(int i = 0; i < userMenuList.size(); i++){
			if(userMenuList.get(i).getParentMenuId() == 0){
				continue;
			}
			boolean orphan = true;
			for(int j = 0; j < userMenuList.size(); j++){				
				if(userMenuList.get(i).getParentMenuId() == userMenuList.get(j).getMenuId()){
					orphan = false;
					break;
				}				
			}
			if(orphan){
				logger.info("菜单[" + userMenuList.get(i).getMenuName() + "/" + userMenuList.get(i).getMenuId() + "]没有父菜单");
				for(int k  = 0; k  < plateSysMenuList.size(); k++){
					if(plateSysMenuList.get(k).getMenuId() == userMenuList.get(i).getParentMenuId()){
						logger.info("把孤儿菜单[" + userMenuList.get(i).getMenuName() + "]的父菜单[" + plateSysMenuList.get(k).getMenuName() + "]自动加入菜单");
						userMenuList.add(plateSysMenuList.get(k));
						break;
					}
				}
			}
		}
		
		
		//子菜单设置
		for(int i = 0; i< userMenuList.size(); i++){		
			for(int j = 0; j< userMenuList.size(); j++){
				if(userMenuList.get(j).getParentMenuId() == userMenuList.get(i).getMenuId()){
					if(userMenuList.get(i).getSubMenuList() == null){
						userMenuList.get(i).setSubMenuList(new ArrayList<Menu>());
					}
					userMenuList.get(i).getSubMenuList().add(userMenuList.get(j));
				}
			}

		}
		//形成树形结构
		ArrayList<Menu> topMenuList = new ArrayList<Menu>();
		for(int i = 0; i< userMenuList.size(); i++){	
			if(userMenuList.get(i).getParentMenuId() == 0){
				topMenuList.add(userMenuList.get(i));
			}
		}
		return topMenuList;
	}


}

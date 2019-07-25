package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.dao.PartnerMenuDao;
import com.maicard.security.domain.Menu;
import com.maicard.security.service.PartnerMenuService;

@Service
public class PartnerMenuServiceImpl extends BaseService implements PartnerMenuService {

	@Resource
	private PartnerMenuDao partnerMenuDao;


	public int insert(Menu partnerMenu) {
		try{
			return partnerMenuDao.insert(partnerMenu);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(Menu partnerMenu) {
		try{
			return  partnerMenuDao.update(partnerMenu);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int menuId) {
		try{
			return   partnerMenuDao.delete(menuId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public Menu select(int menuId) {
		return partnerMenuDao.select(menuId);
	}

	public List<Menu> list(MenuCriteria partnerMenuCriteria) {
		return partnerMenuDao.list(partnerMenuCriteria);
	}
	
	public List<Menu> listOnPage(MenuCriteria partnerMenuCriteria) {
		return partnerMenuDao.listOnPage(partnerMenuCriteria);
	}

}

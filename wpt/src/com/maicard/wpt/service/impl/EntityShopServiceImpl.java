package com.maicard.wpt.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.wpt.criteria.EntityShopCriteria;
import com.maicard.wpt.dao.EntityShopDao;
import com.maicard.wpt.domain.EntityShop;
import com.maicard.wpt.service.EntityShopService;

@Service
public class EntityShopServiceImpl extends BaseService implements EntityShopService {

	@Resource
	private EntityShopDao entityShopDao;

	@Override
	public int insert(EntityShop entityShop) {
		
		try{
			return entityShopDao.insert(entityShop);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public int update(EntityShop entityShop) {
		try{
			return  entityShopDao.update(entityShop);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@Override
	public int delete(long entityShopId) {
		try{
			return  entityShopDao.delete(entityShopId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}

	@Override
	public EntityShop select(long entityShopId) {
		EntityShop entityShop =  entityShopDao.select(entityShopId);
		if(entityShop == null){
			entityShop = new EntityShop();
		}
		return entityShop;
	}




	@Override
	public List<EntityShop> list(EntityShopCriteria entityShopCriteria) {
		return entityShopDao.list(entityShopCriteria);

	}

	@Override
	public List<EntityShop> listOnPage(EntityShopCriteria entityShopCriteria) {
		return entityShopDao.listOnPage(entityShopCriteria);

	}

	@Override
	public int count(EntityShopCriteria entityShopCriteria) {
		return entityShopDao.count(entityShopCriteria);
	}



}

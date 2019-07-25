package com.maicard.wpt.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.criteria.EntityShopCriteria;
import com.maicard.wpt.domain.EntityShop;


public interface EntityShopDao {
	int insert(EntityShop entityShop) throws DataAccessException;

	int update(EntityShop entityShop) throws DataAccessException;

	int delete(long entityShopId) throws DataAccessException;

	EntityShop select(long entityShopId) throws DataAccessException;
	
	List<EntityShop> list(EntityShopCriteria entityShopCriteria) throws DataAccessException;
	
	List<EntityShop> listOnPage(EntityShopCriteria entityShopCriteria) throws DataAccessException;
	
	int count(EntityShopCriteria entityShopCriteria) throws DataAccessException;
}

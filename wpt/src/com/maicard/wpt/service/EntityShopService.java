package com.maicard.wpt.service;

import java.util.List;

import com.maicard.wpt.criteria.EntityShopCriteria;
import com.maicard.wpt.domain.EntityShop;

public interface EntityShopService {
	int insert(EntityShop entityShop);

	int update(EntityShop entityShop);

	int delete(long entityShopId);
	
	EntityShop select(long entityShopId);
	
	List<EntityShop> list(EntityShopCriteria entityShopCriteria);

	List<EntityShop> listOnPage(EntityShopCriteria entityShopCriteria);

	int count(EntityShopCriteria entityShopCriteria);

}

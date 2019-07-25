package com.maicard.product.service;

import com.maicard.common.domain.OpEisObject;
import com.maicard.product.domain.Item;
import com.maicard.security.domain.User;
import com.maicard.site.domain.Document;

public interface StockService {
	int getAvaiableCount(String objectType, long objectId);

	OpEisObject writeItemData(Item item, String objectType, long objectId);


	OpEisObject getTargetObject(String objectType, long objectId);


	int checkPrivilege(Document document, User frontUser);

	/**
	 * 改变一个商品的库存，负数为减少
	 * @return 返回修改后的最新数值
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-19
	 */
	int changeStock(String objectType, long objectId, int offset);

}

package com.maicard.wpt.service.me;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.ec.iface.DeliveryProcessor;
import com.maicard.product.domain.Item;


/**
 * 
 * 购买实体商品后的发货流程
 * 
 * @author NetSnake
 * @date 2018-04-27
 *
 */
public class EntityItemDeliveryProcessor extends BaseService implements DeliveryProcessor{

	@Override
	public EisMessage delivery(Item item) {
		logger.info("进入实体商品发货流程");
		return null;
	}

}

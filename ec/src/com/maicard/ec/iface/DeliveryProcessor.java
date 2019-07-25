package com.maicard.ec.iface;

import com.maicard.common.domain.EisMessage;
import com.maicard.product.domain.Item;

/**
 * 电商系统购物完成后的发货处理接口
 * @author NetSnake
 * @date 2018-04-27
 *
 */
public interface DeliveryProcessor {
	EisMessage delivery(Item item);
}

package com.maicard.ec.iface;

import java.util.HashMap;

import com.maicard.ec.domain.DeliveryOrder;

/**
 * 快递订单的跟踪查询
 *
 *
 * @author NetSnake
 * @date 2016年2月29日
 *
 */
public interface DeliveryTraceProcessor {
	
	public 	 HashMap<String,String>trace(DeliveryOrder deliveryOrder);		


}

package com.maicard.ec.dao;

import java.util.List;

import com.maicard.ec.criteria.DeliveryOrderCriteria;
import com.maicard.ec.domain.DeliveryOrder;


public interface DeliveryOrderDao {
	public int insert(DeliveryOrder deliveryOrder);

	public int update(DeliveryOrder deliveryOrder);

	public List<DeliveryOrder> list(DeliveryOrderCriteria deliveryOrderCriteria);

	public List<DeliveryOrder> listOnPage(DeliveryOrderCriteria deliveryOrderCriteria);

	int delete(long deliveryOrderId);
	
	int count(DeliveryOrderCriteria deliveryOrderCriteria);

	public DeliveryOrder select(long  deliveryOrderId);

}

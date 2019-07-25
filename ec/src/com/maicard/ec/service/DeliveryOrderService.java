package com.maicard.ec.service;

import java.util.List;

import com.maicard.common.domain.EOEisObject;
import com.maicard.ec.criteria.DeliveryOrderCriteria;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.product.domain.Item;



public interface DeliveryOrderService {
	
	public DeliveryOrder select(long deliveryOrderId);
	
	public int insert(DeliveryOrder deliveryOrder);
	
	public int update(DeliveryOrder deliveryOrder);
	
	public int delete(long deliveryOrderId);
	
	public List<DeliveryOrder> list(DeliveryOrderCriteria deliveryOrderCriteria);

	int count(DeliveryOrderCriteria productCriteria);

	public List<DeliveryOrder> listOnPage(DeliveryOrderCriteria deliveryOrderCriteria);
	
	/**
	 * 根据一组订单和快递地址计算费用
	 * 快递费用和快递减免信息放入feeMap中
	 * 并返回EisError或OperateResult

	 */
	DeliveryOrder generateDeliveryOrder(long addressBookId, Item[]items, String refOrderId, String transactionType,	String identify) throws Exception;



	String getFromArea(Item item);

	String getFromArea(EOEisObject targetObject);

	DeliveryOrder generateDeliveryOrder(Item item, AddressBook addressBook);





}

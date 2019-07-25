package com.maicard.ec.dao;

import java.util.List;

import com.maicard.ec.criteria.DeliveryPriceCriteria;
import com.maicard.ec.domain.DeliveryPrice;


public interface DeliveryPriceDao {
	public int insert(DeliveryPrice deliveryPrice);

	public int update(DeliveryPrice deliveryPrice);

	public List<DeliveryPrice> list(DeliveryPriceCriteria deliveryPriceCriteria);

	public List<DeliveryPrice> listOnPage(DeliveryPriceCriteria deliveryPriceCriteria);

	int delete(long deliveryPriceId);
	
	int count(DeliveryPriceCriteria deliveryPriceCriteria);

	public DeliveryPrice select(long  deliveryPriceId);

}

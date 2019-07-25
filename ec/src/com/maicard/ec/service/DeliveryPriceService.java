package com.maicard.ec.service;

import java.util.List;

import com.maicard.ec.criteria.DeliveryPriceCriteria;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.domain.DeliveryPrice;
import com.maicard.money.domain.Price;



public interface DeliveryPriceService {
	
	public DeliveryPrice select(long deliveryPriceId);
	
	public int insert(DeliveryPrice deliveryPrice);
	
	public int update(DeliveryPrice deliveryPrice);
	
	public int delete(long deliveryPriceId);
	
	public List<DeliveryPrice> list(DeliveryPriceCriteria deliveryPriceCriteria);

	int count(DeliveryPriceCriteria productCriteria);

	public List<DeliveryPrice> listOnPage(DeliveryPriceCriteria deliveryPriceCriteria);

	public Price calculatePrice(DeliveryOrder deliveryOrder);

	public int loadBatch(List<String> lines, long deliveryPartnerId, String identify, long ownerId);





}

package com.maicard.ec.service;

import java.util.List;

import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.security.domain.User;

public interface DeliveryCompanyService {
	
	public List<User> list();

	public void checkInfo(DeliveryOrder deliveryOrder);

	long getBestDeliveryCompanyId(DeliveryOrder deliveryOrder);

}

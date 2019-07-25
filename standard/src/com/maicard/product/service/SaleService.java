package com.maicard.product.service;

import com.maicard.common.domain.EisMessage;
import com.maicard.product.domain.Item;

public interface SaleService {
	
	EisMessage sale(Item item) throws Exception;
	EisMessage end(long uuid, String resultString);
		

}

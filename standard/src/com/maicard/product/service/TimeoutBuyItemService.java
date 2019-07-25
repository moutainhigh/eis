package com.maicard.product.service;



import com.maicard.product.domain.Item;

public interface TimeoutBuyItemService {

	public void run();

	int accountCharge(Item item);


}

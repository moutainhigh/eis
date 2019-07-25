package com.maicard.product.service;

import java.util.List;

import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;

public interface ItemLogService {

	//@Async
	void insert(Item item);
		
	List<Item> list(ItemCriteria itemCriteria);

	List<Item> listOnPage(ItemCriteria itemCriteria);

	Item select(String transactionId);

	int count(ItemCriteria itemCriteria);

	int delete(ItemCriteria itemCriteria);

	int getChannelSuccessMoney();

	List<Item> listBadVps();

}

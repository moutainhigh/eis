package com.maicard.product.service;

import java.util.List;

import com.maicard.common.domain.EisMessage;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;

public interface ItemService {

	EisMessage insert(Item item);

	int update(Item item);

	//int delete(int itemId);
	
	//Item select(int itemId);

	List<Item> list(ItemCriteria itemCriteria);
	List<Item> listOnPage(ItemCriteria itemCriteria);
	
	
	Item select(String transactionId);

	Item selectSimple(String transactionId);

	Item fetchWithLock(ItemCriteria itemCriteria);
		
	int count(ItemCriteria itemCriteria);

	int releaseItemWithFrozenMoney(Item item);
	
	int releaseItemWithFrozenMoney2(Item cardItem);

	int plusItemMoneyWithFrozenMoney(Item item);

	int insertLocal(Item item) throws Exception;

	int plusItemMoneyWithoutFrozenMoney(Item item);


	String generateCsv(List<Item> itemList);

	List<Item> listProcessTimeout(ItemCriteria itemCriteria);
	
	String downloadCsv(ItemCriteria itemCriteria);

	int changeStatus(Item item);


	Item selectOneForMatch(ItemCriteria itemCriteria);

	int lockUpdateAndRelaseAdditinalFrozenMoney(Item cardItem);

	List<Item> listForMatch(ItemCriteria itemCriteria);

	void fixOutStatus(Item item);

	void applyTtl(Item item, Product product);

	int updateNoNull(Item item);

	int delete(String transactionId) throws Exception;

	boolean recycle(Item item);

	int lock(Item item);

	List<Item> lock(ItemCriteria itemCriteria);

}

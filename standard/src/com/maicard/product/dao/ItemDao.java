package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;

public interface ItemDao {

	int insert(Item item) throws DataAccessException;

	int update(Item item) throws DataAccessException;

	//Item select(int itemId) throws DataAccessException;

	List<Item> list(ItemCriteria itemCriteria) throws DataAccessException;
	
	List<Item> listOnPage(ItemCriteria itemCriteria) throws DataAccessException;
	
	List<Item> listAll(ItemCriteria itemCriteria) throws DataAccessException;
	
	int count(ItemCriteria itemCriteria) throws DataAccessException;

	Item fetchWithLock(ItemCriteria itemCriteria);


	int delete(ItemCriteria itemCriteria) throws DataAccessException;
	/*
	Item fetchWithPartMoneyLock1(ItemCriteria itemCriteria);
	Item fetchWithPartMoneyLock2(ItemCriteria itemCriteria);
	Item fetchWithPartMoneyLock3(ItemCriteria itemCriteria);
	Item fetchWithPartMoneyLock4(ItemCriteria itemCriteria);*/

	int lockUpdateAndRelaseAdditinalFrozenMoney(Item item);

	int releaseItemWithFrozenMoney(Item item);

	int releaseItemWithFrozenMoney2(Item item);

	int plusItemMoneyWithFrozenMoney(Item item);

	int plusItemMoneyWithoutFrozenMoney(Item item);

	List<Item> listProcessTimeout(ItemCriteria itemCriteria);

	String downloadCsv(ItemCriteria itemCriteria) throws Exception;

	int changeStatus(Item item);

	List<Item> listForMatch(ItemCriteria itemCriteria);

	List<Item> listFrozenDeadAccount(ItemCriteria itemCriteria);

	int totalFailItem(int productID);

	int updateNoNull(Item item);

	int delete(String transactionId);

	/**
	 * 将一个Item的currentStatus设置为afterLockStatus，保证在设置前它的currentStatus必须是beforeLockStatus
	 * 
	 * @param item
	 * @return
	 */
	int lock(Item item);

	boolean exist(String transactionId);



}

package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;

public interface ItemLogDao {

	int insert(Item item) throws DataAccessException;
	
	Item select(String transactionId);

	List<Item> list(ItemCriteria itemCriteria) throws DataAccessException;
	
	List<Item> listOnPage(ItemCriteria itemCriteria) throws DataAccessException;
	
	int count(ItemCriteria itemCriteria) throws DataAccessException;

	int delete(ItemCriteria itemCriteria);

	List<Item> listBadVps() throws DataAccessException;

}

package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;

public interface PriceDao {

	int insert(Price price) throws DataAccessException;

	int update(Price price) throws DataAccessException;

	int delete(long priceId) throws DataAccessException;

	Price select(long priceId) throws DataAccessException;

	List<Price> list(PriceCriteria priceCriteria) throws DataAccessException;
	
	List<Price> listOnPage(PriceCriteria priceCriteria) throws DataAccessException;
	
	int count(PriceCriteria priceCriteria) throws DataAccessException;

}

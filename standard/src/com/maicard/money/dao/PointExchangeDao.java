package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.PointExchangeCriteria;
import com.maicard.money.domain.PointExchange;

public interface PointExchangeDao {

	int insert(PointExchange pointExchange) throws DataAccessException;

	int update(PointExchange pointExchange) throws DataAccessException;

	int delete(int pointExchangeId) throws DataAccessException;

	PointExchange select(int pointExchangeId) throws DataAccessException;

	List<PointExchange> list(PointExchangeCriteria pointExchangeCriteria) throws DataAccessException;
	
	List<PointExchange> listOnPage(PointExchangeCriteria pointExchangeCriteria) throws DataAccessException;
	
	int count(PointExchangeCriteria pointExchangeCriteria) throws DataAccessException;

}

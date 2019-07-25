package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.PointExchangeLogCriteria;
import com.maicard.money.domain.PointExchange;

public interface PointExchangeLogDao {

	int insert(PointExchange pointExchange) throws DataAccessException;

	int update(PointExchange pointExchange) throws DataAccessException;

	int delete(int pointExchangeId) throws DataAccessException;

	PointExchange select(int pointExchangeId) throws DataAccessException;

	List<PointExchange> list(PointExchangeLogCriteria pointExchangeLogCriteria) throws DataAccessException;
	
	List<PointExchange> listOnPage(PointExchangeLogCriteria pointExchangeLogCriteria) throws DataAccessException;
	
	int count(PointExchangeLogCriteria pointExchangeLogCriteria) throws DataAccessException;

}

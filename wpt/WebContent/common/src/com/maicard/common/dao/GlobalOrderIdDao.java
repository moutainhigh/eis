package com.maicard.common.dao;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.GlobalOrderIdCriteria;

public interface GlobalOrderIdDao {

	void insert(String globalOrderId) throws DataAccessException;
	
	boolean exist(GlobalOrderIdCriteria globalOrderIdCriteria) throws DataAccessException;


}

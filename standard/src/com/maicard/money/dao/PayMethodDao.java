package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.domain.PayMethod;

public interface PayMethodDao {

	int insert(PayMethod payMethod) throws DataAccessException;

	int update(PayMethod payMethod) throws DataAccessException;

	int delete(int payMethodId) throws DataAccessException;

	PayMethod select(int payMethodId) throws DataAccessException;
	
	List<Integer> listPkOnPage(PayMethodCriteria payMethodCriteria);
	
	int count(PayMethodCriteria payMethodCriteria) throws DataAccessException;



}

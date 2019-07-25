package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.WithdrawMethodCriteria;
import com.maicard.money.domain.WithdrawMethod;

public interface WithdrawMethodDao {

	int insert(WithdrawMethod withdrawMethod) throws DataAccessException;

	int update(WithdrawMethod withdrawMethod) throws DataAccessException;

	int delete(int withdrawMethodId) throws DataAccessException;

	WithdrawMethod select(int withdrawMethodId) throws DataAccessException;

	List<WithdrawMethod> list(WithdrawMethodCriteria withdrawMethodCriteria) throws DataAccessException;
	
	List<WithdrawMethod> listOnPage(WithdrawMethodCriteria withdrawMethodCriteria) throws DataAccessException;
	
	int count(WithdrawMethodCriteria withdrawMethodCriteria) throws DataAccessException;

}

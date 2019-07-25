package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.WithdrawTypeCriteria;
import com.maicard.money.domain.WithdrawType;

public interface WithdrawTypeDao {

	int insert(WithdrawType withdrawType) throws DataAccessException;

	int update(WithdrawType withdrawType) throws DataAccessException;

	int delete(int withdrawTypeId) throws DataAccessException;

	WithdrawType select(int withdrawTypeId) throws DataAccessException;

	List<WithdrawType> list(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException;
	
	List<WithdrawType> listOnPage(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException;
	
	int count(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException;

	WithdrawType selectByColumn(WithdrawTypeCriteria withdrawTypeCriteria);

}

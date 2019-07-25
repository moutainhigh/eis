package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.domain.Pay;

public interface PayDao {

	int insert(Pay pay) throws DataAccessException;

	int update(Pay pay) throws Exception;

	int delete(String transactionId) throws DataAccessException;

	Pay select(String transactionId) throws DataAccessException;

	List<Pay> list(PayCriteria payCriteria) throws DataAccessException;
	
	List<Pay> listOnPage(PayCriteria payCriteria) throws DataAccessException;
	
	List<Pay> listOnPageByPartner(PayCriteria payCriteria) throws DataAccessException;

	int countByPartner(PayCriteria payCriteria) throws DataAccessException;
	
	int count(PayCriteria payCriteria) throws DataAccessException;

	List<Pay> listOnPageByday(PayCriteria payCriteria)
			throws DataAccessException;


}

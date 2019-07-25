package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.RefundCriteria;
import com.maicard.money.domain.Refund;

public interface RefundDao {

	int insert(Refund refund) throws DataAccessException;

	int update(Refund refund) throws Exception;

	int delete(String transactionId) throws DataAccessException;

	Refund select(String transactionId) throws DataAccessException;

	List<Refund> list(RefundCriteria refundCriteria) throws DataAccessException;
	
	List<Refund> listOnPage(RefundCriteria refundCriteria) throws DataAccessException;
	
	
	int count(RefundCriteria refundCriteria) throws DataAccessException;



}

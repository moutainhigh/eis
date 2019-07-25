package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayType;

public interface PayTypeDao {

	int insert(PayType payType) throws DataAccessException;

	int update(PayType payType) throws DataAccessException;

	int delete(int payTypeId) throws DataAccessException;

	PayType select(int payTypeId) throws DataAccessException;

	List<PayType> list(PayTypeCriteria payTypeCriteria) throws DataAccessException;
	
	List<PayType> listOnPage(PayTypeCriteria payTypeCriteria) throws DataAccessException;
	
	int count(PayTypeCriteria payTypeCriteria) throws DataAccessException;

}

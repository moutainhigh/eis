package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.MoneyCriteria;
import com.maicard.money.domain.Money;
public interface MoneyDao {

	void insert(Money money) throws DataAccessException;

	int update(Money money) throws DataAccessException;

	int delete(long uuid) throws DataAccessException;

	Money select(long uuid) throws DataAccessException;

	List<Money> list(MoneyCriteria moneyCriteria) throws DataAccessException;
	
	List<Money> listOnPage(MoneyCriteria moneyCriteria) throws DataAccessException;
	List<Money> listByPartner() throws DataAccessException;
	int count(MoneyCriteria moneyCriteria) throws DataAccessException;	
	
	/*int plus(Money money) throws DataAccessException;
	int minus(Money money) throws DataAccessException;
	int lock(Money money) throws DataAccessException;
	int unLock(Money money) throws DataAccessException;*/
	void charge(Money money) throws DataAccessException;

	List<Long> listPk(MoneyCriteria moneyCriteria);
	
	List<Long> listPkOnPage(MoneyCriteria moneyCriteria);


}

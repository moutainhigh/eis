package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.MoneyBalanceCriteria;
import com.maicard.money.domain.MoneyBalance;


public interface MoneyBalanceDao {

	int insert(MoneyBalance moneyBalance) throws DataAccessException;

	int update(MoneyBalance moneyBalance) throws DataAccessException;

	MoneyBalance select(int moneyBalanceId) throws DataAccessException;

	List<MoneyBalance> list(MoneyBalanceCriteria moneyBalanceCriteria) throws DataAccessException;
	
	List<MoneyBalance> listOnPage(MoneyBalanceCriteria moneyBalanceCriteria) throws DataAccessException;
	
	int count(MoneyBalanceCriteria moneyBalanceCriteria) throws DataAccessException;
	
	int delete(int id);

	void deleteByCriteria(MoneyBalanceCriteria moneyBalanceCriteria);

}

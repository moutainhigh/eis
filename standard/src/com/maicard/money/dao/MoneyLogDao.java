package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.MoneyLogCriteria;
import com.maicard.money.domain.MoneyLog;

public interface MoneyLogDao {

	int insert(MoneyLog moneyChangeLog) throws DataAccessException;

	int update(MoneyLog moneyChangeLog) throws DataAccessException;

	int delete(int moneyCharngeLogId) throws DataAccessException;

	MoneyLog select(int moneyCharngeLogId) throws DataAccessException;

	List<MoneyLog> list(MoneyLogCriteria moneyChangeLogCriteria) throws DataAccessException;
	
	List<MoneyLog> listOnPage(MoneyLogCriteria moneyChangeLogCriteria) throws DataAccessException;
	
	int count(MoneyLogCriteria moneyChangeLogCriteria) throws DataAccessException;

}

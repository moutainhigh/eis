package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.MoneyExchangeRuleCriteria;
import com.maicard.money.domain.MoneyExchangeRule;

public interface MoneyExchangeRuleDao {

	int insert(MoneyExchangeRule moneyExchangeRule) throws DataAccessException;

	int update(MoneyExchangeRule moneyExchangeRule) throws DataAccessException;

	int delete(int moneyExchangeRuleId) throws DataAccessException;

	MoneyExchangeRule select(int moneyExchangeRuleId) throws DataAccessException;

	List<MoneyExchangeRule> list(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) throws DataAccessException;
	
	List<MoneyExchangeRule> listOnPage(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) throws DataAccessException;
	
	int count(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) throws DataAccessException;

}

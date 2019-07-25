package com.maicard.money.service;

import java.util.List;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.criteria.MoneyExchangeRuleCriteria;
import com.maicard.money.domain.MoneyExchangeRule;

public interface MoneyExchangeRuleService {

	int insert(MoneyExchangeRule moneyExchangeRule);

	int update(MoneyExchangeRule moneyExchangeRule);

	int delete(int moneyExchangeRuleId);
	
	MoneyExchangeRule select(int moneyExchangeRuleId);

	List<MoneyExchangeRule> list(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria);

	List<MoneyExchangeRule> listOnPage(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria);

	int count(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria);

	EisMessage exchange(MoneyExchangeRule moneyExchangeRule);

	int preCheck(MoneyExchangeRule moneyExchangeRule);

}

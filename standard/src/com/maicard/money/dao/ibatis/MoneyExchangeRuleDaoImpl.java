package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.MoneyExchangeRuleCriteria;
import com.maicard.money.dao.MoneyExchangeRuleDao;
import com.maicard.money.domain.MoneyExchangeRule;

@Repository
public class MoneyExchangeRuleDaoImpl extends BaseDao implements MoneyExchangeRuleDao {

	public int insert(MoneyExchangeRule moneyExchangeRule) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("MoneyExchangeRule.insert", moneyExchangeRule);
	}

	public int update(MoneyExchangeRule moneyExchangeRule) throws DataAccessException {
		return getSqlSessionTemplate().update("MoneyExchangeRule.update", moneyExchangeRule);
	}

	public int delete(int moneyExchangeRuleId) throws DataAccessException {
		return getSqlSessionTemplate().delete("MoneyExchangeRule.delete", new Integer(moneyExchangeRuleId));
	}

	public MoneyExchangeRule select(int moneyExchangeRuleId) throws DataAccessException {
		return (MoneyExchangeRule) getSqlSessionTemplate().selectOne("MoneyExchangeRule.select", new Integer(moneyExchangeRuleId));
	}

	public List<MoneyExchangeRule> list(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) throws DataAccessException {
		Assert.notNull(moneyExchangeRuleCriteria, "moneyExchangeRuleCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("MoneyExchangeRule.list", moneyExchangeRuleCriteria);
	}

	public List<MoneyExchangeRule> listOnPage(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) throws DataAccessException {
		Assert.notNull(moneyExchangeRuleCriteria, "moneyExchangeRuleCriteria must not be null");
		Assert.notNull(moneyExchangeRuleCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(moneyExchangeRuleCriteria);
		Paging paging = moneyExchangeRuleCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("MoneyExchangeRule.list", moneyExchangeRuleCriteria, rowBounds);
	}

	public int count(MoneyExchangeRuleCriteria moneyExchangeRuleCriteria) throws DataAccessException {
		Assert.notNull(moneyExchangeRuleCriteria, "moneyExchangeRuleCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("MoneyExchangeRule.count", moneyExchangeRuleCriteria);
	}

}

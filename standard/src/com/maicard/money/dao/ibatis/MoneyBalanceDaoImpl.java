package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.MoneyBalanceCriteria;
import com.maicard.money.dao.MoneyBalanceDao;
import com.maicard.money.domain.MoneyBalance;

@Repository
public class MoneyBalanceDaoImpl extends BaseDao implements MoneyBalanceDao {

	public int insert(MoneyBalance moneyBalance) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.MoneyBalance.insert", moneyBalance);
	}

	public int update(MoneyBalance moneyBalance) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.money.sql.MoneyBalance.update", moneyBalance);


	}

	public int delete(int id) throws DataAccessException {


		return getSqlSessionTemplate().delete("com.maicard.money.sql.MoneyBalance.delete", new Integer(id));


	}

	public MoneyBalance select(int id) throws DataAccessException {
		return (MoneyBalance) getSqlSessionTemplate().selectOne("com.maicard.money.sql.MoneyBalance.select", new Integer(id));
	}


	public List<MoneyBalance> list(MoneyBalanceCriteria moneyBalanceCriteria) throws DataAccessException {
		Assert.notNull(moneyBalanceCriteria, "moneyBalanceCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.money.sql.MoneyBalance.list", moneyBalanceCriteria);
	}


	public List<MoneyBalance> listOnPage(MoneyBalanceCriteria moneyBalanceCriteria) throws DataAccessException {
		Assert.notNull(moneyBalanceCriteria, "moneyBalanceCriteria must not be null");
		Assert.notNull(moneyBalanceCriteria.getPaging(), "paging must not be null");

		int totalResults = count(moneyBalanceCriteria);
		Paging paging = moneyBalanceCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.money.sql.MoneyBalance.list", moneyBalanceCriteria, rowBounds);
	}

	public int count(MoneyBalanceCriteria moneyBalanceCriteria) throws DataAccessException {
		Assert.notNull(moneyBalanceCriteria, "moneyBalanceCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.MoneyBalance.count", moneyBalanceCriteria)).intValue();
	}

	@Override
	public void deleteByCriteria(MoneyBalanceCriteria moneyBalanceCriteria) {
		// TODO Auto-generated method stub

	}
}

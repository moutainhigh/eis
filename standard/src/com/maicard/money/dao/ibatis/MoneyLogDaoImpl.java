package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.MoneyLogCriteria;
import com.maicard.money.dao.MoneyLogDao;
import com.maicard.money.domain.MoneyLog;

@Repository
public class MoneyLogDaoImpl extends BaseDao implements MoneyLogDao {

	public int insert(MoneyLog moneyChangeLog) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.MoneyLog.insert", moneyChangeLog);
	}

	public int update(MoneyLog moneyChangeLog) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.MoneyLog.update", moneyChangeLog);
	}

	public int delete(int moneyCharngeLogId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.MoneyLog.delete", new Integer(moneyCharngeLogId));
	}

	public MoneyLog select(int moneyCharngeLogId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.MoneyLog.select", new Integer(moneyCharngeLogId));
	}

	public List<MoneyLog> list(MoneyLogCriteria moneyChangeLogCriteria) throws DataAccessException {
		Assert.notNull(moneyChangeLogCriteria, "moneyChangeLogCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.MoneyLog.list", moneyChangeLogCriteria);
	}

	public List<MoneyLog> listOnPage(MoneyLogCriteria moneyChangeLogCriteria) throws DataAccessException {
		Assert.notNull(moneyChangeLogCriteria, "moneyChangeLogCriteria must not be null");
		Assert.notNull(moneyChangeLogCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(moneyChangeLogCriteria);
		Paging paging = moneyChangeLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.MoneyLog.list", moneyChangeLogCriteria, rowBounds);
	}

	public int count(MoneyLogCriteria moneyChangeLogCriteria) throws DataAccessException {
		Assert.notNull(moneyChangeLogCriteria, "moneyChangeLogCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.MoneyLog.count", moneyChangeLogCriteria)).intValue();
	}

}

package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PasswordLogCriteria;
import com.maicard.security.dao.PasswordLogDao;
import com.maicard.security.domain.PasswordLog;

@Repository
public class PasswordLogDaoImpl extends BaseDao implements PasswordLogDao {

	@Override
	public int insert(PasswordLog passwordLog) throws DataAccessException {
		return getSqlSessionTemplate().insert("PasswordLog.insert", passwordLog);
	}

	@Override
	public PasswordLog select(int passwordLogId) throws DataAccessException {
		return (PasswordLog) getSqlSessionTemplate().selectOne("PasswordLog.select", new Integer(passwordLogId));
	}

	@Override
	public List<PasswordLog> list(PasswordLogCriteria passwordLogCriteria) throws DataAccessException {
		Assert.notNull(passwordLogCriteria, "passwordLogCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PasswordLog.list", passwordLogCriteria);
	}

	@Override
	public List<PasswordLog> listOnPage(PasswordLogCriteria passwordLogCriteria) throws DataAccessException {
		Assert.notNull(passwordLogCriteria, "PasswordLogCriteria must not be null");
		Assert.notNull(passwordLogCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(passwordLogCriteria);
		Paging paging = passwordLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PasswordLog.list", passwordLogCriteria, rowBounds);
	}

	@Override
	public int count(PasswordLogCriteria passwordLogCriteria) throws DataAccessException {
		Assert.notNull(passwordLogCriteria, "PasswordLogCriteria must not be null");
		
		return  getSqlSessionTemplate().selectOne("PasswordLog.count", passwordLogCriteria);
	}
}

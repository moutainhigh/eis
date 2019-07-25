package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.dao.PayTypeDao;
import com.maicard.money.domain.PayType;

@Repository
public class PayTypeDaoImpl extends BaseDao implements PayTypeDao {

	public int insert(PayType payType) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.money.sql.PayType.insert", payType);
	}

	public int update(PayType payType) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.PayType.update", payType);
	}

	public int delete(int payTypeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.PayType.delete", new Integer(payTypeId));
	}

	public PayType select(int payTypeId) throws DataAccessException {
		return (PayType) getSqlSessionTemplate().selectOne("com.maicard.money.sql.PayType.select", new Integer(payTypeId));
	}

	public List<PayType> list(PayTypeCriteria payTypeCriteria) throws DataAccessException {
		Assert.notNull(payTypeCriteria, "payTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.PayType.list", payTypeCriteria);
	}

	public List<PayType> listOnPage(PayTypeCriteria payTypeCriteria) throws DataAccessException {
		Assert.notNull(payTypeCriteria, "payTypeCriteria must not be null");
		Assert.notNull(payTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(payTypeCriteria);
		Paging paging = payTypeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.PayType.list", payTypeCriteria, rowBounds);
	}

	public int count(PayTypeCriteria payTypeCriteria) throws DataAccessException {
		Assert.notNull(payTypeCriteria, "payTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.PayType.count", payTypeCriteria);
	}

}

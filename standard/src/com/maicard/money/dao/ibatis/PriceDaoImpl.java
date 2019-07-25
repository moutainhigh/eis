package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.dao.PriceDao;
import com.maicard.money.domain.Price;

@Repository
public class PriceDaoImpl extends BaseDao implements PriceDao {

	public int insert(Price price) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.money.sql.Price.insert", price);
	}

	public int update(Price price) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.Price.update", price);
	}

	public int delete(long priceId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.Price.delete", priceId);
	}

	public Price select(long priceId) throws DataAccessException {
		return (Price) getSqlSessionTemplate().selectOne("com.maicard.money.sql.Price.select", priceId);
	}

	public List<Price> list(PriceCriteria priceCriteria) throws DataAccessException {
		Assert.notNull(priceCriteria, "priceCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Price.list", priceCriteria);
	}

	public List<Price> listOnPage(PriceCriteria priceCriteria) throws DataAccessException {
		Assert.notNull(priceCriteria, "priceCriteria must not be null");
		Assert.notNull(priceCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(priceCriteria);
		Paging paging = priceCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Price.list", priceCriteria, rowBounds);
	}

	public int count(PriceCriteria priceCriteria) throws DataAccessException {
		Assert.notNull(priceCriteria, "priceCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.Price.count", priceCriteria);
	}

}

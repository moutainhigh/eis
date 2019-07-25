package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.CardMatchRegionPolicyCriteria;
import com.maicard.product.dao.CardMatchRegionPolicyDao;
import com.maicard.product.domain.CardMatchRegionPolicy;

@Repository
public class CardMatchRegionPolicyDaoImpl extends BaseDao implements CardMatchRegionPolicyDao {

	public void insert(CardMatchRegionPolicy itemMatch) throws DataAccessException {
		getSqlSessionTemplate().insert("CardMatchRegionPolicy.insert", itemMatch);
	}

	public int update(CardMatchRegionPolicy itemMatch) throws DataAccessException {
		return getSqlSessionTemplate().update("CardMatchRegionPolicy.update", itemMatch);

	}

	public int delete(int matchId) throws DataAccessException {
		return getSqlSessionTemplate().delete("CardMatchRegionPolicy.delete", matchId);
	}

	public CardMatchRegionPolicy select(int matchId) throws DataAccessException {
		return (CardMatchRegionPolicy) getSqlSessionTemplate().selectOne("CardMatchRegionPolicy.select", matchId);
	}

	public List<CardMatchRegionPolicy> list(CardMatchRegionPolicyCriteria itemMatchCriteria) throws DataAccessException {
		Assert.notNull(itemMatchCriteria, "itemMatchCriteria must not be null");		
		return getSqlSessionTemplate().selectList("CardMatchRegionPolicy.list", itemMatchCriteria);
	}

	public List<CardMatchRegionPolicy> listOnPage(CardMatchRegionPolicyCriteria itemMatchCriteria) throws DataAccessException {
		Assert.notNull(itemMatchCriteria, "itemMatchCriteria must not be null");
		Assert.notNull(itemMatchCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(itemMatchCriteria);
		Paging paging = itemMatchCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("CardMatchRegionPolicy.list", itemMatchCriteria,rowBounds);
	}

	public int count(CardMatchRegionPolicyCriteria itemMatchCriteria) throws DataAccessException {
		Assert.notNull(itemMatchCriteria, "itemMatchCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("CardMatchRegionPolicy.count", itemMatchCriteria);
	}

	
}

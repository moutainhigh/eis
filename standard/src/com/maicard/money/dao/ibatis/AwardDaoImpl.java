package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.AwardCriteria;
import com.maicard.money.dao.AwardDao;
import com.maicard.money.domain.Award;

public class AwardDaoImpl extends BaseDao implements AwardDao {

	@Override
	public int insert(Award award) throws DataAccessException {
		return getSqlSessionTemplate().insert("Award.insert", award);
	}

	public int update(Award award) throws DataAccessException {
		return getSqlSessionTemplate().update("Award.update", award);
	}


	public int delete(long awardId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Award.delete", awardId);
	}


	public List<Award> list(AwardCriteria awardCriteria) throws DataAccessException {
		Assert.notNull(awardCriteria, "awardCriteria must not be null");

		return getSqlSessionTemplate().selectList("Award.list", awardCriteria);
	}

	public List<Award> listOnPage(AwardCriteria awardCriteria) throws DataAccessException {
		Assert.notNull(awardCriteria, "awardCriteria must not be null");
		Assert.notNull(awardCriteria.getPaging(), "paging must not be null");

		int totalResults = count(awardCriteria);
		Paging paging = awardCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("Award.list", awardCriteria, rowBounds);
	}

	public int count(AwardCriteria awardCriteria) throws DataAccessException {
		Assert.notNull(awardCriteria, "awardCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("Award.count", awardCriteria)).intValue();
	}



	@Override
	public Award select(long awardId) {
		return  getSqlSessionTemplate().selectOne("Award.select", awardId);
	}

}

package com.maicard.stat.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.stat.criteria.WithdrawStatCriteria;
import com.maicard.stat.dao.WithdrawStatDao;
import com.maicard.stat.domain.WithdrawStat;

@Repository
public class WithdrawStatDaoImpl extends BaseDao implements WithdrawStatDao {

	@Override
	public List<WithdrawStat> list(WithdrawStatCriteria withdrawStatCriteria) {
		Assert.notNull(withdrawStatCriteria, "withdrawStatCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.WithdrawStat.list", withdrawStatCriteria);
	}
	
	@Override
	public List<WithdrawStat> listOnPage(WithdrawStatCriteria withdrawStatCriteria) throws DataAccessException{
		Assert.notNull(withdrawStatCriteria, "withdrawStatCriteria must not be null");
		Assert.notNull(withdrawStatCriteria.getPaging(), "paging must not be null");

		int totalResults = count(withdrawStatCriteria);
		Paging paging = withdrawStatCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.WithdrawStat.list", withdrawStatCriteria, rowBounds);
	}

	@Override
	public int count(WithdrawStatCriteria withdrawStatCriteria)  {
		
		return getSqlSessionTemplate().selectOne("com.maicard.stat.sql.WithdrawStat.count", withdrawStatCriteria);
/*		if(withdrawStatCriteria.getStatTimeMode() == null)
			return getSqlSessionTemplate().selectOne("WithdrawStat.bydaycount", withdrawStatCriteria);
		else{
			if (withdrawStatCriteria.getStatTimeMode().equals("day"))
				return getSqlSessionTemplate().selectOne("WithdrawStat.bydaycount", withdrawStatCriteria);			
			else
				return getSqlSessionTemplate().selectOne("WithdrawStat.byhourcount", withdrawStatCriteria);
			}*/
	}

	@Override
	public void calculateProfit() {
		getSqlSessionTemplate().update("com.maicard.stat.sql.WithdrawStat.calculateProfit");
	}

}

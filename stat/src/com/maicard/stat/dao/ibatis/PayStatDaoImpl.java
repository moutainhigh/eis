package com.maicard.stat.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.dao.PayStatDao;
import com.maicard.stat.domain.PayStat;

@Repository
public class PayStatDaoImpl extends BaseDao implements PayStatDao {

	@Override
	public List<PayStat> list(PayStatCriteria payStatCriteria) {
		Assert.notNull(payStatCriteria, "payStatCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.PayStat.list", payStatCriteria);
	}
	
	@Override
	public List<PayStat> listOnPage(PayStatCriteria payStatCriteria) throws DataAccessException{
		Assert.notNull(payStatCriteria, "payStatCriteria must not be null");
		Assert.notNull(payStatCriteria.getPaging(), "paging must not be null");

		int totalResults = count(payStatCriteria);
		Paging paging = payStatCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.PayStat.list", payStatCriteria, rowBounds);
	}

	@Override
	public int count(PayStatCriteria payStatCriteria)  {
		
		return getSqlSessionTemplate().selectOne("com.maicard.stat.sql.PayStat.count", payStatCriteria);
/*		if(payStatCriteria.getStatTimeMode() == null)
			return getSqlSessionTemplate().selectOne("PayStat.bydaycount", payStatCriteria);
		else{
			if (payStatCriteria.getStatTimeMode().equals("day"))
				return getSqlSessionTemplate().selectOne("PayStat.bydaycount", payStatCriteria);			
			else
				return getSqlSessionTemplate().selectOne("PayStat.byhourcount", payStatCriteria);
			}*/
	}

	@Override
	public void calculateProfit() {
		getSqlSessionTemplate().update("com.maicard.stat.sql.PayStat.calculateProfit");
	}

	@Override
	public void statistic(PayStatCriteria payStatCriteria) {
		getSqlSessionTemplate().update("com.maicard.stat.sql.PayStat.statistic",payStatCriteria);
		
	}

}

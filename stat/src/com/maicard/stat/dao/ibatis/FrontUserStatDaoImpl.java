package com.maicard.stat.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.stat.criteria.FrontUserStatCriteria;
import com.maicard.stat.dao.FrontUserStatDao;
import com.maicard.stat.domain.FrontUserStat;

@Repository
public class FrontUserStatDaoImpl  extends BaseDao implements FrontUserStatDao {

	@Override
	public List<FrontUserStat> listOnPage(FrontUserStatCriteria frontUserStatCriteria) throws DataAccessException{
		Assert.notNull(frontUserStatCriteria, "payStatCriteria must not be null");
		Assert.notNull(frontUserStatCriteria.getPaging(), "paging must not be null");

		int totalResults = count(frontUserStatCriteria);
		Paging paging = frontUserStatCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.FrontUserStat.list", frontUserStatCriteria, rowBounds);
	}

	

	@Override
	public int count(FrontUserStatCriteria frontUserStatCriteria) {
		return getSqlSessionTemplate().selectOne("com.maicard.stat.sql.FrontUserStat.count", frontUserStatCriteria);
	}



}

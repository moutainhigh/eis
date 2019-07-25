package com.maicard.stat.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.stat.criteria.ProfitCriteria;
import com.maicard.stat.dao.ProfitDao;
import com.maicard.stat.domain.Profit;

@Repository
public class ProfitDaoImpl extends BaseDao implements ProfitDao {

	@Override
	public List<Profit> list(ProfitCriteria profitCriteria) {
		Assert.notNull(profitCriteria, "profitCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.Profit.list", profitCriteria);
	}
	
	@Override
	public List<Profit> listOnPage(ProfitCriteria profitCriteria) throws DataAccessException{
		Assert.notNull(profitCriteria, "profitCriteria must not be null");
		Assert.notNull(profitCriteria.getPaging(), "paging must not be null");

		int totalResults = count(profitCriteria);
		Paging paging = profitCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.stat.sql.Profit.list", profitCriteria, rowBounds);
	}

	@Override
	public int count(ProfitCriteria profitCriteria)  {
		return getSqlSessionTemplate().selectOne("com.maicard.stat.sql.Profit.count", profitCriteria);
	}

	@Override
	public int insert(Profit profit) {
		return getSqlSessionTemplate().insert("com.maicard.stat.sql.Profit.insert", profit);
	}

	@Override
	public int deleteBy(ProfitCriteria profitCriteria) {
		Assert.notNull(profitCriteria, "profitCriteria must not be null");
		Assert.isTrue(profitCriteria.getUuid() > 0 || profitCriteria.getStatTimeBegin() != null || profitCriteria.getStatTimeEnd() != null, "按条件删除时，profitCriteria中的uuid必须大于0或者查询statTime起止都不为空");

		return getSqlSessionTemplate().delete("com.maicard.stat.sql.Profit.deleteBy", profitCriteria);

	}


}

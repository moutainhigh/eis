package com.maicard.wpt.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.wpt.criteria.WeixinGroupCriteria;
import com.maicard.wpt.dao.WeixinGroupDao;
import com.maicard.wpt.domain.WeixinGroup;

public class WeixinGroupDaoImpl extends BaseDao implements WeixinGroupDao {
	@Override
	public int insert(WeixinGroup weixinGroup) throws DataAccessException {
		return getSqlSessionTemplate().insert("WeixinGroup.insert", weixinGroup);
	}

	@Override
	public int update(WeixinGroup weixinGroup) throws DataAccessException {
		return getSqlSessionTemplate().update("WeixinGroup.update", weixinGroup);
	}

	@Override
	public int delete(long weixinGroupId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Mapbox.delete", weixinGroupId);
	}

	@Override
	public WeixinGroup select(long weixinGroupId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("WeixinGroup.select",weixinGroupId);
	}

	@Override
	public List<WeixinGroup> list(WeixinGroupCriteria weixinGroupCriteria) throws DataAccessException {
		Assert.notNull(weixinGroupCriteria, "weixinGroupCriteria must not be null");
		return getSqlSessionTemplate().selectList("WeixinGroup.list", weixinGroupCriteria);
	}

	@Override
	public List<WeixinGroup> listOnPage(WeixinGroupCriteria weixinGroupCriteria) throws DataAccessException {
		Assert.notNull(weixinGroupCriteria, "weixinGroupCriteria must not be null");
		Assert.notNull(weixinGroupCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(weixinGroupCriteria);
		Paging paging = weixinGroupCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("WeixinGroup.list", weixinGroupCriteria, rowBounds);
	}

	@Override
	public int count(WeixinGroupCriteria weixinGroupCriteria) throws DataAccessException {
		Assert.notNull(weixinGroupCriteria, "weixinGroupCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("WeixinGroup.count",weixinGroupCriteria)).intValue();
	}
}

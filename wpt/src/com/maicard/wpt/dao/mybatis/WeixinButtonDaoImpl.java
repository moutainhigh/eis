package com.maicard.wpt.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.wpt.criteria.WeixinButtonCriteria;
import com.maicard.wpt.dao.WeixinButtonDao;
import com.maicard.wpt.domain.WeixinButton;

public class WeixinButtonDaoImpl extends BaseDao implements WeixinButtonDao {
	@Override
	public int insert(WeixinButton weixinButton) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.wpt.sql.WeixinButton.insert", weixinButton);
	}

	@Override
	public int update(WeixinButton weixinButton) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.wpt.sql.WeixinButton.update", weixinButton);
	}

	@Override
	public int delete(long weixinButtonId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.wpt.sql.WeixinButton.delete", weixinButtonId);
	}
	

	@Override
	public void deleteByUuid(long uuid) {
		getSqlSessionTemplate().delete("com.maicard.wpt.sql.WeixinButton.deleteByUuid", uuid);
	}

	@Override
	public WeixinButton select(long weixinButtonId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.wpt.sql.WeixinButton.select",weixinButtonId);
	}

	@Override
	public List<WeixinButton> list(WeixinButtonCriteria weixinButtonCriteria) throws DataAccessException {
		Assert.notNull(weixinButtonCriteria, "weixinButtonCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.wpt.sql.WeixinButton.list", weixinButtonCriteria);
	}

	@Override
	public List<WeixinButton> listOnPage(WeixinButtonCriteria weixinButtonCriteria) throws DataAccessException {
		Assert.notNull(weixinButtonCriteria, "weixinButtonCriteria must not be null");
		Assert.notNull(weixinButtonCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(weixinButtonCriteria);
		Paging paging = weixinButtonCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.wpt.sql.WeixinButton.list", weixinButtonCriteria, rowBounds);
	}

	@Override
	public int count(WeixinButtonCriteria weixinButtonCriteria) throws DataAccessException {
		Assert.notNull(weixinButtonCriteria, "weixinButtonCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("com.maicard.wpt.sql.WeixinButton.count",weixinButtonCriteria)).intValue();
	}

}

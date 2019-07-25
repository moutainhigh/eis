package com.maicard.wpt.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.wpt.criteria.AutoResponseModelCriteria;
import com.maicard.wpt.dao.AutoResponseModelDao;
import com.maicard.wpt.domain.AutoResponseModel;

public class AutoResponseModelDaoImpl extends BaseDao implements AutoResponseModelDao {
	@Override
	public int insert(AutoResponseModel autoResponseModel) throws DataAccessException {
		return getSqlSessionTemplate().insert("AutoResponseModel.insert", autoResponseModel);
	}

	@Override
	public int update(AutoResponseModel autoResponseModel) throws DataAccessException {
		return getSqlSessionTemplate().update("AutoResponseModel.update", autoResponseModel);
	}

	@Override
	public int delete(long autoResponseModelId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Mapbox.delete", autoResponseModelId);
	}

	@Override
	public AutoResponseModel select(long autoResponseModelId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("AutoResponseModel.select",autoResponseModelId);
	}

	@Override
	public List<AutoResponseModel> list(AutoResponseModelCriteria autoResponseModelCriteria) throws DataAccessException {
		Assert.notNull(autoResponseModelCriteria, "autoResponseModelCriteria must not be null");
		return getSqlSessionTemplate().selectList("AutoResponseModel.list", autoResponseModelCriteria);
	}

	@Override
	public List<AutoResponseModel> listOnPage(AutoResponseModelCriteria autoResponseModelCriteria) throws DataAccessException {
		Assert.notNull(autoResponseModelCriteria, "autoResponseModelCriteria must not be null");
		Assert.notNull(autoResponseModelCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(autoResponseModelCriteria);
		Paging paging = autoResponseModelCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("AutoResponseModel.list", autoResponseModelCriteria, rowBounds);
	}

	@Override
	public int count(AutoResponseModelCriteria autoResponseModelCriteria) throws DataAccessException {
		Assert.notNull(autoResponseModelCriteria, "autoResponseModelCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("AutoResponseModel.count",autoResponseModelCriteria)).intValue();
	}
}

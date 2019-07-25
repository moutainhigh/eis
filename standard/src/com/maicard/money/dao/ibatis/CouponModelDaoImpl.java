package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.dao.CouponModelDao;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.CouponModel;

public class CouponModelDaoImpl extends BaseDao implements CouponModelDao {
	@Override
	public int insert(CouponModel couponModel) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.CouponModel.insert", couponModel);
	}

	@Override
	public int update(CouponModel couponModel) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.CouponModel.update", couponModel);
	}

	@Override
	public int delete(long couponModelId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.CouponModel.delete", couponModelId);
	}

	@Override
	public CouponModel select(long couponModelId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.CouponModel.select",couponModelId);
	}

	@Override
	public List<CouponModel> list(CouponModelCriteria couponModelCriteria) throws DataAccessException {
		Assert.notNull(couponModelCriteria, "couponModelCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.CouponModel.list", couponModelCriteria);
	}

	@Override
	public List<CouponModel> listOnPage(CouponModelCriteria couponModelCriteria) throws DataAccessException {
		Assert.notNull(couponModelCriteria, "couponModelCriteria must not be null");
		Assert.notNull(couponModelCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(couponModelCriteria);
		Paging paging = couponModelCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.CouponModel.list", couponModelCriteria, rowBounds);
	}

	@Override
	public int count(CouponModelCriteria couponModelCriteria) throws DataAccessException {
		Assert.notNull(couponModelCriteria, "couponModelCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("com.maicard.money.sql.CouponModel.count",couponModelCriteria)).intValue();
	}
}

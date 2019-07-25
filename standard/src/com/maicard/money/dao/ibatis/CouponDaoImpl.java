package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.dao.CouponDao;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.domain.Coupon;

public class CouponDaoImpl extends BaseDao implements CouponDao {
	@Override
	public int insert(Coupon coupon) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.Coupon.insert", coupon);
	}

	@Override
	public int update(Coupon coupon) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.Coupon.update", coupon);
	}

	@Override
	public int delete(long couponId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.Coupon.delete", couponId);
	}

	@Override
	public Coupon select(long couponId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.Coupon.select",couponId);
	}

	@Override
	public List<Coupon> list(CouponCriteria couponCriteria) throws DataAccessException {
		Assert.notNull(couponCriteria, "couponCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Coupon.list", couponCriteria);
	}

	@Override
	public List<Coupon> listOnPage(CouponCriteria couponCriteria) throws DataAccessException {
		Assert.notNull(couponCriteria, "couponCriteria must not be null");
		Assert.notNull(couponCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(couponCriteria);
		Paging paging = couponCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Coupon.list", couponCriteria, rowBounds);
	}

	@Override
	public int count(CouponCriteria couponCriteria) throws DataAccessException {
		Assert.notNull(couponCriteria, "couponCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("com.maicard.money.sql.Coupon.count",couponCriteria)).intValue();
	}
}

package com.maicard.ec.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;





import com.maicard.ec.criteria.FeeAdjustCriteria;
import com.maicard.ec.dao.FeeAdjustDao;
import com.maicard.ec.domain.FeeAdjust;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class FeeAdjustDaoImpl extends BaseDao implements FeeAdjustDao {


	@Override
	public int insert(FeeAdjust feeAdjust) {
		return 	 getSqlSessionTemplate().insert("FeeAdjust.insert", feeAdjust);
	}

	@Override
	public int update(FeeAdjust feeAdjust) {
		return getSqlSessionTemplate().update("FeeAdjust.update", feeAdjust);
	}

	@Override
	public int delete(long feeAdjustId) {
		return getSqlSessionTemplate().delete("FeeAdjust.delete", feeAdjustId);
	}

	@Override
	public List<FeeAdjust> list(FeeAdjustCriteria feeAdjustCriteria) {
		Assert.notNull(feeAdjustCriteria, "feeAdjustCriteria must not be null");		
		return getSqlSessionTemplate().selectList("FeeAdjust.list", feeAdjustCriteria);	
	}

	@Override
	public List<FeeAdjust> listOnPage(FeeAdjustCriteria feeAdjustCriteria) {
		Assert.notNull(feeAdjustCriteria, "feeAdjustCriteria must not be null");
		Assert.notNull(feeAdjustCriteria.getPaging(), "paging must not be null");

		int totalResults = count(feeAdjustCriteria);
		Paging paging = feeAdjustCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("FeeAdjust.list", feeAdjustCriteria, rowBounds);
	}

	@Override
	public int count(FeeAdjustCriteria feeAdjustCriteria) {
		Assert.notNull(feeAdjustCriteria, "feeAdjustCriteria must not be null");		
		return getSqlSessionTemplate().selectOne("FeeAdjust.count", feeAdjustCriteria);	
	}

	@Override
	public FeeAdjust select(long feeAdjustId) {
		return getSqlSessionTemplate().selectOne("FeeAdjust.select", feeAdjustId);	
	}



}

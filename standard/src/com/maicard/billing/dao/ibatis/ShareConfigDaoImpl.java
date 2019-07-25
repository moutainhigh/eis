package com.maicard.billing.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.dao.ShareConfigDao;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class ShareConfigDaoImpl extends BaseDao implements ShareConfigDao {

	public int insert(ShareConfig shareConfig) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.billing.sql.ShareConfig.insert", shareConfig);
	}

	public int update(ShareConfig shareConfig) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.billing.sql.ShareConfig.update", shareConfig);
	}
	
	public int updateNoNull(ShareConfig shareConfig) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.billing.sql.ShareConfig.updateNoNull", shareConfig);
	}
	
	public int delete(int shareConfigId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.billing.sql.ShareConfig.delete", new Integer(shareConfigId));
	}

	public ShareConfig select(int shareConfigId) throws DataAccessException {
		return (ShareConfig) getSqlSessionTemplate().selectOne("com.maicard.billing.sql.ShareConfig.select", new Integer(shareConfigId));
	}
	

	public List<ShareConfig> list(ShareConfigCriteria shareConfigCriteria) throws DataAccessException {
		Assert.notNull(shareConfigCriteria, "shareConfigCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.billing.sql.ShareConfig.list", shareConfigCriteria);
	}

	public List<ShareConfig> listOnPage(ShareConfigCriteria shareConfigCriteria) throws DataAccessException {
		Assert.notNull(shareConfigCriteria, "shareConfigCriteria must not be null");
		Assert.notNull(shareConfigCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(shareConfigCriteria);
		Paging paging = shareConfigCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.billing.sql.ShareConfig.list", shareConfigCriteria, rowBounds);
	}

	public int count(ShareConfigCriteria shareConfigCriteria) throws DataAccessException {
		Assert.notNull(shareConfigCriteria, "shareConfigCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.billing.sql.ShareConfig.count", shareConfigCriteria)).intValue();
	}

	@Override
	public void deleteByUuid(long shareUuid) {
		Assert.isTrue(shareUuid != 0, "批量删除的shareUuid不能为0");
		getSqlSessionTemplate().delete("com.maicard.billing.sql.ShareConfig.deleteByUuid", shareUuid);
		
	}



}

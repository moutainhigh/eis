package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.dao.DataDefineDao;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class DataDefineDaoImpl extends BaseDao implements DataDefineDao {

	public int insert(DataDefine dataDefine) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.common.sql.DataDefine.insert", dataDefine);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'DataDefine#' + #dataDefine.dataDefineId")
	public int update(DataDefine dataDefine) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.common.sql.DataDefine.update", dataDefine);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'DataDefine#' + #dataDefineId")
	public int delete(int dataDefineId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.DataDefine.delete", dataDefineId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'DataDefine#' + #dataDefineId")
	public DataDefine select(int dataDefineId) throws DataAccessException {
		
		DataDefine dataDefine = getSqlSessionTemplate().selectOne("com.maicard.common.sql.DataDefine.select", dataDefineId);
		if(dataDefine != null){
			if(logger.isDebugEnabled()){
				logger.debug("从数据库选择DataDefine[" + dataDefine.getDataCode() + "/" + dataDefineId + "]");
			}
		} else {
			logger.debug("从数据库选择DataDefine[空/" + dataDefineId + "]");
		}
		return dataDefine;
	}
	
	public List<Integer> listPk(DataDefineCriteria dataDefineCriteria) throws DataAccessException {
		Assert.notNull(dataDefineCriteria, "dataDefineCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.DataDefine.listPk", dataDefineCriteria);
	}

	public List<Integer> listPkOnPage(DataDefineCriteria dataDefineCriteria) throws DataAccessException {
		Assert.notNull(dataDefineCriteria, "dataDefineCriteria must not be null");
		Assert.notNull(dataDefineCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(dataDefineCriteria);
		Paging paging = dataDefineCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.DataDefine.listPk", dataDefineCriteria, rowBounds);
	}

	public List<DataDefine> list(DataDefineCriteria dataDefineCriteria) throws DataAccessException {
		Assert.notNull(dataDefineCriteria, "dataDefineCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.DataDefine.list", dataDefineCriteria);
	}

	public List<DataDefine> listOnPage(DataDefineCriteria dataDefineCriteria) throws DataAccessException {
		Assert.notNull(dataDefineCriteria, "dataDefineCriteria must not be null");
		Assert.notNull(dataDefineCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(dataDefineCriteria);
		Paging paging = dataDefineCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.DataDefine.list", dataDefineCriteria, rowBounds);
	}

	public int count(DataDefineCriteria dataDefineCriteria) throws DataAccessException {
		Assert.notNull(dataDefineCriteria, "dataDefineCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.DataDefine.count", dataDefineCriteria)).intValue();
	}

}

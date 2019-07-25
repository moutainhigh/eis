package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.DictCriteria;
import com.maicard.common.dao.DictDao;
import com.maicard.common.domain.Dict;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class DictDaoImpl extends BaseDao implements DictDao {

	public int insert(Dict dict) throws DataAccessException {
		return getSqlSessionTemplate().insert("Dict.insert", dict);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Dict#' + #dict.dictId")
	public int update(Dict dict) throws DataAccessException {
		return getSqlSessionTemplate().update("Dict.update", dict);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Dict#' + #dictId")
	public int delete(int dictId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Dict.delete", dictId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Dict#' + #dictId")
	public Dict select(int dictId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("Dict.select", dictId);
	}
	
	@Override
	public List<Integer> listPk(DictCriteria dictCriteria) {
		Assert.notNull(dictCriteria, "dictCriteria must not be null");
		return getSqlSessionTemplate().selectList("Dict.listPk", dictCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(DictCriteria dictCriteria) {
		Assert.notNull(dictCriteria, "dictCriteria must not be null");
		Assert.notNull(dictCriteria.getPaging(), "paging must not be null");

		int totalResults = count(dictCriteria);
		Paging paging = dictCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Dict.listPk", dictCriteria, rowBounds);
	}

	public List<Dict> list(DictCriteria dictCriteria) throws DataAccessException {
		Assert.notNull(dictCriteria, "dictCriteria must not be null");

		return getSqlSessionTemplate().selectList("Dict.list", dictCriteria);
	}

	public List<Dict> listOnPage(DictCriteria dictCriteria) throws DataAccessException {
		Assert.notNull(dictCriteria, "dictCriteria must not be null");
		Assert.notNull(dictCriteria.getPaging(), "paging must not be null");

		int totalResults = count(dictCriteria);
		Paging paging = dictCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Dict.list", dictCriteria, rowBounds);
	}

	public int count(DictCriteria dictCriteria) throws DataAccessException {
		Assert.notNull(dictCriteria, "dictCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("Dict.count", dictCriteria)).intValue();
	}

	

}

package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.AdvertCriteria;
import com.maicard.site.dao.AdvertDao;
import com.maicard.site.domain.Advert;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class AdvertDaoImpl extends BaseDao implements AdvertDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;
;

	public int insert(Advert advert) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("Advert.insert", advert);

	}

	@CacheEvict(value = cacheName, key = "'Advert#' + #advert.advertId")
	public int update(Advert advert) throws DataAccessException {


		return getSqlSessionTemplate().update("Advert.update", advert);


	}
	@CacheEvict(value = cacheName, key = "'Advert#' + #advert.advertId")
	public int delete(int advertId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Advert.delete", new Integer(advertId));

	}

	@Cacheable(value = cacheName, key = "'Advert#' + #advertId")
	public Advert select(int advertId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库选择Advert[" + advertId + "]");
		}
		return getSqlSessionTemplate().selectOne("Advert.select", new Integer(advertId));
	}

	@Override
	public List<Integer> listPk(AdvertCriteria advertCriteria) throws DataAccessException {
		Assert.notNull(advertCriteria, "advertCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Advert.listPk", advertCriteria);
	}


	@Override
	public List<Integer> listPkOnPage(AdvertCriteria advertCriteria) throws DataAccessException {
		Assert.notNull(advertCriteria, "advertCriteria must not be null");
		Assert.notNull(advertCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(advertCriteria);
		Paging paging = advertCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Advert.listPk", advertCriteria, rowBounds);
	}

	public List<Advert> list(AdvertCriteria advertCriteria) throws DataAccessException {
		Assert.notNull(advertCriteria, "advertCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Advert.list", advertCriteria);
	}


	public List<Advert> listOnPage(AdvertCriteria advertCriteria) throws DataAccessException {
		Assert.notNull(advertCriteria, "advertCriteria must not be null");
		Assert.notNull(advertCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(advertCriteria);
		Paging paging = advertCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Advert.list", advertCriteria, rowBounds);
	}

	public int count(AdvertCriteria advertCriteria) throws DataAccessException {
		Assert.notNull(advertCriteria, "advertCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("Advert.count", advertCriteria)).intValue();
	}

}

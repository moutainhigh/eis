package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.dao.TagDao;
import com.maicard.site.domain.Tag;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class TagDaoImpl extends BaseDao implements TagDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(Tag tag) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("Tag.insert", tag);
	}

	@CacheEvict(value = cacheName, key = "'Tag#' + #tag.tagId")
	public int update(Tag tag) throws DataAccessException {
		return getSqlSessionTemplate().update("Tag.update", tag);
	}

	@CacheEvict(value = cacheName, key = "'Tag#' + #tagId")
	public int delete(long tagId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Tag.delete", tagId);
	}

	@Cacheable(value = cacheName, key = "'Tag#' + #tagId")
	public Tag select(long tagId) throws DataAccessException {
		return (Tag) getSqlSessionTemplate().selectOne("Tag.select", tagId);
	}

	@Override
	public List<Long> listPk(TagCriteria tagCriteria) throws DataAccessException {
		Assert.notNull(tagCriteria, "tagsCriteria must not be null");		
		return getSqlSessionTemplate().selectList("Tag.listPk", tagCriteria);
	}

	@Override
	public List<Long> listPkOnPage(TagCriteria tagCriteria) throws DataAccessException {
		Assert.notNull(tagCriteria, "tagsCriteria must not be null");
		Assert.notNull(tagCriteria.getPaging(), "paging must not be null");		
		int totalResults = count(tagCriteria);
		Paging paging = tagCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Tag.listPk", tagCriteria, rowBounds);
	}

	@Override
	public List<Tag> list(TagCriteria tagCriteria) throws DataAccessException {
		Assert.notNull(tagCriteria, "tagsCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Tag.list", tagCriteria);
	}


	@Override
	public List<Tag> listOnPage(TagCriteria tagCriteria) throws DataAccessException {
		Assert.notNull(tagCriteria, "tagsCriteria must not be null");
		Assert.notNull(tagCriteria.getPaging(), "paging must not be null");		
		int totalResults = count(tagCriteria);
		Paging paging = tagCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Tag.list", tagCriteria, rowBounds);
	}

	@Override
	public int count(TagCriteria tagCriteria) throws DataAccessException {
		Assert.notNull(tagCriteria, "tagsCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("Tag.count", tagCriteria)).intValue();
	}

}

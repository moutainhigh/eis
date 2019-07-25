package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.TagStatCriteria;
import com.maicard.site.dao.TagStatDao;
import com.maicard.site.domain.TagStat;

import org.apache.ibatis.session.RowBounds;

@Repository
public class TagStatDaoImpl extends BaseDao implements TagStatDao {

	public void insert(TagStat tagStat) throws DataAccessException {
		getSqlSessionTemplate().insert("TagStat.insert", tagStat);
	}

	public int update(TagStat tagStat) throws DataAccessException {


		return getSqlSessionTemplate().update("TagStat.update", tagStat);


	}

	public int delete(int tagStatId) throws DataAccessException {


		return getSqlSessionTemplate().delete("TagStat.delete", new Integer(tagStatId));


	}

	public TagStat select(int tagStatId) throws DataAccessException {
		return (TagStat) getSqlSessionTemplate().selectOne("TagStat.select", new Integer(tagStatId));
	}


	public List<TagStat> list(TagStatCriteria tagStatCriteria) throws DataAccessException {
		Assert.notNull(tagStatCriteria, "tagStatCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("TagStat.list", tagStatCriteria);
	}


	public List<TagStat> listOnPage(TagStatCriteria tagStatCriteria) throws DataAccessException {
		Assert.notNull(tagStatCriteria, "tagStatCriteria must not be null");
		Assert.notNull(tagStatCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(tagStatCriteria);
		Paging paging = tagStatCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("TagStat.list", tagStatCriteria, rowBounds);
	}

	public int count(TagStatCriteria tagStatCriteria) throws DataAccessException {
		Assert.notNull(tagStatCriteria, "tagStatCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("TagStat.count", tagStatCriteria)).intValue();
	}

}

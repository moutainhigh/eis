package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.dao.TagObjectRelationDao;
import com.maicard.site.domain.TagObjectRelation;

import org.apache.ibatis.session.RowBounds;

@Repository
public class TagObjectRelationDaoImpl extends BaseDao implements TagObjectRelationDao {

	public int insert(TagObjectRelation tagObjectRelation) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.site.sql.TagObjectRelation.insert", tagObjectRelation);
	}

	public int update(TagObjectRelation tagObjectRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.site.sql.TagObjectRelation.update", tagObjectRelation);


	}

	public int delete(long tagObjectRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("com.maicard.site.sql.TagObjectRelation.delete", tagObjectRelationId);


	}

	public TagObjectRelation select(long tagObjectRelationId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.site.sql.TagObjectRelation.select", tagObjectRelationId);
	}


	public List<TagObjectRelation> list(TagObjectRelationCriteria tagObjectRelationCriteria) throws DataAccessException {
		Assert.notNull(tagObjectRelationCriteria, "tagObjectRelationCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.site.sql.TagObjectRelation.list", tagObjectRelationCriteria);
	}


	public List<TagObjectRelation> listOnPage(TagObjectRelationCriteria tagObjectRelationCriteria) throws DataAccessException {
		Assert.notNull(tagObjectRelationCriteria, "tagObjectRelationCriteria must not be null");
		Assert.notNull(tagObjectRelationCriteria.getPaging(), "paging must not be null");

		int totalResults = count(tagObjectRelationCriteria);
		Paging paging = tagObjectRelationCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.site.sql.TagObjectRelation.list", tagObjectRelationCriteria, rowBounds);
	}

	public int count(TagObjectRelationCriteria tagObjectRelationCriteria) throws DataAccessException {
		Assert.notNull(tagObjectRelationCriteria, "tagObjectRelationCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.site.sql.TagObjectRelation.count", tagObjectRelationCriteria)).intValue();
	}


	@Override
	public int delete(TagObjectRelationCriteria tagObjectRelationCriteria) {
		if(tagObjectRelationCriteria == null){
			return -1;
		}
		if(tagObjectRelationCriteria.getObjectType() == null || tagObjectRelationCriteria.getObjectType().equals("") ){
			return -1;
		}
		if(tagObjectRelationCriteria.getObjectId() < 1){
			return -1;
		}
		return getSqlSessionTemplate().delete("com.maicard.site.sql.TagObjectRelation.deleteByCriteria", tagObjectRelationCriteria);

	}
}

package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.domain.TagObjectRelation;

public interface TagObjectRelationDao {

	int insert(TagObjectRelation tagObjectRelation) throws DataAccessException;

	int update(TagObjectRelation tagObjectRelation) throws DataAccessException;

	int delete(long tagObjectRelationId) throws DataAccessException;

	TagObjectRelation select(long tagObjectRelationId) throws DataAccessException;

	List<TagObjectRelation> list(TagObjectRelationCriteria tagObjectRelationCriteria) throws DataAccessException;
	
	List<TagObjectRelation> listOnPage(TagObjectRelationCriteria tagObjectRelationCriteria) throws DataAccessException;
	
	int count(TagObjectRelationCriteria tagObjectRelationCriteria) throws DataAccessException;

	int delete(TagObjectRelationCriteria tagObjectRelationCriteria);
}

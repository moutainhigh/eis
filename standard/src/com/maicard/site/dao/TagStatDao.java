package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.TagStatCriteria;
import com.maicard.site.domain.TagStat;

public interface TagStatDao {

	void insert(TagStat tagStat) throws DataAccessException;

	int update(TagStat tagStat) throws DataAccessException;

	int delete(int tagStatId) throws DataAccessException;

	TagStat select(int tagStatId) throws DataAccessException;

	List<TagStat> list(TagStatCriteria tagStatCriteria) throws DataAccessException;
	
	List<TagStat> listOnPage(TagStatCriteria tagStatCriteria) throws DataAccessException;
	
	int count(TagStatCriteria tagStatCriteria) throws DataAccessException;

}

package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.domain.Tag;

public interface TagDao {

	int insert(Tag tag) throws DataAccessException;

	int update(Tag tag) throws DataAccessException;

	int delete(long tagId) throws DataAccessException;

	Tag select(long tagId) throws DataAccessException;

	List<Tag> list(TagCriteria tagCriteria) throws DataAccessException;
	
	List<Tag> listOnPage(TagCriteria tagCriteria) throws DataAccessException;
	
	int count(TagCriteria tagCriteria) throws DataAccessException;

	List<Long> listPkOnPage(TagCriteria tagCriteria)
			throws DataAccessException;

	List<Long> listPk(TagCriteria tagCriteria) throws DataAccessException;

}

package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.CommentConfigCriteria;
import com.maicard.common.domain.CommentConfig;

public interface CommentConfigDao {

	int insert(CommentConfig commentConfig) throws DataAccessException;

	int update(CommentConfig commentConfig) throws DataAccessException;

	int delete(long commentConfigId) throws DataAccessException;

	/**
	 * 请勿直接调用
	 * 请调用CachedCommentConfigService.select
	 * 
	 * @param commentConfigId
	 * @return
	 * @throws DataAccessException
	 */
	CommentConfig select(long commentConfigId) throws DataAccessException;

	List<CommentConfig> list(CommentConfigCriteria commentConfigCriteria) throws DataAccessException;
	
	List<CommentConfig> listOnPage(CommentConfigCriteria commentConfigCriteria) throws DataAccessException;
	
	int count(CommentConfigCriteria commentConfigCriteria) throws DataAccessException;

	List<Long> listPk(CommentConfigCriteria commentConfigCriteria)
			throws DataAccessException;

	List<Long> listPkOnPage(CommentConfigCriteria commentConfigCriteria)
			throws DataAccessException;

}

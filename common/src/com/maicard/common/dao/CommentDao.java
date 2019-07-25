package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.Comment;

public interface CommentDao {

	int insert(Comment comment) throws DataAccessException;

	int update(Comment comment) throws DataAccessException;

	int delete(long commentId) throws DataAccessException;

	/**
	 * 请勿直接调用
	 * 请调用CachedCommentService.select
	 * 
	 * @param commentId
	 * @return
	 * @throws DataAccessException
	 */
	Comment select(long commentId) throws DataAccessException;

	List<Comment> list(CommentCriteria commentCriteria) throws DataAccessException;
	
	List<Comment> listOnPage(CommentCriteria commentCriteria) throws DataAccessException;
	
	int count(CommentCriteria commentCriteria) throws DataAccessException;

	List<Long> listPk(CommentCriteria commentCriteria)
			throws DataAccessException;

	List<Long> listPkOnPage(CommentCriteria commentCriteria)
			throws DataAccessException;

}

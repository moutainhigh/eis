package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.CommentConfig;
import com.maicard.common.domain.EisMessage;

public interface CommentService {

	EisMessage insert(Comment comment);

	int update(Comment comment);

	int delete(long commentId);
	
	Comment select(long commentId);
	
	List<Comment> list(CommentCriteria commentCriteria);

	List<Comment> listOnPage(CommentCriteria commentCriteria);
	
	int count(CommentCriteria commentCriteria);

	List<List<Comment>> sort(List<Comment> commentList);

	CommentConfig getCommentConfig(Comment comment);



}

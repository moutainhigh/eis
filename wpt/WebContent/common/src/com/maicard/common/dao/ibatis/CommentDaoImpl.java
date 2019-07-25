package com.maicard.common.dao.ibatis;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.dao.CommentDao;
import com.maicard.common.domain.Comment;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class CommentDaoImpl extends BaseDao implements CommentDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;
	
;

	public int insert(Comment comment) throws DataAccessException {
		Assert.notNull(comment, "尝试插入的评论不能为空");
		if(comment.getCreateTime() == null){
			comment.setCreateTime(new Date());
		}
		return (Integer)getSqlSessionTemplate().insert("com.maicard.common.sql.Comment.insert", comment);

	}

	@CacheEvict(value = cacheName, key = "'Comment#' + #comment.commentId")
	public int update(Comment comment) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.common.sql.Comment.update", comment);


	}
	@CacheEvict(value = cacheName, key = "'Comment#' + #commentId")
	public int delete(long commentId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.Comment.delete", commentId);

	}

	@Cacheable(value = cacheName, key = "'Comment#' + #commentId")
	public Comment select(long commentId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库选择Comment[" + commentId + "]");
		}
		return getSqlSessionTemplate().selectOne("com.maicard.common.sql.Comment.select", commentId);
	}

	@Override
	public List<Long> listPk(CommentCriteria commentCriteria) throws DataAccessException {
		Assert.notNull(commentCriteria, "commentCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Comment.listPk", commentCriteria);
	}


	@Override
	public List<Long> listPkOnPage(CommentCriteria commentCriteria) throws DataAccessException {
		Assert.notNull(commentCriteria, "commentCriteria must not be null");
		Assert.notNull(commentCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(commentCriteria);
		Paging paging = commentCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Comment.listPk", commentCriteria, rowBounds);
	}

	public List<Comment> list(CommentCriteria commentCriteria) throws DataAccessException {
		Assert.notNull(commentCriteria, "commentCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Comment.list", commentCriteria);
	}


	public List<Comment> listOnPage(CommentCriteria commentCriteria) throws DataAccessException {
		Assert.notNull(commentCriteria, "commentCriteria must not be null");
		Assert.notNull(commentCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(commentCriteria);
		Paging paging = commentCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Comment.list", commentCriteria, rowBounds);
	}

	public int count(CommentCriteria commentCriteria) throws DataAccessException {
		Assert.notNull(commentCriteria, "commentCriteria must not be null");
		Assert.isTrue(commentCriteria.getOwnerId() > 0,"ownerId不能为0");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.Comment.count", commentCriteria)).intValue();
	}

}

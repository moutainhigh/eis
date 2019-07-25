package com.maicard.common.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.common.criteria.CommentConfigCriteria;
import com.maicard.common.dao.CommentConfigDao;
import com.maicard.common.domain.CommentConfig;
import static com.maicard.common.criteria.CommentConfigCriteria.CACHE_NAME;

import org.apache.ibatis.session.RowBounds;

@Repository
public class CommentConfigDaoImpl extends BaseDao implements CommentConfigDao {
	;

	public int insert(CommentConfig commentConfig) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("CommentConfig.insert", commentConfig);

	}

	@CacheEvict(value = CACHE_NAME, key = "'CommentConfig#' + #commentConfig.commentConfigId")
	public int update(CommentConfig commentConfig) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.common.sql.CommentConfig.update", commentConfig);


	}
	@CacheEvict(value = CACHE_NAME, key = "'CommentConfig#' + #commentConfigId")
	public int delete(long commentConfigId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.CommentConfig.delete", commentConfigId);

	}

	@Cacheable(value = CACHE_NAME, key = "'CommentConfig#' + #commentConfigId")
	public CommentConfig select(long commentConfigId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库选择CommentConfig[" + commentConfigId + "]");
		}
		return getSqlSessionTemplate().selectOne("com.maicard.common.sql.CommentConfig.select", commentConfigId);
	}

	@Override
	public List<Long> listPk(CommentConfigCriteria commentConfigCriteria) throws DataAccessException {
		Assert.notNull(commentConfigCriteria, "commentConfigCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.CommentConfig.listPk", commentConfigCriteria);
	}


	@Override
	public List<Long> listPkOnPage(CommentConfigCriteria commentConfigCriteria) throws DataAccessException {
		Assert.notNull(commentConfigCriteria, "commentConfigCriteria must not be null");
		Assert.notNull(commentConfigCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(commentConfigCriteria);
		Paging paging = commentConfigCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.CommentConfig.listPk", commentConfigCriteria, rowBounds);
	}

	public List<CommentConfig> list(CommentConfigCriteria commentConfigCriteria) throws DataAccessException {
		Assert.notNull(commentConfigCriteria, "commentConfigCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.CommentConfig.list", commentConfigCriteria);
	}


	public List<CommentConfig> listOnPage(CommentConfigCriteria commentConfigCriteria) throws DataAccessException {
		Assert.notNull(commentConfigCriteria, "commentConfigCriteria must not be null");
		Assert.notNull(commentConfigCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(commentConfigCriteria);
		Paging paging = commentConfigCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.CommentConfig.list", commentConfigCriteria, rowBounds);
	}

	public int count(CommentConfigCriteria commentConfigCriteria) throws DataAccessException {
		Assert.notNull(commentConfigCriteria, "commentConfigCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.CommentConfig.count", commentConfigCriteria)).intValue();
	}

}

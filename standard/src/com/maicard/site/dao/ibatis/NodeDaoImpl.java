package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.dao.NodeDao;
import com.maicard.site.domain.Node;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class NodeDaoImpl extends BaseDao implements NodeDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(Node node) throws DataAccessException {
		
		return getSqlSessionTemplate().insert("com.maicard.site.sql.Node.insert", node);
	}

	@CacheEvict(value = cacheName, key = "'Node#' + #node.nodeId")
	public int update(Node node) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.site.sql.Node.update", node);
	}

	@CacheEvict(value = cacheName, key = "'Node#' + #nodeId")
	public int delete(int nodeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.site.sql.Node.delete", nodeId);
	}

	@Cacheable(value = cacheName, key = "'Node#' + #nodeId")
	public Node select(int nodeId) throws DataAccessException {
		return selectNoCache(nodeId);
	}
	
	@Cacheable(value = cacheName, key = "'Node#' + #nodeId")
	@Override
	public Node selectNoCache(int nodeId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.site.sql.Node.select", nodeId);
	}

	@Override
	public List<Integer> listPk(NodeCriteria nodeCriteria) throws DataAccessException {
		Assert.notNull(nodeCriteria, "nodeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Node.listPk", nodeCriteria);
	}


	@Override
	public List<Integer> listPkOnPage(NodeCriteria nodeCriteria) throws DataAccessException {
		Assert.notNull(nodeCriteria, "nodeCriteria must not be null");
		Assert.notNull(nodeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(nodeCriteria);
		Paging paging = nodeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Node.listPk", nodeCriteria, rowBounds);
	}

	public List<Node> list(NodeCriteria nodeCriteria) throws DataAccessException {
		Assert.notNull(nodeCriteria, "nodeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Node.list", nodeCriteria);
	}


	public List<Node> listOnPage(NodeCriteria nodeCriteria) throws DataAccessException {
		Assert.notNull(nodeCriteria, "nodeCriteria must not be null");
		Assert.notNull(nodeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(nodeCriteria);
		Paging paging = nodeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Node.list", nodeCriteria, rowBounds);
	}
	

	public List<Node> listByUdid(int udid) throws DataAccessException {
		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Node.listByUdid", udid);
		
	}

	public int count(NodeCriteria nodeCriteria) throws DataAccessException {
		Assert.notNull(nodeCriteria, "nodeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.site.sql.Node.count", nodeCriteria)).intValue();
	}

}

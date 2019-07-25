package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Node;

public interface NodeDao {

	int insert(Node node) throws DataAccessException;

	int update(Node node) throws DataAccessException;

	int delete(int nodeId) throws DataAccessException;

	Node select(int nodeId) throws DataAccessException;

	List<Node> list(NodeCriteria nodeCriteria) throws DataAccessException;
	
	List<Node> listOnPage(NodeCriteria nodeCriteria) throws DataAccessException;
	
	List<Node> listByUdid(int udid) throws DataAccessException;
	
	int count(NodeCriteria nodeCriteria) throws DataAccessException;

	List<Integer> listPk(NodeCriteria nodeCriteria);

	List<Integer> listPkOnPage(NodeCriteria nodeCriteria)
			throws DataAccessException;

	Node selectNoCache(int nodeId) throws DataAccessException;

}

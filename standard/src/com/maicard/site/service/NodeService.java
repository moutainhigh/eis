package com.maicard.site.service;

import java.util.List;

import com.maicard.security.domain.User;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Node;

public interface NodeService {

	int insert(Node node);

	int update(Node node);

	int delete(int nodeId);
	List<Node> listInTree(NodeCriteria nodeCriteria);	
	int count(NodeCriteria nodeCriteria);	
	Node select(int nodeId);
	Node select(String path, String siteCode, long ownerId);
	
	Node select(String nodePath, long ownerId);

	
	List<Node> list(NodeCriteria nodeCriteria);
	List<Node> listOnPage(NodeCriteria nodeCriteria);	
	List<Node> listByUdid(int udid);	
	List<Node> getNodePath(int nodeId, long ownerId);	
	List<Node> getSameLevelNode(int nodeId, int nodeTypeId, boolean removeSelf);
	
	//获取比本节点高一级、并且显示到导航上的节点
	List<Node> getParentLevelNode(int nodeId, int nodeTypeId);
	
	/*
	 * 获取比本节点低一级、并且显示到导航上的节点
	 * 如果nodeTypeId != 0 则获取对应类型的node
	 */
	List<Node> getChildrenLevelNode(int nodeId, int nodeTypeId);
	
	void listAllChildren(List<Node> all, int  rootNodeId, int nodeTypeId);
	//根据用户UUID，列出他拥有权限的所有节点
	List<Node> listPrivilegedNodeByUuid(User user);

	Node getDefaultNode(String systemCode, long ownerId);

	List<Node> generateTree(List<Node> plateNodeList);

	/**
	 * 根据指定的根节点，获取该节点以下的所有节点
	 * @param rootNode
	 * @return
	 */
	List<Node> generateNavigation(Node rootNode);

	List<Node> listOnPageClone(NodeCriteria nodeCriteria);

}

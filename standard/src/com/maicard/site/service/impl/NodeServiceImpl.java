package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.User;
import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.dao.NodeDao;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.ObjectType;

@Service
public class NodeServiceImpl extends BaseService implements NodeService {

	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private ConfigService configService;
	@Resource
	private NodeDao nodeDao;
	@Resource
	private DocumentNodeRelationService documentNodeRelationService;
	@Resource
	private TemplateService templateService;
	@Resource
	private TagService tagService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;

	private static Map<String,List<Node>> navigationNodeCache = new HashMap<String, List<Node>>();



	public int insert(Node node) {
		Assert.notNull(node,"新增节点为空");
		Assert.notNull(node.getName(),"新增节点名称不能为空");
		Assert.notNull(node.getAlias(),"新增节点别名不能为空");
		Assert.isTrue(node.getAlias().matches("[A-Za-z0-9_\\.]+"),"新增节点多站点识别代码只允许英文字母、数字、下划线或.:" + node.getAlias());
		Assert.notNull(node.getSiteCode(),"新增节点多站点识别代码不能为空");
		Assert.notNull(node.getProcessClass(),"新增节点处理器不能为空");

		node.setCurrentStatus(BasicStatus.normal.getId());

		generateNodePath(node);
		node.setPath(node.getPath().replaceAll("^/", "").replaceAll("/$", ""));
		generateNodeLevel(node);
		int rs = 0;
		rs =  nodeDao.insert(node);

		if(rs != 1){
			logger.error("无法写入新节点");
			return 0;
		}

		//更新tags
		if(node.getTags() == null || node.getTags().equals("")){
			//删除跟之关联的所有tag
			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
			tagObjectRelationCriteria.setObjectType(ObjectType.node.toString());
			tagObjectRelationCriteria.setObjectId(node.getNodeId());
			tagObjectRelationService.delete(tagObjectRelationCriteria);
		} else {
			tagObjectRelationService.sync(node.getOwnerId(), ObjectType.document.toString(), node.getNodeId(), node.getTags());
		}
		return 1;
	}

	public int update(Node node) {
		int actualRowsAffected = 0;

		generateNodePath(node);
		node.setPath(node.getPath().replaceAll("^/", "").replaceAll("/$", ""));
		int nodeId = node.getNodeId();

		Node _oldNode = nodeDao.select(nodeId);

		if (_oldNode == null) {
			return 0;
		}
		try{
			actualRowsAffected = nodeDao.update(node);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		logger.debug("更新节点，父节点ID[" + node.getParentNodeId() + "],关联默认模版是:" + node.getTemplateId());
		//更新tags
		if(node.getTags() == null || node.getTags().equals("")){
			//删除跟之关联的所有tag
			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
			tagObjectRelationCriteria.setObjectType(ObjectType.node.toString());
			tagObjectRelationCriteria.setObjectId(node.getNodeId());
			tagObjectRelationService.delete(tagObjectRelationCriteria);
		} else {
			tagObjectRelationService.sync(node.getOwnerId(), ObjectType.node.toString(), node.getNodeId(), node.getTags());
		}
		return actualRowsAffected;
	}

	public int delete(int nodeId) {
		int actualRowsAffected = 0;

		Node _oldNode = nodeDao.select(nodeId);

		if (_oldNode != null) {
			DocumentNodeRelationCriteria documentNodeRelationCriteria = new DocumentNodeRelationCriteria();
			documentNodeRelationCriteria.setNodeId(nodeId);
			documentNodeRelationService.delete(documentNodeRelationCriteria);
			try{
				actualRowsAffected = nodeDao.delete(nodeId);
			}catch(Exception e){
				logger.error("删除数据失败:" + e.getMessage());
			}
		}
		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
		tagObjectRelationCriteria.setObjectType(ObjectType.node.toString());
		tagObjectRelationCriteria.setObjectId(nodeId);
		tagObjectRelationService.delete(tagObjectRelationCriteria);
		return actualRowsAffected;
	}


	public List<Node> listInTree(NodeCriteria nodeCriteria) {
		List<Node> plateNodeList = list(nodeCriteria);
		if(plateNodeList == null){
			return null;
		}
		return generateTree(plateNodeList);

	}

	@Override
	public List<Node> generateTree(List<Node> plateNodeList) {
		if(plateNodeList == null){
			return Collections.emptyList();
		}
		for(int i = 0; i< plateNodeList.size(); i++){		
			for(int j = 0; j< plateNodeList.size(); j++){
				if(plateNodeList.get(j).getParentNodeId() == plateNodeList.get(i).getNodeId()){
					//logger.info("Add sub node: " + plateNodeList.get(j).getName() +"["+ plateNodeList.get(j).getNodeId() +"] to parent node:" + plateNodeList.get(i).getName() +"["+ plateNodeList.get(i).getNodeId() + "].");
					if(plateNodeList.get(i).getSubNodeList() == null){
						plateNodeList.get(i).setSubNodeList(new ArrayList<Node>());
					}
					plateNodeList.get(i).getSubNodeList().add(plateNodeList.get(j));
				}
			}

		}
		ArrayList<Node> nodeTree = new ArrayList<Node>();

		for(int i = 0; i< plateNodeList.size(); i++){	
			if(plateNodeList.get(i).getParentNodeId() == 0){
				nodeTree.add(plateNodeList.get(i));
			}
		}
		return nodeTree;

	}


	public Node select(int nodeId) {
		Node node =  nodeDao.select(nodeId);
		if(node != null){
			afterFetch(node);
		}
		return node;
	}

	@Override
	public Node select(String path, long ownerId){
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setPath(path);
		nodeCriteria.setOwnerId(ownerId);
		List<Node> nodeList = nodeDao.list(nodeCriteria);
		if(nodeList == null || nodeList.size() < 1){
			return null;
		}		
		Node node =  nodeList.get(0);
		if(node != null){
			afterFetch(node);
		}
		return node;		
	}

	@Override
	public Node select(String path, String siteCode, long ownerId){
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setPath(path);
		nodeCriteria.setSiteCode(siteCode);
		nodeCriteria.setOwnerId(ownerId);
		List<Node> nodeList = nodeDao.list(nodeCriteria);
		if(nodeList == null || nodeList.size() < 1){
			return null;
		}		
		Node node =  nodeList.get(0);
		if(node != null){
			afterFetch(node);
		}
		return node;		
	}

	public List<Node> list(NodeCriteria nodeCriteria) {
		List<Integer> idList = nodeDao.listPk(nodeCriteria);
		List<Node> nodeList =  new ArrayList<Node> ();		
		if(idList != null && idList.size() > 0){
			for(int i = 0; i < idList.size(); i++){
				Node node = nodeDao.select(idList.get(i));
				if(node != null){
					/*node.setId(node.getNodeId());
					node.setIndex(i+1);
					//检查是否具有子节点
					NodeCriteria subNodeCriteria = new NodeCriteria();
					subNodeCriteria.setParentNodeId(node.getNodeId());
					subNodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
					int subNodeCount = nodeDao.count(subNodeCriteria);
					if(subNodeCount > 0){
						node.setSubNodeList(new ArrayList<Node>());
					}*/
					nodeList.add(node);
				}
			}
			idList = null;
		}
		return nodeList;
		/*
		List<Node> nodeList =  nodeDao.list(nodeCriteria);
		for(int i = 0; i < nodeList.size(); i++){
			nodeList.get(i).setId(nodeList.get(i).getNodeId());
			nodeList.get(i).setIndex(i+1);
			//检查是否具有子节点
			NodeCriteria subNodeCriteria = new NodeCriteria();
			subNodeCriteria.setParentNodeId(nodeList.get(i).getNodeId());
			subNodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
			int subNodeCount = nodeDao.count(subNodeCriteria);
			if(subNodeCount > 0){
				nodeList.get(i).setSubNodeList(new ArrayList<Node>());
			}
		}
		return nodeList;
		 */
	}

	public List<Node> listOnPage(NodeCriteria nodeCriteria) {
		List<Integer> idList = nodeDao.listPkOnPage(nodeCriteria);
		if(idList == null || idList.size() < 1) {
			return Collections.emptyList();
		}
		List<Node> nodeList =  new ArrayList<Node> ();		
		for(int i = 0; i < idList.size(); i++){
			Node node = nodeDao.select(idList.get(i));
			if(node != null){
				nodeList.add(node);
			}
		}
		idList = null;
		return nodeList;

		/*
		List<Node> nodeList =  nodeDao.listOnPage(nodeCriteria);
		return nodeList;
		 */

	}

	public List<Node> listByUdid(int udid){
		List<Node> nodeList = nodeDao.listByUdid(udid);
		return nodeList;

	}




	//获取从默认节点[首页]开始一直到本节点的路径上的所有节点
	@Override
	public List<Node> getNodePath(int nodeId, long ownerId){
		Node node = select(nodeId);
		if(node == null){
			return null;
		}
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return null;
		}
		ArrayList<Node> pathNodes = new ArrayList<Node>();
		Node fatherNode = select(node.getParentNodeId());
		//logger.info("获取节点[" + node.getNodeId() + "]的路径节点");
		while(fatherNode != null){
			if(fatherNode.getOwnerId() != ownerId){
				logger.error("尝试获取的节点[" + node.getNodeId() + "]的父节点[" + fatherNode.getNodeId() + "，其ownerId[" + fatherNode.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
				return null;
			}
			pathNodes.add(fatherNode);
			//logger.info("查找节点[" + fatherNode.getNodeId() + "]的父节点:" + fatherNode.getParentNodeId());
			if(fatherNode.getParentNodeId() == 0){
				break;
			}
			fatherNode = select(fatherNode.getParentNodeId());

		}
		logger.info("共获取到了" + pathNodes.size() + "个路径节点");
		ArrayList<Node> sortedPathNodes = new ArrayList<Node>();
		//反转顺序
		for(int i = pathNodes.size()-1; i >= 0; i--){
			sortedPathNodes.add(pathNodes.get(i));
		}
		return sortedPathNodes;

	}
	//获取跟本节点同级别的、并且显示到导航上的节点
	public List<Node> getSameLevelNode(int nodeId, int nodeTypeId, boolean removeSelf){
		Node node = select(nodeId);
		if(node == null){
			return null;
		}
		logger.info("查找节点[" + node.getNodeId() + "/" + node.getParentNodeId() + "]类型为:" + nodeTypeId + " 的同级别节点");

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setParentNodeId(node.getParentNodeId());
		nodeCriteria.setOwnerId(node.getOwnerId());
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		if(nodeTypeId != 0){
			nodeCriteria.setNodeTypeId(nodeTypeId);
		}
		List<Node> sameLevelNodeList = list(nodeCriteria);

		if(sameLevelNodeList.contains(node)){
			logger.info("列表中包含自身");
		}
		if(removeSelf){
			for(int i = 0; i < sameLevelNodeList.size(); i++){
				if(sameLevelNodeList.get(i).getNodeId() == nodeId){
					sameLevelNodeList.remove(i);
				}
			}
		}
		return sameLevelNodeList;

	}

	//获取比本节点高一级、并且显示到导航上的节点
	public List<Node> getParentLevelNode(int nodeId, int nodeTypeId){
		Node node = select(nodeId);
		if(node == null){
			return null;
		}
		Node parentNode = select(node.getParentNodeId());
		if(parentNode == null){
			return null;
		}


		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setParentNodeId(parentNode.getParentNodeId());
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		nodeCriteria.setOwnerId(node.getOwnerId());
		if(nodeTypeId != 0){
			nodeCriteria.setNodeTypeId(nodeTypeId);
		}
		List<Node> parentLevelNodeList = list(nodeCriteria);

		return parentLevelNodeList;

	}

	/*
	 * 获取比本节点低一级、并且显示到导航上的节点
	 * 如果nodeTypeId != 0 则获取对应类型的node
	 */
	public List<Node> getChildrenLevelNode(int nodeId, int nodeTypeId){
		Node node = select(nodeId);
		if(node == null){
			return null;
		}

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setParentNodeId(node.getNodeId());
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		nodeCriteria.setOwnerId(node.getOwnerId());
		if(nodeTypeId != 0){
			nodeCriteria.setNodeTypeId(nodeTypeId);
		}
		List<Node> childrenLevelNodeList = list(nodeCriteria);

		return childrenLevelNodeList;

	}



	//列出所有(指定类型的)子节点，但跳过目录（即存在下级节点的节点）
	public void listAllChildren(List<Node> all, int  rootNodeId, int nodeTypeId){
		//logger.info("开始循环" + nodeId + "的所有子节点");
		List<Node> child = getChildrenLevelNode(rootNodeId, nodeTypeId);
		if(child == null){
			child = new ArrayList<Node>();
		}
		for(int i = 0; i< child.size(); i++){
			listAllChildren(all, child.get(i).getNodeId(),nodeTypeId);
			//logger.info("child::" + child.get(i).getNodeId());
			all.add(child.get(i));
		}


	}


	public int count(NodeCriteria nodeCriteria) {
		return nodeDao.count(nodeCriteria);
	}

	@Override
	public List<Node> listPrivilegedNodeByUuid(User user) {
		if(user == null){
			return null;
		}
		if(user.getRelatedPrivilegeList() == null || user.getRelatedPrivilegeList().size() < 1){
			return null;
		}

		//先获取所有的发布权限
		//String privilegeClass = DocumentFormController.class.getName();
		//String privilegeAction = "write";
		PrivilegeCriteria sysPrivilegeCriteria = new PrivilegeCriteria();
		sysPrivilegeCriteria.setOwnerId(user.getOwnerId());
		//FIXME sysPrivilegeCriteria.setPrivilegeClass(privilegeClass);
		//sysPrivilegeCriteria.setPrivilegeAction(privilegeAction);
		/*List<Privilege> sysPrivilegeList = sysPrivilegeService.list(sysPrivilegeCriteria);
		if(sysPrivilegeList == null || sysPrivilegeList.size() < 1){
			return null;
		}
		//与用户的权限比对
		List<Privilege> userPrivilege = new ArrayList<Privilege>();

		for(int i = 0; i < user.getRelatedPrivilegeList().size(); i++){
			for(int j = 0; j < sysPrivilegeList.size(); j++ ){
				if(user.getRelatedPrivilegeList().get(i).getPrivilegeId() == sysPrivilegeList.get(j).getPrivilegeId()){
					userPrivilege.add(sysPrivilegeList.get(j));
					break;
				}
			}
		}
		 */
		List<Integer> nodeIdList = new ArrayList<Integer>();
		/*FIXME for(int i = 0; i < userPrivilege.size(); i++){
			if(userPrivilege.get(i).getPrivilegeRelationList() == null || userPrivilege.get(i).getPrivilegeRelationList().size() < 1 ){
				continue;
			}
			for(int j = 0; j < userPrivilege.get(i).getPrivilegeRelationList().size(); j++){
				if(userPrivilege.get(i).getPrivilegeRelationList().get(j).getObjectTypeId() == Constants.ObjectType.node.toString()){
					try{
						nodeIdList.add(Integer.parseInt(userPrivilege.get(i).getPrivilegeRelationList().get(j).getObjectId()));
					}catch(Exception e){}
				}
			}
		}*/
		int nodeIdCount = nodeIdList.size();
		//去重
		HashSet<Integer> hs = new HashSet<Integer>(nodeIdList);
		nodeIdList.clear();
		nodeIdList.addAll(hs);
		logger.info("共得到用户发布权限相关的节点[" + nodeIdCount + "]个，去重后[" + nodeIdList.size() + "]个" );

		List<Node> userNodes = new ArrayList<Node>();
		for(int i = 0; i < nodeIdList.size(); i++){
			Node node = null;
			try{
				node = nodeDao.select(nodeIdList.get(i));
			}catch(Exception e){}
			if(node != null){
				userNodes.add(node);
			}

		}
		return userNodes;
	}

	public void generateNodePath(Node node){
		if(node == null){
			throw new RequiredObjectIsNullException("生成节点路径时的节点对象为空");
		}
		//写入根节点到当前节点的路径
		if(node.getParentNodeId() == 0){
			//node.setPath(node.getAlias());
			node.setPath("");
		} else {
			Node _parentNode = select(node.getParentNodeId());
			if(_parentNode == null){
				throw new RequiredObjectIsNullException("生成节点路径时的节点父节点对象为空");

			}
			//node.setPath(_parentNode.getPath() + "/" + node.getAlias());
			node.setPath((_parentNode.getPath() + "/" + node.getAlias()).replaceFirst("^\\./", "").replaceFirst("^/",""));
		}
	}

	@Override
	public List<Node> generateNavigation(Node rootNode){

		if(navigationNodeCache == null){
			navigationNodeCache = new HashMap<String, List<Node>>();
		}
		if(navigationNodeCache.size() < 1 || navigationNodeCache.get(rootNode.getSiteCode()) == null){
			NodeCriteria nodeCriteria = new NodeCriteria(rootNode.getOwnerId());
			nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
			if(rootNode.getNodeTypeId() > 0){
				nodeCriteria.setNodeTypeId(rootNode.getNodeTypeId());
			}
			List<Node> childrenLevelNodeList = list(nodeCriteria);
			navigationNodeCache.put(rootNode.getSiteCode(), childrenLevelNodeList);// this.generateTree(childrenLevelNodeList);
		} 
		if(navigationNodeCache.get(rootNode.getSiteCode()) == null){
			return null;
		} else {
			return navigationNodeCache.get(rootNode.getSiteCode());
		}
	}
	public void generateNodeLevel(Node node){
		if(node == null){
			throw new RequiredObjectIsNullException("生成节点级别时的节点对象为空");
		}
		if(node.getParentNodeId() == 0){
			//node.setLevel(2);
			if(".".equals(node.getAlias()))
				node.setLevel(0);
			else
				node.setLevel(2);

		} else {
			Node _parentNode = select(node.getParentNodeId());
			if(_parentNode == null){
				throw new RequiredObjectIsNullException("生成节点路径时的节点父节点对象为空");

			}
			node.setLevel(_parentNode.getLevel()+1);
		}
	}

	@Override
	public Node getDefaultNode(String siteCode, long ownerId) {
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setAlias(".");
		nodeCriteria.setSiteCode(siteCode);
		nodeCriteria.setOwnerId(ownerId);
		List<Node> nodeList = nodeDao.list(nodeCriteria);
		if(nodeList == null || nodeList.size() < 1){
			return null;
		}		
		Node node =  nodeList.get(0);
		if(node != null){
			afterFetch(node);
		}
		return node;		
	}



	private void afterFetch(Node node){
		//处理tags
		StringBuffer sb = new StringBuffer();

		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
		tagObjectRelationCriteria.setObjectType(ObjectType.node.toString());
		tagObjectRelationCriteria.setObjectId(node.getNodeId());
		List<TagObjectRelation> tagObjectRelationList = tagObjectRelationService.list(tagObjectRelationCriteria);
		if(tagObjectRelationList != null){
			for(TagObjectRelation tagObjectRelation : tagObjectRelationList){
				if(tagObjectRelation != null){
					Tag tag = tagService.select(tagObjectRelation.getTagId());
					if(tag != null){
						sb.append(tag.getTagName());
						sb.append(",");
					}
				}
			}
			try{sb.deleteCharAt(sb.length() - 1);}catch(Exception e){}
			node.setTags(sb.toString());

		}
	}

	@Override
	public List<Node> listOnPageClone(NodeCriteria nodeCriteria) {
		List<Node> nodeList = this.listOnPage(nodeCriteria);
		if(nodeList.size() <= 0) {
			return nodeList;
		}
		List<Node> list2 = new ArrayList<Node>(nodeList.size());
		for(Node node : nodeList) {
			list2.add(node.clone());
		}
		return list2;
	}




}

package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.SiteDomainRelationCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.NodeProcessorListNullException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.criteria.*;
import com.maicard.site.domain.IncludeNodeConfig;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Template;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.ContextType;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.NodeType;


@Controller
@RequestMapping("/node")
public class NodeController extends BaseController{


	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private NodeService nodeService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private TemplateService templateService;
	@Resource
	private AuthorizeService authorizeService;




	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, NodeCriteria nodeCriteria) throws Exception {
		/////////////////////// 标准流程开始 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		if (isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.node.name(), "w")) {
			map.put("isShow", "show" );
		}
		final String view = "common/node/list";
		nodeCriteria.setOwnerId(partner.getOwnerId());
		map.put("title", "栏目列表");

		int totalRows = nodeService.count(nodeCriteria);
		map.put("total", totalRows);

		if(totalRows < 1){
			logger.debug("当前返回的栏目数是0");
			return view;
		}

		List<Node> nodeList = nodeService.list(nodeCriteria);
		List<Node>nodeList2 = new ArrayList<Node>();
		if(nodeList == null || nodeList.size() < 1){
			logger.debug("当前没有任何栏目,ownerId=" + nodeCriteria.getOwnerId());
		} else {
			for(int i = 0; i < nodeList.size(); i++){
				Node n2 = nodeList.get(i).clone();
				
				Template template = templateService.select(n2.getTemplateId());
				if(template != null){
					n2.setTemplateLocation(template.getTemplateName() + "[" + template.getTemplateLocation() + "]");
				}
				nodeList2.add(n2);
			}
		}
		
		String validObjectIdList = authorizeService.listValidObjectId(partner, ObjectType.node.toString(), 0, Operate.relate.name().toString());
		ArrayList<Node> validNodeList = new ArrayList<Node>();
		if (validObjectIdList == null || validObjectIdList.equals("")) {
			logger.info("用户[" + partner.getUuid() + "]没有可发布的节点.");
		} else if (validObjectIdList.equals("*")) {
			logger.info("用户拥有所有节点的发布权限");
			validNodeList.addAll(nodeList);
		} else {
			String[] objectIdStringList = validObjectIdList.split(",");
			if (objectIdStringList == null || objectIdStringList.length < 1) {
				logger.error("无法解析返回的对象ID列表:" + validObjectIdList);
			} else {
				for (String id : objectIdStringList) {
					try {
						int nid = Integer.parseInt(id);
						for (Node node : nodeList) {
							if (node.getNodeId() == nid) {
								validNodeList.add(node);
								break;
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}
		logger.info("共有" + nodeList.size() + "个节点，有" + validNodeList.size() + "个有发布权限节点");
		List<Node> nodeTree = nodeService.generateTree(validNodeList);
		map.put("nodeTree", nodeTree);
		
		map.put("rows",nodeList2);
		if(nodeList != null){
			String nodeListString = "{\"rows\":" + JsonUtils.getInstance().writeValueAsString(nodeList) + "}";
			nodeListString = nodeListString.replaceAll("\"parentNodeId\"", "\"_parentId\"");
			map.put("tree", nodeListString);

		}

		return view;
	}

	/*@RequestMapping(method= RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, NodeCriteria nodeCriteria) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		nodeCriteria.setOwnerId(partner.getOwnerId());


		List<Node> nodeList = nodeService.listInTree(nodeCriteria);
		for(int i = 0; i < nodeList.size(); i++){
			if(nodeList.get(i).getOperate() == null){
				nodeList.get(i).setOperate(new HashMap<String,String>());
			}
			nodeList.get(i).getOperate().put("detail", "./node.do?mode=detail&nodeId=" + nodeList.get(i).getNodeId());
			nodeList.get(i).getOperate().put("add", "./nodeEdit.do?mode=write&parentNodeId=" + nodeList.get(i).getNodeId());
			nodeList.get(i).getOperate().put("remove", "./node.do?mode=delete&nodeId=" + nodeList.get(i).getNodeId());
			nodeList.get(i).getOperate().put("edit", "./nodeEdit.do?mode=edit&nodeId=" + nodeList.get(i).getNodeId());
			nodeList.get(i).getOperate().put("addDocument", "./documentEdit.do?mode=write&nodeId=" + nodeList.get(i).getNodeId());
		}

		if(nodeList != null){
			ObjectMapper mapper = new ObjectMapper();


			String nodeListString2 = mapper.writeValueAsString(nodeList);
			nodeListString2 = nodeListString2.replaceAll("name", "text");
			nodeListString2 = nodeListString2.replaceAll("subNodeList", "children");
			map.put("tree", nodeListString2);
		}

		return "common/node/list";
	}


	 */


	@RequestMapping(value="/get" + "/{nodeId}", method=RequestMethod.GET )			
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("nodeId") int nodeId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		Node node = nodeService.select(nodeId);
		if(node == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + nodeId + "的node对象");			
		}
		if(node.getOwnerId() != partner.getOwnerId()){
			throw new ObjectNotFoundByIdException("找不到ID=" + nodeId + "的node对象");			
		}
		map.put("node", node);
		return "node/get";
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)
	@AllowJsonOutput
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Node template = nodeService.select(deleteId);
			if(template == null){
				logger.warn("找不到要删除的节点，ID=" + deleteId);
				continue;
			}
			if(template.getOwnerId() != partner.getOwnerId() ){
				logger.warn("要删除的节点，ownerId[" + template.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(nodeService.delete(deleteId) > 0){
					successDeleteCount++;
				} 
			}catch(DataIntegrityViolationException forignKeyException ){
				String error  = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}

		}

		String messageContent = "成功删除[" + successDeleteCount + "]个.";
		if(!errors.equals("")){
			messageContent += errors;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));

		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response,ModelMap map) throws Exception {
		map.put("title", "新建栏目");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;		
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		int parentNodeId = ServletRequestUtils.getIntParameter(request, "parentNodeId", 0);
		Node parentNode = nodeService.select(parentNodeId);
		if(parentNode == null){
//			throw new ParentObjectNotFoundException("找不到对应的父节点:" + parentNodeId);
			logger.debug("找不到对应的父节点:" + parentNodeId);
		}else{
			if(parentNode.getOwnerId() != ownerId){
				throw new ParentObjectNotFoundException("找不到对应的父节点:" + parentNodeId);				
			}
			map.put("parentNodeLevel", parentNode.getLevel());
		}

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setOwnerId(ownerId);
		List<Node> nodeList = nodeService.list(nodeCriteria);
		
		String validObjectIdList = authorizeService.listValidObjectId(partner, ObjectType.node.toString(), 0, Operate.relate.name().toString());
		ArrayList<Node> validNodeList = new ArrayList<Node>();
		if (validObjectIdList == null || validObjectIdList.equals("")) {
			logger.info("用户[" + partner.getUuid() + "]没有可发布的节点.");
		} else if (validObjectIdList.equals("*")) {
			logger.info("用户拥有所有节点的发布权限");
			if(nodeList!=null){
				validNodeList.addAll(nodeList);
			}
		} else {
			String[] objectIdStringList = validObjectIdList.split(",");
			if (objectIdStringList == null || objectIdStringList.length < 1) {
				logger.error("无法解析返回的对象ID列表:" + validObjectIdList);
			} else {
				for (String id : objectIdStringList) {
					try {
						int nid = Integer.parseInt(id);
						for (Node node : nodeList) {
							if (node.getNodeId() == nid) {
								validNodeList.add(node);
								break;
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}
		List<Node> nodeTree = new ArrayList<Node>();
		if(nodeList!=null){
			logger.info("共有" + nodeList.size() + "个节点，有" + validNodeList.size() + "个有发布权限节点");
			nodeTree = nodeService.generateTree(validNodeList);
		}
		map.put("nodeTree", nodeTree);
		
		TemplateCriteria templateCriteria = new TemplateCriteria();
		templateCriteria.setOwnerId(ownerId);
		templateCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Template> templateList = templateService.list(templateCriteria);
		map.put("parentNodeId", parentNodeId);
		map.put("nodeList", nodeList);
		map.put("templateList", templateList);
		map.put("statusCodeList", BasicStatus.values());
		map.put("nodeType", NodeType.values());

		SiteDomainRelationCriteria siteDomainRelationCriteria = new SiteDomainRelationCriteria(ownerId);
		List<SiteDomainRelation> siteDomainRelationList = siteDomainRelationService.list(siteDomainRelationCriteria);
		logger.debug("当前ownerId[" + ownerId + "]拥有的站点对应关系是" + (siteDomainRelationList == null ? "空" : siteDomainRelationList.size()));
		List<String>siteCodeList = new ArrayList<String>();
		for(SiteDomainRelation siteDomainRelation : siteDomainRelationList){
			if(!siteCodeList.contains(siteDomainRelation.getSiteCode())){
				siteCodeList.add(siteDomainRelation.getSiteCode());
			}
		}
		map.put("siteCodeList", siteCodeList);

		Node node = null;

		node = new Node();
		node.setParentNodeId(parentNodeId);
		if(parentNode != null){
			node.setParentNodeName(parentNode.getName());
		}

		String[] nodeProcessorList = applicationContextService.getBeanNamesForType(NodeProcessor.class);
		logger.debug("当前系统中的节点处理器有:" + (nodeProcessorList == null ? "空" : nodeProcessorList.length));
		map.put("nodeProcessorList", nodeProcessorList);
		map.put("node", node);
		return "common/node/create";
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			Node node) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		
		
		node.setOwnerId(ownerId);

		if(node.getTemplateId() > 0){
			Template template = templateService.select(node.getTemplateId());
			if(template == null){
				logger.error("尝试添加的节点，其模版不存在:" + node.getTemplateId());
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"错误的模版"));
				return view;		
			}
			if(template.getOwnerId() != ownerId && template.getOwnerId() != 0){
				logger.error("尝试添加的节点，其模版不存在:" + node.getTemplateId());
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"错误的模版"));
				return view;		
			}
		} else {
			//必须选择一个模版
			logger.error("新增节点未选择模版");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请选择一个模版"));
			return view;		

		}
		if(node.getAlias() == null){
			logger.error("新增节点未提供别名代码");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请输入别名"));
			return view;		
		} else if(!".".equals(node.getAlias())&& !node.getAlias().matches("[A-Za-z0-9_]+")){
			logger.error("新增节点别名代码不合法:" + node.getAlias());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"别名不合法，只允许英文字母、数字和下划线"));
			return view;		
		}
		if(!".".equals(node.getAlias()) && node.getNodeTypeId() < 1){
			node.setNodeTypeId(NodeType.normal.getId());
		}
		EisMessage message = null;
		try{
			logger.info("在节点[" + node.getParentNodeId() + "]下新增节点[" + node.getName() + "],默认模版ID[" + node.getTemplateId() + "].路径path:"+node.getPath());
			if(nodeService.insert(node) > 0){
				message = new EisMessage(OperateResult.success.getId(), "节点添加成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "节点添加失败");				
			}

		}catch(Exception e){

			logger.error(ExceptionUtils.getFullStackTrace(e));
			String m = e.getMessage();
			if(m != null && m.indexOf("Duplicate entry") > 0){
				map.put("message", new EisMessage(EisError.dataDuplicate.id, "数据重复，请检查输入"));
				return view;		
			}
			map.put("message", new EisMessage(EisError.dataError.id, "无法新增栏目"));
			return view;	
		}
		map.put("message", message);
		return view;		
	}

	@RequestMapping(value="/update/{nodeId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@PathVariable("nodeId") int nodeId) throws Exception {
		map.put("title", "编辑栏目");
		long ownerId = 0;
		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}
		
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;		
		}

		Node node = nodeService.select(nodeId);
		if(node == null){
			throw new ParentObjectNotFoundException("找不到指定节点[" + nodeId + "]");				
		}
		if(node.getOwnerId() != ownerId){
			throw new ParentObjectNotFoundException("找不到指定节点[" + nodeId + "]");				
		}
		
		Node parentNode = null;
		if (node.getParentNodeId() == 0) {
			parentNode = node.clone();
		} else {
			parentNode = nodeService.select(node.getParentNodeId());
			
			if(parentNode == null){
				throw new ParentObjectNotFoundException("找不到对应的父节点:" + node.getParentNodeId());				
			} 
			if(parentNode.getOwnerId() != ownerId){
				throw new ParentObjectNotFoundException("找不到对应的父节点:" + node.getParentNodeId());				
			}
			map.put("parentNodeLevel", parentNode.getLevel());
		}
		

		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setOwnerId(ownerId);

		List<Node> nodeList = nodeService.list(nodeCriteria);
		TemplateCriteria templateCriteria = new TemplateCriteria();
		templateCriteria.setOwnerId(ownerId);
		templateCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Template> templateList = templateService.list(templateCriteria);
		
		String validObjectIdList = authorizeService.listValidObjectId(partner, ObjectType.node.toString(), 0, Operate.relate.name().toString());
		ArrayList<Node> validNodeList = new ArrayList<Node>();
		if (validObjectIdList == null || validObjectIdList.equals("")) {
			logger.info("用户[" + partner.getUuid() + "]没有可发布的节点.");
		} else if (validObjectIdList.equals("*")) {
			logger.info("用户拥有所有节点的发布权限");
			validNodeList.addAll(nodeList);
		} else {
			String[] objectIdStringList = validObjectIdList.split(",");
			if (objectIdStringList == null || objectIdStringList.length < 1) {
				logger.error("无法解析返回的对象ID列表:" + validObjectIdList);
			} else {
				for (String id : objectIdStringList) {
					try {
						int nid = Integer.parseInt(id);
						for (Node nodes : nodeList) {
							if (nodes.getNodeId() == nid) {
								validNodeList.add(nodes);
								break;
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}
		logger.info("共有" + nodeList.size() + "个节点，有" + validNodeList.size() + "个有发布权限节点");
		List<Node> nodeTree = nodeService.generateTree(validNodeList);
		map.put("nodeTree", nodeTree);
		
		map.put("nodeList", nodeList);
		map.put("templateList", templateList);
		map.put("statusCodeList", BasicStatus.values());
		map.put("nodeType", NodeType.values());
		String nodeProcessorConfig = null;

		nodeProcessorConfig = configService.getValue("siteNodeProcessorList", ownerId);
		if(nodeProcessorConfig == null){
			logger.error("系统配置中没有节点处理器列表。");
			throw new NodeProcessorListNullException("系统配置中没有节点处理器列表。");
		}
		String[] nodeProcessorArray= nodeProcessorConfig.split(",");
		if(nodeProcessorArray == null){
			logger.error("无法解析系统配置中的节点处理器列表");
			throw new NodeProcessorListNullException("无法解析系统配置中的节点处理器列表");
		}
		
		//处理程序
		String[] nodeProcessorList = applicationContextService.getBeanNamesForType(NodeProcessor.class);
		logger.debug("当前系统中的节点处理器有:" + (nodeProcessorList == null ? "空" : nodeProcessorList.length));
		map.put("nodeProcessorList", nodeProcessorList);
		//多站点代码[如不确定，请勿修改
		SiteDomainRelationCriteria siteDomainRelationCriteria = new SiteDomainRelationCriteria(ownerId);
		List<SiteDomainRelation> siteDomainRelationList = siteDomainRelationService.list(siteDomainRelationCriteria);
		logger.debug("当前ownerId[" + ownerId + "]拥有的站点对应关系是" + (siteDomainRelationList == null ? "空" : siteDomainRelationList.size()));
		List<String>siteCodeList = new ArrayList<String>();
		for(SiteDomainRelation siteDomainRelation : siteDomainRelationList){
			if(!siteCodeList.contains(siteDomainRelation.getSiteCode())){
				siteCodeList.add(siteDomainRelation.getSiteCode());
			}
		}
		map.put("siteCodeList", siteCodeList);
		ContextType[] contextTypeArr = ContextType.values();
		HashMap<String, String> contextType = new HashMap<String,String>();
		for (ContextType contextType2 : contextTypeArr) {
			contextType.put(contextType2.toString(), contextType2.toString());
		}
		map.put("contextType", contextTypeArr);
		map.put("nodeProcessorArray", nodeProcessorArray);
		Node node2 = node.clone();
		node2.setParentNodeName(parentNode.getName());
		map.put("node", node2);
		return "common/node/update";
	}

	
	@RequestMapping(value="/update", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("node") Node node) throws Exception {
		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId["
					+ partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
					"您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}

		boolean isPlatformGenericPartner = authorizeService
				.isPlatformGenericPartner(partner);
		// ////////////////////// 结束标准流程 ///////////////////////accessDenied
		
		if (!isPlatformGenericPartner) {
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(),"非内部用户,没有权限使用该功能"));
			return CommonStandard.partnerMessageView;
		}
		logger.info("节点提交");
		Set<IncludeNodeConfig> includeNodeSet = node.getIncludeNodeSet();
		
		
		node.setOwnerId(ownerId);
		logger.debug("要更改的处理器 : " + node.getProcessClass() + " /  节点 ：" + node.getNodeId());
		if(node.getTemplateId() > 0){
			Template template = templateService.select(node.getTemplateId());
			if(template == null){
				logger.error("尝试更新的节点，其模版不存在:" + node.getTemplateId());
				return CommonStandard.partnerMessageView;	
			}
			if(template.getOwnerId() != ownerId && template.getOwnerId() != 0){
				logger.error("尝试更新的节点，其模版不存在:" + node.getTemplateId());
				return CommonStandard.partnerMessageView;	
			}
		}
		
		String[] nodeIds = request.getParameterValues("includeNodeSet.nodeId");
		String[] contextTypes = request.getParameterValues("includeNodeSet.contentType");
		String[] rows = request.getParameterValues("includeNodeSet.rows");
		logger.debug("获取文章数量为"+Arrays.toString(rows)+"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		//Set<IncludeNodeConfig> set = new TreeSet<>();
		if (nodeIds != null) {
			if (nodeIds.length == contextTypes.length && contextTypes.length == rows.length) {
				for (int i = 0; i < nodeIds.length; i++) {
					IncludeNodeConfig includeNodeConfig = new IncludeNodeConfig();
					includeNodeConfig.setNodeId(Integer.parseInt(nodeIds[i]));
					if (!contextTypes[i].equals("0")) {
						includeNodeConfig.setContextType(contextTypes[i]);
					}
					includeNodeConfig.setRows(Integer.parseInt(rows[i]));
					includeNodeSet.add(includeNodeConfig);
				}
			}else {
				map.put("message", new EisMessage(EisError.conditionNotEnough.getId(), "选择的子栏目内容不规范"));
				return CommonStandard.partnerMessageView;	
			}
		}
		logger.debug("提交的节点是 ：" + includeNodeSet);
		//node.setIncludeNodeSet(set);
		//logger.debug(node.getIncludeNodeSet()+"");
		
		EisMessage message = null;
		try{			
			logger.info("更新节点[" + node.getNodeId() + "],父节点ID[" + node.getParentNodeId() + "],模版ID[" + node.getTemplateId() + "].");
			if(nodeService.update(node) > 0){
				message = new EisMessage(OperateResult.success.getId(), "节点修改成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "节点修改失败");				
			}

		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}


}

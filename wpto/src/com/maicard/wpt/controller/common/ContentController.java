	package com.maicard.wpt.controller.common;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.ContentPaging;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.criteria.*;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.SiteStandard;
import com.maicard.standard.SiteStandard.DocumentStatus;

/**
 * 内容控制器
 * 控制整个网站的页面展示内容
 * 
 * 
 * @author NetSnake
 * @date 2012-3-26
 */

@Controller
public class ContentController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;	
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private DocumentService documentService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private NodeService nodeService;	
	@Resource
	private CommentService commentService;
	
	private int defaultRowsPerPage = 10;



	//展示主页或二级主页
	@RequestMapping(value="/index", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = 0;
		logger.debug("进入content/index");
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		Node defaultNode = null;
		//判断二级域名
		String domain =  request.getServerName();
		String secondNodePath = null;

		//获取二级域名
		Pattern pattern = Pattern.compile("^(\\w+)\\.\\w+\\.\\S+$");
		Matcher matcher = pattern.matcher(domain);
		if(matcher.matches()){
			secondNodePath = (matcher.group(1));
			if(logger.isDebugEnabled()){
				logger.debug("尝试查找二级域名:" + secondNodePath);
			}
			if(secondNodePath == null || secondNodePath.equals("www")){

			} else {
				//String searchPath = "/" + SiteStandard.SitePath.contentPrefix.toString() + secondNodePath;
				NodeCriteria nodeCriteria = new NodeCriteria();
				nodeCriteria.setSiteCode(map.get("siteCode").toString());
				nodeCriteria.setPath(secondNodePath);
				nodeCriteria.setOwnerId(ownerId);
				/*if(logger.isDebugEnabled()){
					logger.debug("尝试查找二级域名的条件是path=" + searchPath);
				}*/
				nodeCriteria.setCurrentStatus(BasicStatus.normal.getId());
				List<Node>   nodeList = nodeService.list(nodeCriteria);
				if(nodeList != null && nodeList.size() > 0){
					defaultNode = nodeList.get(0);
				}
			}

		}
		if(defaultNode == null){
			defaultNode = nodeService.getDefaultNode(map.get("siteCode").toString(), ownerId);
		}
		if(defaultNode == null){
			logger.error("找不到[站点代码=" + map.get("siteCode").toString() + ",ownerId=" + ownerId + "]的默认首页节点");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		if(defaultNode.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + defaultNode.getNodeId() + "]，其ownerId[" + defaultNode.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		if(logger.isDebugEnabled()){
			logger.debug("主机名[" + secondNodePath + "]的默认节点是:" + defaultNode.getNodeId());
			logger.debug("节点的tags是:" + defaultNode.getTags());
		}
		NodeProcessor nodeProcessor = null;
		String nodeProcessorName = defaultNode.getProcessClass();
		try{
			//尝试通过node定义的处理类(bean)来获取节点的处理器
			nodeProcessor = (NodeProcessor)applicationContextService.getBean(nodeProcessorName);
		}catch(Exception e){}
		if(nodeProcessor == null){
			Object defaultNodeObj = map.get("defaultNodeProcessor");
			if(defaultNodeObj == null) {
				logger.warn("未能取得指定的处理类:" + nodeProcessorName + ",系统也没有配置默认节点处理器");
				map.put("message", new EisMessage(EisError.nodeProcessorIsNull.getId(),"系统异常","请尝试访问其他页面或返回首页"));
				return CommonStandard.frontMessageView;
			}
			nodeProcessorName = defaultNodeObj.toString();
			logger.warn("未能取得指定的处理类:" + defaultNode.getProcessClass() + ",尝试使用系统设置的默认节点处理器:" + nodeProcessorName);
			try{
				nodeProcessor = (NodeProcessor)applicationContextService.getBean(nodeProcessorName);
			}catch(Exception e){}
		}
		if(nodeProcessor == null){
			logger.error("无法加载节点处理器" + defaultNode.getProcessClass());
			map.put("message", new EisMessage(EisError.nodeProcessorIsNull.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}

		map.put("node", defaultNode);
		
		
		String view =  nodeProcessor.index(request, response, map);		
		return view;
	}


	//展示节点列表页,不带页码
	@RequestMapping(value="/content/**/index", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String nodePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		nodePath = nodePath.replaceAll("\\.\\w+$", "").replaceAll("/index$", "").replaceAll("^/" + SiteStandard.SitePath.contentPrefix, "");

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		Node node = nodeService.select(nodePath, map.get("siteCode").toString(), ownerId);
		logger.debug("处理节点路径[" + nodePath + "],获得的节点是[" + (node == null ? "空" : node.getNodeId()) + "]");

		if(node == null){
			logger.error("找不到[路径=" + nodePath + ",站点代码=" + map.get("siteCode").toString() + ",ownerId=" + ownerId + "]对应的节点");
			map.put("message", new EisMessage(EisError.nodeNotExist.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		if(node.getCurrentStatus() != BasicStatus.normal.getId() && node.getCurrentStatus() != BasicStatus.relation.getId()){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，状态不正常:" + node.getCurrentStatus());
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}

		NodeProcessor nodeProcessor = null;
		//尝试通过node定义的处理类(bean)来获取节点的处理器
		nodeProcessor = applicationContextService.getBeanGeneric(node.getProcessClass());
		if(nodeProcessor == null){
			logger.warn("未能取得指定的处理类:" + node.getProcessClass() + ",，尝试使用系统设置的默认节点处理器");
			nodeProcessor = applicationContextService.getBeanGeneric(map.get("defaultNodeProcessor").toString());
		}
		if(nodeProcessor == null){
			logger.error("无法加载节点处理器");
			map.put("message", new EisMessage(EisError.nodeProcessorIsNull.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		map.put("node", node);
		String view =  nodeProcessor.index(request, response, map);
		return view;
	}


	//展示节点列表页，带页码
	@RequestMapping(value="/content/**/index_{pageSetting}", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String listOnPage(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("pageSetting") String pageSetting) throws Exception {
		String nodePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		nodePath = nodePath.replaceAll("\\.\\w+$", "").replaceAll("/index_\\d+_\\d+$", "").replaceAll("/index_\\d+$", "").replaceAll("^/" + SiteStandard.SitePath.contentPrefix, "");

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		Node node = nodeService.select(nodePath, map.get("siteCode").toString(), ownerId);
		if(logger.isDebugEnabled()){
			logger.debug("处理节点路径[" + nodePath + "],获得的节点是[" + (node == null ? "空" : node.getTemplateId()) + "]");
		}
		if(node == null){
			logger.error("找不到[路径=" + nodePath + ",站点代码=" + map.get("siteCode").toString() + ",ownerId=" + ownerId + "]对应的节点");
			map.put("message", new EisMessage(EisError.nodeNotExist.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		if(node.getCurrentStatus() != BasicStatus.normal.getId() && node.getCurrentStatus() != BasicStatus.relation.getId()){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，状态不正常:" + node.getCurrentStatus());
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		NodeProcessor nodeProcessor = null;
		String nodeProcessorName = node.getProcessClass();
		try{
			//尝试通过node定义的处理类(bean)来获取节点的处理器
			nodeProcessor = (NodeProcessor)applicationContextService.getBean(nodeProcessorName);
		}catch(Exception e){}
		if(nodeProcessor == null){
			logger.warn("未能取得指定的处理类:" + nodeProcessorName + ",，尝试使用系统设置的默认节点处理器");
			try{
				nodeProcessorName = map.get("defaultNodeProcessor").toString();
				nodeProcessor = (NodeProcessor)applicationContextService.getBean(nodeProcessorName);
			}catch(Exception e){}
		}
		if(nodeProcessor == null){
			logger.error("无法加载节点处理器");
			map.put("message", new EisMessage(EisError.nodeProcessorIsNull.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		int rowsPerPage = 0;
		int currentPage = 0;
		if(pageSetting.matches("^\\d+_\\d+$")){
			String[] data = pageSetting.split("_");
			rowsPerPage = Integer.parseInt(data[0]);
			currentPage = Integer.parseInt(data[1]);
		} else 	if(pageSetting.matches("^\\d+$")){
			rowsPerPage = defaultRowsPerPage;
			currentPage = Integer.parseInt(pageSetting);
		} else {
			rowsPerPage = defaultRowsPerPage;
			currentPage = 1;		
		}
		map.put("node", node);

		map.put("currentPage", currentPage);
		map.put("rowsPerPage", rowsPerPage);
		if(logger.isDebugEnabled()){
			logger.debug("当前传递的分页参数是每页[" + rowsPerPage + "]行，当前第[" + currentPage + "]页");
		}
		String view =  nodeProcessor.index(request, response, map);
		return view;
	}


	//显示文档的内容页
	@RequestMapping(value="/content/**/{documentCode}", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,  
			@PathVariable("documentCode") String documentCode) throws Exception {
		String nodePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		nodePath = nodePath.replaceAll("\\.\\w+$", "").replaceAll( "/" + documentCode + "$", "").replaceAll("^/" + SiteStandard.SitePath.contentPrefix, "");

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		Node node = nodeService.select(nodePath,map.get("siteCode").toString(),ownerId);
		if(logger.isDebugEnabled()){
			logger.debug("处理文档[" + documentCode + ",获取到的节点路径[" + nodePath + "],根据路径获得的节点是[" + (node == null ? "空" : node.getNodeId()) + "]");
		}
		if(node == null){
			logger.error("找不到[路径=" + nodePath + ",站点代码=" + map.get("siteCode").toString() + ",ownerId=" + ownerId + "]对应的节点");
			map.put("message", new EisMessage(EisError.nodeNotExist.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		if(node.getCurrentStatus() != BasicStatus.normal.getId() && node.getCurrentStatus() != BasicStatus.relation.getId()){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，状态不正常:" + node.getCurrentStatus());
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"找不到链接对应的栏目","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		NodeProcessor nodeProcessor = null;
		String nodeProcessorName = node.getProcessClass();
		try{
			//尝试通过node定义的处理类(bean)来获取节点的处理器
			nodeProcessor = (NodeProcessor)applicationContextService.getBean(nodeProcessorName);
		}catch(Exception e){}
		if(nodeProcessor == null){
			logger.warn("未能取得指定的处理类:" + nodeProcessorName + ",，尝试使用系统设置的默认节点处理器");
			try{
				nodeProcessorName = map.get("defaultNodeProcessor").toString();
				nodeProcessor = (NodeProcessor)applicationContextService.getBean(nodeProcessorName);
			}catch(Exception e){}
		}
		if(nodeProcessor == null){
			logger.error("无法加载节点处理器:" + nodeProcessorName);
			map.put("message", new EisMessage(EisError.nodeProcessorIsNull.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		map.put("node", node);
		map.put("documentCode", documentCode);
		String view = nodeProcessor.detail(request, response, map);
		return view;
	}



	//显示文档时，只展示iframe中的文档内容
	@RequestMapping(value="/data/**/{documentCode}_{page}", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String data(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("documentCode") String documentCode, @PathVariable("page") Integer page) throws Exception {

		String nodePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		nodePath = nodePath.replaceAll("\\.\\w+$", "").replaceAll("/\\w+_\\d+$", "").replaceAll("^/" + SiteStandard.SitePath.contentPrefix, "");

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		Node node = nodeService.select(nodePath,map.get("siteCode").toString(),ownerId);
		if(logger.isDebugEnabled()){
			logger.debug("处理文档iframe内容页[" + documentCode + ",其节点路径[" + nodePath + "],获得的节点是[" + (node == null ? "空" : node.getTemplateId()) + "]");
		}
		if(node == null){
			return "redirect:/";			
		}

		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}
		String defaultTemplateLocation =null;
		try{
			defaultTemplateLocation = node.getTemplateLocation();
		}catch(NullPointerException e){
		}
		if(defaultTemplateLocation == null){
			logger.error("节点[" + node.getNodeId() + "]未定义默认模版");
			return "redirect:/";

		}
		defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");
		if(defaultTemplateLocation.endsWith("index")){
			defaultTemplateLocation = defaultTemplateLocation.replaceAll("index", "content");
		} else if(defaultTemplateLocation.endsWith("list")){
			defaultTemplateLocation = defaultTemplateLocation.replaceAll("list", "content");
		} else {
			logger.warn("模版不是以index或list结尾，无法应用到内容");
		}

		if(documentCode == null || documentCode.equals("")){
			logger.info("documentcode为空");
			return CommonStandard.frontMessageView;		
		}
		Document document = documentService.select(documentCode, ownerId);
		if(document == null){
			logger.info("按documentcode未能找到document");
			return CommonStandard.frontMessageView;	
		}
		if(document.getCurrentStatus() != DocumentStatus.published.getId()){
			logger.info("文档为未发布状态");
			return CommonStandard.frontMessageView;	
		}
		if(document.getOwnerId() != ownerId){
			logger.error("尝试获取的文档[" + document.getUdid() + "]，其ownerId[" + document.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}

		/*
		 * 处理手工分页
		 */
		String content = document.getContent();
		String[] documentContent = content.split(CommonStandard.documentSplitTag);
		int total = documentContent.length;
		if(page < 1){
			page =1;
		}
		if(page > total){
			page = total;
		}
		String pageContent = documentContent[page-1];
		document.setContent(pageContent);
		ContentPaging paging = new ContentPaging(0);
		paging.setTotalPage(total);
		paging.setCurrentPage(page);


		for(DocumentData dd : document.getDocumentDataMap().values()){
			if(	!authorizeService.havePrivilege(request, ObjectType.documentData.name(), dd)){
				dd = null;				
			}
		}	



		logger.info("paging:" + paging.getTotalPage());
		map.put("document", document);
		map.put("paging", paging);
		//map.put("hasMoreData",hasMoreData);
		return defaultTemplateLocation;		
	}


}

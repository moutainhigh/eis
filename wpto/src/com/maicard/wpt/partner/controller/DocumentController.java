package com.maicard.wpt.partner.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.base.UUIDFilenameGenerator;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Language;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.LanguageService;
import com.maicard.common.util.*;
import com.maicard.exception.*;
import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.domain.Route;
import com.maicard.flow.domain.WorkflowInstance;
import com.maicard.flow.service.WorkflowInstanceService;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.criteria.*;
import com.maicard.site.domain.DisplayType;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Template;
import com.maicard.site.service.DisplayTypeService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.StaticizeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DisplayLevel;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SiteStandard;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;


import static com.maicard.standard.CommonStandard.partnerMessageView;

@Controller
@RequestMapping("/document")
public class DocumentController extends BaseController {

	@Resource
	private ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private WorkflowInstanceService workflowInstanceService;
	@Resource
	private DisplayTypeService displayTypeService;
	@Resource
	private DocumentService documentService;
	@Resource
	private DocumentTypeService documentTypeService;
	@Resource
	private NodeService nodeService;
	@Resource
	private StaticizeService staticizeService;
	@Resource
	private TemplateService templateService;
	@Resource
	private LanguageService languageService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ProductService productService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;

	@Resource
	private PriceService priceService;

	private String documentUploadSaveDir;



	private int rowsPerPage = 10;

	private final int defaultLanguageId = 170001;

	private final String DEFAULT_ORDER = "a.create_time DESC";


	@PostConstruct
	public void init(){
		documentUploadSaveDir = applicationContextService.getDataDir();

		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}


	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			DocumentCriteria documentCriteria) throws Exception {

		final String view = "common/document/list";

		map.put("addUrl", "/document/create" + CommonStandard.DEFAULT_PAGE_SUFFIX);
		logger.debug("地址:"+map.get("addUrl"));
		map.put("title", "文章列表");
		// list操作属于只读操作，不涉及工作流，也不检查工作流

		String nodeStr = request.getParameter("nodeId");
		if (nodeStr != null && nodeStr != "0") {
			String[] objIds = nodeStr.split(",");
			documentCriteria.setNodePath(objIds);
		}


		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());

		logger.debug("后台登陆用户为" +partner+"@@@ zhangxin" );
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return partnerMessageView;
		}

		//得到文章所可访问有类型
		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setOwnerId(partner.getOwnerId());
		documentTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DocumentType> documentTypeLists = documentTypeService.list(documentTypeCriteria);
		String validObjectIdList = authorizeService.listValidObjectId(partner, ObjectType.documentType.toString(), 0, Operate.relate.name().toString());
		logger.debug("权限 ：  " + validObjectIdList.toString());
		ArrayList<DocumentType> validDocumentTypeList = new ArrayList<DocumentType>();

		if (validObjectIdList == null || validObjectIdList.equals("")) {
			logger.info("用户[" + partner.getUuid() + "]没有可访问的类型.");
		} else if (validObjectIdList.equals("*")) {
			logger.info("用户拥有所有文章类型的访问权限");
			validDocumentTypeList.addAll(documentTypeLists);
		} else {
			String[] objectIdStringList = validObjectIdList.split(",");
			logger.debug("列表 : " + objectIdStringList);
			if (objectIdStringList == null || objectIdStringList.length < 1) {
				logger.error("无法解析返回的对象ID列表:" + validObjectIdList);
			} else {
				for (String id : objectIdStringList) {
					logger.debug("id : " + id.toString());
					try {
						int nid = Integer.parseInt(id);
						for (DocumentType documentType : documentTypeLists) {
							if (documentType.getDocumentTypeId() == nid) {
								validDocumentTypeList.add(documentType);
								break;
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}
		logger.info("共有" + documentTypeLists.size() + "个文章类型，有" + validDocumentTypeList.size() + "个有访问权限类型");
		//		List<Node> nodeTree = nodeService.generateTree(validDocumentTypeList);
		map.put("documentTypeList",validDocumentTypeList);
		String mainSite = configService.getValue(DataName.mainSite.toString(), ownerId);
		/*if(manSite == null){
			mainSite = request.getRequestURI().split(regex)
		}*/
		DocumentStatus[] values = SiteStandard.DocumentStatus.values();
		map.put("documentStatusList", values);
		int[] arr = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			arr[i]=values[i].getId();
		}
		documentCriteria.setOwnerId(partner.getOwnerId());
		Integer currentStatus = ServletRequestUtils.getIntParameter(request, "currentStatus");
		if (currentStatus == null || currentStatus == 0) {
			documentCriteria.setCurrentStatus(arr);
		}

		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		logger.debug("查询的文档类型:" + documentCriteria.getDocumentTypeId());
		logger.debug("查询的文档状态:" + documentCriteria.getCurrentStatus());
		int totalRows = documentService.count(documentCriteria);
		map.put("total", totalRows);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回的文档数是0");
			return view;
		}

		String orderBy = ServletRequestUtils.getStringParameter(request, "order", DEFAULT_ORDER);
		documentCriteria.setOrderBy(orderBy);

		Paging paging = new Paging(rows);
		documentCriteria.setPaging(paging);
		documentCriteria.getPaging().setCurrentPage(page);
		logger.debug("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		// logger.info("documentTypeId from documentCriteria from request bind:"
		// + documentCriteria.getDocumentTypeId());
		List<Document> documentList = documentService.listOnPage(documentCriteria);
		// 执行过滤

		List<Document> documentList2 = new ArrayList<Document>();
		if (documentList != null) {
			int beforeCount = documentList.size();
			filter(partner, documentList,partner.getOwnerId());
			logger.debug("执行文档过滤，前后文档数量是:" + beforeCount + ":" + documentList.size());
			for (Document document : documentList) {
				Document document2 = document.clone();
				document2.setOperate(new HashMap<String, String>());
				//				document2.getOperate().put("get", "./document/" + "get" + "/" + document.getUdid());
				document2.getOperate().put("preview", "/document/preview/" + document.getUdid() + CommonStandard.DEFAULT_PAGE_SUFFIX);

				//				document2.getOperate().put("del", "./document/delete");
				//				document2.getOperate().put("update", "./document/update/" + document.getUdid());
				String viewUrl = document2.getViewUrl();
				if(viewUrl == null){
					viewUrl = document2.getDocumentCode();
				}
				if(mainSite != null){
					viewUrl = mainSite + viewUrl;
				}
				logger.debug("文章[" + document2.getUdid() + "]的链接地址是:" + viewUrl);
				document2.getOperate().put("url", viewUrl);

				documentList2.add(document2);

				logger.info("编辑文档时，尝试获取业务流实例[对象类型代码:" + ObjectType.document.toString() + ", 操作代码:" + Operate.update.name() + ",对象ID=" + partner.getUuid() + "].");
				//				查找当前对象update的工作流
				WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria();
				workflowInstanceCriteria.setOwnerId(partner.getOwnerId());
				workflowInstanceCriteria.setTargetObjectType(ObjectType.document.toString());
				workflowInstanceCriteria.setOperateCode(Operate.update.name());
				workflowInstanceCriteria.setUser(partner);
				workflowInstanceCriteria.setObjectId(document.getUdid());
				WorkflowInstance workflowInstanceUpdate = null;
				try {
					workflowInstanceUpdate = workflowInstanceService.getInstance(workflowInstanceCriteria);
				} catch (Exception e) {
					logger.error("找不到已存在实例[对象类型:" + workflowInstanceCriteria.getTargetObjectType() + ",对象ID:" + workflowInstanceCriteria.getObjectId() + ",ownerId=" + workflowInstanceCriteria.getOwnerId());
				}

				//	判断否需要工作流参与
				if (workflowInstanceUpdate == null) {
					logger.error("找不到工作流实例");
					document2.getOperate().put("update", "./document/update/" + document.getUdid());
				} else {
					if (workflowInstanceUpdate.getRouteList() == null || workflowInstanceUpdate.getRouteList().size() < 1) {
						logger.error("找不到工作流实例");
						document2.getOperate().put("update", "./document/update/" + document.getUdid());
					} else {
						logger.debug("找到了工作流实例[" + workflowInstanceUpdate.getWorkflowInstanceId() + "]");
						//	判断编辑权限
						if (document2.getDocumentTypeCode().equals(ObjectType.product.toString())) {
							document2.getOperate().put("update", "");
						} else {
							boolean canAccessWorkflowUpdate = workflowInstanceService.canAccess(workflowInstanceUpdate, partner);
							logger.debug("用户[" + partner.getUuid() + "]是否有权访问当前工作流实例[" + workflowInstanceUpdate + "]:" + canAccessWorkflowUpdate);
							if(!canAccessWorkflowUpdate){
								logger.error("您无权进行当前操作");
								document2.getOperate().put("update", "");
							} else {
								document2.getOperate().put("update", "./document/update/" + document.getUdid());
							}

						}
						//	得到当前工作步骤
						Route currentRouteUpdate = workflowInstanceUpdate.getCurrentRoute();
						if (currentRouteUpdate == null) {
							logger.error("当前步骤为空");

						}else {
							HashMap<String, Attribute> targetObjectAttributeMap = currentRouteUpdate.getTargetObjectAttributeMap();
							if (targetObjectAttributeMap == null) {
								logger.error("当前步骤中的TargetObjectAttributeMap为空！");
							} else {
								Attribute attribute = targetObjectAttributeMap.get("currentStatus");
								if (attribute == null) {
									logger.error("当前步骤中targetObjectAttributeMap的attribute为空！");
								} else {
									String[] validValue = attribute.getValidValue();
									for (String validValueStr : validValue) {
										logger.debug("当前步骤中的currentStatus ： " + validValueStr);
										int validValueTemp = 0;
										if (NumericUtils.isIntNumber(validValueStr)) {
											validValueTemp = Integer.parseInt(validValueStr);
										}
										//	判断是否有审批权限
										if (validValueTemp == DocumentStatus.depAccept.getId() && validValueTemp == document2.getCurrentStatus()) {
											boolean canAccessWorkflowUpdate = workflowInstanceService.canAccess(workflowInstanceUpdate, partner);
											logger.debug("用户[" + partner.getUuid() + "]是否有权访问当前工作流实例[" + workflowInstanceUpdate + "]:" + canAccessWorkflowUpdate);
											if(!canAccessWorkflowUpdate){
												logger.error("您无权进行当前操作");
												document2.getOperate().put(DocumentStatus.depAccept.name(), "");
											} else {
												document2.getOperate().put(DocumentStatus.depAccept.name(), "./document/update/status/" + document.getUdid());
												document2.setExtraValue("workflowInstanceId", workflowInstanceUpdate.getWorkflowInstanceId()+"");
											}
										}
										//	判断是否有发布权
										if (validValueTemp == DocumentStatus.published.getId() && validValueTemp == document2.getCurrentStatus()) {
											boolean canAccessWorkflowUpdate = workflowInstanceService.canAccess(workflowInstanceUpdate, partner);
											logger.debug("用户[" + partner.getUuid() + "]是否有权访问当前工作流实例[" + workflowInstanceUpdate + "]:" + canAccessWorkflowUpdate);
											if(!canAccessWorkflowUpdate){
												logger.error("您无权进行当前操作");
												document2.getOperate().put(DocumentStatus.published.name(), "");
											} else {
												document2.getOperate().put(DocumentStatus.published.name(), "./document/update/status/" + document.getUdid());
												document2.setExtraValue("workflowInstanceId", workflowInstanceUpdate.getWorkflowInstanceId()+"");
											}
										}
									}
								}
							}
						}
					}
				}

				//	是否有删除权限
				logger.debug("检查删除权限");
				List<Privilege> relatedPrivilegeList = partner.getRelatedPrivilegeList();
				if (relatedPrivilegeList == null || relatedPrivilegeList.size() < 1) {
					logger.error("用户没有权限");
					document2.getOperate().put("del", "");
				}
				logger.info("用户[" + partner.getUuid() + "]关联权限有" + relatedPrivilegeList.size() + "条.");
				for (Privilege privilege : relatedPrivilegeList) {
					if(privilege == null){
						continue;
					}
					logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]");
					if(privilege.getObjectTypeCode() == null || privilege.getObjectTypeCode().equals("")){
						logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作对象代码为空，跳过...");
						continue;
					}
					//权限与权限条件的对象类型代码一致
					if(privilege.getObjectTypeCode() == null || !privilege.getObjectTypeCode().equals(ObjectType.document.toString()) ){
						continue;
					}
					//logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]...");
					if(privilege.getOperateCode() == null){
						//logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作码为空");
						continue;
					}
					if (privilege.getOperateCode().equals("*") || privilege.getOperateCode().equals("w")) {
						//如果权限objectList为空，跳过这次比对
						if(privilege.getObjectList() == null || privilege.getObjectList().equals("")){
							//	logger.info("权限[" + privilege.getOperateCode() + "]的对象列表为空...");
							continue;
						}

						String objects[] = privilege.getObjectList().split(",");
						//如果无法获取或解析匹配对象列表，跳过这次比对
						if(objects == null || objects.length == 0){
							//	logger.info("权限[" + privilege.getOperateCode() + "]的对象列表无法解析:" + privilege.getObjectList());
							continue;
						}
						logger.info("拥有权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]");
						document2.getOperate().put("del", "./document/delete");
						break;
					}
				}

				logger.debug("相关特权  ：" + relatedPrivilegeList + " 相关角色 : " + partner.getRelatedRoleList() + "  用户关系 : " + partner.getUserRelationList());
			}
		}
		//		是否有添加权限
		int	documentTypeId = 0;
		logger.info("创建新文档时，尝试获取业务流实例[对象类型代码:" + ObjectType.document.toString() + ", 操作代码:" + Operate.create.name() + "].");
		WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria(0, ObjectType.document.name(), documentTypeId, Operate.create.name(), partner);
		WorkflowInstance workflowInstanceCreate = null;
		//try {
			workflowInstanceCreate = workflowInstanceService.getInstance(workflowInstanceCriteria);
		//} catch (Exception e) {
			// TODO: handle exception
		//}

		if (workflowInstanceCreate == null) {
			logger.error("找不到工作流实例");
			map.put("addUrl", "/document/create" + CommonStandard.DEFAULT_PAGE_SUFFIX);
		} else {
			if (workflowInstanceCreate.getRouteList() == null || workflowInstanceCreate.getRouteList().size() < 1) {
				logger.error("找不到工作流实例");
				map.put("addUrl", "/document/create" + CommonStandard.DEFAULT_PAGE_SUFFIX);
			} else {
				logger.debug("找到了工作流实例[" + workflowInstanceCreate.getWorkflowInstanceId() + "]");
				boolean canAccessWorkflowCreate = workflowInstanceService.canAccess(workflowInstanceCreate, partner);
				logger.debug("用户[" + partner.getUuid() + "]是否有权访问当前工作流实例[" + workflowInstanceCreate + "]:" + canAccessWorkflowCreate);
				if(!canAccessWorkflowCreate){
					logger.error("您无权进行当前操作");
					map.put("addUrl", "");
				} else {
					map.put("addUrl", "/document/create" + CommonStandard.DEFAULT_PAGE_SUFFIX);
				}
			}
		}

		if (documentList2 != null) {

			map.put("rows", documentList2);
		}


		List<DocumentType> documentTypeList = documentTypeLists;
		logger.debug("共得到[" + (documentTypeList == null ? -1 : documentTypeList.size()) + "]种文档类型");

		map.put("documentTypeList", documentTypeList);
		UserCriteria userCriteria = new UserCriteria();
		List<User> partnerList = partnerService.list(userCriteria);
		map.put("partnerList", partnerList);
		return view;
	}

	@RequestMapping(value = "/get" + "/{udid}")
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("documentCriteria") DocumentCriteria documentCriteria,
			@PathVariable("udid") Integer udid) throws Exception {
		// String methodName =
		// Thread.currentThread().getStackTrace()[Constants.jvmStackTraceOffset].getMethodName();
		if (udid == 0) {
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "没有指定要获取的文档ID[udid==0]"));
			return partnerMessageView;
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Document document = null;
		try {
			document = documentService.select(udid);
		} catch (Exception e) {
			logger.error("在查找文档[" + udid + "]时发生异常:" + e.getMessage());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "在查找文档[" + udid + "]时发生异常:" + e.getMessage()));
			return partnerMessageView;
		}
		if (document == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "没有找到指定的文档[udid==" + udid + "]"));
			return partnerMessageView;
		}
		if(document.getOwnerId() != partner.getOwnerId()){
			logger.error("文档[" + document.getTemplateId() + "]的ownerId[" + document.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + udid + "的Document对象");			
		}
		Document document2 = document.clone();
		//documentService.processDataPath(document2);
		if (document2.getRelatedNodeList() == null || document.getRelatedNodeList().size() < 1) {
			logger.info("该文档未定义任何相关发布节点");
		}
		User publisherName=partnerService.select(document.getPublisherId());
		map.put("document", document2);
		map.put("publisherName", publisherName);

		return "common/document/get";

	}

	@RequestMapping(value = "/preview/{udid}")
	public String getPreview(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("udid") Integer udid) throws Exception {
		if (udid == 0) {
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "没有指定要获取的文档ID[udid==0]"));
			return partnerMessageView;
		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			return null;
		}
		String previewUrl = null;
		previewUrl = configService.getValue(DataName.mainSite.toString(), ownerId);

		if (previewUrl == null) {
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "系统配置异常，未找到预览URL或系统域名"));
			return partnerMessageView;
		}
		Document document = null;
		try {
			document = documentService.select(udid);
		} catch (Exception e) {
			logger.error("在查找文档[" + udid + "]时发生异常:" + e.getMessage());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "在查找文档[" + udid + "]时发生异常:" + e.getMessage()));
			return partnerMessageView;
		}
		if (document == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "没有找到指定的文档[udid==" + udid + "]"));
			return partnerMessageView;
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(document.getOwnerId() != partner.getOwnerId()){
			logger.error("文档[" + document.getTemplateId() + "]的ownerId[" + document.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + udid + "的Document对象");			
		}
		Document document2 = document.clone();


		Crypt crypt = new Crypt();
		crypt.setAesKey(CommonStandard.previewKey);

		String previewSign = PreviewToken.generate(document.getDocumentCode());
		previewUrl = previewUrl +  document2.getViewUrl() + "?preview="	+ previewSign;
		logger.debug("预览地址:" + previewUrl);
		response.sendRedirect(previewUrl);
		return  null;

	}

	// 批量删除
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@AllowJsonOutput
	public String deleteList(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		String[] ids = idList.split("-");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		int successDeleteCount = 0;
		String errors = "";
		for (int i = 0; i < ids.length; i++) {
			int deleteId = Integer.parseInt(ids[i]);
			Document document = documentService.select(deleteId);
			if(document == null){
				logger.warn("找不到要删除的文档，ID=" + deleteId);
				continue;
			}
			document.setParam(request);

			if(document.getOwnerId() != partner.getOwnerId()){
				logger.warn("要删除的文档，ownerId[" + document.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try {
				int udid = Integer.parseInt(ids[i]);
				if (documentService.delete(udid) > 0) {
					successDeleteCount++;
					/*
					 * if(configService.getBooleanValue(DataName.siteStaticize.
					 * toString())){
					 * staticizeJob.start(ObjectType.product.toString(), udid);
					 * }
					 */
				}
			} catch (DataIntegrityViolationException forignKeyException) {
				String error = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}

		}

		String messageContent = "成功删除[" + successDeleteCount + "]个.";
		if (!errors.equals("")) {
			messageContent += errors;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(), messageContent));
		return partnerMessageView;

	}

	// 创建新文档的界面
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map, DocumentCriteria documentCriteria) throws Exception {

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		logger.debug("后台登陆用户为" +partner+"@@@ zhangxin" );
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}
		map.put("title","新建文档");

		// 得到所有的文档类型
		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setOwnerId(partner.getOwnerId());
		documentTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DocumentType> documentTypeList = documentTypeService.list(documentTypeCriteria);
		map.put("documentTypeList", documentTypeList);

		DocumentType documentType = null;

		int documentTypeId = ServletRequestUtils.getIntParameter(request, "documentTypeId", 0);
		if (documentTypeId > 0) {
			for(DocumentType dt : documentTypeList) {
				if(dt.getDocumentTypeId() == documentTypeId) {
					map.put("documentType", dt);
					documentType = dt;
					if(dt.getLongExtraValue("isProduct") == 1) {
						writePriceList(null,map,partner.getOwnerId());
					}
					break;
				}
			}
		}





		// 得到所有可用的节点
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setOwnerId(partner.getOwnerId());
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId(),BasicStatus.relation.getId(), BasicStatus.hidden.getId());
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
		logger.info("共有" + nodeList.size() + "个节点，有" + validNodeList.size() + "个有发布权限节点");
		List<Node> nodeTree = nodeService.generateTree(validNodeList);

		// 得到所有可用的语言
		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setOwnerId(partner.getOwnerId());
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Language> languageList = languageService.list(languageCriteria);
		// logger.info("语言数量：" + languageList.size());

		// 得到所有的模版
		TemplateCriteria templateCriteria = new TemplateCriteria();
		templateCriteria.setOwnerId(partner.getOwnerId());
		templateCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Template> templateList = templateService.list(templateCriteria);
		map.put("templateList", templateList);

		// 得到所有的显示类型
		DisplayTypeCriteria displayTypeCriteria = new DisplayTypeCriteria();
		displayTypeCriteria.setOwnerId(partner.getOwnerId());
		displayTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DisplayType> displayTypeList = displayTypeService.list(displayTypeCriteria);




		// 得到所有的状态码，状态码自身没有状态，所以不需要指定状态条件
		// map.put("documentColumnList", displayType.getDocumentColumnList());
		map.put("displayTypeList", displayTypeList);
		//		map.put("documentTypeList", documentTypeList2);
		map.put("nodeList", nodeList);
		map.put("nodeTree", nodeTree);
		map.put("validNodeList", validNodeList);
		map.put("statusCodeList", DocumentStatus.values());
		map.put("languageList", languageList);
		map.put("documentCriteria", documentCriteria);

		//		int	documentTypeId = 0;

		logger.info("创建新文档时，尝试获取业务流实例[对象类型代码:" + ObjectType.document.toString() + ", 操作代码:" + Operate.create.name() + "].");
		WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria(0, ObjectType.document.name(), documentTypeId, Operate.create.name(), partner);

		WorkflowInstance workflowInstance = workflowInstanceService.getInstance(workflowInstanceCriteria);
		if (workflowInstance == null) {
			logger.error("找不到工作流实例");
			map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(), "找不到针对对象[" + ObjectType.document.toString() + "]进行[" + Operate.create.name() + "]操作的工作流"));
			return partnerMessageView;
		}
		if (workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1) {
			logger.error("找不到工作流实例");
			map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(), "针对对象[" + ObjectType.document.toString() + "的工作流，其工作步骤为空"));
			return partnerMessageView;
		}
		map.put("workflowInstanceId", workflowInstance.getWorkflowInstanceId());
		//map.put("validCurrentStatus", workflowInstanceService.getValidCurrentStatus(workflowInstance));

		Document document = new Document();
		document.setDocumentTypeId(documentTypeId);
		document.setDocumentCode(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

		Map<String,DataDefine> dataDefineMap = null;
		if(documentType != null){
			dataDefineMap = documentType.getDataDefineMap();
		}
		logger.debug("创建文档使用" +  workflowInstance.getWorkflowInstanceId() + "#工作流实例，文档类型:" + documentType.getDocumentTypeId() + "对应的数据定义是:" + dataDefineMap);
		
		Map<String, Attribute> validAttributeMap = new HashMap<String, Attribute>();
		validAttributeMap = workflowInstanceService.getValidInputAttribute(document, workflowInstance, dataDefineMap);
		map.put("validAttributeMap",validAttributeMap);


		map.put("currentRoute", workflowInstance.getRouteList().get(0));
		map.put("document", document);
		return "common/document/create";
	}
	//	列出活动产品的编码
	@RequestMapping(value = "/list/product", method = RequestMethod.GET)
	public String getProductCodeList(HttpServletRequest request, HttpServletResponse response, ModelMap map, ProductCriteria productCriteria)throws Exception{
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		String documentTypeIdStr = request.getParameter("documentTypeId");
		int documentTypeId = 0;
		if (NumericUtils.isIntNumber(documentTypeIdStr)) {
			documentTypeId = Integer.parseInt(documentTypeIdStr);
		}


		//		得到文档所有状态码
		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setOwnerId(partner.getOwnerId());
		documentTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DocumentType> documentTypeList = documentTypeService.list(documentTypeCriteria);
		if (documentTypeList == null || documentTypeList.size() < 1) {
			logger.error("所有文档状态码为空");
		} else {
			DocumentType documentType = documentTypeService.select(documentTypeId);
			if (documentType == null) {
				logger.error("没找到活动文档类型");
				map.put("message", new EisMessage(EisError.dataError.getId(), "没找到活动文档类型[" + documentTypeId + "]"));
				return CommonStandard.partnerMessageView;
			}

			int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
			int page = ServletRequestUtils.getIntParameter(request, "page", 1);

			//			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria(partner.getOwnerId());
			//
			//			tagObjectRelationCriteria.setObjectType(ObjectType.product.name());
			//			List<Tag> tagList = tagObjectRelationService.listTags(tagObjectRelationCriteria);

			//			logger.debug("与产品关联的标签数量是:" + (tagList == null ? "空" : tagList.size()));
			//			map.put("tagList", tagList);

			map.put("title", "产品列表");
			productCriteria.setOwnerId(partner.getOwnerId());
			int totalRows = productService.count(productCriteria);
			Paging paging = new Paging(rows);
			productCriteria.setPaging(paging);
			productCriteria.getPaging().setCurrentPage(page);
			logger.debug("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");

			map.put("total", totalRows);
			if(totalRows < 1){
				logger.debug("当前查询的产品数量为0");
				//				return view;
			}
			//计算并放入分页
			map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

			List<Product> productList = productService.listOnPage(productCriteria);
			if (productList == null || productList.size() < 1) {
			} else {
				String order = productCriteria.getOrderBy();
				if(order != null){
					//尝试排序
					String[] orderData = order.split("\\s+");
					String sortField = orderData[0];
					String sortOrder = null;
					if(orderData.length > 1){
						sortOrder = orderData[1];
					}
					logger.debug("以字段[" + sortField + "]进行" + sortOrder + "排序");
					//productService.sort(productList, sortField, sortOrder);
					SortUtils.sort(productList, sortField, sortOrder);

				}
			}

			map.put("rows", productList);


			/*if (!documentTypeList.contains(documentType)) {
				logger.error("活动产品列表为空");
				map.put("message", new EisMessage(EisError.dataError.getId(), "活动产品列表为空"));
				return CommonStandard.partnerMessageView;
			}
			DocumentCriteria documentCriteria = new DocumentCriteria();
			documentCriteria.setOwnerId(partner.getOwnerId());
			documentCriteria.setDocumentTypeId(documentTypeId);
			List<Document> documentLists = documentService.list(documentCriteria);
			if (documentLists == null || documentLists.size() < 1) {
				logger.error("活动产品列表为空");
			} else {
				ArrayList<Product> productList = new ArrayList<Product>();
				for (Document document : documentLists) {
					HashMap<String, DocumentData> documentDataMap = document.getDocumentDataMap();
					logger.debug("活动产品的扩展数据" + documentDataMap);
					Set<Entry<String, DocumentData>> entrySet = documentDataMap.entrySet();
					Iterator<Entry<String, DocumentData>> iterator = entrySet.iterator();
					while (iterator.hasNext()) {
						Entry<String, DocumentData> next = iterator.next();
						ProductCriteria productCriteria = new ProductCriteria();
						productCriteria.setOwnerId(partner.getOwnerId());
						productCriteria.setProductCode(next.getValue().getDataValue());
						List<Product> productLists = productService.list(productCriteria);
						if (productLists == null || productLists.size() < 1) {
							logger.error("活动产品列表为空");
						} else {
							for (Product product : productLists) {
								productList.add(product);
							}
						}

					}
				}
				logger.debug("活动产品列表中有[" + productList.size() + "]个活动产品");
				map.put("productList", productList);
			}*/
		}


		return CommonStandard.partnerMessageView;
	}

	/*
	 * 对文档列表进行过滤 例如只保留自己发布的和自己负责节点的文档
	 */
	private void filter(User publisher, List<Document> documentList, long ownerId) {
		if (documentList == null) {
			return;
		}
		int superGroupId = 800003;
		try {
			superGroupId = Integer.parseInt(configService.getValue("super_user_group_id", ownerId));
		} catch (Exception e) {
		}
		if (superGroupId == 0) {
			superGroupId = 800003;
		}
		if (publisher == null) {
			documentList = null;
		}
		if (publisher.getRelatedRoleList() == null || publisher.getRelatedRoleList().size() < 1) {
			documentList = null;
		}
		for (Role role : publisher.getRelatedRoleList()) {
			if (role.getRoleId() == superGroupId) {
				return;
			}
		}
		boolean excludeOtherPublisherDocument = false;
		try {
			excludeOtherPublisherDocument = Boolean.parseBoolean(configService.getValue("site.excludeOtherPublisherDocument",ownerId));
		} catch (Exception e) {
		}
		List<Document> documentList2=new ArrayList<Document>();
		logger.debug("excludeOtherPublisherDocument的状态为"+excludeOtherPublisherDocument);
		if (excludeOtherPublisherDocument) {
			for (Document doc:documentList) {
				if (doc.getPublisherId() == publisher.getUuid()) {
					documentList2.add(doc);
					//logger.debug("保留"+doc.getTitle());
				}
				else{
					//logger.debug("文档"+doc.getTitle()+"的发布者是"+doc.getPublisherId()+"我是"+publisher.getUuid()+"所以---保留");
				}
			}
			documentList.clear();
			documentList.addAll(documentList2);
		}

		boolean excludeUnprivilegeNode = false;
		try {
			excludeUnprivilegeNode = Boolean.parseBoolean(configService.getValue("site.excludeUnprivilegeNode",ownerId));
		} catch (Exception e) {
		}
		if (excludeUnprivilegeNode) {
			// 得到该用户拥有发布权限的所有节点
			List<Node> userNodeList = nodeService.listPrivilegedNodeByUuid(publisher);
			if (userNodeList == null || userNodeList.size() < 1) {
				documentList = null;
				return;
			}
			for (int i = 0; i < documentList.size(); i++) {
				for (int j = 0; j < userNodeList.size(); j++) {
					if (documentList.get(i).getDefaultNode() == null || documentList.get(i).getDefaultNode() != userNodeList.get(j)) {
						documentList.remove(i);
					}
				}
			}

		}

	}

	// 更新文档的界面
	@RequestMapping(value = "/update/{udid}", method = RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("udid") int udid) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		map.put("title", "编辑文档");
		Document document = documentService.select(udid);
		if (document == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "文档不存在"));
			return partnerMessageView;
		}
		Document document2 = document.clone();

		// 得到所有可用的节点
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId(),BasicStatus.relation.getId(), BasicStatus.hidden.getId());
		List<Node> nodeList = nodeService.list(nodeCriteria);
		String validObjectIdList = authorizeService.listValidObjectId(partner, ObjectType.node.toString(), 0, Operate.relate.name());
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

		// 得到所有可用的语言
		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setOwnerId(partner.getOwnerId());
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Language> languageList = languageService.list(languageCriteria);
		// logger.info("语言数量：" + languageList.size());

		// 得到所有的模版
		TemplateCriteria templateCriteria = new TemplateCriteria();
		templateCriteria.setOwnerId(partner.getOwnerId());
		templateCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Template> templateList = templateService.list(templateCriteria);
		map.put("templateList", templateList);

		// 得到所有的显示类型
		DisplayTypeCriteria displayTypeCriteria = new DisplayTypeCriteria();
		displayTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DisplayType> displayTypeList = displayTypeService.list(displayTypeCriteria);

		DocumentType documentType = documentTypeService.select(document.getDocumentTypeId());
		if(documentType == null){
			logger.error("找不到文档[" + document.getUdid() + "]对应的文档类型:" + document.getDocumentTypeId());

			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到文档[" + document.getUdid() + "]对应的文档类型:" + document.getDocumentTypeId()));
			return partnerMessageView;
		}
		map.put("documentType", documentType);
		if(documentType.getLongExtraValue("isProduct") == 1) {

			writePriceList(document,map,partner.getOwnerId());


		}
		List<Node> nodeTree = nodeService.generateTree(validNodeList);

		map.put("nodeTree", nodeTree);

		// 得到所有的状态码，状态码自身没有状态，所以不需要指定状态条件
		// map.put("documentColumnList", displayType.getDocumentColumnList());
		map.put("displayTypeList", displayTypeList);
		map.put("nodeList", nodeList);
		map.put("validNodeList", validNodeList);
		map.put("statusCodeList", DocumentStatus.values());
		map.put("languageList", languageList);

		/*document.setDocumentCode(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));*/


		WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria(document.getUdid(), ObjectType.document.name(), document.getDocumentTypeId(), Operate.update.name(), partner);
		//		WorkflowInstance workflowInstance = workflowInstanceService.getInstance(workflowInstanceCriteria);
		WorkflowInstance workflowInstance = null;
		try {
			workflowInstance = workflowInstanceService.getInstance(workflowInstanceCriteria);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("找不到已存在实例[对象类型:" + workflowInstanceCriteria.getTargetObjectType() + ",对象ID:" + workflowInstanceCriteria.getObjectId() + ",ownerId=" + workflowInstanceCriteria.getOwnerId());
		}

		if (workflowInstance == null) {
			logger.debug("找不到编辑文档的工作流实例，或不需要使用工作流");

		} else {
			if (workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1) {
				logger.error("针对对象[" + ObjectType.product.toString()	+ "]的工作流，其工作步骤为空");
				map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(),"工作流数据异常"						));
				return partnerMessageView;
			}
			logger.debug("编辑文档使用" +  workflowInstance.getWorkflowInstanceId() + "#工作流实例，当前步骤:" + workflowInstance.getCurrentStep());

			map.put("workflowInstanceId", workflowInstance.getWorkflowInstanceId());
			Map<String,DataDefine> dataDefineMap = null;
			if(documentType != null){
				dataDefineMap = documentType.getDataDefineMap();
			}
			if(workflowInstance.getCurrentRoute() != null){
				String postProcess = workflowInstance.getCurrentRoute().getPostProcess();
				if(NumericUtils.isNumeric(postProcess)){
					int post = NumericUtils.parseInt(postProcess);
					logger.debug("当前操作模式postProcess=" + post);

				}
			} else {
				logger.error("当前工作流[" + workflowInstance.getWorkflowInstanceId() + "]没有当前步骤");
			}
			Map<String, Attribute> validAttributeMap = new HashMap<String, Attribute>();
			validAttributeMap = workflowInstanceService.getValidInputAttribute(document, workflowInstance, dataDefineMap);
			map.put("validAttributeMap",validAttributeMap);

			boolean canAccessWorkflow = workflowInstanceService.canAccess(workflowInstance, partner);
			logger.debug("用户[" + partner.getUuid() + "]是否有权访问当前工作流实例[" + workflowInstance + "]:" + canAccessWorkflow);
			if(!canAccessWorkflow){
				map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"您无权进行当前操作"));
				return partnerMessageView;
			}

			List<Route> routeList=workflowInstance.getNextRouteList();
			map.put("workflowInstanceId", workflowInstance.getWorkflowInstanceId());
			//	map.put("validCurrentStatus", workflowInstanceService.getValidCurrentStatus(workflowInstance));
			map.put("routeList", routeList);

		}

		//User publisherName=partnerService.select(document.getPublisherId());


		map.put("document", document2);
		//logger.debug("需要的documentTypeId"+document2.getDisplayTypeId());
		//map.put("publisherName", publisherName);
		return "common/document/update";
	}


	/**
	 * 对于商品文档，获取当前文档的所有价格，或者是根据系统配置产生一组新的价格
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-23
	 */
	private void writePriceList(Document document, ModelMap map, long ownerId) {
		List<Price> priceList = new ArrayList<Price>();
		if(document != null) {
			PriceCriteria priceCriteria = new PriceCriteria(ownerId);
			priceCriteria.setObjectType(ObjectType.document.name());
			priceCriteria.setObjectId(document.getUdid());
			priceList = priceService.list(priceCriteria);
		}
		String availablePriceType = configService.getValue(DataName.availablePriceType.name(), ownerId);
		logger.debug("当前文档类型是产品文档，获取系统配置的可用价格类型:{}", availablePriceType);
		if(StringUtils.isBlank(availablePriceType)) {
			availablePriceType = PriceType.PRICE_STANDARD.name();
		}
		String[] priceTypeList = availablePriceType.split(",");
		for(String priceType : priceTypeList) {
			boolean exist = false;
			for(Price price : priceList) {
				if(price.getPriceType().equalsIgnoreCase(priceType)) {
					exist = true;
					break;
				}
			}
			if(!exist) {
				Price price = new Price();
				price.setPriceType(priceType);
				priceList.add(price);
			}
		}
		
		
		map.put("priceList",priceList);
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, Document document) throws Exception {

		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			return null;
		}
		/*
		 * 通过模块处理的对象类型和状态，来得到其是否处于工作流中 如果处于工作流中，则找出其工作流的工作步骤
		 */
		User partner = null;
		try {
			partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		} catch (Exception e) {
			throw e;
		}
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}
		document.setOwnerId(partner.getOwnerId());
		document.setPublisherId(partner.getUuid());
		if(document.getLanguageId() == 0){
			document.setLanguageId(CommonStandard.defaultLanguageId);
		}
		int defaultNodeId = ServletRequestUtils.getIntParameter(request, "defaultNodeId",0);
		if (defaultNodeId == 0) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数defaultNodeId");
		}
		EisMessage message = null;

		// 获取同步显示的发布节点
		int[] nodeIds = ServletRequestUtils.getIntParameters(request, "nodeId");
		ArrayList<Node> relatedNodeList = new ArrayList<Node>();
		if (nodeIds != null) {
			if (nodeIds.length > 0) {
				relatedNodeList = new ArrayList<Node>();
				for (int i = 0; i < nodeIds.length; i++) {

					Node node = nodeService.select(nodeIds[i]);

					if (node == null) {
						logger.error("找不到指定的发布节点[" + nodeIds[i] + "]");
						continue;
					}
					if(node.getOwnerId() != partner.getOwnerId()){
						logger.error("指定的发布节点[" + node.getNodeId() + "]的ownerId[" + node.getOwnerId() + "]与系统用户的不一致[" + partner.getOwnerId());
						continue;
					}
					//检查用户是否拥有写入该节点的权限

					boolean havePrivilegeToNode = haveRelatePrivilegeToNode(partner, node.getNodeId(), Operate.relate.name());

					if(!havePrivilegeToNode){
						logger.error("用户没有权限在栏目[" + node.getNodeId() + "]上发布文章");
						map.put("message", new EisMessage(EisError.ACCESS_DENY.id, "您无权在栏目[" + node.getName() + "]上发布文章"));
						return partnerMessageView;
					}
					if (nodeIds[i] == defaultNodeId) {
						node.setCurrentStatus(BasicStatus.relation.getId());
					}

					relatedNodeList.add(node);
				}
			}
		}

		Node defaultNode = nodeService.select(defaultNodeId);
		if (defaultNode == null) {
			String msg = "文档[udid=" + document.getUdid() + "] 所选择的默认发布节点为空！";
			logger.error(msg);
			throw new RequiredParameterIsNullException(msg);
		}
		if(defaultNode.getOwnerId() != partner.getOwnerId()){
			logger.error("指定的默认发布节点[" + defaultNode.getNodeId() + "]的ownerId[" + defaultNode.getOwnerId() + "]与系统用户的不一致[" + partner.getOwnerId());
			throw new RequiredParameterIsNullException("选择的默认发布节点为空");
		}
		boolean havePrivilegeToNode = haveRelatePrivilegeToNode(partner, defaultNode.getNodeId(), Operate.relate.name());
		if(!havePrivilegeToNode){
			logger.error("用户没有权限在栏目[" + defaultNode.getNodeId() + "]上发布文章");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id, "您无权在栏目[" + defaultNode.getName() + "]上发布文章"));
			return partnerMessageView;
		}
		defaultNode.setCurrentStatus(BasicStatus.relation.getId());
		document.setDefaultNode(defaultNode);
		boolean defaultExist = false;
		for (int i = 0; i < relatedNodeList.size(); i++) {
			if (relatedNodeList.get(i).getNodeId() == defaultNodeId) {
				relatedNodeList.get(i).setCurrentStatus(BasicStatus.relation.getId());
				defaultExist = true;
				break;
			}
		}
		if (!defaultExist) {
			relatedNodeList.add(defaultNode);
		}
		document.setRelatedNodeList(relatedNodeList);
		Object workflowResult = workflowInstanceService.executeWorkflow(document, null, request, Operate.create.name(), ownerId);
		WorkflowInstance workflowInstance = null;
		if(workflowResult instanceof WorkflowInstance){
			workflowInstance = (WorkflowInstance)workflowResult;
		} else if(workflowResult instanceof EisMessage){
			EisMessage workflowMsg = (EisMessage)workflowResult;
			logger.error("无法完成创建文档的工作流:" + workflowMsg.getOperateCode() + "=> " + workflowMsg.getMessage());
			map.put("message", workflowMsg);
			return partnerMessageView;

		} else {
			logger.debug("创建文档不需要工作流参与,把文档设置为已发布状态");
			document.setCurrentStatus(CommonStandard.documentDefaultStatus);
		}
		String tags = document.getTags();
		if(tags == null){
			tags = "";
		}
		// XXX 创建文档肯定为宽松模式，可以编辑所有对象，除了指定对象必须变为指定值

		DocumentType documentType = documentTypeService.select(document.getDocumentTypeId());
		if(documentType == null){
			logger.error("找不到指定的文档类型:" + document.getDocumentTypeId());
			map.put("message",  new EisMessage(EisError.typeError.id,"找不到指定的文档类型:" + document.getDocumentTypeId()));
			return partnerMessageView;
		}
		if(documentType.getBooleanExtraValue("removeHtml")){
			String oldContent = document.getContent();
			document.setContent(Jsoup.clean(oldContent, Whitelist.none()));
			logger.debug("文档类型[" + documentType.getDocumentTypeId() + "]要求清除文档内容中的HTML标签,把原始内容[" + oldContent + "]修改为[" + document.getContent());

		}

		if (documentType.getDataDefineMap() == null || documentType.getDataDefineMap().size() == 0) {
			logger.info("当前文档类型[" + document.getDocumentTypeId() + "]没有自定义字段.");
		} else {

			String autoTagConfig = configService.getValue(DataName.autoTagList.toString(), partner.getOwnerId());
			logger.info("当前系统定义的自动tag列表(autoTagList)是:" + autoTagConfig);
			Set<String> autoTagList = new HashSet<String>();
			if(autoTagConfig != null){
				String[] data = autoTagConfig.split(",");
				if(data != null && data.length > 0){
					for(String d : data){
						autoTagList.add(d);
					}
				}
			}

			/*String tagTagConfigString = configService.getValue(DataName.tagtagConfig.toString(), partner.getOwnerId());
			Map<String,String> tagTagConfigMap = new HashMap<String,String>();
			if(tagTagConfigString != null){
				try{
					tagTagConfigMap = JsonUtils.getInstance().readValue(tagTagConfigString, new TypeReference<HashMap<String,String>>(){});
				}catch(Exception e){
					e.printStackTrace();
				}
			}*/


			logger.debug("当前文章类型有[" + documentType.getDataDefineMap().size() + "]个自定义数据规范");
			if (request instanceof MultipartHttpServletRequest) {
				logger.info("请求中带有附件，使用文件上传处理.");
				this.fileUpload(request, "create", document);
			}
			for (DataDefine dataDefine : documentType.getDataDefineMap().values()) {
				logger.debug("尝试获取数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据");
				String documentDataStr = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
				if (documentDataStr == null || documentDataStr.equals("")) {
					logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]没有提交数据");
					continue;
				}
				logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]提交的数据是[" + documentDataStr + "]");
				if(autoTagList.contains(dataDefine.getDataCode())){
					tags += documentDataStr;
					tags += " ";
				}
				/*if(tagTagConfigMap != null && tagTagConfigMap.size() > 0){
					//处理标签的标签
					for(String sourceTagName : tagTagConfigMap.keySet()){
						if(sourceTagName.equals(documentDataStr)){
							logger.debug("把扩展数据[" + dataDefine.getDataCode() + "]放入标签的标签源:" + documentDataStr);
							tagForTagMap.put(documentDataStr, null);
						}
					}
				}*/
				DocumentData documentData = new DocumentData();
				documentData.setOwnerId(partner.getOwnerId());
				documentData.setDataDefineId(dataDefine.getDataDefineId());
				documentData.setDataCode(dataDefine.getDataCode());
				documentData.setCurrentStatus(BasicStatus.normal.getId());
				documentData.setDataValue(documentDataStr);
				if (document.getDocumentDataMap() == null) {
					document.setDocumentDataMap(new HashMap<String,DocumentData>());
				}
				logger.debug("尝试插入自定义文档数据[" + documentData.getDataCode() + "/" + documentData.getDataDefineId() + "]，数据内容:[" 	+ documentData.getDataValue() + "]");
				document.getDocumentDataMap().put(documentData.getDataCode(), documentData);
			}
			/*if(tagForTagMap != null && tagForTagMap.size() > 0){
				for(String key : tagForTagMap.keySet()){
					String valueCode = tagTagConfigMap.get(key);
					if(valueCode == null){
						logger.debug("找不到标签的标签源[" + key + "]对应的目标标签定义");
						continue;						
					}
					String value = document.getExtraValue(valueCode);
					if(value == null){
						value = ClassUtils.getValue(document, valueCode,  CommonStandard.COLUMN_TYPE_NATIVE);

					}
					if(value == null){
						logger.debug("找到标签的标签源[" + key + "]对应的目标标签值:" + value);
						tagForTagMap.put(key,value);
					} else {

						logger.debug("找不到标签的标签源[" + key + "]对应的目标标签值");
					}

				}
			}*/
		}

		document.setTags(tags.replaceAll(",$", "").trim());
		if(documentType != null && documentType.getDocumentTypeCode().equals(ObjectType.activity.name())){
			boolean createActivitySuccess = activityService.syncActivityByDocument(document);
			if(!createActivitySuccess){
				logger.error("该文档是一个活动文档，但是创建对应活动失败");
				map.put("message", new EisMessage(OperateResult.failed.getId(), "无法创建对应活动"));
				return partnerMessageView;
			}
		}
		document.setParam(request);
		int insertRs = 0;
		try {
			insertRs = documentService.insert(document) ;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			map.put("message", new EisMessage(OperateResult.failed.getId(), "系统数据更新异常"));

		}	
		if (insertRs != 1) {
			message = new EisMessage(OperateResult.failed.getId(), "添加失败");

			map.put("message", message);
			return partnerMessageView;
		}



		document.setId(document.getUdid());

		
		tagObjectRelationService.processTagForTag(document);

		if(workflowInstance != null){
			logger.debug("添加文档成功，更新工作流实例[" + workflowInstance.getWorkflowInstanceId() + "，设置其objectId=" + document.getUdid());
			workflowInstance.setObjectId(document.getUdid());
			workflowInstanceService.update(workflowInstance);
		}

		boolean requireStaticize = document.getBooleanExtraValue(DataName.requireStaticize.toString());
		if(requireStaticize){
			staticizeService.staticize(document, CommonStandard.WAIT_DATA_SYNC_SEC);
		}

		message = new EisMessage(OperateResult.success.getId(), "添加成功");

		map.put("udid",document.getUdid());

		map.put("message", message);
		return partnerMessageView;

	}










	// 文件上传
	@SuppressWarnings("rawtypes")
	private int fileUpload(HttpServletRequest request, String mode, Document document) throws Exception {

		// 从Spring容器中获取对应的上传文件目录
		// String fileUploadSavePath =
		// ((FileSystemResource)this.getApplicationContext().getBean("uploadSaveDir")).getPath();
		// logger.info("Spring容器中的上传文件目录在:" + fileUploadSavePath);
		// logger.info("尝试为文档[udid="+udid+"]在[" + mode + "]模式下上传附件");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		// 获得文件： 
		Map<String, MultipartFile> map = multiRequest.getFileMap();
		logger.debug("上传的文件 ：" + map);
		logger.info("上传文件数量:" + (map == null ? "空" : multiRequest.getQueryString()));
		// 获得文件名： 
		Iterator its = multiRequest.getFileNames();
		logger.debug("DocumentDataMap() :" + (document.getDocumentDataMap() == null ? "空" : document.getDocumentDataMap()));
		if (document.getDocumentDataMap() == null) {
			document.setDocumentDataMap(new HashMap<String, DocumentData>());
		}
		int i = 0;
		int addCount = 0;
		int updateCount = 0;
		int ignoreCount = 0;
		while (its.hasNext()) {
			CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile((String) its.next());
			logger.debug("处理上传文件:" + file.getName() + ",大小:" + file.getSize());
			if (file.getSize() == 0) {
				if (i == 0) {
					continue;
				}
				break;
			}

			String key = file.getName();
			logger.debug("key : " + key);
			String simpleKey = key.replaceAll("\\d+$", "");
			logger.debug("simpleKey : " + simpleKey);

			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setDataCode(simpleKey);
			dataDefineCriteria.setObjectType(ObjectType.document.toString());
			dataDefineCriteria.setObjectId(document.getDocumentTypeId());
			DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
			// DocumentColumn documentColumn =
			// documentColumnService.select(file.getName());
			// DocumentTypeColumnRelation dataDefine =
			// (DocumentTypeColumnRelation)it.next();
			if (dataDefine == null) {// 不支持的上传文件名
				dataDefineCriteria.setDataCode(key);
				dataDefine = dataDefineService.select(dataDefineCriteria);
				if(dataDefine == null){
					logger.debug("找不到名字叫[" + simpleKey + "]或者[" + key + "]的数据定义");
					ignoreCount++;
					break;
				}
			}
			if(dataDefine.getDisplayLevel() == null){
				logger.warn("数据定义[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]未定义显示级别，使用默认匿名级别");
				dataDefine.setDisplayLevel(DisplayLevel.open.toString());
			}
			/*
			 * 如果当前为编辑模式,则查找是否有对应的已存在数据 如果有已存在的数据并且本次要上传,则直接更新对应的文件
			 */
			//保存路径
			DocumentData documentData = new DocumentData();
			String fileUploadSavePath = null;
			StringBuffer sb = new StringBuffer();
			sb.append(documentUploadSaveDir).append(File.separator);
			String fileDisplayPath = null;
			if(dataDefine.getDisplayLevel().equals(DisplayLevel.subscriber.toString())){
				fileDisplayPath = CommonStandard.EXTRA_DATA_SUBSCRIBE_PATH;
			} else if(dataDefine.getDisplayLevel().equals(DisplayLevel.login.toString())){
				fileDisplayPath = CommonStandard.EXTRA_DATA_LOGIN_PATH;
			} else {
				fileDisplayPath = CommonStandard.EXTRA_DATA_OPEN_PATH;
			}
			fileUploadSavePath = sb.toString();
			String fileName = fileDisplayPath + File.separator + UUIDFilenameGenerator.generateWithDatePath(file.getOriginalFilename());
			String fileDest = "";
			//			DocumentData existDocumentData = null;
			//			if (mode.equals("update") && document.getDocumentDataMap() != null) {
			//				for (DocumentData d2 : document.getDocumentDataMap().values()) {
			//					if (d2.getDataDefineId() == dataDefine.getDataDefineId()) {
			//						existDocumentData = d2;
			//						break;
			//					}
			//				}
			//				if (existDocumentData != null) {
			//					fileDest = fileUploadSavePath + File.separator + existDocumentData.getDataValue();
			//					File _oldFile = new File(fileDest);
			//					_oldFile.delete();
			//					updateCount++;
			//				} else {
			//					fileDest = fileUploadSavePath + File.separator + fileName;
			//				}
			//
			//			} else {
			//				fileDest = fileUploadSavePath + File.separator + fileName;
			//
			//			}
			fileDest = fileUploadSavePath + File.separator + fileName;
			logger.info("documentUploadSaveDir:" + documentUploadSaveDir + ",fileUploadSavePath:" + fileUploadSavePath + ",fileName:" + fileName);
			File destDir = new File(fileDest).getParentFile();
			if(!destDir.exists()){
				logger.info("目标目录不存在，创建:" + fileUploadSavePath);
				FileUtils.forceMkdir(destDir);
			}
			documentData.setDataDefineId(dataDefine.getDataDefineId());
			documentData.setDataCode(dataDefine.getDataCode());
			documentData.setOwnerId(document.getOwnerId());
			logger.info("保存数据文件[" + file.getOriginalFilename() + "]到:" + fileDest);
			File dest = new File(fileDest);
			//保存  
			file.transferTo(dest);
			if(key.equals(simpleKey)){
				logger.debug("将单独的附件文件[" + key + "]保存到:" + fileName);
				documentData.setDataValue(fileName);
			} else {
				if(document.getExtraValue(simpleKey) == null){
					documentData.setDataValue(fileName);
				} else {
					documentData = document.getDocumentDataMap().get(simpleKey);
					String fileList = documentData.getDataValue();
					fileList += ",";
					fileList += fileName;
					documentData.setDataValue(fileList);
					logger.debug("保存集合文件[" + simpleKey + "]，文件列表:" + fileList);
				}
			}

			logger.debug("code : " + documentData.getDataCode() + "  |  documentData  : " + documentData);
			document.getDocumentDataMap().put(documentData.getDataCode(), documentData);
			addCount++;

		}
		int totalAffected = addCount + updateCount;
		String message = "完成附件上传,新增 " + addCount + " 个,更新 " + updateCount + " 个, 跳过 " + ignoreCount + " 个。";
		logger.info(message);
		return totalAffected;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			Document document) throws Exception {
		long ownerId = 0;

		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		User partner = null;
		try {
			partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		} catch (Exception e) {
			throw e;
		}
		EisMessage message = null;
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}

		if (document.getUdid() < 1) {
			map.put("message", new EisMessage(EisError.dataError.getId(), "错误的Product对象"));
			return partnerMessageView;
		}
		logger.debug("文档 :" + document);
		Document _oldDocument = documentService.select(document.getUdid());
		logger.debug("老文档"+_oldDocument);
		if (_oldDocument == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "文档不存在"));
			return partnerMessageView;
		}
		//XXX 出现一个重复添加doucmentCode的问题，原因未知，只能先强行使用老文档的documentCode, NetSnake,2018-12-13
		document.setDocumentCode(_oldDocument.getDocumentCode());
		if (_oldDocument.getOwnerId() != partner.getOwnerId()) {
			map.put("message",		new EisMessage(EisError.OBJECT_IS_NULL.getId(), "文档不存在"));
			return partnerMessageView;
		}
		document.setPublisherId(partner.getUuid());
		int defaultNodeId = ServletRequestUtils.getIntParameter(request, "defaultNodeId",0);
		if (defaultNodeId == 0) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数defaultNodeId");
		}
		// logger.error("defaultNodeId:" + defaultNodeId);

		// 获取所有的自定义字段数据
		// @update 通过dataDefine代替documentColumn
		if(document.getDocumentDataMap() == null){
			document.setDocumentDataMap(new HashMap<String,DocumentData>());
		}
		DocumentType documentType = documentTypeService.select(document.getDocumentTypeId());
		if(documentType == null){
			logger.error("找不到指定的文档类型:" + document.getDocumentTypeId());
			map.put("message",  new EisMessage(EisError.typeError.id,"找不到指定的文档类型:" + document.getDocumentTypeId()));
			return partnerMessageView;
		}
		if(documentType.getFlag() == 1){
			String oldContent = document.getContent();
			document.setContent(Jsoup.clean(oldContent, Whitelist.none()));
			logger.debug("文档类型[" + documentType.getDocumentTypeId() + "]要求清除文档内容中的HTML标签,把原始内容[" + oldContent + "]修改为[" + document.getContent());

		}
		if (documentType.getDataDefineMap() == null || documentType.getDataDefineMap().size() == 0) {
			logger.info("当前文档类型[" + document.getDocumentTypeId() + "]没有自定义字段.");
		} else {
			logger.debug("当前文章类型有[" + documentType.getDataDefineMap().size() + "]个自定义数据规范");
			if (request instanceof MultipartHttpServletRequest) {
				logger.info("请求中带有附件，使用文件上传处理.");
				this.fileUpload(request, Operate.update.name(), document);
			}
			for(DataDefine dataDefine: documentType.getDataDefineMap().values()){
				if(dataDefine.getDataCode() == null){
					logger.error("错误的dataDefine对象，dataCode为空:" + dataDefine);
					throw new Exception("错误的dataDefine对象，dataCode为空:" + dataDefine);
				}

				logger.debug("尝试获取数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据");
				if (document.getDocumentDataMap().get(dataDefine.getDataCode()) != null) {
					//可能已经由文件上传处理
					logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据已存在，可能是由文件上传处理");
					continue;
				}
				String documentDataStr = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
				logger.info("检查提交请求中可能的扩展数据:" + dataDefine.getDataCode() + ":" + documentDataStr);
				if (documentDataStr == null || documentDataStr.equals("")) {
					logger.info("未提交扩展数据:" + dataDefine.getDataCode() );
					logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]没有提交数据");
					if(dataDefine.getInputMethod() != null && dataDefine.getInputMethod().equals("file")){
						logger.info("未提交扩展数据:" + dataDefine.getDataCode() + "但其数据输入为file，使用旧数据替代" );
						String oldValue = _oldDocument.getExtraValue(dataDefine.getDataCode());
						if(StringUtils.isNotBlank(oldValue)){
							document.setExtraValue(dataDefine.getDataCode(), oldValue);
							logger.debug("在旧文档对象中找到了file类型的数据[" + dataDefine.getDataCode() + "=>" + oldValue + "]，放入");		
						} else {
							logger.debug("在旧文档对象中没有找到了file类型的数据：" + dataDefine.getDataCode());
						}
					} else {
						logger.info("未提交扩展数据:" + dataDefine.getDataCode() + "并且其数据输入不是file，跳过" );
						continue;
					}
				} else {
					boolean exist = false;
					if(_oldDocument.getDocumentDataMap() != null){
						DocumentData dd = _oldDocument.getDocumentDataMap().get(dataDefine.getDataCode());
						logger.info("dd={}", JSON.toJSONString(dd));
						if(dd != null &&  dd.getDataValue() != null && dd.getDataValue().equals(documentDataStr)){
							logger.debug("输入的数据[" + dataDefine.getDataCode() + "=>" + documentDataStr + "]已存在");
							document.getDocumentDataMap().put(dataDefine.getDataCode(), dd);
							exist = true;
						}
					}
					if(!exist){
						if (documentDataStr.equals("DELETE")) {
							documentDataStr="";
						}
						document.setExtraValue(dataDefine.getDataCode(), documentDataStr);
						logger.debug("向文档中放入新的输入数据[" + dataDefine.getDataCode() + "=>" + documentDataStr + "]");
					}
				}/*
				DocumentData documentData = new DocumentData();
				documentData.setDataDefineId(dataDefine.getDataDefineId());
				documentData.setCurrentStatus(BasicStatus.normal.getId());
				documentData.setUdid(document.getUdid());
				documentData.setDataValue(documentDataStr);
				if (document.getDocumentDataMap() == null) {
					document.setDocumentDataMap(new HashMap<String,DocumentData>());
				}
				document.getDocumentDataMap().put(documentData.getDataCode(), documentData);*/
			}
			logger.debug("修改数据 ：" + document.getDocumentDataMap());
		}

		// 获取发布节点
		int[] nodeIds = ServletRequestUtils.getIntParameters(request, "nodeId");
		ArrayList<Node> relatedNodeList = new ArrayList<Node>();
		if (nodeIds != null) {
			if (nodeIds.length > 0) {
				relatedNodeList = new ArrayList<Node>();
				for (int i = 0; i < nodeIds.length; i++) {

					Node node = nodeService.select(nodeIds[i]);

					if (node == null) {
						continue;
					}
					if (nodeIds[i] == defaultNodeId) {
						node.setCurrentStatus(BasicStatus.relation.getId());
					}
					relatedNodeList.add(node);
				}
			}
		}

		Node defaultNode = nodeService.select(defaultNodeId);
		if (defaultNode == null) {
			String msg = "文档[udid=" + document.getUdid() + "] 所选择的默认发布节点为空！";
			logger.error(msg);
			throw new RequiredParameterIsNullException(msg);
		}
		defaultNode.setCurrentStatus(BasicStatus.relation.getId());
		document.setDefaultNode(defaultNode);

		boolean defaultExist = false;
		for (int i = 0; i < relatedNodeList.size(); i++) {
			if (relatedNodeList.get(i).getNodeId() == defaultNodeId) {
				relatedNodeList.get(i).setCurrentStatus(BasicStatus.relation.getId());
				defaultExist = true;
				break;
			}
		}
		if (!defaultExist) {
			relatedNodeList.add(defaultNode);
		}
		document.setRelatedNodeList(relatedNodeList);
		document.setLanguageId(defaultLanguageId);
		document.setOwnerId(_oldDocument.getOwnerId());
		Object workflowResult = workflowInstanceService.executeWorkflow(document, _oldDocument, request, Operate.update.name(), ownerId);

		WorkflowInstance workflowInstance = null;
		if(workflowResult instanceof WorkflowInstance){
			workflowInstance = (WorkflowInstance)workflowResult;
		} else if(workflowResult instanceof EisMessage){
			EisMessage workflowMsg = (EisMessage)workflowResult;
			logger.error("无法完成编辑文档的工作流:" + workflowMsg.getOperateCode() + "=> " + workflowMsg.getMessage());
			map.put("message", workflowMsg);
			return partnerMessageView;
		}
		logger.debug("分割点 ：" + document.getTags());
		
		document.setPublishTime(new Date());
		document.setParam(request);

		try {
			/*logger.info("当前文章的类型为 ：" + documentType.getDocumentTypeCode() + "/" + documentType.getDocumentTypeName());
			if (documentType.getDocumentTypeCode().equals(ObjectType.activity.name())) {
				logger.info("当文章【" + document.getDocumentCode() + "】为[" + documentType.getDocumentTypeCode() + "]类型时修改price中的活动价");
				String activityPrice = document.getDocumentDataMap().get("activityPrice").getDataValue();
				String productCode = document.getDocumentDataMap().get("productCode").getDataValue();
				String identify = document.getDocumentDataMap().get("activityCode").getDataValue();
				logger.debug("活动价 ：" + activityPrice + " 产品 ：" + productCode + " identify ：　" + identify);
				ProductCriteria productCriteria = new ProductCriteria();
				productCriteria.setProductCode(productCode);
				productCriteria.setOwnerId(ownerId);
				productService.list(productCriteria);

				PriceCriteria priceCriteria = new PriceCriteria();
				priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
//				priceCriteria.setObjectId(objectId);//产品id
				priceCriteria.setObjectType(ObjectType.product.toString());
				priceCriteria.setIdentify(identify);
				priceCriteria.setOwnerId(ownerId);

			}*/
			if (documentService.update(document) > 0) {
				message = new EisMessage(OperateResult.success.getId(), "修改成功");
				if(workflowInstance != null){
					workflowInstanceService.closeCurrentStep(document, workflowInstance);
				}

				if(documentType.getLongExtraValue("isProduct") == 1){
					List<Price> priceList = priceService.bindPrice(request, document);

					PriceCriteria priceCriteria = new PriceCriteria();
					priceCriteria.setOwnerId(partner.getOwnerId());
					priceCriteria.setObjectType(ObjectType.document.name());
					priceCriteria.setObjectId(document.getUdid());
					List<Price> oldPriceList = priceService.list(priceCriteria);
					logger.debug("老价格 ： " + oldPriceList + " 价格个数 ：" + oldPriceList.size());
					if(oldPriceList != null && oldPriceList.size() > 0){
						for(Price oldPrice : oldPriceList){
							logger.debug("价格类型 ：" + oldPrice.getPriceType());
							if(oldPrice.getPriceType() != null && oldPrice.getPriceType().equals(PriceType.PRICE_STANDARD.toString())){
								logger.debug("删除文档[" + document.getUdid() + "]的老价格:" + oldPrice);
								priceService.delete(oldPrice.getPriceId());
							} else {
								logger.debug("不删除文档[" + document.getUdid() + "]的非标准老价格:" + oldPrice);

							}
						} 
					}
					if(priceList != null && priceList.size() > 0){
						for(Price price : priceList){
							price.setObjectType(ObjectType.document.name());
							price.setObjectId(document.getUdid());
							price.setOwnerId(document.getOwnerId());
							price.setCurrentStatus(BasicStatus.normal.getId());
							priceService.insert(price);
						}
					}
				}

			} else {
				message = new EisMessage(OperateResult.failed.getId(), "修改失败");
			}

		} catch (Exception e) {
			String m = "数据操作失败" + e.getMessage();
			logger.error(m);
			throw new DataWriteErrorException(m);

		}
		document.setId(document.getUdid());
		tagObjectRelationService.processTagForTag(document);


		map.put("message", message);
		return partnerMessageView;

	}

	private boolean haveRelatePrivilegeToNode(User partner, int nodeId, String code) {
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria();
		privilegeCriteria.setUuid(partner.getUuid());
		privilegeCriteria.setObjectTypeCode(ObjectType.node.name());
		privilegeCriteria.setObjectId(String.valueOf(nodeId));
		privilegeCriteria.setOperateCode(Operate.relate.name());
		privilegeCriteria.setUserTypeId(UserTypes.partner.getId());
		return	authorizeService.havePrivilege(partner, privilegeCriteria);
	}
	//	修改文档状态
	@RequestMapping(value = "/update/status/{udid}", method = RequestMethod.GET)
	@AllowJsonOutput
	public String updateStatus(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		User partner = null;
		try {
			partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		} catch (Exception e) {
			throw e;
		}
		EisMessage message = null;
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.partnerMessageView;
		}
		String documentIdStr = request.getParameter("udid");
		int udid = 0;
		if (NumericUtils.isIntNumber(documentIdStr)) {
			udid = Integer.parseInt(documentIdStr);
		}
		//		Document _oldDocument = documentService.select(document.getUdid());

		Document _oldDocument = documentService.select(udid);
		logger.debug("文档id ： " + udid);
		if (_oldDocument == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "文档不存在"));
			return partnerMessageView;
		}
		if (_oldDocument.getOwnerId() != partner.getOwnerId()) {
			map.put("message",	new EisMessage(EisError.OBJECT_IS_NULL.getId(), "文档不存在"));
			return partnerMessageView;
		}
		String currentStatusStr = request.getParameter("currentStatus");
		int currentStatus = 0;
		if (NumericUtils.isIntNumber(currentStatusStr)) {
			currentStatus = Integer.parseInt(currentStatusStr);
		}
		Document document = _oldDocument.clone();
		document.setCurrentStatus(currentStatus);
		//FIXME
		workflowInstanceService.executeWorkflow(document, _oldDocument, request, Operate.update.name(), ownerId);

		/*Object workflowResult = workflowInstanceService.executeWorkflow(document, _oldDocument, request, Operate.update.name(), ownerId);
		WorkflowInstance workflowInstance = null;
		if(workflowResult instanceof WorkflowInstance){
			workflowInstance = (WorkflowInstance)workflowResult;
			logger.debug("执行工作流完成后，工作流实例:" + workflowInstance);
		} else if(workflowResult instanceof EisMessage){
			EisMessage workflowMsg = (EisMessage)workflowResult;
			logger.error("无法完成创建产品的工作流:" + workflowMsg.getOperateCode() + "=> " + workflowMsg.getMessage());
			map.put("message", workflowMsg);
			return partnerMessageView;

		} else {
			logger.debug("修改状态不需要工作流参与");
		}*/
		try {

			if (documentService.update(document) > 0) {
				logger.debug("修改成功 ：" + documentService.update(document));
				message = new EisMessage(OperateResult.success.getId(), "修改成功");
			} else {
				logger.debug("修改失败 ：" + documentService.update(document));
				message = new EisMessage(OperateResult.failed.getId(), "修改失败");
			}

		} catch (Exception e) {
			String m = "数据操作失败" + e.getMessage();
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		map.put("message", message);
		return partnerMessageView;
	}
}

package com.maicard.wpt.partner.controller;

import static com.maicard.standard.CommonStandard.partnerMessageView;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.AllowJsonOutput;
import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.base.UUIDFilenameGenerator;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.criteria.DictCriteria;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.Column;
import com.maicard.common.domain.ColumnWeightComparator;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.Dict;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.DictService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.LanguageService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.SortUtils;
import com.maicard.common.util.StringTools;
import com.maicard.ec.service.DeliveryPriceService;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.domain.WorkflowInstance;
import com.maicard.flow.service.WorkflowInstanceService;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.domain.ProductType;
import com.maicard.product.service.BusinessProcessor;
import com.maicard.product.service.ProductDataService;
import com.maicard.product.service.ProductService;
import com.maicard.product.service.ProductTypeService;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.criteria.DocumentTypeCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.criteria.TemplateCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.DocumentNodeRelation;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;
import com.maicard.site.domain.Template;
import com.maicard.site.service.DocumentDataService;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DisplayLevel;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.ServiceStatus;
import com.maicard.standard.ShareConfigType;
import com.maicard.standard.SecurityStandard.UserExtraType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;


@Controller
@RequestMapping("/product")
public class ProductController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CookieService cookieService;

	@Resource
	private ShareConfigService shareConfigService;
	/*
	 * XXX 对于EC模块的服务，使用@Autowired来调用
	 * 在某些不需要实体电商，即不使用到EC任何功能的情况下也可以正常工作
	 */
	@Autowired(required=false)
	private DeliveryPriceService deliveryPriceService;


	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private CenterDataService centerDataService;
	@Resource
	private DocumentService documentService;
	@Resource
	private DocumentDataService documentDataService;
	@Resource
	private LanguageService languageService;
	@Resource
	private NodeService nodeService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private ProductService productService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;
	@Resource
	private TemplateService templateService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private ProductDataService productDataService;
	@Resource
	private DictService dictService;
	@Resource
	private WorkflowInstanceService workflowInstanceService;

	@Resource
	private DocumentTypeService documentTypeService;
	@Resource
	private TagService tagService;
	@Resource
	private DocumentNodeRelationService documentNodeRelationService;

	private String documentUploadSaveDir;


	int productDocumentTypeId = 0;

	final int displayTypeId = 176003;

	private int rowsPerPage = 10;

	private final String DEFAULT_ORDER = "create_time DESC";


	@PostConstruct
	public void init(){
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}

		documentUploadSaveDir = applicationContextService.getDataDir();

		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setDocumentTypeCode(ObjectType.product.name());
		List<DocumentType> documentTypeList = documentTypeService.list(documentTypeCriteria);
		if(documentTypeList == null || documentTypeList.size() < 1){
			logger.error("系统中未定义产品类型的文档类型");
		} else {
			productDocumentTypeId = documentTypeList.get(0).getDocumentTypeId();
		}


	}


	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ProductCriteria productCriteria) throws Exception {
		final String view = "common/product/list";
		final String miniView = "common/product/list_mini";

		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			throw new UserNotFoundInRequestException();
		}

		if(authorizeService.havePrivilege(partner, ObjectType.product.name(), "w")){
			map.put("addUrl", "/product/create.shtml" );
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////

		if(!isPlatformGenericPartner){
			//对于非内部用户，只允许查看supplier=他自己的产品
			productCriteria.setSupplierId(partner.getUuid());
		}

		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		map.put("addUrl", "/product/create" + CommonStandard.DEFAULT_PAGE_SUFFIX);

		String display = request.getParameter("mini");

		String deliveryFromArea = ServletRequestUtils.getStringParameter(request, "deliveryFromArea");
		if (StringUtils.isNotBlank(deliveryFromArea)) {
			DataDefine dataDefine = dataDefineService.select("deliveryFromArea");
			ProductDataCriteria productDataCriteria = new ProductDataCriteria();
			productDataCriteria.setDataDefineId(dataDefine.getDataDefineId());
			productDataCriteria.setDataCode(deliveryFromArea);
			List<ProductData> list = productDataService.list(productDataCriteria );
			int[] arr = null;
			if (list != null && list.size() > 0) {
				arr = new int[list.size()];
				for (int i = 0; i < list.size(); i++) {
					arr[i] = (int) list.get(i).getProductId();
				}
			}

			productCriteria.setProductId(arr);
		}

		boolean shareConfigOnly = ServletRequestUtils.getBooleanParameter(request, "shareConfig", false);
		if(shareConfigOnly){
			ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria(ownerId);
			shareConfigCriteria.setObjectType(ShareConfigType.product.name());
			if(!isPlatformGenericPartner){
				partnerService.setSubPartner(shareConfigCriteria, partner);
			} 

			List<ShareConfig> shareConfigList = shareConfigService.listOnPage(shareConfigCriteria);
			List<Integer> shareProductIdList = new ArrayList<Integer>();

			if(shareConfigList.size() > 0){
				for(ShareConfig shareConfig : shareConfigList){
					shareProductIdList.add(shareConfig.getObjectId());
				}
				productCriteria.setProductId(NumericUtils.list2Array(shareProductIdList));
				logger.info("当前查询设置了只查询有分成配置的产品，已配置产品ID是:{}", Arrays.toString(productCriteria.getProductId()));
			} else {
				logger.warn("当前没有返回任何分成配置");
			}
			
		}

		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria(partner.getOwnerId());

		tagObjectRelationCriteria.setObjectType(ObjectType.product.name());
		List<Tag> tagList = tagObjectRelationService.listTags(tagObjectRelationCriteria);

		logger.debug("与产品关联的标签数量是:" + (tagList == null ? "空" : tagList.size()));
		map.put("tagList", tagList);

		Set<Column> displayColumns = getDisplayColumns(request, partner, productCriteria.getProductTypeId());
		Set<Column> systemDisplayColumns = getSystemDisplayColumns(partner, productCriteria.getProductTypeId());
		if(displayColumns != null && displayColumns.size() > 0 && systemDisplayColumns != null && systemDisplayColumns.size() > 0){
			for(Column sysColumn : systemDisplayColumns){
				for(Column userColumn : displayColumns){
					if(userColumn.getColumnName().equals(sysColumn.getColumnName())){
						sysColumn.setCurrentStatus(BasicStatus.relation.getId());
						break;
					}
				}
			}
		}
		map.put("displayColumns", displayColumns);
		map.put("systemDisplayColumns", systemDisplayColumns);


		//获取产品类型
		ProductTypeCriteria productTypeCriteria = new ProductTypeCriteria();
		productTypeCriteria.setOwnerId(partner.getOwnerId());
		List<ProductType> productTypes = new ArrayList<ProductType>();

		String validProductTypeIds = authorizeService.listValidObjectId(partner, ObjectType.productType.name(), 0, Operate.list.name());
		logger.debug("用户[" + partner.getUuid() + "]可以访问的产品类型是:" + validProductTypeIds);
		if(validProductTypeIds == null){
			map.put("message",  new EisMessage(-EisError.ACCESS_DENY.id,"您无权访问产品"));
			return partnerMessageView;
		}



		//对于不是访问所有产品类型的情况，只列出指定的产品类型，并且把这些类型放入产品的查询条件
		if(!validProductTypeIds.equals("*")){
			String[] productTypeString = validProductTypeIds.split(",");
			ArrayList<Integer> idList = new ArrayList<Integer>();
			for(String s : productTypeString){
				idList.add(Integer.parseInt(s));				
			}
			int[] productTypeIds = new int[idList.size()];
			for(int i = 0; i < idList.size(); i++){
				productTypeIds[i] = idList.get(i);
				ProductType productType = productTypeService.select(idList.get(i));
				if(productType == null){
					logger.error("找不到指定的产品类型:" + idList.get(i));
				} else {
					productTypes.add(productType);
				}
			}
			productCriteria.setProductTypeId(0);
			productCriteria.setProductTypeIds(productTypeIds);

		} else {
			productTypes = productTypeService.list(productTypeCriteria);
		}
		map.put("productType", productTypes);
		map.put("supplierMap", getSupplierMap());


		productCriteria.setOwnerId(partner.getOwnerId());
		int totalRows = productService.count(productCriteria);
		Paging paging = new Paging(rows);
		productCriteria.setPaging(paging);
		productCriteria.getPaging().setCurrentPage(page);
		logger.debug("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");

		map.put("total", totalRows);
		if(totalRows < 1){
			logger.debug("当前查询的产品数量为0");
			return view;
		}
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

		String orderBy = ServletRequestUtils.getStringParameter(request, "order", DEFAULT_ORDER);
		productCriteria.setOrderBy(orderBy);

		List<Product> productList = productService.listOnPage(productCriteria);
		List<Product> productList2 = new ArrayList<Product>();


		if (productList == null || productList.size() < 1) {
		} else {
			for(Product p : productList){
				Product p2 = p.clone();
				productList2.add(p2);
			}
			productList = null;
			authorizeService.writeOperate(partner, productList2);
			String order = productCriteria.getOrderBy();
			if(order != null){
				//尝试排序
				String[] orderData = order.split("\\s+");
				String sortField = orderData[0];
				if(sortField.indexOf("_") > 0){
					logger.debug("排序属性有下划线，放弃属性排序");
				} else {
					String sortOrder = null;
					if(orderData.length > 1){
						sortOrder = orderData[1];
					}
					logger.debug("以属性[" + sortField + "]进行" + sortOrder + "排序");
					//productService.sort(productList, sortField, sortOrder);
					SortUtils.sort(productList2, sortField, sortOrder);
				}

			}
			for (Product product : productList2) {
				//	writeOperate(product);
				long deliveryCompanyId = product.getLongExtraValue(DataName.deliveryCompanyId.toString());
				if(deliveryCompanyId > 0 && StringUtils.isBlank(product.getExtraValue(DataName.deliveryCompanyName.toString()))){
					User p = partnerService.select(deliveryCompanyId);
					if(p != null){
						product.setExtraValue(DataName.deliveryCompanyName.toString(), (p.getNickName() == null ? p.getUsername() : p.getNickName()));
					}
				}
				ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria(ownerId);
				shareConfigCriteria.setObjectType(ShareConfigType.product.name());
				shareConfigCriteria.setObjectId(product.getProductId());
				shareConfigCriteria.setShareUuid(partner.getUuid());
				ShareConfig shareConfig = shareConfigService.calculateShare(shareConfigCriteria);
				if(shareConfig != null){
					product.setBuyMoney(product.getBuyMoney() * shareConfig.getSharePercent());
				}
				// Product.setProductCode(Product.getProductId()+"["+Product.getProductCode()+"]");
			}
		}



		map.put("rows", productList2);


		if (display != null && display.equals("true")) {
			String dynamicView = request.getParameter("view");
			if(StringUtils.isNotBlank(dynamicView)){
				return dynamicView; 
			} else {
				return miniView;
			}
		} else {
			return view;
		}
	}

	/*private void writeOperate(Product product) {
		if(product.getOperate().get("haveFullPrivilege") != null || product.getOperate().get("haveReadPrivilege") != null || product.getOperate().get(Operate.list.name()) != null){
			product.getOperate().put("get", "./product/" + Operate.get.name() + "/" + product.getProductId());
		}
		if(product.getOperate().get("haveFullPrivilege") != null || product.getOperate().get("haveReadPrivilege") != null || product.getOperate().get(Operate.get.name()) != null){
			product.getOperate().put("get", "./product/" + Operate.get.name() + "/" + product.getProductId());
		}
		if(product.getOperate().get("haveFullPrivilege") != null || product.getOperate().get("haveWritePrivilege") != null || product.getOperate().get(Operate.update.name()) != null){
			product.getOperate().put("update", "./product/" + Operate.update.name() + "/" + product.getProductId());
		}
		if(product.getOperate().get("haveFullPrivilege") != null || product.getOperate().get("haveWritePrivilege") != null || product.getOperate().get(Operate.delete.name()) != null){
			product.getOperate().put("del", "./product/" + Operate.delete.name());		
		}
	}*/


	/**
	 * 设置用户自身的显示字段，存入Cookie
	 */
	@RequestMapping(value="setViewColumns", method = RequestMethod.POST)
	@IgnorePrivilegeCheck
	public String setViewColumns(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		int productTypeId = ServletRequestUtils.getIntParameter(request, "productTypeId", 0);
		Set<String> customDisplayColumn = getCustomDisplayColumn(request);
		if(customDisplayColumn != null && customDisplayColumn.size() > 0){
			//写入Cookie
			StringBuffer sb = new StringBuffer();
			for(String column : customDisplayColumn){
				sb.append(column).append(",");
			}
			cookieService.addCookie(request, response, 	ProductCriteria.displayColumnKeyPrefix + productTypeId, sb.toString().replaceAll(",$", ""), CommonStandard.COOKIE_MAX_TTL, null);
		} 

		return partnerMessageView;
	}

	/**
	 * 检查用户的Cookie中，是否有显示字段的数据
	 */
	private Set<String> getCustomDisplayColumn(HttpServletRequest request) {

		String displayColumn = request.getParameter("displayColumns");

		//String displayColumn = ServletRequestUtils.getStringParameter(request, "displayColumns", null);
		if(StringUtils.isBlank(displayColumn)){
			logger.warn("用户没提交自定义显示字段");
			return null;
		}
		/*if(displayColumn == null || displayColumn.length < 1){
			logger.warn("用户没提交自定义显示字段");
			return null;
		}*/
		logger.debug("用户本次提交了的自定义显示字段:" + displayColumn);

		Set<String> set = StringTools.getSetFromString(displayColumn.split(","));
		if(set == null || set.size() < 1){
			logger.warn("未能解析用户提交的自定义显示字段:" + displayColumn );
			return null;
		}
		Map<Integer,String> columnMap = new HashMap<Integer, String>();
		for(String column : set){
			if(column.indexOf("#") < 0){
				continue;
			}
			String data[] = column.split("#");
			if(NumericUtils.isNumeric(data[0])){
				columnMap.put(Integer.parseInt(data[0]), data[1]);
			}
		}
		List<Integer> keys = new ArrayList<Integer>(columnMap.keySet());
		Collections.sort(keys);
		Set<String> displayColumns = new HashSet<String>();
		for(Integer id : keys){
			displayColumns.add(columnMap.get(id));
		}
		try {
			logger.debug("根据用户请求获取到的自定义字段是:" + JsonUtils.getInstance().writeValueAsString(displayColumns));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return displayColumns;			
	}		





	/*
	 * 首先尝试从用户Cookie中读取自定义显示字段
	 * 如果没有，则从系统配置中读取
	 * 如果没有，最后从系统中读取默认配置
	 */
	private Set<Column> getDisplayColumns(HttpServletRequest request, User partner, int productTypeId) {

		Set<String> displayColumns = new HashSet<String>();
		Set<Column> filterdDisplayColumns = new HashSet<Column>();

		String key = ProductCriteria.displayColumnKeyPrefix + productTypeId;
		String cookie = cookieService.getCookie(request, key);
		if(cookie != null){
			logger.debug("从用户Cookie中读取到的自定义显示字段[" + key + "]的值:" + cookie);
			displayColumns = StringTools.getSetFromString(cookie.split(","));
		}
		Set<Column> systemDisplayColumns = getSystemDisplayColumns(partner, productTypeId);
		if(displayColumns != null && displayColumns.size() > 0){
			for(String columnName : displayColumns){
				for(Column column : systemDisplayColumns){
					if(column.getColumnName().equals(columnName)){
						filterdDisplayColumns.add(column);
						break;
					}
				}
			}
			return filterdDisplayColumns;
		}
		return systemDisplayColumns;

	}

	private Set<Column> getSystemDisplayColumns(User partner, int productTypeId) {
		Set<Column> displayColumns = new TreeSet<Column>();
		String key = ProductCriteria.displayColumnKeyPrefix + productTypeId;

		String systemConfigData = configService.getValue(key, partner.getOwnerId());
		if(systemConfigData != null){
			logger.debug("从系统配置中读取到的自定义显示字段[" + key + "]的值:" + systemConfigData);
			ObjectMapper om = JsonUtils.getInstance();
			JavaType javaType = om.getTypeFactory().constructCollectionType(Set.class, Column.class);   
			try {
				displayColumns = om.readValue(systemConfigData, javaType);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		if(displayColumns != null && displayColumns.size() > 0){
			Set<Column> sortedSet = new TreeSet<Column>(new ColumnWeightComparator());
			for(Column column : displayColumns){
				sortedSet.add(column);
			}
			return sortedSet;
		}
		logger.debug("从用户Cookie和系统配置中，都未能读取到自定义显示字段[" + key + "]的值，返回系统默认");
		return productService.getDisplayColumns(productTypeId, partner.getOwnerId());
	}



	@RequestMapping(value = "/get/{productId}")
	public String get(HttpServletRequest request, HttpServletResponse response,
			ModelMap map, @PathVariable("productId") int productId,
			ProductCriteria productCriteria)
					throws Exception {
		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			throw new UserNotFoundInRequestException();
		}

		if(authorizeService.havePrivilege(partner, ObjectType.product.name(), "w")){
			map.put("addUrl", "/product/create.shtml" );
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////
		if (productId == 0) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[ProductId]");
		}

		Product product = productService.select(productId);
		if (product == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + productId + "的Product对象");
		}
		if(product.getOwnerId() != partner.getOwnerId()){
			logger.error("产品[" + product.getProductId() + "]的ownerId[" + product.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + product + "的Product对象");			
		}
		if(!isPlatformGenericPartner && product.getSupplyPartnerId() != partner.getUuid()){
			logger.error("非内部商户[" + partner.getUuid() + "]不能查看非自身产品[" + product.getProductId() + "]，该产品属于:" + product.getSupplyPartnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + product + "的Product对象");			
		}
		// if(product == null){
		// logger.error("找不到产品[" + ProductId + "]");
		// return "redirect:/";
		// }
		// if(product.getSupplyPartnerId() != 0){
		// logger.error("试图访问的产品[" + product.getProductCode() + "/" +
		// product.getProductId() + "]不是内部产品");
		// return "redirect:/";
		// }

		/*ProductCriteria productCriteria1 = new ProductCriteria();
		productCriteria1.setParentProductId(product.getProductId());
		productCriteria1.setOrder("a.face_money ASC");
		productCriteria1.setOwnerId(ownerId);
		List<Product> childProductList = productService.list(productCriteria1);
		logger.info("产品[" + product.getProductId() + "]的子产品数量:"
				+ (childProductList == null ? 0 : childProductList.size()));

		map.put("childProductList", childProductList);

		// if(product.getBuyMoney() == 0){
		// return "product/list";
		// }
		if (product.getParentProductId() > 0) {
			// 找到上级产品
			Product parentProduct = productService.select(product
					.getParentProductId());
			if()
			map.put("parentProduct", parentProduct);
		}*/
		// 查找该产品的地区
		/*ProductRegionRelationCriteria productRegionRelationCriteria = new ProductRegionRelationCriteria();
		productRegionRelationCriteria.setProductId(product.getProductId());
		List<Region> regionList = productRegionRelationService
				.listRegion(productRegionRelationCriteria);
		logger.debug("产品[" + product.getProductId() + "]相关的区域有["
				+ (regionList == null ? -1 : regionList.size()) + "]个");
		map.put("regionList", regionList);*/
		// 查找充值服务器
		PriceCriteria priceCriteria = new PriceCriteria(product.getOwnerId());
		priceCriteria.setObjectType(ObjectType.product.name());
		priceCriteria.setObjectId(product.getProductId());
		List<Price> priceList = priceService.list(priceCriteria);
		logger.debug("产品[" + product.getProductId() + "]相关的价格有有[" + (priceList == null ? -1 : priceList.size())		+ "]个");

		HashMap<String,ProductData> productDataMap = new HashMap<>();
		ProductDataCriteria productDataCriteria = new ProductDataCriteria();
		productDataCriteria.setProductId(product.getProductId());
		List<ProductData> list = productDataService.list(productDataCriteria );
		if (list != null && list.size() > 0) {
			for (ProductData productData : list) {
				DataDefine dataDefine = dataDefineService.select(productData.getDataDefineId());
				if (dataDefine != null) {
					productData.setDataName(dataDefine.getDataName());
					productData.setDataCode(dataDefine.getDataCode());
					productDataMap.put(productData.getDataCode(), productData);
				}
			}
		}
		product.setProductDataMap(productDataMap);

		Document document = documentService.select(product.getProductCode(), ownerId);
		List<Tag> tagList = new ArrayList<>();
		Map<String, Node> nodeMap = new HashMap<>();
		if (document != null) {
			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
			tagObjectRelationCriteria.setObjectId(document.getUdid());
			List<TagObjectRelation> tagRelationList = tagObjectRelationService.list(tagObjectRelationCriteria );
			if (tagRelationList != null && tagRelationList.size()>0) {
				for (TagObjectRelation tagObjectRelation : tagRelationList) {
					Tag tag = tagService.select(tagObjectRelation.getTagId());
					if (tag != null) {
						tagList.add(tag);
					}
				}
			}
			DocumentNodeRelationCriteria documentNodeRelationCriteria = new DocumentNodeRelationCriteria();
			documentNodeRelationCriteria.setUdid(document.getUdid());
			List<DocumentNodeRelation> nodeRelations = documentNodeRelationService.list(documentNodeRelationCriteria );
			if (nodeRelations != null && nodeRelations.size() > 0) {
				for (DocumentNodeRelation documentNodeRelation : nodeRelations) {
					Node node = nodeService.select(documentNodeRelation.getNodeId());
					if (documentNodeRelation.getCurrentStatus()==BasicStatus.normal.id) {
						nodeMap.put("showPosition", node);
					}else {
						nodeMap.put("synPosition", node);
					}
				}
			}
		}
		map.put("node", nodeMap);
		map.put("tag", tagList);
		map.put("priceList", priceList);

		map.put("product", product);
		return "common/product/get";
	}

	@RequestMapping("/delete")
	@AllowJsonOutput
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if (idList == null || idList.equals("")) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		logger.debug("删除的参数 ： " + idList);
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for (int i = 0; i < ids.length; i++) {
			try {
				int deleteId = Integer.parseInt(ids[i]);
				Product product = productService.select(deleteId);
				if(product == null){
					logger.warn("找不到要删除的产品，ID=" + deleteId);
					continue;
				}

				if(product.getOwnerId() != partner.getOwnerId()){
					logger.warn("要删除的产品，ownerId[" + product.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
					continue;
				}
				logger.warn("尝试删除产品[" + ids[i] + "]及其所有关联数据");
				if (productService.delete(Integer.parseInt(ids[i])) > 0) {

					successDeleteCount++;
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
		logger.info(messageContent);
		map.put("message", new EisMessage(OperateResult.success.getId(), messageContent));
		return partnerMessageView;

	}

	//修改产品库存
	@RequestMapping("/relate")
	@AllowJsonOutput
	public String updateAmount(HttpServletRequest request, HttpServletResponse response, ModelMap map, Product product) throws Exception {

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(product.getProductId() < 1){
			logger.error("尝试修改Gallery但提交的galleryId是0");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要修改的产品ID"));
			return partnerMessageView;		
		}
		int offset = ServletRequestUtils.getIntParameter(request, "offset", 0);
		boolean forceWrite = false;
		if(product.getUpdateMode() != null && product.getUpdateMode().equals("force")){
			forceWrite = true;
		}
		Product _oldProduct = productService.select(product.getProductId());
		if(_oldProduct == null){
			logger.error("找不到要修改的Product，ID=" + product.getProductId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到对应的产品"));
			return partnerMessageView;		
		}
		if(_oldProduct.getOwnerId() != partner.getOwnerId()){
			logger.warn("要修改的产品[" + _oldProduct.getProductId() + ",ownerId[" + _oldProduct.getOwnerId() + "]与系统会话中的ownerId不一致:" + partner.getOwnerId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到对应的产品"));
			return partnerMessageView;		
		}
		String message = null;

		long newCount = productService.writeAmount(product, offset, forceWrite);
		logger.debug("修改产品[" + product.getProductId() + "]库存为:" + product.getAvailableCount());
		message = "修改产品[" + product.getProductId() + "]库存为:" + product.getAvailableCount();

		map.put("message", new EisMessage(OperateResult.success.getId(),message + ",库存更新结果:" + newCount));

		return partnerMessageView;

	}


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
		}
		String viewAlter = ServletRequestUtils.getStringParameter(request, "alter",null);
		int productTypeId = ServletRequestUtils.getIntParameter(request, "productTypeId", 0);
		ProductType productType = null;
		if(productTypeId <= 0){
			logger.error("未提供新建产品的类型ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"请选择新建产品的类型"						));
			return partnerMessageView;
		}
		productType = productTypeService.select(productTypeId);
		if(productType == null){
			logger.error("找不到新建产品的类型:" + productTypeId);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"请选择正确的新建产品的类型"						));
			return partnerMessageView;
		}
		map.put("productType", productType);

		/*		ProductTypeCriteria productTypeCriteria = new ProductTypeCriteria();
		map.put("productTypeList", productTypeService.list(productTypeCriteria));
		 */		Set<Integer> statusList = new HashSet<Integer>();
		 for(BasicStatus b : BasicStatus.values()){
			 if(b.getId() == 0){
				 continue;
			 }
			 statusList.add(b.getId());
		 }
		 for(ServiceStatus s : ServiceStatus.values()){
			 if(s.getId() == 0){
				 continue;
			 }
			 statusList.add(s.getId());
		 }
		 map.put("statusList", statusList);



		 //得到快递合作商
		 UserCriteria deliveryCriteria = new UserCriteria(partner.getOwnerId());
		 deliveryCriteria.setUserTypeId(UserTypes.partner.getId());
		 deliveryCriteria.setUserExtraTypeId(UserExtraType.deliveryPartner.getId());
		 deliveryCriteria.setCurrentStatus(UserStatus.normal.getId());
		 List<User> deliveryPartnerList = partnerService.list(deliveryCriteria);

		 logger.debug("当前系统中的快递合作伙伴数量是:" + (deliveryPartnerList == null ? "空" : deliveryPartnerList.size()));
		 if(deliveryPartnerList != null && deliveryPartnerList.size() > 0){
			 map.put("deliveryPartnerList", deliveryPartnerList);
		 }



		 Product product = new Product();
		 product.setProductTypeId(productTypeId);

		 //得到当前最大的主键
		 long maxId = productService.getMaxId(new ProductCriteria());
		 product.setProductCode("P" + new DecimalFormat("0000000").format(maxId+1));

		 logger.info("创建新产品时，尝试获取业务流实例,对象类型代码:"	+ ObjectType.product.toString() + ", 操作代码:" + Operate.create.name() + "].");
		 WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria(0, ObjectType.product.name(), productTypeId, Operate.create.name(), partner);
		 WorkflowInstance workflowInstance = workflowInstanceService.getInstance(workflowInstanceCriteria);
		 Map<String, Attribute> validAttributeMap = new HashMap<String, Attribute>();
		 int postProcess = 0;
		 if (workflowInstance == null) {
			 logger.debug("找不到创建新产品的工作流实例，或不需要使用工作流");

		 } else {
			 if (workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1) {
				 logger.error("针对对象[" + ObjectType.product.toString()	+ "]的工作流，其工作步骤为空");
				 map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(),"工作流数据异常"						));
				 return partnerMessageView;
			 }
			 logger.debug("创建产品使用" +  workflowInstance.getWorkflowInstanceId() + "#工作流实例，当前步骤:" + workflowInstance.getCurrentStep());

			 map.put("workflowInstanceId", workflowInstance.getWorkflowInstanceId());
			 Map<String,DataDefine> dataDefineMap = null;
			 if(productType != null){
				 dataDefineMap = productType.getDataDefineMap();
			 }
			 validAttributeMap = workflowInstanceService.getValidInputAttribute(product, workflowInstance, dataDefineMap);
			 if(workflowInstance.getCurrentRoute() != null){
				 String postProcessStr = workflowInstance.getCurrentRoute().getPostProcess();
				 if(NumericUtils.isNumeric(postProcessStr)){
					 postProcess = NumericUtils.parseInt(postProcessStr);
					 logger.debug("当前操作模式postProcess=" + postProcess);

				 } else {
					 logger.error("当前工作流[" + workflowInstance.getWorkflowInstanceId() + "]没有当前步骤");
				 }

			 }
			 if(validAttributeMap != null && validAttributeMap.containsKey("processClass")){
				 //查看系统中是否有业务处理器，如果有，则放入
				 String[] processorNames = applicationContextService.getBeanNamesForType(BusinessProcessor.class);
				 logger.debug("当前系统中存在的业务处理器数量是:" + (processorNames == null ? "空" : processorNames.length ) );
				 if(processorNames != null && processorNames.length > 0){
					 validAttributeMap.get("processClass").setValidValue(processorNames);
					 validAttributeMap.get("processClass").setUseMessagePrefix("ProcessClass");
				 }

			 }
			 map.put("validAttributeMap",validAttributeMap);
		 }
		 if(postProcess > 0){
			 //放入文档相关的数据
			 map.put("postProcess", postProcess);
			 addDocumentEnv(partner,map);
		 }
		 map.put("product", product);
		 map.put("validAttributeMap", validAttributeMap);
		 String view = "common/product/create";
		 if(viewAlter != null){
			 view += "_";
			 view += viewAlter;
		 }

		 return view;
	}

	//在添加或更新产品时，把对应的文档环境放入map，如nodeList、languageList等等
	private void addDocumentEnv(User partner, ModelMap map) {

		// 得到所有可用的节点
		NodeCriteria nodeCriteria = new NodeCriteria();
		nodeCriteria.setOwnerId(partner.getOwnerId());
		nodeCriteria.setCurrentStatus(BasicStatus.normal.getId(),BasicStatus.relation.getId(), BasicStatus.hidden.getId());
		List<Node> nodeList = nodeService.list(nodeCriteria);
		if(nodeList == null || nodeList.size() < 1){
			logger.warn("当前系统没有任何栏目,ownerId=" + partner.getOwnerId());
		} else {
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

			map.put("nodeList", nodeList);
			map.put("nodeTree", nodeTree);
		}


		// 得到所有的模版
		TemplateCriteria templateCriteria = new TemplateCriteria();
		templateCriteria.setOwnerId(partner.getOwnerId());
		templateCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Template> templateList = templateService.list(templateCriteria);
		map.put("templateList", templateList);
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map, Product product) throws Exception {

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = (long)map.get("ownerId");

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return partnerMessageView;		
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return partnerMessageView;
		}
		if(product.getCreateTime() == null){
			product.setCreateTime(new Date());
		}
		if(product.getPublishTime() == null){
			product.setPublishTime(new Date());
		}

		String tags = product.getTags();
		if(tags == null){
			tags = "";
		}
		//在产品中添加购买价格
		if(NumericUtils.isNumeric(request.getParameter("PRICE_" + PriceType.PRICE_STANDARD.toString() + '.' + MoneyType.money.name()))){
			product.setBuyMoney(Float.parseFloat(request.getParameter("PRICE_" + PriceType.PRICE_STANDARD.toString() + '.' + MoneyType.money.name())));
		}
		product.setOwnerId(partner.getOwnerId());
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		dataDefineCriteria.setObjectId(product.getProductTypeId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);


		if (dataDefineList == null || dataDefineList.size() == 0) {
			logger.info("当前产品类型[" + product.getProductTypeId() + "]没有自定义字段.");
		} else {
			String autoTagConfig = configService.getValue(DataName.autoTagList.toString(), partner.getOwnerId());
			logger.info("当前系统定义的自动tag列表(autoTagList)是:" + autoTagConfig);
			Set<String> autoTagList = StringTools.getSetFromString(autoTagConfig, ",");


			logger.debug("当前产品类型有[" + dataDefineList.size() + "]个自定义数据规范");
			if (request instanceof MultipartHttpServletRequest) {
				logger.info("请求中带有附件，使用文件上传处理.");
				this.fileUpload(request, Operate.create.name(), product);
			}

			if (product.getProductDataMap() == null) {
				product.setProductDataMap(new HashMap<String, ProductData>());
			}
			for (DataDefine dataDefine : dataDefineList) {
				logger.debug("尝试获取数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据");
				if(product.getProductDataMap().get(dataDefine.getDataCode()) != null){
					//可能已经由文件上传处理
					logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据已存在，可能是由文件上传处理");
					continue;
				}
				String documentDataStr = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
				if (documentDataStr == null || documentDataStr.equals("")) {
					logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]没有提交数据");
					continue;
				}
				logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]提交的数据是[" + documentDataStr + "]");
				if(autoTagList.contains(dataDefine.getDataCode())){
					tags += documentDataStr;
					tags += ",";
				}
				ProductData productData = new ProductData();
				productData.setDataDefineId(dataDefine.getDataDefineId());
				productData.setDataCode(dataDefine.getDataCode());
				productData.setDataValue(documentDataStr);
				logger.debug("尝试插入自定义产品数据[" + productData.getDataCode() + "/" + productData.getDataDefineId() + "]，数据内容:[" + productData.getDataValue() + "]");
				product.getProductDataMap().put(productData.getDataCode(), productData);
			}

		}


		String deliveryCompanyId = product.getExtraValue(DataName.deliveryCompanyId.toString());
		if(NumericUtils.isIntNumber(deliveryCompanyId)){
			//得到快递合作商
			User deliveryPartner = partnerService.select(Long.parseLong(deliveryCompanyId));
			if(deliveryPartner == null){
				logger.error("找不到指定的快递合作伙伴:" + deliveryCompanyId);
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到指定的快递合作伙伴:" + deliveryCompanyId));
				return partnerMessageView;
			}
			/*if(deliveryPartner.getOwnerId() != ownerId){
				logger.error("指定的快递合作伙伴:" + deliveryCompanyId + ",ownerId=" + deliveryPartner.getOwnerId() + "与当前ownerId=" + ownerId + "不一致");
				map.put("message", new EisMessage(EisError.requiredDataNotFound.id,"找不到指定的快递合作伙伴:" + deliveryCompanyId));
				return partnerMessageView;
			}*/
			if(deliveryPartner.getCurrentStatus() != UserStatus.normal.getId()){
				logger.error("指定的快递合作伙伴:" + deliveryCompanyId + "状态异常:" + deliveryPartner.getCurrentStatus());
				map.put("message", new EisMessage(EisError.statusAbnormal.id,"指定的快递合作伙伴:" + deliveryCompanyId + "状态异常"));
				return partnerMessageView;
			}
			product.setExtraValue(DataName.deliveryCompanyName.toString(), (deliveryPartner.getNickName() == null ? deliveryPartner.getUsername() : deliveryPartner.getNickName()));

		}
		//尝试从提交数据中绑定价格Price
		List<Price> priceList = bindPrice(request, partner.getOwnerId());

		//创建产品是否有工作流参与
		//如果有工作流则不自动创建对应文档

		Object workflowResult = workflowInstanceService.executeWorkflow(product, null, request, Operate.create.name(), ownerId);
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
			logger.debug("创建产品不需要工作流参与");
		}
		product.setTags(tags.replaceAll(",$", "").trim());
		/*if (product.getInitCount() == 0 || product.getInitCount() < 1) {
			logger.error("未提交产品初始数量");			
		} else {*/
		product.setAvailableCount(product.getInitCount());
		//}

		if(product.getCurrentStatus() == DocumentStatus.published.getId()){
			logger.debug("把产品状态从文档发布状态改为基本普通状态");
			product.setCurrentStatus(BasicStatus.normal.getId());
			product.setPublishTime(new Date());
		}

		int rs = 0;
		try {
			rs = productService.insert(product);
		} catch (Exception e) {
			String m = "数据操作失败" + e.getMessage();
			logger.error(m);
			e.printStackTrace();
			map.put("message", new EisMessage(OperateResult.failed.getId(), m));

		}

		if( rs != 1) {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "添加失败"));
			return partnerMessageView;

		}

		product.setId(product.getProductId());
		tagObjectRelationService.processTagForTag(product);



		long deliveryPartnerId = product.getLongExtraValue(DataName.deliveryCompanyId.toString());
		if(deliveryPartnerId > 0){
			//提交了合作快递，那么检查是否有提交快递报价单
			String deliveryFeeFile = product.getExtraValue(DataName.deliveryPriceListFile.toString());
			if(StringUtils.isBlank(deliveryFeeFile)){
				logger.warn("提交了快递合作伙伴但是未提交快递价格表");
			} else {
				/*		String displayLevel = product.getProductDataMap().get(DataName.deliveryPriceListFile.toString()).getDisplayLevel();
				String fileUploadSavePath = null;
				StringBuffer sb = new StringBuffer();

			if(displayLevel == null){
					sb.append(documentUploadSaveDir).append(File.separator).append(CommonStandard.EXTRA_DATA_OPEN_PATH).append(File.separator);
				} else if(displayLevel.equals(DisplayLevel.subscriber.toString())){
					sb.append(documentUploadSaveDir).append(File.separator).append(CommonStandard.EXTRA_DATA_SUBSCRIBE_PATH).append(File.separator);
				} else if(displayLevel.equals(DisplayLevel.login.toString())){
					sb.append(documentUploadSaveDir).append(File.separator).append(CommonStandard.EXTRA_DATA_LOGIN_PATH).append(File.separator);
				} else {
					sb.append(documentUploadSaveDir).append(File.separator).append(CommonStandard.EXTRA_DATA_OPEN_PATH).append(File.separator);
				}
				fileUploadSavePath = sb.toString();*/

				String fileName = documentUploadSaveDir + File.separator + deliveryFeeFile;
				logger.debug("读取快递报价单文件:" + fileName);
				File file = new File(fileName);
				if(!file.exists()){
					logger.error("找不到快递报价单文件:" + fileName);
					map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id, "找不到上传到报价单文件"));
					return partnerMessageView;
				}
				List<String> lines = FileUtils.readLines(file, CommonStandard.DEFAULT_ENCODING);
				if(deliveryPriceService != null){
					int loadRs = deliveryPriceService.loadBatch(lines, deliveryPartnerId, "product#" + product.getProductId(), ownerId);

					logger.debug("批量添加报价数据返回:" + loadRs );
					if(loadRs < 1){
						logger.error("无法批量添加报价单数据");
						/*map.put("message", new EisMessage(EisError.dataError.id, "无法处理报价单文件"));
						return partnerMessageView;		*/
					}
				} else {
					logger.warn("当前系统没有配置实体电商模块");
				}

			}
		}
		map.put("message",new EisMessage(OperateResult.success.getId(), "添加成功"));

		if(priceList != null && priceList.size() > 0){
			for(Price price : priceList){
				price.setObjectType(ObjectType.product.name());
				price.setObjectId(product.getProductId());
				price.setOwnerId(product.getOwnerId());
				price.setCurrentStatus(BasicStatus.normal.getId());
				priceService.insert(price);
			}
		}


		boolean createDocument = false;
		if(workflowInstance != null && workflowInstance.getCurrentRoute() != null){
			String postProcess = workflowInstance.getCurrentRoute().getPostProcess();
			if(NumericUtils.isNumeric(postProcess)){
				int post = NumericUtils.parseInt(postProcess);
				logger.debug("当前操作模式postProcess=" + post);
				if(post > 0){
					createDocument = true;
				} 
			}
		}
		if(workflowInstance != null){
			workflowInstance.setObjectId(product.getProductId());
			workflowInstanceService.closeCurrentStep(product, workflowInstance);
		}
		if(!createDocument){
			logger.debug("产品新增完成，当前工作流不创建对应的产品文档，新增流程结束");
			return partnerMessageView;
		}
		_syncDocument(product, partner, request, map, tags);

		return partnerMessageView;

	}


	private int _syncDocument(Product product, User partner, HttpServletRequest request, ModelMap map, String tags) throws Exception {

		//添加文档
		/*if(productDocumentTypeId < 1){
			map.put("message", new EisMessage(EisError.requiredDataNotFound.id,"创建产品文档但文档类型为0"));
			return;
		}*/
		String defaultNodeIdStr = request.getParameter("defaultNodeId");
		logger.debug("默认节点 ：" + defaultNodeIdStr);
		int defaultNodeId = 0;
		Node defaultNode = null;
		ArrayList<Node> relatedNodeList = null;
		if (!NumericUtils.isNumeric(defaultNodeIdStr)) {
			logger.error("错误的默认节点:" + defaultNodeIdStr);

			map.put("message", new EisMessage(EisError.NO_RELATED_NODE.id, "错误的默认栏目:" + defaultNodeIdStr));
			return -EisError.REQUIRED_PARAMETER.id;
		} else {
			defaultNodeId = Integer.parseInt(defaultNodeIdStr.trim());
			//int defaultNodeId = ServletRequestUtils.getIntParameter(request, "defaultNodeId");
			if (defaultNodeId == 0) {
				throw new RequiredParameterIsNullException("请求中找不到必须的参数defaultNodeId");
			}

			// 获取同步显示的发布节点
			int[] nodeIds = ServletRequestUtils.getIntParameters(request, "nodeId");
			relatedNodeList = new ArrayList<Node>();
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

						boolean havePrivilegeToNode = haveRelatePrivilegeToNode(partner.getUuid(), node.getNodeId(), Operate.relate.name());

						if(!havePrivilegeToNode){
							logger.error("用户没有权限在栏目[" + node.getNodeId() + "]上发布文章");
							map.put("message", new EisMessage(EisError.ACCESS_DENY.id, "您无权在栏目[" + node.getName() + "]上发布文章"));
							return -EisError.ACCESS_DENY.id;
						}
						if (nodeIds[i] == defaultNodeId) {
							node.setCurrentStatus(BasicStatus.relation.getId());
						}

						relatedNodeList.add(node);
					}
				}
			}

			Node dn = nodeService.select(defaultNodeId);
			if (dn == null) {
				String msg = "新增产品文档所选择的默认发布节点为空！";
				logger.error(msg);
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id, msg));
				return -EisError.REQUIRED_PARAMETER.id;
			}
			if(dn.getOwnerId() != partner.getOwnerId()){
				logger.error("指定的默认发布节点[" + dn.getNodeId() + "]的ownerId[" + dn.getOwnerId() + "]与系统用户的不一致[" + partner.getOwnerId());
				throw new RequiredParameterIsNullException("选择的默认发布节点为空");
			}
			boolean havePrivilegeToNode = haveRelatePrivilegeToNode(partner.getUuid(), dn.getNodeId(), Operate.relate.name());
			if(!havePrivilegeToNode){
				logger.error("用户没有权限在栏目[" + dn.getNodeId() + "]上发布文章");
				map.put("message", new EisMessage(EisError.ACCESS_DENY.id, "您无权在栏目[" + dn.getName() + "]上发布文章"));
				return -EisError.ACCESS_DENY.id;
			}
			defaultNode = dn.clone();
			defaultNode.setCurrentStatus(BasicStatus.relation.getId());
		}


		if(tags == null){
			tags = "";
		} 
		tags = StringTools.addElementNoDuplicate(tags, product.getTags());
		logger.debug("添加产品标签后:" + tags);
		Document document = productService.getRefDocument(product);
		if(document == null){
			document = new Document();
		} else {
			tags =  StringTools.addElementNoDuplicate(tags, document.getTags());
			logger.debug("根据产品代码[" + product.getProductCode() + "]找到了已经存在的文档:" + document.getUdid() + "/" + document.getDocumentCode() + ",添加文档标签后:" + tags);
		}
		document.setTags(product.getTags());

		if(product.getPublishTime() != null){
			document.setPublishTime(product.getPublishTime());
		} else {
			document.setPublishTime(new Date());
		}
		if(product.getCurrentStatus() == BasicStatus.normal.getId()){
			document.setCurrentStatus(DocumentStatus.published.getId());
			document.setPublishTime(new Date());

		} else {
			document.setCurrentStatus(product.getCurrentStatus());
		}


		document.setDisplayIndex(product.getDisplayIndex());
		document.setDisplayTypeId(displayTypeId);
		if(product.getDisplayTypeId()==176005){
			document.setDisplayTypeId(176005);
		}
		document.setLanguageId(CommonStandard.defaultLanguageId);
		int templateId = ServletRequestUtils.getIntParameter(request, "templateId", 0);
		if(tags != null){
			document.setTags(tags.replaceAll(",$", "").trim());
		}
		if(product.getCurrentStatus() == 0){
			logger.debug("未提交产品状态，将把产品状态改为基本普通状态，把文档状态改为已发布");
			document.setCurrentStatus(CommonStandard.documentDefaultStatus);
		}

		document.setTitle(product.getProductName());
		document.setContent(product.getContent());
		document.setDocumentTypeId(productDocumentTypeId);
		document.setDocumentDataMap(new HashMap<String,DocumentData>());
		document.setDocumentCode(product.getProductCode());
		document.setOwnerId(partner.getOwnerId());
		document.setPublisherId(partner.getUuid());
		document.setTemplateId(templateId);	
		document.setCreateTime(product.getCreateTime());
		productService.generateProductDocumentData(product, document);

		if (defaultNodeId > 0) {
			if (defaultNode != null) {
				if (relatedNodeList != null && relatedNodeList.size() > 0) {
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
				} else {
					relatedNodeList.add(defaultNode);
				}
				document.setRelatedNodeList(relatedNodeList);
			}
		}
		int rs = 0;
		if(document.getUdid() > 0){
			rs = documentService.update(document);
		} else {
			rs = documentService.insert(document);
		}
		logger.debug("为产品[" + product.getProductId() + "]同步文档结果:" + rs);	

		return rs;
	}


	private List<Price> bindPrice(HttpServletRequest request, long ownerId) {

		float labelMoney = ServletRequestUtils.getFloatParameter(request, "labelMoney", 0);

		Price standardPrice = new Price(PriceType.PRICE_STANDARD.toString());
		standardPrice.setMarketPrice(labelMoney);

		Price tuanPrice = new Price(PriceType.PRICE_TUAN.toString());
		tuanPrice.setMarketPrice(labelMoney);

		Price memberPrice = new Price(PriceType.PRICE_MEMBER.toString());
		memberPrice.setMarketPrice(labelMoney);

		Map<String,String[]>paramMap = request.getParameterMap();


		for(String key : paramMap.keySet()){
			//logger.debug("尝试解析价格参数:" + key);
			if(key.startsWith("PRICE_" + PriceType.PRICE_STANDARD.toString() + '.' + MoneyType.money.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					standardPrice.setMoney(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_STANDARD.toString() + '.' + MoneyType.coin.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					standardPrice.setCoin(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_STANDARD.toString() + '.' + MoneyType.point.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					standardPrice.setPoint(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_STANDARD.toString() + '.' + MoneyType.score.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					standardPrice.setScore(Integer.parseInt(paramMap.get(key)[0]));
				}
			}

			if(key.startsWith("PRICE_" + PriceType.PRICE_TUAN.toString() + '.' + MoneyType.money.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					tuanPrice.setMoney(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_TUAN.toString() + '.' + MoneyType.coin.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					tuanPrice.setCoin(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_TUAN.toString() + '.' + MoneyType.point.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					tuanPrice.setPoint(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_TUAN.toString() + '.' + MoneyType.score.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					tuanPrice.setScore(Integer.parseInt(paramMap.get(key)[0]));
				}
			}

			if(key.startsWith("PRICE_" + PriceType.PRICE_MEMBER.toString() + '.' + MoneyType.money.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					memberPrice.setMoney(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_MEMBER.toString() + '.' + MoneyType.coin.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					memberPrice.setCoin(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_MEMBER.toString() + '.' + MoneyType.point.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					memberPrice.setPoint(Float.parseFloat(paramMap.get(key)[0]));
				}
			}
			if(key.startsWith("PRICE_" + PriceType.PRICE_MEMBER.toString() + '.' + MoneyType.score.name())){
				if(NumericUtils.isNumeric(paramMap.get(key)[0])){
					memberPrice.setScore(Integer.parseInt(paramMap.get(key)[0]));
				}
			}
		}
		List<Price> priceList = new ArrayList<Price>();
		priceList.add(standardPrice);
		priceList.add(tuanPrice);
		priceList.add(memberPrice);
		return priceList;
	}

	// 编辑对象时的界面
	@RequestMapping(value = "/update/{productId}", method = RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("productId") Integer ProductId) throws Exception {
		logger.debug("编辑产品 ：" + ProductId + ";");
		if (ProductId == 0) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[ProductId]");
		}

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return partnerMessageView;		
		}

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Product product = productService.select(ProductId);
		if (product == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + ProductId + "的Product对象");
		}

		if(product.getOwnerId() != partner.getOwnerId()){
			logger.error("产品[" + product.getProductId() + "]的ownerId[" + product.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + product + "的Product对象");			
		}
		map.put("title", "产品编辑");
		boolean lockForUpdate = configService.getBooleanValue(DataName.lockProductForUpdate.toString(), ownerId);
		if(lockForUpdate){
			String key = KeyConstants.PRODUCT_UPDATE_LOCK_PREFIX + "#" + ProductId;

			boolean lockSuccess = centerDataService.setIfNotExist(key, "true", CommonStandard.UPDATE_LOCK_DEFAULT_LOCK_SEC);
			if(!lockSuccess){
				logger.info("产品处于编辑锁定状态，不能被编辑[" + key + "]");
				map.put("message", new EisMessage(EisError.updateLocked.getId(),"产品处于编辑锁定状态，不能被编辑[" + key + "]"	));
				return partnerMessageView;
			}
		}

		Set<Integer> statusList = new HashSet<Integer>();
		for(BasicStatus b : BasicStatus.values()){
			if(b.getId() == 0){
				continue;
			}
			statusList.add(b.getId());
		}
		for(ServiceStatus s : ServiceStatus.values()){
			if(s.getId() == 0){
				continue;
			}
			statusList.add(s.getId());
		}
		map.put("statusList", statusList);

		ProductType productType = productTypeService.select(product.getProductTypeId());
		map.put("productType", productType);
		WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria(product.getProductId(), ObjectType.product.name(), product.getProductTypeId(), Operate.update.name(), partner);
		WorkflowInstance workflowInstance = null;
		try {
			workflowInstance = workflowInstanceService.getInstance(workflowInstanceCriteria);
		} catch (Exception e) {
			logger.error("找不到已存在实例[对象类型:" + workflowInstanceCriteria.getTargetObjectType() + ",对象ID:" + workflowInstanceCriteria.getObjectId() + ",ownerId=" + workflowInstanceCriteria.getOwnerId());
		}
		Map<String, Attribute> validAttributeMap = new HashMap<String, Attribute>();
		int postProcess = 0;

		if (workflowInstance == null) {
			logger.debug("找不到编辑产品的工作流实例，或不需要使用工作流");

		} else {
			if (workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1) {
				logger.error("针对对象[" + ObjectType.product.toString()	+ "]的工作流，其工作步骤为空");
				map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(),"工作流数据异常"						));
				return partnerMessageView;
			}
			logger.debug("编辑产品使用" +  workflowInstance.getWorkflowInstanceId() + "#工作流实例，当前步骤:" + workflowInstance.getCurrentStep());

			map.put("workflowInstanceId", workflowInstance.getWorkflowInstanceId());
			Map<String,DataDefine> dataDefineMap = null;
			if(productType != null){
				dataDefineMap = productType.getDataDefineMap();
			}
			if(workflowInstance.getCurrentRoute() != null){
				String postProcessStr = workflowInstance.getCurrentRoute().getPostProcess();
				if(NumericUtils.isNumeric(postProcessStr)){
					postProcess = NumericUtils.parseInt(postProcessStr);
					logger.debug("当前操作模式postProcess=" + postProcess);
				} else {
					logger.error("当前工作流[" + workflowInstance.getWorkflowInstanceId() + "]没有当前步骤");
				}
			} else {
				logger.error("当前工作流[" + workflowInstance.getWorkflowInstanceId() + "]没有当前步骤");
			}
			validAttributeMap = workflowInstanceService.getValidInputAttribute(product, workflowInstance, dataDefineMap);
			map.put("validAttributeMap",validAttributeMap);
		}

		Document document = productService.getRefDocument(product);
		if(document != null){
			logger.debug("根据产品[" + product.getProductId() + "]找到了对应的文档:" + document.getUdid() + "/" + document.getDocumentCode());
			map.put("document", document);
		}
		PriceCriteria priceCriteria = new PriceCriteria();
		priceCriteria.setObjectId(ProductId);
		priceCriteria.setPriceType(PriceType.PRICE_STANDARD.toString());
		priceCriteria.setObjectType(product.getObjectType());
		priceCriteria.setOwnerId(product.getOwnerId());
		List<Price> priceLists = priceService.list(priceCriteria);
		Product product2 = product.clone();
		if (priceLists == null || priceLists.size() < 1) {
			logger.error("产品[" + ProductId + "]没有对应的price对象");
		} else {
			product2.setPrice(priceLists.get(0));
		}

		if(postProcess > 0){
			//放入文档相关的数据
			map.put("postProcess", postProcess);
			addDocumentEnv(partner,map);
		}

		map.put("product", product2);
		map.put("dictList", dictList());
		//map.put("dataValueArray", dataValueArray);
		String view = "common/product/update_mini";

		return view;
	}

	// 更新指定对象
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map, Product product) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return partnerMessageView;		
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return partnerMessageView;
		}
		if (product.getProductId() < 1) {
			map.put("message", new EisMessage(EisError.dataError.getId(), "错误的Product对象"));
			return partnerMessageView;
		}

		product.setOwnerId(ownerId);

		String key = KeyConstants.PRODUCT_UPDATE_LOCK_PREFIX + "#" + product.getProductId();
		long unlock =	centerDataService.delete(key);
		logger.debug("解除对产品的编辑锁定[" + key + "]，结果:" + unlock);

		//尝试从提交数据中绑定价格Price
		List<Price> priceList = bindPrice(request, partner.getOwnerId());
		logger.debug("提交数据中绑定价格Price : " + priceList);
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectId(product.getProductTypeId());
		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		logger.debug("dataDefineList ：" + dataDefineList);
		//	String productDataStr = "";
		product.setProductDataMap(new HashMap<String, ProductData>());
		logger.debug("产品小图 : " + product.getProductDataMap().get("productSmallImage"));
		if (request instanceof MultipartHttpServletRequest) {
			logger.info("请求中带有附件，使用文件上传处理.");
			this.fileUpload(request, Operate.update.name(), product);
		}
		for (DataDefine dataDefine : dataDefineList) {
			logger.debug("尝试获取数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据");
			if(product.getProductDataMap().get(dataDefine.getDataCode()) != null){
				//可能已经由文件上传处理
				logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据已存在，可能是由文件上传处理");
				continue;
			}
			String documentDataStr = ServletRequestUtils.getStringParameter(request, dataDefine.getDataCode());
			if (documentDataStr == null || documentDataStr.equals("")) {
				logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]没有提交数据");
				continue;
			}
			logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]提交的数据是[" + documentDataStr + "]");
			/*if(autoTagList.contains(dataDefine.getDataCode())){
				tags += documentDataStr;
				tags += " ";
			}*/

			ProductData productData = new ProductData();
			productData.setDataDefineId(dataDefine.getDataDefineId());
			productData.setDataCode(dataDefine.getDataCode());
			productData.setDataValue(documentDataStr);
			logger.debug("尝试插入自定义产品数据[" + productData.getDataCode() + "/" + productData.getDataDefineId() + "]，数据内容:[" + productData.getDataValue() + "]");
			product.getProductDataMap().put(productData.getDataCode(), productData);

		}
		logger.debug("修改数据 ：" + product.getProductDataMap());

		Product _oldProduct = productService.select(product.getProductId());
		if(_oldProduct == null){
			logger.error("找不到指定的产品:" + product.getProductId());
			throw new ObjectNotFoundByIdException("找不到ID=" + product + "的Product对象");			
		}
		if(_oldProduct.getOwnerId() != partner.getOwnerId()){
			logger.error("产品[" + _oldProduct.getProductId() + "]的ownerId[" + product.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + product + "的Product对象");			
		}
		Object workflowResult = workflowInstanceService.executeWorkflow(product, _oldProduct, request, Operate.update.name(), ownerId);

		WorkflowInstance workflowInstance = null;
		if(workflowResult instanceof WorkflowInstance){
			workflowInstance = (WorkflowInstance)workflowResult;
			logger.debug("执行工作流完成后，工作流实例:" + workflowInstance);
		} else if(workflowResult instanceof EisMessage){
			//FIXME 为了修改商品   工作流先注释掉
			/*EisMessage workflowMsg = (EisMessage)workflowResult;
			logger.error("无法完成创建产品的工作流:" + workflowMsg.getOperateCode() + "=> " + workflowMsg.getMessage());
			map.put("message", workflowMsg);
			return partnerMessageView;*/

		} else {
			logger.debug("创建产品不需要工作流参与");
		}
		if(product.getAvailableCount() > 0){
			product.setInitCount(product.getAvailableCount());
		}
		logger.debug("提交修改的产品:" + JsonUtils.getInstance().writeValueAsString(product));

		product.setId(product.getProductId());
		tagObjectRelationService.processTagForTag(product);

		//如果有工作流则不自动创建对应文档
		//boolean createDocument = ServletRequestUtils.getBooleanParameter(request, "createDocumentForProduct", false);

		/*if (product.getAvailableCount() == 0 || product.getAvailableCount() < 1) {
			logger.error("未提交产品剩余数量");			
		}
		if(product.getAvailableCount() > 0){
			product.setInitCount(product.getAvailableCount());
		}*/
		if(product.getCurrentStatus() == DocumentStatus.published.getId()){
			logger.debug("把产品状态从文档发布状态改为基本普通状态");
			product.setCurrentStatus(BasicStatus.normal.getId());
		}

		int update = productService.updateNoNull(product);
		logger.error("是否更新成功 ：" + update);
		try {
			if (update == 1) {
				map.put("message", new EisMessage(OperateResult.success.getId(), "更新成功"));
				PriceCriteria priceCriteria = new PriceCriteria();
				priceCriteria.setOwnerId(partner.getOwnerId());
				priceCriteria.setObjectType(ObjectType.product.name());
				priceCriteria.setObjectId(product.getProductId());
				List<Price> oldPriceList = priceService.list(priceCriteria);
				logger.debug("老价格 ： " + oldPriceList + " 价格个数 ：" + oldPriceList.size());
				if(oldPriceList != null && oldPriceList.size() > 0){
					for(Price oldPrice : oldPriceList){
						logger.debug("价格类型 ：" + oldPrice.getPriceType());
						if(oldPrice.getPriceType() != null && oldPrice.getPriceType().equals(PriceType.PRICE_STANDARD.toString())){
							logger.debug("删除产品[" + product.getProductId() + "]的老价格:" + oldPrice);
							priceService.delete(oldPrice.getPriceId());
						} else {
							logger.debug("不删除产品[" + product.getProductId() + "]的非标准老价格:" + oldPrice);

						}
					} 
				}
				if(priceList != null && priceList.size() > 0){
					for(Price price : priceList){
						price.setObjectType(ObjectType.product.name());
						price.setObjectId(product.getProductId());
						price.setOwnerId(product.getOwnerId());
						price.setCurrentStatus(BasicStatus.normal.getId());
						priceService.insert(price);
					}
				}
			}

		} catch (Exception e) {
			String m = "数据操作失败" + e.getMessage();
			e.printStackTrace();	
			throw new DataWriteErrorException(m);
		}


		tagObjectRelationService.processTagForTag(product);

		boolean createDocument = false;
		if(workflowInstance != null && workflowInstance.getCurrentRoute() != null){
			String postProcess = workflowInstance.getCurrentRoute().getPostProcess();
			if(NumericUtils.isNumeric(postProcess)){
				int post = NumericUtils.parseInt(postProcess);
				logger.debug("当前操作模式postProcess=" + post);
				if(post > 0){
					createDocument = true;
				} 
			}
		}

		if(workflowInstance != null){
			workflowInstance.setObjectId(product.getProductId());
			workflowInstanceService.closeCurrentStep(product, workflowInstance);
		}
		if(!createDocument){
			logger.debug("产品更新完成，当前工作流不创建或过呢更新对应的产品文档，流程结束");
			map.put("message", new EisMessage(OperateResult.success.getId(), "产品更新成功"));			
			return partnerMessageView;
		}
		int rs = _syncDocument(product, partner, request, map, null);
		logger.debug("更新产品关联文档结果:" + rs);

		if(rs == 1){
			map.put("message", new EisMessage(OperateResult.success.getId(), "产品和关联文档更新成功"));			
			return partnerMessageView;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(), "产品更新成功，但关联文档更新失败:" + rs));			

		return partnerMessageView;
	}

	private HashMap<Long, String> getSupplierMap() {

		List<User> supplierList = getSupplierList();

		HashMap<Long, String> supplierMap = new HashMap<Long, String>();
		supplierMap.put(-1l, "所有产品");
		supplierMap.put(0l, "内部产品");
		if (supplierList != null) {
			for (User partner : supplierList) {
				supplierMap.put(partner.getUuid(), partner.getUsername());
			}
		}
		return supplierMap;
	}

	private List<User> getSupplierList() {
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUserTypeId(UserTypes.partner.getId());
		userCriteria.setUserExtraTypeId(UserExtraType.accountChargeSupplier.getId());
		userCriteria.setCurrentStatus(UserStatus.normal.getId());
		List<User> supplierList = partnerService.list(userCriteria);
		if (supplierList == null) {
			return null;
		}
		return supplierList;
	}

	private List<Dict> dictList() {
		DictCriteria dictCriteria = new DictCriteria();
		return dictService.list(dictCriteria);
	}

	// 文件上传
	@SuppressWarnings("rawtypes")
	private int fileUpload(HttpServletRequest request, String mode, Product product) throws Exception {

		// 从Spring容器中获取对应的上传文件目录
		// String fileUploadSavePath =
		// ((FileSystemResource)this.getApplicationContext().getBean("uploadSaveDir")).getPath();
		// logger.info("Spring容器中的上传文件目录在:" + fileUploadSavePath);
		// logger.info("尝试为产品[udid="+udid+"]在[" + mode + "]模式下上传附件");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		// 获得文件： 
		Map<String, MultipartFile> map = multiRequest.getFileMap();
		logger.debug("上传的文件 ：" + map);
		logger.info("上传文件数量:" + (map == null ? "空" : map.size()));
		// 获得文件名：  
		Iterator its = multiRequest.getFileNames();
		logger.debug("ProductDataMap() :" + (product.getProductDataMap() == null ? "空" : product.getProductDataMap()));
		if (product.getProductDataMap() == null) {
			product.setProductDataMap(new HashMap<String,ProductData>());
		}
		int i = 0;
		int addCount = 0;
		int updateCount = 0;
		int ignoreCount = 0;
		while (its.hasNext()) {
			CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile((String) its.next());
			logger.debug("文件 ：" + file + "  /  " + file.getName() + "  / 名字 : " + file.getOriginalFilename() + "  文件大小 ：" + file.getSize());
			if (file.getSize() == 0) {
				if (i == 0) {
					continue;
				}
				break;
			}
			logger.debug("上传文件的名字 : " + file.getOriginalFilename());
			if (file.getOriginalFilename().isEmpty() || file.getOriginalFilename().equals("DELETE")) {
				continue;
			}
			String key = file.getName();
			logger.debug("key : " + key);
			String simpleKey = key.replaceAll("\\d+$", "");
			logger.debug("simpleKey : " + simpleKey);

			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setDataCode(simpleKey);
			dataDefineCriteria.setObjectType(ObjectType.product.toString());
			dataDefineCriteria.setObjectId(product.getProductTypeId());
			DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
			if (dataDefine == null) {// 不支持的上传文件名
				//查找确实是这个名字的dataDefine
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
			ProductData productData = new ProductData();
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
			//			ProductData existProductData = null;
			//			if (mode.equals(Operate.update.name()) && product.getProductDataMap() != null) {
			//				for (ProductData pd : product.getProductDataMap().values()) {
			//					if (pd.getDataDefineId() == dataDefine.getDataDefineId()) {
			//						existProductData = pd;
			//						break;
			//					}
			//				}
			//				if (existProductData != null) {
			//					fileDest = fileUploadSavePath + File.separator + existProductData.getDataValue();
			//					File _oldFile = new File(fileDest);
			//					_oldFile.delete();
			//					updateCount++;
			//				} else {
			//					fileDest = fileUploadSavePath + File.separator + fileName;
			//				}
			//
			//			} else {
			//				fileDest = fileUploadSavePath + File.separator + fileName;
			//			}
			fileDest = fileUploadSavePath + File.separator + fileName;
			logger.info("产品上传保存路径productUploadSaveDir:" + documentUploadSaveDir + ",产品文件上传保存路径fileUploadSavePath:" + fileUploadSavePath + ",文件名字fileName:" + fileName);
			File destDir = new File(fileDest).getParentFile();
			if(!destDir.exists()){
				logger.info("目标目录不存在，创建:" + fileUploadSavePath);
				FileUtils.forceMkdir(destDir);
			}
			productData.setDataDefineId(dataDefine.getDataDefineId());
			productData.setDataCode(dataDefine.getDataCode());
			productData.setOwnerId(product.getOwnerId());
			logger.info("保存产品数据[" + productData.getDataDefineId() + "/" + productData.getDataCode() + "的文件[" + file.getOriginalFilename() + "]到:" + fileDest + ",扩展数据的值设为:" + fileName);
			File dest = new File(fileDest);
			//保存  
			file.transferTo(dest);
			if(key.equals(simpleKey)){
				logger.debug("将单独的附件文件[" + key + "]保存到:" + fileName);
				productData.setDataValue(fileName);
			} else {
				if(product.getExtraValue(simpleKey) == null){
					productData.setDataValue(fileName);
				} else {
					productData = product.getProductDataMap().get(simpleKey);
					String fileList = productData.getDataValue();
					fileList += ",";
					fileList += fileName;
					productData.setDataValue(fileList);
					logger.debug("保存集合文件[" + simpleKey + "]，文件列表:" + fileList);
				}
			}
			/*if (mode.equals("edit") && existDocumentData != null) {
					// 已经更新了已存在附件的文件,无需其他操作
					continue;
				}*/



			product.getProductDataMap().put(dataDefine.getDataCode(), productData);
			addCount++;

		}
		int totalAffected = addCount + updateCount;
		String message = "完成附件上传,新增 " + addCount + " 个,更新 " + updateCount + " 个, 跳过 " + ignoreCount + " 个。";
		logger.info(message);
		return totalAffected;
	}
	private boolean haveRelatePrivilegeToNode(long uuid, int nodeId, String code) {
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria();
		privilegeCriteria.setUuid(uuid);
		privilegeCriteria.setObjectTypeCode(ObjectType.node.name());
		privilegeCriteria.setObjectId(String.valueOf(nodeId));
		privilegeCriteria.setOperateCode(Operate.relate.name());
		privilegeCriteria.setUserTypeId(UserTypes.partner.getId());
		return	authorizeService.havePrivilege(null,privilegeCriteria);
	}

	// 解除对编辑状态的锁定
	@RequestMapping(value = "/unLock")
	public String unLock(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long productId = ServletRequestUtils.getLongParameter(request, "productId", 0);
		if (productId < 1) {
			map.put("message", new EisMessage(EisError.dataError.getId(), "错误的Product对象"));
			return partnerMessageView;
		}


		String key = KeyConstants.PRODUCT_UPDATE_LOCK_PREFIX + "#" + productId;
		long unlock =	centerDataService.delete(key);
		logger.debug("解除对产品的编辑锁定[" + key + "]，结果:" + unlock);
		map.put("message", new EisMessage(OperateResult.success.getId(), "解锁结果:" + unlock));
		return partnerMessageView;
	}

	// 判断product_code是否唯一
	@RequestMapping(value = "/notify")
	public String confirm(HttpServletRequest request, HttpServletResponse response, ModelMap map ,Product product) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}

		Product confirmProduct = productService.select(product.getProductCode(), partner.getOwnerId());
		if (confirmProduct != null) {
			map.put("warning", "产品编码已存在,请重新输入");
		}
		return partnerMessageView;
	}

	/**
	 * 大文件
	 */
	@RequestMapping(value="/bigUpload", method=RequestMethod.POST)
	public void bigUpload(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam MultipartFile mof
			) throws Exception {
		String userUploadDir = configService.getValue(DataName.userUploadDir.toString(),0);
		if(userUploadDir != null){
			userUploadDir = userUploadDir.replaceAll("/$", "");
		}
		if(userUploadDir == null){
			userUploadDir = request.getSession().getServletContext().getRealPath("/userUpload");

			logger.warn("系统未定义用户保存目录，将文件保存到:" + userUploadDir);

		}
		FileUtils.forceMkdir(new File(userUploadDir));
		String 	fileUploadSavePath = userUploadDir.replaceAll("/$", "").replaceAll("\\$", "") + File.separator;


		String fileDest = fileUploadSavePath  + "hah";
		logger.debug("用户头像文件保存到:" + fileDest);

		File _oldFile = new File(fileDest);
		_oldFile.delete();

		File dest = new File(fileDest);
		mof.transferTo(dest);

	}
}


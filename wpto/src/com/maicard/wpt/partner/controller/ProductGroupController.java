package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.EisException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.domain.ProductGroup;
import com.maicard.product.service.ProductGroupService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 用于管理产品分组数据
 * @author GHOST
 * @date 2018-12-13
 *
 */
@Controller
@RequestMapping("/productGroup")
public class ProductGroupController extends BaseController{


	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private ProductGroupService productGroupService;
	@Resource
	private AuthorizeService authorizeService;
	
	@Resource
	private DocumentService documentService;




	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ProductGroupCriteria productGroupCriteria) throws Exception {
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
		if (isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.productGroup.name(), "w")) {
			map.put("addUrl", "./productGroup/create.shtml" );
		}
		final String view = "common/productGroup/index";
		productGroupCriteria.setOwnerId(partner.getOwnerId());
		//默认只需要列出第一级的分组
		/*if(productGroupCriteria.getLevel() == 0) {
			productGroupCriteria.setLevel(1);
		}*/
		int totalRows = productGroupService.count(productGroupCriteria);
		map.put("total", totalRows);

		if(totalRows < 1){
			logger.debug("当前返回的产品分组数是0");
			return view;
		}

		List<ProductGroup> productGroupList = productGroupService.list(productGroupCriteria);
		
		authorizeService.writeOperate(partner, productGroupList);
		
		map.put("rows",productGroupList);
		

		return view;
	}

	@RequestMapping(value="/get" + "/{productGroupId}", method=RequestMethod.GET )			
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("productGroupId") int productGroupId) throws Exception {
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
		ProductGroup productGroup = productGroupService.select(productGroupId);
		if(productGroup == null){
			throw new EisException(EisError.OBJECT_IS_NULL.id,"找不到ID=" + productGroupId + "的productGroup对象");			
		}
		if(productGroup.getOwnerId() != partner.getOwnerId()){
			throw new EisException(EisError.ownerNotMatch.id,"找不到ID=" + productGroupId + "的productGroup对象");			
		}
		map.put("productGroup", productGroup);
		return "productGroup/get";
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
			ProductGroup productGroup = productGroupService.select(deleteId);
			if(productGroup == null){
				logger.warn("找不到要删除的产品分组，ID=" + deleteId);
				continue;
			}
			if(productGroup.getOwnerId() != partner.getOwnerId() ){
				logger.warn("要删除的产品分组，ownerId[" + productGroup.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(productGroupService.delete(deleteId) > 0){
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

		int parentId = ServletRequestUtils.getIntParameter(request, "parentId", 0);
		ProductGroup parentNode = productGroupService.select(parentId);
		if(parentNode == null){
//			throw new ParentObjectNotFoundException("找不到对应的父产品分组:" + parentNodeId);
			logger.debug("找不到对应的父产品分组:" + parentId);
		}else{
			if(parentNode.getOwnerId() != ownerId){
				throw new ParentObjectNotFoundException("找不到对应的父产品分组:" + parentId);				
			}
			map.put("parentNodeLevel", parentNode.getLevel());
		}

		ProductGroupCriteria productGroupCriteria = new ProductGroupCriteria(ownerId);
		List<ProductGroup> productGroupList = productGroupService.list(productGroupCriteria);
		
		
		List<ProductGroup> productGroupTree = new ArrayList<ProductGroup>();
		if(productGroupList!=null){
			logger.info("共有" + productGroupList.size() + "个产品分组，有" + productGroupList.size() + "个有发布权限产品分组");
		}
		productGroupTree = productGroupService.generateTree(productGroupList);
		map.put("productGroupTree", productGroupTree);
		
		
		map.put("parentId", parentId);
		map.put("productGroupList", productGroupList);
		map.put("statusCodeList", BasicStatus.values());

		

		ProductGroup productGroup = new ProductGroup();
		productGroup.setParentId(parentId);
		

		map.put("productGroup", productGroup);
		return "common/productGroup/create";
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			ProductGroup productGroup) throws Exception {
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
		
		
		productGroup.setOwnerId(ownerId);
		
		if(productGroup.getParentId() > 0) {
			//得到父对象
			ProductGroup parentPg = productGroupService.select(productGroup.getParentId());
			if(parentPg == null) {
				logger.error("在添加新分组时，找不到其父分组:{}", productGroup.getParentId());
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"找不到父分组:" +  productGroup.getParentId()));
				return CommonStandard.partnerMessageView;			
			}
			productGroup.setObjectType(parentPg.getObjectType());
			productGroup.setGroupDesc(parentPg.getGroupDesc());
			productGroup.setObjectId(parentPg.getObjectId());
			productGroup.setLevel(parentPg.getLevel()+1);
		}

		if(StringUtils.isBlank(productGroup.getObjectType())) {
			productGroup.setObjectType(ObjectType.document.name());
		}
		if(productGroup.getObjectType().equalsIgnoreCase(ObjectType.document.name())) {
			//检查是否存在该对象
			Document document = documentService.select(NumericUtils.parseInt(productGroup.getGroupTarget()));
			if(document == null) {
				logger.error("找不到产品分组对应的文档:{}", productGroup.getGroupTarget());
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"找不到指定的文档:" +  productGroup.getGroupTarget()));
				return CommonStandard.partnerMessageView;				}
		}
		
		
	
		EisMessage message = null;
		try{
			logger.info("在分组[" + productGroup.getParentId() + "]下新增分组:" + JsonUtils.toStringFull(productGroup));
			if(productGroupService.insert(productGroup) > 0){
				message = new EisMessage(OperateResult.success.getId(), "产品分组添加成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "产品分组添加失败");				
			}

		}catch(Exception e){

			logger.error(ExceptionUtils.getFullStackTrace(e));
			String m = e.getMessage();
			if(m != null && m.indexOf("Duplicate entry") > 0){
				map.put("message", new EisMessage(EisError.dataDuplicate.id, "数据重复，请检查输入"));
				return view;		
			}
			map.put("message", new EisMessage(EisError.dataError.id, "无法新增产品分组"));
			return view;	
		}
		map.put("message", message);
		return view;		
	}
	
	/**
	 * 用来在缓存中创建一个最大的objectId
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-12-19
	 */
	@RequestMapping(value="/create/objectId", method=RequestMethod.POST)
	public String createMaxObjectId(HttpServletRequest request, HttpServletResponse response,ModelMap map) {
		
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

		//boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		
		
		// ////////////////////// 结束标准流程 ///////////////////////
		long maxObjectId = productGroupService.createMaxObjectId(ownerId);
		
		map.put("maxObjectId", maxObjectId);
		
		
		return view;
	}

	@RequestMapping(value="/update/{productGroupId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@PathVariable("productGroupId") int productGroupId) throws Exception {
		map.put("title", "编辑产品分组");
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

		ProductGroup productGroup = productGroupService.select(productGroupId);
		if(productGroup == null){
			throw new ParentObjectNotFoundException("找不到指定产品分组[" + productGroupId + "]");				
		}
		if(productGroup.getOwnerId() != ownerId){
			throw new ParentObjectNotFoundException("找不到指定产品分组[" + productGroupId + "]");				
		}
		
		
		

		//得到这个分组的上级分组
		ProductGroupCriteria productGroupCriteria = new ProductGroupCriteria(ownerId);
		productGroupCriteria.setLevel(productGroup.getLevel() > 1 ? productGroup.getLevel() - 1 : productGroup.getLevel());
		List<ProductGroup> productGroupList = productGroupService.list(productGroupCriteria);
		
		
		
		map.put("parentProductGroupList", productGroupList);
		map.put("statusCodeList", BasicStatus.values());

		map.put("productGroup", productGroup);
		
		
		
		
		return "common/productGroup/update";
	}

	
	@RequestMapping(value="/update", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("productGroup") ProductGroup productGroup) throws Exception {
		
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
		logger.info("产品分组提交");
		
		
		productGroup.setOwnerId(ownerId);
		
		
		
		//productGroup.setIncludeNodeSet(set);
		//logger.debug(productGroup.getIncludeNodeSet()+"");
		
		EisMessage message = null;
		try{			
			logger.info("更新产品分组[" + productGroup.getId() + "],父产品分组ID[" + productGroup.getParentId() + "]");
			if(productGroupService.update(productGroup) > 0){
				message = new EisMessage(OperateResult.success.getId(), "产品分组修改成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "产品分组修改失败");				
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

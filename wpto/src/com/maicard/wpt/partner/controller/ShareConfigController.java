package com.maicard.wpt.partner.controller;

import static com.maicard.standard.CommonStandard.partnerMessageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.ParentObjectNotFoundException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.service.PayMethodService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.ShareConfigType;

/**
 * 处理合作方的产品分成配置
 * 同时也可以用于确定一个合作方合作哪些产品
 *
 *
 * @author NetSnake
 * @date 2017年1月26日
 *
 */
@Controller
@RequestMapping("/shareConfig")
public class ShareConfigController extends BaseController{


	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;

	@Resource
	private PartnerService partnerService;
	@Resource
	private ShareConfigService shareConfigService;
	@Resource
	private AuthorizeService authorizeService;

	@Resource
	private ProductService productService;
	@Resource
	private DataDefineService dataDefineService;

	@Resource
	private PayMethodService payMethodService;

	final DecimalFormat df = new DecimalFormat("0.00");
	private int rowsPerPage = 10;

	private String DEFAULT_SHARE_OBJECT = "pay";





	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	/**
	 * 列出分成配置
	 * @param request
	 * @param response
	 * @param map
	 * @param shareConfigCriteria
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ShareConfigCriteria shareConfigCriteria) throws Exception {

		final String view = "common/shareConfig/index_pay";


		////////////////////////////标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");

		shareConfigCriteria.setOwnerId(ownerId);

		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(shareConfigCriteria, partner);
			shareConfigCriteria.setShareUuid(partner.getUuid());
		}
		//下面代码默认将分成对象设置为pay方式 
		//		if(StringUtils.isBlank(shareConfigCriteria.getObjectType())){
		//			shareConfigCriteria.setObjectType(DEFAULT_SHARE_OBJECT);
		//		}
		if(authorizeService.havePrivilege(partner, ObjectType.shareConfig.name(), "w")){
			map.put("addUrl", "/shareConfig/create.shtml?objectType=" + shareConfigCriteria.getObjectType());
		}
		logger.info("PayCardType验证前是 ：",shareConfigCriteria.getPayCardType());
		if(StringUtils.isEmpty(shareConfigCriteria.getPayCardType())){
			shareConfigCriteria.setPayCardType(null);
		}
		logger.info("PayCardType验证后是 ：",shareConfigCriteria.getPayCardType());
		int totalRows = shareConfigService.count(shareConfigCriteria);
		map.put("total", totalRows);


		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1); 
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		logger.debug("当前返回的分成配置数据是:" + totalRows);
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}



		Paging paging = new Paging(rows);
		shareConfigCriteria.setPaging(paging);
		shareConfigCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		shareConfigCriteria.setOrderBy(" share_config_id DESC ");
		List<ShareConfig> shareConfigList = shareConfigService.listOnPage(shareConfigCriteria);
		if(shareConfigList == null || shareConfigList.size() < 1){
			logger.debug("当前没有任何分成,ownerId=" + shareConfigCriteria.getOwnerId());
			return view;

		} 

		for(ShareConfig shareConfig : shareConfigList){
			shareConfig.setOperateValue("del", "aaaa");
			//下面根据分成对象类型修改数值
			if(shareConfig.getObjectType().equals(ShareConfigType.pay.name())
					|| shareConfig.getObjectType().equals(ShareConfigType.product.name())){
				User shareUser = partnerService.select(shareConfig.getShareUuid());
				if(shareUser == null){
					logger.warn("找不到分成配置的对应商户:" + shareConfig.getShareUuid());
					shareConfig.setOperateValue("shareUser", "未知/" + shareConfig.getShareUuid());
				} else {
					shareConfig.setOperateValue("shareUser", shareUser.getUsername() + "/" + shareConfig.getShareUuid());
				}
			}else if(shareConfig.getObjectType().equals(ShareConfigType.channel.name())){

				PayMethod payMethod = payMethodService.select((int)shareConfig.getShareUuid());
				if(payMethod == null){
					logger.warn("找不到分成配置的对应支付通道:" + shareConfig.getShareUuid());
					shareConfig.setOperateValue("shareUser", "未知/" + shareConfig.getShareUuid());
				} else {
					shareConfig.setOperateValue("shareUser", payMethod.getName() + "/" + shareConfig.getShareUuid());
				}

			}

		}




		authorizeService.writeOperate(partner, shareConfigList);

		map.put("rows",shareConfigList);

		return view;
	}

	/*	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ShareConfigCriteria shareConfigCriteria) throws Exception {
		////////////////////////////标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		final String view = "common/shareConfig/index";

		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(shareConfigCriteria, partner);
		}




		ProductCriteria productCriteria = new ProductCriteria(ownerId);
		productCriteria.setCurrentStatus(BasicStatus.normal.getId());

		int totalRows = productService.count(productCriteria);
		map.put("total", totalRows);


		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1); 
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		logger.debug("当前返回的产品数据是:" + totalRows);
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}

		String orderBy = ServletRequestUtils.getStringParameter(request, "order", DEFAULT_ORDER);

		productCriteria.setOrderBy(orderBy);

		Paging paging = new Paging(rows);
		productCriteria.setPaging(paging);
		productCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<Product> productList = productService.listOnPage(productCriteria);
		if(productList == null || productList.size() < 1){
			return view;
		}

		List<Product> productList2 = new ArrayList<Product>();

		shareConfigCriteria.setCurrentStatus(BasicStatus.normal.getId());
		shareConfigCriteria.setShareUuid(partner.getUuid());
		List<ShareConfig> shareConfigList = shareConfigService.list(shareConfigCriteria);
		for(Product product : productList){
			Product p2 = product.clone();
			if(shareConfigList == null || shareConfigList.size() < 1){
				logger.debug("当前没有任何分成,ownerId=" + shareConfigCriteria.getOwnerId());
			} else {		

				for(ShareConfig shareConfig : shareConfigList){
					if(shareConfig.getObjectId() == product.getProductId()){
						User shareUser = partnerService.select(shareConfig.getShareUuid());
						p2.setCurrentStatus(BasicStatus.relation.getId());
						p2.setOperateValue("shareConfigId",String.valueOf((shareConfig.getShareConfigId())));
						p2.setOperateValue("shareUser", shareUser.getUsername() + "/"  + shareConfig.getShareUuid());
						p2.setOperateValue("sharePercent",df.format(shareConfig.getSharePercent()));
						break;
					}
				}
			}
			productList2.add(p2);
		}
		authorizeService.writeOperate(partner, productList2);

		map.put("rows",productList2);


		return view;
	}*/


	@RequestMapping(value="/get" + "/{shareConfigId}", method=RequestMethod.GET )			
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("shareConfigId") int shareConfigId) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		ShareConfig shareConfig = shareConfigService.select(shareConfigId);
		if(shareConfig == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + shareConfigId + "的shareConfig对象");			
		}
		if(shareConfig.getOwnerId() != partner.getOwnerId()){
			throw new ObjectNotFoundByIdException("找不到ID=" + shareConfigId + "的shareConfig对象");			
		}

		//根据分成对象类型进行判断
		if(shareConfig.getObjectType().equals(ShareConfigType.pay.name())
				|| shareConfig.getObjectType().equals(ShareConfigType.product.name())){
			User shareUser = partnerService.select(shareConfig.getShareUuid());
			if(shareUser == null){
				logger.warn("找不到分成配置的对应商户:" + shareConfig.getShareUuid());
				shareConfig.setOperateValue("shareUser", "未知/" + shareConfig.getShareUuid());
			} else {
				shareConfig.setOperateValue("shareUser", shareUser.getUsername() + "/" + shareConfig.getShareUuid());
			}
		}else if(shareConfig.getObjectType().equals(ShareConfigType.channel.name())){
			PayMethod payMethod = payMethodService.select((int)shareConfig.getShareUuid());
			if(payMethod == null){
				logger.warn("找不到分成配置的对应支付通道:" + shareConfig.getShareUuid());
				shareConfig.setOperateValue("shareUser", "未知/" + shareConfig.getShareUuid());
			} else {
				shareConfig.setOperateValue("shareUser", payMethod.getName() + "/" + shareConfig.getShareUuid());
			}
		}


		if (shareConfig.getObjectType()!=null && shareConfig.getObjectType().equals(ObjectType.product.name())) {
			Product product = productService.select(shareConfig.getObjectId());
			if (product != null) {
				shareConfig.setOperateValue("productName", product.getProductName());
				shareConfig.setOperateValue("productId", product.getProductId()+"");
			}
		}


		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		UserCriteria partnerCriteria = new UserCriteria(ownerId);
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(partnerCriteria, partner);
		}


		List<Integer> statusList = new ArrayList<>();
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.id);
		}
		ShareConfigType[] scTypeArr = ShareConfigType.values();
		map.put("objectType", scTypeArr);
		map.put("statusList", statusList);

		//获取分成对象信息
		for(ShareConfigType shareConfigType : scTypeArr){
			if("pay".equals(shareConfigType.name())){
				List<User> subPartnerList = partnerService.list(partnerCriteria);
				map.put(shareConfigType.name(), subPartnerList);
			}else if("product".equals(shareConfigType.name())){
				List<User> subPartnerList = partnerService.list(partnerCriteria);
				map.put(shareConfigType.name(), subPartnerList);
				//查询产品信息
				ProductCriteria productCriteria = new ProductCriteria();
				productCriteria.setOwnerId(ownerId);
				List<Product> list = productService.list(productCriteria);
				map.put("productList", list);
			}else if("channel".equals(shareConfigType.name())){
				//查询通道信息
				PayMethodCriteria payMethodCriteria = new PayMethodCriteria();
				payMethodCriteria.setOwnerId(ownerId);
				payMethodCriteria.setCurrentStatus(BasicStatus.normal.getId());
				List<PayMethod> list = payMethodService.list(payMethodCriteria);
				map.put(shareConfigType.name(), list);
			}
		}
		map.put("shareConfig", shareConfig);
		return "common/shareConfig/get";
	}

	/**
	 * 删除一个已上架商品，即删除一个商户对一个产品的shareConfig
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST)
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
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");


		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			ShareConfig shareConfig = shareConfigService.select(deleteId);
			if(shareConfig == null){
				logger.warn("找不到要删除的分成配置，ID=" + deleteId);
				continue;
			}
			if(shareConfig.getOwnerId() != partner.getOwnerId() ){
				logger.warn("要删除的分成配置，ownerId[" + shareConfig.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			if(!isPlatformGenericPartner && shareConfig.getShareUuid() != partner.getOwnerId()){
				logger.warn("要删除的分成配置[" + shareConfig.getShareConfigId() + "],所属用户[" + shareConfig.getShareUuid() + "]与当前商户[" + partner.getUuid() + "]不一致");
				continue;
			}
			try{
				if(shareConfigService.delete(deleteId) > 0){
					successDeleteCount++;
				} 
			}catch(DataIntegrityViolationException forignKeyException ){
				String error  = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}

		}

		String messageContent = "成功下架[" + successDeleteCount + "]个.";
		if(!errors.equals("")){
			messageContent += errors;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));

		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)	
	public String onCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		////////////////////////////标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
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
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");

		UserCriteria partnerCriteria = new UserCriteria(ownerId);
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(partnerCriteria, partner);
		}

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ownerId);
		dataDefineCriteria.setObjectType(ObjectType.shareConfig.name());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		map.put("dataDefine", dataDefineList);


		map.put("statusCodeList", BasicStatus.values());

		ShareConfigType[] scTypeArr = ShareConfigType.values();
		map.put("objectType", scTypeArr);
		//获取分成对象信息
		for(ShareConfigType shareConfigType : scTypeArr){
			if("pay".equals(shareConfigType.name())){
				List<User> subPartnerList = partnerService.list(partnerCriteria);
				map.put(shareConfigType.name(), subPartnerList);
			}else if("product".equals(shareConfigType.name())){
				List<User> subPartnerList = partnerService.list(partnerCriteria);
				map.put(shareConfigType.name(), subPartnerList);
				//查询产品信息
				ProductCriteria productCriteria = new ProductCriteria();
				productCriteria.setOwnerId(ownerId);
				List<Product> list = productService.list(productCriteria);
				map.put("productList", list);
			}else if("channel".equals(shareConfigType.name())){
				//查询通道信息
				PayMethodCriteria payMethodCriteria = new PayMethodCriteria();
				payMethodCriteria.setOwnerId(ownerId);
				payMethodCriteria.setCurrentStatus(BasicStatus.normal.getId());
				List<PayMethod> list = payMethodService.list(payMethodCriteria);
				map.put(shareConfigType.name(), list);
			}
		}
		String objectType = ServletRequestUtils.getStringParameter(request, "objectType", DEFAULT_SHARE_OBJECT);
		//String view = "common/shareConfig/" + "create_" + objectType.toLowerCase();
		String view = "common/shareConfig/" + "create_" + "pay";
		return view;
	}


	/**
	 * 配置一个商品上架，即新建一个商户对一个产品的shareConfig
	 */	
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map ,ShareConfig shareConfig) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return partnerMessageView;
		}


		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		String isCreate = ServletRequestUtils.getStringParameter(request, "isCreate");
		if (StringUtils.isNotBlank(isCreate)) {
			//判断分成名称是否重复
			//			ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria();
			//			shareConfigCriteria.setShareConfigName(shareConfig.getShareConfigName());
			//			List<ShareConfig> shareConfigList = shareConfigService.list(shareConfigCriteria);
			//			if(shareConfigList != null && shareConfigList.size() > 0){
			//				//分成名称重复
			//				logger.error("无法新增分成配置，因为分成名称重复");
			//				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"分成名称重复"));
			//				return partnerMessageView;	
			//			}


			if (shareConfig.getObjectType()!=null && "product".equals(shareConfig.getObjectType())) {
				shareConfig.setCurrentStatus(BasicStatus.hidden.getId());
			}else {
				shareConfig.setCurrentStatus(BasicStatus.normal.getId());
			}
			shareConfig.setShareType("business");
			if (shareConfig.getSharePercent()>0) {
				shareConfig.setSharePercent(shareConfig.getSharePercent()/(float)100);
			}
		}else {
			long productId = ServletRequestUtils.getLongParameter(request, "productId", 0);
			if(productId < 1){
				logger.error("未提交正确的产品ID");
				map.put("message", "请提交一个产品");
				return partnerMessageView;
			}
			ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria(ownerId);
			shareConfigCriteria.setCurrentStatus(BasicStatus.hidden.getId());
			shareConfigCriteria.setObjectId(productId);
			shareConfigCriteria.setObjectType(ObjectType.product.name());
			shareConfigCriteria.setWeight((int)partner.getLevel());
			//获取该产品的默认分成配置
			List<ShareConfig> shareConfigList = shareConfigService.list(shareConfigCriteria);
			logger.debug("获取产品[" + productId + "]的默认分成配置得到的数据是:" + (shareConfigList == null ? "空" : shareConfigList.size()));
			if(shareConfigList == null || shareConfigList.size() < 1){
				logger.error("无法为产品配置分成，因为找不到默认分成配置");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到默认分成配置"));
				return partnerMessageView;	
			}
			shareConfig = shareConfigList.get(0).clone();
			shareConfig.setShareConfigId(0);
			shareConfig.setCurrentStatus(BasicStatus.normal.getId());
		}
		shareConfig.setOwnerId(ownerId);

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			//如果不是内部用户，则需要把分成配置的用户强制设为当前用户，以防止出现数据篡改
			shareConfig.setShareUuid(partner.getUuid());
		}
		EisMessage message = null;
		try{
			if(shareConfigService.insert(shareConfig) > 0){
				message = new EisMessage(OperateResult.success.getId(), "分成配置添加成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "分成配置添加失败");				
			}

		}catch(Exception e){

			logger.error(ExceptionUtils.getFullStackTrace(e));
			String m = e.getMessage();
			if(m != null && m.indexOf("Duplicate entry") > 0){
				map.put("message", new EisMessage(EisError.dataDuplicate.id, "数据重复，请检查输入"));
				return partnerMessageView;	
			}
			map.put("message", new EisMessage(EisError.dataError.id, "无法新增栏目"));
			return partnerMessageView;	
		}
		map.put("message", message);
		return partnerMessageView;	
	}

	@RequestMapping(value="/update/{shareConfigId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@PathVariable("shareConfigId") int shareConfigId) throws Exception {
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

		ShareConfig shareConfig = shareConfigService.select(shareConfigId);
		if(shareConfig == null){
			throw new ParentObjectNotFoundException("找不到指定分成配置[" + shareConfigId + "]");				
		}
		if(shareConfig.getOwnerId() != ownerId){
			throw new ParentObjectNotFoundException("找不到指定分成配置[" + shareConfigId + "]");				
		}





		map.put("statusCodeList", BasicStatus.values());



		map.put("shareConfig", shareConfig);
		return "common/shareConfig/update";
	}


	@RequestMapping(value="/update", method=RequestMethod.POST)	
	@AllowJsonOutput
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map, ShareConfig shareConfig) throws Exception {

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

		shareConfig.setOwnerId(ownerId);

		shareConfig.setOwnerId(ownerId);
		if (shareConfig.getObjectType() != null && "pay".equals(shareConfig.getObjectType())) {
			shareConfig.setObjectId(0);
		}
		if (shareConfig.getSharePercent()>0) {
			shareConfig.setSharePercent(shareConfig.getSharePercent()/(float)100);
		}
		EisMessage message = null;
		try{			
			if(shareConfigService.updateNoNull(shareConfig) > 0){
				message = new EisMessage(OperateResult.success.getId(), "分成配置修改成功");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "分成配置修改失败");				
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

package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
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

/**
 * 商户可以查看自己拥有的产品列表，管理员可以增删改查所有商户的产品列表
 * @author hailong
 * @date 2017-6-30
 *
 */
@Controller
@RequestMapping("/partnerProductRelation")
public class PartnerProductRelationController extends BaseController{
	
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ShareConfigService shareConfigService;
	@Resource
	private ProductService productService;
	
	
	
	private int rowsPerPage = 10;

	@PostConstruct
	public void init() {
		rowsPerPage = configService.getIntValue(
				DataName.partnerRowsPerPage.toString(), 0);
		if (rowsPerPage < 1) {
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@ModelAttribute("shareConfigCriteria")ShareConfigCriteria shareConfigCriteria)
			throws Exception {
		final String view = "common/partner/partnerProductRelation/index";
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
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
		// ////////////////////// 结束标准流程 ///////////////////////
		
		if (!isPlatformGenericPartner) {
			partnerService.setSubPartner(shareConfigCriteria, partner);
		}
		
		if (isPlatformGenericPartner && authorizeService.havePrivilege(partner, ObjectType.shareConfig.name(), "w")) {
			map.put("isShow", "show");
		}
		//状态码
		List<Integer> statusList = new ArrayList<>();
		for (BasicStatus basicStatus : BasicStatus.values()) {
			statusList.add(basicStatus.getId());
		}
		map.put("statusList", statusList);
		String currentStatus = ServletRequestUtils.getStringParameter(request,
				"currentStatus");
		int[] arr = new int[statusList.size()];
		for (int i = 0; i < statusList.size(); i++) {
			arr[i]=statusList.get(i);
		}
		if (StringUtils.isBlank(currentStatus) || Integer.parseInt(currentStatus)==0) {
			shareConfigCriteria.setCurrentStatus(arr);
		}
		// 分页功能
		int rows = ServletRequestUtils.getIntParameter(request, "rows",rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		
		//查询
		String username = ServletRequestUtils.getStringParameter(request, "username");
		if (StringUtils.isNotBlank(username)) {
			UserCriteria partnerCriteria = new UserCriteria();
			partnerCriteria.setUsername(username);
			if (shareConfigCriteria.getShareUuid()!=0) {
				partnerCriteria.setUuid(shareConfigCriteria.getShareUuid());
			}
			List<User> list = partnerService.list(partnerCriteria);
			if (list!=null && list.size()>0) {
				shareConfigCriteria.setShareUuid(list.get(0).getUuid());
			}else {
				List<ShareConfig> nullList=new ArrayList<>();
				map.put("shareConfig", nullList);
				return view;
			}
		}
		
		String productName = ServletRequestUtils.getStringParameter(request, "productName");
		String productCode = ServletRequestUtils.getStringParameter(request, "productCode");
		ProductCriteria productCriteria = new ProductCriteria();
		if (StringUtils.isNotBlank(productName)) {
			productCriteria.setOwnerId(ownerId);
			productCriteria.setProductName(productName);
			logger.debug("商品名称是:"+ productName + "++++++++++++++++++");
		}
		if (StringUtils.isNotBlank(productCode)) {
			productCriteria.setOwnerId(ownerId);
			productCriteria.setProductCode(productCode);
		}
		if (shareConfigCriteria.getObjectId()!=0) {
			productCriteria.setOwnerId(ownerId);
			productCriteria.setProductId((int)shareConfigCriteria.getObjectId());
		}
		List<Product> list = productService.list(productCriteria );
		if (list!=null && list.size()>0) {
			logger.debug("****************************************************");
			shareConfigCriteria.setObjectId(list.get(0).getProductId());
		}
		
		
		
		shareConfigCriteria.setPaging(paging);
		shareConfigCriteria.getPaging().setCurrentPage(page);
		shareConfigCriteria.setOwnerId(ownerId);
		shareConfigCriteria.setObjectType(ObjectType.product.name());
		List<ShareConfig> shareConfigs = shareConfigService.listOnPage(shareConfigCriteria);
		if (shareConfigs != null && shareConfigs.size()>0) {
			for (ShareConfig shareConfig : shareConfigs) {
				User user = partnerService.select(shareConfig.getShareUuid());
				if (user != null) {
					shareConfig.setOperateValue("username", user.getUsername());
				}
				Product product = productService.select(shareConfig.getObjectId());
				if (product != null) {
					shareConfig.setOperateValue("productName", product.getProductName());
					shareConfig.setOperateValue("productCode", product.getProductCode());
				}
			}
		}
		
		if (isPlatformGenericPartner) {
			UserCriteria partnerCriteria = new UserCriteria();
			partnerCriteria.setOwnerId(ownerId);
			partnerCriteria.setUserTypeId(121002);
			List<User> partnerList = partnerService.list(partnerCriteria );
			List<HashMap<String, Object>> maps = new ArrayList<>();
			for (User user : partnerList) {
				HashMap<String, Object> hashMap = new HashMap<>();
				hashMap.put("uuid", user.getUuid());
				hashMap.put("username", user.getUsername());
				maps.add(hashMap);
			}
			
			ProductCriteria productCriteria1 = new ProductCriteria();
			productCriteria1.setOwnerId(ownerId);
			productCriteria1.setCurrentStatus(BasicStatus.normal.getId());
			List<Product> list2 = productService.list(productCriteria1 );
			map.put("product", list2);
			map.put("partner", maps);
		}
		int totalRows = shareConfigService.count(shareConfigCriteria);
		map.put("total", totalRows);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		map.put("shareConfig", shareConfigs);
		return view;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createShareConfig(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,ShareConfig shareConfig) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		// //////////////////////标准流程 ///////////////////////
		User partner = certifyService.getLoginedUser(request, response,
				UserTypes.partner.getId());
		if (partner == null) {
			throw new UserNotFoundInRequestException();
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
		// ////////////////////// 结束标准流程 ///////////////////////
		EisMessage message = null;
		if (!isPlatformGenericPartner) {
			message=new EisMessage(OperateResult.failed.getId(), "没有创建权限");
			map.put("message", message);
			return view;
		}
		
		shareConfig.setCurrentStatus(BasicStatus.normal.getId());
		shareConfig.setObjectType(ObjectType.product.name());
		shareConfig.setOwnerId(ownerId);
		
		if (shareConfigService.insert(shareConfig) == 1) {
			message = new EisMessage(OperateResult.success.getId(), "操作完成");
		} else {
			message = new EisMessage(OperateResult.failed.getId(), "操作失败");
		}
		map.put("message", message);
		return view;
	}
	
	//删除功能
		@RequestMapping(value="/delete" + "/{shareConfigId}")		
		public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
				@PathVariable("shareConfigId") int shareConfigId) throws Exception {
			final String view = CommonStandard.partnerMessageView;
			////////////////////////标准流程 ///////////////////////
			User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
			if(partner == null){
				throw new UserNotFoundInRequestException();
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
			EisMessage message = null;
			if (!isPlatformGenericPartner) {
				message=new EisMessage(OperateResult.failed.getId(), "非内部用户,无法执行删除操作");
				map.put("message", message);
				return view;
			}
			
			if (shareConfigService.delete(shareConfigId) == 1) {
				message = new EisMessage(OperateResult.success.getId(), "操作完成");
			} else {
				message = new EisMessage(OperateResult.failed.getId(), "操作失败");
			}
			map.put("message", message);
			return view;
		}
}

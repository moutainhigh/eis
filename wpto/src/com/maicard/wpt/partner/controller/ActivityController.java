package com.maicard.wpt.partner.controller;

import static com.maicard.standard.CommonStandard.partnerMessageView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.service.TagService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.ServiceStatus;
import com.maicard.standard.SecurityStandard.UserExtraType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {

	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ActivityService activityService;
	@Resource
	private TagService tagService;
	@Resource
	private ProductService productService;
	@Resource
	private DataDefineService dataDefineService;

	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ActivityCriteria activityCriteria) throws Exception {
		final String view = "common/activity/list";
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = activityService.count(activityCriteria);
		map.put("total", totalRows);
		map.put("title", activityCriteria.getActivityType() != null ? activityCriteria.getActivityType() : "所有");
		String addUrl = "/activity/create" + CommonStandard.DEFAULT_PAGE_SUFFIX;
		if(activityCriteria.getActivityType() != null){
			addUrl = addUrl + "?activityType=" + activityCriteria.getActivityType();
			logger.debug("addUrl=" + addUrl);
		}
		map.put("addUrl", addUrl);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回的数据行数是0");
			return view;
		}

		Paging paging = new Paging(rows);
		activityCriteria.setPaging(paging);
		logger.debug("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<Activity> activityList = activityService.listOnPage(activityCriteria);
		if (activityList != null) {

		}
		map.put("rows", activityList);
		map.put("supplierMap", getSupplierMap());
		return view;
	}

	@RequestMapping(value = "/get/{activityId}")
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("activityId") Integer activityId,
			@ModelAttribute("productCriteria") ActivityCriteria activityCriteria) throws Exception {

		if (activityId == 0) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[ProductId]");
		}
		Activity activity = activityService.select(activityId);
		if (activity == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + activityId + "的Product对象");
		}
		ActivityCriteria activityCriteria1 = new ActivityCriteria();
		//activityCriteria1.setParentActivityId(activity.getActivityId());
		activityCriteria1.setOrderBy("a.face_money ASC");
		List<Activity> childactivityList = activityService.list(activityCriteria1);
		logger.info("产品[" + activity.getActivityId() + "]的子产品数量:"
				+ (childactivityList == null ? 0 : childactivityList.size()));
		map.put("childactivityList", childactivityList);
		// 查找该产品的地区
		/*ProductRegionRelationCriteria productRegionRelationCriteria = new ProductRegionRelationCriteria();
		productRegionRelationCriteria.setProductId(activity.getActivityId());
		// 查找充值服务器
		ProductServerCriteria productServerCriteria = new ProductServerCriteria();
		productServerCriteria.setProductId(activity.getActivityId());*/
		map.put("product", activity);
		return "common/product/get";
	}

	@RequestMapping("/delete")
	public String deleteB(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if (idList == null || idList.equals("")) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for (int i = 0; i < ids.length; i++) {
			try {
				if (activityService.delete(Integer.parseInt(ids[i])) > 0) {

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
		map.put("message",
				new EisMessage(OperateResult.success.getId(),
						messageContent));
		return CommonStandard.partnerMessageView;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String getCreate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam(value="activityType", required=false)String activityType) throws Exception {
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
			return CommonStandard.frontMessageView;		
		}

		if(partner.getOwnerId() != ownerId){
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return partnerMessageView;
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
				
		map.put("statusCodeList", statusList);
		Activity activity= activityService.generateNewActivity(activityType);
		map.put("activity",activity);
		String[] activityProcessorList = applicationContextService.getBeanNamesForType(ActivityProcessor.class);
		logger.debug("当前系统中的活动处理器有:" + (activityProcessorList == null ? "空" : activityProcessorList.length));
		map.put("activityProcessorList", activityProcessorList);
		
		String view = "common/activity/create";
		if(activityType != null){
			view += "_" + activityType;
		}
		return view;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			Activity activity) throws Exception {
		EisMessage message = null;
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.activity.name());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList == null || dataDefineList.size() < 1){
			logger.info("系统中没有活动[activity]的数据定义");
		} else {
			for(DataDefine dataDefine : dataDefineList){
				String extraValue = ServletRequestUtils.getStringParameter(request, "data." + dataDefine.getDataCode());
				if(StringUtils.isBlank(extraValue)){
					logger.debug("未提交活动的扩展数据:" + dataDefine.getDataCode());
					continue;
				}
				extraValue = extraValue.trim();
				logger.debug("为新建活动添加扩展数据:" + dataDefine.getDataCode() + "=>" + extraValue);
				activity.setExtraValue(dataDefine.getDataCode(), extraValue);
			}
		}
		try {
			if (activityService.insert(activity) > 0) {
				message = new EisMessage(
						OperateResult.success.getId(), "添加成功");
			} else {
				message = new EisMessage(
						OperateResult.failed.getId(), "添加失败");
			}
		} catch (Exception e) {
			String m = "数据操作失败" + e.getMessage();
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}
	// 编辑对象时的界面
	@RequestMapping(value = "/update/{activityId}", method = RequestMethod.GET)
	public String getUpdateB(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("activityId") Integer activityId) throws Exception {
		if (activityId == 0) {
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[ProductId]");
		}
		Activity activity = activityService.select(activityId);
		if (activity == null) {
			throw new ObjectNotFoundByIdException("找不到ID=" + activityId
					+ "的Product对象");
		}
		map.put("product", activity);
		map.put("statusCodeList", BasicStatus.values());
		map.put("serviceStatusCodeList", ServiceStatus.values());
		return "common/activity/update";
	}
	// 更新指定对象
	/*	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateB(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@ModelAttribute("Product") Activity activity) throws Exception {
		if (activity.getActivityId() < 1) {
			map.put("message",
					new EisMessage(Error.dataError.getId(),
							"错误的Product对象"));
			return CommonStandard.backMessageView;
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		//dataDefineCriteria.setObjectId(activity.geto);
		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		String ActivityData="";
		activity.setData(new HashMap<String, String>());
		for (String dataDefine : dataDefineList) {
			ProductData pd = new ProductData();
			pd.setactivityId(activity.getactivityId());
			DataDefineCriteria productDataDefineCriteria = new DataDefineCriteria();
			productDataDefineCriteria.setDataCode(dataDefine.getDataCode());
			if (ServletRequestUtils.getStringParameters(request,
					dataDefine.getDataCode()).length > 1) {
				for (int i = 0; i < ServletRequestUtils.getStringParameters(
						request, dataDefine.getDataCode()).length; i++)
					productDataStr = ServletRequestUtils.getStringParameters(
							request, dataDefine.getDataCode())[i]
							+ ","
							+ productDataStr;
			} else {
				productDataStr = ServletRequestUtils.getStringParameter(
						request, dataDefine.getDataCode());
			}
			pd.setCurrentStatus(BasicStatus.normal.getId());
			pd.setDataDefineId(dataDefine.getDataDefineId());
			pd.setDataValue(productDataStr);
			product.getProductDataMap().put(dataDefine.getDataCode(), pd);
		}
		try {
			if (activityService.update(activity) == 1) {
				map.put("message", new EisMessage(
						OperateResult.success.getId(), "更新成功"));
			}

		} catch (Exception e) {
			String m = "数据操作失败" + e.getMessage();
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return CommonStandard.backMessageView;
	}*/
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

	/*private void getActivityRefObject(Activity activity){
		if(activity.getObjectType() == null || !activity.getObjectType().equalsIgnoreCase(ObjectType.product.name())){
			logger.error("无法处理活动的引用对象:" + activity.getObjectType());
			return;
		}
		Product product = productService.select((int)activity.getObjectId());
		if(product == null){
			logger.error("找不到" + activity.getActivityId() + "#活动对应的产品:" + activity.getObjectId());
			return;
		}
		if(product.getOwnerId() < 1 || product.getOwnerId() != activity.getOwnerId()){
			logger.error(activity.getActivityId() + "#活动对应的产品:" + activity.getObjectId() + "，其ownerId:" + product.getOwnerId() + "与活动的ownerId:"  + activity.getOwnerId() + "不一致");
			return;
		}
		activity.setActivityDesc(product.getProductId() + "/" + product.getProductName());
	}*/
}

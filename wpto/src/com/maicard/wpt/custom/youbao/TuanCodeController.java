package com.maicard.wpt.custom.youbao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ContentPaging;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;



/**
 * 用户申请、查看自己的团购码
 * 跟随（加入）别人的团购码
 * 团购码用于在某些团购或预定活动中把几个用户作为一个群体处理
 *
 *
 * @author NetSnake
 * @date 2015-11-30
 *
 */
@Controller
@RequestMapping("/tuan")
public class TuanCodeController extends BaseController{


	@Resource
	private ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CartService cartService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private DocumentService documentService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;
	@Resource
	private UserRelationService userRelationService;

	private final int DEFAULT_ROWS_PER_PAGE_PC = 20;
	private final int DEFAULT_ROWS_PER_PAGE_MOBILE = 3;
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	//已经有人参与的团购码列表
	private HashMap<String, Integer> tuanCodeMap = new HashMap<String, Integer>();


	//列出当前用户拥有的团购商品
	@RequestMapping(value={"/index","/list"},method=RequestMethod.GET )
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws IOException {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
			return CommonStandard.frontMessageView;
		}
		Map<String,String> requestMap =	HttpUtils.getRequestDataMap(request);

		return _list(requestMap, frontUser, map, DeviceUtils.getCurrentDevice(request));
	}

	//列出当前用户拥有的团购商品，带列表
	@RequestMapping(value={"/list_{page}","/index_{page}"},method=RequestMethod.GET )
	public String listOnPage(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("page") String page) throws IOException {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
			return CommonStandard.frontMessageView;
		}
		Map<String,String> requestMap =	HttpUtils.getRequestDataMap(request);
		if(NumericUtils.isNumeric(page)){
			requestMap.put("page", page);
		}
		return _list(requestMap, frontUser, map, DeviceUtils.getCurrentDevice(request));
	}

	private String _list(Map<String, String> requestDataMap, User frontUser,	ModelMap map, Device device){
		int defaultRows = 0;
		if(device != null && device.isNormal()){
			defaultRows = DEFAULT_ROWS_PER_PAGE_PC;
		} else {
			defaultRows = DEFAULT_ROWS_PER_PAGE_MOBILE;
		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常"));
			return CommonStandard.frontMessageView;		
		}
		final String view = "tuanCode/list";

		int rows = 0;
		if(requestDataMap.get("rows") != null && NumericUtils.isNumeric(requestDataMap.get("rows"))){
			rows = Integer.parseInt(requestDataMap.get("rows"));
		}
		if(rows == 0){
			rows = defaultRows;
		}
		int page = 0;
		if(requestDataMap.get("page") != null && NumericUtils.isNumeric(requestDataMap.get("page"))){
			page = Integer.parseInt(requestDataMap.get("page"));
		}
		if(page == 0){
			page = 1;
		}

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setObjectType(DataName.tuanCode.toString());
		userRelationCriteria.setUuid(frontUser.getUuid());
		int totalRows = userRelationService.count(userRelationCriteria);
		map.put("total", totalRows);
		if(totalRows < 1){
			logger.debug("用户没有任何跟团购码相关的关联数据");
			return view;
		}
		ContentPaging documentContentPaging = new ContentPaging(totalRows);
		documentContentPaging.setRowsPerPage(rows);
		documentContentPaging.setCurrentPage(page);
		documentContentPaging.setDisplayPageCount(5);
		documentContentPaging.caculateDisplayPage();
		logger.debug("一共" + totalRows + "行数据, 每页显示" + rows + "行文章,当前第" + page + "页, 每页显示5个页码, 第一个页码:" + documentContentPaging.getDisplayFirstPage() + ",最后一个页码:" + documentContentPaging.getDisplayLastPage());
		map.put("documentContentPaging",documentContentPaging);


		Paging paging = new Paging(rows);
		userRelationCriteria.setPaging(paging);
		userRelationCriteria.getPaging().setCurrentPage(page);
		List<UserRelation> tuanCodeList = userRelationService.listOnPage(userRelationCriteria);
		List<Document>documentList = new ArrayList<Document>();
		//查找出对应的产品文章
		if(tuanCodeList != null && tuanCodeList.size() > 0){
			int index = 1;
			for(UserRelation userRelation : tuanCodeList){
				Document document = generateDocumentForTuan(userRelation);
				if(document == null){
					continue;
				}
				documentList.add(document);
				document.setIndex(index);
				index++;

			}
		}

		//	map.put("tuanCodeList", tuanCodeList);
		map.put("documentList", documentList);
		return view;	

	}



	//查看参与或创建的一个团详情
	@RequestMapping(value="/get/{tuanId}",method=RequestMethod.GET )
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("tuanId") String tuanCode) {
		final String view = "tuanCode/result";
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
			return CommonStandard.frontMessageView;
		}
		//获取
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setObjectType(DataName.tuanCode.toString());
		userRelationCriteria.setData(tuanCode);
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null || userRelationList.size() < 1){
			logger.info("找不到团购码[" + tuanCode + "]的任何记录");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id, "找不到指定的团购详情"));
			return CommonStandard.frontMessageView;
		}
		UserRelation userRelation = null;
		for(UserRelation u : userRelationList){
			if(u.getUuid() == frontUser.getUuid()){
				userRelation = u;
				break;
			}
		}
		if(userRelation == null){
			logger.info("没有找到用户[" + frontUser.getUuid() + "]与团购码[" + tuanCode + "]的关系");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id, "找不到指定的团购详情"));
			return CommonStandard.frontMessageView;
		}
		Date validTime = null;
		int ttl = 3600 * 24;
		List<User> tuanUserList = new ArrayList<User>();
		for(UserRelation ur : userRelationList){
			//查找这个团购码所有的建团、参团用户
			User tuanUser = frontUserService.select(ur.getUuid());
			if(tuanUser != null){
				User tu = new User();
				tu.setUuid(tuanUser.getUuid());
				tu.setCurrentStatus(ur.getCurrentStatus());
				if(tu.getCurrentStatus() == BasicStatus.normal.getId()){
					if(ur.getCreateTime() == null){
						logger.error("团[" + tuanCode + "]的创建记录[" + ur.getUserRelationId() + "]没有创建时间");
						validTime = new Date();
					} else {
						logger.debug("团[" + tuanCode + "]的创建时间是" + sdf.format(ur.getCreateTime()));
						validTime = ur.getCreateTime();
					}
				}
				tu.setNickName(tuanUser.getNickName() == null ? tuanUser.getUsername() : tuanUser.getNickName());
				tu.setCreateTime(ur.getCreateTime());
				tuanUserList.add(tu);
				if(tuanUser.getUserConfigMap() != null 
						&& tuanUser.getUserConfigMap().get(DataName.userHeadPic.toString()) != null 
					&& tuanUser.getUserConfigMap().get(DataName.userHeadPic.toString()).getDataValue() != null){
					tu.setMemory(tuanUser.getUserConfigMap().get(DataName.userHeadPic.toString()).getDataValue());
				}
			}
		}
		Collections.sort(tuanUserList, new Comparator<User>(){
			@Override
			public int compare(User o1, User o2) {
				if(o1.getCreateTime().before(o2.getCreateTime())){
					return -1;
				}
				return 1;
			}});
		validTime = DateUtils.addSeconds(validTime, ttl);
		logger.debug("将团[" + tuanCode + "]的有效期设置为:" + sdf.format(validTime));
		map.put("validTime", validTime);
		Document document = generateDocumentForTuan(userRelation);
		map.put("document", document);
		map.put("tuanUserList", tuanUserList);
		return view;

	}


	//请求创建一个团购码
	@RequestMapping(value="/create",method=RequestMethod.POST )
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
			return CommonStandard.frontMessageView;
		}

		return _create(HttpUtils.getRequestDataMap(request), frontUser, map);
	}

	private String _create(Map<String, String> requestDataMap, User frontUser,	ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;		
		}
		final String view = "code/tuan/create";

		String productCode = requestDataMap.get("productCode");
		if(StringUtils.isBlank(productCode)){
			logger.error("无法创建用户团购码，因为没有提交任何产品代码");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"请提交要创建团购码的产品代码"));
			return CommonStandard.frontMessageView;
		}

		Product product = productService.select(productCode, frontUser.getOwnerId());
		if(product == null){
			logger.error("找不到用户尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，产品代码错误"));
			return CommonStandard.frontMessageView;
		}
		String tuanActivityId = null;
		if(product.getProductDataMap() != null && product.getProductDataMap().get(DataName.tuanActivity.toString()) != null ){
			tuanActivityId = product.getProductDataMap().get(DataName.tuanActivity.toString()).getDataValue();
		}
		if(tuanActivityId == null){
			logger.error("用户尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]没有tuanActivity，不支持建团");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，产品代码错误"));
			return CommonStandard.frontMessageView;
		}
		if(!NumericUtils.isNumeric(tuanActivityId)){
			logger.error("用户尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]的tuanActivity不是数字:" + tuanActivityId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码错误"));
			return CommonStandard.frontMessageView;
		}
		Activity activity = activityService.select(Integer.parseInt(tuanActivityId));
		if(activity == null){
			logger.error("找不到尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]对应的活动:" + tuanActivityId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码错误"));
			return CommonStandard.frontMessageView;
		}	
		if(activity.getOwnerId() != frontUser.getOwnerId()){
			logger.error("活动[" + activity.getActivityId() + "]对应的ownerId与用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致:" + frontUser.getOwnerId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码"));
			return CommonStandard.frontMessageView;
		}	
		String activityBeanName = activity.getProcessor();

		if(StringUtils.isBlank(activityBeanName)){
			logger.error("活动[" + activity.getActivityId() + "]没有指定处理器");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}		
		Object bean = applicationContextService.getBean(activityBeanName);
		if(bean == null){
			logger.error("活动[" + activity.getActivityId() + "]指定的处理器不存在:" + activityBeanName);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}	
		if(! (bean instanceof ActivityProcessor)){
			logger.error("活动[" + activity.getActivityId() + "]指定的处理器不是一个ActivityProcessor:" + activityBeanName);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}	
		ActivityProcessor processor = (ActivityProcessor)bean;
		EisMessage resultMsg = processor.execute(Operate.create.getCode(), activity, frontUser, product);
		if(resultMsg == null){
			logger.debug("无法为用户[" + frontUser.getUuid() + "]生成产品[" + productCode + "]团购码，活动处理器返回空");
			map.put("message", new EisMessage(OperateResult.failed.getId(),"申请团购码失败"));
			return view;

		}
		logger.debug("为用户[" + frontUser.getUuid() + "]生成产品[" + productCode + "]团购码，结果:" + resultMsg.getOperateCode() + "/" + resultMsg.getMessage() + "/" + resultMsg.getContent());
		map.put("message", resultMsg);

		return view;

	}

	//请求跟随一个团购码参与团购
	@RequestMapping(value="/join",method=RequestMethod.POST )
	public String join(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		return _join(HttpUtils.getRequestDataMap(request), frontUser, map);
	}

	private String _join(Map<String, String> requestDataMap, User frontUser,	ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;		
		}
		final String view = "tuan/follow";

		String productCode = requestDataMap.get("productCode");
		if(StringUtils.isBlank(productCode)){
			logger.error("无法让用户跟随团购码，因为没有提交任何产品代码");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"请提交要参与团购的产品代码"));
			return CommonStandard.frontMessageView;
		}
		String joinCode = requestDataMap.get("code");
		if(StringUtils.isBlank(joinCode)){
			logger.error("无法跟随用户团购码，因为没有提交任何团购码");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"请提交一个要参与的团购码"));
			return CommonStandard.frontMessageView;
		}

		Product product = productService.select(productCode, frontUser.getOwnerId());
		if(product == null){
			logger.error("找不到用户尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，产品代码错误"));
			return CommonStandard.frontMessageView;
		}
		String tuanActivityId = null;
		if(product.getProductDataMap() != null && product.getProductDataMap().get(DataName.tuanActivity.toString()) != null ){
			tuanActivityId = product.getProductDataMap().get(DataName.tuanActivity.toString()).getDataValue();
		}
		if(tuanActivityId == null){
			logger.error("用户尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]没有bookingActivity，不支持预定");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，产品代码错误"));
			return CommonStandard.frontMessageView;
		}
		Activity activity = activityService.select(Integer.parseInt(tuanActivityId));
		if(activity == null){
			logger.error("找不到尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]对应的活动:" + tuanActivityId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码错误"));
			return CommonStandard.frontMessageView;
		}	
		if(activity.getOwnerId() != frontUser.getOwnerId()){
			logger.error("活动[" + activity.getActivityId() + "]对应的ownerId与用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致:" + frontUser.getOwnerId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码"));
			return CommonStandard.frontMessageView;
		}	
		String activityBeanName = activity.getProcessor();

		if(StringUtils.isBlank(activityBeanName)){
			logger.error("活动[" + activity.getActivityId() + "]没有指定处理器");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}		
		Object bean = applicationContextService.getBean(activityBeanName);
		if(bean == null){
			logger.error("活动[" + activity.getActivityId() + "]指定的处理器不存在:" + activityBeanName);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}	
		if(! (bean instanceof ActivityProcessor)){
			logger.error("活动[" + activity.getActivityId() + "]指定的处理器不是一个ActivityProcessor:" + activityBeanName);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}	
		ActivityProcessor processor = (ActivityProcessor)bean;
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("product", product);
		param.put("joinCode", joinCode);
		EisMessage resultMsg = processor.execute(Operate.join.getCode(), activity, frontUser, param);
		if(resultMsg == null){
			logger.debug("无法为用户[" + frontUser.getUuid() + "]生成产品[" + productCode + "]团购码，活动处理器返回空");
			map.put("message", new EisMessage(OperateResult.failed.getId(),"申请团购码失败"));
			return view;

		}
		if(resultMsg.getOperateCode() == OperateResult.success.getId()){
			//更新人数缓存
			if(tuanCodeMap.get(joinCode) != null){
				tuanCodeMap.put(joinCode, tuanCodeMap.get(joinCode)+1);
			}
		}
		logger.debug("为用户[" + frontUser.getUuid() + "]跟随产品[" + productCode + "]团购码，结果:" + resultMsg.getOperateCode() + "/" + resultMsg.getMessage() + "/" + resultMsg.getContent());
		map.put("message", resultMsg);

		return view;

	}


	//显示购买一个已参与的团购界面
	@RequestMapping(value="/buy",method=RequestMethod.GET )
	public String getBuy(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String productCode, String tuanCode) {

		final String view = "tuanCode/buy";
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response,UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setObjectType(DataName.tuanCode.toString());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setData(tuanCode);

		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null || userRelationList.size() < 1){
			logger.error("找不到用户[" + frontUser.getUuid() + "/" + frontUser.getOwnerId() + "]指定的团购码[" + tuanCode + "]的任何关联记录");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "找不到指定的团购码"));
			return CommonStandard.frontMessageView;
		}
		UserRelation userRelation = userRelationList.get(0);

		if(userRelation.getObjectId() < 1){
			logger.error("用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品ID=0");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "找不到指定的团购码产品"));
			return CommonStandard.frontMessageView;	
		}
		Product product = productService.select((int)userRelation.getObjectId());
		if(product == null){
			logger.error("找不到用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品:" + userRelation.getObjectId());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "找不到指定的团购码产品"));
			return CommonStandard.frontMessageView;
		}
		if(product.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品:" + userRelation.getObjectId() + "与系统ownerId[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "找不到指定的团购码产品"));
			return CommonStandard.frontMessageView;
		}

		Document d1 = documentService.select(product.getProductCode(), ownerId);
		logger.debug("用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]找到的文章是:" + d1);
		if(d1 == null){
			logger.error("找不到用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]相关文章");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "找不到指定的团购码产品文档"));
			return CommonStandard.frontMessageView;
		}
		Document document = d1.clone();
		if(document.getDocumentDataMap() == null){
			document.setDocumentDataMap(new HashMap<String,DocumentData>());
		}
		int productDataResult = productService.generateProductDocumentData(product,document);
		logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成扩展数据:" + productDataResult);


		if(product.getProductDataMap() != null && product.getProductDataMap().get(DataName.tuanActivity.toString()) != null){
			int tuanActivityId = NumericUtils.getNumeric(product.getProductDataMap().get(DataName.tuanActivity.toString()).getDataValue());
			if(tuanActivityId < 1){
				logger.error("找不到用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]关联的团购活动ID[tuanActivity]");
			} else {
				Activity tuanActivity = activityService.select(tuanActivityId);
				if(tuanActivity == null){
					logger.error("找不到用户[" + frontUser.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]关联的团购活动[tuanActivity=" + tuanActivityId + "]");
				} else 	if(tuanActivity.getData() == null || tuanActivity.getData().get(DataName.minJoinUserCount.toString()) == null){
					logger.error("产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]关联的团购活动[tuanActivity=" + tuanActivityId + "]未定义参与人数下限[minJoinUserCount]");
				} else {
					logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]对应的团购活动[" + tuanActivityId + "]为文档生成团购人数下限数据:" + tuanActivity.getData().get(DataName.minJoinUserCount.toString()));
					document.getDocumentDataMap().put(DataName.minJoinUserCount.toString(), new DocumentData(DataName.minJoinUserCount.toString(),tuanActivity.getExtraValue(DataName.minJoinUserCount.toString())));
				}
			}
		}
		//得到该团购活动的已参与人数
		if(tuanCodeMap.get(userRelation.getData()) == null){
			//还没有统计该数据，进行统计
			UserRelationCriteria userRelationCriteria2 = new UserRelationCriteria(frontUser.getOwnerId());
		//	userRelationCriteria2.setData(ExtraUtils.userRelation.getData());
			userRelationCriteria2.setObjectType(DataName.tuanCode.toString());
			int joinCount = userRelationService.count(userRelationCriteria2);
			logger.debug("团购码[" + userRelation.getData() + "]参与人数是:" + joinCount);
			//tuanCodeMap.put(userRelation.getData(), joinCount);
		}
		document.getDocumentDataMap().put(DataName.currentJoinUserCount.toString(), new DocumentData(DataName.currentJoinUserCount.toString(), String.valueOf(tuanCodeMap.get(userRelation.getData()))));



		boolean priceDataResult = 		priceService.generatePriceExtraData(document, PriceType.PRICE_TUAN.toString());
		logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成价格结果:" + priceDataResult);

		//document.getDocumentDataMap().put(DataName.tuanCode.toString(), new DocumentData(DataName.tuanCode.toString(), ExtraDataUtils.getStringExtraData(userRelation, DataName.tuanCode.toString()));
		logger.debug("把团购码[" + userRelation.getData() + "]放入" + document.getIndex() + "#文档:");

		map.put("document", document);

		return view;
	}


	//提交购买一个已参与的团购
	@RequestMapping(value="/submitBuy" )
	public String submitBuy(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String productCode, String tuanCode) {

		final String view = "tuanCode/tuanResult";
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常"));
			return CommonStandard.frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response,UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		boolean directBuy = ServletRequestUtils.getBooleanParameter(request, "directBuy", false);		
		boolean createNewCart = ServletRequestUtils.getBooleanParameter(request, "createNewCart", true);
		boolean ignoreDelivery = ServletRequestUtils.getBooleanParameter(request, "ignoreDelivery", false);

		logger.debug("当前购买团购商品订单模式[directBuy=" + directBuy + "，ignoreDelivery=" + ignoreDelivery + ",createNewCart=" + createNewCart + "]");
		if(StringUtils.isBlank(productCode)){
			logger.error("无法购买团购商品，因为没有提交任何产品代码");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"无法添加到购物车，因为没有提交任何产品代码"));
			return CommonStandard.frontMessageView;
		}
		String documentCode = request.getParameter("documentCode");
		if(StringUtils.isBlank(documentCode)){
			logger.warn("未提交产品文档的代码，使用产品代码[" + productCode + "]替代");
			documentCode = productCode;
		}
		//根据产品获取其需要输入的字段
		Product product = productService.select(productCode, frontUser.getOwnerId());
		if(product == null){
			//没找到对应的产品
			logger.error("找不到要团购的产品:" + productCode + ",ownerId=" + frontUser.getOwnerId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到对应的产品"));
			return CommonStandard.frontMessageView;		
		}
		if(product.getCurrentStatus() != BasicStatus.normal.getId()){
			logger.error("请求兑换的产品[" + productCode + "]状态异常:" + product.getCurrentStatus());
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "找不到产品[" + productCode + "]"));
			return CommonStandard.frontMessageView;		
		}
		if(product.getOwnerId() != frontUser.getOwnerId()){
			logger.error("请求兑换的产品[" + productCode + "]ownerId[" + product.getOwnerId() + "]与用户的[" + frontUser.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "找不到产品[" + productCode + "]"));
			return CommonStandard.frontMessageView;		
		}

		if(product.getAvailableCount() <= 0 ){
			logger.error("尝试购买的产品[" + productCode + "]数量为0");
			map.put("message", new EisMessage(EisError.stockEmpty.getId(), "产品[" + productCode + "]已售罄"));
			return CommonStandard.frontMessageView;		
		}

		String tuanActivityId = null;
		if(product.getProductDataMap() != null && product.getProductDataMap().get(DataName.tuanActivity.toString()) != null ){
			tuanActivityId = product.getProductDataMap().get(DataName.tuanActivity.toString()).getDataValue();
		}
		if(tuanActivityId == null){
			logger.error("用户尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]没有bookingActivity，不支持预定");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，产品代码错误"));
			return CommonStandard.frontMessageView;
		}
		Activity activity = activityService.select(Integer.parseInt(tuanActivityId));
		if(activity == null){
			logger.error("找不到尝试创建团购码的产品[" + productCode + ",ownerId=" + frontUser.getOwnerId() + "]对应的活动:" + tuanActivityId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码错误"));
			return CommonStandard.frontMessageView;
		}	
		if(activity.getOwnerId() != frontUser.getOwnerId()){
			logger.error("活动[" + activity.getActivityId() + "]对应的ownerId与用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致:" + frontUser.getOwnerId());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动代码"));
			return CommonStandard.frontMessageView;
		}	
		String activityBeanName = activity.getProcessor();

		if(StringUtils.isBlank(activityBeanName)){
			logger.error("活动[" + activity.getActivityId() + "]没有指定处理器");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}		
		Object bean = applicationContextService.getBean(activityBeanName);
		if(bean == null){
			logger.error("活动[" + activity.getActivityId() + "]指定的处理器不存在:" + activityBeanName);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}	
		if(! (bean instanceof ActivityProcessor)){
			logger.error("活动[" + activity.getActivityId() + "]指定的处理器不是一个ActivityProcessor:" + activityBeanName);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"无法创建团购码，团购活动错误"));
			return CommonStandard.frontMessageView;
		}	
		ActivityProcessor processor = (ActivityProcessor)bean;
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("product", product);
		param.put("tuanCode", tuanCode);
		EisMessage resultMsg = processor.execute(TransactionType.buy.getCode(), activity, frontUser, param);
		if(resultMsg == null){
			logger.debug("无法为用户[" + frontUser.getUuid() + "]购买团购产品[" + productCode + "]，活动处理器返回空");
			map.put("message", new EisMessage(OperateResult.failed.getId(),"购买团购商品失败"));
			return view;

		}
		if(resultMsg.getOperateCode() != OperateResult.success.getId()){
			logger.debug("无法为用户[" + frontUser.getUuid() + "]购买团购产品[" + productCode + "]，活动处理器返回错误:" + resultMsg.getOperateCode());
			map.put("message", new EisMessage(OperateResult.failed.getId(),"购买团购商品失败"));
			return view;
		}

		//生成新订单
		Item item = new Item(product);

		item.setTransactionTypeId(TransactionType.buy.getId());
		item.setTtl(product.getTransactionTtl());
		item.setProductId(product.getProductId());	
		item.setLabelMoney(product.getLabelMoney());
		item.setMaxRetry(product.getMaxRetry());
		item.setRequestMoney(product.getLabelMoney());
		item.setOwnerId(product.getOwnerId());	
		item.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));
		item.setInOrderId(documentCode);
		if(item.getItemDataMap() == null){
			item.setItemDataMap(new HashMap<String,ProductData>());
		}

		item.setName(product.getProductName());
		if(product.getSupplyPartnerId() != 0){
			logger.error("产品[" + productCode + "不是内部产品，不能对外销售");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "错误的请求，产品不对外"));
			return CommonStandard.frontMessageView;	
		}

		if(product.getProductDataMap() != null){
			logger.debug("产品[" + product.getProductId() + "]的数据规范有[" + product.getProductDataMap().size() + "]个");
			if(item.getItemDataMap() == null){
				item.setItemDataMap(new HashMap<String,ProductData>());
			}
			for(ProductData productData : product.getProductDataMap().values()){
				ProductData itemData = productData.clone();
				if(itemData.getDataCode().equals(DataName.productSmallImage.toString())){
					item.getItemDataMap().put(itemData.getDataCode(), itemData);
					continue;
				}
				String value = request.getParameter(itemData.getDataCode());
				if(StringUtils.isBlank(value)){
					continue;						
				}
				itemData.setDataValue(value.trim());
				item.getItemDataMap().put(productData.getDataCode(), itemData);
				logger.info("获取订单[" + item.getTransactionId() + "]提交的产品数据:" +  itemData.getDataCode() + "=" + itemData.getDataCode() + "/" + itemData.getDataValue());
			}
		} else {
			logger.warn("产品[" + product.getProductId() + "]没有任何数据定义");
		}

		item.setChargeFromAccount(frontUser.getUuid());
		item.setCurrentStatus(TransactionStatus.inCart.getId());
		int priceResult = priceService.applyPrice(item, PriceType.PRICE_PROMOTION.toString());
		if(priceResult != OperateResult.success.getId()){
			map.put("message", new EisMessage(EisError.moneyRangeError.getId(), "产品数据异常，无法加入购物车"));
			return CommonStandard.frontMessageView;	
		}
		EisMessage result = new EisMessage();
		ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria2.setChargeFromAccount(frontUser.getUuid());
		itemCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
		HashMap<String,Item> cartList = cartService.map(itemCriteria2);
		int count = 0;
		if(cartList != null){
			count = cartList.size();
		}
		result.setOperateResult(count);
		result.setMessage(String.valueOf(item.getCartId()));
		Cart cart = null;
		try {
			cart = cartService.add(item,createNewCart,null,0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("商品加入购物车结果:" + cart + ",指定创建新的购物车:" + createNewCart);
		if(cart != null){
			result.setOperateCode(OperateResult.success.getId());
			result.setMessage("商品已成功加入购物车");
		} else {

			result.setOperateCode(OperateResult.failed.getId());
			result.setMessage("商品无法加入购物车");	
		}
		map.put("message", result);
		if(directBuy){
			StringBuffer sb = new StringBuffer();
			if(ignoreDelivery){
				sb.append("/pointExchange/cart/settleUp");
			} else {
				sb.append("/pointExchange/cart/delivery");
			}
			sb.append(CommonStandard.DEFAULT_PAGE_SUFFIX);
			sb.append('?');
			sb.append("orderId=");
			sb.append(item.getTransactionId());
			sb.append("&ignoreDelivery=");
			sb.append(ignoreDelivery);

			logger.info("当前是直接购买模式，跳转到:" + sb.toString());
			map.put("jump", sb.toString());
		}

		return view;
	}
	private Document generateDocumentForTuan(UserRelation userRelation) {
		if(userRelation.getObjectId() < 1){
			logger.error("用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品ID=0");
			return null;
		}
		Product product = productService.select((int)userRelation.getObjectId());
		if(product == null){
			logger.error("找不到用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品:" + userRelation.getObjectId());
			return null;
		}
		if(product.getOwnerId() != userRelation.getOwnerId()){
			logger.error("用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品:" + userRelation.getObjectId() + "与系统ownerId[" + userRelation.getOwnerId() + "]不一致");
			return null;
		}

		Document d1 = documentService.select(product.getProductCode(), userRelation.getOwnerId());
		logger.debug("用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]找到的文章是:" + d1);
		if(d1 == null){
			logger.error("找不到用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]相关文章");

		}
		Document document = d1.clone();
		if(document.getDocumentDataMap() == null){
			document.setDocumentDataMap(new HashMap<String,DocumentData>());
		}
		int productDataResult = productService.generateProductDocumentData(product,document);
		logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成扩展数据:" + productDataResult);


		if(product.getProductDataMap() != null && product.getProductDataMap().get(DataName.tuanActivity.toString()) != null){
			int tuanActivityId = NumericUtils.getNumeric(product.getProductDataMap().get(DataName.tuanActivity.toString()).getDataValue());
			if(tuanActivityId < 1){
				logger.error("找不到用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]关联的团购活动ID[tuanActivity]");
			} else {
				Activity tuanActivity = activityService.select(tuanActivityId);
				if(tuanActivity == null){
					logger.error("找不到用户[" + userRelation.getUuid() + "]的团购码[" + userRelation.getData() + "]对应产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]关联的团购活动[tuanActivity=" + tuanActivityId + "]");
				} else 	if(tuanActivity.getData() == null || tuanActivity.getData().get(DataName.minJoinUserCount.toString()) == null){
					logger.error("产品[" + userRelation.getObjectId() + "/" + product.getProductCode() + "]关联的团购活动[tuanActivity=" + tuanActivityId + "]未定义参与人数下限[minJoinUserCount]");
				} else {
					logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]对应的团购活动[" + tuanActivityId + "]为文档生成团购人数下限数据:" + tuanActivity.getData().get(DataName.minJoinUserCount.toString()));
					document.getDocumentDataMap().put(DataName.minJoinUserCount.toString(), new DocumentData(DataName.minJoinUserCount.toString(),tuanActivity.getExtraValue(DataName.minJoinUserCount.toString())));
				}
			}
		}
		//得到该团购活动的已参与人数
		if(tuanCodeMap.get(userRelation.getData()) == null){
			//还没有统计该数据，进行统计
			UserRelationCriteria userRelationCriteria2 = new UserRelationCriteria(userRelation.getOwnerId());
		//	userRelationCriteria2.setData(userRelation.getData());
			userRelationCriteria2.setObjectType(DataName.tuanCode.toString());
			int joinCount = userRelationService.count(userRelationCriteria2);
			logger.debug("团购码[" + userRelation.getData() + "]参与人数是:" + joinCount);
		//	tuanCodeMap.put(userRelation.getData(), joinCount);
		}
		document.getDocumentDataMap().put(DataName.currentJoinUserCount.toString(), new DocumentData(DataName.currentJoinUserCount.toString(), String.valueOf(tuanCodeMap.get(userRelation.getData()))));

	//	document.getDocumentDataMap().put(DataName.tuanCode.toString(), new DocumentData(DataName.tuanCode.toString(), userRelation.getData()));


		boolean priceDataResult = 		priceService.generatePriceExtraData(document, PriceType.PRICE_TUAN.toString());
		logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成价格结果:" + priceDataResult);

		//document.getDocumentDataMap().put(DataName.tuanCode.toString(), new DocumentData(DataName.tuanCode.toString(), userRelation.getData()));
		logger.debug("把团购码[" + userRelation.getData() + "]放入" + document.getIndex() + "#文档:");
		return document;
	}
}

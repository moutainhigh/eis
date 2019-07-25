package com.maicard.wpt.custom.youbao;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PointExchangeLogCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 预定业务控制器
 *
 *
 * @author NetSnake
 * @date 2015年11月28日
 *
 */
@Controller
@RequestMapping("/booking")
public class BookingController extends BaseController{


	@Resource
	private ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ProductService productService;
	@Resource
	private FrontUserService frontUserService;



	//获取预定列表
	@RequestMapping(value="/list",method=RequestMethod.GET )
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

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

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		return _list(HttpUtils.getRequestDataMap(request), frontUser, map);
	}

	@RequestMapping(value="/listCrypt", method=RequestMethod.POST)
	public String listCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) {
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "找不到指定的用户"));
			return CommonStandard.frontMessageView;		
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}

		String cryptKey = null;
		try{
			cryptKey = frontUser.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		}catch(Exception e){
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if(StringUtils.isBlank(cryptKey)){
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try{
			clearData = crypt.aesDecrypt(cryptedData);
		}catch(Exception e){
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if(StringUtils.isBlank(clearData)){
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:" + cryptedData);
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view = _list(HttpUtils.getRequestDataMap(request), frontUser, map);
		/*if(map.get("withdrawList") != null){
			List<Withdraw> withdrawList = (List<Withdraw>)map.get("withdrawList");
			map.put("withdrawList",null);
			try {
				map.put("withdrawList", crypt.aesEncrypt(om.writeValueAsString(withdrawList)));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		return view;

	}
	private String _list(Map<String, String> requestDataMap, User frontUser,	ModelMap map){

		int rows = 0;
		int page = 1;
		PointExchangeLogCriteria withdrawCriteria = new PointExchangeLogCriteria();
		withdrawCriteria.setUuid(frontUser.getUuid());
		Paging paging = null;
		if(requestDataMap.get("rows") != null){
			try{
				rows = Integer.parseInt(requestDataMap.get("rows"));
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		if(rows == 0){
			paging = new Paging();
		} else {
			paging = new Paging(rows);
		}
		if(requestDataMap.get("page") != null){
			try{
				page = Integer.parseInt(requestDataMap.get("page"));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		paging.setCurrentPage(page);

		return "booking/list";	

	}
	//查询指定交易ID的提现状态
	@RequestMapping(value="/query/{transactionId}",method=RequestMethod.GET )
	public String query(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("transactionId") String transactionId) throws Exception {

		/*logger.debug("查询提现订单[" + transactionId + "]");
		Withdraw pay = withdrawService.select(transactionId);
		if(pay == null){
			map.put("message", new EisMessage(EisError.billNotExist.getId(), "订单不存在"));
		} else {
			EisMessage msg = new EisMessage();
			msg.setOperateCode(pay.getCurrentStatus());
			if(msg.getOperateCode() == TransactionStatus.success.getId()){
				msg.setMessage("交易成功");
			}
			if(msg.getOperateCode() == TransactionStatus.failed.getId()){
				msg.setMessage("交易失败");
			}

			map.put("message", msg);
		}*/
		return CommonStandard.frontMessageView;
	}


	//检查是否能进行预定
	@RequestMapping(value="/check", method=RequestMethod.POST)
	public String check(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

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

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		return _check(HttpUtils.getRequestDataMap(request), frontUser, map);

	}
	//检查是否能进行预定,加密形式
	@RequestMapping(value="/checkCrypt", method=RequestMethod.POST)
	public String checkCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) {

		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}

		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
			return CommonStandard.frontMessageView;
		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "找不到指定的用户"));
			return CommonStandard.frontMessageView;		
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
			return CommonStandard.frontMessageView;
		}
		String cryptKey = null;
		try{
			cryptKey = frontUser.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		}catch(Exception e){
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if(StringUtils.isBlank(cryptKey)){
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try{
			clearData = crypt.aesDecrypt(cryptedData);
		}catch(Exception e){
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if(StringUtils.isBlank(clearData)){
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:" + cryptedData);
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view =  _check(HttpUtils.getRequestDataMap(clearData), frontUser, map);
		return view;

	}

	private String _check(Map<String, String> requestDataMap, User frontUser,	ModelMap map){
		map.put("message", check(requestDataMap, frontUser));
		String view = "booking/check";
		return view;

	}


	//提交一次预定
	@RequestMapping(value="/submit", method=RequestMethod.POST)
	public String booking(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
			return CommonStandard.frontMessageView;
		}
		return _submit(HttpUtils.getRequestDataMap(request), frontUser, map);

	}
	//加密形式提交一次预定
	@RequestMapping(value="/submitCrypt", method=RequestMethod.POST)
	public String bookingCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) {

		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

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

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		if(frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
			return CommonStandard.frontMessageView;
		}
		String cryptKey = null;
		try{
			cryptKey = frontUser.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		}catch(Exception e){
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if(StringUtils.isBlank(cryptKey)){
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try{
			clearData = crypt.aesDecrypt(cryptedData);
		}catch(Exception e){
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if(StringUtils.isBlank(clearData)){
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:" + cryptedData);
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view =  _submit(HttpUtils.getRequestDataMap(clearData), frontUser, map);
		if(map.get("message") != null){
			EisMessage msg = (EisMessage)map.get("message");
			map.remove("message");
			try{
				String cryptedResult = crypt.aesEncrypt(JsonUtils.getInstance().writeValueAsString(msg));
				logger.info("对message进行加密:" + JsonUtils.getInstance().writeValueAsString(msg) + ",结果:" + cryptedResult);
				map.put("message", cryptedResult);
			}catch(Exception e){
				e.printStackTrace();
				map.put("message", new EisMessage(EisError.dataError.getId(), "系统异常"));
				return CommonStandard.frontMessageView;
			}
		}
		return view;

	}

	private String _submit(Map<String, String> requestDataMap, User frontUser,	ModelMap map){

		String view = "booking/submit";

		String productCode = null;

		if(StringUtils.isBlank(requestDataMap.get("productCode"))){
			logger.error("预定请求为未指定产品代码");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "未指定要预定的产品"));
			return view;
		}
		productCode = requestDataMap.get("productCode");




		Product product = productService.select(productCode, frontUser.getOwnerId());
		if(product == null){
			logger.error("找不到请求兑换的产品[" + productCode + "]");
			map.put("message", new EisMessage(EisError.productNotExist.getId(), "找不到要预定的产品[" + productCode + "]"));
			return view;
		}
		if(product.getCurrentStatus() != BasicStatus.normal.getId()){
			logger.error("请求兑换的产品[" + productCode + "]状态异常:" + product.getCurrentStatus());
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "找不到要预定的产品[" + productCode + "]"));
			return view;
		}
		if(product.getOwnerId() != frontUser.getOwnerId()){
			logger.error("请求兑换的产品[" + productCode + "]ownerId[" + product.getOwnerId() + "]与用户的[" + frontUser.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "找不到要预定的产品[" + productCode + "]"));
			return view;
		}

		if(product.getAvailableCount() <= 0 ){
			logger.error("请求预定的产品[" + productCode + "]数量为0");
			map.put("message", new EisMessage(EisError.stockEmpty.getId(), "产品[" + productCode + "]已售罄"));
			return view;
		}
		if(product.getSupplyPartnerId() != 0){
			logger.error("产品[" + productCode + "不是内部产品，不能对外销售");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "错误的请求，产品不对外"));
			return CommonStandard.frontMessageView;	
		}

		int bookingActivityId = 0;
		try{
			bookingActivityId = Integer.parseInt(product.getProductDataMap().get(DataName.bookingActivity.toString()).getDataValue());
		}catch(Exception e){
			logger.debug("产品[" + productCode + "]可能没有配置[" + DataName.bookingActivity.toString() + "]");
		}
		if(bookingActivityId < 1){

			logger.error("产品[" + productCode + "]未定义预定活动");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常"));
			return view;

		}
		//获取对应的活动处理器
		Activity activity = activityService.select(bookingActivityId);
		if(activity == null){
			logger.error("找不到产品[" + productCode + "]对应的预定活动:" + bookingActivityId);
			map.put("message",  new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常"));
			return view;
		}
		if(StringUtils.isBlank(activity.getProcessor())){
			logger.error("产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]未定义处理器");
			map.put("message",  new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常"));
			return view;
		}
		Object bean = applicationContextService.getBean(activity.getProcessor());
		if(bean == null){
			logger.error("找不到产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]的处理器:" + activity.getProcessor());
			map.put("message",  new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常"));
			return view;
		}
		if(!(bean instanceof ActivityProcessor)){
			logger.error("产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]的处理器[" +  activity.getProcessor() + "]类型不是活动处理器:" + bean.getClass().getName());
			map.put("message",  new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常"));
			return view;
		}
		ActivityProcessor processor = (ActivityProcessor)bean;
		EisMessage executeMsg = processor.execute(null, activity, frontUser, product);
		logger.debug("产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]的处理器[" +  activity.getProcessor() + "]返回预定结果是:" + executeMsg.getOperateCode());		
		map.put("message", executeMsg);
		return view;

	}

	private EisMessage check(Map<String,String> requestDataMap, User frontUser){
		String productCode = null;

		if(StringUtils.isBlank(requestDataMap.get("productCode"))){
			logger.error("预定请求为未指定产品代码");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "未指定要预定的产品");
		}
		productCode = requestDataMap.get("productCode");
		Product product = productService.select(productCode, frontUser.getOwnerId());
		if(product == null){
			logger.error("找不到请求兑换的产品[" + productCode + "]");
			return new EisMessage(EisError.productNotExist.getId(), "找不到要预定的产品[" + productCode + "]");
		}
		if(product.getCurrentStatus() != BasicStatus.normal.getId()){
			logger.error("请求兑换的产品[" + productCode + "]状态异常:" + product.getCurrentStatus());
			return new EisMessage(EisError.statusAbnormal.getId(), "找不到要预定的产品[" + productCode + "]");
		}
		if(product.getOwnerId() != frontUser.getOwnerId()){
			logger.error("请求兑换的产品[" + productCode + "]ownerId[" + product.getOwnerId() + "]与用户的[" + frontUser.getOwnerId() + "]不一致");
			return new EisMessage(EisError.statusAbnormal.getId(), "找不到要预定的产品[" + productCode + "]");
		}

		if(product.getAvailableCount() <= 0 ){
			logger.error("请求预定的产品[" + productCode + "]数量为0");
			return new EisMessage(EisError.stockEmpty.getId(), "产品[" + productCode + "]已售罄");
		}
		if(product.getSupplyPartnerId() != 0){
			logger.error("产品[" + productCode + "不是内部产品，不能对外销售");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(), "错误的请求，产品不对外");
		}
		int bookingActivityId = 0;
		try{
			bookingActivityId = Integer.parseInt(product.getProductDataMap().get(DataName.bookingActivity.toString()).getDataValue());
		}catch(Exception e){

		}
		if(bookingActivityId < 1 ){
			logger.error("产品[" + productCode + "]没有配置[" + DataName.bookingActivity.toString() + "]数据，无法预定");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(), "产品不支持预定");
		}
		//获取对应的活动处理器
		Activity activity = activityService.select(bookingActivityId);
		if(activity == null){
			logger.error("找不到产品[" + productCode + "]对应的预定活动:" + bookingActivityId);
			return new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常");
		}
		if(StringUtils.isBlank(activity.getProcessor())){
			logger.error("产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]未定义处理器");
			return new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常");
		}
		Object bean = applicationContextService.getBean(activity.getProcessor());
		if(bean == null){
			logger.error("找不到产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]的处理器:" + activity.getProcessor());
			return new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常");
		}
		if(!(bean instanceof ActivityProcessor)){
			logger.error("产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]的处理器[" +  activity.getProcessor() + "]类型不是活动处理器:" + bean.getClass().getName());
			return new EisMessage(EisError.systemDataError.getId(), "产品预定活动异常");
		}
		ActivityProcessor processor = (ActivityProcessor)bean;
		EisMessage preCheckMsg = processor.prepare(activity, frontUser, product);
		logger.debug("产品[" + productCode + "]对应的预定活动[" + bookingActivityId + "]的处理器[" +  activity.getProcessor() + "]返回检查结果是:" + preCheckMsg.getOperateCode());
		if(preCheckMsg.getOperateCode() == OperateResult.success.getId()){
			return new EisMessage(OperateResult.success.getId(), "产品可以预定");
		} 
		return preCheckMsg;

	}
}



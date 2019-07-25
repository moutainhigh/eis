package com.maicard.wpt.custom.youbao;


import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.HttpUtils;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.domain.ScanCodeInfo;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinRichTextMessage;
import com.maicard.wpt.service.AutoResponseModelService;
import com.maicard.wpt.service.WeixinMsgService;
import com.maicard.wpt.service.WeixinRichTextMessageService;

/**
 * 用于微信中扫描二维码/条码功能
 *
 * @author NetSnake
 * @date 2015年8月28日 
 */
@Controller
@RequestMapping(value = "/scanCode")
public class ScanCodeController extends BaseController{

	@Resource
	private ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private WeixinMsgService weixinMsgService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private WeixinRichTextMessageService weixinRichTextMessageService;
	@Resource
	private AutoResponseModelService autoResponseModelService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private ActivityLogService activityLogService;
	

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map
			){
		final String view = "scanCode/list";

		User frontUser = null;

		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		Activity activity=activityService.select(17);
		if (activity==null) {
			logger.info("该活动不存在");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "该活动不存在"));
			return CommonStandard.frontMessageView;
		}
		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		activityLogCriteria.setActivityId(17);
		activityLogCriteria.setUuid(frontUser.getUuid());
		activityLogCriteria.setCurrentStatus(BasicStatus.normal.getId());
		int activityLogCount = activityLogService.count(activityLogCriteria);
		logger.info("用户[" + frontUser.getUuid() + "的送u宝领取次数是:" + activityLogCount);
		if(activityLogCount <= 0){
			logger.info("没找到用户[" + frontUser.getUuid() + "]的领取记录，第一次领取");
			String promotion = activity.getPromotion();
			Money money = new Money();
			money.setUuid(frontUser.getUuid());
			
			boolean havePromotion = false;
			if(StringUtils.isBlank(promotion)){
				logger.info(activity.getActivityId() + "#活动没有奖励");
			} else {
				String[] basicPromotionData = promotion.split("#");
				if(basicPromotionData == null || basicPromotionData.length < 2 || !StringUtils.isNumeric(basicPromotionData[1])){
					logger.info(activity.getActivityId() + "#活动奖励数据错误:" + promotion);
				} else {
					if(basicPromotionData[0].equalsIgnoreCase(MoneyType.coin.name())){
						money.setCoin(Integer.parseInt(basicPromotionData[1]));
						havePromotion = true;
					}
					if(basicPromotionData[0].equalsIgnoreCase(MoneyType.point.name())){
						money.setPoint(Integer.parseInt(basicPromotionData[1]));
						havePromotion = true;
					}
					if(basicPromotionData[0].equalsIgnoreCase(MoneyType.score.name())){
						money.setScore(Integer.parseInt(basicPromotionData[1]));
						havePromotion = true;
					}
				}
				logger.info("送u宝活动,为新注册用户[" + frontUser.getUuid() + "]增加"+Integer.parseInt(basicPromotionData[1])+"个"+basicPromotionData[0]);
			}
			if(havePromotion){
				moneyService.plus(money);
			}
			map.put("message",  new EisMessage(OperateResult.success.getId(),"恭喜！签到成功！"));
		}
		else{
			logger.info("用户已经参加过送u宝活动");
			map.put("message",  new EisMessage(OperateResult.failed.getId(),"对不起，签到失败"));
		}
		WeixinRichTextMessage weixinRichTextMessage=weixinRichTextMessageService.select(17);
		map.put("songList",weixinRichTextMessage);
		return view;
	}

	@RequestMapping(value="submit", method = RequestMethod.POST)
	public String submitCode(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String code,
			String objectType,
			Long objectId,
			@RequestParam(value="codeType",required=false)String codeType){
		final String view = "scanCode/submit";

		User frontUser = null;

		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}

		if(frontUser == null){
			logger.error("提交扫码结果时，未找到用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		if(StringUtils.isBlank(code)){
			logger.info("提交的条码为空");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"所需要的代码为空"));
			return view;
		}
		logger.info("处理扫码结果[扫码类型:" + codeType + ",扫码数据:" + code + ",对象类型:" + objectType + ",对象ID:" + objectId + "]");
		final String testCode = "Netsnake789";
		if(code.equals(testCode)){
			logger.warn("输入条码是测试代码:" + code + ",返回成功");
			map.put("message", new EisMessage(OperateResult.success.getId(),"测试成功"));
			return view;

		}
		if(objectType == null){
			objectType = ObjectType.activity.name();
		}
		if(objectId == null){
			objectId = 2L;
		}
		WeixinMsg weixinMsg = new WeixinMsg();
		weixinMsg.setOwnerId(frontUser.getOwnerId());
		weixinMsg.setFromUserName(frontUser.getUsername());
		weixinMsg.setMsgType("Event");
		weixinMsg.setEventKey(objectType.toLowerCase() + "#" + objectId);

		weixinMsg.setCreateTime(new Date().getTime() / 1000);
		weixinMsg.setEvent("scancode_ajax");
		ScanCodeInfo scanCodeInfo = new ScanCodeInfo();
		scanCodeInfo.setScanType(codeType);
		scanCodeInfo.setScanResult(code);
		weixinMsg.setScanCodeInfo(scanCodeInfo);
		boolean saveMsg = true;
		int messageStatus = 0;
		if(objectType.equalsIgnoreCase(ObjectType.activity.name())){
			Activity activity = activityService.select(objectId);
			if(activity == null){
				logger.error("扫码相关活动[" + objectId + "]不存在");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"活动不存在"));
				return view;
			}
			if(activity.getCurrentStatus() != BasicStatus.normal.getId()){
				logger.error("扫码相关活动[" + objectId + "]状态异常:" + activity.getCurrentStatus());
				map.put("message", new EisMessage(EisError.statusAbnormal.getId(),"活动不存在"));
				return view;
			}
			if(StringUtils.isBlank(activity.getProcessor())){
				logger.error("扫码相关活动[" + objectId + "]未定义活动处理器");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"活动不存在"));
				return view;
			}

			ActivityProcessor activityProcessor = null;
			try{
				activityProcessor = (ActivityProcessor)applicationContextService.getBean(activity.getProcessor());
			}catch(Exception e){
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
			if(activityProcessor == null){
				logger.error("找不到扫码相关活动[" + objectId + "]定义的活动处理器:" + activity.getProcessor());
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),"活动不存在"));
				return view;
			}
			
			Map<String,String>requestData = HttpUtils.getRequestDataMap(request);
			requestData.put("orderId", code);
			EisMessage activityResult = activityProcessor.execute(null,activity, frontUser, requestData);
			logger.info("用户[" + frontUser.getUuid() + "/" + frontUser.getNickName() + "]扫码参与活动结果:" + activityResult.getOperateCode());
			messageStatus = activityResult.getOperateCode() ;

			map.put("message", activityResult);

		}
		if(saveMsg){
			weixinMsgService.insert(weixinMsg);
			weixinMsgService.insertAsUserMessage(weixinMsg, objectType.toLowerCase() + "#" + objectId, messageStatus);
		}
        /////////////////////////////////////////////////////////////////////////////
		
		return view;
	}
    
//	//回复推送消息
//		@RequestMapping(value="/giving", method=RequestMethod.GET)
//		public String giving(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
//			User frontUser = null;
//			try{
//				frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//
//			if(frontUser == null){
//				map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
//				return CommonStandard.frontMessageView;
//			}
//			return _giving(HttpUtils.getRequestDataMap(request), frontUser, map);
//
//		}
//		//回复推送消息,加密形式
//		@RequestMapping(value="/givingCrypt", method=RequestMethod.GET)
//		public String givingCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
//				@RequestParam("uuid")long uuid,
//				@RequestParam("data")String cryptedData) {
//
//			long ownerId = (long)map.get("ownerId");
//			if(ownerId < 1){
//				logger.error("系统会话中没有ownerId数据");
//				return CommonStandard.frontMessageView;		
//			}
//
//			User frontUser = frontUserService.select(uuid);
//			if(frontUser == null){
//				logger.warn("找不到用户[" + uuid + "]");
//				map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
//				return CommonStandard.frontMessageView;
//			}
//			if(frontUser.getOwnerId() != ownerId){
//				logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
//				map.put("message", new EisMessage(EisError.userNotFoundInSystem.getId(), "找不到指定的用户"));
//				return CommonStandard.frontMessageView;		
//			}
//			if(frontUser.getCurrentStatus() != UserStatus.normal.getId()){
//				logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
//				map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
//				return CommonStandard.frontMessageView;
//			}
//			String cryptKey = null;
//			try{
//				cryptKey = frontUser.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
//			}catch(Exception e){
//				logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
//				e.printStackTrace();
//			}
//			if(StringUtils.isBlank(cryptKey)){
//				logger.warn("找不到用户[" + uuid + "]的登录密钥");
//				map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
//				return CommonStandard.frontMessageView;
//			}
//			logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);
//
//			Crypt crypt = new Crypt();
//			crypt.setAesKey(cryptKey);
//			String clearData = null;
//			try{
//				clearData = crypt.aesDecrypt(cryptedData);
//			}catch(Exception e){
//				logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
//			}
//			if(StringUtils.isBlank(clearData)){
//				logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:" + cryptedData);
//				map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
//				return CommonStandard.frontMessageView;
//			}
//			String view =  _giving(HttpUtils.getRequestDataMap(clearData), frontUser, map);
//			JsonUtils.encrypt(map, crypt, "giving");
//			return view;
//
//		}
//
//		private String _giving(Map<String, String> requestDataMap, User frontUser,	ModelMap map){
//		    String language=requestDataMap.get("language");
//		    String view = "scanCode/giving";
//		    if (language.equals("")) {
//				logger.warn("固定语言为空"+language);
//				map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "发送固定语言为空，请检查您发送的固定语言是否正确"));
//				return CommonStandard.frontMessageView;
//			}
//		    //取出商家指定的固定语言
//		    AutoResponseModelCriteria autoResponseModelCriteria=new AutoResponseModelCriteria();
//		    autoResponseModelCriteria.setQuestion(language);
//		    autoResponseModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
//		    List<AutoResponseModel> autoList=autoResponseModelService.list(autoResponseModelCriteria);
//		    if(autoList == null || autoList.size() < 1){
//				logger.info("没找到对应的应答[" + autoList + "]");
//				map.put("message", new EisMessage(EisError.activeSignNotFound.getId(), "没找到对应的应答"));
//				return CommonStandard.frontMessageView;
//			} 
//		    AutoResponseModel autoResponseModel=autoList.get(0);
//		    if (autoResponseModel.getResponseType().equals("richTextMessage")) {
//		    	WeixinRichTextMessage content=weixinRichTextMessageService.select(autoResponseModel.getResponseId());
//			    if (content==null) {
//			    	logger.warn("该图文消息为空"+content);
//					map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "该活动不存在"));
//					return CommonStandard.frontMessageView;
//				}
//			    map.put("giving", content);
//			    map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "发送固定语言成功，恭喜您成为元惠通会员"));
//			}else{
//				logger.info("该应答不是富媒体");
//				map.put("message", new EisMessage(EisError.activeSignNotFound.getId(), "该应答不是富媒体"));
//			}
//		    
//			return view;
//		}
}

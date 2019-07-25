package com.maicard.wpt.custom.youbao;

import static com.maicard.standard.CommonStandard.frontMessageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.captcha.service.CaptchaService;
import com.maicard.common.base.BaseController;
import com.maicard.common.base.UUIDFilenameGenerator;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.domain.Location;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.domain.Uuid;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.DirtyDictService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.service.UuidService;
import com.maicard.common.util.AgentUtils;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.ShortMd5;
import com.maicard.common.util.StringTools;
import com.maicard.common.util.UserUtils;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.exception.ObjectFromServerIsNullException;
import com.maicard.exception.ServiceUnavailableException;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.OperateLogService;
import com.maicard.security.service.UserDataService;
import com.maicard.security.service.UserDynamicDataService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.SiteStandard.NodeType;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;
import com.maicard.wpt.service.VipService;

/**
 * 用户控制器
 * @author NetSnake
 * @date 2012-3-29
 */

@Controller
@RequestMapping("/user")
public class UserController  extends BaseController{	
	
	
	@Resource
	private NodeService nodeService;
	@Autowired(required=false)
	protected DeliveryOrderService deliveryOrderService;
	@Resource
	protected PriceService priceService;
	@Resource
	private CaptchaService captchaService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CookieService cookieService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private DirtyDictService dirtyDictService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private DocumentService documentService;
	

	@Resource
	private PayTypeService payTypeService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private ItemService itemService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private OperateLogService operateLogService;
	@Resource
	private PayService payService;
	@Resource
	private UuidService uuidService;
	@Resource
	private UserDataService userDataService;
	@Resource
	private UserDynamicDataService userDynamicDataService;
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private UserRelationService userRelationService;      
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private CacheService cacheService;
	@Resource
	private ProductService productService;
	@Resource
	protected CommentService commentService;
	
	@Autowired(required=false)
	private VipService vipService;
	
	
	
	
	
	
	private boolean siteRegisterNeedPatchca;
	private boolean mailActive;
	private boolean loginAfterRegister;
	private boolean needOldPassword;
	String welcomeSms;
	int showCaptchaWhenLoginFailCount;
	String bindMailTemplate;
	boolean autoCreateRandomNameWhenNullRegister;//当注册提交的用户名为空时，是否自动创建一个随机数来进行注册
	boolean generateLoginKeyForNewUser;	//是否生成一个新的登陆密钥给新注册用户
	boolean userRegisterNeedSmsSign;		//注册时是否需要短信验证
	private int feedBackTopicId = 0;
	private ObjectMapper om = new ObjectMapper();
	private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	private final String AUTO_CREATE_USER_PREFIX = "guest";

//	private int defaultRowsPerPage = 10;
//	private int maxIndexPerPage = 10;
	private int rowsPerPage = 10;
	//注册时手机验证码有效期
	private int SMS_VALID_SECOND = 120;

	private int sessionTimeout = CommonStandard.COOKIE_MAX_TTL;
	private final String DEFAULT_SMS_VALIDATE_MESSAGE = "短信验证码为${smsCode},请勿将验证码提供给他人。";
	private final String DEFAULT_FORGET_PASSWORD_SMS_VALIDATE_MESSAGE = "找回密码短信验证码为${smsCode},请勿将验证码提供给他人。";
	private final String DEFAULT_BIND_PHONE_VALIDATE_MESSAGE = "绑定手机短信验证码为${smsCode},请勿将验证码提供给他人。";

	private String userUploadDir;


	@PostConstruct
	public void init(){
		om.setDateFormat(sdf);

		siteRegisterNeedPatchca = configService.getBooleanValue(DataName.siteRegisterNeedPatchca.toString(),0);
		mailActive = configService.getBooleanValue(DataName.siteRegisterNeedMailActive.toString(),0);
		loginAfterRegister = configService.getBooleanValue(DataName.siteLoginAfterRegister.toString(),0);
		needOldPassword = configService.getBooleanValue(DataName.needOldPasswordWhenChangePassword.toString(),0);
		welcomeSms = configService.getValue(DataName.userRegisterWelcomeSms.toString(),0);
		showCaptchaWhenLoginFailCount = 1000;//configService.getIntValue(DataName.showCaptchaWhenLoginFailCount.toString(),0);
		bindMailTemplate = configService.getValue("userBindMailTemplate",0);
		//systemIsBusinessSystem = configService.getBooleanValue(DataName.systemIsBusinessSystem.toString(),0);
		//systemIsEcommerceSystem = configService.getBooleanValue(DataName.systemIsEcommerceSystem.toString(),0);
		autoCreateRandomNameWhenNullRegister = configService.getBooleanValue(DataName.autoCreateRandomNameWhenNullRegister.toString(),0);
		generateLoginKeyForNewUser = configService.getBooleanValue(DataName.generateLoginKeyForNewUser.toString(),0);
		feedBackTopicId = configService.getIntValue(DataName.feedBackTopicId.toString(),0);

		userUploadDir = configService.getValue(DataName.userUploadDir.toString(),0);
		if(userUploadDir != null){
			userUploadDir = userUploadDir.replaceAll("/$", "");
		}


	}

	//默认请求，给出登录用户的信息，及其他动态信息
	@RequestMapping(value={"","/index"},method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam(value="locationType", required=false)String locationType,
			@RequestParam(value="locationId", required=false)String locationId,
			@RequestParam(value="locationX", required=false)String locationX,
			@RequestParam(value="locationY", required=false)String locationY,
			@RequestParam(value="locationZ", required=false)String locationZ
			) throws Exception {
		map.put("totalUser", frontUserService.getTotalUser());		
		User frontUser = null;
		String rememberUserName = cookieService.getCookie(request, "remember" + CommonStandard.sessionUsername);
		if(rememberUserName != null){
			map.put("rememberUserName", rememberUserName);
		}		
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){

		}
		if(frontUser == null || frontUser.getUuid() < 1){				
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return "user/login";
		}

		//如果有提交用户当前位置数据，则进行更新
		if(StringUtils.isNotBlank(locationType) && StringUtils.isNotBlank(locationId)){
			syncUserLocation(frontUser.getUuid(), locationType, locationId, locationX, locationY, locationZ);
		}


		//放入支付方式
				PayTypeCriteria payTypeCriteria = new PayTypeCriteria(frontUser.getOwnerId());
				payTypeCriteria.setCurrentStatus(BasicStatus.normal.id);
				List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
				
				map.put("payTypeList", payTypeList);

	
		map.put("operate", OperateCode.GET_USER.toString());
		frontUser.setUserPassword(null);
		logger.debug("用户头像："+frontUser.getExtraValue("userHeadPic"));
		frontUser.setExtraValue("userHeadPic", frontUser.getExtraValue("userHeadPic"));
		map.put("frontUser", frontUser);		

		/* 资金帐户 */
		Money money = moneyService.select(frontUser.getUuid(), frontUser.getOwnerId());
		if(money != null){
			map.put("money", money);
		}

		//获取用户消息
		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setReceiverId(frontUser.getUuid());
		messageCriteria.setCurrentStatus(MessageStatus.unread.id);
		int newMessageCount = userMessageService.count(messageCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("用户未读消息:" + newMessageCount );
		}
		map.put("newMessageCount", newMessageCount);
		//获取u宝信息和签到天数
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setObjectType(ObjectType.sign.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		userRelationCriteria.setOwnerId(frontUser.getOwnerId());
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null){
			userRelationList = new ArrayList<UserRelation>();
		} else {
			//sortSignRelation(userRelationList);
		}

		userRelationCriteria.setBeginTime(DateUtils.truncate(new Date(), Calendar.MONTH));
		int signCount = userRelationService.count(userRelationCriteria);
		logger.info("用户[" + frontUser.getUuid() + "]自本月起[" + sdf.format(userRelationCriteria.getBeginTime()) + "]的签到次数是:" + signCount);
		map.put("signCount", signCount);

		
		
		return "user/index";
	}

	//显示用户资料修改界面
	@RequestMapping(value="/money", method = RequestMethod.GET)
	public String getMoney(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		User frontUser = null;

		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){

		}
		if(frontUser == null || frontUser.getUuid() < 1){

			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return "user/login";
		}
		Money money = moneyService.select(frontUser.getUuid(), frontUser.getOwnerId());
		if(money != null){
			map.put("money", money);
		} else {
			map.put("money", new Money());
		}
		return "user/money";

	}

	@RequestMapping(value="/payList", method = RequestMethod.GET)
	public String getPayList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;

		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){

		}
		if(frontUser == null || frontUser.getUuid() < 1){

			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return "user/login";
		}
		//支付记录
		PayCriteria payCriteria = new PayCriteria();
		payCriteria.setPayToAccount(frontUser.getUuid());
		int totalRows = payService.count(payCriteria);
		map.put("totalRows", totalRows);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		map.put("totalPage", (totalRows % rows > 0 ? (totalRows / rows + 1) : (totalRows / rows)) );
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		payCriteria.setPaging(paging);
		List<Pay> payList = payService.listOnPage(payCriteria);


		if(payList != null){
			SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider ().setFailOnUnknownId(false);
			simpleFilterProvider.addFilter("pay", SimpleBeanPropertyFilter.filterOutAllExcept("transactionId"));
			map.put("payList", payList);
		} else {
			map.put("payList", new ArrayList<Pay>());
		}
		payCriteria = null;
		return "user/money";

	}


	//修改用户资料，除了密码、用户名之外的其他信息
	@RequestMapping(value="/submitChangeOther", method=RequestMethod.POST)
	public String submitChangeOther(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			User modUser) throws Exception{
		logger.debug("收到资料修改请求");


		if(modUser == null){
			map.put("message", new EisMessage(EisError.dataError.id, "请提交正确的数据"));
			return frontMessageView;
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			logger.debug("未找到登录用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return frontMessageView;
		}

		frontUser = frontUserService.select(frontUser.getUuid());


		/*//获取用户自定义配置数据
		HashMap<String, UserData> userConfigMap = new HashMap<String, UserData>();
		Enumeration<String> parameters = request.getParameterNames();
		while(parameters.hasMoreElements()){
			String configName = parameters.nextElement();
			if(configName.startsWith(CommonStandard.requestUserConfigPrefix)){//是用户配置数据
				String configValue = request.getParameter(configName);
				configName = configName.replaceFirst(CommonStandard.requestUserConfigPrefix, "");
				UserData uc = new UserData();
				uc.setUuid(frontUser.getUuid());
				uc.setCurrentStatus(BasicStatus.normal.id);
				uc.setDataCode(configName);
				uc.setDataValue(configValue);
				logger.debug("得到了请求中的用户配置数据:" + configName + ":" + configValue + "/" + uc.getCurrentStatus());
				userConfigMap.put(configName, uc);
			}
		}
		frontUser.setUserConfigMap(userConfigMap);*/
		frontUser.setUserPassword(null);
		modUser.setUserPassword(null);
		modUser.setUuid(frontUser.getUuid());
		modUser.setExtraValue("userHeadPic", frontUser.getExtraValue("userHeadPic"));
		
		String userBindPhoneNumber = ServletRequestUtils.getStringParameter(request, "userBindPhoneNumber");
		String userBindMailBox = ServletRequestUtils.getStringParameter(request, "userBindMailBox");
		if(userBindPhoneNumber!=null){
			modUser.setExtraValue(DataName.userBindPhoneNumber.toString(), userBindPhoneNumber);
		}
		if(userBindMailBox!=null){
			modUser.setExtraValue(DataName.userBindMailBox.toString(), userBindMailBox);
		}
		//修改用户个人简介微博等信息资料
		String user_url = ServletRequestUtils.getStringParameter(request, "user_url");
		String userDescription = ServletRequestUtils.getStringParameter(request, "userDescription");
		String qq_weibo = ServletRequestUtils.getStringParameter(request, "qq_weibo");
		String sina_weibo = ServletRequestUtils.getStringParameter(request, "sina_weibo");
		modUser.setExtraValue("userUrl", user_url);
		modUser.setExtraValue("userDescription", userDescription);
		modUser.setExtraValue("qq_weibo", qq_weibo);
		modUser.setExtraValue("sina_weibo", sina_weibo);
		logger.debug("修改相关信息：sina_weibo："+sina_weibo+"#"+"user_url:"+user_url+"#"+"qq_weibo:"+qq_weibo+"#"+"userDescription:"+userDescription);
		
		//frontUserService.correctUserConfig(frontUser);
		frontUserService.correctUserAttributes(modUser);
		EisMessage es = frontUserService.update(modUser);
		logger.debug("资料修改已提交。结果是："+es);
		
		map.put("message",  es);
		refreshFrontUser(request, response, frontUser);
		return frontMessageView;
	}
	/**更新用户消息设置
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @param uuid
	 * @param allowOtherToSelf
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateMessageSet", method=RequestMethod.POST)
	public String updateMessageSet(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@RequestParam("uuid")long uuid,
			@RequestParam("allowOtherToSelf")String allowOtherToSelf) throws Exception{
		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		frontUser.setExtraValue("allowOtherToSelf", allowOtherToSelf);
		frontUserService.update(frontUser);
		EisMessage em = frontUserService.update(frontUser);
		map.put("message", em);
		return frontMessageView;
	}
	//修改用户资料
	@RequestMapping(value="/updateCrypt/data", method=RequestMethod.POST)
	public String updateDataCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) throws Exception{

		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
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
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
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
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}

		Map<String,String> requestDataMap = HttpUtils.getRequestDataMap(clearData);
		ClassUtils.bindBeanFromMap(frontUser, requestDataMap);
		logger.info("用户昵称:" + frontUser.getNickName());
		//获取用户自定义配置数据
		HashMap<String, UserData> userConfigMap = new HashMap<String, UserData>();
		for(String key : requestDataMap.keySet()){
			if(key.startsWith(CommonStandard.requestUserConfigPrefix)){//是用户配置数据
				String configName = key.replaceFirst(CommonStandard.requestUserConfigPrefix, "");
				UserData uc = new UserData();
				uc.setUuid(frontUser.getUuid());
				uc.setCurrentStatus(BasicStatus.normal.id);
				uc.setDataCode(configName);
				uc.setDataValue(requestDataMap.get(key).trim());
				logger.debug("得到了请求中的用户配置数据:" + configName + ":" + uc.getDataValue() + "/" + uc.getCurrentStatus());
				userConfigMap.put(configName, uc);
			}
		}
		frontUser.setUserConfigMap(userConfigMap);
		frontUser.setUserPassword(null);
		frontUserService.correctUserConfig(frontUser);
		frontUserService.correctUserAttributes(frontUser);

		logger.debug("资料修改已提交。");

		EisMessage msg = frontUserService.update(frontUser);
		String cryptedResult = crypt.aesEncrypt(om.writeValueAsString(msg));
		logger.info("对message进行加密:" + om.writeValueAsString(msg) + ",结果:" + cryptedResult);
		map.put("message", cryptedResult);
		return frontMessageView;
	}

	//修改用户密码
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String submitChangePassword(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("password1")String password1,
			@RequestParam("password2")String password2,
			@RequestParam(value="oldPassword", required=false) String oldPassword
			) throws Exception{

		if(StringUtils.isBlank(password1)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入正确的数据"));
			return frontMessageView;
		}
		if(StringUtils.isBlank(password2)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入正确的数据"));
			return frontMessageView;
		}

		if(!password1.equals(password2)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "两次输入不一致"));
			return frontMessageView;
		}


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}



		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			logger.debug("未找到登录用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return frontMessageView;
		}

		User _oldUser = frontUserService.select(frontUser.getUuid());
		if(_oldUser == null){
			logger.error("根据UUID=" + frontUser.getUuid() + "找不到前端用户");
			map.put("message", new EisMessage(OperateResult.failed.id, "系统异常"));
			return frontMessageView;
		}
		/* 修改密码是否需要提供旧密码
		 * 如果需要，则检查是否更改了密码
		 * 如果更改，则检查提供了旧密码
		 * 并进一步检查就密码是否与之前的一致
		 */
		if(needOldPassword){
			if(StringUtils.isBlank(oldPassword)){
				logger.error("修改密码需要提供原密码但用户未提交");

				map.put("message", new EisMessage(OperateResult.failed.id, "请提供旧的密码"));
				return frontMessageView;
			}


			//检查旧密码是否正确
			if(logger.isDebugEnabled()){
				logger.debug("尝试修改密码，提交的旧密码是" + oldPassword);
			}
			oldPassword = Crypt.passwordEncode(oldPassword);

			if(!_oldUser.getUserPassword().equals(oldPassword)){
				if(logger.isInfoEnabled()){
					logger.info("用户提交的旧密码[" + oldPassword + "]与之前的密码[" + _oldUser.getUserPassword() + "]不一致");
				}
				map.put("message", new EisMessage(OperateResult.failed.id, "旧密码输入错误"));
				return frontMessageView;
			}

		} 
		//只修改密码
		_oldUser.setUserPassword(Crypt.passwordEncode(password1));
		map.put("message", frontUserService.update(_oldUser));
		//修改密码后尝试退出
		certifyService.logout(request, response, _oldUser);;
		frontUser = _oldUser;

		return frontMessageView;
	}

	//显示用户资料修改界面
	@RequestMapping(value="/update/data", method = RequestMethod.GET)
	public String getData(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		//map.put("systemName", configService.getValue("systemName"));
		return "user/data";

	}

	//显示用户密码修改界面
	@RequestMapping(value="/update/security",method = RequestMethod.GET)
	public String getSecurity(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		//map.put("systemName", configService.getValue("systemName"));
		return "user/security";

	}



	//经邮箱找回密码
	@RequestMapping(value="/findPassword/email/submit", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String submitFindPasswordByMail(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		String username = request.getParameter("username");
		if(username == null || username.equals("")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "参数错误"));
			return frontMessageView;
		}
		User frontUser = null;
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(username);
		frontUserCriteria.setCurrentStatus(UserStatus.normal.id);
		List<User> frontUserList = frontUserService.list(frontUserCriteria);
		if(frontUserList == null || frontUserList.size() != 1){
			//用户名不存在这样的邮箱，尝试查找绑定邮箱，并通过该邮箱找出用户

			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria.setDataCode(DataName.userBindMailBox.toString());
			userDataCriteria.setDataValue(username);
			userDataCriteria.setCorrectWithDynamicData(false);
			userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
			List<UserData> userDataList = userDataService.list(userDataCriteria);
			if(userDataList != null && userDataList.size() == 1){
				if(userDataList.get(0).getUuid() > 0){
					frontUser = frontUserService.select(userDataList.get(0).getUuid());
					if(frontUser != null){
						if(frontUser.getCurrentStatus() != UserStatus.normal.id){
							frontUser = null;
						}
					}
				}

			}

		} else {
			frontUser = frontUserList.get(0);
			UserData userBindMailBox = new UserData();
			userBindMailBox.setDataCode(DataName.userBindMailBox.toString());
			userBindMailBox.setDataValue(frontUser.getUsername());
			userBindMailBox.setUuid(frontUser.getUuid());
			frontUser.getUserConfigMap().put(DataName.userBindMailBox.toString(),userBindMailBox);
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "用户不存在"));
			return frontMessageView;
		}

		String mailFindPasswordSign = Crypt.passwordEncode(CommonStandard.mailFindPasswordKey + frontUser.getUuid());
		if(frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String,UserData>());
		}
		UserData findPasswordConfig = new UserData();
		findPasswordConfig.setDataCode(DataName.userMailFindPasswordSign.toString());
		findPasswordConfig.setDataValue(mailFindPasswordSign);
		findPasswordConfig.setUuid(frontUser.getUuid());
		frontUser.getUserConfigMap().put(DataName.userMailFindPasswordSign.toString(),findPasswordConfig);
		/*if(frontUser.getUserConfigMap().get(Constants.DataName.userBindMailBox.toString()) == null){
			UserData bindMailConfig = new UserData();
			bindMailConfig.setDataCode(Constants.DataName.userBindMailBox.toString());
			bindMailConfig.setDataValue(username);
			frontUser.getUserConfigMap().put(Constants.DataName.userBindMailBox.toString(), bindMailConfig);
		}*/


		frontUserService.update(frontUser);
		String findPasswordUrl = request.getScheme() + "://";
		findPasswordUrl += request.getServerName();
		findPasswordUrl += (request.getServerPort() == 80 ? "" : request.getServerPort());
		findPasswordUrl += "/content/help/editNewPasswordByEmail" + CommonStandard.DEFAULT_PAGE_SUFFIX + "?uuid="  + frontUser.getUuid() + "&sign=" + frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue();
		logger.debug("发送用户[" + frontUser.getUuid() +"]找回密码邮件到" + frontUser.getUserConfigMap().get(DataName.userBindMailBox.toString()).getDataValue() + "，链接:" + findPasswordUrl);
		String mailFindPasswordTemplate = null;
		StringBuffer templateContent = new StringBuffer();

		try{
			mailFindPasswordTemplate = configService.getValue("mailFindPasswordTemplate",0);
			mailFindPasswordTemplate = request.getSession().getServletContext().getRealPath(mailFindPasswordTemplate);
			logger.debug("读取找回密码邮件模版:" + mailFindPasswordTemplate);
			//读取模版
			File file = new File(mailFindPasswordTemplate);
			if(!file.exists()){
				logger.error("无法找回邮件找回密码模板:" + mailFindPasswordTemplate);
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id, "系统内部错误"));
				return frontMessageView;
			}

			//BufferedReader bufread=new BufferedReader(new FileReader(file));
			BufferedReader bufread= new   BufferedReader   (new   InputStreamReader(new   FileInputStream(mailFindPasswordTemplate),CommonStandard.DEFAULT_ENCODING));
			String read = "";
			while((read=bufread.readLine()) != null)
			{
				templateContent.append(read);
			}
			bufread.close();
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			map.put("json", new EisMessage(EisError.OBJECT_IS_NULL.id, "系统内部错误"));
			return frontMessageView;
		}
		String msg = templateContent.toString();
		msg = msg.replaceAll("\\$\\{findPasswordUrl\\}", findPasswordUrl);
		UserMessage email = new UserMessage();
		email.setTitle((map.get("systemName") == null ? "" : map.get("systemName").toString()) + "找回密码申请"); 
		email.setContent(msg);
		email.setPerferMethod(new String[]{UserMessageSendMethod.email.toString()});
		email.setReceiverName(username);
		email.setCurrentStatus(MessageStatus.queue.id);
		userMessageService.send(email);
		map.put("message", new EisMessage(OperateResult.success.id, "找回密码操作已完成，请前往邮箱" + username + "查看"));
		return frontMessageView;
	}

	//确认通过邮箱找回密码的验证是否正确
	@RequestMapping(value="/findPassword/email/confirm/{uuid}/{sign}", method=RequestMethod.POST)
	public String confirmFindPasswordByMail(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("uuid") Integer uuid, @PathVariable("sign") String sign) {

		if(uuid == 0){
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id, "错误的找回密码请求"));
			logger.error("message: " + EisError.userNotFoundInRequest.id);
			return frontMessageView;
		}
		if(sign == null){
			map.put("message", new EisMessage(EisError.findPasswordSignNotFound.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.findPasswordSignNotFound.id);
			return frontMessageView;
		}
		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.accountNotExist.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.accountNotExist.id);
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			map.put("message", new EisMessage(EisError.ACCOUNT_LOCKED.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.ACCOUNT_LOCKED.id);
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()) == null){
			map.put("message", new EisMessage(EisError.findPasswordSignNotFoundInSystem.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.findPasswordSignNotFoundInSystem.id);
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue() == null){
			map.put("message", new EisMessage(EisError.findPasswordSignNotFoundInSystem.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.findPasswordSignNotFoundInSystem.id);
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue().equals(sign)){
			logger.debug("签名一致，可以设置新密码:" + frontUser.getUuid());
			//certifyService.generateUserToken(frontUser);
			//certifyService.updateUserCookie(request, response, frontUser.getSsoToken());
			map.put("message", new EisMessage(OperateResult.success.id, "请设置新密码"));				
			return frontMessageView;
		}
		map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "错误的找回密码请求"));				
		logger.error("message: " + EisError.DATE_FORMAT_ERROR.id);
		return frontMessageView;

	}	



	//请求绑定邮箱
	@RequestMapping(value="/bindEmail/submit", method=RequestMethod.POST)
	public String submitBindEmail(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String bindMailBox = ServletRequestUtils.getStringParameter(request, "bindMailBox");
		if(map.get("userBindMailBox")!=null){
			bindMailBox=map.get("userBindMailBox").toString();
		}
		logger.debug("我是一个粉刷墙");
		if(bindMailBox == null || bindMailBox.equals("")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入您的绑定邮箱"));
			return frontMessageView;
		}
		logger.debug("我是一个粉刷墙");
		if(!bindMailBox.matches(CommonStandard.emailPattern)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入合法的邮箱"));
			return frontMessageView;
		}
		logger.debug("我是一个粉刷墙");
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			UserCriteria uc = new UserCriteria();
			uc.setUsername(bindMailBox);
			uc.setOwnerId(ownerId);
			List<User> userList = frontUserService.list(uc);
			if(userList!=null){
				frontUser = userList.get(0);
			}else{
				map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
				return frontMessageView;
			}
		}
		logger.debug("我是一个粉刷墙");
		boolean mailBinded = false;
		try{
			if(frontUser.getUserConfigMap().get(DataName.userMailBound.toString()).getDataValue().equals("true")){
				mailBinded = true;
			}
		}catch(Exception e){}
		if(mailBinded){
			map.put("message", new EisMessage(EisError.mailAlreadyBinded.id, "邮箱已绑定，不能重复绑定。"));
			return frontMessageView;
		}
		logger.debug("我是一个粉刷墙");
		if(!frontUser.getUsername().equals(bindMailBox)){
			UserCriteria frontUserCriteria = new UserCriteria();
			frontUserCriteria.setUsername(bindMailBox);
			List<User> frontUserList = frontUserService.list(frontUserCriteria);
			if(frontUserList != null && frontUserList.size() > 0){
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "已有同名的邮箱账户存在"));
				return frontMessageView;
			}
			logger.debug("我是一个粉刷墙");
			/*			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
			userDataCriteria.setDataValue(bindMailBox);
			List<UserData> userDataList = userDataService.list(userDataCriteria);
			if(userDataList != null && userDataList.size() == 1 &&  userDataList.get(0).getUuid() != frontUser.getUuid()){					
				map.put("message", new EisMessage(EisError.objectAlreadyExist.id, "已有相同的邮箱存在"));
				return frontMessageView;
			}*/
			if(globalUniqueService.exist(new GlobalUnique(bindMailBox, ownerId))){		
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "已有相同的邮箱存在"));
				return frontMessageView;
			}
			logger.debug("我是一个粉刷墙");
			if(!globalUniqueService.create(new GlobalUnique(bindMailBox,ownerId))){		
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "已有相同的邮箱存在"));
				return frontMessageView;
			}
			logger.debug("我是一个粉刷墙");
		}
		frontUserService.processBindEmailData(frontUser, bindMailBox);
		sendBindMailMessage(request, frontUser, bindMailBox);
		map.put("message", new EisMessage(OperateResult.success.id, "验证邮件已发送到[" + bindMailBox + "]"));
		logger.debug("验证邮件已发送到[" + bindMailBox + "]");
		frontUserService.update(frontUser);
		return frontMessageView;
	}

	//经邮件确认，完成绑定邮箱
	@RequestMapping(value="/bindEmail/confirm", method=RequestMethod.GET)
	public String confirmBindEmail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid") Integer uuid, @RequestParam("sign") String sign) {

		//map.put("frontUser", certifyService.getLoginedUser(request, response, Constants.UserType.frontUser.id));

		logger.debug("邮箱绑定确认");
		if(uuid < 1){			
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "参数错误"));
			return frontMessageView;	
		}
		if(sign == null || sign.equals("")){
			map.put("message", new EisMessage(EisError.mailBindSignNotFoundInRequest.id, "对不起，绑定失败，请确认链接正确"));
			return frontMessageView;
		}

		User frontUser = frontUserService.select(uuid);
		logger.debug("邮箱绑定确认"+frontUser);
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.accountNotExist.id, "对不起，绑定失败，请确认链接正确"));
			return frontMessageView;
		} 
		/*else if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			map.put("message", new EisMessage(EisError.accountLocked.id, "对不起，绑定失败，请确认链接正确"));
			return frontMessageView;
		}*/
		logger.debug("邮箱绑定确认");
		for(String dataCode : frontUser.getUserConfigMap().keySet()){
			if(dataCode.equals(DataName.userMailBound.toString())){
				if(frontUser.getUserConfigMap().get(dataCode).getDataValue() != null && frontUser.getUserConfigMap().get(dataCode).getDataValue().equals("true")){
					map.put("message", new EisMessage(EisError.mailAlreadyBinded.id, "对不起，您的账户已绑定邮箱"));
					return frontMessageView;
				}
			}

		}
		boolean canBind = false;
		for(String dataCode : frontUser.getUserConfigMap().keySet()){
			if(dataCode != null && dataCode.equals(DataName.userMailBindSign.toString()) && frontUser.getUserConfigMap().get(dataCode) != null && frontUser.getUserConfigMap().get(dataCode).getDataValue().equals(sign)){
				canBind = true;
				break;
			}

		}
		
		if(canBind){
			UserData userData = new UserData();
			userData.setDataCode(DataName.userMailBound.toString());
			userData.setUuid(uuid);
			userData.setCurrentStatus(BasicStatus.readOnly.id);
			userData.setDataValue("true");
			frontUser.getUserConfigMap().put(DataName.userMailBound.toString(), userData);
			if(frontUser.getCurrentStatus()!=UserStatus.normal.id){//方便绑定激活
				frontUser.setCurrentStatus(UserStatus.normal.id);
				logger.debug("激活状态"+frontUser.getCurrentStatus());
			}
			try{
				frontUserService.update(frontUser);
			}catch(Exception e){
				e.printStackTrace();
			}
			logger.debug("成功绑定邮箱");
			map.put("message", new EisMessage(OperateResult.success.id, "账户已成功绑定到邮箱[" + frontUser.getUserConfigMap().get(DataName.userBindMailBox.toString()).getDataValue() + "]"));
			return "user/login";
		} 
		logger.debug("邮箱绑定失败");
		map.put("message", new EisMessage(EisError.mailBindSignNotMatch.id, "对不起，绑定失败，请确认链接正确"));
		return "index/home";
	}

	//请求生成用户邀请码
	@RequestMapping(value="{uuid}/getInviteCode", method=RequestMethod.GET)
	public String getInviteCode(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));	
			return frontMessageView;
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));	
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userInviteCode.toString()) != null){
			map.put("message", new EisMessage(OperateResult.success.id, frontUser.getUserConfigMap().get(DataName.userInviteCode.toString()).getDataValue()));	
			return frontMessageView;
		}
		frontUser = frontUserService.select(frontUser.getUuid());
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id, "用户数据异常"));	
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userInviteCode.toString()) != null){
			map.put("message", new EisMessage(OperateResult.success.id, frontUser.getUserConfigMap().get(DataName.userInviteCode.toString()).getDataValue()));	
			return frontMessageView;
		}
		//生成新的邀请码
		String inviteCode = "u" + ShortMd5.encode(String.valueOf(frontUser.getUuid()));
		UserData uc = new UserData();
		uc.setUuid(frontUser.getUuid());
		uc.setDataCode(DataName.userInviteCode.toString());
		uc.setDataValue(inviteCode);
		uc.setDataDescription("邀请码");
		frontUser.getUserConfigMap().put(DataName.userInviteCode.toString(), uc);
		try{
			frontUserService.update(frontUser);
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("message", new EisMessage(OperateResult.success.id, frontUser.getUserConfigMap().get(DataName.userInviteCode.toString()).getDataValue()));	
		return frontMessageView;

	}

	//检查用户是否已存在可注册
	@RequestMapping(value="/exist")
	@ResponseBody
	@IgnoreLoginCheck
	public String getExist(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("username")String username) throws Exception {
		logger.debug("检查数据[" + username + "]的全局唯一性");
		if(username == null || username.equals("")){
			return "false";
		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		if(globalUniqueService.exist(new GlobalUnique(username,ownerId))){		
			logger.debug("数据[" + username + "]的全局唯一性检查结果是true，即已存在，返回false，不可注册");
			return "false";		
		}
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(username);
		List<User> frontUserList = frontUserService.list(frontUserCriteria);
		if(frontUserList != null && frontUserList.size() > 0){
			logger.debug("数据[" + username + "]在用户名中已存在，返回false，不可注册");
			return "false";		
		}
		/*UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
		userDataCriteria.setDataValue(username);
		if(userDataService.valueExist(userDataCriteria)){					
			map.put("message", new EisMessage(EisError.objectAlreadyExist.id, "数据已存在"));
			return frontMessageView;
		}*/
		logger.debug("数据[" + username + "]的在中央缓存和用户名中都没有找到，返回true，可以注册");

		return "true";		
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String getRegister(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {

		return "user/register";
	}
	@RequestMapping(value="/revisePassword", method=RequestMethod.GET)
	public String getRevisePassword(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {

		return "/node/user/revisePassword";
	}

	//用户新增注册
	@RequestMapping(method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String create(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			User frontUser			) throws Exception {
		map.put("operate", OperateCode.USER_REGISTER.toString());
		logger.debug("用户新增注册信息："+frontUser.getUsername()+"#"+frontUser.getUserPassword());
		boolean isAutoCreateUser = false;
		String autoPassword = null;
		if(StringUtils.isBlank(frontUser.getUsername())){
			boolean randUser = ServletRequestUtils.getBooleanParameter(request, "rand");

			if(autoCreateRandomNameWhenNullRegister && randUser){
				long uuid = uuidService.insert(new Uuid());
				String autoUserName = AUTO_CREATE_USER_PREFIX + "_" + configService.getServerId() + uuid;
				frontUser.setUsername(autoUserName);
				logger.info("尝试注册的用户名为空，尝试自动生成一个用户名:" + frontUser.getUsername());
				if(StringUtils.isBlank(frontUser.getUserPassword())){
					Random random = new Random();
					autoPassword =String.valueOf(random.nextInt(999999)%(999999-100000+1) + 100000);
					frontUser.setUserPassword(autoPassword);
					logger.info("尝试注册的用户名和密码都为空，尝试为用户[" + autoUserName + "]自动生成一个密码:" + frontUser.getUserPassword());

				}
				isAutoCreateUser = true;
			} else {
				logger.warn("尝试注册用户但未提交用户名");
				map.put("message", new EisMessage(EisError.dataIllegal.id, "请提交用户名"));
				return frontMessageView;
			}
		}
		if(dirtyDictService.isDirty(frontUser.getUsername())){
			logger.warn("尝试注册用户[" + frontUser.getUsername() + "]名称不能通过敏感词检查");
			map.put("message", new EisMessage(EisError.dataIllegal.id, "您的用户名不合法，请换一个注册"));
			return frontMessageView;
		}
		if(dirtyDictService.isDirty(frontUser.getNickName())){
			logger.warn("尝试注册用户[" + frontUser.getNickName() + "]的昵称不能通过敏感词检查");
			map.put("message", new EisMessage(EisError.dataIllegal.id, "您的昵称不合法，请换一个注册"));
			return frontMessageView;

		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		frontUser.setOwnerId(ownerId);




		userRegisterNeedSmsSign	= configService.getBooleanValue(DataName.userRegisterNeedSmsSign.toString(), ownerId);
//		logger.debug("userRegisterNeedSmsSign"+userRegisterNeedSmsSign);
		//如果用户没有提供密码，并且是手机验证用户，则自动以手机验证码作为密码
		boolean autoPasswordForPhoneUser	= configService.getBooleanValue(DataName.autoPasswordForPhoneUser.toString(), ownerId);
		String smsRegisterSign = null;
		String phone = ServletRequestUtils.getStringParameter(request, "phone", null);
//		String phone = frontUser.getUsername();
		String needTelToRec = ServletRequestUtils.getStringParameter(request, "needTelToRec");
		if("N".equals(needTelToRec) || frontUser. getCurrentStatus()==UserStatus.unactive.id){
			isAutoCreateUser=true;
		}
		if(!isAutoCreateUser && userRegisterNeedSmsSign){
			smsRegisterSign = ServletRequestUtils.getStringParameter(request, "smsRegisterSign", null);
			logger.info("系统配置为用户注册必须手机验证，用户提交的手机验证码是"+smsRegisterSign);

			String key = KeyConstants.REGISTER_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;

			String sign = centerDataService.get(key);
			if(sign == null || !sign.equalsIgnoreCase(smsRegisterSign)){
				logger.warn("用户提交的验证码是:" + smsRegisterSign + ",REDIS中key为+"+key+"的的验证码是:" + sign + ",不一致");
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.id, "对不起，您输入的短信验证码错误"));
				return frontMessageView;	
			}
			/*Crypt crypt = new Crypt();
			crypt.setAesKey(aesKey);
			String sourceStr=cookieService.getCookie(request, DataName.smsRegisterSign.toString());
			if(sourceStr == null){
				logger.warn("用户Cookie中没有验证码数据");
				map.put("message", new EisMessage(EisError.verifyError.id, "对不起，您输入的验证码错误"));
				return frontMessageView;		
			} 

			String sign=crypt.aesDecrypt(sourceStr);
			logger.info("解密后的验证码数据是"+sign);
			if(sign == null){
				logger.warn("用户Cookie中的验证码数据解密错误:" + sourceStr);
				map.put("message", new EisMessage(EisError.verifyError.id, "对不起，您输入的验证码错误"));
				return frontMessageView;	
			}
			String[] data = sign.split("\\|");
			if(data == null || data.length != 4){
				logger.warn("用户Cookie中的验证码数据错误:" + sign);
				map.put("message", new EisMessage(EisError.verifyError.id, "对不起，您输入的验证码错误"));
				return frontMessageView;	
			}

			String sign2 = data[2];
			long ts = 0;
			if(NumericUtils.isNumeric(data[3])){
				ts = Long.parseLong(data[3]);
			}
			if(!sign2.equalsIgnoreCase(smsRegisterSign)){
				logger.error("用户提交的手机验证码是:" + smsRegisterSign + ",系统解密后的验证码是:" + sign2);
				map.put("message", new EisMessage(EisError.verifyError.id, "对不起，您输入的验证码错误"));
				return frontMessageView;	
			}
			if(ts > 0){
				long ttl = (new Date().getTime() - ts) / 1000;
				logger.debug("手机验证码生成时间是: " + sdf.format(new Date(ts)) + ",已存在" + ttl + "秒，有效期是:" + REGISTER_SMS_TTL);
				if( ttl > REGISTER_SMS_TTL){
					map.put("message", new EisMessage(EisError.requestTimeout.id, "验证码超时"));
					return frontMessageView;	
				}
			}*/
			//手机号就是用户名，自动绑定
			logger.debug("手机号注册"+phone+"#"+frontUser.getUsername());
			frontUser.setExtraValue(DataName.userBindPhoneNumber.toString(), phone);

		}
		frontUser.setUserTypeId(UserTypes.frontUser.id);
		if( siteRegisterNeedPatchca ){
			boolean patchcaIsOk = false;
			try{
				String sessionPatchca = "";
				sessionPatchca = (String)request.getSession(true).getAttribute(CommonStandard.sessionCaptchaName);
				if(!sessionPatchca.equals("")){
					String userInputPatchca = request.getParameter(CommonStandard.sessionCaptchaName);
					logger.debug("session:" + sessionPatchca + ", input patchca:" + userInputPatchca);
					if(userInputPatchca != null){
						if(userInputPatchca.equalsIgnoreCase(sessionPatchca)){
							patchcaIsOk = true;
						}
					}
				}
			}catch(Exception e){}
			try{
				request.getSession().removeAttribute(CommonStandard.sessionCaptchaName);
			}catch(Exception e){}
			if(!patchcaIsOk){
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.id, "对不起，请输入正确的验证码。"));
				return frontMessageView;
			}
		}
		logger.debug(">>>PERFORMANCE>>>开始检查唯一性globalUnique");
		if(globalUniqueService.exist(new GlobalUnique(frontUser.getUsername(), ownerId))){
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "您的用户名已经被占用，请换一个注册"));
			return frontMessageView;
		}
		logger.debug(">>>PERFORMANCE>>>完成检查唯一性globalUnique");

		logger.debug("尝试注册新用户:" + frontUser.getUsername() + "/" + frontUser.getUserPassword() + ".");

		if(StringUtils.isBlank(frontUser.getUserPassword())){
			if(autoPasswordForPhoneUser && StringUtils.isNotBlank(smsRegisterSign)){
				logger.debug("用户注册未提交密码但当前系统配置为自动为手机注册用户生成密码,使用短信校验码[" + smsRegisterSign + "]作为密码");
				frontUser.setUserPassword(smsRegisterSign);
			} else {
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id, "请输入正确的密码"));
				return frontMessageView;
			}
		}
		setIpAddress(request, frontUser);
		if(frontUser.getCurrentStatus()<1){
			frontUser.setCurrentStatus(UserStatus.normal.id);
		}

		//检查是否有invite编码
		String inviteCode = cookieService.getCookie(request, "inviteCode");
		if(StringUtils.isBlank(inviteCode)){
			inviteCode =ServletRequestUtils.getStringParameter(request, "i",null);
		}
		if(StringUtils.isNotBlank(inviteCode)){
			logger.debug("注册用户拥有邀请码:" + inviteCode);
			cookieService.removeCookie(request, response, "inviteCode", null);
			frontUser.setExtraValue(DataName.userInviteByCode.toString(), inviteCode);
		}
		if(frontUser.getUsername().matches("^\\d{11}$")){
			frontUserService.processBindPhoneData(frontUser, frontUser.getUsername());
		} 
		if(frontUser.getUsername().matches(CommonStandard.emailPattern)){
			frontUserService.processBindEmailData(frontUser, frontUser.getUsername());
		}
		//是否自动为用户生成登陆密钥
		String loginKey = Crypt.shortMd5(DigestUtils.md5Hex(frontUser.getUsername() + System.currentTimeMillis()));
		if(generateLoginKeyForNewUser){
			frontUser.setAuthKey(loginKey);			
			logger.info("为新注册用户[" + frontUser.getUsername() + "]生成登陆密钥:" + loginKey);
			frontUser.setMessageId(loginKey);

		}
		//获取用户注册时的IP
		String ip = IpUtils.getClientIp(request);
		frontUser.setExtraValue(DataName.userRegisterIp.toString(), ip);

		//获取用户个人简介微博等信息资料
		String user_url = ServletRequestUtils.getStringParameter(request, "user_url");
		String userDescription = ServletRequestUtils.getStringParameter(request, "userDescription");
		String qq_weibo = ServletRequestUtils.getStringParameter(request, "qq_weibo");
		String sina_weibo = ServletRequestUtils.getStringParameter(request, "sina_weibo");
		frontUser.setExtraValue("userUrl", user_url);
		frontUser.setExtraValue("userDescription", userDescription);
		frontUser.setExtraValue("qq_weibo", qq_weibo);
		frontUser.setExtraValue("sina_weibo", sina_weibo);
		frontUser.setExtraValue("allowOtherToSelf", "Y");
		String msn = ServletRequestUtils.getStringParameter(request, "extend_field1");
		String workPhone = ServletRequestUtils.getStringParameter(request, "extend_field3");
		String familyPhone = ServletRequestUtils.getStringParameter(request, "extend_field4");
		String passwordQuestion = ServletRequestUtils.getStringParameter(request, "sel_question");
		String passwordAnswer = ServletRequestUtils.getStringParameter(request, "passwd_answer");
		String userBindPhoneNumber = ServletRequestUtils.getStringParameter(request, "phone");
		String qq = ServletRequestUtils.getStringParameter(request, "extend_field2");
		String userBindMailBox = ServletRequestUtils.getStringParameter(request, "email");
		frontUser.setExtraValue("msn", msn);
		frontUser.setExtraValue("workPhone", workPhone);
		frontUser.setExtraValue("familyPhone", familyPhone);
		frontUser.setExtraValue("passwordQuestion", passwordQuestion);
		frontUser.setExtraValue("passwordAnswer", passwordAnswer);
		frontUser.setExtraValue("qq", qq);
		frontUser.setExtraValue(DataName.userBindPhoneNumber.toString(), userBindPhoneNumber);
		frontUser.setExtraValue(DataName.userBindMailBox.toString(), userBindMailBox);
		logger.debug("注册相关信息：sina_weibo："+sina_weibo+"#"+"user_url:"+user_url+"#"+"qq_weibo:"+qq_weibo+"#"+"userDescription:"+userDescription
				+"#"+"msn:"+msn+"#"+"workPhone:"+workPhone+"#"+"familyPhone:"+familyPhone
				+"#"+"passwordQuestion:"+passwordQuestion+"#"+"passwordAnswer:"+passwordAnswer+"#"+"userBindPhoneNumber:"+userBindPhoneNumber
				+"#"+"qq:"+qq+"#"+"userBindMailBox:"+userBindMailBox);
		//String mailActiveSign = null;
		if(mailActive){
			frontUser.setCurrentStatus(UserStatus.unactive.id);
		}


		logger.debug(">>>PERFORMANCE>>>开始插入用户数据");

		int createRs = frontUserService.insert(frontUser);
		if(createRs != 1){
			map.put("message", new EisMessage(OperateResult.failed.id,"无法注册:" + createRs));
			return frontMessageView;
		}
		logger.debug(">>>PERFORMANCE>>>完成插入用户数据，结束流程");
		/*if(fuuid > 0){//注册成功
			if(mailActive){
				map.put("mailActive", mailActive);
				mailActiveSign = Crypt.passwordEncode(Constants.mailActiveKey + fuuid);	
				frontUser = frontUserService.select(fuuid);
				UserData userData = new UserData();
				userData.setDataCode(Constants.DataName.userMailActiveSign.toString());
				userData.setDataDescription("邮箱激活签名");
				userData.setDataValue(mailActiveSign);
				frontUser.getUserConfigMap().put(Constants.DataName.userMailActiveSign.toString(), userData);

				userData = new UserData();
				userData.setDataCode(Constants.DataName.userBindMailBox.toString());
				userData.setDataDescription("绑定/激活邮箱");
				userData.setDataValue(frontUser.getUsername());
				frontUser.getUserConfigMap().put(Constants.DataName.userBindMailBox.toString(), userData);
				frontUserService.update(frontUser);

				String activeUrl = "http://www." + configService.getValue("system_domain") + "/user/" + fuuid + "/postMailActive/" + mailActiveSign;
				frontUserService.sendActivemail(frontUser.getUserConfigMap().get(Constants.DataName.userBindMailBox.toString()).getDataValue(), activeUrl);
				logger.debug("需要邮件激活，为用户[" + frontUser.getUuid() + "]发送邮件到" + frontUser.getUserConfigMap().get(Constants.DataName.userBindMailBox.toString()).getDataValue() + ",激活链接是:" + activeUrl);
				map.put("message", new EisMessage(Constants.OperateResult.success.id,"注册成功，请使用邮件等待激活"));
			} else {
				EisMessage sm = new EisMessage(Constants.OperateResult.success.id,"注册成功");
				if(request.getSession().getAttribute(Constants.sessionReturnUrlName) != null){
					sm.setMessage((String)request.getSession().getAttribute(Constants.sessionReturnUrlName));
				}
				map.put("message", sm);
				if(loginAfterRegister){
					login(frontUser,request,response,map);
				}


			}
		} else {
			map.put("message", new EisMessage(Constants.OperateResult.failed.id, "对不起，系统繁忙，请稍后再试。"));

		}*/
		logger.debug("注册后是否登录:" + loginAfterRegister);
		if("N".equals(needTelToRec)){
			isAutoCreateUser=false;
		}
		if(isAutoCreateUser){
			frontUser.setUserPassword(autoPassword);
		}
		if(loginAfterRegister){
			logger.debug("新增用户密码是:" +  frontUser.getUserPassword());
			logger.debug("尝试在注册后自动登录:" + login(frontUser, request, response, map));
			frontUser.setMessageId(loginKey);
			map.put("frontUser", frontUser);
		} else {
			map.remove("frontUser");
			map.put("message", new EisMessage(OperateResult.success.id,"注册成功"));
		}

		String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
		if(redirectUrl == null){
			redirectUrl = request.getHeader("referer") == null ? "/" : request.getHeader("referer");
		}
		if(!request.getRequestURI().endsWith(".json")){

			logger.debug("当前用户登录成功并且不是JSON访问，将重定向到登陆前URL:" + redirectUrl);
			response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}		
			return null;
		}
		map.put("redirectUrl", redirectUrl);
		return frontMessageView;

	}

	@RequestMapping(value="/checkWeixinUser")
	@IgnoreLoginCheck
	public String checkWeixinUser(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser != null){
			String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
			if(redirectUrl == null){
				redirectUrl = "/";
			}
			try {
				redirectUrl = java.net.URLDecoder.decode(redirectUrl,CommonStandard.DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			//该OpenId用户已存在
			logger.debug("用户已登录，无需检查微信用户状态,跳转到:" + redirectUrl);

			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		boolean isWeixinAccess = AgentUtils.isWeixinAccess(request.getHeader("user-agent"));
		if(!isWeixinAccess){
			logger.error("当前不是微信环境，不能使用微信注册");
			map.put("message", new EisMessage(EisError.VERSION_UNSUPPORTED.id,"请在微信环境下访问"));
			return frontMessageView;
		}
		Object o = map.get("outUuid");
		String openId = null;
		if(o == null){
			logger.error("当前是微信访问但无法获取用户openId");
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"无法获取微信用户信息"));
			return frontMessageView;
		} 			

		openId = o.toString();
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUserTypeId(UserTypes.frontUser.id);
		userCriteria.setAuthKey(openId);
		int existCount = frontUserService.count(userCriteria);
		logger.debug("根据openId=" + openId + "]查询auth_key等于它的用户数量是:" + existCount);
		if(existCount > 0){
			//已存在该用户,返回到redirectUrl;
			String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
			if(redirectUrl == null){
				redirectUrl = "/";
			}
			//该OpenId用户已存在
			frontUser = certifyService.forceLogin(request, response, openId);
			logger.debug("openId=" + openId + "的微信用户已存在，登陆:" + frontUser + ",并跳转到:" + redirectUrl);

			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			//发送到填写信息页面
			String redirectUrl = "/user/weixinRegister.shtml";
			logger.debug("openId=" + openId + "的用户不存在，跳转到微信用户注册页面:" +  redirectUrl);
			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;


	}

	@RequestMapping(value="/weixinRegister")
	@IgnoreLoginCheck
	public String weixinRegister(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		return "user/weixinRegister";


	}

	//用户通过邮箱验证码激活
	@RequestMapping(value="/mailActive/{sign}", method=RequestMethod.GET)
	public String postMailActive(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("uuid") Integer uuid, @PathVariable("sign") String sign) throws Exception {

		if(sign == null || sign.equals("")){
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id, "请提交正确的数据"));
			return frontMessageView;
		}
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setDataCode(DataName.userMailActiveSign.toString());
		userDataCriteria.setDataValue(sign);
		userDataCriteria.setCorrectWithDynamicData(false);
		userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
		java.util.List<UserData> userConfigList = userDataService.list(userDataCriteria);
		if(userConfigList == null || userConfigList.size() != 1){//验证失败
			logger.info("没找到对应的激活码[" + sign + "]");
			map.put("message", new EisMessage(EisError.activeSignNotFound.id, "没找到对应的激活码"));
			return frontMessageView;
		} 
		UserData userData = userConfigList.get(0);
		logger.debug("通过激活码[" + sign + "]查找到UUID为[" + userData.getUuid() + "].");
		User frontUser = frontUserService.select(userData.getUuid());

		if(frontUser == null){
			logger.info("没找到UUID=[" + userData.getUuid() + "]的待激活用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "没找到对应的用户"));
			return frontMessageView;
		}  
		if(frontUser.getUuid() != uuid){
			logger.error("请求的地址UUID[" + uuid + "]与实际认证的用户[" + frontUser.getUuid() + "]不一致");
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "参数错误"));
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.unactive.id){
			logger.info("用户不是处于未激活状态，而是[" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "没找到对应的用户"));
			return frontMessageView;
		} 
		String newMailActiveSign = Crypt.passwordEncode(CommonStandard.mailActiveKey + frontUser.getUuid());
		if(newMailActiveSign.equals(sign)){//激活成功
			logger.debug("成功激活用户:" + frontUser.getUuid());
			frontUser.setCurrentStatus(UserStatus.normal.id);
			frontUserService.update(frontUser);
			map.put("message", new EisMessage(OperateResult.success.id, "用户已成功激活"));
			return frontMessageView;	
		} 
		logger.info("请求的激活码[" + sign + "] 与 校验生成的[" + newMailActiveSign +"]不一致,uuid=" + frontUser.getUuid());

		map.put("message", new EisMessage(EisError.dataVerifyFail.id, "数据校验失败"));
		return frontMessageView;


	}

	//用户退出登录
	@RequestMapping(value="/logout", method=RequestMethod.GET)	
	@IgnoreLoginCheck
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam(value="redirectTo", required=false)String redirectTo) throws IOException {
		map.put("operate", OperateCode.USER_LOGOUT.toString());

		//	User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		User frontUser = new User();
		frontUser.setUserTypeId(UserTypes.frontUser.id);
		/*if(frontUser != null){
			if(frontUser.getUuid() > 0){
				//audit.setAuditRefUserId(frontUser.getUuid());
				//audit.setAuditResult(Constants.OperateResult.success.id);
				//auditService.insert(audit);
				if(redirectTo!=null){
					return "redirect:"+redirectTo;
				}
			}
		}*/
		request.getSession().invalidate();

		certifyService.logout(request, response, frontUser);

		//cookieService.removeCookie(request, response, "uuid");
		map.put("message", new EisMessage(OperateResult.success.id, "用户退出"));
		//response.flushBuffer();
		if(request.getRequestURI().endsWith("json")){
			return frontMessageView;
		}

		if(redirectTo!=null){
			return "redirect:"+redirectTo;
		}
		return "user/logout";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String getLogin(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
		return "user/login";
	}

	//用户登录
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			User user,
			@RequestParam(value="rememberUserName", required=false)boolean rememberUserName) {
		map.put("operate", OperateCode.USER_LOGIN.toString());
		map.remove("user");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		int captchaInputErrorCount = 0;
		boolean needCaptcha = false;		

		try{
			captchaInputErrorCount = (Integer)request.getSession().getAttribute("captchaInputErrorCount");

		}catch(Exception e){}
		logger.debug("验证码输入错误次数:" + captchaInputErrorCount + ", 系统允许的不输入验证码登录次数:" + showCaptchaWhenLoginFailCount);
		if(captchaInputErrorCount >= showCaptchaWhenLoginFailCount){
			needCaptcha = true;
		}
		if(showCaptchaWhenLoginFailCount == -1){
			needCaptcha = false;			
		}
		request.getSession().setAttribute("needCaptcha", needCaptcha);
		if(needCaptcha){
			boolean patchcaIsOk = false;
			String userInputCaptcha = request.getParameter(CommonStandard.sessionCaptchaName);
			if(userInputCaptcha != null){
				patchcaIsOk = captchaService.verify(request, response, null, userInputCaptcha);
			}
			logger.debug("验证码校验结果:" + patchcaIsOk);	

			if(!patchcaIsOk){
				map.put("message", new EisMessage(EisError.AUTH_FAIL.id, "对不起，请输入正确的验证码。"));
				captchaInputErrorCount++;
				logger.debug("设置验证码输入错误次数到Session:" + captchaInputErrorCount);
				request.getSession().setAttribute("captchaInputErrorCount", captchaInputErrorCount);
				return frontMessageView;
			}

		}
		request.getSession().setAttribute("captchaInputErrorCount", 0);
		logger.debug("登录用户信息"+user.toString());
		int result = login(user, request, response, map);
		if(result == OperateResult.success.id){
			if(rememberUserName){
				cookieService.addCookie(request, response, "remember"+ CommonStandard.sessionUsername, user.getUsername(),sessionTimeout, null, false);
				cookieService.addCookie(request, response, "uuid",String.valueOf(user.getUuid()),sessionTimeout, null);
			} else {
				cookieService.removeCookie(request, response, "remember" + CommonStandard.sessionUsername, null);
				cookieService.removeCookie(request, response, "uuid", null);
			}
			EisMessage loginResult =  new EisMessage(OperateResult.success.id, "登录成功");

			map.put("message", loginResult);
			String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
			if(redirectUrl == null){
				redirectUrl =  "/";
				//logger.debug("Cookie中没有重定向URL，使用referer:" + redirectUrl);
			} else {
				logger.debug("使用Cookie中的重定向URL:" + redirectUrl);
			}
			if(!request.getRequestURI().endsWith(".json")){

				logger.debug("当前用户登录成功并且不是JSON访问，将重定向到登陆前URL:" + redirectUrl);
				response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
				try {
					response.sendRedirect(redirectUrl);
				} catch (IOException e) {
					e.printStackTrace();
				}		
				return null;
			} else {
				map.put("redirectUrl", redirectUrl);
			}
			return frontMessageView;

		}
		captchaInputErrorCount++;
		request.getSession().setAttribute("loginErrorCount", captchaInputErrorCount);
		map.put("message", new EisMessage(EisError.AUTH_FAIL.id, "用户名或密码错误"));
		return frontMessageView;
	}

	protected int login(User user, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		int loginErrorCount = 0;
		try{
			loginErrorCount = (Integer)request.getSession().getAttribute("loginErrorCount");
		}catch(Exception e){}

		if(user == null){
			logger.error("Passport null.");
			map.put("json", new EisMessage(EisError.AUTH_FAIL.id, "对不起，请检查您的用户名或密码。"));
			request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
			return EisError.AUTH_FAIL.id;		
		}

		logger.debug("用户[" + user.getUsername() + "/" + user.getUserPassword() + "]尝试登录");
		if(user.getUsername() == null || user.getUserPassword() == null){
			logger.error("Passport username or password null.");
			map.put("json", new EisMessage(EisError.AUTH_FAIL.id, "对不起，请检查您的用户名或密码。"));
			request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);

			return EisError.AUTH_FAIL.id;		
		}
		if(user.getUsername().equals("") || user.getUserPassword().equals("")){
			logger.error("Passport username or password empty.");
			map.put("json", new EisMessage(EisError.AUTH_FAIL.id, "对不起，请检查您的用户名或密码。"));
			request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
			return EisError.AUTH_FAIL.id;		
		}
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(user.getUsername());
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.id);
		/*//查找是否有同名用户存在，如果没有，创建新用户
		int existCount = frontUserService.count(frontUserCriteria);
		if(existCount < 1){
			if(autoCreateRandomNameWhenNullRegister){
				logger.info("找不到用户名=" + user.getUsername() + "的前端用户，自动创建");
				long uuid = uuidService.insert(new Uuid());
				String autoUserName = AUTO_CREATE_USER_PREFIX + "_" + configService.getServerId() + uuid;
				User frontUser = new User();
				frontUser.setUsername(autoUserName);
				logger.info("尝试注册的用户名为空，尝试自动生成一个用户名:" + frontUser.getUsername());
				if(StringUtils.isBlank(frontUser.getUserPassword())){
					
					frontUser.setUserPassword(user.getUsername());
					logger.info("尝试注册的用户名和密码都为空，尝试为用户[" + autoUserName + "]设置与用户名一致的密码:" + frontUser.getUserPassword());
					if(dirtyDictService.isDirty(frontUser.getUsername())){
						logger.warn("尝试注册用户[" + frontUser.getUsername() + "]名称不能通过敏感词检查");
						return EisError.dataIllegal.id;
					}
					if(dirtyDictService.isDirty(frontUser.getNickName())){
						logger.warn("尝试注册用户[" + frontUser.getNickName() + "]的昵称不能通过敏感词检查");
						return EisError.dataIllegal.id;

					}
					
					frontUser.setOwnerId(user.getOwnerId());
					if(globalUniqueService.exist(new GlobalUnique(frontUser.getUsername(), user.getOwnerId()))){
						return EisError.objectAlreadyExist.id;
					}
					int createRs = 0;
					try {
						createRs = frontUserService.insert(frontUser);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(createRs != 1){
						return OperateResult.failed.id;
					}

				}
			}
		}
		*/
		
		frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(user.getUsername());
		frontUserCriteria.setUserPassword(user.getUserPassword());
		frontUserCriteria.setCurrentStatus(UserStatus.normal.id);
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.id);
		User frontUser = certifyService.login(request, response, frontUserCriteria);
		try{
			logger.debug("登录前台用户的配置数据有[" + (frontUser.getUserConfigMap() == null ? 0 : frontUser.getUserConfigMap().size()) + "]条");
			logger.debug("用户的级别方案是：" + frontUser.getUserLevelProject().getUserLevelProjectName());
			/*for(String key : frontUser.getUserLevelProject().getUserLevelConditionMap().keySet()){
				logger.info("用户级别方案配置::::" + key + "====" + frontUser.getUserLevelProject().getUserLevelConditionMap().get(key).getUserLevelConditionValue());
			}*/
		}catch(Exception e){
			logger.debug("用户没有任何级别方案配置");
		}
		if(frontUser != null){//登录成功
			logger.debug("登录用户的级别"+frontUser.getLevel());
			logger.debug("用户[" + user.getUsername() + "/" + user.getUserTypeId() + "/" + user.getCurrentStatus() + "] 已成功登录");
			map.put("message", new EisMessage(OperateResult.success.id, "登录成功"));
			User fUser = frontUser.clone();
			fUser.setMessageId(fUser.getExtraValue(DataName.supplierLoginKey.toString()));
			logger.debug("用户允许发邮件："+fUser.getExtraValue("allowOtherToSelf"));
			SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
			if(siteDomainRelation == null){
				logger.error("根据主机名[" + request.getServerName() + "]找不到指定的站点关系");
				return EisError.AUTH_FAIL.id;	
			}
			cookieService.addCookie(request, response,"eis_userheadpic", fUser.getExtraValue("userHeadPic"),sessionTimeout,siteDomainRelation.getCookieDomain(), false);
			UserRelation ur = vipExpiredOrNot(request, response, frontUser);
			if(cookieService.getCookie(request, "frontVipLevel")!=null){//有对应的cookie
				cookieService.removeCookie(request, response, "frontVipLevel", siteDomainRelation.getCookieDomain());
				if(ur!=null){
					cookieService.addCookie(request, response, "frontVipLevel", ur.getCurrentStatus()+"", sessionTimeout, siteDomainRelation.getCookieDomain(),false);
					logger.debug("会员的缓存");
				}else{
					cookieService.addCookie(request, response, "frontVipLevel", "0", sessionTimeout, siteDomainRelation.getCookieDomain(),false);
					logger.debug("非会员的缓存");
				}
			}
			
			map.put("frontUser", fUser);
			
		//	operateLogService.insert(new OperateLog(ObjectType.user.name(), user.getUsername(), fUser.getUuid(), OperateCode.USER_LOGIN.toString(), String.valueOf(OperateResult.success.id), null, IpUtils.getClientIp(request),0));
			return OperateResult.success.id;		

		} else {
			logger.debug("用户[" + user.getUsername() + "无法登录");
		//	operateLogService.insert(new OperateLog(ObjectType.user.name(), user.getUsername(), user.getUuid(), OperateCode.USER_LOGIN.toString(), String.valueOf(OperateResult.failed.id), null, IpUtils.getClientIp(request),0));
		}

		request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
		return EisError.AUTH_FAIL.id;		
	}
	protected UserRelation vipExpiredOrNot(HttpServletRequest request,
			HttpServletResponse response, User frontUser){
		logger.debug("进入vip过期判定");
		UserRelationCriteria userRelation = new UserRelationCriteria();
		userRelation.setOwnerId(frontUser.getOwnerId());
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setObjectType("vip");
		List<UserRelation> userRelationList = userRelationService.list(userRelation);
		if(userRelationList == null || userRelationList.size()<1){
			logger.debug("未找到用户"+frontUser.getUsername()+"的会员充值记录");
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Calendar cld = Calendar.getInstance();  
		Date d2 = null;
		UserRelation ur = userRelationList.get(0);
		String relationType = ur.getRelationType();
		if("VIP_MONTH".equals(relationType)){
				cld.setTime(ur.getCreateTime());  
			    cld.add(Calendar.MONTH, 1);  
			    d2 = cld.getTime();
			   
		}else
		if("VIP_QUARTER".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
		    cld.add(Calendar.MONTH, 3);  
		    d2 = cld.getTime();
		}else
		if("VIP_YEAR".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
		    cld.add(Calendar.YEAR, 1);  
		    d2 = cld.getTime();
		}
		logger.debug("relationType:"+relationType+"#d2:"+d2);
		if(d2!=null && new Date().getTime() >d2.getTime()){
			 logger.debug("会员到期日期："+sdf.format(d2.getTime()));
		     ur.setCurrentStatus(1);
		     userRelationService.update(ur);
		}
		
		return ur;
	}
	//获取手机验证码
	@RequestMapping(value="/get/{phone}", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String getVerificationCode(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("phone")String phone,
			@RequestParam(value="eis_captcha",required=false)String captcha) throws Exception  {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		/**
		 * 为了支持某些客户端不能直接发送POST请求到一个没有参数的URL
		 */
		if(!phone.matches("^\\d{11}$")){
			phone = ServletRequestUtils.getStringParameter(request, "phone",null);
		}
		if(StringUtils.isBlank(phone) || !phone.matches("^\\d{11}$")){
			logger.warn("错误的手机号:" + phone);
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "手机号不正确"));
			return frontMessageView;		
		}

		String smsTemplate = configService.getValue(DataName.registerSmsValidateMessage.toString(), ownerId);
		if(smsTemplate == null){
			smsTemplate = DEFAULT_SMS_VALIDATE_MESSAGE;
		}

		/*boolean needCaptchaWhenRegisterByPhone = configService.getBooleanValue(DataName.needCaptchaWhenRegisterByPhone.toString(), ownerId);
		if(needCaptchaWhenRegisterByPhone){
			logger.info("收到用户提交的captcha是"+captcha);
			if (!captchaService.verify(request, response, null, captcha)){
				map.put("message", new EisMessage(EisError.dataFormatError.id, "验证码错误"));
				return frontMessageView;	
			}
		}*/
		int registerSmsIdelInterval = configService.getIntValue(DataName.registerSmsIdelInterval.toString(), ownerId);
		if(registerSmsIdelInterval > 0){
			//放入一个键值到REDIS，以防止用户重复提交
			String key = KeyConstants.REGISTER_SMS_LOCK_PREFIX + "#" + ownerId + "#" + phone;
			boolean setResult = centerDataService.setIfNotExist(key, sdf.format(new Date()), registerSmsIdelInterval);
			if(!setResult){
				String value = centerDataService.get(key);
				logger.debug("用户手机号[" + phone + "]已在[" + value + "]时提交验证码申请，还没到限制时间" + registerSmsIdelInterval + ",不允许发送注册短信");
				map.put("message", new EisMessage(EisError.operateTooSoon.id, "操作过于频繁，请稍候再试"));
				return frontMessageView;	
			}
		}
		/*boolean isExist = false;
		//判断手机号是否注册过，没有就注册用户，有就获取验证码
		if(globalUniqueService.exist(new GlobalUnique(phone, ownerId))){
			isExist = true;
		} else {
			isExist = false;
		}*/
		
		Random random = new Random();
		String randomCode=String.valueOf(random.nextInt(999999)%(999999-100000+1) + 100000);
		logger.info("短信验证码是"+randomCode);

		String shortMsg = smsTemplate.replaceAll("\\$\\{smsCode\\}", randomCode);
		/*User user = null;
		User frontUser = new User(ownerId);
		if (isExist) {
			UserCriteria userCriteria = new UserCriteria(ownerId);
			userCriteria.setUsername(phone);
			List<User> frontUserList = frontUserService.list(userCriteria);
			if (frontUserList == null ||frontUserList.size() < 1) {
				int delete = globalUniqueService.delete(new GlobalUnique(phone, ownerId));
				logger.debug("删除缓存用户 ：" + delete);
				String loginKey = Crypt.shortMd5(DigestUtils.md5Hex(phone + System.currentTimeMillis()));
				
				frontUser.setUsername(phone);
				frontUser.setAuthKey(loginKey);
				frontUser.setMessageId(loginKey);
			} else {
				user = frontUserList.get(0);
				logger.debug("存在用户[" + user.getUsername() + "]");
			}
		} else{
			String loginKey = Crypt.shortMd5(DigestUtils.md5Hex(phone + System.currentTimeMillis()));
			frontUser.setUsername(phone);
			frontUser.setAuthKey(loginKey);
			frontUser.setMessageId(loginKey);
		}*/
		
		UserMessage sms = new UserMessage(ownerId);
		sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
		sms.setContent(shortMsg);
		sms.setReceiverName(phone);
		sms.setTitle(randomCode);
		sms.setSign("regist");
		sms.setCurrentStatus(MessageStatus.queue.id);
		int rs = userMessageService.send(sms);

		logger.debug("短信下发到[手机" + phone + ",内容:" + shortMsg + "]，消息服务返回的是:" + rs);

		if(rs < 1){
			map.put("message", new EisMessage(OperateResult.failed.id, "短信下发失败"));
			return frontMessageView;		
		}
		/*int insertUserResult = 0;
		if (user == null) {
			frontUser.setUserPassword(shortMsg);
			insertUserResult = frontUserService.insert(frontUser);
			logger.debug("新建一个用户:" + insertUserResult);
		} else {
			map.put("uuid", user.getUuid());
		}
		if (insertUserResult > 0) {
			globalUniqueService.create(new GlobalUnique(phone,ownerId));
			UserCriteria userCriteria = new UserCriteria(ownerId);
			userCriteria.setUsername(phone);
			List<User> userList = frontUserService.list(userCriteria);
			if (userList == null || userList.size() < 1) {
				logger.debug("没找到用户[" + phone + "]");
			}
			User user2 = userList.get(0);
			map.put("uuid", user2.getUuid());
		}*/

		/*Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		String signData = DataName.smsRegisterSign.toString() + "|" + phone + "|" + randomCode + "|" + new Date().getTime();
		String cookieValue = crypt.aesEncrypt(signData);
		cookieService.removeCookie(request, response, DataName.smsRegisterSign.toString(), null);
		cookieService.addCookie(request, response, DataName.smsRegisterSign.toString(), cookieValue, REGISTER_SMS_TTL, null);

		logger.debug("为手机注册用户放入手机校验内容:" + signData + "，加密后Cookie名:" + DataName.smsRegisterSign.toString() + ",内容:" + cookieValue);
		 */
		map.put("message",new EisMessage(OperateResult.success.id, "短信下发成功"));
		//为兼容客户端这种不保存Cookie的情况，向REDIS中放入对应数据
		String key = KeyConstants.REGISTER_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		logger.debug("获取验证码保存的redis键名"+key);
		centerDataService.setForce(key, randomCode, SMS_VALID_SECOND);


		return frontMessageView;
	}
	/**
	 * 邮箱注册
	 * @param request
	 * @param response
	 * @param map
	 * @param userBindMailBox
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/registerByMailBox", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String submitRegisterByMailBox(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			User frontUser) throws Exception  {
		String userBindMailBox = ServletRequestUtils.getStringParameter(request, "userBindMailBox");
		String userPassword = ServletRequestUtils.getStringParameter(request, "userPassword");
		logger.info("用户请求注册:{}/{}", userBindMailBox, userPassword);
		if(userBindMailBox == null || userBindMailBox.equals("")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入您的注册邮箱"));
			return frontMessageView;
		}
		if(!userBindMailBox.matches(CommonStandard.emailPattern)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入合法的邮箱"));
			return frontMessageView;
		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		frontUser.setUsername(userBindMailBox);
		frontUser.setUserPassword(userPassword);
		if(mailActive) {
			frontUser.setCurrentStatus(UserStatus.unactive.id);//需要邮箱激活
		} else {
			frontUser.setCurrentStatus(UserStatus.normal.id);//需要邮箱激活

		}
		frontUser.setOwnerId(ownerId);
		try {
			int createResult = frontUserService.insert(frontUser);
			map.put("userBindMailBox", userBindMailBox);
			if(createResult==1){
				submitBindEmail(request, response, map);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			logger.error("通过邮箱注册用户失败");
			map.put("message",new EisMessage(OperateResult.failed.id,"注册失败"));
			return frontMessageView;
		}
		logger.debug("邮箱注册完成");
		map.put("message",new EisMessage(OperateResult.success.id));
		return frontMessageView;
	}

	@RequestMapping(value="/registerByPhone/{phone}", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String submitRegisterByPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("phone")String phone,
			@RequestParam(value="eis_captcha",required=false)String captcha)  {
		map.put("operate", OperateCode.SEND_REGISTER_SMS.toString());
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		/**
		 * 为了支持某些客户端不能直接发送POST请求到一个没有参数的URL
		 */
		if(!phone.matches("^\\d{11}$")){
			phone = ServletRequestUtils.getStringParameter(request, "phone",null);
		}
		if(StringUtils.isBlank(phone) || !phone.matches("^\\d{11}$")){
			logger.warn("错误的手机号:" + phone);
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "手机号不正确"));
			return frontMessageView;		
		}



		String smsTemplate = configService.getValue(DataName.registerSmsValidateMessage.toString(), ownerId);
		if(smsTemplate == null){
			smsTemplate = DEFAULT_SMS_VALIDATE_MESSAGE;
		}

		boolean needCaptchaWhenRegisterByPhone = configService.getBooleanValue(DataName.needCaptchaWhenRegisterByPhone.toString(), ownerId);
		if(needCaptchaWhenRegisterByPhone){
			logger.info("收到用户提交的captcha是"+captcha);
			if (!captchaService.verify(request, response, null, captcha)){
				map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "验证码错误"));
				return frontMessageView;	
			}
		}
		int registerSmsIdelInterval = configService.getIntValue(DataName.registerSmsIdelInterval.toString(), ownerId);
		if(registerSmsIdelInterval > 0){
			//放入一个键值到REDIS，以防止用户重复提交
			String key = KeyConstants.REGISTER_SMS_LOCK_PREFIX + "#" + ownerId + "#" + phone;
			boolean setResult = centerDataService.setIfNotExist(key, sdf.format(new Date()), registerSmsIdelInterval);
			if(!setResult){
				String value = centerDataService.get(key);
				logger.debug("用户手机号[" + phone + "]已在[" + value + "]时提交验证码申请，还没到限制时间" + registerSmsIdelInterval + ",不允许发送注册短信");
				map.put("message", new EisMessage(EisError.operateTooSoon.id, "操作过于频繁，请稍候再试"));
				return frontMessageView;	
			}
		}

		if(globalUniqueService.exist(new GlobalUnique(phone, ownerId))){
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "您的用户名已经被占用，请换一个注册"));
			return frontMessageView;
		}
		Random random = new Random();
		String randomCode=String.valueOf(random.nextInt(999999)%(999999-100000+1) + 100000);
		logger.info("短信验证码是"+randomCode);

		String shortMsg = smsTemplate.replaceAll("\\$\\{smsCode\\}", randomCode);

		UserMessage sms = new UserMessage(ownerId);
		sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
		sms.setContent(shortMsg);
		sms.setReceiverName(phone);
		sms.setTitle(randomCode);
		sms.setSign("regist");
		sms.setCurrentStatus(MessageStatus.queue.id);
		int rs = userMessageService.send(sms);

		logger.debug("短信下发到[手机" + phone + ",内容:" + shortMsg + "]，消息服务返回的是:" + rs);

		if(rs < 1){
			map.put("message", new EisMessage(OperateResult.failed.id, "短信下发失败"));
			return frontMessageView;		
		}

		/*		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		String signData = DataName.smsRegisterSign.toString() + "|" + phone + "|" + randomCode + "|" + new Date().getTime();
		String cookieValue = crypt.aesEncrypt(signData);
		cookieService.removeCookie(request, response, DataName.smsRegisterSign.toString(), null);
		cookieService.addCookie(request, response, DataName.smsRegisterSign.toString(), cookieValue, REGISTER_SMS_TTL, null);

		logger.debug("为手机注册用户放入手机校验内容:" + signData + "，加密后Cookie名:" + DataName.smsRegisterSign.toString() + ",内容:" + cookieValue);
		 */
		map.put("message",new EisMessage(OperateResult.success.id, "短信下发成功"));
		//为兼容客户端这种不保存Cookie的情况，向REDIS中放入对应数据
		String key = KeyConstants.REGISTER_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		centerDataService.setForce(key, randomCode, SMS_VALID_SECOND);

		return frontMessageView;		
	}
	

	//提交绑定手机
	@RequestMapping(value="/bindPhone/submit", method=RequestMethod.POST)
	public String submitBindPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String phone) throws Exception {
		
		map.put("operate", OperateCode.SEND_BIND_PHONE_SMS.toString());

		if(!phone.matches("^\\d{11}$")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "不合法的手机号码"));
			return frontMessageView;
		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
			return frontMessageView;
		}
		boolean phoneBinded = frontUser.getBooleanExtraValue(DataName.userPhoneBound.toString());

		if(phoneBinded){
			map.put("message", new EisMessage(EisError.mailAlreadyBinded.id, "手机已绑定，不能重复绑定。"));
			return frontMessageView;
		}
		if(!frontUser.getUsername().equals(phone)){
			if(globalUniqueService.exist(new GlobalUnique(phone,ownerId))){		
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "已有相同的手机存在"));
				return frontMessageView;
			}
		}
		//放入一个键值到REDIS，以防止用户重复提交
		int findPasswordSmsIdelInterval = configService.getIntValue(DataName.findPasswordSmsIdelInterval.toString(), ownerId);
		if(findPasswordSmsIdelInterval < 1){
			findPasswordSmsIdelInterval = 30;
		}
		String key = KeyConstants.FIND_PASSWORD_SMS_LOCK_PREFIX + "#" + ownerId + "#" + phone;
		boolean setResult = centerDataService.setIfNotExist(key, sdf.format(new Date()), findPasswordSmsIdelInterval);
		if(!setResult){
			String value = centerDataService.get(key);
			logger.debug("用户手机号[" + phone + "]已在[" + value + "]时提交验证码申请，还没到限制时间" + findPasswordSmsIdelInterval + ",不允许发送注册短信");
			map.put("message", new EisMessage(EisError.operateTooSoon.id, "操作过于频繁，请稍候再试"));
			return frontMessageView;	
		}
		Random random = new Random();
		String randomCode=String.valueOf(random.nextInt(999999)%(999999-100000+1) + 100000);
		logger.info("短信验证码是"+randomCode);
		String smsTemplate = configService.getValue(DataName.bindPhoneValidateMessage.toString(), ownerId);
		if(smsTemplate == null){
			smsTemplate = DEFAULT_BIND_PHONE_VALIDATE_MESSAGE;
		}

		String shortMsg = smsTemplate.replaceAll("\\$\\{smsCode\\}", randomCode);

		UserMessage sms = new UserMessage(ownerId);
		sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
		sms.setContent(shortMsg);
		sms.setReceiverName(phone);
		sms.setTitle(randomCode);
		sms.setSign("changeInformation");
		sms.setCurrentStatus(MessageStatus.queue.id);
		int rs = userMessageService.send(sms);

		logger.debug("短信下发到[手机" + phone + ",内容:" + shortMsg + "]，消息服务返回的是:" + rs);

		if(rs < 1){
			map.put("message", new EisMessage(OperateResult.failed.id, "短信下发失败"));
			return frontMessageView;		
		}


		map.put("message",new EisMessage(OperateResult.success.id, "短信下发成功"));


		//为兼容客户端这种不保存Cookie的情况，向REDIS中放入对应数据
		key = KeyConstants.BIND_PHONE_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		centerDataService.setForce(key, randomCode, SMS_VALID_SECOND);
		return frontMessageView;
	}

	//确认绑定手机
	@RequestMapping(value="/bindPhone/confirm", method=RequestMethod.POST)
	public String confirmBindPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String phoneBindSign, String phone) throws Exception {
		map.put("operate", OperateCode.BIND_PHONE.toString());

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		if(!phoneBindSign.matches("^\\d{6}$")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "不合法的验证码"));
			return frontMessageView;
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录，请先登录"));
			return frontMessageView;
		}
		boolean phoneBinded = frontUser.getBooleanExtraValue(DataName.userPhoneBound.toString());
		if(phoneBinded){
			map.put("message", new EisMessage(EisError.phoneAlreadyBound.id, "手机已绑定，不能重复绑定。"));
			return frontMessageView;
		}



		//获取REDIS中的签名
		String	key = KeyConstants.BIND_PHONE_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		String oldSign = centerDataService.get(key);
		if(StringUtils.isBlank(oldSign)){
			logger.warn("在REDIS中找不到重置密码的sign:" + key);
			map.put("message", new EisMessage(EisError.requestTimeout.id, "短信验证码失效，请重新操作"));
			return frontMessageView;	
		}
		if(!oldSign.equals(phoneBindSign)){
			logger.warn("在REDIS中找到的重置密码的sign:" + oldSign + ",与用户提交的:" + phoneBindSign + "不一致");
			map.put("message", new EisMessage(EisError.requestTimeout.id, "短信验证码错误"));
			return frontMessageView;	
		}


		boolean replaceUserName = ServletRequestUtils.getBooleanParameter(request, "replaceUserName", false);
		String _oldUsername = null;
		if(replaceUserName){
			User _oldUser = frontUserService.select(frontUser.getUuid());
			if(_oldUser == null){
				logger.error("根据UUID=" + frontUser.getUuid() + "找不到指定用户");
				map.put("message", new EisMessage(EisError.accountNotExist.id, "找不到指定数据"));
				return frontMessageView;
			}

			_oldUsername = _oldUser.getUsername();
			frontUser.setUsername(phone);
			//修改用户名和密码为用户提交的手机号和密码
			String newPassword = ServletRequestUtils.getStringParameter(request, "newPassword");
			if(StringUtils.isBlank(newPassword)){
				logger.warn("用户请求替换用户名[" + _oldUsername + "]为手机号[" + phone + "]但未提交新密码");
			} else {
				frontUser.setUserPassword(UserUtils.correctPassword(newPassword));
			}

			


			frontUser.setUsername(phone);

			globalUniqueService.delete(new GlobalUnique(_oldUsername, ownerId));

		}


		frontUser.setExtraValue(DataName.userPhoneBound.toString(),  "true");
		frontUser.setExtraValue(DataName.userBindPhoneNumber.toString(),  phone);
		String memberCard = ServletRequestUtils.getStringParameter(request, "memberCard",null);
		logger.debug("用户[" + frontUser.getUuid() + "]绑定手机号时提交的会员卡号是:" + memberCard);
		if(StringUtils.isNotBlank(memberCard)){
			frontUser.getUserConfigMap().put(DataName.memberCard.toString(),  new UserData(frontUser.getUuid(), DataName.memberCard.toString(), phone));
		}

		EisMessage result = frontUserService.update(frontUser);

		if(result.getOperateCode() == -EisError.dataDuplicate.id){
			logger.error("无法修改用户名，返回是:" + result.getOperateCode() + ",可能是与其他用户名重复");
			map.put("message", new EisMessage(EisError.dataDuplicate.id, "您要修改的号码已存在"));
			return frontMessageView;
		}

		if(result.getOperateCode() != OperateResult.success.id){
			logger.error("无法修改用户名，返回是:" + result.getOperateCode());
			map.put("message", result);
			return frontMessageView;
		}
		if(replaceUserName){
			globalUniqueService.delete(new GlobalUnique(_oldUsername, ownerId));
		}
		if(!globalUniqueService.create(new GlobalUnique(phone,ownerId))){		
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "已有相同的手机存在"));
			return frontMessageView;
		}
		
		logger.debug("用户[" + frontUser.getUuid() +"]已正确绑定到手机:" + phone);


		map.put("message", new EisMessage(OperateResult.success.id, "您的帐号已成功绑定到手机:" + phone));
		map.put("frontUser", frontUser);
		return frontMessageView;
	}

	//提交通过手机找回密码
	@RequestMapping(value="/findPassword/phone/submit/{phone}", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String submitFindPasswordByPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("phone")String phone) throws Exception {
		logger.debug("提交通过手机找回密码");
		map.put("operate", OperateCode.SEND_FIND_PASSWORD_SMS.toString());
		String userName = ServletRequestUtils.getStringParameter(request, "userName");
		if(StringUtils.isBlank(phone) || phone.equals("1")){
			phone = ServletRequestUtils.getStringParameter(request, "phone");
		}
		if(StringUtils.isBlank(phone)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入手机号码"));
			return frontMessageView;
		}
		if(!phone.matches("^\\d{11}$")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "不合法的手机号码"));
			return frontMessageView;
		}

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		logger.debug("用户名username为"+userName);
		User frontUser = null;
		UserCriteria frontUserCriteria = new UserCriteria(ownerId);
		frontUserCriteria.setUsername(userName);
		frontUserCriteria.setCurrentStatus(UserStatus.normal.id);
		List<User> frontUserList = frontUserService.list(frontUserCriteria);
		/*if(frontUserList == null || frontUserList.size() != 1){
			//用户名不存在这样的手机号，尝试查找绑定手机，并通过该手机找出用户
			logger.debug("没有用户名为[" + userName + "]的用户，尝试查找绑定手机");

			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria.setDataCode(DataName.userBindPhoneNumber.toString());
			userDataCriteria.setDataValue(phone);
			userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
			userDataCriteria.setCorrectWithDynamicData(false);

			List<UserData> dataList = userDataService.list(userDataCriteria);
			logger.debug("全局查找[" + DataName.userBindPhoneNumber.toString() + "]的用户数据结果:" + dataList);
			if(dataList != null && dataList.size() == 1){
				if(dataList.get(0).getUuid() > 0){
					frontUser = frontUserService.select(dataList.get(0).getUuid());
					if(frontUser != null){
						if(frontUser.getCurrentStatus() != UserStatus.normal.id){
							frontUser = null;
						}
					}
				}
			}

		} else {
			frontUser = frontUserList.get(0);
		}*/
		if(frontUserList!=null){
			frontUser = frontUserList.get(0);
			
		}
		
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "用户不存在"));
			return frontMessageView;
		}
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setDataCode(DataName.userBindPhoneNumber.toString());
		userDataCriteria.setUuid(frontUser.getUuid());
		userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
		List<UserData> dataList = userDataService.list(userDataCriteria);
		logger.debug("心里烦"+dataList);
		logger.debug(dataList==null?"是的":"是的"+dataList.size());
		if(dataList != null){
			for(int i = 0;i<dataList.size();i++){
				if(DataName.userBindPhoneNumber.toString().equals(dataList.get(i).getDataCode())){
					if(!phone.equals(dataList.get(i).getDataValue())){
						logger.debug("该用户对应手机号"+dataList.get(i).getDataValue());
						map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "该手机号不是用户对应绑定的手机号"));
						return frontMessageView;
					}
				}
			}
			
		}else{
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "未找到唯一用户绑定的手机"));
			return frontMessageView;
		}
		//放入一个键值到REDIS，以防止用户重复提交
		int findPasswordSmsIdelInterval = configService.getIntValue(DataName.findPasswordSmsIdelInterval.toString(), ownerId);
		if(findPasswordSmsIdelInterval < 1){
			findPasswordSmsIdelInterval = 30;
		}
		String key = KeyConstants.FIND_PASSWORD_SMS_LOCK_PREFIX + "#" + ownerId + "#" + phone;
		boolean setResult = centerDataService.setIfNotExist(key, sdf.format(new Date()), findPasswordSmsIdelInterval);
		if(!setResult){
			String value = centerDataService.get(key);
			logger.debug("用户手机号[" + phone + "]已在[" + value + "]时提交验证码申请，还没到限制时间" + findPasswordSmsIdelInterval + ",不允许发送注册短信");
			map.put("message", new EisMessage(EisError.operateTooSoon.id, "操作过于频繁，请稍候再试"));
			return frontMessageView;	
		}


		String smsTemplate = configService.getValue(DataName.forgetPasswordSmsValidateMessage.toString(), ownerId);
		if(smsTemplate == null){
			smsTemplate = DEFAULT_FORGET_PASSWORD_SMS_VALIDATE_MESSAGE;
		}

		/*logger.info("收到用户提交的captcha是"+captcha);
		if (!captchaService.verify(request, response, captcha)){

			map.put("message", new EisMessage(EisError.dataFormatError.id, "验证码错误"));
			return frontMessageView;	
		}*/
		Random random = new Random();
		String randomCode=String.valueOf(random.nextInt(999999)%(999999-100000+1) + 100000);
		logger.info("重置密码，短信验证码是"+randomCode);

		String shortMsg = smsTemplate.replaceAll("\\$\\{smsCode\\}", randomCode);

		UserMessage sms = new UserMessage(ownerId);
		sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
		sms.setContent(shortMsg);
		sms.setReceiverName(phone);
		sms.setTitle(randomCode);
		sms.setSign("changePassword");
		sms.setCurrentStatus(MessageStatus.queue.id);
		int rs = userMessageService.send(sms);

		logger.debug("短信下发到[手机" + phone + ",内容:" + shortMsg + "]，消息服务返回的是:" + rs);

		if(rs < 1){
			map.put("message", new EisMessage(OperateResult.failed.id, "短信下发失败"));
			return frontMessageView;		
		}




		map.put("message",new EisMessage(OperateResult.success.id, "短信下发成功"));

		//为兼容客户端这种不保存Cookie的情况，向REDIS中放入对应数据
		key = KeyConstants.FIND_PASSWORD_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		centerDataService.setForce(key, randomCode, SMS_VALID_SECOND);
		return frontMessageView;
	}

	/*//确认通过手机找回密码
	@RequestMapping(value="/findPassword/phone/confirm/{phoneFindPasswordSign}", method=RequestMethod.POST)
	public String confirmFindPasswordByPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("phoneFindPasswordSign")String phoneFindPasswordSign) throws Exception {

		if(!phoneFindPasswordSign.matches("^\\d{6}$")){
			map.put("message", new EisMessage(EisError.dataFormatError.id, "不合法的验证码"));
			return frontMessageView;
		}

		User frontUser = null;

		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setDataCode(DataName.userPhoneFindPasswordSign.toString());
		userDataCriteria.setDataValue(phoneFindPasswordSign);
		userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
		userDataCriteria.setCorrectWithDynamicData(false);

		List<UserData> userDataList = userDataService.list(userDataCriteria);
		logger.debug("全局查找[" + DataName.userPhoneFindPasswordSign.toString() + "]的用户数据结果:" + (userDataList == null ? -1 : userDataList.size()));
		if(userDataList != null && userDataList.size() == 1){
			if(userDataList.get(0).getUuid() > 0){
				frontUser = frontUserService.select(userDataList.get(0).getUuid());
				if(frontUser != null){
					if(frontUser.getCurrentStatus() != UserStatus.normal.id){
						frontUser = null;
					}
				}
			}
		}


		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "用户不存在"));
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			map.put("message", new EisMessage(EisError.accountLocked.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.accountLocked.id);
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userPhoneFindPasswordSign.toString()) != null){
			Date lastSubmitTime = frontUser.getUserConfigMap().get(DataName.userPhoneFindPasswordSign.toString()).getCreateTime();
			if(lastSubmitTime != null){
				if((new Date().getTime() - lastSubmitTime.getTime()) /1000 < CommonStandard.smsSendInterval){
					map.put("message", new EisMessage(EisError.serviceUnavaiable.id, "提交过于频繁"));
					return frontMessageView;
				}
			}
		}


		if(frontUser.getUserConfigMap().get(DataName.userPhoneFindPasswordSign.toString()) == null){
			map.put("message", new EisMessage(EisError.findPasswordSignNotFoundInSystem.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.findPasswordSignNotFoundInSystem.id);
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userPhoneFindPasswordSign.toString()).getDataValue() == null){
			map.put("message", new EisMessage(EisError.findPasswordSignNotFoundInSystem.id, "错误的找回密码请求"));	
			logger.error("message: " + EisError.findPasswordSignNotFoundInSystem.id);
			return frontMessageView;
		}
		if(frontUser.getUserConfigMap().get(DataName.userPhoneFindPasswordSign.toString()).getDataValue().equals(phoneFindPasswordSign)){
			logger.info("签名一致，可以设置新密码:" + frontUser.getUuid());
			//certifyService.generateUserToken(frontUser);
			//certifyService.updateUserCookie(request, response, frontUser.getSsoToken());

			map.put("message", new EisMessage(OperateResult.success.id, frontUser.getUuid() + ""));

			return frontMessageView;
		}
		map.put("message", new EisMessage(EisError.dataFormatError.id, "错误的找回密码请求"));				
		logger.error("message: " + EisError.dataFormatError.id);
		return frontMessageView;
	}*/

	//忘记密码后通过手机修改密码
	@RequestMapping(value="/update/forgetPassword/phone", method=RequestMethod.POST)
	public String updateForgetPasswordByPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@RequestParam("sign")String smsSign,
			@RequestParam("userName")String userName,
			@RequestParam("newPassword1")String newPassword1,
			@RequestParam("newPassword2")String newPassword2,
			@RequestParam("phone")String phone)  throws Exception{
		map.put("operate", OperateCode.CHANGE_PASSWORD_FROM_SMS.toString());
		logger.debug("收到忘记密码的修改请求[" + smsSign + "]");

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;




		UserCriteria userCriteria = new UserCriteria(ownerId);
		userCriteria.setUsername(userName);
		userCriteria.setUserTypeId(UserTypes.frontUser.id);
		userCriteria.setCurrentStatus(UserStatus.normal.id);
		List<User> userList = frontUserService.list(userCriteria);
		if(userList != null && userList.size() > 0){
			frontUser = userList.get(0);
		} else {
			logger.warn("找不到用户名为:" + userName + "的正常状态前端用户");
		}
		if(frontUser == null){
			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria.setDataCode(DataName.userBindPhoneNumber.toString());
			userDataCriteria.setDataValue(phone);
			userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
			userDataCriteria.setCorrectWithDynamicData(false);

			List<UserData> dataList = userDataService.list(userDataCriteria);
			logger.debug("全局查找[" + DataName.userBindPhoneNumber.toString() + "]的用户数据结果:" + dataList);
			if(dataList != null && dataList.size() == 1){
				if(dataList.get(0).getUuid() > 0){
					frontUser = frontUserService.select(dataList.get(0).getUuid());
					if(frontUser != null){
						if(frontUser.getCurrentStatus() != UserStatus.normal.id){
							frontUser = null;
						}
					}
				}
			}
		}
		if(frontUser == null){
			logger.warn("找不到用户名为:" + userName + ",也没有绑定手机与之匹配的用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "用户不存在"));
			return frontMessageView;			
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			logger.warn("用户[" + frontUser.getUuid() + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "用户不存在"));
			return frontMessageView;	
		}
		//获取REDIS中的签名
		String	key = KeyConstants.FIND_PASSWORD_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		String oldSign = centerDataService.get(key);
		if(StringUtils.isBlank(oldSign)){
			logger.warn("在REDIS中找不到重置密码的sign:" + key);
			map.put("message", new EisMessage(EisError.requestTimeout.id, "短信验证码失效，请重新操作"));
			return frontMessageView;	
		}
		if(!oldSign.equals(smsSign)){
			logger.warn("在REDIS中找到的重置密码的sign:" + oldSign + ",与用户提交的:" + smsSign + "不一致");
			map.put("message", new EisMessage(EisError.requestTimeout.id, "短信验证码错误"));
			return frontMessageView;	
		}

		if(StringUtils.isBlank(newPassword1) || !newPassword1.equals(newPassword2)){
			map.put("message", new EisMessage(OperateResult.failed.id, "请提供正确的新密码"));
			return frontMessageView;
		}

		frontUser.setUserPassword(UserUtils.correctPassword(newPassword1));
		frontUserService.updateNoNull(frontUser);
		map.put("message", new EisMessage(OperateResult.success.id, "密码已修改，请稍后使用新密码登录"));

		centerDataService.delete(key);

		return frontMessageView;

	}

	
	//通过找回邮件，修改密码
	@RequestMapping(value="/update/forgetPassword/email/{uuid}/{sign}", method=RequestMethod.POST)
	public String updateForgetPasswordByEmail(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("uuid")int uuid, @PathVariable("sign")String sign) throws Exception{
		logger.debug("收到忘记密码的邮件修改请求[uuid=" + uuid + ",sign=" + sign + "]");

		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "用户不存在"));
			return frontMessageView;			
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "用户不存在"));
			return frontMessageView;	
		}

		boolean signIsValid = false;

		logger.debug("用户以邮件方式找回密码");
		if(frontUser.getUserConfigMap() != null){
			/*for(UserData ud : frontUser.getUserConfigMap().values()){
					logger.debug(ud.getDataCode() + "=====" + ud.getDataValue() + "===" + ud.getCurrentStatus());
				}*/
			if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()) != null && frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue() != null){
				if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue().equals(sign)){
					signIsValid = true;		
					frontUser.getUserConfigMap().remove(DataName.userMailFindPasswordSign.toString());

				}
			} else {
				map.put("message", new EisMessage(EisError.dataError.id, "您未请求邮件方式找回密码，如果之前请求过，可能已失效，请重新请求。"));
				return frontMessageView;
			}
		}

		if(signIsValid){
			String password1 =ServletRequestUtils.getStringParameter(request, "newPassword");
			String password2 =ServletRequestUtils.getStringParameter(request, "newPassword2");
			if(password1 == null || password2 == null || !password1.equals(password2)){
				map.put("message", new EisMessage(OperateResult.failed.id, "请提供正确的新密码"));
				return frontMessageView;
			}

			frontUser.setUserPassword(password1);
			frontUserService.update(frontUser);
			map.put("message", new EisMessage(OperateResult.success.id, "密码已修改，请稍后使用新密码登录"));
			return frontMessageView;
		}

		map.put("message", new EisMessage(OperateResult.failed.id, "请提供正确的校验码"));
		return frontMessageView;
	}
	/**
	 * 通过找回邮件用户名，修改密码
	 * @param request
	 * @param response
	 * @param map
	 * @param sign
	 * @param username
	 * @return
	 * @throws Exception
	 */
		@RequestMapping(value="/update/forgetPassword/email/{sign}", method=RequestMethod.POST)
		public String updateForgetPasswordByEmailName(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
				@PathVariable("sign")String sign,@RequestParam("username")String username) throws Exception{
			logger.debug("收到忘记密码的邮件修改请求[用户名=" + username+ ",sign=" + sign + "]");
			long ownerId = 0;
			try{
				ownerId = (long)map.get("ownerId");
			}catch(Exception e){
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
			if(ownerId < 1){
				logger.error("系统会话中没有ownerId数据");
				map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
				return frontMessageView;		
			}
			UserCriteria uc = new UserCriteria();
			uc.setUsername(username);
			uc.setOwnerId(ownerId);
			uc.setCurrentStatus(UserStatus.normal.id);
			List<User> userList = frontUserService.list(uc);
			if(userList == null){
				map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "用户不存在"));
				return frontMessageView;			
			}
			User frontUser = userList.get(0);
			if(frontUser.getCurrentStatus() != UserStatus.normal.id){
				map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "用户账号不正常"));
				return frontMessageView;	
			}

			boolean signIsValid = false;

			logger.debug("用户以邮件方式找回密码");
			if(frontUser.getUserConfigMap() != null){
				/*for(UserData ud : frontUser.getUserConfigMap().values()){
						logger.debug(ud.getDataCode() + "=====" + ud.getDataValue() + "===" + ud.getCurrentStatus());
					}*/
				if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()) != null && frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue() != null){
					if(frontUser.getUserConfigMap().get(DataName.userMailFindPasswordSign.toString()).getDataValue().equals(sign)){
						signIsValid = true;		
						frontUser.getUserConfigMap().remove(DataName.userMailFindPasswordSign.toString());

					}
				} else {
					map.put("message", new EisMessage(EisError.dataError.id, "您未请求邮件方式找回密码，如果之前请求过，可能已失效，请重新请求。"));
					return frontMessageView;
				}
			}

			if(signIsValid){
				String password1 =ServletRequestUtils.getStringParameter(request, "newPassword");
				String password2 =ServletRequestUtils.getStringParameter(request, "newPassword2");
				if(password1 == null || password2 == null || !password1.equals(password2)){
					map.put("message", new EisMessage(OperateResult.failed.id, "请提供正确的新密码"));
					return frontMessageView;
				}

				frontUser.setUserPassword(password1);
				frontUser.setExtraValue(DataName.userMailFindPasswordSign.toString(), "");
				frontUserService.update(frontUser);
				map.put("message", new EisMessage(OperateResult.success.id, "密码已修改，请稍后使用新密码登录"));
				return frontMessageView;
			}

			map.put("message", new EisMessage(OperateResult.failed.id, "请提供正确的校验码"));
			return frontMessageView;
		}
	private void sendBindMailMessage(HttpServletRequest request, User frontUser, String bindMailBox){
		String bindMailUrl = request.getScheme() + "://";
		bindMailUrl += request.getServerName();
		bindMailUrl += (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
		bindMailUrl += "/user/bindEmail/confirm"+ CommonStandard.DEFAULT_PAGE_SUFFIX+"?uuid=" +frontUser.getUuid()+"&sign="+ frontUser.getUserConfigMap().get(DataName.userMailBindSign.toString()).getDataValue();
		logger.debug("发送用户[" + frontUser.getUuid() +"]绑定邮箱的邮件到" + bindMailBox + "，链接:" + bindMailUrl);
		StringBuffer templateContent = new StringBuffer();
		try{
			String bindMailTemplateRealPath = request.getSession().getServletContext().getRealPath(bindMailTemplate);
			logger.debug("读取绑定邮箱的邮件内容模版:" + bindMailTemplateRealPath);
			//读取模版
			File file = new File(bindMailTemplateRealPath);
			if(!file.exists()){
				throw new ObjectFromServerIsNullException("无法找回密码绑定模板:" + bindMailTemplateRealPath);

			}

			//BufferedReader bufread=new BufferedReader(new FileReader(file));
			BufferedReader bufread= new   BufferedReader   (new   InputStreamReader(new   FileInputStream(bindMailTemplateRealPath),CommonStandard.DEFAULT_ENCODING));
			String read = "";
			while((read=bufread.readLine()) != null)
			{
				templateContent.append(read);
			}
			bufread.close();
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new ServiceUnavailableException("系统内部错误");
		}
		String msg = templateContent.toString();
		msg = msg.replaceAll("\\$\\{bindMailUrl\\}", bindMailUrl);

		UserMessage emailMessage = new UserMessage();
		emailMessage.setTitle(configService.getValue(DataName.systemName.toString(),0) == null ? "" : configService.getValue(DataName.systemName.toString(),0) + "邮箱绑定申请"); 
		emailMessage.setContent(msg);
		emailMessage.setPerferMethod(new String[]{UserMessageSendMethod.email.toString()});
		emailMessage.setReceiverName(bindMailBox);
		emailMessage.setSenderStatus(MessageStatus.queue.id);
		userMessageService.send(emailMessage);
	}

	@SuppressWarnings("unused")
	private void sendWelcomeMailMessage(HttpServletRequest request, User frontUser, String bindMailBox){
		String bindMailUrl = request.getScheme() + "://";
		bindMailUrl += request.getServerName();
		bindMailUrl += (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
		bindMailUrl += "/content/user/confirmBindEmail" + CommonStandard.DEFAULT_PAGE_SUFFIX + "?uuid=" + frontUser.getUuid() + "&sign=" + frontUser.getUserConfigMap().get(DataName.userMailBindSign.toString()).getDataValue();
		logger.debug("发送首次注册用户[" + frontUser.getUuid() +"]欢迎邮箱的邮件到" + bindMailBox + "，链接:" + bindMailUrl);
		StringBuffer templateContent = new StringBuffer();
		String bindMailTemplateRealPath = request.getSession().getServletContext().getRealPath(bindMailTemplate);
		logger.debug("读取欢迎邮件的内容模版:" + bindMailTemplateRealPath);
		//读取模版
		File file = new File(bindMailTemplateRealPath);
		if(!file.exists()){
			templateContent.append(CommonStandard.defaultUserRegisterWelcomeMailContent);				
		} else {
			try{
				BufferedReader bufread= new   BufferedReader   (new   InputStreamReader(new   FileInputStream(bindMailTemplateRealPath),CommonStandard.DEFAULT_ENCODING));
				String read = "";
				while((read=bufread.readLine()) != null)
				{
					templateContent.append(read);
				}
				bufread.close();
			}catch(Exception e){}
		}

		String msg = templateContent.toString();
		msg = msg.replaceAll("\\$\\{bindMailUrl\\}", bindMailUrl);
		UserMessage emailMessage = new UserMessage();
		emailMessage.setTitle(configService.getValue(DataName.systemName.toString(),0) == null ? "" : configService.getValue(DataName.systemName.toString(),0) + "邮箱绑定申请"); 
		emailMessage.setContent(msg);
		emailMessage.setPerferMethod(new String[]{UserMessageSendMethod.email.toString()});
		emailMessage.setReceiverName(bindMailBox);
		emailMessage.setSenderStatus(MessageStatus.queue.id);
		userMessageService.send(emailMessage);

	}

	private void setIpAddress(HttpServletRequest request, User user){
		String realIp = null;
		try{
			realIp = request.getHeader("X-Real-IP");
		}catch(Exception e){

		}
		if(realIp != null){
			user.setLastLoginIp(realIp);
		} else {
			user.setLastLoginIp(request.getRemoteAddr());
		}
	}

	private void syncUserLocation(long uuid, String locationType, String locationId,
			String locationX, String locationY, String locationZ) {
		int locationIdNumber = 0;
		float locationXNumber = 0f;
		float locationYNumber = 0f;
		float locationZNumber = 0f;

		if(StringUtils.isNumeric(locationId)){
			locationIdNumber = Integer.parseInt(locationId);
		}
		if(StringUtils.isNumeric(locationX)){
			locationXNumber = Float.parseFloat(locationX);
		}
		if(StringUtils.isNumeric(locationY)){
			locationYNumber = Float.parseFloat(locationY);
		}
		if(StringUtils.isNumeric(locationZ)){
			locationZNumber = Float.parseFloat(locationZ);
		}
		Location location = new Location(ObjectType.user.name(), uuid, locationType, locationIdNumber, locationXNumber, locationYNumber, locationZNumber);
		userDynamicDataService.applyLocation(uuid, location);

	}
	/**
	 * 发送邮件
	 * @param request
	 * @param response
	 * @param map
	 * @param uuid
	 * @param cryptedData
	 * @param title
	 * @param receiver
	 * @return
	 */
	@RequestMapping(value="/sendEmail", method=RequestMethod.POST)
	public String sendEmail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("receiverName")String receiverName,
			@RequestParam("title")String title,
			@RequestParam("content")String content){
		logger.debug("发送邮件的信息"+uuid+"#"+receiverName+"#"+title+"#"+content);
		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		
		/*String cryptKey = null;
		try{
			cryptKey = frontUser.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		}catch(Exception e){
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if(StringUtils.isBlank(cryptKey)){
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		//	logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

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
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}*/
		UserCriteria uc = new UserCriteria();
		uc.setUsername(receiverName);
		List<User> userList = frontUserService.list(uc);
		if(userList ==null || userList.size()<1){
			logger.warn("未查到收件人信息");
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "请输入正确的收件人"));
			return frontMessageView;
		}
		User receiveUser = userList.get(0);
		map.put("receiveUser", receiveUser);
		Map<String, String> requestDataMap = new HashMap<String,String>();
		requestDataMap.put("content", content);
		requestDataMap.put("receiverName", receiverName);
		requestDataMap.put("title", title);
		String view = _sendEmail(requestDataMap, frontUser, map);
		return view;
	}
	
	private String _sendEmail(Map<String, String> dataMap,
			User frontUser, ModelMap map) {
		if(dataMap.get("content") == null){
			map.put("message", new EisMessage(EisError.dataError.id,"意见反馈没有任何内容"));
			return frontMessageView;
		}
		UserMessage userMessage = new UserMessage();
		userMessage.setOwnerId(frontUser.getOwnerId());
		userMessage.setSenderId(frontUser.getUuid());
		userMessage.setSenderName(frontUser.getUsername());
		userMessage.setContent(dataMap.get("content"));
		userMessage.setTitle(dataMap.get("title"));
		userMessage.setReceiverId(((User)(map.get("receiveUser"))).getUuid());
		userMessage.setReceiverName(((User)(map.get("receiveUser"))).getUsername());
		userMessage.setCurrentStatus(MessageStatus.unread.id);
		userMessage.setMessageId("0" + globalOrderIdService.generate(TransactionType.userMessage.id));
		userMessageService.insertLocal(userMessage);

		map.put("message", new EisMessage(OperateResult.accept.id,"信息发送成功"));
		return frontMessageView;	}
	/**
	 * 忘记密码发送的验证码
	 * @throws Exception 
	 */
	@RequestMapping(value="/sendUserMailFindPasswordSign")
	public String sendUserMailFindPasswordSign(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("userBindMailBox")String userBindMailBox,@RequestParam("username")String username) throws Exception{
		String view = "message/frontMessage";
		if(userBindMailBox == null || userBindMailBox.equals("") || username==null || "".equals(username)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入您的邮箱和用户名"));
			return view;
		}
		if(!userBindMailBox.matches(CommonStandard.emailPattern)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入合法的邮箱"));
			return view;
		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return view;		
		}
		User frontUser = null;
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(username);
		frontUserCriteria.setCurrentStatus(UserStatus.normal.id);
		List<User> frontUserList = frontUserService.list(frontUserCriteria);
		if(frontUserList == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSystem.id, "用户不存在"));
			return view;
		}
		frontUser = frontUserList.get(0);
		if(!userBindMailBox.equals(frontUser.getUserConfigMap().get(DataName.userBindMailBox.toString()).getDataValue())){
			map.put("message", new EisMessage(EisError.ACCOUNT_ERROR.id, "用户或邮箱错误"));
			return view;
		}
		String sign = Crypt.passwordEncode(CommonStandard.mailBindKey + frontUser.getUuid()+new Date().getTime());
		sign = sign.substring(0, 6);
		frontUser.setExtraValue(DataName.userMailFindPasswordSign.toString(), sign);
		frontUserService.update(frontUser);
		UserMessage emailMessage = new UserMessage();
		emailMessage.setTitle(configService.getValue(DataName.systemName.toString(),0) == null ? "" : configService.getValue(DataName.systemName.toString(),0) + "找回密码验证码"); 
		emailMessage.setContent("您的验证码是："+sign);
		emailMessage.setPerferMethod(new String[]{UserMessageSendMethod.email.toString()});
		emailMessage.setReceiverName(userBindMailBox);
		emailMessage.setSenderStatus(MessageStatus.queue.id);
		userMessageService.send(emailMessage);
		return view;
	}
	//加密形式提交一个用户意见反馈
	@RequestMapping(value="/feedBack/submitCrypt", method=RequestMethod.POST)
	public String submitFeedBack(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) {
		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		if(frontUser.getCurrentStatus() != UserStatus.normal.id){
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
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
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		//	logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

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
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "登录失败，请检查您的帐号密码是否正确"));
			return frontMessageView;
		}
		String view = _feedBack(HttpUtils.getRequestDataMap(clearData), frontUser, map);
		return view;
	}

	private String _feedBack(Map<String, String> dataMap,
			User frontUser, ModelMap map) {
		if(dataMap.get("content") == null){
			map.put("message", new EisMessage(EisError.dataError.id,"意见反馈没有任何内容"));
			return frontMessageView;
		}
		UserMessage userMessage = new UserMessage();
		userMessage.setTopicId(feedBackTopicId);
		userMessage.setSenderId(frontUser.getUuid());
		userMessage.setContent(dataMap.get("content"));
		userMessageService.send(userMessage);

		map.put("message", new EisMessage(OperateResult.accept.id,"我们已接收了您的意见反馈"));
		return frontMessageView;	}

	//@ResponseBody
	@RequestMapping(value="/favorites",method=RequestMethod.POST)
	public String  insertFavorites(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam("documentCode") String documentCode){
		String uuid=cookieService.getCookie(request, "uuid");
		if (uuid==null){
			return "2";
		}
		String custdata=uuid+"#make#"+documentCode;
		logger.info("custdata是"+custdata+"frontUserService!!!!!!!!!!!!"+frontUserService+"!!!!!!!!!!!!!!!!!!!!");
		String isCollection=frontUserService.makeCollection(custdata);
		EisMessage favoritesResult =  new EisMessage(Integer.valueOf(isCollection), "收藏");
		map.put("message",favoritesResult);
		//return String.valueOf(isCollection);
		return frontMessageView;
	}

	/**
	 * 单纯的根据当前用户修改用户名
	 * @param request
	 * @param response
	 * @param map
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changeUserNameByLocalUser", method=RequestMethod.POST)
	public String submitChangeUserName(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("username") String username 
			) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser=null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录"));
			return frontMessageView;
		}
		
		if(StringUtils.isBlank(username) || dirtyDictService.isDirty(username)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入正确并合法的数据"));
			return frontMessageView;
		}
		if(frontUser.getUsername().equals(username)){
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id, "与当前用户名重复"));
			return frontMessageView;
		}
		String oldUsername = frontUser.getUsername();
		frontUser.setUsername(username);

		EisMessage result = frontUserService.update(frontUser);

		if(result.getOperateCode() == -EisError.dataDuplicate.id){
			logger.error("无法修改用户名，返回是:" + result.getOperateCode() + ",可能是与其他用户名重复");
			map.put("message", new EisMessage(EisError.dataDuplicate.id, "您要修改的号码已存在"));
			return frontMessageView;
		}

		if(result.getOperateCode() != OperateResult.success.id){
			logger.error("无法修改用户名，返回是:" + result.getOperateCode());
			map.put("message", result);
			return frontMessageView;
		}

		globalUniqueService.delete(new GlobalUnique(oldUsername, ownerId));
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + request.getServerName() + "]找不到指定的站点关系");
			return EisError.AUTH_FAIL.toString();	
		}
		cookieService.removeCookie(request, response, "eis_username",siteDomainRelation.getCookieDomain());
		cookieService.addCookie(request, response, "eis_username", frontUser.getUsername(), sessionTimeout, siteDomainRelation.getCookieDomain(), false);
		map.put("message",new EisMessage(OperateResult.success.id, "用户名修改成功"));
		return frontMessageView;
	}

	/**
	 * 修改自己的用户名<br/>
	 * 一般用于强制以手机号注册为用户名的系统<br/>
	 * 系统配置必须有canChangeUserName=true
	 */
	@RequestMapping(value="/changeUserName", method=RequestMethod.POST)
	public String submitChangeUserName(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("password") String password,
			@RequestParam("username1") String username1,
			@RequestParam("username2") String username2 
			) throws Exception {
		if(StringUtils.isBlank(username1)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入正确的数据"));
			return frontMessageView;
		}
		if(StringUtils.isBlank(username2)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请输入正确的数据"));
			return frontMessageView;
		}
		if(StringUtils.isBlank(password)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "请提供您当前的密码"));
			return frontMessageView;
		}
		//XXX
		if(!username1.matches("^\\d{11}$")){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "不合法的手机号码"));
			return frontMessageView;
		}
		if(!username1.equals(username2)){
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "两次输入不一致"));
			return frontMessageView;
		}


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		boolean canChangeUserName = configService.getBooleanValue(DataName.canChangeUserName.toString(), ownerId);
		if(!canChangeUserName){
			logger.error("当前系统不允许修改用户名");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"不允许修改该数据"));
			return frontMessageView;	
		}

		boolean exist = globalUniqueService.exist(new GlobalUnique(username1, ownerId));
		if(exist){
			logger.error("用户尝试修改的用户名[" + username1 + "]已存在于唯一性数据中");
			map.put("message", new EisMessage(EisError.dataDuplicate.id,"您要修改的号码已存在"));
			return frontMessageView;	
		}



		User frontUser=null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录"));
			return frontMessageView;
		}

		String oldUsername = frontUser.getUsername();


		User _oldUser = frontUserService.select(frontUser.getUuid());

		if(_oldUser == null){
			logger.error("根据UUID=" + frontUser.getUuid() + "找不到指定用户");
			map.put("message", new EisMessage(EisError.accountNotExist.id, "找不到指定数据"));
			return frontMessageView;
		}
		String inputPassword = null;

		if(UserUtils.isLegacyPassword(_oldUser.getUserPassword())){
			inputPassword = Crypt.legacyPasswordEncode(password);
		} else {
			inputPassword = Crypt.passwordEncode(password);
		}
		if(!_oldUser.getUserPassword().equals(inputPassword)){
			logger.error("修改用户名时，提交的密码[" + password + "/" + inputPassword + "]与已存在的[" + _oldUser.getUserPassword() + "]不一致");
			map.put("message", new EisMessage(EisError.accountNotExist.id, "密码错误"));
			return frontMessageView;
		}

		_oldUser.setUsername(username1);

		EisMessage result = frontUserService.update(_oldUser);

		if(result.getOperateCode() == -EisError.dataDuplicate.id){
			logger.error("无法修改用户名，返回是:" + result.getOperateCode() + ",可能是与其他用户名重复");
			map.put("message", new EisMessage(EisError.dataDuplicate.id, "您要修改的号码已存在"));
			return frontMessageView;
		}

		if(result.getOperateCode() != OperateResult.success.id){
			logger.error("无法修改用户名，返回是:" + result.getOperateCode());
			map.put("message", result);
			return frontMessageView;
		}

		globalUniqueService.delete(new GlobalUnique(oldUsername, ownerId));

		map.put("message",new EisMessage(OperateResult.success.id, "用户名修改成功"));
		//修改密码后尝试退出
		certifyService.logout(request, response, _oldUser);;
		return frontMessageView;
	}

	/**
	 * 修改用户头像
	 */
	@RequestMapping(value="/changeHeadPic", method=RequestMethod.POST)
	public String submitChangeHeadPic(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam MultipartFile image
			) throws Exception {


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		User frontUser=null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您尚未登录"));
			return frontMessageView;
		}

		String fileName = frontUser.getExtraValue(DataName.userHeadPic.toString());
		if(fileName == null){
			fileName = UUIDFilenameGenerator.generateWithDatePath(image.getOriginalFilename());
			logger.debug("用户之前没有userHeadPic数据，将生成新的文件:" + fileName);
		} else {
			logger.debug("用户已有userHeadPic数据，使用原有文件名:" + fileName);
		}
		fileName = fileName.substring(fileName.indexOf("/")+1);
		//=======================
		/*MultipartFile file = null;
		logger.debug("fileName的值"+fileName);
		if(fileName == null){
			if (request instanceof MultipartHttpServletRequest) {
				file =  ((MultipartHttpServletRequest) request).getFile("fileupload");
				logger.debug("file的值"+file);
				if(file!=null){
					String orignName = file.getOriginalFilename();
					fileName = UUIDFilenameGenerator.generateWithDatePath(file.getOriginalFilename());
					logger.debug("orignName的值"+orignName+"#"+fileName);
				}
			}
		}*/
		if(userUploadDir == null){
			userUploadDir = request.getSession().getServletContext().getRealPath("/userUpload");

			logger.warn("系统未定义用户保存目录，将文件保存到:" + userUploadDir);

		}
		FileUtils.forceMkdir(new File(userUploadDir));
		String 	fileUploadSavePath = userUploadDir.replaceAll("/$", "").replaceAll("\\$", "") + File.separator;


		String fileDest = fileUploadSavePath  + fileName;
		logger.debug("用户头像文件保存到:" + fileDest);

		File _oldFile = new File(fileDest);
		_oldFile.delete();

		File dest = new File(fileDest);
		image.transferTo(dest);



		UserData userHeadPicData = new UserData(frontUser.getUuid(), DataName.userHeadPic.toString(),null);
		userHeadPicData.setDataValue(fileName);
		frontUser.getUserConfigMap().put(DataName.userHeadPic.toString(), userHeadPicData);
		frontUserService.update(frontUser);
		refreshFrontUser(request, response, frontUser);
		return frontMessageView;		

	}

	//提交绑定手机
	@RequestMapping(value="/acceptLicense")
	public String acceptLicense(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = (long)map.get("ownerId");
		
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}



		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			logger.debug("未找到登录用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return frontMessageView;
		}
		
		frontUser.setExtraValue("licenseAccepted", "true");
		map.put("message", frontUserService.update(frontUser));
		return frontMessageView;
	}
	/**
	 * 	查询用户购买记录
	 *  @author xiangqiaogao
	 */
	@RequestMapping(value="/purchasedList")
	public String purchaseList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("进入VIP订购和产品订购订单查询");
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			logger.debug("未找到登录用户");
			String flag = ServletRequestUtils.getStringParameter(request, "flag","product");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			map.put("flag", flag);
			return "node/user/purchasedList";
			//return frontMessageView;
		}
		List<HashMap<String, Object>> productPurchaseList = new ArrayList<HashMap<String, Object>>();
		List<String> vipTypeList = new ArrayList<String>(Arrays.asList("VIP_YEAR", "VIP_MONTH", "VIP_QUARTER"));
		List<Item> vipPurchaseList = new ArrayList<Item>();
		int productPurchaseNum = 0;
		BigDecimal productPurchaseMoney = new BigDecimal("0.0");
		int vipPurchaseNum = 0;
		BigDecimal vipPurchaseMoney = new BigDecimal("0.0");
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		String flag = ServletRequestUtils.getStringParameter(request, "flag","product");
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setChargeFromAccount(frontUser.getUuid());
		int currentStaArr[] = new int[]{
				TransactionStatus.success.id,
				TransactionStatus.deliveryConfirmed.id,
				TransactionStatus.delivering.id,
				TransactionStatus.preDelivery.id,
				TransactionStatus.waitingComment.id,
				TransactionStatus.commentClosed.id};
		itemCriteria.setCurrentStatus(currentStaArr);
		itemCriteria.setOwnerId(ownerId);
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		itemCriteria.setPaging(paging);
		List<Item> itemList = itemService.list(itemCriteria);
		if(itemList==null){
			logger.debug("没有找到对应的订单");
		}
		for(Item item :itemList){
			Product pro = productService.select(item.getProductId());
			if(pro==null){
				logger.debug("没有找到productId为"+item.getProductId()+"的产品");
				continue ;
			}
			if(flag.equals("vip") &&vipTypeList.contains(pro.getProductCode())){
				logger.debug("会员购买查询");
				vipPurchaseNum++;
				vipPurchaseMoney = vipPurchaseMoney.add(new BigDecimal(String.valueOf(item.getLabelMoney())));
				Item item2 = new Item();
				item2.setTransactionId(item.getTransactionId());
				item2.setEnterTime(item.getEnterTime());
				item2.setName(item.getName());
				item2.setCurrentStatus(item.getCurrentStatus());
				item2.setLabelMoney(item.getLabelMoney());
				vipPurchaseList.add(item2);
			}else if(flag.equals("product")){
				logger.debug("产品购买查询");
				HashMap<String, Object> hashMap = new HashMap<String,Object>();
				Document refDocument = null;
				try {
					refDocument = productService.getRefDocument(pro);
				} catch (Exception e) {
					logger.debug("查询productId为"+item.getProductId()+"的产品出错");
				}
				if(refDocument==null){
					logger.debug("没有查询到productId为"+item.getProductId()+"的产品对应的文档");
				}else {
					hashMap.put("refUrl", refDocument.getViewUrl());
				}
				hashMap.put("author", refDocument==null?"作者不详":refDocument.getAuthor());
				productPurchaseNum++;
				productPurchaseMoney = productPurchaseMoney.add(new BigDecimal(String.valueOf(item.getLabelMoney())));
				hashMap.put("transactionId", item.getTransactionId());
				hashMap.put("startTime", item.getEnterTime());
				hashMap.put("name", item.getName());
				hashMap.put("labelMoney", item.getLabelMoney());
				hashMap.put("cartId", item.getCartId());
				hashMap.put("deliveryed", pro.getExtraValue("needDelivery"));
				productPurchaseList.add(hashMap);
			}
		}
		logger.debug("VIP数据和Product数据分别为："+vipPurchaseNum+"#"+vipPurchaseMoney+"#"+vipPurchaseList+"#"+productPurchaseNum+"#"+productPurchaseMoney+"#"+productPurchaseList+"#");
		if(flag.equals("vip")){
			Map<String,Object> vipMap = new HashMap<String,Object>();
			vipMap.put("vipPurchaseNum", vipPurchaseNum);
			vipMap.put("vipPurchaseMoney", vipPurchaseMoney);
			vipMap.put("vipPurchaseList", vipPurchaseList);
			map.put("vipMap", vipMap);
			if(vipService != null){
				Date vipTime = vipService.getExpireTime(frontUser);
				map.put("vipTime",vipTime);
				if(vipTime != null && vipTime.after(new Date())){
					//用户的VIP订购还有效
					map.put("vipValid", true);
				}
				logger.debug("当前用户#{}的VIP到期时间是:{}", frontUser.getUuid(), StringTools.getFormattedTime(vipTime));
			}
		}
		if(flag.equals("product")){
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("productPurchaseNum", productPurchaseNum);
			productMap.put("productPurchaseMoney", productPurchaseMoney);
			productMap.put("productPurchaseList", productPurchaseList);
			map.put("productMap", productMap);
		}
		int totalRows = itemService.count(itemCriteria);
		map.put("flag", flag);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		return "node/user/purchasedList";
	}
	
	/**
	 *  其他非常用推荐
	 *  @author xiangqiaogao
	 *  @date 2017年12月7日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/otherRecommendsList")
	public void otherRecommendsList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("其他非常用推荐");
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return ;		
		}
		NodeCriteria nodeCri = new NodeCriteria();
		nodeCri.setOwnerId(ownerId);
		nodeCri.setNodeTypeId(NodeType.business.id);
		List<Node> nodeList = nodeService.list(nodeCri);
		logger.debug("需要显示数据的栏目共有"+(nodeList==null?0:nodeList.size())+"条");
		for(Node nod : nodeList){
			int pageNum = 1;
			List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
			List<Document> docList = new ArrayList<Document>();
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Paging paging = new Paging(rowsPerPage);
			DocumentCriteria docCri = new DocumentCriteria();
			docCri.setNodePath(nod.getPath());
			paging.setCurrentPage(pageNum);
			docCri.setPaging(paging);
			docList = documentService.listOnPage(docCri);
			if(docList!=null){
				for(Document doc:docList){
					hasMap = new HashMap<String,Object>();
					if(doc!=null){
						Product product = productService.getProductByDocument(doc);
						if(product==null){
							continue;
						}
						hasMap.put("document", getDocByDelDataMap(doc));
//						writeCommentCount(doc);
//						writeReadCount(doc);
						try {
							hasMap.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
							hasMap.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
						} catch (Exception e) {
							logger.error("找不到发布人的信息");
						}
						hasMap.put("buy_money", product.getBuyMoney());
						hasMap.put("vipFree", product.getExtraValue("vipFree"));
						listMAP.add(hasMap);
					}
				}
			}
			map.put(nod.getAlias(), listMAP);
			logger.debug("节点"+nod.getAlias()+"的listMAP"+listMAP);
		}
				return;
	}
	/**
	 *  我的推荐、最新上架、热销、网站推荐
	 *  @author xiangqiaogao
	 *  @date 2017年12月7日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/recommendsList")
	public String recommendsList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("进入我的推荐、最新上架、热销、网站推荐");
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		writeHotSaleList(request, response, frontUser, map);
		writeNewestList(request, response,frontUser, map);
		writeRecommendList(request, response,frontUser, map);
		writeAllFigureList(request, response, frontUser, map);
		writeMyGroom(frontUser, map);
		return frontMessageView;
	}
	/**
	 * 我的推荐
	 * @param frontUser
	 * @param map
	 */
	protected void writeMyGroom(User frontUser,Map<String,Object> map){
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setRelationType("groom");
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setOwnerId(frontUser.getOwnerId());
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> list = userRelationService.listOnPage(userRelationCriteria);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(UserRelation ur :list){
			HashMap<String, Object> hashMap = new HashMap<String,Object>();
			Document doc = documentService.select((int)ur.getObjectId());
			hashMap.put("userRelationId", ur.getUserRelationId());
			hashMap.put("doc", doc);
			listMap.add(hashMap);
		}
		map.put("myGroomList", listMap);
	}
	/**
	 * 首页获取最新上架
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */
	protected void writeNewestList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		logger.debug("首页获取最新发布上架");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(130005);
		documentCriteria.setOrderBy(" publish_time desc ");
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		documentCriteria.setOwnerId(ownerId);
		List<Document> indexHotSaleList= documentService.listOnPage(documentCriteria);
//		logger.debug("数量："+indexHotSaleList==null?"空":indexHotSaleList.size()+"");
		for(Document doc:indexHotSaleList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
//			Document doc = document.clone();
//			writeRelationCount(doc);
			Product pro = productService.getProductByDocument(doc);
			if(pro==null){
				continue;
			}
			hasMap.put("buy_money", pro.getBuyMoney());
			hasMap.put("document", getDocByDelDataMap(doc));
			hasMap.put("vipFree", pro.getExtraValue("vipFree"));
//			writeCommentCount2(doc, hasMap);
			try {
				User u = frontUserService.select(doc.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}
			
		map.put("indexNewestList", listMAP);
	}
	
	/**
	 * 全部指弹教学
	 */
	protected void writeAllFigureList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception{
		logger.debug("首页获取全部指弹教学");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(130005);
		documentCriteria.setNodePath("finger");
		documentCriteria.setOrderBy(" publish_time desc ");
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		documentCriteria.setOwnerId(ownerId);
		List<Document> indexHotSaleList= documentService.listOnPage(documentCriteria);
//		logger.debug("数量："+indexHotSaleList==null?"空":indexHotSaleList.size()+"");
		for(Document doc:indexHotSaleList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
//			Document doc = document.clone();
//			writeRelationCount(doc);
			Product pro = productService.getProductByDocument(doc);
			if(pro==null){
				continue;
			}
			hasMap.put("buy_money", pro.getBuyMoney());
			hasMap.put("document", getDocByDelDataMap(doc));
			hasMap.put("vipFree", pro.getExtraValue("vipFree"));
//			writeCommentCount2(doc, hasMap);
			try {
				User u = frontUserService.select(doc.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}
			
		map.put("thrum", listMAP);
	}
	/**
	 * 首页获取最热销
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */
	protected void writeHotSaleList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		logger.debug("首页获取最热销发布上架");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		ProductCriteria productCriteria = new ProductCriteria(ownerId);
		productCriteria.setCurrentStatus(BasicStatus.normal.id);
		productCriteria.setOrderBy("(init_count-available_count) desc");
		//int count = 1;
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		/*Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(count);
		productCriteria.setPaging(paging);
		productCriteria.setOwnerId(ownerId);
		List<Product> indexHotSaleList= productService.listOnPage(productCriteria);
//		logger.debug("数量信息："+indexHotSaleList==null?"空":indexHotSaleList.size()+"");
		for(Product product:indexHotSaleList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Document doc = productService.getRefDocument(product);
			if(doc==null){
				continue;
			}
//			writeRelationCount(doc);
			hasMap.put("document", getDocByDelDataMap(doc));
			hasMap.put("buy_money", product.getBuyMoney());
			hasMap.put("vipFree", product.getExtraValue("vipFree"));
//			writeCommentCount2(doc, hasMap);
			logger.debug("循环Product");
			try {
				User u = frontUserService.select(doc.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}*/
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setNodePath("vip");
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		//int totalResults = documentService.count(documentCriteria);
		documentCriteria.setOwnerId(ownerId);
		List<Document> documentList = documentService.listOnPage(documentCriteria);
		for (Document document : documentList) {
			Map<String,Object> hasMap = new HashMap<String,Object>();
			writeRelationCount(document);
			hasMap.put("document", getDocByDelDataMap(document));
			hasMap.put("buy_money", 0);
			hasMap.put("vipFree", 0);
			try {
				User u = frontUserService.select(document.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}
		logger.debug("最终热销商品个数{}",listMAP.size());
		map.put("indexHotSaleList", listMAP);
		logger.debug("热销Map"+listMAP);
	}
	
	/**
	 * 首页获取精品推荐
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */
	protected void writeRecommendList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		logger.debug("首页获取精品推荐发布上架");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		/*ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setCurrentStatus(BasicStatus.normal.id);
		productCriteria.setOrderBy(" label_money desc ");
		productCriteria.setOwnerId(ownerId);
		int count = 1;
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(count);
		productCriteria.setPaging(paging);
		productCriteria.setOwnerId(ownerId);
		List<Product> indexRecommendList= productService.listOnPage(productCriteria);
//		logger.debug("数量："+indexRecommendList==null?"空":indexRecommendList.size()+"");
		for(Product product:indexRecommendList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Document doc = productService.getRefDocument(product);
			if(doc==null){
				continue;
			}
//			writeRelationCount(doc);
			hasMap.put("document", getDocByDelDataMap(doc));
			hasMap.put("buy_money", product.getBuyMoney());
			hasMap.put("vipFree", product.getExtraValue("vipFree"));
			try {
				User u = frontUserService.select(doc.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}*/
			
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setNodePath("onlyvip");
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		//int totalResults = documentService.count(documentCriteria);
		documentCriteria.setOwnerId(ownerId);
		List<Document> documentList = documentService.listOnPage(documentCriteria);
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		for (Document document : documentList) {
			Map<String,Object> hasMap = new HashMap<String,Object>();
			writeRelationCount(document);
			hasMap.put("document", getDocByDelDataMap(document));
			hasMap.put("buy_money", 0);
			hasMap.put("vipFree", 0);
			try {
				User u = frontUserService.select(document.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}
		map.put("indexRecommendList", listMAP);
	}
	protected void writeCrouselFigureList(HttpServletRequest request,
			HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		logger.debug("首页轮播图");
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(130005);
		documentCriteria.setDisplayTypeId(176005);
		documentCriteria.setOwnerId(ownerId);
		documentCriteria.setOrderBy(" last_modified desc ");
		int count = 1;
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(count);
		documentCriteria.setPaging(paging);
		documentCriteria.setOwnerId(ownerId);
		List<Document> indexRecommendList= documentService.listOnPage(documentCriteria);
//		logger.debug("数量："+indexRecommendList==null?"空":indexRecommendList.size()+"");
		for(Document doc:indexRecommendList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
//			Document doc = document.clone();
			writeRelationCount(doc);
			hasMap.put("document", getDocByDelDataMap(doc));
			listMAP.add(hasMap);
		}
		map.put("indexCrouselFigureList", listMAP);
	}
	protected void writeRelationCount(Document document) {
		document.setId(document.getUdid());
		userRelationService.setDynamicData(document);		
	}
	
	private void refreshFrontUser(HttpServletRequest request, HttpServletResponse response,User frontUser){
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(frontUser.getUsername());
		frontUserCriteria.setUserPassword(frontUser.getUserPassword());
		frontUserCriteria.setCurrentStatus(UserStatus.normal.id);
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.id);
		User user = certifyService.login(request, response, frontUserCriteria);
		logger.debug("刷新数据之后的："+user.getExtraValue("sina_weibo")+"#"+user.getExtraValue("userHeadPic"));
	}
	protected Map<String,String> getDocByDelDataMap(Document doc){
		logger.debug("拆开"+doc.getExtraValue("productGallery"));
		Map<String,String> map = new HashMap<String,String>();
		writePraiseCount(doc);
		writeCommentCount(doc);
		writeReadCount(doc);
		writeFavoriteCount(doc);
		map.put("productGallery", doc.getExtraValue("productGallery"));
		map.put("productSmallImage", doc.getExtraValue("productSmallImage"));
		map.put("readCount", doc.getDocumentDataMap().get("readCount").getDataValue());
		map.put("favoriteCount", doc.getDocumentDataMap().get("favoriteCount").getDataValue());
		map.put("commentCount", doc.getDocumentDataMap().get("commentCount").getDataValue());
		map.put("praiseCount", doc.getDocumentDataMap().get("praiseCount").getDataValue());
		map.put("viewUrl", doc.getViewUrl());
		//是否是轮播图
		if (doc.getDisplayTypeId() == 176005) {
			map.put("ShufflingFigure", "true");
		}else {
			map.put("ShufflingFigure", "false");
		}
		logger.debug("wocaonima"+map);
		return map;
	}
	/**
	 * 我的作品
	 * @param request
	 * @param response
	 * @param frontUser
	 * @param map
	 */
	@RequestMapping(value="/myUploadProducts")
	public String myUploadProducts(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			logger.debug("未找到登录用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return "node/user/myUploadProducts";
		}
		logger.debug("进入我的作品查询");
		DocumentCriteria dc = new DocumentCriteria();
		dc.setPublisherId((int)(frontUser.getUuid()));
		List<Document> arrayList = new ArrayList<Document>();
		try {
			arrayList = documentService.list(dc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("myUploadList", arrayList);
		logger.debug("我的作品上传总量："+(arrayList==null?0:arrayList.size()));
		return "node/user/myUploadProducts";
	}
	@RequestMapping(value="/myFavoriteList")
	public String myFavoriteList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("进入VIP订购和产品订购订单查询");
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser == null){
			logger.debug("未找到登录用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return "node/user/myFavoriteList";
		}
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
//		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setOwnerId(ownerId);
		List<UserRelation> userFavoriteList = userRelationService.list(userRelationCriteria);
		logger.debug("用户"+frontUser.getUuid()+"收藏的文章列表"+userFavoriteList);
		map.put("userFavoriteList", userFavoriteList);
		return "node/user/myFavoriteList";
	}
	
	/**
	 * 侧边栏数据和底栏数据
	 * @throws Exception 
	 */
	@RequestMapping(value="/sideBarList")
	public String writeSideBarList(HttpServletRequest request,
			HttpServletResponse response,ModelMap map,long ownerId) throws Exception{
		logger.debug("进入侧边栏数据查询");
		/**
		 * ************************用一个通用的方法把首页可能需要提供数据一次性提供*****************************
		 * 也就是把每个业务节点下面的数据各自取一部分出来展示
		 */
		NodeCriteria nodeCri = new NodeCriteria();
		nodeCri.setOwnerId(ownerId);
		nodeCri.setNodeTypeId(NodeType.business.id);
		List<Node> nodeList = nodeService.list(nodeCri);
		logger.debug("需要显示数据的栏目共有"+(nodeList==null?0:nodeList.size())+"条");
		for(Node nod : nodeList){
			int pageNum = 1;
			List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
			List<Document> docList = new ArrayList<Document>();
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Paging paging = new Paging(rowsPerPage);
			DocumentCriteria docCri = new DocumentCriteria();
			docCri.setNodePath(nod.getPath());
			paging.setCurrentPage(pageNum);
			docCri.setPaging(paging);
			docList = documentService.listOnPage(docCri);
			if(docList!=null){
				for(Document doc:docList){
					hasMap = new HashMap<String,Object>();
					if(doc!=null){
						Product product = productService.getProductByDocument(doc);
						if(product==null){
							continue;
						}
						hasMap.put("document", getDocByDelDataMap(doc));
						try {
							hasMap.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
							hasMap.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
						} catch (Exception e) {
							logger.error("找不到发布人的信息");
						}
						hasMap.put("buy_money", product.getBuyMoney());
						hasMap.put("vipFree", product.getExtraValue("vipFree"));
						listMAP.add(hasMap);
					}
				}
			}
			map.put(nod.getAlias(), listMAP);
			logger.debug("节点"+nod.getAlias()+"的listMAP"+listMAP);
		}
		return frontMessageView;
	}
	protected void writeReadCount(Document document){

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setObjectId(document.getUdid());
		int readCount = userRelationService.getRelationCount(userRelationCriteria);
		if(readCount < 1){
			readCount = 1;
		}
		document.setExtraValue("readCount", String.valueOf(readCount));

	}
	protected void writeFavoriteCount(Document document){
		//获取点赞数和收藏数
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		int favoriteCount = userRelationService.count(userRelationCriteria);
		logger.debug("针对文章[" + document.getUdid() + "]的收藏数是:" + favoriteCount);
//		map.put("favoriteCount",favoriteCount);	
		document.setExtraValue("favoriteCount", String.valueOf(favoriteCount));
	}
	protected void writeCommentCount(Document document) {
		//获取评论数
		boolean isProduct = false;
		String productCode = document.getExtraValue(DataName.productCode.toString());

		if(StringUtils.isBlank(productCode)){
			isProduct = false;
		} else {
			isProduct = true;
		}
		CommentCriteria commentCriteria = new CommentCriteria(document.getOwnerId());
		commentCriteria.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
		if(isProduct){
			logger.debug("文档[" + document.getUdid() + "]是一个产品文档，获取对应产品[" + productCode + "]的评价");
			Product product = productService.select(productCode, document.getOwnerId());
			if(product == null){
				logger.error("找不到文档[" + document.getUdid() + "]对应的产品:" + productCode);
				return;
			}
			//查看产品是否有评论
			commentCriteria.setObjectType(ObjectType.product.name());
			commentCriteria.setObjectId(product.getProductId());

		} else {
			//查看文章是否有评论
			commentCriteria.setObjectType(ObjectType.document.name());
			commentCriteria.setObjectId(document.getUdid());

		}

		int commentCount = commentService.count(commentCriteria);
		logger.debug("针对对象[" + commentCriteria.getObjectType() + "#" + commentCriteria.getObjectId() + "]的评论数是:" + commentCount);
		document.setExtraValue("commentCount", String.valueOf(commentCount));
	}
	protected void writePraiseCount(Document document){
		//获取点赞数和收藏数
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_PRAISE);
		int praiseCount = userRelationService.count(userRelationCriteria);
		logger.debug("针对文章[" + document.getUdid() + "]的点赞数是:" + praiseCount);
		document.setExtraValue("praiseCount", String.valueOf(praiseCount));
	}
	/**
	 * 	站内信息
	 *  @author xiangqiaogao
	 *  @date 2017年12月8日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/bbsInformation")
	public String bbsInformation(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser!=null){
			map.put("frontUser", frontUser);
		}
		return "/node/user/bbsInformation";
	}
	/**
	 * 	修改资料
	 *  @author xiangqiaogao
	 *  @date 2017年12月8日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/modifyPersonalInfo")
	public String modifyPersonalInfo(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		getCurrentFrontUser(request, response, map);
		return "/node/user/modifyPersonalInfo";
	}
	/**
	 * 必要时候给用户数据
	 * @param request
	 * @param response
	 * @param map
	 */
	private void getCurrentFrontUser(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return ;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser!=null){
			map.put("frontUser", frontUser);
		}
	}
}



package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.money.criteria.PointExchangeCriteria;
import com.maicard.money.criteria.PointExchangeLogCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.PointExchange;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PointExchangeLogService;
import com.maicard.money.service.PointExchangeService;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/pointExchange")
public class PointExchangeController extends BaseController{


	@Resource
	private CertifyService certifyService;

	@Resource
	private ConfigService configService;

	@Resource
	private ProductService productService;
	@Resource
	private PointExchangeService pointExchangeService;
	@Resource
	private FrontUserService frontUserService;

	@Resource
	private MoneyService moneyService;
	@Resource
	private PointExchangeLogService pointExchangeLogService;

	private final String viewPrefix = "pointExchange";

	private int rowsPerPage = 10;



	private ObjectMapper om = new ObjectMapper();
	private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	@PostConstruct
	public void init(){
		om.setDateFormat(sdf);
		rowsPerPage = configService.getIntValue(DataName.frontRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE; 
		}
	}

	//获取积分兑换列表
	@RequestMapping(value="/list",method=RequestMethod.GET )
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
			return CommonStandard.frontMessageView;
		}
		return _list(HttpUtils.getRequestDataMap(request), frontUser, map);
	}

	@RequestMapping(value="/listCrypt", method=RequestMethod.POST)
	public String listCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) {

		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常"));
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
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "请先登录"));
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
		
		//
		
		final String view =  viewPrefix + "/list";
		int rows = HttpUtils.getIntValueFromRequestMap(requestDataMap, "rows", rowsPerPage);
		int page = HttpUtils.getIntValueFromRequestMap(requestDataMap, "page", 1);
		PointExchangeLogCriteria withdrawCriteria = new PointExchangeLogCriteria();
		withdrawCriteria.setUuid(frontUser.getUuid());
		
		//先不放入分页，查询总条数
		int totalRows = pointExchangeLogService.count(withdrawCriteria);
		//如果总条数是0，不再进行查询
		if(totalRows < 1){
			logger.debug("尝试查询的数据返回总数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);

		withdrawCriteria.setPaging(paging);
		List<PointExchange> pointExchangeLogList = pointExchangeLogService.listOnPage(withdrawCriteria);

		if(pointExchangeLogList != null && pointExchangeLogList.size() > 0){
			List<PointExchange> pointExchangeLogList2 = new ArrayList<PointExchange>();
			for(PointExchange pointExchangeLog : pointExchangeLogList){
				PointExchange p2 = pointExchangeLog.clone();
				Product product = productService.select((int)p2.getObjectId());
				if(product != null){
					p2.setProcessClass(product.getProductName());
				}
				pointExchangeLogList2.add(p2);
			}
			//放入ContengPaging用来分页
			map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));

			map.put("pointExchangeLogList", pointExchangeLogList2);
		}
		
		return view;	

	}


	//提交一次兑换
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行提现"));
			return CommonStandard.frontMessageView;
		}
		return _createExchange(HttpUtils.getRequestDataMap(request), frontUser, map);

	}
	//加密形式提交一次兑换
	@RequestMapping(value="/createCrypt", method=RequestMethod.POST)
	public String createCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
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
		String view =  _createExchange(HttpUtils.getRequestDataMap(clearData), frontUser, map);
		if(map.get("message") != null){
			EisMessage msg = (EisMessage)map.get("message");
			map.remove("message");
			try{
				String cryptedResult = crypt.aesEncrypt(om.writeValueAsString(msg));
				logger.info("对message进行加密:" + om.writeValueAsString(msg) + ",结果:" + cryptedResult);
				map.put("message", cryptedResult);
			}catch(Exception e){
				e.printStackTrace();
				map.put("message", new EisMessage(EisError.dataError.getId(), "系统异常"));
				return CommonStandard.frontMessageView;
			}
		}
		return view;

	}

	private String _createExchange(Map<String, String> requestDataMap, User frontUser,	ModelMap map){

		String view = viewPrefix + "/result";
		String objectType = null;
		String objectCode = null;
		long objectId = 0;

		if(requestDataMap.get("objectType") != null){
			objectType = requestDataMap.get("objectType");
		}
		if(objectType == null){
			logger.warn("积分兑换请求中未指定兑换类型，使用产品product作为兑换类型");
			objectType = ObjectType.product.name();
		}

		if(StringUtils.isBlank(requestDataMap.get("objectCode")) && ! StringUtils.isNumeric(requestDataMap.get("objectId"))){
			logger.error("积分兑换请求中未指定兑换的产品objectCode或objectId");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "未指定要兑换的产品"));
			return view;
		}
		if(!StringUtils.isBlank(requestDataMap.get("objectCode"))){
			objectCode = requestDataMap.get("objectCode");
		} else {
			if(StringUtils.isNumeric(requestDataMap.get("objectId"))){
				objectId = Long.parseLong(requestDataMap.get("objectId"));
			}
		}



		PointExchange pointExchange = null;

		if(objectType.equalsIgnoreCase(ObjectType.product.name())){
			if(objectCode != null){
				Product product = productService.select(objectCode, frontUser.getOwnerId());
				if(product == null){
					logger.error("找不到请求兑换的产品[" + objectCode + "]");
					map.put("message", new EisMessage(EisError.productNotExist.getId(), "找不到请求兑换的产品[" + objectCode + "]"));
					return view;
				}
				if(product.getCurrentStatus() != BasicStatus.normal.getId()){
					logger.error("请求兑换的产品[" + objectCode + "]状态异常:" + product.getCurrentStatus());
					map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "找不到请求兑换的产品[" + objectCode + "]"));
					return view;
				}
				objectId = product.getProductId();
			} else if(objectId > 0){
				Product product = productService.select((int)objectId);
				if(product == null){
					logger.error("找不到请求兑换的产品[" + objectId + "]");
					map.put("message", new EisMessage(EisError.productNotExist.getId(), "找不到请求兑换的产品[" + objectId + "]"));
					return view;
				}
				if(product.getCurrentStatus() != BasicStatus.normal.getId()){
					logger.error("请求兑换的产品[" + objectId + "]状态异常:" + product.getCurrentStatus());
					map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "找不到请求兑换的产品[" + objectCode + "]"));
					return view;
				}
				if(product.getAvailableCount() == 0 ){
					logger.error("请求兑换的产品[" + objectId + "]数量为0");
					map.put("message", new EisMessage(EisError.stockEmpty.getId(), "产品[" + objectCode + "]已售罄"));
					return view;
				}
			} else {
				logger.error("积分兑换请求中未指定兑换的产品objectCode或objectId");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "未指定要兑换的产品"));
				return view;
			}
			PointExchangeCriteria pointExchangeCriteria = new PointExchangeCriteria();
			pointExchangeCriteria.setObjectType(ObjectType.product.name());
			pointExchangeCriteria.setObjectId(objectId);
			pointExchangeCriteria.setCurrentStatus(BasicStatus.normal.getId());
			List<PointExchange> pointExchangeList = pointExchangeService.list(pointExchangeCriteria);
			if(pointExchangeList == null || pointExchangeList.size() < 1){
				logger.warn("找不到产品ID=" + objectId + "的积分兑换规则");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "未找到对应的规则"));
				return view;
			}
			pointExchange = pointExchangeList.get(0);
			logger.info("经计算，产品[" + objectId + "]的积分兑换规则是[" + pointExchange + "]");

		} else {
			logger.warn("暂不支持的兑换类型:" + objectType);
			map.put("message", new EisMessage(EisError.unSupportedProduct.getId(), "暂不支持的兑换类型:" + objectType));
			return view;	
		}
		Money money = moneyService.select(frontUser.getUuid(), frontUser.getOwnerId());
		if(money == null){
			logger.error("找不到用户[" + frontUser.getUuid() + "]的资金账户，无法继续兑换");
			map.put("message", new EisMessage(EisError.moneyAccountNotExist.getId(), "未找到用户的资金账户"));
			return view;		
		}
		if(money.getChargeMoney() < pointExchange.getMoney()){
			logger.info("产品[" + objectId + "]兑换需要" + pointExchange.getMoney() + "的money，但用户[" + frontUser.getUuid() + "]充值资金只有" + money.getChargeMoney());
			map.put("message", new EisMessage(EisError.moneyNotEnough.getId(), "用户的资金不足"));
			return view;		
		}
		if(money.getCoin() < pointExchange.getCoin()){
			logger.info("产品[" + objectId + "]兑换需要" + pointExchange.getCoin() + "的coin，但用户[" + frontUser.getUuid() + "]的coin只有" + money.getCoin());
			map.put("message", new EisMessage(EisError.moneyNotEnough.getId(), "用户的资金不足"));
			return view;		
		}
		if(money.getPoint() < pointExchange.getPoint()){
			logger.info("产品[" + objectId + "]兑换需要" + pointExchange.getPoint() + "的point，但用户[" + frontUser.getUuid() + "]point只有" + money.getPoint());
			map.put("message", new EisMessage(EisError.moneyNotEnough.getId(), "用户的资金不足"));
			return view;		
		}
		if(money.getScore() < pointExchange.getScore()){
			logger.info("产品[" + objectId + "]兑换需要" + pointExchange.getPoint() + "的score，但用户[" + frontUser.getUuid() + "]score只有" + money.getPoint());
			map.put("message", new EisMessage(EisError.moneyNotEnough.getId(), "用户的资金不足"));
			return view;		
		}

		pointExchange.setUuid(frontUser.getUuid());
		int rs = pointExchangeService.begin(pointExchange);
		if(rs == OperateResult.accept.getId()){
			map.put("message", new EisMessage(rs, "兑换已接受，请稍后查看"));
		} else if(rs == OperateResult.success.getId()){
			map.put("message", new EisMessage(rs, "兑换已成功，请稍后查看"));
		} else {
			map.put("message", new EisMessage(rs, "兑换失败"));
		}
		return view;	
	}




}

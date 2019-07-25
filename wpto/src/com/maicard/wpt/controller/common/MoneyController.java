package com.maicard.wpt.controller.common;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.money.criteria.MoneyExchangeRuleCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.MoneyExchangeRule;
import com.maicard.money.service.MoneyExchangeRuleService;
import com.maicard.money.service.MoneyService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户资金数据
 * 
 * 
 * @author NetSnake
 *
 */
@Controller
@RequestMapping("/money")
public class MoneyController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private MoneyService moneyService;
	
	@Resource
	private MoneyExchangeRuleService moneyExchangeRuleService;

	private static ObjectMapper om = new ObjectMapper();
	private static SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){
		om.setDateFormat(sdf);

	}




	//查询用户资金变动
	@RequestMapping
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));			
			return CommonStandard.frontMessageView;

		}
		Map<String,String> requestDataMap = HttpUtils.getRequestDataMap(request);
		requestDataMap.put("userAgent", request.getHeader("User-Agent"));
		return _list(requestDataMap, frontUser, map);

	}

	//加密形式、无Session提交一笔查询
	@RequestMapping(value="listCrypt",method=RequestMethod.POST)
	public String postCrypt(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("uuid")long uuid,
			@RequestParam("data")String cryptedData) throws Exception {

		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "登录失败，请检查您的帐号密码是否正确"));
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

		String view = _list(HttpUtils.getRequestDataMap(clearData), frontUser, map);
		if(map.get("money") != null){
			Money money = (Money)map.get("money");

			String cryptedResult = crypt.aesEncrypt(om.writeValueAsString(money));
			logger.info("对money进行加密:" + om.writeValueAsString(money) + ",结果:" + cryptedResult);

			map.put("money", cryptedResult);
		}		
		return view;

	}

	private String _list(Map<String,String> requestDataMap, User frontUser, ModelMap map){
		Money money = moneyService.select(frontUser.getUuid(), frontUser.getOwnerId());
		if(money != null ){
			map.put("money", money);
		}
		return CommonStandard.frontMessageView;	
	}

	//各种货币之间的兑换
	@RequestMapping(value="/exchange")
	public String exchange(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));			
			return CommonStandard.frontMessageView;

		}
		Map<String,String> requestDataMap = HttpUtils.getRequestDataMap(request);
		requestDataMap.put("userAgent", request.getHeader("User-Agent"));
		return _exchange(requestDataMap, frontUser, map);

	}
	
	private String _exchange(Map<String,String> requestDataMap, User frontUser, ModelMap map){
		final String view = "money/exchange";
		MoneyExchangeRuleCriteria moneyExchangeRuleCriteria = new MoneyExchangeRuleCriteria();
		ClassUtils.bindBeanFromMap(moneyExchangeRuleCriteria, requestDataMap);
		moneyExchangeRuleCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<MoneyExchangeRule> moneyExchangeRuleList = moneyExchangeRuleService.list(moneyExchangeRuleCriteria);
		if(moneyExchangeRuleList == null || moneyExchangeRuleList.size() < 1){
			logger.info("找不到指定的兑换规则[" + moneyExchangeRuleCriteria);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您提交的兑换无法兑换"));			
			return view;
		}
		MoneyExchangeRule moneyExchangeRule = moneyExchangeRuleList.get(0);
		moneyExchangeRule.setUuid(frontUser.getUuid());
		if(!StringUtils.isNumeric(requestDataMap.get("amount"))){
			logger.error("未提交要兑换的数量");
			map.put("message", new EisMessage(EisError.amountError.getId(), "未提交要兑换的数量"));			
			return view;
		}
		moneyExchangeRule.setAmount(Float.parseFloat(requestDataMap.get("amount")));
		map.put("message", moneyExchangeRuleService.exchange(moneyExchangeRule));
		return view;
	}


}

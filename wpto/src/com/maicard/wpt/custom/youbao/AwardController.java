package com.maicard.wpt.custom.youbao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.money.criteria.AwardCriteria;
import com.maicard.money.domain.Award;
import com.maicard.money.service.AwardService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户中奖记录数据
 * 
 * 
 * @author NetSnake
 * 
 * @date 2016-04-29
 *
 */
@Controller
@RequestMapping("/award")
public class AwardController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private AwardService awardService;
	@Resource
	private ConfigService configService;

	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}




	//查询用户中奖记录
	@RequestMapping
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
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
		Map<String,String> requestDataMap = HttpUtils.getRequestDataMap(request);
		requestDataMap.put("userAgent", request.getHeader("User-Agent"));
		return _list(requestDataMap, frontUser, map);

	}

	//加密形式、无Session提交查询
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

		return view;

	}

	private String _list(Map<String,String> requestDataMap, User frontUser, ModelMap map){
		
		int rows = HttpUtils.getIntValueFromRequestMap(requestDataMap, "rows", rowsPerPage);
		int page = HttpUtils.getIntValueFromRequestMap(requestDataMap, "page", 1);
		
		AwardCriteria awardCriteria = new AwardCriteria();
		ClassUtils.bindBeanFromMap(awardCriteria, requestDataMap);
		awardCriteria.setOwnerId(frontUser.getOwnerId());
		awardCriteria.setUuid(frontUser.getUuid());
		int totalRows = awardService.count(awardCriteria);
		if(totalRows < 1){
			logger.debug("尝试查询的数据返回总数是0");
			return CommonStandard.frontMessageView;
		}
		
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		awardCriteria.setPaging(paging);
		
		List<Award> awardList = awardService.list(awardCriteria);
		if (awardList == null || awardList.size() < 1) {
			map.put("message", "中奖记录为空");
			return CommonStandard.frontMessageView;
		}
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));
		map.put("awardList", awardList);
		return CommonStandard.frontMessageView;	
	}
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String awardList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
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

		AwardCriteria awardCriteria = new AwardCriteria();
		awardCriteria.setOwnerId(frontUser.getOwnerId());
		awardCriteria.setUuid(frontUser.getUuid());
		List<Award> awardLists = awardService.list(awardCriteria);
		if (awardLists == null || awardLists.size() < 1) {
			map.put("message", "中奖记录为空");
			return CommonStandard.frontMessageView;
		}
		int totalRows = awardLists.size();
		map.put("totalRows", totalRows);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		map.put("totalPage", (totalRows % rows > 0 ? (totalRows / rows + 1) : (totalRows / rows)) );
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		awardCriteria.setPaging(paging);
		List<Award> awardListOnPage = awardService.listOnPage(awardCriteria);
		if (awardListOnPage == null || awardListOnPage.size() < 0) {
			map.put("awardList", new ArrayList<Award>());
		} else {
			map.put("awardList", awardListOnPage);
		}

		return CommonStandard.frontMessageView;	
	}


}

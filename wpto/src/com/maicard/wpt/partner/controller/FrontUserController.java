package com.maicard.wpt.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.SecurityStandard.UserTypes;


@Controller
@RequestMapping("/frontUser")
public class FrontUserController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private PartnerService partnerService;

	private int rowsPerPage = 10;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	//列出当前合作伙伴及其子账户下的注册用户
	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			UserCriteria frontUserCriteria) throws Exception {
		final String view = "common/frontUser/list";
		
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		User selfPartner  = certifyService.getLoginedUser(request,response, UserTypes.partner.getId());
		if(selfPartner == null ){
			throw new UserNotFoundInRequestException();
		}
		frontUserCriteria.setOwnerId(selfPartner.getOwnerId());
		if (frontUserCriteria.getUsername()!=null && frontUserCriteria.getUsername().equals("")){
			frontUserCriteria.setUsername(null);
		}	
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(selfPartner);
		logger.debug("当前合作伙伴[" + selfPartner.getUuid() + "/" + selfPartner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(frontUserCriteria, selfPartner);
		}
		map.put("isPlatformGenericPartner",isPlatformGenericPartner);
		
		

		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		Paging paging = new Paging(rows);
		frontUserCriteria.setPaging(paging);
		frontUserCriteria.getPaging().setCurrentPage(page);
		frontUserCriteria.setOrderBy("create_time");
	//	frontUserCriteria.setParentUuid(currentUuid);
		/*List<User> allChildPartner = new ArrayList<User>();
		allChildPartner.add(selfPartner);			
		partnerService.listAllChildren(allChildPartner, currentUuid);*/
//		frontUserCriteria.setSubUserList(allChildPartner);
		
		if(frontUserCriteria.getSearchCondition() != null && !frontUserCriteria.getSearchCondition().equals("")){
			int searchUuid = 0;
			try{
				searchUuid = Integer.parseInt(frontUserCriteria.getSearchCondition());
				if(searchUuid > 0){
					frontUserCriteria.setUuid(searchUuid);
				}
			}catch(Exception e){}
			if(searchUuid < 1){
				frontUserCriteria.setLikeUserName(frontUserCriteria.getSearchCondition().trim());
			}
		}
		int totalRows = frontUserService.count(frontUserCriteria);
		map.put("total", totalRows);
		map.put("title","终端用户列表");
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(frontUserCriteria.getCreateTimeBegin() != null){
			logger.debug("查询的注册开始时间:" + sdf.format(frontUserCriteria.getCreateTimeBegin()));
		}
		if(frontUserCriteria.getCreateTimeEnd() != null){
			logger.debug("查询的注册结束时间:" + sdf.format(frontUserCriteria.getCreateTimeEnd()));
		}
		if(totalRows < 1){
			logger.debug("当前查询的结果数量是0");
			return view;
		}
		List<User> frontUserList = frontUserService.listOnPage(frontUserCriteria);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");

		for(User frontUser:frontUserList){
			frontUser.setOperate(new HashMap<String,String>());
			frontUser.getOperate().put("get", "./frontUser/get/"+ frontUser.getUuid());
			frontUser.getOperate().put("del", "./frontUser/delete");
		}
		ArrayList<User> frontUserList2 = new ArrayList<User>();
		for (User user : frontUserList) {
			User userClone = user.clone();
			User partner = partnerService.select(userClone.getInviter());
			if (partner != null) {
				userClone.setExtraValue("channel", partner.getNickName());
			}
			frontUserList2.add(userClone);
		}
		
		map.put("rows",frontUserList2);
		return view;

	}



	@RequestMapping(value="/get" + "/{uuid}")		
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("frontUserCriteria") UserCriteria frontUserCriteria,
			@PathVariable("uuid") Integer uuid) throws Exception {

		if(uuid == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[uuid]");
		}
		User frontUser = frontUserService.select(uuid);
		if(frontUser == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + uuid + "的frontUser对象");			
		}
		map.put("frontUser", frontUser);
		map.put("frontUserCriteria", frontUserCriteria);
		return "common/frontUser/get";
	}
	
	/**
	 * 清除一个用户的所有数据
	 * 包括用户帐号、扩展数据、缓存、唯一性数据和缓存
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)		
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		final String view = CommonStandard.partnerMessageView;
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		
		String username = ServletRequestUtils.getStringParameter(request, "username",null);
		if(StringUtils.isBlank(username)){
			logger.error("删除用户需要至少有UUID或用户名");
			map.put("message", "请提交要删除的用户名");
			return view;
		}
		UserCriteria frontUserCriteria = new UserCriteria(ownerId);
		frontUserCriteria.setUsername(username);
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		List<User> userList = frontUserService.list(frontUserCriteria);
		if(userList == null || userList.size() < 1){
			logger.warn("根据条件[username=" + username + "]找不到要删除的前端用户");

			map.put("message", "找不到要删除的用户:" + username);
			return view;		
		}
		User frontUser = userList.get(0);
		
		
	
		map.put("message", frontUserService.delete(frontUser));
		return view;
	}
}

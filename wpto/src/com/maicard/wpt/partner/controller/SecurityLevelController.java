package com.maicard.wpt.partner.controller;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.ConfigCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.SecurityLevelService;
import com.maicard.common.util.SecurityLevelUtils;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.PasswordStrongGrade;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 对
 *
 *
 * @author NetSnake
 * @date 2015年12月29日
 *
 */
@Controller
@RequestMapping("/securityLevel")
public class SecurityLevelController extends BaseController{

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;

	@Resource
	private SecurityLevelService securityLevelService;
	
	int securityLevelId = SecurityLevelUtils.getSecurityLevel();
	private SecurityLevel securityLevel;
		
	@PostConstruct
	public void init(){
		securityLevel = securityLevelService.select(securityLevelId);
		
	}
	

	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ConfigCriteria configCriteria) throws Exception {
		final String view = "common/securityLevel/list";
		final String title = "安全配置";
		map.put("title", title);
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(),"您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。"));
			return CommonStandard.partnerMessageView;
		}
		
		

		if(securityLevel == null){
			logger.debug("找不到当前安全级别[" + securityLevel + "]的配置");
			return view;
		}
		map.put("securityLevel",securityLevel);
		map.put("passwordStrongGrade", PasswordStrongGrade.values());

		return view;
	}


}

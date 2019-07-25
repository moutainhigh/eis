package com.maicard.security.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.maicard.common.domain.EisMessage;
import com.maicard.exception.EisException;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;

/**
 * 认证服务，确认与保持用户身份
 * @author NetSnake
 * @date 2012-12-30
 */
public interface CertifyService {

	
	//通过请求指定类型的当前用户
	User getLoginedUser(HttpServletRequest request, HttpServletResponse response,  int userTypeId) throws EisException;
	
	//生成用户令牌
	void generateUserToken(User user);
	
	//通过令牌获取用户
	User getUserByToken(String cryptedToken);
	
	//用户登录
	User login(HttpServletRequest request, HttpServletResponse response, UserCriteria userCriteria);
	
	//直接登录，不查询数据库
	User login(HttpServletRequest request, HttpServletResponse response, User user);


	//退出登录
	void logout(HttpServletRequest request, HttpServletResponse response,
			User frontUser);

	//以authKey强制登陆
	User forceLogin(HttpServletRequest request, HttpServletResponse response, String authKey);


	void getRemeberMeStatus(HttpServletRequest request, HttpServletResponse response, ModelMap map);

	void setRememberMe(HttpServletRequest request, HttpServletResponse response, ModelMap map);

	int checkSecAuth(HttpServletRequest request, HttpServletResponse response, User partner, String userPassword);

	boolean isSecAuthed(HttpServletRequest request, HttpServletResponse response, User partner);

	boolean passwordNeedChange(User partner);

	EisMessage passwordIsFine(User partner, String password);

	/**
	 * 通过request中的token参数来确定该用户是否合法<br/>
	 * 主要用于外部合作的SSO登录
	 * @param request
	 * @param userTypeId
	 * @return
	 */
	User getUserByRequestToken(HttpServletRequest request, int userTypeId);

	String generateSsoToken(User user, long timestamp);
	
	/**
	 * 以重定向方式把用户转发到前往的URL
	 * 并把登录后返回URL写入Cookie
	 */

	String redirectByResponse(HttpServletRequest request, HttpServletResponse response, int userTypeId,
			String toUrl, String backUrl, String cookieDomain);

	int strictAuthorize(HttpServletRequest request, HttpServletResponse response, User partner, String userPassword);

//	void redirectByResponse(HttpServletRequest request, HttpServletResponse response, ModelMap map, boolean noJumpWhenIsJson, int userTypeId,			String weixinRegisterUrl, String backUrl, String cookieDomain);



}

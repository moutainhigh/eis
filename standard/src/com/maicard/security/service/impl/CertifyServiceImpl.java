package com.maicard.security.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.SecurityLevelService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.SecurityLevelUtils;
import com.maicard.common.util.UserUtils;
import com.maicard.exception.EisException;
import com.maicard.security.criteria.PasswordLogCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.PasswordLog;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.service.PasswordLogService;
import com.maicard.security.service.UserDynamicDataService;
import com.maicard.security.service.UserOnlineLogService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PasswordStrongGrade;
import com.maicard.standard.SecurityStandard.UserExtraStatus;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import static com.maicard.standard.CommonStandard.COOKIE_REDIRECT_COOKIE_NAME;

@Service
public class CertifyServiceImpl extends BaseService implements CertifyService {

	@Resource
	private CookieService cookieService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private UserDynamicDataService userDynamicDataService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PasswordLogService passwordLogService;
	@Resource
	private SecurityLevelService securityLevelService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private UserOnlineLogService userOnlineLogService;


	private int securityLevelId = SecurityLevelUtils.getSecurityLevel();
	private SecurityLevel securityLevel;
	private int sessionTimeout = 0;
	private String aesKey = null;
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	private final Pattern capitalCharPattern = Pattern.compile("[A-Z]");
	private final Pattern smallCharPattern = Pattern.compile("[a-z]");
	private final Pattern digitalPattern = Pattern.compile("\\d");
	//Unicode标点符号集合
	private final Pattern symbolCharPattern = Pattern.compile("\\pP");



	@PostConstruct
	public void init(){
		securityLevel = securityLevelService.select(securityLevelId);
		if(securityLevel != null && securityLevel.getData() != null && NumericUtils.isNumeric(securityLevel.getData().get(DataName.sessionTimeout.toString()))){
			sessionTimeout = NumericUtils.getNumeric(securityLevel.getData().get(DataName.sessionTimeout.toString()));	
			logger.debug("当前安全级别为[" + securityLevelId + "]配置的sessionTimeout数据:" + sessionTimeout);
		} 
		if(sessionTimeout < 1){
			sessionTimeout = CommonStandard.sessionDefaultTtl;
			logger.debug("当前安全级别为空或者没有配置sessionTimeout数据，配置为系统默认TTL:" + sessionTimeout);
		}
		try {
			aesKey = CryptKeyUtils.readAesKey();
		} catch (Exception e) {
			logger.error("无法读取系统AES密钥");
		}
	}



	/*
	 * 根据Cookie检查用户是否设置了记住我的用户名
	 * 如果记住，则从cookie中取出用户名并放入map供前端调用
	 */
	@Override
	public void getRemeberMeStatus(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		if(response == null){
			logger.error("传入的response为空，放弃处理");
			return;
		}
		String remember =	cookieService.getCookie(request, DataName.rememberMe.toString());
		String rememberName = cookieService.getCookie(request, DataName.userRememberName.toString());
		boolean rememberMe = false;
		if(remember != null && remember.equalsIgnoreCase("true")){
			rememberMe = true;
		}

		map.put(DataName.rememberMe.toString(), rememberMe);
		if(rememberMe){
			logger.debug("用户要求记住用户名，把Cookie中的用户名[" + rememberName + "]放入map");
			map.put(DataName.userRememberName.toString(), rememberName);
		} else {
			logger.debug("用户Cookie中没有要求记住用户名的配置");
		}

		/*if( StringUtils.isBlank(rememberName)){
			cookieService.removeCookie(request, response, DataName.userRememberName.toString());
			return;
		}*/
		//Crypt crypt = new Crypt();
		//crypt.setAesKey(CommonStandard.cookieAesKey);
		//String cryptUser = crypt.aesEncrypt(rememberName + "|" + System.currentTimeMillis());
		//String cryptUser = rememberName;
		//cookieService.addCookie(request, response, DataName.userRememberName.toString(), cryptUser, CommonStandard.COOKIE_MAX_TTL);
	}

	/*
	 * 把用户是否要求记住的用户名的要求放到Cookie中
	 * 如果不要求记住用户名，则删除Cookie中已经记忆的用户名
	 */
	@Override
	public void setRememberMe(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		if(response == null){
			logger.error("传入的response为空，放弃处理");
			return;
		}
		String remember =	request.getParameter(DataName.rememberMe.toString());
		String rememberName = request.getParameter("username");
		boolean rememberMe = false;
		if(remember != null && remember.equalsIgnoreCase("true")){
			rememberMe = true;
		}
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		cookieService.addCookie(request, response, DataName.rememberMe.toString(), remember,CommonStandard.COOKIE_MAX_TTL, siteDomainRelation.getCookieDomain());		
		if(rememberMe){
			logger.debug("用户要求记住用户名，把用户名[" + rememberName + "]写入Cookie");
			cookieService.addCookie(request, response, DataName.userRememberName.toString(), rememberName,CommonStandard.COOKIE_MAX_TTL,siteDomainRelation.getCookieDomain());
			//map.put(DataName.userRememberName.toString(), rememberName);
		} else {
			logger.debug("用户要求不记住用户名，把用户名[" + rememberName + "]从Cookie中删除");
			cookieService.removeCookie(request, response, DataName.userRememberName.toString(),siteDomainRelation.getCookieDomain());			
		}
	}


	@Override
	public User getLoginedUser(HttpServletRequest request,  HttpServletResponse response, int userTypeId) throws EisException{

		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + request.getServerName() + "]找不到指定的站点关系");
			return null;
		}

		User user = null;
		/*
		if(userTypeId == UserTypes.sysUser.getId()){
			Object o = request.getSession().getAttribute("sysUser");
			if(o != null && o instanceof User){
				user = (User)o;
				logger.debug("从Session中获取到了sysUser实例");
			} else {
				user = getUserByToken(cookieService.getCookie(request, CommonStandard.sessionTokenName + "_s"));
			}
			if(user != null){

				if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
					if(siteDomainRelation.getCurrentStatus() == BasicStatus.relation.getId()){
						logger.error("系统用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致，但该主机名是总后台，系统用户仍然允许登录");
					} else {
						logger.error("系统用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
						return null;
					}
				}
				Date lastLoginTimeStamp = user.getLastLoginTimestamp();
				if(lastLoginTimeStamp == null || new Date().getTime() - lastLoginTimeStamp.getTime() > CommonStandard.TOKEN_RENEW_INTERVAL * 1000){
					logger.debug("当前用户最后一次更新时间是:" + (lastLoginTimeStamp == null ? "空" : sdf.format(lastLoginTimeStamp)) + ",已超过" + CommonStandard.TOKEN_RENEW_INTERVAL + "秒，应当更新");

					generateUserToken(user);
					user.setLastLoginIp(IpUtils.getClientIp(request));
					userDynamicDataService.update(userDynamicDataService.generateFromUser(user));
					if(response != null){
						cookieService.addCookie(request, response, CommonStandard.sessionTokenName + "_s", user.getSsoToken(), sessionTimeout, siteDomainRelation.getCookieDomain());
					}
				}
				return user;
			}
		} else */	if(userTypeId == UserTypes.partner.getId()){
			Object o = request.getSession().getAttribute("partner");
			if(o != null && o instanceof User){
				user = (User)o;
				logger.debug("从Session中获取到了partner实例");
			} else {
				user = getUserByToken(cookieService.getCookie(request, CommonStandard.sessionTokenName + "_p"));	
			}
			if(user == null){
				user = getUserByRequestToken(request, UserTypes.partner.getId());
			}
			if(user != null){
				if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
					logger.error("合作伙伴用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
					return null;
				}
				Date lastLoginTimeStamp = user.getLastLoginTimestamp();
				if(lastLoginTimeStamp == null || new Date().getTime() - lastLoginTimeStamp.getTime() > CommonStandard.TOKEN_RENEW_INTERVAL * 1000){
					logger.debug("当前用户最后一次更新时间是:" + (lastLoginTimeStamp == null ? "空" : sdf.format(lastLoginTimeStamp)) + ",已超过" + CommonStandard.TOKEN_RENEW_INTERVAL + "秒，应当更新");

					generateUserToken(user);
					user.setLastLoginIp(IpUtils.getClientIp(request));
					userDynamicDataService.update(userDynamicDataService.generateFromUser(user));
					if(response != null){
						cookieService.addCookie(request, response, CommonStandard.sessionTokenName + "_p", user.getSsoToken(),sessionTimeout, siteDomainRelation.getCookieDomain());
					}
				}
			}
		} else {
			Object o = request.getSession().getAttribute("frontUser");			
			if(o != null && o instanceof User){
				user = (User)o;
				logger.debug("从Session中获取到了frontUser实例");
			} else {
				user = getUserByToken(cookieService.getCookie(request, CommonStandard.sessionTokenName + "_f"));
			}
			if(user != null){
				if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
					logger.error("前端用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
					return null;
				}
				user.setUserTypeId(UserTypes.frontUser.getId());

				Date lastLoginTimeStamp = user.getLastLoginTimestamp();
				if(lastLoginTimeStamp == null || new Date().getTime() - lastLoginTimeStamp.getTime() > CommonStandard.TOKEN_RENEW_INTERVAL * 1000){
					logger.debug("当前用户最后一次更新时间是:" + (lastLoginTimeStamp == null ? "空" : sdf.format(lastLoginTimeStamp)) + ",已超过" + CommonStandard.TOKEN_RENEW_INTERVAL + "秒，应当更新");

					generateUserToken(user);
					user.setLastLoginIp(IpUtils.getClientIp(request));
					userDynamicDataService.update(userDynamicDataService.generateFromUser(user));
					if(response != null){
						logger.debug("从cookie中得到前端用户后，再次向cookie中写入passport");
						cookieService.addCookie(request, response, CommonStandard.sessionTokenName + "_f", user.getSsoToken(),sessionTimeout,siteDomainRelation.getCookieDomain());
						String uesrCookieName = (StringUtils.isBlank(user.getNickName()) ? user.getUsername() : user.getNickName());
						try{
							uesrCookieName = java.net.URLEncoder.encode(uesrCookieName,"UTF-8");
						}catch(Exception e){
							e.printStackTrace();
						}
						cookieService.addCookie(request, response, CommonStandard.sessionUsername, uesrCookieName, sessionTimeout,siteDomainRelation.getCookieDomain(), false);
					}
				}
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("从Cookie令牌中未找到前台用户");
				}
				//微信Cookie经常出现异常，如果没有找到登录用户，以第二种方式检查
				if(request.getAttribute("openId") != null){
					String openId = request.getAttribute("openId").toString();
					user = forceLogin(request,response,openId);
					if(logger.isDebugEnabled()){
						logger.debug("从请求Attributes中找到了openId:" + openId + "，以该openid登录，登录结果:" + user);
					}
				} else {
					if(logger.isDebugEnabled()){
						logger.debug("从请求Attributes也未找到openId");
					}
				}
			}
		}	
		
		if(user == null || user.getCurrentStatus() != UserStatus.normal.getId()){
			return null;

		}
		if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
			logger.error("用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与系统的[" + siteDomainRelation.getOwnerId() + "]不匹配");
			throw new EisException(EisError.ownerNotMatch.getId(), "系统异常");		
		}
		return user;
	}




	//生成SSO Token
	@Override
	public void generateUserToken(User user){
		if(user == null){
			logger.error("尝试生成加密令牌但用户是空");
			return;
		}
		user.setLastLoginTimestamp(new Date());

		//生成由 用户类型ID|UUID|最后登录IP|用户名|登录时间戳 组成的Base64编码后的源
		String src = user.getUserTypeId() + "|" + user.getUuid() + "|" +  user.getLastLoginIp() + "|" + user.getUsername() + "|" + user.getLastLoginTimestamp().getTime();
		logger.debug("用户令牌加密源:" + src);
		src = Crypt.base64Encode(src);
		logger.debug("用户令牌编码后:" + src);
		//强制使用加密后的密码进行校验, NetSnake, 2013-11-12
		String sign = Crypt.passwordEncode(src + UserUtils.correctPassword(user.getUserPassword()) + aesKey);
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		user.setSsoToken(crypt.aesEncrypt(src + "|" + sign));
	}

	/*
	 * 检查用户的Token是否有效
	 * 若有效则更新登录时间
	 */
	@Override
	public User getUserByToken(String cryptedToken){
		if(cryptedToken == null){
			logger.debug("加密令牌为空");
			return null;
		}		
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		//	logger.info("尝试解密TOKEN:" + cryptedToken );
		try{
			String token = crypt.aesDecrypt(cryptedToken);
			if(token == null){
				logger.error("Token解密失败:" + cryptedToken);
				return null;
			}
			logger.debug("解密后的Token:" + token);
			String[] data = token.split("\\|");
			logger.debug("token data:" + data.length);
			if(data == null || data.length != 2){
				logger.error("Token数据异常");
				return null;
			}

			String[] data2 = Crypt.base64Decode(data[0]).split("\\|");
			if(data2 == null || data2.length != 5){
				logger.error("SSO 数据异常");
				return null;
			}
			long tokenUserTypeId = Long.parseLong(data2[0]);
			long tokenUuid = Long.parseLong(data2[1]);
			//String lastLoginIp = data2[2];
			long ssoTs = Long.parseLong(data2[4]);
			if(tokenUserTypeId == 0){
				logger.error("SSO 用户类型为0");
				return null;					
			}
			if(tokenUuid == 0){
				logger.error("SSO UUID为0");
				return null;			
			}
			User user = null;
			if(tokenUserTypeId == UserTypes.frontUser.getId()){
				logger.debug("尝试查找前台用户[" + tokenUuid + "]");
				user = frontUserService.select(tokenUuid);
			} else if(tokenUserTypeId == UserTypes.partner.getId()){
				user = partnerService.select(tokenUuid);
			} else {
				logger.error("错误的用户类型:" + tokenUserTypeId);
				return null;
			}
			if(user == null){
				logger.error("根据UUID[" + tokenUuid + "]找不到类型为[" + tokenUserTypeId + "]的用户");
				return null;
			}
			logger.debug("得到用户[" + user.getUuid() + "]密码是:"+user.getUserPassword());
			String clientSign = data[1];
			String serverSign = Crypt.passwordEncode(data[0] + UserUtils.correctPassword(user.getUserPassword())  + aesKey);
			if(!clientSign.equals(serverSign)){
				logger.warn("SSO Token校验失败，用户提交的SIGN：" + clientSign + ",服务器生成的SIGN：" + data[0] + user.getUserPassword()  + aesKey + "======>" + serverSign);
				return null;
			}		
			if(ssoTs == 0){
				logger.debug("SSO 时间戳为0");
				return null;	
			}
			user.setLastLoginTimestamp(new Date(ssoTs));
			long timeout = (new Date().getTime() - ssoTs)/1000;
			logger.debug("Token中的时间戳[" + ssoTs + "]与当前时间[" + new Date().getTime() + "]相差: " + timeout + ", Token超时时间:" + sessionTimeout);
			if(timeout <= sessionTimeout){
				//尚未超时
				return user;
				//				return user.clone();
			}	

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public User getUserByRequestToken(HttpServletRequest request, int userTypeId) {
		String token = request.getParameter("ssoToken");
		long uuid =  ServletRequestUtils.getLongParameter(request, "uuid", 0);
		long timestamp = ServletRequestUtils.getLongParameter(request, "timestamp",0);
		if(uuid < 1){
			logger.error("请求中未提交用户UUID");
			return null;
		}
		if(timestamp < 1){
			logger.error("请求中未提交timestamp");
			return null;
		}
		String ttl = SecurityLevelUtils.getConfig(securityLevel, DataName.ssoTimestampTtl.toString());
		if(StringUtils.isBlank(ttl)){
			ttl = String.valueOf(CommonStandard.SSO_TIMESTAMP_DEFAULT_TTL);
		}
		if((new Date().getTime() - timestamp) / 1000 > Long.parseLong(ttl)){
			logger.error("时间戳已过期");
			return null;
		}
		if(token == null){
			logger.error("request加密令牌为空");
			return null;
		}
		User partner = partnerService.select(uuid);
		if(partner == null){
			logger.error("找不到UUID=" + uuid + "的partner");
			return null;
		}
		if(partner.getCurrentStatus() != UserStatus.normal.getId()){
			logger.error("UUID=" + uuid + "的partner状态异常:" + partner.getCurrentStatus());
			return null;
		}
		if(partner.getUserConfigMap() == null || partner.getUserConfigMap().size() < 1){
			logger.error("UUID=" + uuid + "的partner没有任何扩展数据，无法验证是否允许SSO登录");
			return null;
		}
		if(partner.getUserConfigMap().get(DataName.allowSsoLogin.toString()) == null || partner.getUserConfigMap().get(DataName.allowSsoLogin.toString()).getDataValue() == null){
			logger.error("UUID=" + uuid + "的partner没有配置allowSsoLogin扩展数据，不允许SSO登录");
			return null;
		}
		if(!partner.getUserConfigMap().get(DataName.allowSsoLogin.toString()).getDataValue().equalsIgnoreCase("true")){
			logger.error("UUID=" + uuid + "的partner的配置allowSsoLogin不是true，是:" + partner.getUserConfigMap().get(DataName.allowSsoLogin.toString()).getDataValue() + "，不允许SSO登录");
			return null;
		}
		if(partner.getUserConfigMap().get(DataName.supplierLoginKey.toString()) == null || partner.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue() == null){
			logger.error("UUID=" + uuid + "的partner的没有配置supplierLoginKey，无法使用SSO登录");
			return null;
		}
		String sign = generateSsoToken(partner, timestamp);
		if(sign.equalsIgnoreCase(token)){
			logger.debug("用户[" + uuid + "]SSO登录token[" + token + "]与本地计算["  + sign + "]一致，SSO登陆成功");
			return partner;
		} 
		logger.debug("用户[" + uuid + "]SSO登录token[" + token + "]与本地计算[" + sign + "]不一致，SSO登陆失败");	
		return null;
	}

	@Override
	public String generateSsoToken(User user, long timestamp){
		String key = user.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		StringBuffer  signSource = new StringBuffer().append(user.getUsername()).append('|').append(key).append('|').append(timestamp);
		return DigestUtils.sha256Hex(signSource.toString());
	}


	@Override
	public User login(HttpServletRequest request,
			HttpServletResponse response, UserCriteria userCriteria){
		if (userCriteria.getAuthKey()==null ||userCriteria.getAuthKey().equals("")){
			if(StringUtils.isBlank(userCriteria.getUsername())){
				logger.warn("尝试登录的用户名为空");
				return null;
			}
			if(StringUtils.isBlank(userCriteria.getUserPassword())){
				logger.warn("尝试登录的用户密码为空");
				return null;
			}
		}
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + request.getServerName() + "]找不到指定的站点关系");
			return null;
		}
		userCriteria.setOwnerId(siteDomainRelation.getOwnerId());

		String plainPassword = userCriteria.getUserPassword();
		userCriteria.setUserPassword(null);
		//userCriteria.setUserPassword(Crypt.passwordEncode(userCriteria.getUserPassword()));
		//登录必须是正常状态的用户
		userCriteria.setCurrentStatus(UserStatus.normal.getId());
		List<User> loginUserList = null;
		String sessionTokenName = CommonStandard.sessionTokenName;
		if(userCriteria.getUserTypeId() == UserTypes.frontUser.getId()){
			sessionTokenName += "_f";
			loginUserList = frontUserService.list(userCriteria);			
		} else 	if(userCriteria.getUserTypeId() == UserTypes.partner.getId()){
			sessionTokenName += "_p";
			loginUserList = partnerService.list(userCriteria);
		} else {
			logger.error("错误的用户类型:" + userCriteria.getUserTypeId());
			return null;					
		}
		if(loginUserList == null){
			logger.info("未能查找到[" + userCriteria.getUsername() + "/" + userCriteria.getUserPassword() + "]、类型为[" + userCriteria.getUserTypeId() + "]的正常状态用户");
			return null;
		}
		if(loginUserList.size() != 1){
			logger.info("查找到[" + userCriteria.getUsername() + "/" + userCriteria.getUserPassword() + "]、类型为[" + userCriteria.getUserTypeId() + "]的正常状态用户数量不唯一:" + loginUserList.size());
			return null;
		}
		User user = loginUserList.get(0);
		String encryptPassword = null;
		if(UserUtils.isLegacyPassword(user.getUserPassword())){
			//大写，是旧密码
			encryptPassword = UserUtils.correctLegacyPassword(plainPassword);
		} else {
			encryptPassword = UserUtils.correctPassword(plainPassword);
		}
		if(!encryptPassword.equals(user.getUserPassword())){
			//密码错误
			logger.warn("用户[" + userCriteria.getUsername() + "]提供的登录密码是:" + encryptPassword + ",与数据库中保存的:" + user.getUserPassword() + "不一致");
			return null;

		}



		user.setLastLoginTimestamp(new java.util.Date());
		user.setLastLoginIp(IpUtils.getClientIp(request));
		//生成客户端Token
		generateUserToken(user);
		logger.info("为用户[" + user.getUuid() + "]生成SSO Token:" + user.getSsoToken() + ",用户类型:" + user.getUserTypeId());
		cookieService.addCookie(request, response, sessionTokenName, user.getSsoToken(),sessionTimeout,siteDomainRelation.getCookieDomain());
		if(user.getUserTypeId() == UserTypes.frontUser.getId()){
			if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
				logger.error("前端用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
				return null;

			}
			String userCookieName = (StringUtils.isBlank(user.getNickName()) ? user.getUsername() : user.getNickName());
			try{
				userCookieName = java.net.URLEncoder.encode(userCookieName,"UTF-8");
			}catch(Exception e){
				e.printStackTrace();
			}
			logger.info("为用户添加用户名Cookie:" + CommonStandard.sessionUsername + "=" + userCookieName);
			cookieService.addCookie(request, response, CommonStandard.sessionUsername, userCookieName,sessionTimeout,siteDomainRelation.getCookieDomain(),false);
		}
		//updateUserRememberName(request, response, user);
		return loginUserList.get(0);

	}

	@Override
	public User forceLogin(HttpServletRequest request,
			HttpServletResponse response, String authKey){
		if(StringUtils.isBlank(authKey)){
			logger.warn("尝试登录的authKey为空");
			return null;
		}
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setCurrentStatus(UserStatus.normal.getId());
		userCriteria.setAuthKey(authKey);
		List<User> loginUserList = null;
		String sessionTokenName = CommonStandard.sessionTokenName;

		sessionTokenName += "_f";
		loginUserList = frontUserService.list(userCriteria);			

		if(loginUserList == null || loginUserList.size() < 1){
			logger.info("未能查找到authKey=" + authKey + "的正常状态前端用户");
			return null;
		}
		if(loginUserList.size() != 1){
			logger.info("查找到authKey=" + authKey + "的正常状态前端用户数量不唯一:" + loginUserList.size());
			return null;
		}
		User user = loginUserList.get(0);
		user.setLastLoginTimestamp(new java.util.Date());
		user.setLastLoginIp(IpUtils.getClientIp(request));
		//生成客户端Token
		generateUserToken(user);
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());

		logger.info("为用户[" + user.getUuid() + "]生成SSO Token:" + user.getSsoToken() + ",用户类型:" + user.getUserTypeId());
		cookieService.addCookie(request, response, sessionTokenName, user.getSsoToken(),sessionTimeout,siteDomainRelation.getCookieDomain());
		if(user.getUserTypeId() == UserTypes.frontUser.getId()){
			String userCookieName = (StringUtils.isBlank(user.getNickName()) ? user.getUsername() : user.getNickName());
			try{
				userCookieName = java.net.URLEncoder.encode(userCookieName,"UTF-8");
			}catch(Exception e){
				e.printStackTrace();
			}
			logger.info("为用户添加用户名Cookie[" + CommonStandard.sessionUsername + "=" + userCookieName + "]");
			cookieService.addCookie(request, response, CommonStandard.sessionUsername, userCookieName,sessionTimeout,siteDomainRelation.getCookieDomain(), false);
		}
		//updateUserRememberName(request, response, user);
		return loginUserList.get(0);

	}

	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response, User user) {
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(user == null){
			logger.debug("请求中未找到用户，删除所有用户令牌");
			cookieService.removeCookie(request, response,CommonStandard.sessionTokenName + "_s",siteDomainRelation.getCookieDomain());	
			cookieService.removeCookie(request, response,CommonStandard.sessionTokenName + "_p",siteDomainRelation.getCookieDomain());	
			cookieService.removeCookie(request, response,CommonStandard.sessionTokenName + "_f",siteDomainRelation.getCookieDomain());
			return;
		}
		try{
			Cookie[] cookies = request.getCookies();
			if(cookies == null || cookies.length < 1){
				return;
			}
			if(user.getUserTypeId() == UserTypes.sysUser.getId()){
				if(logger.isDebugEnabled()){
					logger.debug("尝试删除系统用户[" + user.getUsername() + "/" + user.getUuid() + "]的Cookie.");
				}
				cookieService.removeCookie(request, response,CommonStandard.sessionTokenName + "_s",siteDomainRelation.getCookieDomain());	
			} else if(user.getUserTypeId() == UserTypes.partner.getId()){
				if(logger.isDebugEnabled()){
					logger.debug("尝试删除合作伙伴[" + user.getUsername() + "/" + user.getUuid() + "]的Cookie.");
				}
				cookieService.removeCookie(request, response,CommonStandard.sessionTokenName + "_p",siteDomainRelation.getCookieDomain());	
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("尝试删除终端用户[" + user.getUsername() + "/" + user.getUuid() + "]的Cookie.");
				}
				cookieService.removeCookie(request, response,CommonStandard.sessionTokenName + "_f",siteDomainRelation.getCookieDomain());				
				cookieService.removeCookie(request, response, CommonStandard.sessionUsername,siteDomainRelation.getCookieDomain());
				cookieService.removeCookie(request, response, "outUuid",siteDomainRelation.getCookieDomain());

				String cookie = cookieService.getCookie(request, CommonStandard.sessionTokenName + "_f");
				logger.debug("XXXXXXX>退出后读取Cookie:" + CommonStandard.sessionTokenName + "_f" + "=" + cookie);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return;		
	}

	@Override
	public User login(HttpServletRequest request, HttpServletResponse response,
			User user) {
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + request.getServerName() + "]找不到指定的站点关系");
			return null;
		}
		if(user == null){
			logger.error("尝试直接登录的用户是空");
			return null;
		}
		if(user.getUsername() == null || user.getUsername().equals("")){
			logger.error("尝试直接登录的用户名是空");
			return null;
		}
		if(StringUtils.isBlank(user.getUserPassword()) || StringUtils.isBlank(user.getAuthKey())){
			logger.error("尝试直接登录的用户密码或密钥都是空");
			return null;
		}
		if(user.getUserPassword() != null) {
			user.setUserPassword(UserUtils.correctPassword(user.getUserPassword()));
		}
		user.setLastLoginIp(IpUtils.getClientIp(request));

		//生成客户端Token
		generateUserToken(user);
		String sessionTokenName = CommonStandard.sessionTokenName;
		if(user.getUserTypeId() == UserTypes.sysUser.getId()){
			if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
				if(siteDomainRelation.getCurrentStatus() == BasicStatus.relation.getId()){
					logger.error("系统用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致，但该主机名是总后台，系统用户仍然允许登录");
				} else {
					logger.error("系统用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
					return null;
				}
			}
			sessionTokenName += "_s";
		} else 	if(user.getUserTypeId() == UserTypes.partner.getId()){
			if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
				logger.error("合作用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
				return null;
			}
			sessionTokenName += "_p";
		} else 	{
			if(user.getOwnerId() != siteDomainRelation.getOwnerId()){
				logger.error("前端用户[" + user.getUuid() + "]的ownerId[" + user.getOwnerId() + "]与主机名[" + request.getServerName() + "]对应的ownerId[" + siteDomainRelation.getOwnerId() + "]不一致");
				return null;
			}
			sessionTokenName += "_f";
		} 
		logger.debug("为用户[" + user.getUuid() + "]生成SSO Token:" + user.getSsoToken());
		cookieService.addCookie(request, response, sessionTokenName, user.getSsoToken(),sessionTimeout,siteDomainRelation.getCookieDomain());
		String uesrCookieName = (StringUtils.isBlank(user.getNickName()) ? user.getUsername() : user.getNickName());
		try{
			uesrCookieName = java.net.URLEncoder.encode(uesrCookieName,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		cookieService.addCookie(request, response, CommonStandard.sessionUsername, uesrCookieName,sessionTimeout,siteDomainRelation.getCookieDomain(), false);
		//updateUserRememberName(request, response, user);
		user.setExtraStatus(UserExtraStatus.online.getId());
		userOnlineLogService.sync(user.getUuid(), UserExtraStatus.online.getId(), new Date());
		return user;
	}

	public boolean commonSignVerify(int uuid, String sign){
		User partner = partnerService.select(uuid);
		if(partner == null){
			logger.warn("找不到指定的用户[" + uuid + "]");
			return false;
		}
		if(partner.getCurrentStatus() != UserStatus.normal.getId()){
			logger.warn("用户[" + uuid + "]状态异常[" + partner.getCurrentStatus() + "]");
			return false;

		}		
		if(partner.getUserConfigMap() == null || partner.getUserConfigMap().size() < 1){
			logger.warn("用户[" + uuid + "]没有扩展数据");
			return false;
		}

		UserData ud = partner.getUserConfigMap().get(DataName.userCommonKey.toString());
		if( ud == null || ud.getDataValue() == null || ud.getDataValue().equals("")){
			logger.warn("用户[" + uuid + "]扩展数据中没有userCommonKey");
			return false;
		}		
		return false;
	}


	@Override
	public int checkSecAuth(HttpServletRequest request,  HttpServletResponse response, User partner, String userPassword) {


		if(isSecAuthed(request, response, partner)){
			return OperateResult.success.getId();
		}
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		if(partner.getUserPassword() == null){
			//重新获取密码
			partner = partnerService.select(partner.getUuid());
		}
		boolean passwordIsOk = false;
		logger.debug("用户[" + partner.getUuid() + "]当前密码是[" + partner.getUserPassword() + "]，提供二次验证的密码是:" + userPassword);
		if(partner.getUserPassword().length() == 64){
			//是加密后的密码
			String shaPassword = Crypt.passwordEncode(userPassword);
			if(shaPassword.equals(partner.getUserPassword())){
				passwordIsOk = true;
			}
		} else {
			if(userPassword.equals(partner.getUserPassword())){
				passwordIsOk = true;
			}
		}

		if(!passwordIsOk){
			return EisError.AUTH_FAIL.getId();
		}

		//写入二次验证数据加密Cookie		return 0;
		StringBuffer sb = new StringBuffer();
		sb.append(partner.getUserTypeId());
		sb.append("|");
		sb.append(partner.getUuid());
		sb.append("|");
		sb.append(new Date().getTime());
		String value = crypt.aesEncrypt(sb.toString());
		String ttl = SecurityLevelUtils.getConfig(securityLevel, DataName.secAuthTtl.toString());
		if(StringUtils.isBlank(ttl)){
			ttl = String.valueOf(CommonStandard.COOKIE_SEC_AUTH_COOKIE_DEFAULT_TTL);
		}
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());

		cookieService.addCookie(request, response, CommonStandard.COOKIE_SEC_AUTH_COOKIE_NAME, value, Integer.parseInt(ttl),siteDomainRelation.getCookieDomain());
		return OperateResult.success.getId();
	}

	@Override
	public int strictAuthorize(HttpServletRequest request,  HttpServletResponse response, User partner, String userPassword) {


		
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		if(partner.getUserPassword() == null){
			//重新获取密码
			partner = partnerService.select(partner.getUuid());
		}
		
		String data = ServletRequestUtils.getStringParameter(request, "data", null);

		String clearData = crypt.aesDecrypt(data);
		if(clearData == null){
			logger.error("无法对数据进行解密:" + data);
			return EisError.decryptError.id;
		}
		
		Map<String,String> dataMap = HttpUtils.getRequestDataMap(clearData);
		
		String tokenName = HttpUtils.getStringValueFromRequestMap(dataMap,"strictAuthToken");
		long strictAuthTtl = HttpUtils.getLongValueFromRequestMap(dataMap,"strictAuthTtl",0);

		String authType = HttpUtils.getStringValueFromRequestMap(dataMap,"strictAuthType");

		
		
		
		boolean passwordIsOk = false;
		logger.debug("用户[" + partner.getUuid() + "]当前密码是[" + partner.getUserPassword() + "]，需要验证的密码模式是:" + authType + ",密码token=" + tokenName + ",验证有效期是:" +  strictAuthTtl + "秒，提供二次验证的密码是:" + userPassword);
		
		String rightPassword = null;
		if(authType == null || authType.equalsIgnoreCase(CommonStandard.DEFAULT_STRICT_AUTH_MODE)){
			rightPassword = partner.getAuthKey();
		} else {
			rightPassword = partner.getUserPassword();

		}
		if(rightPassword == null){
			logger.error("用户:" + partner.getUuid() + "未设置密码，密码模式是:" + authType);
			return EisError.usernameOrPasswordIsNull.id;

		}
		if(rightPassword.length() == 64){
			//是加密后的密码
			String shaPassword = Crypt.passwordEncode(userPassword);
			if(shaPassword.equals(rightPassword)){
				passwordIsOk = true;
			}
		} else {
			if(userPassword.equals(rightPassword)){
				passwordIsOk = true;
			}
		}
		
		

		if(!passwordIsOk){
			return EisError.AUTH_FAIL.getId();
		}
		long tokenTime =  new Date().getTime() / 1000;
		logger.info("向用户Session中写入新的严格认证token时间[" + tokenName + "=" + tokenTime + "]");
		request.getSession().setAttribute(tokenName,tokenTime);		
		return OperateResult.success.getId();
	}



	/*
	 * 检查用户Cookie中的二次验证数据是否有效
	 */
	@Override
	public boolean isSecAuthed(HttpServletRequest request, HttpServletResponse response, User partner) {
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);

		//先从Cookie中获取之前的二次验证数据
		String existCookieData = cookieService.getCookie(request, CommonStandard.COOKIE_SEC_AUTH_COOKIE_NAME);
		if(existCookieData == null){
			logger.debug("用户[" + partner.getUuid() + "]Cookie中没有二次验证数据");
		} else {
			String clearExistData = crypt.aesDecrypt(existCookieData);
			if(clearExistData == null){
				logger.error("无法对用户[" + partner.getUuid() + "]Cookie中的二次验证数据进行解密:" + existCookieData);
			} else {
				String[] data = clearExistData.split("\\|");
				if(data.length < 3){
					logger.error("用户[" + partner.getUuid() + "]Cookie中的二次验证数据异常:" + clearExistData);
				} else {
					logger.debug("检查用户[" + partner.getUuid() + "]Cookie中的二次验证数据:" + clearExistData);
					if(data[0].equals(String.valueOf(partner.getUserTypeId()))
							&& data[1].equals(String.valueOf(partner.getUuid()))){
						long ts =  Long.parseLong(data[2]);
						Date beginTime = new Date(ts);
						String ttl = SecurityLevelUtils.getConfig(securityLevel, DataName.secAuthTtl.toString());
						if(StringUtils.isBlank(ttl)){
							ttl = String.valueOf(CommonStandard.COOKIE_SEC_AUTH_COOKIE_DEFAULT_TTL);
						}
						if((new Date().getTime() - ts) / 1000 > Long.parseLong(ttl)){
							logger.debug("Cookie中二次验证时间是" + sdf.format(beginTime) + "，有效期是:" + ttl + ",已过期");
							return false;
						}
						logger.debug("用户[" + partner.getUuid() + "]Cookie中的二次验证数据[" + sdf.format(beginTime) + "]，有效期是:" + ttl + ",仍然有效:" + clearExistData);
						return true;
					}							
				}
			}
		}
		return false;
	}


	/*
	 * 检查用户的密码是否应强制更换
	 */
	@Override
	public boolean passwordNeedChange(User partner) {
		if(securityLevel == null){
			logger.debug("当前未定义安全级别规范，不进行密码有效期检查");
			return false;
		}
		long passwordTtl = 0L;
		String ttl =SecurityLevelUtils.getConfig(securityLevel, DataName.passwordTtl.toString());
		if(NumericUtils.isNumeric(ttl)){
			passwordTtl = Long.parseLong(ttl);
		}
		if(passwordTtl < 1){
			logger.debug("当前安全级别[" + securityLevel + "]未定义密码有效期");
			return false;
		}
		PasswordLogCriteria passwordLogCriteria = new PasswordLogCriteria();
		passwordLogCriteria.setUuid(partner.getUuid());
		passwordLogCriteria.setPassword(partner.getUserPassword());
		//检查当前密码的创建时间
		List<PasswordLog> passwordLogList = passwordLogService.list(passwordLogCriteria);
		logger.debug("用户[" + partner.getUuid() + "]当前密码的记录是:" + (passwordLogList == null ? "空" : passwordLogList.size()));
		if(passwordLogList == null || passwordLogList.size() < 1){
			logger.debug("用户[" + partner.getUuid() + "]当前密码没有记录，认为不需要更改密码");
			return false;
		}
		Collections.sort(passwordLogList, new Comparator<PasswordLog>(){
			@Override
			public int compare(PasswordLog o1, PasswordLog o2) {
				if(o1.getCreateTime().after(o2.getCreateTime())){
					return 1;
				}
				return -1;
			}			
		});
		PasswordLog lastPasswordLog = passwordLogList.get(0);
		if(lastPasswordLog.getCreateTime() == null){
			logger.debug("用户[" + partner.getUuid() + "]当前密码的最近一条记录没有创建时间。");
			return false;
		}
		long createTtl = (new Date().getTime() - lastPasswordLog.getCreateTime().getTime()) / 1000;
		if(createTtl > passwordTtl){
			logger.debug("用户[" + partner.getUuid() + "]当前密码的最近一条创建时间是[" + sdf.format(lastPasswordLog.getCreateTime()) +  "]，已创建" + createTtl + "秒，超过了当前安全级别要求的时限:" + passwordTtl);
			return true;
		} else {
			logger.debug("用户[" + partner.getUuid() + "]当前密码的最近一条创建时间是[" + sdf.format(lastPasswordLog.getCreateTime()) +  "]，已创建" + createTtl + "秒，还未超过当前安全级别要求的时限:" + passwordTtl);
			return false;

		}
	}


	/*
	 * 检查将要设置的密码是否能通过系统安全性检查
	 * 例如长度、复杂度和使用历史等
	 */
	@Override
	public EisMessage passwordIsFine(User partner, String password) {
		if(securityLevel == null){
			logger.debug("当前未定义安全级别规范，不进行密码可用性检查");
			return new EisMessage(OperateResult.success.getId(),"密码符合规范");
		}
		int passwordMinLength = NumericUtils.getNumeric(SecurityLevelUtils.getConfig(securityLevel, DataName.passwordMinLength.toString()));
		if(passwordMinLength > 0){
			if(password.length() < passwordMinLength){
				logger.debug("用户[" + partner.getUuid() + "]尝试新设的密码长度[" + password.length() + "]低于当前安全级别[" + securityLevelId + "]定义的最小密码长度:" + passwordMinLength);
				return new EisMessage(EisError.passwordTooShort.getId(),"密码长度至少需要" + passwordMinLength + "位");
			}
		}
		int passwordStrongGrade = NumericUtils.getNumeric(SecurityLevelUtils.getConfig(securityLevel, DataName.passwordStrongGrade.toString()));
		String passwordStrongDesc = getDescForPasswordStrongGrade(passwordStrongGrade);
		if(passwordStrongGrade >= PasswordStrongGrade.CAPITIAL_AND_SMALL_LETTER.id){
			//需要大小写
			if(capitalCharPattern.matcher(password).find() && smallCharPattern.matcher(password).find()){
				logger.debug("密码符合安全级别[" + securityLevelId + "]要求的大小写同时存在要求");
			} else {
				logger.debug("密码不符合安全级别[" + securityLevelId + "]要求的大小写同时存在要求");
				return new EisMessage(EisError.passwordNotStrong.getId(),passwordStrongDesc);
			}			
		} 
		if(passwordStrongGrade >= PasswordStrongGrade.LETTER_AND_NUM.id){
			//需要前一级要求+数字组合
			if(digitalPattern.matcher(password).find()){
				logger.debug("密码符合安全级别[" + securityLevelId + "]要求的大小写和数字同时存在要求");
			} else {
				logger.debug("密码不符合安全级别[" + securityLevelId + "]要求的大小写和数字同时存在要求");
				return new EisMessage(EisError.passwordNotStrong.getId(),passwordStrongDesc);
			}			
		} 
		if(passwordStrongGrade >= PasswordStrongGrade.WORD_AND_SYMBOL.id){
			//需要前一级要求+标点符号
			if(symbolCharPattern.matcher(password).find()){
				logger.debug("密码符合安全级别[" + securityLevelId + "]要求的大小写、数字和标点符号同时存在要求");
			} else {
				logger.debug("密码不符合安全级别[" + securityLevelId + "]要求的大小写、数字和标点符号同时存在要求");
				return new EisMessage(EisError.passwordNotStrong.getId(), passwordStrongDesc);
			}			
		} 
		return new EisMessage(OperateResult.success.getId(),"密码符合规范");
		/*long passwordTtl = 0L;
		String ttl =SecurityLevelUtils.getConfig(securityLevel, DataName.passwordTtl.toString());
		if(NumericUtils.isNumeric(ttl)){
			passwordTtl = Long.parseLong(ttl);
		}
		if(passwordTtl < 1){
			logger.debug("当前安全级别[" + securityLevel + "]未定义密码有效期");
			return false;
		}
		PasswordLogCriteria passwordLogCriteria = new PasswordLogCriteria();
		passwordLogCriteria.setUuid(partner.getUuid());
		passwordLogCriteria.setPassword(partner.getUserPassword());
		//检查当前密码的创建时间
		List<PasswordLog> passwordLogList = passwordLogService.list(passwordLogCriteria);
		logger.debug("用户[" + partner.getUuid() + "]当前密码的记录是:" + (passwordLogList == null ? "空" : passwordLogList.size()));
		if(passwordLogList == null || passwordLogList.size() < 1){
			logger.debug("用户[" + partner.getUuid() + "]当前密码没有记录，认为不需要更改密码");
			return false;
		}
		Collections.sort(passwordLogList, new Comparator<PasswordLog>(){
			@Override
			public int compare(PasswordLog o1, PasswordLog o2) {
				if(o1.getCreateTime().after(o2.getCreateTime())){
					return 1;
				}
				return -1;
			}			
		});
		PasswordLog lastPasswordLog = passwordLogList.get(0);
		if(lastPasswordLog.getCreateTime() == null){
			logger.debug("用户[" + partner.getUuid() + "]当前密码的最近一条记录没有创建时间。");
			return false;
		}
		long createTtl = (new Date().getTime() - lastPasswordLog.getCreateTime().getTime()) / 1000;
		if(createTtl > passwordTtl){
			logger.debug("用户[" + partner.getUuid() + "]当前密码的最近一条创建时间是[" + sdf.format(lastPasswordLog.getCreateTime()) +  "]，已创建" + createTtl + "秒，超过了当前安全级别要求的时限:" + passwordTtl);
			return true;
		} else {
			logger.debug("用户[" + partner.getUuid() + "]当前密码的最近一条创建时间是[" + sdf.format(lastPasswordLog.getCreateTime()) +  "]，已创建" + createTtl + "秒，还未超过当前安全级别要求的时限:" + passwordTtl);
			return false;

		}*/
	}



	private String getDescForPasswordStrongGrade(int passwordStrongGrade) {
		if(passwordStrongGrade >= PasswordStrongGrade.WORD_AND_SYMBOL.id){
			return "密码必须同时有大小写字母、数字和标点";
		}
		if(passwordStrongGrade >= PasswordStrongGrade.LETTER_AND_NUM.id){
			return "密码必须同时有大小写字母和数字";
		}
		if(passwordStrongGrade >= PasswordStrongGrade.CAPITIAL_AND_SMALL_LETTER.id){
			return "密码必须同时有大小写字母";
		}
		return null;
	}

	/**
	 * 以重定向方式把用户转发到前往的URL
	 * 并把登录后返回URL写入Cookie
	 */
	@Override
	public String redirectByResponse(HttpServletRequest request, HttpServletResponse response,int userTypeId,  String toUrl,
			String backUrl, String cookieDomain){
		String cookieName = null;
		if(userTypeId == UserTypes.sysUser.getId()){
			cookieName = COOKIE_REDIRECT_COOKIE_NAME + "_s";
		} else if(userTypeId == UserTypes.partner.getId()){
			cookieName = COOKIE_REDIRECT_COOKIE_NAME + "_p";
		} else {
			cookieName = COOKIE_REDIRECT_COOKIE_NAME + "_f";
		}
		/*if(backUrl == null){
			logger.debug("重定向请求未提供);
			backUrl = request.getRequestURI();
		}
		if(backUrl.endsWith("json")){
			backUrl = "/";
		}*/
		if(backUrl != null && !backUrl.endsWith(".json")){
			try {
				backUrl = java.net.URLEncoder.encode(backUrl,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			cookieService.addCookie(request, response, cookieName, backUrl , CommonStandard.COOKIE_MAX_TTL, cookieDomain);
			logger.debug("向Cookie中写入新的重定向地址:" + backUrl);
		} else {
			logger.debug("请求重定向但未提供返回URL或返回URL是json:" + backUrl);

		}
		response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
		try {
			toUrl = java.net.URLDecoder.decode(toUrl,"UTF-8");
			response.sendRedirect(toUrl);		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}

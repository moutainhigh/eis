package com.maicard.captcha.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.dao.CaptchaDao;
import com.maicard.captcha.domain.Captcha;
import com.maicard.captcha.service.CaptchaService;
import com.maicard.captcha.service.Generator;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.standard.CommonStandard;

@Service
public class CaptchaServiceImpl  extends BaseService implements CaptchaService{

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CaptchaDao captchaDao;

	private final String  defaultPatchcaGenerator = "patchcaOrgGenerator";

	private String aesKey = null;

	private final int COOKIE_TTL = 600;


	@PostConstruct
	public void init(){
		try {
			aesKey = CryptKeyUtils.readAesKey();
		} catch (Exception e) {
			logger.error("无法读取系统AES密钥");
		}
	}
	@Override
	public Captcha get(CaptchaCriteria captchaCriteria) {
		Generator generator = null;
		String generatorName = null;
		if(captchaCriteria == null || captchaCriteria.getGenerator() == null){
			logger.debug("未指定生成器，使用默认生成器[" + defaultPatchcaGenerator + "]生成默认规格的验证码");
			generatorName = defaultPatchcaGenerator;
		} else {
			generatorName = captchaCriteria.getGenerator();
		}
		try{
			generator = (Generator)applicationContextService.getBean(generatorName);			
		}catch(Exception e){
			logger.error("找不到验证码生成器[" + generatorName + "]");
			return null;
		}	
		if(generator  == null){
			logger.error("找不到验证码生成器[" + generatorName + "]");
			return null;
		}
		return generator.generate(captchaCriteria);


	}

	@Override
	//把生成的验证码加密后保存到Session中
	public void setToCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String word) {
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		String cookieValue = crypt.aesEncrypt(word + "|" + System.currentTimeMillis());
		if(cookieName == null){
			cookieName = CommonStandard.sessionCaptchaName;
		}
		logger.debug("为用户设置Cookie:" + cookieName + ":" + cookieValue + ",TTL=" + COOKIE_TTL);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(COOKIE_TTL);
		cookie.setPath("/");
		response.addCookie(cookie);


	}

	@Override
	//校验word是否与Cookie中保存的一致
	public boolean verify(HttpServletRequest request, HttpServletResponse response, String cookieName, String word){
		String cookieWord = getFromCookie(request, response, cookieName);
		logger.debug("从Cookie中得到的验证码是:" + cookieWord + ",用户提交的验证码是:" + word);
		if(StringUtils.isBlank(cookieWord)|| StringUtils.isBlank(word)){
			return false;
		}
		if (word.length()==32){
			String sign = DigestUtils.md5Hex(cookieWord.toLowerCase()).toLowerCase();
			logger.info("收到验证码为md5加密后的值是"+word+"正确的值是"+sign);
			if (word.equals(sign)){
				return true;
			}
		}
		else{
			if(cookieWord.equalsIgnoreCase(word)){
				return true;
			}
		}
		return false;

	}

	@Override
	//从Cookie中获取上一次的验证码
	public String getFromCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		String encryptCaptcha = null;
		Cookie[] cookies = request.getCookies();
		if(cookies == null){
			return null;
		}
		if(StringUtils.isBlank(cookieName)){
			cookieName = CommonStandard.sessionCaptchaName;
		}
		
		for(Cookie cookie : cookies){
			logger.debug("比对Cookie:" + cookie.getName());
			if(cookie.getName().equals(cookieName)){
				encryptCaptcha = cookie.getValue();
				break;
			}
		}
		if(encryptCaptcha == null){
			return null;
		}
		try{
			String[] src = crypt.aesDecrypt(encryptCaptcha).split("\\|");
			if(src == null || src.length != 2){
				return null;
			}
			return src[0];
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;



	}

	@Override
	public int insert(Captcha captcha) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Captcha captcha) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Captcha captcha) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Captcha> list(CaptchaCriteria captchaCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

}

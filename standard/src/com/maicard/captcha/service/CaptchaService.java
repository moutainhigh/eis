package com.maicard.captcha.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.domain.Captcha;

public interface CaptchaService {
	
	public int insert(Captcha captcha);
	
	public int update(Captcha captcha);
	
	public int delete(Captcha captcha);
	
	public List<Captcha> list(CaptchaCriteria captchaCriteria);
	
	
	public Captcha get(CaptchaCriteria captchaCriteria);

	void setToCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String word);

	String getFromCookie(HttpServletRequest request, HttpServletResponse response, String cookieName);



	boolean verify(HttpServletRequest request, HttpServletResponse response, String cookieName, String word);


}

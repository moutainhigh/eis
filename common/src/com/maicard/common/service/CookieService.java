package com.maicard.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieService {

	
	
	//void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue);
	
	void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String domain);
	
	String getCookie(HttpServletRequest request, String cookieName);

	void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue,		int ttl, String domain, boolean httpOnly);

	void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue,		int ttl, String domain);


}

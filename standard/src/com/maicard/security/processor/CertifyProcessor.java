package com.maicard.security.processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.security.domain.User;

public interface CertifyProcessor {
	
	User getLoginedUser(HttpServletRequest request, HttpServletResponse response,  int userTypeId);

}

package com.maicard.common.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public interface TokenService {
		
	boolean tokenIsValid(HttpServletRequest request);

	void writeToken(ModelMap map);
	

}

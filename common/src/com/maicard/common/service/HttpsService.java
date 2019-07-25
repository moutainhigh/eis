package com.maicard.common.service;


public interface HttpsService {
	String httpsPost(String url,String memo)throws Exception;
	String httpsGet(String httpsURL)throws Exception;
//	public String httpsPost(String httpsUrl, String xmlStr);
	}

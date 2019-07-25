package com.maicard.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpClientPool {
	
	private static final int CONNECTION_TIMEOUT = 5000;
	private static final int SO_TIMEOUT = 30000;


	public static HttpClient getHttpClient(String host, int port) {

		HttpClient httpClients = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClients.getHostConfiguration().setHost(host, port, "http");
		httpClients.getParams().setConnectionManagerTimeout(CONNECTION_TIMEOUT);
		httpClients.getParams().setSoTimeout(SO_TIMEOUT);
		return httpClients;

	}

	

}


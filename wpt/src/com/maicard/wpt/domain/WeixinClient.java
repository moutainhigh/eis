package com.maicard.wpt.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信公众号客户信息
 *
 *
 * @author NetSnake
 * @date 2017年2月4日
 *
 */
public class WeixinClient implements Serializable {

	private static final long serialVersionUID = -7794617100358123518L;
	public WeixinClient(){}
	public long uuid;
	public String appId;
	public String authorizeCode;
	public Date authorizeCodeExpireTime;
	public String accessToken;
	public Date accessTokenExpireTime;
}

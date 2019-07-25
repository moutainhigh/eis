package com.maicard.wpt.domain;

import java.io.Serializable;

/**
 * 微信公众号平台及第三方平台的配置信息
 *
 *
 * @author NetSnake
 * @date 2017年2月3日
 *
 */
public class WeixinPlatformInfo implements Serializable {

	private static final long serialVersionUID = 8017710140375300446L;
	
	public String appId;
	public String appSecret;
	public String cryptKey;

	/**
	 * appToken等用于单平台模式
	 */
	public String appToken;

	public String payKey;

	public String payMechId;
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"appId=" + "'" + appId + "'," + 
				"appToken=" + "'" + appToken + "'" + 
				")";
	}

}

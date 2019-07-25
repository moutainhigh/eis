package com.maicard.wpt.domain;

import java.io.Serializable;

@Deprecated
public class WeixinToken implements Serializable{

	
	private static final long serialVersionUID = 6235490816261802452L;
	private String Token;
	private Long  expires_time;

	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public Long getExpires_time() {
		return expires_time;
	}
	public void setExpires_time(Long create_time) {
		this.expires_time = create_time;
	}
}
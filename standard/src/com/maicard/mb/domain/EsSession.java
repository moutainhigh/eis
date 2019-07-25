package com.maicard.mb.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.StringTools;
import com.maicard.method.ExtraValueAccess;
import com.maicard.security.domain.User;
/**
 * Eis Socket session,简称EsSession<br>
 * 包含web socket或nio socket
 * 
 *
 *
 * @author NetSnake
 * @date 2016年11月13日
 *
 */

@JsonInclude(Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSession implements Serializable,ExtraValueAccess {

	private static final long serialVersionUID = 6783688434592243952L;

	private String esSessionId;
	private String nativeSessionId;
	private String payload;
	private Map<String,Object> attributes;
	private User user;
	private long ownerId;
	private int serverId;
	private String clientIp;
	
	private Date createTime;
	private Date lastSaveTime;
	
	private int currentStatus;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append('(').append("esSessionId=").append(esSessionId).append(",serverId=").append(serverId).append(",createTime=").append(StringTools.getFormattedTime(createTime)).append(",ownerId=").append(ownerId).append(",payload=");
		if(payload == null){
			sb.append("null");
		} else {
			if(payload.length() > 16){
				sb.append(payload.substring(0,16));
			} else {
				sb.append(payload);
			}
		}
		if(this.user != null){
			sb.append(",uuid=" + this.user.getUuid());
		}
		sb.append(")");
		return sb.toString();
	}
	
	public EsSession() {
		this.createTime = new Date();
	}
	
	public EsSession(long ownerId) {
		this.ownerId = ownerId;
		this.createTime = new Date();
	}
	
	public EsSession(String esSessionId, String nativeSessionId, long ownerId) {
		this.esSessionId = esSessionId;
		this.nativeSessionId = nativeSessionId;
		this.ownerId = ownerId;
		this.createTime = new Date();
	}

	public Map<String, Object> getAttributes() {
		if(attributes == null){
			attributes = new HashMap<String,Object>();
		}
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
	@Override
	public void setExtraValue(String code, String value) {
		if(this.attributes == null){
			this.attributes = new HashMap<String,Object>();
		}
		if(StringUtils.isBlank(code) || StringUtils.isBlank(value)){
			return;
		}
		this.attributes.put(code, value);
	}
	
	@Override
	public String getExtraValue(String dataCode) {
		if(this.attributes == null || this.attributes.size() < 1){
			return null;
		}
		Object value = this.attributes.get(dataCode);
		if(value == null){
			return null;
		}
		return value.toString().trim();
			
	}
	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.attributes == null || this.attributes.size() < 1){
			return 0;
		}
		if(this.attributes.containsKey(dataCode) && NumericUtils.isNumeric(this.attributes.get(dataCode).toString())){
			return Float.parseFloat(this.attributes.get(dataCode).toString());
		}
		return 0;
	}

	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.attributes == null || this.attributes.size() < 1){
			return 0;
		}
		if(this.attributes.containsKey(dataCode) && NumericUtils.isNumeric(this.attributes.get(dataCode).toString())){
			return Long.parseLong(this.attributes.get(dataCode).toString());
		}
		return 0;
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.attributes == null || this.attributes.size() < 1){
			return false;
		}
		if(this.attributes.get(dataCode) != null && this.attributes.get(dataCode).toString().trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}

	public String getEsSessionId() {
		return esSessionId;
	}

	public void setEsSessionId(String esSessionId) {
		this.esSessionId = esSessionId;
	}

	public String getNativeSessionId() {
		return nativeSessionId;
	}

	public void setNativeSessionId(String nativeSessionId) {
		this.nativeSessionId = nativeSessionId;
	}

	public Date getLastSaveTime() {
		return lastSaveTime;
	}

	public void setLastSaveTime(Date lastSaveTime) {
		this.lastSaveTime = lastSaveTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

}

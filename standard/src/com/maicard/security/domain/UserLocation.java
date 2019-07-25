package com.maicard.security.domain;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLocation implements Serializable{
	protected final Logger logger = LoggerFactory.getLogger(getClass());


	private static final long serialVersionUID = -6936405443415176935L;

	private long uuid;
	
	private String userDesc;
	
	private String locationType;
	
	private long locationId;
	
	private String actionType;
	
	private Date actionTime;
	
	private String ip;
	
	private long ownerId;
	
	private String wsSessionId;
	
	//在写入位置数据时，是否把位置类型包含到KEY中
	private boolean withLocationTypeKey;
	
	public UserLocation() {
	}

	/*public UserLocation(long uuid, String locationData) {
		this.uuid = uuid;
		String[] data = locationData.split("#");
		if(data.length < 3) {
			logger.error("无法解析用户数据:" + locationData);
		}
		
		this.locationType = data[0];
		if(data.length > 1) {
		this.locationId = Long.parseLong(data[1]);
		}
		if(data.length > 2) {
		this.actionType = data[2];
		}
		try {
			this.actionTime = new SimpleDateFormat(CommonStandard.defaultDateFormat).parse(data[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	public UserLocation(long uuid, String locationType, long locationId, String actionType, long ownerId, String ip, String wsSessionId, boolean withLocationTypeKey){
		this.uuid = uuid;
		this.locationType = locationType;
		this.locationId = locationId;
		this.actionType = actionType;
		this.actionTime = new Date();
		this.ownerId = ownerId;
		this.ip = ip;
		this.wsSessionId = wsSessionId;
		this.withLocationTypeKey = withLocationTypeKey;
		
	}
	
	
	@Override
	public String toString() {
		return new StringBuffer().append(getClass().getName()).append("@").append(Integer.toHexString(hashCode()))
				.append("uuid=").append(uuid)
				.append(",locationType=").append(locationType)
				.append(",locationId=").append(locationId)
				.append(",actionType=").append(actionType)
				.append(",actionTime=").append(actionTime)
				.append(",ip=").append(ip)
				.append(",wsSessionId=").append(wsSessionId)
				.append(")").toString();
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public String getWsSessionId() {
		return wsSessionId;
	}

	public void setWsSessionId(String wsSessionId) {
		this.wsSessionId = wsSessionId;
	}

	public boolean isWithLocationTypeKey() {
		return withLocationTypeKey;
	}

	public void setWithLocationTypeKey(boolean withLocationTypeKey) {
		this.withLocationTypeKey = withLocationTypeKey;
	}

}

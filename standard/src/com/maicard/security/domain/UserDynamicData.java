package com.maicard.security.domain;

import java.io.Serializable;
import java.util.Date;

import com.maicard.common.domain.Location;



/**
 * 用户动态变化数据
 *
 * @author NetSnake
 * @date 2013-11-2 
 */
public class UserDynamicData implements Serializable{
	
	private static final long serialVersionUID = -300928646202619072L;
	private int userTypeId;
	private long uuid;
	private Date lastLogin;
	private String lastLoginIp;
	private int extraStatus;
	private Location location;
	
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"uuid=" + "'" + uuid + "'" + 
				")";	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public int getExtraStatus() {
		return extraStatus;
	}
	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}
	public int getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

}

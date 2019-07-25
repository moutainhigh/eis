package com.maicard.wpt.domain;

import java.util.Map;

import com.maicard.common.domain.EisObject;

public class WeixinGroup extends EisObject{

	private static final long serialVersionUID = 5264639261022573927L;

	private long groupId;

	private long outGroupId;

	private long parentGroupId;
	
	private int userCount;		//本组拥有的用户数

	private String groupName;

	private String groupDesc;

	private String groupIdentify;			//使用,分隔的组识别码

	private Map<String,String> data;			//扩展数据

	private long menuId;			//微信分组所对应自定义公众号菜单ID
	
	private String pageVersion;			//该组对应的页面版本号，用来应对相同的推广促销活动，对不同分组用户展现不同页面
	
	private long inviter;		//该分组属于哪个用户，主要用于微信第三方平台

	public WeixinGroup(){
		
	}
	public WeixinGroup(long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"groupId=" + "'" + groupId + "'," + 
			"outGroupId=" + "'" + outGroupId + "'," + 
			"groupName=" + "'" + groupName + "'," + 
			"currentStatus=" + "'" + currentStatus + "'," + 
			"ownerId=" + "'" + ownerId + "'" + 
			")";
	}
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getParentGroupId() {
		return parentGroupId;
	}

	public void setParentGroupId(long parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getGroupIdentify() {
		return groupIdentify;
	}

	public void setGroupIdentify(String groupIdentify) {
		this.groupIdentify = groupIdentify;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public long getOutGroupId() {
		return outGroupId;
	}

	public void setOutGroupId(long outGroupId) {
		this.outGroupId = outGroupId;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public String getPageVersion() {
		return pageVersion;
	}
	public void setPageVersion(String pageVersion) {
		this.pageVersion = pageVersion;
	}
	public long getInviter() {
		return inviter;
	}
	public void setInviter(long inviter) {
		this.inviter = inviter;
	}

}

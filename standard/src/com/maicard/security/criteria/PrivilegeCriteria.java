package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class PrivilegeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int privilegeId;
	private long uuid;
	private int userTypeId;
	private int parentPid;
	private String operateCode;		
	private String objectTypeCode;		//权限匹配的对象代码，如product、document、node
	private String objectId;		//权限匹配的对象ID
	private String objectAttribute;	//权限匹配的对象属性
	private String objectAttributeValue;	//权限匹配的对象属性的值
	private boolean recursive;		//是否覆盖下级权限
	private boolean inherit;		//是否继承上级权限

	private int[] roleIds;

	private int flag;


	public PrivilegeCriteria() {
	}

	public PrivilegeCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public PrivilegeCriteria(String targetObject, String code, long ownerId) {
		this.objectTypeCode = targetObject;
		this.operateCode = code;
		this.ownerId = ownerId;
	}

	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}
	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public int getParentPid() {
		return parentPid;
	}

	public void setParentPid(int parentPid) {
		this.parentPid = parentPid;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getObjectTypeCode() {
		return objectTypeCode;
	}

	public void setObjectTypeCode(String objectTypeCode) {
		this.objectTypeCode = objectTypeCode;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public boolean isInherit() {
		return inherit;
	}

	public void setInherit(boolean inherit) {
		this.inherit = inherit;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectAttribute() {
		return objectAttribute;
	}

	public void setObjectAttribute(String objectAttribute) {
		this.objectAttribute = objectAttribute;
	}

	public String getObjectAttributeValue() {
		return objectAttributeValue;
	}

	public void setObjectAttributeValue(String objectAttributeValue) {
		this.objectAttributeValue = objectAttributeValue;
	}

	public int[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(int... roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	public String toString(){
		return new StringBuffer().append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append("(objectTypeCode=").append(objectTypeCode).append(",operateCode=").append(operateCode).append(",objectId=").append(objectId).append(",objectAttribute=").append(objectAttribute).append(")").toString();
	}

}

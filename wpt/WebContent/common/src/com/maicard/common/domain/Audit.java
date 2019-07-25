package com.maicard.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;


/**
 * 全局审核记录
 * 审核类型auditType：
 * 账户操作行为（登录、注销、修改资料）
 * 后台操作（增删查改）
 * 资金操作
 * 
 * 
 * @author NetSnake
 * @date 2012-6-24
 */
public class Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private int operateId; 

	private int auditPrivilegeRefId; //相关权限ID
	
	private int auditObjectTypeId; //审核对象类型

	private int auditObjectId; //审核对象ID

	private int auditRefUserId;	//审核相关用户

	private int auditResult;	//审核结果

	private Date auditTime;

	private String auditMessage;

	private String auditSeal;
	
	private String source;
	
	private String serializeData;
	
	private String ip;
	
	//
	private HashMap<String,Object> data;


	public Audit() {
		this.auditTime = new Date();
	}
	
	public Audit(int auditRefUserId, int auditResult,String source){
		this.source = source;
		this.auditRefUserId = auditRefUserId;
		this.auditTime = new Date();
	}
	
	public Audit(
			int operateId, 
			int auditObjectTypeId, 
			int auditObjectId, 
			int auditPrivilegeRefId, 
			int auditRefUserId, 
			int auditResult){
		this.operateId = operateId;
		this.auditObjectTypeId = auditObjectTypeId;
		this.auditObjectId = auditObjectId;
		this.auditPrivilegeRefId = auditPrivilegeRefId;
		this.auditRefUserId = auditRefUserId;
		this.auditResult = auditResult;
		this.auditTime = new Date();
	}


	public int getAuditPrivilegeRefId() {
		return auditPrivilegeRefId;
	}

	public void setAuditPrivilegeRefId(int auditPrivilegeRefId) {
		this.auditPrivilegeRefId = auditPrivilegeRefId;
	}


	public int getAuditRefUserId() {
		return auditRefUserId;
	}

	public void setAuditRefUserId(int auditRefUserId) {
		this.auditRefUserId = auditRefUserId;
	}

	public int getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(int auditResult) {
		this.auditResult = auditResult;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditMessage() {
		return auditMessage;
	}

	public void setAuditMessage(String auditMessage) {
		this.auditMessage = auditMessage;
	}

	public String getAuditSeal() {
		return auditSeal;
	}

	public void setAuditSeal(String auditSeal) {
		this.auditSeal = auditSeal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Audit other = (Audit) obj;
		if (id != other.id)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"id=" + "'" + id + "'" + 
			")";
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getOperateId() {
		return operateId;
	}

	public void setOperateId(int operateId) {
		this.operateId = operateId;
	}

	public int getAuditObjectTypeId() {
		return auditObjectTypeId;
	}

	public void setAuditObjectTypeId(int auditObjectTypeId) {
		this.auditObjectTypeId = auditObjectTypeId;
	}

	public int getAuditObjectId() {
		return auditObjectId;
	}

	public void setAuditObjectId(int auditObjectId) {
		this.auditObjectId = auditObjectId;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSerializeData() {
		return serializeData;
	}

	public void setSerializeData(String serializeData) {
		this.serializeData = serializeData;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}

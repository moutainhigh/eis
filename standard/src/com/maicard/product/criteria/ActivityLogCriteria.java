package com.maicard.product.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class ActivityLogCriteria extends Criteria {


	private static final long serialVersionUID = 2658500222226422077L;
	private String activityLogId;	 //PK
	private long activityId; 
	private String activityType;
	private long uuid;	
	private String ip;	
	private Date beginTime;
	private Date endTime;
	private String action;		//动作代码，比如是具体参与JOIN还是别的
	private String promotion;
	private int index;
	
	private String activityIdentify;	//活动识别码，比如哪些带指定推广代码的用户才能参与

	
	public static final int PAY_FEE_JOIN_NONE = 1;
	public static final int PAY_FEE_JOIN_PAY = 2;			//付费参与
	public static final int PAY_FEE_JOIN_FREE = 3;			//免费参与
	
	private int payFeeJoin;		//本次活动是否是付费参加的

	public String getActivityLogId() {
		return activityLogId;
	}
	public void setActivityLogId(String activityLogId) {
		this.activityLogId = activityLogId;
	}
	public long getActivityId() {
		return activityId;
	}
	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getPayFeeJoin() {
		return payFeeJoin;
	}
	public void setPayFeeJoin(int payFeeJoin) {
		this.payFeeJoin = payFeeJoin;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getActivityIdentify() {
		return activityIdentify;
	}
	public void setActivityIdentify(String activityIdentify) {
		this.activityIdentify = activityIdentify;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
}

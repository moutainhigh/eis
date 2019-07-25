package com.maicard.product.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EVEisObject;
import com.maicard.common.domain.ExtraData;
import com.maicard.security.domain.User;

@NeedJmsDataSyncP2P
public class ActivityLog extends EVEisObject  {
	

	private static final long serialVersionUID = -7028938415871204616L;
	private String activityLogId;	 //PK
	private long activityId; 
	private String activityType; //活动类型
	private String action;		//动作代码，比如是具体参与JOIN还是别的
	private long uuid;	
	private String ip;	
	private HashMap<String,String> data;	//具体活动数据
	private List<ExtraData> extraDataList;
	private Date logTime;
	private int flag;
	//////////
	private int accountLimit;
	private int ipLimit;
	private String activityIdentify;	//活动识别码，比如哪些带指定推广代码的用户才能参与

	private String promotion;		//参与后的奖励，以分号分割多个奖励，每个奖励的格式是：奖励内容#奖励数量#几率，如money#100#0.02是奖励100个元宝，几率是2%
	private String fee;			//参与时是否付费，付费格式
	private int payFeeJoin;
	
	private float completePercent;		//完成百分比
	
	

	public String[] getExtraValues(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return null;
		}
		if(this.data.containsKey(dataCode)){
			return this.data.get(dataCode).trim().split(",");
		}
		return null;	
	}

	

	public int getPayFeeJoin() {
		return payFeeJoin;
	}
	public void setPayFeeJoin(int payFeeJoin) {
		this.payFeeJoin = payFeeJoin;
	}
	public ActivityLog() {
	}
	public ActivityLog(Activity activity, User user, int status) {
		this.activityId = activity.getActivityId();
		this.logTime = new Date();
		this.accountLimit = activity.getAccountLimit();
		this.ipLimit = activity.getIpLimit();
		this.uuid = user.getUuid();
		this.ip = user.getLastLoginIp();
		this.promotion = activity.getPromotion();
		this.fee = activity.getAccountFeePerCount();
		this.currentStatus = status;
		this.activityType = activity.getActivityType();
		
	}
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
	
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public int getAccountLimit() {
		return accountLimit;
	}
	public void setAccountLimit(int accountLimit) {
		this.accountLimit = accountLimit;
	}
	public int getIpLimit() {
		return ipLimit;
	}
	public void setIpLimit(int ipLimit) {
		this.ipLimit = ipLimit;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public float getCompletePercent() {
		return completePercent;
	}
	public void setCompletePercent(float completePercent) {
		this.completePercent = completePercent;
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
	public List<ExtraData> getExtraDataList() {
		return extraDataList;
	}
	public void setExtraDataList(List<ExtraData> extraDataList) {
		this.extraDataList = extraDataList;
	}

	

}

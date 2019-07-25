package com.maicard.product.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class ActivityCriteria extends Criteria {


	private static final long serialVersionUID = -1313735831497854951L;

	public static final String ACTIVITY_TYPE_COUPON = "coupon";	//优惠券活动
	public static final String ACTIVITY_TYPE_SIGN = "sign";	//签名活动
	public static final String ACTIVITY_TYPE_GAME = "game";	//游戏活动
	public static final String ACTIVITY_TYPE_AUCTION = "auction";	//抢购活动
	public static final String ACTIVITY_TYPE_TUAN = "tuan";	//团购
	public static final String ACTIVITY_TYPE_PROMOTION = "promotion";	//促销
	public static final String ACTIVITY_TYPE_BUY = "buy";	//正常购买
	public static final String ACTIVITY_TYPE_SUBSCRIBE = "subscribe";		//关注、新注册
	public static final String ACTIVITY_TYPE_PAY_SUCCESS = "pay_success";		//支付成功后



	private int activityId; 
	private String activityName;	//活动名字
	private String activityCode;	//活动代码
	private String activityIdentify;	//活动识别码，比如哪些带指定推广代码的用户才能参与
	private String activityType; //活动类型
	private int accountLimit;	//每个帐号限制参与次数
	private int ipLimit;		//每个IP限制参与次数
	private boolean logging;	//是否记入日志
	private Date beginTime;
	private Date endTime;

	public ActivityCriteria() {
	}

	public ActivityCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
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

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
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

	public String getActivityIdentify() {
		return activityIdentify;
	}

	public void setActivityIdentify(String activityIdentify) {
		this.activityIdentify = activityIdentify;
	}

}

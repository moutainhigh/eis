package com.maicard.money.criteria;

import com.maicard.common.base.InviterSupportCriteria;

public class CouponModelCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 919072647141694697L;
	
	
	public static final String COUPON_CHARGE_MONEY	= "COUPON_CHARGE_MONEY";		//卡券将直接充值到账户的giftMoney
	public static final String COUPON_NO_CHARGE_MONEY	= "COUPON_NO_CHARGE_MONEY";	//卡券会保留而不会充值到账户


	private long parentCouponModelId;			//上级产品ID

	protected long uuid;				//获取用户

	protected String couponCode;			//可能的活动编码

	private String extraCode;			//外部代码

	private String lockGlobalUniqueId;	//锁定时的全局锁定ID

	private String content;

	private int lockWaitingCount = 5;		//锁定时等待的查询次数

	private int lockWaitingInterval = 3;		//锁定时等待查询的间隔秒数

	private String identify;				//识别码

	private int minKeepCount;			//该卡券在系统中的最少保有量

	private int maxKeepCount;			//该卡券在系统中的最多保有量
	
	private boolean autoKeepEnabled;		//该卡券是否需要自动保有
	
	private boolean fetchWhenList;		//当列出优惠券产品时，同时获取一个优惠券代码
	
	public CouponModelCriteria(){
		
	}
	
	public CouponModelCriteria(long ownerId){
		this.ownerId = ownerId;
	}

	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}
	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId;
	}
	public int getLockWaitingCount() {
		return lockWaitingCount;
	}
	public void setLockWaitingCount(int lockWaitingCount) {
		this.lockWaitingCount = lockWaitingCount;
	}
	public int getLockWaitingInterval() {
		return lockWaitingInterval;
	}
	public void setLockWaitingInterval(int lockWaitingInterval) {
		this.lockWaitingInterval = lockWaitingInterval;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	public String getExtraCode() {
		return extraCode;
	}
	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}
	public long getParentCouponModelId() {
		return parentCouponModelId;
	}
	public void setParentCouponModelId(long parentCouponModelId) {
		this.parentCouponModelId = parentCouponModelId;
	}
	public int getMinKeepCount() {
		return minKeepCount;
	}
	public void setMinKeepCount(int minKeepCount) {
		this.minKeepCount = minKeepCount;
	}
	public int getMaxKeepCount() {
		return maxKeepCount;
	}
	public void setMaxKeepCount(int maxKeepCount) {
		this.maxKeepCount = maxKeepCount;
	}
	public boolean isAutoKeepEnabled() {
		return autoKeepEnabled;
	}
	public void setAutoKeepEnabled(boolean autoKeepEnabled) {
		this.autoKeepEnabled = autoKeepEnabled;
	}

	public boolean isFetchWhenList() {
		return fetchWhenList;
	}

	public void setFetchWhenList(boolean fetchWhenList) {
		this.fetchWhenList = fetchWhenList;
	}

}

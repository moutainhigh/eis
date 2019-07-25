package com.maicard.product.domain;

/**
 * 活动奖品的一个基础原型
 * 可用于活动处理器进行排序等操作
 * 
 *
 * @author NetSnake
 * @date 2015年9月11日 
 */
public class ActivityReward {
	
	private int activityRewardId;
	
	private String rewardType;		//奖励类型，money=真钱,coin=金币,life=生命
	
	private float rewardAmount;	//奖励数量
	
	private float rewardRate;		//中奖几率，
	
	private String rewardName;			//奖品名称
	
	private String rewardDesc;			//奖品说明
	
	private long rewardObjectId;	//奖品ID，对于产品或道具奖励的对象ID
	
	private int requiredUserLevel;		//需要的用户或角色的级别
	
	private int index;
	
	private int currentStatus;
	
	private long uuid;				//中奖用户或角色的UUID
	
	private String promotionData;		//来自活动数据的原始中奖数据，以#分割

	/*
	 * 解析奖品数据，以#分割
	 * 奖励类型#奖励数量#中奖几率或需要的次数如签到次数#需要的用户级别
	 * 最后一个可选
	 */
	public ActivityReward(int index,String promotionData) {
		if(promotionData == null || promotionData.trim().length() < 5){
			return;
		}
		String[] data = promotionData.split("#");
		if(data == null || data.length < 3){
			return;
		}
		this.index  = index;
		this.rewardType = data[0].toLowerCase();
		this.rewardAmount = Float.parseFloat(data[1]);
		this.rewardRate = Float.parseFloat(data[2]);
		this.rewardObjectId = (int)this.rewardRate;
		if(data.length >= 4){
			this.requiredUserLevel = Integer.parseInt(data[3]);
		}
		this.promotionData = promotionData;
	}

	public ActivityReward() {
	}

	public int getActivityRewardId() {
		return activityRewardId;
	}

	public void setActivityRewardId(int activityRewardId) {
		this.activityRewardId = activityRewardId;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public float getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(float rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public float getRewardRate() {
		return rewardRate;
	}

	public void setRewardRate(float rewardRate) {
		this.rewardRate = rewardRate;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getRequiredUserLevel() {
		return requiredUserLevel;
	}

	public void setRequiredUserLevel(int requiredUserLevel) {
		this.requiredUserLevel = requiredUserLevel;
	}

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}
	
	@Override
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(activityRewardId=" + activityRewardId + ",rewardType=" + rewardType + ",rewardAmount=" + rewardAmount + ",rewardRate=" + rewardRate + ",requiredUserLevel=" + requiredUserLevel + ",index=" + index + ",currentStatus=" + currentStatus + ")";
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public long getRewardObjectId() {
		return rewardObjectId;
	}

	public void setRewardObjectId(long rewardObjectId) {
		this.rewardObjectId = rewardObjectId;
	}

	public String getPromotionData() {
		return promotionData;
	}

	public void setPromotionData(String promotionData) {
		this.promotionData = promotionData;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	public String getRewardDesc() {
		return rewardDesc;
	}

	public void setRewardDesc(String rewardDesc) {
		this.rewardDesc = rewardDesc;
	}
	

}

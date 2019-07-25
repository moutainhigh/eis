package com.maicard.product.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EVEisObject;
import com.maicard.views.JsonFilterView;

@JsonIgnoreProperties(ignoreUnknown = true)
@NeedJmsDataSyncP2P
public class Activity extends EVEisObject implements Cloneable {


	private static final long serialVersionUID = -7028938415871204616L;

	private long activityId; //PK

	private String activityName;	//活动名字

	private String activityCode;	//活动代码	

	private String activityDesc;	//活动说明

	private String activityType; //活动类型

	private String activityIdentify;	//活动识别码，比如哪些带指定推广代码的用户才能参与


	@JsonView({JsonFilterView.Partner.class})
	private String processor;	//处理器

	@JsonView({JsonFilterView.Partner.class})
	private int accountLimit;	//每个帐号限制参与次数

	@JsonView({JsonFilterView.Partner.class})
	private int accountFreeLimit;	//每个账号每天可免费参与次数

	@JsonView({JsonFilterView.Partner.class})
	private String accountFeePerCount;		//当免费结束后，每个账号参与一次要付出的费用，格式是费用类型#数量，如coin#100


	@JsonView({JsonFilterView.Partner.class})
	private String promotion;		//参与后的奖励，以分号分割多个奖励，每个奖励的格式是：奖励内容#奖励数量#几率，如money#100#0.02是奖励100个元宝，几率是2%

	@JsonView({JsonFilterView.Partner.class})
	private int ipLimit;		//每个IP限制参与次数
	/*
	 * 是否有充值优惠，如几率、次数等，内容为：优惠类型,优惠数量,是否重复计算,充值产品,充值次数,充值时间，
	 * 如 count,1,true,160204,3,0表示3次针对160204的充值就增加一次机会，重复计算
	 * 如 count,2,false,160204,1,86400表示1天内对160204有充值就增加一次机会，但不重复计算
	 */
	@JsonView({JsonFilterView.Partner.class})
	private String payPromotion;	
	/*
	 * 是否有登录优惠，内容为：优惠类型,优惠数量,是否重复计算,登录产品,在线时长,时间
	 * 如count,1,true,0,7200,86400表示在1天内每在线超过2小时，就可以增加一次机会
	 * 如count,2,false,0,28800,86400表示在1天内在线超过8小时，就可以增加一次机会，不重复计算
	 */
	@JsonView({JsonFilterView.Partner.class})
	private String loginPromotion;	

	@JsonView({JsonFilterView.Partner.class})
	private boolean logging;	//是否记入日志
	
	private String url;		//活动URL

	private Date beginTime;

	private Date endTime;

	private float completePercent;		//完成百分比

	private int weight;		//多个活动同时有效时，活动的优先顺序

	@JsonView({JsonFilterView.Partner.class})
	private String exclude;	//与其他活动的排斥规则

	private String relationObjectType;		//关联对象类型

	private String relationObjectData;		//关联对象数据

	private String relationMode;			//参与该活动的对象的关联模式，如果空，则必须是objectId相等才能参与，如果是ALL，则所有对象均可参与


	public Activity(){

	}

	public Activity(long ownerId) {
		this.ownerId = ownerId;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("@");
		sb.append(Integer.toHexString(hashCode()));
		sb.append("(");
		sb.append("activityId=");
		sb.append("'");
		sb.append(activityId);
		sb.append("',");
		sb.append("activityType=");
		sb.append("'");
		sb.append(activityType);
		sb.append("',");
		sb.append("currentStatus=");
		sb.append("'");
		sb.append(currentStatus);
		sb.append("')");
		return sb.toString();

	}
	public long getActivityId() {
		return activityId;
	}
	public void setActivityId(long activityId) {
		this.activityId = activityId;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
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
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getPayPromotion() {
		return payPromotion;
	}
	public void setPayPromotion(String payPromotion) {
		this.payPromotion = payPromotion;
	}
	public String getLoginPromotion() {
		return loginPromotion;
	}
	public void setLoginPromotion(String loginPromotion) {
		this.loginPromotion = loginPromotion;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public int getAccountFreeLimit() {
		return accountFreeLimit;
	}
	public void setAccountFreeLimit(int accountFreeLimit) {
		this.accountFreeLimit = accountFreeLimit;
	}
	public String getAccountFeePerCount() {
		return accountFeePerCount;
	}
	public void setAccountFeePerCount(String accountFeePerCount) {
		this.accountFeePerCount = accountFeePerCount;
	}

	public Activity clone(){
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Activity)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
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
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getExclude() {
		return exclude;
	}
	public void setExclude(String exclude) {
		this.exclude = exclude;
	}
	public String getRelationObjectType() {
		return relationObjectType;
	}
	public void setRelationObjectType(String relationObjectType) {
		this.relationObjectType = relationObjectType;
	}
	public String getRelationObjectData() {
		return relationObjectData;
	}
	public void setRelationObjectData(String relationObjectData) {
		this.relationObjectData = relationObjectData;
	}
	public String getRelationMode() {
		return relationMode;
	}
	public void setRelationMode(String relationMode) {
		this.relationMode = relationMode;
	}

	

	
}

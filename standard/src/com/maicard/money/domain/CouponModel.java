package com.maicard.money.domain;

import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.EVEisObject;
import com.maicard.views.JsonFilterView;

/**
 *
 *	优惠券产品对象，即静态对象
 *
 * @author NetSnake
 * @date 2015年11月25日
 * 
 */

public class CouponModel extends EVEisObject implements Cloneable {

	private static final long serialVersionUID = -4655346607691924514L;


	protected long couponModelId;			//PK

	@JsonView({JsonFilterView.Full.class})
	private long parentCouponModelId;			//上级产品ID

	protected String couponType;				//卡券产品类型

	protected int level;					//级别

	protected String couponCode;			//优惠券编码

	protected String extraCode;			//外部编码

	protected String couponModelName;			//优惠券名称

	protected String couponModelDesc;			//优惠券说明

	protected String content;			//优惠券的内容，比如条码等

	protected byte[] binContent;			//优惠券的二进制内容，比如图片

	@JsonView({JsonFilterView.Full.class})
	protected String promotionData;			//优惠配置

	@JsonView({JsonFilterView.Full.class})
	protected String processor;			//处理器

	protected String imageUrl;			//该优惠券的对应显示图片


	protected Money costMoney;			//需要多少钱兑换

	protected	Money giftMoney;			//赠送多少钱

	/**
	 * 微信卡券中的描述
	 */
	protected String memory;			

	protected Date validTimeBegin;		//有效期;

	protected String identify;			//识别码

	protected Date validTimeEnd;		

	/**
	 * 用户对应的推广渠道、经销商
	 */
	@JsonView({JsonFilterView.Partner.class})
	private long inviter;			

	private int minKeepCount;			//该卡券在系统中的最少保有量

	private int maxKeepCount;			//该卡券在系统中的最多保有量
	/*
	 * 扩展配置数据，以JSON形式存放在数据库中TEXT属性，取出时转换为HashMap
	 * 取出和存入自动转换
	 * 不应当显示给前端用户
	 */
	@JsonView({JsonFilterView.Partner.class})
	private HashMap<String,String> data;	



	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"couponModelId=" + "'" + couponModelId + "'," + 
				"couponCode=" + "'" + couponCode + "'," + 
				"couponModelName=" + "'" + couponModelName + "'," + 
				"currentStatus=" + "'" + currentStatus + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}

	@Override
	public CouponModel clone(){
		try{
			return (CouponModel)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public long getCouponModelId() {
		return couponModelId;
	}

	public void setCouponModelId(long couponModelId) {
		this.couponModelId = couponModelId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public byte[] getBinContent() {
		return binContent;
	}

	public void setBinContent(byte[] binContent) {
		this.binContent = binContent;
	}

	public String getPromotionData() {
		return promotionData;
	}

	public void setPromotionData(String promotionData) {
		this.promotionData = promotionData;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getValidTimeBegin() {
		return validTimeBegin;
	}

	public void setValidTimeBegin(Date validTimeBegin) {
		this.validTimeBegin = validTimeBegin;
	}

	public Date getValidTimeEnd() {
		return validTimeEnd;
	}

	public void setValidTimeEnd(Date validTimeEnd) {
		this.validTimeEnd = validTimeEnd;
	}



	public String getCouponModelName() {
		return couponModelName;
	}

	public void setCouponModelName(String couponModelName) {
		this.couponModelName = couponModelName;
	}

	public String getCouponModelDesc() {
		return couponModelDesc;
	}

	public void setCouponModelDesc(String couponModelDesc) {
		this.couponModelDesc = couponModelDesc;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	public long getParentCouponModelId() {
		return parentCouponModelId;
	}

	public void setParentCouponModelId(long parentCouponModelId) {
		this.parentCouponModelId = parentCouponModelId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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


	public Money getGiftMoney() {
		return giftMoney;
	}


	public void setGiftMoney(Money giftMoney) {
		this.giftMoney = giftMoney;
	}


	public String getCouponType() {
		return couponType;
	}


	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	
	public Money getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(Money costMoney) {
		this.costMoney = costMoney;
	}

	public long getInviter() {
		return inviter;
	}

	public void setInviter(long inviter) {
		this.inviter = inviter;
	}


}

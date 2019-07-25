package com.maicard.billing.criteria;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.maicard.common.base.InviterSupportCriteria;

public class ShareConfigCriteria extends InviterSupportCriteria implements Cloneable{

	private static final long serialVersionUID = 1L;

	public static final String PLUS = "plus";
	public static final String MINUS = "minus";

	public static final int MONEY_SHARE_MODE_TO_USER	= 1;	//直接分成给当前支付用户
	
	public static final int MONEY_SHARE_MODE_TO_CHANNEL = 2;	//支付分成给用户的渠道

	private int shareConfigId;

	private String moneyDirect;

	private String shareConfigCode;

	private String shareConfigName;

	private String objectType;

	private long objectId;

	private Float sharePercent;
	
	private String chargeType;	
	
	private String[] clearTypes;

	private String payCardType;
	
	private long shareUuid;

	private long ttl;

	private int weight;

	private boolean defaultConfig;

	@Override
	public ShareConfigCriteria clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (ShareConfigCriteria)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public ShareConfigCriteria() {}

	public ShareConfigCriteria(long ownerId) {
		this.ownerId = ownerId;
	}




	public int getShareConfigId() {
		return shareConfigId ;
	}



	public void setShareConfigId(int shareConfigId) {
		this.shareConfigId = shareConfigId;
	}







	public long getObjectId() {
		return objectId ;
	}



	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}



	public Float getSharePercent() {
		return sharePercent ;
	}



	public void setSharePercent(Float sharePercent) {
		this.sharePercent = sharePercent;
	}



	public long getShareUuid() {
		return shareUuid;
	}

	public void setShareUuid(long shareUuid) {
		this.shareUuid = shareUuid;
	}

	public String getShareConfigCode() {
		return shareConfigCode;
	}

	public void setShareConfigCode(String shareConfigCode) {
		if(shareConfigCode != null && !shareConfigCode.trim().equals("")){
			this.shareConfigCode = shareConfigCode.trim();
		}
	}

	public String getShareConfigName() {
		return shareConfigName;
	}



	public void setShareConfigName(String shareConfigName) {
		if(shareConfigName != null && !shareConfigName.trim().equals("")){
			this.shareConfigName = shareConfigName.trim();
		}
	}


	public String getObjectType() {
		return objectType;
	}


	public void setObjectType(String objectType) {
		if(objectType != null && !objectType.trim().equals("")){
			this.objectType = objectType.trim();
		}
	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	public boolean isDefaultConfig() {
		return defaultConfig;
	}
	public void setDefaultConfig(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getMoneyDirect() {
		return moneyDirect;
	}
	public void setMoneyDirect(String moneyDirect) {
		this.moneyDirect = moneyDirect;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}

	public String[] getClearTypes() {
		return clearTypes;
	}

	public void setClearType(String... clearTypes) {
		this.clearTypes = clearTypes;
	}

}

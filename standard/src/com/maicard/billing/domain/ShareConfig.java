package com.maicard.billing.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.maicard.common.domain.EisObject;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraValueAccess;

/**
 * 分成配置信息
 */
public class ShareConfig extends EisObject implements Cloneable,ExtraValueAccess{

	private static final long serialVersionUID = 1L;
	/**
	 * 分成配置ID
	 */
	private int shareConfigId;

	private String shareConfigCode;

	private String shareConfigName;


	private int objectId;
	/**
	 * 分成比例
	 */
	private float sharePercent;
	/**
	 * 资金方向，plus:增加资金，minus:减少资金
	 */
	private String moneyDirect;
	/**
	 * 付费类型：0预付费，1后付费
	 */
	private String chargeType;
	
	private Float beginMoney;
	
	private Float endMoney;

	private long shareUuid;
	/**
	 * 分成类型，business:厂商分成, channel:推广渠道
	 */
	private String shareType;
	
	/**
	 * 付费卡类型
	 */
	private String payCardType;
	/**
	 * 分成类型对应的TTL
	 */
	private int ttl;
	/**
	 * 最大重试次数
	 */
	private int maxRetry;
	
	private Map<String,String> data;
	
	private int weight;
	/**
	 * 是否为某一产品-某一partner的默认配置
	 */
	private boolean defaultConfig;
	
	/*
	 * 交易的进入时间策略，由英文逗号分割。第一位是进入时间调整的天数，第二位是进入时间调整的秒数或固定时分秒
	 * 0,0 表示不做调整，使用当前时间
	 * 1,0 表示将进入时间向后推一天，时分秒不变
	 * 1, 02:00:01 表示将进入时间推后一天，并把时分秒设置为02:00:01，即第二天的02:00:01
	 * 1, 50 表示将进入时间退后一天，并在当前时间的基础上加50秒
	 */
	private String enterTimePolicy;	

	/**
	 * 非持久化属性/动态属性
	 */
	private String objectIdName;
	/**
	 * 结算方式 auto 自动，manual 手动
	 * 根据结算方式：将商户账户资金做结算
	 */
	private String clearWay;
	/**
	 * 清算类型
	 */
	private String clearType;
	/**
	 * 清算周期开始 00:00:00
	 */
	private String clearStartDate;
	/**
	 * 清算周期 单位时间：秒
	 * 根据开始时间做偏移
	 */
	private int offsetClearTimes;
	/**
	 * 结算账户
	 * ACCT 可用余额
	 * BANK 先到余额再到银行卡
	 */
	private String statementAccount;
	
	@Override
	public void setExtraValue(String code, String value) {
		if(this.data == null){
			this.data = new HashMap<String,String>();
		}
		if(StringUtils.isBlank(code) || StringUtils.isBlank(value)){
			return;
		}
		this.data.put(code, value);
	}

	@Override
	public String getExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return null;
		}
		if(this.data.containsKey(dataCode)){
			return this.data.get(dataCode).trim();
		}
		return null;		
	}
	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return 0;
		}
		if(this.data.containsKey(dataCode) && NumericUtils.isNumeric(this.data.get(dataCode))){
			return Float.parseFloat(this.data.get(dataCode));
		}
		return 0;
	}

	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return 0;
		}
		if(this.data.containsKey(dataCode) && NumericUtils.isNumeric(this.data.get(dataCode))){
			return Long.parseLong(this.data.get(dataCode));
		}
		return 0;
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.data == null || this.data.size() < 1){
			return false;
		}
		if(this.data.get(dataCode) != null && this.data.get(dataCode).trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}


	public int getShareConfigId() {
		return shareConfigId ;
	}

	public void setShareConfigId(int shareConfigId) {
		this.shareConfigId = shareConfigId;
	}


	public int getObjectId() {
		return objectId ;
	}



	public void setObjectId(int toAccount) {
		this.objectId = toAccount;
	}



	public float getSharePercent() {
		return sharePercent;
	}



	public void setSharePercent(float sharePercent) {
		this.sharePercent = sharePercent;
	}



	public long getShareUuid() {
		return shareUuid ;
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


	public Float getBeginMoney() {
		return beginMoney;
	}

	public void setBeginMoney(Float beginMoney) {
		this.beginMoney = beginMoney;
	}

	public Float getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(Float endMoney) {
		this.endMoney = endMoney;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public String getObjectIdName() {
		return objectIdName;
	}

	public void setObjectIdName(String objectIdName) {
		this.objectIdName = objectIdName;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}


	public boolean isDefaultConfig() {
		return defaultConfig;
	}

	public void setDefaultConfig(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
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

	public String getClearWay() {
		return clearWay;
	}

	public void setClearWay(String clearWay) {
		this.clearWay = clearWay;
	}

	public String getClearType() {
		return clearType;
	}

	public void setClearType(String clearType) {
		this.clearType = clearType;
	}

	public String getClearStartDate() {
		return clearStartDate;
	}

	public void setClearStartDate(String clearStartDate) {
		this.clearStartDate = clearStartDate;
	}

	public int getOffsetClearTimes() {
		return offsetClearTimes;
	}

	public void setOffsetClearTimes(int offsetClearTimes) {
		this.offsetClearTimes = offsetClearTimes;
	}

	@Override
	public ShareConfig clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (ShareConfig)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getEnterTimePolicy() {
		return enterTimePolicy;
	}

	public void setEnterTimePolicy(String enterTimePolicy) {
		this.enterTimePolicy = enterTimePolicy;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}


	public String getPayCardType() {
		return payCardType;
	}


	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}

	public String getStatementAccount() {
		return statementAccount;
	}

	public void setStatementAccount(String statementAccount) {
		this.statementAccount = statementAccount;
	}

	@Override
	public String toString() {
		return "ShareConfig{" +
				"shareConfigId=" + shareConfigId +
				", shareConfigCode='" + shareConfigCode + '\'' +
				", shareConfigName='" + shareConfigName + '\'' +
				", objectId=" + objectId +
				", sharePercent=" + sharePercent +
				", moneyDirect='" + moneyDirect + '\'' +
				", chargeType='" + chargeType + '\'' +
				", beginMoney=" + beginMoney +
				", endMoney=" + endMoney +
				", shareUuid=" + shareUuid +
				", shareType='" + shareType + '\'' +
				", payCardType='" + payCardType + '\'' +
				", ttl=" + ttl +
				", maxRetry=" + maxRetry +
				", data=" + data +
				", weight=" + weight +
				", defaultConfig=" + defaultConfig +
				", enterTimePolicy='" + enterTimePolicy + '\'' +
				", objectIdName='" + objectIdName + '\'' +
				", clearWay='" + clearWay + '\'' +
				", clearType='" + clearType + '\'' +
				", clearStartDate='" + clearStartDate + '\'' +
				", offsetClearTimes=" + offsetClearTimes +
				", statementAccount=" + statementAccount +
				'}';
	}
}

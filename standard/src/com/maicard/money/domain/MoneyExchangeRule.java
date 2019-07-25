package com.maicard.money.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.EisObject;


//货币之间的兑换规则


@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyExchangeRule extends EisObject implements Cloneable{


	private static final long serialVersionUID = 93634170682701563L;

	private long moneyExchangeRuleId;
	
	private long uuid;

	private String sourceMoneyType;

	private String destMoneyType;

	private float rate;
	
    private String memo;
    
    private float amount;
    
    private long headerUuid;			//该规则属于哪个
   
    
	public MoneyExchangeRule() {
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MoneyExchangeRule other = (MoneyExchangeRule) obj;
		if (moneyExchangeRuleId != other.moneyExchangeRuleId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"moneyExchangeId=" + moneyExchangeRuleId + 
				",uuid=" +  uuid + 
				",sourceMoneyType=" + sourceMoneyType +
				",destMoneyType=" + destMoneyType +
				")";
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public MoneyExchangeRule clone() {
		try{
			return (MoneyExchangeRule)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public String getSourceMoneyType() {
		return sourceMoneyType;
	}

	public void setSourceMoneyType(String sourceMoneyType) {
		this.sourceMoneyType = sourceMoneyType;
	}

	public String getDestMoneyType() {
		return destMoneyType;
	}

	public void setDestMoneyType(String destMoneyType) {
		this.destMoneyType = destMoneyType;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public long getMoneyExchangeRuleId() {
		return moneyExchangeRuleId;
	}

	public void setMoneyExchangeRuleId(long moneyExchangeRuleId) {
		this.moneyExchangeRuleId = moneyExchangeRuleId;
	}

	public long getHeaderUuid() {
		return headerUuid;
	}

	public void setHeaderUuid(long headerUuid) {
		this.headerUuid = headerUuid;
	}
}

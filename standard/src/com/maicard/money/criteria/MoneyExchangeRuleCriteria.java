package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class MoneyExchangeRuleCriteria extends Criteria {

	private static final long serialVersionUID = -3168588436562128552L;

	private long uuid;

	private String sourceMoneyType;

	private String destMoneyType;

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
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
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"uuid=" +  uuid + 
				",sourceMoneyType=" + sourceMoneyType +
				",destMoneyType=" + destMoneyType +
				")";
	}
}

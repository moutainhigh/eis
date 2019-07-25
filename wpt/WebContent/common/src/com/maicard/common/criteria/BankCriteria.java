package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class BankCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int bankId;

	public BankCriteria() {
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

}

package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class WithdrawMethodCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int withdrawMethodId;
	
	private int withdrawTypeId;
	
	
	/**
	 * 对应的系统虚拟账户
	 */
	private long referUuid;
	
	public WithdrawMethodCriteria() {
	}
	
	public WithdrawMethodCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getWithdrawMethodId() {
		return withdrawMethodId;
	}

	public void setWithdrawMethodId(int withdrawMethodId) {
		this.withdrawMethodId = withdrawMethodId;
	}

	public int getWithdrawTypeId() {
		return withdrawTypeId;
	}

	public void setWithdrawTypeId(int withdrawTypeId) {
		this.withdrawTypeId = withdrawTypeId;
	}

	public long getReferUuid() {
		return referUuid;
	}

	public void setReferUuid(long referUuid) {
		this.referUuid = referUuid;
	}


}

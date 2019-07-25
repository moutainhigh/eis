package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class WithdrawTypeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int withdrawTypeId;
	
	private String withdrawTypeName;

	private String memberNo;

	private String tradeType;

	
	public WithdrawTypeCriteria() {
	}

	public int getWithdrawTypeId() {
		return withdrawTypeId;
	}

	public void setWithdrawTypeId(int withdrawTypeId) {
		this.withdrawTypeId = withdrawTypeId;
	}

	public String getWithdrawTypeName() {
		return withdrawTypeName;
	}

	public void setWithdrawTypeName(String withdrawTypeName) {
		this.withdrawTypeName = withdrawTypeName;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
}

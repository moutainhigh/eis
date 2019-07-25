package com.maicard.product.domain;


import com.maicard.common.domain.EisObject;
import com.maicard.product.domain.TransPlan;

public class TransPlan extends EisObject {

	private static final long serialVersionUID = 1L;

	private int transPlanId;

	private String transPlanName;

	private String transPlanDesc;

	private String processClass; //实现该交易方式的类名


	public TransPlan() {
	}

	public int getTransPlanId() {
		return transPlanId;
	}

	public void setTransPlanId(int transPlanId) {
		this.transPlanId = transPlanId;
	}

	public String getTransPlanName() {
		return transPlanName;
	}

	public void setTransPlanName(String transPlanName) {
		this.transPlanName = transPlanName;
	}

	public String getTransPlanDesc() {
		return transPlanDesc;
	}

	public void setTransPlanDesc(String transPlanDesc) {
		this.transPlanDesc = transPlanDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + transPlanId;

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TransPlan other = (TransPlan) obj;
		if (transPlanId != other.transPlanId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"transPlanId=" + "'" + transPlanId + "'" + 
			")";
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}


	
}

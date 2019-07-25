package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.IpPolicyCriteria;
import com.maicard.common.domain.IpPolicy;

public interface IpPolicyService {

	int insert(IpPolicy ipPolicy);
	
	int update(IpPolicy ipPolicy);

	int delete(int ipPolicyId);
	
	IpPolicy select(int ipPolicyId);

	List<IpPolicy> list(IpPolicyCriteria ipPolicyCriteria);

	List<IpPolicy> listOnPage(IpPolicyCriteria ipPolicyCriteria);
	
	int count(IpPolicyCriteria ipPolicyCriteria);

	boolean isForbidden(IpPolicyCriteria ipPolicyCriteria);
	
}

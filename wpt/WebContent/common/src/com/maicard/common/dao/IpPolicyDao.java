package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.IpPolicyCriteria;
import com.maicard.common.domain.IpPolicy;

public interface IpPolicyDao {

	int insert(IpPolicy ipPolicy) throws DataAccessException;
	void updateWrong(IpPolicy ipPolicy) throws DataAccessException;

	int update(IpPolicy ipPolicy) throws DataAccessException;

	int delete(int ipPolicyId) throws DataAccessException;

	IpPolicy select(int ipPolicyId) throws DataAccessException;

	List<IpPolicy> list(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException;
	
	List<IpPolicy> listOnPage(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException;
	
	int count(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException;
	List<Integer> listPk(IpPolicyCriteria ipPolicyCriteria);
	List<Integer> listPkOnPage(IpPolicyCriteria ipPolicyCriteria);

}

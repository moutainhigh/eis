package com.maicard.common.dao;

import java.util.List;

import com.maicard.common.criteria.QUserInfoCriteria;
import com.maicard.common.domain.QUserInfo;



public interface QUserInfoDao {

	int insert(QUserInfo qUserInfo) throws Exception;

	int update(QUserInfo qUserInfo) throws Exception;

	List<QUserInfo> list(QUserInfoCriteria qUserInfoCriteria) throws Exception;
	
	List<QUserInfo> listOnPage(QUserInfoCriteria qUserInfoCriteria) throws Exception;
	
	int count(QUserInfoCriteria qUserInfoCriteria) throws Exception;

	


}

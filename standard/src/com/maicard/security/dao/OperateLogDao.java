package com.maicard.security.dao;

import java.util.List;

import com.maicard.security.criteria.OperateLogCriteria;
import com.maicard.security.domain.OperateLog;



public interface OperateLogDao {

	int insert(OperateLog operateLog) throws Exception;

	int update(OperateLog operateLog) throws Exception;

	List<OperateLog> list(OperateLogCriteria operateLogCriteria) throws Exception;
	
	List<OperateLog> listOnPage(OperateLogCriteria operateLogCriteria) throws Exception;
	
	int count(OperateLogCriteria operateLogCriteria) throws Exception;

	int getFrequentObjectId(
			OperateLogCriteria operateLogCriteria) throws Exception;

	int getRecentObjectId(
			OperateLogCriteria operateLogCriteria) throws Exception;

	int clearOldLog(OperateLogCriteria operateLogCriteria);

	long findPeriod(OperateLogCriteria operateLogCriteria);

}

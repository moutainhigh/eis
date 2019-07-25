package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.OperateLogCriteria;
import com.maicard.security.domain.OperateLog;

public interface OperateLogService {

	int insert(OperateLog operateLog);

	int update(OperateLog operateLog) throws Exception;

	List<OperateLog> list(OperateLogCriteria operateLogCriteria);

	List<OperateLog> listOnPage(OperateLogCriteria operateLogCriteria) throws Exception;

	int count(OperateLogCriteria operateLogCriteria);

	int getFrequentObjectId(
			OperateLogCriteria operateLogCriteria) throws Exception;

	int getRecentObjectId(
			OperateLogCriteria operateLogCriteria) throws Exception;

	
	int insertLocal(OperateLog operateLog) throws Exception;

	void deleteByCriteria(OperateLogCriteria operateLogCriteria);

	long findPeriod(OperateLogCriteria operateLogCriteria);

	OperateLog select(int tagId);

	void cleanOldLog();

}

package com.maicard.flow.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.flow.criteria.WorkflowInstanceLogCriteria;
import com.maicard.flow.dao.WorkflowInstanceLogDao;
import com.maicard.flow.domain.WorkflowInstanceLog;
import com.maicard.flow.service.WorkflowInstanceLogService;

@Service
public class WorkflowInstanceLogServiceImpl extends BaseService implements WorkflowInstanceLogService {

	@Resource
	private WorkflowInstanceLogDao workflowInstanceLogDao;

	
	public void insert(WorkflowInstanceLog workflowInstanceLog) {
		workflowInstanceLogDao.insert(workflowInstanceLog);
	}

	public int update(WorkflowInstanceLog workflowInstanceLog) {
		int actualRowsAffected = 0;
		
		int workflowInstanceLogId = workflowInstanceLog.getWorkflowInstanceLogId();

		WorkflowInstanceLog _oldWorkflowInstanceLog = workflowInstanceLogDao.select(workflowInstanceLogId);
		
		if (_oldWorkflowInstanceLog != null) {
			actualRowsAffected = workflowInstanceLogDao.update(workflowInstanceLog);
		}
		
		return actualRowsAffected;
	}

	public int delete(int workflowInstanceLogId) {
		int actualRowsAffected = 0;
		
		WorkflowInstanceLog _oldWorkflowInstanceLog = workflowInstanceLogDao.select(workflowInstanceLogId);
		
		if (_oldWorkflowInstanceLog != null) {
			actualRowsAffected = workflowInstanceLogDao.delete(workflowInstanceLogId);
		}
		
		return actualRowsAffected;
	}
	
	public WorkflowInstanceLog select(int workflowInstanceLogId) {
		return workflowInstanceLogDao.select(workflowInstanceLogId);
	}

	public List<WorkflowInstanceLog> list(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) {
		return workflowInstanceLogDao.list(workflowInstanceLogCriteria);
	}
	
	public List<WorkflowInstanceLog> listOnPage(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) {
		return workflowInstanceLogDao.listOnPage(workflowInstanceLogCriteria);
	}

}

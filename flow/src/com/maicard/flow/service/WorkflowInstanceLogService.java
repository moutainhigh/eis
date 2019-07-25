package com.maicard.flow.service;

import java.util.List;

import com.maicard.flow.criteria.WorkflowInstanceLogCriteria;
import com.maicard.flow.domain.WorkflowInstanceLog;

public interface WorkflowInstanceLogService {

	void insert(WorkflowInstanceLog workflowInstanceLog);

	int update(WorkflowInstanceLog workflowInstanceLog);

	int delete(int workflowInstanceLogId);
	
	WorkflowInstanceLog select(int workflowInstanceLogId);

	List<WorkflowInstanceLog> list(WorkflowInstanceLogCriteria workflowInstanceLogCriteria);

	List<WorkflowInstanceLog> listOnPage(WorkflowInstanceLogCriteria workflowInstanceLogCriteria);

}

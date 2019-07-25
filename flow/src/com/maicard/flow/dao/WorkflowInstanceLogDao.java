package com.maicard.flow.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.flow.criteria.WorkflowInstanceLogCriteria;
import com.maicard.flow.domain.WorkflowInstanceLog;

public interface WorkflowInstanceLogDao {

	void insert(WorkflowInstanceLog workflowInstanceLog) throws DataAccessException;

	int update(WorkflowInstanceLog workflowInstanceLog) throws DataAccessException;

	int delete(int workflowInstanceLogId) throws DataAccessException;

	WorkflowInstanceLog select(int workflowInstanceLogId) throws DataAccessException;

	List<WorkflowInstanceLog> list(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) throws DataAccessException;
	
	List<WorkflowInstanceLog> listOnPage(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) throws DataAccessException;
	
	int count(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) throws DataAccessException;

}

package com.maicard.flow.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.domain.WorkflowInstance;

public interface WorkflowInstanceDao {

	int insert(WorkflowInstance workflowInstance) throws DataAccessException;

	int update(WorkflowInstance workflowInstance) throws DataAccessException;

	int delete(int workflowInstanceId) throws DataAccessException;

	WorkflowInstance select(int workflowInstanceId) throws DataAccessException;

	List<WorkflowInstance> list(WorkflowInstanceCriteria workflowInstanceCriteria) throws DataAccessException;
	
	List<WorkflowInstance> listOnPage(WorkflowInstanceCriteria workflowInstanceCriteria) throws DataAccessException;
	
	int count(WorkflowInstanceCriteria workflowInstanceCriteria) throws DataAccessException;

}

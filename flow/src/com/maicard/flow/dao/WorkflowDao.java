package com.maicard.flow.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.flow.criteria.WorkflowCriteria;
import com.maicard.flow.domain.Workflow;

public interface WorkflowDao {

	int insert(Workflow workflow) throws DataAccessException;

	int update(Workflow workflow) throws DataAccessException;

	int delete(int workflowId) throws DataAccessException;

	Workflow select(int workflowId) throws DataAccessException;

	List<Workflow> list(WorkflowCriteria workflowCriteria) throws DataAccessException;
	
	List<Workflow> listOnPage(WorkflowCriteria workflowCriteria) throws DataAccessException;
	
	int count(WorkflowCriteria workflowCriteria) throws DataAccessException;

}

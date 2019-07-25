package com.maicard.flow.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.flow.criteria.WorkflowPrivilegeCriteria;
import com.maicard.flow.domain.WorkflowPrivilege;

public interface WorkflowPrivilegeDao {

	int insert(WorkflowPrivilege workflowPrivilege) throws DataAccessException;

	int update(WorkflowPrivilege workflowPrivilege) throws DataAccessException;

	int delete(long workflowPrivilegeId) throws DataAccessException;

	WorkflowPrivilege select(long workflowPrivilegeId) throws DataAccessException;

	List<WorkflowPrivilege> list(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) throws DataAccessException;
	
	List<WorkflowPrivilege> listOnPage(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) throws DataAccessException;
	
	int count(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) throws DataAccessException;

}

package com.maicard.flow.service;

import java.util.List;

import com.maicard.flow.criteria.WorkflowPrivilegeCriteria;
import com.maicard.flow.domain.WorkflowPrivilege;

public interface WorkflowPrivilegeService {

	int insert(WorkflowPrivilege workflowPrivilege);

	int update(WorkflowPrivilege workflowPrivilege);

	int delete(int workflowPrivilegeId);
	
	WorkflowPrivilege select(int workflowPrivilegeId);

	List<WorkflowPrivilege> list(WorkflowPrivilegeCriteria workflowPrivilegeCriteria);

	List<WorkflowPrivilege> listOnPage(WorkflowPrivilegeCriteria workflowPrivilegeCriteria);

	int count(WorkflowPrivilegeCriteria workflowPrivilegeCriteria);

	
	

}

package com.maicard.flow.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.flow.criteria.WorkflowPrivilegeCriteria;
import com.maicard.flow.dao.WorkflowPrivilegeDao;
import com.maicard.flow.domain.WorkflowPrivilege;
import com.maicard.flow.service.RouteService;
import com.maicard.flow.service.WorkflowPrivilegeService;

@Service
public class WorkflowPrivilegeServiceImpl extends BaseService implements WorkflowPrivilegeService {

	

	@Resource
	private WorkflowPrivilegeDao workflowPrivilegeDao;

	@Resource RouteService routeService;

	HashMap<String,String[]> requireWorkflowPrivilegeConfigMap = new HashMap<String,String[]>();



	public int insert(WorkflowPrivilege workflowPrivilege) {
		return workflowPrivilegeDao.insert(workflowPrivilege);
	}

	public int update(WorkflowPrivilege workflowPrivilege) {
		int actualRowsAffected = 0;

		long workflowPrivilegeId = workflowPrivilege.getWorkflowPrivilegeId();

		WorkflowPrivilege _oldWorkflowPrivilege = workflowPrivilegeDao.select(workflowPrivilegeId);

		if (_oldWorkflowPrivilege != null) {
			actualRowsAffected = workflowPrivilegeDao.update(workflowPrivilege);
		}

		return actualRowsAffected;
	}

	public int delete(int workflowPrivilegeId) {
		int actualRowsAffected = 0;

		WorkflowPrivilege _oldWorkflowPrivilege = workflowPrivilegeDao.select(workflowPrivilegeId);

		if (_oldWorkflowPrivilege != null) {
			actualRowsAffected = workflowPrivilegeDao.delete(workflowPrivilegeId);
		}

		return actualRowsAffected;
	}

	public WorkflowPrivilege select(int workflowPrivilegeId) {
		WorkflowPrivilege workflowPrivilege =  workflowPrivilegeDao.select(workflowPrivilegeId);
		return workflowPrivilege;
	}

	public List<WorkflowPrivilege> list(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) {		
		List<WorkflowPrivilege> workflowPrivilegeList = workflowPrivilegeDao.list(workflowPrivilegeCriteria);
		
		return workflowPrivilegeList;
	}

	public List<WorkflowPrivilege> listOnPage(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) {
		List<WorkflowPrivilege> workflowPrivilegeList = workflowPrivilegeDao.listOnPage(workflowPrivilegeCriteria);
		
		return workflowPrivilegeList;
	}

	@Override
	public int count(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) {
		return workflowPrivilegeDao.count(workflowPrivilegeCriteria);
	}

	

}

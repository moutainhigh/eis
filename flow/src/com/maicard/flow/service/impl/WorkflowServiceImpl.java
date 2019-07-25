package com.maicard.flow.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.JsonUtils;
import com.maicard.flow.criteria.RouteCriteria;
import com.maicard.flow.criteria.WorkflowCriteria;
import com.maicard.flow.dao.WorkflowDao;
import com.maicard.flow.domain.Workflow;
import com.maicard.flow.service.RouteService;
import com.maicard.flow.service.WorkflowService;
import com.maicard.standard.DataName;

@Service
public class WorkflowServiceImpl extends BaseService implements WorkflowService {

	@Resource
	private ConfigService configService;

	@Resource
	private WorkflowDao workflowDao;

	@Resource RouteService routeService;

	//以JSON形式保存在配置中，例如：{"product":["create","update"],"document":["create","update"]}
	HashMap<String,String[]> requireWorkflowConfigMap = new HashMap<String,String[]>();


	@PostConstruct
	public void init(){
		

	}


	public int insert(Workflow workflow) {
		return workflowDao.insert(workflow);
	}

	public int update(Workflow workflow) {
		int actualRowsAffected = 0;

		int workflowId = workflow.getWorkflowId();

		Workflow _oldWorkflow = workflowDao.select(workflowId);

		if (_oldWorkflow != null) {
			actualRowsAffected = workflowDao.update(workflow);
		}

		return actualRowsAffected;
	}

	public int delete(int workflowId) {
		int actualRowsAffected = 0;

		Workflow _oldWorkflow = workflowDao.select(workflowId);

		if (_oldWorkflow != null) {
			actualRowsAffected = workflowDao.delete(workflowId);
		}

		return actualRowsAffected;
	}

	public Workflow select(int workflowId) {
		Workflow workflow =  workflowDao.select(workflowId);
		afterFetch(workflow);
		return workflow;
	}

	public List<Workflow> list(WorkflowCriteria workflowCriteria) {		
		List<Workflow> workflowList = workflowDao.list(workflowCriteria);
		for(Workflow workflow : workflowList){
			afterFetch(workflow);
		}
		return workflowList;
	}

	public List<Workflow> listOnPage(WorkflowCriteria workflowCriteria) {
		List<Workflow> workflowList = workflowDao.listOnPage(workflowCriteria);
		for(Workflow workflow : workflowList){
			afterFetch(workflow);
		}
		return workflowList;
	}

	@Override
	public int count(WorkflowCriteria workflowCriteria) {
		return workflowDao.count(workflowCriteria);
	}

	private void afterFetch(Workflow workflow){
		if(workflow == null){
			return;
		}
		RouteCriteria  routeCriteria = new RouteCriteria();
		routeCriteria.setWorkflowId(workflow.getWorkflowId());
		workflow.setRouteList(routeService.list(routeCriteria));
	}

	@Override
	public boolean requiredWorkflow(String objectType, String action, long ownerId) {
		String requireWorkflowConfig = configService.getValue(DataName.requireWorkflowObject.toString(), ownerId );
		if(requireWorkflowConfig != null){
			ObjectMapper om = JsonUtils.getInstance();
			JavaType javaType = om.getTypeFactory().constructMapType(HashMap.class, String.class, String[].class);   

			try{
				requireWorkflowConfigMap = om.readValue(requireWorkflowConfig, javaType);
			}catch(Exception e){
				logger.error("系统配置的requireWorkflowObject异常:" + ExceptionUtils.getFullStackTrace(e));
			}
		}
		if(requireWorkflowConfigMap == null || requireWorkflowConfigMap.size() < 1){
			logger.warn("系统未定义任何需要工作流处理的对象及操作");
			return false;
		}
		for(String ot : requireWorkflowConfigMap.keySet()){
			if(ot.equalsIgnoreCase(objectType)){
				for(String ac : requireWorkflowConfigMap.get(ot)){
					if(ac.equalsIgnoreCase(action)){
						logger.debug("针对对象[" + objectType + "]进行[" + action + "]操作需要工作流参与");
						return true;
					}
				}
			}
		}
		logger.debug("针对对象[" + objectType + "]进行[" + action + "]操作不需要工作流参与");
		return false;
	}


}

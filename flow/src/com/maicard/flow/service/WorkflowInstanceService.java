package com.maicard.flow.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisObject;
import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.domain.WorkflowInstance;
import com.maicard.security.domain.User;

public interface WorkflowInstanceService {

	int insert(WorkflowInstance workflowInstance);

	int update(WorkflowInstance workflowInstance);

	int delete(int workflowInstanceId);
	
	WorkflowInstance select(int workflowInstanceId);

	List<WorkflowInstance> list(WorkflowInstanceCriteria workflowInstanceCriteria);

	List<WorkflowInstance> listOnPage(WorkflowInstanceCriteria workflowInstanceCriteria);

	WorkflowInstance getInstance(
			WorkflowInstanceCriteria workflowInstanceCriteria);

	/**
	 * 根据工作流实例，确定下一步工作步骤<br/>
	 * 并且根据下一步工作步骤的配置来确定如何更新对象
	 * 
	 * @param targetObject 将要新增或更新的对象
	 * @param oldObject		更新之前的旧对象，用于在严格模式下做属性比较
	 * @param request	请求数据
	 * @return				返回WorkflowInstance或者是，EisError或OperateResult的EisMessage
	 * @throws Exception
	 */
	Object executeWorkflow(EisObject targetObject, EisObject oldObject, HttpServletRequest request, String action,
			long ownerId) throws Exception;


	/**
	 * 根据工作流实例，列出所有可能的下一步操作的状态
	 * @param workflowInstance
	 * @return
	 */
	//HashMap<Integer, String> getValidCurrentStatus(WorkflowInstance workflowInstance);


	Map<String, Attribute> getValidInputAttribute(Object targetObject, WorkflowInstance workflowInstance,
			Map<String, DataDefine> generalDataDefineMap);

	/**
	 * 结束当前操作
	 * @param workflowInstance
	 */
	int closeCurrentStep(EisObject targetObject, WorkflowInstance workflowInstance);

	boolean canAccess(WorkflowInstance workflowInstance, User user);

	
	

}

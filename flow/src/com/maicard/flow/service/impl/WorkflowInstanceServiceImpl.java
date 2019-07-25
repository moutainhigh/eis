package com.maicard.flow.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.EisObject;
import com.maicard.common.util.ClassUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.flow.criteria.RouteCriteria;
import com.maicard.flow.criteria.WorkflowCriteria;
import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.criteria.WorkflowPrivilegeCriteria;
import com.maicard.flow.dao.WorkflowInstanceDao;
import com.maicard.flow.domain.Route;
import com.maicard.flow.domain.StepCondition;
import com.maicard.flow.domain.Workflow;
import com.maicard.flow.domain.WorkflowInstance;
import com.maicard.flow.service.RouteService;
import com.maicard.flow.service.WorkflowInstanceService;
import com.maicard.flow.service.WorkflowPrivilegeService;
import com.maicard.flow.service.WorkflowService;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.views.JsonFilterView;

@Service
public class WorkflowInstanceServiceImpl extends BaseService implements WorkflowInstanceService {

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private RouteService routeService;
	@Resource
	private WorkflowService workflowService;
	@Resource
	private WorkflowPrivilegeService workflowPrivilegeService;



	public int insert(WorkflowInstance workflowInstance) {
		return workflowInstanceDao.insert(workflowInstance);
	}

	public int update(WorkflowInstance workflowInstance) {
		int actualRowsAffected = 0;

		int workflowInstanceId = workflowInstance.getWorkflowInstanceId();

		WorkflowInstance _oldWorkflowInstance = workflowInstanceDao.select(workflowInstanceId);

		if (_oldWorkflowInstance != null) {
			actualRowsAffected = workflowInstanceDao.update(workflowInstance);
		}

		return actualRowsAffected;
	}

	public int delete(int workflowInstanceId) {
		int actualRowsAffected = 0;

		WorkflowInstance _oldWorkflowInstance = workflowInstanceDao.select(workflowInstanceId);

		if (_oldWorkflowInstance != null) {
			actualRowsAffected = workflowInstanceDao.delete(workflowInstanceId);
		}

		return actualRowsAffected;
	}

	public WorkflowInstance select(int workflowInstanceId) {
		WorkflowInstance workflowInstance = workflowInstanceDao.select(workflowInstanceId);
		if(workflowInstance == null){
			return null;
		}
		afterFetch(workflowInstance, null);
		return workflowInstance;
	}

	public List<WorkflowInstance> list(WorkflowInstanceCriteria workflowInstanceCriteria) {
		List<WorkflowInstance> workflowInstanceList = workflowInstanceDao.list(workflowInstanceCriteria);
		if(workflowInstanceList == null || workflowInstanceList.size() < 1){
			return null;
		}
		for(WorkflowInstance workflowInstance : workflowInstanceList){
			afterFetch(workflowInstance, null);
		}
		return workflowInstanceList;
	}

	public List<WorkflowInstance> listOnPage(WorkflowInstanceCriteria workflowInstanceCriteria) {
		return workflowInstanceDao.listOnPage(workflowInstanceCriteria);
	}

	@Override
	public WorkflowInstance getInstance(WorkflowInstanceCriteria  workflowInstanceCriteria){
		Assert.notNull(workflowInstanceCriteria,"获取工作流实例条件不能为空");
		Assert.isTrue(workflowInstanceCriteria.getOwnerId() > 0,"获取工作流实例条件中的ownerId不能为0");
		Assert.notNull(workflowInstanceCriteria.getTargetObjectType(),"获取工作流实例条件中的对象类型不能为空");
		Assert.notNull(workflowInstanceCriteria.getOperateCode(),"获取工作流实例条件中的操作不能为空");
		Assert.notNull(workflowInstanceCriteria.getUser(),"获取工作流实例条件中的对象代码不能为空");

		//确定该对象的操作是否需要工作流
		boolean requireWorkflow = workflowService.requiredWorkflow(workflowInstanceCriteria.getTargetObjectType(), workflowInstanceCriteria.getOperateCode(), workflowInstanceCriteria.getOwnerId());
		if(!requireWorkflow){
			logger.debug("对对象["	+ workflowInstanceCriteria.getTargetObjectType() + ",]进行[" + workflowInstanceCriteria.getOperateCode() + "]操作，不需要要工作流参与，直接返回");
			return null;
		}

		if(workflowInstanceCriteria.getObjectId() > 0){
			logger.debug("查找是否已存在实例[对象类型:" + workflowInstanceCriteria.getTargetObjectType() + ",对象ID:" + workflowInstanceCriteria.getObjectId() + ",ownerId=" + workflowInstanceCriteria.getOwnerId());
			List<WorkflowInstance> workflowInstanceList = workflowInstanceDao.list(workflowInstanceCriteria);
			if(workflowInstanceList != null && workflowInstanceList.size() > 0){
				logger.debug("查找到已存在实例" + workflowInstanceList.size() + "个");
				WorkflowInstance workflowInstance = workflowInstanceList.get(0);
				afterFetch(workflowInstance,workflowInstanceCriteria);
				logger.info("返回已存在的工作流实例[id=" + workflowInstance.getWorkflowInstanceId() + ", 工作流ID=" + workflowInstance.getWorkflowId() + ", 对象代码:" + workflowInstance.getTargetObjectType() + ", 对象ID:" + workflowInstance.getObjectId() + "].");
				return workflowInstance;

			} else {
				logger.error("找不到已存在实例[对象类型:" + workflowInstanceCriteria.getTargetObjectType() + ",对象ID:" + workflowInstanceCriteria.getObjectId() + ",ownerId=" + workflowInstanceCriteria.getOwnerId());
				throw new RequiredObjectIsNullException("找不到存在的工作流实例");
			}
		}
		//objectId = 0，尝试根据objectType创建新实例
		WorkflowCriteria workflowCriteria = new WorkflowCriteria(workflowInstanceCriteria.getOwnerId());
		workflowCriteria.setTargetObjectType(workflowInstanceCriteria.getTargetObjectType());
		workflowCriteria.setOperateCode(workflowInstanceCriteria.getOperateCode());
		workflowCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Workflow> workflowList = workflowService.list(workflowCriteria);
		if(workflowList == null || workflowList.size() < 1){
			logger.debug("找不到匹配的工作流[对象代码:" + workflowCriteria.getTargetObjectType() + ",操作代码:" + workflowCriteria.getOperateCode() + ",状态:" + BasicStatus.normal.getId() + ",ownerId=" + workflowInstanceCriteria.getOwnerId() + "].");
			return null;
		}

		//生成新的实例
		WorkflowInstance workflowInstance = new WorkflowInstance(workflowInstanceCriteria.getOwnerId());
		workflowInstance.setWorkflowId(workflowList.get(0).getWorkflowId());
		workflowInstance.setRouteList(workflowList.get(0).getRouteList());
		workflowInstance.setCurrentStatus(TransactionStatus.inProcess.getId());
		workflowInstance.setStartTime(new Date());
		workflowInstance.setTargetObjectType(workflowInstanceCriteria.getTargetObjectType());
		workflowInstance.setObjectId(workflowInstanceCriteria.getObjectId());
		findAdjacentRoute(workflowInstance);
		int id = insert(workflowInstance);
		if(id == 1){
			logger.info("创建了新的工作流实例[id=" + workflowInstance + ", 工作流ID=" + workflowInstance.getWorkflowId() + ", 对象代码:" + workflowInstance.getTargetObjectType() + ", 对象ID:" + workflowInstance.getObjectId() + ",当前步骤:" + workflowInstance.getCurrentStep() + "].");
			afterFetch(workflowInstance, workflowInstanceCriteria);
			return workflowInstance;
		}
		logger.error("无法创建新的工作流实例");
		throw new RequiredObjectIsNullException("无法创建新的工作流实例");

	}

	private void afterFetch(WorkflowInstance workflowInstance, WorkflowInstanceCriteria workflowInstanceCriteria){
		if(workflowInstance == null){
			logger.debug("工作流实例为空");
			return;
		}
		if(workflowInstance.getRouteList() == null){
			RouteCriteria routeCriteria = new RouteCriteria(workflowInstance.getOwnerId());
			routeCriteria.setWorkflowId(workflowInstance.getWorkflowId());
			routeCriteria.setCurrentStatus(BasicStatus.normal.getId());
			List<Route> routeList = routeService.list(routeCriteria);
			if(routeList == null){
				logger.debug("工作流[" + routeCriteria.getWorkflowId() + "]的步骤列表为空");
			}
			workflowInstance.setRouteList(routeList);
		}
		if(workflowInstance.getRouteList() != null){
			//找出上一步和下一步工作步骤
			findAdjacentRoute(workflowInstance);
		}
		if(workflowInstanceCriteria == null){
			return;
		}
		if(workflowInstance.getPreviewsRoute() != null){
			if(!routeService.havePrivilege(workflowInstanceCriteria.getUser(), workflowInstanceCriteria.getObjectId(), workflowInstance.getPreviewsRoute())){
				logger.debug("用户[" + workflowInstanceCriteria.getUser().getUuid() + "]无权访问上一步工作步骤[" + workflowInstance.getPreviewsRoute().getRouteId() + "/" + workflowInstance.getPreviewsRoute().getRouteName() + "].");	
				workflowInstance.setPreviewsRoute(null);
			} else {
				logger.debug("用户[" + workflowInstanceCriteria.getUser().getUuid() + "]可以访问上一步工作步骤[" + workflowInstance.getPreviewsRoute().getRouteId() + "/" + workflowInstance.getPreviewsRoute().getRouteName() + "].");
			}
		}
		if(workflowInstance.getNextRouteList() != null){
			for(Route route : workflowInstance.getNextRouteList()){
				if(!routeService.havePrivilege(workflowInstanceCriteria.getUser(), workflowInstanceCriteria.getObjectId(), route)){
					logger.debug("用户[" + workflowInstanceCriteria.getUser().getUuid() + "]无权访问下一步工作步骤[" + route.getRouteId() + "/" + route.getRouteName() + "].");	
					workflowInstance.setPreviewsRoute(null);
				} else {
					logger.debug("用户[" + workflowInstanceCriteria.getUser().getUuid() + "]可以访问下一步工作步骤[" + route.getRouteId() + "/" + route.getRouteName() + "].");
				}
			}
		}

	}



	//找出相邻的工作步骤
	private void findAdjacentRoute(WorkflowInstance workflowInstance){
		if(workflowInstance == null){
			logger.debug("工作流实例为空，无法查找相邻步骤");
			return;
		}
		if(workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1){
			logger.debug("工作流实例的步骤列表为空，无法查找相邻步骤");
			return;
		}
		Route currentRoute = null;
		List<Route> nextRouteList = new ArrayList<Route>();
		if(workflowInstance.getCurrentStep() > 0){
			//直接找出指定的下一step
			for(Route r : workflowInstance.getRouteList()){
				if(r.getStep() == workflowInstance.getCurrentStep() ){
					currentRoute = r;
					break;
				}
			}
			if(currentRoute == null){
				logger.error("找不到当前步骤:" + workflowInstance.getCurrentStep() );
				return;
			}

			logger.debug("查找步骤[名称" + currentRoute.getRouteName() + "/ID" + currentRoute.getRouteId() + "/STEP:" + currentRoute.getStep() + "/" +  workflowInstance.getCurrentStep() + "的下一工作步骤");
			for(Route r : workflowInstance.getRouteList()){
				//logger.debug("比对工作步骤[" + workflowInstance.getRouteList().get(i).getRouteId() + "的步骤ID" + workflowInstance.getRouteList().get(i).getStep() + "]." );
				if(r.getStep() == currentRoute.getNextStep()){
					logger.debug("找到了步骤[名称" + currentRoute.getRouteName() + "/ID" + currentRoute.getRouteId() + "/STEP:" + currentRoute.getStep() + "/" +  workflowInstance.getCurrentStep() + "的下一工作步骤[" + r.getRouteName() + "]");
					nextRouteList.add(r);
				}
			}
		} else {
			//找出最小的工作步骤
			int nextStepId = -1;
			for(int i = 0; i < workflowInstance.getRouteList().size(); i++){
				//logger.debug("比对工作步骤[" + workflowInstance.getRouteList().get(i).getRouteId() + "的步骤ID" + workflowInstance.getRouteList().get(i).getStep() + "]." );
				if(workflowInstance.getRouteList().get(i).getStep() > workflowInstance.getCurrentStep()){
					if(nextStepId == -1){
						nextStepId = i;
					} 
					if(workflowInstance.getRouteList().get(i).getStep() < workflowInstance.getRouteList().get(nextStepId).getStep()){
						nextStepId = i;
					}

				}
			}
			if(nextStepId == -1){
				logger.debug("找不到最小的操作步骤.");
			} else {
				logger.debug("查找下一步操作的ID，结果为STEP=" +  workflowInstance.getRouteList().get(nextStepId).getStep());
				nextStepId = workflowInstance.getRouteList().get(nextStepId).getStep();
				workflowInstance.setCurrentStep(nextStepId);
				if(nextStepId >= 0){
					for(Route route : workflowInstance.getRouteList()){
						if(route.getStep() == nextStepId){					
							logger.debug("找到了step为" + nextStepId + "]的工作步骤[routeId=" + route.getRouteId() + "]，添加到下一步步骤.");
							nextRouteList.add(route);
						}
					}
					workflowInstance.setNextRouteList(nextRouteList);
				}else {
					logger.debug("找不到最小的操作步骤.");
				}
			}
		}
		logger.debug("根据当前步骤[" + workflowInstance.getCurrentStep() + "]找到的下一步工作步骤有[" + nextRouteList.size() + "]个");
		workflowInstance.setNextRouteList(nextRouteList);
	}

	@Override
	public Object executeWorkflow(EisObject targetObject, EisObject oldObject, HttpServletRequest request, String action, long ownerId) throws Exception {
		String objectType = StringUtils.uncapitalize(targetObject.getClass().getSimpleName());

		//确定该对象的操作是否需要工作流
		boolean requireWorkflow = workflowService.requiredWorkflow(objectType, action, ownerId) ;
		if(!requireWorkflow){
			logger.debug("对对象["	+ objectType + ",]进行[" + action + "]操作，不需要要工作流参与，直接返回");
			return null;
		}
		logger.debug("对对象["	+ objectType + ",]进行[" + action + "]操作，需要要工作流参与");

		int workflowInstanceId = ServletRequestUtils.getIntParameter(request, "workflowInstanceId", 0);
		logger.debug("请求中提交的工作流实例ID是 ：" + workflowInstanceId);
		if (workflowInstanceId < 1) {
			logger.error("请求中未提交工作流实例ID");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"请求中没有提交工作流实例ID");
		}
		WorkflowInstance workflowInstance = select(workflowInstanceId);
		if (workflowInstance == null) {
			logger.error("找不到工作流实例:" + workflowInstanceId);
			return new EisMessage(EisError.workflowInstanceNotFound.getId(),"找不到针对对象[" + ObjectType.product.toString() + "]进行[" + Operate.create.getCode() 	+ "]操作的工作流");
		}
		if (workflowInstance.getRouteList() == null
				|| workflowInstance.getRouteList().size() < 1) {
			logger.error("工作流实例[" + workflowInstanceId + "]未定义任何步骤");
			return new EisMessage(	EisError.workflowInstanceNotFound.getId(),"工作流数据异常");
		}
		logger.debug("当前工作流步骤是：" + workflowInstance.getCurrentStep());
		/*if (workflowInstance.getNextRouteList() == null
				|| workflowInstance.getNextRouteList().size() < 1) {
			logger.error("当前工作流[" + workflowInstanceId + "]已没有下一步操作");
			return	new EisMessage(EisError.objectIsNull.getId(),	"当前工作流[" + workflowInstance.getWorkflowInstanceId()	+ "]已没有下一步操作");
		}
		 */

		Route currentRoute =workflowInstance.getCurrentRoute();

		//按照当前步骤指定数据执行操作


		// 更新指定的属性为指定的值


		boolean executed = false;   
		//HashMap<String, Attribute> targetObjectAttributeMap = currentRoute.getTargetObjectAttributeMap();
		/*Set<Entry<String, Attribute>> entrySet = targetObjectAttributeMap.entrySet();
		Iterator<Entry<String, Attribute>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, Attribute> next = iterator.next();
			String key = next.getKey();
			Attribute value = next.getValue();

			logger.debug("KEY : " + key + "    ,  value : " + value);
		}*/

		for (Attribute attribute : currentRoute.getTargetObjectAttributeMap().values()) {			
			String attributeName = 	attribute.getColumnName();
			if(attributeName == null){
				logger.error("工作步骤[" + currentRoute.getRouteId() + "]所指定的属性值有空");
				return new EisMessage(EisError.dataError.getId(),"工作步骤中指定的属性值异常");
			}
			if(attribute.getHidden() > 0){
				continue;
			}
			if(attribute.isReadonly()){
				//使用老对象的数据
				if(oldObject != null){
					String oldValue = ClassUtils.getValue(oldObject, attributeName, attribute.getColumnType());
					executed = ClassUtils.setAttribute(targetObject, attribute.getColumnName(), oldValue, attribute.getColumnType());
					logger.debug("属性[" + attributeName + "]为只读属性，从旧对象中读取属性值[" + oldValue + "]并放入，结果:" + executed);
				} else {
					logger.debug("属性[" + attributeName + "]为只读属性，但旧对象为空");

				}
				continue;
			}
			String inputValue = ServletRequestUtils.getStringParameter(request, attributeName, null);
			if(StringUtils.isBlank(inputValue)){
				logger.debug("在请求中未输入针对属性[" + attributeName + "]的任何值");
				if(attribute.getValidValue() != null && attribute.getValidValue().length  == 1){
					//只有一个可选，忽略用户输入或选择
					inputValue = attribute.getValidValue()[0];
					logger.debug("在请求中未输入针对属性[" + attributeName + "]的任何值，但该属性只有一个可选值，直接使用该值:" + inputValue);
				}
				if(attribute.isRequired()){
					logger.debug("在请求中未输入针对属性[" + attributeName + "]的任何值但该属性值被设置为必须");
					return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"在请求中未输入必须的的属性[" + attributeName + "]值");
				}
				if(StringUtils.isBlank(inputValue)){
					continue;
				}
			}
			String attributeValue = null;
			if(attribute.getValidValue() == null || attribute.getValidValue().length < 1){
				//以用户输入作为数据
				if(attribute.getInputMethod() != null && attribute.getInputMethod().equals("file")){
					logger.debug("在请求中属性[" + attributeName + "]的输入方式是文件，跳过设置");
					continue;
				} else {
					attributeValue = inputValue.trim();
					logger.debug("对属性[" + attributeName + "]设置用户输入的值:" + inputValue);
				}

			} else if(attribute.getValidValue().length  == 1){
				//只有一个可选，忽略用户输入或选择
				attributeValue = attribute.getValidValue()[0];
			} else {
				boolean haveValidInput = false;
				for(String vv : attribute.getValidValue()){
					if(vv.equalsIgnoreCase(inputValue.trim())){
						haveValidInput = true;
						attributeValue = inputValue.trim();
						break;
					}
				}
				if(!haveValidInput){
					logger.debug("在请求中针对属性[" + attributeName + "]的输入不合法[" + inputValue + "]");
					return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"在请求中的输入参数不合法");			
				}
			}
			executed = ClassUtils.setAttribute(targetObject, attribute.getColumnName(), attributeValue, attribute.getColumnType());
			if (!executed) {
				logger.error("系统异常，在执行步骤[" + currentRoute.getRouteId() 	+ "]时，无法设置属性[" + attribute.getColumnName() + "," + attribute.getColumnType() + ",值:" + attributeValue + "]有反射方法未执行");
				throw new Exception("系统异常，在执行步骤[" + currentRoute.getRouteId()	+ "]时有反射方法未执行");
			}
		}	
		return workflowInstance;
	}

	/*@Override
	public HashMap<Integer, String> getValidCurrentStatus(
			WorkflowInstance workflowInstance) {
		if (workflowInstance == null) {
			logger.error("工作流实例为空");
			return null;
		}
		if (workflowInstance.getNextRouteList() == null
				|| workflowInstance.getNextRouteList().size() < 1) {
			logger.error("工作流实例[" + workflowInstance.getWorkflowInstanceId()
			+ "]下一步骤列表为空");
			return null;
		}
		HashMap<Integer, String> validCurrentStatus = new HashMap<Integer, String>();
		for (Route route : workflowInstance.getNextRouteList()) {
			if(route.getTargetObjectAttributeMap() == null){
				logger.warn("工作步骤[" + route.getRouteId() + "]没有配置任何对象属性数据表");
				continue;
			}
			for (String key : route.getTargetObjectAttributeMap().keySet()) {
				if (key.equals("currentStatus")) {
					Attribute attribute = route.getTargetObjectAttributeMap().get(key);
					if(attribute == null){
						continue;
					}
					if(attribute.getValidValue() == null || attribute.getValidValue().length < 1){
						continue;
					}
					for(String value : attribute.getValidValue()){
						if(NumericUtils.isIntNumber(value)){						
							validCurrentStatus.put(route.getRouteId(),value);
						}
					}
				}
			}
		}
		return validCurrentStatus;
	}*/

	@Override
	public Map<String, Attribute> getValidInputAttribute(Object targetObject,
			WorkflowInstance workflowInstance, Map<String,DataDefine> generalDataDefineMap) {
		if (workflowInstance == null) {
			logger.error("工作流实例为空");
			return null;
		}

		//得到当前操作执行的步骤，如果没有执行过任何操作，那么当前步骤是0
		int currentStep = workflowInstance.getCurrentStep();

		if (workflowInstance.getRouteList() == null
				|| workflowInstance.getRouteList().size() < 1) {
			logger.error("工作流实例[" + workflowInstance.getWorkflowInstanceId() + "]的工作步骤列表为空");
			return null;
		}
		Route currentRoute = null;
		if(currentStep == 0){
			currentRoute = workflowInstance.getRouteList().get(0);
		} else {
			for(Route r : workflowInstance.getRouteList()){
				if(r.getStep() == currentStep){
					currentRoute = r;
					break;
				}
			}
		}
		if(currentRoute == null){
			logger.error("找不到工作流实例[" + workflowInstance.getWorkflowInstanceId() + "]的当前步骤或第一个步骤");
			return null;
		}
		boolean isStrict = false;
		if(currentRoute.getTargetObjectCheckMode() != null && currentRoute.getTargetObjectCheckMode().equalsIgnoreCase(DataName.strict.toString())){
			isStrict = true;
		}

		Map<String, Attribute> attributeMap = null;
		if(isStrict){
			logger.debug("当前步骤是严格模式，只获取步骤中的属性值");
			attributeMap = new HashMap<String, Attribute>();
		} else {
			logger.debug("当前步骤不是严格模式，获取对象[" + targetObject.getClass().getName() + "]中的所有属性值");
			attributeMap = ClassUtils.getAttributeForInputLevel(targetObject, JsonFilterView.Partner.class);
		}
		if(currentRoute.getTargetObjectAttributeMap() == null || currentRoute.getTargetObjectAttributeMap().size() < 1){
			logger.warn("工作步骤[" + currentRoute.getRouteId() + "]未定义任何目标对象的属性值");
			currentRoute.setTargetObjectAttributeMap(new HashMap<String,Attribute>());
			//return attributeMap;
		} else {
			for(Attribute attribute : currentRoute.getTargetObjectAttributeMap().values()){
				if(attribute.getColumnType() != null && attribute.getColumnType().equals(CommonStandard.COLUMN_TYPE_NATIVE)){
					//对原生属性进行判断
					if(attribute.getUseMessagePrefix() == null){
						attribute.setUseMessagePrefix(CommonStandard.DEFAULT_MESSAGE_PREFIX);
					}
					attributeMap.put(attribute.getColumnName(), attribute);
				}
			}
		}
		if(generalDataDefineMap == null || generalDataDefineMap.size() < 1){
			logger.warn("原始数据定义为空");
			//attributeMap = currentRoute.getTargetObjectAttributeMap();
			return attributeMap;
		}

		for(DataDefine dd : generalDataDefineMap.values()){
			if(!currentRoute.getTargetObjectAttributeMap().containsKey(dd.getDataCode())){
				if(isStrict){
					logger.debug("在当前步骤中找不到属性[" + dd.getDataCode() + "]而且当前为严格模式，跳过本属性");
					continue;
				}
				//宽松模式，则加入
				String[] validValue = null;
				if(StringUtils.isBlank(dd.getValidDataEnum())){
					validValue = null;
				} else {
					validValue = dd.getValidDataEnum().split(",");
				}
				attributeMap.put(dd.getDataCode(), new Attribute(CommonStandard.COLUMN_TYPE_EXTRA, dd.getDataCode(), dd.getInputMethod(), false, 0, false, validValue));
			} else {
				//在工作步骤中包含有该属性的定义，直接放入
				attributeMap.put(dd.getDataCode(), currentRoute.getTargetObjectAttributeMap().get(dd.getDataCode()));
			}
		}

		return attributeMap;
	}

	@Override
	public int closeCurrentStep(EisObject targetObject, WorkflowInstance workflowInstance) {
		Route currentRoute = workflowInstance.getCurrentRoute();
		if(currentRoute == null){
			logger.error("尝试结束步骤的工作流实例没有当前步骤");
			return -EisError.stepNotFound.id;
		}
		Set<StepCondition> stepConditionSet = currentRoute.getStepConditionSet();
		if(stepConditionSet == null){
			logger.debug("当前工作步骤[" + currentRoute.getRouteId() + "]没有步骤条件数据");
			update(workflowInstance);
			return OperateResult.success.getId();
		}
		for(StepCondition stepCondition : stepConditionSet){
			if(StringUtils.isBlank(stepCondition.getAttributeName())){
				logger.error("步骤条件未定义属性名称");
				return -EisError.dataError.id;
			}
			if(StringUtils.isBlank(stepCondition.getAttributeValue())){
				logger.error("步骤条件未定义属性值");
				return -EisError.dataError.id;
			}
			String value = ClassUtils.getValue(targetObject, stepCondition.getAttributeName(), stepCondition.getColumnType());
			if(value == null){
				logger.error("无法读取对象[" + targetObject.getClass().getName() + "]的属性值:" + stepCondition.getAttributeName());
				return -EisError.dataError.id;
			}
			logger.debug("对象[" + targetObject.getClass().getName() + "]的属性值" + stepCondition.getAttributeName() + "是:" + value + ",条件定义的属性值是:" + stepCondition.getAttributeValue());

			if(value.equalsIgnoreCase(stepCondition.getAttributeValue())){
				boolean foundNextStep = false;
				for(Route route : workflowInstance.getRouteList()){
					if(route.getStep() == stepCondition.getToStep()){
						workflowInstance.setCurrentStep(route.getStep());
						foundNextStep = true;
						break;
					}
				}
				if(foundNextStep){
					break;
				}

			}
		}

		update(workflowInstance);
		return OperateResult.success.getId();

	}

	@Override
	public boolean canAccess(WorkflowInstance workflowInstance, User user){
		Route currentRoute = workflowInstance.getCurrentRoute();
		if(currentRoute == null){
			logger.error("无法获取工作流实例[" +  workflowInstance.getWorkflowInstanceId() + "]的当前步骤");
			return false;
		}


		WorkflowPrivilegeCriteria workflowPrivilegeCriteria = new WorkflowPrivilegeCriteria(workflowInstance.getOwnerId());


		if(user.getRelatedRoleList() == null || user.getRelatedRoleList().size() < 1){
			logger.error("用户没有任何对应角色");
			return false;
		}
		long[] roleIds = new long[user.getRelatedRoleList().size()];
		int i = 0;
		for(Role role : user.getRelatedRoleList()){
			roleIds[i] = role.getRoleId();
		}
		workflowPrivilegeCriteria.setRoleIds(roleIds);
		workflowPrivilegeCriteria.setWorkflowId(workflowInstance.getWorkflowId());
		workflowPrivilegeCriteria.setStep(currentRoute.getStep());
		workflowPrivilegeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		int count = workflowPrivilegeService.count(workflowPrivilegeCriteria);
		logger.debug("根据条件[" + workflowPrivilegeCriteria + "]查询工作流权限数量是:" + count);
		if(count > 0){
			return true;
		}
		return false;

	}

}
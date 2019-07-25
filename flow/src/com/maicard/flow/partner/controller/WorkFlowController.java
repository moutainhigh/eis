package com.maicard.flow.partner.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.flow.enmus.WorkflowTypeEnum;
import com.maicard.standard.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.Paging;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.flow.criteria.WorkflowCriteria;
import com.maicard.flow.domain.Workflow;
import com.maicard.flow.service.WorkflowService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.standard.SecurityStandard.UserTypes;

@Controller
@RequestMapping("/workflow")
public class WorkFlowController extends BaseController  {

	@Resource
	private CertifyService certifyService;

	@Resource
	private WorkflowService workflowService;



	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			WorkflowCriteria workflowCriteria) throws Exception {
		final String view = "common/workflow/list";

		map.put("addUrl", "./workflow/" + "create");
		map.put("title", "工作流列表");

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		workflowCriteria.setOwnerId(partner.getOwnerId());

		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = workflowService.count(workflowCriteria);
		map.put("total", totalRows);
		if(totalRows < 1){
			logger.debug("当前返回的工作流数是0,ownerId=" + workflowCriteria.getOwnerId());
			return view;
		}
		Paging paging = new Paging(rows);
		workflowCriteria.setPaging(paging);

		workflowCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<Workflow> workflowList = workflowService.listOnPage(workflowCriteria);
		for(Workflow workflow:workflowList){
			workflow.setOperate(new HashMap<String,String>());
			workflow.getOperate().put("get", "./workflow/" + "get" + "/" + workflow.getWorkflowId());
			workflow.getOperate().put("del", "./workflow/" + "delete");		
			workflow.getOperate().put("update", "./workflow/" + "update" + "/" + workflow.getWorkflowId());		
		}
		map.put("statusCodeList", BasicStatus.values());
		map.put("workflowCodeList", WorkflowTypeEnum.values());
		map.put("workflow", new Workflow());
		map.put("rows",workflowList);
		return view;

	}

	@RequestMapping(value="/get" + "/{workflowId}", method=RequestMethod.GET )		
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("workflowId") Integer workflowId) throws Exception {
		if(workflowId == 0){
			logger.error("请求中找不到必须的参数[workflowId]");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"未提供要查看的工作流ID"));
			return CommonStandard.partnerMessageView;
		}
		Workflow workflow = workflowService.select(workflowId);
		if(workflow == null){
			logger.error("找不到ID=" + workflowId + "的workflow对象");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"找不到ID=" + workflowId + "的工作流"));
			return CommonStandard.partnerMessageView;
		}	
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		if(workflow.getOwnerId() != partner.getOwnerId()){
			logger.error("工作流[" + workflow.getWorkflowId() + "]的ownerId[" + workflow.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"找不到ID=" + workflowId + "的工作流"));
			return CommonStandard.partnerMessageView;		
		}
		logger.debug(workflow.getWorkflowId() + "#工作流的工作步骤有:" + (workflow.getRouteList() == null ? "空" : workflow.getRouteList().size()));
		map.put("workflow", workflow);
		map.put("title", "查看工作流");

		return "common/workflow/" + "get";
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)		
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			int deleteId = Integer.parseInt(ids[i]);
			Workflow workflow = workflowService.select(deleteId);
			if(workflow == null){
				logger.warn("找不到要删除的模版，ID=" + deleteId);
				continue;
			}
			if(workflow.getOwnerId() != partner.getOwnerId()){
				logger.warn("要删除的模版，ownerId[" + workflow.getOwnerId() + "]与系统会话中的ownerId不一致:" + deleteId);
				continue;
			}
			try{
				if(workflowService.delete(deleteId) > 0){

					successDeleteCount++;
				}
			}catch(DataIntegrityViolationException forignKeyException ){
				String error  = " 无法删除[" + ids[i] + "]，因为与其他数据有关联. ";
				logger.error(error);
				errors += error + "\n";
			}
		}

		String messageContent = "成功删除[" + successDeleteCount + "]个.";
		if(!errors.equals("")){
			messageContent += errors;
		}
		logger.info(messageContent);
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)	
	public String getCreate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("workflowCriteria") WorkflowCriteria workflowCriteria) throws Exception {

		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		map.put("title", "创建工作流");

		map.put("statusCodeList", BasicStatus.values());
		map.put("workflowCodeList", WorkflowTypeEnum.values());
		map.put("workflow", new Workflow());
		return "common/workflow/" + "create";
	}


	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("workflow") Workflow workflow) throws Exception {

		User loginedSysUser = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(loginedSysUser == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		workflow.setOwnerId(loginedSysUser.getOwnerId());
		try{
			workflowService.insert(workflow);
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));			
		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();	
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/update" + "/{workflowId}", method=RequestMethod.GET)	
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("workflowCriteria") WorkflowCriteria workflowCriteria,
			@PathVariable("workflowId") int workflowId) throws Exception {

		LanguageCriteria languageCriteria = new LanguageCriteria();
		languageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		User loginedSysUser = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(loginedSysUser == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		map.put("title", "编辑系统模版");
		map.put("workflowCriteria", workflowCriteria);
		map.put("statusCodeList", BasicStatus.values());
		Workflow workflow = null;
		if(workflowId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[workflowId]");
		}
		workflow = workflowService.select(workflowId);
		if(workflow == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + workflowId + "的workflow对象");			
		}
		if(workflow.getOwnerId() != loginedSysUser.getOwnerId()){
			logger.error("模版[" + workflow.getWorkflowId() + "]的ownerId[" + workflow.getOwnerId() + "]不属于当前ownerId:" + loginedSysUser.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + workflowId + "的workflow对象");			
		}
		map.put("workflow", workflow);
		return "common/workflow/" + "update";
	}


	@RequestMapping(value="/update", method=RequestMethod.POST)	
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("workflow") Workflow workflow) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		Workflow _oldWorkflow = workflowService.select(workflow.getWorkflowId());
		if(_oldWorkflow == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + workflow.getWorkflowId() + "的模版对象");			
		}
		if(_oldWorkflow.getOwnerId() != partner.getOwnerId()){
			logger.error("模版[" + _oldWorkflow.getWorkflowId() + "]的ownerId[" + _oldWorkflow.getOwnerId() + "]不属于当前ownerId:" + partner.getOwnerId());
			throw new ObjectNotFoundByIdException("找不到ID=" + _oldWorkflow.getWorkflowId() + "的模版对象");			
		}
		workflow.setOwnerId(partner.getOwnerId());
		try{
			workflowService.update(workflow);
			map.put("message", new EisMessage(OperateResult.success.getId(),"修改成功"));
		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();	
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return CommonStandard.partnerMessageView;
	}


}

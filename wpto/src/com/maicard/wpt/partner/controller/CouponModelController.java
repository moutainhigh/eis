package com.maicard.wpt.partner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.domain.Money;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SystemLevel;

/**
 * BOSS管理优惠券的功能
 * 
 *
 *
 * @author NetSnake
 * @date 2015年11月25日
 *
 */
@Controller
@RequestMapping("/couponModel")
public class CouponModelController extends BaseController{
	
	@Resource
	private ConfigService configService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CouponModelService couponModelService;
	@Resource
	private FrontUserService frontUserService;
	
	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			CouponModelCriteria couponModelCriteria) throws Exception {
		map.put("addUrl", "./couponModel/create");
		map.put("title", "优惠券列表");
		Map<String,Map<String,String>>queryCondition = ClassUtils.getQueryCondition(couponModelCriteria,SystemLevel.partner.name());
		logger.debug("当前查询类有" + queryCondition.size() + "个允许的查询条件");
		map.put("queryCondition", queryCondition);
		for(String key : queryCondition.keySet()){
			logger.debug("查询条件:" + key + "=>" + queryCondition.get(key));
		}
		final String view = "common/couponModel/list";
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		

		couponModelCriteria.setOwnerId(ownerId);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = couponModelService.count(couponModelCriteria);
		//计算并放入分页
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		if(totalRows < 1){
			logger.debug("当前返回数据行数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		couponModelCriteria.setPaging(paging);
		couponModelCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<CouponModel> couponModelList = couponModelService.listOnPage(couponModelCriteria);
		
		map.put("total", totalRows);
		map.put("rows",couponModelList);
		return view;
	}


	@RequestMapping(value="/get" + "/{couponModelId}")	
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			CouponModelCriteria couponModelCriteria,
			@PathVariable("couponModelId") Integer couponModelId) throws Exception {

		final String view = "common/couponModel/get";
		if(couponModelId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[couponModelId]");
		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		CouponModel couponModel = couponModelService.select(couponModelId);
		if(couponModel == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + couponModelId + "的couponModel对象");			
		}
		if(couponModel.getOwnerId() != ownerId){
			logger.error("尝试获取的优惠券模型[" + couponModel.getCouponModelId() + "]，其ownerId[" + couponModel.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return view;		
		}
		map.put("couponModel", couponModel);
		map.put("couponModelCriteria", couponModelCriteria);
		return view;
	}

	@RequestMapping(value="/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {		
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.backMessageView;
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;String errors = "";
		for(int i = 0; i < ids.length; i++){
			CouponModel couponModel = couponModelService.select(Long.parseLong(ids[i]));
			if(couponModel == null){
				logger.warn("根据ID[" + ids[i] + "]找不到优惠券模型");
				continue;
			}
			if(couponModel.getOwnerId() != ownerId){
				logger.warn("尝试删除的优惠券模型[" + ids[i] + "]，其ownerId与系统会话中的[" + ownerId + "]不一致");
				continue;
			}

			try{		
				if(couponModelService.delete(couponModel.getCouponModelId()) > 0){
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
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			CouponModelCriteria couponModelCriteria) throws Exception {
		CouponModel couponModel = new CouponModel();
		String[] processor = applicationContextService.getBeanNamesForType(CouponProcessor.class);
		if(processor == null || processor.length < 1){
			logger.warn("系统没有定义优惠券处理器");
		} else {
			Map<String,String> processorMap = new HashMap<String,String>();
			for(String name : processor){
				Object bean = applicationContextService.getBean(name);
				if(bean == null || !(bean instanceof CouponProcessor)){
					continue;
				}
				CouponProcessor p = (CouponProcessor)bean;
				processorMap.put(name, p.getProcessorDesc());
			}
			logger.warn("系统中有" + processorMap.size() + "个优惠券处理器");
			map.put("processorMap", processorMap);
		}
		map.put("couponModelCriteria", couponModelCriteria);
		map.put("couponModel", couponModel);
		map.put("statusCodeList", BasicStatus.values());

		return "common/couponModel/create";
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("couponModel") CouponModel couponModel) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.backMessageView;
		}
		couponModel.setOwnerId(ownerId);
		int giftMoney = ServletRequestUtils.getIntParameter(request, "giftMoney", 1);
		logger.debug("CouponModel数据:"+couponModel.toString()+"还有："+giftMoney);
		Money money = new Money();
		money.setGiftMoney(giftMoney);
		couponModel.setGiftMoney(money);
		try{
			int rs = couponModelService.insert(couponModel);
			if(rs == 1){
				map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));
			} else {
				map.put("message", new EisMessage(OperateResult.failed.getId(),"添加失败:" + rs));

			}

		}catch(Exception e){
			String message = "数据操作失败" + e.getMessage();			
			throw new DataWriteErrorException(message);
		}
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value="/update/{couponModelId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request,ModelMap map,
			CouponModelCriteria couponModelCriteria,
			@PathVariable("couponModelId") Integer couponModelId) throws Exception {
		CouponModel couponModel = null;
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.backMessageView;
		}
		if(couponModelId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[couponModelId]");
		}
		couponModel = couponModelService.select(couponModelId);
		if(couponModel == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + couponModelId + "的couponModel对象");			
		}	
		if(couponModel.getOwnerId() != ownerId){
			logger.warn("尝试修改的优惠券模型[" + couponModelId + "]，其ownerId与系统会话中的[" + ownerId + "]不一致");
			return "couponModel/update";
		}

		map.put("couponModelCriteria", couponModelCriteria);
		map.put("couponModel", couponModel);
		map.put("statusCodeList", BasicStatus.values());

		return "common/couponModel/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)	
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("couponModel") CouponModel couponModel) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}

		CouponModel _oldCouponModel = couponModelService.select(couponModel.getCouponModelId());
		if(_oldCouponModel == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + couponModel.getCouponModelId() + "的couponModel对象");			
		}	
		if(_oldCouponModel.getOwnerId() != ownerId){
			logger.warn("尝试修改的优惠券模型[" + _oldCouponModel.getCouponModelId() + "]，其ownerId与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.partnerMessageView;
		}
		try{			
			couponModelService.update(couponModel);
			map.put("message", new EisMessage(OperateResult.success.getId(),"更新成功"));
		}catch(Exception e){
			String message = "数据操作失败" + e.getMessage();			
			throw new DataWriteErrorException(message);
		}
		return CommonStandard.partnerMessageView;
	}
	
	
}

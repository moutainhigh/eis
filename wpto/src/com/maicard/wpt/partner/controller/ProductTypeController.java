package com.maicard.wpt.partner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.Paging;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.flow.domain.WorkflowInstance;
import com.maicard.flow.service.WorkflowInstanceService;
import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductType;
import com.maicard.product.service.ProductTypeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.InputLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;

@Controller
@RequestMapping("/productType")
public class ProductTypeController extends BaseController {

	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private ProductTypeService productTypeService;

	@Resource
	private WorkflowInstanceService workflowInstanceService;


	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("productTypeCriteria") ProductTypeCriteria productTypeCriteria) throws Exception {
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 50);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = productTypeService.count(productTypeCriteria);
		Paging paging = new Paging(rows);
		productTypeCriteria.setPaging(paging);
		productTypeCriteria.getPaging().setCurrentPage(page);
		List<ProductType> productTypeList = productTypeService.listOnPage(productTypeCriteria);
		for(ProductType productType:productTypeList){
			productType.setOperate(new HashMap<String,String>());
			productType.getOperate().put("get", "./productType/"+ "get" + "/" + productType.getProductTypeId());
			productType.getOperate().put("del", "./productType/"+ "delete");				
			productType.getOperate().put("update", "./productType/"+ "update" + "/" + productType.getProductTypeId());				

		}
		map.put("total", totalRows);
		map.put("rows",productTypeList);
		return "productType/list";
	}


	@RequestMapping(value="/get" + "/{productTypeId}")	
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("productTypeId") Integer productTypeId) throws Exception {
		ProductType productType = null;
		if(productTypeId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[productTypeId]");
		} 
		productType = productTypeService.select(productTypeId);

		if(productType == null){
			throw new ObjectNotFoundByIdException("找不到指定的产品类型[productTypeId=" + productTypeId + "]");			
		}
		
		int workflowInstanceId = ServletRequestUtils.getIntParameter(request, "workflowInstanceId", 0);
		if(workflowInstanceId > 0){
			WorkflowInstance workflowInstance = workflowInstanceService.select(workflowInstanceId);
			Map<String, Attribute> validAttributeMap = new HashMap<String, Attribute>();
			if (workflowInstance == null) {
				logger.debug("找不到创建新产品的工作流实例，或不需要使用工作流");
				
			} else {
				if (workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1) {
					logger.error("针对对象[" + ObjectType.product.toString()	+ "]的工作流，其工作步骤为空");
					map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(),"工作流数据异常"						));
					return CommonStandard.partnerMessageView;
				}
				//根据工作流对可输入数据进行过滤
				
				validAttributeMap = workflowInstanceService.getValidInputAttribute(new Product(), workflowInstance, productType.getDataDefineMap());
				map.put("validAttributeMap",validAttributeMap);
				productType.setDataDefineMap(null);
			}
		}

		map.put("productType", productType);
		return "productType/" + "get";
	}



	@RequestMapping(value="/delete" , method=RequestMethod.GET)	
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			try{
				if(productTypeService.delete(Integer.parseInt(ids[i])) > 0){
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
		return CommonStandard.backMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)	
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		
		map.put("statusCodeList", BasicStatus.values());
		//输入级别
		map.put("inputLevelList", InputLevel.values());
		//产品类型
		ProductTypeCriteria productTypeCriteria = new ProductTypeCriteria();
		HashMap<Integer, String> productTypeMap = productTypeService.map(productTypeCriteria);
		map.put("productTypeMap", productTypeMap);
		//所有的数据规范
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		map.put("dataDefineList", dataDefineList);
		map.put("productType", new ProductType());
		return "productType/" + "create";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("productType") ProductType productType) throws Exception {
		if(productType == null){
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "没有提交productType对象"));
			return CommonStandard.backMessageView;
		}

		int[] dataDefineIds = ServletRequestUtils.getIntParameters(request, "dataDefineId");
		logger.debug("获取到" + (dataDefineIds == null ? 0 : dataDefineIds.length + "个自定义字段"));
		if(dataDefineIds != null){
			productType.setDataDefineMap(new HashMap<String,DataDefine>());
			for(int dataDefineId : dataDefineIds){
				DataDefine dataDefine = dataDefineService.select(dataDefineId);
				if(dataDefine != null){
					DataDefine productDataDefinePolicy = new DataDefine();
					productDataDefinePolicy.setDataDefineId(dataDefineId);
					productDataDefinePolicy.setDataCode(dataDefine.getDataCode());
					productDataDefinePolicy.setCurrentStatus(BasicStatus.normal.getId());
					String inputLevel = ServletRequestUtils.getStringParameter(request, dataDefineId + ".inputLevel");
					if(inputLevel == null){
						logger.debug("未得到数据规范[" + dataDefine.getDataCode() + "]的输入级别");
						inputLevel = InputLevel.system.name();
					} else {
						logger.debug("数据规范[" + dataDefine.getDataCode() + "]的输入级别是:" + inputLevel);
					}
					productDataDefinePolicy.setInputLevel(inputLevel);
					productType.getDataDefineMap().put(dataDefine.getDataCode(), productDataDefinePolicy);
				}
			}			
		}

		try{	
			productTypeService.insert(productType);
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));

		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();			
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return CommonStandard.backMessageView;
	}

	@RequestMapping(value="/update" + "/{productTypeId}", method=RequestMethod.GET)	
	public String getUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("productTypeId") Integer productTypeId) throws Exception {

		
		map.put("statusCodeList", BasicStatus.values());
		map.put("inputLevelList", InputLevel.values());
		if(productTypeId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[productTypeId]");
		}
		ProductType productType = productTypeService.select(productTypeId);
		if(productType == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + productTypeId + "的sysPrivilege对象");			
		}	
		//所有的数据规范
		// -> 所有对应product拥有的数据规范,NetSnake,2013-09-19
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setCurrentStatus(BasicStatus.normal.getId());
		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		dataDefineCriteria.setObjectId(productType.getProductTypeId());
	
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList != null && productType.getDataDefineMap() != null){
			//匹配已经选中的规范
			for(DataDefine dataDefine : dataDefineList){
				for(DataDefine productDataDefinePolicy : productType.getDataDefineMap().values()){
					if(dataDefine.getDataDefineId() == productDataDefinePolicy.getDataDefineId()){
						dataDefine.setCurrentStatus(BasicStatus.relation.getId());
						dataDefine.setInputLevel(productDataDefinePolicy.getInputLevel());
					}
				}
			}
		}
		map.put("dataDefineList", dataDefineList);
		map.put("productType", productType);
		return "productType/" + "update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("productType") ProductType productType) throws Exception {
		EisMessage message = null;
		if(productType == null){
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "没有提交productType对象"));
			return CommonStandard.backMessageView;
		}
		if(productType.getProductTypeId() < 1){
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "没有提交正确的productTypeID"));
			return CommonStandard.backMessageView;
		}
		int[] dataDefineIds = ServletRequestUtils.getIntParameters(request, "dataDefineId");
		logger.debug("获取到" + (dataDefineIds == null ? 0 : dataDefineIds.length + "个自定义字段"));
		if(dataDefineIds != null){
			productType.setDataDefineMap(new HashMap<String,DataDefine>());
			for(int dataDefineId : dataDefineIds){
				DataDefine dataDefine = dataDefineService.select(dataDefineId);
				if(dataDefine != null){
					DataDefine productDataDefinePolicy = new DataDefine();
					productDataDefinePolicy.setDataDefineId(dataDefineId);
					productDataDefinePolicy.setDataCode(dataDefine.getDataCode());
					String inputLevel = ServletRequestUtils.getStringParameter(request, dataDefineId + ".inputLevel");
					if(inputLevel == null){
						logger.debug("未得到数据规范[" + dataDefine.getDataCode() + "]的输入级别");
						inputLevel = InputLevel.system.name();
					} else {
						logger.debug("数据规范[" + dataDefine.getDataCode() + "]的输入级别是:" + inputLevel);
					}
					productDataDefinePolicy.setInputLevel(inputLevel);
					productDataDefinePolicy.setCurrentStatus(BasicStatus.normal.getId());
					productType.getDataDefineMap().put(dataDefine.getDataCode(), productDataDefinePolicy);
				}
			}			
		}

		try{	
			productTypeService.update(productType);
			map.put("message", new EisMessage(OperateResult.success.getId(),"更新成功"));

		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();			
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		map.put("message", message);
		return CommonStandard.backMessageView;
	}

}

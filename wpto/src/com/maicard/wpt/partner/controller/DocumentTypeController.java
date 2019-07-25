package com.maicard.wpt.partner.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.DataDefineService;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.site.criteria.*;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.OperateResult;
import com.maicard.standard.CommonStandard.DataFetchMode;

@Controller
@RequestMapping("/documentType")
public class DocumentTypeController extends BaseController {

	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private DocumentTypeService documentTypeService;



	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			DocumentTypeCriteria documentTypeCriteria) throws Exception {


		map.put("documentTypeCriteria", documentTypeCriteria);
		map.put("addUrl", "./documentType/create");

		int totalRows = documentTypeService.count(documentTypeCriteria);

		List<DocumentType> documentTypeList = documentTypeService.listOnPage(documentTypeCriteria);
		for(DocumentType documentType:documentTypeList){
			documentType.setOperate(new HashMap<String,String>());
			documentType.getOperate().put("get", "./documentType/get/"+ documentType.getDocumentTypeId());
			documentType.getOperate().put("del", "./documentType/delete");				
			documentType.getOperate().put("update", "./documentType/update/"+ documentType.getDocumentTypeId());				

		}
		map.put("total", totalRows);
		map.put("rows",documentTypeList);
		return "common/documentType/list";
	}

	@RequestMapping(value="/get/{documentTypeId}")	
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("documentTypeId") int documentTypeId) throws Exception {

		DocumentType documentType =  documentTypeService.select(documentTypeId, DataFetchMode.full.toString());
		logger.debug("自定义字段数量：" + ( documentType.getDataDefineMap() == null ? -1 : documentType.getDataDefineMap().size()));
		map.put("documentType", documentType);
		return "common/documentType/get";
	}

	@RequestMapping(value="/delete")	
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		String idList = ServletRequestUtils.getStringParameter(request, "idList");
		if(idList == null || idList.equals("")){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[idList]");
		}
		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			try{
				if(documentTypeService.delete(Integer.parseInt(ids[i])) > 0){
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
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		DataDefineCriteria  dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);

		DocumentType documentType = new DocumentType();

		map.put("documentType", documentType);
		map.put("dataDefineList", dataDefineList);
		map.put("statusCodeList", BasicStatus.values());
		return "common/documentType/create";
	}


	@RequestMapping(value="/create", method=RequestMethod.POST)		
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			DocumentType documentType) throws Exception {


		EisMessage message = null;
		try{
				if(documentTypeService.insert(documentType) > 0){
					message = new EisMessage(OperateResult.success.getId(),"添加成功");			
				} else {
					message = new EisMessage(OperateResult.failed.getId(),"添加失败");				
				}
			
		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();			
			logger.error(m);
			throw new DataWriteErrorException(m);
		}

		map.put("message", message);
		return CommonStandard.partnerMessageView;
	}

	

}

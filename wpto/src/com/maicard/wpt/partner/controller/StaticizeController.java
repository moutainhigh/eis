package com.maicard.wpt.partner.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.StaticizeService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;


/**
 * 
 * 系统缓存管理接口
 * 
 * 
 * @author NetSnake
 * 
 */
@Controller
@RequestMapping("/staticize")
public class StaticizeController extends BaseController {

	@Resource
	private DocumentService documentService;
	@Resource
	private StaticizeService staticizeService;
	@Resource
	private NodeService nodeService;

	@RequestMapping("/create")	
	@IgnorePrivilegeCheck
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("objectType") String objectType) throws Exception {
		String[] ids = ServletRequestUtils.getStringParameters(request, "id");
		if(ids == null || ids.length < 1){
			logger.warn("请求创建静态化但是没有提供任何ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交正确的数据"));
			return CommonStandard.partnerMessageView;
		}
		
		int count = 0;
		
		if(objectType.equalsIgnoreCase(ObjectType.node.name())){
			for(String id : ids){
				Node node = nodeService.select(Integer.parseInt(id));
				if(node == null){
					logger.error("找不到要静态化的栏目:" + id);
					continue;
				}
				staticizeService.staticize(node);
				count++;
			}
		} else if(objectType.equalsIgnoreCase(ObjectType.document.name())){
			for(String id : ids){
				Document document = documentService.select(Integer.parseInt(id));
				if(document == null){
					logger.error("找不到要静态化的文章:" + id);
					continue;
				}
				staticizeService.staticize(document,0);
				count++;
			}
			
		}
		map.put("message", new EisMessage(OperateResult.success.getId(),"执行了" + count + "个静态化请求"));
		return CommonStandard.partnerMessageView;

	}

	@RequestMapping("/delete")	
	@IgnorePrivilegeCheck
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("objectType") String objectType) throws Exception {
		logger.debug("删除静态化");
		String[] ids = ServletRequestUtils.getStringParameters(request, "id");
		if(ids == null || ids.length < 1){
			logger.warn("请求删除静态化页面但是没有提供任何ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提交正确的数据"));
			return CommonStandard.partnerMessageView;
		}
		
		int count = 0;
		int rs = 0;
		if(objectType.equalsIgnoreCase(ObjectType.node.name())){
			for(String id : ids){
				Node node = nodeService.select(Integer.parseInt(id));
				if(node == null){
					logger.error("找不到要删除静态化的栏目:" + id);
					continue;
				}
				rs = staticizeService.deleteStaticizeFile(node);
				count++;
			}
		} else if(objectType.equalsIgnoreCase(ObjectType.document.name())){
			for(String id : ids){
				Document document = documentService.select(Integer.parseInt(id));
				if(document == null){
					logger.error("找不到要删除静态化的文章:" + id);
					continue;
				}
				rs = staticizeService.deleteStaticizeFile(document);
				count++;
			}
			
		}
		if (rs > 0) {
			map.put("message", new EisMessage(OperateResult.success.getId(),"删除了" + count + "个静态化文件"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"删除失败"));
		}
		
		return CommonStandard.partnerMessageView;
	}	

	
}

package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisCache;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.CenterDataService;
import com.maicard.mb.service.MessageService;
import com.maicard.product.service.ValidateCacheService;
import com.maicard.standard.CommonStandard;
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
@RequestMapping("/cache")
public class CacheController extends BaseController {

	@Resource
	private CacheService cacheService;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private MessageService messageService;
	@Resource
	private ValidateCacheService validateCacheService;




	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap map)
					throws Exception {

		map.put("rows", _list());
		return "common/cache/list";
	}


	@RequestMapping("/delete")	
	@AllowJsonOutput
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {

		String[] ids = idList.split("-");
		int successDeleteCount = 0;
		String errors = "";
		for(int i = 0; i < ids.length; i++){
			/*	if(partnerService.isValidSubUser(currentPartner.getUuid(), Integer.parseInt(ids[i]))){
				map.put("message", new EisMessage(Error.accessDenied.getId(), "非法请求"));
				return CommonStandard.partnerMessageView;
			}*/
			cacheService.evict(ids[i]);
			messageService.sendJmsDataSyncMessage(null, "cacheService", "evict", ids[i]);
			successDeleteCount++;


		}

		String messageContent = "成功清除[" + successDeleteCount + "]个缓存.";
		if(!errors.equals("")){
			messageContent += errors;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(),messageContent));
		map.put("rows", _list());
		return CommonStandard.partnerMessageView; 
	}	

	@RequestMapping("/redis/list")	
	@IgnorePrivilegeCheck
	public String listRedis(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String key = "rand#" + new Random().nextInt();
		centerDataService.delete(key);
		return CommonStandard.partnerMessageView; 
	}	



	private List<EisCache> _list(){
		String[] cacheNames = cacheService.getCacheNames();
		List<EisCache> list = new ArrayList<EisCache>();
		for(int i=0;i<cacheNames.length;i++){
			EisCache a=new EisCache();
			a.setName(cacheNames[i]);
			list.add(a);			
		}
		EisCache a = new EisCache();
		a.setName("local");
		list.add(a);

		int cacheCount = 0;
		for(EisCache cache:list){
			cacheCount++;
			List<String> keys = cacheService.listKeys(cache.getName(), null);
			if(keys == null){
				cache.setCacheCount("0");
			} else {
				cache.setCacheCount(keys.size()+"");
				keys = null;
			}
			long memory = 0;// cacheService.getMemorySize(cache.getName());
			if(memory > 1024 * 1024){

				cache.setCacheSize(memory / 1024 /1024 +"M");
			} else if( memory > 1024){
				cache.setCacheSize(memory / 1024 +"K");			
			} else {
				cache.setCacheSize(memory / 1024 +"B");					

			}
			cache.setIndex(cacheCount);
			cache.setOperate(new HashMap<String,String>());
			cache.getOperate().put("del", "/cache/delete");		
			cache.getOperate().put("get", "/cache/update");		
		}		
		return list;
	}
}


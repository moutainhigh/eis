package com.maicard.wpt.partner.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.security.criteria.OperateLogCriteria;
import com.maicard.security.domain.OperateLog;
import com.maicard.security.domain.User;
import com.maicard.security.service.OperateLogService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;

/**
 *
 *
 * @author NetSnake
 * @date 2015年12月14日
 *
 */
@Controller
@RequestMapping("/operateLog")
public class OperateLogController extends BaseController  {

	@Resource
	private OperateLogService operateLogService;
	
	@Resource
	private PartnerService partnerService;

	@RequestMapping(method=RequestMethod.GET)
	@AllowJsonOutput
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, OperateLogCriteria tagCriteria) throws Exception {
		final String view =  "common/operateLog/index";
		
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;		
		}
		tagCriteria.setOwnerId(ownerId);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = operateLogService.count(tagCriteria);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
		map.put("total", totalRows);
		if(totalRows < 1){
			logger.debug("当前查询返回的数据行数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		tagCriteria.setPaging(paging);
		tagCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<OperateLog> operateLogList = operateLogService.listOnPage(tagCriteria);
		if(operateLogList.size() < 1){
			logger.debug("根据当前条件未得到任何OperateLog数据");
		} else {
			for(OperateLog operateLog : operateLogList){
				if(operateLog.getUuid() > 0){
					User partner = partnerService.select(operateLog.getUuid());
					if(partner != null){
						operateLog.setOperateValue("username", partner.getNickName() + "-" + partner.getUsername());
					}
				}
			}
		}
		map.put("rows",operateLogList);
		return view;

	}

	@RequestMapping(value="/get" + "/{tagId}", method=RequestMethod.GET )		
	@AllowJsonOutput
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
		@PathVariable("tagId") Integer tagId) throws Exception {
		if(tagId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[tagId]");
		}
		OperateLog tag = operateLogService.select(tagId);
		if(tag == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + tagId + "的tag对象");			
		}	
		map.put("tag", tag);
		return "common/operateLog/" + "get";
	}

	




}

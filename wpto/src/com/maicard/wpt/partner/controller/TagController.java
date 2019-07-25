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

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Language;
import com.maicard.common.service.LanguageService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;
import com.maicard.site.domain.Template;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 *
 *
 * @author NetSnake
 * @date 2015年12月14日
 *
 */
@Controller
@RequestMapping("/tag")
public class TagController extends BaseController  {

	@Resource
	private AuthorizeService authorizeService;


	@Resource
	private CertifyService certifyService;

	@Resource
	private TagService tagService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;

	@RequestMapping(method=RequestMethod.GET)
	@AllowJsonOutput
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, TagCriteria tagCriteria) throws Exception {
		final String view =  "common/tag/index";

		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		//////////////////////// 结束标准流程 ///////////////////////
		tagCriteria.setOwnerId(ownerId);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 50);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = tagService.count(tagCriteria);
		map.put("total", totalRows);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

		if(authorizeService.havePrivilege(partner, ObjectType.partner.name(), "w")){
			map.put("addUrl", "/tag/create.shtml" );
		}

		Map<String,String> objectTypeList = new HashMap<String,String>();
		for(ObjectType ot : ObjectType.values()){
			if(ot.toString().equalsIgnoreCase("unknown")){
				objectTypeList.put("所有","");
			} else {
				objectTypeList.put(ot.name(), ot.toString());

			}
		}
		map.put("objectTypeList", objectTypeList);

		if(totalRows < 1){
			logger.debug("当前查询返回的数据行数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		tagCriteria.setPaging(paging);
		tagCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<Tag> tagList = tagService.listOnPage(tagCriteria);
		if(tagList.size() < 1){
			logger.debug("根据当前条件未得到任何tag数据");
		} else {
			/*for(Tag tag : tagList){

			}*/
		}
		map.put("rows",tagList);
		return view;

	}

	@RequestMapping(value="/get" + "/{tagId}", method=RequestMethod.GET )		
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map, TagObjectRelationCriteria tagObjectRelationCriteria,
			@PathVariable("tagId") Integer tagId) throws Exception {
		
		final String view = "common/tag/get";

		////////////////////////标准流程 ///////////////////////
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			throw new UserNotFoundInRequestException();
		}

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		if(tagId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[tagId]");
		}
		Tag tag = tagService.select(tagId);
		if(tag == null){
			logger.error("找不到ID=" + tagId + "的tag对象");	
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的标签"));
			return CommonStandard.partnerMessageView;
		}	

		tagObjectRelationCriteria.setOwnerId(ownerId);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = tagObjectRelationService.count(tagObjectRelationCriteria);
		
		map.put("total", totalRows);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));

		if(totalRows < 1){
			logger.debug("当前查询返回的数据行数是0");
			return view;
		}
		Paging paging = new Paging(rows);
		tagObjectRelationCriteria.setPaging(paging);
		tagObjectRelationCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<TagObjectRelation> tagObjectRelationList = tagObjectRelationService.listOnPage(tagObjectRelationCriteria);
		if(tagObjectRelationList.size() < 1){
			logger.debug("根据当前条件未得到任何tagObjectRelation数据");
		} 
		map.put("rows", tagObjectRelationList);
		map.put("tag", tag);
		return view;
	}

	@RequestMapping(value="/delete", method=RequestMethod.POST)		
	@AllowJsonOutput
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
				if(tagService.delete(Integer.parseInt(ids[i])) > 0){

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


	@RequestMapping(value="/update" + "/{tagId}", method=RequestMethod.GET)	
	@AllowJsonOutput
	public String getUpdate(HttpServletRequest request, HttpServletResponse response,ModelMap map,
			@ModelAttribute("tagCriteria") TagCriteria tagCriteria,
			@PathVariable("tagId") int tagId) throws Exception {



		map.put("tagCriteria", tagCriteria);
		map.put("statusCodeList", BasicStatus.values());


		if(tagId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[tagId]");
		}
		Tag tag = tagService.select(tagId);
		if(tag == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + tag + "的tag对象");			
		}
		map.put("tag", tag);
		return "common/tag/" + "update";
	}


	@RequestMapping(value="/update", method=RequestMethod.POST)	
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("tag") Tag tag) throws Exception {

		try{
			tagService.update(tag);
			map.put("message", new EisMessage(OperateResult.success.getId(),"修改成功"));
		}catch(Exception e){
			String m = "数据操作失败" + e.getMessage();	
			logger.error(m);
			throw new DataWriteErrorException(m);
		}
		return CommonStandard.partnerMessageView;
	}


}

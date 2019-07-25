package com.maicard.wpt.boss.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Paging;
import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.service.AddressBookService;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SystemLevel;

@Controller
@RequestMapping("/addressBook")
public class AddressBookController extends BaseController{

	@Resource
	private AddressBookService addressBookService;
	@Resource
	private FrontUserService frontUserService;

	@RequestMapping(method=RequestMethod.GET)	
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("addressBookCriteria") AddressBookCriteria addressBookCriteria) throws Exception {
		Map<String,Map<String,String>> queryCondition = ClassUtils.getQueryCondition(addressBookCriteria, SystemLevel.boss.name());
		logger.debug("当前查询类有" + queryCondition.size() + "个允许的查询条件");
		map.put("queryCondition", queryCondition);
		for(String key : queryCondition.keySet()){
			logger.debug("查询条件:" + key + "=>" + queryCondition.get(key));
		}
		final String view = "addressBook/list";
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
		String user = addressBookCriteria.getUsername();
		addressBookCriteria.setUsername(null);
		if(StringUtils.isNotBlank(user)){
			if(StringUtils.isNumeric(user.trim())){
				User frontUser = frontUserService.select(Long.parseLong(user.trim()));
				if(frontUser == null){
					logger.warn("找不到UUID=" + user + "的前端用户");
					return view;
				}
				if(frontUser.getOwnerId() != ownerId){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUser.getOwnerId() + "]与指定的ownerId[" + ownerId + "]不匹配");
					return view;
				}
				addressBookCriteria.setUuid(Long.parseLong(user.trim()));
				logger.info("查询的用户名是:" + user + ",UUID=" + user.trim());
		} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(user.trim());
				List<User> frontUserList = frontUserService.list(frontUserCriteria);
				if(frontUserList == null || frontUserList.size() < 1){
					logger.warn("找不到昵称=" + user + "的前端用户");
					frontUserCriteria.setUsername(user.trim());
					frontUserList = frontUserService.list(frontUserCriteria);
					if(frontUserList == null || frontUserList.size() < 1){
						logger.warn("找不到用户名=" + user + "的前端用户");
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if(frontUserList.get(0).getOwnerId() != ownerId){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与指定的ownerId[" + ownerId + "]不匹配");
					return view;
				}
				addressBookCriteria.setUuid(frontUserList.get(0).getUuid());
				logger.info("查询的用户名是:" + user + ",UUID=" + frontUserList.get(0).getUuid());

			}
		}

		addressBookCriteria.setOwnerId(ownerId);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 10);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int totalRows = addressBookService.count(addressBookCriteria);

		Paging paging = new Paging(rows);
		addressBookCriteria.setPaging(paging);
		addressBookCriteria.getPaging().setCurrentPage(page);
		logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 " + page + " 页。");
		List<AddressBook> addressBookList = addressBookService.listOnPage(addressBookCriteria);
		for(AddressBook addressBook:addressBookList){
			User frontUser = frontUserService.select(addressBook.getUuid());
			if(frontUser == null){
				addressBook.setUsername("未知" + addressBook.getUuid());
			} else if(StringUtils.isBlank(frontUser.getNickName())){
				addressBook.setUsername(frontUser.getUsername());
			} else {
				addressBook.setUsername(frontUser.getNickName());
			}
			addressBook.setOperate(new HashMap<String,String>());
			addressBook.getOperate().put("detail", "./addressBook/get/"+ addressBook.getAddressBookId());
			addressBook.getOperate().put("del", "./addressBook/delete"+ addressBook.getAddressBookId());				
			addressBook.getOperate().put("edit", "./addressBookEdit.do?mode=edit&addressBookId="+ addressBook.getAddressBookId());				
		}
		map.put("total", totalRows);
		map.put("rows",addressBookList);
		return view;
	}


	@RequestMapping(value="/get" + "/{addressBookId}")	
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("addressBookCriteria") AddressBookCriteria addressBookCriteria,
			@PathVariable("addressBookId") Integer addressBookId) throws Exception {

		final String view = "addressBook/get";
		if(addressBookId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[addressBookId]");
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
		AddressBook addressBook = addressBookService.select(""+addressBookId);
		if(addressBook == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + addressBookId + "的addressBook对象");			
		}
		if(addressBook.getOwnerId() != ownerId){
			logger.error("尝试获取的地址本[" + addressBook.getAddressBookId() + "]，其ownerId[" + addressBook.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return view;		
		}
		map.put("addressBook", addressBook);
		map.put("addressBookCriteria", addressBookCriteria);
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
			AddressBook addressBook = addressBookService.select(ids[i]);
			if(addressBook == null){
				logger.warn("根据ID[" + ids[i] + "]找不到地址本");
				continue;
			}
			if(addressBook.getOwnerId() != ownerId){
				logger.warn("尝试删除的地址本[" + ids[i] + "]，其ownerId与系统会话中的[" + ownerId + "]不一致");
				continue;
			}

			try{		
				if(addressBookService.delete(addressBook) > 0){
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
		return CommonStandard.backMessageView;
	}

	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request,ModelMap map,
			@ModelAttribute("addressBookCriteria") AddressBookCriteria addressBookCriteria) throws Exception {
		AddressBook addressBook = new AddressBook();

		map.put("addressBookCriteria", addressBookCriteria);
		map.put("addressBook", addressBook);
		map.put("statusCodeList", BasicStatus.values());

		return "addressBook/create";
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("addressBook") AddressBook addressBook) throws Exception {
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
		addressBook.setOwnerId(ownerId);
		try{
			addressBookService.insert(addressBook);
			map.put("message", new EisMessage(OperateResult.success.getId(),"添加成功"));

		}catch(Exception e){
			String message = "数据操作失败" + e.getMessage();			
			throw new DataWriteErrorException(message);
		}
		return CommonStandard.backMessageView;
	}

	@RequestMapping(value="/update/{addressBookId}", method=RequestMethod.GET)
	public String getUpdate(HttpServletRequest request,ModelMap map,
			@ModelAttribute("addressBookCriteria") AddressBookCriteria addressBookCriteria,
			@PathVariable("addressBookId") Integer addressBookId) throws Exception {
		AddressBook addressBook = null;
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
		if(addressBookId == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[addressBookId]");
		}
		addressBook = addressBookService.select(""+addressBookId);
		if(addressBook == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + addressBookId + "的addressBook对象");			
		}	
		if(addressBook.getOwnerId() != ownerId){
			logger.warn("尝试修改的地址本[" + addressBookId + "]，其ownerId与系统会话中的[" + ownerId + "]不一致");
			return "addressBook/update";
		}

		map.put("addressBookCriteria", addressBookCriteria);
		map.put("addressBook", addressBook);
		map.put("statusCodeList", BasicStatus.values());

		return "addressBook/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)	
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("addressBook") AddressBook addressBook) throws Exception {
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

		AddressBook _oldAddressBook = addressBookService.select(""+addressBook.getAddressBookId());
		if(_oldAddressBook == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + addressBook.getAddressBookId() + "的addressBook对象");			
		}	
		if(_oldAddressBook.getOwnerId() != ownerId){
			logger.warn("尝试修改的地址本[" + _oldAddressBook.getAddressBookId() + "]，其ownerId与系统会话中的[" + ownerId + "]不一致");
			return "addressBook/update";
		}
		try{			
			addressBookService.update(addressBook);
			map.put("message", new EisMessage(OperateResult.success.getId(),"更新成功"));
		}catch(Exception e){
			String message = "数据操作失败" + e.getMessage();			
			throw new DataWriteErrorException(message);
		}
		return CommonStandard.backMessageView;
	}
}

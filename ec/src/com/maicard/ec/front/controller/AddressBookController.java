package com.maicard.ec.front.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ContentPaging;
import com.maicard.common.util.Paging;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.service.AddressBookService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.product.service.CartService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.service.DocumentService;

@Controller
@RequestMapping("/addressBook")
public class AddressBookController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private DocumentService documentService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private AddressBookService addressBookService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayService payService;
	@Resource
	private PayMethodService payMethodService;
	@Resource
	private PayTypeService payTypeService;
	@Resource
	private CartService cartService;

	@RequestMapping (value="/detail/{addressBookId}",method=RequestMethod.GET)
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("addressBookId") String addressBookId
			) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录"));
			return CommonStandard.frontMessageView;
		}		

		AddressBook addressBook = addressBookService.select(addressBookId);
		if(addressBook == null){
			logger.error("找不到指定的地址本:" + addressBookId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到指定的地址本"));
			return CommonStandard.frontMessageView;
		}
		if(addressBook.getOwnerId() != frontUser.getOwnerId()){
			logger.error("指定的地址本:" + addressBookId + ",ownerId[" + addressBook.getOwnerId() + "]与用户的[" + frontUser.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到指定的地址本"));
			return CommonStandard.frontMessageView;
		}
		if(addressBook.getUuid() != frontUser.getUuid()){
			logger.error("指定的地址本:" + addressBookId + ",uuid[" + addressBook.getUuid() + "]与用户的[" + frontUser.getUuid() + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到指定的地址本"));
			return CommonStandard.frontMessageView;
		}
		map.put("addressBook", addressBook);
		return "addressBook/detail";
	}

	@RequestMapping (value="/create",method=RequestMethod.GET)
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		return "addressBook/create";
	}

	//增
	@RequestMapping (value="/create",method=RequestMethod.POST)
	public String postCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			AddressBook addressBook) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录"));
			return CommonStandard.frontMessageView;
		}		


		String identify = null;
		if(request.getHeader("referer") != null){
			String referer = request.getHeader("referer");
			String[] data = referer.split("\\?");
			if(data.length > 1){
				//得到查询字符串
				referer = data[1];
				data = referer.split("&");
				for(String d2 : data){
					if(d2.startsWith("identify")){
						identify = d2.replaceAll("^identify=", "");
						if(identify != null){
							identify = java.net.URLDecoder.decode(identify,"UTF-8");
						}
						break;
					}
				}
			}
		}
		logger.info("新增地址本的识别码是:" + identify);
		if(StringUtils.isNotBlank(identify)){
			addressBook.setIdentify(identify);
		}	
		if(StringUtils.isBlank(addressBook.getCountry())){
			addressBook.setCountry("中国");
		}		
		if(StringUtils.isBlank(addressBook.getContact())){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"请输入联系人"));
			return CommonStandard.frontMessageView;

		}
		if(StringUtils.isBlank(addressBook.getAddress())){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"请输入详细地址"));
			return CommonStandard.frontMessageView;
		}
		if(StringUtils.isBlank(addressBook.getMobile())){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"请输入手机号"));
			return CommonStandard.frontMessageView;
		}
		addressBook.setUuid(frontUser.getUuid());
		addressBook.setOwnerId(frontUser.getOwnerId());
		if(addressBook.getCurrentStatus() == 0){
			addressBook.setCurrentStatus(BasicStatus.normal.getId());
		}
		logger.info("uuid:"+addressBook.getUuid()+"|country:"+addressBook.getCountry()+"|province:"+addressBook.getProvince()+"|district:"+addressBook.getDistrict()+"|address:"+addressBook.getAddress()+"|contact:"+addressBook.getContact()+"|phone:"+addressBook.getPhone()+"|mobile:"+addressBook.getMobile()+"|postcode:"+addressBook.getPostcode());


		int r=1;
		/*try*/{
			r=addressBookService.insert(addressBook);
			logger.info("insert complate,r="+r);
		}
		/*catch(Exception e)
		{
			map.put("message",new EisMessage(OperateResult.failed.getId(),"插入失败"));
		}*/
		if (r==1) {
			map.put("message",new EisMessage(OperateResult.success.getId(),"新增地址成功"));
		} else {
			map.put("message",new EisMessage(OperateResult.failed.getId(),"新增地址成功"));

		}
		AddressBookCriteria addressBookCriteria = new AddressBookCriteria(frontUser.getOwnerId());
		addressBookCriteria.setUuid(frontUser.getUuid());
		//Y表示新增的设置为默认地址
		String setDefaultAdd = ServletRequestUtils.getStringParameter(request, "setDefaultAdd","N");
		int count = addressBookService.count(addressBookCriteria);
		if(count == 1 || setDefaultAdd.equals("Y")){
			logger.debug("当前一共只有1个地址本，将其设置为默认");
			addressBookService.setDefaultAdd(addressBook);
		} else {
			String setDefault = request.getParameter("setDefault");
			if(!StringUtils.isBlank(setDefault) && setDefault.toLowerCase().equals("on")){
				addressBookService.setDefaultAdd(addressBook);
			}
		}
		map.put("addressBook", addressBook);
		return CommonStandard.frontMessageView;
	}
	//删
	@RequestMapping (value="/delete/{addressBookId}",method=RequestMethod.POST)
	public String delete (HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("addressBookId") int addressBookId) throws Exception {
		//int uuid=ServletRequestUtils.getIntParameter(request, "uuid");
		User frontUser=null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录"));
			return CommonStandard.frontMessageView;
		}		
		AddressBook addressBook=new AddressBook();
		addressBook.setUuid(frontUser.getUuid());
		addressBook.setAddressBookId(addressBookId);
		addressBook.setOwnerId(frontUser.getOwnerId());
		try{
			int rs = addressBookService.delete(addressBook);
			logger.info("删除用户[" + frontUser.getUuid() + "]的地址[" + addressBookId + "],结果:" + rs);
			if(rs == 1){
				map.put("message",new EisMessage(OperateResult.success.getId(),"删除成功"));
			} else {
				map.put("message",new EisMessage(OperateResult.failed.getId(),"删除失败"));

			}
		}
		catch (Exception e){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"删除失败"));
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		return CommonStandard.frontMessageView;
	}
	/*	@RequestMapping (value="/update/{addressBookId}",method=RequestMethod.GET)
	public String edit (HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("addressBookId") String addressBookId) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));
			return CommonStandard.frontMessageView;
		}		
		AddressBookCriteria addressBookCriteria=new AddressBookCriteria();
		addressBookCriteria.setAddressBookId(addressBookId);
		AddressBook addressBook =addressBookServer.list(addressBookCriteria).get(0);
		logger.debug("xxxxxxxxxxxxx"+addressBook.getAddress()+"xxxxxxxxxxxxx");
		map.put("editDetail", addressBook);
		AddressBookCriteria addressBookCriteria1=new AddressBookCriteria();
		addressBookCriteria1.setUuid(frontUser.getUuid());
		int totalRows = addressBookServer.count(addressBookCriteria1);
		int rowsPerPage = 3;
		int currentPage = 1;
		Paging pageing = new Paging(rowsPerPage);
		addressBookCriteria1.setPaging(pageing);
		addressBookCriteria1.getPaging().setCurrentPage(currentPage);
		ContentPaging documentContentPaging = new ContentPaging(totalRows);
		documentContentPaging.setRowsPerPage(rowsPerPage);
		documentContentPaging.setCurrentPage(currentPage);
		documentContentPaging.setDisplayPageCount(totalRows);
		documentContentPaging.caculateDisplayPage();		
		List<AddressBook> addressBook1 =addressBookServer.listOnPage(addressBookCriteria1);
		map.put("documentContentPaging", documentContentPaging);
		map.put("rowsPerPage",rowsPerPage);
		map.put("currentPage",currentPage);
		map.put("addressBook", addressBook1);
		logger.debug("xxxxxxxxxxxxx"+addressBook1.size()+"xxxxxxxxxxxxx");
		return "addressBook/update";	
	}	*/
	@RequestMapping (value="/update/{addressbook_id}",method=RequestMethod.GET)
	public String updateList (HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("addressbook_id")int addressBookId) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();
		}		
		if (frontUser==null){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"未登录"));
			return "";
		}
		AddressBookCriteria addressBookCriteria=new AddressBookCriteria();
		AddressBook addressBook=new AddressBook();
		addressBookCriteria.setUuid(frontUser.getUuid());
		addressBook.setOwnerId(frontUser.getOwnerId());
		addressBookCriteria.setAddressBookId(String.valueOf(addressBookId));
		try{
			addressBook=addressBookService.select(String.valueOf(addressBookId));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		map.put("pageTitle","编辑地址");
		map.put("editdetail", addressBook);
		return "addressBook/update";
	}
	//改
	@RequestMapping (value="/update",method=RequestMethod.POST)
	public String update (HttpServletRequest request, HttpServletResponse response, ModelMap map, AddressBook addressBook) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}		
		if (frontUser==null){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"未登录"));
			return "";
		}
		AddressBook _oldAddressBook = addressBookService.select(""+addressBook.getAddressBookId());
		if(_oldAddressBook == null){
			logger.error("找不到要更新的地址本:" + addressBook.getAddressBookId());
			map.put("message",new EisMessage(OperateResult.success.getId(),"更新失败"));
			logger.info("更新失败");
		}
		if(_oldAddressBook.getOwnerId() != frontUser.getOwnerId()){
			logger.error("要更新的地址本:" + _oldAddressBook.getAddressBookId() + "，其ownerId[" + _oldAddressBook.getOwnerId() + "]与当前用户[" + frontUser.getUuid() + "]的ownerId=" + frontUser.getOwnerId() + "不符" );
			map.put("message",new EisMessage(OperateResult.success.getId(),"更新失败"));
			logger.info("更新失败");
		}
		addressBook.setUuid(frontUser.getUuid());
		addressBook.setOwnerId(frontUser.getOwnerId());
		map.put("message",new EisMessage(OperateResult.success.getId(),"更新成功"));
		try{
			addressBookService.update(addressBook);
		}
		catch(Exception e){
			map.put("message",new EisMessage(OperateResult.success.getId(),"更新失败"));
			logger.info("更新失败");
		}
		return "";
	}
	@RequestMapping (value="/index",method=RequestMethod.GET)
	public String index (HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));
			return CommonStandard.frontMessageView;
		}
		
		
		
		int rowsPerPage = 100;
		int currentPage = 1;
		logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+currentPage+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		AddressBookCriteria addressBookCriteria=new AddressBookCriteria();
		addressBookCriteria.setUuid(frontUser.getUuid());
		Paging page = new Paging(rowsPerPage);
		addressBookCriteria.setPaging(page);
		addressBookCriteria.setOwnerId(frontUser.getOwnerId());
		addressBookCriteria.getPaging().setCurrentPage(currentPage);
		int totalRows = addressBookService.count(addressBookCriteria);
		ContentPaging documentContentPaging = new ContentPaging(totalRows);
		documentContentPaging.setRowsPerPage(rowsPerPage);
		documentContentPaging.setCurrentPage(currentPage);
		documentContentPaging.setDisplayPageCount(totalRows);
		documentContentPaging.caculateDisplayPage();
		List<AddressBook> addressBook = addressBookService.listOnPage(addressBookCriteria);
		map.put("pageTitle","地址管理");
		map.put("documentContentPaging", documentContentPaging);
		map.put("rowsPerPage",rowsPerPage);
		map.put("currentPage",currentPage);
		map.put("addressBook", addressBook);
		return "addressBook/index";	
	}	
	//查
	@RequestMapping (value="/list_{pageSetting}",method=RequestMethod.GET)
	public String List1 (HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("pageSetting") String pageSetting) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));
			return CommonStandard.frontMessageView;
		}		
		int rowsPerPage = 3;
		int currentPage = 1;
		if(pageSetting.matches("^\\d+_\\d+$")){
			String[] data = pageSetting.split("_");
			rowsPerPage = Integer.parseInt(data[0]);
			currentPage = Integer.parseInt(data[1]);
		} else 	if(pageSetting.matches("^\\d+$")){
			rowsPerPage = 3;
			currentPage = Integer.parseInt(pageSetting);
		} else {
			rowsPerPage = 3;
			currentPage = 1;		
		}
		logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+currentPage+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		AddressBookCriteria addressBookCriteria=new AddressBookCriteria();
		addressBookCriteria.setUuid(frontUser.getUuid());
		addressBookCriteria.setOwnerId(frontUser.getOwnerId());
		Paging page = new Paging(3);
		addressBookCriteria.setPaging(page);
		addressBookCriteria.getPaging().setCurrentPage(currentPage);
		int totalRows = addressBookService.count(addressBookCriteria);
		ContentPaging documentContentPaging = new ContentPaging(totalRows);
		documentContentPaging.setRowsPerPage(rowsPerPage);
		documentContentPaging.setCurrentPage(currentPage);
		documentContentPaging.setDisplayPageCount(2);
		documentContentPaging.caculateDisplayPage();
		List<AddressBook> addressBook = addressBookService.listOnPage(addressBookCriteria);
		//List<AddressBook> addressBook =addressBookServer.list(addressBookCriteria);
		map.put("documentContentPaging", documentContentPaging);
		map.put("rowsPerPage",rowsPerPage);
		map.put("currentPage",currentPage);
		map.put("addressBook", addressBook);
		return "addressBook/list";	
	}
	@RequestMapping (value="/setDefault/{addressBookId}",method=RequestMethod.GET)
	public String setDefaultAdd(HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("addressBookId") int addressBookId)throws Exception 
	{
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录"));
			return CommonStandard.frontMessageView;
		}		
		AddressBook addressBook=new AddressBook();
		addressBook.setAddressBookId(addressBookId);
		if (addressBookService.setDefaultAdd(addressBook)>0) {
			map.put("message", new EisMessage(OperateResult.success.getId(), "设置成功"));
			return ":true";
		}
		else{
			map.put("message", new EisMessage(OperateResult.failed.getId(), "设置失败"));
			return "false";
		}

	}
}
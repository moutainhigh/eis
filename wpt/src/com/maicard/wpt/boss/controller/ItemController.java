package com.maicard.wpt.boss.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.service.AddressBookService;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.service.MessageService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.domain.ProductServer;
import com.maicard.product.service.ItemDataService;
import com.maicard.product.service.ItemLogService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.NotifyService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.ts.SettlementStatus;
/**
 * 
 * 基于之前的ItemDataController修改的ItemController
 * 
 * 
 * @author NetSnake
 * 2015-11-04
 * 
 * 
 * 
 * 交易记录
 * 
 * 
 * @author SimonSun
 * 
 */
@Controller
@RequestMapping("/item")
public class ItemController extends BaseController {

	@Resource
	private AddressBookService addressBookService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private MessageService messageService;
	@Resource
	private ItemService itemService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ProductService productService;
	@Resource
	private NotifyService notifyService;
	@Resource
	private ItemLogService itemLogService;
	@Resource
	private ItemDataService itemDataService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private ShareConfigService shareConfigService;

	private final String historyTableName = "item_history";

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			ItemCriteria itemCriteria)
					throws Exception {

		final String view = "item/list";
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
		User sysUser = certifyService.getLoginedUser(request, response, UserTypes.sysUser.getId());
		if(sysUser == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		itemCriteria.setOwnerId(sysUser.getOwnerId());
		logger.debug("当前查询订单的ownerId=" + itemCriteria.getOwnerId());
		String user = itemCriteria.getUsername();
		if(user != null){
			if(NumericUtils.isNumeric(user.trim())){
				User frontUser = frontUserService.select(Long.parseLong(user.trim()));
				if(frontUser == null){
					logger.warn("找不到UUID=" + user + "的前端用户");
					return view;
				}
				if(frontUser.getOwnerId() != sysUser.getOwnerId()){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUser.getOwnerId() + "]与登录系统用户的ownerId[" + sysUser.getOwnerId() + "]不匹配");
					return view;
				}
				itemCriteria.setChargeFromAccount(Long.parseLong(user.trim()));
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
				if(frontUserList.get(0).getOwnerId() != sysUser.getOwnerId()){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与登录系统用户的ownerId[" + ownerId + "]不匹配");
					return view;
				}
				itemCriteria.setChargeFromAccount(frontUserList.get(0).getUuid());
			}
			itemCriteria.setUsername(null);
		}
		String isQuery = request.getParameter("isQuery");
		if (isQuery == null) {
			isQuery = "1";
		}
		String currentStatusValue = request.getParameter("currentStatus");
		map.put("TransactionType",TransactionStandard.TransactionType.values());
		map.put("errorList", EisError.values());
		map.put("transactionStatus",
				TransactionStandard.TransactionStatus.values());
		map.put("addUrl", "./item/jump");
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 50);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.product.name());
		List<DataDefine> thisDataDefine = dataDefineService
				.list(dataDefineCriteria);
		ProductDataCriteria productDataCriteria = new ProductDataCriteria();
		productDataCriteria.setOwnerId(sysUser.getOwnerId());
		boolean exist = false;
		for (DataDefine pd : thisDataDefine) {
			String value = ServletRequestUtils.getStringParameter(request,
					pd.getDataCode());
			if (value != null && !value.trim().equals("")) {
				exist = true;
				ProductData pd1 = new ProductData();
				pd1.setDataDefineId(pd.getDataDefineId());
				pd1.setDataValue(value);
				productDataCriteria
				.setQueryCondition(new ArrayList<ProductData>());
				productDataCriteria.getQueryCondition().add(pd1);

			}
		}
		List<User> allChild = new ArrayList<User>();
		List <User> fitterChild=new ArrayList<User>();
		partnerService.listAllChildren(allChild, 300001);
		for (int i=0;i<allChild.size();i++){
			allChild.get(i).setUsername(allChild.get(i).getUsername().replaceAll("([.*])", ""));
			//logger.info("xxxxxxxxxxxxxxxxxxxxx"+allChild.get(i).getUsername()+"xxxxxxxxxxxxxxxx");
			fitterChild.add(allChild.get(i));
		}
		map.put("inviterList", fitterChild);
		map.put("thisDataDefine", thisDataDefine);
		if (StringUtils.isBlank(ServletRequestUtils.getStringParameter(request,
				"page"))) {
			return view;

		}
		int totalRows;
		List<Item> itemList = null;
		// 未输入扩展数据
		if (!exist) {
			if (currentStatusValue != null) {
				if (Integer.parseInt(currentStatusValue) != 0) {
					itemCriteria.setCurrentStatus(Integer.parseInt(currentStatusValue));
				}else{
					itemCriteria.setCurrentStatus(null);
				}
			}
			if (itemCriteria.getTransactionId() != null
					&& itemCriteria.getTransactionId().equals(""))
				itemCriteria.setTransactionId(null);
			Paging paging = new Paging(rows);
			itemCriteria.setPaging(paging);
			itemCriteria.getPaging().setCurrentPage(page);
			itemCriteria.setTableName(historyTableName);

			if (Integer.parseInt(isQuery) == 0) {
				itemCriteria.setQueryProcessingItem(DataName.both.toString());
			} else {
				itemCriteria.setQueryProcessingItem(DataName.only.toString());
			}
			SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
			logger.info("查询的transactionId=" + itemCriteria.getTransactionId() + ",beginTime=" + sdf.format(itemCriteria.getEnterTimeBegin()) + ",endTime=" + sdf.format(itemCriteria.getCloseTimeEnd()));
			totalRows = itemService.count(itemCriteria);
			itemList = itemService.listOnPage(itemCriteria);
			logger.debug("无扩展查询获得的数据:" + (itemList == null ? "空" : itemList.size()));

		} else {
			productDataCriteria.setQueryCondition(productDataCriteria
					.getQueryCondition());
			productDataCriteria.setQueryConditonSize(productDataCriteria
					.getQueryCondition().size());
			List<Long> itemDataList = itemDataService.listBy(productDataCriteria);
			if (itemDataList.size() != 0) {
				long[] productID = new long[itemDataList.size()];
				for (int i = 0; i < itemDataList.size(); i++) {
					if (itemDataList.get(i) >= 0) {
						productID[i] = itemDataList.get(i);
					}
				}
				itemCriteria.setItemIds(productID);
			} else {
				itemCriteria.setItemIds(null);
				itemCriteria.setTransactionId("0");
			}
			if (currentStatusValue != null) {

				if (Integer.parseInt(currentStatusValue) != 0) {

					itemCriteria.setCurrentStatus(Integer
							.parseInt(currentStatusValue));

				}else{

					itemCriteria.setCurrentStatus(null);
				}
			}
			itemCriteria.setTableName(historyTableName);
			if (itemCriteria.getTransactionId() != null
					&& itemCriteria.getTransactionId().equals(""))
				itemCriteria.setTransactionId(null);

			Paging paging = new Paging(rows);
			itemCriteria.setPaging(paging);
			itemCriteria.getPaging().setCurrentPage(page);

			if (Integer.parseInt(isQuery) == 0) {
				itemCriteria.setQueryProcessingItem(DataName.both.toString());

			} else {
				itemCriteria.setQueryProcessingItem(DataName.only.toString());
			}
			logger.info("wwwwwwwwwwwww" + itemCriteria.getQueryProcessingItem());
			totalRows = itemService.count(itemCriteria);
			itemList = itemService.listOnPage(itemCriteria);
			logger.debug("有扩展查询获得的数据:" + (itemList == null ? "空" : itemList.size()));
		}

		List<Item> itemList2 = new ArrayList<Item>();
	
		if (itemList != null && itemList.size() > 0) {
			for (Item i : itemList) {
				logger.debug("把订单[" + i.getTransactionId() + "]放入itemList2");
				Item item = i.clone();
				//Product product = productService.select(item.getProductId());
				User itemUser  = frontUserService.select(item.getChargeFromAccount());
				
				if(itemUser != null){
					item.setChargeFromAccountName(itemUser.getNickName() + "/" + itemUser.getUsername());
				} else {
					item.setChargeFromAccountName("");
				}

				if(item.getInOrderId() != null && StringUtils.isNumeric(item.getInOrderId())){
					AddressBook addressBook = addressBookService.select(item.getInOrderId());
					if(addressBook != null){
						String address = (addressBook.getProvince() == null ? "" : addressBook.getProvince() + " ")
								+ (addressBook.getCity() == null ? "" : addressBook.getCity() + " ")
								+ (addressBook.getDistrict() == null ? "" : addressBook.getDistrict() + " ")
								+ (addressBook.getAddress() == null ? "" : addressBook.getAddress() + " ")
								+ (addressBook.getContact() == null ? "" : addressBook.getContact() + " ")
								+ (addressBook.getMobile() == null ? "" : addressBook.getMobile());
						item.setInOrderId(address);
					}
				}
				item.setOperate(new HashMap<String, String>());
				item.getOperate().put("update",
						"/item/update/" + item.getTransactionId());
				item.getOperate().put("get",
						"/item/get/" + item.getTransactionId());
				item.getOperate().put("preview",
						"./item/preview/" + item.getTransactionId());
				item.getOperate().put("relate",
						"./item/relate/" + item.getTransactionId());
				
				itemList2.add(item);
			}
		} else {
			logger.debug("当前未返回任何记录");
		}
		// map.put("thisDataDefine", thisDataDefine);
		map.put("total", totalRows);
		map.put("rows", itemList2);


		return view;
	}

	

	@RequestMapping("/jump")
	public String insertMatch(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("idList") String idList) throws Exception {
		if (idList == null || idList.equals("")) {
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
		
		String transactionId[] = idList.split("-");
		String contentWord = "";

		for (int i = 0; i < transactionId.length; i++) {

			Item item = itemService.select(transactionId[i]);
			if (item != null) {
				if(item.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
					return CommonStandard.backMessageView;
				}
				contentWord += "<li>订单" + transactionId[i] + "操作结果:"
						+ notifyService.syncSendNotify(item) + "</li>";

			} else
				map.put("message", new EisMessage(
						OperateResult.failed.getId(),
						"user对象获取失败！！"));
		}

		map.put("message",
				new EisMessage(OperateResult.success.getId(),
						contentWord));

		return CommonStandard.backMessageView;

	}

	@RequestMapping(value = "/update/" + "{transactionId}")
	public String retry(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId)
					throws Exception {
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
		Item i = itemService.select(transactionId);
		logger.info("transactionId是" + transactionId);
		if (i != null) {
			if(i.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.backMessageView;
			}
			logger.info("cgsbaaaaaaaaaaaaaaaaa"
					+ notifyService.syncSendNotify(i));

			map.put("message",
					new EisMessage(
							OperateResult.success.getId(),
							notifyService.syncSendNotify(i)));

		} else
			map.put("message",
					new EisMessage(OperateResult.failed.getId(),
							"user对象获取失败！！"));
		return CommonStandard.backMessageView;
	}

	@RequestMapping(value = "/preview/" + "{transactionId}")
	public String statusEdit(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId)
					throws Exception {
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
		Item item = itemService.select(transactionId);
		if (item != null) {
			if(item.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.backMessageView;
			}
		}
		
		int serverId = 0;
		try {
			serverId = Integer.parseInt(item.getItemDataMap()
					.get(DataName.productServer.toString()).getDataValue());
		} catch (Exception e) {
		}
		if (serverId > 0) {
			
		} else {
			if (item == null)
				logger.info("xxxxxxxxxxxxxxxxxxxxxxxxitem为空！！xxxxxxxxxxxxxxxxxxxxxxxx");
			Product product = productService.select(item.getProductId());
			if (product != null) {
				if (item.getItemDataMap() == null) {
					item.setItemDataMap(new HashMap<String, ProductData>());
				}
				if (item.getItemDataMap()
						.get(DataName.productSmallImage.toString()) == null) {
					item.getItemDataMap().put(
							DataName.productSmallImage.toString(),
							new ProductData());
				}
				item.getItemDataMap()
				.get(DataName.productSmallImage.toString())
				.setDataValue(
						product.getProductName() + "["
								+ product.getProductId() + "]");

			}
		}

		map.put("item", item);
		map.put("errorList", EisError.values());
		map.put("transactionStatus",
				TransactionStandard.TransactionStatus.values());

		return "itemData/edit";
	}

	@RequestMapping(value = "/relate/" + "{transactionId}")
	public String relateEdit(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId)
					throws Exception {
		Item item = itemService.select(transactionId);
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
		if (item != null) {
			if(item.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.backMessageView;
			}
		}
		Float singleSupplement = item.getLabelMoney() - item.getSuccessMoney();
		String productAccountSerialName = "";
		if (item.getItemDataMap() != null) {
			if (item.getTransactionTypeId() == 12
					&& item.getItemDataMap().get("productAccountName") != null) {
				productAccountSerialName = item.getItemDataMap()
						.get("productAccountName").getDataValue();
			} else if (item.getTransactionTypeId() == 13
					&& item.getItemDataMap().get("productSerialNumber") != null) {
				productAccountSerialName = item.getItemDataMap()
						.get("productSerialNumber").getDataValue();
			}
		} else {
			productAccountSerialName = "未知";

		}

		map.put("productAccountSerialName", productAccountSerialName);
		map.put("singleSupplement", singleSupplement);
		map.put("item", item);

		return "itemData/preview";
	}

	@RequestMapping(value = "/relate", method = RequestMethod.POST)
	public String relatePost(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
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
		try {
			Item i = itemService.select(request.getParameter("transactionId"));
			if (i != null) {
				if(i.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
					return CommonStandard.backMessageView;
				}
			}
			i.setTtl((int) new java.util.Date().getTime()
					/ 1000
					- (int) i.getEnterTime().getTime()
					/ 1000
					+ Integer.parseInt(request
							.getParameter("singleSupplementTime")));
			logger.info("wwwwwww" + i.getTtl());
			i.setRequestMoney(Float.parseFloat(request
					.getParameter("singleSupplement")));
			i.setFrozenMoney(0);
			i.setCurrentStatus(710021);
			itemService.update(i);
			map.put("message", new EisMessage(
					OperateResult.success.getId(), "补单成功"));
		} catch (Exception e) {
			String message = "数据操作失败" + e.getMessage();
			logger.error(message);
			throw new DataWriteErrorException(message);
		}

		return CommonStandard.backMessageView;

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatedetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@ModelAttribute("item") Item item) throws Exception {
		User sysUser = certifyService.getLoginedUser(request, response, UserTypes.sysUser.getId());
		if(sysUser == null){
			return null;
		}
		boolean fullModify = false;
		if(sysUser.getRelatedRoleList() != null){
			for(Role role : sysUser.getRelatedRoleList()){
				if( role.getRoleId() == 800006){
					logger.info("用户[" + sysUser.getUsername() + "]是运营总监800006，可以完全修改数据");
					fullModify = true;
				}
			}
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
		try {
			Item i = itemService.select(request.getParameter("transactionId"));
			if (i != null) {
				if(i.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
					return CommonStandard.backMessageView;
				}
			}
			i.setTtl(item.getTtl());
			i.setWeight(item.getWeight());

			if(item.getFrozenMoney() >= 0){
				i.setFrozenMoney(item.getFrozenMoney());
			}
			if(fullModify){
				i.setSuccessMoney(item.getSuccessMoney());
				i.setRequestMoney(item.getRequestMoney());
			} else {
				if(item.getSuccessMoney() > i.getSuccessMoney()){
					i.setSuccessMoney(item.getSuccessMoney());
				}
				i.setRequestMoney(i.getLabelMoney() - i.getSuccessMoney() - i.getFrozenMoney());
			}
			i.setCurrentStatus(item.getCurrentStatus());
			i.setOutStatus(item.getCurrentStatus());

			/*i.setSuccessMoney(Float.parseFloat(request
					.getParameter("successMoney")));
			i.setRequestMoney(Float.parseFloat(request
					.getParameter("requestMoney")));
			i.setFrozenMoney(Float.parseFloat(request
					.getParameter("frozenMoney")));


			i.setCurrentStatus(Integer.parseInt(request
					.getParameter("currentStatus")));
			i.setOutStatus(Integer.parseInt(request
					.getParameter("currentStatus")));
			 */
			if(i.getCurrentStatus() == TransactionStatus.success.getId()){
				//shareConfigService.calculateShare(i);
				i.setBillingStatus(SettlementStatus.billed.id);
				notifyService.sendNotify(i);
			} else {
				i.setBillingStatus(0);
			}
			itemService.update(i);
			messageService.sendJmsDataSyncMessage(null, "itemService",
					"update", i);
			map.put("message", new EisMessage(
					OperateResult.success.getId(), "更新成功"));
		} catch (Exception e) {
			String message = "数据操作失败" + e.getMessage();
			logger.error(message);
			throw new DataWriteErrorException(message);
		}

		return CommonStandard.backMessageView;

	}

	@RequestMapping(value = "/get/" + "{transactionId}")
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId)
					throws Exception {
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
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setTransactionId(transactionId);
		List<Item> log = itemLogService.list(itemCriteria);
		
		Item item = itemService.select(transactionId);
		if (item != null) {
			if(item.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.backMessageView;
			}
		}
		int serverId = 0;
		try {
			serverId = Integer.parseInt(item.getItemDataMap()
					.get(DataName.productServer.toString()).getDataValue());
		} catch (Exception e) {
		}
		if (serverId > 0) {
			
		} else {
			if (item == null)
				logger.info("xxxxxxxxxxxxxxxxxxxxxxxxitem为空！！xxxxxxxxxxxxxxxxxxxxxxxx");
			Product product = productService.select(item.getProductId());
			if (product != null) {
				if (item.getItemDataMap() == null) {
					item.setItemDataMap(new HashMap<String, ProductData>());
				}
				if (item.getItemDataMap()
						.get(DataName.productSmallImage.toString()) == null) {
					item.getItemDataMap().put(
							DataName.productSmallImage.toString(),
							new ProductData());
				}
				item.getItemDataMap()
				.get(DataName.productSmallImage.toString())
				.setDataValue(
						product.getProductName() + "["
								+ product.getProductId() + "]");

			}
		}

		map.put("item", item);
		map.put("log", log);
		return "itemData/get";
	}

	@RequestMapping(value = "/create/{transactionId}", method = RequestMethod.GET)
	@ResponseBody
	public String getCreate(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId,
			@ModelAttribute("itemCriteria") ItemCriteria itemCriteria)
					throws Exception {
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
		Item i = itemService.select(transactionId);
		
		if (i != null) {
			if(i.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.backMessageView;
			}
		}
		i.setCurrentStatus(TransactionStatus.needProcess.getId());

		itemService.changeStatus(i);
		if (itemService.changeStatus(i) > 0) {

			return "true";
		} else {

			return "false";
		}

	}
}

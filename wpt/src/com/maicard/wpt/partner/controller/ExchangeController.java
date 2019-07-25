package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.StatusUtils;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.service.AddressBookService;
import com.maicard.ec.service.DeliveryCompanyService;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.service.MessageService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.domain.ProductServer;
import com.maicard.product.service.CartService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.product.service.ItemDataService;
import com.maicard.product.service.ItemLogService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.NotifyService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
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
 * 用于团购、抢购的兑奖
 * 查找用户提交的订单号或兑换卡号密码
 * 手工给用户兑奖
 * 然后修改该订单为已兑奖状态
 *
 *
 * @author NetSnake
 * @date 2017年2月27日
 *
 */
@Controller
@RequestMapping("/exchange")
public class ExchangeController extends BaseController {

	@Resource
	private AddressBookService addressBookService;
	@Resource
	private ConfigService configService;

	@Resource
	private DeliveryCompanyService deliveryCompanyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private CartService cartService;
	@Resource
	private DeliveryOrderService deliveryOrderService;
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
	@Resource
	private AuthorizeService authorizeService;

	private final String historyTableName = "item_history";

	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, ItemCriteria itemCriteria) throws Exception {

		final String view = "common/tuan/exchange/index";
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		
		
		itemCriteria.setOwnerId(partner.getOwnerId());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		itemCriteria.setPaging(paging);
		itemCriteria.getPaging().setCurrentPage(page);
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(itemCriteria, partner);
		}
		
		List<Item> itemList2 = _list(request, partner, itemCriteria, map);
		map.put("title", "团购兑奖");
		map.put("rows", itemList2);

		List<User> deliveryCompanyList = deliveryCompanyService.list();
		Map<String,String> deliveryCompanyMap = new HashMap<String,String>();
		if(deliveryCompanyList == null || deliveryCompanyList.size() < 1){
			logger.warn("当前系统未配置任何快递公司");
		} else {
			for(User dc : deliveryCompanyList){
				String companyName = (dc.getUsername().charAt(0)) + "-" + dc.getNickName();
				deliveryCompanyMap.put(dc.getUsername(), companyName);
			}
			//排序

		}
		map.put("deliveryCompanyMap", deliveryCompanyMap);
		return view;
	}

	private List<Item> _list(HttpServletRequest request, User partner, ItemCriteria itemCriteria, ModelMap map) throws Exception{

		itemCriteria.setOwnerId(partner.getOwnerId());
		String user = itemCriteria.getUsername();
		if(user != null && user != ""){
			if(StringUtils.isNumeric(user.trim())){
				User frontUser = frontUserService.select(Long.parseLong(user.trim()));
				if(frontUser == null){
					logger.warn("找不到UUID=" + user + "的前端用户");
					return null;
				}
				if(frontUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUser.getOwnerId() + "]与登录系统用户的ownerId[" + partner.getOwnerId() + "]不匹配");
					return null;
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
						return null;
					}
				}
				if(frontUserList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与登录系统用户的ownerId[" + partner.getOwnerId() + "]不匹配");
					return null;
				}
				itemCriteria.setChargeFromAccount(frontUserList.get(0).getUuid());
			}
			itemCriteria.setUsername(null);
		}
		
//		itemService.fixQueryTime(itemCriteria);
		//是否只查询item表
		if(itemCriteria.getQueryProcessingItem() == null){
			itemCriteria.setQueryProcessingItem(DataName.both.toString());
		}
		/*String queryItemTableOnly = request.getParameter("isQuery");
		if (queryItemTableOnly == null) {
			//默认只查询item表
			queryItemTableOnly = "1";
		}*/
		String currentStatusValue = request.getParameter("currentStatus");
		map.put("TransactionType",TransactionStandard.TransactionType.values());

		map.put("statusList",StatusUtils.getAllStatusValue(new String[]{}));
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.product.name());
		List<DataDefine> thisDataDefine = dataDefineService.list(dataDefineCriteria);
		ProductDataCriteria productDataCriteria = new ProductDataCriteria();
		productDataCriteria.setOwnerId(partner.getOwnerId());
		boolean exist = false;
		for (DataDefine pd : thisDataDefine) {
			String value = ServletRequestUtils.getStringParameter(request,pd.getDataCode());
			if (value != null && !value.trim().equals("")) {
				exist = true;
				ProductData pd1 = new ProductData();
				pd1.setDataDefineId(pd.getDataDefineId());
				pd1.setDataValue(value);
				productDataCriteria.setQueryCondition(new ArrayList<ProductData>());
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
		/*		if (StringUtils.isBlank(ServletRequestUtils.getStringParameter(request,
				"page"))) {
			return view;

		}*/
		
		int totalRows;
		List<Item> itemList = null;
		// 未输入扩展数据
		if (!exist) {
			if (NumericUtils.isNumeric(currentStatusValue)) {
				if (Integer.parseInt(currentStatusValue) != 0) {
					itemCriteria.setCurrentStatus(Integer.parseInt(currentStatusValue));
				}else{
					itemCriteria.setCurrentStatus(null);
				}
			} else {
				itemCriteria.setCurrentStatus(null);
			}
			if (itemCriteria.getTransactionId() != null && itemCriteria.getTransactionId().equals(""))
				itemCriteria.setTransactionId(null);


			itemCriteria.setTableName(historyTableName);

			/*if (Integer.parseInt(queryItemTableOnly) == 0) {
				itemCriteria.setQueryProcessingItem(DataName.both.toString());
			} else {
				itemCriteria.setQueryProcessingItem(DataName.only.toString());
			}*/
			logger.info("无扩展数据查询，查询的表条件:" + itemCriteria.getQueryProcessingItem());

			totalRows = itemService.count(itemCriteria);
			if(totalRows < 1){
				logger.debug("当前返回的数据行数是0");
			}
			itemList = itemService.listOnPage(itemCriteria);

		} else {
			productDataCriteria.setQueryCondition(productDataCriteria.getQueryCondition());
			productDataCriteria.setQueryConditonSize(productDataCriteria.getQueryCondition().size());
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

					itemCriteria.setCurrentStatus(Integer.parseInt(currentStatusValue));

				}else{

					itemCriteria.setCurrentStatus(null);
				}
			}
			itemCriteria.setTableName(historyTableName);
			if (itemCriteria.getTransactionId() != null && itemCriteria.getTransactionId().equals(""))
				itemCriteria.setTransactionId(null);



			/*if (Integer.parseInt(queryItemTableOnly) == 0) {
				itemCriteria.setQueryProcessingItem(DataName.both.toString());

			} else {
				itemCriteria.setQueryProcessingItem(DataName.only.toString());
			}*/
			logger.info("有扩展数据查询，查询的表条件:" + itemCriteria.getQueryProcessingItem());
			totalRows = itemService.count(itemCriteria);
			if(totalRows < 1){
				logger.debug("当前返回的数据行数是0");
			}
			itemList = itemService.listOnPage(itemCriteria);
		}
		
		List<Item> itemList2 = new ArrayList<Item>();

		if (itemList != null && itemList.size() > 0) {
			for (Item i : itemList) {
				Item item = i.clone();
				//Product product = productService.select(item.getProductId());
				User itemUser  = frontUserService.select(item.getChargeFromAccount());
				
				if(itemUser != null){
					item.setChargeFromAccountName(itemUser.getNickName() + "/" + itemUser.getUsername());
				} else {
					logger.warn("找不到交易[" + item.getTransactionId() + "]对应的用户:" + item.getChargeFromAccount());
					item.setChargeFromAccountName("");
				}
				long deliveryOrderId = item.getLongExtraValue(DataName.deliveryOrderId.toString());

				if(deliveryOrderId > 0){
					DeliveryOrder deliveryOrder = deliveryOrderService.select(deliveryOrderId);
					if(deliveryOrder != null){
						String brief = deliveryOrder.getBrief();
						item.setInOrderId(brief);
						if(item.getItemDataMap() == null){
							item.setItemDataMap(new HashMap<String,ProductData>());
						}
						item.getItemDataMap().put(DataName.deliveryOrderBrief.toString(), new ProductData(DataName.deliveryOrderBrief.toString(),brief));
					} 
				}
				
				itemList2.add(item);
			}
		}
		//计算并放入分页
		if(itemCriteria.getPaging() != null){
			map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, itemCriteria.getPaging().getMaxResults(), itemCriteria.getPaging().getCurrentPage()));
		}
		map.put("total", totalRows);

		return itemList2;
	}

	@RequestMapping("/jump")
	public String insertMatch(HttpServletRequest request, HttpServletResponse response, ModelMap map,
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
			return CommonStandard.partnerMessageView;
		}

		String transactionId[] = idList.split("-");
		String contentWord = "";

		for (int i = 0; i < transactionId.length; i++) {

			Item item = itemService.select(transactionId[i]);
			if (item != null) {
				if(item.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
					return CommonStandard.partnerMessageView;
				}
				contentWord += "<li>订单" + transactionId[i] + "操作结果:" + notifyService.syncSendNotify(item) + "</li>";

			} else
				map.put("message", new EisMessage(OperateResult.failed.getId(), "user对象获取失败！！"));
		}

		map.put("message", new EisMessage(OperateResult.success.getId(), contentWord));

		return CommonStandard.partnerMessageView;

	}
	//兑奖
	@RequestMapping(value = "/update/" + "{transactionId}", method=RequestMethod.POST)
	public String retry(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId) throws Exception {
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
		Item i = itemService.select(transactionId);
		logger.info("transactionId是" + transactionId);
		if (i != null) {
			if(i.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.partnerMessageView;
			}
			logger.info("cgsbaaaaaaaaaaaaaaaaa" + notifyService.syncSendNotify(i));
			Item itemClone = i.clone();
			if (itemClone.getExtraStatus() == TransactionStatus.closed.id) {
				map.put("message", new EisMessage(OperateResult.failed.getId(), "已兑奖！"));
				return CommonStandard.partnerMessageView;
			}
			itemClone.setExtraStatus(TransactionStatus.closed.id);
			int changeResult = itemService.changeStatus(itemClone);
			if (changeResult > 0) {
				map.put("message", new EisMessage(OperateResult.success.getId(), "兑奖成功！"));
			} else {
				map.put("message", new EisMessage(OperateResult.failed.getId(), "兑奖失败！"));
			}

		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "user对象获取失败！！"));
		}
		
		return CommonStandard.partnerMessageView;
	}

	@RequestMapping(value = "/preview/" + "{transactionId}")
	public String statusEdit(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId) throws Exception {
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
		Item item = itemService.select(transactionId);
		if (item != null) {
			if(item.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.partnerMessageView;
			}
		}

		int serverId = 0;
		try {
			serverId = Integer.parseInt(item.getItemDataMap().get(DataName.productServer.toString()).getDataValue());
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
				if (item.getItemDataMap().get(DataName.productSmallImage.toString()) == null) {
					item.getItemDataMap().put(DataName.productSmallImage.toString(), new ProductData());
				}
				item.getItemDataMap().get(DataName.productSmallImage.toString()).setDataValue(product.getProductName() + "[" + product.getProductId() + "]");

			}
		}

		map.put("item", item);
		map.put("errorList", EisError.values());
		map.put("transactionStatus", TransactionStandard.TransactionStatus.values());

		return "common/item/edit";
	}


	//创建快递单
	@RequestMapping(value = "/relate", method = RequestMethod.POST)
	public String relatePost(HttpServletRequest request, HttpServletResponse response, ModelMap map, String transactionId,
			@RequestParam(value="outDeliveryOrderId",required=false) String outDeliveryOrderId,
			@RequestParam(value="deliveryCompanyName",required=false) String deliveryCompanyName) throws Exception {
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

		Item i = itemService.select(transactionId);
		if (i == null) {
			logger.error("找不到进行发货的订单[" + transactionId + "]");
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.id,"找不到指定的订单"));
			return CommonStandard.partnerMessageView;		
		}
		if(i.getOwnerId() != ownerId){
			logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.id,"找不到指定的订单"));
			return CommonStandard.partnerMessageView;		
		}
		if(i.getCurrentStatus() != TransactionStatus.delivering.id){
			logger.debug("订单[" + i.getTransactionId() + "]状态是:" + i.getCurrentStatus() + ",设置为发货中");
			Item item = new Item();
			item.setItemId(i.getItemId());
			item.setTransactionId(i.getTransactionId());
			item.setCurrentStatus(i.getCurrentStatus());
			itemService.updateNoNull(item);
		} else {
			logger.debug("订单[" + i.getTransactionId() + "]状态是发货中:" + i.getCurrentStatus() + ",仅更新快递单号");
		}
		if(StringUtils.isBlank(outDeliveryOrderId)){
			logger.debug("当前提交的快递单号是空，不进行更新");
			map.put("message", new EisMessage(OperateResult.success.getId(),"订单已变更为发货状态，不更新快递单号"));
			return CommonStandard.partnerMessageView;		
		}
		outDeliveryOrderId = outDeliveryOrderId.trim();
		if(i.getItemDataMap() != null && i.getItemDataMap().get(DataName.deliveryOrderId.toString()) != null){
			ProductData pd = i.getItemDataMap().get(DataName.deliveryOrderId.toString());
			logger.debug("当前交易[" + i.getTransactionId() + "]已有快递单号数据[" + pd.getDataValue() + ",更新为新的快递单号:" + outDeliveryOrderId);
			pd.setDataValue(outDeliveryOrderId);
			itemDataService.update(pd);
		} else {
			ProductData pd = new ProductData(DataName.deliveryOrderId.toString(), outDeliveryOrderId.trim());
			logger.debug("当前交易[" + i.getTransactionId() + "]没有快递单号数据，新增新的快递单号:" + outDeliveryOrderId);
			pd.setProductId(i.getItemId());
			itemDataService.insert(pd);
		}
		deliveryCompanyName = deliveryCompanyName.trim();
		if(i.getItemDataMap() != null && i.getItemDataMap().get(DataName.deliveryCompanyName.toString()) != null){
			ProductData pd = i.getItemDataMap().get(DataName.deliveryCompanyName.toString());
			logger.debug("当前交易[" + i.getTransactionId() + "]已有快递公司数据[" + pd.getDataValue() + ",更新为新的快递公司:" + deliveryCompanyName);
			pd.setDataValue(deliveryCompanyName);
			itemDataService.update(pd);
		} else {
			ProductData pd = new ProductData(DataName.deliveryCompanyName.toString(), deliveryCompanyName.trim());
			logger.debug("当前交易[" + i.getTransactionId() + "]没有快递公司数据，新增新的快递公司:" + deliveryCompanyName);
			pd.setProductId(i.getItemId());
			itemDataService.insert(pd);
		}
		if(i.getCartId() < 1){
			logger.warn("交易[" + i.getTransactionId() + "]没有cartId");
		} else {
			Cart cart = cartService.select(i.getCartId());
			if(cart == null){
				logger.error("找不到交易[" + i.getTransactionId() + "]对应的Cart[" + i.getCartId() + "]");
			} else {
				if(cart.getDeliveryOrderId() > 0){
					logger.debug("订单[" + cart.getCartId() + "]有关联配送单:" + cart.getDeliveryOrderId() + "]，更新其配送数据");
					DeliveryOrder deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
					if(deliveryOrder != null){
						deliveryOrder.setOutOrderId(outDeliveryOrderId);
						deliveryOrder.setDeliveryCompany(deliveryCompanyName);
						deliveryOrderService.update(deliveryOrder);

					} else {
						logger.debug("找不到订单[" + cart.getCartId() + "]关联的配送单:" + cart.getDeliveryOrderId() + "]");

					}
				} else {
					logger.debug("订单[" + cart.getCartId() + "]没有关联配送单");

				}
			}
		}
		map.put("message", new EisMessage(OperateResult.success.getId(),"订单已变更为发货状态，快递单号已更新"));
		return CommonStandard.partnerMessageView;		

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatedetail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("item") Item item) throws Exception {
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			return null;
		}
		boolean fullModify = false;

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
		try {
			Item i = itemService.select(request.getParameter("transactionId"));
			if (i != null) {
				if(i.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
					return CommonStandard.partnerMessageView;
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
			messageService.sendJmsDataSyncMessage(null, "itemService", "update", i);
			map.put("message", new EisMessage(OperateResult.success.getId(), "更新成功"));
		} catch (Exception e) {
			String message = "数据操作失败" + e.getMessage();
			logger.error(message);
			throw new DataWriteErrorException(message);
		}

		return CommonStandard.partnerMessageView;

	}

	@RequestMapping(value = "/get/" + "{transactionId}")
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId) throws Exception {
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
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setTransactionId(transactionId);
		List<Item> log = itemLogService.list(itemCriteria);
		
		Item item = itemService.select(transactionId);
		if (item != null) {
			if(item.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.partnerMessageView;
			}
		}
		int serverId = 0;
		try {
			serverId = Integer.parseInt(item.getItemDataMap().get(DataName.productServer.toString()).getDataValue());
		} catch (Exception e) {
		}
		if (serverId > 0) {
			
		} else {
			if (item == null){
				logger.info("xxxxxxxxxxxxxxxxxxxxxxxxitem为空！！xxxxxxxxxxxxxxxxxxxxxxxx");
				map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.id,"订单不存在"));
				return CommonStandard.partnerMessageView;
			} else {
				Product product = productService.select(item.getProductId());
				if (product != null) {
					if (item.getItemDataMap() == null) {
						item.setItemDataMap(new HashMap<String, ProductData>());
					}
					if (item.getItemDataMap().get(DataName.productSmallImage.toString()) == null) {
						item.getItemDataMap().put(DataName.productSmallImage.toString(),new ProductData());
					}
					item.getItemDataMap().get(DataName.productSmallImage.toString()).setDataValue(product.getProductName() + "[" + product.getProductId() + "]");

				}
			}
		}

		map.put("item", item);
		return "item/get";
	}

	@RequestMapping(value = "/create/{transactionId}", method = RequestMethod.GET)
	@ResponseBody
	public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId,
			@ModelAttribute("itemCriteria") ItemCriteria itemCriteria) throws Exception {
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
		Item i = itemService.select(transactionId);

		if (i != null) {
			if(i.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return CommonStandard.partnerMessageView;
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

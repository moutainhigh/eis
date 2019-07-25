package com.maicard.wpt.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.StringTools;
import com.maicard.common.util.TablePartitionUtils;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.MessageService;
import com.maicard.mb.service.UserMessageService;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.domain.ProductServer;
import com.maicard.product.service.CartService;
import com.maicard.ec.service.DeliveryCompanyService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.product.service.ItemDataService;
import com.maicard.product.service.ItemLogService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.NotifyService;
import com.maicard.product.service.ProductDataService;
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
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.ts.SettlementStatus;

import static com.maicard.standard.CommonStandard.partnerMessageView;
/**
 * 
 * 
 * 
 * 基于cart的订单管理
 * 
 * 
 * @author NetSnake
 * @date 2016-06-24
 * 

 * 
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {


	@Resource
	private ConfigService configService;


	/*
	 * XXX 对于EC模块的服务，使用@Autowired来调用
	 * 在某些不需要实体电商，即不使用到EC任何功能的情况下也可以正常工作
	 */
	@Autowired(required=false)
	private DeliveryCompanyService deliveryCompanyService;
	@Autowired(required=false)
	private DeliveryOrderService deliveryOrderService;
	
	@Resource
	private AuthorizeService authorizeService;

	@Resource
	private FrontUserService frontUserService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private CartService cartService;

	@Resource
	private MessageService messageService;
	@Resource
	private ItemService itemService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ProductService productService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductDataService productDataService;
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
	private UserMessageService userMessageService;
	private int rowsPerPage = 10;

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, CartCriteria cartCriteria) throws Exception {

		final String view = "common/order/list";
		map.put("title", "购买订单列表");
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
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		cartCriteria.setOwnerId(partner.getOwnerId());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		cartCriteria.setPaging(paging);
		cartCriteria.getPaging().setCurrentPage(page);
		cartCriteria.setOwnerId(partner.getOwnerId());
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			setSubPartner(cartCriteria, partner);
		}

		if (cartCriteria.getCurrentStatus() != null) {
			if (cartCriteria.getCurrentStatus().length == 0) {
				cartCriteria.setCurrentStatus(null);
			}
		}

		String user = ServletRequestUtils.getStringParameter(request, "username",null);
		if(StringUtils.isNotBlank(user)){
			if(NumericUtils.isNumeric(user.trim()) && !StringTools.isMobilePhone(user)){
				User frontUser = frontUserService.select(Long.parseLong(user.trim()));
				if(frontUser == null){
					logger.warn("找不到UUID=" + user + "的前端用户");
					return view;
				}
				if(frontUser.getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUser.getOwnerId() + "]与登录系统用户的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				cartCriteria.setUuid(Long.parseLong(user.trim()));
			} else {
				UserCriteria frontUserCriteria = new UserCriteria();
				frontUserCriteria.setNickName(user.trim());
				List<User> frontUserList = frontUserService.list(frontUserCriteria);
				if(frontUserList == null || frontUserList.size() < 1){
					logger.warn("找不到昵称=" + user + "的前端用户");
					UserCriteria frontUserCriteria1 = new UserCriteria();
					frontUserCriteria1.setUsername(user.trim());
					frontUserList = frontUserService.list(frontUserCriteria1 );
					if(frontUserList == null || frontUserList.size() < 1){
						logger.warn("找不到用户名=" + user + "的前端用户");
						frontUserCriteria.setUsername(null);
						frontUserCriteria.setNickName(null);
						return view;
					}
				}
				if(frontUserList.get(0).getOwnerId() != partner.getOwnerId()){
					logger.warn("UUID=" + user + "对应的前端用户，其ownerid[" + frontUserList.get(0).getOwnerId() + "]与登录系统用户的ownerId[" + partner.getOwnerId() + "]不匹配");
					return view;
				}
				cartCriteria.setUuid(frontUserList.get(0).getUuid());
			}
		}

		//产品代码和产品名称
		ProductCriteria productCriteria = new ProductCriteria(ownerId);

		boolean queryProduct = false;

		String productCode = ServletRequestUtils.getStringParameter(request, "productCode",null);
		if(StringUtils.isNotBlank(productCode)){
			productCriteria.setProductCode(productCode);
			queryProduct = true;
		}

		String productName = ServletRequestUtils.getStringParameter(request, "productName",null);
		int isFuzzyProductName = ServletRequestUtils.getIntParameter(request, "isFuzzyProductName", 0);
		if(StringUtils.isNotBlank(productName)){
			if(isFuzzyProductName > 0){
				productCriteria.setFuzzyProductName(productName);
			} else {
				productCriteria.setProductName(productName);
			}
			queryProduct = true;
		}
		if(queryProduct){
			List<Long> productIdPkList = productService.listPk(productCriteria);
			if(productIdPkList != null && productIdPkList.size() > 0){
				long[] productIds = new long[productIdPkList.size()];
				for(int i = 0 ; i < productIdPkList.size(); i++){
					productIds[i] = productIdPkList.get(i);
				}
				cartCriteria.setProductIds(productIds);
			}
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.product.name());
		List<DataDefine> thisDataDefine = dataDefineService.list(dataDefineCriteria);

		for (DataDefine dd : thisDataDefine) {
			String value = ServletRequestUtils.getStringParameter(request,dd.getDataCode(),null);
			if (StringUtils.isNotBlank(value)) {
				cartCriteria.getQueryExtraMap().put(String.valueOf(dd.getDataDefineId()), value.trim());
			}
		}
		List<User> allChild = new ArrayList<User>();
		List <User> fitterChild=new ArrayList<User>();
		partnerService.listAllChildren(allChild, 300001);
		for (int i=0;i<allChild.size();i++){
			allChild.get(i).setUsername(allChild.get(i).getUsername().replaceAll("([.*])", ""));
			fitterChild.add(allChild.get(i));
		}
		map.put("inviterList", fitterChild);
		map.put("thisDataDefine", thisDataDefine);
		int totalRows = cartService.count(cartCriteria);
		map.put("total", totalRows);
		if(totalRows < 1){
			logger.debug("当前返回的数据行数是0");
			return view;
		}
		List<Cart> cartList = cartService.listOnPage(cartCriteria);
		logger.debug("本次返回的记录条数是:" + (cartList == null ? "空" : cartList.size()));
		if(cartList != null && cartList.size() > 0){
			for(Cart cart : cartList){
				User frontUser = frontUserService.select(cart.getUuid());
				if(frontUser == null){
					logger.debug("找不到订单[" + cart.getCartId() + "]对应用户:" + cart.getUuid());
					cart.setExtraValue(DataName.userNickName.toString(), String.valueOf(cart.getUuid()));
				} else {
					cart.setExtraValue(DataName.userNickName.toString(), (frontUser.getNickName() == null ? frontUser.getUsername() : frontUser.getNickName()));
				}
				if(cart.getDeliveryOrderId() > 0){
					if(deliveryOrderService != null){
						//获取配送信息
						DeliveryOrder deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
						if(deliveryOrder == null){
							logger.error("找不到订单[" + cart.getCartId() + "]所指定的快递单:" + cart.getDeliveryOrderId());
						} else {
							String brief = deliveryOrder.getBrief();
							cart.setExtraValue(DataName.deliveryOrderBrief.toString(), brief);
							Price price = deliveryOrder.getFee();
							if(price != null){
								if(price.getMoney() > 0){
									cart.setExtraValue(DataName.deliveryFee.toString(), String.valueOf(price.getMoney()));

								}
							}
						}
					}
				}
				ItemCriteria itemCriteria = new ItemCriteria();
				itemCriteria.setCartId(cart.getCartId());
				logger.debug("订单[" + cart.getCartId() + "]生成时间是:" + sdf.format(cart.getCreateTime()));
				if(cart.getCreateTime() != null){
					itemCriteria.setEnterTimeBegin(DateUtils.truncate(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
					itemCriteria.setEnterTimeEnd(DateUtils.ceiling(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
				}
				List<Item> itemList = itemService.list(itemCriteria);
				logger.debug("订单[" + cart.getCartId() + "]对应的交易品数量是:" + (itemList == null ? "空" : itemList.size()));
				if(itemList != null && itemList.size() > 0 ){
					for (Item item : itemList) {
						HashMap<String, String> operateMap = new HashMap<String, String>();
						if (item.getCurrentStatus() == TransactionStatus.preDelivery.id) {
							operateMap.put("relate", "./order/" + Operate.relate.name());
							item.setOperate(operateMap);
						} else if (item.getCurrentStatus() == TransactionStatus.delivering.id) {
							operateMap.put("updateExpressInfo", "./order/" + Operate.relate.name());
							item.setOperate(operateMap);
						}
					}
					cart.setMiniItemList(itemList);
				}
				logger.debug("XXXXXXX:" + JsonUtils.getInstance().writeValueAsString(cart));
			}
		}
		map.put("rows", cartList);

		//计算并放入分页
		if(cartCriteria.getPaging() != null){
			map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, cartCriteria.getPaging().getMaxResults(), cartCriteria.getPaging().getCurrentPage()));
		}



		if(deliveryCompanyService != null){

			List<User> deliveryCompanyList = deliveryCompanyService.list();
			Map<String,String> deliveryCompanyMap = new HashMap<String,String>();
			if(deliveryCompanyList == null || deliveryCompanyList.size() < 1){
				logger.warn("当前系统未配置任何快递公司");
			} else {
				for(User dc : deliveryCompanyList){
					String companyName = (dc.getUsername().charAt(0)) + "-" + dc.getNickName();
					deliveryCompanyMap.put(dc.getUuid()+"", companyName);
				}
				//排序

			}
			map.put("deliveryCompanyMap", deliveryCompanyMap);
		}
		//产品的状态信息
		List<Integer> statusList = new ArrayList<>();
		for(TransactionStatus ts : TransactionStatus.values()){
			if(ts.id > 0){
				statusList.add(ts.id);
			}
		}
		map.put("currentStaus", statusList);
		return view;
	}

	private void setSubPartner(CartCriteria cartCriteria, User partner) {
		partnerService.applyMoreDynmicData(partner);
		List<User> grogeny = partner.getProgeny();
		StringBuffer sb = new StringBuffer();
		if(grogeny == null || grogeny.size() < 1){
			cartCriteria.setInviters(partner.getUuid());		
			sb.append(partner.getUuid());
		} else {
			long[] inviters = new long[grogeny.size()];
			for(int i = 0; i < grogeny.size(); i++){
				inviters[i] = grogeny.get(i).getUuid();
				sb.append(inviters[i]).append(',');
			}
			cartCriteria.setInviters(inviters);
		}
		logger.debug("当前parnter[" + partner.getUuid() + "]的子账户列表:" + sb.toString().replaceAll(",$", ""));		
	}

	//以CSV格式下载当前请求
	@RequestMapping(value="download", method = RequestMethod.GET)
	public String download(HttpServletRequest request, HttpServletResponse response, ModelMap map, ItemCriteria itemCriteria) throws Exception {

		final String view = "common/item/list";
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

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		itemCriteria.setOwnerId(partner.getOwnerId());

		if(deliveryCompanyService != null){
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
		}
		return view;
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
			return partnerMessageView;
		}

		String transactionId[] = idList.split("-");
		String contentWord = "";

		for (int i = 0; i < transactionId.length; i++) {

			Item item = itemService.select(transactionId[i]);
			if (item != null) {
				if(item.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
					return partnerMessageView;
				}
				contentWord += "<li>订单" + transactionId[i] + "操作结果:" + notifyService.syncSendNotify(item) + "</li>";

			} else
				map.put("message", new EisMessage(OperateResult.failed.getId(), "user对象获取失败！！"));
		}

		map.put("message", new EisMessage(OperateResult.success.getId(), contentWord));

		return partnerMessageView;

	}

	@RequestMapping(value = "/update/" + "{transactionId}")
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
			return partnerMessageView;
		}
		Item i = itemService.select(transactionId);
		logger.info("transactionId是" + transactionId);
		if (i != null) {
			if(i.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return partnerMessageView;
			}
			logger.info("cgsbaaaaaaaaaaaaaaaaa" + notifyService.syncSendNotify(i));

			map.put("message", new EisMessage(OperateResult.success.getId(), notifyService.syncSendNotify(i)));

		} else
			map.put("message", new EisMessage(OperateResult.failed.getId(), "user对象获取失败！！"));
		return partnerMessageView;
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
			return partnerMessageView;
		}
		Item item = itemService.select(transactionId);
		if (item != null) {
			if(item.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
				return partnerMessageView;
			}
		}

		int serverId = 0;
		try {
			serverId = Integer.parseInt(item.getItemDataMap().get(DataName.productServer.toString()).getDataValue());
		} catch (Exception e) {
		}
		if (serverId > 0) {
			/*ProductServer bs = ProductServerService.select(serverId);
			if (bs != null) {
				//item.getItemDataMap().get(DataName.productServer.toString()).setDataValue(bs.getProductName() + "-" + bs.getServerName() + "[" + serverId + "]");
			}*/
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


	//创建快递单/修改快递信息
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
			return partnerMessageView;
		}

		Item i = itemService.select(transactionId);
		if (i == null) {
			logger.error("找不到准备发货的订单[" + transactionId + "]");
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.id,"找不到指定的订单"));
			return partnerMessageView;		
		}
		if(i.getOwnerId() != ownerId){
			logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.id,"找不到指定的订单"));
			return partnerMessageView;		
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			boolean canAccess = authorizeService.isBelongUser(i, partner);
			if(!canAccess){
				logger.error("当前交易[" + i.getTransactionId() + "]不属于合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]");
				map.put("message", new EisMessage(EisError.ACCESS_DENY.id,"无权访问"));
				return partnerMessageView;		

			}
		}
		//		是否发货
		if(i.getCurrentStatus() != TransactionStatus.delivering.id){
			logger.debug("订单[" + i.getTransactionId() + "]状态是:" + i.getCurrentStatus() + ",设置为发货中");
			//			Item item = new Item();
			//			item.setItemId(i.getItemId());
			//			item.setTransactionId(i.getTransactionId());
			//			item.setCurrentStatus(TransactionStatus.delivering.id);
			//			itemService.updateNoNull(item);
			Item item = i.clone();
			item.setCurrentStatus(TransactionStatus.delivering.id);
			itemService.changeStatus(item);
			//前端状态
			long cartId = item.getCartId();
			Cart changeCartStat = cartService.select(cartId);
			changeCartStat.setCurrentStatus(TransactionStatus.delivering.id);
			cartService.update(changeCartStat);
		} else {
			logger.debug("订单[" + i.getTransactionId() + "]状态是发货中:" + i.getCurrentStatus() + ",仅更新快递单号");
		}
		if(StringUtils.isBlank(outDeliveryOrderId)){
			logger.debug("当前提交的快递单号是空，不进行更新");
			map.put("message", new EisMessage(OperateResult.failed.getId(),"当前提交的快递单号是空，不进行更新"));
			return partnerMessageView;		
		}
		outDeliveryOrderId = outDeliveryOrderId.trim();
		if(i.getItemDataMap() != null && i.getItemDataMap().get(DataName.deliveryOrderId.toString()) != null){
			ProductData pd = i.getItemDataMap().get(DataName.deliveryOrderId.toString());
			logger.debug("当前交易[" + i.getTransactionId() + "]已有快递单号数据[" + pd.getDataValue() + ",更新为新的快递单号:" + outDeliveryOrderId);
			pd.setDataValue(outDeliveryOrderId);
			pd.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX + TablePartitionUtils.getTableMonth(transactionId));
			itemDataService.update(pd);
			messageService.sendJmsDataSyncMessage(null, "itemDataService", "update", pd);
		} else {
			ProductData pd = new ProductData(DataName.deliveryOrderId.toString(), outDeliveryOrderId.trim());
			logger.debug("当前交易[" + i.getTransactionId() + "]没有快递单号数据，新增新的快递单号:" + outDeliveryOrderId);
			pd.setProductId(i.getItemId());
			pd.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX + TablePartitionUtils.getTableMonth(transactionId));
			itemDataService.insert(pd);
			messageService.sendJmsDataSyncMessage(null, "itemDataService", "insert", pd);
		}
		deliveryCompanyName = deliveryCompanyName.trim();
		logger.debug("已有快递公司是 ：" + i.getItemDataMap().get(DataName.deliveryCompanyName.toString()));
		if(i.getItemDataMap() != null && i.getItemDataMap().get(DataName.deliveryCompanyName.toString()) != null){
			ProductData pd = i.getItemDataMap().get(DataName.deliveryCompanyName.toString());
			logger.debug("当前交易[" + i.getTransactionId() + "]已有快递公司数据[" + pd.getDataValue() + ",更新为新的快递公司:" + deliveryCompanyName);
			pd.setDataValue(deliveryCompanyName);
			pd.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX + TablePartitionUtils.getTableMonth(transactionId));
			logger.debug("开始更改快递公司为 :" + deliveryCompanyName + "   tableName : " + ItemCriteria.ITEM_DATA_TABLE_PREFIX + TablePartitionUtils.getTableMonth(transactionId));
			itemDataService.update(pd);
			messageService.sendJmsDataSyncMessage(null, "itemDataService", "update", pd);
		} else {
			ProductData pd = new ProductData(DataName.deliveryCompanyName.toString(), deliveryCompanyName.trim());
			logger.debug("当前交易[" + i.getTransactionId() + "]没有快递公司数据，新增新的快递公司:" + deliveryCompanyName);
			pd.setProductId(i.getItemId());
			pd.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX + TablePartitionUtils.getTableMonth(transactionId));
			itemDataService.insert(pd);
			messageService.sendJmsDataSyncMessage(null, "itemDataService", "insert", pd);
		}
		if(i.getCartId() < 1){
			logger.warn("交易[" + i.getTransactionId() + "]没有cartId");
		} else {
			Cart cart = cartService.select(i.getCartId());
			if(cart == null){
				logger.error("找不到交易[" + i.getTransactionId() + "]对应的Cart[" + i.getCartId() + "]");
			} else {
				if(cart.getDeliveryOrderId() > 0){
					if(deliveryOrderService != null){

						logger.debug("订单[" + cart.getCartId() + "]有关联配送单:" + cart.getDeliveryOrderId() + "]，更新其配送数据");
						DeliveryOrder deliveryOrder = null;
						deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());

						if(deliveryOrder != null){
							logger.debug("outDeliveryOrderId ： " + outDeliveryOrderId + " / deliveryCompanyName : " + deliveryCompanyName);
							deliveryOrder.setOutOrderId(outDeliveryOrderId);
							deliveryOrder.setDeliveryCompanyId(Integer.parseInt(deliveryCompanyName));
							deliveryOrderService.update(deliveryOrder);

							
							UserMessage sms = new UserMessage(i.getOwnerId());
							if (deliveryOrder != null ) {
								sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
								logger.debug("订单有配送地址,向用户手机发送短信");
								if (deliveryOrder.getMobile() != null) {
//									String smsTemplate = configService.getValue("notifyDeliverGoodsMessage", cart.getOwnerId());
//									if(smsTemplate != null){
										sms.setContent("1");
										sms.setReceiverName(deliveryOrder.getMobile());
										sms.setCurrentStatus(MessageStatus.queue.id);
										sms.setTitle("1");
										sms.setSign("deliveryNotify");
										int rs = userMessageService.send(sms);
										logger.debug("短信下发到[手机" + deliveryOrder.getMobile() + ",内容:" + "]，消息服务返回的是:" + rs);
										
									//}
									
								}
								//sms.setContent("尊敬的客户,您的订单已成功付款,七星乐器将自动帮您分拣,发货,祝您愉快!");
							}
								
							
						} else {
							logger.debug("找不到订单[" + cart.getCartId() + "]关联的配送单:" + cart.getDeliveryOrderId() + "]");

						}
					}
				} else {
					logger.debug("订单[" + cart.getCartId() + "]没有关联配送单");

				}
			}
		}
		
		map.put("message", new EisMessage(OperateResult.success.getId(),"订单已变更为发货状态，快递单号已更新"));
		return partnerMessageView;		

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatedetail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			Item item) throws Exception {
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
			return partnerMessageView;
		}
		try {
			Item i = itemService.select(request.getParameter("transactionId"));
			if (i != null) {
				if(i.getOwnerId() != ownerId){
					logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
					return partnerMessageView;
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

		return partnerMessageView;

	}

	@RequestMapping(value = "/get/{cartId}")
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("cartId") Long cartId) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return partnerMessageView;
		}

		final String view = "common/order/detail";

		Cart cart = cartService.select(cartId);
		if(cart == null){
			logger.error("找不到指定的cart:" + cartId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单:" + cartId));
			return partnerMessageView;
		}
		if(cart.getOwnerId() != ownerId){

			logger.error("指定的cart:" + cartId + ",其ownerId=" + cart.getOwnerId() + ",与系统当前ownerId=" + ownerId + "不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单:" + cartId));
			return partnerMessageView;
		}
		long uuid = cart.getUuid();
		String username = null;
		User user = frontUserService.select(uuid);
		if (user == null) {
			logger.error("没找到指定的订单[" + cartId + "]的客户[" + uuid + "]");
			username = "未知";
		} else {
			if(user.getNickName() != null){
				username = user.getUsername() + "（" + user.getNickName() + "）";
			}
		}
		map.put("userName", username);

		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCartId(cartId);
		itemCriteria.setChargeFromAccount(uuid);
		itemCriteria.setOwnerId(ownerId);
		//如果产品定义的TTL太长比如超过24小时或2天，那么有可能造成cart的创建时间与item创建时间不一致
		//并造成查看订单时，看不到对应的商品
		//此问题应当已经在buyController中修复，有待观察
		//如果未解决，则不能使用这两个判断条件
		itemCriteria.setEnterTimeBegin(DateUtils.truncate(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
		itemCriteria.setEnterTimeEnd(DateUtils.ceiling(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
		List<Item> itemLists = itemService.list(itemCriteria);

		/*ArrayList<Product> productList = new ArrayList<Product>();
		logger.debug("订单[" + cartId + "]购买商品的数量  : " + itemLists.size());
		for (Item item : itemLists) {
			Product product = productService.select(item.getProductId());
			if (product == null) {
				logger.error("产品[ " + item.getProductId() + "]不存在");

			}
			Product productClone = product.clone();
			productClone.setProductDataMap(new HashMap<String, ProductData>());
			ProductDataCriteria productDataCriteria = new ProductDataCriteria();
			productDataCriteria.setProductId(product.getProductId());
			List<ProductData> productDataList = productDataService.list(productDataCriteria);
			if (productDataList == null || productDataList.size() < 1) {
				logger.error("订单[" + cartId + "]里面产品[" + product.getProductId() + "]的扩展数据是 ： " + (productDataList.size() < 1 ? "空" : productDataList.size()));

			}
			for (ProductData productData : productDataList) {
				DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
				dataDefineCriteria.setObjectId(product.getProductTypeId());
				dataDefineCriteria.setObjectType(ObjectType.product.toString());
				dataDefineCriteria.setDataDefineId(productData.getDataDefineId());
				List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
				if (dataDefineList == null || dataDefineList.size() < 1) {
					logger.error("订单[" + cartId + "]里面产品[" + product.getProductId() + "]里的扩展字段个数是 ：" + (dataDefineList.size() < 1 ? "0" : dataDefineList.size()));

				}
				for (DataDefine dataDefine : dataDefineList) {
					productClone.getProductDataMap().put(dataDefine.getDataCode(), productData);
				}
			}
			productList.add(productClone);
		}*/

		long deliveryOrderId = cart.getDeliveryOrderId();
		if (deliveryOrderId <= 0) {
			logger.error("订单[" + cartId + "]没有配送地址信息 " + deliveryOrderId);

		}
		if(	deliveryOrderService != null){
			DeliveryOrder deliveryOder = deliveryOrderService.select(deliveryOrderId);
			if (deliveryOder == null) {
				logger.error("订单[" + cartId + "]没的配送地址信息 为空");

			}
			map.put("deliveryOder", deliveryOder);
		}

		//		PriceCriteria priceCriteria = new PriceCriteria();
		//		priceCriteria.setObjectId(productClone.getProductId());
		//		priceCriteria.setObjectType(ObjectType.product.toString());
		//		priceCriteria.setPriceType(PriceType.PRICE_STANDARD.toString());
		//		priceCriteria.setOwnerId(ownerId);
		//		List<Price> priceLists = priceService.list(priceCriteria);
		//		if (priceLists == null || priceLists.size() < 1) {
		//			logger.error("没找到订单[" + cartId + "]里面产品[" + product.getProductId() + "]的单价  ：" + (priceLists.size() < 1 ? "0" : priceLists.size()));
		//			map.put("message", new EisMessage(EisError.dataError.getId(), "没找到订单[" + cartId + "]里面产品[" + product.getProductId() + "]的单价  ：" + (priceLists.size() < 1 ? "0" : priceLists.size())));
		//			return partnerMessageView;
		//		}
		map.put("itemLists", itemLists);
		//map.put("productLists", productList);
		map.put("cart",cart);

		return view;
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
			return partnerMessageView;
		}
		Item i = itemService.select(transactionId);

		if (i != null) {
			if(i.getOwnerId() != ownerId){
				logger.error("尝试访问的Item[" + i.getTransactionId() + "]其ownerId[" + i.getOwnerId() + "]与系哦他能够会话中的[" + ownerId + "]不一致");
				return partnerMessageView;
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

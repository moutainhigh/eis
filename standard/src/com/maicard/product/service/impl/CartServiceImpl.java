package com.maicard.product.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Uuid;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.service.UuidService;
import com.maicard.exception.EisException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PriceService;
import com.maicard.money.util.MoneyUtils;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.dao.CartDao;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.product.service.TransPlanService;
import com.maicard.product.service.TransactionExecutor;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.TransactionStandard.TransactionStatus;

@Service
public class CartServiceImpl extends BaseService implements CartService {
	@Resource
	private CartDao cartDao;

	@Resource
	private ConfigService configService;

	@Resource
	private MessageService messageService;
	@Resource
	private ItemService itemService;
	@Resource
	private PayService payService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;
	@Resource
	private UuidService uuidService;

	@Resource
	private TransPlanService transPlanService;

	@Resource
	private GlobalOrderIdService globalOrderIdService;

	@Value("${MQ_ENABLED:true}")
	private boolean mqEnabled;


	private int cartTtl = 0;
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	//在新增Item时，是否先在本地插入数据，再传递给后端节点
	private boolean itemNoLocalInsert = false;


	@PostConstruct
	public void init(){
		cartTtl = configService.getIntValue(DataName.cartTtl.toString(),0);
		itemNoLocalInsert = configService.getBooleanValue(DataName.itemNoLocalInsert.toString(), 0);

	}

	@Override
	public Cart add(Item item, boolean directBuy, String identify, int cartId) throws EisException {

		if(item == null){
			logger.error("商品为空,无法添加购物车");
			return null;
		}
		if(item.getChargeFromAccount() <= 0){
			logger.error("商品没有用户ID,无法添加购物车");
			return null;
		}
		if(StringUtils.isBlank(item.getTransactionId())){
			item.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));
		}
		if(item.getCount() < 1){
			item.setCount(1);
		}
		if(item.getEnterTime() == null){
			item.setEnterTime(new Date());
		}
		String priceType = null;
		if(item.getPrice() != null){
			priceType = item.getPrice().getPriceType();
		}
		Cart cart  = null;
		if(cartId > 0){
			cart = select(cartId);
			if(cart == null){
				logger.error("找不到加入商品的指定购物车:" + cartId);
				return null;
			}
			if(cart.getOrderType() != null && cart.getOrderType().equals(CartCriteria.ORDER_TYPE_STORE)){

				logger.error("准备加入商品的指定购物车:" + cartId + "是一个已存储的购物车，不能再放入商品");
				return null;
			}
			if(item.getSupplyPartnerId() > 0){
				cart.setInviter(item.getSupplyPartnerId());
			}
			//FIXME 已有购物车结算时添加新产品VIP
			//return cart;

		}
		cart = getCurrentCart(item.getChargeFromAccount(), priceType, CartCriteria.ORDER_TYPE_TEMP, identify, item.getOwnerId(), directBuy);
		if(cart == null){
			logger.error("在把交易[" + item.getTransactionId() + "]加入购物车时，无法获得当前购物车");
			return null;
		}
		logger.debug("得到了购物车:{}", cart.getCartId());
		cart.setTtl(item.getTtl());
		if(item.getSupplyPartnerId() > 0){
			cart.setInviter(item.getSupplyPartnerId());
		}
		item.setCartId(cart.getCartId());


		int totalProduct = 0;
		int totalGoods = 0;
		boolean goodsCountUpdated = false;
		if(!directBuy){
			ItemCriteria itemCriteria = new ItemCriteria();
			itemCriteria.setCartId(cart.getCartId());
			itemCriteria.setCurrentStatus(TransactionStatus.inCart.getId());
			List<Item> inCartItemList = list(itemCriteria);
			if(inCartItemList != null && inCartItemList.size() > 0){
				for(Item i : inCartItemList){
					totalProduct++;
					if(i.getProductId() == item.getProductId()){
						logger.info("在购物车[" + cart.getCartId() + "]找到了相同的产品[" + i.getProductId() + "]，仅增加其数量" );
						goodsCountUpdated = true;
						i.setCount(i.getCount() + item.getCount());
						/*if(cart.getTotalProduct() < 1){
						cart.setTotalProduct(1);
					}*/
						update(i, i.getCount());
						logger.debug("仅增加购物车[" + cart.getCartId() + "]中的商品[" + i.getProductId() + "]数量:" + i.getCount());
					}
					totalGoods += i.getCount();

				}
			}
		}
		if(goodsCountUpdated){
			cart.setTotalGoods(totalGoods);
			cart.setTotalProduct(totalProduct);
			logger.debug("增加购物车[" + cart.getCartId() + "]中的商品数量后，商品总数:" + cart.getTotalProduct() + ",物品总数:" + cart.getTotalGoods() + ",更新购物车");
			update(cart);
			return cart;
		}
		//对应购物车中没有任何商品，直接添加商品

		logger.info("对应购物车[" + cart.getCartId() + "]中没有找到对应商品[" + item.getProductId() + "]，直接向数据库中添加商品");
		itemService.insert(item);

		cart.setTotalGoods(totalGoods+1);
		cart.setTotalProduct(totalProduct+1);
		if(cart.getMoney() == null) {
			cart.setMoney(Money.from(item.getPrice(), cart.getUuid()));
		} else {
			cart.setMoney(MoneyUtils.plus(cart.getMoney(), Money.from(item.getPrice(), cart.getUuid())));
		}
		/*cart.setTotalGoods(item.getCount() + cart.getTotalGoods());
		cart.setTotalProduct(cart.getTotalProduct()+1);*/
		logger.debug("添加交易[" + item.getTransactionId() + "]后，购物车[" + cart.getCartId() + "]商品总数:" + cart.getTotalProduct() + ",物品总数:" + cart.getTotalGoods() + ",购物车总资金是:" + cart.getMoney());
		update(cart);
		return cart;
	}

	@Override
	public int update(Cart cart) {
		Assert.notNull(cart,"尝试更新的Cart不能为空");
		if(cart.getTotalGoods() < 0){
			cart.setTotalGoods(0);
		}
		if(cart.getTotalProduct() < 0){
			cart.setTotalProduct(0);
		}
		
		Cart _oldCart = cartDao.select(cart.getCartId());
		if(_oldCart == null) {
			logger.warn("没有找到对应的cart对象:{}，改为insert", cart.getCartId());
			return cartDao.insert(cart);
		} else {
			return cartDao.update(cart);
		}

	}

	@Override
	public int insert(Cart cart) {
		Assert.notNull(cart,"尝试新增的Cart不能为空");
		if(cart.getCartId() < 1){
			cart.setCartId(createNewCartId());
		}
		if(select(cart.getCartId()) != null){
			logger.warn("本地已有相同主键#{}的Cart订单存在", cart.getCartId());
			return 0;
		}
		if(cart.getCreateTime() == null){
			cart.setCreateTime(new Date());
		}
		return cartDao.insert(cart)	;

	}

	@Override
	public List<Cart> list(CartCriteria cartCriteria) {
		Assert.notNull(cartCriteria,"尝试查询Cart条件不能为空");

		return cartDao.list(cartCriteria)	;

	}

	/*@Override
	public int insert(Item item, boolean useLabelMoney, boolean isFreeProduct) {

		if(item == null){
			logger.error("商品为空,无法添加购物车");
			return 0;
		}
		if(item.getChargeFromAccount() <= 0){
			logger.error("商品没有用户ID,无法添加购物车");
			return 0;
		}
		if(item.getTransactionId() == null || item.getTransactionId().equals("")){
			logger.error("商品交易ID为空,无法添加购物车");
			return 0;
		}
		if(item.getCount() < 1){
			item.setCount(1);
		}
		if(item.getEnterTime() == null){
			item.setEnterTime(new Date());
		}
		Product product = productService.select(item.getProductId());
		if(product == null){
			logger.error("在尝试新增订单[" + item.getTransactionId() + "]时，找不到对应的产品[" + item.getProductId() + "]");
			return -1;		
		}
		float totalMoney = 0;
		try{
			if(useLabelMoney){
				totalMoney = product.getLabelMoney() * item.getCount();
			} else {
				totalMoney = product.getBuyMoney() * item.getCount();
			}
			item.setRequestMoney(totalMoney);
			//item.setLabelMoney(totalMoney);
		}catch(Exception e){}
		if(isFreeProduct){
			item.setCurrentStatus(TransactionStatus.inProcess.getId());
		}
		if(totalMoney == 0 && !isFreeProduct){
			logger.warn("在尝试新增订单[" + item.getTransactionId() + "]时，总金额异常为0");
			return -1;	
		}
		int currentCartId = getCurrentCartId(item.getChargeFromAccount());
		if(currentCartId < 1){
			currentCartId = createNewCartId();
		}
		item.setCartId(currentCartId);
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCartId(currentCartId);
		itemCriteria.setCurrentStatus(TransactionStatus.inCart.getId());
		List<Item> inCartItemList = null;
		if(transactionCachePolicy == TransactionCachePolicy.TS_SAVE_IN_DB){
			inCartItemList = itemService.list(itemCriteria);
		} else {
			inCartItemList = itemCacheService.list(itemCriteria);
		}
		if(inCartItemList != null && inCartItemList.size() > 0){
			for(Item i : inCartItemList){
				if(i.getProductId() == item.getProductId()){
					logger.info("在购物车[" + currentCartId + "]找到了相同的产品[" + i.getProductId() + "]，仅增加其数量" );
					i.setCount(i.getCount() + item.getCount());
					i.setRequestMoney( product.getLabelMoney()* i.getCount() );
					update(i);
					return 1;
				}
			}
		}
		//对应购物车中没有任何商品，直接添加商品
		logger.info("对应购物车[" + currentCartId + "]中没有找到对应商品[" + item.getProductId() + "]，直接添加商品");
		if(transactionCachePolicy == TransactionCachePolicy.TS_SAVE_IN_DB){
			itemService.insert(item);
		} else {
			itemCacheService.insert(item);
		}
		return 1;


	}*/

	@Override
	public List<Item> list(ItemCriteria itemCriteria){
		List<Item> itemList = null;
			if(!itemNoLocalInsert){
				logger.debug("当前系统配置为先插入本地数据库，不进行JMS等待");
				return itemService.list(itemCriteria);
			}
			for(int i = 0; i < CommonStandard.JMS_MAX_RETRY; i++){
				itemList = itemService.list(itemCriteria);
				if(itemList != null && itemList.size() > 0){
					logger.debug("当前交易保存到数据库，第" + (i+1) + "次查询用户购物车[cartId=" + itemCriteria.getCartId() + "]中的订单，已查询到订单，返回该组订单:" + itemList.size());
					break;
				}
				logger.debug("当前交易保存到数据库，第" + (i+1) + "次查询用户购物车[cartId=" + itemCriteria.getCartId() + "]中的订单，未查询到订单，等待" + (i+1) + "秒后再次查询");
				try {
					Thread.sleep( (i+1) * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
		if(itemList == null || itemList.size() < 1){
			return null;
		}
		List<Item> itemList2 = new ArrayList<Item>();

		for(Item item : itemList){
			if(cartTtl > 0 && item.getEnterTime() != null && item.getCurrentStatus() == TransactionStatus.inCart.id){
				if((new Date().getTime() - item.getEnterTime().getTime()) / 1000 > cartTtl){
					logger.info("购物车订单[" + item.getTransactionId() + "]进入时间[" + sdf.format(item.getEnterTime()) + "]已超过有效期" + cartTtl + ",删除");
					
						try {
							itemService.delete(item.getTransactionId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					continue;
				}
			}
			itemList2.add(item);
		}		
		return itemList2;
	}

	@Override
	public HashMap<String, Item> map(ItemCriteria itemCriteria) {

		List<Item> itemList = list(itemCriteria);
		if(itemList == null || itemList.size() < 1){
			return null;
		}
		HashMap<String, Item> cart = new HashMap<String, Item>();
		for(Item item : itemList){
			cart.put(item.getTransactionId(), item);
		}
		return cart;	
	}

	@Override
	public int count(long uuid, int status) {

		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setChargeFromAccount(uuid);
		itemCriteria.setCurrentStatus(status);//只列出
			return itemService.count(itemCriteria);
			

	}

	@Override
	public Item select(long uuid, String transactionId) {
		logger.debug("尝试为用户[" + uuid + "]获取订单[" + transactionId + "]...");
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setChargeFromAccount(uuid);
		HashMap<String, Item> cart = map(itemCriteria);
		if(cart == null){
			logger.debug("购物车为空...");
			return null;
		}
		Item item = cart.get(transactionId);
		if(item == null){
			logger.debug("购物车中没有该订单[" + transactionId + "]...");
			return null;
		}
		logger.debug("返回订单[" + transactionId + "=" + item.toString() + "].");
		return item;
	}

	@Override
	public void delete(long uuid, String transactionId) throws Exception {

		Item item = select(uuid, transactionId);
		if(item == null){
			logger.warn("没找到尝试删除的订单[" + transactionId + "]");
			return;
		}
		if(item.getChargeFromAccount() != uuid){
			logger.warn("订单[" + transactionId + "]不属于[" + uuid + "]，属于[" + item.getChargeFromAccount() + "]，不能删除");
			return;
		}
		/*if(cacheItem.getCurrentStatus() != TransactionStatus.inCart.getId()){
			logger.warn("缓存中的订单[" + transactionId + "]不是购物车中状态，是[" + cacheItem.getCurrentStatus() + ",不能删除");
			return;
		}*/
		int count = item.getCount();
		if(count < 1){
			count = 1;
		}
		long cartId = 0;
		if(item.getCartId() > 0){
			cartId = item.getCartId();
		}

			int rs = itemService.delete(transactionId);
			logger.debug("当前交易保存到数据库，尝试删除数据库中的交易:" + transactionId + "，结果:" + rs);
		
		logger.info("已删除订单[" + transactionId + "]");
		if(cartId > 0){
			Cart cart = select(cartId);
			if(cart == null){
				logger.debug("找不到删除订单[" + transactionId + "]对应的购物车:" + cartId);
			} else {
				logger.debug("更新订单[" + item.getTransactionId() + "]对应的购物车:" + cartId + "，将其总物品数从" + cart.getTotalGoods() + "-" + count + ",总商品数从" + cart.getTotalProduct() + "-1");
				cart.setTotalGoods(cart.getTotalGoods() - count);
				cart.setTotalProduct(cart.getTotalProduct() - 1);
				update(cart);
			}
		}

	}

	@Override
	public void clear(long uuid) throws Exception{
		//清除购物车
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setChargeFromAccount(uuid);
		List<Item> cart = list(itemCriteria);
		if(cart == null || cart.size() < 1){
			logger.info("用户[" + uuid + "]购物车为空，无需清空");
			return;
		}
		for(Item item : cart){
				itemService.delete(item.getTransactionId());
			
		}
		logger.info("用户[" + uuid + "]购物车已清空[" + cart.size() + "]");
	}

	@Override
	public void update(Item item, int changeCount) throws EisException {
		/*Product product = productService.select(item.getProductId());
		if(product == null){
			logger.error("在尝试修改订单[" + item.getTransactionId() + "]时，找不到对应的产品[" + item.getProductId() + "]");
			return;		
		}
		float totalMoney = 0;

		if(totalMoney == 0){
			try{
				totalMoney = product.getBuyMoney() * item.getCount();
				item.setRequestMoney(totalMoney);
				item.setLabelMoney(totalMoney);
			}catch(Exception e){}
			logger.error("在尝试修改订单[" + item.getTransactionId() + "]时，总金额异常为0，修正为" + totalMoney);
		}
		if(totalMoney == 0){			
			logger.error("在尝试修改订单[" + item.getTransactionId() + "]时，总金额异常为0");
			return;	
		}*/
		int count = item.getCount();
		if(count < 1){
			count = 1;
		}
		long cartId = 0;
		if(item.getCartId() > 0){
			cartId = item.getCartId();
		}
		if(item.getPrice() == null){
			throw new EisException(EisError.priceNotExist.id,"更新的订单[" + item.getTransactionId() + "]没有价格对象");
		}
		priceService.applyPrice(item, item.getPrice());
			itemService.update(item);
		
		if(cartId > 0){
			Cart cart = select(cartId);
			if(cart == null){
				logger.debug("找不到要更新订单[" + item.getTransactionId() + "]对应的购物车:" + cartId);
			} else {
				logger.debug("更新订单[" + item.getTransactionId() + "]对应的购物车:" + cartId + "，将其总物品数从" + cart.getTotalGoods() + "+" + count);
				cart.setTotalGoods(cart.getTotalGoods() + count);
				update(cart);
			}
		}
	}

	@Override
	public Cart getCurrentCart(long uuid, String priceType, String orderType, String identify, long ownerId, boolean directBuy) {
		if(uuid <= 0){
			logger.error("尝试获取当前购物车ID的UUID为空");
			return null;
		}
		CartCriteria cartCriteria = new CartCriteria(ownerId);
		cartCriteria.setPriceType(priceType);
		cartCriteria.setIdentify(identify);
		cartCriteria.setUuid(uuid);
		if(orderType == null){
			orderType = CartCriteria.ORDER_TYPE_TEMP;
		}
		cartCriteria.setOrderType(orderType);

		if(directBuy){
			Cart cart = new Cart(ownerId);
			cart.setUuid(uuid);
			cart.setIdentify(identify);
			cart.setPriceType(priceType);
			cart.setOrderType(orderType);
			cart.setCurrentStatus(TransactionStatus.inCart.id);
			cart.setBuyType(CartCriteria.BUY_TYPE_DIRIECT);
			int rs = insert(cart);
			logger.debug("当前请求直接购买，将创建新的购物车，创建结果:" + rs + ",cartId=" + cart.getCartId());
			if(rs == 1){
				//XXX 由于此处是内部调用的insert，不会被JMS切面捕获到，因此需要手工发布数据同步
				messageService.sendJmsDataSyncMessage(null, "cartService", "insert", cart);
				return cart;
			} else {
				logger.error("无法创建新的购物车");
				return null;
			}		
		}

		cartCriteria.setBuyType(CartCriteria.BUY_TYPE_NORMAL);


		List<Cart> cartList = list(cartCriteria);
		if(cartList == null || cartList.size() < 1){
			logger.debug("找不到符合条件[" + cartCriteria + "]的购物车，创建新的购物车");
			Cart cart = new Cart(ownerId);
			cart.setUuid(uuid);
			cart.setIdentify(identify);
			cart.setPriceType(priceType);
			cart.setOrderType(orderType);
			cart.setBuyType(CartCriteria.BUY_TYPE_NORMAL);
			int rs = insert(cart);
			logger.debug("找不到符合条件[" + cartCriteria + "]的购物车，创建新的购物车，创建结果:" + rs + ",cartId=" + cart.getCartId());
			if(rs == 1){
				//XXX 由于此处是内部调用的insert，不会被JMS切面捕获到，因此需要手工发布数据同步
				messageService.sendJmsDataSyncMessage(null, "cartService", "insert", cart);
				return cart;
			} else {
				logger.error("无法创建新的购物车");
				return null;
			}		
		} else {
			//由于修改了Cart.xml中的排序为create_time DESC，因此第一个Cart就是最后创建的那一个,NetSnake,2016-06-30
			Cart cart = cartList.get(0);
			//			Cart cart = cartList.get(cartList.size() - 1);
			logger.debug("找到了符合条件[" + cartCriteria + "]的购物车，共" + cartList.size() + "个，返回第一个:" + cart.getCartId());
			return cart;
		}

	}

	/*@Override
	public int getCurrentCartId(long uuid) {
		if(uuid <= 0){
			logger.error("尝试获取当前购物车ID的UUID为空");
			return 0;
		}
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setChargeFromAccount(uuid);
		itemCriteria.setCurrentStatus(TransactionStatus.inCart.getId());
		List<Item> inCartItemList = null;
		if(transactionCachePolicy == TransactionCachePolicy.TS_SAVE_IN_DB){
			inCartItemList = itemService.list(itemCriteria);
		} else {
			inCartItemList = itemCacheService.list(itemCriteria);
		}

		if(inCartItemList == null || inCartItemList.size() < 1){
			logger.info("未能找到UUID=" + uuid + "并且状态为inCart的交易");
			return 0;
		}
		//把第一个符合条件的Item的cartId作为当前的cartId
		logger.info("找到了用户[" + uuid + "]还处于inCart状态中的交易，直接返回相同的cartId:" + inCartItemList.get(0).getCartId());
		return inCartItemList.get(0).getCartId();
	}*/


	@Override
	public int createNewCartId() {
		long uuid = uuidService.insert(new Uuid());
		if(uuid < 1){
			logger.error("无法生成本地UUID");
			return -1;
		}
		int cartId = Integer.parseInt(configService.getServerId() + "" + uuid);

		return cartId;
	}

	@Override
	public Cart select(long cartId) {
		return cartDao.select(cartId);
	}

	@Override
	public int updateNoNull(Cart cart) {
		
		Cart _oldCart = cartDao.select(cart.getCartId());
		if(_oldCart == null) {
			logger.warn("没有找到对应的cart对象:{}，改为insert", cart.getCartId());
			return cartDao.insert(cart);
		} else {
			return cartDao.updateNoNull(cart);
		}
	}

	@Override
	public int count(CartCriteria cartCriteria) {
		return cartDao.count(cartCriteria);
	}

	@Override
	public List<Cart> listOnPage(CartCriteria cartCriteria) {
		return cartDao.listOnPage(cartCriteria);
	}

	/**
	 * 回收一个Cart订单
	 * 不考虑其中的Item子交易
	 * 将状态改为已超时
	 * 如果有半支付情况，则尝试退款
	 * 
	 */
	@Override
	public int recycle(Cart order) {
		boolean noRefund = false;
		if(order.getCurrentStatus() == TransactionStatus.timeout.id){
			logger.warn("订单[" + order.getCartId() + "]已经是超时状态，不进行退款");
			noRefund = true;
		}
		order.setCurrentStatus(TransactionStatus.timeout.id);
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCartId(order.getCartId());
		if(order.getCreateTime() != null){
			itemCriteria.setEnterTimeBegin(DateUtils.truncate(order.getCreateTime(), Calendar.DAY_OF_MONTH));
			itemCriteria.setEnterTimeEnd(DateUtils.ceiling(order.getCreateTime(), Calendar.DAY_OF_MONTH));
		}
		List<Item> itemList = itemService.list(itemCriteria);
		logger.debug("订单[" + order.getCartId() + "]对应的交易品数量是:" + (itemList == null ? "空" : itemList.size()));
		if(itemList != null && itemList.size() > 0 ){
			float subOrderMoney = 0;
			for(Item item : itemList){
				subOrderMoney += item.getRequestMoney();
			}
			if(order.getMoney() == null){
				order.setMoney(new Money(order.getUuid()));
			}
			order.getMoney().setChargeMoney(subOrderMoney);
		}
		int rs = updateNoNull(order);

		messageService.sendJmsDataSyncMessage(null, "cartService", "updateNoNull", order);
		if(rs != 1){
			logger.error("无法将订单[" + order.getCartId() + "]回收:" + rs);
			return rs;
		}
		boolean coinPaied = order.getBooleanExtraValue(DataName.coinPaid.toString());
		if(coinPaied && !noRefund){
			PayCriteria payCriteria = new PayCriteria(order.getOwnerId());
			payCriteria.setRefBuyTransactionId(String.valueOf(order.getCartId()));
			payCriteria.setMoneyTypeId(MoneyType.coin.getId());
			payCriteria.setCurrentStatus(TransactionStatus.inProcess.id);
			List<Pay> payList = payService.list(payCriteria);
			if(payList == null || payList.size() < 1){
				logger.warn("找不到订单[" + order.getCartId() + "]对应的任何已进行coin支付的支付订单");
				return -EisError.REQUIRED_PARAMETER.id;
			}
			for(Pay pay : payList){
				payService.refund(pay);
			}
		} else {
			logger.debug("订单[" + order.getCartId() + "]未进行coin支付，不需要退款");
		}
		return rs;

	}



	@Override public int delete(long cartId) {
		return cartDao.delete(cartId);		
	}

	@Override
	public void finish(Cart order) {
		order.setCurrentStatus(TransactionStatus.waitingMinusMoney.id);
		if(mqEnabled) {
			EisMessage eisMessage = new EisMessage();
			eisMessage.setOperateCode(Operate.create.getId());
			eisMessage.setAttachment(new HashMap<String, Object>());
			eisMessage.getAttachment().put("order", order);
			eisMessage.setObjectType(ObjectType.order.toString());
			messageService.send(null, eisMessage);
		} else {
			//直接在本地执行交易的后续操作
			TransactionExecutor exector = transPlanService.getTransactionExecutor(null, 0);
			try {
				exector.begin(order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}


	/*private boolean save(int uuid, HashMap<String, Item> cart){
		Cache cache = cacheManager.getCache(Constants.cacheNameSupport);
		if(cache == null){
			return false;
		}
		if(cart == null){
			logger.debug("将要存放的购物车是空的");
		}
		logger.debug("将购物车[共" + (cart == null ? 0 : cart.size()) + "个商品]存放到缓存[" + Constants.cacheNameSupport + "]中[" + cartPrefix + uuid + "]");
		cache.put(cartPrefix + uuid , cart);
		return true;
	}*/





}

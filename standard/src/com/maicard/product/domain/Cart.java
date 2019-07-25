package com.maicard.product.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EVEisObject;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Price;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.views.JsonFilterView;

/**
 * 2016-02-27
 * 增加了对应的交易订单、价格等，成为一个包含了若干交易订单的用户订单
 * 初期为购物车，仅提供一个cartId
 * 
 *
 *
 * @author NetSnake
 * @date 2016年2月27日
 *
 */

@NeedJmsDataSyncP2P
public class Cart extends EVEisObject{

	private static final long serialVersionUID = 7019579580265081277L;

	private long uuid = 0;

	private long cartId = 0;
	
	private String goodsDesc;

	private Date createTime;
	
	private Date endTime;
	
	private Date lastAccessTime;	//最后一次访问或操作该订单的时间
	
    @JsonView({JsonFilterView.Partner.class})
	private long ttl;
	
    @JsonView({JsonFilterView.Partner.class})
	private String priceType;				//订单的价格类型，不同价格类型不能作为同一个订单
	
    @JsonView({JsonFilterView.Partner.class})
	private String orderType  = CartCriteria.ORDER_TYPE_TEMP;						//订单类型
    
    @JsonView({JsonFilterView.Partner.class})
	private String buyType = CartCriteria.BUY_TYPE_NORMAL;
	
	private String[] transactionIds;		//该订单所包含的交易订单号
	
	private Price price;					//该订单的价格
	
	private Money money;					//该订单的总金额
	
	/**
	 * 赞助模式下的赞助类型
	 */
	private String quotaType;
	
	/**
	 * 总份数
	 */
	private long totalQuota = -1;
	
	
	/**
	 * 已完成份数
	 */
	private long successQuota;
	
	/**
	 * 已锁定份数
	 */
	private long lockedQuota;
	
	/**
	 * 锁定时lockedQuota必须为这个数，否则认为锁定失败
	 */
    @JsonView({JsonFilterView.Partner.class})
	private long beforeLockQuota = -1;
	
	
	private int totalProduct;				//该订单包含的总产品数
	
	private int totalGoods;				//该订单包含的总物品数，即所有产品数X购买数量
	
    @JsonView({JsonFilterView.Partner.class})
	private Map<String,Price> feeMap;		//该订单可能的减免费用
	
	private long deliveryOrderId;			//该订单对应的快递单
	
	private String invoiceInfo;				//发票信息，可能是发票JSON内容或ID
	
	/**
	 * 用于展现给用户的各种明细数据
	 */
	private Map<String,String> specInfo;
	
    @JsonView({JsonFilterView.Partner.class})
	private String identify;

    @JsonView({JsonFilterView.Partner.class})
    private List<Item> miniItemList;			//对应的交易简单数据
    
    private long inviter;						//对应的推广用户ID

	public Cart() {
		this.createTime = new Date();
		this.lastAccessTime = this.createTime;
	}

	public Cart(long ownerId) {
		this.ownerId = ownerId;
		this.createTime = new Date();
		this.lastAccessTime = this.createTime;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)cartId;

		return result;
	}
	
	@Override
	public Cart clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Cart)in.readObject();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Cart other = (Cart) obj;
		if (cartId != other.cartId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"cartId=" + "'" + cartId + "'" + 
			")";
	}

	public String[] getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(String... transactionIds) {
		this.transactionIds = transactionIds;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Map<String, Price> getFeeMap() {
		return feeMap;
	}

	public void setFeeMap(Map<String, Price> feeMap) {
		this.feeMap = feeMap;
	}

	public long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public int getTotalProduct() {
		return totalProduct;
	}

	public void setTotalProduct(int totalProduct) {
		this.totalProduct = totalProduct;
	}

	public int getTotalGoods() {
		return totalGoods;
	}

	public void setTotalGoods(int totalGoods) {
		this.totalGoods = totalGoods;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}


	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(String invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	
	
	

	public List<Item> getMiniItemList() {
		if(miniItemList == null){
			miniItemList = new ArrayList<Item>();
		}
		return miniItemList;
	}

	public void setMiniItemList(List<Item> miniItemList) {
		this.miniItemList = miniItemList;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getInviter() {
		return inviter;
	}

	public void setInviter(long inviter) {
		this.inviter = inviter;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public long getTotalQuota() {
		return totalQuota;
	}

	public void setTotalQuota(long totalQuota) {
		this.totalQuota = totalQuota;
	}

	public long getSuccessQuota() {
		return successQuota;
	}

	public void setSuccessQuota(long successQuota) {
		this.successQuota = successQuota;
	}

	public long getLockedQuota() {
		return lockedQuota;
	}

	public void setLockedQuota(long lockedQuota) {
		this.lockedQuota = lockedQuota;
	}

	public long getBeforeLockQuota() {
		return beforeLockQuota;
	}

	public void setBeforeLockQuota(long beforeLockQuota) {
		this.beforeLockQuota = beforeLockQuota;
	}

	public Map<String, String> getSpecInfo() {
		return specInfo;
	}

	public void setSpecInfo(Map<String, String> specInfo) {
		this.specInfo = specInfo;
	}

	

}

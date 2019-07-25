package com.maicard.product.service.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.Task;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 回收超时订单，将状态设置为超时，并将该订单数量加回系统
 * 从REDIS中更新产品数据到缓存和数据库
 *
 *
 * @author NetSnake
 * @date 2016年4月23日
 *
 */

@Service
public class OrderRecycleTask extends BaseService implements Task{
	
	@Resource
	private CartService cartService;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private ItemService itemService;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@Override
	public EisMessage start() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage stop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage status() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage start(String objectType, int... objectIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		_start();
		
	}
	
/*	private void _start(){
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCurrentStatus(TransactionStatus.waitingPay.id, TransactionStatus.inCart.id);
		itemCriteria.setQueryProcessingItem(DataName.only.toString());
		itemCriteria.setTimeoutPolicy(ItemCriteria.TIMEOUT_ONLY);
		List<Item> itemList = itemService.list(itemCriteria);
		if(itemList == null || itemList.size() < 1){
			logger.debug("当前系统中找不到等待付款或放入购物车的的超时订单，不进行回收");
			return;
		}
		int recycleCount = 0;
		for(Item item : itemList){
			long liveTime = (new Date().getTime() -item.getEnterTime().getTime()) / 1000;
			if(liveTime > item.getTtl()){
				logger.debug("订单[" + item.getTransactionId() + "]进入时间是:" + sdf.format(item.getEnterTime()) + ",存活时间[" + liveTime + "]已超过其TTL" + item.getTtl() + "秒，应该回收");
				int timeoutStatus = item.getIntConfig(DataName.timeoutConfig.toString());
				if(timeoutStatus < 1){
					timeoutStatus = TransactionStatus.timeout.id;
				}
				item.setCurrentStatus(timeoutStatus);
				itemService.recycle(item);
				recycleCount++;
			}
		}
		logger.debug("共回收订单[" + recycleCount + "]，刷新产品数量");
		productService.flushProductAmountCache();
		
	}*/

	//按照Cart对象进行订单回收
	private void _start(){
		
		
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCurrentStatus(TransactionStatus.waitingPay.id, TransactionStatus.inCart.id);
		itemCriteria.setQueryProcessingItem(DataName.only.toString());
		itemCriteria.setTimeoutPolicy(ItemCriteria.TIMEOUT_ONLY);
		List<Item> itemList = itemService.list(itemCriteria);
		if(itemList == null || itemList.size() < 1){
			logger.debug("当前系统中找不到等待付款或放入购物车的的超时订单，不进行回收");
			return;
		}
		Set<Long> recycleCartSet = new HashSet<Long>();
		int recycleCount = 0;
		for(Item item : itemList){
			long liveTime = (new Date().getTime() -item.getEnterTime().getTime()) / 1000;
			if(liveTime > item.getTtl()){
				logger.debug("订单[" + item.getTransactionId() + "]进入时间是:" + sdf.format(item.getEnterTime()) + ",存活时间[" + liveTime + "]已超过其TTL" + item.getTtl() + "秒，应该回收");
				int timeoutStatus = item.getIntConfig(DataName.timeoutConfig.toString());
				if(timeoutStatus < 1){
					timeoutStatus = TransactionStatus.timeout.id;
				}
				item.setCurrentStatus(timeoutStatus);
				itemService.recycle(item);
				recycleCartSet.add(item.getCartId());
				recycleCount++;
			}
		}
		logger.debug("共回收交易" + recycleCount + "个，需回收订单" + recycleCartSet.size() + "个，刷新产品数量");
		if(recycleCartSet.size() > 0){
			for(Long cartId : recycleCartSet){
				Cart order = cartService.select(cartId.longValue());
				if(order == null){
					logger.warn("找不到订单[" + cartId.longValue() + "]，无法进行订单Cart回收");
				} else {
					cartService.recycle(order);
				}		
			}
		}
		productService.flushProductAmountCache();
		
	}

}

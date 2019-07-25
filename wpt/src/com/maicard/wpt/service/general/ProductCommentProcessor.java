package com.maicard.wpt.service.general;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.processor.CommentProcessor;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 负责处理针对产品的评论
 *
 *
 * @author NetSnake
 * @date 2016年7月9日
 *
 */

@Service
public class ProductCommentProcessor extends BaseService implements CommentProcessor{

	@Resource
	private ItemService itemService;
	@Resource
	private CartService cartService;
	@Resource
	private ProductService productService;

	@Override
	public EisMessage execute(String action, Comment comment, Map<String, Object> parameterMap) {
		if(action.equals(Operate.close.getCode())){
			return _close(comment);
		}
		return _create(comment);

	}

	/**
	 * 把对应的交易状态改为已评论
	 * 如果对应的订单只有一个交易，那么把订单状态也改为已评论
	 * @param comment
	 * @return
	 */
	private EisMessage _close(Comment comment) {
		String tid = comment.getExtraValue("transactionId");
		if(tid == null){
			logger.warn("针对产品的评论[" + comment + "]没有对应的扩展数据transactionId");
			return new EisMessage(-EisError.dataError.id,"评论的扩展数据异常");
		}

		Item item = itemService.select(tid);
		if(item == null){
			logger.warn("找不到产品的评论[" + comment + "]对应的交易:" + tid);
			return new EisMessage(-EisError.dataError.id,"评论的扩展数据异常");
		}
		item.setCurrentStatus(TransactionStatus.commentClosed.id);
		itemService.changeStatus(item);

		long orderId = item.getCartId();
		if(orderId < 1){
			logger.warn("交易[" + tid + "]没有对应的订单论:" + orderId);
			return new EisMessage(-EisError.dataError.id,"评论的扩展数据异常");

		}
		/**
		 * 检查该订单对应的交易
		 * 如果有多个交易，检查是否都已经被评论，如果是则将订单状态设置为被评论
		 */
		Cart order = cartService.select(orderId);
		if(order == null){
			logger.warn("找不到交易[" + tid + "]对应的订单论:" + orderId);
			return new EisMessage(-EisError.dataError.id,"评论的扩展数据异常");
		}

		ItemCriteria itemCriteria = new ItemCriteria(comment.getOwnerId());
		if(order.getCreateTime() != null){
			itemCriteria.setEnterTimeBegin(DateUtils.truncate(order.getCreateTime(), Calendar.DAY_OF_MONTH));
			itemCriteria.setEnterTimeEnd(DateUtils.ceiling(order.getCreateTime(), Calendar.DAY_OF_MONTH));
		}
		List<Item> itemList = itemService.list(itemCriteria);

		logger.debug("订单[" + orderId + "]对应的交易数量是:" + (itemList == null ? "空" : itemList.size()));
		if(itemList == null || itemList.size() < 1){
			logger.error("找不到订单[" + orderId + "]对应的交易");
			return new EisMessage(-EisError.dataError.id,"评论的扩展数据异常");


		}
		if(itemList.size() == 1){
			logger.info("订单[" + orderId + "]对应的交易数量是1,将该订单状态也改为已评论");
			order.setCurrentStatus(TransactionStatus.commentClosed.id);
			cartService.update(order);
			return new EisMessage(OperateResult.success.getId(),"评论处理完毕");
		} 
		boolean bothCommented = true;
		for(Item i : itemList){
			if(i.getCurrentStatus() != TransactionStatus.commentClosed.id){
				bothCommented = false;
				break;
			}
		}
		if(bothCommented){
			logger.info("订单[" + orderId + "]所有交易都是已评论状态,将该订单状态也改为已评论");
			order.setCurrentStatus(TransactionStatus.commentClosed.id);
			cartService.update(order);
		}

		return new EisMessage(OperateResult.success.getId(),"评论处理完毕");
	}

	private EisMessage _create(Comment comment) {
		ItemCriteria itemCriteria = new ItemCriteria(comment.getOwnerId());
		itemCriteria.setChargeFromAccount(comment.getUuid());
		itemCriteria.setProductIds(comment.getObjectId());
		itemCriteria.setCurrentStatus(TransactionStatus.waitingComment.getId());
		int canCommentCount = itemService.count(itemCriteria);
		logger.debug("检查用户:" + comment.getUuid() + "购买产品:" + comment.getObjectId() + "的购买并且是等待评论状态的交易数是:" + canCommentCount);
		if(canCommentCount < 1){
			//不允许评论
//			return new EisMessage(-EisError.subscribeCountError.id, "不允许评论因为没有处于待评论状态");
		}
		Product product = productService.select(comment.getObjectId());
		if(product == null){
			logger.error("找不到评论对应的产品:" + comment.getObjectId());
			return new EisMessage(-EisError.subscribeCountError.id, "找不到对应的产品:" + comment.getObjectId());
		}
		String image = product.getExtraValue(DataName.productSmallImage.toString());
		/*Document document = null;
		try {
			document = productService.getRefDocument(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(document != null){
			if(comment.getExtraValue("refUrl") == null){
				comment.setExtraValue("refUrl", document.getViewUrl());
			}
			if(comment.getExtraValue("refTitle") == null){
				comment.setExtraValue("refTitle", document.getTitle());
			}
			if(image == null){
				image = document.getExtraValue(DataName.productSmallImage.toString());
			}
		}*/
		if(image != null){
			comment.setExtraValue("refImage", image);
		}
		return new EisMessage(OperateResult.success.getId(), "允许评论");
	}

}

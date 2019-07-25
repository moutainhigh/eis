package com.maicard.stat.service.impl.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;

import com.maicard.common.base.BaseService;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ItemLogService;
import com.maicard.product.service.ItemService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.stat.criteria.ItemStatCriteria;
import com.maicard.stat.service.ItemStatService;

public class TakedCardStatImpl extends BaseService{

	@Resource
	private ItemStatService itemStatService;
	@Resource
	private ItemLogService itemLogService;
	@Resource
	private ItemService itemService;
	@Resource
	private UserMessageService userMessageService;

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.statDayFormat);
	private final SimpleDateFormat sdf2 = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	public void run(){
		ItemStatCriteria itemStatCriteria = new ItemStatCriteria();
		String statDay =sdf.format(DateUtils.addDays(new Date(),  -1));
		logger.info("检查日期[" + statDay + "]的骏卡盛大点券黑单情况");
		itemStatCriteria.setBeginTime(statDay);
		List<Item> blackedAccountList = itemStatService.getJWShengdaTakeCard(itemStatCriteria);
		logger.info("日期[" + statDay + "]内骏卡冲盛大被黑单数量:" + ( blackedAccountList == null ? "无" : blackedAccountList.size()));
		if(blackedAccountList == null || blackedAccountList.size() < 1){
			return;
		}
		StringBuffer report = new StringBuffer();
		boolean alert = false;
		for(Item poolAccount : blackedAccountList){
			ItemCriteria itemCriteria = new ItemCriteria();
			//itemCriteria.setCurrentStatus(TransactionStatus.success.getId());
			itemCriteria.setTransactionId(poolAccount.getTransactionId());
			List<Item> itemLogList = itemLogService.list(itemCriteria);
			logger.info("蓄水池帐号[" + poolAccount.getTransactionId() + "/" + poolAccount.getContent() + "]对应的记录是:" + (itemLogList == null ? "空" : itemLogList.size()));
			if(itemLogList == null || itemLogList.size() < 1){
				continue;
			}
			for(Item itemLog : itemLogList){
				if(itemLog.getCurrentStatus() == TransactionStatus.success.getId()){
					logger.info("检查蓄水池帐号[" + poolAccount.getTransactionId() + "/" + poolAccount.getContent() + "]对应的成功充值订单，外部订单号:" + itemLog.getOutOrderId());
					ItemCriteria itemCriteria2 = new ItemCriteria();
					itemCriteria2.setInOrderId(itemLog.getOutOrderId());
					itemCriteria2.setQueryProcessingItem(DataName.only.toString());
					int inOrderCount = itemService.count(itemCriteria2);
					if(inOrderCount <= 0){
						logger.warn("蓄水池帐号[" + poolAccount.getTransactionId() + "/" + poolAccount.getContent() + "]对应的成功充值订单，外部订单号:" + itemLog.getOutOrderId() + "不存在，可能是被黑单");
						String account = itemLog.getContent();

						String messageId = itemLog.getMessageId();
						for(Item itemLog2 : itemLogList){
							if(messageId.equals(itemLog2.getMessageId()) && itemLog2.getCurrentStatus() == TransactionStatus.auctionSuccess.getId()){
								logger.info("找到了蓄水池帐号[" + poolAccount.getTransactionId() + "/" + poolAccount.getContent() + "]被黑订单[外部订单号:" + itemLog.getOutOrderId() + "]的对应卡密订单:" + itemLog2.getOutOrderId());

								Item cardItem = itemService.select(itemLog2.getOutOrderId());
								if(cardItem == null ){
									logger.error("找到了蓄水池帐号[" + poolAccount.getTransactionId() + "/" + poolAccount.getContent() + "]被黑订单[外部订单号:" + itemLog.getOutOrderId() + "]的对应卡密订单:" + itemLog2.getOutOrderId() + "，但找不到对应的Item");
									continue;
								}
								String vps = itemLog2.getName();
								String ip = itemLog2.getContent();
								String startTime = sdf2.format(itemLog2.getEnterTime());
								String msg = "卡密[订单号:" + itemLog2.getOutOrderId() + ",卡密:" + cardItem.getContent() + "]于[" + startTime + "]左右在VPS[" + vps + ",IP=" + ip + "]上给帐号[" + account + "]充值被黑单";
								alert = true;
								logger.warn(msg);
								report.append(msg);
								report.append("\n");

							}
						}
					} else {
						logger.info("蓄水池帐号[" + poolAccount.getTransactionId() + "/" + poolAccount.getContent() + "]对应的成功充值订单，外部订单号:" + itemLog.getOutOrderId() + "存在，数量是:" + inOrderCount);

					}
				}
			}
		}
		if(alert){
			sendAlert(report.toString());
		}
		

	}
	
	private void sendAlert(String msg){
		String statDay =sdf2.format(DateUtils.addDays(new Date(),  -1));
		UserMessage userMessage = new UserMessage();
		userMessage.setTopicId(5);
		userMessage.setTitle(statDay + "骏网冲盛大黑单统计");
		userMessage.setContent(msg);
		userMessage.setCurrentStatus(BasicStatus.normal.getId());
		logger.warn("发送骏卡黑单报警消息，内容大小:" + msg.length() + ":" + msg);
		userMessageService.send(userMessage);	
	}

}

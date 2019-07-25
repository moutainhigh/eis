package com.maicard.wpt.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.product.service.ActivityService;
import com.maicard.wpt.criteria.WeixinRichTextMessageCriteria;
import com.maicard.wpt.dao.WeixinRichTextMessageDao;
import com.maicard.wpt.domain.WeixinRichTextMessage;
import com.maicard.wpt.service.WeixinRichTextMessageService;

@Service
public class WeixinRichTextMessageServiceImpl extends BaseService implements WeixinRichTextMessageService {
	
	@Resource
	private WeixinRichTextMessageDao weixinRichTextMessageDao;
	

	@Resource
	private ActivityService activityService;

	@Resource
	private ApplicationContextService applicationContextService;

	@Override
	public int insert(WeixinRichTextMessage weixinRichTextMessage) {
		try{
			return weixinRichTextMessageDao.insert(weixinRichTextMessage);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public int update(WeixinRichTextMessage weixinRichTextMessage) {
		try{
			return  weixinRichTextMessageDao.update(weixinRichTextMessage);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
			}

	@Override
	public int delete(long weixinRichTextMessageId) {
		try{
			return  weixinRichTextMessageDao.delete(weixinRichTextMessageId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}
	
	@Override
	public WeixinRichTextMessage select(long weixinRichTextMessageId) {
		WeixinRichTextMessage weixinRichTextMessage =  weixinRichTextMessageDao.select(weixinRichTextMessageId);
		if(weixinRichTextMessage == null){
			weixinRichTextMessage = new WeixinRichTextMessage();
		}
		return weixinRichTextMessage;
	}
	
	


	@Override
	public List<WeixinRichTextMessage> list(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) {
		return weixinRichTextMessageDao.list(weixinRichTextMessageCriteria);
		
	}
	
	@Override
	public List<WeixinRichTextMessage> listOnPage(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) {
		return weixinRichTextMessageDao.listOnPage(weixinRichTextMessageCriteria);

	}
	
	@Override
	public int count(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) {
		return weixinRichTextMessageDao.count(weixinRichTextMessageCriteria);
	}
	

}

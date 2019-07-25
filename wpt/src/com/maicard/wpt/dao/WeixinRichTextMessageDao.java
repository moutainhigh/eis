package com.maicard.wpt.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.criteria.WeixinRichTextMessageCriteria;
import com.maicard.wpt.domain.WeixinRichTextMessage;


public interface WeixinRichTextMessageDao {
	int insert(WeixinRichTextMessage weixinRichTextMessage) throws DataAccessException;

	int update(WeixinRichTextMessage weixinRichTextMessage) throws DataAccessException;

	int delete(long weixinRichTextMessageId) throws DataAccessException;

	WeixinRichTextMessage select(long weixinRichTextMessageId) throws DataAccessException;
	
	List<WeixinRichTextMessage> list(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) throws DataAccessException;
	
	List<WeixinRichTextMessage> listOnPage(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) throws DataAccessException;
	
	int count(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) throws DataAccessException;
}

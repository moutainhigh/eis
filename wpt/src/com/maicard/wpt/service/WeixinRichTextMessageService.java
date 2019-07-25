package com.maicard.wpt.service;

import java.util.List;

import com.maicard.wpt.criteria.WeixinRichTextMessageCriteria;
import com.maicard.wpt.domain.WeixinRichTextMessage;

public interface WeixinRichTextMessageService {
	int insert(WeixinRichTextMessage weixinRichTextMessage);

	int update(WeixinRichTextMessage weixinRichTextMessage);

	int delete(long weixinRichTextMessageId);
	
	WeixinRichTextMessage select(long weixinRichTextMessageId);
	
	List<WeixinRichTextMessage> list(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria);

	List<WeixinRichTextMessage> listOnPage(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria);

	int count(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria);

}

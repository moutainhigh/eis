package com.maicard.wpt.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.wpt.criteria.WeixinRichTextMessageCriteria;
import com.maicard.wpt.dao.WeixinRichTextMessageDao;
import com.maicard.wpt.domain.WeixinRichTextMessage;

public class WeixinRichTextMessageDaoImpl extends BaseDao implements WeixinRichTextMessageDao {
	@Override
	public int insert(WeixinRichTextMessage weixinRichTextMessage) throws DataAccessException {
		return getSqlSessionTemplate().insert("WeixinRichTextMessage.insert", weixinRichTextMessage);
	}

	@Override
	public int update(WeixinRichTextMessage weixinRichTextMessage) throws DataAccessException {
		return getSqlSessionTemplate().update("WeixinRichTextMessage.update", weixinRichTextMessage);
	}

	@Override
	public int delete(long weixinRichTextMessageId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Mapbox.delete", weixinRichTextMessageId);
	}

	@Override
	public WeixinRichTextMessage select(long weixinRichTextMessageId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("WeixinRichTextMessage.select",weixinRichTextMessageId);
	}

	@Override
	public List<WeixinRichTextMessage> list(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) throws DataAccessException {
		Assert.notNull(weixinRichTextMessageCriteria, "weixinRichTextMessageCriteria must not be null");
		return getSqlSessionTemplate().selectList("WeixinRichTextMessage.list", weixinRichTextMessageCriteria);
	}

	@Override
	public List<WeixinRichTextMessage> listOnPage(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) throws DataAccessException {
		Assert.notNull(weixinRichTextMessageCriteria, "weixinRichTextMessageCriteria must not be null");
		Assert.notNull(weixinRichTextMessageCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(weixinRichTextMessageCriteria);
		Paging paging = weixinRichTextMessageCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("WeixinRichTextMessage.list", weixinRichTextMessageCriteria, rowBounds);
	}

	@Override
	public int count(WeixinRichTextMessageCriteria weixinRichTextMessageCriteria) throws DataAccessException {
		Assert.notNull(weixinRichTextMessageCriteria, "weixinRichTextMessageCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("WeixinRichTextMessage.count",weixinRichTextMessageCriteria)).intValue();
	}
}

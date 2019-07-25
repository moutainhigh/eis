package com.maicard.mb.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.dao.SubscribeMessageDao;

@Repository
public class SubscribeMessageDaoImpl extends BaseDao implements SubscribeMessageDao {

	public int insert(EisMessage subscribeMessage) throws DataAccessException {
		int ugid = Integer.parseInt("" + getSqlSessionTemplate().insert("SubscribeMessage.insert", subscribeMessage));
		return ugid;

	}

	public int update(EisMessage subscribeMessage) throws DataAccessException {
		return getSqlSessionTemplate().update("SubscribeMessage.update", subscribeMessage);
	}

	public int delete(int ugid) throws DataAccessException {
		return getSqlSessionTemplate().delete("SubscribeMessage.delete", new Integer(ugid));
	}

	

	public EisMessage select(int ugid) throws DataAccessException {
		return (EisMessage) getSqlSessionTemplate().selectOne("SubscribeMessage.select", new Integer(ugid));
	}

	public List<EisMessage> list(MessageCriteria subscribeMessageCriteria) throws DataAccessException {
		Assert.notNull(subscribeMessageCriteria, "subscribeMessageCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SubscribeMessage.list", subscribeMessageCriteria);
	}

	public List<EisMessage> listOnPage(MessageCriteria subscribeMessageCriteria) throws DataAccessException {
		Assert.notNull(subscribeMessageCriteria, "subscribeMessageCriteria must not be null");
		Assert.notNull(subscribeMessageCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(subscribeMessageCriteria);
		Paging paging = subscribeMessageCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("SubscribeMessage.list", subscribeMessageCriteria, rowBounds);
	}
	
	public int count(MessageCriteria subscribeMessageCriteria) throws DataAccessException {
		Assert.notNull(subscribeMessageCriteria, "subscribeMessageCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SubscribeMessage.count", subscribeMessageCriteria)).intValue();
	}
}

package com.maicard.mb.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.MessageTypeCriteria;
import com.maicard.mb.dao.MessageTypeDao;
import com.maicard.mb.domain.MessageType;

@Repository
public class MessageTypeDaoImpl extends BaseDao implements MessageTypeDao {

	public int insert(MessageType messageType) throws DataAccessException {
		int ugid = Integer.parseInt("" + getSqlSessionTemplate().insert("MessageType.insert", messageType));
		return ugid;

	}

	public int update(MessageType messageType) throws DataAccessException {
		return getSqlSessionTemplate().update("MessageType.update", messageType);
	}

	public int delete(int ugid) throws DataAccessException {
		return getSqlSessionTemplate().delete("MessageType.delete", new Integer(ugid));
	}

	

	public MessageType select(int ugid) throws DataAccessException {
		return (MessageType) getSqlSessionTemplate().selectOne("MessageType.select", new Integer(ugid));
	}

	public List<MessageType> list(MessageTypeCriteria messageTypeCriteria) throws DataAccessException {
		Assert.notNull(messageTypeCriteria, "messageTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("MessageType.list", messageTypeCriteria);
	}

	public List<MessageType> listOnPage(MessageTypeCriteria messageTypeCriteria) throws DataAccessException {
		Assert.notNull(messageTypeCriteria, "messageTypeCriteria must not be null");
		Assert.notNull(messageTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(messageTypeCriteria);
		Paging paging = messageTypeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("MessageType.list", messageTypeCriteria, rowBounds);
	}
	
	public int count(MessageTypeCriteria messageTypeCriteria) throws DataAccessException {
		Assert.notNull(messageTypeCriteria, "messageTypeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("MessageType.count", messageTypeCriteria)).intValue();
	}
}

package com.maicard.mb.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.dao.UserMessageDao;
import com.maicard.mb.domain.UserMessage;

@Repository
public class UserMessageDaoImpl extends BaseDao implements UserMessageDao {

	public int insert(UserMessage eisMessage) throws DataAccessException {
		return  getSqlSessionTemplate().insert("com.maicard.mb.sql.UserMessage.insert", eisMessage);

	}

	public int update(UserMessage eisMessage) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.mb.sql.UserMessage.update", eisMessage);
	}

	public int delete(String messageId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.mb.sql.UserMessage.delete", messageId);
	}

	public List<UserMessage> list(MessageCriteria messageCriteria) throws DataAccessException {
		Assert.notNull(messageCriteria, "messageCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.mb.sql.UserMessage.list", messageCriteria);
	}

	public List<UserMessage> listOnPage(MessageCriteria messageCriteria) throws DataAccessException {
		Assert.notNull(messageCriteria, "messageCriteria must not be null");
		Assert.notNull(messageCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(messageCriteria);
		Paging paging = messageCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.mb.sql.UserMessage.list", messageCriteria, rowBounds);
	}
	
	public int count(MessageCriteria messageCriteria) throws DataAccessException {
		Assert.notNull(messageCriteria, "messageCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.mb.sql.UserMessage.count", messageCriteria)).intValue();
	}

	@Override
	public List<String> getUniqueIdentify(MessageCriteria messageCriteria) {
		return getSqlSessionTemplate().selectList("com.maicard.mb.sql.UserMessage.getUniqueIdentify", messageCriteria);
	}

	@Override
	public int deleteSubscribe(MessageCriteria messageCriteria) {
		return getSqlSessionTemplate().delete("com.maicard.mb.sql.UserMessage.deleteSubscribe", messageCriteria);
	}
}

package com.maicard.product.dao.ibatis;

import java.util.List;


import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.NotifyLogCriteria;
import com.maicard.product.dao.NotifyLogDao;
import com.maicard.product.domain.NotifyLog;

@Repository
public class NotifyLogDaoImpl extends BaseDao implements NotifyLogDao {
		

	public int insert(NotifyLog notifyLog) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("NotifyLog.insert", notifyLog);
	}

	public NotifyLog select(String transactionId) throws DataAccessException {
		NotifyLog notifyLog = new NotifyLog();
		notifyLog.setTransactionId(transactionId);
		return (NotifyLog) getSqlSessionTemplate().selectOne("NotifyLog.select", notifyLog);
	}

	public List<NotifyLog> list(NotifyLogCriteria notifyLogCriteria) throws DataAccessException {
		Assert.notNull(notifyLogCriteria, "notifyLogCriteria must not be null");
		return getSqlSessionTemplate().selectList("NotifyLog.list", notifyLogCriteria);
	}

	public List<NotifyLog> listOnPage(NotifyLogCriteria notifyLogCriteria) throws DataAccessException {
		Assert.notNull(notifyLogCriteria, "notifyLogCriteria must not be null");
		Assert.notNull(notifyLogCriteria.getPaging(), "paging must not be null");
		int totalResults = count(notifyLogCriteria);
		Paging paging = notifyLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("NotifyLog.list", notifyLogCriteria, rowBounds);
	}

	public int count(NotifyLogCriteria notifyLogCriteria) throws DataAccessException {
		Assert.notNull(notifyLogCriteria, "notifyLogCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("NotifyLog.count", notifyLogCriteria)).intValue();
	}

	@Override
	public List<NotifyLog> getFailedNotify(NotifyLogCriteria notifyLogCriteria) {
		return getSqlSessionTemplate().selectList("NotifyLog.getFailedNotify", notifyLogCriteria);
	}
	
	@Override
	public List<NotifyLog> getUnSendNotify(NotifyLogCriteria notifyLogCriteria) {
		return getSqlSessionTemplate().selectList("NotifyLog.getUnSendNotify", notifyLogCriteria);
	}

	@Override
	public int cleanOldNotifyLog(NotifyLogCriteria notifyLogCriteria) {
		return getSqlSessionTemplate().delete("NotifyLog.cleanOldNotifyLog",notifyLogCriteria);
	}
	

}

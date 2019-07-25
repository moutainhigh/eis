package com.maicard.product.dao.ibatis;

import java.util.Date;
import java.util.List;





import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.FailedNotifyCriteria;
import com.maicard.product.dao.FailedNotifyDao;
import com.maicard.product.domain.FailedNotify;

@Repository
public class FailedNotifyDaoImpl extends BaseDao implements FailedNotifyDao {
		

	public int insert(FailedNotify failedNotify) throws DataAccessException {
		if(failedNotify.getFirstSendTime() == null){
			failedNotify.setFirstSendTime(new Date());
		}
		return (Integer)getSqlSessionTemplate().insert("com.maicard.product.sql.FailedNotify.insert", failedNotify);
	}

	public FailedNotify select(String transactionId) throws DataAccessException {
		return (FailedNotify) getSqlSessionTemplate().selectOne("com.maicard.product.sql.FailedNotify.select", transactionId);
	}

	public List<FailedNotify> list(FailedNotifyCriteria failedNotifyCriteria) throws DataAccessException {
		Assert.notNull(failedNotifyCriteria, "failedNotifyCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.FailedNotify.list", failedNotifyCriteria);
	}

	public List<FailedNotify> listOnPage(FailedNotifyCriteria failedNotifyCriteria) throws DataAccessException {
		Assert.notNull(failedNotifyCriteria, "failedNotifyCriteria must not be null");
		Assert.notNull(failedNotifyCriteria.getPaging(), "paging must not be null");
		int totalResults = count(failedNotifyCriteria);
		Paging paging = failedNotifyCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.FailedNotify.list", failedNotifyCriteria, rowBounds);
	}

	public int count(FailedNotifyCriteria failedNotifyCriteria) throws DataAccessException {
		Assert.notNull(failedNotifyCriteria, "failedNotifyCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.product.sql.FailedNotify.count", failedNotifyCriteria)).intValue();
	}

	@Override
	public int delete(String transactionId) {
		Assert.notNull(transactionId, "transactionId must not be null when delete failed notify");
		return getSqlSessionTemplate().update("com.maicard.product.sql.FailedNotify.delete", transactionId);
	}

	@Override
	public int update(FailedNotify failedNotify) {
		return getSqlSessionTemplate().update("com.maicard.product.sql.FailedNotify.update", failedNotify);
	}

	@Override
	public int replace(FailedNotify failedNotify) {
		Assert.notNull(failedNotify, "failedNotify must not be null when replace failed notify");
		Assert.notNull(failedNotify.getTransactionId(), "transactionId must not be null when replace failed notify");
		FailedNotify _oldNotify = select(failedNotify.getTransactionId());
		if(_oldNotify == null){
			if(failedNotify.getFirstSendTime() == null){
				failedNotify.setFirstSendTime(new Date());
			}
			return insert(failedNotify);
		}
		if(failedNotify.getFirstSendTime() == null){
			failedNotify.setFirstSendTime(_oldNotify.getFirstSendTime());
		}
		if(failedNotify.getFirstSendTime() == null){
			failedNotify.setFirstSendTime(new Date());
		}
		
		
		
		return update(failedNotify);
	}

	
	

}

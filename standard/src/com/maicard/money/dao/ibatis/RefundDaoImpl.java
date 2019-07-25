package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.money.criteria.RefundCriteria;
import com.maicard.money.dao.RefundDao;
import com.maicard.money.domain.Refund;

import javax.annotation.Resource;

@Repository
public class RefundDaoImpl extends BaseDao implements RefundDao {
	
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	
	private static final String defaultTableName = "refund";

	public int insert(Refund refund) throws DataAccessException {
		refund.setTableName(defaultTableName);
		return getSqlSessionTemplate().insert("Refund.insert", refund);
	}

	public int update(Refund refund) throws Exception {
		if(refund.getTransactionId() == null || refund.getTransactionId().equals("")){
			throw new RequiredAttributeIsNullException("要更新的支付订单没有订单号");
		}
		refund.setTableName(defaultTableName);
		return getSqlSessionTemplate().update("Refund.update", refund);
	}

	public int delete(String transactionId) throws DataAccessException {
		Refund refund = new Refund();
		refund.setTransactionId(transactionId);
		refund.setTableName(defaultTableName);
		return getSqlSessionTemplate().delete("Refund.delete", refund);
	}

	public Refund select(String transactionId) throws DataAccessException {
		Refund refund = new Refund();
		refund.setTransactionId(transactionId);
		return getSqlSessionTemplate().selectOne("Refund.select", refund);
	}

	public List<Refund> list(RefundCriteria refundCriteria) throws DataAccessException {
		Assert.notNull(refundCriteria, "refundCriteria must not be null");
		return getSqlSessionTemplate().selectList("Refund.list", refundCriteria);
	}

	public List<Refund> listOnPage(RefundCriteria refundCriteria) throws DataAccessException {
		Assert.notNull(refundCriteria, "refundCriteria must not be null");
		Assert.notNull(refundCriteria.getPaging(), "paging must not be null");
		int totalResults = count(refundCriteria);
		Paging paging = refundCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("Refund.list", refundCriteria, rowBounds);
	}

	
	public int count(RefundCriteria refundCriteria) throws DataAccessException {
		Assert.notNull(refundCriteria, "refundCriteria must not be null");
		return getSqlSessionTemplate().selectOne("Refund.count", refundCriteria);
	}

	
	

}

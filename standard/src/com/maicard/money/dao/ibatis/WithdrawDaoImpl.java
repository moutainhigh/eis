package com.maicard.money.dao.ibatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.dao.WithdrawDao;
import com.maicard.money.domain.Withdraw;

import javax.annotation.Resource;

@Repository
public class WithdrawDaoImpl extends BaseDao implements WithdrawDao {
	
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	
	private static final String defaultTableName = "withdraw";

	public int insert(Withdraw withdraw) throws DataAccessException {
		withdraw.setTableName(defaultTableName);
		return getSqlSessionTemplate().insert("com.maicard.money.sql.Withdraw.insert", withdraw);
	}

	public int update(Withdraw withdraw) throws Exception {
		if(withdraw.getTransactionId() == null || withdraw.getTransactionId().equals("")){
			throw new RequiredAttributeIsNullException("要更新的支付订单没有订单号");
		}
		withdraw.setTableName(defaultTableName);
		return getSqlSessionTemplate().update("com.maicard.money.sql.Withdraw.update", withdraw);
	}

	public int delete(String transactionId) throws DataAccessException {
		Withdraw withdraw = new Withdraw();
		withdraw.setTransactionId(transactionId);
		withdraw.setTableName(defaultTableName);
		return getSqlSessionTemplate().delete("com.maicard.money.sql.Withdraw.delete", withdraw);
	}

	public Withdraw select(String transactionId) throws DataAccessException {
		//Withdraw withdraw = new Withdraw();
		//withdraw.setTransactionId(transactionId);
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.Withdraw.select", transactionId);
	}

	public List<Withdraw> list(WithdrawCriteria withdrawCriteria) throws DataAccessException {
		Assert.notNull(withdrawCriteria, "withdrawCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Withdraw.list", withdrawCriteria);
	}

	public List<Withdraw> listOnPage(WithdrawCriteria withdrawCriteria) throws DataAccessException {
		Assert.notNull(withdrawCriteria, "withdrawCriteria must not be null");
		Assert.notNull(withdrawCriteria.getPaging(), "paging must not be null");
		int totalResults = count(withdrawCriteria);
		Paging paging = withdrawCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Withdraw.list", withdrawCriteria, rowBounds);
	}



	public int count(WithdrawCriteria withdrawCriteria) throws DataAccessException {
		Assert.notNull(withdrawCriteria, "withdrawCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.Withdraw.count", withdrawCriteria)).intValue();
	}

    @Override
    public Withdraw queryByChannelRequestNo(String channelReqNo) {
		Withdraw withdraw = new Withdraw();
		withdraw.setCurChannelRequestNo(channelReqNo);
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.Withdraw.queryByChannelRequestNo", withdraw);
    }

    @Override
    public int updateWithdrawForManualOperate(Map map) {
		return getSqlSessionTemplate().update("com.maicard.money.sql.Withdraw.updateWithdrawForManualOperate", map);
    }


}

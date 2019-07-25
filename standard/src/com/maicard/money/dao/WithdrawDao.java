package com.maicard.money.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.domain.Withdraw;

public interface WithdrawDao {

	int insert(Withdraw withdraw) throws DataAccessException;

	int update(Withdraw withdraw) throws Exception;

	int delete(String transactionId) throws DataAccessException;

	Withdraw select(String transactionId) throws DataAccessException;

	List<Withdraw> list(WithdrawCriteria withdrawCriteria) throws DataAccessException;
	
	List<Withdraw> listOnPage(WithdrawCriteria withdrawCriteria) throws DataAccessException;
		
	int count(WithdrawCriteria withdrawCriteria) throws DataAccessException;

	/**
	 * 根据渠道请求号获取支付订单信息
	 * @param channelReqNo
	 * @return
	 */
	public Withdraw queryByChannelRequestNo(String channelReqNo);
	/**
	 * 人工操作修改订单状态
	 * @param map
	 * @return 修改行数
	 */
	public int updateWithdrawForManualOperate(Map map);
}

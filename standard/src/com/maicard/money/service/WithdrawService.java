package com.maicard.money.service;

import java.util.List;
import java.util.Map;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.security.domain.User;

public interface WithdrawService {

	int insert(Withdraw withdraw);

	int update(Withdraw withdraw) throws Exception;

	int delete(String  transactionId);
	
	Withdraw select(String transactionId);
	
	List<Withdraw> list(WithdrawCriteria withdrawCriteria);

	List<Withdraw> listOnPage(WithdrawCriteria withdrawCriteria);
	
	EisMessage end(int withdrawMethodId, String resultString);
	
	int count(WithdrawCriteria withdrawCriteria);	
	

	/**
	 * 开始一个提现
	 * @param withdraw
	 * @return
	 * @throws Exception 
	 */
	int begin(Withdraw withdraw, List<Withdraw> subWithdrawList) throws Exception;
		
	EisMessage end(Withdraw withdraw) throws Exception;

	int isValidWithdraw(Withdraw withdraw);

	/**
	 * 返回一个支持的提现方法列表，不可返回null
	 * @param pay
	 * @param partner
	 * @return
	 */
	List<WithdrawMethod> getWithdrawMethod(Withdraw pay, User partner);

	int save(Withdraw withdraw);

	int updateWithdrawForManualOperate(@SuppressWarnings("rawtypes") Map map);

	Withdraw queryByChannelRequestNo(String channelReqNo);



	

}

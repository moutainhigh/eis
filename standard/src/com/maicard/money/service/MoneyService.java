package com.maicard.money.service;

import java.util.List;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.criteria.MoneyCriteria;
import com.maicard.money.domain.Money;

public interface MoneyService {

	EisMessage insert(Money money);

	int update(Money money);

	int delete(long uuid);
	
//	Money select(long uuid);

	List<Money> list(MoneyCriteria moneyCriteria);
    
	List<Money> listByPartner();
	
	List<Money> listOnPage(MoneyCriteria moneyCriteria);	
	
	int count(MoneyCriteria moneyCriteria);

	EisMessage plus(Money money);

	EisMessage minus(Money money);
	
/*	EisMessage minus(Money money ,String code);
*/
	EisMessage lock(Money money);

	EisMessage unLock(Money money);

	//int lockLocal(Money money);

	//int plusLocal(Money money);

	//int unLockLocal(Money money);

	//int minusLocal(Money money);
    
	//int chargeMoney(Money money);
	
	//void chargeMoneyLocal(Money money);

	boolean haveEnoughMoney(Money money);

	int updateLocal(Money money);

	Money selectLocal(long uuid);

	Money select(long uuid, long ownerId);
	
	Money select(long uuid, long ownerId, boolean needLock);

	
	/**
	 * 设置一个指定时间段的瞬间资金账户到REDIS中，例如每天的第一单金额
	 * 
	 *
	 * @author GHOST
	 * @date 2017-12-13
	 */
	boolean snapBalance(Money mony, String balanceTime, long liveSec);

	Money getSnapBalance(long uuid, String balanceTime);


}

package com.maicard.money.service;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.domain.Withdraw;

public interface WithdrawTxService {

	EisMessage end(int withdrawMethodId, String resultString);
	
	EisMessage end(Withdraw withdraw) throws Exception;

}

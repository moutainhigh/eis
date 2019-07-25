package com.maicard.money.service;

import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.domain.Withdraw;

public interface ChannelService {

	public PayChannelMechInfo  getChannelInfo(Pay pay);
	
	public PayChannelMechInfo  getChannelInfo(Withdraw withdraw);

}

package com.maicard.money.service;

import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayMethod;
import com.maicard.security.domain.User;

public interface ChannelRobinService {

	PayMethod getPayMethod(Pay pay, User partner);

}

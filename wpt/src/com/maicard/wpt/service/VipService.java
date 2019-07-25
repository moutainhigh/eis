package com.maicard.wpt.service;

import java.util.Date;

import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;

public interface VipService {
	Date getExpireTime(User user);
	
	UserRelation getVipRelation(User user);
}

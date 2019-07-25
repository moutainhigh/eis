package com.maicard.product.service;

import com.maicard.common.domain.EisMessage;
import com.maicard.product.domain.Activity;
import com.maicard.security.domain.User;

public interface ActivityProcessor {
	
	EisMessage prepare(Activity activity, User user, Object parameter);

	EisMessage execute(String action, Activity activity, Object targetObject, Object parameter);
	
	
}

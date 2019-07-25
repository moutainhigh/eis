package com.maicard.wpt.service;

import com.maicard.security.domain.User;

public interface ScanCodeService {
	
	Object getScanResult(User user, Object msg, String identify);

}

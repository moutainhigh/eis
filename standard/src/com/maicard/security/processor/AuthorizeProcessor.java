package com.maicard.security.processor;

import com.maicard.security.domain.User;

public interface AuthorizeProcessor {
	Object filter(User user, Object targetObject, String operateCode);

}

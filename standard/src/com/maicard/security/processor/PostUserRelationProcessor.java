package com.maicard.security.processor;

import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;

public interface PostUserRelationProcessor {
	
	/**
	 * 当新建或删除一个用户关联后的处理程序
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-25
	 */
	void postProcess(User frontUser, UserRelation userRelation, String action);

}

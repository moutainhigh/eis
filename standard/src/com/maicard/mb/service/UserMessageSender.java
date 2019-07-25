package com.maicard.mb.service;



public interface UserMessageSender {

	int send(String toUser, Object content, long ownerId);
	
		


}

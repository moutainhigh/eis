package com.maicard.security.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.domain.Money;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;

public interface FrontUserService {

	int insert(User frontUser) throws Exception;

	EisMessage update(User frontUser) throws Exception;	
	


	EisMessage delete(User frontUser) throws Exception;

	User select(long fuuid);

	List<User> list(UserCriteria frontUserCriteria);

	List<User> listByPrepayment(long uuid);
	
	List<User> listOnPage(UserCriteria frontUserCriteria);

	int count(UserCriteria frontUserCriteria);

	User login(UserCriteria frontUserCriteria);

	void correctUserConfig(User frontUser);

	void correctUserAttributes(User frontUser);

	void processBindEmailData(User frontUser, String bindMailBox) throws Exception;

	String processBindPhoneData(User frontUser, String phone) throws Exception;

	void calculateUserPromotion(User user, Money money);

	void calculateUserPromotion(long uuid, Money money);

	User getUnLoginedUser();

	String getTotalUser();

	String searchInviter(long uuid);

	User lockUser(UserCriteria userCriteria);

	void reSyncLostUser(long chargeFromAccount);

	void reSyncUuidNotMatchUser(User user);
	
	String downloadNewAccountCsv(int num);
	
	String downloadbalanceCsv(int num);
	
	String makeCollection(String custdata);

	void updateUserHeadPic(User frontUser, String textValue);

	int updateNoNull(User modUser);

	@Async
	void updateAsync(User frontUser);

}

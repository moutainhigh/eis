package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;

public interface FrontUserDao {

	int insert(User frontUser) throws DataAccessException;

	int update(User frontUser) throws DataAccessException;

	int delete(long fuuid) throws DataAccessException;

	User select(long fuuid) throws DataAccessException;

	List<User> list(UserCriteria frontUserCriteria) throws DataAccessException;
		
	List<User> listOnPage(UserCriteria frontUserCriteria) throws DataAccessException;
	
	List<Long> listPk(UserCriteria frontUserCriteria) throws DataAccessException;
	
	List<Long> listPkOnPage(UserCriteria frontUserCriteria) throws DataAccessException;
 
	int updateDynamicData(UserDynamicData userDynamicData)throws DataAccessException;
	
	int count(UserCriteria frontUserCriteria) throws DataAccessException;

	List<User> listOnPageByPartner(UserCriteria frontUserCriteria);
	
	List<User> listByPrepayment(long uuid);
	 
	String search_Inviter(long uuid);

	User lockUser(UserCriteria userCriteria);

	int changeUuid(User frontUser);
	
	String downloadNewAccountCsv(int num);
	
	String downloadBalanceCsv(int num);

	String makeCollection(String custdata);

	int updateNoNull(User frontUser) throws DataAccessException;

	long getMaxId();
}

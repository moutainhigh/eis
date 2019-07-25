package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.AwardCriteria;
import com.maicard.money.domain.Award;


public interface AwardDao {

	int insert(Award award) throws DataAccessException;

	int update(Award award) throws DataAccessException;
	
	int delete(long awardId) throws DataAccessException;

	List<Award> list(AwardCriteria awardCriteria) throws DataAccessException;
		
	List<Award> listOnPage(AwardCriteria awardCriteria) throws DataAccessException;
	
	int count(AwardCriteria awardCriteria) throws DataAccessException;


	Award select(long awardId);

}

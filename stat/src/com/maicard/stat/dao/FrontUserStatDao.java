package com.maicard.stat.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.stat.criteria.FrontUserStatCriteria;
import com.maicard.stat.domain.FrontUserStat;

public interface FrontUserStatDao {	
	
	List<FrontUserStat> listOnPage(FrontUserStatCriteria frontUserStatCriteria) throws DataAccessException;	

	int count(FrontUserStatCriteria frontUserStatCriteria);
	

}

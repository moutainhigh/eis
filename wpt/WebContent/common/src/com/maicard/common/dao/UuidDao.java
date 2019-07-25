package com.maicard.common.dao;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.UserUniqueCriteria;
import com.maicard.common.domain.UserUnique;
import com.maicard.common.domain.Uuid;


public interface UuidDao {

	long insert(Uuid uuid) throws DataAccessException;

	int insert(UserUnique userUniqueId);

	long getMaxIdForUser(UserUniqueCriteria userUniqueCriteria);


}

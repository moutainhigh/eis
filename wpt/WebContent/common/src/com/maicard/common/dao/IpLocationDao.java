package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.IpLocationCriteria;
import com.maicard.common.domain.IpLocation;

public interface IpLocationDao {

	int insert(IpLocation ipLocation) throws DataAccessException;

	int update(IpLocation ipLocation) throws DataAccessException;

	int delete(int ipLocationId) throws DataAccessException;

	IpLocation select(int ipLocationId) throws DataAccessException;

	List<IpLocation> list(IpLocationCriteria ipLocationCriteria) throws DataAccessException;
	
	List<IpLocation> listOnPage(IpLocationCriteria ipLocationCriteria) throws DataAccessException;
	
	int count(IpLocationCriteria ipLocationCriteria) throws DataAccessException;
	
	List<Integer> listPk(IpLocationCriteria ipLocationCriteria);
	
	List<Integer> listPkOnPage(IpLocationCriteria ipLocationCriteria);

}

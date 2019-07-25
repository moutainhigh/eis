package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.AdvertCriteria;
import com.maicard.site.domain.Advert;

public interface AdvertDao {

	int insert(Advert advert) throws DataAccessException;

	int update(Advert advert) throws DataAccessException;

	int delete(int advertId) throws DataAccessException;

	Advert select(int advertId) throws DataAccessException;

	List<Advert> list(AdvertCriteria advertCriteria) throws DataAccessException;
	
	List<Advert> listOnPage(AdvertCriteria advertCriteria) throws DataAccessException;
	
	int count(AdvertCriteria advertCriteria) throws DataAccessException;

	List<Integer> listPk(AdvertCriteria advertCriteria)
			throws DataAccessException;

	List<Integer> listPkOnPage(AdvertCriteria advertCriteria)
			throws DataAccessException;

}

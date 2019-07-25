package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.StaticizeCriteria;
import com.maicard.site.domain.Staticize;

public interface StaticizeDao {

	int insert(Staticize staticize) throws DataAccessException;

	int update(Staticize staticize) throws DataAccessException;

	int delete(long staticizeId) throws DataAccessException;

	Staticize select(long staticizeId) throws DataAccessException;

	List<Staticize> list(StaticizeCriteria staticizeCriteria) throws DataAccessException;
	
	List<Staticize> listOnPage(StaticizeCriteria staticizeCriteria) throws DataAccessException;
	
	int count(StaticizeCriteria staticizeCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(StaticizeCriteria staticizeCriteria)
			throws DataAccessException;

	List<Integer> listPk(StaticizeCriteria staticizeCriteria)
			throws DataAccessException;

}

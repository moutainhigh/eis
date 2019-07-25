package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.JobCriteria;
import com.maicard.common.domain.Job;

public interface JobDao {

	int insert(Job job) throws DataAccessException;

	int update(Job job) throws DataAccessException;

	int delete(int jobId) throws DataAccessException;

	Job select(int jobId) throws DataAccessException;

	List<Job> list(JobCriteria jobCriteria) throws DataAccessException;
	
	List<Job> listOnPage(JobCriteria jobCriteria) throws DataAccessException;
	
	int count(JobCriteria jobCriteria) throws DataAccessException;

}

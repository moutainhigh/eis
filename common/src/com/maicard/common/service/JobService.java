package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.JobCriteria;
import com.maicard.common.domain.Job;

public interface JobService {

	int insert(Job job);

	int update(Job job);

	int delete(int jobId);
	
	Job select(int jobId);

	List<Job> list(JobCriteria jobCriteria);


}

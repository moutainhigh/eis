package com.maicard.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.JobCriteria;
import com.maicard.common.dao.JobDao;
import com.maicard.common.domain.Job;
import com.maicard.common.service.JobService;

@Service
public class JobServiceImpl extends BaseService implements JobService {

	@Resource
	private JobDao jobDao;
	

	public int insert(Job job) {
		try{
			return jobDao.insert(job);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(Job job) {
		try{
			return  jobDao.update(job);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int jobId) {
		try{
			return  jobDao.delete(jobId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;		
		
	}
	
	public Job select(int jobId) {
		return jobDao.select(jobId);
	}

	public List<Job> list(JobCriteria jobCriteria) {
		List<Job> jobList = jobDao.list(jobCriteria);
		if(jobList != null){
			for(int i = 0; i < jobList.size(); i++){
				jobList.get(i).setIndex(i+1);
			//	afterFetch(jobList.get(i));
			}
		}
		return jobList;
	}
	
	/*private void afterFetch(Job job) {
		if(job == null){
			return;
		}
		if(job.getBeanName() == null || job.getBeanName().equals("")){
			return;
		}
		if(applicationContextService.getBean(job.getBeanName()) == null){
			job.setCurrentStatus(EisError.serviceUnavaiable.getId());
		}
		try{
			job.setCurrentStatusName(BasicStatus.disable.findById(job.getCurrentStatus()).getName());
			job.setRunningStatusName(ServiceStatus.closed.findById(job.getRunningStatus()).getName());
		}catch(Exception e){}
		job.setId(job.getJobId());
		
	}*/

}

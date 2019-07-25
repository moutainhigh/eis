package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.JobCriteria;
import com.maicard.common.dao.JobDao;
import com.maicard.common.domain.Job;
import com.maicard.common.util.Paging;

@Repository
public class JobDaoImpl extends BaseDao implements JobDao {

	public int insert(Job job) throws DataAccessException {
		return getSqlSessionTemplate().insert("Job.insert", job);
	}

	public int update(Job job) throws DataAccessException {
		return getSqlSessionTemplate().update("Job.update", job);
	}

	public int delete(int jobId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Job.delete", new Integer(jobId));
	}

	public Job select(int jobId) throws DataAccessException {
		return (Job) getSqlSessionTemplate().selectOne("Job.select", new Integer(jobId));
	}

	public List<Job> list(JobCriteria jobCriteria) throws DataAccessException {
		Assert.notNull(jobCriteria, "jobCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Job.list", jobCriteria);
	}

	public List<Job> listOnPage(JobCriteria jobCriteria) throws DataAccessException {
		Assert.notNull(jobCriteria, "jobCriteria must not be null");
		Assert.notNull(jobCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(jobCriteria);
		Paging paging = jobCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Job.list", jobCriteria, rowBounds);
	}

	public int count(JobCriteria jobCriteria) throws DataAccessException {
		Assert.notNull(jobCriteria, "jobCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("Job.count", jobCriteria)).intValue();
	}

}

package com.maicard.flow.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.flow.criteria.WorkflowInstanceLogCriteria;
import com.maicard.flow.dao.WorkflowInstanceLogDao;
import com.maicard.flow.domain.WorkflowInstanceLog;

@Repository
public class WorkflowInstanceLogDaoImpl extends BaseDao implements WorkflowInstanceLogDao {

	public void insert(WorkflowInstanceLog workflowInstanceLog) throws DataAccessException {
		getSqlSessionTemplate().insert("com.maicard.flow.sql.WorkflowInstanceLog.insert", workflowInstanceLog);
	}

	public int update(WorkflowInstanceLog workflowInstanceLog) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.flow.sql.WorkflowInstanceLog.update", workflowInstanceLog);
	}

	public int delete(int workflowInstanceLogId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.flow.sql.WorkflowInstanceLog.delete", new Integer(workflowInstanceLogId));
	}

	public WorkflowInstanceLog select(int workflowInstanceLogId) throws DataAccessException {
		return (WorkflowInstanceLog) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.WorkflowInstanceLog.select", new Integer(workflowInstanceLogId));
	}

	public List<WorkflowInstanceLog> list(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) throws DataAccessException {
		Assert.notNull(workflowInstanceLogCriteria, "workflowInstanceLogCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.WorkflowInstanceLog.list", workflowInstanceLogCriteria);
	}

	public List<WorkflowInstanceLog> listOnPage(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) throws DataAccessException {
		Assert.notNull(workflowInstanceLogCriteria, "workflowInstanceLogCriteria must not be null");
		Assert.notNull(workflowInstanceLogCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(workflowInstanceLogCriteria);
		Paging paging = workflowInstanceLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.WorkflowInstanceLog.list", workflowInstanceLogCriteria, rowBounds);
	}

	public int count(WorkflowInstanceLogCriteria workflowInstanceLogCriteria) throws DataAccessException {
		Assert.notNull(workflowInstanceLogCriteria, "workflowInstanceLogCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.WorkflowInstanceLog.count", workflowInstanceLogCriteria)).intValue();
	}

}

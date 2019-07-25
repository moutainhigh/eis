package com.maicard.flow.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.dao.WorkflowInstanceDao;
import com.maicard.flow.domain.WorkflowInstance;

@Repository
public class WorkflowInstanceDaoImpl extends BaseDao implements WorkflowInstanceDao {

	public int insert(WorkflowInstance workflowInstance) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.flow.sql.WorkflowInstance.insert", workflowInstance);
	}

	public int update(WorkflowInstance workflowInstance) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.flow.sql.WorkflowInstance.update", workflowInstance);
	}

	public int delete(int workflowInstanceId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.flow.sql.WorkflowInstance.delete", new Integer(workflowInstanceId));

	}

	public WorkflowInstance select(int workflowInstanceId) throws DataAccessException {
		return (WorkflowInstance) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.WorkflowInstance.select", new Integer(workflowInstanceId));
	}

	public List<WorkflowInstance> list(WorkflowInstanceCriteria workflowInstanceCriteria) throws DataAccessException {
		Assert.notNull(workflowInstanceCriteria, "workflowInstanceCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.WorkflowInstance.list", workflowInstanceCriteria);
	}

	public List<WorkflowInstance> listOnPage(WorkflowInstanceCriteria workflowInstanceCriteria) throws DataAccessException {
		Assert.notNull(workflowInstanceCriteria, "workflowInstanceCriteria must not be null");
		Assert.notNull(workflowInstanceCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(workflowInstanceCriteria);
		Paging paging = workflowInstanceCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.WorkflowInstance.list", workflowInstanceCriteria, rowBounds);
	}

	public int count(WorkflowInstanceCriteria workflowInstanceCriteria) throws DataAccessException {
		Assert.notNull(workflowInstanceCriteria, "workflowInstanceCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.WorkflowInstance.count", workflowInstanceCriteria)).intValue();
	}

}

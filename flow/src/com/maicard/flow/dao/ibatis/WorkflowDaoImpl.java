package com.maicard.flow.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.flow.criteria.WorkflowCriteria;
import com.maicard.flow.dao.WorkflowDao;
import com.maicard.flow.domain.Workflow;

@Repository
public class WorkflowDaoImpl extends BaseDao implements WorkflowDao {

	public int insert(Workflow workflow) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.flow.sql.Workflow.insert", workflow);
	}

	public int update(Workflow workflow) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.flow.sql.Workflow.update", workflow);
	}

	public int delete(int workflowId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.flow.sql.Workflow.delete", new Integer(workflowId));
	}

	public Workflow select(int workflowId) throws DataAccessException {
		return (Workflow) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.Workflow.select", new Integer(workflowId));
	}

	public List<Workflow> list(WorkflowCriteria workflowCriteria) throws DataAccessException {
		Assert.notNull(workflowCriteria, "workflowCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.Workflow.list", workflowCriteria);
	}

	public List<Workflow> listOnPage(WorkflowCriteria workflowCriteria) throws DataAccessException {
		Assert.notNull(workflowCriteria, "workflowCriteria must not be null");
		Assert.notNull(workflowCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(workflowCriteria);
		Paging paging = workflowCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.Workflow.list", workflowCriteria, rowBounds);
	}

	public int count(WorkflowCriteria workflowCriteria) throws DataAccessException {
		Assert.notNull(workflowCriteria, "workflowCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.Workflow.count", workflowCriteria)).intValue();
	}

}

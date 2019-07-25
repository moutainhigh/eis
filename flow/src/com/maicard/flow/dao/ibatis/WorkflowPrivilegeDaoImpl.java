package com.maicard.flow.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.flow.criteria.WorkflowPrivilegeCriteria;
import com.maicard.flow.dao.WorkflowPrivilegeDao;
import com.maicard.flow.domain.WorkflowPrivilege;

@Repository
public class WorkflowPrivilegeDaoImpl extends BaseDao implements WorkflowPrivilegeDao {

	public int insert(WorkflowPrivilege workflowPrivilege) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.flow.sql.WorkflowPrivilege.insert", workflowPrivilege);
	}

	public int update(WorkflowPrivilege workflowPrivilege) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.flow.sql.WorkflowPrivilege.update", workflowPrivilege);
	}

	public int delete(long workflowPrivilegeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.flow.sql.WorkflowPrivilege.delete", workflowPrivilegeId);

	}

	public WorkflowPrivilege select(long workflowPrivilegeId) throws DataAccessException {
		return (WorkflowPrivilege) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.WorkflowPrivilege.select", workflowPrivilegeId);
	}

	public List<WorkflowPrivilege> list(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(workflowPrivilegeCriteria, "workflowPrivilegeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.WorkflowPrivilege.list", workflowPrivilegeCriteria);
	}

	public List<WorkflowPrivilege> listOnPage(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(workflowPrivilegeCriteria, "workflowPrivilegeCriteria must not be null");
		Assert.notNull(workflowPrivilegeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(workflowPrivilegeCriteria);
		Paging paging = workflowPrivilegeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.WorkflowPrivilege.list", workflowPrivilegeCriteria, rowBounds);
	}

	public int count(WorkflowPrivilegeCriteria workflowPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(workflowPrivilegeCriteria, "workflowPrivilegeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.WorkflowPrivilege.count", workflowPrivilegeCriteria)).intValue();
	}

}

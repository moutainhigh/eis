package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.dao.DocumentNodeRelationDao;
import com.maicard.site.domain.DocumentNodeRelation;

import org.apache.ibatis.session.RowBounds;

@Repository
public class DocumentNodeRelationDaoImpl extends BaseDao implements DocumentNodeRelationDao {

	public int insert(DocumentNodeRelation documentNodeRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("DocumentNodeRelation.insert", documentNodeRelation)).intValue();
	}

	public int update(DocumentNodeRelation documentNodeRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("DocumentNodeRelation.update", documentNodeRelation);


	}

	public int delete(int documentNodeRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("DocumentNodeRelation.delete", new Integer(documentNodeRelationId));


	}

	public DocumentNodeRelation select(int documentNodeRelationId) throws DataAccessException {
		return (DocumentNodeRelation) getSqlSessionTemplate().selectOne("DocumentNodeRelation.select", new Integer(documentNodeRelationId));
	}


	public List<DocumentNodeRelation> list(DocumentNodeRelationCriteria documentNodeRelationCriteria) throws DataAccessException {
		Assert.notNull(documentNodeRelationCriteria, "documentNodeRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("DocumentNodeRelation.list", documentNodeRelationCriteria);
	}


	public List<DocumentNodeRelation> listOnPage(DocumentNodeRelationCriteria documentNodeRelationCriteria) throws DataAccessException {
		Assert.notNull(documentNodeRelationCriteria, "documentNodeRelationCriteria must not be null");
		Assert.notNull(documentNodeRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(documentNodeRelationCriteria);
		Paging paging = documentNodeRelationCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DocumentNodeRelation.list", documentNodeRelationCriteria, rowBounds);
	}

	public int count(DocumentNodeRelationCriteria documentNodeRelationCriteria) throws DataAccessException {
		Assert.notNull(documentNodeRelationCriteria, "documentNodeRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("DocumentNodeRelation.count", documentNodeRelationCriteria)).intValue();
	}

	@Override
	public int delete(DocumentNodeRelationCriteria documentNodeRelationCriteria) {
		return getSqlSessionTemplate().delete("DocumentNodeRelation.delete", documentNodeRelationCriteria);
	}

}

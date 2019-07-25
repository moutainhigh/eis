package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.DocumentTypeCriteria;
import com.maicard.site.dao.DocumentTypeDao;
import com.maicard.site.domain.DocumentType;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class DocumentTypeDaoImpl extends BaseDao implements DocumentTypeDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;
;

	public int insert(DocumentType documentType) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("DocumentType.insert", documentType);

	}

	@CacheEvict(value = cacheName, key = "'DocumentType#' + #documentType.documentTypeId")
	public int update(DocumentType documentType) throws DataAccessException {


		return getSqlSessionTemplate().update("DocumentType.update", documentType);


	}
	@CacheEvict(value = cacheName, key = "'DocumentType#' + #documentType.documentTypeId")
	public int delete(int documentTypeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("DocumentType.delete", new Integer(documentTypeId));

	}

	@Cacheable(value = cacheName, key = "'DocumentType#' + #documentTypeId")
	public DocumentType select(int documentTypeId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库选择DocumentType[" + documentTypeId + "]");
		}
		return getSqlSessionTemplate().selectOne("DocumentType.select", new Integer(documentTypeId));
	}

	@Override
	public List<Integer> listPk(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException {
		Assert.notNull(documentTypeCriteria, "documentTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("DocumentType.listPk", documentTypeCriteria);
	}


	@Override
	public List<Integer> listPkOnPage(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException {
		Assert.notNull(documentTypeCriteria, "documentTypeCriteria must not be null");
		Assert.notNull(documentTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(documentTypeCriteria);
		Paging paging = documentTypeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DocumentType.listPk", documentTypeCriteria, rowBounds);
	}

	public List<DocumentType> list(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException {
		Assert.notNull(documentTypeCriteria, "documentTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("DocumentType.list", documentTypeCriteria);
	}


	public List<DocumentType> listOnPage(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException {
		Assert.notNull(documentTypeCriteria, "documentTypeCriteria must not be null");
		Assert.notNull(documentTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(documentTypeCriteria);
		Paging paging = documentTypeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DocumentType.list", documentTypeCriteria, rowBounds);
	}

	public int count(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException {
		Assert.notNull(documentTypeCriteria, "documentTypeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("DocumentType.count", documentTypeCriteria)).intValue();
	}

}

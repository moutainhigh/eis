package com.maicard.site.dao.ibatis;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.service.CacheService;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.dao.DocumentDataDao;
import com.maicard.site.domain.DocumentData;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class DocumentDataDaoImpl extends BaseDao implements DocumentDataDao {
	
	@Resource
	private CacheService cacheService;
	
	private final String cacheName = CommonStandard.cacheNameDocument;


	public int insert(DocumentData documentData) throws DataAccessException {
		int rs = getSqlSessionTemplate().insert("DocumentData.insert", documentData);
		if(rs == 1){
			cacheService.put(cacheName, "DocumentData#" + documentData.getDocumentDataId(), documentData);
		}
		return rs;
		
	}

	@CacheEvict(value = cacheName, key = "'DocumentData#' + #documentData.documentDataId")
	public int update(DocumentData documentData) throws DataAccessException {
		return getSqlSessionTemplate().update("DocumentData.update", documentData);
	}

	@CacheEvict(value = cacheName, key = "'DocumentData#' + #documentDataId")
	public int delete(int documentDataId) throws DataAccessException {
		return getSqlSessionTemplate().delete("DocumentData.delete", new Integer(documentDataId));
	}


	@Cacheable(value = cacheName, key = "'DocumentData#' + #documentDataId")
	public DocumentData select(int documentDataId) throws DataAccessException {
		return (DocumentData) getSqlSessionTemplate().selectOne("DocumentData.select", new Integer(documentDataId));
	}


	public List<DocumentData> list(DocumentDataCriteria documentDataCriteria) throws DataAccessException {
		Assert.notNull(documentDataCriteria, "documentDataCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("DocumentData.list", documentDataCriteria);
	}


	public List<DocumentData> listOnPage(DocumentDataCriteria documentDataCriteria) throws DataAccessException {
		Assert.notNull(documentDataCriteria, "documentDataCriteria must not be null");
		Assert.notNull(documentDataCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(documentDataCriteria);
		Paging paging = documentDataCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DocumentData.list", documentDataCriteria, rowBounds);
	}
	
	@Override
	public List<Integer> listPk(DocumentDataCriteria documentDataCriteria) throws DataAccessException {
		Assert.notNull(documentDataCriteria, "documentDataCriteria must not be null");		
		return getSqlSessionTemplate().selectList("DocumentData.listPk", documentDataCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(DocumentDataCriteria documentDataCriteria) throws DataAccessException {
		Assert.notNull(documentDataCriteria, "documentDataCriteria must not be null");
		Assert.notNull(documentDataCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(documentDataCriteria);
		Paging paging = documentDataCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DocumentData.listPk", documentDataCriteria, rowBounds);
	}

	public int count(DocumentDataCriteria documentDataCriteria) throws DataAccessException {
		Assert.notNull(documentDataCriteria, "documentDataCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("DocumentData.count", documentDataCriteria)).intValue();
	}
	
	public DocumentData matchByContent(DocumentDataCriteria documentDataCriteria) throws DataAccessException {
		Assert.notNull(documentDataCriteria.getContent(), "documentDataCriteria content must not be null");
		return (DocumentData) getSqlSessionTemplate().selectOne("DocumentData.matchByContent", documentDataCriteria.getContent());	
		
	}

	@Override
	public void delete(DocumentDataCriteria documentDataCriteria) {
		if(documentDataCriteria == null){
			throw new RequiredAttributeIsNullException("documentDataCriteria为空");
		}
		if(documentDataCriteria.getDataDefineId() == 0 && documentDataCriteria.getUdid() ==0){
			throw new RequiredAttributeIsNullException("documentDataCriteria至少要指定dataDefineId和udid中的一个");		
		}
		int rs = getSqlSessionTemplate().delete("DocumentData.deleteByCriteria", documentDataCriteria);
		logger.debug("按条件删除的documentData有[" + rs + "]个");

	}

}

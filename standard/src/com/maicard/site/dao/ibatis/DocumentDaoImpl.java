package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.dao.DocumentDao;
import com.maicard.site.domain.Document;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class DocumentDaoImpl extends BaseDao implements DocumentDao {

	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(Document document) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.site.sql.Document.insert", document);

	}

	@CacheEvict(value = cacheName, key = "'Document#' + #document.udid")
	public int update(Document document) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.site.sql.Document.update", document);

	}

	@CacheEvict(value = cacheName, key = "'Document#' + #udid")
	public int delete(int udid) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.site.sql.Document.delete", new Integer(udid));
	}


	
	public Document select(int udid) throws DataAccessException {
		return this.selectNoCache(udid);
	}
	
	@Override
	public Document selectNoCache(int udid) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.site.sql.Document.select", udid);
	}

	public List<Document> list(DocumentCriteria documentCriteria) throws DataAccessException {
		Assert.notNull(documentCriteria, "documentCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Document.list", documentCriteria);
	}


	public List<Integer> listPk(DocumentCriteria documentCriteria) throws DataAccessException {
		Assert.notNull(documentCriteria, "documentCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Document.listPk", documentCriteria);
	}

	public List<Document> listOnPage(DocumentCriteria documentCriteria) throws DataAccessException {
		Assert.notNull(documentCriteria, "documentCriteria must not be null");
		Assert.notNull(documentCriteria.getPaging(), "paging must not be null");

		int totalResults = count(documentCriteria);
		Paging paging = documentCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Document.list", documentCriteria, rowBounds);
	}

	public List<Integer> listPkOnPage(DocumentCriteria documentCriteria) throws DataAccessException {
		Assert.notNull(documentCriteria, "documentCriteria must not be null");
		Assert.notNull(documentCriteria.getPaging(), "paging must not be null");

		int totalResults = count(documentCriteria);
		Paging paging = documentCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.site.sql.Document.listPk", documentCriteria, rowBounds);
	}
	public int count(DocumentCriteria documentCriteria) throws DataAccessException {
		Assert.notNull(documentCriteria, "documentCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.site.sql.Document.count", documentCriteria)).intValue();
	}


	
}

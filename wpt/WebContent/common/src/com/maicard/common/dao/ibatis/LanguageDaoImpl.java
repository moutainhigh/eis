package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.dao.LanguageDao;
import com.maicard.common.domain.Language;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class LanguageDaoImpl extends BaseDao implements LanguageDao {

	public int insert(Language language) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("Language.insert", language)).intValue();
	}
	
	public  void updateWrong(Language language) throws DataAccessException {
		getSqlSessionTemplate().insert("Language.insertwrong", language);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Language#' + #language.languageId")
	public int update(Language language) throws DataAccessException {
		return getSqlSessionTemplate().update("Language.update", language);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Language#' + #languageId")
	public int delete(int languageId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Language.delete", languageId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Language#' + #languageId")
	public Language select(int languageId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("Language.select",languageId);
	}
	
	public List<Integer> listPk(LanguageCriteria languageCriteria) throws DataAccessException {
		Assert.notNull(languageCriteria, "languageCriteria must not be null");		
		return getSqlSessionTemplate().selectList("Language.listPk", languageCriteria);
	}

	public List<Integer> listPkOnPage(LanguageCriteria languageCriteria) throws DataAccessException {
		Assert.notNull(languageCriteria, "languageCriteria must not be null");
		Assert.notNull(languageCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(languageCriteria);
		Paging paging = languageCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Language.listPk", languageCriteria, rowBounds);
	}

	public List<Language> list(LanguageCriteria languageCriteria) throws DataAccessException {
		Assert.notNull(languageCriteria, "languageCriteria must not be null");		
		return getSqlSessionTemplate().selectList("Language.list", languageCriteria);
	}

	public List<Language> listOnPage(LanguageCriteria languageCriteria) throws DataAccessException {
		Assert.notNull(languageCriteria, "languageCriteria must not be null");
		Assert.notNull(languageCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(languageCriteria);
		Paging paging = languageCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Language.list", languageCriteria, rowBounds);
	}

	public int count(LanguageCriteria languageCriteria) throws DataAccessException {
		Assert.notNull(languageCriteria, "languageCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("Language.count", languageCriteria)).intValue();
	}

}

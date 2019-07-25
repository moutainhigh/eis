package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.Language;

public interface LanguageDao {

	int insert(Language language) throws DataAccessException;
	void updateWrong(Language language) throws DataAccessException;

	int update(Language language) throws DataAccessException;

	int delete(int languageId) throws DataAccessException;

	Language select(int languageId) throws DataAccessException;

	List<Language> list(LanguageCriteria languageCriteria) throws DataAccessException;
	
	List<Language> listOnPage(LanguageCriteria languageCriteria) throws DataAccessException;
	
	int count(LanguageCriteria languageCriteria) throws DataAccessException;
	List<Integer> listPk(LanguageCriteria languageCriteria);
	List<Integer> listPkOnPage(LanguageCriteria languageCriteria);

}

package com.maicard.ec.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.domain.AddressBook;

public interface AddressBookDao {

	int insert(AddressBook addressBook) throws DataAccessException;

	int update(AddressBook addressBook) throws DataAccessException;

	AddressBook select(String addressBookId) throws DataAccessException;

	List<AddressBook> list(AddressBookCriteria addressBookCriteria) throws DataAccessException;
	
	List<AddressBook> listOnPage(AddressBookCriteria addressBookCriteria) throws DataAccessException;
	
	int count(AddressBookCriteria addressBookCriteria) throws DataAccessException;
	
	int delete(AddressBook addressBook);

	void deleteByCriteria(AddressBookCriteria addressBookCriteria);

	int setDefaultAdd(AddressBook addressBook) throws DataAccessException;
}

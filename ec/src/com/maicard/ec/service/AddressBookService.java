package com.maicard.ec.service;

import java.util.List;

import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.domain.AddressBook;

public interface AddressBookService {

	int insert(AddressBook addressBook);

	int update(AddressBook addressBook);

	int delete(AddressBook addressBook);
		
	AddressBook select(String addressBookId);
	
	List<AddressBook> list(AddressBookCriteria addressBookCriteria);

	List<AddressBook> listOnPage(AddressBookCriteria addressBookCriteria);

	int count(AddressBookCriteria addressBookCriteria);
	
	int setDefaultAdd(AddressBook addressBook); 
}

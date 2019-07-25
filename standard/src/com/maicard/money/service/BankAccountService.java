package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.domain.BankAccount;


public interface BankAccountService {

	int insert(BankAccount bankAccount);

	int update(BankAccount bankAccount);

	int delete(int bankAccountId);
		
	BankAccount select(int bankAccountId);
	
	List<BankAccount> list(BankAccountCriteria bankAccountCriteria);

	List<BankAccount> listOnPage(BankAccountCriteria bankAccountCriteria);

	int count(BankAccountCriteria bankAccountCriteria);
	
	int setDefaultAdd(BankAccount bankAccount);

	int createIfNotExist(BankAccount bankAccount);

}

package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.domain.BankAccount;


public interface BankAccountDao {

	int insert(BankAccount bankAccount) throws DataAccessException;

	int update(BankAccount bankAccount) throws DataAccessException;

	BankAccount select(int bankAccountId) throws DataAccessException;

	List<BankAccount> list(BankAccountCriteria bankAccountCriteria) throws DataAccessException;
	
	List<BankAccount> listOnPage(BankAccountCriteria bankAccountCriteria) throws DataAccessException;
	
	int count(BankAccountCriteria bankAccountCriteria) throws DataAccessException;
	
	int delete(int id);

	void deleteByCriteria(BankAccountCriteria bankAccountCriteria);

	int setDefaultAdd(BankAccount bankAccount) throws DataAccessException;
}

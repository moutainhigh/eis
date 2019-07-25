package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.dao.BankAccountDao;
import com.maicard.money.domain.BankAccount;

@Repository
public class BankAccountDaoImpl extends BaseDao implements BankAccountDao {

	public int insert(BankAccount bankAccount) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.money.sql.BankAccount.insert", bankAccount);
	}

	public int update(BankAccount bankAccount) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.money.sql.BankAccount.update", bankAccount);


	}

	public int delete(int id) throws DataAccessException {


		return getSqlSessionTemplate().delete("com.maicard.money.sql.BankAccount.delete", new Integer(id));


	}

	public BankAccount select(int id) throws DataAccessException {
		return (BankAccount) getSqlSessionTemplate().selectOne("com.maicard.money.sql.BankAccount.select", new Integer(id));
	}


	public List<BankAccount> list(BankAccountCriteria bankAccountCriteria) throws DataAccessException {
		Assert.notNull(bankAccountCriteria, "bankAccountCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.money.sql.BankAccount.list", bankAccountCriteria);
	}


	public List<BankAccount> listOnPage(BankAccountCriteria bankAccountCriteria) throws DataAccessException {
		Assert.notNull(bankAccountCriteria, "bankAccountCriteria must not be null");
		Assert.notNull(bankAccountCriteria.getPaging(), "paging must not be null");

		int totalResults = count(bankAccountCriteria);
		Paging paging = bankAccountCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.money.sql.BankAccount.list", bankAccountCriteria, rowBounds);
	}

	public int count(BankAccountCriteria bankAccountCriteria) throws DataAccessException {
		Assert.notNull(bankAccountCriteria, "bankAccountCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.BankAccount.count", bankAccountCriteria)).intValue();
	}

	@Override
	public void deleteByCriteria(BankAccountCriteria bankAccountCriteria) {
		// TODO Auto-generated method stub

	}
	@Override
	public int setDefaultAdd(BankAccount bankAccount) throws DataAccessException{
		try
		{
			getSqlSessionTemplate().update("com.maicard.money.sql.BankAccount.setNormalAdd","");
			getSqlSessionTemplate().update("com.maicard.money.sql.BankAccount.setDefaultAdd", bankAccount.getBankAccountId());
		}
		catch (Exception e)
		{
			return 0;
		}
		return 1;
	}
}

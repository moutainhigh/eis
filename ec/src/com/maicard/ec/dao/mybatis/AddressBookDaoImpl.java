package com.maicard.ec.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.dao.AddressBookDao;
import com.maicard.ec.domain.AddressBook;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.standard.BasicStatus;

@Repository
public class AddressBookDaoImpl extends BaseDao implements AddressBookDao {

	public int insert(AddressBook addressBook) throws DataAccessException {
		if (addressBook.getCurrentStatus()== BasicStatus.relation.getId()){
			getSqlSessionTemplate().update("AddressBook.updateAllToNormal",addressBook);
		}
		return getSqlSessionTemplate().insert("AddressBook.insert", addressBook);
	}

	public int update(AddressBook addressBook) throws DataAccessException {

		if (addressBook.getCurrentStatus()== BasicStatus.relation.getId()){
			//把这个用户的所有记录更新为普通非默认
			getSqlSessionTemplate().update("AddressBook.updateAllToNormal",addressBook);
		}
		return getSqlSessionTemplate().update("AddressBook.update", addressBook);


	}

	public int delete(AddressBook addressBook) throws DataAccessException {


		return getSqlSessionTemplate().delete("AddressBook.delete", addressBook);


	}

	public AddressBook select(String id) throws DataAccessException {
		return (AddressBook) getSqlSessionTemplate().selectOne("AddressBook.select", id);
	}


	public List<AddressBook> list(AddressBookCriteria addressBookCriteria) throws DataAccessException {
		Assert.notNull(addressBookCriteria, "addressBookCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("AddressBook.list", addressBookCriteria);
	}


	public List<AddressBook> listOnPage(AddressBookCriteria addressBookCriteria) throws DataAccessException {
		Assert.notNull(addressBookCriteria, "addressBookCriteria must not be null");
		Assert.notNull(addressBookCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(addressBookCriteria);
		Paging paging = addressBookCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("AddressBook.list", addressBookCriteria, rowBounds);
	}

	public int count(AddressBookCriteria addressBookCriteria) throws DataAccessException {
		Assert.notNull(addressBookCriteria, "addressBookCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("AddressBook.count", addressBookCriteria)).intValue();
	}

	@Override
	public void deleteByCriteria(AddressBookCriteria addressBookCriteria) {
		// TODO Auto-generated method stub
		
	}
   @Override
   public int setDefaultAdd(AddressBook addressBook) throws DataAccessException{
		try
		{
	    getSqlSessionTemplate().update("AddressBook.setNormalAdd","");
	    getSqlSessionTemplate().update("AddressBook.setDefaultAdd", addressBook.getAddressBookId());
		}
		catch (Exception e)
		{
			return 0;
		}
	    return 1;
   }
}

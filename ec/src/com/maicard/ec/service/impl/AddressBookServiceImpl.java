package com.maicard.ec.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.dao.AddressBookDao;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.service.AddressBookService;
import com.maicard.standard.CommonStandard;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Uuid;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.UuidService;


@Service
public class AddressBookServiceImpl extends BaseService implements AddressBookService {

	@Resource
	private AddressBookDao addressBookDao;

	@Resource
	private ConfigService configService;
	@Resource
	private UuidService uuidService;

	@Resource
	private DataDefineService dataDefineService;

	
	public int insert(AddressBook addressBook) {
		if(addressBook.getAddressBookId() <= 0){
			long uuid = uuidService.insert(new Uuid());
			if(uuid <= 0){
				logger.error("无法创建本地UUID，因此无法建立AddressBook主键");
				return -1;
			}
			long newUuid = Long.parseLong(configService.getServerId() + "" + uuid);
			addressBook.setAddressBookId(newUuid);

		}
		if(addressBook.getCreateTime() == null){
			addressBook.setCreateTime(new Date());
		}
		return addressBookDao.insert(addressBook);

	}

	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'AddressBook#' + #addressBook.addressBookId")
	public int update(AddressBook addressBook) {
		int actualRowsAffected = 0;
		actualRowsAffected = addressBookDao.update(addressBook);

		return actualRowsAffected;
	}



	public List<AddressBook> list(AddressBookCriteria addressBookCriteria) {
		List<AddressBook> addressBookList = addressBookDao.list(addressBookCriteria);
		if(addressBookList != null){
			for(int i = 0; i < addressBookList.size(); i++){
				addressBookList.get(i).setIndex(i+1);
				afterFetch(addressBookList.get(i));
			}
		}
		return addressBookList;
	}

	public List<AddressBook> listOnPage(AddressBookCriteria addressBookCriteria) {
		List<AddressBook> addressBookList = addressBookDao.listOnPage(addressBookCriteria);
		if(addressBookList != null){
			for(int i = 0; i < addressBookList.size(); i++){
				addressBookList.get(i).setIndex(i+1);
				afterFetch(addressBookList.get(i));
			}
		}
		return addressBookList;	
	}

	@Override
	public int count(AddressBookCriteria addressBookCriteria) {
		return addressBookDao.count(addressBookCriteria);
	}

	private void afterFetch(AddressBook addressBook){


	}

	@Override
	@Cacheable(value = CommonStandard.cacheNameProduct, key = "'AddressBook#' + #addressBookId")
	public AddressBook select(String addressBookId) {
		AddressBook addressBook =  addressBookDao.select(addressBookId);	
		if(addressBook != null){
			afterFetch(addressBook);
		}
		return addressBook;
	}

	@Override
	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'AddressBook#' + #addressBook.addressBookId")
	public int delete(AddressBook addressBook) {
		int actualRowsAffected = 0;
		actualRowsAffected = addressBookDao.delete(addressBook);
		return actualRowsAffected;
	}

	@Override
	public int setDefaultAdd(AddressBook addressBook)
	{
		int actualRowsAffected = addressBookDao.setDefaultAdd(addressBook);
		return actualRowsAffected;
	}
}

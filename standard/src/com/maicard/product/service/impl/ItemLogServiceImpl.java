package com.maicard.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.dao.ItemLogDao;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ItemLogService;

@Service
public class ItemLogServiceImpl extends BaseService implements ItemLogService{

	@Resource
	private ItemLogDao itemLogDao;
	
	@Resource
	private ApplicationContextService applicationContextService;
	
	private final String dbSourceBeanName = "dataSource";
	
	@Override
	//@Async
	public void insert(Item item) {
		itemLogDao.insert(item);
	}


	@Override
	public List<Item> list(ItemCriteria itemCriteria) {
		DataSource dataSource =  null;
		try{
			dataSource = (DataSource)applicationContextService.getBean(dbSourceBeanName);
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(dataSource != null){
			if(dataSource.getDriverClassName().startsWith("org.sqlite")){
				itemCriteria.setDbType("sqlite");
			}
		}
		try{
			return itemLogDao.list(itemCriteria);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Item> listOnPage(ItemCriteria itemCriteria) {
		return itemLogDao.listOnPage(itemCriteria);

	}

	@Override
	public Item select(String transactionId) {
		return itemLogDao.select(transactionId);

	}


	@Override
	public int count(ItemCriteria itemCriteria) {
		
		return itemLogDao.count(itemCriteria);
	}
	
	@Override
	public int delete(ItemCriteria itemCriteria) {		
		return itemLogDao.delete(itemCriteria);
	}

	@Override
	public List<Item> listBadVps() {		
		return itemLogDao.listBadVps();
	}

	@Override
	public int getChannelSuccessMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

}

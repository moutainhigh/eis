package com.maicard.money.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.dao.PayTypeDao;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.PayTypeService;
import com.maicard.standard.CommonStandard;

@Service
public class PayTypeServiceImpl extends BaseService implements PayTypeService {

	@Resource
	private PayTypeDao payTypeDao;

	public int insert(PayType payType) {
		try{
			return payTypeDao.insert(payType);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'PayType#' + #payType.payTypeId")
	public int update(PayType payType) {
		try{
			return  payTypeDao.update(payType);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'PayType#' + #payTypeId")
	public int delete(int payTypeId) {
		try{
			return  payTypeDao.delete(payTypeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'PayType#' + #payTypeId")
	public PayType select(int payTypeId) {
		return payTypeDao.select(payTypeId);
	}

	public List<PayType> list(PayTypeCriteria payTypeCriteria) {
		List<PayType> payTypeList = payTypeDao.list(payTypeCriteria);
		if(payTypeList == null){
			return Collections.emptyList();
		} else {
			return payTypeList;
		}
	}

	public List<PayType> listOnPage(PayTypeCriteria payTypeCriteria) {
		return payTypeDao.listOnPage(payTypeCriteria);
	}

	public int count(PayTypeCriteria payTypeCriteria) {
		return payTypeDao.count(payTypeCriteria);
	}

}

package com.maicard.money.service.impl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.CacheService;
import com.maicard.common.util.ClassUtils;
import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.dao.PayMethodDao;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.service.PayMethodService;
import com.maicard.standard.CommonStandard;

@Service
public class PayMethodServiceImpl extends BaseService implements PayMethodService {

	@Resource
	private PayMethodDao payMethodDao;
	
	@Resource
	private CacheService cacheService;
	
	//缓存支付通道的主键
	private static Set<Integer> pkSet = new HashSet<Integer>();


	@Override
	public int insert(PayMethod payMethod) {
		int rs = payMethodDao.insert(payMethod);
		
		return rs;
	}

	@Override
	public int update(PayMethod payMethod) {
		int actualRowsAffected = 0;
		
		int payMethodId = payMethod.getPayMethodId();

		PayMethod _oldPayMethod = payMethodDao.select(payMethodId);
		
		if (_oldPayMethod != null) {
			actualRowsAffected = payMethodDao.update(payMethod);
			if(actualRowsAffected > 0 && payMethod.getSyncFlag() == 0) {
				//由于使用redis cache，因此只有第一次更新才需要更新缓存
				String cacheKey = "PayMethod#" + payMethod.getPayMethodId();
				//cacheService.delete(new CacheCriteria(CommonStandard.cacheNameProduct, cacheKey));
				cacheService.put(CommonStandard.cacheNameProduct,  cacheKey , payMethod);
			}
		}
		
		return actualRowsAffected;
	}

	@Override
	public int delete(int payMethodId) {
		int actualRowsAffected = 0;
		
		PayMethod _oldPayMethod = payMethodDao.select(payMethodId);
		
		if (_oldPayMethod != null) {
			actualRowsAffected = payMethodDao.delete(payMethodId);
		}
		
		return actualRowsAffected;
	}
	
	@Override
	public PayMethod select(int payMethodId) {
		return payMethodDao.select(payMethodId);
	}
	
	private void initPk() {
		List<Integer> pkList = payMethodDao.listPkOnPage(new PayMethodCriteria());
		if(pkList == null || pkList.size() < 1) {
			logger.warn("系统中没有任何支付通道");
			return;
		}
		pkSet.clear();
		for(Integer pk : pkList) {
			pkSet.add(pk);
		}
		logger.info("初始化{}个支付通道主键", pkSet.size());
	}

	@Override
	public List<PayMethod> list(PayMethodCriteria payMethodCriteria){
		payMethodCriteria.setPaging(null);
		if(pkSet.size() < 1) {
			initPk();
		}
		List<PayMethod> list = new ArrayList<PayMethod>();
		//从缓存中获取所有支付方式
		for(Integer pk : pkSet) {
			PayMethod payMethod = payMethodDao.select(pk);
			if(payMethod != null) {
				list.add(payMethod);
			}
		}
		List<PayMethod> filteredList = null;
		try {
			filteredList = ClassUtils.search(list, payMethodCriteria);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
		if(filteredList == null) {
			return Collections.emptyList();
		}
		return filteredList;
		/*
		List<Integer> pkList = payMethodDao.listPkOnPage(payMethodCriteria);
		if(pkList == null || pkList.size() < 1) {
			return Collections.emptyList();
		}
		for(Integer pk : pkList) {
			PayMethod payMethod = payMethodDao.select(pk);
			if(payMethod != null) {
				list.add(payMethod);
			}
		}
		return list;*/
		
	}
	
	@Override
	public List<PayMethod> listOnPage(PayMethodCriteria payMethodCriteria) {
		List<PayMethod> list = new ArrayList<PayMethod>();
		List<Integer> pkList = payMethodDao.listPkOnPage(payMethodCriteria);
		if(pkList == null || pkList.size() < 1) {
			return Collections.emptyList();
		}
		for(Integer pk : pkList) {
			PayMethod payMethod = payMethodDao.select(pk);
			if(payMethod != null) {
				list.add(payMethod);
			}
		}
		return list;
		
	}

	/**
	 * 获取payMethod id 为key 的 map
	 * @param payMethodCriteria
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map<Integer, PayMethod> list4IdKeyMap(PayMethodCriteria payMethodCriteria) {
		Map<Integer, PayMethod> payMethodMap = new HashMap<Integer, PayMethod>();
		List<PayMethod> payMethodList = list(payMethodCriteria);
		if (payMethodList == null) {
			return payMethodMap;
		}
		for (PayMethod payMethod : payMethodList) {
			payMethodMap.put(payMethod.getPayMethodId(), payMethod);
		}
		return payMethodMap;
	}

	@Override
	public int count(PayMethodCriteria payMethodCriteria) {
		return payMethodDao.count(payMethodCriteria);
	}

	
}

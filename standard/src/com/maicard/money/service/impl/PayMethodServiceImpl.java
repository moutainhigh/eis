package com.maicard.money.service.impl;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
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
	private CenterDataService centerDataService;


	private static final String CACHE_TABLE = "PAY_METHOD";

	@Override
	public int insert(PayMethod payMethod) {
		int rs = payMethodDao.insert(payMethod);
		if(payMethod.getPayMethodId() > 0) {
			try {
				centerDataService.setHmPlainValue(CACHE_TABLE + "_" + payMethod.getOwnerId(), "PayMethod#" + payMethod.getPayMethodId(), JSON.toJSONString(payMethod), (int)CommonStandard.CACHE_MAX_TTL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return rs;
	}

	@Override
	public int update(PayMethod payMethod) {
		int actualRowsAffected = 0;

		int payMethodId = payMethod.getPayMethodId();

		PayMethod _oldPayMethod = payMethodDao.select(payMethodId);

		if (_oldPayMethod != null) {
			actualRowsAffected = payMethodDao.update(payMethod);
			boolean syncCache = actualRowsAffected > 0 && payMethod.getSyncFlag() == 0;
			logger.info("更新支付通道:{},version={},结果:{}，是否需要刷新缓存:{}", payMethod.getPayMethodId(), payMethod.getVersion(), actualRowsAffected, syncCache);
			if(syncCache) {
				//由于使用redis cache，因此只有第一次更新才需要更新缓存
				try {
					centerDataService.setHmPlainValue(CACHE_TABLE + "_" + payMethod.getOwnerId(), "PayMethod#" + payMethod.getPayMethodId(), JSON.toJSONString(payMethod), (int)CommonStandard.CACHE_MAX_TTL);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		try {
			centerDataService.setHmPlainValue(CACHE_TABLE + "_" + _oldPayMethod.getOwnerId(), "PayMethod#" + _oldPayMethod.getPayMethodId(), null, (int)CommonStandard.CACHE_MAX_TTL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualRowsAffected;
	}

	@Override
	public PayMethod select(int payMethodId) {
		return payMethodDao.select(payMethodId);
	}

	private PayMethod select(int payMethodId, long ownerId) {
		final String tableName = CACHE_TABLE + "_" + ownerId;
		String text = null;
		try {
			text = centerDataService.getHmPlainValue(tableName, "PayMethod#" + payMethodId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(text == null) {
			logger.error("未能从缓存:{}中获取到支付通道:{}", tableName, payMethodId);
			return payMethodDao.select(payMethodId);
		}
		PayMethod payMethod = null;
		try {
			payMethod = JsonUtils.getInstance().readValue(text, PayMethod.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(payMethod == null) {
			logger.error("未能将缓存数据:{}转换为支付通道:{}", text, payMethodId);
			return payMethodDao.select(payMethodId);
		} else {
			return payMethod;
		}



	}


	@Override
	public List<PayMethod> list(PayMethodCriteria payMethodCriteria){

		Set<Object> keys = centerDataService.getHmKeys(CACHE_TABLE + "_" + payMethodCriteria.getOwnerId());
		if(keys == null || keys.size() < 1) {
			initCache(payMethodCriteria.getOwnerId());
			keys = centerDataService.getHmKeys(CACHE_TABLE + "_" + payMethodCriteria.getOwnerId());
		}
		List<PayMethod> list = new ArrayList<PayMethod>();
		//从缓存中获取所有支付方式
		for(Object pk : keys) {
			String pkStr = pk.toString().replaceAll("^PayMethod#", "");
			logger.debug("准备从缓存查找支付通道:{}={}", pk.toString(), pkStr);
			int payMethodId = NumericUtils.parseInt(pkStr);
			PayMethod payMethod = select(payMethodId, payMethodCriteria.getOwnerId());
			if(payMethod != null) {
				list.add(payMethod);
			} else {
				logger.error("未能获取支付通道:{}", payMethodId);
			}
		}
		if(list.size() < 1) {
			logger.error("从系统中获取到的支付通道数量是0");
			return Collections.emptyList();
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

	private void initCache(long ownerId) {
		PayMethodCriteria critera = new PayMethodCriteria(ownerId);
		Paging paging = new Paging(99999999);
		paging.setCurrentPage(1);
		critera.setPaging(paging);
		List<Integer> pkList = payMethodDao.listPkOnPage(critera);
		if(pkList == null || pkList.size() < 1) {
			logger.warn("系统中没有任何ownerId={}的支付通道", ownerId);
			return;
		}
		final String tableName = CACHE_TABLE + "_" + ownerId;
		try {
			for(int pk : pkList) {
				PayMethod payMethod = payMethodDao.select(pk);
				if(payMethod == null) {
					logger.error("从系统中找不到支付通道:{}", pk);
					continue;
				}	
				centerDataService.setHmPlainValue(tableName, "PayMethod#" + pk, JSON.toJSONString(payMethod), (int)CommonStandard.CACHE_MAX_TTL);
			}
			logger.info("初始化{}个支付通道到缓存", pkList.size());

		}catch(Exception e) {
			e.printStackTrace();
		}
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
			} else {
				logger.error("找不到指定的支付方式:{}", pk);
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

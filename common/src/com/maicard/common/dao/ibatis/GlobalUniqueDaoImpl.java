package com.maicard.common.dao.ibatis;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.GlobalUniqueCriteria;
import com.maicard.common.dao.GlobalUniqueDao;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.service.CacheService;
import com.maicard.standard.CommonStandard;

import static com.maicard.common.criteria.GlobalUniqueCriteria.CACHE_PREFIX;
@Repository
public class GlobalUniqueDaoImpl extends BaseDao implements GlobalUniqueDao {

	@Resource
	private CacheService cacheService;

	private final String cacheName = CommonStandard.cacheNameValidate;



	@Override
	public boolean exist(GlobalUnique globalUnique) throws Exception {
		if(globalUnique == null ||StringUtils.isBlank(globalUnique.getData())){
			return false;
		}
		GlobalUnique existGlobalUnique = null;
		String key = globalUnique.getData() + "#" + globalUnique.getOwnerId();
		try{
			existGlobalUnique = (GlobalUnique)((ValueWrapper)cacheService.get(cacheName, CACHE_PREFIX + "#" + key)).get();		
		}catch(Exception e){}
		if(existGlobalUnique != null){
			logger.debug("从缓存中找到了全局唯一数据[" + existGlobalUnique + "]");
			return true;
		}
		if(!globalUnique.isNeedSave()){
			return false;
		}
		logger.debug("从缓存中未找到全局唯一数据[" + globalUnique + "]，尝试从数据库中查找...");
		int rs = getSqlSessionTemplate().selectOne("GlobalUnique.count", globalUnique);
		if (rs == 0){
			logger.debug("从数据库中未找到全局唯一数据[" + globalUnique.getData() + "]");
			return false;
		}
		cacheService.put(cacheName, CACHE_PREFIX + "#" + key,	globalUnique);
		return true;
	}

	@Override
	public boolean create(GlobalUnique globalUnique) throws Exception {
		
		if(globalUnique == null || StringUtils.isBlank(globalUnique.getData())){
			return false;
		}
		String key = globalUnique.getData() + "#" + globalUnique.getOwnerId();

		if(globalUnique.isNeedSave()){
			try{
				if(getSqlSessionTemplate().insert("GlobalUnique.insert", globalUnique) == 1){
					logger.debug("成功创建全局唯一数据[" + globalUnique + "]");
				}
				return true;
			}catch(Exception e){
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
		} else {
			logger.debug("向全局唯一数据缓存[" + cacheName + "]中放入数据: " + CACHE_PREFIX + "#" + key + ":" + globalUnique);
			cacheService.put(cacheName, CACHE_PREFIX + "#" + key,	globalUnique);
			return true;
		}
		return false;
	}

	@Override
	public List<GlobalUnique> list(GlobalUniqueCriteria globalUniqueCriteria) throws Exception {
		return getSqlSessionTemplate().selectList("GlobalUnique.list", globalUniqueCriteria);
	}

	@Override
	public int insertIgnore(GlobalUnique globalUnique) {
		return getSqlSessionTemplate().insert("GlobalUnique.insertIgnore", globalUnique);
	}

	@Override
	public int delete(GlobalUnique globalUnique) {
		return getSqlSessionTemplate().insert("GlobalUnique.delete", globalUnique);

	}


}

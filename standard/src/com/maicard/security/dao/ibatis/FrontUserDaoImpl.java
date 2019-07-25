package com.maicard.security.dao.ibatis;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.dao.FrontUserDao;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.SecurityStandard.UserStatus;

@Repository
public class FrontUserDaoImpl extends BaseDao implements FrontUserDao {

	@Resource
	private CacheService cacheService;
	@Resource
	private ConfigService configService;

	private final String cacheName = CommonStandard.cacheNameUser;

	public int insert(User frontUser) throws DataAccessException {
		if(frontUser.getUuid() < 1){
			
		} else {
			if(logger.isDebugEnabled()){
				logger.debug("新注册用户已有UUID[" + frontUser.getUuid() + "]");
			}
		}

		return getSqlSessionTemplate().insert("com.maicard.security.sql.FrontUser.insert", frontUser);
	}

	@CacheEvict(value=cacheName, key = "'FrontUser#' + #uuid")
	public int update(User frontUser) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.security.sql.FrontUser.update", frontUser);
	}
	
	@Override
	@CacheEvict(value=cacheName, key = "'FrontUser#' + #uuid")
	public int updateNoNull(User frontUser) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.security.sql.FrontUser.updateNoNull", frontUser);
	}

	@CacheEvict(value=cacheName, key = "'FrontUser#' + #uuid")
	public int delete(long fuuid) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.security.sql.FrontUser.delete", fuuid);
	}

	public User select(long uuid) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库查找用户[" + uuid + "].");
		}
		return  getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.select", uuid);
	}

	public List<User> list(UserCriteria frontUserCriteria) throws DataAccessException {
		Assert.notNull(frontUserCriteria, "frontUserCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.security.sql.FrontUser.list", frontUserCriteria);
	}
	
	public List<User> listByPrepayment(long uuid) throws DataAccessException {
		return getSqlSessionTemplate().selectList("FrontUser.listByPrepayment",uuid);
	}	
	public List<User> listOnPage(UserCriteria frontUserCriteria) throws DataAccessException {
		Assert.notNull(frontUserCriteria, "frontUserCriteria must not be null");
		Assert.notNull(frontUserCriteria.getPaging(), "paging must not be null");

		int totalResults = count(frontUserCriteria);
		Paging paging = frontUserCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.FrontUser.list", frontUserCriteria, rowBounds);
	}


	@Override
	public int updateDynamicData(UserDynamicData userDynamicData){
		return getSqlSessionTemplate().update("com.maicard.security.sql.FrontUser.updateDynamicData", userDynamicData);
	}

	public int count(UserCriteria frontUserCriteria) throws DataAccessException {
		Assert.notNull(frontUserCriteria, "frontUserCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.count", frontUserCriteria)).intValue();
	}

	@Override
	public List<User> listOnPageByPartner(UserCriteria frontUserCriteria) {
		Assert.notNull(frontUserCriteria, "frontUserCriteria must not be null");
		Assert.notNull(frontUserCriteria.getPaging(), "paging must not be null");

		int totalResults = count(frontUserCriteria);
		Paging paging = frontUserCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.FrontUser.list", frontUserCriteria, rowBounds);
	}
	@Override
	public String search_Inviter(long uuid){
		Assert.notNull(uuid, "frontUserCriteria must not be null");
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.searchinviter",uuid);
	}

	@Override
	public List<Long> listPk(UserCriteria frontUserCriteria)
			throws DataAccessException {
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.FrontUser.listPk", frontUserCriteria);
	}

	@Override
	public List<Long> listPkOnPage(UserCriteria frontUserCriteria)
			throws DataAccessException {
		int totalResults = count(frontUserCriteria);
		Paging paging = frontUserCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.FrontUser.listPk", frontUserCriteria, rowBounds);

	}

	@Override
	public User lockUser(UserCriteria userCriteria) {
		int rs = getSqlSessionTemplate().update("com.maicard.security.sql.FrontUser.lockUser", userCriteria);
		if(rs < 1){
			logger.warn("根据锁定条件[" + userCriteria.getLockGlobalUniqueId() + "]无法锁定任何记录");
			return null;
		}
		if(rs > 1){
			logger.error("根据锁定条件[" + userCriteria.getLockGlobalUniqueId() + "]锁定的记录多于1个:" + rs);
			return null;
		}
		userCriteria.setCurrentStatus(UserStatus.locked.getId());
		userCriteria.setParentUuid(0);
		try{
			User frontUser =  getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.list", userCriteria);
			if(frontUser != null){
				cacheService.delete(new CacheCriteria(cacheName, "FrontUser#" + frontUser.getUuid() ));
			}
			return frontUser;
		}catch(Exception e){
			logger.error("在返回被锁定用户时发生异常:" + e.getMessage());
		}
		return null;
	}

	@Override
	public int changeUuid(User frontUser) {
		int rs = getSqlSessionTemplate().update("com.maicard.security.sql.FrontUser.changeUuid", frontUser);
		return rs;
	}
	@Override
	public String downloadNewAccountCsv(int num) {
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.downloadNewAccountCsv",
				num);
	}
	@Override
	public String downloadBalanceCsv(int num) {
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.downloadBalanceCsv",
				num);
	}
	@Override
	public
	String  makeCollection(String custdata){
	  logger.info("收到的参数custdata的值是"+custdata+"!!!!!!!!!!!!!!!!!!!!!!");
	  return getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.makeCollection",custdata);
	}

	@Override
	public long getMaxId() {
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.FrontUser.getMaxId");
	}
}

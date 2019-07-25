package com.maicard.security.dao.ibatis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;


import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.PartnerDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.PartnerPrivilegeService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.security.service.UserDataService;
import com.maicard.security.service.UserDynamicDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 
 * 2014.1.29 启用缓存，并把扩展数据的加载放在DAO层
 *
 * @author NetSnake
 * @date 2014-1-29 
 */
@Repository
public class PartnerDaoImpl extends BaseDao implements PartnerDao {

	private final String cacheName = CommonStandard.cacheNameUser;

	@Resource
	private UserDataService userDataService;
	@Resource
	private UserDynamicDataService userDynamicDataService;
	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;
	@Resource
	private PartnerRoleService partnerRoleService;
	@Resource
	private PartnerPrivilegeService partnerPrivilegeService;



	@Override
	public int insert(User partner) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("com.maicard.security.sql.Partner.insert", partner)).intValue();
	}

	@Override
	@CacheEvict(value=cacheName, key = "'Partner#' + #partner.uuid")
	public int update(User partner) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.security.sql.Partner.update", partner);
	}

	@Override
	@CacheEvict(value=cacheName, key = "'Partner#' + #partner.uuid")
	public int updateNoNull(User partner) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.security.sql.Partner.updateNoNull", partner);
	}


	@Override
	@CacheEvict(value=cacheName, key = "'Partner#' + #uuid")
	public int delete(long uuid) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.security.sql.Partner.delete", uuid);
	}

	@Override
	@Cacheable(value=cacheName, key = "'Partner#' + #uuid")
	public User select(long uuid) throws DataAccessException {
		User user = getSqlSessionTemplate().selectOne("com.maicard.security.sql.Partner.select", uuid);
		if(user != null){
			afterFetch(user);
		}
		return user;
	}


	@Override
	public List<User> list(UserCriteria partnerCriteria) throws DataAccessException {
		Assert.notNull(partnerCriteria, "PartnerCriteria must not be null");	

		List<User> userList = getSqlSessionTemplate().selectList("com.maicard.security.sql.Partner.list", partnerCriteria);
		return userList;
	}

	@Override
	public List<Long> listPk(UserCriteria partnerCriteria) throws DataAccessException {
		Assert.notNull(partnerCriteria, "PartnerCriteria must not be null");	

		List<Long> userList = getSqlSessionTemplate().selectList("com.maicard.security.sql.Partner.listPk", partnerCriteria);
		return userList;
	}



	@Override
	public List<User> listOnPage(UserCriteria partnerCriteria) throws DataAccessException {
		Assert.notNull(partnerCriteria, "PartnerCriteria must not be null");
		Assert.notNull(partnerCriteria.getPaging(), "paging must not be null");

		int totalResults = count(partnerCriteria);
		Paging paging = partnerCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.Partner.list", partnerCriteria, rowBounds);
	}

	@Override
	public List<Long> listPkOnPage(UserCriteria partnerCriteria) throws DataAccessException {
		Assert.notNull(partnerCriteria, "PartnerCriteria must not be null");
		Assert.notNull(partnerCriteria.getPaging(), "paging must not be null");

		int totalResults = count(partnerCriteria);
		Paging paging = partnerCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.Partner.listPk", partnerCriteria, rowBounds);
	}

	public int count(UserCriteria partnerCriteria) throws DataAccessException {
		Assert.notNull(partnerCriteria, "PartnerCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.security.sql.Partner.count", partnerCriteria)).intValue();
	}

	@Override
	public int updateDynamicData(UserDynamicData userDynamicData){
		return getSqlSessionTemplate().update("com.maicard.security.sql.Partner.updateDynamicData", userDynamicData);
	}

	private void afterFetch(User partner){
		if(partner == null){
			return;
		}
		if(partner.getNickName() == null || partner.getNickName().equals("")){
			partner.setNickName(partner.getUsername());
		}
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(partner.getUuid());
		userDataCriteria.setUserTypeId(UserTypes.partner.getId());
		try{
			partner.setUserConfigMap(userDataService.map(userDataCriteria));
		}catch(Exception e){
			e.printStackTrace();
		}		
		List<Privilege> privilegeList = new ArrayList<Privilege>();
		UserRoleRelationCriteria userRoleRelationCriteria = new UserRoleRelationCriteria();
		userRoleRelationCriteria.setUuid(partner.getUuid());
		List<UserRoleRelation> sysRoleUserRelationList = partnerRoleRelationService.list(userRoleRelationCriteria);
		logger.info("合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]拥有的关联角色数量是" + (sysRoleUserRelationList == null ? "空" : sysRoleUserRelationList.size()));
		if(sysRoleUserRelationList == null || sysRoleUserRelationList.size() < 1){
			partner.setRelatedPrivilegeList(Collections.<Privilege>emptyList());
			userDynamicDataService.applyToUser(partner);
			return;
		}

		ArrayList<Role> relatedRoleList = new ArrayList<Role>();
		int[] roleIds = new int[sysRoleUserRelationList.size()];
		for(int i = 0; i < sysRoleUserRelationList.size(); i++){
			logger.debug("查找合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]对应角色[" + sysRoleUserRelationList.get(i).getRoleId() + "]");
			roleIds[i] = sysRoleUserRelationList.get(i).getRoleId();
			Role partnerRole = partnerRoleService.select(sysRoleUserRelationList.get(i).getRoleId());
			if(partnerRole != null){
				logger.debug("找到了合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]对应角色[" + partnerRole.getRoleId() + "/" + partnerRole.getRoleName() + "]");
				relatedRoleList.add(partnerRole);
			}
		}
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria();
		privilegeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		privilegeCriteria.setRoleIds(roleIds);
		privilegeList = partnerPrivilegeService.listByRole(privilegeCriteria);

		partner.setRelatedRoleList(relatedRoleList);
		//权限去重
		//int before = privilegeList.size();
		HashSet<Privilege> hs = new HashSet<Privilege>(privilegeList);
		privilegeList.clear();
		privilegeList.addAll(hs);
		partner.setRelatedPrivilegeList(privilegeList);
		userDynamicDataService.applyToUser(partner);
	}

	@Override
	public String listProgeny(long rootUuid) {
		String progeny = getSqlSessionTemplate().selectOne("com.maicard.security.sql.Partner.listProgeny", rootUuid);
		if(progeny == null){
			return progeny;
		}
		return progeny.replaceFirst(",", "");
	}
}

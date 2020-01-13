package com.maicard.security.service;

import java.util.List;

import com.maicard.common.base.InviterSupportCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;

public interface PartnerService {

	int insert(User partner);

	int update(User partner);

	int delete(long uuid);
	
	User select(long uuid);

	List<User> list(UserCriteria partnerCriteria);

	List<User> listOnPage(UserCriteria partnerCriteria);
	
	int count(UserCriteria partnerCriteria);

	//列出指定partner的所有子账户
	void listAllChildren(List<User> all, long  fatherId);
	
	boolean isValidSubUser(long parentUuid, long childUuid);

	int insertLocal(User partner);

	int deleteLocal(User partner);

	int updateLocal(User partner);

	//列出指定账户的子孙账户
	List<User> listProgeny(long rootUuid, long ownerId);
	
	/**
	 * 检查用户是否已经获取了更多动态数据，如partner的所有子账户
	 * 这些数据大部分时候并不会使用，只在某些时候会用到
	 * 因此不需要在取出User对象时就设置
	 * @param partner
	 */	
	void applyMoreDynmicData(User partner);

	void correctUserAttributes(User frontUser);

	int updateNoNull(User user);

	void setSubPartner(InviterSupportCriteria criteria, User partner) throws Exception;

	/**
	 * 获取一个指定parnter的祖先uuid
	 */
	long getHeadUuid(User partner);

	long getHeadUuid(long inviter);

	void evictCache(User partner);

}

package com.maicard.wpt.service.me;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.common.util.StringTools;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.UserRelationService;
import com.maicard.wpt.service.VipService;

public class VipServiceImpl extends BaseService implements VipService {



	@Resource
	protected UserRelationService userRelationService;

	@Override
	public Date getExpireTime(User user) {
		UserRelation ur = this.getVipRelation(user);
		if(ur == null){
			return null;
		}
		Calendar cld = Calendar.getInstance();  

		String relationType = ur.getRelationType();
		if("VIP_MONTH".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
			cld.add(Calendar.MONTH, 1);  
			
		}else if("VIP_QUARTER".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
			cld.add(Calendar.MONTH, 3);  
			
		}else if("VIP_YEAR".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
			cld.add(Calendar.YEAR, 1);  
			
		} else {
			logger.error("未知的VIP订购类型:{}", ur.getRelationType());
			return null;
		}
		return cld.getTime();
	}

	@Override
	public UserRelation getVipRelation(User user) {
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setOwnerId(user.getOwnerId());
		userRelationCriteria.setUuid(user.getUuid());
		userRelationCriteria.setObjectType("VIP");
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList.size()<1){
			logger.debug("未找到用户"+user.getUsername()+"的VIP会员关联数据");
			return null;
		}

		Calendar cld = Calendar.getInstance();  
		Date d2 = null;
		int lastIndex = -1;



		for(int i = 0; i < userRelationList.size(); i++){
			UserRelation ur = userRelationList.get(i);
			String relationType = ur.getRelationType();
			if("VIP_MONTH".equals(relationType)){
				cld.setTime(ur.getCreateTime());  
				cld.add(Calendar.MONTH, 1);  
				if(d2 == null || d2.before(cld.getTime())){
					d2 = cld.getTime();
					lastIndex = i;
				} 
			}else if("VIP_QUARTER".equals(relationType)){
				cld.setTime(ur.getCreateTime());  
				cld.add(Calendar.MONTH, 3);  
				if(d2 == null || d2.before(cld.getTime())){
					d2 = cld.getTime();
					lastIndex = i;
				} 
			}else if("VIP_YEAR".equals(relationType)){
				cld.setTime(ur.getCreateTime());  
				cld.add(Calendar.YEAR, 1);  
				if(d2 == null || d2.before(cld.getTime())){
					d2 = cld.getTime();
					lastIndex = i;
				} 
			}
		}
		logger.debug("最长有效期的VIP订购关系是第{}个订购关系，订购时间是:{}", lastIndex, StringTools.getFormattedTime(d2));

		if(lastIndex == -1){
			return null;
		}
		UserRelation userRleation = userRelationList.get(lastIndex);

		return userRleation;
	}

}

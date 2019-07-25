package com.maicard.stat.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.stat.criteria.FrontUserStatCriteria;
import com.maicard.stat.dao.FrontUserStatDao;
import com.maicard.stat.domain.FrontUserStat;
import com.maicard.stat.service.FrontUserStatService;

@Service
public class FrontUserStatServiceImpl extends BaseService implements FrontUserStatService {
	@Resource
	private FrontUserStatDao frontUserStatDao;

	@Resource
	private PartnerService partnerService;

	@Override
	public List<FrontUserStat> listOnPage(FrontUserStatCriteria frontUserStatCriteria) {
		if(frontUserStatCriteria == null){
			return null;
		}
		/*if(!frontUserStatCriteria.isSuperMode() && frontUserStatCriteria.getPartnerUuid() < 1){
			return null;
		}
		*/
		return frontUserStatDao.listOnPage(frontUserStatCriteria);
	}
	
	private void generateChildCondition(FrontUserStatCriteria frontUserStatCriteria){
		List<User> childrenList = new ArrayList<User>();
		ArrayList<Long> inviteUuid = new ArrayList<Long>();
		ArrayList<Long> inviteUuid2 = new ArrayList<Long>();
		String inviteUuids = "";
		String otherCondition;

		//非系统用户或超级用户
		if(!frontUserStatCriteria.isSuperMode()){
			//找出该partner所拥有的所有子账户ID
			partnerService.listAllChildren(childrenList, frontUserStatCriteria.getPartnerUuid());
			for(int i = 0; i < childrenList.size(); i++){
				inviteUuid.add(childrenList.get(i).getUuid());
			}
			//也包含自己发展的用户
			inviteUuid.add(frontUserStatCriteria.getPartnerUuid());
			//如果条件中指定了子账户，那么检查这些指定的账户是否与合法子账户一致
			if(frontUserStatCriteria.getUuidRange() != null &&  !frontUserStatCriteria.getUuidRange().equals("")){

				String[] specialPartnerIds = frontUserStatCriteria.getUuidRange().split(",");
				if(specialPartnerIds != null && specialPartnerIds.length > 0){
					for(int i = 0; i < specialPartnerIds.length; i++){
						for(int j = 0; j < inviteUuid.size(); j++){
							try{
								if(Integer.parseInt(specialPartnerIds[i]) == inviteUuid.get(j)){
									inviteUuid2.add(inviteUuid.get(j));
									break;
								}
							}catch(Exception e){}
						}
					}

				}
				logger.info("当前查询条件指定了[" + inviteUuid2.size() + "]个子账户[" + frontUserStatCriteria.getUuidRange() + "]");
			}
			//如果指定了子账户，则只查询（合法）的子账户，否则，查询当前partner及其所拥有的所有子账户
			if(inviteUuid2.size() > 0){
				for(int i = 0; i < inviteUuid2.size(); i++){
					inviteUuids += inviteUuid2.get(i) + ",";
				}
			} else {
				for(int i = 0; i < inviteUuid.size(); i++){
					inviteUuids += inviteUuid.get(i) + ",";
				}
			}
		} else {
			//如果条件中指定了子账户，那么在超级模式下，只查询指定的子账户及其子子账户
			if(frontUserStatCriteria.getUuidRange() != null &&  !frontUserStatCriteria.getUuidRange().equals("")){
				String[] specialPartnerIds = frontUserStatCriteria.getUuidRange().split(",");
				if(specialPartnerIds != null && specialPartnerIds.length > 0){
					for(int i = 0; i < specialPartnerIds.length; i++){
						try{
							inviteUuid2.add(Long.parseLong(specialPartnerIds[i]));
						}catch(Exception e){}
					}

				}
				logger.info("当前查询条件指定了[" + inviteUuid2.size() + "]个子账户[" + frontUserStatCriteria.getUuidRange() + "]");

				//为每个账户添加其子账户
				for(int  i = 0; i < inviteUuid2.size(); i++){
					partnerService.listAllChildren(childrenList, inviteUuid2.get(i));
					childrenList.add(partnerService.select(inviteUuid2.get(i)));
				}
				logger.info("当前查询条件指定了[" + inviteUuid2.size() + "]个子账户，所有子账户数量为[" + childrenList.size() + "][" + frontUserStatCriteria.getUuidRange() + "]");
				for(int i = 0; i < childrenList.size(); i++){
					inviteUuids += childrenList.get(i).getUuid() + ",";

				}
			}
		}
		
		inviteUuids = inviteUuids.replaceAll(",$", "");
		otherCondition = " invite_by_uuid in (" + inviteUuids + ")";
		
		if(!frontUserStatCriteria.isSuperMode()){
			logger.info("当前partner[uuid=" + frontUserStatCriteria.getPartnerUuid() + "]共有 " + childrenList.size() + " 个子账户,查询条件为" + otherCondition );	
			frontUserStatCriteria.setUuidRange(otherCondition);
		}else {
			if(inviteUuid2 != null && inviteUuid2.size() > 0){
				frontUserStatCriteria.setUuidRange(otherCondition);
				logger.info("当前为系统用户，但指定了[" + inviteUuid2.size() + "]个查询子账户:" + otherCondition);
			} else {
				logger.info("当前为系统用户，且未指定查询子账户，不考虑当前合作伙伴ID");
			}
		}
		childrenList = null;
		inviteUuid = null;
		inviteUuid2 = null;
		inviteUuids = null;
	}
	
	@Override
	public int count(FrontUserStatCriteria frontUserStatCriteria)
	{
		//查询开始时间
		if(frontUserStatCriteria.getQueryBeginTime() == null){
			//如果没设置起始时间，设置为一周前
			frontUserStatCriteria.setQueryBeginTime(DateUtils.truncate(DateUtils.addDays(new Date(), -7),Calendar.DAY_OF_MONTH));
		}
		return frontUserStatDao.count(frontUserStatCriteria);    	
	}

}

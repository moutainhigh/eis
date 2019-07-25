package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.IpPolicyCriteria;
import com.maicard.common.dao.IpPolicyDao;
import com.maicard.common.domain.IpPolicy;
import com.maicard.common.service.IpPolicyService;
import com.maicard.standard.BasicStatus;

@Service
public class IpPolicyServiceImpl extends BaseService implements IpPolicyService{

	@Resource
	private IpPolicyDao ipPolicyDao;

	
	public int insert(IpPolicy ipPolicy) {
		try{
			return ipPolicyDao.insert(ipPolicy);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
		
	}


	public int update(IpPolicy ipPolicy) {
		try{
			return  ipPolicyDao.update(ipPolicy);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int ipPolicyId) {
		try{
			return  ipPolicyDao.delete(ipPolicyId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public IpPolicy select(int ipPolicyId) {
		IpPolicy ipPolicy =  ipPolicyDao.select(ipPolicyId);
		//ipPolicy.setCurrentStatusName(BasicStatus.disable.findById(ipPolicy.getCurrentStatus()).getName());	
		return ipPolicy;
	}

	public List<IpPolicy> list(IpPolicyCriteria ipPolicyCriteria) {
		List<Integer> idList = ipPolicyDao.listPk(ipPolicyCriteria);
		if(idList != null && idList.size() > 0){
			List<IpPolicy> ipPolicyList =  new ArrayList<IpPolicy> ();		
			for(int i = 0; i < idList.size(); i++){
				IpPolicy ipPolicy = ipPolicyDao.select(idList.get(i));
				if(ipPolicy != null){
					ipPolicy.setId(ipPolicy.getIpPolicyId());
					ipPolicy.setIndex(i+1);
					//ipPolicy.setCurrentStatusName(BasicStatus.unknown.findById(ipPolicy.getCurrentStatus()).getName());	
					ipPolicyList.add(ipPolicy);
				}
			}
			idList = null;
			return ipPolicyList;
		}
		return null;
		/*
		List<IpPolicy> ipPolicyList =  ipPolicyDao.list(ipPolicyCriteria);
		if(ipPolicyList == null){
			return null;
		}
		for(int i = 0; i < ipPolicyList.size(); i++){
			ipPolicyList.get(i).setId(ipPolicyList.get(i).getIpPolicyId());
			ipPolicyList.get(i).setIndex(i+1);
			ipPolicyList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(ipPolicyList.get(i).getCurrentStatus()).getName());	
		}
		return ipPolicyList;
		*/
	}
	
	public List<IpPolicy> listOnPage(IpPolicyCriteria ipPolicyCriteria) {
		List<Integer> idList = ipPolicyDao.listPkOnPage(ipPolicyCriteria);
		if(idList != null && idList.size() > 0){
			List<IpPolicy> dictList =  new ArrayList<IpPolicy> ();		
			for(int i = 0; i < idList.size(); i++){
				IpPolicy ipPolicy = ipPolicyDao.select(idList.get(i));
				if(ipPolicy != null){
					ipPolicy.setId(ipPolicy.getIpPolicyId());
					ipPolicy.setIndex(i+1);
				//	ipPolicy.setCurrentStatusName(BasicStatus.unknown.findById(ipPolicy.getCurrentStatus()).getName());	
					dictList.add(ipPolicy);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<IpPolicy> ipPolicyList =  ipPolicyDao.listOnPage(ipPolicyCriteria);
		if(ipPolicyList == null){
			return null;
		}
		for(int i = 0; i < ipPolicyList.size(); i++){
			ipPolicyList.get(i).setId(ipPolicyList.get(i).getIpPolicyId());
			ipPolicyList.get(i).setIndex(i+1);
			ipPolicyList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(ipPolicyList.get(i).getCurrentStatus()).getName());	
		}
		return ipPolicyList;
		*/
	}
	
	public int count(IpPolicyCriteria ipPolicyCriteria){
		return ipPolicyDao.count(ipPolicyCriteria);
	}
	
	@Override
	public boolean isForbidden(IpPolicyCriteria ipPolicyCriteria){
		if(StringUtils.isBlank(ipPolicyCriteria.getIpAddress())){
			return false;
		}
		IpPolicyCriteria ipPolicyCriteria2 = new IpPolicyCriteria();
		ipPolicyCriteria2.setCurrentStatus(BasicStatus.normal.getId());
		List<IpPolicy> allIpPolicyList = list(ipPolicyCriteria2);
		if(allIpPolicyList == null || allIpPolicyList.size() < 1){
			return false;
		}
		for(IpPolicy ipPolicy : allIpPolicyList){
			if(ipPolicy.getIpPolicyType() == IpPolicy.WHITE_LIST){//白名单
				if(ipPolicyCriteria.getIpAddress().matches(ipPolicy.getIpPolicyReg().trim())){
					if(logger.isDebugEnabled()){
						logger.debug("IP地址[" + ipPolicyCriteria.getIpAddress() + "]与白名单规则[" + ipPolicy.getIpPolicyReg() + "]匹配，直接返回false未封禁");
					}
					ipPolicyCriteria2 = null;
					allIpPolicyList = null;
					return false;
				}				
			}
		}
		for(IpPolicy ipPolicy : allIpPolicyList){
			if(ipPolicy.getIpPolicyType() == IpPolicy.BLACK_LIST){//黑名单
				if(ipPolicyCriteria.getIpAddress().matches(ipPolicy.getIpPolicyReg().trim())){
					if(logger.isDebugEnabled()){
						logger.debug("IP地址[" + ipPolicyCriteria.getIpAddress() + "]与黑名单规则[" + ipPolicy.getIpPolicyReg() + "]匹配，直接返回true已封禁");
					}
					ipPolicyCriteria2 = null;
					allIpPolicyList = null;
					return true;
				}				
			}
		}
		
		return false;
	}

}

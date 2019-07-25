package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.IpLocationCriteria;
import com.maicard.common.dao.IpLocationDao;
import com.maicard.common.domain.IpLocation;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.IpLocationService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;

@Service
public class IpLocationServiceImpl extends BaseService implements IpLocationService{

	@Resource
	private IpLocationDao ipLocationDao;

	@Resource
	private ConfigService configService;


	private static Map<String, IpQueryProviderInfo> providerCache = new HashMap<String, IpQueryProviderInfo>();

	public int insert(IpLocation ipLocation) {
		try{
			return ipLocationDao.insert(ipLocation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;

	}


	public int update(IpLocation ipLocation) {
		try{
			return  ipLocationDao.update(ipLocation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	public int delete(int ipLocationId) {
		try{
			return  ipLocationDao.delete(ipLocationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	public IpLocation select(int ipLocationId) {
		IpLocation ipLocation =  ipLocationDao.select(ipLocationId);
		return ipLocation;
	}

	@Override
	public IpLocation query(String ip) {
		if(ip == null){
			return null;
		}
		
		IpLocationCriteria ipLocationCriteria = new IpLocationCriteria();

		String[] ipRange = ip.split("\\.");
		
		if(ipRange.length == 4){
			ipLocationCriteria.setIp(ipRange[0] + "." + ipRange[1] + "." + ipRange[2]);
		} else {
			ipLocationCriteria.setIp(ip);
		}
		List<IpLocation> ipLocationList = list(ipLocationCriteria);
		if(ipLocationList == null || ipLocationList.size() < 1){
			logger.debug("从系统中找不到IP=" + ipLocationCriteria.getIp() + "]对应的信息");
			IpQueryProviderInfo provider = this.getProvider(ipLocationCriteria.getOwnerId());
			if(provider == null){
				logger.warn("系统未配置IP查询供应商，无法查询IP信息");
				return null;
			}
			String url = provider.queryUrl + "?ip=" + ip + "&token=" + provider.key;
			String queryResult = HttpUtils.sendData(url);
			logger.info("从地址[" + url + "]查询IP归属返回:" + queryResult);
			ObjectMapper om = JsonUtils.getInstance();
			JsonNode node = null;
			try{
				node = om.readTree(queryResult.trim());
			}catch(Exception e){
				logger.error("在查询IP地址时发生异常:" + e.getMessage());
				e.printStackTrace();
			}
			if(node == null){
				logger.error("无法查询IP[" + ip + "]的归属");
				return null;
			}
			if(!node.path("data").has(5)){
				logger.error("无法解析查询IP[" + ip + "]的归属信息:" + queryResult);
				return null;
			}
			IpLocation ipLocation = new IpLocation();
			ipLocation.setIpRange(ipLocationCriteria.getIp());
			ipLocation.setCountry(node.path("data").get(0).asText());
			ipLocation.setProvince(node.path("data").get(1).asText());
			ipLocation.setCity(node.path("data").get(2).asText());
			ipLocation.setIsp(node.path("data").get(3).asText());
			ipLocation.setPostCode(node.path("data").get(4).asText());
			ipLocation.setAreaNumber(node.path("data").get(5).asText());
			this.insert(ipLocation);
			return ipLocation;


		} else {
			return ipLocationList.get(0);
		}
	}

	public List<IpLocation> list(IpLocationCriteria ipLocationCriteria) {
		List<Integer> idList = ipLocationDao.listPk(ipLocationCriteria);
		if(idList != null && idList.size() > 0){
			List<IpLocation> ipLocationList =  new ArrayList<IpLocation> ();		
			for(int i = 0; i < idList.size(); i++){
				IpLocation ipLocation = ipLocationDao.select(idList.get(i));
				if(ipLocation != null){
					ipLocationList.add(ipLocation);
				}
			}
			idList = null;
			return ipLocationList;
		}
		return null;
		/*
		List<IpLocation> ipLocationList =  ipLocationDao.list(ipLocationCriteria);
		if(ipLocationList == null){
			return null;
		}
		for(int i = 0; i < ipLocationList.size(); i++){
			ipLocationList.get(i).setId(ipLocationList.get(i).getIpLocationId());
			ipLocationList.get(i).setIndex(i+1);
			ipLocationList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(ipLocationList.get(i).getCurrentStatus()).getName());	
		}
		return ipLocationList;
		 */
	}

	public List<IpLocation> listOnPage(IpLocationCriteria ipLocationCriteria) {
		List<Integer> idList = ipLocationDao.listPkOnPage(ipLocationCriteria);
		if(idList != null && idList.size() > 0){
			List<IpLocation> dictList =  new ArrayList<IpLocation> ();		
			for(int i = 0; i < idList.size(); i++){
				IpLocation ipLocation = ipLocationDao.select(idList.get(i));
				if(ipLocation != null){
					dictList.add(ipLocation);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<IpLocation> ipLocationList =  ipLocationDao.listOnPage(ipLocationCriteria);
		if(ipLocationList == null){
			return null;
		}
		for(int i = 0; i < ipLocationList.size(); i++){
			ipLocationList.get(i).setId(ipLocationList.get(i).getIpLocationId());
			ipLocationList.get(i).setIndex(i+1);
			ipLocationList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(ipLocationList.get(i).getCurrentStatus()).getName());	
		}
		return ipLocationList;
		 */
	}

	public int count(IpLocationCriteria ipLocationCriteria){
		return ipLocationDao.count(ipLocationCriteria);
	}

	private IpQueryProviderInfo getProvider(long ownerId){
		IpQueryProviderInfo provider = null;
		if(providerCache != null && providerCache.size() > 0){
			provider = providerCache.get(String.valueOf(ownerId));
		}
		if(provider != null){
			logger.debug("从缓存中获取到ownerId=" + ownerId + "的IP查询信息:" + provider);
			return provider;
		}
		provider = new IpQueryProviderInfo();
		String key = configService.getValue("ip138QueryKey", ownerId);
		if(key != null){
			provider.key = key;
		} else {
			logger.error("找不到ownerId=" + ownerId + "的IP查询信息ip138QueryKey");
			return null;
		}

		String queryUrl = configService.getValue("ip138QueryUrl", ownerId);
		if(queryUrl != null){
			provider.queryUrl = queryUrl;
		} else {
			logger.error("找不到ownerId=" + ownerId + "的IP查询queryUrl");
			return null;
		}

		synchronized(this){
			providerCache.put(String.valueOf(ownerId), provider);
		}
		logger.debug("把ownerId=" + ownerId + "的IP查询信息:" + provider + "放入缓存");
		return provider;
	}


}

class IpQueryProviderInfo {

	public String accountId;
	public String key;
	public String queryUrl;

	@Override
	public String toString(){
		return new StringBuffer().append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(').append("accountId=").append("'").append(accountId).append("',").append("key='").append(key).append("',queryUrl='").append(queryUrl).append("')").toString();
	}
}

package com.maicard.flow.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.flow.criteria.RouteCriteria;
import com.maicard.flow.dao.RouteDao;
import com.maicard.flow.domain.Route;
import com.maicard.flow.service.RouteService;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

@Service
public class RouteServiceImpl extends BaseService implements RouteService {

	@Resource
	private RouteDao routeDao;

	@Override
	public void insert(Route route) {
		routeDao.insert(route);
	}

	@Override
	public int update(Route route) {
		int actualRowsAffected = 0;

		int routeId = route.getRouteId();

		Route _oldRoute = routeDao.select(routeId);

		if (_oldRoute != null) {
			actualRowsAffected = routeDao.update(route);
		}

		return actualRowsAffected;
	}

	@Override
	public int delete(int routeId) {
		int actualRowsAffected = 0;

		Route _oldRoute = routeDao.select(routeId);

		if (_oldRoute != null) {
			actualRowsAffected = routeDao.delete(routeId);
		}

		return actualRowsAffected;
	}

	@Override
	public Route select(int routeId) {
		Route route = routeDao.select(routeId);
		afterFetch(route);
		return route;
	}

	@Override
	public List<Route> list(RouteCriteria routeCriteria) {
		List<Route> routeList =  routeDao.list(routeCriteria);
		if(routeList != null && routeList.size() > 0){
			for(int i = 0 ; i  < routeList.size(); i++){
				afterFetch(routeList.get(i));
				routeList.get(i).setIndex(i+1);
			}
		}
		return routeList;
	}

	@Override
	public List<Route> listOnPage(RouteCriteria routeCriteria) {
		List<Route> routeList =  routeDao.listOnPage(routeCriteria);
		if(routeList != null && routeList.size() > 0){
			for(int i = 0 ; i  < routeList.size(); i++){
				afterFetch(routeList.get(i));
				routeList.get(i).setIndex(i+1);
			}
		}
		return routeList;	
	}

	@Override
	public int count(RouteCriteria routeCriteria) {
		return routeDao.count(routeCriteria);
	}

	private void afterFetch(Route route){

		route.setId(route.getRouteId());
	}

	//检查指定用户是否具备操作指定工作步骤的权限
	@Override
	public boolean havePrivilege(User user, long objectId, Route route){
		if(user == null){
			return false;
		}
		if(user.getUuid() < 1){
			return false;
		}
		if(route == null){
			return false;
		}

		if(user.getRelatedPrivilegeList() == null || user.getRelatedPrivilegeList().size() < 1){
			return false;
		}
		logger.info("用户[" + user.getUuid() + "]关联权限有" + user.getRelatedPrivilegeList().size() + "条.");
		for(Privilege privilege : user.getRelatedPrivilegeList()){
			if(privilege == null){
				continue;
			}
			logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]");
			if(privilege.getObjectTypeCode() == null || privilege.getObjectTypeCode().equals("")){
				logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作对象代码为空，跳过...");
				continue;
			}
			//权限与权限条件的对象类型代码一致
			if(privilege.getObjectTypeCode() == null || route.getTargetObjectType() == null || !privilege.getObjectTypeCode().equals(route.getTargetObjectType())){
				continue;
			}
			//logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]...");
			if(privilege.getOperateCode() == null){
				//logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作码为空");
				continue;
			}					
			if(privilege.getOperateCode().equals("*")
					||(privilege.getOperateCode().equals("r") && (route.getTargetObjectOperateCode().equals("list") || route.getTargetObjectOperateCode().equals("get")))
					||(privilege.getOperateCode().equals("w") && (route.getTargetObjectOperateCode().equals("create") || route.getTargetObjectOperateCode().equals("delete") || route.getTargetObjectOperateCode().equals("update")))
					||privilege.getOperateCode().equals(route.getTargetObjectOperateCode())
					){
				//logger.info("比对权限[" + privilege.getOperateCode() + "]与权限条件[" + route.getTargetObjectOperateCode() + "]...");
				//如果权限objectList为空，跳过这次比对
				if(privilege.getObjectList() == null || privilege.getObjectList().equals("")){
					//	logger.info("权限[" + privilege.getOperateCode() + "]的对象列表为空...");
					continue;
				}

				String objects[] = privilege.getObjectList().split(",");
				//如果无法获取或解析匹配对象列表，跳过这次比对
				if(objects == null || objects.length == 0){
					//	logger.info("权限[" + privilege.getOperateCode() + "]的对象列表无法解析:" + privilege.getObjectList());
					continue;
				}
				//如果匹配对象的第一个是*，表示匹配所有
				if(objects[0].equals("*")){
					//	logger.info("权限[" + privilege.getOperateCode() + "]的对象列表第一个为*，查看是否需要进行属性权限匹配...");
					//判断属性权限，如果权限的属性模式为空，那么直接返回成功
					if(privilege.getObjectAttributePattern() == null || privilege.getObjectAttributePattern().equals("") || privilege.getObjectAttributePattern().equals("*")){
						logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的对象为*，且无属性匹配，直接返回true");
						return true;
					}
					try{
						String[] attributes = privilege.getObjectAttributePattern().split(",");
						if(attributes == null || attributes.length < 1){
							logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的对象为*，但无法解析属性匹配，返回false");
							return false;
						}
						for(String attribute : attributes){
							String[] datas = attribute.split("=");
							if(datas == null || datas.length < 1){
								logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的对象为*，但属性匹配无法用=分割，返回false");
								return false;
							}
							for(String key : route.getTargetObjectAttributeMap().keySet()){
								if(datas[0].equals(key) && datas[1].equals(route.getTargetObjectAttributeMap().get(key))){
									logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的对象为*，属性[" + key + "=" + datas[1] + "]匹配一致，返回true");
									return true;
								}
							}
						}

					}catch(Exception e){
						logger.error("在解析权限的属性匹配时出错:" + e.getMessage());
						return false;
					}
					return false;
				}
				logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的对象为列表，逐个检查");
				for(String oid : objects){
					try{
						//logger.info("比对权限[" + privilege.getOperateCode() + "]的对象列表ID[" + oid + "]");
						if(Integer.parseInt(oid) == objectId){
							//判断属性权限，如果权限的属性模式为空，那么直接返回成功
							if(privilege.getObjectAttributePattern() == null || privilege.getObjectAttributePattern().equals("") || privilege.getObjectAttributePattern().equals("*")){
								return true;
							}
							try{
								String[] attributes = privilege.getObjectAttributePattern().split(",");
								if(attributes == null || attributes.length < 1){
									return false;
								}
								for(String attribute : attributes){
									String[] datas = attribute.split("=");
									if(datas == null || datas.length < 1){
										return false;
									}
									for(String key : route.getTargetObjectAttributeMap().keySet()){
										if(datas[0].equals(key) && datas[1].equals(route.getTargetObjectAttributeMap().get(key))){
											return true;
										}
									}
								}

							}catch(Exception e){
								logger.error("在解析权限的属性匹配时出错:" + e.getMessage());
								return false;
							}
						}
					}catch(Exception e){}
				}
			}

		}
		return false;

	}

}

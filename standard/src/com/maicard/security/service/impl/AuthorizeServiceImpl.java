package com.maicard.security.service.impl;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.EisObject;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerPrivilegeService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.util.Uri2PrivilegeCriteria;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.Operate;
import com.maicard.standard.SecurityStandard.UserTypes;

@Service
public class AuthorizeServiceImpl extends BaseService implements AuthorizeService{
	@Resource 
	CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private PartnerService partnerService;

	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;

	@Resource
	private PartnerPrivilegeService partnerPrivilegeService;

	@Override
	public String listOperateCode(User user, String objectTypeCode) {
		UserRoleRelationCriteria userRoleRelationCriteria = new UserRoleRelationCriteria();
		userRoleRelationCriteria.setUuid(user.getUuid());
		userRoleRelationCriteria.setOwnerId(user.getOwnerId());
		List<UserRoleRelation> userList = partnerRoleRelationService.list(userRoleRelationCriteria);
		if(CollectionUtils.isEmpty(userList)){
			return "";
		}
		int roleId = userList.get(0).getRoleId();
		//获取权限列表
		int[] roleIds = new int[]{roleId};
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria();
		privilegeCriteria.setOwnerId(user.getOwnerId());
		privilegeCriteria.setRoleIds(roleIds);
		privilegeCriteria.setObjectTypeCode(objectTypeCode);
		List<Privilege> list = partnerPrivilegeService.listByRole(privilegeCriteria);
		if(CollectionUtils.isEmpty(list)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(Privilege privilege : list){
			if(privilege.getOperateCode().equals("*")) {
				//权限为*，直接返回所有权限
				return "*";
			}
			sb.append(privilege.getOperateCode()+",");
		}
		sb = sb.delete(sb.length()-1,sb.length());
		logger.info("权限列表"+sb.toString());
		return sb.toString();
	}


	//检查指定HTTP请求的合法性
	@Override
	public boolean havePrivilege(HttpServletRequest request, HttpServletResponse response, int userTypeId) {
		User user = certifyService.getLoginedUser(request, response, userTypeId);
		logger.debug("检查用户[" + user + "]是否对Url有访问权限:" +  request.getRequestURI());
		if(user == null){
			return false;
		}
		//分析URL，以确定访问权限

		PrivilegeCriteria privilegeCriteria = Uri2PrivilegeCriteria.convert(request);
		if(privilegeCriteria == null){
			logger.error("无法解析请求URI:" + request.getRequestURI());
			return false;
		}
		privilegeCriteria.setOwnerId(user.getOwnerId());
		privilegeCriteria.setUuid(user.getUuid());
		privilegeCriteria.setUserTypeId(user.getUserTypeId());

		return havePrivilege(user, privilegeCriteria);
	}


	@Override
	public boolean havePrivilege(User user, PrivilegeCriteria privilegeCriteria){
		if(user == null){
			user = initCheck(privilegeCriteria);
		}
		//	logger.info("user"+user.getUsername()+"进入啦！！！！！");
		if(user == null){
			logger.info("user对象为空！！！！！");
			return false;
		}

		for(Privilege privilege : user.getRelatedPrivilegeList()){
			logger.debug("比对权限[" + privilege + "]");
			if(privilege == null){
				continue;
			}
			if(privilege.getOwnerId() != privilegeCriteria.getOwnerId()){
				logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的平台ID与条件中的不一致，跳过...");
			}
			if(privilege.getObjectTypeCode() == null || privilege.getObjectTypeCode().equals("")){
				logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作对象代码为空，跳过...");
				continue;
			}
			//权限与权限条件的对象类型代码一致
			if(privilege.getObjectTypeCode().equals(privilegeCriteria.getObjectTypeCode())){
				logger.debug("比对相同的操作对象:" + privilegeCriteria.getObjectTypeCode() + "的权限[" + privilege.getPrivilegeId() + "/" + privilege.getOperateCode() + "]");
				/*
				 * 权限的操作代码是*
				 * 或者
				 * 权限的操作代码是r，而且权限条件的操作代码是list或get
				 * 或者
				 * 权限的操作代码是w，而且权限条件的操作代码是delete、update或create
				 * 或者
				 * 权限与权限条件的操作代码完全一致
				 */
				//logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]...");
				if(privilege.getOperateCode() == null){
					logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作码为空");
					continue;
				}					
				if(privilege.getOperateCode().equals("*")
						||(privilege.getOperateCode().equals("r") && (privilegeCriteria.getOperateCode().equals("list") || privilegeCriteria.getOperateCode().equals("get")))
						||(privilege.getOperateCode().equals("w") && (privilegeCriteria.getOperateCode().equals("create") || privilegeCriteria.getOperateCode().equals("delete") || privilegeCriteria.getOperateCode().equals("update")))
						||privilege.getOperateCode().equals(privilegeCriteria.getOperateCode())
						){

					logger.debug("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]与权限条件[" + privilegeCriteria.getOperateCode() + "]...");
					//如果权限objectList为空，跳过这次比对
					if(privilege.getObjectList() == null || privilege.getObjectList().equals("")){
						logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象列表为空...");
						continue;
					}
					//如果要权限条件要比对的对象ID为空，跳过这次比对
					/*if(privilegeCriteria.getObjectId() == 0){
							logger.info("权限[" + privilege.getOperateCode() + "]的对象ID为0...");
							continue;
						}*/
					String objects[] = privilege.getObjectList().split(",");
					//如果无法获取或解析匹配对象列表，跳过这次比对
					if(objects == null || objects.length == 0){
						logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode()  + "]的对象列表无法解析:" + privilege.getObjectList());
						continue;
					}
					//如果匹配对象的第一个是*，表示匹配所有
					if(objects[0].equals("*")){
						//logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象列表第一个为*，查看是否需要进行属性权限匹配...");
						//判断属性权限，如果权限的属性模式为空，那么直接返回成功
						if(privilege.getObjectAttributePattern() == null || privilege.getObjectAttributePattern().equals("") || privilege.getObjectAttributePattern().equals("*")){
							logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的属性匹配规则*，直接为真");
							return true;
						}
						//如果权限条件中未指定属性和值，则说明不需要进行判断
						if(privilegeCriteria.getObjectAttribute() == null || privilegeCriteria.getObjectAttributeValue() == null){
							logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的属性匹配内容为空，直接返回true...");
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
								if(datas[0].equals(privilegeCriteria.getObjectAttribute()) && datas[1].equals(privilegeCriteria.getObjectAttributeValue())){
									return true;
								}
							}

						}catch(Exception e){
							logger.error("在解析权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的属性匹配时出错,返回false:" + e.getMessage());
							return false;
						}
						logger.debug("当前操作与权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]操作对象匹配，但是操作码不匹配，返回false.");
						return false;
					}

					for(String objectId : objects){
						try{
							logger.debug("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象列表ID[" + objectId + "]与当前条件的对象列表:" + privilegeCriteria.getObjectId());
							if(objectId.equals(privilegeCriteria.getObjectId())){
								//判断属性权限，如果权限的属性模式为空，那么直接返回成功
								if(privilege.getObjectAttributePattern() == null || privilege.getObjectAttributePattern().equals("") || privilege.getObjectAttributePattern().equals("*")){
									return true;
								}
								//如果权限条件中未指定属性和值，则说明不需要进行判断
								if(privilegeCriteria.getObjectAttribute() == null && privilegeCriteria.getObjectAttributeValue() == null){
									logger.debug("在比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]时，权限条件中的属性和值都为空，直接返回true...");
									return true;
								}
								try{
									String[] attributes = privilege.getObjectAttributePattern().split(",");
									if(attributes == null || attributes.length < 1){
										logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象属性数据异常:" + privilege.getObjectAttributePattern());
										return false;
									}
									for(String attribute : attributes){
										logger.debug("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象属性[" + attribute + "]与当前条件的对象属性:" + privilegeCriteria.getObjectAttribute() + "=" + privilegeCriteria.getObjectAttributeValue());
										String[] datas = attribute.split("=");
										if(datas == null || datas.length < 1){
											return false;
										}
										if(datas[0].equals(privilegeCriteria.getObjectAttribute()) && (datas[1].equals("*") || datas[1].equals(privilegeCriteria.getObjectAttributeValue()))){
											logger.debug("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的匹配条件/值与权限条件中的条件/值完全匹配，返回true...");
											return true;
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
		}
		logger.warn("未能匹配到与当前操作:" + privilegeCriteria + "一致的任何权限,返回false");
		return false;

	}

	/**
	 * 根据用户名或昵称得到用户UUID
	 * 如果该用户UUID是一个数字，则判断为输入的就是UUID
	 * @param username
	 * @return
	 */
	@Override
	public User getUserByName(String username, int userType) {

		if(userType == 0) {
			userType = UserTypes.partner.id;
		}
		username = username.trim();
		if (StringUtils.isNumeric(username)) {
			User payFromUser = null;
			long uuid = NumericUtils.parseLong(username);
			if (userType == UserTypes.frontUser.getId()) {
				payFromUser = frontUserService.select(uuid);
			} else {
				payFromUser = partnerService.select(uuid);
			}
			if (payFromUser != null) {
				logger.debug("使用uuid={},userType={}找到了用户，返回该用户:{}", username, userType, payFromUser);
				return payFromUser;
			} 
		}
		//按用户名搜索
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setNickName(username);

		List<User> userList = null;
		//先假设是昵称
		if (userType == UserTypes.frontUser.getId()) {
			userList = frontUserService.list(frontUserCriteria);
		} else {
			userList = partnerService.list(frontUserCriteria);
		}
		if(userList.size() > 0) {
			logger.debug("使用昵称={},userType={}找到了{}个用户，返回第一个用户:{}", username, userType, userList.size(), userList.get(0));
			return userList.get(0);
		}
		logger.debug("找不到昵称=" + username + "的用户,尝试使用用户名查询，查询用户类型:" + userType);
		frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(username);
		userList = frontUserService.list(frontUserCriteria);
		if(userList.size() > 0) {
			logger.debug("使用username={},userType={}找到了{}个用户，返回第一个用户:{}", username, userType, userList.size(), userList.get(0));
		return userList.get(0);
		}
		logger.info("找不到uuid/用户名/昵称=" + username + "的用户,查询用户类型:" + userType);
		return null;


	}

	//检查指定用户是否具备操作指定工作步骤的权限
	/*@Override
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
			if(privilege.getObjectTypeCode().equals(route.getTargetObjectType())){

	 * 权限的操作代码是*
	 * 或者
	 * 权限的操作代码是r，而且权限条件的操作代码是list或get
	 * 或者
	 * 权限的操作代码是w，而且权限条件的操作代码是delete、update或create
	 * 或者
	 * 权限与权限条件的操作代码完全一致

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
					//如果要权限条件要比对的对象ID为空，跳过这次比对
					if(privilegeCriteria.getObjectId() == 0){
						logger.info("权限[" + privilege.getOperateCode() + "]的对象ID为0...");
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
		}
		return false;

	}*/

	/**
	 * @param user 指定的操作用户
	 * @param objectTypeCode 指定的对象类型代码，在ObjectType中定义
	 * @param objectTypeId 指定的对象类型ID
	 * @param operateCode 针对该对象的操作代码
	 * 
	 * @return 返回该用户能合法操作的类的对象ID列表，以英文逗号分割的整数ID列表，*表示所有
	 */
	@Override
	public String listValidObjectId(User user, String objectTypeCode, int objectTypeId, String operateCode){
		if(user == null){			
			return null;
		}
		if(user.getRelatedPrivilegeList() == null || user.getRelatedPrivilegeList().size() < 1){
			return null;
		}
		StringBuffer validObjectIdList = new StringBuffer();
		for(Privilege privilege : user.getRelatedPrivilegeList()){
			logger.debug("比对权限[" + privilege + "]");
			if(privilege == null){
				continue;
			}
			if(StringUtils.isBlank(privilege.getObjectTypeCode())){
				logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作对象代码为空，跳过...");
				continue;
			}
			if(!objectTypeCode.equals(privilege.getObjectTypeCode())){
				logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作对象代码是:" + privilege.getObjectTypeCode() + ",与被比较对象:" + objectTypeCode + "不一致，跳过...");
				continue;
			}
			//权限与权限条件的对象类型代码一致
			if(privilege.getObjectTypeCode().equals(objectTypeCode)){
				/*
				 * 权限的操作代码是*
				 * 或者
				 * 权限的操作代码是r，而且权限条件的操作代码是list或get
				 * 或者
				 * 权限的操作代码是w，而且权限条件的操作代码是delete、update或create
				 * 或者
				 * 权限与权限条件的操作代码完全一致
				 */
				logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]...");
				if(privilege.getOperateCode() == null){
					logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "]的操作码为空");
					continue;
				}					
				if(privilege.getOperateCode().equals("*")
						||(privilege.getOperateCode().equals("r") && (operateCode.equals(Operate.list.getCode()) || operateCode.equals(Operate.get.getCode())|| operateCode.equals(Operate.relate.getCode()) ))
						||(privilege.getOperateCode().equals("w") && (operateCode.equals(Operate.create.getCode()) || operateCode.equals(Operate.delete.getCode()) || operateCode.equals(Operate.update.getCode())))
						||privilege.getOperateCode().equals(operateCode)
						){
					logger.info("比对权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]与操作码[" + operateCode + "]...");
					//如果权限objectList为空，跳过这次比对
					if(privilege.getObjectList() == null || privilege.getObjectList().equals("")){
						logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象列表为空...");
						continue;
					}

					String objects[] = privilege.getObjectList().split(",");
					//如果无法获取或解析匹配对象列表，跳过这次比对
					if(objects == null || objects.length == 0){
						logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode()  + "]的对象列表无法解析:" + privilege.getObjectList());
						continue;
					}
					//如果匹配对象的第一个是*，表示匹配所有
					if(objects[0].equals("*")){
						logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的对象列表第一个为*，查看是否需要进行属性权限匹配:" + privilege.getObjectAttributePattern());
						//判断属性权限，如果权限的属性模式为空，那么直接返回成功
						if(privilege.getObjectAttributePattern() == null || privilege.getObjectAttributePattern().equals("") || privilege.getObjectAttributePattern().equals("*")){
							logger.info("权限[" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName() + "/" + privilege.getOperateCode() + "]的属性匹配规则为空或*，直接返回*");
							return "*";
						}						
					}
					validObjectIdList.append(',').append(privilege.getObjectList());
				}

			}
		}
		String result = validObjectIdList.toString().replaceFirst(",","");
		logger.info("用户[" + user.getUuid() + "针对类型[" + objectTypeCode + "]的操作" + operateCode + "的合法对象列表是:" + result);
		return result;
	}


	@Override
	public boolean isSuperPartner(User partner) {
		if(partner == null){
			return false;
		}
		if(partner.getUuid() == 300001 && partner.getUsername().equals("sa")){
			return true;
		}
		return false;
	}

	/**
	 * 该用户是否为平台级别的用户
	 * 如果不是，则只能查询该用户及其子账户的数据
	 * 如果是，可以查询所有ownerId一致的数据
	 * @param partner
	 * @return
	 */
	@Override
	public boolean isPlatformGenericPartner(User partner) {
		Assert.notNull(partner,"尝试检查是否为平台一般账户的对象为空");
		if(partner.getUserExtraTypeId() > 0){
			return false;
		}
		return true;

	}



	private User initCheck(PrivilegeCriteria privilegeCriteria){
		if(privilegeCriteria == null){
			logger.info("权限条件为空");
			return null;
		}
		if(privilegeCriteria.getUuid() == 0){
			logger.info("权限条件中UUID=0");
			return null;
		}
		if(privilegeCriteria.getUserTypeId() == 0){
			logger.info("权限条件中用户类型未指定");
			return null;
		}
		if(privilegeCriteria.getObjectTypeCode() == null || privilegeCriteria.getObjectTypeCode().equals("")){
			logger.info("权限条件中对象类型ID和对象类型代码有一个为空");
			return null;
		}
		//查找用户
		User user = null;

		if(privilegeCriteria.getUserTypeId() == UserTypes.partner.getId()){
			user = partnerService.select(privilegeCriteria.getUuid());
			if(user == null){
				logger.info("指定的合作用户不存在[" + privilegeCriteria.getUuid() + "]");
				return null;
			}
		}
		if(privilegeCriteria.getUserTypeId() == UserTypes.frontUser.getId()){
			user = frontUserService.select(privilegeCriteria.getUuid());
			if(user == null){
				logger.info("指定的终端用户不存在[" + privilegeCriteria.getUuid() + "]");
				return null;
			}
		}
		if(user == null){
			logger.info("找不到指定的用户[" + privilegeCriteria.getUuid() + "/" + privilegeCriteria.getUserTypeId() + "]");
			return null;
		}
		if(user.getRelatedRoleList() == null || user.getRelatedRoleList().size() < 1){
			logger.info("指定的用户[" + privilegeCriteria.getUuid() + "/" + privilegeCriteria.getUserTypeId() + "]没有任何关联角色");
			return null;
		}
		return user;
	}

	@Override
	public boolean havePrivilege(HttpServletRequest request, String objectType,
			Object targetObject) {

		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void writeOperate(User user, List<?> targetList) throws JsonParseException, JsonMappingException, IOException {

		long beginTs = new Date().getTime();

		Assert.notNull(user);
		if(user.getRelatedPrivilegeList() == null || user.getRelatedPrivilegeList().size() < 1){
			logger.warn("用户[" + user.getUuid() + "]的权限列表为空");
			return;
		}
		if(targetList == null || targetList.size() < 1){
			logger.error("尝试写入操作的EisObject列表为空");
			return;
		}

		if(!(targetList.get(0) instanceof EisObject)){
			logger.error("无法处理非EisObject列表的权限");
			return;
		}
		List<EisObject> eisList = new ArrayList<EisObject>();
		eisList = (List<EisObject>)targetList;
		String objectType = StringUtils.uncapitalize(targetList.get(0).getClass().getSimpleName());
		ObjectMapper om = JsonUtils.getInstance();
		JavaType javaType = om.getTypeFactory().constructMapType(HashMap.class, String.class, Attribute.class);   

		for(Privilege privilege : user.getRelatedPrivilegeList()){
			if(privilege.getObjectTypeCode() == null || !privilege.getObjectTypeCode().equals(objectType)){
				logger.debug("忽略对象代码不一致的权限:" + privilege.getPrivilegeId() + ",其对象是:" + privilege.getObjectTypeCode());
				continue;
			}
			boolean haveObjectAccess = false;
			boolean haveAttributeAccess = false;
			if(privilege.getObjectList() != null && !privilege.getObjectList().trim().equals("*")){
				//权限对应的对象列表不是空也不是*，需要检查是否严格和对象ID匹配
				String[] objectList = privilege.getObjectList().split(",");
				for(String o : objectList){
					for(EisObject eis : eisList){
						if(o.equals(String.valueOf(eis.getId()))){
							logger.debug("权限[" + privilege.getPrivilegeId() + "]的权限对象与判断对象的ID一致，可访问");
							haveObjectAccess = true;
							break;
						}
					}
				}
			} else {
				logger.debug("权限[" + privilege + "]的权限对象是空或*，可全部访问");
				haveObjectAccess  = true;
			}
			if(!haveObjectAccess){
				continue;
			}


			if(privilege.getObjectAttributePattern() != null && !privilege.getObjectAttributePattern().trim().equals("*")){
				//权限对应的对象属性列表不是空也不是*，需要检查是否严格和对象属性匹配
				Map<String,Attribute> attributeMap = JsonUtils.getInstance().readValue(privilege.getObjectAttributePattern(), javaType);
				for(String key : attributeMap.keySet()){
					for(EisObject eis : eisList){						
						String value = ClassUtils.getValue(eis, key, "native");
						if(value != null && value.equals(attributeMap.get(key).getValidValue())){
							//有对应权限
							haveAttributeAccess = true;
							break;

						}
					}
				}
			} else {
				logger.debug("权限[" + privilege + "]的权限对象属性是空或*，可访问");
				haveAttributeAccess  = true;
			}
			if(!haveAttributeAccess){
				continue;
			}


			//判断拥有哪些权限
			if(privilege.getOperateCode() == null){
				logger.debug("权限[" + privilege + "]的操作代码为空，忽略");
				continue;
			}
			for(EisObject eis : eisList){

				if(privilege.getOperateCode().equals("*")){
					eis.setOperateValue("haveFullPrivilege","true");
					eis.setOperateValue("get", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
					eis.setOperateValue("update", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
					eis.setOperateValue("delete", "./" + objectType + "/" + Operate.delete.getCode() + "/" + eis.getId());
				}	
				if(privilege.getOperateCode().equals("w")){
					eis.setOperateValue("haveWritePrivilege","true");
					eis.setOperateValue("get", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
					eis.setOperateValue("update", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
					eis.setOperateValue("delete", "./" + objectType + "/" + Operate.delete.getCode() + "/" + eis.getId());
				}
				if(privilege.getOperateCode().equals("r")){
					eis.setOperateValue("haveReadPrivilege","true");
					eis.setOperateValue("get", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
				}
				if(privilege.getOperateCode().equals(Operate.create.getCode())){
					eis.setOperateValue(Operate.create.getCode(),"true");
					eis.setOperateValue("get", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
				}
				if(privilege.getOperateCode().equals(Operate.update.getCode())){
					eis.setOperateValue(Operate.update.getCode(),"true");
					eis.setOperateValue("get", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
					eis.setOperateValue("update", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
				}
				if(privilege.getOperateCode().equals(Operate.list.toString())){
					eis.setOperateValue(Operate.list.getCode(),"true");
				}
				if(privilege.getOperateCode().equals(Operate.delete.getCode())){
					eis.setOperateValue(Operate.delete.getCode(),"true");
					eis.setOperateValue("get", "./" + objectType + "/" + Operate.get.getCode() + "/" + eis.getId());
					eis.setOperateValue("delete", "./" + objectType + "/" + Operate.delete.getCode() + "/" + eis.getId());
				}
			}
		}

		long timing = new Date().getTime() - beginTs;
		logger.debug("操作权限判断耗时:" + timing);
	}


	/**
	 * 该对象是否属于某个指定用户或其下级用户
	 */
	@Override
	public boolean isBelongUser(EisObject targetObject, User user) {
		partnerService.applyMoreDynmicData(user);
		List<User> grogeny = user.getProgeny();
		if(grogeny == null || grogeny.size() < 1){
			logger.info("用户[" + user.getUuid() + "]没有任何子孙账户");
			grogeny = new ArrayList<User>();
			grogeny.add(user);
		}
		String conditionAttribute = null;
		Method m = org.springframework.util.ClassUtils.getMethodIfAvailable(targetObject.getClass(), "getInviter",  new Class<?>[]{});
		if(m == null){
			logger.info("对象[" + targetObject.getClass().getName() + "]没有getInviter，查找getSupplyPartnerId方法");
			m = org.springframework.util.ClassUtils.getMethodIfAvailable(targetObject.getClass(), "getSupplyPartnerId",  new Class<?>[]{});
			conditionAttribute = "supplyPartnerId";
		} else {
			conditionAttribute = "inviter";
		}
		if(m == null){
			logger.error("对象[" + targetObject.getClass().getName() + "]没有getInviter或getSupplyPartnerId方法");
			return false;
		}
		String result = ClassUtils.getValue(targetObject, conditionAttribute, CommonStandard.COLUMN_TYPE_NATIVE);
		logger.debug("对象[" + targetObject.getClass().getName() + "]获取从属于属性" + conditionAttribute + "=" + result);
		if(!NumericUtils.isNumeric(result)){
			logger.error("对象[" + targetObject.getClass().getName() + "]获取从属于属性" + conditionAttribute + "=" + result + "的结果异常");
			return false;
		}
		long inviter = Long.parseLong(result);
		for(User child : grogeny){
			if(child.getUuid() == inviter){				
				logger.debug("对象[" + targetObject.getClass().getName() + "]的从属于属性" + conditionAttribute + "=" + inviter + "]与用户[" + user.getUuid() + "]的子账户[" + child.getUuid() + "]一致");
				return true;
			}
		}
		logger.debug("对象[" + targetObject.getClass().getName() + "]的inviter=" + inviter + "]没有与用户[" + user.getUuid() + "]或其子账户一致的");
		return false;
	}


	@Override
	public boolean havePrivilege(User user, String targetObject, String code ) {

		boolean result =  havePrivilege(user, new PrivilegeCriteria(targetObject, code, user.getOwnerId()));
		logger.info("检查用户[" + user.getUuid() + "是否对对象[" + targetObject + "]具备操作:" + code + "的权限:" + result);
		return result;
	}

}

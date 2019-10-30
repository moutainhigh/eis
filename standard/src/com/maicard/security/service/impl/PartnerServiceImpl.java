package com.maicard.security.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.base.InviterSupportCriteria;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.ShortMd5;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.mb.service.MessageService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.dao.PartnerDao;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.PartnerRelationService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.service.UserDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.CommonStandard.DataFetchMode;
import com.maicard.standard.SecurityStandard.UserTypes;

@Service
public class PartnerServiceImpl extends BaseService implements PartnerService {

	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private MessageService messageService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private PartnerDao partnerDao;
	@Resource
	private UserDataService userDataService;
	@Resource
	private PartnerRelationService partnerRelationService;
	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;
	@Resource
	private PartnerRoleService partnerRoleService;
	private String messageBusName;


	@PostConstruct
	public void init(){
		messageBusName = configService.getValue(DataName.messageBusUser.toString(),0);//"com.maicard.mb.yeele.System.user"//

	}

	@Override
	@CacheEvict(value=CommonStandard.cacheNameUser, key = "'Partner#' + #partner.uuid")
	public void evictCache(User partner) {
 	}

	@Override
	public int insert(User partner) {
		Assert.notNull(partner,"尝试插入的partner对象为空");
		if(globalUniqueService.exist(new GlobalUnique(partner.getUsername(),partner.getOwnerId()))){
			logger.error("无法创建前台用户，因为全局数据不唯一[" + partner.getUsername() + "]");
			return -1;
		}
		if(!globalUniqueService.create(new GlobalUnique(partner.getUsername(),partner.getOwnerId()))){
			logger.error("无法创建前台用户，因为无法创建全局唯一数据[" + partner.getUsername() + "]");
			return -1;
		}
		String inviteByCode = partner.getExtraValue(DataName.userInviteByCode.toString());
		if(StringUtils.isNotBlank(inviteByCode)){
			inviteByCode = inviteByCode.trim();
			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria = new UserDataCriteria();
			userDataCriteria.setUserTypeId(UserTypes.partner.getId());
			userDataCriteria.setDataCode(DataName.userInviteCode.toString());
			userDataCriteria.setDataValue(inviteByCode);
			userDataCriteria.setCorrectWithDynamicData(false);
			List<UserData> userDataList = userDataService.list(userDataCriteria);
			if(userDataList != null && userDataList.size() == 1 ){
				logger.info("根据邀请码[" + inviteByCode + "]查找对应的推广ID:" + userDataList.get(0).getUuid());
				long inviteUuid = userDataList.get(0).getUuid();
				partner.setInviter(inviteUuid);
				partner.setParentUuid(inviteUuid);
			} else {
				logger.info("根据邀请码[" + inviteByCode + "]找不到对应的推广ID.");
			}
		}

		int rs = insertLocal(partner);	
		if(rs != 1){
			logger.error("本地插入Partner失败,返回值:" + rs);
			return rs;
		}
		//发送新增用户的同步信息到消息总线
		if(partner.getSyncFlag() == 1){
			return 1;
		} else {
			if(logger.isDebugEnabled()){
				logger.debug("发送新增合作伙伴用户[" + partner.getUuid() + "]的同步信息到消息总线");
			}
			messageService.sendJmsDataSyncMessage(messageBusName, "partnerService", "insertLocal", partner);
		}
		return 1;

	}
	@Override
	public int insertLocal(User partner) {
		if(StringUtils.isBlank(partner.getUserPassword())){
			logger.error("合作伙伴没有设置密码");
			return -1;
		}
		if(partner.getUserPassword().length() != 64){//防止二次加密
			//将密码改为加密形式
			String plainPassword = partner.getUserPassword();
			partner.setUserPassword(Crypt.passwordEncode(partner.getUserPassword()));
			logger.debug("新增合作伙伴密码是:" + plainPassword + "/" + partner.getUserPassword());
		}
		if(partner.getAuthKey() != null && partner.getAuthKey().length() != 64){//防止二次加密
			//将密码改为加密形式
			String plainPassword = partner.getAuthKey();
			partner.setAuthKey(Crypt.passwordEncode(partner.getAuthKey()));
			logger.debug("新增合作伙伴二级密码是:" + plainPassword + "/" + partner.getAuthKey());
		}
		
		if(partner.getLevel() == 0){
			partner.setLevel(2);
		}
		if(partner.getCreateTime() == null){
			partner.setCreateTime(new Date());
		}
		partner.setUserTypeId(UserTypes.partner.getId());
		int rs = 0;
		try{
			rs = partnerDao.insert(partner);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("插入数据失败,数据操作未返回1");
			return rs;	
		}
		logger.debug("新增合作伙伴UUID=" + partner.getUuid());
		partnerRoleRelationService.deleteByUuid(partner.getUuid());
		if(partner.getRelatedRoleList() != null){
			logger.debug("为新增合作伙伴[" + partner.getUuid() + "]增加关联角色:" + partner.getRelatedRoleList().size() );
			for(int i = 0; i < partner.getRelatedRoleList().size(); i++){
				UserRoleRelation userRoleRelation = new  UserRoleRelation();
				userRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				userRoleRelation.setUuid(partner.getUuid());
				userRoleRelation.setRoleId(partner.getRelatedRoleList().get(i).getRoleId());
				userRoleRelation.setOwnerId(partner.getOwnerId());
				partnerRoleRelationService.insert(userRoleRelation);
			}
		} else {
			int partnerDefaultRoleId = configService.getIntValue("partner.defaultRoleId",0);
			if(partnerDefaultRoleId > 0){
				logger.info("新增的合作伙伴[" + partner.getUuid() + "]未包含对应角色，为其添加默认角色:" + partnerDefaultRoleId);
				UserRoleRelation userRoleRelation = new  UserRoleRelation();
				userRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				userRoleRelation.setUuid(partner.getUuid());
				userRoleRelation.setRoleId(partnerDefaultRoleId);
				partnerRoleRelationService.insert(userRoleRelation);
			} else {
				logger.warn("新增的合作伙伴[" + partner.getUuid() + "]未包含对应角色，系统也未配置添加默认角色:" + partnerDefaultRoleId);

			}
		}
		//TODO userConfigService.deleteByUser(uuid);
		if(partner.getUserConfigMap() == null){
			partner.setUserConfigMap(new HashMap<String,UserData>());
		}
		if(partner.getUserConfigMap().get(DataName.userInviteCode.toString()) == null || StringUtils.isBlank(partner.getUserConfigMap().get(DataName.userInviteCode.toString()).getDataValue())){
			String inviteCode = "p" + ShortMd5.encode(String.valueOf(partner.getUuid()));
			logger.info("新增的合作伙伴[" + partner.getUuid() + "]未生成邀请码，生成邀请码:" + inviteCode);
			UserData userData = new UserData();
			userData.setUuid(partner.getUuid());
			userData.setDataCode(DataName.userInviteCode.toString());
			userData.setDataValue(inviteCode);
			partner.getUserConfigMap().put(DataName.userInviteCode.toString(), userData);
		} else {
			logger.info("新增的合作伙伴[" + partner.getUuid() + "]已生成邀请码:" + partner.getUserConfigMap().get(DataName.userInviteCode.toString()).getDataValue());
		}

		try{
			userDataService.processUserConfig(partner);
		}catch(Exception e){
			throw new DataWriteErrorException("在处理用户配置数据时出错:" + e.getMessage());
		}	
		return 1;
	}

	@Override
	public int update(User partner){
		int rs = updateLocal(partner);	
		if(rs != 1){
			logger.error("本地修改Partner失败,返回值:" + rs);
			return rs;
		}
		//发送更新用户的同步信息到消息总线
		if(partner.getSyncFlag() == 1){
			return 1;
		} else {
			if(logger.isDebugEnabled()){
				logger.debug("发送修改合作伙伴用户[" + partner.getUuid() + "]的同步信息到消息总线");
			}
			messageService.sendJmsDataSyncMessage(messageBusName, "partnerService", "updateLocal", partner);
		}
		return 1;
	}

	@Override
	public int updateNoNull(User user) {
		Assert.notNull(user,"尝试检查的用户对象为空");
		Assert.isTrue(user.getUuid() > 0,"尝试检查的用户对象UUID为空");
		User _oldPartner = partnerDao.select(user.getUuid());
		user.setUserTypeId(UserTypes.partner.getId());
		if (StringUtils.isNotBlank(user.getUserPassword())) {
			if (_oldPartner != null) {
				//与原密码不一致
				if(!user.getUserPassword().equals(_oldPartner.getUserPassword())){
					if(user.getUserPassword().length() != 64){//防止二次加密
						//将密码改为加密形式
						String plainPassword = user.getUserPassword();
						user.setUserPassword(Crypt.passwordEncode(user.getUserPassword()));
						if(logger.isDebugEnabled()){
							logger.debug("修改用户密码是:" + plainPassword + "/" + user.getUserPassword());
						}
					}
				}
			}
		}
		if (StringUtils.isNotBlank(user.getAuthKey())) {
			if (_oldPartner != null) {
				//与原密码不一致
				if(!user.getAuthKey().equals(_oldPartner.getAuthKey())){
					if(user.getAuthKey().length() != 64){//防止二次加密
						//将密码改为加密形式
						String plainPassword = user.getAuthKey();
						user.setAuthKey(Crypt.passwordEncode(user.getAuthKey()));
						if(logger.isDebugEnabled()){
							logger.debug("修改用户二级密码是:" + plainPassword + "/" + user.getAuthKey());
						}
					}
				}
			}
		}
		int rs = partnerDao.updateNoNull(user);
		if(rs == 1){
			messageService.sendJmsDataSyncMessage(null, "partnerService", "updateNoNull", user);
		}
		return rs;


	}

	@Override
	public int updateLocal(User partner) {
		if(partner == null){
			return 0;
		}
		int actualRowsAffected = 1;
		long uuid = partner.getUuid();	
		User _oldPartner = partnerDao.select(uuid);
		partner.setUserTypeId(UserTypes.partner.getId());
		if (_oldPartner != null) {
			if(partner.getUserPassword() == null || partner.getUserPassword().equals("")){
				//未修改密码
				partner.setUserPassword(_oldPartner.getUserPassword());
			} else {
				//与原密码不一致
				if(!partner.getUserPassword().equals(_oldPartner.getUserPassword())){
					if(partner.getUserPassword().length() != 64){//防止二次加密
						//将密码改为加密形式
						String plainPassword = partner.getUserPassword();
						partner.setUserPassword(Crypt.passwordEncode(partner.getUserPassword()));
						if(logger.isDebugEnabled()){
							logger.debug("修改用户密码是:" + plainPassword + "/" + partner.getUserPassword());
						}
					}
				}
			}
			if(logger.isDebugEnabled()){
				logger.debug("更新用户，密码是:" + partner.getUserPassword() + "/" + partner.getCurrentStatus());
			}
			try{
				partnerDao.update(partner);
			}catch(Exception e){
				logger.error("更新数据失败:" + e.getMessage());
			}
		}
		try{
			userDataService.processUserConfig(partner);
		}catch(Exception e){
			throw new DataWriteErrorException("在处理用户配置数据时出错:" + e.getMessage());
		}
		if(partner.getRelatedRoleList() != null){
			partnerRoleRelationService.deleteByUuid(partner.getUuid());
			logger.debug("更新合作伙伴[" + partner.getUuid() + "]增加关联角色:" + partner.getRelatedRoleList().size() );
			for(int i = 0; i < partner.getRelatedRoleList().size(); i++){
				UserRoleRelation userRoleRelation = new  UserRoleRelation();
				userRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				userRoleRelation.setUuid(partner.getUuid());
				userRoleRelation.setRoleId(partner.getRelatedRoleList().get(i).getRoleId());
				partnerRoleRelationService.insert(userRoleRelation);
			}
		}
		return actualRowsAffected;
	}

	@Override
	public int delete(long uuid) {
		User _oldUser = partnerDao.select(uuid);
		if(_oldUser == null){
			logger.warn("尝试删除的Partner[" + uuid + "]不存在");
			return 0;
		}
		int rs = deleteLocal(_oldUser);	
		if(rs != 1){
			logger.error("本地删除Partner[" + uuid + "]失败,返回值:" + rs);
			return rs;
		}
		//发送新增用户的同步信息到消息总线

		if(logger.isDebugEnabled()){
			logger.debug("发送删除合作伙伴用户[" + _oldUser.getUuid() + "]的同步信息到消息总线");
		}
		messageService.sendJmsDataSyncMessage(messageBusName, "partnerService", "deleteLocal", _oldUser);

		return 1;
	}
	public int deleteLocal(User partner) {
		int actualRowsAffected = 0;
		try{
			actualRowsAffected = partnerDao.delete(partner.getUuid());
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		if(actualRowsAffected == 1){
			partnerRoleRelationService.deleteByUuid(partner.getUuid());
			UserDataCriteria userDataCriteria = new UserDataCriteria();
			userDataCriteria.setUuid(partner.getUuid());
			userDataService.delete(userDataCriteria);
		}
		return actualRowsAffected;
	}

	public User select(long uuid) {
		return partnerDao.select(uuid);
	}

	public List<User> list(UserCriteria partnerCriteria) {
		if(partnerCriteria == null){
			return Collections.emptyList();
		}
		if(partnerCriteria.getParentUuid() > 0){
			//		logger.debug("条件中指定了父UUID，查询该父UUID[" + partnerCriteria.getParentUuid() + "]拥有的所有子账户,同时强制以简易模式获取数据");
			if (partnerCriteria.getDataFetchMode().equals(""))
				partnerCriteria.setDataFetchMode(DataFetchMode.simple.toString());
			List<User> subUserList = new ArrayList<User>();
			listAllChildren(subUserList, partnerCriteria.getParentUuid());
			if(subUserList != null && subUserList.size() > 0){
				partnerCriteria.setSubUserList(subUserList);
			}
		}
		List<Long> pkList =  partnerDao.listPk(partnerCriteria);
		logger.debug("找到的合作伙伴有[" + (pkList == null ? -1 :pkList.size()) + "]个" );
		if(pkList == null || pkList.size() < 1){
			return Collections.emptyList();
		}
		List<User> partnerList = new ArrayList<User>();
		for(int i = 0; i < pkList.size(); i++){
			User partner = partnerDao.select(pkList.get(i));
			if(partner != null){
				partner.setIndex(i);
				partnerList.add(partner);
			}
		}
		return partnerList;
	}

	public List<User> listOnPage(UserCriteria partnerCriteria) {
		if(partnerCriteria == null){
			return null;
		}
		logger.debug("list");
		if(partnerCriteria.getParentUuid() > 0){
			//		logger.debug("条件中指定了父UUID，查询该父UUID[" + partnerCriteria.getParentUuid() + "]拥有的所有子账户,同时强制以简易模式获取数据");
			if (partnerCriteria.getDataFetchMode().equals(""))
				partnerCriteria.setDataFetchMode(DataFetchMode.simple.toString());
			List<User> subUserList = new ArrayList<User>();
			listAllChildren(subUserList, partnerCriteria.getParentUuid());
			if(subUserList != null && subUserList.size() > 0){
				partnerCriteria.setSubUserList(subUserList);
			}
		}
		List<Long> pkList =  partnerDao.listPkOnPage(partnerCriteria);
		logger.debug("找到的合作伙伴有[" + (pkList == null ? -1 :pkList.size()) + "]个" );
		if(pkList == null || pkList.size() < 1){
			return null;
		}
		List<User> partnerList = new ArrayList<User>();
		for(int i = 0; i < pkList.size(); i++){
			User partner = partnerDao.select(pkList.get(i));
			if(partner != null){
				partner.setIndex(i);
				partnerList.add(partner);
			}
		}
		return partnerList;
	}

	//列出指定partner的所有子账户
	public void listAllChildren(List<User> all, long  fatherId){
		//logger.debug("开始循环" + fatherId + "的所有子账户");
		List<User> child = getChildren(fatherId);
		if(child == null){
			child = new ArrayList<User>();
		}
		for(int i = 0; i< child.size(); i++){
			listAllChildren(all, child.get(i).getUuid());
			//logger.info("child::" + child.get(i).getUuid());
			all.add(child.get(i));
		}

	}
	private List<User> getChildren(long uuid){
		User partner = select(uuid);
		if(partner == null){
			return null;
		}

		UserCriteria partnerCriteria = new UserCriteria();
		partnerCriteria.setParentUuid(uuid);
		List<Long> pkList = partnerDao.listPk(partnerCriteria);
		if(pkList != null && pkList.size() > 0){
			List<User> childrenList = new ArrayList<User>();
			for(Long subUuid : pkList){
				User child = partnerDao.select(subUuid);
				if(child != null){
					childrenList.add(child);
				}
			}
			return childrenList;
		}
		return null;
	}

	public int count(UserCriteria partnerCriteria){
		return partnerDao.count(partnerCriteria);
	}

	//获取当前用户直到2级用户（即总合作伙伴账户]的所有用户
	public List<User> getUserPath(long uuid){
		User user = select(uuid);
		if(user == null){
			return null;
		}
		ArrayList<User> pathUsers = new ArrayList<User>();
		User fatherUser = select(user.getParentUuid());
		//logger.info("获取节点[" + node.getNodeId() + "]的路径节点");
		while(fatherUser != null){
			pathUsers.add(fatherUser);
			//logger.info("查找节点[" + fatherNode.getNodeId() + "]的父节点:" + fatherNode.getParentNodeId());
			if(fatherUser.getLevel() <= 2){
				break;
			}
			fatherUser = select(fatherUser.getParentUuid());

		}
		logger.info("共获取到了" + pathUsers.size() + "个上级用户");
		ArrayList<User> sortedPathUsers = new ArrayList<User>();
		//反转顺序
		for(int i = pathUsers.size()-1; i >= 0; i--){
			sortedPathUsers.add(pathUsers.get(i));
		}
		return sortedPathUsers;

	}

	@Override
	public boolean isValidSubUser(long parentUuid, long childUuid) {
		List<User>all = new ArrayList<User>();
		listAllChildren(all, parentUuid);
		if(all == null || all.size() < 1){
			return false;
		}
		for(User user : all){
			if(user.getUuid() == childUuid){
				return true;
			}
		}

		return false;
	}

	public User getByInviteCode(String inviteCode){
		Assert.notNull(inviteCode, "查找对应合作伙伴的邀请码不能为空");
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUserTypeId(UserTypes.partner.getId());
		userDataCriteria.setDataCode(DataName.userInviteCode.toString());
		userDataCriteria.setDataValue(inviteCode);
		userDataCriteria.setCorrectWithDynamicData(false);
		List<UserData> userDataList = userDataService.list(userDataCriteria);
		if(userDataList != null && userDataList.size() == 1 ){
			long uuid = userDataList.get(0).getUuid();

			logger.info("根据邀请码[" + inviteCode + "]查找到对应的合作伙伴UUID:" + uuid);
			User partner = select(uuid);
			if(partner == null){
				logger.error("根据UUID=" + uuid + "找不到对应的合作伙伴");
			}
			return partner;			
		} 
		logger.info("根据邀请码[" + inviteCode + "]找不到对应的推广ID，或返回数据不唯一.");
		return null;

	}

	@Override
	public List<User> listProgeny(long rootUuid, long ownerId) {
		String uuids = partnerDao.listProgeny(rootUuid);
		if(StringUtils.isBlank(uuids)){
			return null;
		}
		String[] uuidArray = uuids.split(",");
		List<User> progenyList = new ArrayList<User>();
		for(String uuid : uuidArray){
			User child = select(Long.parseLong(uuid));
			if(child == null || child.getOwnerId() != ownerId){
				continue;
			}
			progenyList.add(child);

		}

		return progenyList;
	}

	@Override
	public long getHeadUuid(User partner){

		Assert.notNull(partner,"尝试获取祖先账户的用户不能为空");
		if(partner.getHeadUuid() > 0){
			logger.debug("当前商户[" + partner.getUuid() + "]已有祖先账户ID:" + partner.getHeadUuid());
			return partner.getHeadUuid();
		}
		List<User> userList = this.getUserPath(partner.getUuid());
		if(userList == null || userList.size() < 1){
			if(partner.getParentUuid() > 0){
				logger.debug("当前商户[" + partner.getUuid() + "]没有祖先账户，返回其父账户ID作为祖先账户ID:" + partner.getParentUuid());
				return partner.getParentUuid();
			} else {
				logger.debug("当前商户[" + partner.getUuid() + "]没有祖先账户也没有父账户，返回其自身账户ID作为祖先账户ID:" + partner.getUuid());
				return partner.getUuid();
			}
		}
		long headUuid = userList.get(0).getUuid();
		logger.debug("经过计算，当前商户[" + partner.getUuid() + "]的祖先账户ID是:" + headUuid);
		return headUuid;
	}

	@Override
	public long getHeadUuid(long inviter){
		if(inviter <= 0){
			return 0;
		}
		User partner = select(inviter);
		if(partner == null){
			logger.error("找不到指定的partner:" + inviter);
			return 0;
		}
		return getHeadUuid(partner);
	}

	@Override
	public void applyMoreDynmicData(User partner) {
		if(partner.getDynamicFlag() == 1){
			return;
		}
		partner.setProgeny(listProgeny(partner.getUuid(), partner.getOwnerId()));
		partner.setDynamicFlag(1);


	}

	/**
	 * 检查并放入指定用户的子账户到条件的inviter中
	 * @throws SecurityException 
	 * @throws Exception 
	 */
	@Override
	public void setSubPartner(InviterSupportCriteria criteria, User partner) throws Exception	{
		this.applyMoreDynmicData(partner);
		List<User> grogeny = partner.getProgeny();
		StringBuffer sb = new StringBuffer();

		List<Long> inviterList = new ArrayList<Long>();

		//没放入自己
		inviterList.add(partner.getUuid());
		sb.append(partner.getUuid()).append(',');
		if(grogeny != null && grogeny.size() > 0){			
			for(int i = 0; i < grogeny.size(); i++){
				inviterList.add(grogeny.get(i).getUuid());
				sb.append(grogeny.get(i).getUuid()).append(',');
			}
		}		
		if(logger.isDebugEnabled())logger.debug("当前parnter[" + partner.getUuid() + "]的子账户列表:" + sb.toString().replaceAll(",$", ""));

		
		long[] inviters = NumericUtils.longList2Array(inviterList);
		criteria.setInviters(inviters);

	}

	@Override
	public void correctUserAttributes(User user){
		Assert.notNull(user,"尝试检查的用户对象为空");
		Assert.isTrue(user.getUuid() > 0,"尝试检查的用户对象UUID为空");


		User _oldUser = partnerDao.select(user.getUuid());
		//将不允许自行修改的项目设置为旧的
		user.setOwnerId(_oldUser.getOwnerId());
		user.setUsername(_oldUser.getUsername());
		user.setCreateTime(_oldUser.getCreateTime());
		user.setInviter(_oldUser.getInviter());
		user.setUserTypeId(UserTypes.partner.getId());
		user.setLevel(_oldUser.getLevel());
		user.setCurrentStatus(_oldUser.getCurrentStatus());
	}
}

package com.maicard.security.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EisObject;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraValueAccess;
import com.maicard.views.JsonFilterView;

/**
 * 用户，后台系统用户、合作伙伴、前台网站用户都是用户类型
 * 用户对象只保存基本的数据
 * 其他所有需要的数据都由userData来保存
 * 
 * 
 * @author NetSnake
 * @date 2012-9-23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NeedJmsDataSyncP2P
@JsonInclude(Include.NON_EMPTY)
public class User  extends EisObject implements Cloneable,ExtraValueAccess{

	private static final long serialVersionUID = -7034636553562199385L;
	private long uuid;

	@JsonView({JsonFilterView.Partner.class})
	private int userTypeId;

	@JsonView({JsonFilterView.Partner.class})
	private int userExtraTypeId;
	private String username;
	private String userPassword;
	private int authType;
	private Date createTime;
	private Date lastLoginTimestamp;
	private String lastLoginIp;
	private String nickName;

	@JsonView({JsonFilterView.Partner.class})
	private long parentUuid;
	private long level;
	private long inviter;
	private int extraStatus;

	@JsonView({JsonFilterView.Partner.class})
	private long headUuid; //分权限帐号的总账号
	private String lockGlobalUniqueId;
	private String authKey;
	private String memory;
	private int gender;

	public User(){

	}

	public User(long ownerId) {
		this.ownerId = ownerId;
	}

	public User(int type, long uuid, long ownerId) {
		this.userTypeId = type;
		this.uuid = uuid;
		this.ownerId = ownerId;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	//非持久化属性
	private String ssoToken;
	//完成二次验证的时间
	private String secAuthTimestamp;

	private String email;

	private HashMap<String,UserData> userConfigMap;
	private List<Role> relatedRoleList;
	private List<Privilege> relatedPrivilegeList;
	private List<UserRelation> userRelationList;
	private UserLevelProject userLevelProject;
	private String inviterName;
	
	//所有子孙账户
	private List<User> progeny;
	
	//指定该用户是否已经设置了所有动态信息
	private int dynamicFlag = 0;
	
	//private Map<String,Serializable>cachedObject;


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final User other = (User) obj;
		if (uuid != other.uuid)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"userTypeId='" + userTypeId + "'" +
				"uuid=" + "'" + uuid + "'" + 
				")";
	}
	public String getInviterName() {
		return inviterName;
	}
	public void setInviterName(String inviterName) {
		this.inviterName = inviterName;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username != null && username.trim() != "")
			this.username = username.trim();
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		if(userPassword != null && userPassword.trim() != "")
			this.userPassword = userPassword;
	}
	public int getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}
	public String getSsoToken() {
		return ssoToken;
	}
	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}
	public int getAuthType() {
		return authType;
	}
	public void setAuthType(int authType) {
		this.authType = authType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastLoginTimestamp() {
		return lastLoginTimestamp;
	}

	public void setLastLoginTimestamp(Date lastLoginTimestamp) {
		this.lastLoginTimestamp = lastLoginTimestamp;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public long getParentUuid() {
		return parentUuid;
	}
	public void setParentUuid(long parentUuid) {
		this.parentUuid = parentUuid;
	}

	public long getLevel() {
		return level;
	}
	public void setLevel(long level) {
		this.level = level;
	}
	public HashMap<String, UserData> getUserConfigMap() {
		return userConfigMap;
	}
	public void setUserConfigMap(HashMap<String, UserData> userConfigMap) {
		this.userConfigMap = userConfigMap;
	}
	public List<Role> getRelatedRoleList() {
		return relatedRoleList;
	}
	public void setRelatedRoleList(List<Role> relatedRoleList) {
		this.relatedRoleList = relatedRoleList;
	}
	public List<Privilege> getRelatedPrivilegeList() {
		return relatedPrivilegeList;
	}
	public void setRelatedPrivilegeList(List<Privilege> relatedPrivilegeList) {
		this.relatedPrivilegeList = relatedPrivilegeList;
	}
	public List<UserRelation> getUserRelationList() {
		return userRelationList;
	}
	public void setUserRelationList(List<UserRelation> userRelationList) {
		this.userRelationList = userRelationList;
	}
	public UserLevelProject getUserLevelProject() {
		return userLevelProject;
	}
	public void setUserLevelProject(UserLevelProject userLevelProject) {
		this.userLevelProject = userLevelProject;
	}
	public long getInviter() {
		return inviter;
	}
	public void setInviter(long inviter) {
		this.inviter = inviter;
	}
	public int getUserExtraTypeId() {
		return userExtraTypeId;
	}
	public void setUserExtraTypeId(int userExtraTypeId) {
		this.userExtraTypeId = userExtraTypeId;
	}
	public int getExtraStatus() {
		return extraStatus;
	}
	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}
	public long getHeadUuid() {
		return headUuid;
	}
	public void setHeadUuid(long headUuid) {
		this.headUuid = headUuid;
	}
	@Override
	public User clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (User)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}

	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getSecAuthTimestamp() {
		return secAuthTimestamp;
	}

	public void setSecAuthTimestamp(String secAuthTimestamp) {
		this.secAuthTimestamp = secAuthTimestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getExtraValue(String dataCode) {
		if(this.userConfigMap != null &&  this.userConfigMap.containsKey(dataCode) &&  this.userConfigMap.get(dataCode).getDataValue() != null){
			return this.userConfigMap.get(dataCode).getDataValue().trim();
		}
		return null;
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {

		if(this.userConfigMap != null &&  this.userConfigMap.containsKey(dataCode) &&  this.userConfigMap.get(dataCode).getDataValue() != null && this.userConfigMap.get(dataCode).getDataValue().trim().equalsIgnoreCase("true") ){
			return true;
		}
		return false;
	}

	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.userConfigMap != null &&  this.userConfigMap.containsKey(dataCode) &&  NumericUtils.isNumeric(this.userConfigMap.get(dataCode).getDataValue())){
			return Long.parseLong(this.userConfigMap.get(dataCode).getDataValue().trim());
		}

		return 0;
	}

	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.userConfigMap != null &&  this.userConfigMap.containsKey(dataCode) &&  NumericUtils.isNumeric(this.userConfigMap.get(dataCode).getDataValue())){
			return Float.parseFloat(this.userConfigMap.get(dataCode).getDataValue().trim());
		}

		return 0;
	}

	@Override
	public void setExtraValue(String dataCode, String dataValue) {
		if(this.userConfigMap == null){
			this.userConfigMap = new HashMap<String, UserData>();
		}
		this.userConfigMap.put(dataCode, new UserData(this.uuid, dataCode,dataValue));

	}

	public List<User> getProgeny() {
		return progeny;
	}

	public void setProgeny(List<User> progeny) {
		this.progeny = progeny;
	}

	public int getDynamicFlag() {
		return dynamicFlag;
	}

	public void setDynamicFlag(int dynamicFlag) {
		this.dynamicFlag = dynamicFlag;
	}

	/*public Map<String, Serializable> getCachedObject() {
		if(cachedObject == null){
			cachedObject = new HashMap<String,Serializable>();
		}
		return cachedObject;
	}

	public void setCachedObject(Map<String, Serializable> cachedObject) {
		this.cachedObject = cachedObject;
	}
*/
}

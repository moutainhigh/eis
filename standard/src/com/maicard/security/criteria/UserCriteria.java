package com.maicard.security.criteria;

import java.util.Date;
import java.util.List;

import com.maicard.common.base.InviterSupportCriteria;
import com.maicard.security.domain.User;

public class UserCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;


	public static final int GENDER_BOY  = 1;	//男性
	public static final int GENDER_GIRL = 2;	//女性
	public static final int GENDER_OTHER = 3;	//不知道、变性人

	private long uuid;
	private String username;
	private String nickName;
	private String userPassword;
	private long parentUuid;
	private long headUuid;
	private int level;
	private int userTypeId;
	private int userExtraTypeId;
	private String searchCondition;
	private String likeUserName;
	private long[] uuids;		//结算账户UUID
	private String[]usernames;
	private int[] extraStatus;
	private String lockGlobalUniqueId;
	private String authKey;
	private Date createTimeBegin;
	private Date createTimeEnd;
	
	private Date queryBeginTime;
	private Date queryEndTime;
	
	private boolean merchant;






	public long[] getUuids() {
		return uuids;
	}

	public void setUuids(long... uuids) {
		this.uuids = uuids;
	}

	/*
	 * 当parentUuid>0时，数据库应查询所有parentUuid in (parentUuid和subUserList中的用户)
	 * 当inviter>0时，数据库应查询所有inviter in (inviter和subUserList中的用户)
	 */
	private List<User> subUserList; 

	public UserCriteria() {
	}

	public UserCriteria(long ownerId) {
		this.ownerId = ownerId;
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
		if(username == null || username.trim().equals("")){
			return;
		}
		this.username = username;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public long getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(long parentUuid) {
		this.parentUuid = parentUuid;
	}


	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getLikeUserName() {
		return likeUserName;
	}

	public void setLikeUserName(String likeUserName) {
		this.likeUserName = likeUserName;
	}

	public int getUserExtraTypeId() {
		return userExtraTypeId;
	}

	public void setUserExtraTypeId(int userExtraTypeId) {
		this.userExtraTypeId = userExtraTypeId;
	}

	public List<User> getSubUserList() {
		return subUserList;
	}

	public void setSubUserList(List<User> subUserList) {
		this.subUserList = subUserList;
	}

	public String[] getUsernames() {
		return usernames;
	}

	public void setUsernames(String...usernames) {
		this.usernames = usernames;
	}

	public long getHeadUuid() {
		return headUuid;
	}

	public void setHeadUuid(long headUuid) {
		this.headUuid = headUuid;
	}

	public int[] getExtraStatus() {
		return extraStatus;
	}

	public void setExtraStatus(int... extraStatus) {
		this.extraStatus = extraStatus;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		if(nickName == null || nickName.trim().equals("")){
			return;
		}
		this.nickName = nickName;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Date getQueryBeginTime() {
		return queryBeginTime;
	}

	public void setQueryBeginTime(Date queryBeginTime) {
		this.queryBeginTime = queryBeginTime;
	}

	public Date getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(Date queryEndTime) {
		this.queryEndTime = queryEndTime;
	}

	public boolean isMerchant() {
		return merchant;
	}

	public void setMerchant(boolean merchant) {
		this.merchant = merchant;
	}



}

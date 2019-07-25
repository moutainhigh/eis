package com.maicard.money.domain;

import java.util.Date;

import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EisObject;

/**
 * 中奖记录
 *
 *
 * @author NetSnake
 * @date 2016年4月29日
 *
 */
@NeedJmsDataSyncP2P
public class Award extends EisObject{

	public Award(){

	}

	public Award(long ownerId){
		this.ownerId = ownerId;
	}

	public Award(long uuid, long ownerId) {
		this.uuid = uuid;
		this.ownerId = ownerId;
	}

	private static final long serialVersionUID = 1933642026231973486L;

	private long awardId;

	//继承自父类的objectType

	private long objectId;

	private String objectUnit;			//奖励对象的单位，如个、元、件

	private int awardCount;



	private String awardDesc;

	private long uuid;

	private Date createTime;

	private String memory;


	public long getAwardId() {
		return awardId;
	}

	public void setAwardId(long awardId) {
		this.awardId = awardId;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getAwardCount() {
		return awardCount;
	}

	public void setAwardCount(int awardCount) {
		this.awardCount = awardCount;
	}

	public String getAwardDesc() {
		return awardDesc;
	}

	public void setAwardDesc(String awardDesc) {
		this.awardDesc = awardDesc;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getObjectUnit() {
		return objectUnit;
	}

	public void setObjectUnit(String objectUnit) {
		this.objectUnit = objectUnit;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}





}

package com.maicard.common.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.views.JsonFilterView;

public class EisObject extends JmsObject implements Cloneable{

	protected static final long serialVersionUID = -6728894025835529890L;
   // @JsonView({JsonFilterView.Partner.class})
	protected long id = 0;

	protected long index = 0;
    
	protected String messageId;
	protected int currentStatus;

    @JsonView({JsonFilterView.Full.class})
	protected String tableName;
	
    @JsonView({JsonFilterView.Full.class})
	protected HashMap<String,String> operate;
    
    @JsonView({JsonFilterView.Partner.class})
   protected String updateMode;
    
    @JsonView({JsonFilterView.Partner.class})
	protected String objectType;

    @JsonView({JsonFilterView.Full.class})
    protected	long ownerId;		//平台ID
    
    @JsonView({JsonFilterView.Full.class})
    protected long version;

	public EisObject(){

	}

	public EisObject(int currentStatus){
		this.currentStatus = currentStatus;
	}

	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName == null ? null : tableName.trim();
	}
	public int getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	public HashMap<String, String> getOperate() {
		if(operate == null){
			operate = new HashMap<String,String>();
		}
		return operate;
	}
	public void setOperate(HashMap<String, String> operate) {
		this.operate = operate;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId == null ? null : messageId.trim();
	}
	
	public String getObjectType() {
		if(objectType == null) {
			objectType = StringUtils.uncapitalize(this.getClass().getSimpleName());
		}
		return objectType;
	}
	public void setObjectType(String objectType) {
		if(objectType != null && !objectType.trim().equals("")){
			this.objectType = objectType.trim();
		}
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	
	@Override
	public  EisObject clone(){
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return  (EisObject)in.readObject();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}
	
	public void setOperateValue(String code, String value){
		if(operate == null){
			operate = new HashMap<String,String>();
		}
		operate.put(code, value);
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public long incrVersion() {
		this.version++;
		return this.version;
	}

}

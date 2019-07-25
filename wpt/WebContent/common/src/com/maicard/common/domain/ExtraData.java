package com.maicard.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.views.JsonFilterView;

@NeedJmsDataSyncP2P
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraData extends DataDefine{

	private static final long serialVersionUID = 4430445966070174491L;
	
	@JsonView({JsonFilterView.Partner.class})
	private long extraDataId;
	
	private String dataValue;
		
	@JsonView({JsonFilterView.Partner.class})
	private long uuid;
	
	//扩展自父类的objectId和objectType
	
	public ExtraData(){
		
	}
	
	@Override
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(extraDataId=" + extraDataId + ",dataDefineId=" + dataDefineId + ",dataCode=" + dataCode + ",objectId=" + objectId + ",objectType=" + objectType + ",dataValue=" + dataValue + ")";
	}
	
	public ExtraData(String dataCode, String value){
		this.dataCode = dataCode;
		this.dataValue = value;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public long getExtraDataId() {
		return extraDataId;
	}

	public void setExtraDataId(long extraDataId) {
		this.extraDataId = extraDataId;
	}


	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	
	

}

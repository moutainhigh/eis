package com.maicard.common.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.views.JsonFilterView;

public class DataDefine extends EisObject {

	private static final long serialVersionUID = -3920207787605379611L;

	protected int dataDefineId = 0;

	protected String dataCode;	//数据代码
	
	@JsonView(JsonFilterView.Partner.class)
	protected String dataName; //显示名称

	@JsonView(JsonFilterView.Partner.class)
	protected String dataDescription;

	@JsonView(JsonFilterView.Partner.class)
	protected String dataType;	//数据类型，如String

	@JsonView(JsonFilterView.Partner.class)
	protected String displayWeight; //显示时的权重，数值越大越靠前

	@JsonView(JsonFilterView.Partner.class)
	protected String inputLevel;		//从哪个级别输入
	
	@JsonView(JsonFilterView.Partner.class)
	protected String displayLevel; //哪个级别可以显示

	@JsonView(JsonFilterView.Partner.class)
	protected String inputMethod;	//输入方式

	@JsonView(JsonFilterView.Partner.class)
	protected String objectType;

	@JsonView(JsonFilterView.Partner.class)
	protected long objectId = 0;

	@JsonView(JsonFilterView.Partner.class)
	protected long objectExtraId = 0;

	@JsonView(JsonFilterView.Partner.class)
	protected int ttl = 0; //配置存活时间，秒

	@JsonView(JsonFilterView.Partner.class)
	protected String validDataEnum;  //有效的范围
	
	@JsonView(JsonFilterView.Partner.class)
	protected String compareMode;	//比较模式，等于=、大于>、属于in等
	
	@JsonView(JsonFilterView.Partner.class)
	protected int flag;
	
	/**
	 * 当显示给外界键是用dataCode还是用dataName
	 */
	@JsonView(JsonFilterView.Partner.class)
	protected String displayMode;
		
	
	@JsonView(JsonFilterView.Partner.class)
	protected String defaultValue;

	
	@JsonView(JsonFilterView.Partner.class)
	protected boolean readonly;			//是否不可写入

	@JsonView(JsonFilterView.Partner.class)
	protected boolean hidden;				//是否不可显示

	@JsonView(JsonFilterView.Partner.class)
	protected boolean required;			//是否必须输入


	public DataDefine() {
	}

	

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		if(dataCode != null && !dataCode.trim().equals("")){
			this.dataCode = dataCode.trim();
		}
	}

	public String getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(String dataDescription) {
		if(dataDescription != null && !dataDescription.trim().equals("")){
			this.dataDescription = dataDescription.trim();
		}
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		if(dataType != null && !dataType.trim().equals(""))
			this.dataType = dataType.trim();
	}

	public String getValidDataEnum() {
		return validDataEnum;
	}

	public void setValidDataEnum(String validDataEnum) {
		if(validDataEnum != null && !validDataEnum.trim().equals(""))
		this.validDataEnum = validDataEnum.trim();
	}

	public String getInputMethod() {
		return inputMethod;
	}

	public void setInputMethod(String inputMethod) {
		if(inputMethod != null && !inputMethod.trim().equals(""))
		this.inputMethod = inputMethod.trim();
	}


	@Override
	public int hashCode() {
		final Integer prime = 31;
		Integer result = 1;
		result = (int)(prime * result + dataDefineId);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DataDefine other = (DataDefine) obj;
		if (dataDefineId != other.dataDefineId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"dataDefineId=" + "'" + dataDefineId + "'" + 
				"dataCode=" + "'" + dataCode + "'" + 
				")";
	}


	public String getInputLevel() {
		return inputLevel;
	}

	public void setInputLevel(String inputLevel) {
		if(inputLevel != null && !inputLevel.trim().equals(""))
			this.inputLevel = inputLevel.trim();
	}

	public String getDisplayWeight() {
		return displayWeight;
	}

	public void setDisplayWeight(String displayWeight) {
		if(displayWeight != null && !displayWeight.trim().equals(""))
			this.displayWeight = displayWeight.trim();
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		if(objectType != null && !objectType.trim().equals(""))
		this.objectType = objectType.trim();
	}


	public String getDisplayLevel() {
		if(displayLevel == null){
			return "";
		}
		return displayLevel;
	}

	public void setDisplayLevel(String displayLevel) {
		this.displayLevel = displayLevel;
	}

	public String getCompareMode() {
		return compareMode;
	}

	public void setCompareMode(String compareMode) {
		this.compareMode = compareMode;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}


	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}



	public int getDataDefineId() {
		return dataDefineId;
	}



	public void setDataDefineId(int dataDefineId) {
		this.dataDefineId = dataDefineId;
	}



	public long getObjectId() {
		return objectId;
	}



	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}



	public long getObjectExtraId() {
		return objectExtraId;
	}



	public void setObjectExtraId(long objectExtraId) {
		this.objectExtraId = objectExtraId;
	}



	public int getTtl() {
		return ttl;
	}



	public void setTtl(int ttl) {
		this.ttl = ttl;
	}



	public boolean isReadonly() {
		return readonly;
	}



	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}



	public boolean isHidden() {
		return hidden;
	}



	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}



	public boolean isRequired() {
		return required;
	}



	public void setRequired(boolean required) {
		this.required = required;
	}



	public String getDisplayMode() {
		return displayMode;
	}



	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}


}

package com.maicard.product.domain;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.maicard.common.domain.EVEisObject;

public class ProductGroup extends EVEisObject implements Cloneable{

	private static final long serialVersionUID = 2662416789505353264L;
	
	private long objectId;
	
	private int level;
	
	private long parentId;
	
	private String groupDesc;
	
	private String groupKey;
	
	private String groupValue;
	
	private String groupTarget;
	
	private List<ProductGroup> subProductGroupList;
	

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(String groupValue) {
		this.groupValue = groupValue;
	}

	public String getGroupTarget() {
		return groupTarget;
	}

	public void setGroupTarget(String groupTarget) {
		this.groupTarget = groupTarget;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<ProductGroup> getSubProductGroupList() {
		return subProductGroupList;
	}

	public void setSubProductGroupList(List<ProductGroup> subProductGroupList) {
		this.subProductGroupList = subProductGroupList;
	}

	@Override
	public  ProductGroup clone(){
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return  (ProductGroup)in.readObject();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	
	
	
	


}

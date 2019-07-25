package com.maicard.product.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.dao.ProductTypeDao;
import com.maicard.product.domain.ProductType;
import com.maicard.product.service.ProductTypeService;
import com.maicard.standard.ObjectType;

@Service
public class ProductTypeServiceImpl extends BaseService implements ProductTypeService {

	@Resource
	private ProductTypeDao productTypeDao;

	@Resource
	private DataDefineService dataDefineService;

	public int insert(ProductType productType) {
		if(productType == null){
			return 0;
		}
		if(productType.getProductTypeId() < 1){
			throw new RequiredAttributeIsNullException("找不到必须的属性productTypeId");
		}
		if(productTypeDao.insert(productType) != 1){
			throw new DataWriteErrorException("无法写入新的productType");
		}
		if(productType.getDataDefineMap() != null ){
			for(DataDefine productDataDefinePolicy : productType.getDataDefineMap().values()){
				productDataDefinePolicy.setObjectType(ObjectType.product.toString());
				productDataDefinePolicy.setObjectId(productType.getProductTypeId());
				dataDefineService.insert(productDataDefinePolicy);
			}
		}
		return 1;
	}

	public int update(ProductType productType) {
		int actualRowsAffected = 0;

		int productTypeId = productType.getProductTypeId();

		ProductType _oldProductType = productTypeDao.select(productTypeId);

		if (_oldProductType != null) {
			actualRowsAffected = productTypeDao.update(productType);

			DataDefineCriteria productDataDefinePolicyCriteria = new DataDefineCriteria();
			productDataDefinePolicyCriteria.setObjectType(ObjectType.product.toString());
			productDataDefinePolicyCriteria.setObjectId(productTypeId);
			dataDefineService.delete(productDataDefinePolicyCriteria);
			if(productType.getDataDefineMap() != null ){
				for(DataDefine productDataDefinePolicy : productType.getDataDefineMap().values()){
					productDataDefinePolicy.setObjectType(ObjectType.product.toString());
					productDataDefinePolicy.setObjectId(productType.getProductTypeId());
					dataDefineService.insert(productDataDefinePolicy);
				}
			}
		}
		return actualRowsAffected;
	}

	public int delete(int productTypeId) {
		int actualRowsAffected = 0;

		ProductType _oldProductType = productTypeDao.select(productTypeId);

		if (_oldProductType != null) {
			actualRowsAffected = productTypeDao.delete(productTypeId);
			DataDefineCriteria productDataDefinePolicyCriteria = new DataDefineCriteria();
			productDataDefinePolicyCriteria.setObjectType(ObjectType.product.toString());
			productDataDefinePolicyCriteria.setObjectId(productTypeId);
			dataDefineService.delete(productDataDefinePolicyCriteria);
		}

		return actualRowsAffected;
	}

	public ProductType select(int productTypeId) {
		ProductType productType =  productTypeDao.select(productTypeId);
		if(productType != null){
			afterFetch(productType);
		}
		return productType;
	}

	public List<ProductType> list(ProductTypeCriteria productTypeCriteria) {
		List<ProductType> productTypeList =  productTypeDao.list(productTypeCriteria);
		if(productTypeList != null){
			for(int i = 0; i < productTypeList.size(); i++){
				afterFetch(productTypeList.get(i));
				productTypeList.get(i).setIndex(i+1);
			}
		}
		return productTypeList;
	}

	public List<ProductType> listOnPage(ProductTypeCriteria productTypeCriteria) {
		List<ProductType> productTypeList =  productTypeDao.listOnPage(productTypeCriteria);
		if(productTypeList != null){
			for(int i = 0; i < productTypeList.size(); i++){
				afterFetch(productTypeList.get(i));
				productTypeList.get(i).setIndex(i+1);
			}
		}
		return productTypeList;	}

	@Override
	public HashMap<Integer, String> map(
			ProductTypeCriteria productTypeCriteria) {
		List<ProductType> productTypeList = list(productTypeCriteria);
		if(productTypeList == null){
			return null;
		}
		HashMap<Integer, String> productTypeMap = new HashMap<Integer, String>();
		for(ProductType productType : productTypeList){
			afterFetch(productType);
			productTypeMap.put(productType.getProductTypeId(), productType.getProductTypeName());
		}
		return productTypeMap;
	}

	@Override
	public int count(ProductTypeCriteria productTypeCriteria) {
		return productTypeDao.count(productTypeCriteria);
	}

	private void afterFetch(ProductType productType){
		if(productType == null){
			return;
		}
		if(productType.getProductTypeId() < 1){
			return;
		}
		productType.setId(productType.getProductTypeId());
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		dataDefineCriteria.setObjectId(productType.getProductTypeId());
		List<DataDefine> productDataDefinePolicyList = dataDefineService.list(dataDefineCriteria);
		if(productDataDefinePolicyList == null){
			return;
		}
		HashMap<String, DataDefine>productDataDefinePolicyMap = new HashMap<String, DataDefine>();
		for(DataDefine productDataDefinePolicy : productDataDefinePolicyList){
			if(productDataDefinePolicy != null){
				productDataDefinePolicyMap.put(productDataDefinePolicy.getDataCode(), productDataDefinePolicy);
			}
		}
		logger.debug("产品类型[" + productType.getProductTypeName() + "]有" + productDataDefinePolicyMap.size() + "个数据定义");
		productType.setDataDefineMap(productDataDefinePolicyMap);
	}



}

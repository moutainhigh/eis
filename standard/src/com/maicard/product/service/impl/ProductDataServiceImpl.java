package com.maicard.product.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.exception.DataInvalidException;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ProductDataDao;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ProductDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.ObjectType;

@Service
public class ProductDataServiceImpl extends BaseService implements ProductDataService {

	@Resource
	private ProductDataDao productDataDao;

	@Resource
	private DataDefineService dataDefineService;


	public int insert(ProductData productData) {
		if(productData == null){
			throw new RequiredObjectIsNullException("将要插入的productData为空")	;
		}
		if(productData.getProductId() == 0){
			throw new RequiredAttributeIsNullException("将要插入的productData，其productId为0");
		}
		if(productData.getDataDefineId() == 0 &&productData.getDataCode() == null ){
			throw new RequiredAttributeIsNullException("将要插入的productData，其dataDefineId和dataCode都为空");		
		}
		if(productData.getDataDefineId() == 0 ){
			throw new RequiredAttributeIsNullException("数据[" + productData.getDataCode() + "]的dataDefineId为空");
		}

		if(logger.isDebugEnabled()){
			logger.debug("插入新的Product数据[" + productData + "]" );
		}
		if(productData.getDataValue() == null || productData.getDataValue().equals("")){
			return 0;
		}
		return productDataDao.insert(productData);
	}

	public int update(ProductData productData) {
		if(productData == null){
			throw new RequiredObjectIsNullException("将要更新的productData为空");
		}
		if(productData.getProductDataId() < 1){
			throw new RequiredAttributeIsNullException("将要更新的productData其productDataId为空");
		}
		int actualRowsAffected = 0;

		int productDataId = productData.getProductDataId();

		ProductData _oldProductData = productDataDao.select(productDataId);

		if (_oldProductData != null) {
			actualRowsAffected = productDataDao.update(productData);
		} else {
			logger.error("找不到要更新的ProductData:" + productData);
		}

		return actualRowsAffected;
	}

	public int delete(int productDataId) {
		int actualRowsAffected = 0;

		ProductData _oldProductData = productDataDao.select(productDataId);

		if (_oldProductData != null) {
			actualRowsAffected = productDataDao.delete(productDataId);
		}

		return actualRowsAffected;
	}

	public ProductData select(int productDataId) {
		return productDataDao.select(productDataId);
	}

	/*cache模式
	 public List<ProductData> list(ProductDataCriteria productDataCriteria) {
		//return productDataDao.list(productDataCriteria);

		List<Integer> idList = productDataDao.listPk(productDataCriteria);
		if(idList == null || idList.size() < 1){
			return null;
		}
		List<ProductData> productDataList = new ArrayList<ProductData>();
		for(int id : idList){
			ProductData pd = productDataDao.select(id);
			if(pd != null ){
				productDataList.add(pd);
			}			
		}
		idList = null;
		return productDataList;
	}*/
	//非cache模式
	@Override
	public List<ProductData> list(ProductDataCriteria productDataCriteria) {
		return productDataDao.list(productDataCriteria);		
	}

	/* cache模式
	 public List<ProductData> listOnPage(ProductDataCriteria productDataCriteria) {
		//return productDataDao.listOnPage(productDataCriteria);
		List<Integer> idList = productDataDao.listPkOnPage(productDataCriteria);
		if(idList == null || idList.size() < 1){
			return null;
		}
		List<ProductData> productDataList = new ArrayList<ProductData>();
		for(int id : idList){
			ProductData pd = productDataDao.select(id);
			if(pd != null ){
				productDataList.add(pd);
			}			
		}
		idList = null;
		return productDataList;
	}*/

	@Override
	public List<ProductData> listOnPage(ProductDataCriteria productDataCriteria) {
		return productDataDao.listOnPage(productDataCriteria);

	}


	@Override
	public HashMap<String, ProductData> map(
			ProductDataCriteria productDataCriteria) {
		List<ProductData> productDataList = list(productDataCriteria);
		if(productDataList == null){
			return new HashMap<String, ProductData>();
		}
		HashMap<String, ProductData> productDataMap = new HashMap<String, ProductData>();
		for(ProductData productData : productDataList){
			format(productData);
			productDataMap.put(productData.getDataCode(), productData);
		}
		return productDataMap;
	}

	private void format(ProductData productData){
		if(productData == null){
			return;
		}
		if(productData.getFlag() == 1 && productData.getInputMethod().equals("select")){
			//将select的数据转换为html
			try{
				String[] src = productData.getDataValue().split(",");

				if(src == null || src.length < 1){
					return ;
				}
				StringBuffer html = new StringBuffer();
				for(String pair : src){
					String[] dv = pair.split("=");
					if(dv == null || dv.length < 2){
						continue;
					}
					html.append("<option value=\"" + dv[0] + "\">" + dv[1] + "</option>");					
				}
				productData.setFormattedDataValue(html.toString());
			}catch(Exception e){}

		} else {
			productData.setFormattedDataValue(productData.getDataValue());
		}
	}

	@Override
	public int delete(ProductDataCriteria productDataCriteria) {
		List<Integer> idList = productDataDao.listPk(productDataCriteria);
		if(idList == null || idList.size() < 1){
			return 0;
		}
		int rs = 0;
		for(int id : idList){
			productDataDao.delete(id);		
			rs++;
		}
		idList = null;
		return rs;

	}

	@Override
	public int put(ProductData productData) {
		if(productData == null){
			throw new RequiredObjectIsNullException("productData为空");
		}
		if(productData.getProductId() < 1){
			logger.error("没找到productData的productId");
			throw new RequiredAttributeIsNullException("productData中没有productId");
		}
		logger.debug("尝试put productData[" + productData.getDataCode() + "/" + productData.getDataDefineId() + ":" + productData.getDataValue() + "]");
		if(productData.getProductDataId() > 0){//已存在知道的productDataId，必定是更新模式
			ProductData _oldProductData = select(productData.getProductDataId());
			if(_oldProductData != null){
				if(_oldProductData.getProductId() == productData.getProductId()){
					logger.debug("在本地找到了与put数据[productId=" + _oldProductData.getProductId() + ",productData=" + _oldProductData.getProductDataId() + "]一样的旧数据，更新");
					return update(productData);
				}
				logger.debug("在本地找到了与put数据[productId=" + productData.getProductId() + ",productData=" + productData.getProductDataId() + "]的productDataId一致但是productId不一致的旧数据,其productId=" + _oldProductData.getProductId()  + " ，强行插入新数据");
				productData.setProductDataId(0);
				return insert(productData);
			}
		}
		//该ProductData没有主键，尝试查找productCode、dataDefineId和productId对应的旧数据
		ProductDataCriteria productDataCriteria = new ProductDataCriteria();
		productDataCriteria.setDataCode(productData.getDataCode());
		productDataCriteria.setDataDefineId(productData.getDataDefineId());
		productDataCriteria.setProductId(productData.getProductId());
		List<Integer> _oldProductDataList = productDataDao.listPk(productDataCriteria);
		if(_oldProductDataList == null || _oldProductDataList.size() < 1){
			logger.debug("没有找到dataCode=" + productData.getDataCode() + ", dataDefineId=" + productData.getDataDefineId() + ", productId=" + productData.getProductId() + "]的旧数据，将插入新数据");
			return insert(productData);
		}
		if(_oldProductDataList.size() != 1){
			logger.warn("根据条件dataCode=" + productData.getDataCode() + ", dataDefineId=" + productData.getDataDefineId() + ", productId=" + productData.getProductId() + "]找到的数据不止一条");
		}
		int oldProductDataId = _oldProductDataList.get(0).intValue();
		logger.debug("找到了productDataId=" + oldProductDataId + ",dataCode=" + productData.getDataCode() + ", dataDefineId=" + productData.getDataDefineId() + ", productId=" + productData.getProductId() + "]的旧数据[" + _oldProductDataList.size() + "]条，将更新数据");

		/*
		 * 有旧数据，如果该数据类型是file，同时数据中的内容为空，说明未做更新也不需要删除
		 */
		/*try{
			if(_oldProductDataList.get(0).getInputMethod().equals("file") && (productData.getDataValue() == null || productData.getDataValue().equals(""))){
				logger.debug("产品[" + productData.getProductId() + "]的文件类型数据[" + productData.getDataCode() + "不需要进行更新");
				return 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}*/
		//FIXME 
		productData.setProductDataId(oldProductDataId);
		logger.debug("将要更新的数据ID设置为:" + productData.getProductDataId());
		int rs = update(productData);
		logger.debug("旧数据更新结果:" + rs);
		return rs;


	}

	@Override
	public List<Integer> listProductIdByCriteria(
			ProductDataCriteria productDataCriteria) {
		try{
			return productDataDao.listProductIdByCriteria(productDataCriteria);
		}catch(Exception e){
			logger.error("在获取数据时发生异常:" + e.getMessage());
		}
		return null;
	}

	@Override
	public void sync(Product product) {
		/*Assert.isTrue(product.getProductId() > 0,"条件删除产品数据的产品ID不能为0");
		ProductDataCriteria productDataCriteria = new ProductDataCriteria();
		productDataCriteria.setProductId(product.getProductId());
		delete(productDataCriteria);*/

		if(product.getProductDataMap() == null || product.getProductDataMap().size() < 1){
			logger.debug("产品[" + product.getProductCode() + "没有productDataMap");
		}
		for(ProductData productData : product.getProductDataMap().values()){
			if(productData.getCurrentStatus() == BasicStatus.dynamic.getId()){
				logger.debug("尝试插入的新的产品数据[" + productData + "]状态为动态，忽略");
				continue;
			}
			if(StringUtils.isBlank(productData.getDataValue())){
				logger.debug("尝试插入的新的产品数据[" + productData + "]值为空，忽略" );
				continue;
			}
			if (productData.getDataValue().equals("DELETE")) {
				productData.setDataValue("");
			}
			logger.debug("尝试插入的新的产品数据[" + productData + "]值为 ： " + productData.getDataValue());
			if(productData.getProductId() < 1){
				productData.setProductId(product.getProductId());
			}
			if(productData.getDataDefineId() <= 0 ){
				if(productData.getDataCode() != null){
					DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
					dataDefineCriteria.setDataCode(productData.getDataCode());
					dataDefineCriteria.setObjectType(ObjectType.product.name());
					dataDefineCriteria.setObjectId(product.getProductTypeId());
					DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
					if(dataDefine == null){
						logger.error("找不到产品扩展数据定义[objectType=" + dataDefineCriteria.getObjectType() + ",objectId=" + dataDefineCriteria.getObjectId() + ",dataCode=" + dataDefineCriteria.getDataCode() + "]");
						continue;
					}
					productData.setDataDefineId(dataDefine.getDataDefineId());
				} else {
					throw new DataInvalidException("尝试新增的productData数据[" + productData + "]既没有dataDefineId也没有dataCode");
				}
			}
			put(productData);
			/*
				productData.setProductId(product.getProductId());
				logger.debug("尝试插入新的产品扩展数据:" +productData );
				insert(productData);*/
		}

	}

}

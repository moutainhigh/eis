package com.maicard.product.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ItemDataDao;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ItemDataService;

@Service
public class ItemDataServiceImpl extends BaseService implements ItemDataService {

	@Resource
	private ItemDataDao itemDataDao;
	@Resource
	private DataDefineService dataDefineService;

	@IgnoreJmsDataSync
	public int insert(ProductData productData) throws Exception {
		if(productData.getDataDefineId() < 1){
			if(productData.getDataCode() == null){
				logger.error("尝试插入的itemData[" + productData + "]既没有dataDefineId，也没有dataCode.");
				return 0;
			}
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setObjectType(productData.getObjectType());
			dataDefineCriteria.setDataCode(productData.getDataCode());
			dataDefineCriteria.setObjectId(productData.getObjectId());
			DataDefine dd = dataDefineService.select(dataDefineCriteria);
			if(dd == null){
				logger.error("找不到数据定义[objectType=" + productData.getObjectType() + ",dataCode=" + productData.getDataCode() + ", objectTypeId=" + productData.getObjectId() + "]或不唯一");
				return 0;
			}
			productData.setDataDefineId(dd.getDataDefineId());

		}
		if(StringUtils.isBlank(productData.getDataValue())){
			logger.warn("尝试插入的ProductData其值为空，停止插入");
			return 0;
		} else {
			productData.setDataValue(productData.getDataValue().trim());
		}
		int rs = itemDataDao.insert(productData);
		if(rs == 1 && productData.getBinData() != null && productData.getBinData().length > 0 && productData.getDataValue() != null){
			logger.debug("尝试保存productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]");
			File file = new File(productData.getDataValue());
			/*if(!file.exists()){
				String path = file.getAbsolutePath();
				logger.debug("productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]目标目录[" + path + "]不存在，创建");
				File dir = new File(path);
				if(!dir.exists()){
					if(!dir.mkdirs()){
						logger.error("无法创建目录:" + path);
					} 
				}

			}*/
			logger.info("写入productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]");
			try{
				FileUtils.writeByteArrayToFile(file, productData.getBinData());
			}catch(IOException e){
				logger.error("无法写入productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]:" + e.getMessage());
				e.printStackTrace();
			}
		}		
		return rs;
	}

	@IgnoreJmsDataSync
	public int update(ProductData productData) throws Exception {
		int actualRowsAffected = 0;
		boolean useInsert =false;
		int productDataId = productData.getProductDataId();
		if(productDataId < 1){
			logger.debug("尝试更新的ProductData对象没有主键，准备使用insert模式");
			useInsert = true;
		}
		if(!useInsert){
			ProductDataCriteria productDataCriteria = new ProductDataCriteria();
			productDataCriteria.setProductDataId(productDataId);
			productDataCriteria.setTableName(productData.getTableName());
			int existCount = itemDataDao.count(productDataCriteria);

			logger.debug("在数据库中查找准备更新的ProductData[" + productDataId + "],数量:" + existCount);
			if (existCount > 0) {
				actualRowsAffected = itemDataDao.update(productData);
				logger.debug("当前使用更新模式更新数据:" + productData + ",结果:" + actualRowsAffected);
			}else {
				useInsert = true;
			}
		}
		if(useInsert){
			actualRowsAffected = insert(productData);
			logger.debug("当前使用插入模式更新数据:" + productData + ",结果:" + actualRowsAffected);
		} 
		if(actualRowsAffected == 1 && productData.getBinData() != null && productData.getBinData().length > 0 && productData.getDataValue() != null){
			logger.debug("尝试保存productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]");
			File file = new File(productData.getDataValue());
			if(!file.exists()){
				String path = file.getAbsolutePath();
				File dir = new File(path);
				if(!dir.exists()){
					if(!dir.mkdirs()){
						logger.error("无法创建目录:" + path);
					} 
				}

			}
			logger.info("写入productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]");
			try{
				FileUtils.writeByteArrayToFile(file, productData.getBinData());
			}catch(IOException e){
				logger.error("无法写入productData[" + productData.getProductDataId() + "]的二进制数据[" + productData.getDataValue() + "]:" + e.getMessage());
				e.printStackTrace();
			}
		}		
		return actualRowsAffected;
	}

	
	@IgnoreJmsDataSync
	public int delete(int productDataId) throws Exception {
		int actualRowsAffected = 0;

		ProductData _oldProductData = itemDataDao.select(productDataId);

		if (_oldProductData != null) {
			actualRowsAffected = itemDataDao.delete(productDataId);
		}

		return actualRowsAffected;
	}

	public ProductData select(int productDataId) throws Exception {
		return itemDataDao.select(productDataId);
	}

	public List<ProductData> list(ProductDataCriteria productDataCriteria) throws Exception{
		productDataCriteria.setTableName("item_data");
		List<ProductData> list1=itemDataDao.list(productDataCriteria);
		productDataCriteria.setTableName("item_data_history");
		List<ProductData> list2=itemDataDao.list(productDataCriteria);
		List<ProductData> list = list1;
		list.addAll(list2);
		return list;

	}

	public List<ProductData> listOnPage(ProductDataCriteria productDataCriteria) throws Exception{
		return itemDataDao.listOnPage(productDataCriteria);
	}



	@Override	
	public List<Long> listBy(ProductDataCriteria productDataCriteria) throws Exception{
		productDataCriteria.setTableName("item_data");
		List<Long> list1=itemDataDao.listBy(productDataCriteria);
		productDataCriteria.setTableName("item_data_history");
		List<Long> list2=itemDataDao.listBy(productDataCriteria);
		List<Long> list = list1;
		list.addAll(list2);
		return list;
	}

	@Override
	public Map<String, ProductData> map (
			ProductDataCriteria productDataCriteria) throws Exception{

		return itemDataDao.map(productDataCriteria);
	}

	@SuppressWarnings("unused")
	private void format(ProductData productData){
		if(productData == null){
			return;
		}
		logger.debug("格式化商品数据[" + productData.getDataCode() + "]");
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

		}
		/*if(productData.getForeignProductDataValue() == null){
			productData.setForeignProductDataValue("");
		}*/
	}


	@Override
	public int copyToHistory(ProductDataCriteria itemDataCriteria) {
		return itemDataDao.copyToHistory(itemDataCriteria);

	}


	@Override
	@IgnoreJmsDataSync
	public int delete(ProductDataCriteria itemDataCriteria) throws Exception{
		Assert.notNull(itemDataCriteria);

		return itemDataDao.delete(itemDataCriteria);
	}






}

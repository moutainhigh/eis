package com.maicard.product.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.TablePartitionUtils;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ItemDataDao;
import com.maicard.product.domain.ProductData;
import com.maicard.standard.TablePartitionPolicy;

@Repository
public class ItemDataDaoImpl extends BaseDao implements ItemDataDao {


	@Resource
	private GlobalOrderIdService globalOrderIdService;


	public int insert(ProductData productData) throws Exception {
		if(StringUtils.isBlank(productData.getTableName())){
			productData.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX);
		} else {
			//不是向item_data中新增扩展数据
			if(productData.getProductDataId() < 1){
				//插入一个新的空数据到item_data以获取一个自增ID
				ProductData p2 = productData.clone();
				p2.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX);
				int rs = getSqlSessionTemplate().insert("com.maicard.product.sql.ItemData.insert", p2);
				if(rs != 1){
					logger.error("无法向item_data中插入新的自增ID数据,返回:" + rs);
					return rs;
				}
				int productDataId = p2.getProductDataId();
				delete(productDataId);
				productData.setProductDataId(productDataId);
			}

		}
		return getSqlSessionTemplate().insert("com.maicard.product.sql.ItemData.insert", productData);
	}

	public int update(ProductData productData) throws Exception {
		if(StringUtils.isBlank(productData.getTableName())){
			productData.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX);
		}
		return getSqlSessionTemplate().update("com.maicard.product.sql.ItemData.update", productData);
	}



	public int delete(int productDataId) throws Exception {
		ProductDataCriteria itemDataCriteria = new ProductDataCriteria();
		itemDataCriteria.setProductDataId(productDataId);
		return getSqlSessionTemplate().delete("com.maicard.product.sql.ItemData.delete", itemDataCriteria);
	}

	public ProductData select(int productDataId) throws Exception {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.ItemData.select", new Integer(productDataId));
	}

	public List<ProductData> list(ProductDataCriteria productDataCriteria) throws Exception {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");
		getTableName(productDataCriteria);
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ItemData.list", productDataCriteria);
	}

	@Override
	public Map<String, ProductData> map(ProductDataCriteria productDataCriteria) throws Exception {
		//未使用mybatis的selectMap，因为有可能会返回key为null的情况，造成json无法输出，NetSnake,2016-06-25.
		List<ProductData> list = list(productDataCriteria);
		if(list == null || list.size() < 1){
			return null;
		}
		Map<String, ProductData> map = new HashMap<String,ProductData>();
		for(ProductData pd : list){
			if(StringUtils.isBlank(pd.getDataCode())){
				logger.warn("扩展数据[" + pd.getProductDataId() + "]没有对应的dataCode");
				continue;
			}
			map.put(pd.getDataCode(), pd);
		}
		return map;
	}

	public List<Long> listBy(ProductDataCriteria productDataCriteria) throws Exception {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");
		getTableName(productDataCriteria);
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ItemData.listBy", productDataCriteria);
	}

	public List<ProductData> listOnPage(ProductDataCriteria productDataCriteria) throws Exception {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");
		Assert.notNull(productDataCriteria.getPaging(), "paging must not be null");

		int totalResults = count(productDataCriteria);
		Paging paging = productDataCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ItemData.list", productDataCriteria, rowBounds);
	}

	public int count(ProductDataCriteria productDataCriteria) throws Exception {
		Assert.notNull(productDataCriteria, "productDataCriteria must not be null");

		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.ItemData.count", productDataCriteria);
	}

	@Override
	public void deleteByProductId(int productId) {		
		getSqlSessionTemplate().delete("com.maicard.product.sql.ItemData.deleteByProductId", new Integer(productId));

	}

	@Override
	public int copyToHistory(ProductDataCriteria itemDataCriteria) {
		if(itemDataCriteria == null){
			return -1;
		}

		getTableName(itemDataCriteria);
		if(itemDataCriteria.getTableName() == null || itemDataCriteria.getTableName().equals(ItemCriteria.ITEM_DATA_TABLE_PREFIX)){
			logger.error("清理交易附加数据时错误的表名[" + itemDataCriteria.getTableName() + "]");
			return -1;
		}
		return getSqlSessionTemplate().update("com.maicard.product.sql.ItemData.copyToHistory",itemDataCriteria);
	}


	private void getTableName(ProductDataCriteria itemDataCriteria) {
		if(itemDataCriteria != null && itemDataCriteria.getTableName() != null){
			return;
		}
		if(TablePartitionPolicy.itemHistory.toString().equals("MM")){
			if(itemDataCriteria.getTransactionId() != null){
				logger.debug("查询条件中包含了transcationId=" + itemDataCriteria.getTransactionId() + "，使用该ID判断表名");

				itemDataCriteria.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX + TablePartitionUtils.getTableMonth(itemDataCriteria.getTransactionId()));
				return;
			}
			if(itemDataCriteria.getBeginTime() != null){
				itemDataCriteria.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX  + "_" + itemDataCriteria.getBeginTime().substring(4,6));	
			} else if(itemDataCriteria.getEndTime() != null){
				itemDataCriteria.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX + "_" + itemDataCriteria.getEndTime().substring(4,6));	
			} else {
				itemDataCriteria.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX);			
			}
		} else {
			itemDataCriteria.setTableName(ItemCriteria.ITEM_DATA_TABLE_PREFIX);
		}		
	}

	@Override
	public int delete(ProductDataCriteria itemDataCriteria)
			throws Exception {
		Assert.notNull(itemDataCriteria, "itemDataCriteria must not be null");
		if(itemDataCriteria.getProductDataId() < 1 && itemDataCriteria.getProductDataIds() == null && itemDataCriteria.getProductId() < 0){
			logger.error("无法按条件删除ItemData，必须的条件为空");
			throw new Exception("无法按条件删除ItemData，必须的条件为空");
		}
		getTableName(itemDataCriteria);
		return getSqlSessionTemplate().delete("com.maicard.product.sql.ItemData.delete", itemDataCriteria);
	}


}

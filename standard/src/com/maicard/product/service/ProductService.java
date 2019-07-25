package com.maicard.product.service;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;

import com.maicard.common.domain.Column;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Product;
import com.maicard.site.domain.Document;
import com.maicard.standard.CommonStandard;

public interface ProductService {

	int insert(Product product);

	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'Product#' + #product.productId")
	int update(Product product);

	int delete(long productId);
	
	Product select(long productId);
	
	List<Product> list(ProductCriteria productCriteria);

	List<Product> listOnPage(ProductCriteria productCriteria);		
	
	int count(ProductCriteria productCriteria);

	@Deprecated
	Product select(String productCode);
	
	Product select(String productCode, long ownerId);

	
	int put(Product product);
	
	void sync(List<Product>productList);


	void flushProductAmountCache();

	int forceDeleteAllAndRelation(long productId);

	List<Long> listPk(ProductCriteria productCriteria);

	void updateDynamicPrice(long productId, float buyMoney);

	int generateProductDocumentData(Product product, Document document);

	int updateNoNull(Product product);

	int getAmount(long productId);

	Set<Column> getDisplayColumns(int productTypeId, long ownerId);

	int getDistributedAmount(long productId);

	/**
	 * 如果是forceWrite，则直接写入product.availableAmount
	 * 否则写入+offset
	 * @param product
	 * @param offset
	 * @param forceWrite
	 * @return
	 */
	long writeAmount(Product product, int offset, boolean forceWrite);

	/**
	 * 根据产品获取其对应发布的文档
	 * @param product
	 * @return
	 * @throws Exception 
	 */
	Document getRefDocument(Product product) throws Exception;

	/**
	 * 根据文档来查找对应的产品
	 * @param document
	 * @return
	 */
	Product getProductByDocument(Document document);

	long getMaxId(ProductCriteria productCriteria);


	/*
	 * 排序
	 */
	//void sort(List<Product> productList, String sortField, String sortOrder);




	


}

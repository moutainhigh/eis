package com.maicard.product.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.annotation.DisplayColumn;
import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.Column;
import com.maicard.common.domain.ColumnWeightComparator;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.MessageService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ProductDao;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ProductDataService;
import com.maicard.product.service.ProductExtraService;
import com.maicard.product.service.ProductService;
import com.maicard.product.utils.DataUtils;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.criteria.DocumentTypeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.service.DocumentDataService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DisplayLevel;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ObjectType;

@Service
public class ProductServiceImpl extends BaseService implements ProductService{

	@Resource
	private ProductDao productDao;
	@Resource
	private CacheService cacheService;

	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private DocumentService documentService;
	@Resource
	private DocumentTypeService documentTypeService;
	@Resource
	private DocumentDataService documentDataService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private MessageService messageService;
	@Resource
	private ProductDataService productDataService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;	
	@Resource
	private ProductExtraService productExtraService;


	private static final String cacheName = CommonStandard.cacheNameProduct;

	private String EXTRA_DATA_URL_PREFIX;


	@PostConstruct
	public void init(){
		EXTRA_DATA_URL_PREFIX = configService.getValue(DataName.EXTRA_DATA_URL_PREFIX.toString(),0);
		if(StringUtils.isBlank(EXTRA_DATA_URL_PREFIX)){
			EXTRA_DATA_URL_PREFIX = "/static";
		}
		logger.debug("附加数据下载URL前缀是:" + EXTRA_DATA_URL_PREFIX);
	}



	public int insert(Product product) {
		if(product.getCreateTime() == null){
			product.setCreateTime(new Date());
		}
		if(product.getInitCount() < 1){
			product.setInitCount(product.getAvailableCount());
		}
		int rs = productDao.insert(product);
		if(rs != 1){
			logger.error("无法插入产品，返回:" + rs);
			return rs;
		}

		productDataService.sync(product);

		/*ProductDataCriteria  documentDataCriteria = new ProductDataCriteria();
		documentDataCriteria.setProductId(product.getProductId());
		productDataService.delete(documentDataCriteria);

		//然后插入对应的自定义数据
		if(product.getProductDataMap() != null){
			for(ProductData pd : product.getProductDataMap().values()){
				if(StringUtils.isBlank(pd.getDataValue())){
					logger.info("忽略空白的产品附加数据[" + pd.getDataCode() + "/" + pd.getDataDefineId() + "]，数据内容:[" + pd.getDataValue() + "]");
					continue;
				}
				pd.setProductId(product.getProductId());
				pd.setProductDataId(0);
				pd.setCurrentStatus(BasicStatus.normal.getId());
				logger.debug("尝试插入附加产品数据[" + pd.getDataCode() + "/" + pd.getDataDefineId() + "]，数据内容:[" + pd.getDataValue() + "]");
				if(pd.getDataDefineId() <= 0 && pd.getDataCode() != null){
					DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
					dataDefineCriteria.setDataCode(pd.getDataCode());
					dataDefineCriteria.setObjectType(ObjectType.product.name());
					dataDefineCriteria.setObjectId(product.getProductTypeId());
					DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
					if(dataDefine == null){
						logger.error("找不到产品附加数据定义[objectType=" + dataDefineCriteria.getObjectType() + ",objectId=" + dataDefineCriteria.getObjectId() + ",dataCode=" + dataDefineCriteria.getDataCode() + "]");
						continue;
					}
					pd.setDataDefineId(dataDefine.getDataDefineId());
				}
				productDataService.insert(pd);
			}
		} else {
			logger.warn("新增的产品不包含自定义数据[productId=" + product.getProductId() + "]");
		}*/
		return rs;
	}

	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'Product#' + #product.productId")
	public int update(Product product) {
		if(product == null){
			throw new RequiredObjectIsNullException("将要更新的product为空");
		}
		if(product.getProductId() < 1){
			throw new RequiredAttributeIsNullException("将要更新的product其productId为空");
		}
		int actualRowsAffected = 0;

		int productId = product.getProductId();

		Product _oldProduct = productDao.select(productId);

		if (_oldProduct == null) {
			logger.warn("尝试更新产品，但找不到对应的旧产品");
			return 0;
		}
		product.setCreateTime(_oldProduct.getCreateTime());
		if(logger.isDebugEnabled()){
			logger.debug("尝试更新产品[" + product.getProductCode() + "]");
		}
		actualRowsAffected = productDao.update(product);

		if(_oldProduct.getSupplyPartnerId() == 0){//内部产品
			//先删除关联关系
			/*InternalForeignProductDataMapCriteria internalForeignProductDataMapCriteria = new InternalForeignProductDataMapCriteria();
			internalForeignProductDataMapCriteria.setInternalProductId(productId);

			internalForeignProductDataMapService.delete(internalForeignProductDataMapCriteria);*/
			/*if(product.getMappedProductIdList() != null){
				if(product.getParentProductId() == 0){

			 * 对于由外部产品关联创建的一级内部产品
			 * 尝试由关联外部产品获取定义数据


					if(product.getProductDataMap() == null ){
						product.setProductDataMap(new HashMap<String, ProductData>());
					}
					ProductDataCriteria productDataCriteria = new ProductDataCriteria();
					productDataCriteria.setProductId(product.getMappedProductIdList()[0]);
					HashMap<String, ProductData> foreignProductDataMap = productDataService.map(productDataCriteria);
					if(foreignProductDataMap != null){
						for(String key : foreignProductDataMap.keySet()){
							if(product.getProductDataMap().containsKey(key)){
								if(product.getProductDataMap().get(key).getCurrentStatus() != CommonStandard.BasicStatus.dynamic.getId()){
									logger.debug("更新外部产品定义的数据[" + key + "]");
									ProductData productData = foreignProductDataMap.get(key);
									productData.setProductId(productId);
									productDataService.put(productData);									
								}
							} else {
								logger.debug("添加外部产品定义的数据[" + key + "]");
								ProductData productData = foreignProductDataMap.get(key);
								productData.setProductId(productId);
								productDataService.put(productData);
							}
						}
					}
				}
				for(int foreignProductId : product.getMappedProductIdList()){
					InternalForeignProductDataMap internalForeignProductDataMap = new InternalForeignProductDataMap();
					internalForeignProductDataMap.setInternalProductId(productId);
					internalForeignProductDataMap.setForeignProductId(foreignProductId);
					internalForeignProductDataMap.setCurrentStatus(CommonStandard.BasicStatus.normal.getId());
					Product _foreignProduct = select(foreignProductId);
					if(_foreignProduct == null){
						throw new ObjectNotFoundByIdException("找不到指定的外部产品[" + foreignProductId + "].");
					}
					if(_foreignProduct.getSupplyPartnerId() == 0){
						throw new DataInvalidException("指定的外部产品[" + foreignProductId + "]没有供应商ID");
					}
					internalForeignProductDataMap.setSupplyPartnerId(_foreignProduct.getSupplyPartnerId());
					internalForeignProductDataMapService.insert(internalForeignProductDataMap);
				}
			}*/
		}
		productDataService.sync(product);

		/*if(product.getProductDataMap() != null ){
			findDataDefineId(product);
			for(ProductData productData : product.getProductDataMap().values()){
				if(productData.getCurrentStatus() == BasicStatus.dynamic.getId()){
					continue;
				}
				if(productData.getDataDefineId() == 0){
					throw new RequiredAttributeIsNullException("数据[" + productData.getDataCode() + "]的dataDefineId为空");
				}
				productData.setProductId(productId);
				productDataService.insert(productData);
			}
		} else {
			logger.debug("产品[" + product.getProductCode() + "没有productDataMap");
		}*/

		//更新tags

		tagObjectRelationService.sync(product.getOwnerId(),ObjectType.product.toString(), product.getProductId(), product.getTags());



		return actualRowsAffected;
	}

	@Override
	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'Product#' + #product.productId")
	public int updateNoNull(Product product) {
		Assert.notNull(product, "尝试更新的Product不能为空");
		Assert.isTrue(product.getProductId() > 0, "将要更新的Product其productId不能小于1");

		int actualRowsAffected = 0;

		int productId = product.getProductId();

		Product _oldProduct = productDao.select(productId);

		if (_oldProduct == null) {
			logger.warn("尝试更新产品，但找不到对应的旧产品");
			return 0;
		}
		actualRowsAffected = productDao.updateNoNull(product);


		return actualRowsAffected;
	}

	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'Product#' + #productId")
	public int delete(long productId) {
		int actualRowsAffected = 0;

		Product _oldProduct = productDao.select(productId);

		if (_oldProduct != null) {
			actualRowsAffected = productDao.forceDeleteAllAndRelation(productId);
			ProductDataCriteria productDataCriteria = new ProductDataCriteria();
			productDataCriteria.setProductId(productId);
			productDataService.delete(productDataCriteria);

			//更新tags
			tagObjectRelationService.sync(_oldProduct.getOwnerId(), ObjectType.product.toString(), _oldProduct.getProductId(), _oldProduct.getTags());

		}
		return actualRowsAffected;
	}

	@Override
	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'Product#' + #productId")
	public int forceDeleteAllAndRelation(long productId) {
		int actualRowsAffected = 0;

		Product _oldProduct = productDao.select(productId);

		if (_oldProduct != null) {
			actualRowsAffected = productDao.forceDeleteAllAndRelation(productId);


			//更新tags
			tagObjectRelationService.sync(_oldProduct.getOwnerId(), ObjectType.product.toString(), _oldProduct.getProductId(), _oldProduct.getTags());

		}
		return actualRowsAffected;
	}

	@Override
	public Product select(long productId) {
		return productExtraService.select(productId);
	}

	@Override
	public List<Product> list(ProductCriteria productCriteria) {
		/*if(productCriteria.isMustForeignProduct() && productCriteria.getProductId().length == 1){
			//指定通过某个产品ID查找外部产品，通过ioServerMap进行匹配
			IoServerMapCriteria ioServerMapCriteria = new IoServerMapCriteria();
			ioServerMapCriteria.setInProductId(productCriteria.getProductId()[0]);
			List<IoServerMap> ioServerMapList = ioServerMapService.list(ioServerMapCriteria);
			if(ioServerMapList == null || ioServerMapList.size() < 1){
				return null;
			}
			int[] outProductIds = new int[ioServerMapList.size()];
			for(int i = 0; i < ioServerMapList.size(); i++){
				outProductIds[i] = ioServerMapList.get(i).getOutServerId();
			}
			productCriteria.setProductId(outProductIds);
		}*/
		List<Long> pkList = productDao.listPk(productCriteria);
		List<Product> productList = new ArrayList<Product>();
		if(pkList != null && pkList.size() > 0){
			for(int i =0; i < pkList.size(); i++){
				productList.add(productExtraService.select(pkList.get(i)));
				productList.get(i).setIndex(i+1);
			}
		}
		return productList;

	}

	public List<Product> listOnPage(ProductCriteria productCriteria) {
		List<Long> pkList = productDao.listPkOnPage(productCriteria);
		List<Product> productList = new ArrayList<Product>();
		if(pkList != null && pkList.size() > 0){
			for(int i =0; i < pkList.size(); i++){
				Product product = productExtraService.select(pkList.get(i));
				if(product == null){
					logger.error("无法获取指定的产品[" + pkList.get(i) + "]");
					continue;
				}
				product.setIndex(i+1);
				productList.add(product);
			}
		}
		return productList;

	}

	@Override
	public int count(ProductCriteria productCriteria) {
		return productDao.count(productCriteria);
	}


	//如果产品存在则更新，如果产品不存在则添加，同时处理子产品
	@Override
	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'Product#' + #product.productId")
	public int put(Product product){
		if(product == null){
			throw new RequiredObjectIsNullException("尝试put的产品为空");
		}
		if(product.getCreateTime() == null){
			product.setCreateTime(new Date());
		}
		int actualRowsAffected = 0;		
		boolean isUpdate = true;
		if(product.getProductId() == 0){
			isUpdate = false;
		} else {
			Product _oldProduct = productDao.select(product.getProductId());
			if(_oldProduct == null){
				isUpdate = false;
			}
		}

		if(!isUpdate){
			//插入
			logger.debug("数据库中找不到尝试put的产品或其productId=0，尝试插入新产品");
			insert(product);
			actualRowsAffected++;
		} else {
			logger.debug("尝试put的产品productId不为0或者在数据库中找到了对应的产品，尝试更新旧产品");
			if(update(product) >0){
				actualRowsAffected++;				
			}
		}
		return actualRowsAffected;
	}

	@Override
	@Deprecated
	public Product select(String productCode) {
		return select(productCode, 0);
	}

	@Override
	public Product select(String productCode, long ownerId) {
		ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setProductCode(productCode);
		productCriteria.setOwnerId(ownerId);
		List<Long> productIdList= productDao.listPk(productCriteria);
		Product product = null;
		if(productIdList == null || productIdList.size() != 1){
		} else {
			return productExtraService.select(productIdList.get(0));
		}
		productCriteria = null;
		productIdList = null;
		return product;
	}


	/*@Override
	public int updateAmount(int productId, int count) {

		String cacheKey = "Product#" + productId;
		ValueWrapper vw = cacheService.getCacheManager().getCache(cacheName).get(cacheKey);
		Product product = null;
		if(vw != null){
			try{
				product = (Product)vw.get();
			}catch(Exception e){
				e.printStackTrace();
			}
		} 
		if(product == null){
			logger.warn("缓存中找不到要更新数量的产品[" + productId + "],从数据库查找");
			product = productDao.select(productId);
		}
		if(product == null){
			logger.warn("在缓存和数据库中都找不到要更新数量的产品[" + productId + "]");
			return -1;
		}
		int oldCount = product.getAvailableCount();
		if(oldCount < 0){
			if(logger.isDebugEnabled()){
				logger.debug("产品数量为负，不进行数量更新");
			}
			return -1;
		}
		if(oldCount + count <= 0){
			product.setAvailableCount(0);
			if(logger.isDebugEnabled()){
				logger.debug("将产品[" + productId + "/" + product.getProductCode() + "]剩余数量强行设置为0");
			}	
		} else {
			product.setAvailableCount(product.getAvailableCount()+count);
			if(logger.isDebugEnabled()){
				logger.debug("将产品[" + productId + "/" + product.getProductCode() + "]剩余数量从" + oldCount + "设置为" + product.getAvailableCount());
			}	
		}
		productDao.updateAmount(product);
		cacheService.getCacheManager().getCache(cacheName).put(cacheKey, product);
		return 1;

	}*/



	@Override
	@IgnoreJmsDataSync
	public void updateDynamicPrice(long productId, float buyMoney) {

		String cacheKey = "Product#" + productId;
		Product product = cacheService.get(cacheName, cacheKey);

		if(product == null){
			logger.warn("缓存中找不到要更新售价的产品[" + productId + "],从数据库查找");
			return;
		}

		product.setBuyMoney(buyMoney);
		cacheService.put(cacheName, cacheKey, product);

	}




	@PreDestroy
	@Override
	public void flushProductAmountCache() {
		List<String> keys = cacheService.listKeys(cacheName, null);
		if(keys == null || keys.size() < 1){
			if(logger.isDebugEnabled()){
				logger.debug("尝试刷新的产品数据缓存为空");
			}
			return;
		}
		if(logger.isDebugEnabled()){
			logger.debug("当前产品缓存共有[" + keys.size() + "]条");
		}
		for(String key : keys){
			if(!key.startsWith("Product#")){
				continue;
			}

			Product product = cacheService.get(cacheName, key);

			if(product == null){
				logger.error("找不到缓存中的产品:" + key);
				continue;
			}
			if(product.getAvailableCount() == -1){
				if(logger.isDebugEnabled()){
					logger.debug("产品的数量为-1，不更新");
				}
				continue;
			}
			int distributedCount = getDistributedAmount(product.getProductId());
			if(distributedCount < 0){
				logger.warn("从中心缓存获取到的产品[" + product.getProductId() + "]数量是:" + distributedCount);
			}
			if(distributedCount == -EisError.OBJECT_IS_NULL.id){
				logger.warn("从中心缓存获取到的产品[" + product.getProductId() + "]数量是:" + distributedCount + "，视为未能获取到数据，不更新此产品");
				continue;
			}
			product.setAvailableCount(distributedCount);
			if(logger.isDebugEnabled()){
				logger.debug("更新产品[" + product.getProductId() + "/" + product.getProductCode() + "]数量" + product.getAvailableCount() + "到数据库");
			}
			productDao.updateNoNull(product);



		}
	}



	/*	private void findDataDefineId(Product product){
		if(product.getProductDataMap() == null){
			throw new RequiredAttributeIsNullException("产品的数据productDataMap为空")	;
		}
		if(product.getProductTypeId() == 0){
			throw new RequiredAttributeIsNullException("产品的类型为0");
		}

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.product.toString());
		dataDefineCriteria.setObjectId(product.getProductTypeId());
		HashMap<String, DataDefine> dataDefineMap = dataDefineService.map(dataDefineCriteria);
		if(dataDefineMap == null){
			logger.error("产品类型[" + product.getProductTypeId() + "]没有数据定义");
			return;
		}
		for(String dataCode : dataDefineMap.keySet()){
			for(String productDataCode : product.getProductDataMap().keySet() ){
				if(dataCode.equals(productDataCode)){
					product.getProductDataMap().get(productDataCode).setDataDefineId(dataDefineMap.get(dataCode).getDataDefineId());
					break;
				}
			}
		}
	}*/

	@Override
	public void sync(List<Product> productList) {
		if(productList == null){
			logger.error("将要保存的骏卡产品列表为空");
			return;
		}
		//查询旧数据
		for(Product product : productList){
			Product _oldProduct = select(product.getProductCode(), product.getOwnerId());
			if(_oldProduct != null){
				//更新
				product.setProductId(_oldProduct.getProductId());
				update(product);
			} else {
				//新增
				int tempProductId = product.getProductId();
				product.setProductId(0);
				insert(product);
				for(Product p2 : productList){
					if(p2.getParentProductId() > 0 && p2.getParentProductId() == tempProductId){
						p2.setParentProductId(tempProductId);
						update(p2);
					}
				}

			}
		}

	}
	@SuppressWarnings("unused")
	private void clearDynamicData(Product product) {
		if(product == null){
			return;
		}
		if(product.getProductDataMap() == null || product.getProductDataMap().size() < 1){
			return;
		}
		HashMap<String, ProductData> pdMap = new HashMap<String, ProductData> ();
		for(ProductData pd : product.getProductDataMap().values()){
			if(pd == null){
				continue;
			}
			if(StringUtils.isNotBlank(pd.getDataValue())){
				pdMap.put(pd.getDataCode(), pd);
			}
		}
		product.setProductDataMap(pdMap);

	}

	@Override
	public List<Long> listPk(ProductCriteria productCriteria) {
		return productDao.listPk(productCriteria);
	}

	/**
	 * 根据产品数据，为文档生成对应的扩展文档数据
	 * 用于创建文档和显示文档的时候
	 */
	@Override
	public int generateProductDocumentData(Product product, Document document) {
		if(document.getDocumentDataMap() == null){
			document.setDocumentDataMap(new HashMap<String,DocumentData>());
		}
		int count = 0;
		for(ProductData pd : product.getProductDataMap().values()){
			/*if(pd.getDisplayLevel() != null  && (pd.getDisplayLevel().equals(DisplayLevel.platform.toString()) || pd.getDisplayLevel().equals(DisplayLevel.system.toString()))){
				logger.debug("当前产品数据[" + pd + "]显示级别[" + pd.getDisplayLevel() + "]不能在前端展示");
				continue;
			}*/
			if(pd.getDataCode() == null){
				logger.debug("忽略产品[" + product.getProductId() + "]的空扩展数据:" + pd);
				continue;
			}
			if(pd.getDataValue() == null){
				logger.debug("忽略产品[" + product.getProductId() + "]的空扩展数据:" + pd);
				continue;
			}
			DocumentData dd = DataUtils.productData2DocumentData(pd);
			//把发货地、原产地等信息进行处理
			if(dd.getDataValue() != null && dd.getDataCode() != null && (dd.getDataCode().equals(DataName.defaultFromArea.toString()) ||dd.getDataCode().equals(DataName.productOrigin.toString()) || dd.getDataCode().equals(DataName.deliveryFromArea.toString()))){
				if(dd.getDataValue().startsWith("北京") || dd.getDataValue().startsWith("天津") || dd.getDataValue().startsWith("重庆") || dd.getDataValue().startsWith("上海")){
					dd.setDataValue(dd.getDataValue().split("#")[0]);
				} else {
					dd.setDataValue(dd.getDataValue().replaceAll("#", " "));
				}
			}

			document.getDocumentDataMap().put(dd.getDataCode(), dd);
			logger.debug("根据产品[" + product.getProductId() + "]为文档[" + document.getUdid() + "]生成附加数据:" + dd.getDataCode() + "=>" + dd.getDataValue() + "]");
			count++;
		}
		//生成产品代码		
		DocumentData d0 = new DocumentData(DataName.productCode.toString(), product.getProductCode());
		document.getDocumentDataMap().put(d0.getDataCode(), d0);
		logger.debug("根据产品[" + product.getProductId() + "]为文档[" + document.getUdid() + "]生成产品代码:" + d0.getDataCode() + "=>" + d0.getDataValue() + "]");
		document.getDocumentDataMap().put(d0.getDataCode(), d0);


		//生成剩余数量		
		int availableCount = getDistributedAmount(product.getProductId());
		if(availableCount < -2){
			availableCount = product.getAvailableCount();
		}
		product.setAvailableCount(availableCount);
		DocumentData d1 = new DocumentData(DataName.availableCount.toString(), String.valueOf(availableCount));
		document.getDocumentDataMap().put(d1.getDataCode(), d1);
		logger.debug("根据产品[" + product.getProductId() + "]为文档[" + document.getUdid() + "]生成剩余数量:" + d1.getDataCode() + "=>" + d1.getDataValue() + "]");

		//生成已销售数量
		int soldCount = 0;
		int initCount = product.getInitCount();
		if(initCount < availableCount){
			initCount = availableCount;
		}
		soldCount = initCount - availableCount;
		DocumentData d2 = new DocumentData(DataName.soldCount.toString(), String.valueOf(soldCount));
		document.getDocumentDataMap().put(d2.getDataCode(), d2);
		logger.debug("根据产品[" + product.getProductId() + "]为文档[" + document.getUdid() + "]生成已售出数量:" + d2.getDataCode() + "=>" + d2.getDataValue() + "]");

		if(product.getProductDataMap() == null || product.getProductDataMap().size() < 1 ){
			logger.debug("当前产品[" + product.getProductId() + "]没有任何扩展数据");
			return 3;
		}




		return count+3;
	}

	/**
	 * 尝试写入产品的最新数量
	 * 如原产品数量是100，offset=-1，则期望得到-1后的产品数量
	 * 如果数量异常，说明锁定数量失败，不能进行交易
	 */
	@Override
	public long writeAmount(Product product, int offset, boolean forceWrite){

		if(product.getAvailableCount() < 0){
			logger.info("产品[" + product.getProductId() + "/" + product.getProductCode() + "]数量小于0，视为无限制，不进行数量操作");
			return product.getAvailableCount();
		}

		Product _oldProduct = productDao.select(product.getProductId());
		Product p = new Product();
		p.setOwnerId(_oldProduct.getOwnerId());
		p.setProductId(_oldProduct.getProductId());
		if(forceWrite){			
			p.setAvailableCount(product.getAvailableCount());
			productDao.updateNoNull(p);
		} else {

			int distributedCount = getAmount(product.getProductId());
			int updateCount = 0;
			if(distributedCount < 0){
				if(logger.isDebugEnabled()){
					logger.debug("产品数量为负，不进行数量更新");
				}
				return -1;
			}
			if(distributedCount + offset <= 0){
				updateCount = 0;
				if(logger.isDebugEnabled()){
					logger.debug("将产品[" + product.getProductId() + "/" + _oldProduct.getProductCode() + "]剩余数量强行设置为0");
				}	
			} else {
				updateCount = distributedCount + offset;
				if(logger.isDebugEnabled()){
					logger.debug("将产品[" + product.getProductId() + "/" + _oldProduct.getProductCode() + "]剩余数量从" + distributedCount + "设置为" + updateCount);
				}	
			}
			p.setAvailableCount(updateCount);
			productDao.updateNoNull(p);
		}
		/*
		 * 修补了一个漏洞
		 * 之前直接把从dao中取出的product放入缓存，导致该product的扩展数据未取出, 
		 * 现在改成直接删除缓存中的数据
		 * NetSnake,2016-04-08
		 */
		//		cacheService.getCacheManager().getCache(cacheName).put(cacheKey, _oldProduct);
		String cacheKey = "Product#" + product.getProductId();
		logger.debug("从缓存[" + cacheName + "]中删除产品缓存:" + cacheKey);
		cacheService.delete(new CacheCriteria(cacheName, cacheKey));
		if(product.getSyncFlag() > 0){
			logger.debug("当前产品[" + product.getProductId() + "]是同步模式，不更新REDIS");
			return product.getAvailableCount();
		}
		cacheService.delete(new CacheCriteria(CommonStandard.cacheNameUser, "Partner#XXXXX"));
		//如果还未同步，向REDIS服务器写入数量

		/*if(centerDataService == null){
			logger.error("当前系统未实现分布式锁服务");
			return product.getAvailableCount();
		}*/
		String key = "Amount#Product#" + product.getProductId();
		if(forceWrite){
			logger.debug("尝试为产品强行写入数量:" + product.getAvailableCount() + ",锁定KEY=" + key);
			centerDataService.setForce(key, String.valueOf(product.getAvailableCount()), 0);
			return product.getAvailableCount();
		}
		long newNumber = centerDataService.increaseBy(key, offset, product.getAvailableCount(), 0);
		logger.debug("为产品锁定改变数量" + offset + ",本节点数量:" + product.getAvailableCount() + ",锁定KEY=" + key + ",写入后结果:" + newNumber);
		if(newNumber == 0){
			return -EisError.serviceUnavaiable.id;
		}
		product.setAvailableCount((int)newNumber);
		return newNumber;

	}

	@Override
	public int getDistributedAmount(long productId){
		if(centerDataService == null){
			logger.error("当前系统未实现分布式锁服务");
			return -EisError.serviceUnavaiable.id;
		}
		String key = KeyConstants.STOCK_PREFIX + "#product#" + productId;
		String newNumber = centerDataService.get(key);
		logger.debug("获取中心缓存的产品[" + productId + "]数量是:" + newNumber);
		if(newNumber == null){
			return -EisError.OBJECT_IS_NULL.id;
		}
		return (int)Long.parseLong(newNumber);

	}

	@Override
	public Document getRefDocument(Product product) throws Exception{
		
		if(product == null){
			return null;
		}
		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setDocumentTypeCode(ObjectType.product.name());
		List<DocumentType> documentTypeList = documentTypeService.list(documentTypeCriteria);
		if(documentTypeList == null || documentTypeList.size() < 1){
			logger.error("系统中未定义产品类型的文档类型");
			return null;
		} 
		int productDocumentTypeId = documentTypeList.get(0).getDocumentTypeId();

		//查找是否有同名文档
		DocumentCriteria documentCriteria = new DocumentCriteria(product.getOwnerId());
		documentCriteria.setDocumentCode(product.getProductCode());
		documentCriteria.setDocumentTypeId(productDocumentTypeId);
		List<Document> documentList = null;
		documentList = documentService.list(documentCriteria);
		logger.info("文档类型是[" + productDocumentTypeId + "]、documentCode=" + product.getProductCode() + "]的文档条数是:" + (documentList == null ? "空" : documentList.size()));
		if(documentList != null && documentList.size() > 0){
			return documentList.get(0);
		}

		//查找是否有对应的文档已发布
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.document.name());
		dataDefineCriteria.setObjectId(productDocumentTypeId);
		dataDefineCriteria.setDataCode(DataName.productCode.toString());
		DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
		if(dataDefine == null){
			logger.error("找不到文档类型[" + productDocumentTypeId + "]对应的productCode数据定义");
			return null;
		}
		DocumentDataCriteria documentDataCriteria = new DocumentDataCriteria();
		documentDataCriteria.setDataDefineId(dataDefine.getDataDefineId());
		documentDataCriteria.setDataValue(product.getProductCode());
		List<DocumentData> documentDataList = documentDataService.list(documentDataCriteria);
		logger.info("文档类型是[" + productDocumentTypeId + "]、productCode=" + product.getProductCode() + "]的文档扩展数据条数是:" + (documentDataList == null ? "空" : documentDataList.size()));
		if(documentDataList.size() < 1){
			return null;
		}
		Set<Integer> udidList = new HashSet<Integer>();
		for(DocumentData dd : documentDataList){
			int udid = dd.getUdid();
			udidList.add(udid);
		}
		logger.info("根据documentCode/productCode={}得到的文档扩展数据是{}条，对应的udid是{}条",product.getProductCode(), documentDataList.size(),  udidList.size());
		if(udidList.size() < 1){
			return null;
		}
		for(int udid : udidList){
			Document document = documentService.select(udid);
			if(document == null){
			}
			if(	document.getOwnerId() == product.getOwnerId()){
				logger.debug("根据数据字典productCode={}找到了匹配的文档:{}",product.getProductCode(),document.getUdid());
				return document;
			}
		}
		logger.warn("根据documentCode/productCode={},ownerId={}得到的文档扩展数据是{}条，对应的udid是{}条，但是没有找到任何完全一致的文档",product.getProductCode(), product.getOwnerId(), documentDataList.size(),  udidList.size());
		return null;
	}

	@Override
	public int getAmount(long productId) {
		int distributedCount = getDistributedAmount(productId);
		if(distributedCount < 0){
			Product p = select(productId);
			return p.getAvailableCount();
		} 
		return distributedCount;

	}
	
	@Override
	public long getMaxId(ProductCriteria productCriteria) {
		return productDao.getMaxId(productCriteria);
	}

	@Override
	public Set<Column> getDisplayColumns(int productTypeId, long ownerId) {
		String key = "DisplayColumn#" + ownerId + "#" + ObjectType.product.name() + "#" + productTypeId;
		Set<Column> displayColumns = null;
		try{
			displayColumns = cacheService.get(cacheName, key);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(displayColumns != null){
			logger.debug("从缓存中返回[" + key + "]");
			return displayColumns;
		}
		displayColumns = new TreeSet<Column>(new ColumnWeightComparator());
		Field[] fields = Product.class.getFields();
		if(fields != null && fields.length > 0){
			for(Field field : fields){
				DisplayColumn d = field.getAnnotation(DisplayColumn.class);
				if(d == null){
					logger.debug("对象[" + key + "]的字段:" + field.getName() + "]没有DisplayColumn注解，不放入显示字段中");
					continue;
				}				
				displayColumns.add(new Column(field.getName(), CommonStandard.COLUMN_TYPE_NATIVE, d.value(), 0, BasicStatus.normal.getId()));			
			}
		}

		fields = Product.class.getDeclaredFields();
		if(fields != null && fields.length > 0){
			for(Field field : fields){
				DisplayColumn d = field.getAnnotation(DisplayColumn.class);
				if(d == null){
					logger.debug("对象[" + key + "]的字段:" + field.getName() + "]没有DisplayColumn注解，不放入显示字段中");
					continue;
				}				
				displayColumns.add(new Column(field.getName(), CommonStandard.COLUMN_TYPE_NATIVE, d.value(), 0, BasicStatus.normal.getId()));	
			}
		}

		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ObjectType.product.name(), productTypeId, ownerId);
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList != null && dataDefineList.size() > 0){
			for(DataDefine dataDefine : dataDefineList){
				if(dataDefine.getDisplayLevel() != null && dataDefine.getDisplayLevel().equals(DisplayLevel.system.toString())){
					continue;
				}
				displayColumns.add(new Column(dataDefine.getDataCode(), CommonStandard.COLUMN_TYPE_EXTRA, "DataName", 0, BasicStatus.normal.getId()));			
			}
		}
		logger.debug("将显示字段放入缓存:" + key);
		cacheService.put(cacheName, key, displayColumns);
		return displayColumns;
	}



	@Override
	public Product getProductByDocument(Document document) {
		String productCode = document.getExtraValue(DataName.productCode.toString());
		if(productCode == null){
			productCode = document.getDocumentCode();
			logger.debug("文档[" + document.getUdid() + "]未定义productCode，尝试使用文档代码[" + productCode + "]查找对应产品");
		}

		Product product = select(productCode, document.getOwnerId());
		if(product == null){
			logger.warn("找不到产品文档[" + document.getUdid() + "]所定义的产品[" + productCode + "]");
			return null;
		}
		return product;
	}

	/*	@Override
	public void processDataPath(Product product) {
		if(product == null){
			return;
		}
		if(product.getProductDataMap() == null || product.getProductDataMap().size() < 1){
			return;
		}
		for(ProductData pd : product.getProductDataMap().values()){
			if(pd == null){
				continue;
			}
			if(pd.getInputMethod() != null && pd.getInputMethod().equals("file")){
				if(pd.getDataValue() != null && !pd.getDataValue().startsWith(EXTRA_DATA_URL_PREFIX)){
					if(pd.getDisplayLevel() == null){
						pd.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_OPEN_PATH + "/" + pd.getDataValue());
					} else {
						if(pd.getDisplayLevel().equals(DisplayLevel.subscriber.toString())){
							pd.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_SUBSCRIBE_PATH + "/" + pd.getDataValue());
						} else if(pd.getDisplayLevel().equals(DisplayLevel.login.toString())){
							pd.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_LOGIN_PATH + "/" + pd.getDataValue());
						} else {
							pd.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_OPEN_PATH + "/" + pd.getDataValue());
						}
					}
				}
			}

		}
	}
	 */
	/*	@Override
	public void sort(List<Product> productList, String sortField, String sortOrder) {
		if(productList == null || productList.size() < 1){
			logger.error("尝试排序的对象列表不能为空");
			return;		
		}
		CustomProductComparator customComparator = new CustomProductComparator(sortField, sortOrder);
		Collections.sort(productList, customComparator);

	}*/

}

/*
class CustomProductComparator implements Comparator<Product>{

	private String sortField;
	private String sortOrder;

	private  static final Log logger = LogFactory.getLog(CustomProductComparator.class);


	public CustomProductComparator(String sortField, String sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Product o1, Product o2) {
		if(this.sortField == null){
			logger.error("无法排序因为未指定排序字段");
			return 0;
		}
		String getMethodName = "get" + StringUtils.capitalize(this.sortField);
		Object result1 = null;
		Object result2 = null;

		boolean useExtraDataSort = false;
		try {
			Method method = o1.getClass().getMethod(getMethodName, new Class<?>[]{});
			if(method == null){
				logger.warn("找不到要排序字段[" + this.sortField + "]的GET方法:" + getMethodName);
				useExtraDataSort = true;
			} else {
				result1 = method.invoke(o1, new Object[]{});
				result2 = method.invoke(o2, new Object[]{});
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		} 
		if(useExtraDataSort){
			result1 = o1.getExtraValue(this.sortField);
			result2 = o2.getExtraValue(this.sortField);
		}
		if(result1 == null || result2 == null){
			logger.warn("执行排序字段[" + this.sortField + "]的GET方法:" + getMethodName + "，返回的数据为空，或者扩展数据为空");
			return 0;
		}
		int compareResult = 0;


		//排序
		Collator collator = Collator.getInstance();
		CollationKey key1 = collator.getCollationKey(result1.toString());
		CollationKey key2 = collator.getCollationKey(result2.toString());

		compareResult =  key1.compareTo(key2);
		if(this.sortOrder != null && this.sortOrder.equalsIgnoreCase("DESC")){
			return - compareResult;
		} 
		return compareResult;

	}

}

 */
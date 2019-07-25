package com.maicard.product.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ProductDao;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.domain.ProductType;
import com.maicard.product.service.ProductDataService;
import com.maicard.product.service.ProductExtraService;
import com.maicard.product.service.ProductTypeService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.ObjectType;

public class ProductExtraServiceImpl extends BaseService implements ProductExtraService{
	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private ProductDao productDao;
	@Resource
	private ProductDataService productDataService;
	@Resource
	private TagService tagService;

	@Resource
	private TagObjectRelationService tagObjectRelationService;	
	@Resource
	private PartnerService partnerService;
	private static final String cacheName = CommonStandard.cacheNameProduct;

	
	
	@Override
	@Cacheable(value = cacheName, key = "'Product#' + #productId")
	public Product select(long productId) {
		logger.debug("从数据库选择产品:" + productId);
		Product product = productDao.select(productId);
		if(product != null){
			afterFetchFull(product);
			/*if(product.getLabelMoney() > 0 & product.getBuyMoney() > 0){
				product.setRate(String.valueOf(product.getBuyMoney() / product.getLabelMoney()));
			}*/
		} else {
			logger.warn("尝试查找的产品是空[productId=" + productId + "]");
		}
		return product;
	}

	
	private void afterFetchFull(Product product){
		if(product == null){
			return;
		}
		product.setId(product.getProductId());
		ProductType productType = productTypeService.select(product.getProductTypeId());
		if(productType != null){
			product.setProductTypeName(productType.getProductTypeName());
		}
		/*try{
			product.setCurrentStatusName(BasicStatus.normal.findById(product.getCurrentStatus()).getName());
		}catch(Exception e){}
		try{
			product.setExtraStatusName(ServiceStatus.closed.findById(product.getExtraStatus()).getName());
		}catch(Exception e){}

		if(product.getSupplyPartnerId() < 1){//内部产品
			product.setSupplyPartnerName("内部产品");
		} else {
			User partner = partnerService.select(product.getSupplyPartnerId());
			if(partner == null){
				product.setSupplyPartnerName("未知");
			} else {
				product.setSupplyPartnerName(partner.getUsername());
			}
		}
*/
		//获取标签
		StringBuffer sb = new StringBuffer();

		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
		tagObjectRelationCriteria.setObjectType(ObjectType.product.toString());
		tagObjectRelationCriteria.setObjectId(product.getProductId());
		List<TagObjectRelation> tagObjectRelationList = tagObjectRelationService.list(tagObjectRelationCriteria);
		if(tagObjectRelationList != null){
			for(TagObjectRelation tagObjectRelation : tagObjectRelationList){
				if(tagObjectRelation != null){
					Tag tag = tagService.select(tagObjectRelation.getTagId());
					if(tag != null){
						sb.append(tag.getTagName());
						sb.append(",");
					}
				}
			}
			try{sb.deleteCharAt(sb.length() - 1);}catch(Exception e){}
			product.setTags(sb.toString());

		}


		logger.debug("尝试获取产品[" +  product.getProductId() + "]的全部扩展数据");
		
		//获取产品自定义数据
		ProductDataCriteria productDataCriteria = new ProductDataCriteria();
		productDataCriteria.setProductId(product.getProductId());
		productDataCriteria.setCurrentStatus(BasicStatus.normal.getId());
		HashMap<String,ProductData> productDataMap = productDataService.map(productDataCriteria);
		if(productDataMap != null && productDataMap.size() > 0){
			logger.debug("产品[" + product.getProductId() + "]有" + productDataMap.size() + "条自定义数据");
			product.setProductDataMap(productDataMap);
		} else {
			logger.debug("产品[" + product.getProductId() + "]没有自定义数据");
			product.setProductDataMap(new HashMap<String, ProductData>());
			/* 
			 * 如果是内部产品
			 * 如果是二级产品，那么尝试获取并继承上一级产品的数据
			 */
			if(product.getSupplyPartnerId() == 0){
				HashMap<String, ProductData> dynamicProductDataMap = new HashMap<String, ProductData>();
				//int dynamicProductId = 0;
				if(product.getParentProductId() != 0){
					//是二级产品				
					logger.debug("二级产品[" + product.getProductId() + "]没有自定义数据，获取上级产品的数据定义");
					productDataCriteria = new ProductDataCriteria();
					productDataCriteria.setProductId(product.getParentProductId());
					productDataCriteria.setCurrentStatus(BasicStatus.normal.getId());
					dynamicProductDataMap = productDataService.map(productDataCriteria);
					if(dynamicProductDataMap == null || dynamicProductDataMap.size() < 1){
						/*logger.debug("二级产品[" + product.getProductId() + "]的上级产品[" +product.getParentProductId() + "]也没有自定义数据，尝试获取上级产品的关联外部产品");						internalForeignProductDataMapCriteria = new InternalForeignProductDataMapCriteria();
						internalForeignProductDataMapCriteria.setInternalProductId(product.getParentProductId());
						list = internalForeignProductDataMapService.listMappedProductId(internalForeignProductDataMapCriteria);
						if(list == null || list.size() < 1){
							logger.warn("二级产品[" + product.getProductId() + "]的上级产品没有没有外部关联产品");
						} else {
							dynamicProductId = list.get(0);
							logger.debug("二级产品[" + product.getProductId() + "]的上级产品的外部关联产品ID是[" + dynamicProductId + "]");
						}*/
					} else {
						logger.debug("为二级产品[" + product.getProductId() + "]映射上级产品[" + product.getParentProductId() + "]的自定义数据");
						//internalForeignProductDataMapCriteria = new InternalForeignProductDataMapCriteria();
						for(ProductData pd: dynamicProductDataMap.values()){
							pd.setProductId(product.getProductId());
							pd.setCurrentStatus(BasicStatus.dynamic.getId());
							product.getProductDataMap().put(pd.getDataCode(), pd);
						}		
					}

				} 

			}

		}
		DataDefineCriteria productDataDefineCriteria = new DataDefineCriteria();
		productDataDefineCriteria.setObjectType(ObjectType.product.toString());
		productDataDefineCriteria.setObjectId(product.getProductTypeId());
		List<DataDefine> productDataDefineList = dataDefineService.list(productDataDefineCriteria);

		if(productDataDefineList != null && product.getProductDataMap() != null){

			//然后添加可能还没有的数据规范
			for(DataDefine dataDefine: productDataDefineList){
				if(!product.getProductDataMap().containsKey(dataDefine.getDataCode())){
					logger.debug("产品[" + product.getProductId() + "]中没有规范定义的数据[" + dataDefine.getDataCode() + "]，添加新的空数据");
					ProductData pd = new ProductData();
					pd.setProductId(product.getProductId());
					pd.setDataCode(dataDefine.getDataCode());
					pd.setDataDefineId(dataDefine.getDataDefineId());
					pd.setInputLevel(dataDefine.getInputLevel());
					pd.setInputMethod(dataDefine.getInputMethod());
					pd.setCurrentStatus(dataDefine.getCurrentStatus());
					pd.setDataDescription(dataDefine.getDataDescription());
					product.getProductDataMap().put(pd.getDataCode(), pd);
				}
			}
		}

		//XXX 已停止在Service中处理图片连接URL，NetSnake,2017-01-27
		//对图片连接进行处理，添加前缀
		/*if(product.getProductDataMap() != null){
			for(ProductData pd : product.getProductDataMap().values()){
				try{
					if(pd.getInputMethod().equals("file")){
						if(pd.getDataValue().startsWith(productFileUrl)){
							continue;
						}
						if(pd.getDisplayLevel() == null){
							pd.setDisplayLevel(DisplayLevel.open.toString());
						}
						// 对于后来新增加的gallery没有处理每一个文件的前缀
						String urlPrefix = productFileUrl + "/";
						if(pd.getDisplayLevel().equals(DisplayLevel.login.toString())){
							urlPrefix += CommonStandard.EXTRA_DATA_LOGIN_PATH;
						} else if(pd.getDisplayLevel().equals(DisplayLevel.subscriber.toString())){
							urlPrefix += CommonStandard.EXTRA_DATA_SUBSCRIBE_PATH;
						} else {
							urlPrefix += CommonStandard.EXTRA_DATA_OPEN_PATH;
						}
						pd.setDataValue(urlPrefix + "/" + pd.getDataValue());
						logger.debug("将数据[" + pd.getDataCode() + "]设置为:" + pd.getDataValue());

					}
				}catch(Exception e){}

			}
		}*/
		
		//获取产品的区域数据
		/*RegionCriteria regionCriteria = new RegionCriteria();
		regionCriteria.setRegionType(ObjectType.product.name());
		regionCriteria.setRefObjectId(product.getProductId());
		HashMap<String,Region> regionMap = regionService.mapInTree(regionCriteria);
		logger.debug("产品[" + product.getProductId() + "]的区服数据有[" + (regionMap == null ? -1 : regionMap.size()) + "]个");
		if((regionMap == null || regionMap.size() < 1) && (product.getParentProductId() > 0)){
			logger.debug("二级产品[" + product.getProductId() + "]未定义区域，从父产品[" + product.getParentProductId() + "]中获取.");
			regionCriteria = new RegionCriteria();
			regionCriteria.setRefObjectId(product.getParentProductId());		
			regionMap = regionService.mapInTree(regionCriteria);
			logger.debug("产品[" + product.getProductId() + "]从父产品[" + product.getParentProductId() + "]获取的区服数据有[" + (regionMap == null ? -1 : regionMap.size()) + "]个");
		}
		if(regionMap == null || regionMap.size() < 1){
			logger.info("未能获取到产品[" + product.getProductId() + "/" + product.getProductCode() + "]的区服数据");
		} else {
			product.setRegionMap(regionMap);
		}*/
		
	}
}

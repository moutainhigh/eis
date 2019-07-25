package com.maicard.product.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.criteria.ProductPartnerRelationCriteria;
import com.maicard.product.dao.ProductPartnerRelationDao;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductPartnerRelation;
import com.maicard.product.service.ProductPartnerRelationService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.DataName;

@Service
public class ProductPartnerRelationServiceImpl extends BaseService implements ProductPartnerRelationService {

	@Resource
	private ProductPartnerRelationDao productPartnerRelationDao;

	@Resource
	private ConfigService configService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ProductService productService;

	private boolean allProductValideForAllPartner;

	@PostConstruct
	public void init(){
		allProductValideForAllPartner = configService.getBooleanValue(DataName.allProductValideForAllPartner.toString(),0);
	}


	public int insert(ProductPartnerRelation productPartnerRelation) {
		return productPartnerRelationDao.insert(productPartnerRelation);
	}

	public int update(ProductPartnerRelation productPartnerRelation) {
		int actualRowsAffected = 0;

		int productPartnerRelationId = productPartnerRelation.getProductPartnerRelationId();

		ProductPartnerRelation _oldProductPartnerRelation = productPartnerRelationDao.select(productPartnerRelationId);

		if (_oldProductPartnerRelation != null) {
			actualRowsAffected = productPartnerRelationDao.update(productPartnerRelation);
		}

		return actualRowsAffected;
	}

	public int delete(int productPartnerRelationId) {
		int actualRowsAffected = 0;

		ProductPartnerRelation _oldProductPartnerRelation = productPartnerRelationDao.select(productPartnerRelationId);

		if (_oldProductPartnerRelation != null) {
			actualRowsAffected = productPartnerRelationDao.delete(productPartnerRelationId);
		}

		return actualRowsAffected;
	}

	public ProductPartnerRelation select(int productPartnerRelationId) {
		return productPartnerRelationDao.select(productPartnerRelationId);
	}

	public List<ProductPartnerRelation> list(ProductPartnerRelationCriteria productPartnerRelationCriteria) {
		return productPartnerRelationDao.list(productPartnerRelationCriteria);
	}

	public List<ProductPartnerRelation> listOnPage(ProductPartnerRelationCriteria productPartnerRelationCriteria) {
		return productPartnerRelationDao.listOnPage(productPartnerRelationCriteria);
	}

	@Override
	public List<User> listPartner(
			ProductPartnerRelationCriteria productPartnerRelationCriteria) {
		List<ProductPartnerRelation> list = list(productPartnerRelationCriteria);
		if(list == null || list.size() < 1){
			return null;
		}		
		long[] partnerIds = new long[list.size()];
		for(int i = 0; i < list.size(); i++){
			partnerIds[i] = list.get(i).getPartnerId();
		}
		UserCriteria partnerCriteria  = new UserCriteria();
		partnerCriteria.setUuids(partnerIds);
		return partnerService.list(partnerCriteria);
	}

	@Override
	public List<Product> listProduct(
			ProductPartnerRelationCriteria productPartnerRelationCriteria) {
		if(allProductValideForAllPartner){
			ProductCriteria productCriteria  = new ProductCriteria();
			productCriteria.setMustInternalProduct(true);
			return productService.list(productCriteria);
		}
		List<ProductPartnerRelation> list = list(productPartnerRelationCriteria);
		if(list == null || list.size() < 1){
			return null;
		}		
		int[] productIds = new int[list.size()];
		for(int i = 0; i < list.size(); i++){
			productIds[i] = list.get(i).getProductId();
		}
		ProductCriteria productCriteria  = new ProductCriteria();
		productCriteria.setProductId(productIds);
		return productService.list(productCriteria);
	}


	@Override
	public void applyPartnerSetting(Item item) {
		if(item == null){
			logger.error("尝试加载合作伙伴设置的交易为空");
			return;
		}
		ProductPartnerRelationCriteria productPartnerRelationCriteria = new ProductPartnerRelationCriteria();
		productPartnerRelationCriteria.setProductId(item.getProductId());
		productPartnerRelationCriteria.setPartnerId(item.getChargeFromAccount());
		
		List<ProductPartnerRelation> productPartnerRelationList = list(productPartnerRelationCriteria);
		if(productPartnerRelationList == null || productPartnerRelationList.size() < 1){
			logger.info("根据产品" + productPartnerRelationCriteria.getProductId() + "和合作方" + productPartnerRelationCriteria.getPartnerId() + "找不到任何配置");
			productPartnerRelationCriteria = null;
			return;
		}
		ProductPartnerRelation productPartnerRelation = null;
		if(productPartnerRelationList.size() > 1){
			logger.warn("根据产品" + productPartnerRelationCriteria.getProductId() + "和合作方" + productPartnerRelationCriteria.getPartnerId() + "找到的配置不止一条，强制使用第一条");
		}
		productPartnerRelation = productPartnerRelationList.get(0);
		if(productPartnerRelation.getTtl() > 0){
			item.setTtl(productPartnerRelation.getTtl());
		}
		if(productPartnerRelation.getMaxRetry() > 0){
			item.setMaxRetry(productPartnerRelation.getMaxRetry());
		}
		/*if(StringUtils.isNotBlank(productPartnerRelation.getFailPolicy())){
			item.setFailPolicy(productPartnerRelation.getFailPolicy());
		}*/
		productPartnerRelation = null;
		productPartnerRelationList = null;
		productPartnerRelationCriteria = null;

	}


	@Override
	public int count(
			ProductPartnerRelationCriteria productPartnerRelationCriteria) {
		return productPartnerRelationDao.count(productPartnerRelationCriteria);
	}

}

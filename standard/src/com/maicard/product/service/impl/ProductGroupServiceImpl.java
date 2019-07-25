package com.maicard.product.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.NumericUtils;
import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.dao.ProductGroupDao;
import com.maicard.product.domain.ProductGroup;
import com.maicard.product.service.ProductGroupService;
import com.maicard.product.service.ProductService;
import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.ObjectType;

@Service
public class ProductGroupServiceImpl extends BaseService implements ProductGroupService {

	@Resource
	private ProductGroupDao productGroupDao;

	@Resource
	private DocumentService documentService;
	
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private ProductService productService;
	
	static final String MAX_OBJECT_ID_KEY_PREFIX = "PRODUCT_GROUP_MAX_OBJECT_ID";
	
	/**
	 * 默认maxObjectId的缓存存活时间，一个月
	 */
	static final long KEY_TTL =  3600 * 24 * 30 * 12;


	public int insert(ProductGroup productGroup) {
		return productGroupDao.insert(productGroup);
	}

	public int update(ProductGroup productGroup) {
		int actualRowsAffected = 0;

		long matchId = productGroup.getId();

		ProductGroup _oldCardMatch = productGroupDao.select(matchId);

		if (_oldCardMatch != null) {
			actualRowsAffected = productGroupDao.update(productGroup);
		}

		return actualRowsAffected;
	}

	public int delete(long productGroupId) {
		int actualRowsAffected = 0;

		ProductGroup _oldCardMatch = productGroupDao.select(productGroupId);

		if (_oldCardMatch != null) {
			actualRowsAffected = productGroupDao.delete(productGroupId);
		}

		return actualRowsAffected;
	}

	public ProductGroup select(long productGroupId) {
		return productGroupDao.select(productGroupId);
	}

	public List<ProductGroup> list(ProductGroupCriteria productGroupCriteria) {
		List<ProductGroup> list =  productGroupDao.list(productGroupCriteria);
		if(list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	public List<ProductGroup> listOnPage(ProductGroupCriteria productGroupCriteria) {
		List<ProductGroup> list  = productGroupDao.listOnPage(productGroupCriteria);
		if(list == null) {
			return Collections.emptyList();
		}
		return list;
	}


	@Override
	public int count(ProductGroupCriteria productGroupCriteria) {
		return productGroupDao.count(productGroupCriteria);
	}

	@Override
	public List<ProductGroup> listNextGroup(ProductGroupCriteria productGroupCriteria) {
		Assert.notNull(productGroupCriteria, "productGroupCriteria must not be null");		
		Assert.notNull(productGroupCriteria.getObjectType(), "productGroupCriteria objectType must not be null");		
		Assert.notNull(productGroupCriteria.getGroupValue(), "productGroupCriteria groupValue must not be null");		
		Assert.isTrue(productGroupCriteria.getObjectId() > 0, "productGroupCriteria objectId must not be null");		
		List<ProductGroup> list = productGroupDao.listNextGroup(productGroupCriteria);
		if(list == null) {
			return Collections.emptyList();
		}
		return list;

	}

	/**
	 * 根据上下级关系生成树形结构
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-12-12
	 */
	@Override
	public List<ProductGroup> generateTree(String objectType, long groupId) {
		ProductGroupCriteria productGroupCriteria = new ProductGroupCriteria(objectType, groupId);
		productGroupCriteria.setCurrentStatus(BasicStatus.normal.id);
		List<ProductGroup> plateList = this.list(productGroupCriteria);
		if(plateList == null){
			return Collections.emptyList();
		}


		return generateTree(plateList);

	}

	@Override
	public List<ProductGroup> generateTree(List<ProductGroup> plateList) {
		for(int i = 0; i< plateList.size(); i++){		
			for(int j = 0; j< plateList.size(); j++){
				if(plateList.get(j).getParentId() == plateList.get(i).getId()){
					if(plateList.get(i).getSubProductGroupList() == null){
						plateList.get(i).setSubProductGroupList(new ArrayList<ProductGroup>());
					}
					ProductGroup pg = plateList.get(j);
					if(pg.getObjectType() != null && pg.getObjectType().equals(ObjectType.document.name())) {
						Document document = documentService.select(NumericUtils.parseInt(pg.getGroupTarget()));
						if(document != null) {
							pg.setGroupTarget(document.getViewUrl());
						} else {
							logger.error("分组数据:{}对应的文档不存在", JSON.toJSONString(pg));
							pg.setGroupTarget(null);
						}
					}
					plateList.get(i).getSubProductGroupList().add(pg);
				}
			}

		}
		ArrayList<ProductGroup> nodeTree = new ArrayList<ProductGroup>();

		for(int i = 0; i< plateList.size(); i++){	
			ProductGroup pg = plateList.get(i);
			if(pg.getParentId() == 0) {
				if(pg.getObjectType() != null && pg.getObjectType().equals(ObjectType.document.name())) {
					Document document = documentService.select(NumericUtils.parseInt(pg.getGroupTarget()));
					if(document != null) {
						pg.setGroupTarget(document.getViewUrl());
					} else {
						logger.error("分组数据:{}对应的文档不存在", JSON.toJSONString(pg));
						pg.setGroupTarget(null);
					}
				}
				nodeTree.add(pg);
			}
		}
		return nodeTree;	
	}

	@Override
	public long createMaxObjectId(long ownerId) {
		long maxObjectId = readMaxObjectId(ownerId);
		if(maxObjectId < 0) {
			//缓存没有，从数据库读取
			maxObjectId = productGroupDao.readMaxObjectId(ownerId);
		}
		String key = MAX_OBJECT_ID_KEY_PREFIX + "#" + ownerId;
		long newMaxObjectId = centerDataService.increaseBy(key, 1, maxObjectId+1, KEY_TTL);
		
		
		return newMaxObjectId;
	}
	
	private long readMaxObjectId(long ownerId) {
		String key = MAX_OBJECT_ID_KEY_PREFIX + "#" + ownerId;
		
		String src = centerDataService.get(key);
		
		if(src == null) {
			return -1;
		} else {
			return NumericUtils.parseLong(src);
		}
		
		
		
	}


}

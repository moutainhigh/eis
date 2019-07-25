package com.maicard.product.utils;

import org.springframework.beans.BeanUtils;

import com.maicard.product.domain.ProductData;
import com.maicard.site.domain.DocumentData;

public class DataUtils {
	public static DocumentData productData2DocumentData(ProductData productData){
		if(productData == null){
			return null;
		}
		DocumentData documentData = new DocumentData(productData.getDataCode(), productData.getDataValue());
		documentData.setDisplayLevel(productData.getDisplayLevel());
		
		return documentData;
		
	}
	
	public static ProductData documentData2ProductData(DocumentData dd){
		if(dd == null){
			return null;
		}
		ProductData pd = new ProductData();
		BeanUtils.copyProperties(dd, pd);
		
		return pd;
		
	}
}

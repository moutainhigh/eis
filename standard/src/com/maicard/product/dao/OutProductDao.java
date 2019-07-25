package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.OutProductCriteria;
import com.maicard.product.domain.OutProduct;

public interface OutProductDao {

	List<OutProduct> list(OutProductCriteria outProductCriteria) throws DataAccessException;

}

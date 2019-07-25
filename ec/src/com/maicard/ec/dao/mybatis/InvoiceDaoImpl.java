package com.maicard.ec.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;





import com.maicard.ec.criteria.InvoiceCriteria;
import com.maicard.ec.dao.InvoiceDao;
import com.maicard.ec.domain.Invoice;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class InvoiceDaoImpl extends BaseDao implements InvoiceDao {


	@Override
	public int insert(Invoice invoice) {
		try{
			return 	 getSqlSessionTemplate().insert("Invoice.insert", invoice);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public int update(Invoice invoice) {
		return getSqlSessionTemplate().update("Invoice.update", invoice);
	}

	@Override
	public int delete(int invoiceId) {
		return getSqlSessionTemplate().delete("Invoice.delete", new Integer(invoiceId));
	}

	@Override
	public List<Invoice> list(InvoiceCriteria invoiceCriteria) {
		Assert.notNull(invoiceCriteria, "invoiceCriteria must not be null");		
		return getSqlSessionTemplate().selectList("Invoice.list", invoiceCriteria);	
	}

	@Override
	public List<Invoice> listOnPage(InvoiceCriteria invoiceCriteria) {
		Assert.notNull(invoiceCriteria, "invoiceCriteria must not be null");
		Assert.notNull(invoiceCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(invoiceCriteria);
		Paging paging = invoiceCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Invoice.list", invoiceCriteria, rowBounds);
	}

	@Override
	public int count(InvoiceCriteria invoiceCriteria) {
		Assert.notNull(invoiceCriteria, "invoiceCriteria must not be null");		
		return getSqlSessionTemplate().selectOne("Invoice.count", invoiceCriteria);	
	}

	@Override
	public Invoice select(int invoiceId) {
		return getSqlSessionTemplate().selectOne("Invoice.select", invoiceId);	
	}



}

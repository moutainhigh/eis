package com.maicard.ec.dao;

import java.util.List;

import com.maicard.ec.criteria.InvoiceCriteria;
import com.maicard.ec.domain.Invoice;


public interface InvoiceDao {
	public int insert(Invoice invoice);

	public int update(Invoice invoice);

	public List<Invoice> list(InvoiceCriteria invoiceCriteria);

	public List<Invoice> listOnPage(InvoiceCriteria invoiceCriteria);

	int delete(int invoiceId);
	
	int count(InvoiceCriteria invoiceCriteria);

	public Invoice select(int invoiceId);

}

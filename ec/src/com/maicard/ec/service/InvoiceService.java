package com.maicard.ec.service;

import java.util.List;

import com.maicard.ec.criteria.InvoiceCriteria;
import com.maicard.ec.domain.Invoice;



public interface InvoiceService {
	
	public Invoice select(int invoiceId);
	
	public int insert(Invoice invoice);
	
	public int update(Invoice invoice);
	
	public int delete(int invoiceId);
	
	public List<Invoice> list(InvoiceCriteria invoiceCriteria);

	int count(InvoiceCriteria productCriteria);

	public List<Invoice> listOnPage(InvoiceCriteria invoiceCriteria);

	public Invoice select(String invoiceCode);



}

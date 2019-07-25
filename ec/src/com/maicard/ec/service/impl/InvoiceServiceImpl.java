package com.maicard.ec.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;






import com.maicard.ec.criteria.InvoiceCriteria;
import com.maicard.ec.dao.InvoiceDao;
import com.maicard.ec.domain.Invoice;
import com.maicard.ec.service.InvoiceService;

import com.maicard.common.base.BaseService;

@Service
public class InvoiceServiceImpl  extends BaseService implements InvoiceService{

	@Resource
	private InvoiceDao invoiceDao;


	@Override
	public int insert(Invoice invoice) {
		// TODO Auto-generated method stub
		try{
			invoiceDao.insert(invoice);
		}
		catch(Exception e){
			return 0;
		}
		return 1;
	}
	@Override
	public int update(Invoice invoice) {
		int actualRowsAffected = 0;
		int id = invoice.getInvoiceId();
		Invoice _oldInvoice=invoiceDao.select(id);
		if (_oldInvoice !=null){
			try{
				actualRowsAffected=invoiceDao.update(invoice);
			}
			catch(Exception e){return 0;}
		}
		return actualRowsAffected;
	}
	@Override
	public List<Invoice> list(InvoiceCriteria invoiceCriteria) {
		return invoiceDao.list(invoiceCriteria);
	}
	@Override
	public int count(InvoiceCriteria productCriteria) {
		// TODO Auto-generated method stub
		try{
			return invoiceDao.count(productCriteria);
		}
		catch(Exception e){}
		return 0;
	}
	@Override
	public List<Invoice> listOnPage(InvoiceCriteria invoiceCriteria) {
		return invoiceDao.listOnPage(invoiceCriteria);
	}
	@Override
	public Invoice select(String invoiceCode) {
		InvoiceCriteria invoiceCriteria = new InvoiceCriteria();
		invoiceCriteria.setInvoiceCode(invoiceCode);
		List<Invoice> invoiceList= list(invoiceCriteria);
		logger.debug("[" + invoiceCode + "]得到的画册数量是:" + (invoiceList == null ? -1 : invoiceList.size()));
		if(invoiceList == null || invoiceList.size() != 1){
			return null;
		}	
		return invoiceList.get(0);
	}

	@Override
	public Invoice select(int invoiceId) {
		return invoiceDao.select(invoiceId);
	}
	@Override
	public int delete(int invoiceId) {
		return invoiceDao.delete(invoiceId);
	}



}

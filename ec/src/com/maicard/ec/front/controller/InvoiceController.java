package com.maicard.ec.front.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ContentPaging;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.maicard.ec.criteria.InvoiceCriteria;
import com.maicard.ec.domain.Invoice;
import com.maicard.ec.service.InvoiceService;
import com.maicard.money.domain.Pay;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.service.DocumentService;

@Controller
@RequestMapping("/invoice")
public class InvoiceController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private DocumentService documentService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private InvoiceService invoiceServer;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayService payService;
	@Resource
	private PayMethodService payMethodService;
	@Resource
	private PayTypeService payTypeService;
	@Resource
	private ConfigService configService;
	//查询指定交易ID的支付状态
	@RequestMapping(value="/query/{transactionId}",method=RequestMethod.GET )
	public String query(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("transactionId") String transactionId) throws Exception {

		logger.debug("查询支付订单[" + transactionId + "]");
		Pay pay = payService.select(transactionId);
		if(pay == null){
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在"));
		} else {
			EisMessage msg = new EisMessage();
			msg.setOperateCode(pay.getCurrentStatus());
			if(msg.getOperateCode() == TransactionStatus.success.getId()){
				msg.setMessage("交易成功");
			}
			if(msg.getOperateCode() == TransactionStatus.failed.getId()){
				msg.setMessage("交易失败");
			}

			map.put("message", msg);
		}
		return CommonStandard.frontMessageView;
	}

	//增
	@RequestMapping (value="/add",method=RequestMethod.POST)
	public String add(HttpServletRequest  request, HttpServletResponse response, ModelMap map, 		
			Invoice invoice
			) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "用户未登录"));
			return CommonStandard.frontMessageView;
		}	

		//String FILE_PATH =configService.getValue(DataName.documentUploadSaveDir.toString(), frontUser.getOwnerId())+"/";
		if(invoice.getData() == null){
			invoice.setData(new HashMap<String,String>());
		}
		/*if(taxRegistrationCertificatecImage != null){
			String filename1=FILE_PATH+"TaxRegistration_"+frontUser.getUuid();
			invoice.getData().put(DataName.taxRegistrationCertificatecImage.toString(), filename1);
			uploadFile(taxRegistrationCertificatecImage, filename1, FILE_PATH); 
		}

		if(generalTaxPayerCetificateImage != null){
			String filename2=FILE_PATH+"GeneralTaxPayer_"+frontUser.getUuid();
			invoice.getData().put(DataName.generalTaxPayerCetificateImage.toString(), filename2);

			uploadFile(generalTaxPayerCetificateImage, filename2, FILE_PATH); 
		}*/
		invoice.setUuid(frontUser.getUuid());
		map.put("message",new EisMessage(OperateResult.success.getId(),"插入成功"));
		int r=1;
		{
			r=invoiceServer.insert(invoice);
			logger.info("insert complate,r="+r);
		}
		if (r==0) {
			map.put("message",new EisMessage(OperateResult.failed.getId(),"插入失败"));
		}
		return CommonStandard.frontMessageView;
	}
	//删
	@RequestMapping (value="/delete/{invoiceId}",method=RequestMethod.GET)
	public String delete (HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("invoiceId") int invoiceId) throws Exception {
		//int uuid=ServletRequestUtils.getIntParameter(request, "uuid");
		map.put("message",new EisMessage(OperateResult.success.getId(),"删除成功"));
		try{
			invoiceServer.delete(invoiceId);
		}
		catch (Exception e){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"删除失败"));
		}
		return CommonStandard.frontMessageView;
	}

	@RequestMapping (value="/update/{invoiceId}",method=RequestMethod.GET)
	public String edit (HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("invoiceId") int invoiceId) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "用户未登录"));
			return CommonStandard.frontMessageView;
		}
		logger.info("提到的invoiceID是"+invoiceId);
		Invoice invoice = invoiceServer.select(invoiceId);
		logger.info("得到的地址信息是"+invoice.getContactPhone());
		map.put("invoice", invoice);
		return "invoice/update";	
	}	
	//改
	@RequestMapping (value="/update",method=RequestMethod.POST)
	public String update (HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@ModelAttribute("invoice") Invoice invoice) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}		
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "用户未登录"));
			return CommonStandard.frontMessageView;
		}		
		if(invoice.getUuid() != frontUser.getUuid()){
			logger.error("尝试修改的Invoice的UUID[" + invoice.getUuid() + "]与当前登录用户[" + frontUser.getUuid() + "]不一致");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "用户未登录"));
			return CommonStandard.frontMessageView;
		}
		try{
			invoiceServer.update(invoice);
		}
		catch(Exception e){
			map.put("message",new EisMessage(OperateResult.success.getId(),"更新失败"));
		}
		return CommonStandard.frontMessageView;
	}
	@RequestMapping (value="/list",method=RequestMethod.GET)
	public String List (HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));
			return CommonStandard.frontMessageView;
		}		
		int rowsPerPage = 3;
		int currentPage = 1;
		logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+currentPage+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		InvoiceCriteria invoiceCriteria=new InvoiceCriteria();
		invoiceCriteria.setUuid(frontUser.getUuid());
		Paging page = new Paging(rowsPerPage);
		invoiceCriteria.setPaging(page);
		invoiceCriteria.getPaging().setCurrentPage(currentPage);
		int totalRows = invoiceServer.count(invoiceCriteria);
		ContentPaging documentContentPaging = new ContentPaging(totalRows);
		documentContentPaging.setRowsPerPage(rowsPerPage);
		documentContentPaging.setCurrentPage(currentPage);
		documentContentPaging.setDisplayPageCount(totalRows);
		documentContentPaging.caculateDisplayPage();
		List<Invoice> invoice = invoiceServer.listOnPage(invoiceCriteria);
		map.put("documentContentPaging", documentContentPaging);
		map.put("rowsPerPage",rowsPerPage);
		map.put("currentPage",currentPage);
		map.put("invoice", invoice);
		return "invoice/list";	
	}	
	//查
	@RequestMapping (value="/list_{pageSetting}",method=RequestMethod.GET)
	public String List1 (HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("pageSetting") String pageSetting) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));
			return CommonStandard.frontMessageView;
		}		
		int rowsPerPage = 3;
		int currentPage = 1;
		if(pageSetting.matches("^\\d+_\\d+$")){
			String[] data = pageSetting.split("_");
			rowsPerPage = Integer.parseInt(data[0]);
			currentPage = Integer.parseInt(data[1]);
		} else 	if(pageSetting.matches("^\\d+$")){
			rowsPerPage = 3;
			currentPage = Integer.parseInt(pageSetting);
		} else {
			rowsPerPage = 3;
			currentPage = 1;		
		}
		logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+currentPage+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		InvoiceCriteria invoiceCriteria=new InvoiceCriteria();
		invoiceCriteria.setUuid(frontUser.getUuid());
		Paging page = new Paging(3);
		invoiceCriteria.setPaging(page);
		invoiceCriteria.getPaging().setCurrentPage(currentPage);
		int totalRows = invoiceServer.count(invoiceCriteria);
		ContentPaging documentContentPaging = new ContentPaging(totalRows);
		documentContentPaging.setRowsPerPage(rowsPerPage);
		documentContentPaging.setCurrentPage(currentPage);
		documentContentPaging.setDisplayPageCount(2);
		documentContentPaging.caculateDisplayPage();
		List<Invoice> invoice = invoiceServer.listOnPage(invoiceCriteria);
		//List<Invoice> invoice =invoiceServer.list(invoiceCriteria);
		map.put("documentContentPaging", documentContentPaging);
		map.put("rowsPerPage",rowsPerPage);
		map.put("currentPage",currentPage);
		map.put("invoice", invoice);
		return "invoice/list";	
	}
	@RequestMapping (value="/setDefault/{invoiceId}",method=RequestMethod.GET)
	public String setDefaultAdd(HttpServletRequest request, HttpServletResponse response, ModelMap map,@PathVariable("invoiceId") int invoiceId)throws Exception 
	{
		Invoice invoice=new Invoice();
		invoice.setInvoiceId(invoiceId);
		/*if (invoiceServer.setDefaultAdd(invoice)>0) {
			return ":true";
		}
		else{
			return "false";
		}*/
		return "false";

	}

	public  void uploadFile(MultipartFile file,String fileName, String FILE_PATH) throws IOException {   
		logger.info("filename="+fileName+"xxxxxFILE_PATH="+FILE_PATH);
		File tempFile = new File(FILE_PATH,fileName);  
		if (!tempFile.getParentFile().exists()) {  
			tempFile.getParentFile().mkdir();  
		}  
		if (!tempFile.exists()) {  
			tempFile.createNewFile();  
		}  
		file.transferTo(tempFile);   
	}  
}
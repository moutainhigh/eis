package com.maicard.views;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractPdfStamperView;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfStamper;
import com.maicard.common.service.ApplicationContextService;

/**
 * 输出PDF文件
 * 使用itext
 * 
 * 
 * @author NetSnake
 * @date 2013-3-1
 */
public class FrontPdfView extends AbstractPdfStamperView {

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	@Resource
	private ApplicationContextService applicationContextService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void mergePdfDocument(Map<String,Object> modelMap, PdfStamper pdfStamper, HttpServletRequest request , HttpServletResponse response) throws Exception{
		
			
		String outputPdf =  null;
		if(modelMap.get("pdfFileName") != null){
			outputPdf = modelMap.get("pdfFileName").toString();
		}
		if(outputPdf == null){
			String[] uris = request.getRequestURI().split("/");
			outputPdf = uris[uris.length-1];
		}
		
		logger.debug("输出PDF文件:" + request.getRequestURI() + "/" + outputPdf);
		BaseFont bf=BaseFont.createFont( "STSong-Light",   "UniGB-UCS2-H",   BaseFont.NOT_EMBEDDED);

		
		response.setHeader("Content-Disposition", "attachment;filename=" + outputPdf);
//		response.setHeader("Content-Disposition", "attachment;filename=" + new String(modelMap.get("pdfFileName").toString().getBytes(), "ISO8859-1"));
		AcroFields fields = pdfStamper.getAcroFields();
		fields.addSubstitutionFont(bf);
		Map<String,String>fillMap = new HashMap<String,String>();
		if(modelMap.get("pdf_owner_name") != null && modelMap.get("pdf_owner_name") instanceof HashMap){
			fillMap = (HashMap)modelMap.get("pdf_owner_name"); 
		} else {
			fillMap.put("fill_part_a_name", "鬼哥");
		}
		for (String key : fillMap.keySet()) {
			String value = fillMap.get(key);
			fields.setField(key, value);
		}
		pdfStamper.setFormFlattening(true);

	}
}

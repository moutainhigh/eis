package com.maicard.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.maicard.common.base.BaseService;


public class DirectResponse extends BaseService{

	public void directResponse(HttpServletResponse response, String message,String templateRealPath) throws Exception{
		//logger.info("direct response...");
		File file = new File(templateRealPath);
		if(!file.exists()){
			throw new FileNotFoundException();
		}
		BufferedReader bufread= new   BufferedReader   (new   InputStreamReader(new   FileInputStream(templateRealPath),"UTF-8"));
		StringBuffer templateContent = new StringBuffer();
		String read = "";
		while((read=bufread.readLine()) != null)
		{
			templateContent.append(read);
		}	
		bufread.close();
		String content = templateContent.toString();
		content = content.replaceAll("\\$\\{message\\}", message);
		
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		
		writer.println(content);
		writer.flush();
		writer.close();
	}
}

package com.maicard.site.service;

import org.springframework.scheduling.annotation.Async;

//import org.springframework.scheduling.annotation.Async;

import com.maicard.site.domain.Document;

public interface DocumentPostProcessor {
	
	int process(Document document, String mode) throws Exception;
	
	@Async
	public void asyncProcess(Document document, String mode);


}

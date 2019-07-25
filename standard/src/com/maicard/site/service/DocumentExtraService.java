package com.maicard.site.service;
import com.maicard.site.domain.Document;

public interface DocumentExtraService {
		
	
	Document select(int udid);

	Document selectNoCache(int udid);
		


}

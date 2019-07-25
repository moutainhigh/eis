package com.maicard.common.base;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.common.service.ConfigService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;

public abstract class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected int ROW_PER_PAGE = 10;
	
	@Resource
	protected ConfigService configService;
	
	@PostConstruct
	public void init() {
		ROW_PER_PAGE = configService.getIntValue(
				DataName.partnerRowsPerPage.toString(), 0);
		if (ROW_PER_PAGE < 1) {
			ROW_PER_PAGE = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
		}
	}


}

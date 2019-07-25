package com.maicard.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagingUtils {
	
	protected static final Logger logger = LoggerFactory.getLogger(PagingUtils.class);

	
	public static final int DISPLAY_PAGE_COUNT = 5;

	public static ContentPaging generateContentPaging(int totalRows, int rowPerPage, int currentPage){
		ContentPaging contentPaging = new ContentPaging(totalRows);
		contentPaging.setRowsPerPage(rowPerPage);
		contentPaging.setCurrentPage(currentPage);
		contentPaging.setDisplayPageCount(DISPLAY_PAGE_COUNT);
		contentPaging.caculateDisplayPage();
		logger.debug("一共" + totalRows + "行数据, " +  contentPaging.getTotalPage() + "页，每页显示" + rowPerPage + "行数据,当前第" + currentPage + "页, 每页显示5个页码, 第一个页码:" + contentPaging.getDisplayFirstPage() + ",最后一个页码:" + contentPaging.getDisplayLastPage());
		return contentPaging;
	}
}

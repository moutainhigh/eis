package com.maicard.site.domain;

import java.io.Serializable;
//PDF等文档需要的目录
public class DocumentCatalog implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String title;
	private String author;
	private int page;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	

}

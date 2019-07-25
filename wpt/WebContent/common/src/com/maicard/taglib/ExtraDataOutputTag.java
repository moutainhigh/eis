package com.maicard.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.common.domain.EisObject;

public class ExtraDataOutputTag implements Tag  {

	protected static final Logger logger = LoggerFactory.getLogger(ExtraDataOutputTag.class);

	@SuppressWarnings("unused")
	private PageContext pageContext;
	@SuppressWarnings("unused")
	private Tag tag;
	private EisObject targetObject;



	@Override
	public int doEndTag() throws JspException {
		logger.debug("开始doEndTag");
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		return 0;
	}

	@Override
	public Tag getParent() {
		return null;
	}

	@Override
	public void release() {

	}

	@Override
	public void setPageContext(PageContext arg0) {
		logger.debug("开始setPageContext");
		this.pageContext = arg0;

	}

	@Override
	public void setParent(Tag arg0) {
		logger.debug("开始setParent设置tag");
		this.tag = arg0;

	}

	public EisObject getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(EisObject targetObject) {
		this.targetObject = targetObject;
	}




}

package com.maicard.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.common.util.MediaUtils;

/**
 * 用于判断一个来源是什么类型的文件
 * 如果是图片则显示为img
 * 否则显示一个链接
 *
 *
 * @author NetSnake
 * @date 2016年7月11日
 *
 */
public class MediaTag implements BodyTag  {

	protected static final Logger logger = LoggerFactory.getLogger(MediaTag.class);

	private PageContext pageContext;
	@SuppressWarnings("unused")
	private Tag tag;
	private BodyContent bc;



	@Override
	public int doEndTag() throws JspException {
		logger.debug("开始doEndTag");
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		if(this.bc == null || this.bc.getString() == null){
			return BodyTag.EVAL_BODY_BUFFERED;
		}

		JspWriter w = pageContext.getOut();
		if (w instanceof BodyContent) {
			w = ((BodyContent) w).getEnclosingWriter();
		}
		String content = this.bc.getString();
		logger.debug("开始处理:" + content);
		if(StringUtils.isBlank(content)){
			return BodyTag.EVAL_BODY_BUFFERED;
		}
		content = content.trim();
		StringBuffer sb = new StringBuffer();
		String[] data = content.split(",");
		for(String pic : data){
			if(MediaUtils.isPictue(content)){
				sb.append("<img src=\"").append(pic).append("\" alt=\"").append(pic).append("\" />");
			} else {
				sb.append("<a href=\"").append(pic).append("\" target=\"_blank\">").append(pic).append("</a>");
			}
			sb.append(' ');
		}
		logger.debug("输出:" + sb.toString());
		try {
			w.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return BodyTag.EVAL_BODY_BUFFERED;
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


	@Override
	public int doAfterBody() throws JspException {
		logger.debug("开始doAfterBody设置");
		return 0;
	}

	@Override
	public void doInitBody() throws JspException {
		logger.debug("开始doInitBody设置:" + 		this.bc.getString());

	}

	@Override
	public void setBodyContent(BodyContent arg0) {
		logger.debug("开始setBodyContent设置");
		this.bc = arg0;

	}

}

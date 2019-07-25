package com.maicard.common.base;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard.DataFetchMode;
import com.maicard.views.JsonFilterView;

public abstract class Criteria implements Serializable {

	protected static final long serialVersionUID = 1L;

	/**
	 * ORDERBY参数只允许字符、字母、空白和下划线，其他都是非法输入
	 */
	public static final String ORDER_BY_PATTERN = "^[\\s_a-zA-Z0-9]+$";

	@JsonView({JsonFilterView.Full.class})
	protected long ownerId;


	@JsonView({JsonFilterView.Full.class})
	@JsonIgnore
	protected Paging paging;

	@JsonView({JsonFilterView.Full.class})
	protected String mode;

	@JsonView({JsonFilterView.Full.class})
	protected String dataFetchMode = DataFetchMode.full.toString();


	@JsonView({JsonFilterView.Full.class})
	protected String tableName;


	protected int[] currentStatus;

	@JsonView({JsonFilterView.Partner.class})
	protected String orderBy;

	@JsonView({JsonFilterView.Full.class})
	protected String groupBy;

	public Criteria() {
		this(new Paging());
	}

	public Criteria(int maxResults, int pagingLinks) {
		this(new Paging(maxResults, pagingLinks));
	}

	public Criteria(Paging paging) {
		this.paging = paging;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDataFetchMode() {
		return dataFetchMode;
	}

	public void setDataFetchMode(String dataFetchMode) {
		this.dataFetchMode = dataFetchMode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int[] getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int... currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getOrderBy() {
		return orderBy;
		/*
		 * Pattern p = Pattern.compile( "_([a-z])" );
		Matcher m = p.matcher( orderBy );
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
		    m.appendReplacement(sb, m.group(1).toUpperCase());
		}
		m.appendTail(sb);		
		return sb.toString();
		 */
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public void setOrderBy(String orderBy) {
		if(orderBy == null || orderBy.trim().equals("")){
			return;
		}
		orderBy = StringUtils.uncapitalize(orderBy).replaceAll("DESC", "desc").replaceAll("ASC", "asc");
		if(!orderBy.matches(ORDER_BY_PATTERN)){
			System.out.println("Illegal ORDER BY input!无效的排序参数:" + orderBy);
			return;
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0, length = orderBy.length(); i < length; i++){
			char letter = orderBy.charAt(i);
			if(letter <= 90 && letter >= 65){	
				sb.append('_').append(Character.toLowerCase(letter));
			} else {
				sb.append(letter);
			}
		}	
		this.orderBy = sb.toString();
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

}

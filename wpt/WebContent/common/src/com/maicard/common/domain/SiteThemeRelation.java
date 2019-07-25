package com.maicard.common.domain;

/**
 * 主机/域名与对应的合作方及站点主题的关系
 * 也就是一个子域名，属于哪个合作方partner所有，由此确认这个合作方当前选择的站点主题是什么
 *
 *
 * @author NetSnake
 * @date 2017年1月25日
 *
 */
public class SiteThemeRelation extends EisObject{
	
	
	private static final long serialVersionUID = -2630145023566122660L;
	
	private int siteThemeRelationId;
	
	private String hostCode;
	
	private long uuid;
	
	private int themeId;
	
	private String themeCode;
	
	private String siteName;
	
	
	public SiteThemeRelation() {
	}
	
	public SiteThemeRelation(long ownerId) {
		this.ownerId = ownerId;
	}


	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"siteThemeRelationId=" + "'" + siteThemeRelationId + "'," + 
				"hostCode=" + "'" + hostCode + "'," + 
				"uuid=" + "'" + uuid + "'," + 
				"themeId=" + "'" + themeId + "'," + 
				"themeCode=" + "'" + themeCode + "'" + 
				"currentStatus=" + "'" + currentStatus + "'" + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}


	public int getSiteThemeRelationId() {
		return siteThemeRelationId;
	}


	public void setSiteThemeRelationId(int siteThemeRelationId) {
		this.siteThemeRelationId = siteThemeRelationId;
	}


	public String getHostCode() {
		return hostCode;
	}


	public void setHostCode(String hostCode) {
		this.hostCode = hostCode;
	}


	public long getUuid() {
		return uuid;
	}


	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


	public int getThemeId() {
		return themeId;
	}


	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}


	public String getThemeCode() {
		return themeCode;
	}


	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	

}

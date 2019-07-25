package com.maicard.common.domain;

/**
 * 用户所使用的界面主题
 *
 *
 * @author NetSnake
 * @date 2017年1月25日
 *
 */
public class Theme extends EisObject {

	private static final long serialVersionUID = -7637370140985931793L;

	
	private int themeId;
	
	/**
	 * 主题代码，也是主题对应资源的目录
	 */
	private String themeCode;
	
	private String themeName;
	
	private String themeDesc;
	
	/**
	 * 主题缩略图
	 */
	private String pic;
	
	/**
	 * 可以把主题分为用户主题、管理后台主题等类型
	 */
	private String themeType;

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

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeDesc() {
		return themeDesc;
	}

	public void setThemeDesc(String themeDesc) {
		this.themeDesc = themeDesc;
	}

	public String getThemeType() {
		return themeType;
	}

	public void setThemeType(String themeType) {
		this.themeType = themeType;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	

}

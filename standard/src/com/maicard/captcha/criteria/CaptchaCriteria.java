package com.maicard.captcha.criteria;

import com.maicard.common.base.Criteria;

public class CaptchaCriteria extends Criteria{
	private static final long serialVersionUID = 494717925659158220L;
	
	private int captchaId;
	private String characters;
	private int maxLength;
	private int minLength;
	private int foreColor;
	private int backColor;
	private int imageWidth;
	private int imageHeight;
	private String imageName;
	private String imageFormat;
	private String word;
	private String generator;
	private long supplier;
	private String checksum;
	private String identify;
	
	
	public int getForeColor() {
		return foreColor;
	}
	public void setForeColor(int foreColor) {
		this.foreColor = foreColor;
	}
	public int getBackColor() {
		return backColor;
	}
	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public String getImageFormat() {
		return imageFormat;
	}
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
	public String getCharacters() {
		return characters;
	}
	public void setCharacters(String characters) {
		this.characters = characters;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	public long getSupplier() {
		return supplier;
	}
	public void setSupplier(long supplier) {
		this.supplier = supplier;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public int getCaptchaId() {
		return captchaId;
	}
	public void setCaptchaId(int captchaId) {
		this.captchaId = captchaId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}

}

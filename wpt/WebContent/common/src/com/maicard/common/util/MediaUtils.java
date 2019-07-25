package com.maicard.common.util;

public class MediaUtils {
	public static final String[] IMAGE_TYPE = new String[]{"jpg","png","gif","jpeg","tif","tiff","bmp"};
	
	public static boolean isPictue(String fileName){
		for(String image : IMAGE_TYPE){
			if(fileName.toLowerCase().endsWith(image)){
				return true;
			}
		}
		return false;
		
	}

}

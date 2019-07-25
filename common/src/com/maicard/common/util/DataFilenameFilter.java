package com.maicard.common.util;

import java.io.*;

public class DataFilenameFilter implements FilenameFilter{
	private String fileType;

	public DataFilenameFilter(String fileType){
		this.fileType = fileType;
	}

	public boolean accept(File fl,String path){
		return path.endsWith(this.fileType);  
	}

}

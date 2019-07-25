package com.maicard.wpt.domain;

public class ScanCodeInfo {

	private String ScanType;
	
	private String ScanResult;

	public String getScanType() {
		return ScanType;
	}

	public void setScanType(String scanType) {
		ScanType = scanType;
	}

	public String getScanResult() {
		return ScanResult;
	}

	public void setScanResult(String scanResult) {
		ScanResult = scanResult;
	}
	
	@Override
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"ScanType=" + "'" + ScanType + "'," + 
				"ScanResult=" + "'" + ScanResult + "'" + 

				")";
	}
}

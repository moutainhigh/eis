package com.maicard.common.domain;

public class Dict extends EisObject {


	private static final long serialVersionUID = -111118702957325251L;

	private int dictId = 0;

	private String dictData;
	
	public Dict() {
	}

	public int getDictId() {
		return dictId;
	}

	public void setDictId(int dictId) {
		this.dictId = dictId;
	}

	public String getDictData() {
		return dictData;
	}

	public void setDictData(String dictData) {
		this.dictData = dictData;
	}

}

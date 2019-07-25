package com.maicard.wpt.domain;

public class CouponModelDateInfo {
	
protected String type;
	
	private String begin_timestamp;
	
	private String end_timestamp;

	private String fixed_term;

	private String fixed_begin_term;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBegin_timestamp() {
		return begin_timestamp;
	}

	public void setBegin_timestamp(String begin_timestamp) {
		this.begin_timestamp = begin_timestamp;
	}

	public String getEnd_timestamp() {
		return end_timestamp;
	}

	public void setEnd_timestamp(String end_timestamp) {
		this.end_timestamp = end_timestamp;
	}

	public String getFixed_term() {
		return fixed_term;
	}

	public void setFixed_term(String fixed_term) {
		this.fixed_term = fixed_term;
	}

	public String getFixed_begin_term() {
		return fixed_begin_term;
	}

	public void setFixed_begin_term(String fixed_begin_term) {
		this.fixed_begin_term = fixed_begin_term;
	}


}

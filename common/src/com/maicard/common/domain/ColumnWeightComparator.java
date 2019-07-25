package com.maicard.common.domain;

import java.io.Serializable;
import java.util.Comparator;

public class ColumnWeightComparator implements Comparator<Column>,Serializable{
	

	private static final long serialVersionUID = -562348579192617775L;

	@Override
	public int compare(Column o1, Column o2) {
		if(o1.getWeight() >= o2.getWeight()){
			return -1;
		}
		return 1;
	}
	
}
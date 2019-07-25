package com.maicard.standard.ts;

public enum PaddingPolicy {
	notPadding(0,"不补单"),
	fullPadding(1,"全额补单"),
	paddingWhenHalfSuccess(2,"当部分成功时进行补单"),
	addTimeAndWeightWhenHalfSuccess(3,"当部分成功时延长时间增加优先级"),
	addTimeForce(4,"无条件延长时间，不增加优先级");
	
	private final int id;
	private final String name;
	private PaddingPolicy(int id, String name){
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	@Override
	public String toString(){
		return this.name + "[" + this.id + "]" + "[" + this.getClass().getSimpleName() + "]";
	}	
	public PaddingPolicy findById(int id){
		for(PaddingPolicy value: PaddingPolicy.values()){
			if(value.getId() == id){
				return value;
			}
		}
		return notPadding;
	}
}

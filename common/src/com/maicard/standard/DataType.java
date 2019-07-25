package com.maicard.standard;

public enum DataType {
	builtIn(100013, "内建"),
	custom(100014,"自定义"),
	unique(0,"唯一"),
	multi(1,"并存"),
	globalUnique(2,"全局唯一");

	private final int id;
	private final String name;
	private DataType(int id, String name){
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
}

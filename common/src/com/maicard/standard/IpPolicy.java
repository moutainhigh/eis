package com.maicard.standard;

public enum IpPolicy {
	unknown(0,"unknown","未知策略，自行处理"),
	keep(1,"keep","保留当前IP"),
	change(2,"change","更换IP"),
	changeArea(3,"changeArea","更换IP地区"),
	changeBothSection(4,"changeBothSection","更换IP地址的所有4个段");	
	
	private int id;
	private String code;
	private String name;
	
	private IpPolicy(int id, String code, String name){
		this.id = id;
		this.code = code;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	
	public IpPolicy findById(int id){
		for(IpPolicy ipPolicy : IpPolicy.values()){
			if(ipPolicy.getId() == id){
				return ipPolicy;
			}
		}
		return null;
	}



}

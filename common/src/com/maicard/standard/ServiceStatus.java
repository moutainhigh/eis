package com.maicard.standard;


public enum ServiceStatus {
	waitingOpen(122001,"等待开放"),
	opening(122002,"正常开放"),
	maintain(122003,"维护中"),
	closed(122004,"已关闭"),
	updating(122005,"更新中"),
	restarting(122006,"重启中"),
	installing(122007,"安装中"),
	autoClose(122008,"自动关闭");

	public final int id;
	public final String name;
	private ServiceStatus(int id, String name){
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
	public ServiceStatus findById(int id){
		for(ServiceStatus value: ServiceStatus.values()){
			if(value.getId() == id){
				return value;
			}
		}
		return null;
	}
}

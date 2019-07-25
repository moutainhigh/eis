package com.maicard.standard;

public interface SiteStandard {
	//文档状态
	public static enum DocumentStatus{
		unknown(0,"未知"),
		drift(130001,"草稿"),
		newAdd(130002,"新增尚未发布"),
		depAccept(130004,"交下一环节"),
		published(130005,"已发布"),
		timeUp(130006,"已到期"),
		close(130007,"已关闭"),
		interrupt(130008,"已中止"),
		abnormal(130009,"异常"),
		inProgress(130010,"处理中"),
		deleted(130011,"已删除");

		private final int id;
		private final String name;
		private DocumentStatus(int id, String name){
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
		public DocumentStatus findById(int id){
			for(DocumentStatus value: DocumentStatus.values()){
				if(value.getId() == id){
					return value;
				}
			}
			return unknown;
		}
	}
	
	public static enum DocumentTypes {
		normal(171001, "普通文档"),
		system(171002,"系统文档"),
		link(171003,"链接文档"),
		suggest(171005,"推荐业务"),
		kaifu(171008,"开服表"),
		product(171006,"产品文档"),
		activity(171010,"活动文档");
		private final int id;
		private final String name;
		private DocumentTypes(int id, String name){
			this.id = id;
			this.name = name;
		}
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
		
	}

	public static enum NodeType{
		normal(1, "普通"),
		hiddenInPath(2,"不在路径中显示"),
		hiddenAll(3,"不可访问"),
		business(4,"业务节点"),
		navigation(5,"导航节点");

		public final int id;
		public final String name;
		private NodeType(int id, String name){
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

	//文档路径
	public static enum SitePath{
		contentPrefix("content/"),
		documentPrefix(""),
		createIndexFile("true"); //是否创建目录index

		private final String name;
		private SitePath(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString(){
			return name;
		}
	}


	
	public static final int defaultDocumentFetchCount = 30;
	public static enum  VirtualDocument{
		xsk,
		quickstart,
		miniLogin,
		miniServer;
	}


}

/* 

	SearchField 
	written by Alen Grakalic, provided by Css Globe (cssglobe.com)
	please visit http://cssglobe.com/post/1202/style-your-websites-search-field-with-jscss/ for more info
	
*/

this.searchfield = function(){
	
	// CONFIG 
	
	// this is id of the search field you want to add this script to. 
	// You can use your own id just make sure that it matches the search field in your html file.
	var id = "searchfield";
	
	// Text you want to set as a default value of your search field.
	var defaultText = "请输入关键字，进行搜索...";	
	
	// set to either true or false
	// when set to true it will generate search suggestions list for search field based on content of variable below
	var suggestion = true;
	
	// static list of suggestion options, separated by comma
	// replace with your own
	var suggestionText = "阿凡达之恋, 阿凡龙, 艾尔之光, 艾米尔编年史, 暗黑纪元, 傲神传, 傲世, 百度网页游戏, 百年战争, 白蛇传说, 白蛇传, 碧雪情天, 暴风网页游戏, 霸王, 八仙过海, 八仙外传, 八泽网页游戏, 霸者无双, 边锋茶苑游戏…, 飚车世界, 兵王, 8090网页游戏, 彩虹岛, 沧海, 苍穹之怒, 苍生, 苍天, 侠义道, 长江七号, 超级跑跑, 超级舞者, 超级西西三国, 成吉思汗2, 成吉思汗怀旧版, 成吉思汗盛大版, 赤壁, 重返神州, 宠物森林, 宠物小精灵, 传世群英传, 创世2, 创世, 创世西游, 传奇3, 传奇归来, 传奇世界, 传奇外传, 传奇续章, 传说, 穿越火线, 出发, 楚河汉界, 春秋传, 春秋外传, 大冲锋, 大海战Ⅱ, 大汉龙腾, 大话红楼, 大话水浒, 大话仙剑, 大话西游Ⅱ, 大话西游Ⅲ, 大话西游之战歌, 大话战国归来, 大剑, 大明龙权, 弹弹堂专区, 弹头奇兵, 刀剑2, 刀剑英雄, 刀剑笑, 大宋, 大唐豪侠, 大唐豪侠外传, 大唐无双, 大玩网页游戏, 帝国传奇, 第九大陆, 地盘网网页游戏, 丢丢, 地下城与勇士, 地下城守护者, 东邪西毒, 东邪西毒2, 东游记, 斗神, 独孤九剑, 夺宝传世, 多玩网页游戏, 都市快打, 29网页游戏, 2918网页游戏, 恶魔法则3, 恶魔法则盛大版, 2061, 恶魔法则, 侠网页游戏, 反恐精英, 反恐行动, 凡人修仙传, 凡人修仙传蝌…, 飞天风云, 飞天西游专区, 飞雪网页游戏, 风暴战区, 烽火大唐, 烽火情缘, 烽火情缘2, 烽火战国, 疯狂赛车, 疯狂石头, 风色幻想, 风色群英传, 封神, 封神榜, 封神榜国际版, 封神榜叁, 封神榜Ⅱ, 封神世界, 封神演义之前传, 封神争霸, 凤舞天骄, 风行网页游戏, 封印之剑, 风云, 风云横扫千军, 风云网页游戏, 风云之问鼎天下, 风云之武魂传说, 风云之雄霸天下, 风云之诸神之战, 风雨寻秦, 纷争, 封之神, 2, 复仇, 富甲西游, 2网页游戏, 高尔夫达人, 个个玩网页游戏, 跟我玩网页游戏, 功夫小子, 功夫世界, 功夫英雄, 劲舞团2, 光之冒险, 鬼吹灯, 鬼吹灯外传, 股市大亨, 古域, 2, 海魂, 海战传奇, 海战集结号, 海之乐章启航, 海之梦, 航海世纪, 汉武大帝, 浩方游戏专区, 好玩网页游戏, 黑暗帝国, 核金风暴, 火力突击, 洪荒, 红楼梦, 红颜, 后宫计, 欢畅网页游戏, 欢动世界网页…, 黄易群侠传, 幻灵游侠, 幻魔之眼, 幻魔之眼（盛…, 幻兽大陆, 幻想大陆, 幻想封神, 华夏Ⅱ, 华夏免费版, 华夏, 华夏前传, 火凤燎原, 火玩网页游戏, 网页游戏, , 九鼎传说, 92好玩网页游戏, 江湖行, 将军令, 江山美人, 将星录:群雄, 剑侠情缘Ⅱ, 剑圣, 剑舞江南, 剑舞江南怀旧版, 剑侠传奇, 剑侠贰外传, 剑仙, 剑侠情缘, 剑侠情缘Ⅲ盛…, 剑侠时代, 剑侠世界, 剑侠世界盛大版, 剑侠世界新浪版, 剑侠情缘Ⅲ, 剑雨, 剑雨天下, 街头篮球2, 街头篮球, 界王, 极光世界, 极光世界盛大版, 机甲世纪, 机甲世纪革新版, 机甲世纪, 金刚8号, 劲唱团, 精灵传说, 精灵复兴, 精灵牧场, 精灵乐章悠游版, 惊天动地, 惊天动地2, 晶铁之门, 劲舞团, 锦衣卫, 极速轮滑, 九城网页游戏, 九界, 九刃, 9377网页游戏, 九维网网页游戏, 九阴真经, 91玩网页游戏, 九州, 机战, 机战奋斗版, 激战海陆空, 网页游戏, 96网页游戏, 01, 绝地反击, 绝对火力, 绝对女神Ⅱ, 决战, 决战双城, 巨刃, 巨人, 巨人双版测试, 巨人网页游戏, 巨人, 聚仙, 巨星, 聚游网页游戏, 95网页游戏, 开创世纪, 开心, 开心大陆, 开心网001, 开心网页游戏, 抗战2, 抗战英雄传, 科洛斯风云, 空战世纪, 口袋西游, 快乐神仙, 快乐无双, 快乐营网页游戏, 快玩网页游戏, 酷狗网页游戏, 昆仑, 昆仑万维网页…, 酷我网页游戏…, 蓝港网页游戏, 狼队, 浪漫唐, 浪漫庄园, 4399浪漫庄园, 蓝海战记, 蓝空, 篮球也疯狂, 乐都网页游戏, 雷电, 雷霆, 乐土专区, 乐子网网页游戏, 恋爱盒子, 亮剑, 烈焰飞雪, 炼狱2, 炼狱, 联众世界, 联众游戏专区, 聊斋, 猎国, 猎刃, 烈焰行动, 零纪元, 六道2, 6711网页游戏, 六圣群侠传, 流星蝴蝶剑, 龙, 龙的传人, 龙魂, 龙盛大版, 龙腾世界, 龙纹, 龙之大陆, 龙之谷, 龙之梦, 乱世, 鹿鼎记, 2, 洛奇, 洛奇英雄传, 绿色征途腾讯版, 绿色征途, 麻辣江湖, 蛮荒, 漫游, 冒险岛, 梦幻穿越, 梦幻迪士尼, 梦幻国度, 梦幻江湖, 梦幻昆仑, 梦幻聊斋, 梦幻龙族, 梦幻魔兽, 梦幻情缘, 梦幻群侠传, 梦幻蜀山, 梦幻问情, 梦幻星球, 梦幻西游, 梦回山海, 梦境之城, 梦三国, 梦想岛, 梦想世界, 梦幻古龙, 灭神, 明朝演义, 名将, 名将2, 名将三国, 冥王神话, 命运之轮, 军魂, 魔霸, 魔法之门, 魔界2, 魔戒传奇, 魔卡精灵, 魔卡, 魔力宝贝, 魔力宝贝Ⅱ, 魔力岛网页游戏, 魔龙战纪, 魔骑士, 魔神无双, 魔神争霸(天机), 魔神传, 魔兽世界(国服), 墨香, 魔钥, 魔域, 魔域（版）, 魔域（掉钱版）, 魔域（怀旧版）, 魔咒2, 魔咒, , 2 , 逆天, 牛网页游戏, 诺诺网页游戏, 诺亚传说, 拍拍部落, 潘多拉, 盘古战纪, 跑跑卡丁车, 泡泡堂, 泡泡游戏, 泡泡战士, 蓬莱, 飘流幻境, 网页游戏, 远征, 网页游戏, 乾坤在线, 千年3, 倩女幽魂, 千橡网页游戏, 千寻, 七宝网页游戏, 起凡游戏平台, 奇迹, 七剑, 奇迹世界2, 77网页游戏, 76175网页游戏, 奇门, 清风武易, 倾国倾城, 秦伤免费版, 秦伤时间版, 秦殇世界, 秦始皇, 7366网页游戏, 其他, 奇侠传, 七雄争霸, 飞车, 飞行岛, 封神记, 幻想, 幻想世界, 华夏, 九仙, 空间, 魔域, 群, 三国, 堂, 仙境, 仙侠传, 西游, 炫舞, 英雄岛, 音速, 自由幻想, 岐山, 全民足球, 全球使命, 群英赋, 趣玩网网页游戏, 2, 热斗传说, 热血传奇, 人人网, 人人网网页游戏, 忍者世界, 热舞派对百度版, 热舞派对, 热血江湖, 热血昆仑, 热血骑士, 热血世界, 热血天骄, 热血天下, 热血西游, 热血战队, －冲锋, 2144网页游戏, 三国策Ⅴ, 三国鼎立, 三国群英传, 三国群英传2, 三国杀, 三国战魂, 三界奇缘, 三界西游, 360网页游戏, 3737网页游戏, 37网页游戏, 37网页…, 敢达, 杀毒软件, 商国网络网页…, 上古神殿, 上海滩, 山海奇缘, 山海志, 闪投部落, 神兵传奇, 神兵传奇盛大版, 神雕, 圣道传奇, 盛大网页游戏, 圣斗士, 圣斗士2, 生化战场, 神迹（奇浪特…, 圣灵传说, 圣龙传, 神魔大陆, 圣魔之血, 盛世传奇, 盛世, 盛世三国, 神鬼传奇, 神鬼世界, 生肖传说, 生肖外传, 神话, 神迹·仙魔道, 神迹贰, 神魔传, 神墓, 神泣, 神骑世界, 神兽, 神武, 神仙, 神仙世界, 神仙传, 神州, 十二封印, 十二之天贰, 十虎, 世纪天成网页…, 时空战记, 什么什么大冒…, 石器2012, 石器时代, 石器时代2, 弑神, 守护之剑, 手机网游专区, 兽血沸腾, 蜀山新传, 水浒传2, 水浒无双, 水浒传, 水晶物语, 书剑恩仇录, 蜀门, 蜀山神话, 丝路传说, 4399网页游戏, 死神之刃, 隋唐演义, 坦克世界, , 桃花源记, 淘米网页游戏, 桃园, 腾龙传说, 腾讯, 腾讯网页游戏, 特勤队2, 特种部队, 天朝, 天道, 天地决, 甜豆网页游戏, 天黑请闭眼, 天境, 天骄Ⅱ, 天骄, 天迹, 天空战记, 天龙八部3, 天命, 天魔传说, 天上碑, 天使之恋, 天堂, 天堂2, 天堂梦, 天外飞仙, 天下3, 天涯, 天翼决, 天元, 天源4591, 天之传说, 天之痕, 天之炼狱, 天之游侠, 天子, 天子传奇, 铁血丹心, 铁血迷情, 铁血迷情完美版, 铁血三国志, 铁血星球, 投名状, 突击风暴, 吞食天地2 , 游戏竞技平台, 网络电话卡专区, 网球宝贝, 网球小妹, 王权, 网页游戏专区, 网易网页游戏, 王者大陆, 王者世界, 完美传奇, 完美国际, 完美世界, 53网页游戏, 万王之王3, 万王之王3腾…, 薇拉网页游戏, 唯舞独尊, 问道, 问道外传, 问鼎, 5.网页游戏, 56网页游戏, 我的小傻瓜, 蜗牛网页游戏, 我玩网网页游戏, 5网页游戏, 舞街区, 悟空传, 武林群侠传, 武林秘籍, 武林群侠传2, 武林外传, 武林英雄, 武林至尊, 武神, 巫师之怒, 武侠无双, 舞型舞秀, 武易, 武易传奇, 武易传奇, 51网页游戏, 51网页游戏, 五岳乾坤, 武魂, 侠客列传, 侠客无双, 侠客行, 降龙之剑, 降龙之剑极致版, 仙剑, 仙剑神曲, 仙界传, 仙境传说, 仙境冒险, 仙侣奇缘二, 仙侣奇缘3, 仙侣外传, 仙魔传奇, 仙, 仙蛇传, 仙途, 仙元天下, 仙尊, 笑傲江湖, 小游戏平台, 笑闹天宫, 逍遥传说, 逍遥界, 侠义道(官方), 侠义道Ⅱ(官方), 侠义世界, 暇月战歌, 邪风曲, 新三国群英传…, 新苍天, 新传奇外传, 心动网页游戏, 新飞飞, 星辰变, 星尘传说, 星尘传说魅…, 新封印传说, 新海盗王, 星际争霸, 星际家园, 新鬼吹灯, 星月传说, 星战前夜, 新浪网页游戏, 新浪游戏专区, 新龙骑士, 新龙影, 新龙族, 新梦幻诛仙, 新密传, 新魔界, 新魔界国际版, 新破天一剑, 新奇迹世界, 新蜀山剑侠, 新热血英豪, 新神界, 新天骄, 新天下无双怀…, 新天下无双之火, 新天羽传奇, 新侠义道(中…, 51新炫舞, 信仰, 新英雄年代, 新倚天剑与屠…, 新倚天王者…, 新战国英雄, 新魔界战神…, 西山001, 修魔, 希望, 西夏, 嬉戏飞车, 西游群英传, 西游记, 西游记百度版, 西游, 西游世界, 西游天下, 仙侣奇缘之天…, 玄界, 玄天之剑, 玄武, 炫舞吧, 炫舞吧Ⅱ, 轩辕传奇, 迅雷网页游戏, 迅雷游戏专区, 寻仙, 要玩网页游戏, 玩网页游戏, 英雄Ⅱ, 英雄联盟, 英雄美人, 英雄美人2, 英雄年代2, 英雄传, 英雄往事, 英雄无敌在线, 一起新游网页…, 17173游戏专区, 倚天2外传, 倚天Ⅱ自由世界, 倚天剑与屠龙…, 倚天剑与屠龙…, 倚天屠龙记, 倚天屠魔, 158网页游戏, 198网页…, 佣兵天下, 永恒之塔, 永久基地, 勇士, 勇者传说, 勇者斗斗龙, 勇者之翼, 游宝网页游戏, 游城天下网页…, 优玩网页游戏, 游窝网页游戏, 游戏风云网页…, 游戏谷网页游戏, 游戏人生, 游戏网网页游戏, 游艺世界网页…, 游艺世界游戏…, 游族网页游戏, 远航游戏中心, 远征, 远征(盛大版), 月光宝盒, 越野狂飙, 月影传说, 御龙在天, 预言2, 预言怀旧版, 预言经典版, 预言, 灾变, 战地风云, 战地之王, 战鼓传说, 战国群雄, 战神不败, 战神传, 战争前线, 真爱西游, 真封神, 征服, 征途, 征途2, 征途怀旧版, 征途时间版, 真·三国无双, 指环王, 纸片人, 中华龙塔, 中国游戏中心, 中华英雄, 中华英雄·国…, 终极火力, 众神之战, 中游互动网页…, 中游龙腾世界, 中游兽血外传, 中游网页游戏, 众游网页游戏, 壮志凌云, 诸侯, 诸侯海外国…, 煮酒天下, 逐浪网页游戏, 卓越之剑2, 诸神国度, 诛仙前传, 朱元璋, 自在飞车盛大版, 自在飞车, 纵横三国, 纵横时空, 纵横时空新疆版, 纵横时空诸…, 醉逍遥$$游戏装备&; 担保交易#,  XML schema"; 
	
	// END CONFIG (do not edit below this line, well unless you really, really want to change something :) )
	
	// Peace, 
	// Alen

	var field = document.getElementById(id);	
	var classInactive = "sf_inactive";
	var classActive = "sf_active";
	var classText = "sf_text";
	var classSuggestion = "sf_suggestion";
	this.safari = ((parseInt(navigator.productSub)>=20020000)&&(navigator.vendor.indexOf("Apple Computer")!=-1));
	if(field && !safari){
		field.value = defaultText;
		field.c = field.className;		
		field.className = field.c + " " + classInactive;
		field.onfocus = function(){
			this.className = this.c + " "  + classActive;
			this.value = (this.value == "" || this.value == defaultText) ?  "" : this.value;
		};
		field.onblur = function(){
			this.className = (this.value != "" && this.value != defaultText) ? this.c + " " +  classText : this.c + " " +  classInactive;
			this.value = (this.value != "" && this.value != defaultText) ?  this.value : defaultText;
			clearList();
		};
		if (suggestion){
			
			var selectedIndex = 0;
						
			field.setAttribute("autocomplete", "off");
			var div = document.createElement("div");
			var list = document.createElement("ul");
			list.style.display = "none";
			div.className = classSuggestion;
			list.style.width = field.offsetWidth + "px";
			div.appendChild(list);
			field.parentNode.appendChild(div);	

			field.onkeypress = function(e){
				
				var key = getKeyCode(e);
		
				if(key == 13){ // enter
					selectList();
					selectedIndex = 0;
					return false;
				};	
			};
				
			field.onkeyup = function(e){
			
				var key = getKeyCode(e);
		
				switch(key){
				case 13:
					return false;
					break;			
				case 27:  // esc
					field.value = "";
					selectedIndex = 0;
					clearList();
					break;				
				case 38: // up
					navList("up");
					break;
				case 40: // down
					navList("down");		
					break;
				default:
					startList();			
					break;
				};
			};
			
			this.startList = function(){
				var arr = getListItems(field.value);
				if(field.value.length > 0){
					createList(arr);
				} else {
					clearList();
				};	
			};
			
			this.getListItems = function(value){
				var arr = new Array();
				var src = suggestionText;
				var src = src.replace(/, /g, ",");
				var arrSrc = src.split(",");
				for(i=0;i<arrSrc.length;i++){
					if(arrSrc[i].substring(0,value.length).toLowerCase() == value.toLowerCase()){
						arr.push(arrSrc[i]);
					};
				};				
				return arr;
			};
			
			this.createList = function(arr){				
				resetList();			
				if(arr.length > 0) {
					for(i=0;i<arr.length;i++){				
						li = document.createElement("li");
						a = document.createElement("a");
						a.href = "javascript:void(0);";
						a.i = i+1;
						a.innerHTML = arr[i];
						li.i = i+1;
						li.onmouseover = function(){
							navListItem(this.i);
						};
						a.onmousedown = function(){
							selectedIndex = this.i;
							selectList(this.i);		
							return false;
						};					
						li.appendChild(a);
						list.setAttribute("tabindex", "-1");
						list.appendChild(li);	
					};	
					list.style.display = "block";				
				} else {
					clearList();
				};
			};	
			
			this.resetList = function(){
				var li = list.getElementsByTagName("li");
				var len = li.length;
				for(var i=0;i<len;i++){
					list.removeChild(li[0]);
				};
			};
			
			this.navList = function(dir){			
				selectedIndex += (dir == "down") ? 1 : -1;
				li = list.getElementsByTagName("li");
				if (selectedIndex < 1) selectedIndex =  li.length;
				if (selectedIndex > li.length) selectedIndex =  1;
				navListItem(selectedIndex);
			};
			
			this.navListItem = function(index){	
				selectedIndex = index;
				li = list.getElementsByTagName("li");
				for(var i=0;i<li.length;i++){
					li[i].className = (i==(selectedIndex-1)) ? "selected" : "";
				};
			};
			
			this.selectList = function(){
				li = list.getElementsByTagName("li");	
				a = li[selectedIndex-1].getElementsByTagName("a")[0];
				field.value = a.innerHTML;
				clearList();
			};			
			
		};
	};
	
	this.clearList = function(){
		if(list){
			list.style.display = "none";
			selectedIndex = 0;
		};
	};		
	this.getKeyCode = function(e){
		var code;
		if (!e) var e = window.event;
		if (e.keyCode) code = e.keyCode;
		return code;
	};
	
};

// script initiates on page load. 

this.addEvent = function(obj,type,fn){
	if(obj.attachEvent){
		obj['e'+type+fn] = fn;
		obj[type+fn] = function(){obj['e'+type+fn](window.event );}
		obj.attachEvent('on'+type, obj[type+fn]);
	} else {
		obj.addEventListener(type,fn,false);
	};
};
addEvent(window,"load",searchfield);


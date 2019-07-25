<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../css/newCss/main.css">
<link rel="stylesheet" type="text/css" href="../../css/newCss/map.css">
<link rel="stylesheet" type="text/css" href="../../css/newCss/set1.css">
<link rel="stylesheet" type="text/css" href="../../css/newCss/index.css">
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/lib/raphael-min.js"></script>
<script type="text/javascript" src="../../js/res/chinaMapConfig.js"></script> 
<script type="text/javascript" src="../../js/map-min.js"></script>
</head>
<body >
   <div class="wid-100" id="wid-100">
       <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	  <div class="MapContanier" id="allmap">
   <div id="MAP">
    <div class="itemCon" style="float: left">
        <div id="ChinaMap"></div>
		
        <div id="stateTip"></div>
		<div class="nanhai" style="float: right;margin-top: -100px;">
			<img src="../../image/nanhaidao.jpg" />
		</div>
    </div>
	<div style="float:left;margin-top:300px;" >
		<img src="../../image/meishizhengji.png" style="width:150px; height:auto;" />
	 </div>
   	
	
    <div id="mapTipContent" style="width: 900px;margin: 0 auto;display:none;">
        
        <div class="mapTipText mapTipText0">
            <div class="mapTipList">
                <h4>黑龙江</h4>
                <input type="hidden" value="黑龙江" id="provinceName0"  />
				<ul class="tagList" id="product0">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage0">
				</ul>
				</marquee>
            </div>
        </div>
        <div class="mapTipText mapTipText1">
            <div class="mapTipList">
                <h4>吉&nbsp;&nbsp;林</h4>
                <input type="hidden" value="吉林" id="provinceName1"  />
				<ul class="tagList" id="product1">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage1">
				</ul>
				</marquee>
            </div>
        </div>
       <div class="mapTipText mapTipText2">
            <div class="mapTipList">
                <h4>辽&nbsp;&nbsp;宁</h4>
                <input type="hidden" value="辽宁" id="provinceName2"  />
				<ul class="tagList" id="product2">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage2">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText3">
            <div class="mapTipList">
                <h4>河&nbsp;&nbsp;北</h4>
                <input type="hidden" value="河北" id="provinceName3"  />
				<ul class="tagList" id="product3">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage3">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText4">
            <div class="mapTipList">
                <h4>山&nbsp;&nbsp;东</h4>
                <input type="hidden" value="山东" id="provinceName4"  />
				<ul class="tagList" id="product4">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage4">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText5">
            <div class="mapTipList">
                <h4>江&nbsp;&nbsp;苏</h4>
                <input type="hidden" value="江苏" id="provinceName5"  />
				<ul class="tagList" id="product5">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage5">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText6">
            <div class="mapTipList">
                <h4>浙&nbsp;&nbsp;江</h4>
                <input type="hidden" value="浙江" id="provinceName6"  />
				<ul class="tagList" id="product6">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage6">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText7">
            <div class="mapTipList">
                <h4>安&nbsp;&nbsp;徽</h4>
                <input type="hidden" value="安徽" id="provinceName7"  />
				<ul class="tagList" id="product7">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage7">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText8">
            <div class="mapTipList">
                <h4>河&nbsp;&nbsp;南</h4>
                <input type="hidden" value="河南" id="provinceName8"  />
				<ul class="tagList" id="product8">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage8">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText9">
            <div class="mapTipList">
                <h4>山&nbsp;&nbsp;西</h4>
                <input type="hidden" value="山西" id="provinceName9"  />
				<ul class="tagList" id="product9">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage9">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText10">
            <div class="mapTipList">
                <h4>陕&nbsp;&nbsp;西</h4>
                <input type="hidden" value="陕西" id="provinceName10"  />
				<ul class="tagList" id="product10">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage10">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText11">
            <div class="mapTipList">
                <h4>甘&nbsp;&nbsp;肃</h4>
                <input type="hidden" value="甘肃" id="provinceName11"  />
				<ul class="tagList" id="product11">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage11">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText12" >
            <div class="mapTipList ">
				<h4>湖&nbsp;&nbsp;北</h4>
				<input type="hidden" value="湖北" id="provinceName12"  />
				<ul class="tagList" id="product12">
				</ul>
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage12">

				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText13">
            <div class="mapTipList">
                <h4>江&nbsp;&nbsp;西</h4>
                <input type="hidden" value="江西" id="provinceName13"  />
				<ul class="tagList" id="product13">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage13">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText14">
            <div class="mapTipList">
                <h4>福&nbsp;&nbsp;建</h4>
                <input type="hidden" value="福建" id="provinceName14"  />
				<ul class="tagList" id="product14">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage14">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText15">
            <div class="mapTipList">
                <h4>湖&nbsp;&nbsp;南</h4>
                <input type="hidden" value="湖南" id="provinceName15"  />
				<ul class="tagList" id="product15">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage15">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText16">
            <div class="mapTipList">
                <h4>贵&nbsp;&nbsp;州</h4>
                <input type="hidden" value="贵州" id="provinceName16"  />
				<ul class="tagList" id="product16">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage16">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText17">
            <div class="mapTipList">
                <h4>四&nbsp;&nbsp;川</h4>
                <input type="hidden" value="四川" id="provinceName17"  />
				<ul class="tagList" id="product17">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage17">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText18">
            <div class="mapTipList">
                <h4>云&nbsp;&nbsp;南</h4>
                <input type="hidden" value="云南" id="provinceName18"  />
				<ul class="tagList" id="product18">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage18">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText19">
            <div class="mapTipList">
                <h4>青&nbsp;&nbsp;海</h4>
                <input type="hidden" value="青海" id="provinceName19"  />
				<ul class="tagList" id="product19">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage19">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText20">
            <div class="mapTipList">
                <h4>海&nbsp;&nbsp;南</h4>
                <input type="hidden" value="海南" id="provinceName20"  />
				<ul class="tagList" id="product20">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage20">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText21">
            <div class="mapTipList">
                <h4>上&nbsp;&nbsp;海</h4>
                <input type="hidden" value="上海" id="provinceName21"  />
				<ul class="tagList" id="product21">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage21">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText22">
            <div class="mapTipList">
                <h4>重&nbsp;&nbsp;庆</h4>
                <input type="hidden" value="重庆" id="provinceName22"  />
				<ul class="tagList" id="product22">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage22">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText23">
            <div class="mapTipList">
                <h4>天&nbsp;&nbsp;津</h4>
                <input type="hidden" value="天津" id="provinceName23"  />
				<ul class="tagList" id="product23">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage23">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText24">
            <div class="mapTipList"         >
                <h4>北&nbsp;&nbsp;京</h4>
                <input type="hidden" value="北京" id="provinceName24"  />
				<ul class="tagList" id="product24">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage24">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText25">
            <div class="mapTipList">
                <h4>宁&nbsp;&nbsp;夏</h4>
                <input type="hidden" value="宁夏" id="provinceName25"  />
				<ul class="tagList" id="product25">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage25">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText26">
            <div class="mapTipList">
                <h4>内蒙古</h4>
                <input type="hidden" value="内蒙古" id="provinceName26"  />
				<ul class="tagList" id="product26">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage26">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText27">
            <div class="mapTipList">
                <h4>广&nbsp;&nbsp;西</h4>
                <input type="hidden" value="广西" id="provinceName27"  />
				<ul class="tagList" id="product27">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage27">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText28">
            <div class="mapTipList">
                <h4>新&nbsp;&nbsp;疆</h4>
                <input type="hidden" value="新疆" id="provinceName28"  />
				<ul class="tagList" id="product28">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage28">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText29">
            <div class="mapTipList">
                <h4>西&nbsp;&nbsp;藏</h4>
                <input type="hidden" value="西藏" id="provinceName29"  />
				<ul class="tagList" id="product29">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage29">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText30">
            <div class="mapTipList">
                <h4>广&nbsp;&nbsp;东</h4>
                <input type="hidden" value="广东" id="provinceName30"  />
				<ul class="tagList" id="product30">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage30">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText31">
            <div class="mapTipList">
                <h4>香&nbsp;&nbsp;港</h4>
                <input type="hidden" value="香港" id="provinceName31"  />
				<ul class="tagList" id="product31">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage31">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText32">
            <div class="mapTipList">
                <h4>台&nbsp;&nbsp;湾</h4>
                <input type="hidden" value="台湾" id="provinceName32"  />
				<ul class="tagList" id="product32">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage32">
				</ul>
				</marquee>
            </div>
        </div>
		<div class="mapTipText mapTipText33">
            <div class="mapTipList">
                <h4>澳&nbsp;&nbsp;门</h4>
                <input type="hidden" value="澳门" id="provinceName33"  />
				<ul class="tagList" id="product33">
				</ul>
				
				<marquee onmouseover="this.stop()" onmouseout="this.start()">
				<ul class="imgList" id="smallImage33">
				</ul>
				</marquee>
            </div>
		</div>
    
    </div>
	</div>

	</div>
	<script type="text/javascript">
        $(function(){
            $('#ChinaMap').SVGMap({
                mapWidth: 800,
                mapHeight: 800
            });
		 });
    </script>
    <div class="box_container">
		<div class="nav_bar wid-80 martop10">
    		<div class="btn_left"><span class="redbar"></span><span>营养膳食</span></div> 
			<div class="btn_right"><a class="orange" href="/content/collection/yingyangshanshi/index.shtml?rows=1000">查看更多</a></div>
        </div>
		<ul class="list_xinyang martop45 wid-80">
		<c:choose>
		  <c:when test="${fn:length(yingyangshanshiList)>0}">
		  <c:forEach var="document" items="${yingyangshanshiList}" begin="0" end="3">
				<li>
			    <a href="${document.viewUrl}">
			       <img class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../image/listImgDefault.png">
				   <div class="explain">
				     ${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}
					 </div>
					 </a>
				 </li>
			</c:forEach>
            </c:when>	
			<c:otherwise>
			<p style="text-align:center; font-size:14px; color:#333;">暂时没有相关产品</p>
			</c:otherwise>
           </c:choose>			
		</ul>
	</div>
	 <div class="box_container">
			<div class="nav_bar wid-80 martop10">
				<div class="btn_left"><span class="redbar"></span><span>明星厨房</span></div> 
				<div class="btn_right"><a class="orange" href="/content/videolist/shipinghuizong/index.shtml">查看更多</a></div>
			</div>
			<div class="box_shicaisuyuan martop45 wid-80">
			<c:choose>
		        <c:when test="${fn:length(shipinghuizongList)>0}">
				<div class="grid video_lr video_left">
				
					<c:forEach var="video" items="${shipinghuizongList}" varStatus="status" begin="0" end="0">
						<figure class="effect-bubba">
							<a href="${video.viewUrl}" style="position:relative;display:block; width:100%; height:100%;float:left;">
								<img class="lazy" data-original="${video.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/listImgDefault.png"/>
								<div class="playIcon"></div>
							</a>
							<figcaption>
								<h2><!--Fresh <span>Bubba</span>--></h2>
								<p style="padding: 60px 2.5em;"><!--${fn:substring(video.title ,0,10)}${fn:length(video.title)>10?"...":""}-->${video.title}</p>
								<a href="${video.viewUrl}">View more</a>
							</figcaption>			
						</figure>
					</c:forEach>
				</div>
				<div class="grid video_lr video_right">
					<c:forEach var="sc" items="${shipinghuizongList}" varStatus="status" begin="1" end="4">
						<figure class="effect-bubba" style="width:49% !important; height:247.5px !important;margin-left:1%;margin-bottom:5px;">
							<a href="${sc.viewUrl}" style="position:relative;display:block; width:100%; height:100%;float:left;">
								<img class="lazy" data-original="${sc.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/listImgDefault.png"/>
								<div class="playIcon"></div>
							</a>
							<figcaption>
								<h2><!--Fresh <span>Bubba</span>--></h2>
								<p>${fn:substring(sc.title ,0,10)}${fn:length(sc.title)>10?"...":""}</p>
								<a href="${sc.viewUrl}">View more</a>
							</figcaption>			
						</figure>
					</c:forEach>
          		
				</div>
				  </c:when>	
			<c:otherwise>
			<p style="text-align:center; font-size:14px; color:#333;">暂时没有相关产品</p>
			</c:otherwise>
           </c:choose>	
			</div>				
	   </div>
	 <div class="box_container">
		<div class="nav_bar wid-80 martop10 no_border">
				<div class="btn_left"><span class="redbar"></span><span>食材鉴别</span></div> 
				<div class="btn_right"><a class="orange" href="/content/collection/shicaijianbie/index.shtml?rows=1000">查看更多</a></div>
		</div>
		<ul class="list_jianbie martop45 wid-80">
		<c:choose>
		  <c:when test="${fn:length(shicaijianbieList)>0}">
		 <c:forEach var="document" items="${shicaijianbieList}" begin="0" end="3">
				<li>
			        <a href="${document.viewUrl}">
			    	<div class="box_img">
			            <p style="margin:0px !important"><img class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/listImgDefault.png"></p>
				    </div>
				    <p class="fo-15 margin_sx">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
				   <p>${document.documentDataMap.get('documentBrief').dataValue}</p>
				    </a>
				</li>				
			</c:forEach>
			</c:when>
			<c:otherwise>
			<p style="text-align:center; font-size:14px; color:#333;">暂时没有相关产品</p>
			</c:otherwise>
           </c:choose>	
		</ul>
		<div class="empty"></div>
	</div>
	</div>
	 <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	 <script type="text/javascript">
	 	function conceal(){
	 if(document.getElementById("two-dimension").style.display=="none"){
    document.getElementById("two-dimension").style.display="block";
   }
else{
    document.getElementById("two-dimension").style.display="none";
    }
}
	 </script>
	 
<!--<script type="text/javascript">
	var array = new Array(); 
	<c:forEach items="${document}" var="a"> 
   		array.push(${a}); //生成如 array.push(123)的字符串 这样前台拿到后就是js 
	</c:forEach> 
	
	
	var mp = new BMap.Map("allmap");    // 创建Map实例
	mp.centerAndZoom(new BMap.Point(101.74,36.56), 5);  // 初始化地图,设置中心点坐标和地图级别
	mp.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	
	//全景图控件
	var stCtrl = new BMap.PanoramaControl(); //构造全景控件
	stCtrl.setOffset(new BMap.Size(20, 20));
	mp.addControl(stCtrl);//添加全景控件
	
	
	// 复杂的自定义覆盖物
	// 定义自定义覆盖物的构造函数 
    function ComplexCustomOverlay(point, text){
      this._point = point;
      this._text = text;
    }
	// 继承API的BMap.Overlay
    ComplexCustomOverlay.prototype = new BMap.Overlay();
	// 实现初始化方法
    ComplexCustomOverlay.prototype.initialize = function(map){
      // 保存map对象实例 
	  this._map = map;
	   // 创建div元素，作为自定义覆盖物的容器   
      var div = this._div = document.createElement("div");
	  // 可以根据参数设置元素外观
      div.style.position = "absolute";
      div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);  //
      //div.style.backgroundColor = "#EE5D5B";
      //div.style.border = "5px solid blue";
      div.style.color = "white";
      //div.style.height = "18px";
      div.style.padding = "2px";
      //div.style.lineHeight = "18px";
      div.style.whiteSpace = "nowrap";
      div.style.MozUserSelect = "none";
      div.style.fontSize = "12px";
	   //创建span标签 用于存放覆盖物信息
      var span = this._span = document.createElement("span");
      //span.style.border = "5px solid green";
      //添加到div下面
	  div.appendChild(span);
      //span.appendChild(document.createTextNode(this._text)); 
      var oImg = document.createElement('img');
	  span.appendChild(oImg);
	  oImg.src=this._text;
      var that = this;

	   //添加角图标
      var arrow = this._arrow = document.createElement("div");
      arrow.style.background = "url(http://map.baidu.com/fwmap/upload/r/map/fwmap/static/house/images/label.png) no-repeat";
      arrow.style.position = "absolute";
      arrow.style.width = "11px";
      arrow.style.height = "10px";
      //arrow.style.top = "22px";
      arrow.style.top = "112px";
      arrow.style.left = "10px";
      arrow.style.overflow = "hidden";
      div.appendChild(arrow);
    
	// 将div添加到覆盖物容器中
      mp.getPanes().labelPane.appendChild(div);
      
      return div;
    }
	// 实现绘制方法 
    ComplexCustomOverlay.prototype.draw = function(){
      var map = this._map;
	  // 根据地理坐标转换为像素坐标，并设置给容器
      var pixel = map.pointToOverlayPixel(this._point);
      this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + "px";
      //this._div.style.top  = pixel.y - 30 + "px";
      this._div.style.top  = pixel.y - 120 + "px";
    }
	
	 //添加监听事件  
        ComplexCustomOverlay.prototype.addEventListener = function(event,fun){  
            this._div['on'+event] = fun;  
        } 
	 
      //创建信息窗口
		function createInfoWindow(i){
			var json=array[i];
			var show = "";
			//show = "<p onclick='getDocument()'>"+json.title+"</p><img class='imgStyle' src='http://yixian.mo4u.cn/static/documentFile/open/"+json.picture+"' />'";
			show += "<input type='hidden' id='udid' value='"+json.udid+"' />";
			show += "<a class='titleStyle' href=''><p onclick='getDocument()'>"+json.title+"</p></a>";
			show += "<a href=''><img onclick='getDocument()' class='imgStyle' src='http://yixian.mo4u.cn/static/documentFile/open/"+json.picture+"' />'</a>";
			var iw = new BMap.InfoWindow(show,{                                       
                width: 390, //宽度     
				});   
			return iw;
		}
        
	for(var i=0;i<array.length;i++){
		var json=array[i];
		console.log("json  "+json);
		
		//var txt = array[i].title;
		var txt = "http://yixian.mo4u.cn/static/documentFile/open/"+array[i].picture;
		var pointx = array[i].longitude;
        var pointy = array[i].latitude;
        console.log("pintx  "+pointx+"   pinty   "+pointy);
		var myCompOverlay = new ComplexCustomOverlay(new BMap.Point(pointx,pointy), txt);
		mp.addOverlay(myCompOverlay);
            
            (function(){
            	var p0 = json.longitude;
    			var p1 = json.latitude;
    			console.log("p0  "+p0+"   p1   "+p1);
    			var point = new BMap.Point(p0,p1);
    			var _iw = createInfoWindow(i);
    			var _marker = myCompOverlay; //当初存的覆盖物变量，这里派上用场了。
    			_marker.addEventListener("click",function(e){
    				mp.openInfoWindow(_iw,point);
    			});

    		})()

	}
	
	
	//进入到相应的文章
		function getDocument(){
			var udid = $("#udid").val();
			$.ajax({ 
				//type: "POST", 	
				url: "getDocumentUrl/"+udid,
				dataType: "json",
				success: function(data){
					/*$(data).each(function(i,m) {						
						var document_url = m;
						alert(document_url);
					});*/
					window.location.href=data[1];

				},
				/*error: function(XMLHttpRequest, textStatus, errorThrown) {
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);
				} */
				error: function(data) {
					alert("系统繁忙");
				} 
			}); 
		}
</script> 
-->

</body>
</html>
<script type="text/javascript" src="../../js/collection.js"></script>



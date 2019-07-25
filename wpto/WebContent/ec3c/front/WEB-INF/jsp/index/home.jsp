
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<title>${systemName}-首页</title>
<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/common.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/index.js" ></script>
</head>

<body class="index_page" style="min-width:1200px;">
 
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js" ></script>

<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<c:if test="${theme=='beijingjiamei'}">
 <div class="block">
   
  <div class="AreaL">
<script type="text/javascript">
          //åˆå§‹åŒ–ä¸»èœå•
            function sw_nav2(obj,tag)
            {
            var DisSub2 = document.getElementById("DisSub2_"+obj);
            var HandleLI2= document.getElementById("HandleLI2_"+obj);
                if(tag==1)
                {
                    DisSub2.style.display = "block";
          HandleLI2.className="current";
                }
                else
                {
                    DisSub2.style.display = "none";
          HandleLI2.className="";
                }
            }
</script>
 
    
    
  <div id="category_tree">
  <div class="tit">商品分类</div>
  <dl class="clearfix">
        <c:forEach items="${navigation}" var="a" >
             <div class="dt" onmouseover="sw_nav2(1,1);" onmouseout="sw_nav2(1,0);">
              <div id="HandleLI2_1" class="">
                <a class="a" href="${a.viewUrl}" >${a.name}
                  <img src="theme/${theme}/images/biao8.gif">
                </a>
              </div>
              <!-- <dd id="DisSub2_1" style="display: none;"> 
               <a class="over_2" href="#">77</a>  
              <div class="clearfix"></div>   
              </dd>  -->
              </div>
        </c:forEach>
</dl>
</div>
 <div class="blank5"></div>
<div class="blank"></div> 
    
  <script type="text/javascript">
          
            function sw_nav2(obj,tag)
            {
            var DisSub2 = document.getElementById("DisSub2_"+obj);
            var HandleLI2= document.getElementById("HandleLI2_"+obj);
                if(tag==1)
                {
                    DisSub2.style.display = "block";
          HandleLI2.className="current";
                }
                else
                {
                    DisSub2.style.display = "none";
          HandleLI2.className="";
                }
            }
</script></div>

 </div>

</c:if>
<!--  <div id="idTransformView" style="overflow:hidden; position:relative;" class="container">
  <ul class="slider" id="idSlider">
        <li style="background:url(/theme/${theme}/images/20160308xdwyet.jpg) center 0 no-repeat; position:relative;"><c:if test="${theme=='beijingjiamei'}"><img src="/theme/${theme}/images/20160308xdwyet.jpg"></c:if><a href="index.htm" target="_blank"></a></li>
        <li style="background:url(/theme/${theme}/images/20160308awaoit.jpg) center 0 no-repeat; position:relative;"><c:if test="${theme=='beijingjiamei'}"><img src="/theme/${theme}/images/20160308awaoit.jpg"></c:if><a href="index.htm" target="_blank"></a></li>
      
  </ul>
  <ul class="num" id="idNum">
          <li>
    1    </li> 
         <li>
    2    </li> 
        
  </ul>
</div> -->
<script type="text/javascript">
var $s = function (id) {
	return "string" == typeof id ? document.getElementById(id) : id;
};
var Class = {
  create: function() {
	return function() {
	  this.initialize.apply(this, arguments);
	}
  }
}
Object.extend = function(destination, source) {
	for (var property in source) {
		destination[property] = source[property];
	}
	return destination;
}
var TransformView = Class.create();
TransformView.prototype = {
  //容器对象,滑动对象,切换参数,切换数量
  initialize: function(container, slider, parameter, count, options) {
	if(parameter <= 0 || count <= 0) return;
	var oContainer = $s(container), oSlider = $s(slider), oThis = this;
	this.Index = 0;//当前索引
	
	this._timer = null;//定时器
	this._slider = oSlider;//滑动对象
	this._parameter = parameter;//切换参数
	this._count = count || 0;//切换数量
	this._target = 0;//目标参数
	
	this.SetOptions(options);
	
	this.Up = !!this.options.Up;
	this.Step = Math.abs(this.options.Step);
	this.Time = Math.abs(this.options.Time);
	this.Auto = !!this.options.Auto;
	this.Pause = Math.abs(this.options.Pause);
	this.onStart = this.options.onStart;
	this.onFinish = this.options.onFinish;
	
	oContainer.style.overflow = "hidden";
	oContainer.style.position = "relative";
	
	oSlider.style.position = "absolute";
	oSlider.style.top = oSlider.style.left = 0;
  },
  //设置默认属性
  SetOptions: function(options) {
	this.options = {//默认值
		Up:			true,//是否向上(否则向左)
		Step:		5,//滑动变化率
		Time:		10,//滑动延时
		Auto:		true,//是否自动转换
		Pause:		2000,//停顿时间(Auto为true时有效)
		onStart:	function(){},//开始转换时执行
		onFinish:	function(){}//完成转换时执行
	};
	Object.extend(this.options, options || {});
  },
  //开始切换设置
  Start: function() {
	if(this.Index < 0){
		this.Index = this._count - 1;
	} else if (this.Index >= this._count){this.Index = 0;}
	
	this._target = -1 * this._parameter * this.Index;
	this.onStart();
	this.Move();
  },
  //移动
  Move: function() {
	clearTimeout(this._timer);
	var oThis = this, style = this.Up ? "top" : "left", iNow = parseInt(this._slider.style[style]) || 0, iStep = this.GetStep(this._target, iNow);
	
	if (iStep != 0) {
		this._slider.style[style] = (iNow + iStep) + "px";
		this._timer = setTimeout(function(){oThis.Move();}, this.Time);
	} else {
		this._slider.style[style] = this._target + "px";
		this.onFinish();
		if (this.Auto) { this._timer = setTimeout(function(){oThis.Index++; oThis.Start();}, this.Pause); }
	}
  },
  //获取步长
  GetStep: function(iTarget, iNow) {
	var iStep = (iTarget - iNow) / this.Step;
	if (iStep == 0) return 0;
	if (Math.abs(iStep) < 1) return (iStep > 0 ? 1 : -1);
	return iStep;
  },
  //停止
  Stop: function(iTarget, iNow) {
	clearTimeout(this._timer);
	this._slider.style[this.Up ? "top" : "left"] = this._target + "px";
  }
};
window.onload=function(){
	function Each(list, fun){
		for (var i = 0, len = list.length; i < len; i++) {fun(list[i], i);}
	};
	
	var objs = $s("idNum").getElementsByTagName("li");
	var obj_len = objs.length;
	var tv = new TransformView("idTransformView", "idSlider", 425, obj_len, {
		onStart : function(){ Each(objs, function(o, i){o.className = tv.Index == i ? "on" : "";}) }//按钮样式
	});
	
	tv.Start();
	
	Each(objs, function(o, i){
		o.onmouseover = function(){
			o.className = "on";
			tv.Auto = false;
			tv.Index = i;
			tv.Start();
		}
		o.onmouseout = function(){
			o.className = "";
			tv.Auto = true;
			tv.Start();
		}
	})
	
	////////////////////////test2
	
//	var objs2 = $("idNum2").getElementsByTagName("li");
//	
//	var tv2 = new TransformView("idTransformView2", "idSlider2",1200, 3, {
//		onStart: function(){ Each(objs2, function(o, i){o.className = tv2.Index == i ? "on" : "";}) },//按钮样式
//		Up: false
//	});
//	
//	tv2.Start();
//	
//	Each(objs2, function(o, i){
//		o.onmouseover = function(){
//			o.className = "on";
//			tv2.Auto = false;
//			tv2.Index = i;
//			tv2.Start();
//		}
//		o.onmouseout = function(){
//			o.className = "";
//			tv2.Auto = true;
//			tv2.Start();
//		}
//	})
//	
//	$("idStop").onclick = function(){tv2.Auto = false; tv2.Stop();}
//	$("idStart").onclick = function(){tv2.Auto = true; tv2.Start();}
//	$("idNext").onclick = function(){tv2.Index++; tv2.Start();}
//	$("idPre").onclick = function(){tv2.Index--;tv2.Start();}
//	$("idFast").onclick = function(){ if(--tv2.Step <= 0){tv2.Step = 1;} }
//	$("idSlow").onclick = function(){ if(++tv2.Step >= 10){tv2.Step = 10;} }
//	$("idReduce").onclick = function(){ tv2.Pause-=1000; if(tv2.Pause <= 0){tv2.Pause = 0;} }
//	$("idAdd").onclick = function(){ tv2.Pause+=1000; if(tv2.Pause >= 5000){tv2.Pause = 5000;} }
//	
//	$("idReset").onclick = function(){
//		tv2.Step = Math.abs(tv2.options.Step);
//		tv2.Time = Math.abs(tv2.options.Time);
//		tv2.Auto = !!tv2.options.Auto;
//		tv2.Pause = Math.abs(tv2.options.Pause);
//	}
	
}
</script>
 
 
 
 
 
 <div class="blank5"></div>
<div class="blank"></div><div class="blank"></div>
<div class="block clearfix">
<c:if test="${theme=='basic2' || theme=='basic3'}">
 <div id="category_tree">
    <dl class="clearfix" style="margin-top:-454px;*margin-top:-456px;height:399px;">
       <c:forEach items="${navigation}" var="a" >
            <div class="dt">
                <div id="HandleLI2_1"><a class="a" href="${a.viewUrl}">${a.name}<i class="iconfont">></i></a> 
                </div>
                <dd id="DisSub2_1" style="display: none;"></dd> 
            </div>
      </c:forEach>
    </dl>
</div>
<div class="AreaL">
 
<div id="mallNews" class="  box_1">
    <h3><span>站内快讯</span></h3>
    <div class="NewsList tc  " style="border-top:none">
        <ul>
          <c:forEach items="${indexNewestList}" var="a">
            <li>
              <a href="${a.viewUrl}" title="${a.title}">${a.title}</a>
            </li>
          </c:forEach>
        </ul>
    </div>
</div>
<div class="blank"></div>  
<table cellpadding="0" cellspacing="0">
<tbody><tr>
<td><a href="/" target="_blank"><img src="theme/${theme}/images/1442387757617091875.png" width="240" height="140" border="0"></a></td>
</tr>
</tbody></table>
<div class="blank"></div>
</div>
</c:if>

  <div class="goodsBox_1">
  
  
  
<div class="xm-box">
  <div class="title"><span class="floor-icon"><i></i><b></b></span>
    <h2>热卖商品</h2>
    <a class="more" href="/content/dianshi/index.shtml">更多</a></div>
  <div id="show_hot_area" class="clearfix xm-boxs"> 
     <c:forEach items="${indexHotSaleList}" var="a">
        <div class="goodsItem goodsItems">
          <a href="${a.document}">
            <img src="${a.product.productDataMap.productSmallImage.dataValue}" alt="${a.product.productName}" class="goodsimg" />
          </a><br />
      <p class="f1"><a href="${a.document}" title="${a.product.productName}">${a.product.productName}</a></p>

       <p class="">市场价：<font class="market">￥${a.product.labelMoney}元</font> <br/>
      本店价：<font class="f1"> ￥<em class="moneynumber" style="font-style:normal;">${a.product.buyMoney}</em>元 </font>
      </p>
       </div>
     </c:forEach>
  </div>
</div>
<div class="blank"></div>
 
 
<div class="xm-box">
  <div class="title"><span class="floor-icon"><i></i><b></b></span>
    <h2>精品推荐</h2>
    <a class="more" href="/content/kongtiao/index.shtml">更多</a></div>
  <div id="show_best_area" class="clearfix xm-boxs"> 
     <c:forEach items="${indexRecommendList}" var="a">
        <div class="goodsItem goodsItems">
          <a href="${a.document}">
            <img src="${a.product.productDataMap.productSmallImage.dataValue}" alt="${a.product.productName}" class="goodsimg" />
          </a><br />
      <p class="f1"><a href="${a.document}" title="${a.product.productName}">${a.product.productName}</a></p>

       <p class="">市场价：<font class="market">￥${a.product.labelMoney}元</font> <br/>
      本店价：<font class="f1"> ￥<em class="moneynumber" style="font-style:normal;">${a.product.buyMoney}</em>元 </font>
      </p>
       </div>
     </c:forEach>

   
     
  </div>
</div>
<div class="blank"></div>
 
 
<div class="xm-box">
  <div class="title"><span class="floor-icon"><i></i><b></b></span>
    <h2>新品上架</h2>
    <a class="more" href="/content/bingxiang/index.shtml">更多</a></div>
  <div id="show_new_area" class="clearfix xm-boxs"> 
    <c:forEach items="${indexNewestList}" var="a">
        <div class="goodsItem goodsItems">
          <a href="${a.document}">
            <img src="${a.product.productDataMap.productSmallImage.dataValue}" alt="${a.product.productName}" class="goodsimg" />
          </a><br />
      <p class="f1"><a href="${a.document}" title="${a.product.productName}">${a.product.productName}</a></p>

       <p class="">市场价：<font class="market">￥${a.product.labelMoney}元</font> <br/>
      本店价：<font class="f1"> ￥<em class="moneynumber" style="font-style:normal;">${a.product.buyMoney}</em>元 </font>
      </p>
       </div>
     </c:forEach>
    
     
  </div>
</div>
<div class="blank"></div>
 
 
  </div> 
  
    </div>
  
 
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    <script type="text/javascript">
    // 分字段
$('.moneynumber').each(function () {
  var neirongzhi = $(this).text();
  var arry = {};
  arry = neirongzhi.split(';'); 
  // console.log(arry);
  if (arry.length>0) {
    var num = arry[0].replace(/[^\d.]/g,"");
      // console.log(num);
  };
  $(this).text(num);
})
function imgsrc(){
  $('.goodsItem').each(function(){
    var src = $(this).find('img').attr('src');
    var arry = [];
    arry = src.split(',');
    $(this).find('img').attr('src','/file/'+arry[0]);
  })
  
}
imgsrc();</script>
</body>
</html>

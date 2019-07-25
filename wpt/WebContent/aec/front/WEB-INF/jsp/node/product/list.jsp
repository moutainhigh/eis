<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/newCss/main.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/newCss/index.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/newCss/shop.css?v=03031021">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/shicaisuyuan.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/newCss/swiper.min.css">
<script  type="text/javascript" src="../../../theme/${theme}/js/jquery.min.js"></script>
<script  type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../theme/${theme}/js/respond.src.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/pull-down list_index.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/common.js?v=03131139"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/lazyload.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
</head>
<body>
   <div class="wid-100" id="wid-100">
	  <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	  <div class="swiper-container" style="max-width: 1150px;min-width:1000px;height: auto;margin: 0 auto;display: block;position: relative;">
		<div class="swiper-wrapper"> 
 		<c:set var="num" value="0"></c:set> 
				<c:forEach items="${newsList}" var="list" varStatus="i">
						<c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						<div class="swiper-slide"><a href="${list.redirectTo }"><img alt="" src="${list.documentDataMap.get('bannerImage').dataValue} "> </a></div>
			          </c:if>
					</c:if>
				</c:forEach>
		</div>
		<div class="swiper-pagination"></div>
		<div class="swiper-button-prev swiper-button-white"></div>
        <div class="swiper-button-next swiper-button-white"></div>
	</div>
	<script language="javascript"> 
	var mySwiper = new Swiper('.swiper-container',{
	pagination : '.swiper-pagination',
	//pagination : '#swiper-pagination1',
	effect : 'fade',
	fade: {
		crossFade: false,
		},
	autoplay : 2000,
	speed:700,
	preventClicks : true,
	prevButton:'.swiper-button-prev',
	nextButton:'.swiper-button-next',
	paginationClickable :true
	})
</script>
	<div class="box_container">
		<ul class="list_product martop35 wid-80">
		<c:set var="num" value="0"></c:set>
		<c:choose>
		<c:when test="${fn:length(tegongList)>0}">
			<c:forEach items="${tegongList}" var="document">
				<c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<4}">
			    <li>
				    <a href="${document.viewUrl}">
			    	   <img data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../../image/listImgDefault.png" class="lazy"/>
					</a>
				</li>
				</c:if>
				</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				 
			</c:otherwise>
		</c:choose>	
		</ul>
		<ul class="y_activity  martop30 wid-80">
		<c:forEach items="${huodongquList}" var="document" begin="0" end="3">
		   <li><a href="${document.viewUrl}"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../../image/listImgDefault.png" class="lazy"/></a></li>
		 </c:forEach>
		</ul>
	</div>
	<div class="box_container" style="background-color:#f8f8f8;">
	      <div class="nav_bar wid-80 martop30 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>热销产品</span></div> 
        </div>
		<ul class="list_hot martop20 wid-80">
			<c:set var="num" value="0"></c:set>
		<c:choose>
		<c:when test="${fn:length(rexiaoList)>0}">
			<c:forEach items="${rexiaoList}" var="document">
				<c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<4}">
			    <li>
				    <a href="${document.viewUrl}">
			    	   <img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"/>
					   <div class="p_wrap">
					      <p class="p_name margin_sx">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
						  <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
						  <p class="p_price martop20 fo_mA"><span class="red fo-17 marright20"><span class="fo-12">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney"). dataValue,"money:","")}</span><span class="textLH original fo-12">￥${fn:replace(document.documentDataMap.get("productMarketPrice"). dataValue,"money:","")}</span></p>
					   </div>
					</a>
				</li>
				</c:if>
				</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p style="text-align:center">暂时没有相关产品</p>
			</c:otherwise>
		</c:choose>	
		</ul>
		</div>
			 <div class="box_container" >
	    <c:if test="${fn:length(tuanList)>0}">
	    <div class="nav_bar wid-80 martop30">
    		<div class="btn_left"><span class="redbar"></span><span>火热预售</span></div> 
			<div class="btn_right"><a class="orange" href="/content/product/tuan/index.shtml">查看更多</a></div>
        </div>
		</c:if>
		<ul class="list_FV martop10 wid-80">
		<fmt:formatDate value="<%=new java.util.Date()%>" type="both" dateStyle="long" pattern="yyyy/MM/dd HH:mm:ss" var="now"/>
		       <c:forEach items="${tuanList}" var="document" begin="0" end="3" varStatus="i">
		        <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set value="${document.documentDataMap.get('timeCurrent').dataValue}" var="timeCurrent"></c:set>
			<li class="${i.index<4?'marbottom30':''}">
			    <a href="${document.viewUrl}">
			    	<div class="box_p">
					 <div class="box_img">
			         <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"></p>
			         <div class="p_wrap box_container">
						 <p class="p_name margin_sx">${document.title}</p>
						 <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
					 </div>
					 </div>
					 <div class="look_goods">
					     <span class="red fo-21 marright2 marleft15 fo_mA"><span class="fo-14 fo_mA">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span>
						 <span class="textLH original fo-12 fo_mA">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span>
						 <c:choose>
						<c:when test="${timeCurrent==500072}">
						 <span class="fr_a fr_a3" title="已结束" target="_blank" href="${document.viewUrl}">已结束</span>
						 </c:when>
						<c:when test="${timeCurrent==500080}">
						  <span class="fr_a fr_a1" title="去看看" target="_blank" href="${document.viewUrl}">去看看</span>
						 </c:when>
						<c:otherwise>
						<span class="fr_a fr_a2" title="立即购" target="_blank" href="${document.viewUrl}">立即购</span>
						</c:otherwise>
						</c:choose>
					  </div>
					  <c:if test="${timeCurrent==500080}">
					  <div class="clock fo-12" id="colorBox${i.index}">
					     <p>开始倒计时</p>
						 <p><span class="day">0</span>天</p>
						 <p><span class="hour">0</span>:<span class="minute">0</span>:<span class="second">0</span></p>
					  </div>
					  <script>var timer=new Timer("${document.documentDataMap.get('activityBeginTime').dataValue}",'#colorBox${i.index}',"${now}");timer.start();</script>
					  </c:if>
					  <c:if test="${timeCurrent==500072}">
					     <span class="iconOver">已结束</span>
					  </c:if>
					 </div>
					  
					  
				</a>
			</li>
			</c:if>
			</c:forEach>
			<c:forEach items="${noIndexCountList}" var="document" begin="0" end="3" varStatus="i">
		        <c:if test="${document.documentTypeCode != 'image'}" >
			<li class="${i.index<4?'marbottom30':''}">
			    <a href="${document.viewUrl}">
			    	<div class="box_p">
					 <div class="box_img">
			         <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"></p>
			         <div class="p_wrap box_container">
						 <p class="p_name margin_sx">${document.title}</p>
						 <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
					 </div>
					 </div>
					 <div class="look_goods">
					     <span class="red fo-21 marright2 marleft15 fo_mA"><span class="fo-14 fo_mA">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span>
						 <span class="textLH original fo-12 fo_mA">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span>
						 <c:choose>
						<c:when test="${timeCurrent==500072}">
						 <span class="fr_a fr_a3" title="已结束" target="_blank" href="${document.viewUrl}">已结束</span>
						 </c:when>
						<c:when test="${timeCurrent==500080}">
						  <span class="fr_a fr_a1" title="去看看" target="_blank" href="${document.viewUrl}">去看看</span>
						 </c:when>
						<c:otherwise>
						<span class="fr_a fr_a2" title="立即购" target="_blank" href="${document.viewUrl}">立即购</span>
						</c:otherwise>
						</c:choose>
					  </div>
					  <c:if test="${timeCurrent==500080}">
					  <div class="clock fo-12" id="colorBox${i.index}">
					     <p>开始倒计时</p>
						 <p><span class="day">0</span>天</p>
						 <p><span class="hour">0</span>:<span class="minute">0</span>:<span class="second">0</span></p>
					  </div>
					  <script>var timer=new Timer("${document.documentDataMap.get('activityBeginTime').dataValue}",'#colorBox${i.index}',"${now}");timer.start();</script>
					  </c:if>
					  <c:if test="${timeCurrent==500072}">
					     <span class="iconOver">已结束</span>
					  </c:if>
					 </div>
					  
					  
				</a>
			</li>
			</c:if>
			</c:forEach>	
		</ul>
	</div> 
	 <div class="box_container" >
	    <div class="nav_bar wid-80 martop30">
    		<div class="btn_left"><span class="redbar"></span><span>生鲜果蔬</span></div> 
			<div class="btn_right"><a class="orange" href="/content/product/guoshu/index.shtml">查看更多</a></div>
        </div>
		<ul class="list_FV martop10 wid-80">
		    <c:choose>
			<c:when test="${fn:length(guoshuList)>0}">
		       <c:forEach items="${guoshuList}" var="document" begin="0" end="7" varStatus="i">
		        <c:if test="${document.documentTypeCode != 'image'}" >
			<li class="${i.index<4?'marbottom30':''}">
			    <a href="${document.viewUrl}">
			    	<div class="box_p">
					 <div class="box_img">
			         <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"></p>
			         <div class="p_wrap box_container">
						 <p class="p_name margin_sx">${document.title}</p>
						 <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
					 </div>
					 </div>
					 <div class="look_goods">
					     <span class="red fo-21 marright2 marleft15 fo_mA"><span class="fo-14 fo_mA">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span>
						 <span class="textLH original fo-12 fo_mA">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span>
						 <span class="fr" title="去看看" target="_blank" href="${document.viewUrl}">去看看</span>
					  </div>
					 </div>
					  
					  
				</a>
			</li>
			</c:if>
			</c:forEach>
			<c:forEach items="${noIndexCountList}" var="document" begin="0" end="7" varStatus="i">
		        <c:if test="${document.documentTypeCode != 'image'}" >
			<li class="${i.index<4?'marbottom30':''}">
			    <a href="${document.viewUrl}">
			    	<div class="box_p">
					 <div class="box_img">
			         <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"></p>
			         <div class="p_wrap box_container">
						 <p class="p_name margin_sx">${document.title}</p>
						 <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
					 </div>
					 </div>
					 <div class="look_goods">
					     <span class="red fo-21 marright2 marleft15 fo_mA"><span class="fo-14 fo_mA">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span>
						 <span class="textLH original fo-12 fo_mA">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span>
						 <span class="fr" title="去看看" target="_blank" href="${document.viewUrl}">去看看</span>
					  </div>
					 </div>
					  
					  
				</a>
			</li>
			</c:if>
			</c:forEach>
			</c:when>
			<c:otherwise>
				<p style="text-align:center">暂时没有相关产品</p>
			</c:otherwise>
		</c:choose>		
		</ul>
	</div> 
	<div class="box_container" >
	    <div class="nav_bar wid-80 martop30">
    		<div class="btn_left"><span class="redbar"></span><span>南北干货</span></div> 
			<div class="btn_right"><a class="orange" href="/content/product/ganhuo/index.shtml">查看更多</a></div> 
        </div>
		<ul class="list_FV martop10 wid-80">
		    <c:choose>
			<c:when test="${fn:length(ganhuoList)>0}">
			  <c:set var="num" value="0"></c:set> 
		       <c:forEach items="${ganhuoList}" var="document" varStatus="i">
		        <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<9}">
			<li class="${i.index<4?'marbottom30':''}">
			    <a href="${document.viewUrl}">
			    	<div class="box_p">
					 <div class="box_img">
			         <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../../image/listImgDefault.png" class="lazy"></p>
			         <div class="p_wrap box_container">
						 <p class="p_name margin_sx">${document.title}</p>
						 <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
					 </div>
					 </div>
					 <div class="look_goods">
					     <span class="red fo-21 marright2 marleft15 fo_mA"><span class="fo-14 fo_mA">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span>
						 <span class="textLH original fo-12 fo_mA">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span>
						 <a class="fr" title="去看看" target="_blank" href="${document.viewUrl}">去看看</a>
					  </div>
					 </div>
					  
					  
				</a>
			</li>
			</c:if>
			</c:if>
			</c:forEach>
			</c:when>
			<c:otherwise>
				<p style="text-align:center">暂时没有相关产品</p>
			</c:otherwise>
		</c:choose>		
		</ul>
	</div>
	<div class="box_container" >
	    <div class="nav_bar wid-80 martop30">
    		<div class="btn_left"><span class="redbar"></span><span>休闲食品</span></div> 
			<div class="btn_right"><a class="orange" href="/content/product/shiping/index.shtml">查看更多</a></div> 
        </div>
		<ul class="list_FV martop10 wid-80">
		    <c:choose>
			<c:when test="${fn:length(shipingList)>0}">
			  <c:set var="num" value="0"></c:set> 
		       <c:forEach items="${shipingList}" var="document" varStatus="i">
		        <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<9}">
			<li class="${i.index<4?'marbottom30':''}">
			    <a href="${document.viewUrl}">
			    	<div class="box_p">
					 <div class="box_img">
			         <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../../image/listImgDefault.png" class="lazy"></p>
			         <div class="p_wrap box_container">
						 <p class="p_name margin_sx">${document.title}</p>
						 <p class="p_heading fo-12 margin_sx">${document.documentDataMap.get("subtitle").dataValue}</p>
					 </div>
					 </div>
					 <div class="look_goods">
					     <span class="red fo-21 marright2 marleft15 fo_mA"><span class="fo-14 fo_mA">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span>
						 <span class="textLH original fo-12 fo_mA">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span>
						 <a class="fr" title="去看看" target="_blank" href="${document.viewUrl}">去看看</a>
					  </div>
					 </div>
				</a>
			</li>
			</c:if>
			</c:if>
			</c:forEach>
			</c:when>
			<c:otherwise>
				<p style="text-align:center">暂时没有相关产品</p>
			</c:otherwise>
		</c:choose>		
		</ul>
	</div>
	<!--
	<div class="box_container">
	    <h3 class="martop30 "><span class="orange spacing hot">火热</span><span class="spacing">预售</span><a href="/content/product/tuan/index.shtml?rows=1000" class="orange" style="font-size:12px!important;margin-left:20px;">更多...</a></h3>	
		<ul class="list5 martop45 wid-80">
		<c:set var="num" value="0"></c:set>
		<c:choose>
			<c:when test="${fn:length(tuanList)>0}">
				<c:forEach items="${tuanList}" var="document" >
				 <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<5}">
				<a href="${document.viewUrl}">
			    <li>
			    	<div class="box_img product_waikuang_yuan">
			       <p style="margin: 2px 0px !important;"><img src="${document.documentDataMap.get('activitySmallImage').dataValue}" class="product_tu_yuan"></p>
			         <div class="mask yuan">
					     <div class="box_content">
					     <p>单价:￥${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</p>
						 <p>规格:${document.documentDataMap.get("goodsSpec").dataValue}</p>
						 <p>产地:${document.documentDataMap.get("productOrigin").dataValue}</p>
						 <p><a href="${document.viewUrl}" class="btn_buy">立即购买</a></p>
						 </div>
					  </div>
				   </div>
				   <p class="fo-15">${document.title}</p>
				   <p>${document.documentDataMap.get('documentBrief').dataValue}</p>
				</li>
				</a>
				</c:if>
				</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<p style="padding-left:440px; font-size:14px; color:#333;">您所筛选的产品未参加活动</p>
			</c:otherwise>
		</c:choose>	 	
		</ul>
		
		 
		
	</div>-->
	
	
	<!--<div class="box_container">
	    <h3 class="martop30"><span class="orange spacing">以先</span><span class="spacing">推荐</span></h3>
		<ul class="list5 martop45 wid-80">
		<c:choose>
			<c:when test="${fn:length(suggestList)>0}">
		<c:forEach items="${suggestList}" var="document">
		 <c:if test="${document.documentTypeCode != 'image'}" >
						<a href="${document.viewUrl}">
			   <li>
			    	<div class="box_img">
			       <p style="margin:0px !important"><img src="${document.documentDataMap.get('productSmallImage').dataValue}"></p>
			         <div class="mask">
					     <div class="box_content">
					     <p>单价:${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</p>
						 <p>规格:${document.documentDataMap.get("goodsSpec").dataValue}</p>
						 <p>产地:${document.documentDataMap.get("productOrigin").dataValue}</p>
						 <p><a href="${document.viewUrl}" class="btn_buy">立即购买</a></p>
						 </div>
					  </div>
				   </div>
				   <p class="fo-15 margin_sx">${document.title}</p>
				   <p>${document.documentDataMap.get('documentBrief').dataValue}</p>
				</li>
				</a>
				</c:if>
	</c:forEach>
			</c:when>
			<c:otherwise>
				<li style="padding-left:440px; font-size:14px; color:#333;">暂时没有相关产品</li>
			</c:otherwise>
		</c:choose>	
			
		</ul>
	</div>-->
 
		<div class="martop40"></div>
	  <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</div>

</body>
</html>

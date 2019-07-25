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
  <link rel="stylesheet" type="text/css" href="../../../css/newCss/main.css">
<link rel="stylesheet" type="text/css" href="../../../css/shicaisuyuan.css">
<link rel="stylesheet" type="text/css" href="../../../css/set1.css">
<link rel="stylesheet" type="text/css" href="../../css/swiper.min.css">
<script type="text/javascript" src="../../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js"></script>
<script type="text/javascript" src="../../../js/common.js"></script>
<script type="text/javascript" src="../../js/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript" src="../../js/lazyload.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
$(function(){
		$(".list_xinyang li").hover(function(){
	       $(this).addClass("ac");
        },function(){
	       $(this).removeClass("ac");
        })
})
</script>
</head>
<body>
   <div class="wid-100" id="wid-100">
      <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
      <div class="swiper-container" style="max-width: 1150px;min-width:1000px;height: auto;margin: 0 auto;display: block;position: relative;">
		<div class="swiper-wrapper">
		<c:set var="num" value="0"></c:set>
				<c:forEach items="${newsList}" var="list">
					<c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						<div class="swiper-slide"><a href="${list.redirectTo }"><img alt="" src="${list.documentDataMap.get('bannerImage').dataValue} "> </a> </div>
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
	    <div class="nav_bar wid-80 martop30">
    		<div class="btn_left"><span class="redbar"></span><span>食材信仰</span></div> 
			<div class="btn_right"><a class="orange" href="/content/shicaixinyang/shicaixinyang/index.shtml?rows=1000">查看更多</a></div>
        </div>
		<ul class="wid-80 list_xinyang martop15 marbottom30">
		   	  <c:forEach var="document" items="${shicaixinyangList}" begin="0" end="7">
			    <li>
			    <a href="${document.viewUrl}">
			       <img  class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png">
				   <div class="explain">
				     ${document.title}
					 </div>
					 </a>
				 </li>
				</c:forEach>
		</ul>	
	    </div>
	  <div class="box_container" style="background-color:#f8f8f8;">
	    <div class="nav_bar wid-80 martop30 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>营养膳食</span></div> 
			<div class="btn_right"><a class="orange" href="/content/collection/yingyangshanshi/index.shtml?rows=1000">查看更多</a></div>
        </div>
		<ul class="martop15 wid-80 list_shanshi">
		   	  <c:forEach var="document" items="${yingyangshanshiList}" begin="0" end="3">
			    <li>
			    <a href="${document.viewUrl}">
			       <img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="box_img lazy">
				   <div class="explain2">
				     ${fn:substring(document.title ,0,15)}${fn:length(document.title)>15?"...":""}
					 </div>
					 </a>
				 </li>
				</c:forEach>
		</ul>	
	    </div>
	   	   <div class="box_container">
	     <div class="nav_bar wid-80 martop30 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>食材百科</span></div> 
			<div class="btn_right"><a class="orange" href="/content/collection/shicaijianbie/index.shtml?rows=1000">查看更多</a></div>
        </div>
		<ul class="list_jianbie martop15 wid-80">
		 <c:forEach var="document" items="${shicaijianbieList}" begin="0" end="7">
		     <li>
		 		<a href="${document.viewUrl}">
			        <div class="box_img">
				      <img class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../../image/listImgDefault.png" title="${document.title}">
				   </div>
				   <p class="explain margin_sx">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
				   </a>
			</li>  
			</c:forEach>
		</ul>
	  </div>
	</div>
	 <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>

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
<link rel="stylesheet" type="text/css" href="../../../../css/newCss/main.css">
<link rel="stylesheet" type="text/css" href="../../../../css/newCss/videolist.css">
<link rel="stylesheet" type="text/css" href="../../../../css/newCss/swiper.min.css">
<script type="text/javascript" src="../../../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../../js/respond.src.js"></script>
<script type="text/javascript" src="../../../../../js/common.js"></script>
<script  type="text/javascript" src="../../../../js/jump.js"></script>
<script type="text/javascript" src="../../../../js/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript" src="../../../../js/lazyload.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
window.onresize=function(){
	mySwiper.onResize();
}
$(function(){
	$(".list_jianbie img").each(function(k, a) {
		new JumpObj(a, 10)
	});
})
</script>
</head>
<body id="wid-100">
	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="swiper-container" style="height: auto;max-width:1150px;min-width:1000px;margin: 0 auto;display: block;position: relative;">
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
	observer:true,
	observeParents:true,
	effect : 'fade',
	autoHeight: true,
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
	<div class="box_container padbtm50">
	    <div class="nav_bar wid-80 martop30">
    		<div class="btn_left"><span class="redbar"></span><span>精彩推荐</span></div> 
        </div>
		<ul class="list_jianbie wid-80 martop30">
			   <c:forEach var="document" items="${jingcaituijianList}" begin="0" end="9">
			    <li>
			    <a href="${document.viewUrl}">
			       <div style="position: relative;" >
			    		<img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="box-img lazy"/>
			            <div style="z-index: 1;width:53px;height: 53px;top:50%; left:50%;margin:-26px 0px 0px -26px;position: absolute!important ;background-image: url(../../../image/play.png);" ></div>
			    	</div>
			    	<!-- <p class="time_bar"></p>  --> 
				   <p class="box_title">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
				   </a>
				</li>
			</c:forEach>
		</ul>
		<div class="nav_bar wid-80 martop30 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>视频汇总</span></div> 
			<div class="btn_right"><a class="orange" href="/content/videolist/shipinghuizong/index.shtml">查看更多</a></div>
        </div>
        <ul class="martop30 wid-80 list_Video">
		   	<c:set var="num" value="0"></c:set>
			 <c:forEach var="document" items="${shipinghuizongList}" begin="0" end="11">
			  <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<13}">
			    <li>
			        <a href="${document.viewUrl}">
			    	<div class="box_img">
			            <p style="margin:0px !important"><img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/listImgDefault.png" class="lazy"></p>
				  		 <div style="z-index: 1;width:53px;height: 53px;top:50%; left:50%;margin:-26px 0px 0px -26px;position: absolute!important ;background-image: url(../../../image/play.png);" ></div>
				    </div>
				    <p class="fo-15 margin_sx">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
				   <p>${document.documentDataMap.get('documentBrief').dataValue}</p>
				    </a>
				</li>
				</c:if>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
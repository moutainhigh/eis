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
<link rel="stylesheet" type="text/css" href="../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../css/index.css">
<link rel="stylesheet" type="text/css" href="../../css/yidiyipin.css">
<link rel="stylesheet" type="text/css" href="../../css/shicaisuyuan.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>
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
	<nav>
	    <div class="banner-box">
		<div class="bd martop20" >
			<ul class="clearUl">
				<!--<li style="position: absolute; width: 1010px; left: 0px; top: 0px; display: none;">
					<div class="m-width"> <a href="#"><img alt="" src="../../../image/shicaisuyuan/banner1.jpg"/> </a> </div>
				</li>
				<li style="position: absolute; width: 1010px; left: 0px; top: 0px; display: list-item;">
					<div class="m-width"> <a href="#"><img alt="" src="../../../image/shicaisuyuan/banner2.jpg"> </a> </div>
				</li>
				<li style="position: absolute; width: 1010px; left: 0px; top: 0px; display: none;">
					<div class="m-width"> <a href="#"><img alt="" src="../../../image/shicaisuyuan/banner3.jpg"> </a> </div>
				</li>-->
				<c:set var="num" value="0"></c:set>
				<c:forEach items="${newsList}" var="list">
					<c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						   <li style="position: absolute; width: 1010px; left: 0px; top: 0px; display: none;">
							<div class="m-width"> <a href="${list.redirectTo }"><img alt="" src="${list.documentDataMap.get('bannerImage').dataValue} "> </a> </div>
							</li>
						</c:if>
					</c:if>
				</c:forEach>
			
			</ul>
		</div>

		<div class="hd">
			<ul>
			   <c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						   <li class=""></li>
						</c:if>
					</c:if>
			</ul>
		</div>
		
    
	  </div>
	   <script type="text/javascript" src="../../js/slider.min.js"></script>
	 <script>
	    	$(function(){
		$(".banner-box").slide({
			titCell:".hd ul",
			mainCell:".bd ul",
			effect:"fold",
			interTime:1200,
			delayTime:500,
			autoPlay:true,
			autoPage:true,
			trigger:"click"
		});
		$(window).resize(function(){
			$('.clearUl').height($(".clearUl li:first").height());
		});
	})
	 </script>
	   <div class="list_nav">
		  <ul class="list_ul">
			<li><a href="/content/best/index.shtml?tags=蔬菜水果" class="color-white">蔬菜水果</a></li>
			<li><a href="/content/best/index.shtml?tags=肉禽蛋奶" class="color-white">肉禽蛋奶</a></li>
			<li><a href="/content/best/index.shtml?tags=水产海鲜" class="color-white">水产海鲜</a></li>
			<li><a href="/content/best/index.shtml?tags=粮油调味" class="color-white">粮油调味</a></li>
			<li><a href="/content/best/index.shtml?tags=南北干货" class="color-white">南北干货</a></li>
			<li><a href="/content/best/index.shtml?tags=休闲食品" class="color-white">休闲食品</a></li>
			<li><a href="/content/best/index.shtml?tags=酒水茶饮" class="color-white">酒水茶饮</a></li>
			<li><a href="/content/best/index.shtml?tags=进口食品" class="color-white">进口食品</a></li>
		</ul> 
	</div>
	</nav>
	<div class="flotage">
		<a onclick="conceal()"><img src="../../image/erweima.jpg"/></a>
		<a href="#wid-100"><img src="../../image/up.jpg"/></a>
	</div>
	<div style="display:block;" class="two-dimension" id="two-dimension">
		<img src="../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	</div>
	<div class="box_container">
	    <h3 class="martop30 "><span class="orange spacing hot">优选</span><span class="spacing">预售</span><a href="/content/best/youxuanyushou/index.shtml" class="orange" style="font-size:12px!important;margin-left:20px;">更多...</a></h3>	
		<ul class="list5 martop45 wid-80">
		<c:set var="num" value="0"></c:set>
		<c:choose>
			<c:when test="${fn:length(youxuanyushouList)>0}">
				<c:forEach items="${youxuanyushouList}" var="document">
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
				   <p class="fo-13">${document.title}</p>
				   <p>${document.documentDataMap.get('documentBrief').dataValue}</p>
				</li>
				</a>
				</c:if>
				</c:if>
		</c:forEach>
			</c:when>
			<c:otherwise>
				<li style="padding-left:440px; font-size:14px; color:#333;">您所筛选的产品未参加活动</li>
			</c:otherwise>
		</c:choose>	 	
		</ul>
	</div>
	
	<div class="box_container">
	    <h3 class="martop30"><span class="orange spacing">优选</span><span class="spacing">推荐</span></h3>
		
		<ul class="list5 martop45 wid-80">
		   <c:choose>
			<c:when test="${fn:length(youxuantuijianList)>0}">
		<c:forEach items="${youxuantuijianList}" var="document">
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
				   <p class="fo-12 margin_sx">${document.title}</p>
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
	</div>
	  <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</div>
</body>
</html>

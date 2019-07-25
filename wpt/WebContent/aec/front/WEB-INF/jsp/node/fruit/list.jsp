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
<link rel="stylesheet" type="text/css" href="../../../css/newCss/index.css">
<link rel="stylesheet" type="text/css" href="../../../css/newCss/shop.css">
<link rel="stylesheet" type="text/css" href="../../../css/shicaisuyuan.css">
<script  type="text/javascript" src="../../../js/jquery.min.js"></script>
<script  type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../../js/pull-down list_index.js"></script>
<script  type="text/javascript" src="../../../js/common.js"></script>
<script type="text/javascript" src="../../../js/swiper.3.1.2.jquery.min.js"></script>
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
	 <div class="page_title fo-14"><ul class="wid-80"><li><a href="/">首页</a></li><li><a href="/content/product/index.shtml">以先商城</a></li><li><a href="#" class="orange">${pageTitle}</a></li></ul></div>
	<div class="box_container">
		<ul class="list_FV martop30 wid-80">
		<fmt:formatDate value="<%=new java.util.Date()%>" type="both" dateStyle="long" pattern="yyyy/MM/dd HH:mm:ss" var="now"/>
		    <c:choose>
			<c:when test="${fn:length(newsList)>0}">
		       <c:forEach items="${newsList}" var="document">
		        <c:if test="${document.documentTypeCode != 'image'}" >
			<li class="marbottom30">
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
						 <a class="fr" title="去看看" target="_blank" href="${document.viewUrl}">去看看</a>
					  </div>
					 </div>
					  
					  
				</a>
			</li>
			</c:if>
			</c:forEach>
			<c:forEach items="${noCountList}" var="document">
		        <c:if test="${document.documentTypeCode != 'image'}" >
			<li class="marbottom30">
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
						 <a class="fr" title="去看看" target="_blank" href="${document.viewUrl}">去看看</a>
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
	</div>
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

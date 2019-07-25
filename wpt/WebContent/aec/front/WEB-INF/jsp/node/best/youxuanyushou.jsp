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
<link rel="stylesheet" type="text/css" href="../../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../../css/index.css">
<link rel="stylesheet" type="text/css" href="../../../css/yidiyipin.css">
<script  type="text/javascript" src="../../../js/jquery.min.js"></script>
<script  type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../../js/pull-down list_index.js"></script>
<script  type="text/javascript" src="../../../js/common.js"></script>
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
	   <ul class="list_img">
	     <li><img src="../../../image/banner/img1.jpg"></li>
		 <li><img src="../../../image/banner/img2.jpg"></li>
		 <li><img src="../../../image/banner/img3.jpg"></li>
	   </ul>
	
	</nav>
	<div class="flotage">
		<a onclick="conceal()"><img src="../../../image/erweima.jpg"/></a>
		<a href="#wid-100"><img src="../../../image/up.jpg"/></a>
	</div>
	<div style="display:black;" class="two-dimension" id="two-dimension">
		<img src="../../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	</div>
	<div class="box_container">
	    <h3 class="martop30 "><span class="orange spacing hot">优选</span><span class="spacing">预售</span></h3>	
		<ul class="list5 martop45 wid-80">
		
		<c:choose>
			<c:when test="${fn:length(newsList)>0}">
			
				<c:forEach var="document" items="${newsList}">
				<c:choose>
					<c:when test="${document.documentTypeId=='171005'}">
					<c:if test="${document.documentTypeCode != 'image'}" >
				
				<a href="${document.viewUrl}">
			    <li>
			    	<div class="box_img">
			       <p><img src="${document.documentDataMap.get('productSmallImage').dataValue}"></p>
			         <div class="mask">
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
					</c:when>
					<c:otherwise>
						
					</c:otherwise>
				</c:choose>
			   </c:forEach> 
			</c:when>
			<c:otherwise>
				<li style="padding-left:440px; font-size:14px; color:#333;">暂时没有相关产品</li>
			</c:otherwise>
		</c:choose>
		
			 	
		</ul>
	</div>
	
	
	
	
	<!--<div class="wid-80">
	<ul class="listbox"> 
			 <a href="#"><li><</li></a> 
			 <a href="#"><li>2</li></a> 
			 <a href="#"><li>3</li></a> 
			 <a href="#"><li>4</li></a> 
			 <a href="#"><li>5</li></a> 
			 <a href="#" class="especially"><li>.</li></a> 
			 <a href="#" class="especially"><li>.</li></a> 
			 <a href="#" class="especially"><li>.</li></a> 
			 <a href="#"><li>9</li></a> 
			 <a href="#"><li>></li></a>   
		</ul>
		</div>
	-->
	  <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</div>
</body>
</html>

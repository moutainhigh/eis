<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css?v=?0701121438"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/index.css?v=0701121438"/>
<script  type="text/javascript" src="../../../theme/${theme}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../theme/${theme}/js/respond.src.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/swiper.3.1.2.jquery.min.js"></script>
</head>
<body>
	<%@include file="/WEB-INF/jsp/include/header.jsp" %>
		  <div class="navbar navbar1 mt5">
		     <p><span class="orange">	 <c:choose>
				<c:when test="${node.name=='农家果'}">
					农家果
				</c:when>
				<c:when test="${node.name=='农家蛋'}">
					农家蛋
				</c:when>
				<c:when test="${node.name=='农家肉'}">
					农家肉
				</c:when>
				<c:otherwise>
					全部食品
				</c:otherwise>
			 </c:choose></span></p>
		  </div>	
		  <ul class="productList">
		  <c:choose>
		  <c:when test="${fn:length(fruitList)>0}">
		   <c:forEach items="${newsList}" var="document">
		        <c:if test="${document.documentTypeCode != 'image'}" >
			    <li>
				<a href="${document.viewUrl}">
				<img data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../../theme/${theme}/image/mobile/listImgDefault.png" class="lazy"/>
				<div class="p-info">
					<div class="left">
					  <p>${document.title}</p>
					  <p><span class="old_price">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span><span class="orange">￥${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span></p>
					</div>
					<span class="addToCart right">加入购物车</span>
					<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
				</div>
				<c:if test="${document.documentDataMap.get('availableCount').dataValue==0}">
					<span class="no-stock">已售罄</span>
				</c:if>
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
	<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>
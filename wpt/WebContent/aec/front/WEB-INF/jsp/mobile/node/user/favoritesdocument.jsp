<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<title>${systemName}</title>
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/footer.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/favoritesdocument.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../../theme/${theme}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span >我的收藏</span></a>
  </div>
 <div class="wrapper">
   <ul class="favoriteList clearfix">
    <c:forEach var="favorite" items="${userFavoriteList}" varStatus="i" >
      <c:choose>
						<c:when test="${empty favorite}">
							<li>没有更多内容了</li>
						</c:when>
						<c:otherwise>
              <li>
                  <a href="${favorite.data.refUrl}"><img src="${favorite.data.refImage}" >
                 <p>${favorite.data.refTitle}</p>
                 <p style="color: #89B307;">¥39</p>
                    </a>
                      <!-- <p class="ware_ymd"><fmt:formatDate value="${favorite.createTime}"  type="both"/></p> -->
                     <!--  <div class="ware_hws">
                         <span><fmt:formatDate value="${document.publishTime}" pattern="HH:mm:ss "/></span>
                      </div> -->
                  
              </li>
						</c:otherwise>
        </c:choose>
    </c:forEach>
   </ul>
   </div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>
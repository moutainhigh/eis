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
        <a class="back" href="javascript:history.go(-1);"></a><span >帮助中心</span></a>
    </div>
    <div class="wrapper">
        <ul class="favoriteList">
            <c:forEach items="${newsList}" var="a" varStatus="status">
                <c:choose>
                    <c:when test="${empty a}">
                        <li>没有更多内容了</li>
                    </c:when>
                    <c:otherwise>
                        <li style="height:auto;">
                            <div class="afbox">
                                <div class="zuic">
                                    <c:choose>
                                        <c:when test="${empty a.documentDataMap.productGallery.dataValue}">
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${a.viewUrl}"><img src="/file/${a.documentDataMap.productGallery.dataValue}" style="width:100%;height:auto;"></a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="zhtw">                           
                                    <h3 class="plain1" style="padding:5px 0;color:#92B431;font-size:14px;">文章标题：<a href="${a.viewUrl}">${a.title}</a></h3>
                                    <h3 class="plain1 mt15"  style="padding:5px 0;color:#92B431;font-size:14px;">文章内容：${a.content}</h3>
                                    <!--<c:if test="${!empty commention.data.get('productGallery')}">
                                        <ul class="img-list">
                                            <c:forEach var="img" items="${fn:split(commention.data.get('productGallery'),',')}">
                                                <li><img src="/static/userUploadDir/${img}"></li>
                                            </c:forEach>									
                                        </ul>
                                    </c:if>-->
                                    <div class="other" style="margin: 5px 0;"><div><fmt:formatDate value="${a.publishTime}"  type="both"/></div>   </div>
                                </div>
                            </div>                      
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>  
        </ul>  
    </div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>
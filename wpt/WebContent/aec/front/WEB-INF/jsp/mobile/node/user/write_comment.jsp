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
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/productdetail.js"></script>

</head>
<body>
    <div class="header" id="header">
        <a class="back" href="javascript:history.go(-1);"></a><span >我的评论</span></a>
    </div>
    <!--评论入口-->
    <c:choose>
        <c:when test="${empty document.documentDataMap.get('productCode')}">
                <input type="hidden" value="document" id="productType">
        </c:when>
        <c:otherwise>
            <input type="hidden" value="product" id="productType">
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="objectId" value="${document.udid}"  id="udid">
    <input type="hidden" value="${document.title}" id="title">
    <input type="hidden" name="uuid" value="${frontUser.uuid}">
    <div class="goods_comm">发表评论</div>
    <div class="comment">
        <div style="margin-top:15px;">
            <textarea  name="content"  aria-required="true" placeholder="字数不能超过5000字..." style="width:100%;height:200px;padding: 10px;box-sizing: border-box;border: 1px dashed #dedede;margin-bottom: 10px;border-radius: 5px;"></textarea>
        </div>
        <button onclick="comments()" style="float: right;border: 0;background: #7da215;color: #fff;margin-bottom: 15px;padding: 5px 20px;box-sizing: border-box;border-radius: 5px;">评论</button>
    </div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>
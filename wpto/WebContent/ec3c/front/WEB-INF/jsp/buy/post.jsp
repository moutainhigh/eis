<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="renderer" content="webkit">
    <title>${systemName}</title>`
</head>
<body onload="document.getElementById('payInfo').submit();">
    <div class="content wrap1000">
        <form action="${url}" method="POST" id="payInfo">
            <c:forEach items="${postData}" var="item" >
                <input type="hidden" name="${item.name}" value="${item.value}"/>
            </c:forEach>
        </form>
    </div>
</body>
</html>

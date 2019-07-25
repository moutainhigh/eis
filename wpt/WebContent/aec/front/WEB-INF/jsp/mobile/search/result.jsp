<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!--<div style="height:30px; background-color:#F0F">
</div> -->
<c:forEach items="${searchList }" var="sl">
	<li onclick="selectInput(this)" onmouseover="mouseOver(this)" onmouseout="mouseOut(this)" style="background-color:#fff; padding-left:10px; height:20px; line-height:20px; font-family:simsun; font-size:14px; color:#333;">${sl}</li>
</c:forEach> 

  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
   <div class="box1 cate" id="cate">
   	<c:forEach items="${navigation}" var="a" >
		<h1 onclick="tab(0)"  style="border-top:none">
		<span class="f_l"><img src="../../../../theme/${theme}/images/btn_fold.gif" style="padding-top:10px;padding-right:6px;cursor:pointer;"></span>
		<a href="${a.viewUrl}"  class="f_l">${a.name}</a>
		</h1>
		<ul style="display:none" >
				</ul>
		<div style="clear:both"></div>
	 </c:forEach>				
</div>
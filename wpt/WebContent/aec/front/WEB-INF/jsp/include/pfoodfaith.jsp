<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<div class="box_left">
	     <div>
	      <div class="box_title">食材信仰</div>
		  <ul>
           <c:forEach var="document" items="${newsList}" >
		     <li><a href="${host}/${contentPrefix}${document.defaultNode.path}/${documentPrefix}${document.documentCode}${pageSuffix}">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</a></li>
               </c:forEach>
			
		  </ul>
		  </div>
</div>
   
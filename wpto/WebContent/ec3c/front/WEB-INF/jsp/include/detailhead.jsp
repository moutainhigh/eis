<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<div class="header"  id="header"> <a class="home"
 href="/"></a><a 
<c:choose>  
    <c:when test="${iscollection eq 0}">  
    class="nofav" 
     </c:when>
      <c:otherwise>  
        class="fav" 
    </c:otherwise>  
</c:choose>  


href="#" onclick="collection(${documentCode});" id="fav"></a> <a class="dmenu" href="/content/shicaixinyang/index.shtml"></a><a href="#">${fn:substring(pageTitle ,0,6)}${fn:length(pageTitle)>6?"...":""}</a> </div>






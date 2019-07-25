<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<script>
$(document).ready(function () {
var eis_username=decodeURI(Cookie.getCookie("eis_username"));
 if(Cookie.getCookie("eis_username")==null){
         
		 Cookie.delCookie("eis_username");
         location.href = "/content/user/login.shtml";
         

     }
});

</script>
<div class="header"  id="header"> <a 
<c:choose>  
    <c:when test="${'user'==node.alias}">  
    class="set" href="/content/user/setting.shtml"
     </c:when>
      <c:otherwise>  
      class="dhed" href="/content/user/pcenter.shtml?favPage=1&favRows=10"
    </c:otherwise>  
</c:choose> 


 ></a> <a class="dmenu" href="javascript:goBack();"></a><a href="/">${pageTitle}</a> </div>
 

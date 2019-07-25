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
<div class="header"  id="header"> <a class="home1"
 href="/"></a><a 

 
    class="set" 
 
      



href="/content/user/setting.shtml"></a> <a class="dmenu" href="javascript:goBack();"></a><a href="#">${pageTitle}</a> </div>






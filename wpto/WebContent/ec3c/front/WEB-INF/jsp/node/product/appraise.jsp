<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>以先</title>
<link href="/style/main.css" rel="stylesheet" type="text/css">
<link href="/style/message.css" rel="stylesheet" type="text/css">
<link href="/style/pmenu.css" rel="stylesheet" type="text/css">
<link rel="icon" href="/style/images/favicon.ico" type="image/x-icon" />
<script src="/pc/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/pc/js/common.js"></script>
<script>
$(function(){
	$('textarea').keyup(function () {
       $(this).height(this.scrollHeight);
   });
	})
</script>
</head>
<body>
<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="content">
    
       <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	   <div class="box_right">
	      <div class="box_title"><span class="line_orange"></span><span>首页> 个人中心 > 评价晒单</span></div>
		  <div class="bg_white">
		  
		 123
	   </div>
    </div>
   </div>
   <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>

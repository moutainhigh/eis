<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
 <meta name="apple-mobile-web-app-capable" content="yes" />
 <meta name="apple-mobile-web-app-status-bar-style" content="black" />
 <link rel="icon" href="image/favicon.ico" type="image/x-icon" />
 <title>以先</title>
 <link href="/css/main.css" rel="stylesheet" type="text/css"> 
<link rel="stylesheet" type="text/css" href="/css/header.css">
 <link href="/css/paysuccess.css" rel="stylesheet" type="text/css">
 <link href="/css/pmenu.css" rel="stylesheet" type="text/css">
 <link rel="stylesheet" href="/css/jquery.spinner.css" />
 <script src="/js/jquery-1.7.1.js"></script>
 <script type="text/javascript" src="/js/common.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
 <style>
	.box_right{
		float:none;	
	}
 </style>
 
 </head>
 <body>
	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="content">
	<div class="code_bg1">
	    <div class="box_top">
		  <div class="box_right">
		     <p><span class="text_success">恭喜！亲已支付成功！</span></p>
		     <p><span class="text_wait">订单正在马不停蹄的处理中，请您耐心等待...</span></p>
		  </div>
		</div>
		<p class="a_group">您现在可以去:<a href='/order/index.shtml'>查看我的订单</a><a href="/">继续逛逛</a></p>
	</div> 
</div>
</body>
</html>

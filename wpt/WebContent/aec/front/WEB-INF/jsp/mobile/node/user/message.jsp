<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<title>给我留言</title>
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/message.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script type="text/javascript">
	 function cooperation(){
	 	alert("感谢您的支持，谢谢合作!")
	 	location.reload();
	 }
</script>
</head>
<body>
 <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>给我留言</span>
  </div>
<!--  <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %> -->
 <form class="content">
   <textarea  class="txt" placeholder="对我们的改进意见？
发现了新食材要推荐给我们？
想说点什么就说点什么吧"></textarea>
   <div class="btn_confirm"><input type="button" value="确认" class="bluebutton" onClick="cooperation()"/></div>
 </form> 
</body>
</html>
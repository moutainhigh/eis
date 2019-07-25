<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/about.css"/>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<!-- <script type="text/javascript" src="http://api.map.baidu.com/api?key=&v=1.1&services=true"></script> -->
</head>
<style>

#wrapper_1 >.text{
	display: inline-block;
	width: 46%;
	padding:20px 0;
	background-color: rgba(109, 187, 7, 0.34);
	margin-bottom: 9px;
}
#wrapper_1 >.text.site{
	width: 94%;
}
</style>

<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>${node.name}</span><a class="list2" href="/"></a>
 </div>
	<div id="wrapper_1" class="contact">
		<div class="orange wid90" style="margin-bottom:20px;">
		<p class="title">农青公司</p>
		</div>
			<div class="text"><p>微信</p><p>181818</p></div>
			<div class="text"><p>Q Q</p><p>1611661</p></div>
		    <div class="text"><p>邮箱</p><p>111111@163.com</p></div>
		    <div class="text"><p>电话</p><p>886-995785</p></div>
			<div class="text site">地址：湖北省武汉市洪山区街道口阜华大厦</div>
	 </div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

</body>

</html>
   
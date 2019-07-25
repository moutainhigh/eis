<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
<link rel="stylesheet" href="../../../theme/${theme}/css/iconfont.css">
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<style>
	.iconfont{
		font-size: 22px;
	}
</style>
<body>
	<div class="header color_bg" id="header" style="text-align:left">
	  <a class="back" href="javascript:history.go(-1)"></a>
	  <div class="inputBox">
	    <span class="sprite-icon"></span>
	    <input type="text" placeholder="搜索以先商城商品" id="headerInput"/>
	  </div>
	  <a class="hanbao" id="hanbao">
		<i class="icon iconfont icon-kai"></i>
	  </a>
	  <div class="erweimaCode" id="erweimaCode">
	     <img src="/image/QRcode.png">
		 <p><a href="/content/about/20160709160756.shtml">关于我们</a></p>
	  </div>
	</div>
</body>
</html>


	
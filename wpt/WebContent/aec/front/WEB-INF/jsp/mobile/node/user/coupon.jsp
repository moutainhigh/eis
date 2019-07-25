<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
<title>${systemName}</title>
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/footer.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/favoritesdocument.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../../theme/${theme}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
</head>
<style>
	ul.couponlist li{
		width: 95%;
	    background-color: #eee;
	    margin-top: 13px;
	    height: 94px;
	    padding-top: 15px;
	    margin: 16px auto;
	        box-shadow: 3px -3px 1px rgba(197, 197, 197, 0.88);
	}
	ul.couponlist li img{
		float: left;
		    margin-right: 20px;
		width: 85px;
	}
	ul.couponlist li .right{
		float: left;
		    margin-top: 10px;
	}
	ul.couponlist li .right .bigfont{
		font-size: 30px;
    	color: #83AF19;
    	margin-right: 5px;
	}
	ul.couponlist li .right .smalleee{
		font-size: 14px;
		color: #949494;
		margin-top: 7px;
	}
	    
</style>
<body>
    <div class="header" id="header">
        <a class="back" href="javascript:history.go(-1);"></a><span >我的代金券</span></a>
    </div>
    <div class="wrapper">
        <ul class="favoriteList couponlist">
            <li class="clearfix">
				<img src="../../../theme/${theme}/image/mobile/Coupon/qian.png" alt="">
            	<div class="right"><p><span class="bigfont">50</span>元，满199可使用</p>
            	<p class="smalleee">有效期 2017.11.11 - 2017.11.11</p></div>
            </li>
            <li class="clearfix">
				<img src="../../../theme/${theme}/image/mobile/Coupon/qian.png" alt="">
            	<div class="right"><p><span class="bigfont">100</span>元，满299可使用</p>
            	<p class="smalleee">有效期 2017.11.11 - 2017.11.11</p></div>
            </li>
            <li class="clearfix">
				<img src="../../../theme/${theme}/image/mobile/Coupon/past.png" alt="">
            	<div class="right"><p style="color: #949494;"><span class="bigfont" style="color: #949494;" >20</span>元，满99可使用</p>
            	<p class="smalleee">有效期 2017.11.11 - 2017.11.11</p></div>
            </li>
        </ul>
    </div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>
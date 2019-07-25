
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>七星乐器-支付结果</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
    <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
</head>
<body>
    <div class="row-fluid">
        <div class="span8">
            <div class="widget-box">
                <div class="code_bg1">
                    <div class="box_top">
                    <div class="box_right" style="padding:35%;">
                        <p><span class="text_success">恭喜！亲已支付成功！</span></p>
                        <p><span class="text_wait">您可以去<a href="/user/purchasedList.shtml?flag=product" style="color: #F58B3C;">已购教学</a></span>查看您的订单</p>
                    </div>
                    </div>
                    <p class="a_group" style="padding-left: 70%;">您现在可以去:<a href='/user/purchasedList.shtml?flag=product' style="padding-left: 10px;">查看我的订单</a><a href="/" style="padding-left: 10px;">继续逛逛</a></p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
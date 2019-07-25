<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../css/bangzhuzhongxin.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>
</head>
<body>
   <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
<script type="text/javascript" src="../../../js/pull-down list.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/pull-down list.css" /> 
    <div class="wid-60 padtop50 padbtm50">
    	<div class="box_container_left">
    		<!--<span class="help_center_title"></span>-->
            <span class="help_center">帮助中心</span>
    		<ul class="navigation">
    		<li><a href="/content/notice/20151125160620.shtml">购物指南</a></li>
    		<li><a href="/content/notice/20151125142743.shtml">账户注册</a></li>
    		<li><a href="/content/notice/20151125131746.shtml">发票说明</a></li>
    		</ul>
        	<span class="help_center">支付配送</span>
        	<ul class="navigation">
            <li><a href="/content/notice/20151125140740.shtml">支付方式</a></li>
            <li><a href="/content/notice/20160709155948.shtml">配送方式</a></li>
        	</ul>
        	<span class="help_center">售后服务</span>
        	<ul class="navigation">
            <li><a href="/content/notice/20170227100648.shtml">退换货政策</a></li>
			<li><a href="/content/notice/20151125152356.shtml">常见问题</a></li>
            <li><a href="/content/notice/20151125133710.shtml">投诉与建议</a></li>
           <!--  <li>
			  <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1294919039&site=qq&menu=yes">
			    在线客服
			  </a>
			</li> -->
       		</ul>
        	<span class="help_center">卡券使用</span>
        	<ul class="navigation">
            <!-- <li><a href="#">常见问题</a></li> -->
            <li><a href="/content/notice/20151125135511.shtml">代金券消费</a></li>
        	</ul>
			<!-- <span class="help_center">合作申请</span>
            <ul class="navigation">
            <li><a href="/content/notice/20160709194847.shtml">企业合作</a></li>
            <li><a href="/content/notice/20160709194520.shtml">合作社合作</a></li>
			<li><a href="/content/notice/20160709194628.shtml">个体农商户合作</a></li>
            </ul>-->
    	</div>
    <div class="box_container_right box_content">
    		${document.content}
    </div>
   </div>
    <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
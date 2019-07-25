<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
     <title>公众号管理-${systemName}</title>
	<link href="/theme/${theme}/style/caiwangbao/common.css" rel="stylesheet">	   
	<link href="/theme/${theme}/style/caiwangbao/bind_wx.css" rel="stylesheet">	   
	<!--[if lt IE 9]>
	<script src="js/respond.min.js"></script>
	<![endif]-->   
    <script src="/theme/${theme}/js/jquery.js"></script>
  </head>
  <body>	
	<div class="top">
		<div class="nav wid-nav">
			<div class="logo">
				<img src="/theme/${theme}/images/logo/${systemCode}.png" class="logo-img"/>
			</div>
			<ul class="nav-menu">
				<li>您好，欢迎来到${systemName}！</li>
				<li>注册</li>
				<li>登录</li>
				<li>购买记录</li>
				<li>我是买家</li>
				<li><span class="download-icon"><img src="/theme/${theme}/images/download_icon.png" class="download-icon-img"/></span>下载app</li>
			</ul>
		</div>
	</div>
	<div class="main">
		<div class="wid-cont wx-bind-cont">
			<ul class="tab-menu visible clearfl">
				<li><span class="tab-num">1</span><span class="tab-item-cont">注册${systemName}</span></li>
				<li class="current"><span class="tab-num">2</span><span class="tab-item-cont">公众号管理</span></li>
				<li><span class="tab-num">3</span><span class="tab-item-cont">选择主题</span></li>
			</ul>
			<div class="cont-title">
				绑定指引
			</div>
			<div class="wx-cont">
				<p class="tishi-img"><img src="/theme/${theme}/images/tishi/tishi2.png" alt=""></p>
				<div class="tishi-txt">
					<p>提示：</p>
					<p>点击下一步，将跳转到微信开放平台绑定您的公众号，请用公众号管理员的微信扫码，并同意所有权限，这样我们才能为您提供更好的服务。</p>		
				</div>
			</div>
			<a href="${authUrl}" class="block">
				<div class="btn next-step">
					下一步
				</div>
			</a>
		</div>
	</div>
	<div class="bottom footer">
		${commonFooter}
	</div>
  </body>
</html>
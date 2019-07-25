<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
    <title>选择主题-${systemName}</title>
	<link href="/theme/${theme}/style/caiwangbao/common.css" rel="stylesheet">	   
	<link href="/theme/${theme}/style/caiwangbao/theme_chose.css" rel="stylesheet">	   
	<!--[if lt IE 9]>
	<script src="/theme/${theme}/js/respond.min.js"></script>
	<![endif]-->   
    <script src="/theme/${theme}/js/jquery-1.11.3.min.js"></script>
    <script src="/theme/${theme}/js/noticeBox.js"></script>
    <script src="/theme/${theme}/js/theme_chose.js"></script>
  </head>
  <body>	
	<div class="top">
		<div class="nav wid-nav">
			<div class="logo">
				<a href="/" class="block"><img src="/theme/${theme}/images/logo/${systemCode}.png" class="logo-img"/></a>
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
		<div class="wid-cont theme-bind-cont">
			<ul class="tab-menu visible clearfl">
				<li><span class="tab-num">1</span><span class="tab-item-cont">注册${systemName}</span></li>
				<li><span class="tab-num">2</span><span class="tab-item-cont">公众号管理</span></li>
				<li class="current"><span class="tab-num">3</span><span class="tab-item-cont">选择主题</span></li>
			</ul>
			<div class="cont-title">
				选择主题
			</div>
			<div class="theme-cont">
				<ul class="theme-list  theme-list-current display-none">
					<c:forEach var="i" items="${themeList}">
					<li>
						<div class="item">
							<div class="center"><img src="/theme/${theme}/images/theme_img_1.png" class="theme-img"/></div>
							<div class="theme-title">${i.themeId}-${i.themeName}</div>
						</div>
						<input type="hidden" value=${i.id} class="theme-id"/>
					</li>
					</c:forEach>
				</ul>
			</div>
			<%@include file="/WEB-INF/jsp/common/include/paging/round_index.jsp"%>
			<div class="btn finish-step">
				设置完成
			</div>
		</div>
	</div>
	<div class="bottom footer">
		${commonFooter}
	</div>
  </body>
</html>
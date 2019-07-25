<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>

<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">${systemName}管理中心</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
         
		<ul class="nav navbar-nav navbar-right hidden-xs">
            <li><a href="/config.shtml" target="_blank">设置</a></li>
            <li><a href="/user/logout.shtml">退出</a></li>
            <li><a href="#">关于</a></li>
          </ul>
          <div class="navbar-right hidden-xs" style="color:#9d9d9d;height:50px;line-height:50px;">
			${securityLevelDesc} 欢迎您，${welcomeName}！
          </div>
		   <ul class="nav navbar-nav navbar-right operate" id="operate">
			<li><a id="text" href="#">文本</a></li>
			<li><a id="bgd" href="#">背景</a></li>
			<li><a id="music" href="#">音乐</a></li>
			<li><a id="video" href="#">视频</a></li>
			<li><a id ="picture" href="#">图片</a></li>
			<li><a id="input" href="#">输入框</a></li>
			<li><a id="button" href="#">按钮</a></li>
			<li><a id="pics" href="#">图集</a></li>
			<li class="redHover"><a id="save" href="#">保存</a></li>
		</ul>	
		<ul class="nav navbar-nav navbar-right operate setting" >
            <li><a href="/config.shtml" target="_blank">设置</a></li>
            <li><a href="/user/logout.shtml">退出</a></li>
            <li><a href="#">关于</a></li>
          </ul>
        </div>
      </div>
    </nav>
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
        <div id="navbar" class="navbar-collapse collapse gysztc">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/config.shtml" target="_blank">设置</a></li>
            <li><a href="/user/logout.shtml">退出</a></li>
            <li><a href="#" style="margin-right:20px;">关于</a></li>
          </ul>
          <div class="navbar-right" style="color:#9d9d9d;height:50px;line-height:50px;">
			${securityLevelDesc} 欢迎您，${welcomeName}！
          </div>
        </div>
      </div>
    </nav>
    <style>
.navbar-inverse .navbar-nav>li>a{
  color: #fff;
}
    </style>
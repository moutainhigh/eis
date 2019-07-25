<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<link rel="stylesheet" type="text/css" href="../../css/jquery.spinner.css">
<header id="header1">
	    <div class="wid-80">
		   <img src="../../image/logo.png" class="logo"/>
		     <div class="box_container_header1">
			   <ul class="btns box_container_right fo-12">
			     <li class="username_res orange" >您好，欢迎来到以先365</li>
			      <li class="zhanghu_res"> <a href="/content/user/login.shtml" class="orange">登陆 | 注册</a></li>
				  <a href="/cart.shtml"  target="_blank" class="orange"><li>我的购物车</li></a>
				  <a href="/order/index.shtml" class="orange"><li>我的订单</li></a>
				  <a href="/content/notice/20151125134300.shtml"  class="orange"><li>联系我们</li></a>
                  
				  <a href="/content/notice/20151125160620.shtml" class="orange"><li>帮助中心</li></a>
			   </ul>	  
				<ul class="btngroup box_container_right martop15"> 
					<a href="/"><li class="${selectMenu=='index'?'active':'' }">首页</li></a>
					<a href="/content/shicaixinyang/index.shtml"><li class="${selectMenu=='articlelist'?'active':'' }">食材溯源</li></a>
					<a href="/content/collection/index.shtml"><li class="${selectMenu=='shicaiku'?'active':'' }">食材库</li></a>
					<a href="/content/product/index.shtml"><li class="${selectMenu=='produstlist'?'active':'' }">一地一品</li></a>
					<a href="/content/best/index.shtml"><li class="${selectMenu=='youxuan'?'active':'' }">以先优选</li></a>
					<a href="/content/interactive/index.shtml"><li class="${selectMenu=='hudong'?'active':'' }">互动专区</li></a>
					<a href="/content/about/aboutus.shtml"><li class="${selectMenu=='aboatus'?'active':'' }">关于我们</li></a>
				</ul>
			</div>
		</div>	
		
	</header>
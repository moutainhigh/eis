<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!--<script type="text/javascript" src="../../../js/pull-down list.js"></script>-->

  <header id="header2">
	    <div class="wid-80">
		   <img src="../../image/logo.png" class="logo  martop18"/>
		
		    <div class="box_container_header2">
			   <ul class="btns box_container_right fo-12">
			     <li class="username_res orange" >您好，欢迎来到以先365</li>
			      <li class="zhanghu_res"> <a href="/content/user/login.shtml" class="orange">登陆 | 注册</a></li>
				  <a href="/order/index.shtml" class="orange"><li>我的订单</li></a>
				  <a href="/content/notice/20151125134300.shtml"  class="orange"><li>联系我们</li></a>
                  
				  <a href="/content/notice/20151125160620.shtml" class="orange"><li>帮助中心</li></a>
			   </ul>
			    <div class="box_container martop25">
					<div id="search-form2">
                    	<form action="${pageContext.request.contextPath}/search/list.shtml" method="post">
							<div id="search-hd" class="search-hd">
								<div class="search-bg">
							  		<span class="pholder"></span>
							  		<input type="hidden" name="projectName" id="projectName" value="${pageContext.request.contextPath}"/>
									<input type="text" name="keywords" id="shuRu2" value="${inputResult }" autocomplete="off" onkeyup="doAjax()" class="search-input">
								</div>
								<button id="submit" class="btn-search" value="搜索">搜索</button>
							</div>
                         </form>
                         <ul id="ulId">
                         </ul>
					</div>
					<a href="/cart.shtml">
					<div class="mycart">
					   <span class="mycart_icon"></span>
					   我的购物车
					</div>
					</a>
			   </div>
				<ul class="btngroup box_container_right martop15"> 
					<a href="/"><li>首页</li></a>
					<a href="/content/shicaixinyang/index.shtml"><li>食材溯源</li></a>
					<a href="/content/collection/index.shtml"><li>食材库</li></a>
					<a href="/content/product/index.shtml"><li>一地一品</li></a>
					<a href="/content/best/index.shtml"><li class="${selectMenu=='youxuan'?'active':'' }">以先优选</li></a>
					<a href="/content/interactive/index.shtml"><li class="${selectMenu=='hudong'?'active':'' }">互动专区</li></a>
					<a href="/content/about/aboutus.shtml"><li>关于我们</li></a>
				</ul>
			</div>
			
		</div>	
		
	</header>
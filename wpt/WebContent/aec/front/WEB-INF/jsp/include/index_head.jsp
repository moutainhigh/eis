<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<link rel="stylesheet" type="text/css" href="/css/jquery.spinner.css">
<header id="header1">
        <div class="wid-80">
		</div>
	    <div class="wid-80">
		  <a href="/"> <img src="/image/logo.png" class="logo  martop18"/></a>
		     <div class="box_container_header1">
			   <ul class="btns box_container_right fo-12" style="">
			      <li class="zhanghu_res"> <a href="/content/user/login.shtml" class="orange">登录 | 注册</a></li>
			   </ul>
				<div class="box_container martop25" style="display:none;" id="search_show">
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
							<ul id="ulId" class="shelper2">
							</ul>
                         </form>
                        
					</div>
					<a href="/cart.shtml">
					<!--<div class="mycart">
					   <span class="mycart_icon"></span>
					   我的购物车
					</div>-->
					</a>
			   </div>
				<ul class="btngroup box_container_right martop18 menu"> 

					<a href="/"><li class="${selectMenu=='index'?'active':'' }" >首页</li></a>
					<a href="/content/shicaixinyang/index.shtml"><li class="${selectMenu=='articlelist'?'active':'' }">食材溯源</li></a>
					<a href="/content/collection/index.shtml"><li class="${selectMenu=='shicaiku'?'active':'' }">食材库</li></a>
					<a href="/content/product/index.shtml?rows=1000"><li class="${selectMenu=='produstlist'?'active':'' }">以先商城</li></a>
					<!--<a href="/content/best/index.shtml"><li class="${selectMenu=='youxuan'?'active':'' }">以先优选</li></a> -->
					<!-- <a href="/content/interactive/index.shtml"><li class="${selectMenu=='hudong'?'active':'' }">互动专区</li></a>-->
					<a href="/content/about/20160709160756.shtml"><li class="${selectMenu=='20160709160756'?'active':'' }">关于我们</li></a>
				</ul>
			</div>
		</div>	
		
	</header>
	
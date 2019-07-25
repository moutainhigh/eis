<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<script type="text/javascript" src="../../../js/pull-down list.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/pull-down list.css" /> 
<link rel="stylesheet" type="text/css" href="../../../css/newCss/pheader.css" /> 
<script type="text/javascript" src="../../../js/lazyload.js"></script>
  <header id="header">
  		<div class="headtop fo-12">
		    <div class="wid-80">
  		    <span class="header_left">您好，欢迎来到以先！</span>
  			<ul class="header_right">
  				<li class="zhanghu_res"> <a href="/content/user/login.shtml" class="color-white">登录</a> | <a href="/content/user/login.shtml" class="color-white">注册</a></li>
  				<li><a href="/order/index.shtml?page=1&rows=10" class="color-white">我的订单</a></li>
				<li class="lHidden"><a href="/content/user/pcenter.shtml?favPage=1&favRows=8" class="color-white">我的以先</a></li>
  				<li class="${selectMenu=='20160709160756'?'active':'' }"><a href="/content/about/20160709160756.shtml">关于我们</a></li>
  			</ul>
			</div>
  		</div>
		<div class="header_center box_container">
	    <div class="wid-80">
		   <a href="/"><img src="../../../image/logo.png" class="web_logo box_container_left"/></a>		
		    <div class="navDiv">
			   <div class="box_container">
			     <div class="box_con" style="display:block;" id="search_show">
				    <div class="mycart">
					<a href="/cart.shtml">
					   <span class="mycart_icon1"></span>
					   <span class="text_icon">我的购物车</span>
					   <img src="../../../image/btn_right.png" class="arrow_right_icon" />
						</a>
					</div>
					<div id="search-form3">
                    	<form action="${pageContext.request.contextPath}/search/list.shtml" method="post">
							<div id="search-hd" class="search-hd">
								<div class="search-bg">
							  		<input type="hidden" name="projectName" id="projectName" value="${pageContext.request.contextPath}"/>
									<input type="text" name="keywords" id="shuRu2" value="${inputResult }" autocomplete="off" onkeyup="doAjax()" class="search-input" placeholder="搜索">
									<!-- <span class="pholder" id="submit"></span> -->
									<input type="submit" class="pholder">
								</div>
								 <!-- <button id="submit" class="btn-search" value="搜索">搜索</button> -->
							</div>
							<ul id="ulId" class="shelper2">
							</ul>
                         </form>
					</div>
			   </div>
			   </div>
			   <div id="nav_bar"> 
			      <ul class="nav_menu box_container_left fo15" >
					<li class="${selectMenu==''?'active':'' } brand"><a href="/">首页</a></li>					
					<li class="${selectMenu=='shicaixinyang'?'active':'' }"><a href="/content/shicaixinyang/index.shtml">食材溯源</a>
					<ol>
					<div class="wid-80">
					<li class="${selectMenu2=='shicaixinyang'?'active':'' }"><a href="/content/shicaixinyang/shicaixinyang/index.shtml?rows=1000">食材信仰</a></li>
					<li class="${selectMenu2=='yingyangshanshi'?'active':'' }"><a href="/content/collection/yingyangshanshi/index.shtml?rows=1000">营养膳食</a></li>
					<li class="${selectMenu2=='shicaijianbie'?'active':'' }"><a href="/content/collection/shicaijianbie/index.shtml?rows=1000">食材百科</a></li>
					</div>
					</ol>
					</li>
					<li class="${selectMenu=='product'?'active':'' }"><a href="/content/product/index.shtml">以先商城</a>
					<ol>
					<div class="wid-80">
					<li class="${selectMenu2=='tuan'?'active':'' }"><a href="/content/product/tuan/index.shtml">火热预售</a></li> 
					 <li class="${selectMenu2=='guoshu'?'active':'' }"><a href="/content/product/guoshu/index.shtml">生鲜果蔬</a></li> 
					<li class="${selectMenu2=='ganhuo'?'active':'' }"><a href="/content/product/ganhuo/index.shtml">南北干货</a></li>
					<li class="${selectMenu2=='shiping'?'active':'' }"><a href="/content/product/shiping/index.shtml">休闲食品</a></li>
					<!-- <li class="${selectMenu2=='sushi'?'active':'' }"><a href="/content/product/sushi/index.shtml">溯食同源</a></li> -->
					</div>
					</ol>
					</li>
					<li class="${selectMenu=='collection'?'active':'' } brand"><a href="/content/collection/index.shtml">一地一品</a></li>					
					<li class="${selectMenu=='videolist'?'active':'' }"><a href="/content/videolist/index.shtml">精彩视频</a>
					<ol>
					<div class="wid-80">
					<li class="${selectMenu2=='sushi'?'active':'' }"><a href="/content/videolist/shipinghuizong/index.shtml">精彩推荐</a></li>
					<li class="${selectMenu2=='shipinghuizong'?'active':'' }"><a href="/content/videolist/shipinghuizong/index.shtml">视频汇总</a></li>
					</div>
					</ol>
					</li>
				</ul>
			</div>
		</div>	
		</div>
		</div>
		<div class="scroll_h">
			<div class="wid-80">
				 <div id="nav_bar"> 
			      <ul class="nav_menu box_container_left fo15" >
					<li class="${selectMenu==''?'active':'' } brand"><a href="/">首页</a></li>					
					<li class="${selectMenu=='shicaixinyang'?'active':'' }"><a href="/content/shicaixinyang/index.shtml">食材溯源</a>
					<ol>
					<div class="wid-80">
					<li class="${selectMenu2=='shicaixinyang'?'active':'' }"><a href="/content/shicaixinyang/shicaixinyang/index.shtml?rows=1000">食材信仰</a></li>
					<li class="${selectMenu2=='yingyangshanshi'?'active':'' }"><a href="/content/collection/yingyangshanshi/index.shtml?rows=1000">营养膳食</a></li>
					<li class="${selectMenu2=='shicaijianbie'?'active':'' }"><a href="/content/collection/shicaijianbie/index.shtml?rows=1000">食材百科</a></li>
					</div>
					</ol>
					</li>
					<li class="${selectMenu=='product'?'active':'' }"><a href="/content/product/index.shtml">以先商城</a>
					<ol>
					<div class="wid-80">
					<li class="${selectMenu2=='guoshu'?'active':'' }"><a href="/content/product/guoshu/index.shtml">生鲜果蔬</a></li>
					<li class="${selectMenu2=='ganhuo'?'active':'' }"><a href="/content/product/ganhuo/index.shtml">南北干货</a></li>
					<li class="${selectMenu2=='shiping'?'active':'' }"><a href="/content/product/shiping/index.shtml">休闲食品</a></li>
					<li class="${selectMenu2=='sushi'?'active':'' }"><a href="/content/product/sushi/index.shtml">溯食同源</a></li>
					</div>
					</ol>
					</li>
					<li class="${selectMenu=='collection'?'active':'' } brand"><a href="/content/collection/index.shtml">一地一品</a></li>					
					<li class="${selectMenu=='videolist'?'active':'' }"><a href="/content/videolist/index.shtml">精彩视频</a>
					<ol>
					<div class="wid-80">
					<li class="${selectMenu2=='sushi'?'active':'' }"><a href="/content/videolist/shipinghuizong/index.shtml">精彩推荐</a></li>
					<li class="${selectMenu2=='shipinghuizong'?'active':'' }"><a href="/content/videolist/shipinghuizong/index.shtml">视频汇总</a></li>
					</div>
					</ol>
					</li>
				</ul>
			</div>
			</div>
		</div>
		<div class="header_bottom nav_menu box_container_left fo15 box_container" id="nav_bar_cont"></div>
	</header>
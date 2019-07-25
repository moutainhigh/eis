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
<link rel="stylesheet" type="text/css" href="../css/main.css">
<link rel="stylesheet" type="text/css" href="../css/orderlist.css">
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script>
$(document).ready(function(){
	$("#btnReply").click(function(){
        $(".review1").toggle();
	});
})
</script>
</head>
<body>
   <header id="header2">
	    <div class="wid-80">
		   <img src="../image/logo.png" class="logo  martop30"/>
		
		    <div class="box_container_header2">
			   <ul class="martop15 btns box_container_right fo-12">
			      <li class="orange">您好，欢迎来到以先365</li>
			      <a href="#" class="orange"><li>登陆</li></a>
				  <li class="orange">|</li>
				  <a href="#" class="orange"><li>注册</li></a>
				  <a href="#" class="orange"><li>我的购物车</li></a>
				  <a href="#" class="orange"><li>我的订单</li></a>
				  <a href="#" class="orange"><li>联系我们</li></a>
				  <a href="#" class="orange"><li>帮助中心</li></a>
			   </ul>
			    <div class="martop45 box_container">
					<div id="search-form2">
						<div id="search-hd" class="search-hd">
							<div class="search-bg"></div>
							<span class="pholder"></span>
							<input type="text" id="s1" class="search-input">
							<button id="submit" class="btn-search" value="搜索">搜索</button>
						</div>
					</div>
					<div class="mycart">
					   <span class="mycart_icon"></span>
					   我的购物车
					</div>
			   </div>
				
			</div>
			<ul class="btngroup martop10 box_container_right"> 
					<a href="#"><li>首页</li></a>
					<a href="#"><li>食材溯源</li></a>
					<a href="#"><li>食材库</li></a>
					<a href="#"><li>一地一品</li></a>
					<a href="#"><li>以先优选</li></a>
					<a href="#"><li>互动专区</li></a>
					<a href="#"><li>关于我们</li></a>
				</ul>
		</div>	
		
	</header>
   <div class="wid-80 martop50">
     <ul class="leftMenu">
	    <a href="#"><span class="orange">我的以先</span></a>
		<a href="#"><span>交易管理</span></a>
		<a href="#"><li>我的订单</li></a>
		<a href="#"><li>收货地址</li></a>
		<a href="#"><li>我的评论</li></a>
		<a href="#"><li>我的收藏</li></a>
		<a href="#"><span class="orange">账户中心</span></a>
		<a href="#"><li>个人信息</li></a>
	 </ul>
	 <div class="rightbox">
	   <div><a href="#">账户中心</a>><a href="#">我的订单</a></div>
	   <div class="BoxOrder">
	      <ul class="btnsOrder1">
		     <li><a href="#">全部订单</a></li>
			 <li><a href="#">待收货</a></li>
			 <li><a href="#">待付款</a></li>
			 <li><a href="#">待评价</a></li>
		  </ul>
		  <ul class="btnsOrder2">
		     <li class="li1">商品</li>
			 <li class="li2">单价(元)</li>
			 <li class="li3">数量</li>
			 <li class="li4">实付款(元)</li>
			 <li class="li5">交易状态</li>
			 <li class="li6">评价</li>
		  </ul>
		  
		  <ul class="orderlist fo-12">
		     <li class="li1">
			    <p class="orderNum">订单编号：99999997849248284723482347</p>
				
				   <img src="../image/header.png"/>
				   <div>
				      <p>大马哈鱼</p>
					  <p>大马哈鱼大马哈鱼大马哈鱼大马哈鱼大马哈鱼</p>
				   </div>
				
			 </li>
			 <li class="li2  lineH120 orange">￥180.00</li>
			 <li class="li3 lineH120">2</li>
			 <li class="li4 orange">
			     <p class="martop40">￥180.00</p>
				 <p>(运费￥10.00)</p>
			 </li>
			 <li class="li5">
			   <p>待收货</p>
			   <p class="orange">确认收货</p>
			   <p class="orange">查看物流</p>
			   <p class="orange">订单详情</p>
			 </li>
			 <li class="li6">
			   <p><a href="#" class="orange">删除</a></p>
			   <p class="martop30"><a href="#">评价</a></p>
			 </li>
		  </ul>
		  <ul class="orderlist fo-12">
		     <li class="li1">
			    <p class="orderNum">订单编号：99999997849248284723482347</p>
				
				   <img src="../image/header.png"/>
				   <div>
				      <p>大马哈鱼</p>
					  <p>大马哈鱼大马哈鱼大马哈鱼大马哈鱼大马哈鱼</p>
				   </div>
				
			 </li>
			 <li class="li2  lineH120 orange">￥180.00</li>
			 <li class="li3 lineH120">2</li>
			 <li class="li4 orange">
			     <p class="martop40">￥180.00</p>
				 <p>(运费￥10.00)</p>
			 </li>
			 <li class="li5">
			   <p class="martop25">待付款</p>
			   <p class="orange">去付款</p>
			   <p class="orange">取消订单</p>
			 </li>
			 <li class="li6">
			   <p><a href="#" class="orange">删除</a></p>
			   <p class="martop30"><a href="#">评价</a></p>
			 </li>
		  </ul>
		  <ul class="orderlist fo-12">
		     <li class="li1">
			    <p class="orderNum">订单编号：99999997849248284723482347</p>
				
				   <img src="../image/header.png"/>
				   <div>
				      <p>大马哈鱼</p>
					  <p>大马哈鱼大马哈鱼大马哈鱼大马哈鱼大马哈鱼</p>
				   </div>
				
			 </li>
			 <li class="li2  lineH120 orange">￥180.00</li>
			 <li class="li3 lineH120">2</li>
			 <li class="li4 orange">
			     <p class="martop40">￥180.00</p>
				 <p>(运费￥10.00)</p>
			 </li>
			 <li class="li5">
			   <p class="martop25">交易成功</p>
			   <p class="orange">查看物流</p>
			   <p class="orange">订单详情</p>
			 </li>
			 <li class="li6">
			   <p><a href="#" class="orange">删除</a></p>
			   <p class="martop30"><a href="#" class="orange">评价</a></p>
			 </li>
		  </ul>
		  <ul class="listbox"> 
			 <a href="#"><li><</li></a> 
			 <a href="#"><li>1</li></a> 
			 <a href="#"><li>2</li></a> 
			 <a href="#"><li>3</li></a> 
			 <a href="#"><li>4</li></a> 
			 <a href="#"><li>5</li></a> 
			 <a href="#" class="especially"><li>...</li></a> 
			
			 <a href="#"><li>9</li></a> 
			 <a href="#"><li>></li></a>   
		</ul>
	   </div>
	 </div>
  </div>
</body>
</html>
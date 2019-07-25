<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<ul class="leftMenu">
	    <span>我的以先</span>
		<a href="/content/user/pcenter.shtml?favPage=1&favRows=8"><li class="${selectMenu=='pcenter'?'active':'' }">个人信息</li></a>
		<span>账户中心</span>
		<a href="/order/index.shtml?page=1&rows=10"><li class="${selectMenu=='order'?'active':'' }">我的订单</li></a>
		<a href="/addressBook/index.shtml"><li class="${selectMenu=='addressBook'?'active':'' }">收货地址</li></a>
		<a href="/comment/index.shtml?page=1&rows=10"><li class="${selectMenu=='comment'?'active':'' }">我的评论</li></a>
		<a href="/content/user/morefavorites.shtml?favPage=1&favRows=8"><li class="${selectMenu=='morefavorites'?'active':'' }">我的收藏</li></a>
	
		
	 </ul>
   
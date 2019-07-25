<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
</head>
<style>
	body,div,li,span.p{
		margin: 0;
		padding:0;
		font-family: "微软雅黑";
		font-size:14px;
	}
	.searchContainer-top{
		width:100%;
		position: fixed;
		top: 0;
		z-index: 99;
	}
	.searchContainer-top-header{
		box-sizing: border-box;
		padding: 0 15px;
		height: 50px;
		background: #313131;
		display: flex;
		display: -webkit-flex;
		align-items: center;
		justify-content: space-between;
	}
	.searchContainer-top-header span{
		color: #fff;
	}
	.searchContainer-top-header img{
	    width: 20px;
    	height: auto;
	}
	.searchContainer-body ul{
		margin-top: 50px;
		display: flex;
		display: -webkit-flex;
		flex-direction: column;
		width:100%;
		overflow: hidden;
	}
</style>
<body>
<div class="searchContainer">
	<div class="searchContainer-top">
		<div class="searchContainer-top-header">
			<img src="../../../../theme/basic/image/mobile/back.png" alt="" onclick="closeSearch()">
			<span>搜索</span>
			<a href="/"><img src="../../../../theme/basic/image/mobile/shouye.png" alt=""></a>
		</div>
	</div>
	<div class="searchContainer-body">
		<ul>
			<c:forEach items="${newsList}" var="a" varStatus="status"  begin="0" end="9" >
				<li>
					<a href="${a.viewUrl}" class="content">
						<span class="title">${a.title}</span>
						<!--<span class="text" >${a.content}</span>-->
						<span class="time">日期：<fmt:formatDate value="${a.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
					</a>
				</li>
			</c:forEach>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
			<li>搜索</li>
		</ul>
	</div>
</div>
<script>
		function closeSearch(){
			window.history.back(-1); 
		}
</script>
</body>
</html>
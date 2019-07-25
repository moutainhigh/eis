<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!--  common header nav, top include of page -->


<link href="/style/jquery.alerts.css" rel="stylesheet" type="text/css"
	media="screen" />
<script src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/jquery.ui.draggable.js" type="text/javascript"></script>
<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>
<link rel="stylesheet" href="/style/boxy.css" type="text/css" />
<!--  common header nav, top include of page -->

<DIV class="wrapper"
	style="border-bottom: 2px solid #004ca8; height: 29px;">
	<DIV class=topleft2>
		<DIV class=mainNav_bg>
			<UL class=mainNav>
				<LI><A id=header_HyperLink
					<c:choose> <c:when test='${""==node.alias}'>class=current</c:when></c:choose>
					href="/" target=_top>首页</A></LI>
				<LI><A id=header_HyperLink2 href="/user/login.shtml"
					target=_top>登录</A></LI>
				<LI><A id=header_HyperLink3
					href="/${contentPrefix}user/register${pageSuffix}.shtml"
					target=_top>实名注册</A></LI>
				<LI><A id=header_HyperLink4
					<c:choose> <c:when test='${"product"==node.alias}'>class=current</c:when></c:choose>
					href="/${contentPrefix}product/index${pageSuffix}" target=_top>产品大全</A></LI>
				<LI><A id="header_HyperLink5"
					<c:choose> <c:when test='${"pay"==node.alias}'>class=current</c:when></c:choose>
					href="/${contentPrefix}pay/index${pageSuffix}" target=_top>充值中心</A></LI>
				<LI><A id=header_HyperLink6
					href="/content/help/jiazhang/index.shtml" target=_top>家长监控</A></LI>



			</UL>
		</DIV>
	</DIV>

</DIV>
<DIV class="wrapper">
	<DIV class="left1">
		<a href="/" target="_top"><IMG align="left" src="/style/logo.gif"
			width="190" height="54"></a>
	</DIV>
	<div>
		<div class="wrapper_searchleft2">
			<div class="wrapper_searchDL"></div>
			<div class="wrapper_searchDM">
				<input name="header$txtSearchKey" id="header_txtSearchKey"
					onkeydown="">
			</div>
			<div class="wrapper_searchDR">
				<input type="image" name="header$btnSearch"
					src="/style/index_29.gif" id="header_btnSearch"
					style="BORDER-RIGHT-WIDTH: 0px; WIDTH: 87px; BORDER-TOP-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; HEIGHT: 33px; BORDER-LEFT-WIDTH: 0px">
			</div>
		</div>
	</div>
</DIV>

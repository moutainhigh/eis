<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<TITLE>${systemName}</TITLE>
<meta http-equiv="x-ua-compatible" content="ie=7">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<META name=description
			content=炒卡网主营游戏充值,话费充值,影音娱乐,杀毒软件,软件序列号，在线教育，影票兑换码等产品。，高效快捷，实时充值,安全放心。7*24小时为您提供全面的支付服务。>
			<META name=keywords
				content=炒卡网,jcard,炒卡网充值,炒卡网官网,充值,官网,qq卡,q币充值中心,qq充值中心,qq蓝钻,蓝钻豪华版,q币查询,点卡充值,游戏点卡,魔兽世界点卡,魔兽世界,大话西游3,剑网三,九阴真经,神仙传,神仙道,人人豆,>
				<LINK rel="Shortcut Icon" href="/favicon.ico">
					<LINK rel=Bookmark href="/favicon.ico">
						<LINK rel=Stylesheet type=text/css href="/style/UCardTop.css">
							<LINK rel=Stylesheet type=text/css href="/style/CarouselAD.css">
								<LINK rel=stylesheet type=text/css href="/style/OldUcard.css">
									<LINK rel=stylesheet type=text/css href="/style/Web.css">
										<LINK rel=stylesheet type=text/css href="/style/list.css">
											<LINK rel=stylesheet type=text/css href="/style/Footer.css">
												<link href="/style/jquery.alerts.css" rel="stylesheet"
													type="text/css" media="screen" />
												<title>${document.title }| ${systemName }</title> <script
													src="/js/lib/constants.js"></script>
												<script src="/js/ui/jquery-1.7.2.min.js"></script>
												<script src="/js/ui/jquery.boxy.js"></script>
												<script src="/js/ui/jquery.ui.draggable.js"
													type="text/javascript"></script>
												<script src="/js/ui/jquery.alerts.js" type="text/javascript"></script>
												<script src="/js/ui/box.js"></script>
												<script type="text/javascript" src="/js/ui/jquery.form.js"></script>


												<script src="/style/loading/jquery.bgiframe.min.js"
													type="text/javascript"></script>
												<script src="/style/loading/loading-min.js"></script>
												<link href="/style/loading/loading.css" type="text/css"
													rel="stylesheet">
													<script src="/js/ui/commonui.js"></script>
</head>

<body>
	<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
	<div id="mainbody">
		<div class="leftbar">
			<%@include file="/WEB-INF/jsp/include/common_login.jsp"%>

		</div>
		<div class="rightbar">
			<div class="new_gamelist">

				<div class="position">
					<span>当前位置: <c:forEach var="node" items="${pathNodeList}">
							<a href="/${node.alias}">${node.name}</a>
							<em>&nbsp;&gt;&nbsp;</em>
						</c:forEach> <a href="/${contentPrefix}${node.alias}${pageSuffix}">${node.name}</a></span>
				</div>

				<div class="open_game_body">
					<div
						style="text-align: center; font-size: 18px; line-height: 50px; border-bottom: 1px solid #eeeeee;">${document.title}</div>
					<div style="padding: 20px; line-height: 26px;"><img src="${document.documentDataMap.productSmallImage.dataValue}"/></div>
					<div style="padding: 20px; line-height: 26px;">${document.documentDataMap.productProductCode.dataValue}</div>
					<div style="padding: 20px; line-height: 26px;">${document.documentDataMap.labelMoney.dataValue}</div>
					<div style="padding: 20px; line-height: 26px;">${document.documentDataMap.buyMoney.dataValue}</div>
					<div style="height: 8px; width: 100%; float: left"></div>
				</div>

				<div class="game_bot"></div>

			</div>
		</div>
	</div>
	<div class="clear"></div>
	<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>

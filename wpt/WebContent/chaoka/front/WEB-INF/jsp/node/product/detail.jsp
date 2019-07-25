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
												<script src="/js/ui/commonui.js"></script>
												<script src="/js/buy/buydetail.js"></script>


												<script src="/style/loading/jquery.bgiframe.min.js"
													type="text/javascript"></script>
												<script src="/style/loading/loading-min.js"></script>
												<link href="/style/loading/loading.css" type="text/css"	rel="stylesheet">
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
				<input type="hidden" name="productId" value="${document.documentDataMap.productId.dataValue}"/>
				<input type="hidden" name="buyMoney" value="${document.documentDataMap.productBuyMoney.dataValue}"/>
				<input type="hidden" name="saleMoney" value="${document.documentDataMap.productSaleMoney.dataValue}"/>
				<div class="open_game_body">
					<div
						style="text-align: center; font-size: 18px; line-height: 50px; border-bottom: 1px solid #eeeeee;">${document.title}</div>
					<div style="padding: 20px; line-height: 26px;"><img alt="${document.title}" src="/${document.documentDataMap.productSmallImage.dataValue == null ? 'style/images/default_product.jpg' : document.documentDataMap.productSmallImage.dataValue}" /> 
</div>
					<div style="padding: 20px; line-height: 26px;"><span>产品名称</span><span id="productName">${document.title}</span></div>
					<div style="padding: 20px; line-height: 26px;"><span>产品代码</span>${document.documentDataMap.productProductCode.dataValue}</div>
					<div style="padding: 20px; line-height: 26px;"><span>面额</span>${document.documentDataMap.labelMoney.dataValue}</div>
					<div style="padding: 20px; line-height: 26px;"><span>最新买入价格:</span>${document.documentDataMap.productBuyMoney.dataValue}</div>
					<div style="padding: 20px; line-height: 26px;"><span>最新卖出价格:</span>${document.documentDataMap.productSaleMoney.dataValue}</div>
					<div style="padding: 8px; width: 100%; float: left">${document.content}</div>
					<div style="height: 8px; width: 100%; float: left"></div>
					
					<div style="text-align:center">					
	   					<div style="float:left"><a onclick="javascript:productBuy('${product.productCode}');" class="btn btn-primary" role="button" href="javascript:void(0);">购买</a></div>
	   					<div style=""><a onclick="javascript:showSaleInput();" class="btn btn-primary" role="button" href="javascript:void(0);">寄售</a></div>
	   					<div class="saleInput" style="display:none" id="saleInputLayer" name="saleInputLayer">
	   						<textarea name="saleCardInput" placeholder="请录入卡号密码，每行一个，格式是:卡号,密码" id="saleCardInput" rows="5", cols="100"></textarea>
	   					</div>
	   					<div class="saleInpput" style="margin:0 250px"><a onclick="javascript:productSale('${product.productCode}');" class="btn btn-primary" role="button" href="javascript:void(0);">确认寄售</a></div>
	   				</div>
				</div>

				<div class="game_bot"></div>

			</div>
		</div>
	</div>
	<div class="clear"></div>
	<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>

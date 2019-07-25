<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<TITLE>${systemName}</TITLE>
<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<META name=description content=炒卡网主营游戏充值,话费充值,影音娱乐,杀毒软件,软件序列号，在线教育，影票兑换码等产品。，高效快捷，实时充值,安全放心。7*24小时为您提供全面的支付服务。>
				<META name=keywords content=炒卡网,jcard,炒卡网充值,炒卡网官网>
					<LINK rel="Shortcut Icon" href="/favicon.ico">
						<LINK rel=Bookmark href="/favicon.ico">

							<LINK rel=Stylesheet type=text/css href="/style/CarouselAD.css">
								<LINK rel=stylesheet type=text/css href="/style/Web.css">
									<LINK rel=stylesheet type=text/css href="/style/list.css">
</HEAD>
<script src="/js/lib/constants.js"></script>
<script src="/js/ui/jquery-1.7.2.min.js"></script>
<script src="/js/ui/jquery.boxy.js"></script>
<script src="/js/ui/box.js"></script>
<script src="/style/JQuery.FloatDiv.js"></script>
<script src="/style/jquery.zxxbox.js"></script>
<script src="/style/TreeData.js"></script>
<script src="/style/ProductData.js"></script>
<script src="/style/QuickCharge.js"></script>
<script src="/style/searchfield.js"></script>

<script src="/js/ui/commonui.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		//弹出广告关闭按钮
		rowRender('.card', 'odd', 'even', 'ovbeg');
		rowRender('.charge', 'odd', 'even', 'ovbeg');

		var $div_li = $("div .menu_news ul li");
		$div_li.mouseover(function() {
			$(this).addClass("current").siblings().removeClass("current");
			var index = $div_li.index(this);
			$("div.menu_newslist > div").eq(index).show().siblings().hide();
			<!--SetQuickBuyDropDownList();
			-->
		})

		//按字母搜索游戏
		var $allGameNav = $("ul.all_cp_containerD_AZ li:first").siblings();

		$allGameNav.hover(function() {
			$(this).addClass("Cur");
			SetIndexLettersList(this);
		}, function() {
			$(this).removeClass("Cur");
			$(this).find("ul").hide();
		})

	});

	function rowRender(rowclass, even, odd, className) {
		even = even || "";
		odd = odd || "";
		$(rowclass + ":odd").addClass(odd); //奇数行样式。
		$(rowclass + ":even").addClass(even); //偶数行样式。
		$(rowclass).hover(function() {
			$(this).addClass(className); //鼠标移上去时背景样式
		}, function() {
			$(this).removeClass(className); //鼠标移开背景样式
		});
	}
</script>
<link rel="stylesheet" href="/style/bootstrap/css/bootstrap.min.css">
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
        <script src="/style/bootstrap/js/html5shiv.min.js"></script>
        <script src="/style/bootstrap/js/respond.min.js"></script>
    <![endif]-->
	<script src="/style/bootstrap/js/bootstrap.min.js"></script>


	<BODY>
		<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
		<div class="wrapper"></div>
		<div style="Z-INDEX: 100; float: left" class="wrapper">
			<%@include file="/WEB-INF/jsp/include/common_login.jsp"%>
			<DIV class=left2>
				<DIV id=CarouselAD style="margin-bottom: 5px;">
					<div style="width: 510px; float: left; margin-top: 5px;">
						<div class="focus">
							<script type="text/javascript">
								var focus_width = 510;
								var focus_height = 150;
								var text_height = 0;
								var swf_height = focus_height + text_height;
								var pics = '', links = '', texts = '';
								pics += '|/style/img01.jpg';
								links += '|http://www.chaoka.cn';
								texts += '|炒卡网';
								pics += '|/style/img02.jpg';
								links += '|http://www.chaoka.cn';
								texts += '|炒卡网';
								pics += '|/style/img03.jpg';
								links += '|http://www.chaoka.cn';
								texts += '|炒卡网';

								pics = pics.substring(1);
								links = links.substring(1);
								texts = texts.substring(1);

								document
										.write('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+ focus_width +'" height="'+ swf_height +'">');
								document
										.write('<param name="allowScriptAccess" value="sameDomain"><param name="movie" value="/style/focus.swf"><param name="quality" value="high"><param name="bgcolor" value="#FFFFFF">');
								document
										.write('<param name="menu" value="false"><param name=wmode value="opaque">');
								document
										.write('<param name="FlashVars" value="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'">');
								document
										.write('<embed src="/style/focus.swf" wmode="opaque" FlashVars="pics='
												+ pics
												+ '&links='
												+ links
												+ '&texts='
												+ texts
												+ '&borderwidth='
												+ focus_width
												+ '&borderheight='
												+ focus_height
												+ '&textheight='
												+ text_height
												+ '" menu="false" ?bgcolor="#ffffff" quality="high" width="'
												+ focus_width
												+ '" height="'
												+ swf_height
												+ '" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />');
								document.write('</object>');
							</script>
						</div>

					</div>

				</DIV>
			</div>
			<DIV class="contentD_left3">
				<DIV class="menu_news">
					<UL>
						<LI class="current">最新公告</li>
						<LI>产品公告</li>
						<LI>客服公告</LI>
					</UL>
				</DIV>
				<DIV id="tabcontent1">
					<DIV class="menu_newslist">
						<div style="display: block;">
							<div id="NewsControl">
								<ul>
									<li><a class="NewsRedLink" target="_blank" href="#">炒卡网上线啦...</a></li>


								</ul>
							</div>
							<div class="kf_more">
								<a href="/News/NewsList.aspx?CategoryCode=39">更多&gt;&gt;&nbsp;</a>
							</div>
						</div>
						<div class="hide" style="display: none;">
							<div id="NewsControl1">
								<ul>
									<li><a class="NewsRedLink" target="_blank" href="#">新版客户端 飞一般的流畅支...</a></li>
									<li><a class="NewsRedLink" target="_blank" href="#">专挑你喜欢的 《幻仙》挑战兴奋...</a></li>
									<li><a class="NewsRedLink" target="_blank" href="#">期待时空裂痕3月火爆公测</a></li>

								</ul>
							</div>
							<div class="kf_more">
								<a href="/News/NewsList.aspx?CategoryCode=40">更多&gt;&gt;&nbsp;</a>
							</div>
						</div>
						<div class="hide" style="display: none;">
							<div id="NewsControl2">
								<ul>
									<li><a target="_blank" href="#">关于严禁利用炒卡进行非法操作的...</a></li>


								</ul>
							</div>
							<div class="kf_more">
								<a href="/News/NewsList.aspx?CategoryCode=41">更多&gt;&gt;&nbsp;</a>
							</div>
						</div>
					</DIV>
				</DIV>
			</DIV>
		</div>
		<div style="Z-INDEX: 100" class="wrapper">
			<DIV class=contentD_left2>
				<DIV id=gocz>
					<DIV id=goczT>&nbsp;</DIV>
					<DIV id=goczD>
						<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr align="center">
							<c:forEach var="product" items="${cardProductList}" begin="0" end="49" varStatus="status">
								<!--  ${status.index} --> 
								
										<td class="product_cell">
											<a href="${product.viewUrl}" target="_blank">
											<div class="product_image">
												<img alt="${product.title}" src="/${product.documentDataMap.productSmallImage.dataValue == null ? 'style/images/default_product.jpg' : product.documentDataMap.productSmallImage.dataValue}" /> 
											</div>
											<div class="product_name">${product.title }</div>
											<div class="product_label_money">面额:${product.documentDataMap.labelMoney.dataValue}</div>
											<div class="product_buy_money">最新售价:${product.documentDataMap.productBuyMoney.dataValue}</div>
											<div class="product_sale_money">最新买入价:${product.documentDataMap.productSaleMoney.dataValue}</div>
											</a>
										</td>
								<c:if test="${status.index > 0 && status.index % 4 == 0}">
									</tr><tr>
									</c:if>
							</c:forEach>
</tr>
						</table>

					</DIV>
				</DIV>
			</DIV>

		</DIV>


		<LINK rel=Stylesheet type=text/css href="/style/Footer.css">
			</DIV>

			<DIV class=wrapper>
				<DIV class=footerT>
					<DIV class=footer_txtL>
						<STRONG>关于互联网行为规范的法律要求</STRONG> <BR> 根据《中华人民共和国宪法》和相关法律法规规定，在保护公民合法言论自由的同时，禁止利用互联网、通讯工具、媒体以及其他方式从事以下行为： <BR> 一、组织、煽动抗拒、破坏宪法和法律、法规实施的。 <BR> 二、捏造或者歪曲事实，散布谣言，妨害社会管理秩序的。 <BR>
										三、组织、煽动非法集会、游行、示威、扰乱公共场所秩序的。 <BR> 四、从事其他侵犯国家、社会、集体利益和公民合法权益的。 <BR> 管理部门将依法严加监管上述行为并予以处理；对构成犯罪的，司法机关将追究刑事责任。 
					</DIV>
					<DIV class=footer_txtR>
						<STRONG>未成年人上网安全指引</STRONG> <BR> 1. 本网站不向未成年人提供服务。 <BR> 2. 未成年人获得一卡通充值服务卡的，应在监护人指导和监督下使用。 <BR> 3. 未成年人网友要注意区分网络与现实的区别，避免沉迷于网络。 <BR> 4. 如有违法行为，我公司保留配合公安和电信部门追查的权利。
										我公司郑重提醒用户正确认识网络世界，重视参加网络活动的安全性，加强防范，确保安全。 <BR>
					</DIV>
				</DIV>
				<DIV class="cf"></DIV>

				<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
			</DIV>
	</BODY>
</HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<TITLE>${systemName}-绑定收款信息</TITLE>
<meta http-equiv="x-ua-compatible" content="ie=7" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<META name=description content=炒卡网主营游戏充值,话费充值,影音娱乐,杀毒软件,软件序列号，在线教育，影票兑换码等产品。，高效快捷，实时充值,安全放心。7*24小时为您提供全面的支付服务。 />
<META name=keywords content=炒卡网,jcard,炒卡网充值,炒卡网官网,充值,官网,qq卡,q币充值中心,qq充值中心,qq蓝钻,蓝钻豪华版,q币查询,点卡充值,游戏点卡,魔兽世界点卡,魔兽世界,大话西游3,剑网三,九阴真经,神仙传,神仙道,人人豆, />
<LINK rel="Shortcut Icon" href="/favicon.ico" />
<LINK rel=Bookmark href="/favicon.ico" />
<LINK rel=Stylesheet type=text/css href="/style/UCardTop.css" />
<LINK rel=Stylesheet type=text/css href="/style/CarouselAD.css" />
<LINK rel=stylesheet type=text/css href="/style/OldUcard.css" />
<LINK rel=stylesheet type=text/css href="/style/Web.css" />
<LINK rel=stylesheet type=text/css href="/style/list.css" />
<LINK rel=stylesheet type=text/css href="/style/Footer.css" />
<script src="/js/lib/constants.js"></script>
<script src="/js/ui/jquery-1.7.2.min.js"></script>
<script src="/js/ui/bind_money_account.js"></script>
<script src="/js/ui/jquery.boxy.js"></script>

<script type="text/javascript" src="/js/ui/jquery.form.js"></script>
<script src="/js/ui/commonui.js"></script>
</head>

<body>
	<%@include file="/WEB-INF/jsp/include/common_header_nav.jsp"%>
	<div id="mainbody">
		<div class="leftbar">
			<%@include file="/WEB-INF/jsp/include/common_login.jsp"%>
			<%@include file="/WEB-INF/jsp/include/common_info.jsp"%>
		</div>
		<div class="rightbar">
			<div class="position">
				<span>当前位置: <a href="/" target="_blank">${systemName}首页</a> - 我的账户
				</span>
			</div>
			<div id="usernamezh" style="display: block; margin: 50px 5px 5px 68px; font-size: 14px; color: #ff6d00;"></div>
			<div id="dengji" style="margin: 15px 5px 5px 68px;"></div>
			<c:choose>
				<c:when test="${bankAccountList == null || bankAccountList.size() < 1 }">
				
				<div class="mobile_form">
				<form id="bankAccountForm" name="bankAccountForm" enctype="multipart/form-data" >
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">银行卡姓名：</span> <span><input type="text" id="bankAccountName"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="bankAccountName"></span>

					</p>
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">银行卡号/微信号/支付宝账号：</span> <span><input type="text" id="bankAccountNumber"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="bankAccountNumber"></span>

					</p>
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">银行：</span> <span> <select id="bankCode"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="bankCode">
								<option value="ALIPAY">支付宝</option>
								<option value="WECHAT">微信</option>
								<option value="ICBC">中国工商银行</option>
								<option value="ABC">中国农业银行</option>
								<option value="BOC">中国银行</option>
								<option value="CCB">中国建设银行</option>
						</select>
						</span>

					</p>
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">省份：</span> <span><input type="text" id="province"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="bindMailBox"></span>

					</p>
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">地市：</span> <span><input type="text" id="city"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="bindMailBox"></span>

					</p>
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">开户行：</span> <span><input type="text" id="issueBank"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="bindMailBox"></span>

					</p>
					<p style="margin-bottom: 0px;">
						<span style="height: 30px; width: 50px; line-height: 30px; margin-top: 10px; vertical-align: middle;">支付宝/微信收款码或银行卡照片：</span> <span><input type="file" id="certifyUpload"
							style="height: 30px; width: 250px; line-height: 30px; margin-top: 10px; vertical-align: middle;" name="certifyUpload"></span>

					</p>
					<p style="font-size: 12px; margin: 0 0 0 60px; color: #d5d5d5" class="grey">请输入正确的信息</p>


					<p>
						<input type="button" name="bind" id="bind" class="submit_subscribe" value="确定">
					</p>
				</form>
			</div>				
			</c:when>
			<c:otherwise>
			<div class="mobile_form2">您已绑定收款信息</div>
				<div>银行名  账户名 银行账号</div>
			<c:forEach var="ba" items="${bankAccountList}">
				<div>${ba.bankName} ${ba.bankAccountName} ${ba.bankAccountNumber}</div>
			</c:forEach>
			</c:otherwise>
			</c:choose>
			
			<div class="clear"></div>
		</div>
		<div class="clear"></div>
	</div>
	<%@include file="/WEB-INF/jsp/include/common_footer.jsp"%>
</body>
</html>

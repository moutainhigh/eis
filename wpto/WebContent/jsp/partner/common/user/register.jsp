<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
     <title>注册-${systemName}</title>
	<link href="/theme/${theme}/style/caiwangbao/common.css" rel="stylesheet">	   
	<link href="/theme/${theme}/style/caiwangbao/reg.css" rel="stylesheet">	   
	<!--[if lt IE 9]>
	<script src="js/respond.min.js"></script>
	<![endif]-->   
    <script src="/theme/${theme}/js/jquery-1.11.3.min.js"></script>
	<script src="/theme/${theme}/js/security.js" type="text/javascript"></script>
	<script src="/theme/${theme}/js/jquery.form.3.5.1.js"></script>
    <script src="/theme/${theme}/js/jquery.validate.min.js"></script>
	<script src="/theme/${theme}/js/jquery.md5.js" type="text/javascript"></script>
    <script src="/theme/${theme}/js/noticeBox.js"></script>

	<script language="javascript" type="text/javascript">    
		function encoded(oldPass){
			var exponent = "${publicKeyExponent}";
			var modulus = "${publicKeyModulus}";
			if(exponent == null || modulus == null){
				alert("系统数据异常，无法初始化安全控件!");
				return;
			}
			RSAUtils.setMaxDigits(200);  
			var key = new RSAUtils.getKeyPair(exponent, "", modulus);  
			var encrypedPwd = RSAUtils.encryptedString(key, oldPass);
			return encrypedPwd;
		}	
	</script>
    <script src="/theme/${theme}/js/reg.js"></script>
  </head>
  <body>	
	<div class="top">
		<div class="nav wid-nav">
			<div class="logo">
				<a href="/"><img src="/theme/${theme}/images/logo/${appCode}.png" class="logo-img"/></a>
			</div>
			<ul class="nav-menu">
				<li>您好，欢迎来到${systemName}！</li>
				<li>注册</li>
				<li>登录</li>
				<li>购买记录</li>
				<li>我是买家</li>
				<li><span class="download-icon"><img src="/theme/${theme}/images/download_icon.png" class="download-icon-img"/></span>下载app</li>
			</ul>
		</div>
	</div>
	<div class="main">
		<div class="wid-cont">
			<ul class="tab-menu visible clearfl">
				<li class="current"><a href="javascript:void(0);"><span class="tab-num">1</span><span class="tab-item-cont">注册${systemName}</span></a></li>
				<li><a href="/guide/step1.shtml"><span class="tab-num">2</span><span class="tab-item-cont">公众号管理</span></a></li>
				<li><a href="/guide/step2.shtml"><span class="tab-num">3</span><span class="tab-item-cont">完成配置</span></a></li>
			</ul>
			<form method="POST" id="reg_form" name="reg_form"onsubmit="return false;" >
				<table class="table-cont">
					<tbody>
						<tr>
							<td class="title">用户名：</td>
							<td class="input-col">
								<input class="input" placeholder="请输入用户名（手机号）" name="username" id="username"/>
								<input type="hidden" name="i" id="i" value="${i}"/>
								<div class="extra-col notice-line"><span class="icon"></span><span class="notice"></span></div>

							</td>
						</tr>
						<tr>
							<td class="title">设置密码：</td>	
							<td class="input-col relative">
								<input class="input" name="userPassword" placeholder="请设置密码" id="userPassword" type="password">
								<img src="/theme/${theme}/images/input_eye.png" class="input-eye" onclick="changeTypeOfPassword(this)"/>
								<div class="extra-col notice-line"><span class="icon"></span><span class="notice"></span></div>

							</td>
						</tr>
						<tr>
							<td class="title">验证码：</td>	
							<td class="input-col">
								<div class="input-line pic-code-line">
									<input class="input" name="eis_captcha" placeholder="请输入验证码" id="eis_captcha">
									<span class="tx-code"><img src="/captcha.shtml" class="tx-code-img" onclick="onchangePatchca();"/></span>
									<span class="cancel-icon"><img src="/theme/${theme}/images/cancel_icon_gray.png" class="cancel-icon-img" onclick="onchangePatchca();"/></span>
									<div class="notice-line graph-hint"><span class="icon"></span><span class="notice"></span></div>

								</div>
							</td>
							<td class="extra-col"></td>						
						</tr>
						<tr>
							<td class="title">手机验证码：</td>	
							<td class="input-col relative">
								<div class="input-line sms-code-line">
									<input class="input" placeholder="请输入手机验证码" id="phoneBindSign">
									<input type="button" class="btn sms-code height44" onclick="sendMessage();" id="btnSendCode"value="获取验证码"/>
									<div class="notice-line "><span class="icon"></span><span class="notice"></span></div>
								</div>
							</td>
							<td class="extra-col"><span class="icon"></span><span class="notice"></span></td>						
						</tr>
						<tr class="btns">
							<td></td>
							<td class="input-col">
								<div class="agree-line"><span class="icon left-locate"><img src="/theme/${theme}/images/agree_icon_red.png" class="agree-icon-img"/></span><span class="agree">我已阅读并接受<em class="red">版权声明</em>和<em class="red">隐私保护</em>条款</span></div>
								<div class="btn-line hidden">
									<input class="btn submit" value="注册" type="submit"/>
									<div class="btn reset" onclick="resetForm()">重置</div>						
								</div>
							</td>
							<td></td>
						</tr>
					</tbody>				
				</table>
			</form>
		</div>
	</div>
	<div class="bottom footer">
		${commonFooter}
	</div>
  </body>
</html>

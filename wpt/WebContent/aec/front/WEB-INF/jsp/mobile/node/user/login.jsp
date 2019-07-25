<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>${systemName}</title>
<!--<link rel="stylesheet/less" href="/style/mobile/common.less" />
<script src="/js/less.min.js"></script>-->
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/list.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/footer.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/login.min.js?v=20161133"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script>

wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: '${appid}', // 必填，公众号的唯一
    timestamp: '${timestamp}', // 必填，生成签名的时间戳
    nonceStr: '9hKgyCLgGZOgQmEI', // 必填，生成签名的随机串
    signature: '${signature}',// 必填，签名，见附录1
    jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone','openLocation','getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

</script>

<script>
</script>
</head>

<body>
 <div class="header" id="header">
      <a class="back" href="javascript:history.go(-1);"></a><span>登录</span><a class="list2" href="/"></a>
    </div>
<!-- <%@include file="/WEB-INF/jsp/include/loginhead.jsp" %> -->
<div class="login">
        <h2>使用账号登录</h2>
        <form  method="POST"  name="member_loginForm" id="member_loginForm"   onsubmit="return false;">
        <div class="lomex">                 
            <input type="text" placeholder="请输入您的手机号" id="username" name="username">
            <div style="visibility:hidden;" id="err_username" class="losi fcc"></div>
            <input type="password" placeholder="密码" id="userPassword" name="userPassword" >
            <div style="visibility:hidden;" id="err_password" class="losi fcc"></div>
            <!--<div style="display:none" id="captcha">
                <input type="text" style="width:38%;" placeholder="验证码" maxlength="4" id="code">
                <img width="100" height="30" style="vertical-align:middle;" src="/captcha?type=login" id="codeimg" class="czl mls mrs">
                <div style="visibility:hidden" id="err_public" class="losi"></div>
            </div>-->
            
             <input type="hidden" id="loginurl" value="${return_Url}" name="login">
            <input type="submit" id="login" class="bluebutton writetext"  value="登 录" name="login">
                   
            <div class="mbie">                          
                <a class="btnzyu mb10" href="${host}/content/user/register.shtml">免费注册</a>
                <a class="btnzyy" href="/content/user/forget.shtml">忘记密码</a>
            </div>
            
             <!--a id="wxlogin" class="wxdl" href="${host}/usr/weixinlogin&scope=high&returnurl=${return_Url}"-->
			<a id="wxlogin" class="wxdl" href="/weixinUser/check.shtml">
            <span style="margin-left: 35px ;">可以直接用微信登录或者注册哦！</span>
             </a>    
        </div>
        </form>
      
    </div>

</body>
</html>
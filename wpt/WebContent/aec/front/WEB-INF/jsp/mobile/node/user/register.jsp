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
<link href="../../../theme/${theme}/css/mobile/register.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/footer.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/main.css" rel="stylesheet" type="text/css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.md5.js"></script>
<script src="../../../theme/${theme}/js/mobile/reg.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 
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
wx.ready(function(){wx.onMenuShareTimeline({title:"${document.title}",link:"${outShareUrl}",imgUrl:'${document.documentDataMap.get("productSmallImage").dataValue}',success:function(){$.ajax({type:"GET",url:"/weixin/event",data:{outUuid:"${outUuid}",outShareUrl:"${outShareUrl}",outOperateCode:"shareToCircles"},dataType:"json",success:function(){return!1},error:function(){return!1}})},cancel:function(){}}),wx.onMenuShareAppMessage({title:"${document.title}",desc:'${document.documentDataMap.get("productIntruduction").dataValue}',link:"${outShareUrl}",imgUrl:'${document.documentDataMap.get("productSmallImage").dataValue}',type:"",dataUrl:"",success:function(){$.ajax({type:"GET",url:"/weixin/event",data:{outUuid:"${outUuid}",outShareUrl:"${outShareUrl}",outOperateCode:"sharetoFriend"},dataType:"json",success:function(){return!1},error:function(){return!1}})},cancel:function(){}}),wx.onMenuShareQQ({title:"${document.title}",desc:'${document.documentDataMap.get("productIntruduction").dataValue}',link:"${outShareUrl}",imgUrl:'${document.documentDataMap.get("productSmallImage").dataValue}',success:function(){$.ajax({type:"GET",url:"/weixin/event",data:{outUuid:"${outUuid}",outShareUrl:"${outShareUrl}",outOperateCode:"shareToQQ"},dataType:"json",success:function(){return!1},error:function(){return!1}})},cancel:function(){}}),wx.onMenuShareWeibo({title:"${document.title}",desc:'${document.documentDataMap.get("productIntruduction").dataValue}',link:"${outShareUrl}",imgUrl:'${document.documentDataMap.get("productSmallImage").dataValue}',success:function(){$.ajax({type:"GET",url:"/weixin/event",data:{outUuid:"${outUuid}",outShareUrl:"${outShareUrl}",outOperateCode:4},dataType:"json",success:function(){return!1},error:function(){return!1}})},cancel:function(){}}),wx.onMenuShareQZone({title:"${document.title}",desc:'${document.documentDataMap.get("productIntruduction").dataValue}',link:"${outShareUrl}",imgUrl:'${document.documentDataMap.get("productSmallImage").dataValue}',success:function(){$.ajax({type:"GET",url:"/weixin/event",data:{outUuid:"${outUuid}",outShareUrl:"${outShareUrl}",outOperateCode:5},dataType:"json",success:function(){return!1},error:function(){return!1}})},cancel:function(){}})});
</script>
</head>

<body>
 <div class="header" id="header">
      <a class="back" href="javascript:history.go(-1);"></a><span>注册</span><a class="list2" href="/"></a>
    </div>

<div class="login">
  <form  method="POST"  name="member_regForm" id="member_regForm"   onsubmit="return false;">
        <div class="lomex">                 
           <p> <input type="text" placeholder="请输入您的手机号" id="username" name="username"></p>
           
            <p><input type="password" placeholder="密码不能少于6位" id="userPassword" name="userPassword" ></p>
            
             <p> <input type="text" placeholder="图形验证码" id="eis_captcha" name="eis_captcha" ><img src="/captcha"  id="captcha" onClick="onchangePatchca(this);" style="border: 1px #b1b1b3 solid;border-radius: 5px;" /></p>
              
              <p> <input type="text" placeholder="手机验证码" id="phoneBindSign" name="phoneBindSign" ><input id="btnSendCode" type="button" value="点击获取" onclick="sendMessage()" /></p>
          
            <div class="box_input"><input type="submit" id="login" class="btnzda writetext"  value="注  册" name="login"></div>
                   
            <div class="mbie">    
                <div class="checkBox">
  		            <input type="checkbox" value="1" id="checkboxgroup" name=""/>
	  	            <label for="checkboxgroup"></label>
  	             </div>                      
                <span>同意<a class="mb10" href="/content/about/20170122142130.shtml" style="text-decoration:underline">以先用户注册协议</a></span>
               
            </div>

        </div>
        </form>
      
    </div>

</body>
</html>
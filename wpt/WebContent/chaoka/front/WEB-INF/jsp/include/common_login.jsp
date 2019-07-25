<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<LINK rel=stylesheet type=text/css href="/style/blue.css">
<script  src="/js/ui/icheck.js"></script>

<script>
$(function(){ 

  $('input').iCheck({
    checkboxClass: 'icheckbox_minimal-blue',
    radioClass: 'iradio_minimal-blue',
    increaseArea: '20%' // optional
  });
/*
document.onkeydown = function(e){ 
var ev = document.all ? window.event : e; 
if(ev.keyCode==13) { 
 login();
} 
}
*/
}); 

</script>
 <link rel="stylesheet" href="/style/bootstrap/css/bootstrap.min.css">
  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="/style/bootstrap/js/html5shiv.min.js"></script>
        <script src="/style/bootstrap/js/respond.min.js"></script>
    <![endif]-->
  <script src="/style/bootstrap/js/bootstrap.min.js"></script>
<div id="loginbg" >
  <div id="no_login" style="display:none"> <div class="loginOk"  style="visibility: hidden;"></div>
    <form  method="post" name="User" id="User" >
      <labl><span class="txts">账号：</span> <span class="forms">
        <input type="text"  name="username" id="username" size="18" />
        </span></labl>
      <labl><span class="txts">密码：</span> <span class="forms">
        <input type="password" name="userPassword" id="userPassword" size="18" />
        </span></labl>
      <div id="remember_u">
       
        <span style="float:left; display:block"> <input type="checkbox" id="userRememberName" name="userRememberName" style=" float:left;width:20px;display:block" checked="checked" />记住账号</span><span style="float:left; display:block">&nbsp;&nbsp;<a href="/${contentPrefix}help/forgetPassword${pageSuffix}">忘记密码?</a></span></div>
      <div class="clear"></div>
      <div style="margin-left:60px;font-family:'微软雅黑',Verdana,sans-serif;">
        <input type="button"  id="denglu" onclick="login();" value="登录" />
      </div>
    </form>
  </div>
  <div id="loginres" style="display:none"> 
    <p style="height:30px; line-height:30px;margin-left:10px;" id="info"> </p>
     <p style="height:30px; line-height:30px;margin-left:10px;" >账户余额:<span id="chargeMoney"></span>元</p>
       <p style="height:30px; line-height:30px;margin-left:10px;font-weight:bold" ><a href="/order.shtml" target="_blank">[我的交易记录]</a></p>
    <div style="height:30px; line-height:30px;margin-left:10px;font-weight:bold"> <a href="javascript:logout();" >[安全退出]</a> <a href="/${contentPrefix}user/index${pageSuffix}">[我的账户]</a> </div>
  </div>
</div>

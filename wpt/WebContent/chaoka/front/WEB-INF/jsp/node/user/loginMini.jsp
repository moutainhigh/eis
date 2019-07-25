<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

 <form  method="post" name="Useral" id="Useral">
<script>



document.onkeydown = function(e){ 
var ev = document.all ? window.event : e; 
if(ev.keyCode==13) { 
 loginuser();
} 
} 


</script>
<table width="500" height="200" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right">&nbsp;</td>
    <td><span class="notifyOk"  style=" font-size:12px; display:none"></span></td>
  </tr>
  <tr>
    <td width="42%" height="40" align="right" style=" font-size:12px">请输入您的${systemName}账号：</td>
    <td width="58%" height="40"><input type="text"  name="username" id="username" size="40"  style="height:25px; width:220px;" /></td>
  </tr>
  <tr>
    <td height="40" align="right"  style=" font-size:12px">输入您在${systemName}的密码：</td>
    <td height="40"><input type="password" name="userPassword" id="userPassword" size="40" style="height:25px;width:220px;" /></td>
  </tr>
  <tr>
    <td colspan="2" align="center"><span class="agree"  style=" font-size:12px">
      <input type="checkbox" id="userRememberName" name="userRememberName" checked="checked" />
请记住我的帐号</span>&nbsp;&nbsp;<span ><a href="/content/user/register.shtml" style="font-size:12px;color:#0070fe;">还没注册账号？</a></span></td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="left">
      <input type="button" id="loginres"  onclick="loginuser();" value="登录" style="	background:url(/style/bottonbg.gif) no-repeat;
	text-align:center;
	width:74px;
	height:25px;font-family:'微软雅黑', Verdana, sans-serif;
	line-height:27px; font-weight:bold; color:#FFFFFF;
	border:0px" />
   </td>
  </tr>
</table></form>


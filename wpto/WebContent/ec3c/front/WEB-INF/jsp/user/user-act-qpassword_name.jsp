<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>用户中心_深圳市凯特威武科技有限公司 - Powered by ECShop</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" tppabs="http://www.ktwwg.top/js/common.js"></script><script type="text/javascript" src="/theme/${theme}/js/user.js" tppabs="http://www.ktwwg.top/js/user.js"></script><script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" tppabs="http://www.ktwwg.top/js/transport_jquery.js"></script></head>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.9.1.min.js" tppabs="http://www.ktwwg.top/js/jquery-1.9.1.min.js"></script><script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" tppabs="http://www.ktwwg.top/js/jquery.json.js"></script> <script type="text/javascript" src="/theme/${theme}/js/utils.js" tppabs="http://www.ktwwg.top/js/utils.js"></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
 

 



<div class="block block1">
  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="..htm" tppabs="http://www.ktwwg.top/.">首页</a> <code>&gt;</code> 用户中心 
</div>
</div>
<div class="blank"></div>




    


    

    <div class="usBox">
  <div class="usBox_2 clearfix">
    <form action="http://www.ktwwg.top/user.php" method="post">
        <br />
        <table width="70%" border="0" align="center">
          <tr>
            <td colspan="2" align="center"><strong>请输入您注册的用户名以取得您的密码提示问题。</strong></td>
          </tr>
          <tr>
            <td width="29%" align="right">用户名</td>
            <td width="61%"><input name="user_name" type="text" size="30" class="inputBg" /></td>
          </tr>
          <tr>
            <td></td>
            <td><input type="hidden" name="act" value="get_passwd_question" />
              <input type="submit" name="submit" value="提 交" class="bnt_blue" style="border:none;" />
              <input name="button" type="button" onclick="history.back()" value="返回上一页" style="border:none;" class="bnt_blue_1" />
	    </td>
          </tr>
        </table>
        <br />
      </form>
  </div>
</div>


    

</div>
<div class="blank"></div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    


</body>
<script type="text/javascript">
var process_request = "正在处理您的请求...";
var username_empty = "- 用户名不能为空。";
var username_shorter = "- 用户名长度不能少于 3 个字符。";
var username_invalid = "- 用户名只能是由字母数字以及下划线组成。";
var password_empty = "- 登录密码不能为空。";
var password_shorter = "- 登录密码不能少于 6 个字符。";
var confirm_password_invalid = "- 两次输入密码不一致";
var email_empty = "- Email 为空";
var email_invalid = "- Email 不是合法的地址";
var agreement = "- 您没有接受协议";
var msn_invalid = "- msn地址不是一个有效的邮件地址";
var qq_invalid = "- QQ号码不是一个有效的号码";
var home_phone_invalid = "- 家庭电话不是一个有效号码";
var office_phone_invalid = "- 办公电话不是一个有效号码";
var mobile_phone_invalid = "- 手机号码不是一个有效号码";
var msg_un_blank = "* 用户名不能为空";
var msg_un_length = "* 用户名最长不得超过7个汉字";
var msg_un_format = "* 用户名含有非法字符";
var msg_un_registered = "* 用户名已经存在,请重新输入";
var msg_can_rg = "* 可以注册";
var msg_email_blank = "* 邮件地址不能为空";
var msg_email_registered = "* 邮箱已存在,请重新输入";
var msg_email_format = "* 邮件地址不合法";
var msg_blank = "不能为空";
var no_select_question = "- 您没有完成密码提示问题的操作";
var passwd_balnk = "- 密码中不能包含空格";
var username_exist = "用户名 %s 已经存在";
</script>
</html>

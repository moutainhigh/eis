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

 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.9.1.min.js" tppabs="http://www.ktwwg.top/js/jquery-1.9.1.min.js"></script><script type="text/javascript" src="js/jquery.json.js" tppabs="http://www.ktwwg.top/js/jquery.json.js"></script> <script type="text/javascript" src="/theme/${theme}/js/utils.js" tppabs="http://www.ktwwg.top/js/utils.js"></script>

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
   <div class="usBox_1 f_l">
    <div class="login_tab">
    <ul>
        <li  ><a href="user.php.htm" tppabs="http://www.ktwwg.top/user.php">用户登录</a></li>
        <li class="active"><a href="user.php-act=register.htm" tppabs="http://www.ktwwg.top/user.php?act=register">用户注册</a></li>
    </ul>
    </div>
    <form action="http://www.ktwwg.top/user.php" method="post" name="formUser" onsubmit="return register();">
      <table width="100%"  border="0" align="left" cellpadding="5" cellspacing="3">
        <tr>
          <td width="25%" align="right">用户名</td>
          <td width="65%">
          <input name="username" type="text" size="25" id="username" onblur="is_registered(this.value);" class="inputBg"/>
            <span id="username_notice" style="color:#FF0000"> *</span>
          </td>
        </tr>
        <tr>
          <td align="right">email</td>
          <td>
          <input name="email" type="text" size="25" id="email" onblur="checkEmail(this.value);"  class="inputBg"/>
            <span id="email_notice" style="color:#FF0000"> *</span>
          </td>
        </tr>
        <tr>
          <td align="right">密码</td>
          <td>
          <input name="password" type="password" id="password1" onblur="check_password(this.value);" onkeyup="checkIntensity(this.value)" class="inputBg" style="width:179px;" />
            <span style="color:#FF0000" id="password_notice"> *</span>
          </td>
        </tr>
        <tr>
          <td align="right">密码强度</td>
          <td>
            <table width="145" border="0" cellspacing="0" cellpadding="1">
              <tr align="center">
                <td width="33%" id="pwd_lower">弱</td>
                <td width="33%" id="pwd_middle">中</td>
                <td width="33%" id="pwd_high">强</td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td align="right">确认密码</td>
          <td>
          <input name="confirm_password" type="password" id="conform_password" onblur="check_conform_password(this.value);"  class="inputBg" style="width:179px;"/>
            <span style="color:#FF0000" id="conform_password_notice"> *</span>
          </td>
        </tr>
        	        <tr>
         
          <td align="right" id="extend_field1i">MSN          <td>
          <input name="extend_field1" type="text" size="25" class="inputBg" /><span style="color:#FF0000"> *</span>          </td>
        </tr>
			        <tr>
         
          <td align="right" id="extend_field2i">QQ          <td>
          <input name="extend_field2" type="text" size="25" class="inputBg" /><span style="color:#FF0000"> *</span>          </td>
        </tr>
			        <tr>
         
          <td align="right" id="extend_field3i">办公电话          <td>
          <input name="extend_field3" type="text" size="25" class="inputBg" /><span style="color:#FF0000"> *</span>          </td>
        </tr>
			        <tr>
         
          <td align="right" id="extend_field4i">家庭电话          <td>
          <input name="extend_field4" type="text" size="25" class="inputBg" /><span style="color:#FF0000"> *</span>          </td>
        </tr>
			        <tr>
         
          <td align="right" id="extend_field5i">手机          <td>
          <input name="extend_field5" type="text" size="25" class="inputBg" /><span style="color:#FF0000"> *</span>          </td>
        </tr>
			        <tr>
          <td align="right">密码提示问题</td>
          <td>
          <select name='sel_question'>
	  <option value='0'>请选择密码提示问题</option>
	  <option value="friend_birthday">我最好朋友的生日？</option><option value="old_address">我儿时居住地的地址？</option><option value="motto">我的座右铭是？</option><option value="favorite_movie">我最喜爱的电影？</option><option value="favorite_song">我最喜爱的歌曲？</option><option value="favorite_food">我最喜爱的食物？</option><option value="interest">我最大的爱好？</option><option value="favorite_novel">我最喜欢的小说？</option><option value="favorite_equipe">我最喜欢的运动队？</option>	  </select>
          </td>
        </tr>
        <tr>
         <td align="right" id="passwd_quesetion">密码问题答案</td>
          <td>
	  <input name="passwd_answer" type="text" size="25" class="inputBg" maxlengt='20'/><span style="color:#FF0000"> *</span>          </td>
        </tr>
		              <tr>
          <td>&nbsp;</td>
          <td><label>
            <input name="agreement" type="checkbox" value="1" checked="checked" />
            我已看过并接受《<a href="article.php-cat_id=-1.htm" tppabs="http://www.ktwwg.top/article.php?cat_id=-1" style="color:blue" target="_blank">用户协议</a>》</label></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td align="left">
          <input name="act" type="hidden" value="act_register" >
          <input type="hidden" name="back_act" value="" />
          <input name="Submit" type="submit" value="" class="us_Submit_reg">
          </td>
        </tr>
        <tr>
          <td colspan="2">&nbsp;</td>
        </tr>
                 <tr>
                    <td bgcolor="#ffffff" colspan="2" align="center"><a href="user.php-act=qpassword_name.htm" tppabs="http://www.ktwwg.top/user.php?act=qpassword_name" class="f6">密码问题找回密码</a>   <a href="user.php-act=get_password.htm" tppabs="http://www.ktwwg.top/user.php?act=get_password" class="f6">注册邮件找回密码</a></td>
                  </tr>

      </table>
    </form>
  <div class="blank"></div>
  </div>
  <div class="usTxt">
   <a href="javascript:if(confirm('http://bbs.ecmoban.com/  \n\n���ļ��޷��� Teleport Ultra ����, ��Ϊ ����һ�����·���ⲿ������Ϊ������ʼ��ַ�ĵ�ַ��  \n\n�����ڷ������ϴ���?'))window.location='http://bbs.ecmoban.com/'" tppabs="http://bbs.ecmoban.com/" title="ecshop模板堂论坛 ecshop资源下载第一站" target="_blank"><img alt="ecshop模板堂论坛 ecshop资源下载第一站" src="/theme/${theme}/images/ecmoban.jpg" tppabs="http://www.ktwwg.top//theme/${theme}/images/ecmoban.jpg" /></a>   </div>
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

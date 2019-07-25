<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>用户中心_深圳市凯特威武科技有限公司 - Powered by ECShopsad</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" src="js/user.js" tppabs="http://www.ktwwg.top/js/user.js"></script><script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" tppabs="http://www.ktwwg.top/js/transport_jquery.js"></script></head>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.9.1.min.js" tppabs="http://www.ktwwg.top/js/jquery-1.9.1.min.js"></script><script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" tppabs="http://www.ktwwg.top/js/jquery.json.js"></script> <script type="text/javascript" src="/theme/${theme}/js/utils.js" tppabs="http://www.ktwwg.top/js/utils.js"></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="..htm" tppabs="http://www.ktwwg.top/.">首页</a> <code>&gt;</code> 用户中心 
</div>
</div>
<div class="blank"></div>

<div class="block clearfix">
  
  <div class="AreaL">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox">
        <div class="userMenu">
<a href="/" class="curs"><img src="themes/ecmoban_haier2015/images/u1.gif"> 欢迎页</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u2.gif"> 用户信息</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u3.gif"> 我的订单</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u4.gif"> 收货地址</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u5.gif"> 我的收藏</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u6.gif"> 我的留言</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u7.gif"> 我的标签</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u8.gif"> 缺货登记</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u9.gif"> 我的红包</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u10.gif"> 我的推荐</a><a href="user.php?act=comment_list"><img src="themes/ecmoban_haier2015/images/u11.gif"> 我的评论</a>
<!--<a href="user.php?act=group_buy">我的团购</a>-->
<a href="/"><img src="themes/ecmoban_haier2015/images/u12.gif"> 跟踪包裹</a>
<a href="/"><img src="themes/ecmoban_haier2015/images/u13.gif"> 资金管理</a>
<a href="/" style="background:none; text-align:right; margin-right:10px;"><img src="themes/ecmoban_haier2015/images/bnt_sign.gif"></a>
</div>      </div>
     </div>
    </div>
  </div>
  <div class="AreaR">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox boxCenterList clearfix" style="_height:1%;">
         
                   <font class="f5"><b class="f4">${frontUser.username}</b> 欢迎您回到 深圳市凯特威武科技有限公司！</font><br>
          <div class="blank"></div>
          您的上一次登录时间: 2017-08-06 11:09:00<br>
          <div class="blank5"></div>
          您的等级是 注册用户  <br>
          <div class="blank5"></div>
                     您还没有通过邮件认证 <a href="javascript:sendHashMail()" style="color:#006bd0;">点此发送认证邮件</a><br>
                      <div style="margin:5px 0; border:1px solid #f7dd98;padding:10px 20px; background-color:#fffad5;">
           <img src="themes/ecmoban_haier2015/images/note.gif" alt="note">&nbsp;用户中心公告！           </div>
           <br><br>
          <div class="f_l" style="width:350px;">
          <h5><span>您的账户</span></h5>
          <div class="blank"></div>
          余额:<a href="user.php?act=account_log" style="color:#006bd0;">￥0.00元</a><br>
                    红包:<a href="user.php?act=bonus" style="color:#006bd0;">共计 0 个,价值 ￥0.00元</a><br>
          积分:0积分<br>
          </div>
          <div class="f_r" style="width:350px;">
          <h5><span>用户提醒</span></h5>
          <div class="blank"></div>
                     您最近30天内提交了0个订单<br>
                    </div>
    
        <div class="blank5"></div>
   
      
         
    
  
      </div>
     </div>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>登录</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script></head>
<body onkeydown="keyLogin();">
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.base64.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" ></script> 
 <script type="text/javascript" src="/theme/${theme}/js/utils.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/user.js" ></script>
 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 



<div class="block block1">
  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/">首页</a> <code>&gt;</code> 用户中心 <code>&gt;</code>登录
</div>
</div>
<div class="blank"></div>

<div class="usBox clearfix">
  <div class="usBox_1 f_l">
    <div class="login_tab">
    <ul>
        <li class="active"><a href="/user/login.shtml" tppabs="http://www.ktwwg.top/user.php">用户登录</a></li>
        <li ><a href="/user/register.shtml" tppabs="http://www.ktwwg.top/user.php?act=register">用户注册</a></li>
    </ul>
    </div>
   <form name="formLogin" id="formLogin"  method="post">
        <table width="100%" border="0" align="left" cellpadding="3" cellspacing="5">
          <tr>
            <td width="25%" align="right">用户名：</td>
            <td width="65%"><input name="username" type="text" size="25" class="inputBg" /></td>
          </tr>
          <tr>
            <td align="right">请输入您的手机号：</td>
            <td>
                <input name="log" type="password" id="wpuf-user_email" size="15"  class="inputBg"/>            
            </td>
          </tr>
          <tr>
            <td>验证码：</td>
            <td>
                <input type="text" placeholder="请输入验证码" id="smsRegisterSign" name="smsRegisterSign" class="passwd" maxlength="6" autocomplete="off">
                <input type="button" class="btn_sendCode" id="btnSendCode" value="发送验证码" onclick="findpasswordMessage()" style="padding: 5px;background: #31a030;color:#fff;" />
            </td>
          </tr>
           <tr>
               <td>新密码：</td>
               <td><input type="password" name="pwd" id="userPassword" class="input" value="" size="20" /></td>
           </tr>
           <tr>
               <td>确认密码：</td>
               <td><input type="password" name="pwd" id="userPassword2" class="input" value="" size="20" /></td>
           </tr>
           <tr>
               <td><input type="button" name="wp-submit" id="wp-submit" value="提交" onclick="findpassword()" style="margin: 35px;"/></td>
           </tr>
      </table>
    </form>
    <div class="blank"></div>
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

// 回车登录
function keyLogin(){
    if(window.event.keyCode == 13){
        userLogin();
    }
}

        // 找回密码提交按钮
        var InterValObj; //timer变量，控制时间
        var count = 120; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        var code = ""; //验证码
        var codeLength = 4;//验证码长度


        function SetRemainTime() {
            0 == curCount ? (window.clearInterval(InterValObj), $("#btnSendCode").removeAttr("disabled"), $("#btnSendCode").val("重发验证码"), code = "") : (curCount--, $("#btnSendCode").val(curCount + "秒内输入"))
        }

        function findpassword(){
            var username =$('#usernames').val();
            var phone = $("#wpuf-user_email").val();
            var sign = $('#smsRegisterSign').val();
            var newPassword1 = $('#userPassword').val();
            var pwda = $.base64.encode(newPassword1);
            var newPassword2 = $('#userPassword2').val();
            var pwdb = $.base64.encode(newPassword2);
            if (!(/^1[3|4|5|7|8]\d{9}$/.test(phone))|| sign.length == 0 ||  pwda.length < 7 || pwda != pwdb || username.length == 0) {
                alert('输入有误，请重新输入！');
            } else {
                $.ajax({
                    type:"POST",
                    url:'/user/update/forgetPassword/phone.json',
                    data:{ 
                        userName: username,
                        phone:phone,
                        sign:sign,
                        newPassword1:pwda,
                        newPassword2:pwdb
                    },           
                    async:false,
                    success: function(data) {
                        if(data.message.operateCode == 102008){
                            alert(data.message.message);
                            if (data.redirectUrl == '' || data.redirectUrl == undefined) {
                                window.location.href = "/";
                            } else {
                                window.location.href = data.redirectUrl;
                            }
                        }else{
                            alert(data.message.message);
                            return false;
                        }
                        
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    }
                })
            }
        }
       //  找回密码发送验证码
        function findpasswordMessage(){
                curCount = count;
                var phone = $("#wpuf-user_email").val();//手机号码
                var username = $('#usernames').val();//用户名
                if (phone != "" && /^1[3|4|5|7|8]\d{9}$/.test(phone) || username.length != 0) {
                    //产生验证码
                    for (var i = 0; i < codeLength; i++) {
                        code += parseInt(Math.random() * 9).toString();
                    }
                    //设置button效果，开始计时
                    $.ajax({
                        type: "POST",
                        url: '/user/findPassword/phone/submit/'+phone+'.json', //目标地址
                        data: {
                            phone: phone,
                            userName:username
                        },
                        dataType: 'json',
                        success: function (data) {
                            switch (data.message.operateCode) {
                                case 500031:
                                    alert(data.message.message);
                                    location.href="/user/login.shtml";
                                    break;
                                case 102008:
                                    return "102008" == data.message.operateCode ? ($("#btnSendCode").attr("disabled", "true"), $("#btnSendCode").val(curCount + "秒内输入"), InterValObj = window.setInterval(SetRemainTime, 1e3), !0) : void 0
                                    break;
                                case 500058:
                                    alert(data.message.message);
                                    break;
                            }	
                        },
                        error: function () {
                            alert("系统繁忙,请稍后再试");
                            return false;
                        }
                    });
                } else {
                    alert("手机号码输入不正确！");
                }
        } 
</script>
</html>

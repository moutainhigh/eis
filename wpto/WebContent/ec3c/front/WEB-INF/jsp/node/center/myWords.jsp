<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>用户中心_${systemName}</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" src="js/user.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>
</head>
<body>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
<div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/">首页</a> <code>&gt;</code> 用户中心 
</div>
</div>
<div class="blank"></div>

<div class="block clearfix">
  
  <div class="AreaL">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox">
        <div class="userMenu">
<a href="/content/center/center.shtml"><img src="/theme/${theme}/images/u1.gif"> 欢迎页</a>
<a href="/content/center/userMessage.shtml"><img src="/theme/${theme}/images/u2.gif"> 用户信息</a>
<a href="/content/center/myOrders.shtml"><img src="/theme/${theme}/images/u3.gif"> 我的订单</a>
<a href="/content/center/myAddress.shtml"><img src="/theme/${theme}/images/u4.gif"> 收货地址</a>
<a href="/content/center/myLove.shtml"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
<a href="/content/center/myWords.shtml" class="curs"><img src="/theme/${theme}/images/u6.gif"> 我的留言</a>
<a href="/content/center/myLabel.shtml"><img src="/theme/${theme}/images/u7.gif"> 我的标签</a>
<a href="/content/center/shortsSupply.shtml"><img src="/theme/${theme}/images/u8.gif"> 缺货登记</a>
<a href="/content/center/myPacket.shtml"><img src="/theme/${theme}/images/u9.gif"> 我的红包</a>
<a href="/content/center/myRecommend.shtml"><img src="/theme/${theme}/images/u10.gif"> 我的推荐</a>
<a href="/content/center/myComments.shtml"><img src="/theme/${theme}/images/u11.gif"> 我的评论</a>
<!--<a href="user.php?act=group_buy">我的团购</a>-->
<a href="/content/center/trackingPackages.shtml"><img src="/theme/${theme}/images/u12.gif"> 跟踪包裹</a>
<a href="/content/center/fundManagement.shtml"><img src="/theme/${theme}/images/u13.gif"> 资金管理</a>
<a href="/" style="background:none; text-align:right; margin-right:10px;"><img src="/theme/${theme}/images/bnt_sign.gif" onclick="secede()"></a>
</div>      </div>
     </div>
    </div>
  </div>
  <div class="AreaR">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox boxCenterList clearfix" style="_height:1%;" id="notLogin">
            <h5><span>我的留言</span></h5>
                <div class="blank"></div>
                                    <div class="blank"></div>
                <!--<form action="user.php" method="post" enctype="multipart/form-data" name="formMsg" onSubmit="return submitMsg()">-->
                  <table width="100%" border="0" cellpadding="3">
                                        <tr>
                      <td align="right">留言类型：</td>
                      <td>
                                                    <input name="msg_type" type="radio" value="message" checked="checked" />
                        留言                        <input type="radio" name="msg_type" value="complain" />
                        投诉                        <input type="radio" name="msg_type" value="afterSales" />
                        询问                        <input type="radio" name="msg_type" value="equire" />
                        售后                        <input type="radio" name="msg_type" value="ask" />
                        求购              
                        </td>
                    </tr>
                                        <tr>
                      <td align="right">主题：</td>
                      <td><input name="msg_title" type="text" size="30" class="inputBg" /></td>
                    </tr>
                    <tr>
                      <td align="right" valign="top">留言内容：</td>
                      <td><textarea name="msg_content" cols="50" rows="4" wrap="virtual" class="B_blue"></textarea></td>
                    </tr>
                    <!--<tr>
                      <td align="right">上传文件：</td>
                      <td><input type="file" name="message_img"  size="45"  class="inputBg" /></td>
                    </tr>-->
                    <tr>
                      <td>&nbsp;</td>
                      <td>
                        <input type="button" value="提 交" class="bnt_bonus" onclick="send()"/>
                      </td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>
                      <font color="red">小提示：</font><br />
                      您可以上传以下格式的文件：<br />gif、jpg、png、word、excel、txt、zip、ppt、pdf                      </td>
                    </tr>
                  </table>
                <!--</form>-->
                <input type="hidden" value="${frontUser.uuid}" id="uuid">
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
var bonus_sn_empty = "请输入您要添加的红包号码！";
var bonus_sn_error = "您输入的红包号码格式不正确！";
var email_empty = "请输入您的电子邮件地址！";
var email_error = "您输入的电子邮件地址格式不正确！";
var phone_error = "您输入的手机号格式不正确！";
var old_password_empty = "请输入您的原密码！";
var new_password_empty = "请输入您的新密码！";
var confirm_password_empty = "请输入您的确认密码！";
var both_password_error = "您现两次输入的密码不一致！";
var msg_blank = "不能为空";
var no_select_question = "- 您没有完成密码提示问题的操作";

//   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     }
     
 })

         // 站内信息发送按钮
        var messageType;
        function send() {
            $('input[name="msg_type"]').each(function(){
              if($(this).is(':checked')){
                messageType = $(this).val();
              }
            })
            var message_title = $('input[name="msg_title"]').val();
            var message_content = $('textarea[name="msg_content"]').val();
            var uuid = $('#uuid').val();
            if ( message_title.length == 0 || message_content == '') {
                alert('还有未填的信息，请填写完整后发布！')
            }else{
                $.ajax({
                    type: "post",
                    url: '/userMessage/create.json',
                    data: {
                        receiverName:'cyx',
                        title:message_title,
                        content:message_content,
                        // uuid:uuid,
                        messageType:messageType
                    },
                    dataType: 'json',
                    // async: false,
                    success: function(data) {
                          alert(data.message.message);
                          window.location.reload();
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    },
                }, 'json');
          }
        }

</script>
</html>


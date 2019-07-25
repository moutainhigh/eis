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
</head>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/jquery.form.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/jquery.base64.js" ></script>

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
<a href="/content/center/userMessage.shtml" class="curs"><img src="/theme/${theme}/images/u2.gif"> 用户信息</a>
<a href="/content/center/myOrders.shtml"><img src="/theme/${theme}/images/u3.gif"> 我的订单</a>
<a href="/content/center/myAddress.shtml"><img src="/theme/${theme}/images/u4.gif"> 收货地址</a>
<a href="/content/center/myLove.shtml"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
<a href="/content/center/myWords.shtml"><img src="/theme/${theme}/images/u6.gif"> 我的留言</a>
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

      <h5><span>个人资料</span></h5>
      <div class="blank"></div>

      <!--登录用户修改资料-->
     <form id="formEdit" action="">
      <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
                <!--<tr>
                  <td width="28%" align="right" bgcolor="#FFFFFF">出生日期： </td>
                  <td width="72%" align="left" bgcolor="#FFFFFF"> <select name="birthdayYear"><option value="1957">1957</option><option value="1958">1958</option><option value="1959">1959</option><option value="1960">1960</option><option value="1961">1961</option><option value="1962">1962</option><option value="1963">1963</option><option value="1964">1964</option><option value="1965">1965</option><option value="1966">1966</option><option value="1967">1967</option><option value="1968">1968</option><option value="1969">1969</option><option value="1970">1970</option><option value="1971">1971</option><option value="1972">1972</option><option value="1973">1973</option><option value="1974">1974</option><option value="1975">1975</option><option value="1976">1976</option><option value="1977">1977</option><option value="1978">1978</option><option value="1979">1979</option><option value="1980">1980</option><option value="1981">1981</option><option value="1982">1982</option><option value="1983">1983</option><option value="1984">1984</option><option value="1985">1985</option><option value="1986">1986</option><option value="1987">1987</option><option value="1988">1988</option><option value="1989">1989</option><option value="1990">1990</option><option value="1991">1991</option><option value="1992">1992</option><option value="1993">1993</option><option value="1994">1994</option><option value="1995">1995</option><option value="1996">1996</option><option value="1997">1997</option><option value="1998">1998</option><option value="1999">1999</option><option value="2000">2000</option><option value="2001">2001</option><option value="2002">2002</option><option value="2003">2003</option><option value="2004">2004</option><option value="2005">2005</option><option value="2006">2006</option><option value="2007">2007</option><option value="2008">2008</option><option value="2009">2009</option><option value="2010">2010</option><option value="2011">2011</option><option value="2012">2012</option><option value="2013">2013</option><option value="2014">2014</option><option value="2015">2015</option><option value="2016">2016</option><option value="2017">2017</option><option value="2018">2018</option></select>&nbsp;<select name="birthdayMonth"><option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option><option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option><option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option></select>&nbsp;<select name="birthdayDay"><option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option><option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option><option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option><option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option><option value="29">29</option><option value="30">30</option><option value="31">31</option></select> </td>
                </tr>-->
                <tr>
                  <td width="28%" align="right" bgcolor="#FFFFFF">性　别： </td>
                  <td width="72%" align="left" bgcolor="#FFFFFF"><input type="radio" name="sex" value="null" checked="checked" />
                    保密&nbsp;&nbsp;
                    <input type="radio" name="sex" value="1"  />
                    男&nbsp;&nbsp;
                    <input type="radio" name="sex" value="2"  />
                  女&nbsp;&nbsp; </td>
                </tr>
                <tr>
                  <td width="28%" align="right" bgcolor="#FFFFFF">电子邮件地址： </td>
                  <td width="72%" align="left" bgcolor="#FFFFFF"><input name="userBindMailBox" type="text" value="${frontUser.userConfigMap.userBindMailBox.dataValue}" size="25" class="inputBg" /><span style="color:#FF0000"> *</span></td>
                </tr>
                        <!--<tr>
           <td width="28%" align="right" bgcolor="#FFFFFF" id="extend_field1i">MSN：</td>
            <td width="72%" align="left" bgcolor="#FFFFFF">
            <input name="extend_field1" type="text" class="inputBg" value="xqgdtx1@sina.com"/><span style="color:#FF0000"> *</span>            </td>
        </tr>
        		                <tr>
           <td width="28%" align="right" bgcolor="#FFFFFF" id="extend_field2i">QQ：</td>
            <td width="72%" align="left" bgcolor="#FFFFFF">
            <input name="extend_field2" type="text" class="inputBg" value="756528875"/><span style="color:#FF0000"> *</span>            </td>
        </tr>
        		                <tr>
           <td width="28%" align="right" bgcolor="#FFFFFF" id="extend_field3i">办公电话：</td>
            <td width="72%" align="left" bgcolor="#FFFFFF">
            <input name="extend_field3" type="text" class="inputBg" value="6314130"/><span style="color:#FF0000"> *</span>            </td>
        </tr>
        		                <tr>
           <td width="28%" align="right" bgcolor="#FFFFFF" id="extend_field4i">家庭电话：</td>
            <td width="72%" align="left" bgcolor="#FFFFFF">
            <input name="extend_field4" type="text" class="inputBg" value="6314130"/><span style="color:#FF0000"> *</span>            </td>
        </tr>-->
        		                <tr>
           <td width="28%" align="right" bgcolor="#FFFFFF" id="extend_field5i">手机：</td>
            <td width="72%" align="left" bgcolor="#FFFFFF">
            <input name="userBindPhoneNumber" type="text" class="inputBg" value="${frontUser.userConfigMap.userBindPhoneNumber.dataValue}"/><span style="color:#FF0000"> *</span>            </td>
        </tr>
        		                <!--<tr>
          <td width="28%" align="right" bgcolor="#FFFFFF">密码提示问题：</td>
          <td width="72%" align="left" bgcolor="#FFFFFF">
          <select name='sel_question'>
          <option value='0'>请选择密码提示问题</option>
          <option value="friend_birthday">我最好朋友的生日？</option><option value="old_address">我儿时居住地的地址？</option><option value="motto" selected>我的座右铭是？</option><option value="favorite_movie">我最喜爱的电影？</option><option value="favorite_song">我最喜爱的歌曲？</option><option value="favorite_food">我最喜爱的食物？</option><option value="interest">我最大的爱好？</option><option value="favorite_novel">我最喜欢的小说？</option><option value="favorite_equipe">我最喜欢的运动队？</option>          </select>
          </td>
        </tr>
        <tr>
       <td width="28%" align="right" bgcolor="#FFFFFF" id="passwd_quesetion">密码问题答案：</td>
          <td width="72%" align="left" bgcolor="#FFFFFF">
          <input name="passwd_answer" type="text" size="25" class="inputBg" maxlengt='20' value="我曹啊"/><span style="color:#FF0000"> *</span>          </td>
        </tr>-->
        		                <tr>
                  <td colspan="2" align="center" bgcolor="#FFFFFF">
                    <input type="button" value="确认修改" class="bnt_blue_1" style="border:none;" onclick="userEdit()"/>
                  </td>
                </tr>
       </table>
        </form>

            <!--登录用户修改密码-->
        <form  action="" >
        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
            <tr>
            <td width="28%" align="right" bgcolor="#FFFFFF">原密码：</td>
            <td width="76%" align="left" bgcolor="#FFFFFF"><input name="oldPassword" type="password" size="25"  class="inputBg" /></td>
            </tr>
            <tr>
            <td width="28%" align="right" bgcolor="#FFFFFF">新密码：</td>
            <td align="left" bgcolor="#FFFFFF"><input name="password1" type="password" size="25"  class="inputBg" /></td>
            </tr>
            <tr>
            <td width="28%" align="right" bgcolor="#FFFFFF">确认密码：</td>
            <td align="left" bgcolor="#FFFFFF"><input name="password2" type="password" size="25"  class="inputBg" /></td>
            </tr>
            <tr>
            <td colspan="2" align="center" bgcolor="#FFFFFF">
                <input  type="button" class="bnt_blue_1" style="border:none;" value="确认修改" onclick="editPassword()"/>
            </td>
            </tr>
        </table>
        </form>

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

//  登录用户修改资料
 function userEdit(){
      if(!( /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/.test($('input[name="userBindMailBox"]').val())) || $('input[name="userBindMailBox"]').val().length == 0){
          alert(email_error);
          return false;
      }else if(!(/0?(13|14|15|18)[0-9]{9}/.test($('input[name="userBindPhoneNumber"]').val()))){
          alert(phone_error);
          return false;
      }else{
          $.ajax({
                type:"POST",
                url:"/user/submitChangeOther.json",
                data:{  
                    userBindMailBox:$('input[name="userBindMailBox"]').val(),
                    userBindPhoneNumber:$('input[name="userBindPhoneNumber"]').val()
                },           
                async:false,
                success: function(data) {
                    if(data.message.operateCode == 102008){
                        alert(data.message.message);
                        document.location.reload();
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

//  登录用户修改密码
function editPassword(){
      if($('input[name="oldPassword"]').val().length == 0){
          alert(old_password_empty);
          return false;
      }else if($('input[name="password1"]').val().length < 6){
          alert('密码不得少于6位数！');
          return false;
      }else if($('input[name="password2"]').val().length == 0){
          alert(confirm_password_empty);
          return false;
      }else if($('input[name="password1"]').val() != $('input[name="password2"]').val()){
         alert(both_password_error);
          return false;
      }else if($('input[name="password1"]').val() == $('input[name="oldPassword"]').val()){
          alert('新密码不得和原密码一样！');
          return false;
      }else{
          $.ajax({
                type:"POST",
                url:"/user/changePassword.json",
                data:{  
                    oldPassword:$.base64.encode($('input[name="oldPassword"]').val()),
                    password1:$.base64.encode($('input[name="password1"]').val()),
                    password2:$.base64.encode($('input[name="password2"]').val())
                },           
                async:false,
                success: function(data) {
                    if(data.message.operateCode == 102008){
                        alert(data.message.message);
                        document.location.reload();
                        window.location.href  = '/user/login.shtml';
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
</script>
</html>

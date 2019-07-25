<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>购物流程_深圳市凯特威武科技有限公司 - Powered by ECShop</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" ></script><script type="text/javascript" src="/theme/${theme}/js/shopping_flow.js" ></script></head>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.9.1.min.js" ></script><script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" tppabs="http://www.ktwwg.top/js/jquery.json.js"></script> <script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" tppabs="http://www.ktwwg.top/js/transport_jquery.js"></script><script type="text/javascript" src="/theme/${theme}/js/utils.js" tppabs="http://www.ktwwg.top/js/utils.js"></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
 

 




  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="..htm" tppabs="http://www.ktwwg.top/.">首页</a> <code>&gt;</code> 购物流程 
</div>
</div>
<div class="blank"></div>
 
<div class="block table">
  
  
  
  <script type="text/javascript" src="js/showdiv.js" tppabs="http://www.ktwwg.top/js/showdiv.js"></script>  <script type="text/javascript">
      var user_name_empty = "请输入您的用户名！";
      var email_address_empty = "请输入您的电子邮件地址！";
      var email_address_error = "您输入的电子邮件地址格式不正确！";
      var new_password_empty = "请输入您的新密码！";
      var confirm_password_empty = "请输入您的确认密码！";
      var both_password_error = "您两次输入的密码不一致！";
      var show_div_text = "请点击更新购物车按钮";
      var show_div_exit = "关闭";
    </script>
  <div class="flowBox">
    <h6><span>商品列表</span></h6>
        <form id="formCart" name="formCart" method="post" action="http://www.ktwwg.top/flow.php">
           <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
            <tr>
              <th bgcolor="#ffffff">商品名称</th>
                    <th bgcolor="#ffffff">属性</th>
                                          <th bgcolor="#ffffff">市场价</th>
                            <th bgcolor="#ffffff">本店价</th>
              <th bgcolor="#ffffff">购买数量</th>
              <th bgcolor="#ffffff">小计</th>
              <th bgcolor="#ffffff">操作</th>
            </tr>
                      </table>
          <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
            <tr>
              <td bgcolor="#ffffff">
                            购物金额小计 ￥0.00元，比市场价 ￥0.00元 节省了 ￥0.00元 (0)              </td>
              <td align="right" bgcolor="#ffffff">
                <input type="button" value="清空购物车" class="bnt_blue_1" onclick="location.href='index.htm'/*tpa=http://www.ktwwg.top/flow.php?step=clear*/" />
                <input name="submit" type="submit" class="bnt_blue_1" value="更新购物车" />
              </td>
            </tr>
          </table>
          <input type="hidden" name="step" value="update_cart" />
        </form>
        <table width="99%" align="center" border="0" cellpadding="5" cellspacing="0" bgcolor="#dddddd">
          <tr>
            <td bgcolor="#ffffff"><a href="index.htm" tppabs="http://www.ktwwg.top/"><img src="/theme/${theme}/images/continue.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/continue.gif" alt="continue" /></a></td>
            <td bgcolor="#ffffff" align="right"><a href="flow.php-step=checkout.htm" tppabs="http://www.ktwwg.top/flow.php?step=checkout"><img src="/theme/${theme}/images/checkout.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/checkout.gif" alt="checkout" /></a></td>
          </tr>
        </table>
       </div>
    <div class="blank"></div>
   
    <div class="blank5"></div>
  
  
  

        
        
                



</div>


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
var compare_no_goods = "您没有选定任何需要比较的商品或者比较的商品数少于 2 个。";
var btn_buy = "购买";
var is_cancel = "取消";
var select_spe = "请选择商品属性";
</script>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<meta name="Description" content="" />
<meta http-equiv="refresh" content="3;URL=javascript:history.back()" />

<title>结算</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" tppabs="http://www.ktwwg.top/js/common.js"></script><style type="text/css">
p a{color:#006acd; text-decoration:underline;}
</style>
</head>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>

<div class="blank"></div>
<div class="block table">
        <script type="text/javascript" src="/theme/${theme}/js/region.js"></script> 
        <script type="text/javascript">
          region.isAdmin = false;
                    var consignee_not_null = "收货人姓名不能为空！";
                    var country_not_null = "请您选择收货人所在国家！";
                    var province_not_null = "请您选择收货人所在省份！";
                    var city_not_null = "请您选择收货人所在城市！";
                    var district_not_null = "请您选择收货人所在区域！";
                    var invalid_email = "您输入的邮件地址不是一个合法的邮件地址。";
                    var address_not_null = "收货人的详细地址不能为空！";
                    var tele_not_null = "电话不能为空！";
                    var shipping_not_null = "请您选择配送方式！";
                    var payment_not_null = "请您选择支付方式！";
                    var goodsattr_style = "1";
                    var tele_invaild = "电话号码不有效的号码";
                    var zip_not_num = "邮政编码只能填写数字";
                    var mobile_invaild = "手机号码不是合法号码";
          
          
          onload = function() {
            if (!document.all)
            {
              document.forms['theForm'].reset();
            }
          }
          
        </script>
        
                <form action="flow.php" method="post" name="theForm" id="theForm" onsubmit="return checkConsignee(this)">
        <div class="flowBox">
<h6><span>收货人信息</span></h6>
<table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
    
  <tbody><tr>
    <td bgcolor="#ffffff">配送区域:</td>
    <td colspan="3" bgcolor="#ffffff">
    <select name="country" id="selCountries_0" onchange="region.changed(this, 1, 'selProvinces_0')" style="border:1px solid #ccc;">
        <option value="0">请选择国家</option>
                <option value="1" selected="">中国</option>
              </select>
      <select name="province" id="selProvinces_0" onchange="region.changed(this, 2, 'selCities_0')" style="border:1px solid #ccc;">
        <option value="0">请选择省</option>
                <option value="2">北京</option>
                <option value="3">安徽</option>
                <option value="4">福建</option>
                <option value="5">甘肃</option>
                <option value="6">广东</option>
                <option value="7">广西</option>
                <option value="8">贵州</option>
                <option value="9">海南</option>
                <option value="10">河北</option>
                <option value="11">河南</option>
                <option value="12">黑龙江</option>
                <option value="13">湖北</option>
                <option value="14">湖南</option>
                <option value="15">吉林</option>
                <option value="16">江苏</option>
                <option value="17">江西</option>
                <option value="18">辽宁</option>
                <option value="19">内蒙古</option>
                <option value="20">宁夏</option>
                <option value="21">青海</option>
                <option value="22">山东</option>
                <option value="23">山西</option>
                <option value="24">陕西</option>
                <option value="25">上海</option>
                <option value="26">四川</option>
                <option value="27">天津</option>
                <option value="28">西藏</option>
                <option value="29">新疆</option>
                <option value="30">云南</option>
                <option value="31">浙江</option>
                <option value="32">重庆</option>
                <option value="33">香港</option>
                <option value="34">澳门</option>
                <option value="35">台湾</option>
              </select>
      <select name="city" id="selCities_0" onchange="region.changed(this, 3, 'selDistricts_0')" style="border:1px solid #ccc;">
        <option value="0">请选择市</option>
              </select>
      <select name="district" id="selDistricts_0" style="display:none">
        <option value="0">请选择区</option>
              </select>
    (必填) </td>
  </tr>
    <tr>
    <td bgcolor="#ffffff">收货人姓名:</td>
    <td bgcolor="#ffffff"><input name="consignee" type="text" class="inputBg" id="consignee_0" value="">
    (必填) </td>
    <td bgcolor="#ffffff">电子邮件地址:</td>
    <td bgcolor="#ffffff"><input name="email" type="text" class="inputBg" id="email_0" value="1689330420@qq.com">
    (必填)</td>
  </tr>
    
  <tr>
    <td bgcolor="#ffffff">详细地址:</td>
    <td bgcolor="#ffffff"><input name="address" type="text" class="inputBg" id="address_0" value="">
    (必填)</td>
    <td bgcolor="#ffffff">邮政编码:</td>
    <td bgcolor="#ffffff"><input name="zipcode" type="text" class="inputBg" id="zipcode_0" value=""></td>
  </tr>
    <tr>
    <td bgcolor="#ffffff">电话:</td>
    <td bgcolor="#ffffff"><input name="tel" type="text" class="inputBg" id="tel_0" value="">
    (必填)</td>
    <td bgcolor="#ffffff">手机:</td>
    <td bgcolor="#ffffff"><input name="mobile" type="text" class="inputBg" id="mobile_0" value=""></td>
  </tr>
    
  <tr>
    <td bgcolor="#ffffff">标志建筑:</td>
    <td bgcolor="#ffffff"><input name="sign_building" type="text" class="inputBg" id="sign_building_0" value=""></td>
    <td bgcolor="#ffffff">最佳送货时间:</td>
    <td bgcolor="#ffffff"><input name="best_time" type="text" class="inputBg" id="best_time_0" value=""></td>
  </tr>
    <tr>
    <td colspan="4" align="center" bgcolor="#ffffff">
    <input type="submit" name="Submit" class="bnt_blue_2" value="配送至这个地址">
            <input type="hidden" name="step" value="consignee">
      <input type="hidden" name="act" value="checkout">
      <input name="address_id" type="hidden" value="">
      </td>
  </tr>
</tbody></table>
</div>        </form>
                
        
                



</div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

</body>
</html>

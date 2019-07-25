<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}-新增收货地址</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/addaddress.css"/>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.cityselect.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/addaddress.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/allcity.js"></script>
</head>
<body>
	<div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>新增收货地址</span><!--<a class="home" href="/">-->
 </div>
  <div id="wrapper_1">
   <div class="wid90">
   	<form id="message" class="abc">
		<div class='box_message'>
			<ul class='box_message_list'>
				<li><span class='text_left'>收&nbsp货&nbsp人：</span><input type='text'  id='contact' name='contact' /></li>
				<li><span class='text_left'>联系方式：</span> <input type='text'  id='mobile' name='mobile' /></li>
				<li>
					<span class='text_left'>区域选择：</span>
					<span style='display:block;'  class='selects'>
						<select id="province" name="province"></select>  
					    <select id="city" name="city" ></select>  
					    <!-- <select id="county" name="county"></select>  -->
					    <script class="resources library" src="../../../theme/${theme}/js/mobile/area.js" type="text/javascript"></script>
					    <script type="text/javascript">_init_area();</script>  
					</span>
				</li> 
				<li><span class='text_left'>详细地址：</span><input type='text'  id='address' name='address'/></li>
			</ul> 
			<input type='hidden' value='' id='currentStatus' name='currentStatus' />
		</div>
		<div class='wid90'>
			<div class='box_left'>
				<div class='checkBox' >
					<input type='checkbox' value='1' id='checkboxgroup' name='' class='check' /><label id="label" for='checkboxgroup'></label>
				</div>
			</div>
			<a><span style='font-size:0.8em'>设为默认地址</span><span style='color: #717777;font-size:0.75em'>(每次购买时会默认使用该地址)</span></a>
		</div> 
		<div class='wid90'>
			<div class='box_center'><input type='submit' class='btn_login mt30' id='login' name='login' value='保存'/></div>		
		</div>
   	</form>
   </div>
  </div>
  <script type="text/javascript">
var Gid  = document.getElementById ;
var showArea = function(){
	Gid('show').innerHTML = "<h3>省" + Gid('s_province').value + " - 市" + 	
	Gid('s_city').value + " - 县/区" + 
	Gid('s_county').value + "</h3>"
							}


</script>
</body>
</html>

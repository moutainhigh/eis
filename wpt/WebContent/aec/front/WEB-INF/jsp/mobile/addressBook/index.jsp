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
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/address.css"/>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.cityselect.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/address.js"></script>
</script>
</head>
<body>
	<div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>收货地址</span><!--<a class="home" href="/">-->
 </div>
  <div id="wrapper_1">
   <div class="wid90">
   	<form id="message" class="abc">
   	
   	</form>
   </div>
	
<c:forEach var="address" items="${addressBook}">
	 <div class="wid90">
	 	<div class="box_left">
					<div class="checkBox " >
	  				<label ></label>
  					</div>
		</div>
			<div class="box_left address_box">
				<span class="address"><c:if test="${address.currentStatus == '100003'}"><span style="color: #0091ff">[默认]</span></c:if>${address.province}&nbsp;&nbsp;&nbsp;${address.city}&nbsp;&nbsp;&nbsp;${address.address}</span>
				<p></p>
			<p>${address.contact} <span>${address.mobile}</span></p>
			</div>
			<div class="box_right change none">
				<span class="orange update_btn" ><a href="/addressBook/update/${address.addressBookId}.shtml" id="addressBookId" value="${address.addressBookId}">修改</a></span>
			<p></p>
			</div>
	 </div>
	<div class="line"></div>
</c:forEach>
	
	 <div class="wid90 martop20 none">
				<div class="box_left " style="margin-left: 8%;">
					<span>新增收货地址</span>
				</div>
				<div class="box_right orange">
					<a class="addAddress" style="color:#ff6400; margin-left:0%;">添加</a>
				</div>
			</div>
	<div class="line"></div>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../css/main.css">
<link rel="stylesheet" type="text/css" href="../css/pcenter.css">
<link rel="stylesheet" type="text/css" href="../css/address1.css">
<script  type="text/javascript" src="../js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../js/jquery.cityselect.js"></script>
<script  type="text/javascript" src="../js/jquery.validate.min.js"></script>
<script  type="text/javascript" src="../js/address.js"></script>
<script  type="text/javascript" src="../js/common.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
</head>
<body>
    	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50 marbottom50">
    <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox">
	    <div class="martop20">
	    	<span class="orange">账户中心>收货地址</span>
	    </div>
	   <div class="default martop20">
	    <c:forEach var="address" items="${addressBook}">
	   		<div class="first_box">
	   			<img src="../image/ditu.png" class="ditu_icon" style="display:none;"/>
	   			<input type="radio" class="radio_style" />
	   			<div class="default_adderss">
	   				<ul class="address_consignee">
				 <c:if test="${address.currentStatus == '100003'}"> <li class="orange">[默认]</li></c:if> 
	   				   <li>${address.contact}</li>
	   				   <li style="margin-left: 90px;">${address.mobile}</li>
	   				</ul><br>
	   				<ul class="address_specific">
	   				   
	   				    <li>${address.province}</li>
	   				    <li>${address.city}</li>
	   				    <li>${address.address}</li>
	   			</ul>
	   			
	   			</div>
				
				<div class="change_address">
	   				<a href="#" class="black" onclick="del(${editdetail.addressBookId}1)">删除</a>
	   			</div>
				<div class="change_address">
	   				<a href="/addressBook/update/${address.addressBookId}.shtml" class="black">修改</a>
	   			</div>
				 <c:if test="${address.currentStatus != '100003'}"> <div class="change_address">
	   				<a href="#" class="black">设为默认</a>
	   			</div></c:if>
	   		</div>
			</c:forEach>
            <a class="btn_newaddress">
	   			<span class="add_icon">+</span>
	   			<span class="text_btn_new">新增地址</span>
	   		</a>


	   </div>
      <form id="member_xgForm">
	      
	 </form>
	 
	 </div>
   </div>
 	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
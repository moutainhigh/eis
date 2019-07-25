<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/main.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/pcenter.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/address1.css">
<script  type="text/javascript" src="../../../theme/${theme}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../theme/${theme}/js/respond.src.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/jquery.cityselect.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/jquery.validate.min.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/address.js"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/common.js"></script>
<style type="text/css">
.btn_save2 {
  width: 150px;
  height: 40px;
  border: 0;
  background-color: #ff6400;
  color: #fff;
  font-size: 20px;
  letter-spacing: 2px;
  margin-top: 20px;
  margin-left: 100px;
  cursor: pointer;
  outline: none;
}
</style>
<script type="text/javascript">
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

$(function(){
	$(".update_btn").click(function(){
		$("#member_xgForm2").css("display","block");
		$(".default").css("display","none");
		$(".selects").citySelect({
		          nodata:"none",
		           required:false
		})
		//alert("xxxx   "+$("#addressBookId").val());
		$.ajax({
			type:"GET",
			url:"/addressBook/update/"+$(this).children("input[id='addressBookId']").val()+".json",
			dataType:"json",
			success:(function(data){
				$(data.editdetail).each(function(i,m) {												
					$("#contact").val(m.contact);
					$("#province").val(m.province);
					//$("#city").val(m.city);
					$("#city").html("<option>"+m.city+"</option>");
				    //alert("城市  "+m.city);
					$("#address").val(m.address);
					$("#mobile").val(m.mobile);
					$("#postcode").val(m.postcode);
					$("#uuid").val(m.uuid);
					//alert("addressBookId   "+m.addressBookId);
					$("#addressBookId").val(m.addressBookId);
					$("#currentStatus").val(m.currentStatus);
					//alert("是否选中   "+m.currentStatus);
					if(m.currentStatus == "100003"){
						document.getElementById("checkboxInput").checked = true;
					}				
				});
			}),
			error:function (data) {
				alert("系统繁忙,请稍后再试");
				return false;
			}
			/*error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readyState);
				alert(textStatus);
			},*/
		})
	})
	
	
})
</script>
<script type="text/javascript">
$(function(){

	$(".btn_save2").click(function(){
		if (document.getElementById("checkboxInput").checked == true){
			$("#currentStatus").attr("value","100003");
		}else{
			$("#currentStatus").attr("value","100001");
		} 
		$.ajax({
			type:"POST",
			url:"/addressBook/update.json",
			data:{
				contact:$("#contact").val(),
				province:$("#province").val(),
				city:$("#city").val(),
				address:$("#address").val(),
				mobile:$("#mobile").val(),
				postcode:$("#postcode").val(),
				uuid:$("#uuid").val(),
				addressBookId:$("#addressBookId").val(),
				currentStatus:$("#currentStatus").val()
			},
			dataType:"json",
			success:(function(data){
				alert(data.message.message);
				if(data.message.operateCode == "102008"){
					$("#member_xgForm2").css("display","none");
					window.location.reload();
				}
				
			}),
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert("系统繁忙");
			},
		})
	})
})
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
	   			<!--<input type="radio" class="radio_style"  />-->
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
	   				<a href="#" class="orange" onclick="del(${address.addressBookId})">删除</a>
	   			</div>
				<div class="change_address">
	   				<a class="orange update_btn"><input type="hidden" id="addressBookId" value="${address.addressBookId}" />修改</a>
	   			</div>
				 <c:if test="${address.currentStatus != '100003'}"> <div class="change_address">
	   				<a href="#" class="orange" id="check_01" onclick="moren(${address.addressBookId})">设为默认</a>
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
	 <!-- 修改 -->
	 <form id="member_xgForm2" style="display:none;">
		<div class="message_box martop20">
   <div class="new_address1">
    <div class="addressInfo">
     <div>
      <span class="icon_star">*</span> 收&nbsp;&nbsp;货&nbsp;&nbsp;人 : 
      <input type="text" id="contact" name="contact" value="" />
     </div>
     <div>
      <span class="icon_star">*</span> 区域选择 :
      <span class="selects">
		<select class="prov" id="province" name="province"></select>
		<select class="city marleft20" id="city" name="city"></select>
	  </span>
     </div>
     <div>
      <span class="icon_star">*</span> 详细地址 : 
      <input type="text" id="address" name="address" value="" />
     </div>
     <div>
      <span class="icon_star">*</span> 联系方式 : 
      <input type="text" id="mobile" name="mobile" value="" />
     </div>
     <input type="hidden" value="" id="currentStatus" name="currentStatus" class="box_right" />
    </div>
    <div class="checkBox">
     <input type="checkbox" value="1" id="checkboxInput" name="" checked="" />
     <label for="checkboxInput" id="checkBoxLabel"></label>
    </div>设为默认地址
   </div>
  </div>
  <input type="hidden" id="contact" name="contact" value="" />
  <input type="hidden" id="province" name="province" value="" />
  <input type="hidden" id="city" name="city" value="" />
  <input type="hidden" id="address" name="address" value="" />
  <input type="hidden" id="mobile" name="mobile" value="" />
  <input type="hidden" id="postcode" name="postcode" value="" />
  <input type="hidden" id="uuid" name="uuid" value="" />
  <input type="hidden" id="addressBookId" name="addressBookId" value="" />
  <input type="hidden" id="currentStatus" name="currentStatus" value="" />
  <input type="button" value="保存" id="adddistrict" name="adddistrict" class="btn_save2" />
	 </form>
	 </div>
   </div>
 	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
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
<link rel="stylesheet" type="text/css" href="../css/tijiaodingdan.css">
<script  type="text/javascript" src="../js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../js/jquery.cityselect.js"></script>
<script  type="text/javascript" src="../js/jquery.validate.min.js"></script>
<script  type="text/javascript" src="../js/common.js"></script>
<script  type="text/javascript" src="../js/orderconfirm.js"></script>
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
  <div class="wid-80 martop50">
     <div class="status_right">
     <div class="status"> 
			 <span class="text_status">我的购物车</span> 
			 <span class="text_status">提交订单</span> 
			 <span class="text_status">成功提交订单</span> 
			</div>
			<div class="status">
				<div class="btn_val"> 
				 <span class="line"></span>
				 <span class="circles"></span>
				 <span class="line"></span>
				</div>
				<div class="btn_val"> 
				<span class="line"></span> 
				<span class="circles"></span> 
				<span class="line"></span> 
				</div>
				<div class="btn_val_gray"> 
				<span class="line"></span> 
				<span class="circles"></span> 
				<span class="line"></span> 
				</div>
			</div>
			
	 </div>
	 
	 <input type="hidden" name="orderId" id="orderId" value="${orderId}" />

	 <div class="content">
	   <div class="shipping_address martop20">
	   	<span>收货地址</span>
	   </div>
	  <c:forEach var="address" items="${addressBookList}" begin="0" end="0">
	 
	   		<div class="first_box">
	   			<img src="../image/ditu.png" class="ditu_icon" style="display:none;"/>
	   			<input type="radio" class="radio_style" checked=checked   value="${address.addressBookId}"  name="addressChange"/>
	   			<div class="default_adderss">
	   				<ul class="address_consignee">
				       <li class="orange">[默认]</li>
	   				   <li>${address.contact}</li>
	   				   <li style="margin-left: 90px;">${address.mobile}</li>
	   				</ul><br>
	   				<ul class="address_specific">
	   				   
	   				   <li>${address.province}</li>
	   				    <li>${address.city}</li>
	   				    <li>${address.address}</li>
	   			</ul>
	   			
	   			</div>
	   		</div>
 
			 </c:forEach>
			 
			
			<div class="btngroup_address martop10">
			  <div class="box_container_left">
			     <a id="btnMoreAddress">更多收货地址</a>
			  </div>
			  <div class="box_container_right">
			  <a id="btnAddAddress">新增收货地址</a>
			  </div>
			</div>
	  <div id="box_moreAddress" style="display:none;">
			 <c:forEach var="address" items="${addressBookList}">
	       <c:if test="${address.currentStatus != '100003'}">
	   		<div class="first_box">
	   			<img src="../image/ditu.png" class="ditu_icon" style="display:none;"/>
	   			<input type="radio" class="radio_style" value="${address.addressBookId}" name="addressChange"/>
	   			<div class="default_adderss">
	   				<ul class="address_consignee">
	   				   <li>${address.contact}</li>
	   				   <li style="margin-left: 90px;">${address.mobile}</li>
	   				</ul><br>
	   				<ul class="address_specific">
	   				   
	   				    <li>${address.province}</li>
	   				    <li>${address.city}</li>
	   				    <li>${address.address}</li>
	   				  
	   			</ul>
	   			
	   			</div>
	   		</div>
			 </c:if> 
			 </c:forEach>
			 </div>
        <!--<div class="add_box martop22">
	    	 <a href="#" class="add_btn">添加收货地址</a>
	    </div>-->
		<div id="xg_form" style="display:none;" class="martop30">
	  
	   		<form id="member_xgForm">
			   <div class='message_box martop20'>
			   <div class='new_address1'>
			   <div class='addressInfo'>
			   <div><span class='icon_star'>*</span> 收&ensp;货&ensp;人 : <input type='text' id='contact'  name='contact'  placeholder='请输入收货人姓名'/>
			   </div>
			   <div><span class='icon_star'>*</span> 区域选择 :<span class='selects'><select class='prov' id='province' name='province'></select><select class='city marleft20' id='city' name='city' disabled='disabled'></select></span>
			   </div>
			   <div><span class='icon_star'>*</span> 详细地址 : <input type='text' placeholder=''  id='address'  name='address'/>
			   </div>
			   <div><span class='icon_star'>*</span> 联系方式 : <input type='text' placeholder='请输入手机号'  id='mobile'  name='mobile'/></div>
			   <!-- <div>
			   <span class='icon_star'></span> 邮政编码 : <input type='text' placeholder=''  id='postcode'  name='postcode'/>
			   </div> -->
			   <input type='hidden' value='' id='currentStatus'  name='currentStatus' class='box_right'/>
			   </div>
			   <div class='checkBox'><input type='checkbox' value='1' id='checkboxInput'  name='' checked /><label for='checkboxInput' id='checkBoxLabel'></label>
			   </div>设为默认地址
			   </div>
			   </div>
			   <input type='submit' value='保存' id='adddistrict' name='adddistrict' class='btn_save'/>
			 </form>

	      
	      
	
		   </div>
	       <div class="martop30 ">
	       	<span>支付方式</span>
	       </div>
	       <div class="thirdly_box martop10">
	       		<ul class="pay">
				 <c:forEach var="payType" items="${payTypeList}" varStatus="status" begin="0" end="0">
					<li><input type="radio" name="checkit" checked=cheched value="${payType.payTypeId}"/></li>
	       			<li><img src="../image/weixin.png" class="weixin"></li>
	       			<li>${payType.name}</li>
				 </c:forEach>
	       		<!--<c:forEach var="payType" items="${payTypeList}" varStatus="status" begin="1" end="1">
	       			<li style="margin-left: 50px;"><input type="radio" name="checkit" value="${payType.payTypeId}"/></li>
	       			<li><img src="../image/zhifubao.png" class="weixin"></li>
	       			<li>${payType.name}</li>	
				</c:forEach>-->			
	       		</ul>
	       </div>

	       <div class="martop45 ">
	       <span>商品信息</span>
	       </div>
	       <div class="blurb orange martop20">
	       		<ul class="goods_title">
	       			<li class="li1">商品名</li>
	       			<li class="li2">单价</li>
	       			<li class="li3">数量</li>
	       			<li class="li4">小计</li>
	       		</ul>
	       </div>
	       <div class="goods_content1">
		   <c:forEach var="cartStr" items="${cart}" varStatus="status">
					<c:set var="str" value="${cartStr.value}">
					</c:set>
		     <input type="checkbox" id="checkboxInput${status.count}"   name="foods"  style="display:none" value="${str.transactionId}"/>
	       	 <ul class="goods_content1_message">
	       	 	<li class="li1"><img src="${str.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left"/><span>${str.name}</span></li>
	       	 	<li class="li2">￥${str.labelMoney}</li>
	       	 	<li class="li3">${str.count}</li>
	       	 	<li class="li4">￥<fmt:formatNumber value="${str.requestMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></li>
	       	 </ul>
			 </c:forEach>
	       </div>
	       	<div class="xuanzhe martop15">
       			<input type="checkbox" style="width: 15px;height: 15px;" id="isfapiao"/>
       			<span>开具发票</span>
       			<div class="fapiao martop15" id="box_fapiao" style="display:none">
       			发票抬头：
       			<input type="text" class="input_style"  id="title" placeholder="北京盛世家和科技有限公司"/>
       		   </div>
	       	</div>
	       	<div class="xuanzhe martop15">
       			<input type="checkbox" style="width: 15px;height: 15px;" id="isdaijinquan"/>
       			<span >使用代金券</span> 
				<div id="box_daijinquan"  class="box_daijinquan  martop15" style="display:none;">
				<p>现有代金券金额：<span class="orange"><span class="balance">${totalCouponMoney}</span>元</span></p>
				<!--<p class="martop15">现有代金券张数：<span class="orange">2</span></p>-->
				<!--<p  class="martop15">请输入使用代金券金额：<span class="orange">￥</span><input type="text" id="couponing" value="0" class="orange" onkeyup="value=value.replace(/[^\d.]/g,'')"/></p>-->
       			
				
	       
	       
			<span class="begin  martop15"><a class="orange" id="addCoupon">添加代金券</a></span>
			<div class="box_coupon">
			</div>
			  	<div class="martop30">
				<input type="hidden" id="giftMoney" value="${giftMoney}">
	       		</div>
			</div>
			</div>
			<!-- <div class="xuanzhe martop15">代金券抵用金额：<span class="orange">￥200</span></div> -->
		<!-- 	<div class="xuanzhe martop15">
       			<input type="checkbox" style="width: 15px;height: 15px;" id="ischongzhi" autocomplete="off"/>
       			<span >储值券充值</span> 
				<div id="box_chongzhi"  class="box_chongzhi  martop15" style="display:none;">
			      <a class="orange" id="addChongzhi">添加储值券</a>
				  <div class="box_chongzhiquan"></div>
			   </div>
			</div> -->
			<input type="hidden" value="${orderId}" id="orderId">
	        <div class="martop15 last_box">
	        		<ul class="text_shangpinjine">
	        			<li><span class="box_left">商品金额总计：</span>
	        				<span class="orange box_right">￥ <span id="productMoney"><fmt:formatNumber value="${totalMoney}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></span>
	        			</li>
	        			<li><span class="box_left">运费总计：</span>
	        				<span class="orange  box_right">￥ <span id="fee"><fmt:formatNumber value="${empty deliveryOrder.fee.money?0.0:deliveryOrder.fee.money}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber></span></span>
	        			</li>
	        			<li><span class="box_left">代金券抵扣：</span>
	        				<span class="orange box_right" id="giftmoney">￥ <span id="couponMoney">${askUseCouponMoney}</span></span>
	        			</li>
	        			<!--<li><span class="box_left">合计：</span><span class="orange box_right">￥ ${money}</span></li>-->
					
	        			<li><span class="box_left">订单金额总计：</span>
	        				<span class="orange">￥</span><span class="orange" id="totalMoney"></span>
	        			</li>
	        			<li><input type="submit" value="提交订单" class="corange present" /></li>
	        		</ul>        	
	        </div>
	 </div>

   </div>
 	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	
	
</body>
</html>
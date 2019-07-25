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
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../js/jquery.cityselect.js"></script>
<script  type="text/javascript" src="../js/allcity.js"></script>
<script>
$(document).ready(function(){
	$("#btnReply").click(function(){
        $(".review1").toggle();
	});
})
</script>
</head>
<body>
   <header id="header2">
	    <div class="wid-80">
		   <img src="../image/logo.png" class="logo  martop30"/>
		
		   
			<ul class="btngroup martop50 box_container_right"> 
					<a href="#"><li>首页</li></a>
					<a href="#"><li>食材溯源</li></a>
					<a href="#"><li>食材库</li></a>
					<a href="#"><li>一地一品</li></a>
					<a href="#"><li>以先优选</li></a>
					<a href="#"><li>互动专区</li></a>
					<a href="#"><li>关于我们</li></a>
				</ul>
		</div>	
		
	</header>
   <div class="wid-80 martop50">
     <div class="status_right">
     <div class="status"> 
			 <span class="text_status">我的购物车</span> 
			 <span class="text_status">提交订单</span> 
			 <span class="text_status">成功提交订单</span> 
			</div>
			<div class="status">
				<div class="btn_val_gray"> 
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
				<div class="btn_val_gray"> 
				<span class="line"></span> 
				<span class="circles"></span> 
				<span class="line"></span> 
				</div>
			</div>
			
	 </div>

	 <div class="content">
	    <div class="martop20">
	    	<span >收货地址</span>
	    </div>
	    <div class="add_box martop22">
	    	 <a href="#" class="add_btn">添加收货地址</a>
	    </div>
	   <div class="message_box martop20">
	   		<form id="member_xgForm">
			 <div class="new_address1">
				<div class="addressInfo">
				   <div>
				       <span class="icon_star">*</span> 收&ensp;货&ensp;人 : <input type="text" id="contact"  name="contact"  placeholder="请输入收货人姓名"/>
				   </div>
				    <div>
				       <span class="icon_star">*</span> 区域选择 :  
					   <span class="selects">
                       <select class="prov" id="province" name="province"></select> 
    	               <select class="city" id="city" name="city" disabled="disabled"></select>    
                       </span>
				   </div>
				    <div>
				       <span class="icon_star">*</span> 详细地址 : <input type="text" placeholder=""  id="address"  name="address"/>
				   </div>
				   <div>
				       <span class="icon_star">*</span> 联系方式 : <input type="text" placeholder="请输入手机号"  id="mobile"  name="mobile" />
				   </div> 
                     <div>
				       <span class="icon_star">*</span> 邮政编码 : <input type="text" placeholder=""  id="postcode"  name="postcode" />
				   </div> 
                   <input type="hidden" value="" id="currentStatus"  name="currentStatus"  class="box_right"/>
				</div>
      			  <div class="checkBox">
  		           <input type="checkbox" value="1" id="checkboxInput"  name="" checked />
	  	           <label for="checkboxInput" id="checkBoxLabel"></label>
  	              </div>
                    <span class="setfault">设为默认地址</span>
			 </div>
			 </form>
	       </div>
	        <input type="submit" value="保存" id="adddistrict" name="adddistrict" class="btn_save"/>
	   <div class="shipping_address martop20">
	   	<span>收货地址</span>
	   </div>
	   <div class="details martop15">
	   		<ul class="message_name">
	   			<li >收货人：</li>
	   		    <li style="margin-left: 35px;">李琪琪</li>
	   			<li style="margin-left: 90px;">13682197589</li>
	   		</ul><br>
	   		<ul class="shop">
	   				<li>收货地址：</li>
	   				<li>北京市</li>
	   				<li>朝阳区</li>
	   				<li>八里庄街道</li>
	   				<li>八里庄北里小区320楼8单元203室</li>
	   			</ul>

	   			<div class="change_box">
	   				<a href="#" class="change orange">修改</a>
	   			</div>
	   </div>
	   <div class="default martop20">
	   		<div class="first_box">
	   			<img src="../image/ditu.png" class="ditu_icon"/>
	   			<input type="radio" class="radio_style1" />
	   			<div class="default_adderss">
	   				<ul class="address_consignee">
	   					<li>收货人：</li>
	   				   <li style="margin-left: 35px;">李琪琪</li>
	   				   <li style="margin-left: 90px;">13682197589</li>
	   				</ul><br>
	   				<ul class="address_specific">
	   				    <li>收货地址：</li>
	   				    <li>北京市</li>
	   				    <li>朝阳区</li>
	   				    <li>八里庄街道</li>
	   				    <li>八里庄北里小区320楼8单元203室</li>
	   			</ul>
	   			<div class="change_address">
	   				<a href="#" class="black">默认地址</a>
	   			</div>
	   			</div>
	   		</div>
	   		<div class="second_box">
	   			<input type="radio" class="radio_style2" />
	   			<div class="consignee">
	   				<ul class="goods">
	   				   <li>收货人：</li>
	   				   <li style="margin-left: 35px;">李琪琪</li>
	   				   <li style="margin-left: 90px;">13682197589</li>
	   				</ul><br>
	   				<ul class="goods_address">
	   				    <li>收货地址：</li>
	   				    <li>北京市</li>
	   				    <li>朝阳区</li>
	   				    <li>八里庄街道</li>
	   				    <li>八里庄北里小区320楼8单元203室</li>
	   			    </ul>
	   			
	   			</div>
	   			
	   		</div>
            
	   				<a href="#" class="btn_newaddress">
	   					<span class="add_icon">+</span>
	   					<span class="newbtn_text">新增地址</span>
	   				</a>
	   			
	   </div>

	   <div class="message_box martop20">
	   		<form id="member_xgForm">
			 <div class="new_address1">
				<div class="addressInfo">
				   <div>
				       <span class="icon_star">*</span> 收&ensp;货&ensp;人 : <input type="text" id="contact"  name="contact"  placeholder="请输入收货人姓名"/>
				   </div>
				    <div>
				       <span class="icon_star">*</span> 区域选择 :  
					   <span class="selects">
                       <select class="prov" id="province" name="province"></select> 
    	               <select class="city" id="city" name="city" disabled="disabled"></select>    
                       </span>
				   </div>
				    <div>
				       <span class="icon_star">*</span> 详细地址 : <input type="text" placeholder=""  id="address"  name="address"/>
				   </div>
				   <div>
				       <span class="icon_star">*</span> 联系方式 : <input type="text" placeholder="请输入手机号"  id="mobile"  name="mobile" />
				   </div> 
                     <div>
				       <span class="icon_star">*</span> 邮政编码 : <input type="text" placeholder=""  id="postcode"  name="postcode" />
				   </div> 
                   <input type="hidden" value="" id="currentStatus"  name="currentStatus"  class="box_right"/>
				</div>
      			  <div class="checkBox">
  		           <input type="checkbox" value="1" id="checkboxInput"  name="" checked />
	  	           <label for="checkboxInput" id="checkBoxLabel"></label>
  	              </div>
                    <span class="setfault">设为默认地址</span>
			 </div>
			 </form>

	       </div>
	       <input type="submit" value="保存" id="adddistrict" name="adddistrict" class="btn_save"/>
	       <div class="martop30 ">
	       	<span>支付方式</span>
	       </div>
	       <div class="thirdly_box martop10">
	       		<ul class="pay">
	       			<li><input type="radio" name="checkit"/></li>
	       			<li><img src="../image/weixin.png" class="weixin"></li>
	       			<li>微信支付</li>
	       			<li style="margin-left: 50px;"><input type="radio" name="checkit"/></li>
	       			<li><img src="../image/zhifubao.png" class="weixin"></li>
	       			<li>支付宝支付</li>		
	       		</ul>
	       </div>

	       <div class="martop45 ">
	       <span>商品信息</span>
	       </div>
	       <div class="blurb orange martop20">
	       		<ul class="goods_title">
	       			<li>商品名</li>
	       			<li>单价</li>
	       			<li>数量</li>
	       			<li>小计</li>
	       		</ul>
	       </div>
	       <div class="goods_content1">
	       	 <ul class="goods_content1_message">
	       	 	<li style="margin-top: 20px;"><img src="../image/shangpintupian.png"/></li>
	       	 	<li>大马哈鱼</li>
	       	 	<li>￥188.00</li>
	       	 	<li>2</li>
	       	 	<li>￥360.00</li>
	       	 </ul>
	       </div>
	       <div class="goods_content2">
	       	 <ul class="goods_content2_message">
	       	 	<li><img src="../image/shangpintupian.png"/></li>
	       	 	<li>大马哈鱼</li>
	       	 	<li>￥188.00</li>
	       	 	<li>2</li>
	       	 	<li>￥360.00</li>
	       	 </ul>
	       </div>
	       	<div class="xuanzhe martop15">
       			<input type="checkbox" style="width: 15px;height: 15px;"/>
       			<span>开具发票</span>
       			<div class="fapiao martop15">
       			发票抬头：
       			<input type="text" class="input_style"  placeholder="北京盛世家和科技有限公司"/>
       		   </div>
	       	</div>
	       	<div class="xuanzhe martop15">
       			<input type="checkbox" style="width: 15px;height: 15px;"/>
       			<span >使用代金券</span>
       			<span class="begin"><a href="#" class="orange">继续添加</a></span>
       			<div class="voucher martop15">
       				<span class="daijinquan">请输入代金券验证码 :</span>
       				<input type="text" class="code" />
       				<span class="mianzhi martop22">面值：￥100</span>
       		   </div>
	       	</div>
	       	<div class="martop30">
	       		<span>本次使用代金券总张数:0</span>
	       			<input type="submit" value="确认使用" class="confirm_btn" />
	       		<span class="">本次使用代金券总金额:￥0</span>
	       	</div>
	        <div class="martop15 last_box">
	        		<ul class="text_shangpinjine">
	        			<li><span class="box_left">商品金额总计：</span><span class="orange box_right">￥128.00</span></li>
	        			<li><span class="box_left">运费总计：</span><span class="orange  box_right">￥00.00</span></li>
	        			<li><span class="box_left">代金券抵扣：</span><span class="orange box_right">￥00.00</span></li>
	        			<li><span class="box_left">和计：</span><span class="orange box_right">￥00.00</span></li>
	        			<li><span class="box_left">订单金额总计：<span class="orange ">￥128.00</span></span><input type="submit" value="提交订单" class="corange present" /></li>
	        		</ul>





	        	<!-- <div class="text_shangpin">
	        		商品金额总计：<span class="orange">￥128.00</span>
	        	</div >
	        	<div class="text_yunfei">
	        		运费总计：<span class="orange">￥00.00</span>
	        	</div>
	        	<div class="text_daijinquan">
	        		代金券抵扣：<span class="orange">￥00.00</span>
	        	</div >
	        	<div class="text_total">
	        		和计：<span class="orange">￥128.00</span>
	        	</div> -->
	        	
	        	
	        </div>
	 </div>

   </div>
   <footer>
	   <div class="footerBox">
<div class="Nbg">
<P>
<span>购物指南</span><br>
<a href="/content/notice/20151125160620.shtml">购物流程</a> 
<a href="/content/notice/20151125142743.shtml">账户注册</a>
<a href="/content/notice/20151125131746.shtml">发票说明</a>
</P>
<P>
<span>支付配送</span><br>
<a href="/content/notice/20151125140740.shtml">支付方式</a>
<a href="/content/notice/20151125140740.shtml">配送方式</a> 
</P>
<P>
<span>售后服务</span><br>
<a href="/content/notice/20151125152356.shtml">常见问题</a>
<a href="/content/notice/20151125133710.shtml">投诉与建议</a> 
</P>
<P>
<span>券卡使用</span><br>
<a href="/content/notice/20151125135511.shtml">代金券消费</a> 


</P>

</div>
</div>

<div class="box_center">
<p>京ICP备：15043882号  |  营业执照号：911101053484079  |  食品经营许可证号：JY11105300065940
</p>
<p>地址： 北京市朝阳区八里庄北里129号院9号楼C座11层  |  联系方式：010-85904348
</p>
<p>©2015 北京盛世家和科技有限公司 版权所有
</p>
</div>
	</footer>
</body>
</html>
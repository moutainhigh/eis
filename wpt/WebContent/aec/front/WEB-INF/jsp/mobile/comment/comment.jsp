<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/comment.css"/>
<script type="text/javascript">
			
</script>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>写评论</span><!--<a class="home" href="/">-->
 </div>
	<div id="wrapper_1">
	   <div class="box_title">
			<div class="wid90">
				<div class="box_left martop10" >
					订单号:<span class="orange">${item.cartId}</span>
				</div>
					<div class="box_right orange martop10">
					<fmt:formatDate pattern="yyyy-MM-dd" value="${item.enterTime}" />
				</div>
			</div>
	 
	<div class="line martop20"></div> 
	<div class="wid90">
		<ul class="buy-list">		
	    <li>
		   <div class="ware_img"><img src="${item.itemDataMap.productSmallImage.dataValue}"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_name">${item.name}</span></div>
			  <div class="divCenter"> <span class="ware_from">${item.itemDataMap.goodsSpec.dataValue}</span><!--span class="all_num">×${str.count}</span--></div>
              <div class="one_sprice"><span class="price_mark">实付款￥${item.requestMoney}</span> <span class="fare">数量：<span style="color:#ff6400;">${item.count}</span></span></div>
			  </div>
		</li>
	  </ul>
	</div>
	 
	   </div>
	   <div class="box_title  martop20">
	   <div class="comment_title">
	   <ul class="comment_list">
	   	<li><input type="radio" name="comm" checked="checked">好评</li>
	   	<li><input type="radio" name="comm">中评</li>
	   	<li><input type="radio" name="comm">差评</li>
	   </ul> 
	   </div>
	   <div class="wid90">
	   	<div class="box_left"><span class="orange">*</span>心得:</div>
	   <div class="box_container_left">
			<form>
			   <p><textarea placeholder="10-500个字之间,写下购买体会" maxlength=500 id="suggestContent"></textarea></p>			  
			   </form> 
			</div>
	   </div>
	   </div>
	   <div class="wid90">
	   	<span class="orange upphoto martop10" >上传图片</span>
	   	<div class="photo martop20">
	   		<ul class="photolist">
	   			<li></li>
	   			<li></li>
	   			<li></li>
	   			<li></li>

	   		</ul>
	   	</div>
	   </div>
	    <div class="wid90">
			<div class="box_center2">
				 <input type="submit" class="btn_login" id="login" name="login" value="我要评论"/>
			</div>		
	 </div>
	</div>
</body>
</html>
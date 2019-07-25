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
<script type="text/javascript" src="/js/mobile/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/js/mobile/jquery.form.3.5.1.js"></script>
</head>
<body>   
 <script type="text/javascript" src="/js/mobile/addComment.js"></script>
 <input type="hidden" id="gallerySize" value="${fn:split(commentConfig.extraDataDefine.productGallery,'#')[1]}">
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
              <div class="one_sprice"><span class="price_mark">实付款￥${item.requestMoney}</span> <!--<span class="fare">数量：<span style="color:#ff6400;">${item.count}</span></span>--></div>
			  </div>
		</li>
	  </ul>
	</div>
	 
	   </div>
	   <form id="upload" enctype="multipart/form-data">
	   <div class="box_title  martop20" style="padding-top: 20px; padding-bottom: 20px;background:transparent;">
	   
	   <div class="comment_title">
	   <ul class="comment_list">
	   	<li><input type="radio" name="rank" checked="checked" value="1">好评</li>
	   	<li><input type="radio" name="rank" value="2">中评</li>
	   	<li><input type="radio" name="rank" value="3">差评</li>
	   </ul> 
	   </div>
	   <div class="wid90">
	   	<div class="box_left"><span class="orange">*</span>心得:</div>
	   <div class="box_container_left">
			<p><textarea placeholder="10-500个字之间,写下购买体会" name="content" maxlength=500 id="suggestContent"></textarea></p>			  
			   
		</div>
	   </div>
	   </div>
	   <div class="wid90">	   
		<div id="box_input">
			<a class="btn_addPic orange upphoto martop10" href="javascript:void(0);" ><span>添加图片</span>
                <input type="file" class="filePrew" name="productGallery0" onchange="dropHandler(this.files)" accept="image/gif,image/jpeg,image/png,image/bmp">
			</a>						
		</div>	   	
	   	<div id="dropbox">
            <ul id="pageContent">
            </ul>
            <ul id="uploaded"></ul> 
        </div>
		<input type="hidden" id="tid" value="${item.transactionId}" name="tid">
	   </div>
	    <div class="wid90">
			<div class="box_center2">
				 <input type="submit" class="btn_login" id="login" value="我要评论"/>
			</div>		
	 </div>
	 </form>
	</div>
</body>
</html>
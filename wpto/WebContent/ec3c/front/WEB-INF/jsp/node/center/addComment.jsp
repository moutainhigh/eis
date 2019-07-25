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
<link rel="stylesheet" type="text/css" href="../css/apprise.css">
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>

</head>
<body>
   <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50">
     <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox">
	   <div><a href="#">账户中心</a>><a href="#">评论</a></div>
	   <div class="BoxOrder">
		  <ul class="btnsOrder2">
		     <li class="li1">商品</li>
			 <li class="li2">单价(元)</li>
			 <li class="li3">数量</li>
			 <li class="li4">实付款(元)</li>
			 <li class="li5">交易状态</li>
			
		  </ul>
		  <ul class="orderlist fo-12">
		    
		     <li class="li1">
			  <!--<span>${order.cartId}</span>-->
			  <p class="orderNum martop10">订单编号：${item.cartId}</p>
			   <div class="productlist">
				   <img src="${item.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left"/>
				   <div class="box_container_left">
				      <p>${item.name}</p>
					  <p>${item.content}</p>
				   </div>
				 <!--<span>单价：${product.price.money}</span>-->
				</div>
				
			 </li>
			 <li class="li2  lineH120 orange">￥${item.labelMoney}</li>
			 <li class="li3 lineH120">${item.count}</li>
			<li class="li4 orange">
			     <p class="martop40">￥${item.requestMoney}</p>
				 <p>(运费￥${deliveryOrder.fee.money})</p>
			 </li>
			 <li class="li5">
			   <p class="martop10">${item.currentStatusName}</p>
			 
			     <p class="orange"><a href="#">查看物流</a></p>
				 <p class="orange"><a href="#">订单详情</a></p>
	
			 </li>
			<input type="hidden" id="tid" value="${item.transactionId}"/>
		  </ul>
		  
		  <div class="box_apprise martop30">
		  <input type="hidden" value="${fn:replace(commentConfig.extraDataDefine.productGallery,'gallery#','')}" id="maxImgLength">
       <form id="upload" enctype="multipart/form-data">
		      <p>
          <span><input type="radio" name="rank" checked=true value="1"/>好评</span>
          <span><input type="radio" name="rank" value="2"/>中评</span>
          <span><input type="radio"name="rank" value="3"/>差评</span>
          </p>
			  <p class="martop10">
			     <span class="box_container_left"><span class="orange">*</span>心得：</span>
				 <textarea name="content" class="martop10" cols="50" rows="5" id="textarea" maxlength="500" placeholder="长度在10-500个字以内写下购物体会或者使用过程中带来的帮助等，可以为其他小伙伴提供参考"></textarea>
			  </p>
			  
			   <div id="uploadready" class="marleft75 martop30">
                         <div id="box_input">
						 <a class="btn_addPic" href="javascript:void(0);"><span>添加图片</span>
                        <input type="file" class="filePrew" name="productGallery0"  onchange="dropHandler(this.files)" accept="image/gif,image/jpeg,image/png,image/bmp" class="blue" />
						 </a>
						</div>
                        <div id="dropbox">
                           <ul id="pageContent">
                           </ul>
                           <ul id="uploaded"></ul> 
                        </div>

                  
               </div> 
			    <input type="hidden" id="tid" value="${item.transactionId}" name="tid">
			    </form>
              

			  <p><a class="orange marleft75 martop10" onclick="uploadApprise()">提交评价</a></p>
		  </div>
	   </div>
	 </div>
  </div>
</body>
</html>

<script type="text/javascript" src="../../js/addComment.js"></script>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}-商品评价</title>
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/theme/${theme}/js/common.js" src="js/user.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>
</head>
<style>
input[type='file']{
	    position: relative;
    top: 0;
    left: -75px;
    opacity: 0;
}
#uploadready span{
	    display: inline-block;
    background-color: #eee;
    padding: 2px 9px;
    border-radius: 3px;
    letter-spacing: 2px;
        border: 1px solid #B7B7B7;
}
textarea{
	    width: 550px;
    height: 150px;
    margin: 10px 0px;
    border: 2px solid #DADADA;
    padding: 4px;
}
	.plicon{
		width: 20px;
	    height: 18px;
	    display: inline-block;
	    vertical-align: middle;
	    overflow: hidden;
	   margin: 0 25px 0 3px;
}
.plicon1{
 background: transparent url(/theme/${theme}/images/pinglunicon.png) no-repeat 0 -100px;
}
.plicon2{
 background: transparent url(/theme/${theme}/images/pinglunicon.png) no-repeat 0 -50px;
}
.plicon3{
 background: transparent url(/theme/${theme}/images/pinglunicon.png) no-repeat 0 0;
}
.boxCenterList li img{
	    height: 50px;width: auto;
    border: 3px solid #C3C3C3;
}

input.submitbtn{
	background-color: #333;
    color: #fff;
    border: none;
    padding: 6px 12px;
    border-radius: 3px;
}
.boxCenterList li{
	width: auto!important;position: relative;
	margin: 10px 10px 20px 0;
}

.boxCenterList li>span{
	position: absolute;top: 0;left: 0;
	display: inline-block;
	width: 100%;height: 100%;
	text-align: center;
	line-height: 50px;
	padding: 0!important;
	color: #fff;
	background-color: rgba(0,0,0,0.65)!important;
	opacity: 0;
	cursor: pointer;
}
.boxCenterList li:hover span{
opacity: 1;
}
	}
</style>
<body>
   <%@include file="/WEB-INF/jsp/include/head.jsp" %>
   <div class="block box">
	<div class="blank"></div>
	 <div id="ur_here">
	当前位置: <a href="/">首页</a> <code>&gt;</code> 用户中心 
	</div>
	</div>
<div class="blank"></div>

<div class="block clearfix">
  
  <div class="AreaL">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox">
        <div class="userMenu">
<a href="/content/center/center.shtml"><img src="/theme/${theme}/images/u1.gif"> 欢迎页</a>
<a href="/content/center/userMessage.shtml"><img src="/theme/${theme}/images/u2.gif"> 用户信息</a>
<a href="/content/center/myOrders.shtml" class="curs"><img src="/theme/${theme}/images/u3.gif"> 我的订单</a>
<a href="/content/center/myAddress.shtml"><img src="/theme/${theme}/images/u4.gif"> 收货地址</a>
<a href="/content/center/myLove.shtml"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
<a href="/content/center/myWords.shtml" ><img src="/theme/${theme}/images/u6.gif"> 我的留言</a>
<a href="/content/center/myLabel.shtml"><img src="/theme/${theme}/images/u7.gif"> 我的标签</a>
<a href="/content/center/shortsSupply.shtml"><img src="/theme/${theme}/images/u8.gif"> 缺货登记</a>
<a href="/content/center/myPacket.shtml"><img src="/theme/${theme}/images/u9.gif"> 我的红包</a>
<a href="/content/center/myRecommend.shtml"><img src="/theme/${theme}/images/u10.gif"> 我的推荐</a>
<a href="/content/center/myComments.shtml"><img src="/theme/${theme}/images/u11.gif"> 我的评论</a>
<!--<a href="user.php?act=group_buy">我的团购</a>-->
<a href="/content/center/trackingPackages.shtml"><img src="/theme/${theme}/images/u12.gif"> 跟踪包裹</a>
<a href="/content/center/fundManagement.shtml"><img src="/theme/${theme}/images/u13.gif"> 资金管理</a>
<a href="/" style="background:none; text-align:right; margin-right:10px;"><img src="/theme/${theme}/images/bnt_sign.gif" onclick="secede()"></a>
</div>      </div>
     </div>
    </div>
  </div>
  <div class="AreaR">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox boxCenterList clearfix" style="_height:1%;" id="notLogin">
             <h5><span>购买详情</span></h5>
                <div class="blank"></div>
                
                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
                    <thead>
                       <th bgcolor="#ffffff">商品</th>
                       <th bgcolor="#ffffff">单价</th>
                       <th bgcolor="#ffffff">数量</th>
                       <th bgcolor="#ffffff">实付款</th>
                    </thead>
                    <tr align="center">
                        <td bgcolor="#ffffff">
                        	<img style="width: 100px;max-height: 100px;" src="/file/${item.itemDataMap.get('productSmallImage').dataValue}" class="box_container_left"/>
						   <div class="box_container_left">
						      <p>${item.name}</p>
							  <p>${item.content}</p>
						   </div>
                        </td>
                        <td bgcolor="#ffffff">￥${item.labelMoney}</td>
                        <td bgcolor="#ffffff">${item.count}</td>
                        <td bgcolor="#ffffff">
                        	<p class="martop40">￥${item.requestMoney}</p>
				 			<p>(运费￥${deliveryOrder.fee.money}
				 				<c:if test="${empty deliveryOrder.fee.money}">
				 				0
				 				</c:if>
				 				)</p>
                        </td>
                        
                    </tr>
                </table>
                  <h5 style="margin-top:20px;"><span>评价商品</span></h5>
                <div class="blank"></div>
      <input type="hidden" id="tid" value="${item.transactionId}"/>
		  <div class="box_apprise martop30">
		  <input type="hidden" value="${fn:replace(commentConfig.extraDataDefine.productGallery,'gallery#','')}" id="maxImgLength">
       <form id="upload" enctype="multipart/form-data"  onsubmit="return false">
		      <p>
          <span><input type="radio" name="rank" checked=true value="1"/>好评<span class="plicon plicon1" ></span></span>
          <span><input type="radio" name="rank" value="2"/>中评<span class="plicon plicon2"></span></span>
          <span><input type="radio"name="rank" value="3"/>差评<span class="plicon plicon3"></span></span>
          </p>
			  <p class="martop10">
				 <textarea name="content" class="martop10" cols="50" rows="5" id="textarea" maxlength="500" placeholder="长度在10-500个字以内写下购物体会或者使用过程中带来的帮助等，可以为其他小伙伴提供参考"></textarea>
			  </p>
			  
			   	<div id="uploadready" class="marleft75 martop30"><!--productGallery0-->
					<div class="box_input" style="margin-bottom: 10px;" id="box_input_productGallery">
						<a class="btn_addPic inputa" href="javascript:void(0);"><span>晒照片</span>
						<input type="file" class="filePrew" name="productGallery" id="productGallery" value=""  class="blue"  data-name="productGallery"/>                  
						</a>
					</div>

					<div class="dropbox">
						<ul class="pageContent clearfix" id="pageContent_productGallery">
						</ul>
					</div>
				</div>
			    <input type="hidden" id="tid" value="${item.transactionId}" name="tid">
			    <p style="display:inline-block;width:550px;text-align:center;"><input type="button" value="提交评论" class="submitbtn" onClick="uploadApprise()"/></p>
			    </form>
              

			  
		  </div>     		


    </div>
      </div>
     </div>
    </div>
  </div>
<div class="blank"></div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
<script type="text/javascript" src="/theme/${theme}/js/addComment.js"></script>





</body>
</html>


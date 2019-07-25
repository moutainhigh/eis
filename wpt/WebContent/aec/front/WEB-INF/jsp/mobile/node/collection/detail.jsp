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
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/productdetail.css"/>
<link rel="stylesheet" href="../../../../css/mobile/jquery.spinner.css" />
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/jquery.spinner.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/jquery.slides.min.js"></script>
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../../js/mobile/productdetail.js"></script>
<script type="text/javascript" src="../../../../js/mobile/share.js"></script>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>一地一品</span><a class="list1" onclick="vanish()"></a>
 </div>
	<div id="wrapper_1">
			<%@include file="/WEB-INF/jsp/include/BtnList.jsp" %>
	   <div class="cont wid90">
      <div class="container">
         <div id="slides">
         <c:if test="${!empty document.documentDataMap.get('goodsShowImg1').dataValue}">
            <img src="${document.documentDataMap.get('goodsShowImg1').dataValue}" alt="">
          </c:if> 
          
          <c:if test="${!empty document.documentDataMap.get('goodsShowImg2').dataValue}">
            <img src="${document.documentDataMap.get('goodsShowImg2').dataValue}" alt="">
          </c:if> 
            
            <c:if test="${!empty document.documentDataMap.get('goodsShowImg3').dataValue}">
            <img src="${document.documentDataMap.get('goodsShowImg3').dataValue}" alt="">
          </c:if> 
         
          </div>
      </div>
      <div class="con_a">
	  <div  class="ware_name">${fn:substring(document.title ,0,14)}${fn:length(document.title)>14?"..":""}&nbsp;<c:if test="${document.documentDataMap.get('availableCount').dataValue=='0'}"><span style="color:#ccc">(已售罄)</span></c:if></div>
	    <div class="box_left">   
		  <span class="one_price">单价：<span style="color:#ff6400" id="productprice">￥ ${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,'money:','')}</span>
          <span  class="ware_where">产地：${document.documentDataMap.get("goodsProvenance").dataValue}&nbsp&nbsp&nbsp</span>  
          <span  class="ware_where">规格：${document.documentDataMap.get("goodsUnit").dataValue}&nbsp&nbsp&nbsp</span> 
          <span  class="ware_where">数量：</span>
		 <div class="box_right">
          <div class="btn_group"> 
         <input type="text" class="spinner"/>
          <input type="hidden" id="documentCode" value="${document.documentCode}"/>
           <input type="hidden" id="productCode" value="${document.documentDataMap.get('productCode').dataValue}"/>
         </div>
        </div>
         <span class="con_b">发货地：${document.documentDataMap.get("defaultFromArea").dataValue}</span>
      </div>
	  </div>
	  <div class="detailbtns">
	     <div id="cpxq" class="btn_detail current" >产品详情</div>
		 <div id="yhpj" class="btn_detail">产品评价</div>
      </div> 
      <div class="detail_con cpxq" style="display: block;">
	   <div class="detail_con_text">
       ${document.content}
	   <div class="linellae"></div>	
			<div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
			<div class="linellae1"></div>
	   </div>
      </div>
       <div class="detail_con yhpj" style="display:none">
       <ul class="buy-list wid90">
	   <c:forEach var="comment" items="${commentList}">
		<c:forEach var="i" items="${comment}" varStatus="status">
		<c:if test="${status.index==0}">
	    <li>
		</c:if>
		<c:if test="${status.index!=0}">
		<li style="margin-left:8%;">
		</c:if>
		   <div class="ware_img">
			<c:choose>
			  <c:when test="${!empty i.data.userHeadPic}">
				<c:if test="${fn:indexOf(i.data.userHeadPic,'http://')!=-1}">
					<img src="${i.data.userHeadPic}">
				</c:if>
				<c:if test="${fn:indexOf(i.data.userHeadPic,'http://')==-1}">
					<img src="/static/userUploadDir/${i.data.userHeadPic}">
				</c:if>
			  </c:when>
			  <c:otherwise>
				<img src="../../image/header.png">
			  </c:otherwise>
			</c:choose>		   
		   </div>
		   <div class="box_right2">
			  <div> <span class="ware_names">${i.data.userRealName}</span><span ><img src="../../../../image/mobile/header/preview_unlike_icon1.png" style="width: 25px;height: 25px;float: right;"></span></div>
			  <div class="divCenter"> <span class="ware_from">${i.title}:${i.content}</span></div>
              <div class="one_sprice"><span class="price_mark"><fmt:formatDate value="${i.publishTime}"  type="both"/></span> <span class="fare orange" style="float:right;display:none;">删除</span></div>
			  </div>
		</li>
		 </c:forEach>
		</c:forEach>
	  </ul>

	  	 <div class="linellae"></div>	
			    <div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
			   <div class="linellae1"></div>
      </div>

  </div>	 
	  	
  </div>

   <div class="btns_over">
      <c:choose>
      <c:when test="${document.documentDataMap.get('availableCount').dataValue!='0'}">
      <div class="btn_over">
      <input type="button" value="立即购买" class="buy_now"/>
         <input type="button" value="加入购物车" class="addtocard"/>
         
      </div>
	  </c:when>
      <c:otherwise> 
       <div class="btn_over_gray">
        <input type="button" value="立即购买" class="buy_now_gray" style="background-color:#ccc"/>
         <input type="button" value="加入购物车" class="addtocard_gray"/>
        
      </div> 
	 </c:otherwise>   
      </c:choose>
      </div>
</body>
</html>    
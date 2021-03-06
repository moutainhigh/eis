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
<link rel="stylesheet" href="../../../../css/mobile/jquery.spinner.css?v=02201431" />
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/swiper.min.css"/>
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/jquery.spinner.js"></script> 
<script type="text/javascript"  charset="utf-8" src="../../../../js/mobile/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../../js/mobile/productdetail.js"></script>
</head>
<body>
     <%@include file="/WEB-INF/jsp/include/header.jsp" %>
	<div id="wrapper_1">
		
	   <div class="cont wid90">
      <div class="swiper-container">
	    <div class="swiper-wrapper">
         <c:if test="${!empty document.documentDataMap.get('productGallery').dataValue}">
			<c:forEach items="${document.documentDataMap.get('productGallery').dataValue.split(',')}" var="i">
				<div class="swiper-slide"><img src="/static/productFile/open/${i}" alt=""></div>
			</c:forEach>
          </c:if> 
          </div>
	   <div class="swiper-pagination"></div>
	   </div>
	   <script language="javascript"> 
			var mySwiper = new Swiper('.swiper-container',{
			pagination : '.swiper-pagination',
			autoplay : 2000,
			paginationClickable :true
			})
		</script>
      <div class="con_a">
	  <div  class="ware_name">${fn:substring(document.title ,0,14)}${fn:length(document.title)>14?"..":""}&nbsp;<c:if test="${document.documentDataMap.get('availableCount').dataValue=='0'}"><span style="color:#ccc">(已售罄)</span></c:if></div>
	    <div class="box_left">   
		  <p><span class="one_price"><span class="textTh">￥ ${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,'money:','')}</span><span style="color:#ff6400" id="productprice">￥ ${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,'money:','')}</span><span class="right">已售：${document.documentDataMap.get("soldCount").dataValue}</span></p>
          <span  class="ware_where">产地：${document.documentDataMap.get("productOrigin").dataValue}&nbsp&nbsp&nbsp</span>  
          <span  class="ware_where">规格：${document.documentDataMap.get("goodsSpec").dataValue}&nbsp&nbsp&nbsp</span> 
		 <div class="box_right">
          <div class="btn_group"> 
          <input type="hidden" id="documentCode" value="${document.documentCode}"/>
           <input type="hidden" id="productCode" value="${document.documentDataMap.get('productCode').dataValue}"/>
         </div>
        </div>
         <span class="con_b">发货地：${document.documentDataMap.get("deliveryFromArea").dataValue}<span style="font-size:12px;"></span></span>
      </div>
	  </div>
	 <!--  <div class="detailbtns">
	     <div id="cpxq" class="btn_detail current" >产品详情</div>
		 <div id="yhpj" class="btn_detail">产品评价</div>
      </div>  -->
      <div class="detail_con cpxq" >
	   <div class="detail_con_text">
       ${document.content}
	       <!-- <div class="linellae"></div>	
			<div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
			<div class="linellae1"></div> -->
	   </div>
      </div>
      <div class="goods_comm">商品评论</div>
       <div class="detail_con yhpj" >
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
			  <div> <span class="ware_names"><!-- ${i.data.userRealName} --><c:set var="l" value="${fn:length(i.data.userRealName)}"></c:set>			
				${fn:substring(i.data.userRealName,0,1)}***${fn:substring(i.data.userRealName,l-1,l)}
			  </span></div>
			  <div class="divCenter"> <span class="ware_from">${i.title}${i.content}</span></div>
              <div class="one_sprice"><span class="price_mark"><fmt:formatDate value="${i.publishTime}"  type="both"/></span> <span class="fare orange" style="float:right;display:none;">删除</span></div>
			  </div>
		</li>
		 </c:forEach>
		</c:forEach>
		<c:if test="${fn:length(commentList)==0||empty commentList}">
		   <p style="text-align:center">此商品暂无评价</p>
		</c:if>
	  </ul>

	  	       <!--  <div class="linellae"></div>	
			    <div class=" write_comment"><span style="margin:0 auto;color: #cccccc">已经到底啦</span></div>
			    <div class="linellae1"></div> -->
      </div>

  </div>	 
	  	
  </div>
<div class="fixDiv">
     <a href="/cart.shtml">
     <div class="left">
	    <img src="../../../../image/mobile/footer/cart.png">
		<span class="${empty cartaCount?'':'cartIcon'}">${empty cartaCount?'':cartaCount}</span>
	 </div>
	 </a>
	 <div class="right">
	        <c:if test="${empty productCount||productCount==0||productCount==null}">				
				<c:choose>
      			<c:when test="${result.operateCode==500080||result.operateCode==500072}">
					<a class="btn_buy2" >已结束</a>
	  			</c:when> 
				<c:when test="${document.documentDataMap.get('availableCount').dataValue=='0'&&result.operateCode!=500080&&result.operateCode!=500072}">
					<a class="btn_buy2" >已售罄</a>
	  			</c:when>
				<c:otherwise> 
				    <a class="btn_buy" id="buyNow">加入购物车</a>
	            </c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${!empty productCount&&productCount!=0&&productCount!=null}">
	 	    <c:choose>
      			<c:when test="${result.operateCode==500080||result.operateCode==500072}">
					<a class="btn_buy2" >已结束</a>
	  			</c:when> 
				<c:when test="${document.documentDataMap.get('availableCount').dataValue=='0'&&result.operateCode!=500080&&result.operateCode!=500072}">
					<a class="btn_buy2" >已售罄</a>
	  			</c:when> 
				<c:otherwise> 
				<div class="spinner" >
					<a class="decrease" onClick="decrease(this,'${transaId}')">-</a>
					<input type="text" class="spinner value passive" value="${empty productCount?0:productCount}" id="productcount" maxlength="2" oninput="changevalue(this.value,'${transaId}')"  >
					<a class="increase" onClick="increase(this,'${transaId}')">+</a>
				</div>
	            </c:otherwise>
            </c:choose>
			</c:if>
			
	</div>
</div>
</body>
</html>    
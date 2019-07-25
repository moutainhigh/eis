<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css?v=0701121438"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/index.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/productdetail.css"/>
<script  type="text/javascript" src="../../../../js/jquery-1.11.3.min.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../../js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../../js/mobile/lazyload.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../../js/mobile/home.js?v=678"></script>

</head>
<body>
   <%@include file="/WEB-INF/jsp/include/header.jsp" %>
   <div id="wrapper_1">
		  <div class="navbar navbar1 mt5" id="hotNav">
		     <p><span class="orange">•火•热•预•售•</span></p>
		  </div>
		   <!--<div class="navbar navbar1 mt5" id="shopNav">
		     <p><span class="orange">•以•先•商•城•</span></p>
		  </div>
			<ul class="productList" id="productList">
				<c:forEach var="suggest" items="${suggestList}" varStatus="status">
					<c:if test="${suggest.documentTypeCode!= 'image'}" >
				<li>
				<a href="${suggest.viewUrl}">
				<img data-original="${suggest.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/mobile/listImgDefault.png" class="lazy"/>
				<div class="p-info">
					<div class="left">
					  <p>${suggest.title}</p>
					  <p><span class="old_price">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span><span class="orange">￥${fn:replace(suggest.documentDataMap.get('productBuyMoney').dataValue,"money:","￥")}</span></p>
					</div>
					<span class="addToCart right"></span>
				</div>
				<span class="no-stock">已售罄</span>
				</a>
			   </li>
				 </c:if>
				</c:forEach>
			 </ul>-->
			 <ul class="box_huoreyushou" id="box_huoreyushou">
			<fmt:formatDate value="<%=new java.util.Date()%>" type="both" dateStyle="long" pattern="yyyy/MM/dd HH:mm:ss" var="now"/>
			<c:forEach var="tuan" items="${newsList}" varStatus="status">
			<c:if test="${tuan.documentTypeCode!= 'image'}" >
			  <c:set value="${tuan.documentDataMap.get('timeCurrent').dataValue}" var="timeCurrent"></c:set>
			  <li class="mt5">
			   <a href="${tuan.viewUrl}">
			     <div class="activitylist">
			     <c:choose>
				 <c:when test="${timeCurrent==500072}">
				    <div class="statusBox">
				    <span class="iconOver">已结束</span>
					</div>
				 </c:when>
				 <c:when test="${timeCurrent==500080}">
				    <div class="timeBox"  id="colorBox${status.index}">
					<p>开始倒计时</p>
				    <p><span class="day">0</span>天</p>
				    <p><span class="hour">0</span>:<span class="minute">0</span>:<span class="second">0</span></p>
					</div>
					<script>var timer=new Timer("${tuan.documentDataMap.get('activityBeginTime').dataValue}",'#colorBox${status.index}',"${now}");timer.start();</script>
				 </c:when>
				 <c:otherwise>
				    <div class="statusBox">
				    <span class="iconIng">进行中</span>
					</div>
				 </c:otherwise>
				 </c:choose>
				 </div>
			   <img data-original="${tuan.documentTypeCode=='activity'?tuan.documentDataMap.get('activitySmallImage').dataValue:tuan.documentDataMap.get('productSmallImage').dataValue}" src="../../../image/mobile/listImgDefault.png" class="lazy"/>
			   <div class="product-info">                           
                    <p style="min-height:40px;">${tuan.title}</p>
					<p class="">净重：${tuan.documentDataMap.get('goodsSpec').dataValue}</p>
					<p class="" >原价：<span class="fo13 old_price">￥${tuan.documentDataMap.get('marketPrice').dataValue}</span></p>              
					<p class="price orange">${fn:replace(tuan.documentDataMap.get('productBuyMoney').dataValue,"money:","￥")}</p>				
                </div>
				</a>
				<span class="no-stock">已售罄</span>
			</li> 
			 </c:if>
			</c:forEach>
			</ul>
 </div>
    <%@include file="/WEB-INF/jsp/include/footer.jsp" %>
	<script type="text/javascript">
/*$(function(){	
	switch(testUrl("subtitle")){
		case 'presale':
			$('#productList').css('display','none');
			$('#box_huoreyushou').css('display','block');
			$('#shopNav').css('display','none');
			$('#hotNav').css('display','block');				
		break;
		case 'suggest':     
			$('#productList').css('display','block');
			$('#box_huoreyushou').css('display','none');	
			$('#shopNav').css('display','block');
			$('#hotNav').css('display','none');
		break;		
		default:     
			$('#productList').css('display','none');
			$('#box_huoreyushou').css('display','block');
			$('#shopNav').css('display','none');
			$('#hotNav').css('display','block');	 	
	}
})*/
function delCollect(e){
			$.ajax({
            type:"POST",
            url: '/userRelation/delete.json',
            data:{
               userRelationId:e
            },
            dataType:'json', 

            success:function (data) {
                alert("删除成功!");
            },
            error:function (data) {
                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		}
</script>
</body>
</html>
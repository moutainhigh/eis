<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css?v=0702101805"/>  
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/index.css?v=0702101805"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/swiper.min.css"/>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/home.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../theme/${theme}/js/mobile/swiper.3.1.2.jquery.min.js"></script>
</head>
<style>
	#coupon>ul li.widthculen{
		width: 115px!important;
		    margin: 0 3px!important;
	}
	#coupon{
		overflow: auto;
	}
	#coupon>ul li >div p{
		margin-top: 0!important;
	}
	.my-swiper-pagination{
		display: none;
	}
	.successcubox{
	    position: fixed;
	    top: 50%;
	    left: 50%;
	    width: 100px;
	    height: 40px;
	    margin-left: -50px;
	    margin-top: -20px;
	    background-color: rgba(0, 0, 0, 0.7);
	    color: #fff;
	    text-align: center;
	    line-height: 40px;
	    border-radius: 5px;
	    box-shadow: 0 0 60px #FFF;
	    transition: all 1s ease;
	    -webkit-transition: all 1s ease;
	    opacity: 0;
	}
	.successcubox.show{
		opacity: 1;
	}
	.successcubox.small{
		transform: scale(0.8);
		-webkit-transform: scale(0.8);
		/*opacity: 0;*/
	}
</style>
<body>
	<%@include file="/WEB-INF/jsp/include/mheader.jsp" %>

	<div id="wrapper" >
	<div id="scroller">
	   <div class="swiper-container" id="box_search">
	    <div class="swiper-wrapper">
		<c:set var="num" value="0"></c:set>
				<c:forEach items="${newsList}" var="list" varStatus="i">
 				<c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						<div class="swiper-slide"><a href="${list.redirectTo }"><img alt="" src="${list.documentDataMap.get('bannerImage').dataValue} "> </a></div>
			          </c:if>
					</c:if> 
				</c:forEach>
		<!-- 例子 -->
		<div class="swiper-slide"><a href="#"><img alt="" src="../../../theme/${theme}/image/mobile/banner.jpg"> </a></div>
		</div> 
        <div class="my-swiper-pagination swiper-pagination-custom"></div> 
	   </div>
	   <script type="text/javascript"> 
			var mySwiper = new Swiper('.swiper-container',{
			pagination : '.my-swiper-pagination',
			paginationType : 'custom',
			loop:true,
			autoplay : 4000,
			paginationClickable :true,
			bulletClass : 'my-bullet',
			bulletActiveClass : 'my-bullet-active'
			})
			var w=(100-($(".my-swiper-pagination .my-bullet").length-1))/$(".my-swiper-pagination .my-bullet").length;
			$(".my-bullet").each(function(){
				$(this).css("width",w+"%");
            });
		</script>
			<div id="coupon">
				<ul class="clearfix">
					<li>
						<div>
							<span>5</span>元
							<p>单笔满99元使用</p>
							<em>领</em>
						</div>
					</li>
					<li>
						<div>
							<span>25</span>元
							<p>单笔满199元使用</p>
							<em>领</em>
						</div>
					</li>
					<li>
						<div>
							<span>25</span>元
							<p>单笔满199元使用</p>
							<em>领</em>
						</div>
					</li>
					<li>
						<div>
							<span>25</span>元
							<p>单笔满199元使用</p>
							<em>领</em>
						</div>
					</li>
				</ul>
			</div>
			<!-- 领域成功弹出 -->
			<div class="successcubox">
				<span>领取成功</span>
			</div>
	   <ul class="box mt5" id="menulist">
	     <li>
		    <a href="<c:url value='/content/meat/index.shtml' />">
			   <!-- <p><img src="/image/mobile/xiuxian.png"></p> -->
			   <p>农家肉</p><span>[Peasant meat]</span>
			</a>
		  </li>
		  <li>
		    <a href="<c:url value='/content/egg/index.shtml' />">
			   <p>农家蛋</p><span>[Farm eggs]</span>
			</a>
		  </li>
		  <li>  
		    <a href="<c:url value='/content/fruit/index.shtml' />">
			   <p>农家果</p><span>[Fruit farm]</span>
			</a>
		  </li>
		  <li>
		    <a href="<c:url value='/content/meat/index.shtml' />">
			   <!-- <p><img src="/image/mobile/xiuxian.png"></p> -->
			   <p>全部食品</p><span>[All]</span>
			</a>
		  </li>
		 <!--  <li>
		    <a href="/content/product/sushi/index.shtml">
			   <p><img src="/image/mobile/sushi.png"></p>
			   <p>溯食同源</p>
			</a>
		  </li> -->
		  </ul>
		  <c:if test="${fn:length(tuanList)!=0}">
		  <div class="navbar navbar1 mt5">
		     <p><span class="orange">•火•热•预•售•</span><a href="/content/product/tuan/index.shtml" class="fo12">更多&gt;</a></p>
		  </div>
		  </c:if>
		   <ul class="box_huoreyushou" id="box_huoreyushou">
			<fmt:formatDate value="<%=new java.util.Date()%>" type="both" dateStyle="long" pattern="yyyy/MM/dd HH:mm:ss" var="now"/>
			<c:forEach var="tuan" items="${tuanList}" varStatus="status" begin="0" end="0">
			<c:if test="${tuan.documentTypeCode!= 'image'}" >
			<fmt:parseDate  value="${tuan.documentDataMap.get('activityBeginTime').dataValue}" type="both" pattern="yyyy-MM-dd HH:mm:ss" var="begintimestr"/>
			<fmt:formatDate value="${begintimestr}"  dateStyle="long" pattern="yyyy/MM/dd HH:mm:ss" var="begintime"/>
			<fmt:parseDate  value="${tuan.documentDataMap.get('activityEndTime').dataValue}" type="both" pattern="yyyy-MM-dd HH:mm:ss" var="endtimestr"/>
			<fmt:formatDate value="${endtimestr}"  dateStyle="long" pattern="yyyy/MM/dd HH:mm:ss" var="endtime"/>	
			<li class="mt5">
			   <a href="${tuan.viewUrl}">
			     <div class="activitylist">
			     <c:choose>
				 <c:when test="${now>endtime}">
				    <div class="statusBox">
				    <span class="iconOver">已结束</span>
					</div>
				 </c:when>
				 <c:when test="${now<begintime}">
				    <div class="timeBox"  id="colorBox${status.index}">
					<p>开始倒计时</p>
				    <p><span class="day">0</span>天</p>
				    <p><span class="hour">0</span>:<span class="minute">0</span>:<span class="second">0</span></p>
					</div>
				 </c:when>
				 <c:otherwise>
				    <div class="statusBox">
				    <span class="iconIng">进行中</span>
					</div>
				 </c:otherwise>
				 </c:choose>
				 </div>
			   <img data-original="${tuan.documentTypeCode=='activity'?tuan.documentDataMap.get('activitySmallImage').dataValue:tuan.documentDataMap.get('productSmallImage').dataValue}"  class="lazy"/>
			   <div class="product-info">                           
                    <p style="min-height:40px;">${tuan.title}</p>
					<p class="">净重：${tuan.documentDataMap.get('goodsSpec').dataValue}</p>
					<p class="" >原价：<span class="fo13 old_price">￥${tuan.documentDataMap.get('marketPrice').dataValue}</span></p>              
					<p class="price orange">${fn:replace(tuan.documentDataMap.get('productBuyMoney').dataValue,"money:","￥")}</p>				
                </div>
				</a>
				<span class="no-stock">已售罄</span>
			</li>
			<script>if(${now<begintime}){var timer=new Timer("${begintime}",'#colorBox${status.index}',"${now}");timer.start()};</script>
			 </c:if>
			</c:forEach>
			</ul>
			<div class="swiper-container swiper-container1 mt5">
				<div class="swiper-wrapper">
				<c:forEach items="${tegongList}" var="document" varStatus="i">
						<div class="swiper-slide"><a href="${document.viewUrl}"><img alt="" src="${document.documentDataMap.get('productSmallImage').dataValue}"> </a></div>
				</c:forEach>
				</div>
			</div>
			<script type="text/javascript"> 
				var mySwiper = new Swiper('.swiper-container1',{
					paginationClickable :true,
					slidesPerView: 2.5,
					spaceBetween: 5
			})
		  </script>
		  <c:forEach items="${huodongquList}" begin="0"  end="3" var="document" varStatus="i">
			<a href="${document.viewUrl}"><img  data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  class="lazy mt5"/></a>
		  </c:forEach>
		  <c:forEach var="tuan" items="${rexiaoList}" begin="0"  end="2" varStatus="status">
		  <a href="${tuan.viewUrl}">
		  <div class="list_hot mt5">
		     <img data-original="${tuan.documentTypeCode=='activity'?tuan.documentDataMap.get('activitySmallImage').dataValue:tuan.documentDataMap.get('productSmallImage').dataValue}"   class="lazy">
			 <div class="product-info">                           
                    <p>${tuan.title}</p>
					<p>${tuan.documentDataMap.get("subtitle").dataValue==''||empty tuan.documentDataMap.get("subtitle").dataValue?'一地一品 实惠热销':tuan.documentDataMap.get("subtitle").dataValue} </p>
					<p class="">净重：${tuan.documentDataMap.get('goodsSpec').dataValue}</p>
					<p class="" >原价：<span class="fo13 old_price">￥${tuan.documentDataMap.get('productMarketPrice').dataValue}</span></p>              
					<p class="price orange">${fn:replace(tuan.documentDataMap.get('productBuyMoney').dataValue,"money:","￥")}</p>				
             </div>
			 <div class="hot">
				 <span>热销</span>
				 <div class="addToCart"></div>
				 <input type="hidden" value="${tuan.documentDataMap.get('productCode').dataValue}" id="productCode">
			 </div>
		  </div>	
			</a>		  
		  </c:forEach>
		<!-- 商品 -->
		   <div class="navbar navbar1 mt5">
		     <p><span class="orange">农家肉</span><a href="/content/meat/index.shtml" class="fo12">更多&gt;</a></p>
		  </div>	
		  <ul class="productList">
		    <c:forEach var="document" items="${meatList}" varStatus="status" begin="0" end="3">
			<li>
			   <a href="${document.viewUrl}">
				<img data-original="/file/${document.documentDataMap.get('productSmallImage').dataValue}"  class="lazy"/>
				<div class="p-info">
					<div class="left">
					  <p>${document.title}</p>
					  <p><span class="old_price">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span><span class="orange">￥${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span></p>
					</div>
					<span class="addToCart right">加入购物车</span>
					<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
				</div>
				<c:if test="${document.documentDataMap.get('availableCount').dataValue==0}">
					<span class="no-stock">已售罄</span>
				</c:if>
				</a>
			</li>
			</c:forEach>
		  </ul> 
		  <div class="navbar navbar1 mt5">

		     <p><span class="orange">农家蛋</span><a href="/content/egg/index.shtml" class="fo12">更多&gt;</a></p>
		  </div>	
		  <ul class="productList">
			 <c:forEach var="document" items="${eggList}" varStatus="status" begin="0" end="3">
			<li>
			   <a href="${document.viewUrl}">
				<img data-original="/file/${document.documentDataMap.get('productSmallImage').dataValue}"  class="lazy"/>
				<div class="p-info">
					<div class="left">
			      	<p>${document.title}</p>
					  <p><span class="old_price">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span><span class="orange">￥${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span></p>
					</div>
					<span class="addToCart right">加入购物车</span>
					<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
				</div>
				<c:if test="${document.documentDataMap.get('availableCount').dataValue==0}">
					<span class="no-stock">已售罄</span>
				</c:if>
				</a>
			</li>
			</c:forEach>
		  </ul>
			<div class="navbar navbar1 mt5">
		     <p><span class="orange">农家果</span><a href="/content/fruit/index.shtml" class="fo12">更多&gt;</a></p>
		  </div>	
		  <ul class="productList">
			 <c:forEach var="document" items="${fruitList}" varStatus="status" begin="0" end="3">
			<li>
			   <a href="${document.viewUrl}">
				<img data-original="/file/${document.documentDataMap.get('productSmallImage').dataValue}"  class="lazy"/>
				<div class="p-info">
					<div class="left">
					  <p>${document.title}</p>
					  <p><span class="old_price">￥${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}</span><span class="orange">￥${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span></p>
					</div>
					<span class="addToCart right">加入购物车</span>
					<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode">
				</div>
				<c:if test="${document.documentDataMap.get('availableCount').dataValue==0}">
					<span class="no-stock">已售罄</span>
				</c:if>
				</a>
			</li>
			</c:forEach>
		  </ul>
		</div>
	</div>
	<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
<script>
	function coupon() {
		if ($('#coupon ul li').length>3) {
			$('#coupon ul li').addClass('widthculen');
			var ulwidth = ($('#coupon ul li').outerWidth()+6)*$('#coupon ul li').length;
			console.log(ulwidth);
			$('#coupon ul').css('width',ulwidth+'px');
		}
	};
	coupon()
	// 领取代金券
	$('#coupon>ul li >div em').on('click',function () {
		// $.ajax({
  //           type:"POST",
  //           url: '/userRelation/delete.json',
  //           data:{
  //              userRelationId:e
  //           },
  //           dataType:'json', 
  //           success:function (data) {
  //               alert("领取成功！");
  //           },
  //           error:function (data) {
  //               alert("系统繁忙,请稍后再试");
  //               return false;
  //           }
  //       });
  			$('.successcubox').addClass('show');
  			setTimeout(function(){
  				$('.successcubox').removeClass('show');
  				$('.successcubox').addClass('small');
  			},1000)
  			setTimeout(function(){
  				$('.successcubox').removeClass('small');
  			},2000)

	})
</script>
</html>
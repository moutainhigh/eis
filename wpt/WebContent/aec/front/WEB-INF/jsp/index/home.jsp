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
<link rel="stylesheet" type="text/css" href="css/newCss/main.css"/>
<link rel="stylesheet" type="text/css" href="css/newCss/index.css"/>
<link rel="stylesheet" type="text/css" href="css/newCss/swiper.min.css">
<link rel="stylesheet" type="text/css" href="css/newCss/set1.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js">	</script>
<script type="text/javascript"  charset="utf-8" src="../../js/common.js"></script>
<script type="text/javascript" src="../../../js/pull-down list_index.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/pull-down list.css" /> 
<script type="text/javascript" src="../../js/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript" src="../../js/lazyload.js"></script>
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
   <div class="wid-100" id="wid-100">
	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<nav>
	  <div class="swiper-container" style="height:auto;display:block;position: relative;min-width:1000px;max-width:1150px"> 
		<div class="swiper-wrapper">			
 		<c:set var="num" value="0"></c:set>
				<c:forEach items="${newsList}" var="list" varStatus="i">
 				<c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						<div class="swiper-slide"><a href="${list.redirectTo }"><img alt="" src="${list.documentDataMap.get('bannerImage').dataValue} " > </a></div>
			          </c:if>   
					</c:if>
				</c:forEach> 
		</div>
		<div class="swiper-pagination"></div>
		<div class="swiper-button-prev swiper-button-white"></div>
        <div class="swiper-button-next swiper-button-white"></div>
	</div>
	</nav>
	    <script language="javascript"> 
			var mySwiper = new Swiper('.swiper-container',{
			effect : 'fade',
			pagination : '.swiper-pagination',
			autoplay : 3000,
			prevButton:'.swiper-button-prev',
			nextButton:'.swiper-button-next',			
			paginationClickable :true,
			observer:true,
			observeParents:true,
			})
		</script>
	<div class="box_container">
	     <div class="nav_bar wid-80 martop30 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>以先优选</span></div> 
			<div class="btn_right"><a class="orange" href="/content/product/index.shtml">查看更多</a></div>
        </div>
		<ul class="list_product martop25 wid-80">
		    <c:set var="num" value="0"></c:set>
			 <c:forEach var="document" items="${shouyeyixianyouxuanList}">
			  <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<7}">
			    <li>
			        <a href="${document.viewUrl}">
			    	<div class="box_img">
						<p style="margin:0px !important"><img class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../image/listImgDefault.png"></p>
						<p class="fo-15 margin_sx" style="text-align:center">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
						<p class="fo-12 subheading margin_sx" style="text-align:center">${document.documentDataMap.get("subtitle").dataValue}</p>
						<div class="look_goods marbottom10" style="text-align:center"><span class="red fo-17 marright20"><span class="fo-12">￥</span>${fn:replace(document.documentDataMap.get("productBuyMoney"). dataValue,"money:","")}</span><span class="textLH original fo-12">￥${fn:replace(document.documentDataMap.get("productMarketPrice"). dataValue,"money:","")}</span></div>
				   </div>
				   <div class="mask"></div>
				   </a>
				   
				</li>
				</c:if>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<div class="box_container">
	    <div class="nav_bar wid-80 martop10" style="border-bottom: 0px;border-top: 1px #ff6400 solid;padding-top: 40px;">
    		<div class="btn_left"><span class="redbar"></span><span>食材信仰</span></div> 
			<div class="btn_right"><a class="orange" href="/content/shicaixinyang/shicaixinyang/index.shtml?rows=1000">查看更多</a></div>
        </div>
		<ul class="wid-80 list_xinyang martop35">
		   	  <c:forEach var="document" items="${shouyeshicaixinyangList}" begin="0" end="7">
			    <li>
			    <!--  <a href="http://v.qq.com/iframe/player.html?vid=s0194ugcjl7&width=670&height=502.5&auto=0" target="_blank"> -->
			    <a href="${document.viewUrl}">
			       <img class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}" src="../../image/listImgDefault.png">
				   <div class="explain">
				     ${document.title}
					 </div>
					 </a>
				 </li>
				</c:forEach>
		</ul>	

	</div>
	<div class="box_container">
	    <div class="nav_bar wid-80 martop10 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>溯源实拍</span></div> 
			<div class="btn_right"><a class="orange" href="/content/videolist/index.shtml">查看更多</a></div>
        </div>
		<div class="box_shicaisuyuan martop35 wid-80">
			<c:choose>
		        <c:when test="${fn:length(shouyesuyuanshipaiList)>0}">
				<div class="grid video_lr video_left">
					<c:forEach var="video" items="${shouyesuyuanshipaiList}" varStatus="status" begin="0" end="0">
						<figure class="effect-bubba">
							<a href="${video.viewUrl}" style="position:relative;display:block; width:100%; height:100%;float:left;">
								<img class="lazy" data-original="${video.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/listImgDefault.png"/>
								<div class="playIcon"></div>
							</a>
							<figcaption>
								<h2><!--Fresh <span>Bubba</span>--></h2>
								<p style="padding: 60px 2.5em;"><!--${fn:substring(video.title ,0,10)}${fn:length(video.title)>10?"...":""}-->${video.title}</p>
								<a href="${video.viewUrl}">View more</a>
							</figcaption>			
						</figure>
					</c:forEach>
				</div>
				<div class="grid video_lr video_right">
					<c:forEach var="sc" items="${shouyesuyuanshipaiList}" varStatus="status" begin="1" end="4">
						<figure class="effect-bubba" style="width:49% !important; height:247.5px !important;margin-left:1%;margin-bottom:5px;">
							<a href="${sc.viewUrl}" style="position:relative;display:block; width:100%; height:100%;float:left;">
								<img class="lazy" data-original="${sc.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/listImgDefault.png"/>
								<div class="playIcon"></div>
							</a>
							<figcaption>
								<h2><!--Fresh <span>Bubba</span>--></h2>
								<p>${fn:substring(sc.title ,0,10)}${fn:length(sc.title)>10?"...":""}</p>
								<a href="${sc.viewUrl}">View more</a>
							</figcaption>			
						</figure>
					</c:forEach>
          		
				</div>
				  </c:when>	
			<c:otherwise>
			<p style="text-align:center; font-size:14px; color:#333;">暂时没有相关产品</p>
			</c:otherwise>
           </c:choose>	
			</div>					
	</div>
	<div class="box_container">
	    <div class="nav_bar wid-80 martop30 no_border">
    		<div class="btn_left"><span class="redbar"></span><span>食材百科</span></div> 
			<div class="btn_right"><a class="orange" href="/content/collection/shicaijianbie/index.shtml?rows=1000">查看更多</a></div>
        </div>
		<ul class="martop30 wid-80 list_jianbie">
		   	<c:set var="num" value="0"></c:set>
			 <c:forEach var="document" items="${shouyeshicaijianbieList}">
			  <c:if test="${document.documentTypeCode != 'image'}" >
				<c:set var="num" value="${num+1 }"></c:set>
				<c:if test="${num<5}">
			    <li>
			        <a href="${document.viewUrl}">
			    	<div class="box_img">
			            <p style="margin:0px !important"><img class="lazy" data-original="${document.documentDataMap.get('productSmallImage').dataValue}"  src="../../image/listImgDefault.png"></p>
				    </div>
				    <p class="fo-15 margin_sx">${fn:substring(document.title ,0,10)}${fn:length(document.title)>10?"...":""}</p>
				   <p>${document.documentDataMap.get('documentBrief').dataValue}</p>
				    </a>
				</li>
				</c:if>
				</c:if>
			</c:forEach>
		</ul>	
		</div>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
<script type="text/javascript">
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3Faca36f9d36d03a04d61c93f28c896386' type='text/javascript'%3E%3C/script%3E"));
</script>
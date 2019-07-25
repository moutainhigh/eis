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
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/shicaixinyang.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/swiper.min.css"/>
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/lazyload.js"></script>
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../../js/mobile/swiper.3.1.2.jquery.min.js"></script>
<script type="text/javascript">
	var iWidth = document.documentElement.clientWidth;
	document.getElementsByTagName('html')[0].style.fontSize = iWidth/10+'px';
	$(window).resize(function(){
		var iWidth = document.documentElement.clientWidth;
		document.getElementsByTagName('html')[0].style.fontSize = iWidth/10+'px';
	})		
</script>
<style>
	body{
		background-color: #f7f7f7;
	}
	.box-img{
		width:100%;
		height:auto;
		background:#222222;
		position:relative;
	}
	.box-img img{
		width:100%;
		height:100%;
	}
	.box-img .default{
		width:100%;
		height:6rem;
		margin-top:0.75rem;
	}
		/* style for waterfall grid */
			.wf-container {
				margin: 0 auto;
			}
			.wf-container:before,.wf-container:after {
				content: '';
				display: table;
			}
			.wf-container:after {
				clear: both;
			}
			.wf-box {
			    margin: 10px 5px;
			}
			.wf-box img { 
				display: block;
				width: 100%;
			}
			.wf-box .content {
				border: 1px solid #ccc;
				border-top-width: 0;
				padding: 5px 8px;
			}
			.wf-column {
				float: left;
			}
			.wf-container{
				max-width:640px;
			}
			@media screen and (min-width: 768px) {
				.wf-container { width: 750px; }
			}
			@media screen  and (min-width: 992px) {
				.wf-container { width: 970px; }
			}
			@media screen and (min-width: 1200px) {
				.wf-container { width: 1170px; }
			}
			*{
				font-size: 16px;
			}

</style>
</head>
<body>
 <%@include file="/WEB-INF/jsp/include/header.jsp" %>
	<div id="wrapper_1">
	    <div class="swiper-container" id="box_search">
	    <div class="swiper-wrapper">
	   <c:set var="num" value="0"></c:set>
				<c:forEach items="${newsList}" var="list">
					<c:if test="${list.documentTypeCode == 'image'}" >
						<c:set var="num" value="${num+1 }"></c:set>
						<c:if test="${num<6}">
						<div class="swiper-slide"><a href="${list.redirectTo }"><img alt="" src="${list.documentDataMap.get('bannerImage').dataValue} "> </a> </div>
			          </c:if>
					</c:if>
				</c:forEach>
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
		  <div class="navbar navbar2 mt5">
		     <p><span class="orange ">•食•材•信•仰•</span><a href="/content/shicaixinyang/shicaixinyang/index.shtml?rows=1000" class="fo12">更多&gt;</a></p>
		  </div>
		 	 <!--食材信仰-->
		 <ul class="box-xy  scxy">
		  <div class="line"></div> 
			<c:forEach var="str" items="${shicaixinyangList}" begin="0" end="2">
			  <c:if test="${str.documentTypeCode!= 'image'}">
			<li>
			   <div class="ware_img"><a href="${str.viewUrl}"><img src="${str.documentDataMap.productSmallImage.dataValue}"></a> </div>
			   <div class="box_right2">
				  <div> <span class="ware_names">${str.title}</span></div>
				  <div class="divCenter"> <span class="ware_from">${fn:substring(str.documentDataMap.documentBrief.dataValue,0,25)}${fn:length(str.documentDataMap.documentBrief.dataValue)>25?"..":""}</span></div>
				  <div class="one_sprice"><span class="price_mark">阅读（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</span> 
				  <span class="fare orange favorite"><img src="../../../../image/mobile/header/shoucang.png" style="width: 20px;height: 20px;">（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</span> </div>
			   </div>			 
			</li>
			 <div class="line"></div> 
			 </c:if>
			</c:forEach>
		  </ul>
		   <div class="navbar navbar2 ">
		     <p><span class="orange ">•营•养•膳•食•</span><a href="/content/collection/yingyangshanshi/index.shtml?rows=1000" class="fo12">更多&gt;</a></p>
		  </div>
		 <ul class="box-xy  scxy">
		  <div class="line"></div> 
			<c:forEach var="str" items="${yingyangshanshiList}" begin="0" end="2">
			  <c:if test="${str.documentTypeCode!= 'image'}">
			<li>
			   <div class="ware_img"><a href="${str.viewUrl}"><img src="${str.documentDataMap.productSmallImage.dataValue}"></a> </div>
			   <div class="box_right2">
				  <div> <span class="ware_names">${str.title}</span></div>
				  <div class="divCenter"> <span class="ware_from">${fn:substring(str.documentDataMap.documentBrief.dataValue,0,25)}${fn:length(str.documentDataMap.documentBrief.dataValue)>25?"..":""}</span></div>
				  <div class="one_sprice"><span class="price_mark">阅读（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</span> 
				  <span class="fare orange favorite"><img src="../../../../image/mobile/header/shoucang.png" style="width: 20px;height: 20px;">（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</span> </div>
			   </div>			 
			</li>
			 <div class="line"></div> 
			 </c:if>
			</c:forEach>
		  </ul>
         <div class="navbar navbar2 ">
		     <p><span class="orange ">•食•材•百•科•</span><a href="/content/collection/shicaijianbie/index.shtml?rows=1000" class="fo12">更多&gt;</a></p>
		  </div>
	<ul class="box-xy  scxy">
		  <div class="line"></div> 
			<c:forEach var="str" items="${shicaijianbieList}" begin="0" end="2">
			  <c:if test="${str.documentTypeCode!= 'image'}">
			<li>
			   <div class="ware_img"><a href="${str.viewUrl}"><img src="${str.documentDataMap.productSmallImage.dataValue}"></a> </div>
			   <div class="box_right2">
				  <div> <span class="ware_names">${str.title}</span></div>
				  <div class="divCenter"> <span class="ware_from">${fn:substring(str.documentDataMap.documentBrief.dataValue,0,25)}${fn:length(str.documentDataMap.documentBrief.dataValue)>25?"..":""}</span></div>
				  <div class="one_sprice"><span class="price_mark">阅读（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</span> 
				  <span class="fare orange favorite"><img src="../../../../image/mobile/header/shoucang.png" style="width: 20px;height: 20px;">（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</span> </div>
			   </div>			 
			</li>
			 <div class="line"></div> 
			 </c:if>
			</c:forEach>
		  </ul>
	</div>	
		 <%@include file="/WEB-INF/jsp/include/footer.jsp" %> 
</body>
<script src="/js/mobile/responsive_waterfall.js"></script>
<script src="/js/mobile/app.js"></script>
</html>    
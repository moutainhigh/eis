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
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/lazyload.js"></script>
<script type="text/javascript"  charset="utf-8" src="../../../../js/mobile/swiper.3.1.2.jquery.min.js"></script>
<style>

	.box-img{
		width:90%;
		margin:0 auto;
		height:7.5rem;
		background:#222222;
		position:relative;
		margin-top: 10px;
	}
	.box-img img{
		width:100%;
		height:100%;
	}
	.box-xy li{
		padding: 10px 12px;
	}
	.box-img .default{
		width:100%;
		height:6rem;
		margin-top:0.75rem;
	}
	.included{
		width: 100%;
		height: auto;
		overflow: hidden;
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
			$(".my-swiper-pagination .my-bullet").css("width",w+"%");
		</script>
	    <div class="line"></div> 
	   <div class="navbar navbar3">
		     <p><span class="orange ">•精•彩•推•荐•</span></p>
		  </div>
	  <!--精彩推荐-->
		<div class="box-yx mxcf ">
		<div class="line"></div> 
		 <c:forEach var="str" items="${jingcaituijianList}" begin="0" end="2">
		 <div class="included">
		    <div class="box-img po-re "><a href="${str.viewUrl}"><img class="${!empty str.documentDataMap.verticalSmallImage.dataValue?'':'default'}" src="${!empty str.documentDataMap.verticalSmallImage.dataValue?str.documentDataMap.verticalSmallImage.dataValue:'../../../../image/mobile/video_list.png'}"><img src="../../../../image/mobile/play.png" class="icon_play" style=""><span class="time" style="display:none;">2:10</span></a></div>
			<div class="box_con">
			   <a href="${str.viewUrl}"><p class="video-name" style="font-size:16px!important">${str.title}</p></a>
			   <ul class="num-list">
			    <a href="${str.viewUrl}"><li>播放(${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0})</li></a>
				<a href="${str.viewUrl}"><li>赞(${!empty str.documentDataMap.praiseCount.dataValue?str.documentDataMap.praiseCount.dataValue:0})</li></a>
				<a href="${str.viewUrl}"><li>☆(${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0})</li></a>
				<a href="${str.viewUrl}"><li class="no-border">评论(${!empty str.documentDataMap.commentCount.dataValue?str.documentDataMap.commentCount.dataValue:0})</li></a>
			   </ul>
			</div>
			<div class="line"></div> 
			 </div>
		  </c:forEach>
		 
		 </div>


		   <div class="navbar navbar3">
		     <p><span class="orange ">•视•频•汇•总•</span><a href="/content/videolist/shipinghuizong/index.shtml" class="fo12">更多&gt;</a></p>
		  </div>
	  <!--视频汇总-->
		<div class="box-yx mxcf ">
		<div class="line"></div> 
		 <c:forEach var="str" items="${shipinghuizongList}" begin="0" end="2">
		 <div class="included">
		    <div class="box-img po-re"><a href="${str.viewUrl}"><img class="${!empty str.documentDataMap.verticalSmallImage.dataValue?'':'default'}" src="${!empty str.documentDataMap.verticalSmallImage.dataValue?str.documentDataMap.verticalSmallImage.dataValue:'../../../../image/mobile/video_list.png'}"><img src="../../../../image/mobile/play.png" class="icon_play" style=""><span class="time" style="display:none;">2:10</span></a></div>
			<div class="box_con">
			   <a href="${str.viewUrl}"><p class="video-name" style="font-size:16px!important">${str.title}</p></a>
			   <ul class="num-list">
			    <a href="${str.viewUrl}"><li>播放（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</li></a>
				<a href="${str.viewUrl}"><li>赞（${!empty str.documentDataMap.praiseCount.dataValue?str.documentDataMap.praiseCount.dataValue:0}）</li></a>
				<a href="${str.viewUrl}"><li>☆（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</li></a>
				<a href="${str.viewUrl}"><li class="no-border">评论（${!empty str.documentDataMap.commentCount.dataValue?str.documentDataMap.commentCount.dataValue:0}）</li></a>
			   </ul>
			</div>
			<div class="line"></div> 
			</div>
		  </c:forEach>
		 </div>
	 <%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
<script type="text/javascript">
function testUrl()
{
	var t="";
	var url=window.location.search;
	if(url.indexOf("?")!=-1) 
		{ 
			var str = url.substr(1) 
			strs = str.split("&"); 
			var key=new Array(strs.length);
			var value=new Array(strs.length);
			for(i=0;i<strs.length;i++) 
			{ 
			key[i]=strs[i].split("=")[0]
				 value[i]=unescape(strs[i].split("=")[1]); 
				 if(key[i]=="subtitle"){
					 t=value[i];
				 }
			   } 
			}
		return t;
	}
</script>
</html>    
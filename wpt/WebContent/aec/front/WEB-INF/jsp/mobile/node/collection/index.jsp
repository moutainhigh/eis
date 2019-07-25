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
	.box-img{
		width:100%;
		height:7.5rem;
		background:#222222;
		position:relative;
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
</style>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="/"></a><span>${node.name}</span><a class="list1"></a>
 </div>
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
	 <!--营养膳食-->
	 <ul class="box-xy wid90 yyss " style="display: block;">
	    <c:forEach var="str" items="${yingyangshanshiList}">
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
		</c:forEach>
	  </ul>
	  <!--明星厨房-->
		<div class="box-yx mxcf " style="display: none">
		 <c:forEach var="str" items="${mingxingchufangList}">
		    <div class="box-img po-re"><a href="${str.viewUrl}"><img class="${!empty str.documentDataMap.productSmallImage.dataValue?'':'default'}" src="${!empty str.documentDataMap.productSmallImage.dataValue?str.documentDataMap.productSmallImage.dataValue:'../../../../image/mobile/video_list.png'}"><img src="../../../../image/mobile/play.png" class="icon_play" style=""><span class="time" style="display:none;">2:10</span></a></div>
			<div class="box_con">
			   <a href="${str.viewUrl}"><p class="video-name" style="font-size:16px!important">${str.title}</p></a>
			   <ul class="num-list">
			    <a href="#"><li>播放（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</li></a>
				<a href="#"><li>赞（${!empty str.documentDataMap.praiseCount.dataValue?str.documentDataMap.praiseCount.dataValue:0}）</li></a>
				<a href="#"><li>☆（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</li></a>
				<a href="#"><li class="no-border">评论（${!empty str.documentDataMap.commentCount.dataValue?str.documentDataMap.commentCount.dataValue:0}）</li></a>
			   </ul>
			</div>
			<div class="line"></div> 
		  </c:forEach>
		 </div>
	 <!--食材鉴别-->
	  <ul class="box-xy wid90 scjb " style="display: none;">
	     <c:forEach var="str" items="${shicaijianbieList}">
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
		</c:forEach>
	  </ul>
</body>
<script type="text/javascript">
	$(function() {
	 var $yyss = $('#yyss');
     var $mxcf = $('#mxcf');
     var $scjb = $('#scjb');
     $yyss.click(function(){
        $mxcf.removeClass('current');
        $scjb.removeClass('current');
        $yyss.addClass('current');
        $('.mxcf').css('display','none');
        $('.scjb').css('display','none');
        $('.yyss').css('display','block');
    });
    $mxcf.click(function(){
        $yyss.removeClass('current');
        $scjb.removeClass('current');
        $mxcf.addClass('current');
        $('.mxcf').css('display','block');
        $('.yyss').css('display','none');
        $('.scjb').css('display','none');
    });
     $scjb.click(function(){
        $mxcf.removeClass('current');
        $yyss.removeClass('current');
        $scjb.addClass('current');
        $('.mxcf').css('display','none');
        $('.yyss').css('display','none');
        $('.scjb').css('display','block');   
    });
	switch(testUrl()){
		case 'yingyangshanshi':
			$mxcf.removeClass('current');
			$scjb.removeClass('current');
			$yyss.addClass('current');
			$('.mxcf').css('display','none');
			$('.scjb').css('display','none');
			$('.yyss').css('display','block');			
		break;
		case 'shicaijianbie':
			$mxcf.removeClass('current');
			$yyss.removeClass('current');
			$scjb.addClass('current');
			$('.mxcf').css('display','none');
			$('.yyss').css('display','none');
			$('.scjb').css('display','block'); 			
		break		;
		case 'mingxingchufang':
			$yyss.removeClass('current');
			$scjb.removeClass('current');
			$mxcf.addClass('current');
			$('.mxcf').css('display','block');
			$('.yyss').css('display','none');
			$('.scjb').css('display','none');
		break;
		default:
			$mxcf.removeClass('current');
			$scjb.removeClass('current');
			$yyss.addClass('current');
			$('.mxcf').css('display','none');
			$('.scjb').css('display','none');
			$('.yyss').css('display','block');
	}
})
function  vanish(){
   if(document.getElementById("nav_list").style.display=="none"){
    document.getElementById("nav_list").style.display="block";
   }
else{
    document.getElementById("nav_list").style.display="none";
    }
}
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
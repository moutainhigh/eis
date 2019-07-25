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
<script type="text/javascript" src="../../../../js/mobile/lazyload.js"></script>
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
    *{
	    font-size: 16px;
	}
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
		   <div class="navbar navbar3">
		     <p><span class="orange ">•视•频•汇•总•</span></p>
		  </div>
	  <!--视频汇总-->
		<div class="box-yx mxcf ">
		<div class="line"></div> 
		 <c:forEach var="str" items="${newsList}">
		 <div class="included">
		    <div class="box-img po-re"><a href="${str.viewUrl}"><img class="${!empty str.documentDataMap.productSmallImage.dataValue?'':'default'}" src="${!empty str.documentDataMap.productSmallImage.dataValue?str.documentDataMap.productSmallImage.dataValue:'../../../../image/mobile/video_list.png'}"><img src="../../../../image/mobile/play.png" class="icon_play" style=""><span class="time" style="display:none;">2:10</span></a></div>
			<div class="box_con">
			   <a href="${str.viewUrl}"><p class="video-name" style="font-size:16px!important">${str.title}</p></a>
			   <ul class="num-list">
			    <a href="#"><li>播放(${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0})</li></a>
				<a href="#"><li>赞(${!empty str.documentDataMap.praiseCount.dataValue?str.documentDataMap.praiseCount.dataValue:0})</li></a>
				<a href="#"><li>☆(${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0})</li></a>
				<a href="#"><li class="no-border">评论(${!empty str.documentDataMap.commentCount.dataValue?str.documentDataMap.commentCount.dataValue:0})</li></a>
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
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
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/shicaixinyang.css"/>
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script> 
<style>
	.btn-list li.more{width: 49%;}
	.btn-list li.mores{width: 49%;border-right: 0px;}
</style>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>食材库</span><a class="list1" onclick="vanish();"></a>
 </div>
	<div id="wrapper_1">
	   	 <ul id="nav_list" style="display:block;">
		   <li  ><a href="/"><img src="../../../../image/mobile/home.png" style="width:17px;height: 17px;" />首页</a></li>
		   <li  > <a href="/content/user/pcenter.shtml?favPage=1&favRows=10"><img src="../../../../image/mobile/personicon.png" style="width:17px;height: 17px;" />个人中心</a></li>
		   <li  ><a href="/cart.shtml"><img src="../../../../image/mobile/caricon.png" style="width:17px;height: 17px;" />购物车</a></li>
		   <li class="no-border"><a href="/search/index.shtml"><img src="../../../../image/mobile/search.png" style="width:17px;height: 17px;" />搜索</a></li>
		 </ul>
	   <div class="box-yx">
	      <img src="../../../../image/mobile/video.png"/>
	   </div>
	    
	    <ul class="btn-list">
		   <li id="scxy" class="current more" >优选推荐</li>
		   <li id="jxsp" class="mores">优选预售</li>
		   <!--<li id="jcsk" class="no-border">精彩时刻</li>-->
		 </ul>
		  <!--精选视频-->
		 <div class="box-yx mt30 jxsp" style="display: none">
		 	<c:forEach var="str" items="${youxuanyushouList}">
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
		 <!--<c:forEach var="str" items="${videoList}">
		    <div class="box-img po-re"><a href="${str.viewUrl}"><img src="${!empty str.documentDataMap.productSmallImage.dataValue?str.documentDataMap.productSmallImage.dataValue:'../../../../image/mobile/video_list.png'}"><img src="../../../../image/mobile/13.png" class="icon_play" style="display:none;"><span class="time" style="display:none;">2:10</span></a></div>
			<div class="box_con">
			   <a href="${str.viewUrl}"><p class="video-name">${str.title}</p></a>
			   <ul class="num-list">
			    <a href="#"><li>播放（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</li></a>
				<a href="#"><li>赞（${!empty str.documentDataMap.praiseCount.dataValue?str.documentDataMap.praiseCount.dataValue:0}）</li></a>
				<a href="#"><li>☆（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</li></a>
				<a href="#"><li class="no-border">评论（${!empty str.documentDataMap.commentCount.dataValue?str.documentDataMap.commentCount.dataValue:0}）</li></a>
			   </ul>
			</div>
		  </c:forEach>-->
		  
		 </div>
		  <!--<!--精彩时刻
		 <div class="box_sk jcsk" style="display: none">
		 <c:forEach var="str" items="${jingcaishikeList}" varStatus="status">
		 <c:if test="${status.index%2==0}">
		 <div class="box_left">
		 <img src="${str.documentDataMap.productSmallImage.dataValue}" style="width :100%;height:100%;">
		 <p class="fo_st oneline">${str.title}</p>
		 </div>
		 </c:if>
		 <c:if test="${status.index%2!=0}">
		 <div class="box_right">
		  <img src="${str.documentDataMap.productSmallImage.dataValue}" style="width :100%;height:100%;">
		 <p class="fo_st oneline">${str.title}</p>
		 </div>
		 <div style="clear:both"></div>
		 </c:if>
		 </c:forEach>
		 </div>
		  <div class="line"></div>-->
		
	</div>
	 <!--食材信仰-->
	 <ul class="box-xy wid90 scxy" style="display: block;">
		<c:forEach var="str" items="${youxuantuijianList}">
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
	 var $scxy = $('#scxy');
     var $jxsp = $('#jxsp');
     var $jcsk = $('#jcsk');

      $scxy.click(function(){
        $jxsp.removeClass('current');
        $jcsk.removeClass('current');
        $scxy.addClass('current');
        $('.jxsp').css('display','none');
        $('.jcsk').css('display','none');
        $('.scxy').css('display','block');
    });
    $jxsp.click(function(){

        $scxy.removeClass('current');
        $jcsk.removeClass('current');
        $jxsp.addClass('current');
        $('.jxsp').css('display','block');
        $('.scxy').css('display','none');
        $('.jcsk').css('display','none');
    });

     $jcsk.click(function(){

        $scxy.removeClass('current');
        $jxsp.removeClass('current');
        $jcsk.addClass('current');
        $('.scxy').css('display','none');
        $('.jxsp').css('display','none');
        $('.jcsk').css('display','block');
   
    });
})
	function vanish(){
	if(document.getElementById("nav_list").style.display=="none"){
    document.getElementById("nav_list").style.display="block";
   }
else{
    document.getElementById("nav_list").style.display="none";
    }
}
</script>
</html>    
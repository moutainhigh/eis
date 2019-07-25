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
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/hudongzhongxin.css"/>
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script>
<script type="text/javascript">
			
</script>
<style>
.hidden{display:none;}
</style>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>互动中心</span><a class="list1" onclick="vanish()"></a>
 </div>
	<div id="wrapper_1">
			<%@include file="/WEB-INF/jsp/include/BtnList.jsp" %>
	   <div class="box-yx">
	      <img src="../../../../image/mobile/video.png"/>
	   </div>
	     <ul class="btn-list">
		   <li id="xwbd" class="current">新闻报道</li>
		   <li id="hzsq" class="no-border">合作申请</li>
		 </ul>
		
		  <!--精彩时刻-->
		 <div class="box_sk hzsq" style="display: none;">
		 <div class="wid90">合作流程:</div>				 
		 <div class="wid90 martop20">
		 	 <span>下载文件</span><img src="../../../../image/arrowR.png"/><span>填写文件</span><img src="../../../../image/arrowR.png"/><span>发送文件至邮箱</span><img src="../../../../image/arrowR.png"/><span>部门审核</span><img src="../../../../image/arrowR.png"/><span>实地考察</span><img src="../../../../image/arrowR.png"/><span>商务洽谈</span><img src="../../../../image/arrowR.png"/><span>签订合同</span> 
		 </div>
		  <div class="line"></div>
		  <div class="wid90 martop10" >文件下载:</div>	
		 <div class="box_left martop10">
		 <span>企业合作文件</span>
		 </div>
		 <div class="box_right orange martop10">
		 	<a href="/content/notice/20160709194847.shtml">下载文件</a><!--企业合作-->
		 </div>
		  <div class="box_left martop10">
		 <span>合作社合作文件</span>
		 </div>
		 <div class="box_right orange martop10">
		 	<a href="/content/notice/20160709194520.shtml">下载文件</a><!--合作社合作-->
		 </div>
		   <div class="box_left martop10">
		 <span>个体农户合作文件</span>
		 </div>
		  <div class="box_right orange martop10">
		 	<a href="/content/notice/20160709194628.shtml"">下载文件</a><!--个体农户合作-->
		 </div>
		  <div class="line1"></div>
		 <div class="wid90 martop10">联系方式:</div>	
		 <div class="box_left martop10">合作邮箱:</div>
		  <div class="box_right3 martop10">yixian@yixian365.com(具体项目申请方式请查看相应下载文件)</div>		
		  <div class="box_left martop10">地址:</div>
		  <div class="box_right4 martop10">北京市朝阳区八里庄北里129号保利东郡C座11层</div>
		  <div class="box_left ">邮编:100025</div>	
		  </div>
	</div>
	 <!--食材信仰-->
	 <ul class="box-xy wid90 xwbd" style="display: block;">
	 	<c:forEach var="str" items="${newsList}">
	    <li>
		   <div class="ware_img"><a href="${str.viewUrl}"><img src="${str.documentDataMap.productSmallImage.dataValue}"></a> </div>
		   <div class="box_right2">
			  <div> <span class="ware_names">${str.title}</span></div>
			  <div class="divCenter"> <span class="ware_from hidden">${fn:substring(str.documentDataMap.documentBrief.dataValue,0,25)}${fn:length(str.documentDataMap.documentBrief.dataValue)>25?"..":""}</span></div>
              <div class="one_sprice"><span class="price_mark">阅读（${!empty str.documentDataMap.readCount.dataValue?str.documentDataMap.readCount.dataValue:0}）</span> 
              <span class="fare orange favorite"><img src="../../../../image/mobile/header/shoucang.png" style="width: 20px;height: 20px;">（${!empty str.documentDataMap.favoriteCount.dataValue?str.documentDataMap.favoriteCount.dataValue:0}）</span> </div>
		   </div>			 
		</li>
		 <div class="line"></div> 
		</c:forEach>
	    <!--<li>
		   <div class="ware_img"><img src="http://www.yixian365.com/static/documentFile/open/201605181056-3a7d364d-89d1-4e07-bff3-384130bc06c5.jpg"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_names">昵称</span></div>
			  <div class="divCenter"> <span class="ware_from">写评论</span></div>
              <div class="one_sprice"><span class="price_mark">[食品安全]</span> 
              <span class="fare price_mark"><img src="../../../../image/mobile/header/shoucang.png" style="width: 20px;height: 20px;">（22）</span> </div>
		   </div>
			 
		</li>
		 <div class="line"></div> -->
	  </ul>
	  

</body>
<script type="text/javascript">
	$(function() {
	 var $xwbd = $('#xwbd');
     var $hzsq = $('#hzsq');

      $xwbd.click(function(){
        $hzsq.removeClass('current');
        $xwbd.addClass('current');
       
        $('.hzsq').css('display','none');
        $('.xwbd').css('display','block');
    });

     $hzsq.click(function(){

        $xwbd.removeClass('current');
        $hzsq.addClass('current');
        $('.xwbd').css('display','none');
        $('.hzsq').css('display','block');
   
    });
})
function  vanish(){
   if(document.getElementById("nav_list").style.display=="none"){
    document.getElementById("nav_list").style.display="block";
   }
else{
    document.getElementById("nav_list").style.display="none";
    }
}
</script>
</html>    
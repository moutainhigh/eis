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
<link rel="stylesheet" type="text/css" href="../../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../../css/shicaisuyuandetail.css">
<script  type="text/javascript" src="../../../js/jquery.min.js"></script>
<script  type="text/javascript" src="../../../js/common.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
	$(document).ready(function(){
		$("#btnReply").click(function(){
			$(".review1").toggle();
		});
});


function publishSubmit(){
	 if(Cookie.getCookie("eis_username")==null){
        alert("您还没有登录");
        location.href = "/content/user/login.shtml";

    }else{
		$.ajax({
            type:"POST",
            url: '/comment/submit.json',
            data:{  
                objectType:"document",
                objectId:$("#udid").val(),
                content:$("#review_text").val()
            },
            dataType:'json',
            success:function (data) {
				if(data.message.operateCode == 102008){
					alert("评论成功");
					window.location.reload();
				}else{
					alert(data.message.message);
				}  
            },
            error:function(XMLResponse){		
				alert("操作失败:" + XMLResponse.responseText);
			},
        });
	}		
}	
</script>
</head>
<body id="wid-100">
	 <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80">
		  <div class="box_container">
		     <div class="box_container_left martop10">
		     <a href="/content/shicaixinyang/index.shtml" class="color-black">返回</a>
			    
			 </div>
			 <div class="box_container_right martop20">
			    <div class="bshare-custom">
				   <a title="分享到" href="http://www.bShare.cn/" id="bshare-shareto" class="bshare-more">分享到</a>
				   <a title="分享到QQ空间" class="bshare-qzone">QQ空间</a>
				   <a title="分享到新浪微博" class="bshare-sinaminiblog">新浪微博</a>
				   <a title="分享到微信" class="bshare-weixin">微信</a>
				   <a title="更多平台" class="bshare-more bshare-more-icon more-style-addthis"></a>
				  <!-- <span class="BSHARE_COUNT bshare-share-count">0</span>-->
				</div>
				<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=1&amp;lang=zh"></script>
				<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>
			 </div>
		  </div>
		  	 <div class="flotage">
				<a onclick="conceal()"><img src="../../../image/erweima.jpg"/></a>
				<a href="#wid-100"><img src="../../../image/up.jpg"/></a>
			 </div>
			<div style="display:block;" class="two-dimension" id="two-dimension">
		       <img src="../../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	        </div>
		  <div class="box_container martop30">
		     <!-- <img src="${document.documentDataMap.get('productSmallImage').dataValue}" class="img_article"> -->
		  </div>
		  <div class="box_article martop30">
		     <h3>${document.title}</h3>
			 <h5 class="martop30"></h5>
			 <div class="box_container martop30"> 						
   				 ${document.content}				
			 </div>
		  </div>
			   <div class="box_container_left">
			       <span>阅读(${readCount})</span>
	     	<c:choose>
			<c:when test="${document.documentDataMap.get('praised').dataValue == 'true'}">
				<a href="#"  onclick="unlike(${document.udid});" class="color-black2">
				赞
   			<span id="fav"  class="preview_icon 
 				 <c:choose>  
				 <c:when test="${document.documentDataMap.get('praised').dataValue == 'true'}">  
						check
				 </c:when>
					 <c:otherwise>  
						 no_check 
				 </c:otherwise>  
				</c:choose>  ">
 			 </span>
				 </a> 
			</c:when>
			<c:otherwise>
				<a href="#"  onclick="like(${document.udid});" class="color-black2">
				赞
   			<span id="fav"  class="preview_icon 
 				 <c:choose>  
				 <c:when test="${document.documentDataMap.get('praised').dataValue == 'true'}">  
						check
				 </c:when>
					 <c:otherwise>  
						 no_check 
				 </c:otherwise>  
				</c:choose>  ">
 			 </span>
				 </a> 
			</c:otherwise>
		</c:choose>
		<span class="count1">${praiseCount}</span>
		
	
	
		<c:choose>
			<c:when test="${document.documentDataMap.get('favorited').dataValue == 'true'}">
				<a href="#"  onclick="uncollection(${document.udid});" class="color-black">
				收藏
   			<span id="fav"  class="star_icon 
 				 <c:choose>  
				 <c:when test="${document.documentDataMap.get('favorited').dataValue == 'true'}">  
						check
				 </c:when>
					 <c:otherwise>  
						 no_check 
				 </c:otherwise>  
				</c:choose>  ">
 			 </span>
				 </a> 
			</c:when>
			<c:otherwise>
				
		<a href="#"  onclick="collection(${document.udid});" class="color-black">
		收藏
   			<span id="fav"  class="star_icon 
 				 <c:choose>  
				 <c:when test="${document.documentDataMap.get('favorited').dataValue == 'true'}">  
						check
				 </c:when>
					 <c:otherwise>  
						 no_check 
				 </c:otherwise>  
				</c:choose>  ">
 			 </span>
				 </a> 
		
			</c:otherwise>
		</c:choose>
	<span class="count2">${favoriteCount}</span>
 
			   </div>
			   <div class="box_container_right">
			      <a href="${lastDocument.viewUrl}" class="color-black"><span class="orange">上一篇：</span></a>
			       <span>${lastDocument.title}</span>
				   <a href="${nextDocument.viewUrl}" class="color-black"><span class="orange" >下一篇：</span></a>
				   <span> ${nextDocument.title} </span>
			   </div>
           <div class="review">
		 <form >
		  <input type="hidden"  value="${document.udid}" id="udid"/> 
		 	<textarea id="review_text" placeholder="看后感想..."></textarea>
			<p> <input type="button" value="发表" onclick="publishSubmit()" id="abc" class="thoughts" /></p>
		 </form >
		    
		  </div>
		   <div class="viewlist martop30">
		     <div class="title">评论(${fn:length(commentList)})</div>
			 <div class="viewed martop30">
			 <div class="box_container_left">
			 <ul>
			 	<c:forEach var="comment" items="${commentList}"> 
			 <c:forEach var="c" items="${comment}" >
				 <c:choose>
			  <c:when test="${!empty c.data.userHeadPic}">
				<c:if test="${fn:indexOf(c.data.userHeadPic,'http://')!=-1}">
					<li class="c-top"><img src="${c.data.userHeadPic}" ></li>
				</c:if>
				<c:if test="${fn:indexOf(c.data.userHeadPic,'http://')==-1}">
					<li class="c-top"><img src="/static/userUploadDir/${c.data.userHeadPic}" ></li>
				</c:if>
			  </c:when>
			  <c:otherwise>
				<li class="c-top"><img src="../../image/header.png" ></li>
			  </c:otherwise>
			</c:choose>
			<li class="c-bottom" style="text-align:center">${c.data.userRealName}</li>
			</ul>
			</div>
				<div class="box_container_right">
				<ul>
				   <li class="c-top"><span class="heading">${c.content}</span></li>
				   <li class="c-bottom"><span class="datetime"><fmt:formatDate value="${c.createTime}"  type="both"/></span></li>
				   </ul>
				</div>
			 </li>
			</c:forEach>
			</c:forEach>
			
			 
			 
			 
			 </div>
			<!--  <div class="wid-100">
			    <ul class="listbox"> 
					<a href="#"><li>1</li></a>    
					<a href="#"><li>2</li></a> 
					<a href="#"><li>3</li></a> 
					<a href="#"><li>4</li></a> 
					<a href="#"><li>5</li></a> 
					<a href="#" class="especially"><li>.</li></a> 
					<a href="#" class="especially"><li>.</li></a> 
					<a href="#" class="especially"><li>.</li></a> 
					<a href="#"><li>9</li></a> 
					<a href="#"><li>></li></a>   
				</ul>
			 </div> -->
		  </div>
		<div class="martop10"></div>
</div>
 <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
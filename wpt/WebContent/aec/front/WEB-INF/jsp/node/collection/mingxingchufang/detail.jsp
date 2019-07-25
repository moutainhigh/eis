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
<!-- <link rel="stylesheet" type="text/css" href="../../../css/index.css"> -->
<link rel="stylesheet" type="text/css" href="../../../css/videolist.css">
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../../js/jquery.min.js"></script>
<script  type="text/javascript" src="../../../js/common.js"></script>
<script  type="text/javascript" src="../../../js/jump.js"></script>
<script>window.jQuery || document.write('<script src="/js/jquery-1.11.0.min.js"><\/script>')</script>
<script src="../../../js/l-by-l.min.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
jQuery(document).ready(function($){
	
	$(".word1").lbyl({
		content: "${document.title}",
		speed: 200,
		type: 'show',
		finished: function(){ 
			$('.word2').lbyl({
				content:"",
				speed: 200,
				type: 'fade',
				fadeSpeed: 500
			})
		} // Finished Callback
	});

});
function publishSubmit(objectId){
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
					window.location.reload();
				}

            },
            error:function (data) {
                alert("系统繁忙,请稍后再试");
                return false;
            }
        });	
        }		
}
</script>
</head>
<body>
	 <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80" id="wid-100">
		  <div class="box_container">
		     <div class="box_container_left martop20">
		     <a href="javascript:history.go(-1)" class="color-black"><返回</a>  
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
		  <div class="box_container martop10">
		  </div>
		  <div class="box_article ">
		      <h3><span  class="word1"></span></h3>
			 <div class="wit-80 martop10">
				<!-- <c:set var="videoUrl" value="http://v.qq.com/iframe/player.html?vid=${document.documentDataMap.get('videoClip').dataValue}&amp;width=1000&amp;height=800&amp;auto=0" />
				<iframe class="video_iframe" style=" z-index:1; " src="${videoUrl}" allowfullscreen="" frameborder="0" height="800" width="1000" ></iframe> -->
				 <iframe frameborder="0" height="700" width="1000" src="http://v.qq.com/iframe/player.html?vid=${document.documentDataMap.get('videoClip').dataValue}&tiny=0&auto=0" allowfullscreen></iframe> 
   				<!-- ${document.content} -->
			 </div>
			 <div>
		 <div class="box_article martop10">
				<div class="wit-80 martop30">
					${document.content}
				</div>
			 </div>	
			   <div class="box_container_left2 martop30">
			       <span>阅读(${readCount})</span>
		           	<c:choose>
			          <c:when test="${document.documentDataMap.get('praised').dataValue == 'true'}">
				        <a href="#"  onclick="unlike(${document.udid});" class="color-black2">
						<span>赞(${praiseCount})</span>
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
				<span>赞(${praiseCount})</span>
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
		
		<c:choose>
			<c:when test="${document.documentDataMap.get('favorited').dataValue == 'true'}">
				<a href="#"  onclick="uncollection(${document.udid});" class="color-black">
				<span>收藏(${favoriteCount})</span>
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
		<span>收藏(${favoriteCount})</span>
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
			   </div>
			   <div class="box_container_right  martop30">
			       <a href="${lastDocument.viewUrl}" class="color-black"><span class="orange">上一篇：</span></a>
			       <span>${lastDocument.title}</span>
				   <a href="${nextDocument.viewUrl}" class="color-black"> <span class="orange" >下一篇：</span></a>
				   <span>${nextDocument.title}</span>
			   </div>
			 </div>
		  </div>
		  <!--<c:forEach items="${relatedDocumentList}" var="list">
				  <a href="${list.viewUrl}"><img src="${list.documentDataMap.get('productSmallImage').dataValue}" /></a>
		        </c:forEach>-->
		   <div class="box_container padbtm50 viewlist martop30">
			<div class="title">相关视频</div>
		<ul class="list4 martop45 wid-80">
		  <c:forEach items="${relatedDocumentList}" var="list" begin="0" end="4">
			    <li class="ff">
			    <a href="${list.viewUrl}">
			       <div style="position: relative;">
			    		<img src="${list.documentDataMap.get('productSmallImage').dataValue}" />
			           <div style="z-index: 99;width:51px;height: 51px;margin-top:-59%; position: absolute!important ;margin-left: 74px;background-image: url(../../../image/play.png);" ></div>
			    	</div>
				   </a>
				</li>
			</c:forEach>
		</ul>
	</div>
			 
			 
           <div class="review">
			<form >
		  <input type="hidden"  value="${document.udid}" id="udid"/> 
		 	<textarea id="review_text" placeholder="看后感想..."></textarea>
			<p> <input type="button" value="发表" onclick="publishSubmit()" id="abc" class="thoughts" /></p>
		 </form >
		    
		  </div>
		   <div class="viewlist2 martop302">
		     <div class="title">评论(${fn:length(commentList)})</div>
			 <div class="viewed2 martop302">
			 <div class="box_container_left2">
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
			<li class="c-bottom">${c.data.userRealName}</li>
			</ul>
			</div>
				<div class="box_container_right2">
				<ul>
				   <li class="c-top"><span class="heading">${c.content}</span></li>
				   <li class="c-bottom"><span class="datetime"><fmt:formatDate value="${c.createTime}"  type="both"/></span></li>
				   </ul>
				</div>
			 </li>
			</c:forEach>
			</c:forEach> 
			</div>


	    </div>
			
		  </div>
		<div class="martop10"></div>
</div>
 <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
	<script type="text/javascript">
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3Faca36f9d36d03a04d61c93f28c896386' type='text/javascript'%3E%3C/script%3E"));
</script>
</html>
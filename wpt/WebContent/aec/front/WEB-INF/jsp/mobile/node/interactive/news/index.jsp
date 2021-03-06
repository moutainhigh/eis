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
<title>以先</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/shicaixinyangdetail.css"/>
<script type="text/javascript" src="/js/mobile/jquery.min.js"></script>
<script type="text/javascript" src="/js/mobile/common.min.js"></script>
<script type="text/javascript" src="/js/mobile/shicaixinyangdetail.js"></script>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>食材信仰</span><a class="list1" onclick="vanish()"></a>
 </div>
	<div id="wrapper_1">
		<%@include file="/WEB-INF/jsp/include/BtnList.jsp" %>
		<div class="wid90 title">
			<span style="font-size: 20px">${document.title}</span>
		</div>
	   <div class="box-yx wid90">
	      ${document.content}
	   </div>	   
		 <div class="box_title">
			<div class="wid90">
				<ul class="yuedulist">
					<li>阅读<span>${readCount}</span></li>
					<li><c:choose><c:when test="${document.documentDataMap.get('praised').dataValue==true}"><img src="../../../../image/mobile/header/preview_unlike_icon.png" style="width:20px;height: 20px;margin-top:-5%;" class="favorited" onClick="unlike(${document.udid})"></c:when><c:otherwise><img src="../../../../image/mobile/header/preview_unlike_icon1.png" style="width:20px;height: 20px;margin-top:-5%;" class="favorite" onClick="like(${document.udid})"></c:otherwise></c:choose><span>${praiseCount}</span></li>
					<li><c:choose><c:when test="${document.documentDataMap.get('favorited').dataValue==true}"><img src="../../../../image/mobile/header/praiseChecked.png" style="width:20px;height: 20px;margin-top:-5%;" class="praised" onClick="uncollection(${document.udid})"></c:when><c:otherwise><img src="../../../../image/mobile/header/icon.png" style="width:20px;height: 20px;margin-top:-5%;" class="praise" onClick="collection(${document.udid})"></c:otherwise></c:choose><span>${favoriteCount}</span></li>
				</ul>
			</div>
	     </div>
	      <div class="line"></div>
	 	 <div class="fapiao">
	        <div class="wid90">
				<div class="box_left martop10">
					<span>上一篇：<a href="${lastDocument.viewUrl}">${fn:substring(lastDocument.title ,0,7)}${fn:length(lastDocument.title)>7?"..":""}</a></span>
					<span class="orange">|</span>
					<span>下一篇：<a href="${nextDocument.viewUrl}">${fn:substring(nextDocument.title ,0,7)}${fn:length(nextDocument.title)>7?"..":""}</a></span>
				</div>
			</div>
	     </div>

	      <div class="comment_title martop40">
			<div class="wid90" style="text-align:center">
					<div class="linellae"></div>	
					<div class=" write_comment"><span style="margin:0 auto;">写评论</span></div>
				   <div class="linellae1"></div>
			</div>
	      </div>
	      <div class="comment_box">
	        <form>
				<input type="hidden" id="udid" value="${document.udid}">
			   <textarea placeholder="评论将由以先后台筛选后显示，对所有人显示" maxlength=400 id="suggestContent" style="width: 90%;height: 100px;margin-left: 5%; margin-top:2%;outline:none;"></textarea>			  
			  <p><input type="button" value="提交" onclick="publishSubmit()"/></p>
	        </form>
	      </div>	   
  </div>
  <div class="wid90" style="text-align:center">
			<div class="linellae"></div>	
			<div class=" write_comment"><span style="margin:0 auto;">精选评论</span></div>
			<div class="linellae1"></div>
	  </div>
	  <ul class="buy-list wid90">
	    <c:forEach var="comment" items="${commentList}">
		<c:forEach var="i" items="${comment}" varStatus="status">
		<c:if test="${status.index==0}">
	    <li>
		</c:if>
		<c:if test="${status.index!=0}">
		<li style="margin-left:8%;">
		</c:if>
		   <div class="ware_img">
			<c:choose>
			  <c:when test="${!empty i.data.userHeadPic}">
				<c:if test="${fn:indexOf(i.data.userHeadPic,'http://')!=-1}">
					<img src="${i.data.userHeadPic}">
				</c:if>
				<c:if test="${fn:indexOf(i.data.userHeadPic,'http://')==-1}">
					<img src="/static/userUploadDir/${i.data.userHeadPic}">
				</c:if>
			  </c:when>
			  <c:otherwise>
				<img src="../../image/header.png">
			  </c:otherwise>
			</c:choose>		   
		   </div>
		   <div class="box_right2">
			  <div> <span class="ware_names">${i.data.userRealName}</span><span ><img src="../../../../image/mobile/header/preview_unlike_icon1.png" style="width: 25px;height: 25px;float: right;display:none;"></span></div>
			  <div class="divCenter"> <span class="ware_from"><c:if test="${!empty i.title}">${i.title}:</c:if>${i.content}</span></div>
              <div class="one_sprice"><span class="price_mark"><fmt:formatDate value="${i.createTime}"  type="both"/></span> <span class="fare orange" style="float:right;display:none;">删除</span></div>
			  </div>
		</li>
		 </c:forEach>
		</c:forEach>
	  </ul>
	  <script type="text/javascript">
	  	function  vanish(){
   			if(document.getElementById("nav_list").style.display=="none"){
    		document.getElementById("nav_list").style.display="block";
   			}
			else{
    		document.getElementById("nav_list").style.display="none";
    		}
}
	  </script>
</body>
</html>    
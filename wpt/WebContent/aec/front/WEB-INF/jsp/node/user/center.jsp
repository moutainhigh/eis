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
<link rel="stylesheet" type="text/css" href="../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../css/pcenter.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>
<script>
  // if(Cookie.getCookie("eis_username")==null){

  //       location.href = "/content/user/login.shtml";

  //   }
  if(Cookie.getCookie("eis_username")==null){

        if(Cookie.getCookie("eis_username")==null){
        alert("您还没有登录");
        location.href = "/content/user/login.shtml";

    }

    }
	
	function delCollect(e){
			$.ajax({
            type:"POST",
            url: '/userRelation/delete.json',
            data:{
               userRelationId:e
            },
            dataType:'json',

            success:function (data) {
                alert("删除成功!");
                location.reload();
            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		}
</script>
</head>
<body>
   <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50 padbtm50">
     <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox">
	   <div class="myinfo">
	      <div class="boxLeft">
	      	  
		      <p>
				<c:choose>
					<c:when test="${!empty frontUser.userConfigMap.get('userHeadPic').dataValue}">
						<c:if test="${fn:indexOf(frontUser.userConfigMap.get('userHeadPic').dataValue,'http://')!=-1}">
							<img src="${frontUser.userConfigMap.get('userHeadPic').dataValue}">
						</c:if>
						<c:if test="${fn:indexOf(frontUser.userConfigMap.get('userHeadPic').dataValue,'http://')==-1}">
							<img src="/static/userUploadDir/${frontUser.userConfigMap.get('userHeadPic').dataValue}">
						</c:if>
					</c:when>
					<c:otherwise>
						<img src="../../image/header.png">
					</c:otherwise>
				</c:choose>
			  </p>
		   
			 <p><a href="/content/user/changemobile.shtml" class="fo-13">编辑个人资料</a></p>
		  </div>
		  <div class="boxCenter">
		  </div>
		  <div class="boxRight">
		    <p class="fo-17">您好！${frontUser.nickName}!</p>
			<p>我的昵称：<span class="orange">${frontUser.nickName}</span></p>
			<p>手机号码：<span class="orange">${frontUser.username}</span></p>
			<p>我的账户余额：<span class="orange"><c:choose><c:when test="${!empty money.chargeMoney}">${money.giftMoney}</c:when><c:otherwise>0</c:otherwise></c:choose></span></p>
			<p>我的代金券余额：<span class="orange"><c:choose><c:when test="${!empty money.giftMoney}">${money.giftMoney}</c:when><c:otherwise>0</c:otherwise></c:choose></span></p>
			<!--<p>邮箱：<span class="orange"></span></p>-->
			<p>我的收货地址：<span class="orange">${addressBook.province} ${addressBook.city} ${addressBook.address}</span></p>
		  </div>
	   </div>
	   <div class="myviews">
	      <div class="title">
		    <div class="box_container_left">我的评论</div>
			<div class="box_container_right orange fo-13"><a href="/comment/index.shtml?page=1&rows=10">更多>></a></div>
		  </div>
		  <ul class="writings">
		    <c:forEach var="commention" items="${comment}" begin="0" end="2" varStatus="i"> 
		     <li>
			     <a href="${commention.data.refUrl}"><img src="${commention.data.refImage}"  class="box_container_left"/></a>
				<div class="box_container_right">
				   <span class="heading">${commention.content}</span>
				   <span class="datetime"><fmt:formatDate value="${commention.createTime}"  type="both"/></span>
				</div>
			 </li>
			</c:forEach>
			
		  </ul>
	   </div>
	   
		
	   <div class="mycollect martop50">
	      <div class="title">
		    <div class="box_container_left">我的收藏</div>
			<div class="box_container_right orange fo-13"><a href="/content/user/morefavorites.shtml?favPage=1&favRows=8">更多>></a></div>
		  </div>                                  
		  <ul>
		    <c:forEach var="favorite" items="${favoriteList}" begin="0" end="3" varStatus="i">
			
		    <li>
			  <a href="${favorite.data.get('refUrl')}"><img src="${favorite.data.get('refImage')}"></a>
			  <a href="${favorite.data.get('refUrl')}"><span class="row">${favorite.data.get('refTitle')}</span></a>
			  <span class="row fo-13">${favorite.data.get('refBrief')}</span> 
			  <span class="row"><span class="box_container_left fo-12"><fmt:formatDate value="${favorite.createTime}"  type="both"/></span><a class="box_container_right fo-13 orange" href="#" onclick="delCollect(${favorite.userRelationId})">删除</a></span>
			</li>
			
			</c:forEach>
		  </ul>
	   </div>
	 </div>
   </div>
    <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	  <div class="modal" style="display:none;">
      <span>确认删除？</span>
	  <span><a class="orange" class="btn_confirm">确认</a><a class="orange" class="btn_cancel">取消</a></span>
   </div>
</body>
</html>
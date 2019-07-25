<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../css/main.css">

<link rel="stylesheet" type="text/css" href="../../css/mycollect.css">
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="../../js/common.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
  if(Cookie.getCookie("eis_username")==null){

        location.href = "/content/user/login.shtml";

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
   <div class="wid-80 martop50 minH555">
   
    <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox">
	   <div><a href="#">账户中心</a>><a href="#">我的收藏</a></div>
	   <div class="mycollect martop10">
		  <ul class="co_list">
		    <c:forEach var="favorite" items="${favoriteList}" varStatus="i">
		     <li>
			  <!-- <a href="${favorite.data.get('shareUrl')}"><img src="${fn:split(favorite.data.get('productSmallImage'),',')[i.index]}"></a>
			  <a href="${favorite.data.get('shareUrl')}"><span class="row">${favorite.data.get('productName')}</span></a>
			  <a href="${favorite.data.get('shareUrl')}"><span class="row fo-13">${favorite.data.get('productBrief')}</span></a>  -->  
        <a href="${favorite.data.get('refUrl')}"><img src="${favorite.data.get('refImage')}"></a>
        <a href="${favorite.data.get('refUrl')}"><span class="row">${favorite.data.get('refTitle')}</span></a>
        <span class="row fo-13">${favorite.data.get('refBrief')}</span>
			  <span class="row"><span class="box_container_left fo-12"><fmt:formatDate value="${favorite.createTime}"  type="both"/></span><a class="box_container_right fo-13 orange" href="#" onclick="delCollect(${favorite.userRelationId})">删除</a></span>
			</li>
			</c:forEach>
			
			</ul>
			<%@include file="/WEB-INF/jsp/include/page.jsp" %>
	   </div>
	 </div>
   </div>
   <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
   <!--<div class="modal" style="display:none;">
      <span>确认删除？</span>
	  <span><a class="orange">确认</a><a class="orange">取消</a></span>
   </div>-->
</body>
</html>
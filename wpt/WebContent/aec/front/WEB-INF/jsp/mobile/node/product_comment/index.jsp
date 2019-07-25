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
<link rel="stylesheet" type="text/css" href="../css/main.css">

<link rel="stylesheet" type="text/css" href="../css/myapprise.css">
<link rel="stylesheet" type="text/css" href="../css/style.css">
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/popuppicture.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script type="text/javascript" src="../js/jquery-1.11.0.min.js"> </script>
<script type="text/javascript" src="../js/jquey-bigic.js"> </script>
<script  type="text/javascript" src="../js/common.js"></script>
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
		$(function(){
			$('img').bigic();
			$(".appriseImg li img").click(function(){
				if($("#layer2").css('display')=="none"){
					$("#layer2").css('display','block');
				}
				 $("#layer2 img").attr("src",$(this)[0].src);
			})
			
			$("#layer2").click(function(){
				$(this).css("display","none");
			})
		})
 

</script>
<style>
.u_img{
	width: 40px!important;
    height: 40px!important;
    margin-top: 20px;
}
</style>
</head>
<body>
  <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
   <div class="wid-80 martop50 minH555 padbtm50">
     <%@include file="/WEB-INF/jsp/include/pmenu.jsp" %>
	 <div class="rightbox">
	   <div><a href="/content/user/pcenter.shtml">个人中心</a>><a href="#">我的评价</a></div>
	         <div class="viewed martop30 viewed2" style="border:1px solid #FEAD78;padding-bottom:150px;">
			    <ul class="title_sty">
					<li class="documentInfo"><p>商品信息</p></li>
					<li class="appraiseConnent"><p>评论内容</p></li>
					<li class="appraiseTime"><p>时间</p></li>
				</ul>
			 
			  <c:forEach var="commention" items="${commentList}"> 
			<div class="viewed martop10">
				<ul>
					<li class="appraiseImg"><a href="${commention.data.refUrl}"><img src="${commention.data.refImage}"/><span class="textContent">${commention.data.refTitle}</span></a></li>
					<!-- <li class="documentName">${commention.data.refTitle}</li> -->
					<li class="appraiseConnent">
					${commention.content}
					<div>
						<ul style="border-bottom:0px;">
					    <c:forEach var="img_src" items="${fn:split(commention.data.get('productGallery'),',')}">
							<c:if test="${!empty img_src }">
								<li><img src="/static/userUploadDir/${img_src}" class="u_img"/></li>
							</c:if>				           
						</c:forEach>
					   </ul>
					</div>
					</li>
					<li class="appraiseTime"><fmt:formatDate value="${commention.createTime}"  type="both"/></li>
				</ul>
			 </div>
			</c:forEach>		
			 </div>
			  <%@include file="/WEB-INF/jsp/include/page.jsp" %>
	  </div>
   </div>
   <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
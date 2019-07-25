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
<script  type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../js/respond.src.js"></script>
<script  type="text/javascript" src="../js/common.js"></script>
<script>
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
   <div class="wid-80 martop50">
     <ul class="leftMenu">
	    <a href="#"><span class="orange">我的以先</span></a>
		<a href="#"><span>交易管理</span></a>
		<a href="#"><li>我的订单</li></a>
		<a href="#"><li>收货地址</li></a>
		<a href="#"><li>我的评论</li></a>
		<a href="#"><li>我的收藏</li></a>
		<a href="#"><span class="orange">账户中心</span></a>
		<a href="#"><li>个人信息</li></a>
	 </ul>
	 <div class="rightbox">
	   <div><a href="#">账户中心</a>><a href="#">我的评价</a></div>
	   <div class="viewed martop30">
			     <p>
				     <img src="../image/logo.png"/>
				     <span class="view_con">这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃这个鱼特别好吃</span>
				 </p>
				 <p><span class="view_header">白衣魔鬼</span><span class="view_con">2016-02-03 10:15 <a href="#" class="orange marleft50">赞(201)</a><a href="javascript:void()" class="orange marleft50" id="btnReply">回复(103)</a></span></p>
				 <div class="review1">
					<textarea id="review_text" placeholder="回复该评论..."></textarea>
					<a href="#" class="orange btnReview">发表</a>
				</div>
			 </div>
	 </div>
   </div>
   <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>
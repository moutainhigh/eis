<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="农青" />
<meta name="description" content="农青" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/style.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css">
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/center.css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../theme/${theme}/js/respond.src.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/jquery-1.11.0.min.js"> </script>

<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/jquey-bigic.js"> </script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
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

$(function(){
		$('img').bigic();
	});
		
</script>
</head>
<style>
	.content .zuic{
		background-color: #fff;
	}
</style>
<body>

  <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span >个人中心</span><a class="set" href="/content/user/setting.shtml"></a>
	</div> 

   <div class="wrapper_1">
      <div class="headphoto">
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
			<img src="../../../theme/${theme}/image/header.png">
		</c:otherwise>
	  </c:choose>
	  <div class="username">${frontUser.nickName}  <a href="javascript:logout();">退出</a> </div>
	  </div>
	  
	  <div class="qita">
			<ul>
				<li><a href="/content/user/morefavorites.shtml" class="more"><span>50</span><span>收藏夹</span></a></li>
				<li class="txt1"><a href="/content/user/mycoupon.shtml"><span>10<em style="font-size: 10px;font-style: normal;">张</em></span><span>代金券</span></a></li>
				<li class="txt2">
				<a href="/cart.shtml"><span>${cartCount}</span><span>购物车</span></a></li>
			</ul>
	  </div>
	  <div class="shic">
				<h2 class="bglines"><div><a href="/order/index.shtml?page=1&rows=10">我的订单</a></div> </h2>
				<ul class="waitlogo clearfix">
				    <a href="/order/index.shtml?page=1&rows=10">
				    	<li class="allorder">
				    		<img src="../../../theme/${theme}/image/mobile/order4.png">
				    		<p class="fo14">全部订单</p>
				    	</li>
				    </a>
					<a href="/order/index.shtml?currentStatus=710019&page=1&rows=10">
						<li class="allorder">
							<img src="../../../theme/${theme}/image/mobile/order1.png">
							<p class="fo14">待付款</p>
							<c:if test="${!empty waitPayCount}">
								<span class="orderIcon fo12">${waitPayCount}</span>
							</c:if>
						</li>
					</a>
					<a href="/order/index.shtml?currentStatus=710050&page=1&rows=10">
						<li class="allorder">
							<img src="../../../theme/${theme}/image/mobile/order3.png">
							<p class="fo14">待收货</p>
							<c:if test="${!empty waitGoodsCount}">
							<span class="orderIcon fo14">${waitGoodsCount}</span>
							</c:if>
						</li>
					</a>
					<a href="/order/index.shtml?currentStatus=710052&page=1&rows=10">
						<li class="allorder"><img src="../../../theme/${theme}/image/mobile/order2.png">
							<p class="fo14">待评价</p>
							<c:if test="${!empty waitPayCount}">
							<span class="orderIcon fo14">${waitCommentCount}</span>
							</c:if>
						</li>
					</a>
				</ul>
	  </div>
	  <div class="content">
			<h2 class="list">
				<span >我的评论</span>
				<div class="shicailink">
					<a href="/content/user/mycomment.shtml" class="more">更多></a>					
				</div>
			</h2>
			<ul class="favoriteList" style="display:none;">
				<c:forEach var="commention" items="${comment}"  begin="0" end="3"  varStatus="i"> 	
					<li style="height:auto;border-bottom: 1px solid #dedede;">
						<div class="afbox">
							<div class="zuic">
								<a href="${commention.data.refUrl}"><img src="/file/${commention.data.refImage}" style="width:100%;height:auto;"></a>
							</div>
							<div class="zhtw">                           
								<h3 class="plain1" style="padding:5px 0;color:#92B431;font-size:14px;">文章标题：<a href="${commention.data.refUrl}">${commention.title}</a></h3>
								<h3 class="plain1 mt15"  style="padding:5px 0;color:#92B431;font-size:14px;">文章内容：${commention.content}</h3>
								<c:if test="${!empty commention.data.get('productGallery')}">
									<ul class="img-list">
										<c:forEach var="img" items="${fn:split(commention.data.get('productGallery'),',')}">
											<li><img src="/static/userUploadDir/${img}"></li>
										</c:forEach>									
									</ul>
								</c:if>
								<div class="other" style="margin-bottom: 5px;"><div><fmt:formatDate value="${commention.createTime}"  type="both"/></div>   </div>
							</div>
						</div>                      
					</li>
				</c:forEach>
			</ul>
			<div style="clear:both;"></div>
		</div>
		<!-- 最新物流 -->
		<div class="content">
			<div class="logistics">
				<ul>
					<li><a href="#">
						<div class="left">
							<span>最新物流</span>
							<span>09:25</span>
						</div>
						<div class="right">
							<img src="../../../theme/${theme}/image/mobile/video2.png">
							<div class="text">
								<span>运输中</span>
								<span>由【湖北武昌中转部】发往【湖北武汉白沙洲公司】</span>
							</div>
						</div>
					</a></li>
					<li><a href="#">
						<div class="left">
							<span>最新物流</span>
							<span>09:25</span>
						</div>
						<div class="right">
							<img src="../../../theme/${theme}/image/mobile/video2.png">
							<div class="text">
								<span>运输中</span>
								<span>由【湖北武昌中转部】发往【湖北武汉白沙洲公司】</span>
							</div>
						</div>
					</a></li>
					<li><a href="#">
						<div class="left">
							<span>最新物流</span>
							<span>09:25</span>
						</div>
						<div class="right">
							<img src="../../../theme/${theme}/image/mobile/video2.png">
							<div class="text">
								<span>运输中</span>
								<span>由【湖北武昌中转部】发往【湖北武汉白沙洲公司】</span>
							</div>
						</div>
					</a></li>
					<li><a href="#">
						<div class="left">
							<span>最新物流</span>
							<span>09:25</span>
						</div>
						<div class="right">
							<img src="../../../theme/${theme}/image/mobile/video2.png">
							<div class="text">
								<span>运输中</span>
								<span>由【湖北武昌中转部】发往【湖北武汉白沙洲公司】</span>
							</div>
						</div>
					</a></li>
					<li><a href="#">
						<div class="left">
							<span>最新物流</span>
							<span>09:25</span>
						</div>
						<div class="right">
							<img src="../../../theme/${theme}/image/mobile/video2.png">
							<div class="text">
								<span>运输中</span>
								<span>由【湖北武昌中转部】发往【湖北武汉白沙洲公司】</span>
							</div>
						</div>
					</a></li>
				</ul>
			</div>
			<div class="line" style="width: 100%;height: 5px;background-color: #eee;"></div>
		</div>
		<!--<div class="content">
			<h2 class="list">
				<span>支付中心</span>
				<div class="shicailink">
					<a href="/content/pay/index.shtml" class="more">更多></a>
				</div>
			</h2>
		</div>-->
		<!-- <div class="content">
			<h2 class="list">
				<span >帮助中心</span>
				<div class="shicailink">
					<a href="/content/help/index.shtml" class="more">更多></a>
				</div>
			</h2>
		</div> -->
   </div>
	<script type="text/javascript">
		$(function(){
			$('.list').each(function(){
				$(this).click(function(){
					$(this).siblings().toggle();
				})
			})

			$('.more').click(function(e){
				e.stopPropagation();//阻止向上冒泡
			})
			// 物流
			var wuliuli = $('.content .logistics ul li').outerHeight(true);
			$('.content .logistics').css('height',wuliuli*2+'px');
			function xunhuanwl(){
				var ulH = $('.content .logistics ul').outerHeight(true);
				var logH = wuliuli*2;
				var shu = Math.floor(ulH/logH);
				var ulTop = $('.content .logistics ul').position().top;
				setInterval(function(){
					var actmv = 0 + logH;
					if (ulTop == -(logH*shu)) {
						ulTop=0;
					}else{
						ulTop += (-logH)
						console.log('2')
					}
					$('.content .logistics ul').css('top',ulTop);
				},3000)
			}
			xunhuanwl();
			
		})
	</script>
    <%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
</html>
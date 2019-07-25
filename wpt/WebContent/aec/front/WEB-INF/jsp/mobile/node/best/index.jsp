<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>以先</title>
<link rel="stylesheet" type="text/css" href="../../css/mobile/main.css">
<link rel="stylesheet" type="text/css" href="../../css/mobile/index.css">
<link rel="stylesheet" type="text/css" href="../../../../../css/mobile/list1.css"/>
<script  type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script  type="text/javascript" src="/js/mobile/common.min.js"></script>
<script>
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
		isImgLoad(function(){imgAdaption($(".box_yidiyipin img"),$(".box_yidiyipin .shadow .imgLink"))},$('.box_yidiyipin img'));		
	})
</script>
<style>
	.btn-list a{
		display: block;
		height: 25px;
		width: 49%;
		float: left;
	}
	.btn-list li {		
		float:none;
		width:100%;
	}
	.hidden{
		display:none;
	}
</style>
</head>
<body>
   <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>以先优选</span><a class="list1" onclick="vanish()"></a>
	</div>
   <div id="wrapper_1">
		<%@include file="/WEB-INF/jsp/include/BtnList.jsp" %>
         <div class="box-yx">
	      <img src="../../../../image/mobile/video.png"/>
	    </div> 

        
		 <ul class="btn-list">
		   <a href="/content/best/index.shtml?subtitle=suggest"><li id="xwbd" class="current ">优选推荐</li></a>
		   <a href="/content/best/index.shtml?subtitle=presale"><li id="hzsq" class="no-border">优选预售</li></a>
		 </ul>
		 <!-- 小先推荐 -->
		
		<div class="box_yidiyipin xwbd">
			   <ul>
				<c:forEach var="suggest" items="${youxuantuijianList}" varStatus="status">
				<c:if test="${suggest.documentTypeCode!= 'image'}">
			     <li>
				    <div class="shadow">
					    <div><a href="${suggest.viewUrl}" class="imgLink"><img src="${suggest.documentDataMap.get('productSmallImage').dataValue}" /></a></div>
					    <p class="plain marleft suggestTitle"><a href="${suggest.viewUrl}" class="oneline">${suggest.title}</a></p>
						<div class="box_left marleft data">
							<p class="fo13 oneline">产地：${suggest.documentDataMap.get('productOrigin').dataValue}</p>
							<p class="fo13 oneline">规格：${suggest.documentDataMap.get('goodsSpec').dataValue}</p>
						</div>
						<div class="box_right marleft price">
							<span class="orange">单价：${fn:replace(suggest.documentDataMap.get('productBuyMoney').dataValue,"money:","￥")}</span>
						</div>
					</div>
				 </li>
				 <c:if test="${status.index%2!=0}"><div style="clear:both;"></div></c:if>
				 </c:if>
				</c:forEach>
			   </ul>
		  </div>
		<div class="box_sk hzsq " style="display: none;">
			<c:forEach var="tuan" items="${tuanList}">
			<c:if test="${tuan.documentTypeCode!= 'image'}">
			<div class="box_product">
			   <a href="${tuan.viewUrl}"><img src="${tuan.documentDataMap.get('productSmallImage').dataValue}"/></a>
			   <div class="product-info">                           
                    <p class="plain"><a href="${tuan.viewUrl}" style="display:block;" class="oneline">${tuan.title}</a></p>
					<p class="fo13 oneline">规格：${tuan.documentDataMap.get('goodsSpec').dataValue}</p>
					<p class="fo13 oneline" >原价：<span class="old_sprice">￥${tuan.documentDataMap.get('productMarketPrice').dataValue}</span></p>                               
					<div class="box_left">
						<span class="orange">${fn:replace(tuan.documentDataMap.get('productBuyMoney').dataValue,"money:","￥")}</span>								 
					</div>
					<div class="box_right">
						<a href="${tuan.viewUrl}" class="orangeButton1 fo12">立即购买</a>
					</div>							
                </div>
			</div>
			</c:if>
			</c:forEach>
		 </div>
 </div>
    <%@include file="/WEB-INF/jsp/include/footer.jsp" %>
	<script type="text/javascript">
$(function(){	
	var $xwbd = $('#xwbd');
    var $hzsq = $('#hzsq');
	switch(testUrl("subtitle")){
		case 'presale':
			$xwbd.removeClass('current');
			$hzsq.addClass('current');
			$('.xwbd').css('display','none');
			$('.hzsq').css('display','block');
			isImgLoad(function(){imgAdaption($(".box_yidiyipin img"),$(".box_yidiyipin .shadow .imgLink"))},$('.box_yidiyipin img'));			
		break;
		case 'suggest':
			$hzsq.removeClass('current');
			$xwbd.addClass('current');       
			$('.hzsq').css('display','none');
			$('.xwbd').css('display','block'); 		
			isImgLoad(function(){imgAdaption($(".box_yidiyipin img"),$(".box_yidiyipin .shadow .imgLink"))},$('.box_yidiyipin img'));
		break;		
		default:
			$hzsq.removeClass('current');
			$xwbd.addClass('current');       
			$('.hzsq').css('display','none');
			$('.xwbd').css('display','block'); 	
	}
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
</body>
</html>
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

<link rel="stylesheet" type="text/css" href="../../../../css/newCss/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/productdetail.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/jquery.spinner.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/myapprise.css"/> 
<link rel="stylesheet" type="text/css" href="../../../../css/style.css">
<script type="text/javascript" src="../../../../js/jquery.min.js"></script>
<script src="../../../../js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="../../../../js/common.js"></script>
<script type="text/javascript" src="../../../../js/jquery.spinner.js"></script>
 <script type="text/javascript" src="../../../../js/popuppicture.js"></script>
 <script type="text/javascript" src="../../../../js/jquey-bigic.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../../js/respond.src.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

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
</head>
<body>
 
   <div class="wid-100">
   	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
	<div class="page_title fo-14">
	<ul class="wid-80">
	<li><a href="/">首页</a></li>
	<li><a href="/content/product/index.shtml">以先商城</a></li> 
	<li><a href="javascript:history.go(-1)">${node.name}</a></li> 
	<li><a href="#" class="orange">${fn:substring(document.title ,0,14)}${fn:length(document.title)>14?"..":""}</a></li></ul></div>
    
    <div class="wid-80">
	
	<div class="box_ware">
		    <div class="box_left">  <div class="preview">
	<div id="vertical" class="bigImg">
		  
          <!-- <c:if test="${!empty document.documentDataMap.get('goodsShowImg1').dataValue}">
           <img src="${document.documentDataMap.get('goodsShowImg1').dataValue}" alt=""  class="ware" id="midimg">
          </c:if> -->
		   <c:set var="img0" value="${fn:split(document.documentDataMap.get('productGallery').dataValue,',')[0]}"/>
			<c:set var="img0_all" value="/static/productFile/open/${img0}" />
			<c:if test="${!empty img0_all}">
				<img src="${img0_all}" alt=""  class="ware" id="midimg" />
			</c:if>
          
		<div style="left: 0px; top: 112px; display: none;" id="winSelector"></div>
	</div><!--bigImg end-->	
	<div class="smallImg">
		<div class="scrollbutton smallImgUp disabled"></div>
		<!--<div id="imageMenu">
			<ul class="">			
			<li>
				<c:set var="img0" value="${fn:split(document.documentDataMap.get('productGallery').dataValue,',')[0]}"/>
				<c:set var="img0_l" value="${fn:substring(img0,0,21)}" />
				<c:set var="img0_r" value="${fn:substring(img0,21,74)}" />
				<c:set var="img0_all" value="/static/productFile/open/${img0_r}" />
				<c:if test="${!empty img0_all}">
					<img src="${img0_all}" alt=""  class="ware" />
				</c:if>
			</li>
			<li>
				<c:set var="img1" value="${fn:split(document.documentDataMap.get('productGallery').dataValue,',')[1]}"/>
				<c:set var="img1_l" value="${fn:substring(img1,0,21)}" />
				<c:set var="img1_r" value="${fn:substring(img1,21,74)}" />
				<c:set var="img1_all" value="/static/productFile/open/${img1_r}" />
				<c:if test="${!empty img1_all}">
					<img src="${img1_all}" alt="" />
				</c:if>
			</li>
			<li>
				<c:set var="img2" value="${fn:split(document.documentDataMap.get('productGallery').dataValue,',')[2]}"/>
				<c:set var="img2_l" value="${fn:substring(img2,0,21)}" />
				<c:set var="img2_r" value="${fn:substring(img2,21,74)}" />
				<c:set var="img2_all" value="/static/productFile/open/${img2_r}" />
				<c:if test="${!empty img2_all}" >
					<img src="${img2_all}" alt="" />
				</c:if>
			</li>
		</ul>
		</div>-->
		<div id="imageMenu">
			<ul class="">
			<c:if  test="${!empty document.documentDataMap.get('productGallery').dataValue}">
			<c:set var="gallery" value="${fn:split(document.documentDataMap.get('productGallery').dataValue,',')}"/>
			<c:forEach var="i" items="${gallery}">	
				<li> 
					<img src="/static/productFile/open/${i}" alt=""  class="ware">
				</li>
			</c:forEach>
			</c:if>
			</ul>
		</div>
		<div class="scrollbutton smallImgDown"></div>
	</div><!--smallImg end-->	
	<div id="bigView" style="width: 313.856427378965px; height: 229.425px; top: 180px; left: 1064.5px; display: none;"><img alt="" width="800" height="800" src="../../../../image/product/img1.jpg" style="left: 0px; top: -122.36px;"></div>
</div>

		</div>
        <div class="box_right">
			<div class="ware_name ">
			<p class="fo-26 fow-b">${fn:substring(document.title ,0,14)}${fn:length(document.title)>14?"..":""}</p>
			<p class="fo-14">${document.documentDataMap.get("subtitle").dataValue}</p>
			</div>
			<div class="ware_sprice" style="padding-left:15px;">
			   <p class="fo-14">以先价：<span class="red">￥<span class="fo-26 fo_mA">${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</span></span></p>
			   <p class="fo-12">市场价：<span>￥<span class="fo_mA">${fn:replace(document.documentDataMap.get("productMarketPrice").dataValue,"money:","")}	</span></span></p>
			</div>
			<div class="ware_from fo martop30 pa8">
			<span class="text_left">
			产地：
			</span>
			${document.documentDataMap.get("productOrigin").dataValue}
			</div>
			<div class="ware_format fo pa8">
			<span class="text_left">规格：</span>
			${document.documentDataMap.get("goodsSpec").dataValue}
			</div>
			<div class="count fo pa8">
			<span class="text_left">数量：</span>
                <c:if test="${document.documentDataMap.get('availableCount').dataValue=='0'}">
                <span class="orange lineH50">已售完</span>
                </c:if>
                <input type="text" class="spinner" id="productCount"/>
			   <input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode"/>	
			</div>
            <div class="ware_format   pa8 fo">发货地：${document.documentDataMap.get("deliveryFromArea").dataValue}<span style="font-size:12px;"></span></div>
            
			 <c:choose>
      			<c:when test="${document.documentDataMap.get('availableCount').dataValue!='0'}">
    			 <div class="btns btnG martop50  pa8">
				<input type="button" value="加入购物车" class="add_cart"/>
				<input type="button" value="立即购买" class="btn_buy" onclick="buynow()"/>
				</div> 
	  			</c:when> 
      		<c:otherwise> 
    		<div class="btnG martop50 pa ">
				<input type="button" value="加入购物车" class="add_cart">
				<input type="button" value="立即购买" class="btn_buy" style="background-color:#ccc">
			</div> 
	        </c:otherwise>
            </c:choose>
		</div>	
	</div>

		
	<div class="ware_intro">
	   <div class="box_container_left">
	    
	     
	      <div class="fo-16 recomT">热门推荐</div>
		  
		   <c:forEach items="${relatedDocumentList}" var="list" begin="0" end="4">
				<a href="${list.viewUrl}"><img src="${list.documentDataMap.get('productSmallImage').dataValue}"/>
				<div class="p_recom">
				<p>${list.title}</p>
				<p>￥${fn:replace(list.documentDataMap.get("productBuyMoney").dataValue,"money:","")}</p>
				</div>
				</a>
		   </c:forEach>
	       
	   </div>
	   <div class="box_container_right">
	    <div class="btn_list">
	         <a  id="cpxq" class="current marleft50">商品详情</a>
		     <a  class="marleft50" id="yhpj">累计评价(${fn:length(commentList)})</a>
	    </div>
	      <div class="cpxq">
			${document.content}

	      </div>
		  <div class="yhpj"  style="display:none;">
		  <div class="martop20 comm_title">
	    		<span>
	    		<a class="orange" id="all">全部(${fn:length(commentList)})</a></span>
	    		<span><a  class="marleft10" id="good_reputation">好评(<c:choose><c:when test="${!empty goodRankCount}">${goodRankCount}</c:when><c:otherwise>0</c:otherwise></c:choose>)</a> </span>
	    		<span><a  class="marleft10" id="middle_reputation">中评(<c:choose><c:when test="${!empty normalRankCount}">${normalRankCount}</c:when><c:otherwise>0</c:otherwise></c:choose>)</a></span>
	    		<span><a  class="marleft10" id="bad_reputation">差评(<c:choose><c:when test="${!empty badRankCount}">${badRankCount}</c:when><c:otherwise>0</c:otherwise></c:choose>)</a></span>
	    	</div>
			<div class="viewed martop302">
		      	<c:forEach var="comment" items="${commentList}"> 
			 <c:forEach var="c" items="${comment}" >
			 <div class="viewDiv martop20">
			   <div class="view_header">
			        <c:choose>
			  <c:when test="${!empty c.data.userHeadPic}">
				<c:if test="${fn:indexOf(c.data.userHeadPic,'http://')!=-1}">
					<p><img src="${c.data.userHeadPic}"  class="header_photo"></p>
				</c:if>
				<c:if test="${fn:indexOf(c.data.userHeadPic,'http://')==-1}">
					<p><img src="/static/userUploadDir/${c.data.userHeadPic}"  class="header_photo"></p>
				</c:if>
			    </c:when>
			    <c:otherwise>
				<p><img src="../../../../image/header.png" class="header_photo"></p>
			    </c:otherwise>
			   </c:choose>
				<p class="martop15" id="tel" >
				<c:set var="l" value="${fn:length(c.data.userRealName)}"></c:set>			
				${fn:substring(c.data.userRealName,0,1)}***${fn:substring(c.data.userRealName,l-1,l)}
				</p>
				 </div>
				  <div class="view_con">
				    <div class="title">${c.content}</div>
					 <ul class="commentImgList">
					    <c:forEach var="img_src" items="${fn:split(c.data.get('productGallery'),',')}">
							<c:if test="${!empty img_src }">
								<li><img src="/static/userUploadDir/${img_src}" /></li>
							</c:if>				           
						</c:forEach>
					   </ul>
				 <div class="time fo-12 martop10"><fmt:formatDate value="${c.createTime}"  type="both"/></div>
			     <div id="layer2" style="display:none"> 
  						<img src="/static/userUploadDir/${fn:split(commention.data.get('productGallery'),',')[0]}"/>
			     </div> 
		
			</div>
	    </div>
	      <div class="circulation_line martop10"></div> 
	     </hr>
		</c:forEach>
		</c:forEach> 
		
	</div>
     </div>
	</div>
     </div>
    </div>
    </div>
	</div>
    <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
<script type="text/javascript" src="../../../../js/productdetail.js"></script>
</body>
</html>


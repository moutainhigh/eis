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
<link rel="stylesheet" type="text/css" href="/css/main.css"/>

<link rel="stylesheet" type="text/css" href="../../css/productdetail.css"/>
<link rel="stylesheet" type="text/css" href="../../css/jquery.spinner.css"/>
<script type="text/javascript" src="../../js/jquery.min.js"></script>
<script src="../../js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/jquery.spinner.js"></script>
<script type="text/javascript" src="../../js/productdetail.js"></script>
<script type="text/javascript" src="../../js/popuppicture.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?d2a1a610189ccada91fa1fca8472cddb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
</head>
<body>
 
   <div class="wid-100">
   	<%@include file="/WEB-INF/jsp/include/phead.jsp" %>
    <div class="content">
	<div class="page_title martop30"><a href="#" class="orange "><返回产品列表</a></div>
	<div class="box_ware">
		    <div class="box_left">  <div class="preview">
	<div id="vertical" class="bigImg">
		  
            <c:set var="img0" value="${fn:split(document.documentDataMap.get('productGallery').dataValue,',')[0]}"/>
			<c:set var="img0_all" value="/static/productFile/open/${img0}" />
			<c:if test="${!empty img0_all}">
				<img src="${img0_all}" alt=""  class="ware" id="midimg" />
			</c:if>
          
		<div style="left: 0px; top: 112px; display: none;" id="winSelector"></div>
	</div><!--bigImg end-->	
	<div class="smallImg">
		<div class="scrollbutton smallImgUp disabled"></div>
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
	<div id="bigView" style="width: 313.856427378965px; height: 229.425px; top: 180px; left: 1064.5px; display: none;"><img alt="" width="800" height="437" src="../../image/product/img1.jpg" style="left: 0px; top: -122.36px;"></div>
</div>
		</div>
        <div class="box_right">
			<div class="ware_name pa">
			<span>${fn:substring(document.title ,0,14)}${fn:length(document.title)>14?"..":""}</span>
			</div>
			<div class="ware_sprice pa ">
			单价:
			<span class="new_sprice fo-25">
			￥${fn:replace(document.documentDataMap.get("productBuyMoney").dataValue,"money:","")}
			</span>
			
			</div>
			<div class="ware_from pa fo">
			<span class="text_left">
			产地：
			</span>${document.documentDataMap.get("productOrigin").dataValue}</div>
			<div class="ware_format pa fo">
			<span class="text_left">规格：</span>
			${document.documentDataMap.get("goodsSpec").dataValue}
			</div>
			<div class="count pa fo">
			<span class="text_left">数量：
			</span>
               
                <c:if test="${document.documentDataMap.get('availableCount').dataValue=='0'}"><span class="orange lineH50">已售完</span></c:if>
                <input type="text" class="spinner" id="productCount"/>
			   <input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode"/>

				
			</div>
            <div class="ware_format pa fo">发货地：${document.documentDataMap.get("defaultFromArea").dataValue}</div>
           <!--  <div class="btns btnG pa martop50">
				<input type="button" value="加入购物车" class="add_cart">
				<input type="button" value="立即购买" class="btn_buy">
			</div>  -->
			 <c:choose>
    
      <c:when test="${document.documentDataMap.get('availableCount').dataValue!='0'}">
     <div class="btns btnG pa martop50">
				<input type="button" value="加入购物车" class="add_cart"/>
				<input type="button" value="立即购买" class="btn_buy"/>
			</div> 
	  </c:when> 
      <c:otherwise> 
    <div class="btnG pa martop50">
				<input type="button" value="加入购物车" class="add_cart">
				<input type="button" value="立即购买" class="btn_buy" style="background-color:#ccc">
			</div> 
      
	 </c:otherwise>
       
      </c:choose>
	
		</div>
		
	</div>
	<div class="ware_intro">
	   <div class="box_container_left">
	    
	     
	      <a href="#" class="orange">推荐类似产品</a>
		   <p>
		    <c:forEach items="${relatedDocumentList}" var="list">
				<a href="${list.viewUrl}"><img src="${list.documentDataMap.get('productSmallImage').dataValue}" /></a>
		   </c:forEach>
		   </p>
	       
	   </div>
	   <div class="box_container_right">
	         <a  id="cpxq" class="current">商品详情</a>
		     <a  class="marleft50" id="yhpj">累计评价(${commentCount})</a>
	    	
	      <div class="cpxq">
	        <img alt="白虾干详情图.jpg" src="../../image/product/img4.jpg" title="" class="imgDetail"/>
	      </div>
		  <div class="yhpj"  style="display:none;">
		  <div class="martop20 ">
	    		<span>
	    		<a class="orange" id="all">全部(${commentCount})</a></span>
	    		<span>
	    		 <a  class="marleft10" id="good_reputation">好评(${goodRankCount})</a> 

	    		</span>
	    		<span><a  class="marleft10" id="middle_reputation">中评(${normalRankCount})</a></span>
	    		<span><a  class="marleft10" id="bad_reputation">差评(${badRankCount})</a></span>
	    	</div>
	    	<c:forEach var="comment" items="">
		     <div class="viewed martop45 all ">

			     <div>
				     <img src="../../image/logo.png" class="user_photo" />
					 <div>
				     <span class="comment">这个鱼特别好</span>
					 <ul class="appriseImg">
					    <li>
					    <a href="#" onClick="return false" >
					   <img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" />
					   </a>
					    </li>
						
						<li>
						<a href="#" onClick="return false">
							<img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" />
						</a>
						</li>
					 </ul>
					 <div id="layer2" style="position: absolute; left: 48%; top: 50%; width: 540; height: 630px; z-index: 1; visibility: hidden"> 
  						<table border="0" width="761" height="216" cellspacing="0" cellpadding="0">
   						 <tr bordercolor="#FFFFFF"> 
      					<td width="624" valign="top" align="left"> 　 
       					 <div align="center"> 
         				 <center>
            			<table border="0" width="122%" height="181" cellspacing="0" cellpadding="0">
              			<tr> 
                     <td width="100%" height="151" valign="top" align="center"><a href="" onclick="return false" >
                    <img  class="shrink" name="big" src="${comment.data.get('productGallery')}" onclick="MM_showHideLayers('layer2','','hide');MM_showHideLayers('layer','','show');MM_showHideLayers('layer1','','show');return false;" border="0" width="540" height="130" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" /></a> 
                    </td>
                  </tr>
                    <tr> 
                       <td width="100%" height="12" valign="top" align="center">　</td>
                   </tr>
                    </table>
                    </center>
                    </div>
                    </td>
                   </tr>
                 </table>
			    </div> 
				</div>
				 </div>
				 <div class="product_message">
				 	<ul class="productlist">
				 		<li><span>${comment.data.get("commentUserName")}</span></li>
				 		<li><span>2016-02-03 10:15</span></li>
				 		<li><span>赞()</span></li>
				 	</ul>
				 </div>
				 <div>
				 	 
				 </div>
				
			 	</div>
			 </c:forEach>


			 	
			 	<div class="viewed martop45 good">
			     <div>
				     <img src="../../image/logo.png" class="user_photo" />
					 <div>
				     <span class="comment">这个鱼特别好</span>
					 <ul class="appriseImg">
					    <li>
					    <a href="#" onClick="return false" >
					   <img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" style="cursor:pointer;"/>
					   </a>
					    </li>
						
						<li>
						<a href="#" onClick="return false">
							<img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" />
						</a>
						</li>
					 </ul>
					 
					 
					 <div id="layer2" style="position: absolute; left: 48%; top: 50%; width: 540; height: 630px; z-index: 1; visibility: hidden"> 
  						<table border="0" width="761" height="216" cellspacing="0" cellpadding="0">
   						 <tr bordercolor="#FFFFFF"> 
      					<td width="624" valign="top" align="left"> 　 
       					 <div align="center"> 
         				 <center>
            			<table border="0" width="122%" height="181" cellspacing="0" cellpadding="0">
              			<tr> 
                     <td width="100%" height="151" valign="top" align="center"><a href="" onclick="return false" >
                    <img  class="shrink" name="big" src="${comment.data.get('productGallery')}" onclick="MM_showHideLayers('layer2','','hide');MM_showHideLayers('layer','','show');MM_showHideLayers('layer1','','show');return false;" border="0" width="540" height="130" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" /></a> 
                    </td>
                  </tr>
                    <tr> 
                       <td width="100%" height="12" valign="top" align="center">　</td>
                   </tr>
                    </table>
                    </center>
                    </div>
                    </td>
                   </tr>
                 </table>
			    </div> 
				</div>
				 </div>
				 <div class="product_message">
				 	<ul class="productlist">
				 		<li><span>${comment.data.get("commentUserName")}</span></li>
				 		<li><span>2016-02-03 10:15</span></li>
				 		<li><span>赞()</span></li>
				 	</ul>
				 	
				 	
				 </div>
				 <div>
				 	 
				 </div>
				
			 	</div>


			  <div class="viewed martop45 well">
			     <div>
				     <img src="../../image/logo.png" class="user_photo" />
					 <div>
				     <span class="comment">这个鱼特别好</span>
					 <ul class="appriseImg">
					    <li>
					    <a href="#" onClick="return false" >
					   <img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" />
					   </a>
					    </li>
						
						<li>
						<a href="#" onClick="return false">
							<img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" />
						</a>
						</li>
					 </ul>
					 
					 
					 <div id="layer2" style="position: absolute; left: 48%; top: 50%; width: 540; height: 630px; z-index: 1; visibility: hidden"> 
  						<table border="0" width="761" height="216" cellspacing="0" cellpadding="0">
   						 <tr bordercolor="#FFFFFF"> 
      					<td width="624" valign="top" align="left"> 　 
       					 <div align="center"> 
         				 <center>
            			<table border="0" width="122%" height="181" cellspacing="0" cellpadding="0">
              			<tr> 
                     <td width="100%" height="151" valign="top" align="center"><a href="" onclick="return false" >
                    <img  class="shrink" name="big" src="${comment.data.get('productGallery')}" onclick="MM_showHideLayers('layer2','','hide');MM_showHideLayers('layer','','show');MM_showHideLayers('layer1','','show');return false;" border="0" width="540" height="130" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" /></a> 
                    </td>
                  </tr>
                    <tr> 
                       <td width="100%" height="12" valign="top" align="center">　</td>
                   </tr>
                    </table>
                    </center>
                    </div>
                    </td>
                   </tr>
                 </table>
			    </div> 
				</div>
				 </div>
				 <div class="product_message">
				 	<ul class="productlist">
				 		<li><span>${comment.data.get("commentUserName")}</span></li>
				 		<li><span>2016-02-03 10:15</span></li>
				 		<li><span>赞()</span></li>
				 	</ul>
				 	
				 	
				 </div>
				 <div>
				 	 
				 </div>
				
			 	</div>



			 	<div class="viewed martop45 wrong">
			     <div>
				     <img src="../../image/logo.png" class="user_photo" />
					 <div>
				     <span class="comment">这个鱼特别好</span>
					 <ul class="appriseImg">
					    <li>
					    <a href="#" onClick="return false" >
					   <img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" style="cursor:pointer;"/>
					   </a>
					    </li>
						
						<li>
						<a href="#" onClick="return false">
							<img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" style="cursor:pointer;"/>
						</a>
						</li>
					 </ul>
					 
					 
					 <div id="layer2" style="position: absolute; left: 48%; top: 50%; width: 540; height: 630px; z-index: 1; visibility: hidden"> 
  						<table border="0" width="761" height="216" cellspacing="0" cellpadding="0">
   						 <tr bordercolor="#FFFFFF"> 
      					<td width="624" valign="top" align="left"> 　 
       					 <div align="center"> 
         				 <center>
            			<table border="0" width="122%" height="181" cellspacing="0" cellpadding="0">
              			<tr> 
                     <td width="100%" height="151" valign="top" align="center"><a href="" onclick="return false" >
                    <img  class="shrink" name="big" src="${comment.data.get('productGallery')}" onclick="MM_showHideLayers('layer2','','hide');MM_showHideLayers('layer','','show');MM_showHideLayers('layer1','','show');return false;" border="0" width="540" height="130" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" /></a> 
                    </td>
                  </tr>
                    <tr> 
                       <td width="100%" height="12" valign="top" align="center">　</td>
                   </tr>
                    </table>
                    </center>
                    </div>
                    </td>
                   </tr>
                 </table>
			    </div> 
				</div>
				 </div>
				 <div class="product_message">
				 	<ul class="productlist">
				 		<li><span>${comment.data.get("commentUserName")}</span></li>
				 		<li><span>2016-02-03 10:15</span></li>
				 		<li><span>赞()</span></li>
				 	</ul>
				 	
				 	
				 </div>
				 <div>
				 	 
				 </div>
				
			 	</div>
			 	<div class="viewed martop45 wrong">
			     <div>
				     <img src="../../image/logo.png" class="user_photo" />
					 <div>
				     <span class="comment">这个鱼特别好</span>
					 <ul class="appriseImg">
					    <li>
					    <a href="#" onClick="return false" >
					   <img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);"/>
					   </a>
					    </li>
						
						<li>
						<a href="#" onClick="return false">
							<img name="small" border="0" src="${comment.data.get('productGallery')}" width="250" height="60" onClick="MM_showHideLayers('layer2','','show');MM_showHideLayers('layer','','hide');MM_showHideLayers('layer1','','hide');" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" style="cursor:pointer;"/>
						</a>
						</li>
					 </ul>
					 
					 
					 <div id="layer2" style="position: absolute; left: 48%; top: 50%; width: 540; height: 630px; z-index: 1; visibility: hidden"> 
  						<table border="0" width="761" height="216" cellspacing="0" cellpadding="0">
   						 <tr bordercolor="#FFFFFF"> 
      					<td width="624" valign="top" align="left"> 　 
       					 <div align="center"> 
         				 <center>
            			<table border="0" width="122%" height="181" cellspacing="0" cellpadding="0">
              			<tr> 
                     <td width="100%" height="151" valign="top" align="center"><a href="" onclick="return false" >
                    <img  class="shrink" name="big" src="../../image/product/img1.jpg" onclick="MM_showHideLayers('layer2','','hide');MM_showHideLayers('layer','','show');MM_showHideLayers('layer1','','show');return false;" border="0" width="540" height="130" onload="return imgzoom(this,600);" onclick="javascript:window.open(this.src);" /></a> 
                    </td>
                  </tr>
                    <tr> 
                       <td width="100%" height="12" valign="top" align="center">　</td>
                   </tr>
                    </table>
                    </center>
                    </div>
                    </td>
                   </tr>
                 </table>
			    </div> 
				</div>
				 </div>
				 <div class="product_message">
				 	<ul class="productlist">
				 		<li><span>${comment.data.get("commentUserName")}</span></li>
				 		<li><span>2016-02-03 10:15</span></li>
				 		<li><span>赞()</span></li>
				 	</ul>
				 	
				 	
				 </div>
				 <div>
				 	 
				 </div>
				
			 	</div>

			 	<div class="empty"></div>

		  </div>
		  
         
	   </div>
	  
	</div>
     </div>
    </div>
    <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
</html>


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
<link rel="stylesheet" type="text/css" href="../../css/index.css">
<link rel="stylesheet" type="text/css" href="../../css/hudongzhongxin.css">
<script type="text/javascript" src="../../js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../js/respond.src.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
</head>
<body>
   <div class="wid-100" id="wid-100">
<%@include file="/WEB-INF/jsp/include/index_head.jsp" %>	
	<nav>
	   <ul class="list_img">
	     <li><img src="../../image/banner/img1.jpg"></li>
		 <li><img src="../../image/banner/img2.jpg"></li>
		 <li><img src="../../image/banner/img3.jpg"></li>
	   </ul>
	</nav>
		<div class="flotage">
		<a onclick="conceal()"><img src="../../image/erweima.jpg"/></a>
		<a href="#wid-100"><img src="../../image/up.jpg"/></a>
	    </div>
	    <div style="display:block" class="two-dimension" id="two-dimension">
		<img src="../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	    </div>
	<div class="box_container">
	   <h3 class="martop30"><span class="orange">新闻报道</span>
	    <a href="/content/interactive/news/index.shtml" class="orange" style="font-size:12px!important;margin-left:20px;">更多...</a>
	    </h3>
		<c:forEach items="${newsList}" var="list" varStatus="status" begin="0" end="2">
		<c:if test="${status.count%2 == 0}">
		<a href="${list.viewUrl}">
			<ul class="martop45 wid-70 newslist">  
			    <li class="box_text box_container_right">
			       	<div class="new" style="color: #000">${list.title}</div>
			       	<div  class="content" style="color: #000">${list.documentDataMap.get('documentBrief').dataValue} </div>			       
				</li>
				<li class="box_img box_container_left">
			       <img src="${list.documentDataMap.get('productSmallImage').dataValue}" >	  
				</li>
			</ul>
			</a>
		</c:if>
		<c:if test="${status.count%2 != 0}">
		<a href="${list.viewUrl}">
			<ul class="martop45 wid-70 newslist">  
			    <li class="box_text box_container_left">
			       	<div class=" new" style="color: #000">${list.title}</div>
			       	<div  class=" content" style="color: #000"> ${list.documentDataMap.get('documentBrief').dataValue} </div>
				</li>
				  <li class="box_img box_container_right">
			       <img src="${list.documentDataMap.get('productSmallImage').dataValue}" >	  
				</li>
		    </ul>
		    </a>
		</c:if>

		</c:forEach>
		
	</div>
	
	<div class="box_container padbtm50">
	   <h3 class="martop30"><span class="orange spacing">合作申请</span></h3>
		
		<ul class="cooperation_list martop45 wid-70">		   
			    <li class="po_re">
			        <img src="../../image/hudongzhongxin/1.jpg"/>
                    <a href="/content/notice/20160709194847.shtml" class="btnOrange po_ab">合作申请</a>				   
			    </li>
			    <li class="po_re">
			        <img src="../../image/hudongzhongxin/22.jpg"/>
					<a href="/content/notice/20160709194520.shtml" class="btnOrange po_ab">合作申请</a>					   
				</li>
			    <li class="po_re">
			       <img src="../../image/hudongzhongxin/33.jpg"/>		
				   <a href="/content/notice/20160709194628.shtml" class="btnOrange po_ab">合作申请</a>	
				</li>
		</ul>
	</div>
	<script type="text/javascript">
		function conceal(){
	 if(document.getElementById("two-dimension").style.display=="none"){
    document.getElementById("two-dimension").style.display="block";
   }
else{
    document.getElementById("two-dimension").style.display="none";
    }
}
	</script>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	</div>
</body>
</html>
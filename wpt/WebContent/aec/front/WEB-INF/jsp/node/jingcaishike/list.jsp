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
<link rel="stylesheet" type="text/css" href="../../../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../../../css/shicaisuyuan.css">
<script type="text/javascript" src="../../../../js/jquery.min.js"></script>
<script type="text/javascript" src="../../../../js/slider.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../../js/respond.src.js"></script>
<link rel="stylesheet" type="text/css" href="../../../../css/component.css" />
<link rel="stylesheet" type="text/css" href="../../../../css/demo.css" />
<script type="text/javascript" src="../../../../js/pubuliu/modernizr.custom.js"></script>
<script type="text/javascript" src="../../../../js/jquery.min.js"></script>
<script type="text/javascript" src="../../../../js/common.js"></script>
<script type="text/javascript" src="../../../../js/jquery.masonry.min.js"></script>
<style type="text/css">
img { border:none; }
.wrapper { width:1000px; margin:0 auto; }
.wrapper h3{color:#3366cc;font-size:16px;height:35px;line-height:1.9;text-align:center;border-bottom:1px solid #E5E5E5;margin:0 0 10px 0;}
#con1_1 { position:relative; margin-left: 40px;}
#con1_1 .product_list { position:absolute; left:0px; top:0px; padding:10px; background:#eee; margin:5px;}
.product_list img { width:200px;}
.product_list p { padding:5px 0px; font-size:12px; text-align:center; color:#333;  white-space:normal; width:200px;}
</style>
<script type="text/javascript">
$(document).ready(function(){
    var $container = $('#con1_1');    
    $container.imagesLoaded(function(){
        $container.masonry({
            itemSelector: '.product_list',
            columnWidth: 5 //每两列之间的间隙为5像素
        });
    });
    
});
</script>
</head>
<body>
      <%@include file="/WEB-INF/jsp/include/index_head.jsp" %>
      	
   <div class="wid-80" id="wid-80">
   <div class="flotage">
		<a onclick="conceal()"><img src="../../../../image/erweima.jpg"/></a>
		<a href="#wid-80"><img src="../../../../image/up.jpg"/></a>
	</div>
	<div style="display:block;" class="two-dimension" id="two-dimension">
		<img src="../../../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	</div>
	  <div class="box_container">
		<h3 class="martop30 orange">精彩时刻</h3>
      </div>
	  <div id="grid-gallery" class="grid-gallery martop45">
	 <section class="grid-wrap">
		<ul class="grid">
		<div class="wrapper">
  <div id="con1_1">
 <c:forEach var="document" items="${newsList}">
      <div class="product_list"> 
		 <a href="${document.viewUrl}"><img src="${document.documentDataMap.get('productSmallImage').dataValue}" title="${document.title}" ></a>
	  </div>
</c:forEach>
  </div>
</div>
	
 </div><!-- // grid-gallery -->
</div>
  	 <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
</body>
<script type="text/javascript" src="../../../../js/pubuliu/imagesloaded.pkgd.min.js"></script>
<script type="text/javascript" src="../../../../js/pubuliu/masonry.pkgd.min.js"></script>
<script type="text/javascript" src="../../../../js/pubuliu/classie.js"></script>
<script type="text/javascript" src="../../../../js/pubuliu/cbpGridGallery.js"></script>
<script type="text/javascript">
new CBPGridGallery(document.getElementById('grid-gallery'));
$(function(){
$(".grid li").hover(function(){
  $("this")
})
})
function conceal(){
	 if(document.getElementById("two-dimension").style.display=="none"){
    document.getElementById("two-dimension").style.display="block";
   }
else{
    document.getElementById("two-dimension").style.display="none";
    }
}

</script>

</html>
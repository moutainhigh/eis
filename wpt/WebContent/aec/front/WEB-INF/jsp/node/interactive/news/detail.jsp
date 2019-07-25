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
<link rel="stylesheet" type="text/css" href="../../../css/main.css">
<link rel="stylesheet" type="text/css" href="../../../css/shicaisuyuandetail.css">
<script  type="text/javascript" src="../../../js/jquery.min.js"></script>
<script  type="text/javascript" src="../../../js/common.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="../../../js/respond.src.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$("#btnReply").click(function(){
			$(".review1").toggle();
		});
});	
</script>

</head>
<body style="background-color: #f7f7f7;">
	 <%@include file="/WEB-INF/jsp/include/index_head.jsp" %>
   <div class="wid-80" id="wid-100">
		  <div class="box_container">
		     <div class="box_container_left martop10">
		     <a href="/content/interactive/news/index.shtml" class="color-black">返回</a>   
			 </div>
			 <div class="box_container_right martop20">
			    <div class="bshare-custom">
				   <a title="分享到" href="http://www.bShare.cn/" id="bshare-shareto" class="bshare-more">分享到</a>
				   <a title="分享到QQ空间" class="bshare-qzone">QQ空间</a>
				   <a title="分享到新浪微博" class="bshare-sinaminiblog">新浪微博</a>
				   <a title="分享到微信" class="bshare-weixin">微信</a>
				   <a title="更多平台" class="bshare-more bshare-more-icon more-style-addthis"></a>
				  <!-- <span class="BSHARE_COUNT bshare-share-count">0</span>-->
				</div>
				<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=1&amp;lang=zh"></script>
				<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>
			 </div>
			 
			 	<div class="flotage">
		<a onclick="conceal()"><img src="../../../image/erweima.jpg"/></a>
		<a href="#wid-100"><img src="../../../image/up.jpg"/></a>
	</div>
	<div style="display:block;" class="two-dimension" id="two-dimension">
		<img src="../../../image/bigerweima.jpg" style="width: 100px;height: 100px" />
	</div>
		  </div>
		  <div style="background-color: #fff; ">
		  <div class="box_article martop30">
		     <h3 class="martop30">${document.title}</h3>
			 <div class="box_container2 martop302">
   			      ${document.content}	    
			 </div>
			 </div>
			</div>
		  </div>
	    <div>
			
	    </div>
		
		  </div>
		<div class="martop40"></div>
</div>
</div>
	<%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	<script type="text/javascript">
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3Faca36f9d36d03a04d61c93f28c896386' type='text/javascript'%3E%3C/script%3E"));
function conceal(){
	 if(document.getElementById("two-dimension").style.display=="none"){
    document.getElementById("two-dimension").style.display="block";
   }
else{
    document.getElementById("two-dimension").style.display="none";
    }
}
</script>
</body>
</html>
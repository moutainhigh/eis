<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta name="keywords" content="以先,以先食材" />
<meta name="description" content="以先是一个有信仰的食材资讯平台，更是一个保证安全健康生活的平台" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../../css/mobile/video.css"/>
<script type="text/javascript" src="../../../../js/mobile/jquery.min.js"></script> 
<script type="text/javascript" src="../../../../js/mobile/common.min.js"></script>
</head>
<body>
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>精彩视频</span><a class="list1" onclick="vanish();"></a>
 </div>
	<div id="wrapper_1">
		<%@include file="/WEB-INF/jsp/include/BtnList.jsp" %>
	    <div class="box-yx wid90">
	     <p class="video-name">视频名称</p> 
	     <p style="font-size: 11px;">2016-7-4</p>
	    </div>
	  
		  <!--精选视频-->
		 <div class="box-yx mt30 " style="display: block">
		    <div class="box-img po-re"><img src="../../../../image/mobile/video_list.png"><img src="../../../../image/mobile/13.png" class="icon_play"><span class="time">2:10</span></div>
			<div class="box_con">

			   
			   <ul class="num-list">
			    <a href="#"><li>阅读&nbsp&nbsp333</li></a>
				<a href="#"><li><img src="../../../../image/mobile/header/preview_unlike_icon1.png" style="width: 20px;height: 20px;margin-top:-6px;"/>&nbsp&nbsp<span>22</span></li></a>
				<a href="#"><li><img src="../../../../image/mobile/header/icon.png" style="width: 17px;height: 17px;margin-top:-6px;"/>&nbsp&nbsp<span>22</span></li></a>
				
			   </ul>
			</div>
		 </div>
		 <div class="line"></div>
		 <div class="wid90">
		 	<h5>简介</h5>
		 	<div class="neirong">
		 		的乡愁有些象“邮票”，也有些象“年轮树”，但更象我的心事，她生在心头长在眉宇间。 记得初次离开家乡的时候，人早就象霜打的茄子——恹了。 离别的那天，妻子默默地跟在后面一直将我送出村口，没说句动感的话语，没做个激情的吻别，只是恋恋不舍地一直地跟着
		 	</div>
		 </div>
		 <div class="wid90">
	  	 <div class="linellae"></div>	
			    <div class=" write_comment"><span style="margin:0 auto;">相关视频</span></div>
			   <div class="linellae1"></div>
	  </div>
		  <!--精彩时刻-->
		 <div class="box_sk ">
		 <div class="box_left">
		 <img src="../../../../image/mobile/10.png" style="width :100%;height:100%;">
		 <p class="fo_st">吃货们的美食艺术</p>
		 </div>
		 <div class="box_right">
		  <img src="../../../../image/mobile/10.png" style="width :100%;height:100%;">
		 <p class="fo_st">吃货们的美食艺术</p>
		 </div>

		 </div>
		  <div class="line"></div>
		  <div class="wid90">
	  	 <div class="linellae"></div>	
			    <div class="write_comment"><span style="margin:0 auto;">写评论</span></div>
			   <div class="linellae1"></div>
	     </div> 
	     <div class="comment_box">
	      <form>
			   <textarea placeholder="评论将由以先后台筛选后显示，对所有人显示" maxlength=400 id="suggestContent" style="width: 90%;height: 100px;margin-left: 5%; margin-top:2%;"></textarea>			  
			  <p><input type="submit" value="提交" onclick="suggestSubmit()"/></p>
	      </form>
	     </div>
	      <div class="wid90">
	  	 <div class="linellae"></div>	
			    <div class=" write_comment"><span style="margin:0 auto;">我的评论</span></div>
			   <div class="linellae1"></div>
	  </div>
		 <ul class="buy-list wid90">
	    <li>
		   <div class="ware_img"><img src="http://www.yixian365.com/static/documentFile/open/201605181056-3a7d364d-89d1-4e07-bff3-384130bc06c5.jpg"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_name">昵称</span><img src="../../../../image/mobile/header/preview_unlike_icon1.png" style="width: 15px;height: 15px;float: right;" /></div>
			  <div class="divCenter"> <span class="ware_from">写评论</span><span class="all_num"></span></div>
              <div class="one_sprice"><span class="price_mark">一分钟前</span> <span class="fare orange">删除</span></div>
			  </div>
		</li>
	  </ul>
	   <div class="wid90">
	   <div class="linellae"></div>	
			    <div class=" write_comment"><span style="margin:0 auto;">精选评论</span></div>
			   <div class="linellae1"></div>
	  </div>
	  <ul class="buy-list wid90">
	    <li>
		   <div class="ware_img"><img src="http://www.yixian365.com/static/documentFile/open/201605181056-3a7d364d-89d1-4e07-bff3-384130bc06c5.jpg"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_name">昵称</span><span><img src="../../../../image/mobile/header/preview_unlike_icon1.png" style="width: 15px;height: 15px;float: right;" /></span></div>
			  <div class="divCenter"> <span class="ware_from">写评论</span></div>
              <div class="one_sprice"><span class="price_mark">一分钟前</span> <span class="fare orange">删除</span></div>
			  </div>
		</li>
	  </ul>
	</div>

	 <!--食材信仰-->
<!-- 	 <ul class="box-xy wid90 scxy" style="display: block;">
	    <li>
		   <div class="ware_img"><img src="http://www.yixian365.com/static/documentFile/open/201605181056-3a7d364d-89d1-4e07-bff3-384130bc06c5.jpg"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_names">昵称</span></div>
			  <div class="divCenter"> <span class="ware_from">写评论</span></div>
              <div class="one_sprice"><span class="price_mark">阅读(2001)</span> 
              <span class="fare orange"><img src="../../../../image/mobile/header/shoucang.png" style="width: 20px;height: 20px;">（22）</span> </div>
		   </div>
			 
		</li>
		 <div class="line"></div> 
	  </ul> -->
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
</body>
<script type="text/javascript">
	function vanish(){
	if(document.getElementById("nav_list").style.display=="none"){
    document.getElementById("nav_list").style.display="block";
   }
else{
    document.getElementById("nav_list").style.display="none";
    }
}
</script>
</html>    
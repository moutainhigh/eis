<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="user-scalable=no,width=device-width, initial-scale=1">
<title>${systemName}-写评价</title>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/main.css"/>
<link rel="stylesheet" type="text/css" href="../../../theme/${theme}/css/mobile/comment.css"/>
<link rel="stylesheet" href="../../../theme/${theme}/css/iconfont.css" />
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.form.3.5.1.js"></script>
</head>
<style>
	.btn_login{
	    background-color: #7E9A0E;
	}
	.wid90{
		width: 100%;
		padding:0 10px;
		box-sizing: border-box;
		margin: 0;
	}
	.buy-list{
		padding: 0;
	}
	.ware_img{
		border:none;
	}
	.ware_img img{
		width: 100%;
		height: 100
	}
	.box_right2 {
	    float: left;
	    margin-left: 1%;
	    width: 69%;
	}
	.box_title{
		height: auto;
	}
	.comment_list{
		width: 100%;
		padding:0 10px;
		box-sizing: border-box;
	}
	.comment_list li{
		font-size: 14px;
		margin-right: 10px;
		margin-left: 0;
		position: relative;
		vertical-align: middle;
		    line-height: 22px;
	}
	.comment_list li span{
		font-size: 14px;
	}
	.comment_list li input{
		position: absolute;left:0;
		z-index: 999999;
		width: 24px;
    	height: 24px;
    	opacity: 0;
	}
	.box_container_left{
		width: 100%;
		margin:0;
		margin-top:10px;
	}
	.box_container_left textarea{
		padding: 5px;
		font-size:14px;
		font-family: "黑体"!important;
	}
	.commentcolor1{
		color: #ff5f5f;
	}
	.commentcolor2{
		color: #FFCD73;
	}
	.commentcolor3{
		color: #BFBFBF;
	}
	.iconfont{
		font-size:23px;
	}
	.box_center2{
		width: 100%;
	}

	.btn_login{
		position: fixed;
    	bottom: 0;
    	left: 0;
    	width: 100%;
    	font-family: "黑体"!important;
    	letter-spacing:1px;
	}
	/*图片上传*/
	.container {
		width: 100%;
		height: 100%;
		/*overflow: auto;*/
		clear: both;
		padding-top: 10px;
	}
	.tupianout{
		width: 45px;
		height: 45px;
		padding: 5px 15px 17px 15px;
		/*overflow: hidden;*/
		clear: both;
		/*margin: 1rem auto;*/
		border: 1px dashed #555;
		margin-left: 10px;
		    float: left;
		   /* margin-right: 0.2rem;*/
	}
	.z_photo {
		width: 100%;
		/*height: 45px;*/
		/*padding: 5px 15px 17px 15px;*/
		/*overflow: hidden;*/
		clear: both;
		/*margin: 1rem auto;*/
	}
	
	.z_photo img {
		width: 69px;
		height: 69px;

	}
	
	.z_addImg {
		float: left;
		margin-left: 10px;
    	margin-bottom: 10px;
		position: relative;
	}
	.z_addImg i{
		position: absolute;
    right: -5px;
    top: -5px;
    color: #333;
    font-size: 17px;
    background-color: #fff;
    display: inline-block;
    border-radius: 100%;
    cursor: pointer;
	}
	.z_file {
    width: 44px;
    height: 44px;
    background: url(../../../theme/basic/image/mobile/z_add.png) no-repeat;
    background-size: 100% 100%;
    /* float: left; */
    /* margin-right: 0.2rem; */
    margin: 0;
}
	
	.z_file input::-webkit-file-upload-button {
		width: 1rem;
		height: 1rem;
		border: none;
		position: absolute;
		outline: 0;
		opacity: 0;
	}
	
	.z_file input#file {
		display: block;
		width: auto;
		border: 0;
		vertical-align: middle;
	}
	/*遮罩层*/
	
	.z_mask {
		width: 100%;
		height: 100%;
		background: rgba(0, 0, 0, .5);
		position: fixed;
		top: 0;
		left: 0;
		z-index: 999;
		display: none;
	}
	
	.z_alert {
		width: 3rem;
		height: 2rem;
		border-radius: .2rem;
		background: #fff;
		font-size: .24rem;
		text-align: center;
		position: absolute;
		left: 50%;
		top: 50%;
		margin-left: -1.5rem;
		margin-top: -2rem;
	}
	
	.z_alert p:nth-child(1) {
		line-height: 1.5rem;
	}
	
	.z_alert p:nth-child(2) span {
		display: inline-block;
		width: 49%;
		height: .5rem;
		line-height: .5rem;
		float: left;
		border-top: 1px solid #ddd;
	}
	
	.z_cancel {
		border-right: 1px solid #ddd;
	}
	img{
		cursor: pointer;
	}
	.img_amplify{
		/*transition: all 1s ease;
		-webkit-transition: all 1s ease;*/
	}
	.imgbig{
		position: fixed;top: 0;left:0;
		width: 100%!important;
		height: 100%!important;
		    z-index: 9999999999999999999999999999999;
	}
</style>
<body>   
 <script type="text/javascript" src="../../../theme/${theme}/js/mobile/addComment.js"></script>
 <input type="hidden" id="gallerySize" value="${fn:split(commentConfig.extraDataDefine.productGallery,'#')[1]}">
 <div class="header" id="header">
	  <a class="back" href="javascript:history.go(-1);"></a><span>写评论</span><!--<a class="home" href="/">-->
 </div>
	<div id="wrapper_1" style="margin-top: 0;">
	   <div class="box_title">
	<div class="wid90">
		<ul class="buy-list">		
	    <li>
		   <div class="ware_img"><img src="${item.itemDataMap.productSmallImage.dataValue}"> </div>
		   <div class="box_right2">
			  <div> <span class="ware_name" style="font-size:14px;">${item.name}商品名称商品名称商品名称商品名称商品名称商品名称商品名称商品名称</span></div>
			  <div class="divCenter"> <span class="ware_from">${item.itemDataMap.goodsSpec.dataValue}</span><!--span class="all_num">×${str.count}</span--></div>
             <!--  <div class="one_sprice"><span class="price_mark">实付款￥${item.requestMoney}</span> <span class="fare">数量：<span style="color:#ff6400;">${item.count}</span></span></div> -->
			  </div>
		</li>
	  </ul>
	</div>
	 
	   </div>
	   <form id="upload" enctype="multipart/form-data">
	   <div class="box_title  martop20" style="padding-bottom: 20px;background:transparent;">
	   <ul class="comment_list">
	   	<li>评分等级：</li>
	   	<li><i class="icon iconfont icon-hua1 commentcolor1"></i><input type="radio" name="rank" checked="checked" value="1"><span>好评</span></li>
	   	<li><i class="icon iconfont icon-hua"></i><input type="radio" name="rank" value="2"><span>中评</span></li>
	   	<li><i class="icon iconfont icon-hua"></i><input type="radio" name="rank" value="3"><span>差评</span></li>
	   </ul> 
	   <div class="">
	   	<div class="box_left"></div>
	   <div class="box_container_left">
			<p><textarea placeholder="10-500个字之间,写下购买体会" name="content" maxlength=500 id="suggestContent"></textarea></p>			  
			   
		</div>
	   </div>
	   </div>
	   <!-- 添加图片 -->
	   <div class="container">
	<!--    照片添加    -->
			<div class="z_photo">
				<div class="tupianout">
				<div class="z_file">
					<input type="file" name="file" id="file" value="" accept="image/*" onchange="imgChange('z_photo','z_file');" multiple/>
					
				</div>
				<p style="text-align: center;font-size:12px;color: #A0A0A0;width: 50px;
    margin-left: -2px;">添加图片</p>
				</div>
			</div>
			<!--遮罩层-->
			<div class="z_mask">
				<!--弹出框-->
				<div class="z_alert">
					<p>确定要删除这张图片吗？</p>
					<p>
						<span class="z_cancel">取消</span>
						<span class="z_sure">确定</span>
					</p>
				</div>
			</div>
		</div>

	   <!-- <div class="wid90">	   
		<div id="box_input">
			<a class="btn_addPic orange upphoto martop10" href="javascript:void(0);" ><span>添加图片</span>
                <input type="file" class="filePrew" name="productGallery0" onchange="dropHandler(this.files)" accept="image/gif,image/jpeg,image/png,image/bmp">
			</a>						
		</div>	   	
	   	<div id="dropbox">
            <ul id="pageContent">
            </ul>
            <ul id="uploaded"></ul> 
        </div>
		
	   </div> -->
	   <input type="hidden" id="tid" value="${item.transactionId}" name="tid">
		<div class="box_center2">
			 <input type="submit" class="btn_login" id="login" value="发表评论"/>
		</div>		
	 </form>
	</div>
</body>
<script>
	$('.comment_list li input').on('click',function(){
		$(this).attr('checked','checked');
		$(this).parent('li').siblings('li').find('input').removeAttr('checked','checked');
		var index = $(this).parent().index();
		$(this).prev('i').attr('class', "icon iconfont icon-hua1")
		$(this).prev('i').addClass('commentcolor'+index);
		$(this).parent('li').siblings('li').find('i').attr('class','icon iconfont  icon-hua')
		
	})
</script>
<script type="text/javascript">
	//px转换为rem
	(function(doc, win) {
		var docEl = doc.documentElement,
			resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
			recalc = function() {
				var clientWidth = docEl.clientWidth;
				if (!clientWidth) return;
				if (clientWidth >= 640) {
					docEl.style.fontSize = '100px';
				} else {
					docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
				}
			};

		if (!doc.addEventListener) return;
		win.addEventListener(resizeEvt, recalc, false);
		doc.addEventListener('DOMContentLoaded', recalc, false);
	})(document, window);

	function imgChange(obj1, obj2) {
		if ($('.z_addImg').length && $('.z_addImg').length>0) {
				$('.z_addImg').remove()
			}
		//获取点击的文本框
		var file = document.getElementById("file");var z_addImg = document.getElementsByClassName("z_addImg");
		//存放图片的父级元素
		var imgContainer = document.getElementsByClassName(obj1)[0];
		//获取的图片文件
		var fileList = file.files;
		//文本框的父级元素
		var input = document.getElementsByClassName(obj2)[0];
		var imgArr = [];
		//遍历获取到得图片文件
		for (var i = 0; i < fileList.length; i++) {

			var imgUrl = window.URL.createObjectURL(file.files[i]);
			imgArr.push(imgUrl);
			var img = document.createElement("img");
			var icon = document.createElement("i");
			icon.setAttribute("class", "icon iconfont icon-guanbi");
			img.setAttribute("src", imgArr[i]);
			img.setAttribute("class", 'img_amplify');
			var imgAdd = document.createElement("div");
			imgAdd.setAttribute("class", "z_addImg");
			imgAdd.appendChild(img);
			imgAdd.appendChild(icon);

			imgContainer.appendChild(imgAdd);
		};
		imgRemove();
	};

	function imgRemove() {
		var imgList = document.getElementsByClassName("z_addImg");
		var mask = document.getElementsByClassName("z_mask")[0];
		var cancel = document.getElementsByClassName("z_cancel")[0];
		var sure = document.getElementsByClassName("z_sure")[0];
		$('body').on('click','.z_addImg i',function(){
			$(this).parent('.z_addImg').css('display','none');
		})

		// for (var j = 0; j < imgList.length; j++) {
		// 	imgList[j].index = j;
		// 	imgList[j].onclick = function() {
		// 		var t = this;
		// 		mask.style.display = "block";
		// 		cancel.onclick = function() {
		// 			mask.style.display = "none";
		// 		};
		// 		sure.onclick = function() {
		// 			mask.style.display = "none";
		// 			t.style.display = "none";
		// 		};

		// 	}
		// };
	};
	$('body').on('click','.img_amplify',function(){
		$(this).toggleClass('imgbig')
	})
	$("body").on("touchstart",'.imgbig', function(e) {

　　　　e.preventDefault();
　　　　startX = e.originalEvent.changedTouches[0].pageX,
　　　　startY = e.originalEvent.changedTouches[0].pageY;
　　});
　　$("body").on("touchmove",'.imgbig', function(e) {
　　　　e.preventDefault();
//$(this).attr('index',$(this).index())
　　　　moveEndX = e.originalEvent.changedTouches[0].pageX,
　　　　moveEndY = e.originalEvent.changedTouches[0].pageY,
　　　　X = moveEndX - startX,
　　　　Y = moveEndY - startY;
 	if ( X < 5 ) {
			// 左滑
			if ($('.img_amplify').length && $('.img_amplify').length>1){
				
				if ($(this).index() == $('.img_amplify').length-1) {
					return
				}else{
					$(this).removeClass('imgbig');
					$(this).parent('.z_addImg').next('.z_addImg').find('img').addClass('imgbig');
				}
			}
　　　　}
　　　　else if ( X > 5 ) {
			//右滑
			if ($('.img_amplify').length && $('.img_amplify').length>1){
				if ($(this).index()==0) {
					return
				}else{
					$(this).removeClass('imgbig');
					$(this).parent('.z_addImg').prev('.z_addImg').find('img').addClass('imgbig');
				}
				
			}
　　　　}else{
			$(this).toggleClass('imgbig')
		}
　　　　
　　　　
　　　});
// $("body").on("touchend",'.imgbig', function(e) {
// 	e.preventDefault();
// 	EndX = e.originalEvent.changedTouches[0].pageX,
// 　　EndY = e.originalEvent.changedTouches[0].pageY,
// 	X = EndX - startX,
// 　　Y = EndY - startY;
// 	if (x==0 && y==0) {}{
// 		$(this).removeClass('imgbig')
// 	}

// })
</script>
<!-- 图片左右滑动 -->

</html>
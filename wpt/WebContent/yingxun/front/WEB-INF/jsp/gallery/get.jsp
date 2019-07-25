<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${gallery.galleryName}</title>

    <!-- Bootstrap core CSS -->
	
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">	
	<link href="//netdna.bootstrapcdn.com/font-awesome/3.0.2/css/font-awesome.css" rel="stylesheet">

    <!-- Custom styles for this template -->
	
	<link href="/theme/${theme}/style/galleryGet.css" rel="stylesheet">	
	<link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
	<link rel="stylesheet" href="/theme/${theme}/style/animation.css">
	<link rel="stylesheet" href="http://bdimg.share.baidu.com/static/api/css/share_style0_16.css?v=6aba13f0.css">
	
	
	<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->	
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>	
	<script src="/theme/${theme}/js/sweetalert.min.js"></script> 
	<script src="/theme/${theme}/js/video.js"></script> 
	<script src="/theme/${theme}/js/qrcode.js"></script> 
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->	
	
	<style>
		body a{outline:none;blr:expression(this.onFocus=this.blur());}
		a:focus{outline:none;}	
		ul li{list-style:none;}
		html{height:100%;width:100%;}
		body{height:100%;width:100%;margin:0;padding:0;}
		.fullscreenBg{height:100%;width:100%;background:url(/theme/${theme}/images/fullscreenBg.jpg) top left no-repeat;background-size:100% 100%;position:fixed;}
		.body{box-sizing:border-box;background:transparent;}
		#audio_btn {
			position: absolute;
			right: 10px;
			top: 10px;
			z-index: 200;
			width: 50px;
			height: 50px;
			background-repeat: no-repeat;
			cursor:pointer;
		}
		.play_yinfu {
			background-image: url(/theme/${theme}/images/music.gif);
			background-position: center center;
			background-size: 60px 60px;
		}
		.rotateMusic {
			position: absolute;
			left: 10px;
			top: 10px;
			width: 30px;
			height: 30px;
			background-size: 100% 100%;
			background-image: url(/theme/${theme}/images/music_off.png);
			-webkit-animation: rotating 1.2s linear infinite;
			-moz-animation: rotating 1.2s linear infinite;
			-o-animation: rotating 1.2s linear infinite;
			animation: rotating 1.2s linear infinite;
		}
		.off {
			background-image: url(/theme/${theme}/images/music_off.png);
			background-size: 30px 30px;
			background-repeat: no-repeat;
			background-position: center center;
		}
		@-webkit-keyframes rotating
			{from{-webkit-transform:rotate(0deg)}to{-webkit-transform:rotate(360deg)}}
		@keyframes rotating{from{transform:rotate(0deg)}to{transform:rotate(360deg)}}
		@-moz-keyframes rotating{from{-moz-transform:rotate(0deg)}to{-moz-transform:rotate(360deg)}}
		.top{
			width:100%;
			background:url(/theme/${theme}/images/yulan_phone_top.png);
			background-size:100% 100%;
		}
		.phoneMenubar{
			width:100%;
			background:url(/theme/${theme}/images/phonetitle1.jpg);
			background-size:100% 100%;
		} 
		.sceneTitleBanner{	
			width:100%;
			border-left: 1px solid #F6F6F6;
			border-right: 1px solid #F6F6F6;
			background:#000;
			color:#fff;
			text-align:center;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
		} 
		.nr{
			width:100%;
			position: relative;
			cursor: pointer;
			height: 486px;
			overflow-x: hidden;
			overflow-y: hidden;
			-webkit-user-select: none;
		}
		@media(min-width:768px){
			.nr{
				border-left: 1px solid #F6F6F6;
				border-right: 1px solid #F6F6F6;
			}			
			.body{
				padding-top:80px;				
			}
			.phoneBox{
				width:65%;
				margin-left:15%;	
			}
		}
		@media(max-width:768px){					
			.body{
				height:100%;
				width:100%;
			}
			.container{
				height:100%;
				width:100%;
			}
			.row{
				height:100%;
			}
			.left{
				height:100%;
				width:100%;
			}
			.phoneBox{
				height:100%;
				width:100%;
			}
		}
		
		.bottom{
			width:100%;
			background:url(/theme/${theme}/images/yulan_phone_bottom.png);
			background-size:100% 100%;
		}
		
		.previewWrapper{
			width:100%;
			height:100%;
			position:relative;
		}
		.previewArea{
			width:100%;
			height:100%;
			padding:0;
			margin:0;
			-webkit-margin-before: 0;
			-webkit-margin-after: 0;
			-webkit-margin-start: 0;
			-webkit-margin-end: 0;
			-webkit-padding-start: 0;
			display:none;
		}
		.previewArea li{
			position:absolute;
			z-index:0;
		}
		.scanCode{
			margin-top: 140px;
			margin-left: 35%;
			width:60%;
		}
		#qrcode{
			width:100%;
			margin-top:20px;
			margin-bottom:20px;
		}
		.share{
			height:40px;
			margin-left:35%;
		}
		.bdsharebuttonbox{
			height:100%;
		}
		.bdsharebuttonbox a{
			height:100%;
		}
		#share a{
			height:30px;
		}
		
		.slidePage {
			position: absolute;
			top: 0%;
			width: 100%;
			height: 100%;
		}
		.page {
			position:absolute;
			width: 100%;
			height: 100%;
			overflow:hidden;
		}
		.pBtn {
			position: absolute;
			z-index: 99;
			width: 16%;
			top: 1%;
			left: 48%;
			display:none;
		}
		.pBtn img {
			top: 10%;
			left: -4%;
			width: 36%;
		}
		.nBtn img {
			bottom: 10%;
			left: -4%;
			width: 36%;
		}
		.nBtn {
			position: absolute;
			z-index: 99;
			width: 16%;
			bottom: 2%;
			left: 48%;
			display:none;
		}
		span.bds_more{
			height:40px !important;
		}
		.bds_tools a{
			height:40px !important;
		}
		.moveUp{
			-webkit-animation: moveUp 1s ease;
			-moz-animation: moveUp 1s ease;
			-o-animation: moveUp 1s ease;
			animation: moveUp 1s ease;
			top:-100%;
		}
		.moveDown{
			-webkit-animation: moveDown 1s ease;
			-moz-animation: moveDown 1s ease;
			-o-animation: moveDown 1s ease;
			animation: moveDown 1s ease;
			top:100%;
		}
		@-webkit-keyframes moveUp{
		0%{top:0%;}
		100%{top:-100%;}
		}
		@moz-keyframes moveUp{
		0%{top:0%;}
		100%{top:-100%;}
		}
		@keyframes moveDown{
		0%{top:0%;}
		100%{top:100%;}
		}
		@-webkit-keyframes moveDown{
		0%{top:0%;}
		100%{top:100%;}
		}
		@-moz-keyframes moveDown{
		0%{top:0%;}
		100%{top:100%;}
		}
		@keyframes moveDown{
		0%{top:0%;}
		100%{top:100%;}
		}
		@-webkit-keyframes zoomInn
			{from{-webkit-transform:scale(0,0)}to{-webkit-transform:scale(1,1)}}
		@keyframes zoomInn{from{-webkit-transform:scale(0,0)}to{-webkit-transform:scale(1,1)}}
		@-moz-keyframes zoomInn{from{-webkit-transform:scale(0,0)}to{-webkit-transform:scale(1,1)}}
		.zoomInn{
			-webkit-animation: zoomInn 1s ease;
			-moz-animation: zoomInn 1s ease;
			-o-animation: zoomInn 1s ease;
			animation: zoomInn 1s ease;
		}
		@-webkit-keyframes roomOut
			{from{-webkit-transform:scale(1,1)}to{-webkit-transform:scale(0,0)}}
		@keyframes roomOut{from{-webkit-transform:scale(1,1)}to{-webkit-transform:scale(0,0)}}
		@-moz-keyframes roomOut{from{-webkit-transform:scale(1,1)}to{-webkit-transform:scale(0,0)}}
		.zoomOut{
			-webkit-animation: roomOut 1s ease;
			-moz-animation: roomOut 1s ease;
			-o-animation: roomOut 1s ease;
			animation: roomOut 1s ease;
		}
		@-webkit-keyframes inertialRight{from{-webkit-transform:translate3d(-100%,0,0);transform:translate3d(-100%,0,0);}to{-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);;}}
		@keyframes inertialRight{from{-webkit-transform:translate3d(-100%,0,0);transform:translate3d(-100%,0,0);}to{-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);;}}
		@-moz-keyframes inertialRight{from{-webkit-transform:translate3d(-100%,0,0);transform:translate3d(-100%,0,0);}to{-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);;}}
		.inertialRight{
			-webkit-animation: inertialRight 0.5s ease;
			-moz-animation: inertialRight 0.5s ease;
			-o-animation: inertialRight 0.5s ease;
			animation: inertialRight 0.5s ease;
		}
		@-webkit-keyframes inertialRight2{from{-webkit-transform:scale(1,1) translate3d(0,0,0);transform:scale(1,1) translate3d(0,0,0)}to{-webkit-transform:scale(0,0) translate3d(100%,0,0);transform:scale(0,0) translate3d(100%,0,0)}}
		@keyframes inertialRight2{from{-webkit-transform:scale(1,1) translate3d(0,0,0);transform:scale(1,1) translate3d(0,0,0)}to{-webkit-transform:scale(0,0) translate3d(100%,0,0);transform:scale(0,0) translate3d(100%,0,0)}}
		@-moz-keyframes inertialRight2{from{-webkit-transform:scale(1,1) translate3d(0,0,0);transform:scale(1,1) translate3d(0,0,0)}to{-webkit-transform:scale(0,0) translate3d(100%,0,0);transform:scale(0,0) translate3d(100%,0,0)}}
		.inertialRight2{
			-webkit-animation: inertialRight2 0.5s ease;
			-moz-animation: inertialRight2 0.5s ease;
			-o-animation: inertialRight2 0.5s ease;
			animation: inertialRight2 0.5s ease;
		}
		@-webkit-keyframes inertialLeft					{from{-webkit-transform:translate3d(100%,0,0);transform:translate3d(100%,0,0);}to{-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);;}}
		@keyframes inertialLeft{from{-webkit-transform:translate3d(100%,0,0);transform:translate3d(100%,0,0);}to{-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);;}}
		@-moz-keyframes inertialLeft{from{-webkit-transform:translate3d(100%,0,0);transform:translate3d(100%,0,0);}to{-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);;}}
		.inertialLeft{
			-webkit-animation: inertialLeft 0.5s ease;
			-moz-animation: inertialLeft 0.5s ease;
			-o-animation: inertialLeft 0.5s ease;
			animation: inertialLeft 0.5s ease;
		}
		@-webkit-keyframes inertialLeft2{from{-webkit-transform:scale(1,1) translate3d(0,0,0);transform:scale(1,1) translate3d(0,0,0)}to{-webkit-transform:scale(0,0) translate3d(-100%,0,0);transform:scale(0,0) translate3d(-100%,0,0)}}
		@keyframes inertialLeft2{from{-webkit-transform:scale(1,1) translate3d(0,0,0);transform:scale(1,1) translate3d(0,0,0)}to{-webkit-transform:scale(0,0) translate3d(-100%,0,0);transform:scale(0,0) translate3d(-100%,0,0)}}
		@-moz-keyframes inertialLeft2{from{-webkit-transform:scale(1,1) translate3d(0,0,0);transform:scale(1,1) translate3d(0,0,0)}to{-webkit-transform:scale(0,0) translate3d(-100%,0,0);transform:scale(0,0) translate3d(-100%,0,0)}}
		.inertialLeft2{
			-webkit-animation: inertialLeft2 0.5s ease;
			-moz-animation: inertialLeft2 0.5s ease;
			-o-animation: inertialLeft2 0.5s ease;
			animation: inertialLeft2 0.5s ease;
		}
	</style>
	<script type="text/javascript">
		//获取设备渲染像素宽度
		$(function(){
			var iWidth = $(".phoneBox").width();
			document.getElementsByTagName('html')[0].style.fontSize = iWidth/10+'px';
			$(window).resize(function(){
				var iWidth =  $(".phoneBox").width();
				document.getElementsByTagName('html')[0].style.fontSize = iWidth/10+'px';
			})
		})
		
	</script>
	<script>		
		
		function writeInput(){
			var phoneHtml='';
			phoneHtml+='<li class="rotate resize input" ctype="input" title="拖拽模式右击鼠标出现功能菜单，点击编辑可以进入编辑模式(编辑模式使用工具栏编辑时须选中文字)，点击页面空白处重回拖拽模式">';
			phoneHtml+='<div class="bar bar-rotate bar-radius" style="display:none;)"  ></div>';
			phoneHtml+='<div class="bar bar-line" style="display:none;"  ></div>';	
			phoneHtml+='<div class="draggable" style="width:100%;height:100%;">';			
			phoneHtml+='<div class="elementBox" id="elementBox" style="display:block;height:100%;width:100%;outline:0;border:#ddd 1px solid" >';	
			phoneHtml+='<input style="display:block;outline:0;height:100%;background:transparent;width:100%;border:none;resize:none;padding-left:10px;"  value=""/>';		
			phoneHtml+='</div>';				
			phoneHtml+='</div>';
			phoneHtml+='<div class="bar bar-n" style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-s"  style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-e"  style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-w" style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-ne bar-radius" style="display:none;"  ></div>';
			phoneHtml+='<div class="bar bar-nw bar-radius" style="display:none;" ></div>';
			phoneHtml+='<div class="bar bar-se bar-radius"  style="display:none;" ></div>';
			phoneHtml+='<div class="bar bar-sw bar-radius"  style="display:none;" ></div>';							
			phoneHtml+='</li>';
			return phoneHtml;			
		}
		function writeButton(){
			var phoneHtml='';
			phoneHtml+='<li class="rotate resize button" ctype="button" title="拖拽模式右击鼠标出现功能菜单，点击编辑可以进入编辑模式(编辑模式使用工具栏编辑时须选中文字)，点击页面空白处重回拖拽模式">';
			phoneHtml+='<div class="bar bar-rotate bar-radius" style="display:none;)"  ></div>';
			phoneHtml+='<div class="bar bar-line" style="display:none;"  ></div>';	
			phoneHtml+='<div class="draggable" style="width:100%;height:100%;">';
			phoneHtml+='<div class="elementBox" id="elementBox" style="display:block;height:100%;width:100%;outline:0;background:#f1f1f1;border:#ddd 1px solid">';
			phoneHtml+='<button style="display:block;outline:0;height:100%;background:transparent;width:100%;border:none;text-align:center"></button>';	
			phoneHtml+='</div>';
			phoneHtml+='</div>';
			phoneHtml+='<div class="bar bar-n" style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-s"  style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-e"  style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-w" style="display:none;" ><div class="bar-radius" style="display:none;" ></div></div>';
			phoneHtml+='<div class="bar bar-ne bar-radius" style="display:none;"  ></div>';
			phoneHtml+='<div class="bar bar-nw bar-radius" style="display:none;" ></div>';
			phoneHtml+='<div class="bar bar-se bar-radius"  style="display:none;" ></div>';
			phoneHtml+='<div class="bar bar-sw bar-radius"  style="display:none;" ></div>';					
			phoneHtml+='</li>';
			return phoneHtml;			
		}
		
		
	</script>
	<script>
	$(function(){
		var w=$(".phoneBox").width();
		var w0=$("html").width();
		if(w0>768){
			var hTop=0.149*w;
			var hMenubar=0.062*w;
			var hTitleBanner=0.124*w;
			var hNr=1.71*w;
			var hBottom=0.14*w;
			$(".top").css("height",hTop);
			$(".phoneMenubar").css("height",hMenubar);
			$(".sceneTitleBanner").css("height",hTitleBanner);
			$(".sceneTitleBanner").css("lineHeight",hTitleBanner+'px');
			$(".nr").css("height",hNr);
			$(".bottom").css("height",hBottom);
		}else{
			$(".editArea").css("height","100%");
		}
		$(window).resize(function(){
			var w=$(".phoneBox").width();
			var hTop=0.149*w;
			var hMenubar=0.062*w;
			var hTitleBanner=0.124*w;
			var hNr=1.71*w;
			var hBottom=0.14*w;
			$(".top").css("height",hTop);
			$(".phoneMenubar").css("height",hMenubar);
			$(".sceneTitleBanner").css("height",hTitleBanner);			
			$(".sceneTitleBanner").css("lineHeight",hTitleBanner+'px');
			$(".nr").css("height",hNr);
			$(".bottom").css("height",hBottom);
		})
		$(".previewWrapper").children(".previewArea").eq(0).css("display","block");
		$("#audio_btn").on("touchstart",function(e){ 
			//offsetX = 0;					
		}); 
		$("body").on("touchend","#audio_btn",function(event){
			//alert(offsetX);
			//offsetX = 0;
			event.preventDefault();
			var music = $('.musicAudio').get(0); 
			if($("#audio_btn").hasClass("off")){ 
				music.play(); 
				$("#audio_btn").removeClass("off").addClass("play_yinfu"); 
				$("#yinfu").addClass("rotateMusic");
			}else{ 
				music.pause(); 
				$("#audio_btn").addClass("off").removeClass("play_yinfu"); 
				$("#yinfu").removeClass("rotateMusic"); 
			} 
		});
		$("body").on("mousedown","#audio_btn",function(event){
			//alert(offsetX);
			//offsetX = 0;
			event.preventDefault();
			var music = $('.musicAudio').get(0); 
			if($("#audio_btn").hasClass("off")){ 
				music.play(); 
				$("#audio_btn").removeClass("off").addClass("play_yinfu"); 
				$("#yinfu").addClass("rotateMusic");
			}else{ 
				music.pause(); 
				$("#audio_btn").addClass("off").removeClass("play_yinfu"); 
				$("#yinfu").removeClass("rotateMusic"); 
			} 
		});
	})
	</script>
	<script>
		var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		var base64DecodeChars = new Array(
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
			-1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
			-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);
		function base64encode(str) {
			var out, i, len;
			var c1, c2, c3;
			len = str.length;
			i = 0;
			out = "";
			while(i < len) {
			c1 = str.charCodeAt(i++) & 0xff;
			if(i == len)
			{
			out += base64EncodeChars.charAt(c1 >> 2);
				out += base64EncodeChars.charAt((c1 & 0x3) << 4);
				out += "==";
				break;
			}
			c2 = str.charCodeAt(i++);
			if(i == len)
		{
				out += base64EncodeChars.charAt(c1 >> 2);
			out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
				out += base64EncodeChars.charAt((c2 & 0xF) << 2);
				out += "=";
				break;
			}
		c3 = str.charCodeAt(i++);
			out += base64EncodeChars.charAt(c1 >> 2);
			out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
			out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));
			out += base64EncodeChars.charAt(c3 & 0x3F);
			}
			return out;
		}
		//解码的方法
		function base64decode(str) {
			var c1, c2, c3, c4;
			var i, len, out;
			len = str.length;
			i = 0;
			out = "";
			while(i < len) {
			
		do {
				c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
		} while(i < len && c1 == -1);
			if(c1 == -1)
				break;
		
			do {
			c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
			} while(i < len && c2 == -1);
		if(c2 == -1)
			break;
			out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
			
			do {
				c3 = str.charCodeAt(i++) & 0xff;
				if(c3 == 61)
				return out;
			c3 = base64DecodeChars[c3];
			} while(i < len && c3 == -1);
			if(c3 == -1)
				break;
			out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
			
			do {
				c4 = str.charCodeAt(i++) & 0xff;
				if(c4 == 61)
			return out;
				c4 = base64DecodeChars[c4];
			} while(i < len && c4 == -1);
			if(c4 == -1)
				break;
			out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
			}
			return out;
		}
	function utf16to8(str) {
		var out, i, len, c;
		out = "";
		len = str.length;
		for(i = 0; i < len; i++) {
		c = str.charCodeAt(i);
		if ((c >= 0x0001) && (c <= 0x007F)) {
			out += str.charAt(i);
		} else if (c > 0x07FF) {
			out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
			out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
			out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
		} else {
			out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
			out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
		}
		}
		return out;
	}
	
	function utf8to16(str) {
		var out, i, len, c;
		var char2, char3;
		out = "";
		len = str.length;
		i = 0;
		while(i < len) {
		c = str.charCodeAt(i++);
		switch(c >> 4)
		{ 
			case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
			// 0xxxxxxx
			out += str.charAt(i-1);
			break;
			case 12: case 13:
			// 110x xxxx   10xx xxxx
			char2 = str.charCodeAt(i++);
			out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
			break;
			case 14:
			// 1110 xxxx  10xx xxxx  10xx xxxx
			char2 = str.charCodeAt(i++);
			char3 = str.charCodeAt(i++);
			out += String.fromCharCode(((c & 0x0F) << 12) |
							((char2 & 0x3F) << 6) |
							((char3 & 0x3F) << 0));
			break;
		}
		}
		return out;
	}
	</script>
	<script>
	$(function(){
		var l=$(".slidePage").children(".previewArea").eq(0).children("li").length;
		for(var i=0;i<l;i++){
			if($(".slidePage").children(".previewArea").eq(0).children("li").eq(i).hasClass("music")){
				var musicSrc=$(".slidePage").children(".previewArea").eq(0).children("li").eq(i).find(".musicSrc").html();
				$(".nr").append('<div id="audio_btn" class="play_yinfu" style="position:absolute"><div id="yinfu" class="rotateMusic"></div></div>');
				$(".slidePage").children(".previewArea").eq(0).children("li").eq(i).find(".elementBox").append('<audio id="music" class="musicAudio" src='+musicSrc+' loop="loop" autoplay="autoplay"></audio>');
			}
		}		
	})	
	</script>
  </head>
  <body>
	<div class="fullscreenBg hidden-xs"></div>
	<div class="body">	
		<div class="container">
		  <div class="row">
			<div class="col-sm-6 col-md-6 col-xs-12 left">
				<div class="row">
					<div class="phoneBox">
						<div class="top hidden-xs">
							
						</div>
						<div class="phoneMenubar hidden-xs">
							
						</div>
						<div class="sceneTitleBanner hidden-xs">
							<span><c:out value="${gallery.galleryName}" escapeXml="false"/></span>
						</div>
						<div class="nr" style="height:100%;background:#ffffff;">
							<c:if test="${!empty gallery.sceneList}">
								<div class="pBtn"><img src="/theme/${theme}/images/pbtn.png"/></div>
								<div class="nBtn"><img src="/theme/${theme}/images/nbtn.png"/></div>
								
								<div class="previewWrapper slidePage">
								<c:forEach items="${gallery.sceneList}" var="item">
									<c:choose>
										<c:when test="${!empty item.background}">
											<c:choose>
											<c:when test="${item.background=='none'}">
												<ul class="previewArea page" style="background:#ffffff;">
											</c:when>
											<c:when test="${item.background==''}">
												<ul class="previewArea page" style="background:#ffffff;">
											</c:when>
											<c:otherwise>
												<ul class="previewArea page" style="background:url(/static/${item.background});background-size:100% 100%;">
											</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<ul class="previewArea page" style="background:#ffffff">
										</c:otherwise>
									</c:choose>
									<span class="coverUrl" style="display:none;">${item.coverUrl}</span>
									<c:if test="${!empty item.materialList}">
										<c:forEach items="${item.materialList}" var="materialList">
											<c:choose>
												<c:when test="${materialList.materialType=='text'}">
													<li ctype="text" style=${materialList.css}>
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<span class="html" style="display:none;">${materialList.content}</span>														
														<div class="elementBox" style="width:100%;height:100%;${materialList.cssExt};">
															
														</div>
													</li>
												</c:when>
												<c:when test="${materialList.materialType=='image'}">
													<li ctype="image" style=${materialList.css}>
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<div class="elementBox" style="height:100%;width:100%;${materialList.cssExt};overflow:hidden;">
															<img src="/static/${materialList.content}" style="height:100%;width:100%;"/>
														</div>
													</li>
												</c:when>
												<c:when test="${materialList.materialType=='music'}">
													<li ctype="music" style=${materialList.css} class="music">
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<div class="elementBox" style="display:none;">
															<span class="musicSrc" style="display:none;">/static/${materialList.content}</span>
														</div>
													</li>																										 
												</c:when>
												<c:when test="${materialList.materialType=='video'}">
													<li ctype="video" style=${materialList.css}>
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<video id="my-video" class="video-js elementBox vjs-default-skin" controls preload="auto"  style="width:100%;height:100%;${materialList.cssExt};overflow:hidden;"  poster="" data-setup="{'controls': true}" webkit-playsinline>
															<source src="/static/${materialList.content}" type='video/mp4'>
														</video>
													</li>
												</c:when>
												<c:when test="${materialList.materialType=='collection'}">
													<li class="collection" ctype="collection" id="slide" style=${materialList.css}>
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<div class="elementBox" style="width:100%;height:100%;${materialList.cssExt};overflow:hidden">
															<div id="html5zoo-1" style="">
																<ul class="html5zoo-slides" style="">
																	<c:if test="${!empty materialList.content}">
																		<c:set value='${fn:split (materialList.content,",")}' var='imageStrs'/>
																		<c:forEach var="colImage" items="${imageStrs}">
																			<li style="position:relative;"><a href=""><img src="/static/${colImage}"  data-description="" /></a></li>
																		</c:forEach>
																	</c:if>																
																</ul>
															</div>
														</div>
													</li>
													<script src="/theme/${theme}/js/html5zoo.js"></script>
													<script src="/theme/${theme}/js/lovelygallery.js"></script>
													<script>variousSlide($(".collection").attr("id"))</script>												
												</c:when>
												<c:when test="${materialList.materialType=='input'}">
													<li class="rotate resize input" ctype="input" style=${materialList.css}>
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<span class="inputContent" style="display:none;">${materialList.content}</span>
														<div class="elementBox" id="elementBox" style="display:block;height:100%;width:100%;outline:0;${materialList.cssExt};overflow:hidden" >
															<input style="display:block;outline:0;height:100%;background:transparent;width:100%;border:none;resize:none;padding-left:10px;"  placeholder="" type="" value=""/>		
														</div>
													</li>
												</c:when>
												<c:when test="${materialList.materialType=='button'}">
													<li class="rotate resize button" ctype="button" style=${materialList.css}>
														<span class="animation" style="display:none;">${materialList.animation}</span>
														<div class="elementBox" id="elementBox" style="display:block;height:100%;width:100%;outline:0;${materialList.cssExt};overflow:hidden" >
															<button style="display:block;outline:0;height:100%;background:transparent;width:100%;border:none;text-align:center">${materialList.content}</button>	
														</div>
													</li>
												</c:when>
											</c:choose>
										</c:forEach>
									</c:if>
									</ul>
								</c:forEach> 
								</div>
							</c:if>
						</div>
						<div class="bottom hidden-xs">
						
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-5 col-md-5 hidden-xs right">
				<div class="scanCode">
					扫一扫，分享给朋友！<br/>
					<div id="qrcode"></div>
					<script type="text/javascript">
						new QRCode(document.getElementById("qrcode"), document.URL);
					</script>					
				</div>
				<div class="share">
					<!--<div class="bdsharebuttonbox bdshare-button-style0-32" id="share"  data="{'pic':'这里写入您想要自定义的分享图片 B'}">
                        <a class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
                        <a class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a>
                        <a class="bds_qzone" data-cmd="qzone" href="#" title="分享到QQ空间"></a>
                        <a class="bds_sqq" data-cmd="sqq" href="#" title="分享到QQ好友"></a>
                        <a class="bds_douban" data-cmd="douban" href="#" title="分享到豆瓣网"></a>
                        <a class="bds_count" data-cmd="count" title="累计分享1次">1</a>
                    </div>-->
					<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
						<a class="bds_qzone">QQ空间</a>
						<a class="bds_tsina">新浪微博</a>
						<a class="bds_tqq">腾讯微博</a>
						<a class="bds_renren">人人网</a>
						<span class="bds_more">更多</span>
					</div>
				</div>
			</div>
		  </div>
		</div>
	</div>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	<script>
		$(function(){
			$(".page").removeClass("currentPage");
			$(".page").eq(0).addClass("currentPage");
		})
	</script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>	  		
	<script src="/theme/${theme}/js/html5zoo.js"></script>
	<script src="/theme/${theme}/js/lovelygallery.js"></script>
	<script type="text/javascript" src="/theme/${theme}/js/jquery.touchSwipe.min.js"></script>
	<script type="text/javascript" src="/theme/${theme}/js/TweenMax.min.js"></script>
	<script src="/theme/${theme}/js/addListenerWinSize.js"></script>
	<script type="text/javascript" id="swipeMode"></script>
	<script>
		
		$(document).ready(function() {
			if("${gallery.data.gallerySwitchMode}"=="verticalSlide"){
				document.getElementById('swipeMode').src="/theme/${theme}/js/verticalSlide.js";
			}else if("${gallery.data.gallerySwitchMode}"=="horizontalSlide"){
				document.getElementById('swipeMode').src="/theme/${theme}/js/horizontalSlide.js";
			}else if("${gallery.data.gallerySwitchMode}"=="inertialVertical"){
				document.getElementById('swipeMode').src="/theme/${theme}/js/inertialVertical.js";
			}else if("${gallery.data.gallerySwitchMode}"=="inertialHorizontal"){
				document.getElementById('swipeMode').src="/theme/${theme}/js/inertialHorizontal.js";
			} else if("${gallery.data.gallerySwitchMode}"=="zoomInSlide"){
				document.getElementById('swipeMode').src="/theme/${theme}/js/zoomInSlide.js";
			} else{
				document.getElementById('swipeMode').src="/theme/${theme}/js/verticalSlide.js";
			}
			if("${gallery.data.galleryAutoPaging}"=="autoplay"){
				setInterval("changeImg()",10000);//每隔timeInterval时间，执行一次changeImg事件
			}	
		});
		
		
	</script>
	<script type="text/javascript" id="bdshare_js" data="type=tools&mini=1" ></script> 
	<script type="text/javascript" id="bdshell_js"></script> 
	<script type="text/javascript">
		//在这里定义bds_config
		var bdPic="http://p.yingxun.mo4u.cn/static/"+$(".coverUrl").eq(0).html();
		var bds_config = {
			'bdDes':'您的自定义分享摘要',		//'请参考自定义分享摘要'
			'bdText':'点此修改您的自定义分享内容',		//'请参考自定义分享内容'
			'bdComment':'点此修改您的自定义分享评论',	//'请参考自定义分享评论'
			'bdPic':bdPic,	//'请参考自定义分享出去的图片'
			'searchPic':0,//'0为抓取，1为不抓取，默认为0，目前只针对新浪微博'
			'wbUid':'您的自定义微博 ID',		//'请参考自定义微博 id'
			
		};
		document.getElementById('bdshell_js').src = "http://share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000);
	</script>
	<script>
		$(function(){
			var l=$(".html").length;
			var element=new Array();
			for(var i=0;i<l;i++){
				element[i]=utf8to16(base64decode($(".html").eq(i).html()));
				$(".html").eq(i).siblings(".elementBox").html(element[i]);
			}
		})
		$(function(){
			var l=$(".inputContent").length;
			var element=new Array();
			for(var i=0;i<l;i++){
				element[i]=$(".inputContent").eq(i).html();
				element=JSON.parse(element[i]);				
				$(".inputContent").eq(i).siblings(".elementBox").find("input").attr("placeholder",element.placeholder);
				$(".inputContent").eq(i).siblings(".elementBox").find("input").attr("type",element.type);
			}
		})		
	</script>
	<script>
		$(function(){
			
				
				var animLength=$(".currentPage").children("li").length;
				for(var j=0;j<animLength;j++){
					var	anim=$(".currentPage").children("li").eq(j).children(".animation").html();
					anim=JSON.parse(anim);
					var m=anim.length;
					function previewEach(i,j){
						if(i<m){
							$(".currentPage").children("li").eq(j).css("animation",""),$(".currentPage").children("li").eq(0).get(0).offsetWidth = $(".currentPage").children("li").eq(0).get(0).offsetWidth,$(".currentPage").children("li").eq(j).css("animation", anim[i].animationName +" "+ anim[i].animationDuration + "s ease " + anim[i].animationDelay + "s "+  anim[i].animationIterationCount).css("animation-fill-mode", "backwards");
							if(anim[i].animationIterationCount=="infinite"){
								$(".currentPage").children("li").eq(j).css("animation-iteration-count", "infinite");												
							}	
							$(".currentPage").children("li").eq(j).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend", function(){ //动画结束时事件 
								i++,previewEach(i,j);
							});
						}						
					}
					previewEach(0,j);
				}
		})
			
		
		
	</script>
  </body>
</html>

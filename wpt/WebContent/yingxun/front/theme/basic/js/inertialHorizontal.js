// JavaScript Document
var pageID=0;
var wh;


function initAction(){
	/*上下滑动*/
	$(".slidePage").swipe({
		swipeDown:function(){swipeDownFun()},
		swipeUp:function(){swipeUpFun()},
	})
	/*上下按钮点击事件*/
	$(".nBtn").mousedown(function(){swipeUpFun()});
	$(".pBtn").mousedown(function(){swipeDownFun()})
}
/*向上滑动*/
function swipeDownFun (){
	var num=$(".slidePage .page").length;
	showpage1(pageID);
	if(pageID==0) 
        return ; 
    else 
        pageID--;
	
	/* if(pageID>0){
		showpage1(pageID);
		pageID--;
	} */ 
	
}
/*向下滑动*/
function swipeUpFun (){
	var num=$(".slidePage .page").length;
	/* if(pageID<num-1){
		showpage2(pageID);
		pageID++;
	} */ 
	showpage2(pageID);	
	if(pageID == len-1) 
        return; 
     else 
        pageID ++ ;	
	
}

function showAnim(num){
	var animLength=$(".page").eq(num).children("li").length;
		for(var j=0;j<animLength;j++){
			var	anim=$(".page").eq(num).children("li").eq(j).children(".animation").html();
			if (anim!=undefined){
			anim=JSON.parse(anim);
				var m=anim.length;
				function previewEach(i,j){
					if(i<m){
						$(".page").eq(num).children("li").eq(j).css("animation",""),$(".page").eq(num).children("li").eq(0).get(0).offsetWidth = $(".page").eq(num).children("li").eq(0).get(0).offsetWidth,$(".page").eq(num).children("li").eq(j).css("animation", anim[i].animationName +" "+ anim[i].animationDuration + "s ease " + anim[i].animationDelay + "s "+  anim[i].animationIterationCount).css("animation-fill-mode", "backwards");
						if(anim[i].animationIterationCount=="infinite"){
							$(".page").eq(num).children("li").eq(j).css("animation-iteration-count", "infinite");												
						}	
						$(".page").eq(num).children("li").eq(j).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend", function(){ //动画结束时事件 
							i++,previewEach(i,j);
						});
					}						
				}
				previewEach(0,j);
			}
		}
	
}
function showpage1(num){
	l=$(".slidePage .page").length;
	//alert(num);
	num1=num-1;
	if(num>=1){
		/* $(".page").css("marginTop",0);
		$(".page").css("width",0);
		$(".page").css("top",0);
		$(".page").css("height","100%");
		$('.page').eq(num).css("width","100%");
		$('.page').eq(num).css("width","100%");
		$('.page').eq(num1).css("left",0);
		$('.page').eq(num1).animate({width:"100%"},500)
		$(".page").eq(num1).stop().animate({width:"100%"},{queue:!1,duration:500});
		$('.page').eq(num).css("marginTop","-50%");
		$('.page').eq(num).css("top","50%");
		$('.page').eq(num).animate({left:"100%",width:"0%",height:"0%",marginTop:"0%"},500)
		$(".page").eq(num).stop().animate({left:"100%",width:"0%",height:"0%",marginTop:"0%"},{queue:!1,duration:500});
		showAnim(num1); */
		
		$(".page").css("zIndex",0);
		$(".page").eq(num).css("zIndex",10);
		$(".page").eq(num1).css("zIndex",20);
		$('.page').css("transform","scale(0,0)");
		$(".page").css("height","100%").css("width","100%");
		$(".page").css("transform","translate3d(-100%,0,0) scale(0,0)");
		$(".page").eq(num).css("transform","translate3d(0,0,0)");		
		setTimeout("changeTranslate("+num1+")",500);
		setTimeout("changeTranslate1("+num+")",500); 
		$(".page").removeClass("inertialRight").removeClass("inertialRight2").removeClass("inertialLeft").removeClass("inertialLeft2");
		$(".page").eq(num1).addClass("inertialRight");
		$(".page").eq(num).addClass("inertialRight2");
		showAnim(num1);
	}
}
function showpage2(num){
	l=$(".slidePage .page").length;
	num1=num+1;
	if(num<l-1){
		/*  $(".page").css("marginTop",0);
		$(".page").css("width",0);
		$(".page").css("top",0);
		$(".page").css("height","100%");		
		$(".page").removeClass("inertialRight").removeClass("inertialRight2");
		$('.page').eq(num).css("width","100%");
		$('.page').eq(num1).css("left","100%");
		$(".page").eq(num1).stop().animate({left:0,width:"100%"},{queue:!1,duration:500});
		$('.page').eq(num).css("marginTop","-50%");
		$('.page').eq(num).css("left",0);
		$('.page').eq(num).css("top","50%");
		$(".page").eq(num).stop().animate({width:"0%",height:"0%",marginTop:"0%"},{queue:!1,duration:500});
		$(".page").eq(num).addClass("currentPage")
		showAnim(num1);  */
		$(".page").css("zIndex",0);
		$(".page").eq(num).css("zIndex",10);
		$(".page").eq(num1).css("zIndex",20);
		$(".page").css("height","100%").css("width","100%");
		$(".page").css("transform","translate3d(100%,0,0) scale(0,0)");
		$(".page").eq(num).css("transform","scale(1,1) translate3d(0,0,0)");
		$('.page').eq(num1).css("transform","scale(1,1)");
		setTimeout("changeTranslate2("+num1+")",500);
		setTimeout("changeTranslate3("+num+")",500);
		$(".page").removeClass("inertialRight").removeClass("inertialRight2").removeClass("inertialLeft").removeClass("inertialLeft2");
		$(".page").eq(num1).addClass("inertialLeft");
		$(".page").eq(num).addClass("inertialLeft2");
		showAnim(num1);
		
	}
}
//手机事件
		//初始化
			var arr=$(".page");
			var len=arr.length;
			var scaleW=$(window).height();
			var index = 0;
		//绑定事件				 
        function upNext() { 
            var action; 
            var width = $(".page").width(); 
            var height = $(".page").height();
			var offsetX;
            $(".page").on("touchstart",function(e){ 
				var touch = e.originalEvent.targetTouches[0]
				startX = touch.pageX;
				offsetX = 0;				
			}); 
			$(".page").on("touchmove",function(e){ 
				e.preventDefault();
				var touch = e.originalEvent.targetTouches[0]
				offsetX = touch.pageX -startX;				
			}).on("touchend",function(e){ 
				move=false;
				e.preventDefault();
				var boundary = scaleW/10;
				if(offsetX >= boundary){
					showpage1(pageID);
					if(pageID==0) 
                       return ; 
                   else 
                       pageID--; 
				   
				}else if(offsetX < 0 && offsetX < -boundary){
					showpage2(pageID);	
					if(pageID == len-1) 
                    return; 
                  else 
                      pageID ++ ; 
				  				
				}		
									
			});	
        } 
			
				
	//函数启用	
$(document).ready(function() {
	var w=$(window).width();
	if(w>=768){
		initAction();
		var win=$(window);
		wh=win.height()>=win.width()?win.height():win.width();
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(0),0,{css:{zIndex:20},ease:Expo.easeOut})
		$(".page").eq(0).addClass("currentPage")
		$(".page").show();
		$(".nBtn").css("left","89%").css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		$(".pBtn").css("left",0).css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		
	}else{
		$('.page').css({"position":"absolute","top":0});
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(0),0,{css:{zIndex:20},ease:Expo.easeOut})
		upNext();
		/* $(".nBtn").on("touchstart",function(){swipeUpFun()});
		$(".pBtn").on("touchstart",function(){swipeDownFun()}) 
		$(".nBtn").hide();
		$(".pBtn").hide();*/
		$(".page").show();
		$(".nBtn").css("left","89%").css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		$(".pBtn").css("left",0).css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		
		//setInterval(changeImg,10000);每隔timeInterval时间，执行一次changeImg事件
	}
	
});
function changeImg()
{
	showpage2(pageID);
	pageID ++ 
	if(pageID == len) 
	{  
		pageID=0;
	}	
    
}
function changeMarginLeft(num){
	var w=$(".page").eq(num).width();
	$(".page").eq(num).css("marginLeft",-w/2);
}

function changeScale(num1){
	$('.page').eq(num1).css("transform","scale(0,0)");
}
function changeTranslate(num1){
	$('.page').eq(num1).css("transform","translate3d(0,0,0)");
}
function changeTranslate1(num1){
	$('.page').eq(num1).css("transform","translate3d(100%,0,0) scale(0,0)");
}
function changeTranslate2(num1){
	$('.page').eq(num1).css("transform","translate3d(0,0,0)");
}
function changeTranslate3(num1){
	$('.page').eq(num1).css("transform","translate3d(-100%,0,0) scale(0,0)");
}
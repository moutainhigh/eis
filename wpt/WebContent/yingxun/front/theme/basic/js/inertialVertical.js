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
		$(".page").css("marginLeft",0);
		$(".page").css("height",0);
		$(".page").css("left",0);
		$(".page").css("width","100%");
		$('.page').eq(num).css("height","100%");
		$('.page').eq(num1).css("top",0);
		//$('.page').eq(num1).animate({height:"100%"},500)
		$(".page").eq(num1).stop().animate({height:"100%"},{queue:!1,duration:500});
		$('.page').eq(num).css("marginLeft","-50%");
		$('.page').eq(num).css("left","50%");
		//$('.page').eq(num).animate({top:"100%",width:"0%",height:"0%",marginLeft:"0%"},500)
		$(".page").eq(num).stop().animate({left:"100%",width:"0%",height:"0%",marginTop:"0%"},{queue:!1,duration:500});
		$(".page").eq(num).addClass("currentPage")
		showAnim(num1);
	}
}
function showpage2(num){
	l=$(".slidePage .page").length;
	num1=num+1;
	if(num<l-1){
		$(".page").css("marginLeft",0);
		$(".page").css("height",0);
		$(".page").css("left",0);
		$(".page").css("width","100%");
		$('.page').eq(num).css("height","100%");
		$('.page').eq(num1).css("top","100%");
		//$('.page').eq(num1).animate({top:0,height:"100%"},500)		
		$(".page").eq(num1).stop().animate({top:0,height:"100%"},{queue:!1,duration:500});
		$('.page').eq(num).css("marginLeft","-50%");
		$('.page').eq(num).css("top",0);
		$('.page').eq(num).css("left","50%");
		//$('.page').eq(num).animate({width:"0%",height:"0%",marginLeft:"0%"},500)		
		$(".page").eq(num).stop().animate({width:"0%",height:"0%",marginLeft:"0%"},{queue:!1,duration:500});
		$(".page").eq(num).addClass("currentPage")
		showAnim(num1);
	}
}
//手机事件
		//初始化
			var arr=$(".page");
			var len=arr.length;
			var scaleH=$(window).height();
			var index = 0;
		//绑定事件				 
        function upNext() { 
            var action; 
            var width = $(".page").width(); 
            var height = $(".page").height();
			var offsetY;
            $(".page").on("touchstart",function(e){ 
				var touch = e.originalEvent.targetTouches[0]
				startY = touch.pageY;
				offsetY = 0;				
			}); 
			$(".page").on("touchmove",function(e){ 
				e.preventDefault();
				var touch = e.originalEvent.targetTouches[0]
				offsetY = touch.pageY -startY;				
			}).on("touchend",function(e){ 
				move=false;
				e.preventDefault();
				var boundary = scaleH/10;
				if(offsetY >= boundary){
					showpage1(pageID);
					if(pageID==0) 
                       return ; 
                   else 
                       pageID--; 
				   
				}else if(offsetY < 0 && offsetY < -boundary){
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
		 $(".nBtn").show();
		$(".pBtn").show();
	}else{
		$('.page').css({"position":"absolute","top":0});
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(0),0,{css:{zIndex:20},ease:Expo.easeOut})
		upNext();
		/* $(".nBtn").on("touchstart",function(){swipeUpFun()});
		$(".pBtn").on("touchstart",function(){swipeDownFun()})
		 */
		 $(".nBtn").show();
		$(".pBtn").show();
		$(".page").show();
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
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
		$(".page").removeClass("currentPage")
		TweenMax.to($('.page'),0,{css:{left:0},ease:SteppedEase.easeOut})
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(num1),0,{css:{zIndex:10},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(num),0,{css:{zIndex:20},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(num),1,{css:{left:"100%"},ease:Expo.easeOut})
		$(".page").eq(num).addClass("currentPage")
		showAnim(num1);
	}
}
function showpage2(num){
	l=$(".slidePage .page").length;
	num1=num+1;
	if(num<l-1){
		$(".page").removeClass("currentPage")
		TweenMax.to($('.page'),0,{css:{left:0},ease:SteppedEase.easeOut})
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(num1),0,{css:{zIndex:10},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(num),0,{css:{zIndex:20},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(num),1,{css:{left:"-100%"},ease:Expo.easeOut})
		$(".page").eq(num).addClass("currentPage")
		showAnim(num1);
	}
}
//手机事件
		//初始化
			var arr=$(".page");
			var len=arr.length;
			var scaleW=$(window).width();
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
		$('.page').css({"position":"absolute","left":0});
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(0),0,{css:{zIndex:20},ease:Expo.easeOut})
		$(".page").eq(0).addClass("currentPage")
		$(".nBtn").css("left","89%").css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		$(".pBtn").css("left",0).css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		$(".page").show();
	}else{
		$('.page').css({"position":"absolute","left":0});
		TweenMax.to($('.page'),0,{css:{zIndex:0},ease:Expo.easeOut})
		TweenMax.to($('.page').eq(0),0,{css:{zIndex:20},ease:Expo.easeOut})
		upNext();
		$(".nBtn").css("left","89%").css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		$(".pBtn").css("left",0).css("top","40%").css("transform",'rotateZ(-'+90+'deg)').css("height","7%").show();
		$(".page").show();//每隔timeInterval时间，执行一次changeImg事件
	}
	
});
function changeImg()
{
	showpage2(pageID);
	if(pageID == len-1) 
	{  
		pageID=0;
	}	
    else 
    {
		pageID ++ ; 		
	}
}
$(function(){
	$(".orient-left").mouseover(function(){
		$(this).attr("src","/theme/basic/images/left_list_arrow_current.png");
	})
	$(".orient-left").mouseout(function(){
		$(this).attr("src","/theme/basic/images/left_list_arrow.png");
	})
	$(".orient-right").mouseover(function(){
		$(this).attr("src","/theme/basic/images/right_list_arrow_current.png");
	})
	$(".orient-right").mouseout(function(){
		$(this).attr("src","/theme/basic/images/right_list_arrow.png");
	})
	var pageCount = $(".paginations").children("li").length;
	var currentIndex = 0;
	$(".theme-cont").width(4*100+'%');
	$(".theme-list").css("width",100/4.0+'%');
	/*
	//左右滑动
	$(".orient-right").click(function(){
		currentIndex ++;
		currentIndex = currentIndex%pageCount;
		$(".theme-cont").addClass("move-anim");		
		$(".theme-cont").css("margin-left",-100*currentIndex+'%');
		$(".paginations").find(".item").removeClass("current");
		$(".paginations").find(".item").eq(currentIndex).addClass("current");
	})
	$(".orient-left").click(function(){
		if(currentIndex === 0){
			currentIndex = pageCount-1;
		}else{
			currentIndex --;
		}		
		$(".theme-cont").addClass("move-anim");		
		$(".theme-cont").css("margin-left",-100*currentIndex+'%');		
		$(".paginations").find(".item").removeClass("current");
		$(".paginations").find(".item").eq(currentIndex).addClass("current");
	})*/
	//改为渐进
	$(".theme-list-current").fadeIn();
	var imageSelectedIndex;
	$(".theme-cont .item").click(function(){
		imageSelectedIndex=$(this).siblings(".theme-id").val();
		$(".theme-chose").remove();
		$(".theme-cont .item").removeClass("current");
		$(this).addClass("current")
		$(this).find(".theme-title").append('<div class="theme-chose"><img src="/theme/basic/images/theme-chosen.png" class="theme-chose-img"/></div>')
	})
	$(".finish-step").click(function(){		
		if(imageSelectedIndex==undefined){
			noticeBox("请选择所需主题！");
			return false;
		}
		$.ajax({
            type: "GET",
            url: '/guide/step3.json',
            data: {
               themeId: imageSelectedIndex
            },
            async: false,
            dataType: 'json',
            success: function (data) {
                if(data.message.operateCode==102008){
					noticeBox(data.message.message,'<a href="/">点击进入主界面</a>');
				}else{
					noticeBox(data.message.message);
				}
            },
            error: function (xml, err) {
               noticeBox("系统繁忙,请稍后再试");
            }
        });
	})
})
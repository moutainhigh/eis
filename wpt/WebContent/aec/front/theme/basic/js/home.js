require.config({
    // baseUrl:'/',
urlArgs: "v=" +  (new Date()).getTime(),

shim: {
        "jquery": {
            "exports": "$"
        },
		"topslider" : {
            deps : ["jquery"],
			exports: 'jQuery.fn.slide'
        },
		"bottomslider" : {
            deps : ["jquery"]
        },
		"common" : {
            deps : ["jquery"]
        },

		"respond" : {
            deps : ["jquery"]
        }//,
//		"main" : {
//            deps : ["jquery"]
//        }

    },
	
	    paths:{
		
		"jquery" : ["http://libs.baidu.com/jquery/1.11.3/jquery.min", "/pc/js/jquery-1.11.3.min"],
		// "jquery": "empty:"
	   // "domReady" : '/pc/config/domReady',
		 
		 
		"topslider":"/pc/js/slider",
		
		"common":"/pc/js/common.min",
		"bottomslider":"/js/jquery.slides.min",
		    
		"respond":"/pc/js/respond.src",
		//"main":"/pc/r/main"
		

    },
    });

require(['jquery','common','topslider','bottomslider','respond'],function($){
	
//require(['jquery','main'],function($){
$(".prev,.next").hover(function(){
		$(this).stop(true,false).fadeTo("show",0.9);
	},function(){
		$(this).stop(true,false).fadeTo("show",0.4);
	});
	
$(".banner-box").slide({
		
		titCell:".hd ul",
		mainCell:".bd ul",
		effect:"fold",
		interTime:1500,
		delayTime:500,
		autoPlay:true,
		autoPage:true,
		trigger:"click"
	});
	
	$('#slides').slidesjs({
        width: 992,
        height: 300,
        navigation: false,
        play: {
            active: false,
            auto: true,
            interval: 3500,
            swap: true
        }
    });
	
	
       try{if(window.console&&window.console.log){console.log("一张网页，要经历怎样的过程，才能抵达用户面前？\n一位新人，要经历怎样的成长，才能站在技术之巅？\n探寻这里的秘密；\n体验这里的挑战；\n成为这里的主人；\n加入盛世家和，你可以影响世界。\n");}}catch(e){};
	  



	

   

});


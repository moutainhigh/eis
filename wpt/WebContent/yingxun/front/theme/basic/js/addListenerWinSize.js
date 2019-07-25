/**********************屏幕尺寸改变**************************************/

$(function(){
	windowSizeChange()
	window.addEventListener("resize", windowSizeChange, false);
})

function windowSizeChange(){
		 var newHeight=$(window).height();
		 var newWidth=$(window).width();
		 if(newWidth > newHeight){
			$(".slidepage").hide();
			$(".heng").show();
		 }else{
			$(".slidepage").show(); 
			$(".heng").hide();
		 }
}
/************************************************************/

$(function(){
	windowChange()
	window.addEventListener('resize',windowChange,false);
})
function windowChange(){
	var newHeight=$(window).height()
	var newWhidth=$(window).width()
	if(newWhidth>newHeight){
		$('.slidePage').hide()
		$('.heng').show()
		//alert(".heng.show")
	}else{
		$('.slidePage').show()
		$('.heng').hide()
		//alert(".heng.hide")
	}
}

function noticeBox(title,cont){
	$(".notice-modal").remove();
	var popupBox = writeTxt(title,cont,"notice");
	$("body").append(popupBox);
	$(".notice-modal").css("visibility","visible");
	$(".modal-close").click(function(){
		$(".pop-modal").remove();
	})	
}

function writeTxt(title,cont,type){
	if(cont==undefined){
		cont='';
	}
	var html= '<div class="full-screen">';
	html+='<div class="notice-modal zoomIn">';
	html+='<h1 class="title oneline '+ type+'-h1">'+title+'</h1>';
	html+='<h3 class="cont">'+cont+'</h3>';	
	html+='<div class="modal-close" onClick="removeModal(this)"><img src="/theme/basic/images/popup_close.png" class="modal-close-img"></div>';	
	html+='</div>';
	html+='</div>';
	return html;
}

function removeModal(ele){
	$(ele).parent().parent().remove();
}

function successBox(title,cont){
	$(".notice-modal").remove();
	var popupBox = writeTxt(title,cont,"success");
	$("body").append(popupBox);
	$(".notice-modal").css("visibility","visible");
	$(".modal-close").click(function(){
		$(".pop-modal").remove();
	})	
}
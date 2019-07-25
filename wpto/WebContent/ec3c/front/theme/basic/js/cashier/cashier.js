function toggle(_this){
    var thisObj = $(_this);
    var show = thisObj.data("show");
    if(show == undefined){
      	show = false;
    }
    if(show){
      	//已展开
      	thisObj.data("show",false);
      	thisObj.find("font").html("订单详情");
    }else{
      	//已隐藏
      	thisObj.data("show",true);
      	thisObj.find("font").html("收起详情");
    }
    $(".order-info").slideToggle("fast");
}
function testUrl()
{
	var t="";
	var url=window.location.search;
	if(url.indexOf("?")!=-1) 
	{ 
	   var str = url.substr(1) 
		strs = str.split("&"); 
		var key=new Array(strs.length);
		var value=new Array(strs.length);
		for(i=0;i<strs.length;i++) 
		{ 
		 key[i]=strs[i].split("=")[0]
		 value[i]=unescape(strs[i].split("=")[1]); 
		 if(key[i]=="currentStatus"){
			 t=value[i];
		 }
	   } 
	}
	return t;
}
function cancel(id){
	var confirm = window.confirm('确定删除此订单吗？删除之后将无法恢复！');  
	if (confirm) {
		$.ajax({
			type:"POST",
			url:'/order/delItem.json',
			async:false,
			data:{cartId:id},
			dataType:'json',
			success:function(data){
				alert(data.message.message);
				location.reload();
			},
			error:function(data){
				alert("系统繁忙，请稍后再试！");
			}

		})
	}
}
function confirmReceived(tid){
	var confirm = window.confirm('确认收货么？');  
    if(confirm){  
		$.ajax({
			type : "POST",
			url :  'confirmDelivery/'+tid+'.json',
			async:false,
			dataType : 'json',
			success : function (data) { 
				if(data.message.operateCode==102008){
					alert(data.message.message);
					location.href = "/order/index.shtml?currentStatus=710052&page=1&rows=10"
				}else{
					alert(data.message.message);
				}			
			},
			error:function(data){
				alert("系统繁忙，请稍后再试！");
			},
		}) 
    }else{  
        return false;  
    }  
}	
function getDeliveryInfo(orderId){
	$(".msg").show();
	$.ajax({ 
	type: "GET", 	
	url: "/deliveryOrder/traceByDeliveryOrder/"+orderId+".json",
	data:{returnUrl:location.href},
	dataType: "json",
	success: function(data){
		$(".msg").hide();
		if(data.deliveryOrder){
			url=data.deliveryOrder.traceData.traceUrl;
			url+=(url.indexOf('?')!=-1?"&":"?")+"ts="+(new Date()).getTime();
			location.href=url;
		}else if(data.message){
			alert(data.message.message);
		}
	},				
	});	
}
$(function(){
	if(testUrl()==710019){
		$(".ui-btn-group li a").removeClass("current");
		$(".ui-btn-group li").eq(1).children("a").addClass("current");
	}else if(testUrl()==710050||testUrl()==710051){
		$(".ui-btn-group li a").removeClass("current");
		$(".ui-btn-group li").eq(2).children("a").addClass("current");
	}else if(testUrl()==710051||testUrl()==710052){
		$(".ui-btn-group li a").removeClass("current");
		$(".ui-btn-group li").eq(3).children("a").addClass("current");
	}	
})

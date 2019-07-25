function buySuccess(responseText, statusText){
	if(responseText.message.operateCode == 710018){
		//获取当前的交易ID
		alert(responseText.message.messageId + "/" + responseText.message.message);
		$.ajax({
		   type:"POST",
		   url:'/buy.json',
		   data:{orderId:responseText.message.messageId},
		   success: function(msg){
				if(msg.message.messageId == 710013){
					alert("您的请求已提交，请耐心等待");
				} else {
					alert(msg.message.message + "[" + msg.message.operateCode + "]");
				}
		   }
		});
	}
}

function buyError(responseText, statusText){
		alert(responseText.message.operateCode + "/" + responseText.message.message);
}

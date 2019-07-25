$(function(){
    var conts = 0;
    $("#rongQi input[name=food]").each(function () {

        if ($(this).prop("checked")) {
            for (var i = 0; i < $(this).length; i++) {
                conts += parseFloat($(this).parent().parent().prev().find("#productcount").val());
            }
        }else{

            $(".quanxuan").removeAttr("checked");

        }
    });
    $(".num").html(conts);
})
function ok(tid){
	var isFirefox=navigator.userAgent.toUpperCase().indexOf("FIREFOX")?true:false;
    var confirm = window.confirm('确认收货么？');
    console.log("asdsadsa");
    if (confirm) {
        $.ajax({
            type:"POST",
            url: '/order/confirmDelivery/'+tid+'.json',
            success: function (data) {
                alert(data.message.message);
				//location.replace(window.location.href);
				//location.href = location.href;
				//location.reload(true);
				if(isFirefox){
					//window.location.href=window.location.href+"&r="+10000*Math.random();火狐不好使
					window.location=window.location.href+"&r="+10000*Math.random();火狐不好使
				}
				else{
					location.reload();
				}
				//window.history.go(0);
            },
            error: function (data) {
                alert("系统繁忙，请稍后再试！");
            },
        })
    }else{
        return false;
    }
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
function abolish(orderId){
    var confirm=window.confirm("确定要取消吗?");
    console.log(orderId);
    $.ajax({
        url:"/order/cancel/"+orderId+".json",
        type:"post",
        dataType:'json',
        success:function(data){
            //ajax返回的数据
            //console.log(data.message.operateCode);
            //console.log(operateResultSuccess);
            location.reload();
            if(data.message.operateCode != operateResultSuccess){

                alert(data.message.message);
            }
            if (data.message.operateCode == operateResultSuccess) { //成功


                alert("取消成功!");
                location.reload();


            }



        }
    });
}
	var isfoodchecked = true;
$(function() {
	isChecked=false; 
    $("#rongQi input[name=food],#quanxuan").prop("checked",true);
    GetCount();
	GetNum();
	settleStatus();
    $("#rongQi input[name=food]").click(function () { 
	    isFoodChecked();  
		GetCount(); 
		settleStatus();		
		GetNum();      
		isQuanquanChecked();			 
    });
    $(".quanxuan").click(function() {         
        if (this.checked) {
            $(":checkbox").prop("checked", "checked");           
        }
        else {
            $("#zong1").html("0.00");
            $(":checkbox").removeAttr("checked");  					  
		}	
		settleStatus();
		GetCount();
		GetNum();
    });
    $("#delgwc").click(function() {
		
        var strIds = new Array();//声明一个存放id的数组
        $('input[name="food"]:checked').each(function (i, d) {
            if (d.checked) {
                strIds.push(d.value);
            }
        });
        if (strIds.length < 1){
            alert("请选择!");
            return false;
        }
        $.ajax({
            type:"POST",
            url:'/cart/clear.json',
            data:{
                id:strIds
            },
            dataType:'json',
            success:function (data) {
                alert("删除成功");
                window.location.reload();
            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
    });    
    $("#balance").click(function() {
		if(isfoodchecked)
		{
			var a="";
			var orderId = new Array();//声明一个存放id的数组
			$('input[name="food"]:checked').each(function (i, d) {
				if (d.checked) {
				   a+='tid='+d.value+'&';
                }
			});
			a=a.substring(0,a.length-1)
			location.href='/buy/delivery.shtml?'+a;
		}
    });
});
function settleStatus(){	
	var strIds = new Array();
	$('input[name="food"]:checked').each(function (i, d) {	
            if (d.checked) {
				strIds.push(d.value);
            }
        });
       if (strIds.length < 1){           
		isfoodchecked=false;
		$("#balance").css("backgroundColor","#ddd");
         }
		 else{
			 isfoodchecked=true;
			 $("#balance").css("backgroundColor","rgb(224, 67, 67)");
		 }      
}
function changevalue(t,transactionId,e){
    e.preventDefault();
	$("body").append('<div class="msg">正在处理，请稍等…</div>');
    $.ajax({
        type:"POST",
        url:'/cart/update.json',
        data:{
            transactionId:transactionId,
            count:t
        },
        dataType:'json',
        success:function (data) {
			$(t).attr("value",t)           
             //window.location.reload();
           GetNum();
		   GetCount();
		   $(".msg").remove();
        },
        error:function (data) {
			$(".msg").remove();
            alert("系统繁忙,请稍后再试");
            return false;
        }
    });	
}
function increase(t,transactionId,e){
    e.preventDefault();
	$("body").append('<div class="msg">正在处理，请稍等…</div>');
    var addcount = parseInt($(t).prev().val())+1;
    $.ajax({
        type:"POST",
        url:'/cart/update.json',
        data:{
            transactionId:transactionId,
            count:addcount
        },
        dataType:'json',
        success:function (data) {
			$(".msg").remove();
            $(t).prev().val(addcount);
            GetNum();
			GetCount();
        },
        error:function (data) {
			$(".msg").remove();
            alert("系统繁忙,请稍后再试");
            return false;
        }
    });
}
function decrease(t,transactionId,e){
   e.preventDefault();
	var jiancount = parseInt($(t).next().val())-1;
	if(jiancount<0){
		return false;
	}
	$("body").append('<div class="msg">正在处理，请稍等…</div>');
    $.ajax({
        type:"POST",
        url:'/cart/update.json',
        data:{
            transactionId:transactionId,
            count:jiancount
        },
        dataType:'json',
        success:function (data) {
			$(".msg").remove();
            $(t).next().val(jiancount);	
            GetNum();
			GetCount();  
			location.reload();
        },
        error:function (data) {
			$(".msg").remove();
            alert("系统繁忙,请稍后再试");
            return false;
        }
    });
}
function complete(t,e){
	e.preventDefault();		
	$(t).parent().prev().show();
	$(t).parent().hide();
}
function edit(t,e){
	e.preventDefault();
	$(t).next().show();
	$(t).hide();
}
function GetCount() {
    var conts = 0;    
	$(".quanxuan").prop("checked","checked");
    $("#rongQi input[name=food]").each(function () {
        if ($(this).prop("checked")) {
            for (var i = 0; i < $(this).length; i++) {
				conts += parseFloat($(this).parent().parent().parent().find("#productcount").val()*$(this).parent().parent().parent().find(".danjia").html());
				$(this).parent().parent().parent().find(".sumtotal").html(($(this).parent().parent().parent().find("#productcount").val()*$(this).parent().parent().parent().find(".danjia").html()).toFixed(2));
            }
        }else{
            $(".quanxuan").removeAttr("checked");
        }
    });
    $("#zong1").html(conts.toFixed(2));
}
function GetNum() {
    var conts = 0;
	$(".quanxuan").prop("checked","checked");
   $("#rongQi input[name=food]").each(function (i,d) {
        if ($(this).prop("checked")) { 
            for (var i = 0; i < $(this).length; i++) {
				conts += parseInt($(this).parent().parent().parent().find("#productcount").val());
        }
         }else{
            $(".quanxuan").removeAttr("checked");
        } 
    });
    $("#balance").html("去结算("+conts+")");
}
function isFoodChecked(){
	 var arr=[];
	 $("#rongQi input[name=food]").each(function () {
		arr.push($(this).prop("checked"));
    });
	for(var i=0;i<arr.length;i++){
		if(arr[i]){
			isChecked=true;
			break;
			}
			else{
			isChecked=false;	
				}			
		}
	}
function isQuanquanChecked(){
	 var arr=[];
	 $("#rongQi input[name=food]").each(function () {
		arr.push($(this).prop("checked"));
    });
	$("#rongQi #quanxuan").prop("checked","checked");
	for(var i=0;i<arr.length;i++){
		if(arr[i]==false){
			$("#rongQi #quanxuan").prop("checked",false);
		}
	} 
}
function deleteCart(id,e,t){
	e.preventDefault();
	if(confirm("是否要删除该商品")){
        $.ajax({
            type:"POST",
            url:'/cart/delete.json',
            data:{
                transactionIds:id
            },
            dataType:'json',
            success:function (data) {
                //malert(data.message.message,1000);
                //window.location.reload();
                $(t).parents('#gouwuche_center').next('div').remove()
                $(t).parents('#gouwuche_center').remove()
                if ($('#gouwuche_center').length && $('#gouwuche_center').length>0 ) {
                    return false
                }else{
                    $('#rongQi').append('<p  style="text-align:center;margin-top: 50px;">购物车没有商品哦~ <br><a href="/" style="color: #fff; margin-top: 23px; display: inline-block; padding: 6px 12px;border-radius: 5px; background-color: rgba(127, 164, 24, 0.7);"><i class="icon iconfont icon-arw-toLeft"></i>去购物</a></p>')
                }
            },
            error:function (data) {
                malert("系统繁忙,请稍后再试",1000);
                return false;
            }
        }); 
        }
}

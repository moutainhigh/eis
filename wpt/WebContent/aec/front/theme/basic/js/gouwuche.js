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
       if($(".btn_cancel").text()=="编辑"){
		     
			  if(isChecked)
			   {
				  $(".shanchu2").css("backgroundColor","#ff6400");
			       }
				   else{
					    $(".shanchu2").css("backgroundColor","#a4a4a4");
					   }
			  
		   }
		   else if($(".btn_cancel").text()=="取消"){
			 
			 if(isChecked)
			   {
				  $("#balance").css("backgroundColor","#ff6400");
			       }
				   else{
					    $("#balance").css("backgroundColor","#a4a4a4");
					   }
			}
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
        //var s='';
        var strIds = "";//声明一个存放id的数组
        $('input[name="food"]:checked').each(function (i, d) {

            if (d.checked) {
                //strIds.push(d.value);
				var str="&"+d.value;
				strIds+=str;
            }
            

             //s+=$(this).val()+',';
        });
        if(strIds.length<1){
            alert("请选择要删除的商品");
            return;
        }
        if(confirm("是否要删除该商品")){
              strIds=strIds.substr(1);
        $.ajax({
            type:"POST",
            url:'/cart/delete.json',
            data:{
                id:strIds
            },
            dataType:'json',

            success:function (data) {

                //alert("删除成功");

                window.location.reload();

            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        }); 
        }
     
    });
    $(".btn_cancel").click(function() {

        if ($(this).text()=="取消") {

            $("#rongQi input[name=food],#quanxuan").prop("checked",true);

            $(this).text("编辑");
            GetCount();
            $(".delgwc").hide();
            $(".qujiesuan").show();
        }

        else {

            $("#rongQi input[name=food],#quanxuan").prop("checked",false);

            $(this).text("取消");
            $(".delgwc").show();
            $(".qujiesuan").hide();
            
            $("#zong1").html("0.00");

        }


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
			 $("#balance").css("backgroundColor","#ff6400");
		 }
      
}
function changevalue(t,transactionId){

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
             window.location.reload();
             GetNum();
		     GetCount();
        },
        error:function (data) {

            alert("系统繁忙,请稍后再试");
            return false;
        }
    });
	
}
function increase(t,transactionId){
    var addcount = parseInt($(t).prev().attr("value"))+1;
    $.ajax({
        type:"POST",
        url:'/cart/update.json',

        data:{
            transactionId:transactionId,
            count:addcount

        },
        dataType:'json',

        success:function (data) {

            $(t).prev().attr("value",addcount)
           
            window.location.reload();
            GetNum();
			GetCount();

        },
        error:function (data) {

            alert("系统繁忙,请稍后再试");
            return false;
        }
    });

}
function decrease(t,transactionId){


    var jiancount = parseInt($(t).next().attr("value"))-1;
	if(jiancount==0){
		return false;
	}
    $.ajax({
        type:"POST",
        url:'/cart/update.json',
        data:{
            transactionId:transactionId,
            count:jiancount

        },
        dataType:'json',

        success:function (data) {
            $(t).next().attr("value",jiancount)
			
             window.location.reload();
            GetNum();
           GetCount();
             
        },
        error:function (data) {

            alert("系统繁忙,请稍后再试");
            return false;
        }
    });

}
function GetCount() {
    var conts = 0;

    
    $("#rongQi input[name=food]").each(function () {

        if ($(this).prop("checked")) {


            for (var i = 0; i < $(this).length; i++) {
                conts += parseFloat($(this).parent().parent().prev().find(".sumtotal").text());
            }
        }else{

            $(".quanxuan").removeAttr("checked");

        }
    });
    $("#zong1").html(conts.toFixed(2));

}
function GetNum() {
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
	for(var i=0;i<arr.length;i++){
		if(arr[i]==false){
			$("#rongQi #quanxuan").prop("checked",false);
			}
		}
	}

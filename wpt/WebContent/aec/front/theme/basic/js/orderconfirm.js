$(function(){
	var isxgform=true;
	var ismoreaddress=true;
	setTotalMoney();
	$(".selects").citySelect({
		          nodata:"none",
		           required:false
	        });
	$("#title").blur(function(){
		if($("#title").val().length==0){
			return false;
		}
		$.ajax({
            type: "POST",
            url: '/buy/setInvoice.json',
            data: {
                title: $("#title").val(),
                orderId: $("input[id='orderId']").val()
            },
            dataType: 'json',
            success: function (data) {

                 alert("保存发票信息成功");
            },
            error: function (data) {
        
                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
	})
	$("#isfapiao").click(function (){

        if($("#isfapiao").prop("checked")){
            $("#isfapiaohang").removeClass("no_border");
            $("#box_fapiao").slideDown();
			
			

        }else{

            $("#isfapiaohang").addClass("no_border");
            $("#box_fapiao").slideUp();
			
        }

    })
	$("#isdaijinquan").click(function (){

        if($("#isdaijinquan").prop("checked")){
            $("#isdaijinquanhang").removeClass("no_border");
            $("#box_daijinquan").slideDown();
			
			

        }else{

            $("#isdaijinquanhang").addClass("no_border");
            $("#box_daijinquan").slideUp();
			
        }

    })
	$("#ischongzhi").click(function (){

        if($("#ischongzhi").prop("checked")){
            $("#ischongzhihang").removeClass("no_border");
            $("#box_chongzhi").slideDown();
			
			

        }else{

            $("#ischongzhihang").addClass("no_border");
            $("#box_chongzhi").slideUp();
			
        }

    })
	$(".first_box input[type=radio]").click(function(){
		var foods = document.getElementsByName("foods");
		var openIDStr="";
		var param={};
		var arr=[];
		for ( var i = 0; i < foods.length; i++) {	
				var str=foods[i].value+",";
				openIDStr+=str;
			    //param["tid"]=foods[i].value
				//arr.push(foods[i].value);
			}
		openIDStr.substring(0,openIDStr.length-1);
		param["orderId"]=$("#orderId").val();
		param["addressBookId"]=$(".first_box input[type=radio]:checked").val();
		param["tid"]=openIDStr;
        $.ajax({
            type: "POST",
            url: '/buy/countDeliveryFee.json',
            data: param,
            dataType: 'json',
            success: function (data) {
				      var fee=parseFloat(data.deliveryOrder.fee.money);
					  $("#fee").html(fee.toFixed(2));
					  setTotalMoney();  
            },
            error: function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		})
	    $("#btnAddAddress").click(function (){

          if(isxgform){
			
            if(ismoreaddress==false)
			{
				$("#box_moreAddress").slideUp();
			    $("#btnMoreAddress").html("更多收货地址");
				ismoreaddress=!ismoreaddress;
			}
			
				$("#btnAddAddress").html("取消");
				$("#xg_form").slideDown();
			}else{
				$("#xg_form").slideUp();
				$("#btnAddAddress").html("新增收货地址");
			
			}
			isxgform=!isxgform;
        })
	$("#btnMoreAddress").click(function (){

        if(ismoreaddress){ 
			if(isxgform==false)
			{
				$("#xg_form").slideUp();
			$("#btnAddAddress").html("新增收货地址");
			isxgform=!isxgform;
			}
			$("#box_moreAddress").slideDown();
			$("#btnMoreAddress").html("收起收货地址");
        }else{
            $("#box_moreAddress").slideUp();
			$("#btnMoreAddress").html("更多收货地址");
			
        }
        ismoreaddress=!ismoreaddress;
    })
		   jQuery.validator.addMethod("mobile", function (value, element) {
           var mobile = /^1[3|4|5|7|8]\d{9}$/;
           return this.optional(element) || (mobile.test(value));
       }, "手机格式不对");
	   jQuery.validator.addMethod("postcode", function(value, element) {  
			var postcode = /^[0-9]{6}$/;
			return this.optional(element) || (postcode.test(value));
		}, "请正确填写您的邮政编码");
              $("#member_xgForm").validate({

            rules: {

                contact: {
                    required: true,
                    maxlength: 20
                },
                province: {
                required: true

                },
                city: {
                    required: true

                },
                address: {
                    required: true

                },
                mobile: {
                    required: true,
					 mobile: true
                }

            },
            messages: {

                contact: {

                    required: "请输入"
                },

                province: {

                    required: "请输入"
                },


                city: {
                    required: "请输入"
                },
                address: {
                    required: "请输入"
                },
                mobile: {

                    required: "请输入"
                } 
                

            },
            errorPlacement: function (error, element) {

                error.insertAfter(element);

            },
            success: function (label) {


                label.addClass("valid").text("");
            },
            submitHandler: function (form) {

                if(document.getElementById("checkboxInput").checked){
                    $("#currentStatus").attr("value","100003");
                }
                else{
                    $("#currentStatus").attr("value","100001");
                }
                $.ajax({
                    url:"/addressBook/create.json",
                    data:$("#member_xgForm").serialize(),
                    type:"post",
                    dataType:'json',
                    success:function(data){//ajax返回的数据
                         //location.href="/buy/settleUp.shtml";
						  alert("添加成功!");
						//window.location.reload();
                        if(data.message.operateCode != operateResultSuccess){

                            alert(data.message.message);
                        }
                        if (data.message.operateCode == operateResultSuccess) { //成功


                           
							
                           location.reload();
                                

                        }


                    },
					error:function(data){
						alert("系统繁忙，请稍后再试！")
					}
                });
            }

        });
		$(document).on("click",".confirm_btn",function(){
			  var giftCardNumber=$(this).prev().val();
          if(giftCardNumber.length==0){

           alert("请输入代金券编号");     
           return false;
          }
        $.ajax({
            type: "POST",
            url: '/buy/setCoupon.json',
            data: {
				couponCode:"MONEY_COUPON",
                couponSerialNumber: giftCardNumber.replace(/\ +/g,"").substr(0,8),
				couponPassword:giftCardNumber.replace(/\ +/g,"").substr(8, 16),
				orderId:$("#orderId").val()
            },
            dataType: 'json',
            success: function (data) {
               
                switch (data.message.operateCode) {

                    case 102008:
                       alert("充值成功，充值金额为"+data.coupon.giftMoney.giftMoney+"元");
						$(".box_coupon").empty();
						$(".balance").html(parseFloat($(".balance").html())+parseFloat(data.coupon.giftMoney.giftMoney));
                        break;

                    case operateResultFailed:

                        alert(data.message.message);

                        $("#giftCardNumber").val("");

                        break;

                }


            },
            error: function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		});
		$(document).on("keyup","#giftCardNumber",function(){
			var value=$(this).val().replace(/\s/g,'').replace(/(.{4})/g,"$1 ");
           $(this).val(value);
		})
		$("#addCoupon").click(function(){
			var $newvoucher="<div class='voucher martop15'><span class='daijinquan'>代金券验证码 :</span><input type='text' class='code' id='giftCardNumber' maxlength='19' placeholder='请输入16位数字或大写字母'><input type='submit' value='确认添加' class='confirm_btn'/></div>";
			$(".box_coupon").append($newvoucher);
		})
		$("#addChongzhi").click(function(){
			var $newvoucher="<div class='voucher martop15'><span class='daijinquan'>储值券验证码 :</span><input type='text' class='code' id='giftCardNumber' maxlength='19' placeholder='请输入16位数字或大写字母'><input type='submit' value='确认添加' class='confirm_btn'/></div>";
			$(".box_chongzhiquan").append($newvoucher);
		})
		
		$("#couponing").blur(function(){
			if(parseFloat($(this).val())>parseFloat($(".balance").html())){
				alert("代金券余额不足，请充值！");
				$(this).val("");
				return false;
			}
			$.ajax({
            type: "POST",
            url: '/buy/setCouponMoney.json',
            data: {
				money:$(this).val(),
				orderId:$("#orderId").val()
            },
            dataType: 'json',
            success: function (data) {
           
                switch (data.message.operateCode) {

                    case 102008:
                        alert(data.message.message);
						setTotalMoney();
                        break;

                    case operateResultFailed:

                        alert(data.message.message);
                        $(this).val("");
                        break;

                }


            },
            error: function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		})
	     $(".present").click(function(){
			var foods = document.getElementsByName("foods");
			var j=0;
			var addressBookId;
			var openIDStr="";
			for ( var i = 0; i < foods.length; i++) {

				var str="&tid="+foods[i].value;
				openIDStr+=str;
				
			}
			//openIDStr=openIDStr.substring(0,openIDStr.length-1);
			var iptRadio=$(".first_box input[type=radio]:checked").val();
			if(!iptRadio)
			{
				 alert("请添加或者选择地址");
		    }else{
				  addressBookId=$(".first_box input[type=radio]:checked").val();
				  location.href="/buy/settleUp.shtml?addressBookId="+addressBookId+openIDStr+"&payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ $("input[id='orderId']").val();
		}
    });
	
})
function setTotalMoney(){
	var totalNum=parseFloat($("#productMoney").html())+parseFloat($("#fee").html())-parseFloat($("#couponMoney").html());
	totalNum=totalNum.toFixed(2);
	$("#totalMoney").html(totalNum);
}
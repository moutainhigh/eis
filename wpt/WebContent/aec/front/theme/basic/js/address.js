
    $(document).ready(function () {
		var xg_form="<div class='message_box martop20'><div class='new_address1'><div class='addressInfo'><div><span class='icon_star'>*</span> 收&ensp;货&ensp;人 : <input type='text' id='contact'  name='contact'  placeholder='请输入收货人姓名'/></div><div><span class='icon_star'>*</span> 区域选择 :<span class='selects'><select class='prov' id='province' name='province'></select><select class='city marleft20' id='city' name='city' disabled='disabled'></select></span></div><div><span class='icon_star'>*</span> 详细地址 : <input type='text' placeholder=''  id='address'  name='address'/></div><div><span class='icon_star'>*</span> 联系方式 : <input type='text' placeholder='请输入手机号'  id='mobile'  name='mobile'/></div><div><span class='icon_star'></span></div><input type='hidden' value='' id='currentStatus'  name='currentStatus' class='box_right'/></div><div class='checkBox'><input type='checkbox' value='1' id='checkboxInput'  name='' checked /><label for='checkboxInput' id='checkBoxLabel'></label></div>设为默认地址</div></div><input type='submit' value='保存' id='adddistrict' name='adddistrict' class='btn_save'/>";
		
		$(".btn_newaddress").click(function(){
			 $("#member_xgForm2").css("display","none");
			 $(".default").css("display","none");
			var a=$("#member_xgForm").html().toString();
			if(a.length >20){
				return false;
			}

			$("#member_xgForm").append(xg_form);
					$(".selects").citySelect({
		          nodata:"none",
		           required:false
	        });

			
		})
		
		$("input[type=radio]").change(function(){
			$("input[type=radio]").each(function (i,d){
				d.checked=false;
			});
			$(this).attr("checked","checked");
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
                        if(data.message.operateCode != operateResultSuccess){

                            alert(data.message.message);
                        }
                        if (data.message.operateCode == operateResultSuccess) { //成功
                            alert(data.message.message);
                            location.href='/addressBook/index.shtml';
                                

                        }


                    }
                });
            }

        });

    });
    function del(id){ //id 需要传一个需要删除的ID号  在数据库不可重复的数据 一般都用id  ID=3
        $.ajax({
            url:"/addressBook/delete/"+id+".json",
            type:"post",
            dataType:'json',
            success:function(data){
            //ajax返回的数据

                if(data.message.operateCode != operateResultSuccess){

                    alert(data.message.message);
                }
                if (data.message.operateCode == operateResultSuccess) { //成功
                    alert("删除成功!");
                    window.location.reload();


                }



            }
        });
    }
	
 function moren(uid){
        $.ajax({
            url:"/addressBook/setDefault/"+uid+".json",
            type:"GET",
            dataType:'json',
            success:function(data){
                console.log(data);
                location.reload();

            }
        });
    }


    $(document).ready(function () {
		var xg_form="<div class='box_message'><ul class='box_message_list'><li><span class='text_left'>收&nbsp货&nbsp人：</span><input type='text'  id='contact' name='contact' /></li><li><span class='text_left'>联系方式：</span> <input type='text'  id='mobile' name='mobile' /></li><li><span class='text_left'>区域选择：</span><span style='width:70%;display:block;float: right;'  class='selects'><select id='s_province' name='province' class='prov'></select>   <select id='s_city' name='city' class='city' ></select> </span> </li> <li><span class='text_left'>详细地址: </span><input type='text'  id='address' name='address'/></li> </ul> <input type='hidden' value='' id='currentStatus' name='currentStatus' /></div><div class='wid90'><div class='box_left'><div class='checkBox' ><input type='checkbox' value='1' id='checkboxgroup' name='' class='check' /><label for='checkboxgroup'></label></div></div><a><span style='font-size:0.8em'>设为默认地址</span><span style='color: #717777;font-size:0.75em'>(每次购买时会默认使用该地址)</span></a> </div> <div class='wid90'><div class='box_center'><input type='submit' class='btn_login mt30' id='login' name='login' value='保存'/></div>		</div>";
		$(".addAddress").click(function(){
            $("#wrapper_1 div:gt(0)").css("display","none");
			var a=$(".abc").html().toString();
			if(a.length >20){
				return false;
			}			
			$(".abc").append(xg_form);
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
	   /*jQuery.validator.addMethod("postcode", function(value, element) {  
			var postcode = /^[0-9]{6}$/;
			return this.optional(element) || (postcode.test(value));
		}, "请正确填写您的邮政编码");*/
        $(".abc").validate({
            rules: {
				//收货人
                contact: {
                    required: true,
                    maxlength: 20
                },
				//联系方式
                mobile: {
                    required: true,
					 mobile: true
                },
				//省
                s_province: {
                required: true

                },
				//市
                s_city: {
                    required: true

                },
				//详细地址
                address: {
                    required: true

                }
				
                /*postcode: {
                    required: true,
                    postcode: true
                }*/

            },
            messages: {

                contact: {

                    required: "请输入"
                },
				mobile: {

                    required: "请输入"
                },
               s_province: {

                    required: "请输入"
                },
                s_city: {
                    required: "请输入"
                },
				 s_county: {
                    required: "请输入"
                },
                address: {
                    required: "请输入"
                },                
			/*
                postcode: {
                    required: "请输入",
                   maxlength: "邮政编码不能大于6个字符"
                } */

            },			
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            },
            success: function (label) {
                label.addClass("valid").text("");
            },
            submitHandler: function (form) {
                if(document.getElementById("checkboxgroup").checked){
                    $("#currentStatus").attr("value","100003");
                }
                else{
                    $("#currentStatus").attr("value","100001");
                }
                $.ajax({
                    url:"/addressBook/create.json",
                    data:$(".abc").serialize(),
                    type:"post",
                    dataType:'json',
                    success:function(data){//ajax返回的数据
                        if(data.message.operateCode != operateResultSuccess){
							alert(data.message.message);
                        }
                        if (data.message.operateCode == operateResultSuccess) { //成功
                            alert("添加成功!");							
                            location.href='/addressBook/index.shtml';
						}
					}					
                });
            }
        });     
    });
    function del(id){
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
                    location.href='/addressBook/index.shtml';
                }
            }
        });
    }

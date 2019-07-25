
    $(document).ready(function () {
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
                province: {
                    required: true

                },
                city: {
                    required: true

                },
                contact: {
                    required: true,
                    maxlength: 20
                },

                address: {
                    required: true

                },
                mobile: {
                    required: true,
					 mobile: true
                },
                postcode: {
                    required: true,
                   postcode: true
                }

            },
            messages: {
                province: {

                    required: "请输入"
                },


                city: {
                    required: "请输入"
                },
                contact: {

                    required: "请输入"
                },


                address: {
                    required: "请输入"
                },
                mobile: {

                    required: "请输入"
                },


                postcode: {
                    required: "请输入",
                   maxlength: "邮政编码不能大于6个字符"
                }

            },
            errorPlacement: function (error, element) {

                error.insertAfter(element);

            },
            success: function (label) {


                label.addClass("valid").text("")
            },
            submitHandler: function (form) {

                if(document.getElementById("checkboxInput").checked){
                    $("#currentStatus").attr("value","100003");
                }
                else{
                    $("#currentStatus").attr("value","100001");
                }
                $.ajax({
                    url:"/addressBook/update.json",
                    data:$("#member_xgForm").serialize(),
                    type:"post",
                    dataType:'json',
                    success:function(data){//ajax返回的数据

                        if(data.message.operateCode != operateResultSuccess){

                            alert(data.message.message);
                        }
                        if (data.message.operateCode == operateResultSuccess) { //成功


                            alert("更改成功!");
                            location.href='/addressBook/index.shtml';


                        }


                    }
                });
            }

        });


    });

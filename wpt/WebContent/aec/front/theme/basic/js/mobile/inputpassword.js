

$(document).ready(function () {


    $("#member_regForm").validate({

        rules: {


            userPassword: {
                required: true,
                minlength: 6,
                maxlength: 16
            }
        },
        messages: {


            userPassword: {
                required: "请输入密码",
                minlength: "确认密码不能小于6个字符",
                maxlength: "确认密码不能大于16个字符"
            }
        },
        errorPlacement: function (error, element) {

            error.insertAfter(element.parent("p"));


        },
        success: function (label) {


            label.addClass("valid").text("")
        },
        submitHandler: function (form) {
            $.ajax({
                type: "POST",
                url: '/weixinUser/createPassword.json',
                data: {
                    userPassword: $("#member_regForm input[id=userPassword]").val(),                   
				},
                async: false,
                dataType: 'json',
                success: function (msg) {
                    switch (msg.message.operateCode) {
                        case operateResultSuccess:
                            alert(msg.message.message);
							if(msg.redirectUrl){location.href=UrlDecode(msg.redirectUrl)+""}
							else{location.href="/"};
                            break;
                        default://输入密码
                            alert(msg.message.message);
                            break;
                    }
                },
                error: function (xml, err) {
                    alert("系统繁忙,请稍后再试");
                }
            });
        }
    });
});

function onchangePatchca(obj) {
    var xmlR = Math.random(10000);
    $(obj).attr("src", "/captcha?rd=" + xmlR);
}

var InterValObj; //timer变量，控制时间
var count = 120; //间隔函数，1秒执行
var curCount;//当前剩余秒数
var code = ""; //验证码
var codeLength = 6;//验证码长度

function sendMessage() {
    curCount = count;
    var phone = $("#username").val();//手机号码
    if (phone != "") {
        //产生验证码
        for (var i = 0; i < codeLength; i++) {
            code += parseInt(Math.random() * 9).toString();
        }
        //  alert(code);
        //设置button效果，开始计时
        $.ajax({
            type: "POST",
            url: "/user/findPassword/phone/submit/"+phone+".json",
            dataType: 'json',
            success: function (data) {
                if (data.message.operateCode != '102008') {					
                    malert(data.message.message+"",1000);
                    return false;
                }
                if (data.message.operateCode == '102008') { //成功
                    $("#btnSendCode").attr("disabled", "true");
                    $("#btnSendCode").val("请在" + curCount + "秒内输入");
                    InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
                    return true;
                }

            },
            error: function (data) {
                malert("系统繁忙,请稍后再试",1000);
                return false;
            }
        });
        //向后台发送处理数据
    } else {
        malert("手机号码不能为空！",1000);
    }
}
//timer处理函数
function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        $("#btnSendCode").removeAttr("disabled");//启用按钮
        $("#btnSendCode").val("重新发送验证码");
        code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
    }
    else {
        curCount--;
        $("#btnSendCode").val("请在" + curCount + "秒内输入");
    }
}

$(document).ready(function () {

    jQuery.validator.addMethod("mobile", function (value, element) {
        var mobile = /^1[3|4|5|7|8]\d{9}$/;
        return this.optional(element) || (mobile.test(value));
    }, "手机格式不对");
    $("#member_forgetForm").validate({

        rules: {

            username: {
                required: true,
                mobile: true,
                remote:!{


                    url: "/user/exist.json", //后台处理程序
                    type: "GET",  //数据发送方式
                    dataType: "json",       //接受数据格式
                    data:  {                     //要传递的数据
                        username: function() {
                            return $("#member_forgetForm input[id=username]").val();
                        }

                    }

                }
            },

            userPassword: {
                required: true,
                minlength: 6,
                maxlength: 16
            },
            userPassword2: {
                required: true,
                minlength: 6,
                maxlength: 16,
                equalTo:"#userPassword"
            },
            phoneBindSign: {
                required: true
            }
        },
        messages: {

            username: {

                required: "请输入用户名",
                remote:"没有这个用户!"
            },


            userPassword: {
                required: "请输入密码",
                minlength: "密码不能小于6个字符",
                maxlength: "密码不能大于16个字符"

            },
            userPassword2: {
                required: "请再输入一次密码",
                minlength: "确认密码不能小于6个字符",
                maxlength: "确认密码不能大于16个字符",
                equalTo: "两次输入密码不一致"
            },

            phoneBindSign: {
                required: "请输入手机验证码"
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
				url: '/user/update/forgetPassword/phone.json',
				data: {
					sign:$("#member_forgetForm input[id=phoneBindSign]").val(),
					newPassword1: $("#member_forgetForm input[id=userPassword]").val(),
					newPassword2:$("#member_forgetForm input[id=userPassword2]").val()
				},
                async: false,
                dataType: 'json',
                success: function (msg) {
                     if(msg.message.operateCode==102008){
                         malert(msg.message.message+"",1000);
                         location.href='/content/user/login.shtml';
                     }else{
						 malert(msg.message.message+"",1000);
                         location.href='/content/user/forgetpassword.shtml';
                     }
                },
                error: function (xml, err) {
                    malert("系统繁忙,请稍后再试",1000);
                }
            });
        }

    });


});


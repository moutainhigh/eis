var InterValObj; //timer变量，控制时间
var count = 120; //间隔函数，1秒执行
var curCount;//当前剩余秒数
var code = ""; //验证码
var codeLength = 6;//验证码长度

function sendMessage() {

    if($("#member_regForm").valid()){



    curCount = count;
    var phone = $("#username").val();//手机号码
    var eis_captcha = $("#member_regForm input[id=eis_captcha]").val().toLocaleLowerCase();//图形验证码

    if (phone != "") {

        if (eis_captcha == "") {

            alert("请先输入图形验证码");
            return false;
        }
        //产生验证码
        for (var i = 0; i < codeLength; i++) {
            code += parseInt(Math.random() * 9).toString();
        }
        //  alert(code);
        //设置button效果，开始计时



        $.ajax({
            type: "POST",
            url: '/sms/register/submit/' + phone + '.json', //目标地址

            data: {
                eis_captcha: $.md5(eis_captcha)
            },
            dataType: 'json',
            success: function (data) {


                if (data.message.operateCode != '102008') {

                    //alert(data.message.message);
					 var xmlR = Math.random(10000);
                     $("#captcha").attr("src", "/captcha?rd=" + xmlR);
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

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
        //向后台发送处理数据

    } else {
        alert("手机号码不能为空！");
    }

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
    $("#member_regForm").validate({

        rules: {

            username: {
                required: true,
                mobile: true,
                remote:{


                    url: "/user/exist.json", //后台处理程序
                    type: "POST",  //数据发送方式
                    dataType: "json",       //接受数据格式
                    data:  {                     //要传递的数据
                        username: function() {
                            return $("#member_regForm input[id=username]").val();
                        }

                    }

                }
            },

            userPassword: {
                required: true,
                minlength: 6,
                maxlength: 16
            },
            eis_captcha: {
                required: true,
                maxlength: 4
            }
        },
        messages: {

            username: {

                required: "请输入用户名",
                remote:"用户名已经存在!"
            },


            userPassword: {
                required: "请输入密码",
                minlength: "确认密码不能小于6个字符",
                maxlength: "确认密码不能大于16个字符"
            },
            eis_captcha: {
                required: "请输入图形验证码",

                maxlength: "图形验证码不能大于4个字符"
            }
        },
        errorPlacement: function (error, element) {

            error.insertAfter(element.parent("p"));


        },
        success: function (label) {


            label.addClass("valid").text("")
        },
        submitHandler: function (form) {

            if($("#member_regForm input[id=phoneBindSign]").val()==""){

                alert("请输入短信验证码");
                return false;
            }

            if(!$("#checkboxgroup").is(":checked")){

                alert("请认真阅读以先用户协议");
                return false;
            }
            $.ajax({
                type: "POST",
                url: '/usr.json',
                data: {
                    username: $("#member_regForm input[id=username]").val(),
                    userPassword: $("#member_regForm input[id=userPassword]").val(),
                    phoneBindSign:$("#member_regForm input[id=phoneBindSign]").val()

                },
                async: false,
                dataType: 'json',
                success: function (msg) {
                   // alert(msg.message.operateCode);

                    switch (msg.message.operateCode) {


                        case operateResultSuccess:


                            alert("注册成功");

                            location.href='/content/user/pcenter.shtml';
                            break

                        case operateResultFailed:


                            alert(msg.message.message);

                            window.location.reload();
                            break

                        case captchaTimeOut:
                            alert(msg.message.message);
                            break
                        case errorVerifyError:
                            alert(msg.message.message);
                            break

                        case errorObjectAlreadyExist:
                            alert(msg.message.message);
                            break

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

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

                    alert(data.message.message);
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
                mobile: true
            },


            eis_captcha: {
                required: true,
                maxlength: 4
            }
        },
        messages: {

            username: {

                required: "请输入用户名"
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


            $.ajax({
                type: "POST",
                url: '/usr/completeinfo.json',
                data: {
                    username: $("#member_regForm input[id=username]").val(),
                    eis_captcha: $("#member_regForm input[id=eis_captcha]").val(),
                    phoneBindSign:$("#member_regForm input[id=phoneBindSign]").val(),
                    openID: $("#member_regForm input[id=openID]").val(),
                    nickName:$("#member_regForm input[id=nickName]").val()

                },
                async: false,
                dataType: 'json',
                success: function (msg) {


                    switch (msg.message.operateCode) {


                        case operateResultSuccess:


                            alert(msg.message.message);

                            location.href='/content/user/pcenter.shtml';


                            break

                        case operateJump:
//输入密码
         location.href='/usr/inputpassword?openID='+$("#member_regForm input[id=openID]").val()+'&nickName='+$("#member_regForm input[id=nickName]").val()+'&username='+$("#member_regForm input[id=username]").val();
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

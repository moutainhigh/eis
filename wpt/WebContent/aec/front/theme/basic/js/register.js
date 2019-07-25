var InterValObj; //timer变量，控制时间
var count = 120; //间隔函数，1秒执行
var curCount;//当前剩余秒数
var code = ""; //验证码
var codeLength = 4;//验证码长度



function onchangePatchca(obj) {
    var xmlR = Math.random(10000);
    $(obj).attr("src", "/captcha?rd=" + xmlR);
}
//timer处理函数


function sendMessage(){

    if($("#member_regForm").validate()){  

    curCount = count;
    var phone = $("#member_regForm input[id=username]").val()//手机号码
    var r= $("#member_regForm input[id=eis_captcha]").val();//图形验证码
    if (phone != "") {
        if (eis_captcha == "") {
            alert("请先输入图形验证码");
            return false;
        }
        //产生验证码
        for (var i = 0; i < codeLength; i++) {
            code += parseInt(Math.random() * 9).toString();
        }
       //alert(code);
        //设置button效果，开始计时
        $.ajax({
            type: "POST",
            url: '/user/registerByPhone/' + phone + '.json', //目标地址
            data: {
                eis_captcha: r
            },
            dataType: 'json',
            success: function (e) {
                if ("102008" != e.message.operateCode) {
                        alert(e.message.message);
                        var r = Math.random(1000);
                        return $("#captcha").attr("src", "/captcha?rd=" + r),
                        !1
                    }
                    return "102008" == e.message.operateCode ? ($("#member_regForm #btnSendCode").attr("disabled", "true"), $("member_regForm #btnSendCode").val(curCount + "秒内输入"), InterValObj = window.setInterval(SetRemainTime, 1e3), !0) : void 0
            },
            error: function () {

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






function SetRemainTime() {
    0 == curCount ? (window.clearInterval(InterValObj), $("#member_regForm #btnSendCode").removeAttr("disabled"), $("#member_regForm #btnSendCode").val("重发验证码"), code = "") : (curCount--, $("#member_regForm #btnSendCode").val(curCount + "秒内输入"))
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
                minlength:4,
                maxlength: 4
            },
			smsRegisterSign1: {
                required: true,
				maxlength:6,
				minlength:6
            }
        },
        messages: {
            username: {
                required: "请输入手机号",
                remote:"用户名已经存在!"
            },
            userPassword: {
                required: "请输入密码",
                minlength: "密码不能小于6个字符",
                maxlength: "密码不能大于16个字符"
            },

            eis_captcha: {
                required: "请输入图形验证码",
               minlength: "图形验证码不能小于4个字符",
                maxlength: "图形验证码不能大于4个字符"
            },
			       
			smsRegisterSign1: {
                required: "请输入验证码",
				minlength: "密码不能小于6个字符",
                maxlength: "密码不能大于6个字符"
            }
			
        },

        errorPlacement: function (error, element) {
            error.insertAfter(element.parent().parent(".line_box"));
        },
        success: function (label) {
            label.text();
        },
        submitHandler: function (form) {   
            if(!$("#checkboxgroup").is(":checked")){
                alert("请认真阅读以先用户协议");
                return false;
            }
            $.ajax({
                type: "POST",
                url: '/user.json',
                data: {
                    username: $("#member_regForm input[id=username]").val(),
                    userPassword: $("#member_regForm input[id=userPassword]").val(),
                    eis_captcha:$("#member_regForm input[id=eis_captcha]").val(),
                    smsRegisterSign:$("#member_regForm input[id=smsRegisterSign1]").val()
                },
                async: false,
                dataType: 'json',
                success: function (msg) {
           
                    switch (msg.message.operateCode) {
                        case operateResultSuccess:
                            alert("注册成功");
                            if(msg.redirectUrl){
							 location.href=UrlDecode(msg.redirectUrl)+"";
							 return false;
						    }
							location.href="/content/user/pcenter.shtml";
                            break;

                        case operateResultFailed:
                            alert(msg.message.message);
                            window.location.reload();
                            break;
                        case captchaTimeOut:
                            alert(msg.message.message);
                            break;
                        case errorVerifyError:
                            alert(msg.message.message);
                            break;
                        case errorObjectAlreadyExist:
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
function UrlDecode(str){ 
var ret=""; 
for(var i=0;i<str.length;i++){ 
var chr = str.charAt(i); 
if(chr == "+"){ 
ret+=" "; 
}else if(chr=="%"){ 
var asc = str.substring(i+1,i+3); 
if(parseInt("0x"+asc)>0x7f){ 
ret+=asc2str(parseInt("0x"+asc+str.substring(i+4,i+6))); 
i+=5; 
}else{ 
ret+=asc2str(parseInt("0x"+asc)); 
i+=2; 
} 
}else{ 
ret+= chr; 
} 
} 
return ret; 
} 
function str2asc(str){ 
return str.charCodeAt(0).toString(16); 
} 
function asc2str(str){ 
return String.fromCharCode(str); 
} 

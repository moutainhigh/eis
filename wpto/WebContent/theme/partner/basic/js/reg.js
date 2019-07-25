var InterValObj; //timer变量，控制时间
var count = 60; //间隔函数，1秒执行
var curCount;//当前剩余秒数
var code = ""; //验证码
var codeLength = 6;//验证码长度
function onchangePatchca() {
    var xmlR = Math.random(10000);
    $(".tx-code-img").attr("src", "/captcha?rd=" + xmlR);
}
function resetForm(){
	$(".input").val('');
}
function changeTypeOfPassword(that){
	if($(that).siblings(".input").attr("type")==="password"){
		var val=$(that).siblings(".input[type='password']").val();
		$(that).parent().append('<input class="input" name="userPassword" placeholder="请设置密码" id="userPassword" type="text" '+'value='+val+'>');
		$(that).siblings(".input[type='password']").remove();
		$(that).attr("src","/theme/basic/images/input_eye_active.png");
	}else{
		var val=$(that).siblings(".input").val();
		$(that).parent().append('<input class="input" name="userPassword" placeholder="请设置密码" id="userPassword" type="password" '+'value='+val+'>');
		$(that).siblings(".input[type='text']").remove();
		$(that).attr("src","/theme/basic/images/input_eye.png");
	}
}
function sendMessage() {
    if($("#reg_form").valid()){
		curCount = count;
		var phone = $("#username").val();//手机号码
		var eis_captcha = $("#reg_form input[id=eis_captcha]").val().toLocaleLowerCase();//图形验证码
		if (phone != "") {
			if (eis_captcha == "") {
				noticeBox("请先输入图形验证码");
				return false;
			}
			//设置button效果，开始计时
			$.ajax({
				type : "POST",
						url : "/user/registerByPhone.json",
						data : {
							phone : $('#username').val(),
							eis_captcha : $('#eis_captcha').val(),
						},
						dataType : "json",
						success : function(data) {
							if(data.message.operateCode==102008){
								successBox(data.message.message);
								$("#btnSendCode").attr("disabled", "true");
								$("#btnSendCode").val("请在" + curCount + "秒内输入");
								InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
							}else if(data.message.operateCode==500023){
								noticeBox("验证码错误["+data.message.operateCode+"]");
							}else if(data.message.operateCode==500018){
								noticeBox("用户名已存在["+data.message.operateCode+"]");
							}
						},
						error:function(){
							noticeBox("对不起，网络繁忙，请稍后再试！");
						}
			});
		} else {
			noticeBox("手机号码不能为空！");
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
    }, "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>手机格式不对");		
	$("input").on("input",function(){  
		$(this).removeData("previousValue").valid(); 
	}); 
    $("#reg_form").validate({
        rules: {
            username: {
                required: true,
                mobile: true,
				minlength: 11,
                remote:{
                    url: "/user/exist.json", //后台处理程序
                    type: "POST",  //数据发送方式
                    dataType: "json",       //接受数据格式
                    data:  {                     //要传递的数据
                        username: function() {
                            return $("#reg_form input[id=username]").val();
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
                required: true
            }
        },
        messages: {
            username: {
                required: "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>请输入用户名",
				minlength: "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>确认密码不能小于11个字符",
                remote:"<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>用户名已经存在!"
            },
            userPassword: {
                required: "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>请输入密码",
                minlength: "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>确认密码不能小于6个字符",
                maxlength: "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>确认密码不能大于16个字符"
            },
            eis_captcha: {
                required: "<img src='/theme/basic/images/wrong_icon_red.png' class='icon'/>请输入图形验证码",
            }
        },
        errorPlacement: function (error, element) {
            error.insertAfter($(element).parent().find(".notice-line").children(".icon"));
        },
        success: function (label) {
            label.addClass("valid").text("")
        },
        submitHandler: function (form) {
            if($("#reg_form input[id=phoneBindSign]").val()==""){
                noticeBox("请输入短信验证码");
                return false;
            }
            $.ajax({
                type: "POST",
                url: '/user.json',
                data: {
                    username: $("#reg_form input[id=username]").val(),
                    userPassword: encoded($("#reg_form input[id=userPassword]").val()),
                    smsRegisterSign:$("#reg_form input[id=phoneBindSign]").val()
                },
                async: false,
                dataType: 'json',
                success: function (data) {
                    if(data.message.operateCode==102008){
						successBox(data.message.message);
						location.href="/guide.shtml";
					}else{
						noticeBox(data.message.message);
					}
                },
                error: function (xml, err) {
                    noticeBox("系统繁忙,请稍后再试");
                }
            });
        }
    });
});

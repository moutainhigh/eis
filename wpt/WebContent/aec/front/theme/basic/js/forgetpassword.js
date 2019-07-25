
var InterValObj1; //timer变量，控制时间
var count1 = 120; //间隔函数，1秒执行
var curCount1;//当前剩余秒数
var code1 = ""; //验证码
var codeLength1 = 6;//验证码长度

// function SetRemainTime1() {
//     0 == curCount1 ? (window.clearInterval(InterValObj1), $("#member_forgetForm #btnSendCode").removeAttr("disabled"), $("#member_forgetForm #btnSendCode").val("重发验证码"), code = "") : (curCount--, $("member_forgetForm #btnSendCode").val(curCount + "秒内输入"))
// }

// function sendMessage2() {
//     curCount1 = count1;
//     var phone1 = $("#member_forgetForm #username").val();//手机号码

//     if (phone1 != "") {
//         //产生验证码
//         for (var i = 0; i < codeLength1; i++) {
//             code += parseInt(Math.random() * 9).toString();
//         }
//          //alert(code);
//         //设置button效果，开始计时


//         $.ajax({
//             type: "POST",
//             url: '/user/findPassword/phone/submit/' + phone1 + '.json',//目标地址

//             dataType: 'json',
//             success: function (data) {
                   

//                 if (data.message.operateCode != '102008') {

//                     alert(data.message.message);
//                     return false;

//                 }
//                 if (data.message.operateCode == '102008') { //成功
//                     $("#member_forgetForm #btnSendCode").attr("disabled", "true");
//                     $("#member_forgetForm #btnSendCode").val(curCount1 + "秒内输入");
//                     InterValObj1 = window.setInterval(SetRemainTime1, 1000); //启动计时器，1秒执行一次
//                     return true;

//                 }

//             },
//             error: function () {

//                 alert("系统繁忙,请稍后再试");
//                 return false;
//             }
//         });
//         //向后台发送处理数据

//     } else {
//         alert("手机号码不能为空！");
//     }
// }
//timer处理函数


$(function () {
    jQuery.validator.addMethod("mobile", function (value, element) {

        var mobile = /^1[3|4|5|7|8]\d{9}$/;
        return this.optional(element) || (mobile.test(value));
    }, "手机格式不对");
    $("#member_forgetForm").validate({
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
                            return $("#member_forgetForm input[id=username]").val();
                        }
                    },
					dataFilter: function (data,type) {
                       if (data == "true") {
						     return false;
					   }   
                        else {
							 return true; 
						}
                           
                        }
				    }
                
            },

            userPassword1: {
                required: true,
                minlength: 6,
                maxlength: 16
            },
            userPassword2: {
                required: true,
                minlength: 6,
                maxlength: 16,
                equalTo:"#member_forgetForm #userPassword1"
            },
            smsRegisterSign: {
                required: true
            }
        },
        messages: {
            username: {
                required: "请输入用户名",
                remote:"没有这个用户!"
            },


            userPassword1: {
                required: "请输入密码",
                minlength: "密码不能小于6个字符",
                maxlength: "密码不能大于16个字符"

            },
            userPassword2: {
                required:"请再输入一次密码",
                minlength:"确认密码不能小于6个字符",
                maxlength:"确认密码不能大于16个字符",
                equalTo:"两次输入密码不一致"
            },

            smsRegisterSign:{
            required: "请输入手机验证码"
            }
        },
		success: function (label) {
            label.text();
        },
        errorPlacement: function (error, element) {
            error.insertAfter(element.parent().parent(".line_box"));
        },
         submitHandler:function (form) {
            $.ajax({
                type: "POST",
                url: '/user/update/forgetPassword/phone.json',
                data: {
					
                    sign:$("#member_forgetForm input[id=smsRegisterSign]").val(),
                    newPassword1:$("#member_forgetForm input[id=userPassword1]").val(),
                    newPassword2:$("#member_forgetForm input[id=userPassword2]").val()
                },
                async: false,
                dataType: 'json',
                success: function (msg) {
                     if(msg.message.operateCode==operateResultSuccess){
                         alert(msg.message.message);
                         location.href='/content/user/login.shtml';
                     }else{
                         alert(msg.message.message);
                         //location.href='/content/user/login.shtml';
                     }  
                },
                error: function (xml, err) {

                    alert("系统繁忙,请稍后再试");

                }
            });
        }
        
    });


});

function sendMessage3(){
	
    curCount1 = count1;
    var phone = $("#member_forgetForm input[id=username]").val()//手机号码
    if (phone != "") {
        //产生验证码
        for (var i = 0; i < codeLength1; i++) {
            code1 += parseInt(Math.random() * 9).toString();
        
        }
         //alert(code1);
        //设置button效果，开始计时

        $.ajax({
            type: "POST",
            url: '/user/findPassword/phone/submit/' + phone + '.json', //目标地址
           
            dataType: 'json',
            success: function (e) {
              
                    return "102008" == e.message.operateCode ? ($("#member_forgetForm #btnSendCode").attr("disabled", "true"), $("member_forgetForm #btnSendCode").val(curCount1 + "秒内输入"), InterValObj1 = window.setInterval(SetRemainTime1, 1e3), !0) : void 0
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
function SetRemainTime1() {

    0 == curCount1 ? (window.clearInterval(InterValObj1), $("#member_forgetForm #btnSendCode").removeAttr("disabled"), $("#member_forgetForm #btnSendCode").val("重发验证码"), code1 = "") : (curCount1--, $("#member_forgetForm #btnSendCode").val(curCount1 + "秒内输入"))
}

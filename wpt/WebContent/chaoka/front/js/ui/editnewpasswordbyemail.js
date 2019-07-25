//验证找回密码输出方式
function request(paras) {
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {}
    for (i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof(returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}

$(document).ready(function () {

    $('#findnewpForm').submit(function () {

        var obj = $("#findnewpForm input[id=newPassword]");
        var obj2 = $("#findnewpForm input[id=newPassword2]");
        $newPassword = $("#findnewpForm input[id=newPassword]").val();
        $newPassword2 = $("#findnewpForm input[id=newPassword2]").val();
        $uuid = $("#findnewpForm input[id=uuid]").val();
        $sign = $("#findnewpForm input[id=sign]").val();
        if ($newPassword == "") {
            $(obj).parent().next(".regok").text("请输入您的新密码");
            $(obj).parent().next(".regok").css('color', '#ff0000');
            $(obj).parent().next(".regok").css({
                'background':'url(/style/cuo.png)',
                'background-repeat':'no-repeat',
                'padding-left':'20px',
                'line-height':'30px',
                'height':'30px',
                'background-position':'0% 50%'
            });
            return false;
        }
        if ($newPassword2 == "") {
            $(obj2).parent().next(".regok").text("请再输入一次新密码");
            $(obj2).parent().next(".regok").css('color', '#ff0000');
            $(obj2).parent().next(".regok").css({
                'background':'url(/style/cuo.png)',
                'background-repeat':'no-repeat',
                'padding-left':'20px',
                'line-height':'30px',
                'height':'30px',
                'background-position':'0% 50%'
            });
            return false;
        }
        if ($newPassword2 != $newPassword) {
            $(obj2).parent().next(".regok").text("两次密码输入不一致");
            $(obj2).parent().next(".regok").css('color', '#ff0000');
            $(obj2).parent().next(".regok").css({
                'background':'url(/style/cuo.png)',
                'background-repeat':'no-repeat',
                'padding-left':'20px',
                'line-height':'30px',
                'height':'30px',
                'background-position':'0% 50%'
            });
            return false;
        }
        $.ajax({
            type:"POST",
            data:{
                uuid:$uuid,
                sign:$sign,
                newPassword:$newPassword,
                newPassword2:$newPassword2

            },
            url:userHost + '/user/update/forgetPassword/email/' + $uuid + '/' + $sign + '.json',
            dataType:'json',
            success:function (data) {

                if (data.message.operateCode == operateResultSuccess) {
                    jAlert(data.message.message, '提示信息', function (r) {

                        location.href = '/';

                    });

                } else {

                    jAlert(data.message.message, '提示信息', function (r) {

                        location.reload();

                    });

                }
            },
                error : function (data) {

                    jAlert("系统繁忙,请稍后再试", '提示信息');
                    return false;
                }
            });

    });

    $('#rmcp').hide();
    //读取绑定邮箱结果
    $.ajax({
        type:"POST",
        url:userHost + '/user/findPassword/email/confirm/' + request("uuid") + '/' + request("sign") + '.json',

        dataType:'json',

        success:function (data) {

            if (data.message.operateCode == operateResultSuccess) {

                $('.forgetbody').show();
                $('#findnewpForm #uuid').attr("value", request("uuid"));

                $('#findnewpForm #sign').attr("value", request("sign"));

            } else {

                $('#jieguop').show();
                $('#jieguop').html(data.message.message);

            }
        },
        error:function (data) {

            jAlert("系统繁忙,请稍后再试", '提示信息');
            return false;
        }
    });

});

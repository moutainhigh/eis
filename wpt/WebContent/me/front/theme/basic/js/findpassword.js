        // 找回密码提交按钮
        var InterValObj; //timer变量，控制时间
        var count = 120; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        var code = ""; //验证码
        var codeLength = 4;//验证码长度


        function SetRemainTime() {
            0 == curCount ? (window.clearInterval(InterValObj), $("#btnSendCode").removeAttr("disabled"), $("#btnSendCode").val("重发验证码"), code = "") : (curCount--, $("#btnSendCode").val(curCount + "秒内输入"))
        }

        function findpassword(){
            var username =$('#usernames').val();
            var phone = $("#wpuf-user_email").val();
            var sign = $('#smsRegisterSign').val();
            var newPassword1 = $('#userPassword').val();
            var pwda = $.base64.encode(newPassword1);
            var newPassword2 = $('#userPassword2').val();
            var pwdb = $.base64.encode(newPassword2);
            if (!(/^1[3|4|5|7|8]\d{9}$/.test(phone))|| sign.length == 0 ||  newPassword1.length < 7 || pwda != pwdb || username.length == 0 || username.length > 6) {
                alert('输入有误，请重新输入！');
            } else {
                $.ajax({
                    type:"POST",
                    url:'/user/update/forgetPassword/phone.json',
                    data:{ 
                        userName:username,
                        phone:phone,
                        sign:sign,
                        newPassword1:pwda,
                        newPassword2:pwdb
                    },           
                    async:false,
                    success: function(data) {
                        if(data.message.operateCode == 102008){
                            alert(data.message.message);
                            if (data.redirectUrl == '' || data.redirectUrl == undefined) {
                                window.location.href = "/";
                            } else {
                                window.location.href = data.redirectUrl;
                            }
                        }else{
                            alert(data.message.message);
                            return false;
                        }
                        
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    }
                })
            }
        }
       //  找回密码发送验证码
        function findpasswordMessage(){
                curCount = count;
                var phone = $("#wpuf-user_email").val();//手机号
                var username = $('#usernames').val();//用户名
                if (phone != "" && /^1[3|4|5|7|8]\d{9}$/.test(phone) && username.length != 0 && username.length <= 6) {
                    //产生验证码
                    // for (var i = 0; i < codeLength; i++) {
                    //     code += parseInt(Math.random() * 9).toString();
                    // }
                    //设置button效果，开始计时
                    $.ajax({
                        type: "POST",
                        url: '/user/findPassword/phone/submit/'+phone+'.json', //目标地址
                        data: {
                            phone: phone,
                            userName:username
                        },
                        dataType: 'json',
                        success: function (data) {
                            switch (data.message.operateCode) {
                                case 500031:
                                    alert(data.message.message);
                                    location.href="/user/login.shtml";
                                    break;
                                case 102008:
                                    alert('发送成功！');
                                    return "102008" == data.message.operateCode ? ($("#btnSendCode").attr("disabled", "true"), $("#btnSendCode").val(curCount + "秒内输入"), InterValObj = window.setInterval(SetRemainTime, 1e3), !0) : void 0
                                    break;
                                case 500058:
                                    alert(data.message.message);
                                    break;
                            }	
                        },
                        error: function () {
                            alert("系统繁忙,请稍后再试");
                            return false;
                        }
                    });
                } else {
                    alert("电子邮箱地址或用户名输入不正确！");
                }
        } 


                // 左上角登陆按钮
                function login() {
                    if ($('#rememberme')[0].checked === false || $('#user_login')[0].value.indexOf(' ') > -1 || $('#user_login')[0].value.length > 6 || $('#pass1')[0].value.length >= 0 && $('#pass1')[0].value.length < 7) {
                        alert('输入有误，请重新输入！');
                    } else {

                    var name = $('#user_login').val();
                    var pwds = $('#pass1').val();
                    var pwd = $.base64.encode(pwds);
                        $.ajax({
                            type: "post",
                            url: '/user/login.json',
                            data: {
                                username:name,
                                userPassword:pwd    
                            },
                            dataType: 'json',
                            // async: false,
                            success: function(msg) {
                                switch (msg.message.operateCode) {
                                    case 102008:
                                        alert("登录成功");
                                        if(msg.redirectUrl){
                                            location.href=msg.redirectUrl;
                                            return false;
                                        }
                                            location.href="/";
                                            break;
                                    // case operateResultFailed:
                                    //     alert(msg.message.message);
                                    //     window.location.reload();
                                    //     break;
                                    // case captchaTimeOut:
                                    //     alert(msg.message.message);
                                    //     break;
                                    // case errorVerifyError:
                                    //     alert(msg.message.message);
                                    //     break;
                                    case 500005:
                                        alert(msg.message.message);
                                        break;
                                }
                            },
                            error: function(XMLResponse) {
                                alert("操作失败:" + XMLResponse.responseText);
                            },
                        }, 'json');
                    }
                }


        $(function() {
                // 导航菜单点击隐藏显示
                $('.visible-phone').click(function(){
                    $('.visible-phone').siblings('ul').toggle();
                })

                $('.submenu').each(function(){
                    $(this).click(function(){
                        $(this).find('ul').toggle();
                    })
                })
                // pc端加载完管理后的
                var toggles = true;
                $('#menu').mouseenter(function() {
                    // if(toggles == true && $(this).attr('class') == 'dropdown'){
                        if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  
                            $('.dropdown-menu').css({'display':'block','height':'427px'});   
                        } else if (/(Android)/i.test(navigator.userAgent)) {  
                            $('.dropdown-menu').css({'display':'block','height':'427px'});    
                        } else {  
                            $('.dropdown-menu').css({'display':'block','height':'60px'});
                        }; 
                    //     toggles = false;
                    //     $(this).removeClass('dropdown');
                    // }else{
                    //     // $('.dropdown-menu').css('display','none');
                    //     // toggles = true;
                    //     // $(this).addClass('dropdown');
                    // }
                })

                $('#menu').mouseleave(function() {
                    // if(toggles == true && $(this).attr('class') == 'dropdown'){

                    // }else{
                        $('.dropdown-menu').css({'display':'none'});
                    //     toggles = true;
                    //     $(this).addClass('dropdown');
                    // }
                })


                // 修改资料及注册页面左上角登录
                if(jQuery.cookie('eis_username') == null){
                    $('#user-name').css('display','none');
                    $('#user-img').css('display','none');
                    $('#user-email').css('display','none');
                    // alert('请登陆！');
                }else{
                    var userImg = jQuery.cookie('eis_userheadpic');
                    var name = jQuery.cookie('eis_username');
                    $('#user-name').text('你好,'+name);                    
                    if(userImg == 0){
                        $('#user-img').append('<img src="../../../theme/basic/images/touxiang.png" />');
                    }else{    
                        $('#user-img').append('<img style="width:20px;height:20px;border-radius:10px;" src="/file/client/'+userImg+'" />');
                    }
                    $('#user-email a').append('<img src="../../../theme/basic/images/user-email.png" />');
                    // <li><a href="http://me.mo4u.cn/content/user/20170515102939.shtml"><img src="../../../theme/basic/images/moneygl.png" style="margin-right:15px;position:relative;left:2px;" />账户余额</a></li>
                    $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">管理</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li></ul>');
                    // <a title=""  class="dropdown-toggle"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">管理</span><b class="caret"></b></a><div style="height:22px;background:rgba(0,0,0,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="width:100px;"><li><a href="/content/friend/upload/index.shtml"><img src="../../../theme/basic/images/productgl.png" style="margin-right:13px;width:12px;position:relative;left:1px;"/>我的作品</a></li><li><a href="/content/user/20170515123310.shtml"><img src="../../../theme/basic/images/diamgl.png" style="margin-right:13px;position:relative;left:1px;"/>我的收藏</a></li><li><a href="/content/user/20170515125420.shtml?vPro=purchaseList"><img src="../../../theme/basic/images/shopgl.png" style="margin-right:10px;"/>已购教学</a></li><li><a href="/content/user/20170515130841.shtml?vPro=purchaseList"><img src="../../../theme/basic/images/giftgl.png" style="margin-right:14px;position:relative;left:2px;"/>VIP订购</a></li><li><a href="/content/user/20170515133906.shtml"><img src="../../../theme/basic/images/messagegl.png" style="margin-right:16px;position:relative;left:3px;"/>站内信息</a></li><li><a href="/content/user/20170515141440.shtml"><img src="../../../theme/basic/images/setgl.png" style="margin-right:12px;position:relative;left:2px;width:13px;"/>修改资料</a></li></ul>
                    $("#user-register").empty().html('<a onclick="quit()"><img src="../../../theme/basic/images/login.png" alt="" class="register" style="margin-bottom: 2px;"></i><span class="text">登出</span></a>');

                    $('#user-name').css('display','block');
                    $('#user-img').css('display','block');
                    $('#user-email').css('display','block');
                }
            })
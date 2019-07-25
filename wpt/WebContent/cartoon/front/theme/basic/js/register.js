 //   qq第三方接入
  function qqLogin(){
            // QC.Login({
            //     btnId:"qq"    //插入按钮的节点id

            // });
            QC.Login.showPopup();

            QC.api("get_user_info", {}) //get_user_info是API参数

            //指定接口访问成功的接收函数，s为成功返回Response对象

                .success(function (s) {

                    //成功回调，通过s.data获取OpenAPI的返回数据

                    nick = s.data.nickname; //获得昵称

                    headurl = s.data.figureurl_qq_1; //获得头像

                    if (QC.Login.check()) {//判断是否登录

                        QC.Login.getMe(function (openId, accessToken) { //这里可以得到openId和accessToken

                            //下面可以调用自己的保存方法

                            $.post('/你的后台处理Action',{name:nick,openid:openId,otype:1,token:accessToken},function(data,status){
                                    if(status=="success"){
                                        alert(nick+"恭喜你,登录成功!");
                                        location.href = "/";
                                    }else{
                                        alert("获取用户信息成功！登录失败！");
                                        location.href = "/";
                                    }
                                })
                        });

                    }

                })

            //指定接口访问失败的接收函数，f为失败返回Response对象

                .error(function (f) {

                    //失败回调

                    alert("获取用户信息失败！");

                });

            ////指定接口完成请求后的接收函数，c为完成请求返回Response对象

            //.complete(function (c) {

            //    //完成请求回调

            //    alert("获取用户信息完成！");

            //});
      }

        // 微信第三方接入
        function wxLogin(){
             window.location.href='https://open.weixin.qq.com/connect/qrconnect?' +
                'appid=1106128779&redirect_uri=http%3a%2f%2fwww.me.mo4u.cn%2f'+window.location.pathname.substr(1)+'&response_type=code&scope=snsapi_login&state=3d6be0a4035d839573b04816624a415e#wechat_redirect';
        }

        //注册点击获取验证码
        var InterValObj; //timer变量，控制时间
        var count = 120; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        var code = ""; //验证码
        var codeLength = 4;//验证码长度

        function SetRemainTime() {
            0 == curCount ? (window.clearInterval(InterValObj), $("#btnSendCode").removeAttr("disabled"), $("#btnSendCode").val("重发验证码"), code = "") : (curCount--, $("#btnSendCode").val(curCount + "秒内输入"))
        }

        function sendMessage(){  
                curCount = count;
                var phone = $("#wpuf-user_email").val();//手机号码
                if (phone != "" && /^1[3|4|5|7|8]\d{9}$/.test(phone)) {
                    //产生验证码
                    for (var i = 0; i < codeLength; i++) {
                        code += parseInt(Math.random() * 9).toString();
                    }
                    //设置button效果，开始计时
                    $.ajax({
                        type: "POST",
                        url: '/user/get/'+phone+'.json', //目标地址
                        data: {
                            phone: phone
                        },
                        dataType: 'json',
                        success: function (e) {
                                return "102008" == e.message.operateCode ? ($("#btnSendCode").attr("disabled", "true"), $("#btnSendCode").val(curCount + "秒内输入"), InterValObj = window.setInterval(SetRemainTime, 1e3), !0) : void 0
                        },
                        error: function () {
                            alert("系统繁忙,请稍后再试");
                            return false;
                        }
                    });
                } else {
                    alert("手机号码输入不正确！");
                }
        }   
        
        // 注册按钮
        function send() {
            if ($('#checkout')[0].checked === false || $('#userName').val().length < 2 ||  $('#userName').val().length > 20 || $('#user_text').text().indexOf('输入') > -1  || $('#email_text').text().indexOf('输入') > -1 || $('#wpuf-user_email').val().length === 0 || $('#password_text').text().indexOf('输入') > -1 || $('#userPassword').val().length === 0 || $('#userPassword').val().length < 6 || $('#pass-strength-result').text().indexOf('不匹配') > -1 || $('#wz_text').text().indexOf('有误') > -1 ||  $('#userPassword').val().length != $('#userPassword2').val().length) {
                alert('输入有误，请重新输入！');
            } else {
                var name = $('#userName').val();
                var pwds = $("#userPassword").val();
                var pwd = $.base64.encode(pwds);
                var eml = $('#wpuf-user_email').val();
                var smsRegisterSign = $('#smsRegisterSign').val();
                $.ajax({
                    type: "post",
                    url: '/user.json',
                    data: {
                        username:name,
                        userPassword:pwd,
                        phone:eml,
                        smsRegisterSign:smsRegisterSign
                    },
                    dataType: 'json',
                    async: false,
                    success: function(data) {
                        if(data.message.operateCode == 102008){
                            alert('注册成功！');
                            location.reload();
                        }else{
                            alert(data.message.message)
                            location.reload();
                        }
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    },
                }, 'json');
            }
        }
        
        //  回车键点击
        function keyLoginone(){
            if(window.event.keyCode == 13){
                    oneLogin();
                }
            }

        // 登录页面的登录按钮
        function oneLogin() {
            if ($('#wpuf-rememberme')[0].checked === false ||$('#userName').val().indexOf(' ') > -1 || $('#userName').val().length < 2 || $('#userName').val().length > 20 ||  $('#userPassword').val().length === 0 ||  $('#userPassword').val().length < 6 ) {
                    alert('输入有误，请重新输入！');
                } else {
                    var pwds = $("#userPassword").val();
                    var pwd = $.base64.encode(pwds);
                    $.ajax({
                        type: "post",
                        url: '/user/login.json',
                        data: { 
                            username:$('#userName').val(),
                            userPassword:pwd
                        },
                        dataType: 'json',
                        async: false,
                        success: function(data) {
                            if(data.message.operateCode == 102008){
                                if (data.redirectUrl == '' || data.redirectUrl == undefined) {
                                    window.location.href = "/";
                                } else {
                                    window.location.href = decodeURIComponent(data.redirectUrl);
                                }
                            }else{
                                alert(data.message.message);
                                return false;
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

            
            // 修改资料或者注册里的用户名信息
            // $('#userName').val(jQuery.cookie('eis_username'));
           
            // console.log(jQuery.cookie('eis_username').length)
            // 修改资料及注册页面左上角登录
            if(jQuery.cookie('eis_username') == null){
                $('#user-name').css('display','none');
                $('#user-img').css('display','none');
                $('#user-email').css('display','none');
                // alert('请登陆！');
            }else{
                //  登录页面登录后的状态
                 // jQuery.cookie('eis_username',$('#userName').val())
                $('#login').empty().append('<div style="background-color: #fef5be;border: 2px solid #fdd425; border-radius: 5px;-moz-border-radius: 5px;-webkit-border-radius: 5px;padding: 5px 10px;margin: 100px;font-size: 13px;width: 222px;">您已登录成功！</div>');
                var userImg = jQuery.cookie('eis_userheadpic');
                var name = jQuery.cookie('eis_username');
                // $('#user-name').text('你好,'+ name);
                if(userImg == 0){
                    $('#user-img').append('<img src="../../../theme/basic/images/touxiang.png" />');
                }else{    
                    $('#user-img').append('<img style="width:20px;height:20px;border-radius:10px;" src="/file/client/'+userImg+'" />');
                }
                $('#user-email a').append('<img src="../../../theme/basic/images/user-email.png" />');
                // <li><a href="http://me.mo4u.cn/content/user/20170515102939.shtml"><img src="../../../theme/basic/images/moneygl.png" style="margin-right:15px;position:relative;left:2px;" />账户余额</a></li>
                // <li><a href="http://me.mo4u.cn/content/friend/upload/index.shtml"><img src="../../../theme/basic/images/productgl.png" style="margin-right:13px;width:12px;position:relative;left:1px;"/>我的作品</a></li>
                if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  
                    $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li><li style="float:left;"><a href="/cart.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的购物车</a></li></ul>');  
                } else if (/(Android)/i.test(navigator.userAgent)) {  
                    $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li><li style="float:left;"><a href="/cart.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的购物车</a></li></ul>');
                } else {  
                    $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li></ul>');
                }
                $("#user-register").empty().html('<a onclick="quit()"><img src="../../../theme/basic/images/login.png" alt="" class="register" style="margin-bottom: 2px;"></i><span class="text">登出</span></a>');
                
                // 加载完我的后的
                var toggles = true;
                $('#menu').mouseenter(function() {
                    // if(toggles == true && $(this).attr('class') == 'dropdown'){
                        $('.dropdown-menu').css('display','block');
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
                        $('.dropdown-menu').css('display','none');
                    //     toggles = true;
                    //     $(this).addClass('dropdown');
                    // }
                })

                $('#user-name').css('display','block');
                $('#user-img').css('display','block');
                $('#user-email').css('display','block');
            }

                var pwsL10n = {
                    empty: "强度评估",
                    short: "极弱",
                    bad: "弱",
                    good: "中等",
                    strong: "强",
                    mismatch: "不匹配"
                };
                $('.entry input').blur(function() {
                        // 用户名验证
                        if ($('#userName')[0].value.indexOf(' ') > -1) {
                            $('#user_text').html("<span class='wpuf-help' style='color:red;' id='user_text'>输入有空格，请重新输入！</span>");
                        } else if($('#userName').val().length <4 || $('#userName').val().length > 20){
                            $('#user_text').html("<span class='wpuf-help' style='color:red;' id='user_text'>请输入4到20位数的用户名！</span>");
                        }else{
                            $('#user_text').html('<span class="wpuf-help" id="user_text">支持中文、字母、数字4-20个字符</span>')
                        }

                        // 手机号验证
                        if ($(this).is('#wpuf-user_email')) {
                            if (this.value.length === 0) {
                                $('#email_text').html("");
                            } else if (!(/^1[3|4|5|7|8]\d{9}$/.test(this.value))) {
                                $('#email_text').html("<span class='wpuf-help' style='color:red;' id='email_text'>输入有误，请重新输入！</span>");
                            } else {
                                $('#email_text').html("");
                            }
                        }

                        // 密码验证
                        if ($(this).is('#userPassword')) {
                            if (this.value.length === 0) {
                                $('#password_text').html('<span class="wpuf-help" id="password_text">可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>');
                            } else if (this.value.length >= 7 && this.value.length < 10) {
                                $('#pass-strength-result').addClass('short');
                                $('#pass-strength-result').text(pwsL10n.short);
                                $('#password_text').html('<span class="wpuf-help" id="password_text">可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>');
                            } else if (this.value.length >= 10 && this.value.length < 13) {
                                $('#pass-strength-result').addClass('bad');
                                $('#pass-strength-result').text(pwsL10n.bad);
                                $('#password_text').html('<span class="wpuf-help" id="password_text">可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>');
                            } else if (this.value.length >= 13 && this.value.length < 16) {
                                $('#pass-strength-result').addClass('good');
                                $('#pass-strength-result').removeClass('strong');
                                $('#pass-strength-result').text(pwsL10n.good);
                                $('#password_text').html('<span class="wpuf-help" id="password_text">可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>');
                            } else if (this.value.length >= 16) {
                                $('#pass-strength-result').addClass('strong');
                                $('#pass-strength-result').text(pwsL10n.strong);
                                $('#password_text').html('<span class="wpuf-help" id="password_text">可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>');
                            } else {
                                $('#password_text').html("<span class='wpuf-help' style='color:red;' id='password_text'>密码输入少于7位！</span>");
                            }
                        }


                        // 确认密码
                        if ($(this).is('#userPassword2')) {
                            if (this.value == $('#userPassword')[0].value && this.value.length >= 7 && this.value.length < 10) {
                                $('#pass-strength-result').text(pwsL10n.short);
                            } else if (this.value == $('#userPassword')[0].value && this.value.length >= 10 && this.value.length < 13) {
                                $('#pass-strength-result').text(pwsL10n.bad);
                            } else if (this.value == $('#userPassword')[0].value && this.value.length >= 13 && this.value.length < 16) {
                                $('#pass-strength-result').text(pwsL10n.good);
                            } else if (this.value == $('#userPassword')[0].value && this.value.length >= 16) {
                                $('#pass-strength-result').text(pwsL10n.strong);
                            } else {
                                $('#pass-strength-result').text(pwsL10n.mismatch);
                            }
                        }

                    })
                    .keyup(function() {
                        $(this).triggerHandler("blur");
                    }).focus(function() {
                        $(this).triggerHandler("blur");
                    }); 
            })

                // 左上角登陆按钮
                function login() {
                    if ($('#rememberme')[0].checked === false || $('#user_login')[0].value.indexOf(' ') > -1 || $('#user_login')[0].value.length < 4 || $('#user_login')[0].value.length > 20|| $('#pass1')[0].value.length < 7) {
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
                                        // alert("登录成功");
                                        if(msg.redirectUrl){    
                                            window.open(decodeURIComponent(msg.redirectUrl));
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

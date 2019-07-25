        var openBlock;
        var transactionId;
        var cartId;
        $(function() {
            // 手机端
            if($(window).width()<768) {
                $('.qqzxzx').attr('href','mqqwpa://im/chat?chat_type=wpa&uin=3268551923&version=1&src_type=web&web_src=oicqzone.com');
                $('.visible-phone>span').text('指弹教学分区（点击展开）')
            }
             
            // 隐藏左上角登录框
            $('#main-content').on("click", function(){
                // 隐藏左上角登录框
                // $('#loginForm').css({'display':'none'});
                openBlock = true;
                // 隐藏左下角客服QQ悬浮窗
                $('#box').css('display', 'none');
                $('#kefu a').first().find('span').remove();
                $('#kefu a').first().html('<img alt="客服中心" tilt="客服中心" class="adid_icon" src="../../../theme/basic/images/kefu.png">');
                // 隐藏左下角购物车悬浮窗
                $('.ptfcart').hide(0,function(){
                    $('#asid_share').css('right','20px');
                });

                $('.cartLists p').remove();
                $('.ptfImg').css('display','block');
            });
            //  登录注册显示隐藏
            //  document.getElementById("menu").addEventListener('click',function() {
            //             var classVal = document.getElementById("profile-messages").getAttribute("class");
            //             if (classVal == 'dropdown-toggle') {
            //                 document.getElementById("profile-messages").setAttribute("class", 'ula');
            //                 // document.getElementById("dropdown-menu").setAttribute("class", 'openul dropdown-menu');
            //                 document.getElementById("dropdown-menu").style.display = 'block';
            //                 $('#user_login').focus();
            //                 $('#main-content').click(function(){
            //                     document.getElementById("dropdown-menu").setAttribute("class", 'dropdown-menu'); 
            //                     document.getElementById("profile-messages").setAttribute("class", 'dropdown-toggle'); 
            //                 })
            //                 return;
            //             } else {
            //                 // document.getElementById("dropdown-menu").setAttribute("class", 'dropdown-menu');
            //                 document.getElementById("dropdown-menu").style.display = 'none';
            //                 document.getElementById("profile-messages").setAttribute("class", 'dropdown-toggle');
            //                 return;
            //             }
            //   })
            $('#menu').click(function(){
                if($(this).text().indexOf('登录') > -1){
                    if(openBlock == true || openBlock == undefined){
                        $('#loginForm').css({'display':'block'});
                        $('#user_login').focus();
                        openBlock = false;
                    }else{
                        $('#loginForm').css({'display':'none'});
                        openBlock = true;
                    }
                }
            })


            // 搜索栏功能
            $('.left').focus(function(){
                    if (this.value == '搜索 ...') {
                        this.value = '';
                    }
            }) 
            $('.left').blur(function(){
                    if (this.value == '') {
                        this.value = '搜索 ...';
                    }
            })
        })

        // 搜索框回车键
        function keySearch(){
                if(window.event.keyCode == 13){
                        search();
                }
        }

        //  搜索按钮
        function search(){
                var title = $('.left').val();
                window.location.href = '/content/post/index.shtml?search=search&title='+title;
        }

        // 注册隐藏显示
        // function block(){
        //     if($('#profile-messages').attr('class') == 'dropdown-toggle'){
        //                  $('#profile-messages').removeClass('dropdown-toggle');
        //                  $('#profile-messages').addClass('ula');
        //                  $('#dropdown-menu').addClass('openul');
        //     }else{
        //                  $('#dropdown-menu').removeClass('openul');
        //                  $('#dropdown-menu').addClass('dropdown-menu');
        //                  $('#profile-messages').removeClass('ula');
        //     }
        // }

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

                            $.post('/weixinUser/createByAccessCode.json',{name:nick,openid:openId,otype:1,token:accessToken},function(data,status){
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
            //  window.location.href='https://open.weixin.qq.com/connect/qrconnect?' +
            //     'appid=wxac02160238252bfa&redirect_uri=http%3a%2f%2fwww.me.mo4u.cn%2f'+window.location.pathname.substr(1)+'&response_type=code&scope=snsapi_login&state=3d6be0a4035d839573b04816624a415e#wechat_redirect';
            $.ajax({
                    type: "post",
                    url: '/weixinUser/createByAccessCode.json',
                    data: {},
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        console.log(msg);
                }
            })
        }


        // 回车登录
        function keyLogin(){
            if(window.event.keyCode == 13){
                $(".login-btn").click();
            }
        }
        
        // 左上角登陆按钮
        function login() {
            console.log($('#user_login')[0].value.length);
            if ($('#rememberme')[0].checked === false || $('#user_login')[0].value.indexOf(' ') > -1 || $('#user_login')[0].value.length > 20|| $('#user_login')[0].value.length < 2 || $('#pass1')[0].value.length < 6) {
                alert('输入有误，请重新输入！');
            } else {
                var pwds = $("#pass1").val();
                var name = $('#user_login').val();
                var pwd = $.base64.encode(pwds);
                $.ajax({
                    type: "post",
                    url: '/user/login.json',
                    data: {
                        username:name,
                        userPassword:pwd    
                    },
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        switch (msg.message.operateCode) {
                            case 102008:
                                if(msg.redirectUrl){
                                    window.location.href = (decodeURIComponent(msg.redirectUrl));
                                    // jQuery.cookie('eis_username',$('#user_login').val());
                                    return false;
                                }
                                    //console.log('222')
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



        // 退出登录按钮
        function quit(){ 
               $.ajax({
                    type: "get",
                    url: '/user/logout.json',
                    dataType: 'json',
                    // async: false,
                    success: function(data) {
                        if(data.message.operateCode == 102008){
                                // alert(data.message.message);
                                // jQuery.cookie('eis_username',null)
                                window.location.reload(); 
                        }else{
                            alert("您已经是登陆状态！");
                        }
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    },
                }, 'json');
        }


    // 提交订单
    function submitPay(){
        // var tid ='';
        // $('.tid').each(function(d,f){
        //     tid+= 'tid='+f.value+'&';
        // });
        // tid = tid.substring(0,tid.length-1);
        if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  
            weixin();  
        } else if (/(Android)/i.test(navigator.userAgent)) {  
            weixin();   
        } else {  
            pcweixin();
        }; 
    }

    // 移动端提交订单微信扫码支付
    function weixin(){
        var orderId = $('.cartId').val();
        var ip;
        if($('input[name="priceOrder"]').is(':checked')){
            if($('#vipRemove').text() == ''){
                  location.href="/buy/settleUp.shtml?payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId; 
            }else{
                $('.vip').each(function(a,b){
                    if($(this).is(':checked')){
                        $.ajax({
                            type: "post", 
                            url:'/buy/add.json',
                            data:{  
                                productCode:$(this).val(),
                                count:1,
                                directBuy:false,
                                cartId:orderId
                            },
                            dataType: 'json',
                            // async: false,
                            success: function(data) {
                                location.href="/buy/settleUp.shtml?payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId;
                                return false;
                            },
                            error: function(XMLResponse) {
                                alert("操作失败:" + XMLResponse.responseText);
                            },
                        }, 'json');
                        return false;
                    }else{
                        if(a == 2){
                            location.href="/buy/settleUp.shtml?payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId; 
                        }
                    }
                })
            }
        }else{
            alert('请阅读完付费课程服务条款并勾选！');
        }
    }


    // pc端提交订单微信扫码支付
    function pcweixin(){
        var orderId = $('.cartId').val();
        var ip;
        if($('input[name="priceOrder"]').is(':checked')){
            if($('#vipRemove').text() == ''){
                  location.href="/buy/settleUp.shtml?payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId; 
            }else{
                $('.vip').each(function(a,b){
                    if($(this).is(':checked')){
                        $.ajax({
                            type: "post", 
                            url:'/buy/add.json',
                            data:{  
                                productCode:$(this).val(),
                                count:1,
                                directBuy:false,
                                cartId:orderId
                            },
                            dataType: 'json',
                            // async: false,
                            success: function(data) {
                                location.href="/buy/settleUp.shtml?payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId;
                                return false;
                            },
                            error: function(XMLResponse) {
                                alert("操作失败:" + XMLResponse.responseText);
                            },
                        }, 'json');
                        return false;
                    }else{
                        if(a == 2){
                            location.href="/buy/settleUp.shtml?payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId; 
                        }
                    }
                })
            }
        }else{
            alert('请阅读完付费课程服务条款并勾选！');
        }
    }

    // 配送地址提交订单
    function submitPayAddress(){
        var addressBookId ='';
        $('.addressBookId').each(function(){
            if ($(this).is(':checked')) {
                addressBookId= $(this).val();
            };
        })
        var orderId = $('.cartId').val();
        var ip;
        if($('input[name="priceOrder"]').is(':checked')){
            if(addressBookId == ''){    
                $("html, body").animate({
                    scrollTop: $(".content").offset().top},
                    {duration: 500,easing: "swing"}
                );
                alert('亲，请勾选配送地址！');
            }else{  
                location.href="/buy/settleUp.shtml?addressBookId="+addressBookId+"&payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ orderId; 
            }
          }else{
            alert('请阅读完付费课程服务条款并勾选！');
        }
    }


    // vip购买
    function buy(){
        var font =$('#moremoney').text();
        var vip = $("#vip").val();
        if(jQuery.cookie('eis_username')== null){
            alert("您还没有登录");
            location.href = "/user/login.shtml";
        }else{
            // $('.userType').each(function(){
            //     if($(this).is(':checked') == true){
            //         if(font.substring(0,font.length-1) < $(this).val()){
            //             alert('可用余额不足！')
                        // $('#buyForm').append('<h2 style="color:red;">当前可用余额不足以完成本次交易，请充值后再试！</h2>');
                    // }else{
                        // $(this).val() 这是购买的产品
                        // location.href="/buy/settleUp.shtml?addressBookId="+addressBookId+openIDStr+"&payTypeId="+$(".pay input[type=radio]:checked").val()+"&orderId="+ $("input[id='orderId']").val();
                        //给参数直接跳到二维码购买界面
            if(jQuery.cookie('frontVipLevel')== 2){
                alert('您已经是vip用户了！')
            }else{
                    $('.userType').each(function(){
                            if($(this).is(':checked') == true){
                                $.ajax({
                                    type:"POST",
                                    url:'/buy/add.json',
                                    data:{  
                                        productCode:$(this).val(),
                                        count:1,
                                        directBuy:true
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var orderId = data.order.cartId;
                                        switch (data.message.operateCode) {
                                            case 500031:
                                                alert(data.message.message);
                                                location.href="/user/login.shtml";
                                                break;
                                            case 102008:
                                                // alert(data.message.message);
                                                location.href='/buy/delivery.shtml?orderId='+orderId+'&tid='+ data.item.transactionId;
                                                break;
                                            case 500004:
                                                alert(data.message.message);
                                                break;
                                        }
                                    },
                                    error:function (data) {
                                        alert("系统繁忙,请稍后再试");
                                        return false;
                                    }
                                });
                        }
                    // }
                })    
            }
    }
    }


    // 加入购物车
    function cart(){
        if(jQuery.cookie('eis_username')== null){
            alert("您还没有登录");
            // location.href = "/user/login.shtml";
            $('#loginForm').css({'display':'block','top':59});
            // $('#profile-messages').removeClass('dropdown-toggle');
            // $('#profile-messages').addClass('ula');
            // $('#dropdown-menu').css('display','block');
            $('#user_login').focus();
        }else{
            $.ajax({
                type:"POST",
                url:'/buy/add.json',
                data:{  
                    productCode:$('#productCode').val(),
                    count:1,
                    directBuy:false
                },
                dataType:'json',
                success:function (data) {
                    var orderId = data.order.cartId;
                    switch (data.message.operateCode) {
                        case 500031:
                            alert(data.message.message);
                            location.href="/user/login.shtml";
                            break;
                        case 102008:
                            // 这个接口没有提供当前加入的商品购物车中是否有
                            window.location.reload();
                            // 左侧购物车加1
                            // var num = $('.ptfcarts p').eq(1).text();
                            // $('.ptfcarts p').eq(1).text(parseFloat(num) + 1);
                            alert(data.message.message);
                            break;
                        case 500004:
                            window.location.reload();
                            alert(data.message.message);
                            break;
                    }
                },
                error:function (data) {
                    alert("系统繁忙,请稍后再试");
                    return false;
                }
            });
        }
    }

    //  金额充值
    function buynow(){
        var r = /^\+?[1-9][0-9]*$/;  //正整数
        if(jQuery.cookie('eis_username')== null){
            alert("您还没有登录");
            location.href = "/user/login.shtml";

        }else if(!r.test($('#ice_money').val())){
            alert('请输入正确的金额！');
            return false;
        }else{
            $.ajax({
                type:"POST",
                url:'/buy/add.json',
                data:{  
                    productCode:$('#ice_money').val()

                },
                dataType:'json',
                success:function (data) {
                    switch (data.message.operateCode) {
                        case 500031:
                            alert(data.message.message+"这里返回的operateCode 500031");
                            location.href="/user/login.shtml";
                            break;
                        case operateResultSuccess:
                            location.href='/buy/delivery.shtml?tid='+data.tsId;
                            break;
                        case 500004:
                            alert(data.message.message);
                            break;
                    }
                },
                error:function (data) {
                    alert("系统繁忙,请稍后再试");
                    return false;
                }
            });
            }
        };


        // 详情页用户立即购买
        function buy_now(){
            if(jQuery.cookie('eis_username')== null){
                alert("您还没有登录");
                // location.href = "/user/login.shtml";

                $('#loginForm').append('<img src="../../../theme/basic/images/error.png" class="loginFormImg" onclick="hideImg()">').css({'display':'block','top':59});
                // $('#profile-messages').removeClass('dropdown-toggle');
                // $('#profile-messages').addClass('ula');
                // $('#dropdown-menu').css('display','block');
                $('#user_login').focus();
            }else{
                $.ajax({
                    type:"POST",
                    url:'/buy/add.json',
                    data:{  
                        productCode:$('#documentCode').val(),
                        count:1,
                        directBuy:true
                    },
                    dataType:'json',
                    success:function (data) {
                        switch (data.message.operateCode) {
                            case 500031:
                                alert(data.message.message);
                                location.href="/user/login.shtml";
                                break;
                            case 102008:
                                // alert(data.message.message);
                                location.href='/buy/delivery.shtml?orderId='+data.order.cartId+'&tid='+ data.item.transactionId;
                                break;
                            case 500004:
                                alert(data.message.message);
                                break;
                        }
                    },
                    error:function (data) {
                        alert("系统繁忙,请稍后再试");
                        return false;
                    }
                });
            }
        }


        // 详情页点赞按钮
        function zan(){
            $.ajax({
                type:"POST",
                url:"/userRelation/add.json",
                data:{  
                    uuid:$('input[name="uuid"]').val(),
                    objectType:$('input[name="objectType"]').val(),
                    objectId:$('input[name="objectId"]').val(),
                    relationType:'praise',
                },           
                async:false,
                success:function(data){
                    switch (data.message.operateCode) {
                        case 500031:
                            alert(data.message.message);
                            location.href="/user/login.shtml";
                            break;
                        case 102008:
                            alert(data.message.message);
                            window.reload();
                            break;
                        case 500018:
                            alert(data.message.message);
                            window.reload();
                            break;
                    }			
                }
            })
        }

        // 修改用户名
        function reviseUsername(){
            if($('input[name="user_login"]').val().length == 0 || $('input[name="user_login"]').val().length < 2 || $('input[name="user_login"]').val().length > 20){
                alert('输入有误，请重新输入用户名！');
            }else{
                $.ajax({
                    type: "post",
                    url: '/user/changeUserNameByLocalUser.json',
                    data: {
                        username:$('input[name="user_login"]').val()
                    },
                    dataType: 'json',
                    async: false,
                    success: function(data) {
                        if(data.message.operateCode == 102008){
                            alert(data.message.message);
                            // jQuery.cookie('eis_username',$('input[name="user_login"]').val())
                            location.reload();
                            console.log(jQuery.cookie('eis_username'))

                        }else{
                            location.reload();
                        }
                        
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    },
                }, 'json');
            }
        }

        // 收藏按钮
        function  collect(){
                $.ajax({
                    type:"POST",
                    url:"/userRelation/add.json",
                    data:{  
                        uuid:$('input[name="uuid"]').val(),
                        objectType:$('input[name="objectType"]').val(),
                        objectId:$('input[name="objectId"]').val(),
                        relationType:'favorite',
                    },           
                    async:false,
                    success:function(data){
                        switch (data.message.operateCode) {
                            case 500031:
                                alert(data.message.message);
                                location.href="/user/login.shtml";
                                break;
                            case 102008:
                                alert(data.message.message);
                                window.location.reload();
                                break;
                            case 500018:
                                alert(data.message.message);
                                window.location.reload();
                                break;
                        }			
                    }
                })
        }

       
        // 修改资料
        function updata(){
            var file = $('#fileupload').val();//头像文件
            var name = $('#userName').val();
            // var eml = $('#wpuf-user_email').val();
            var type="";
            var description = dcp.length;
            var wpufuser_url = url.length;
            var qq_weibo = qq.length;
            var sina_weibo = sina.length;

            if(jQuery.cookie('eis_username') == null){
                alert('您还未登录！');
            }else if(document.getElementById("fileupload").value ==''){   
                    // if(description == 0 && wpufuser_url == 0 && qq_weibo == 0 && sina_weibo == 0){
                    //     alert("至少选一项填写！")
                    // }else{
                        $.ajax({
                            type:"POST",
                            url:"/user/submitChangeOther.json",
                            data:{  
                                username:name,
                                // phone:eml,
                            },           
                            async:false,
                            success: function(data) {
                                if(data.message.operateCode == 102008){
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
                    // }
            }else{
                // 检验上传图像的文件格式
                ype=document.getElementById("fileupload").value.match(/^(.*)(\.)(.{1,8})$/)[3];
                type=ype.toUpperCase();
                if(type!="JPEG"   &&   type!="PNG"   &&   type!="JPG"   &&   type!="GIF"){
                    alert("上传图片类型错误");  
                    return false;
                } 
                // 上传图片不能超过2M
                var maxsize = 2*1024*1024;//2M  
                var errMsg = "上传的附件文件不能超过2M！！！";  
                var tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过2M，建议使用IE、FireFox、Chrome浏览器。";  
                var  browserCfg = {};  
                var ua = window.navigator.userAgent;  
                if (ua.indexOf("MSIE")>=1){  
                    browserCfg.ie = true;  
                }else if(ua.indexOf("Firefox")>=1){  
                    browserCfg.firefox = true;  
                }else if(ua.indexOf("Chrome")>=1){  
                    browserCfg.chrome = true;  
                }
                
                var obj_file = document.getElementById("fileupload");  
                var filesize = 0;  
                if(browserCfg.firefox || browserCfg.chrome ){  
                    filesize = obj_file.files[0].size;  
                }else if(browserCfg.ie){  
                    var obj_img = document.getElementById('tempimg');  
                    obj_img.dynsrc=obj_file.value;  
                    filesize = obj_img.fileSize;  
                }else{  
                    alert(tipMsg);  
                    return false;  
                }  
                if(filesize==-1){  
                    alert(tipMsg);  
                    return false;  
                }else if(filesize>maxsize){  
                    alert(errMsg);  
                    return false;  
                }else{ 
                    if(description == 0 && wpufuser_url == 0 && qq_weibo == 0 && sina_weibo == 0){
                        alert("至少选一项填写！")
                    }else{
                        $.ajax({
                            type:"POST",
                            url:"/user/submitChangeOther.json",
                            data:{  
                                username:name,
                                // phone:eml,
                                userDescription:dcp,
                                user_url:url,
                                qq_weibo: qq,
                                sina_weibo:sina,
                            },           
                            async:false,
                            success: function(data) {
                                if(data.message.operateCode == 102008){
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
            }
        }               
                                        
              //图片上传预览    IE是用了滤镜。
                function previewImage(file)
                {
                    if(jQuery.cookie('eis_username') == null){
                        alert('您还未登录！');
                    }else{
                        var MAXWIDTH  = 260; 
                        var MAXHEIGHT = 180;
                        var div = document.getElementById('preview'); 
                        if (file.files && file.files[0])
                        {
                            div.innerHTML ='<img id=imghead>';
                            var img = document.getElementById('imghead');
                            img.onload = function(){
                                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                                img.width  =  rect.width;
                                img.height =  rect.height;
                //                 img.style.marginLeft = rect.left+'px';
                                img.style.marginTop = rect.top+'px';
                            }
                            var reader = new FileReader();
                            reader.onload = function(evt){img.src = evt.target.result;}
                            reader.readAsDataURL(file.files[0]);
                        }
                        else //兼容IE
                        {
                            var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
                            file.select();
                            var src = document.selection.createRange().text;
                            div.innerHTML = '<img id=imghead>';
                            var img = document.getElementById('imghead');
                            img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
                            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                            status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
                            div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
                        }
                    }
                } 
                function clacImgZoomParam( maxWidth, maxHeight, width, height ){
                    var param = {top:0, left:0, width:width, height:height};
                    if( width>maxWidth || height>maxHeight )
                    {
                        rateWidth = width / maxWidth;
                        rateHeight = height / maxHeight;
                        
                        if( rateWidth > rateHeight )
                        {
                            param.width =  maxWidth;
                            param.height = Math.round(height / rateWidth);
                        }else
                        {
                            param.width = Math.round(width / rateHeight);
                            param.height = maxHeight;
                        }
                    }
                    
                    param.left = Math.round((maxWidth - param.width) / 2);
                    param.top = Math.round((maxHeight - param.height) / 2);
                    return param;
                } 


                // 我的作品上传
                function submitProduct(form){
                    if(form.title.value.length == 0 || form.redirectTo.value.length == 0 || form.documentBrief.value.length == 0 || form.defaultNodeId.value == -1){
                        alert('请填写完整！');
                        return false;
                    }else if($(".wpuf__check_pu_8544").is(':checked') == true){
                        if(form.author.value.length == 0 || document.getElementById("files").files[0] == undefined){
                            alert('请填写完整！');
                            return false;
                        }else{
                            $('.comments-loading').css('display','block');
                            $('#submitForm').ajaxForm(function(){ 
                                $('.comments-loading').css('display','none');
                                alert('提交成功');   
                            });  
                        }
                    }else{
                        $('.comments-loading').css('display','block');
                        $('#submitForm').ajaxForm(function(){
                            $('.comments-loading').css('display','none'); 
                            alert('提交成功');   
                        });  
                    }
                }
                
//    if(navigator.userAgent.indexOf("Chrome") > -1){
//      如果是Chrome：
//      <audio src="" type="audio/mp3" autoplay=”autoplay” hidden="true"></audio>
//     }else if（navigator.userAgent.indexOf("Firefox")!=-1）{
//      如果是Firefox：
//      <embed src="" type="audio/mp3" hidden="true" loop="false" mastersound></embed>
//     }else if（navigator.appName.indexOf("Microsoft Internet Explorer")!=-1 && document.all）{
//       如果是IE(6,7,8):
//       <object classid="clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95"><param name="AutoStart" value="1" /><param name="Src" value="" /></object>
//     }else if（navigator.appName.indexOf("Opera")!=-1）{
//        如果是Oprea：
//        <embed src="" type="audio/mpeg"   loop="false"></embed>
//     }else{
//        <embed src="" type="audio/mp3" hidden="true" loop="false" mastersound></embed>
//     }


            //以下是在线调弦 将秒数转为00:00格式
            function timeToStr(time) {
                 var m = 0,
                 s = 0,
                 _m = '00',
                 _s = '00';
                 time = Math.floor(time % 3600);
                 m = Math.floor(time / 60);
                 s = Math.floor(time % 60);
                 _s = s < 10 ? '0' + s : s + '';
                 _m = m < 10 ? '0' + m : m + '';
                 return _m + ":" + _s;
             }
             //触发播放事件
            $('.plays').each(function(){
                $(this).click(function(){
                    $('.ao').each(function(x,y){
                          y.pause();
                    })
                    $('.plays').each(function(x,y){
                        y.style.opacity = 0.8;
                    })
                    $(this).parent().siblings().eq(1).children().css('opacity',0.8);
                    $(this).css('opacity',1);
                    var that = this;
                    var audio = $(this).parent('span').siblings('audio')[0];
                    audio.play();
                    var flag = setInterval(function(){
                        var t=parseInt(audio.currentTime);
                        var j = parseInt(audio.duration);
                        $(that).parent('span').siblings('input').attr({'max':j});
                        // $('.max').html(timeToStr(360));
                        $(that).parent('span').siblings('input').val(t);
                        // $('.cur').text(timeToStr(t));
                    },1000);

                    //监听滑块，可以拖动
                    $(this).parent('span').siblings('input').on('change',function(){
                        audio.currentTime=this.value;
                        $(this).parent('span').siblings('audio').val(this.value);
                    });
                })
            })
         
            //  触发暂停
            // $('.pasues').on('click',function(){
            //     var audio=document.getElementById('ao');
            //     audio.pause();
            // })
            $('.pasues').each(function(){
                $(this).click(function(){
                    $('.pasues').each(function(x,y){
                        y.style.opacity = 0.8;
                    })
                    $(this).parent().siblings().eq(1).children().css('opacity',0.8);
                    $(this).css('opacity',1);
                    var audio = $(this).parent('span').siblings('audio')[0];
                    audio.pause();
                })
            })
            //触发循环
            // $('.oneAgain').on('click',function(){
            //     var audio=document.getElementById('ao');
            //     audio.loop=true;
            // })
            $('.oneAgain').each(function(){
                $(this).click(function(){
                    var audio = $(this).parent('span').siblings('audio')[0];
                
                    if(audio.loop == true){
                        $(this).css('opacity',0.8);
                        audio.loop=false;
                    }else{
                        $(this).css('opacity',1);
                        audio.loop=true;
                    }
                })
            })

            // 页面左侧实物购买书籍
            function buyBook(){
                var num = $('input[name="num"]').val();
                var qixingbook = $('input[name="qixingbook"]').val();
                if(jQuery.cookie('eis_username')== null){
                    alert("您还没有登录");
                    // location.href = "/user/login.shtml";
                    $('#loginForm').css({'display':'block','top':59});
                    // $('#profile-messages').removeClass('dropdown-toggle');
                    // $('#profile-messages').addClass('ula');
                    // $('#dropdown-menu').css('display','block');
                    $('#user_login').focus();
                }else{
                    $.ajax({
                        type:"POST",
                        url:'/buy/add.json',
                        data:{  
                            productCode:qixingbook,
                            count:num,
                            directBuy:true
                        },
                        dataType:'json',
                        success:function (data) {
                            switch (data.message.operateCode) {
                                case 500031:
                                    alert(data.message.message);
                                    location.href="/user/login.shtml";
                                    break;
                                case 102008:
                                    // alert(data.message.message);
                                    location.href='/buy/delivery.shtml?orderId='+data.order.cartId+'&tid='+ data.item.transactionId;
                                    break;
                                case 500004:
                                    alert(data.message.message);
                                    break;
                            }
                        },
                        error:function (data) {
                            alert("系统繁忙,请稍后再试");
                            return false;
                        }
                    });
                }
            }

            // 打开新建地址窗口
            function addAddress(){
                $('.box').css('display','block');
                $('#top-bar').css('position','relative');
                $('.addAddress_head span').text('新增收货地址');
                $('.modelAddress tr td button').text('保存');
                $('.modelAddress tr td button').attr('onclick','newAddress()');
                $('#main-content').css('margin-top','0');
            }
            
            // 关闭新建地址窗口
            function closeBox(){
                $('.box').css('display','none');
                $('#top-bar').css('position','fixed');
            }

            // 新建地址按钮
            function newAddress(){
                var people = $('input[name="model_people"]').val().length;
                var phone = $("input[name='model_phone']").val();
                var address = $("input[name='model_address']").val().length;
 
                if( people < 2 || people > 6){
                      alert('请输入长度为2-6个字符的收货人名称！');
                }else if(phone = "" || !(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
                      alert('输入手机号码有误！')
                }else if(address < 5 || address > 50){
                      alert('请输入5-30字符的详细地址!')
                }else{
                    $.ajax({     
                        type : "post",  
                        url : "/addressBook/create.json",  
                        dataType:"json",
                        data:{
                            province:$("#prov  option:selected").text(),
                            city:$("#city  option:selected").text(),
                            district:$("#dist  option:selected").text(),
                            address:$("input[name='model_address']").val(),
                            contact:$('input[name="model_people"]').val(),
                            // phone:phone,
                            mobile:$("input[name='model_phone']").val(),
                            // postcode:$('#zipcode').val(),邮政编码
                            setDefaultAdd:$('#setDefaultAdd').attr('datavalue')
                        }, 
                        success : function(data) { 
                            if (data.message.operateCode=='102008') {
                                alert(data.message.message);
                                location.reload(); 
                            }else{
                                alert(data.message.message);
                                return false;    
                            }
                        },  
                        error : function(data) {  
                            alert(data.message.message);
                        }  
                    });  
                }
            }

            // 修改地址按钮
            function updateAddress(){
                var people = $('input[name="model_people"]').val().length;
                var phone = $("input[name='model_phone']").val();
                var address = $("input[name='model_address']").val().length;
                var id = $('.addAddress_head span').attr('dataaddressBookId');
 
                if( people < 2 || people > 6){
                      alert('请输入长度为2-6个字符的收货人名称！');
                }else if(phone = "" || !(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
                      alert('输入手机号码有误！')
                }else if(address < 5 || address > 30){
                      alert('请输入5-30字符的详细地址!')
                }else{
                    $.ajax({     
                        type : "post",  
                        url : "/addressBook/update.json",  
                        dataType:"json",
                        data:{
                            province:$("#prov  option:selected").text(),
                            city:$("#city  option:selected").text(),
                            district:$("#dist  option:selected").text(),
                            address:$("input[name='model_address']").val(),
                            contact:$('input[name="model_people"]').val(),
                            // phone:phone,
                            mobile:$("input[name='model_phone']").val(),
                            // postcode:$('#zipcode').val(),邮政编码
                            setDefaultAdd:$('#setDefaultAdd').attr('datavalue'),
                            addressBookId:id
                        }, 
                        success : function(data) { 
                            if (data.message.operateCode=='102008') {
                                alert(data.message.message);
                                location.reload(); 
                            }else{
                                alert(data.message.message);
                                return false;    
                            }
                        },  
                        error : function(data) {  
                            alert(data.message.message);
                        }  
                    });  
                }
            }

            // 加载更多收货地址
            function getMoreAddress(){
                alert('more');
            }

            // 添加地址默认地址更变
            var ab = true;
            function defaultAddress(){
                 if(ab == true){
                     $('#setDefaultAdd').attr('datavalue','Y');
                     ab = false;
                 }else{
                     $('#setDefaultAdd').attr('datavalue','N');
                     ab = true;
                 }
            }

            
            // 设置默认地址
            function one(a){
                $.ajax({  
                    type : "get",  
                    url : "/addressBook/setDefault/"+a+".json",  
                    dataType:"json",
                    data:{}, 
                    success : function(data) {
                        if (data.message.operateCode=='102008') {
                            alert(data.message.message);
                            location.reload(); 
                        }else{
                            alert(data.message.message);
                            return false;    
                        }
                    },  
                    error : function(data) {  
                        alert(data.message.message);
                    }  
                });
            }
            // 删除地址记录
            function deletes(a){
            if (confirm('确定删除该条收货地址信息吗？')) {
                    $.ajax({  
                        type : "post",  
                        url : "/addressBook/delete/"+a+".json",  
                        dataType:"json",
                        data:{}, 
                        success : function(data) { 
                            if (data.message.operateCode=='102008') {
                                alert(data.message.message);
                                location.reload(); 
                            }else{
                                alert(data.message.message);
                                return false;    
                            }
                        },  
                        error : function(data) {  
                            alert(data.message.message);
                        }  
                    }); 
                } 
            }

            // 修改地址
            $('.modification').on('click',function(){
                var dataaddressBookId = $(this).attr('dataaddressBookId');
                $('.addAddress_head span').text('修改收货地址');
                $('.addAddress_head span').attr('dataaddressBookId',dataaddressBookId);
                $('.modelAddress tr td button').text('更新');
                $('.modelAddress tr td button').attr('onclick','updateAddress()');

                // 联系人
                var contact = $(this).parents('tr').find('td').eq(0).text();
                $('input[name="model_people"]').val(contact);
                // 手机号码
                var phone = $(this).parents('tr').find('td').eq(3).text();
                $('input[name="model_phone"]').val(phone);
                // 详细地址
                var address = $(this).parents('tr').find('td').eq(2).text();
                $('input[name="model_address"]').val(address);
                // 地区
                var spantext = $(this).parents('tr').find('td span');
                var prov = spantext.eq(0).text(); 
                var city = spantext.eq(1).text(); 
                var dist = spantext.eq(2).text(); 
                cityselects(prov,city,dist); 
                // 弹出框显示
                $('.box').css('display','block');
                $('#top-bar').css('position','relative');
                $('#main-content').css('margin-top','0');
            })

            // 查看订单
        $('.seeOrder').each(function(){
            $(this).click(function(){
                cartId = $(this).siblings().val();
                var a = $(this).children().val();
                $('.wrap').empty().append('<img src="../../../theme/basic/images/07093129-1-24Q58.gif" alt="" style="border: 0;margin: 10% auto;display: block;">');
                $.ajax({  
                        type : "post",  
                        url : "/order/get.json",  
                        dataType:"json",
                        data:{
                            cartId:$(this).siblings().val()
                        }, 
                        success : function(data) { 
                            console.log(data);
                            var status = data.order.currentStatus;
                            var deliveryOrderId = data.order.deliveryOrderId;
                            if(deliveryOrderId == undefined){
                                $('.wrap').empty().append('<div class="my_product" style="margin:10px 0;"><div class="my_productmessage"><div class="my_productmessage_left"><img src="../../../theme/basic/images/bookone.png" alt=""><span>'+data.order.goodsDesc+'</span></div><div class="my_productmessage_right"><span>￥ '+data.order.money.chargeMoney+'</span> <span>'+data.order.totalProduct+'</span></div></div><div class="my_productmoney">商品总价：<span>'+data.order.money.chargeMoney+'</span> 您已支付：<span style="font-size:23px;color:red;font-weight: bold;">￥ '+data.order.money.chargeMoney+'</span></div></div>');
                                return;
                            }
                            transactionId = data.order.transactionIds[0];
                            $.ajax({  
                                type : "post",  
                                url : "/deliveryOrder/traceByDeliveryOrder/"+deliveryOrderId+".json",  
                                dataType:"json",
                                data:{}, 
                                success : function(message) { 
                                    console.log(message);
                                    console.log(a);
                                    if(a == 'true'){
                                        console.log('实物')
                                        if(message.deliveryOrder == undefined){
                                            console.log('暂时没有物流信息')
                                            if(status == 710053 || status == 710052 || status == 710047 || status == 710010){
                                                console.log('没有物流信息，不过用户点了确认收货')
                                                $('.wrap').empty().append('<div style="margin: 10px 0;">暂时没有物流消息！</div><div class="my_product"><div class="my_productmessage"><div class="my_productmessage_left"><img src="../../../theme/basic/images/bookone.png" alt=""><span>'+data.order.goodsDesc+'</span></div><div class="my_productmessage_right"><span>￥ '+data.order.money.chargeMoney+'</span> <span>'+data.order.totalProduct+'</span></div></div><div class="my_productmoney">商品总价：<span>'+data.order.money.chargeMoney+'</span> 您已支付：<span style="font-size:23px;color:red;font-weight: bold;">￥ '+data.order.money.chargeMoney+'</span></div></div>');
                                            }else{
                                                console.log('没有物流信息，还没点了确认收货')
                                                $('.wrap').empty().append('<div style="margin: 10px 0;">暂时没有物流消息！</div><div class="my_product"><div class="my_productmessage"><div class="my_productmessage_left"><img src="../../../theme/basic/images/bookone.png" alt=""><span>'+data.order.goodsDesc+'</span></div><div class="my_productmessage_right"><span>￥ '+data.order.money.chargeMoney+'</span> <span>'+data.order.totalProduct+'</span></div></div><div class="my_productmoney">商品总价：<span>'+data.order.money.chargeMoney+'</span> 您已支付：<span style="font-size:23px;color:red;font-weight: bold;">￥ '+data.order.money.chargeMoney+'</span></div><button class="comfirmProduct" onclick="comfirmProduct()">确认收货</button></div>');
                                            }
                                        }else{
                                            console.log('有物流信息');
                                            var traceData = message.deliveryOrder.traceData.traceUrl;
                                            if(status == 710053 || status == 710052 || status == 710047 || status == 710010){
                                                console.log('有物流信息，点了确认收货')
                                                $('.wrap').empty().append('<iframe src='+traceData+'></iframe><div class="my_message"><p> <span>'+data.deliveryOrder.brief+'</span></p><p>'+data.deliveryOrder.memory+'</p></div><div class="my_product"><div class="my_productmessage"><div class="my_productmessage_left"><img src="../../../theme/basic/images/bookone.png" alt=""><span>'+data.order.goodsDesc+'</span></div><div class="my_productmessage_right"><span>￥ '+data.order.money.chargeMoney+'</span> <span>'+data.order.totalProduct+'</span></div></div><div class="my_productmoney">商品总价：<span>'+data.order.money.chargeMoney+'</span> 您已支付：<span style="font-size:23px;color:red;font-weight: bold;">￥ '+data.order.money.chargeMoney+'</span></div></div>');
                                            }else{
                                                console.log('有物流信息，还没点了确认收货')
                                                $('.wrap').empty().append('<iframe src='+traceData+'></iframe><div class="my_message"><p> <span>'+data.deliveryOrder.brief+'</span></p><p>'+data.deliveryOrder.memory+'</p></div><div class="my_product"><div class="my_productmessage"><div class="my_productmessage_left"><img src="../../../theme/basic/images/bookone.png" alt=""><span>'+data.order.goodsDesc+'</span></div><div class="my_productmessage_right"><span>￥ '+data.order.money.chargeMoney+'</span> <span>'+data.order.totalProduct+'</span></div></div><div class="my_productmoney">商品总价：<span>'+data.order.money.chargeMoney+'</span> 您已支付：<span style="font-size:23px;color:red;font-weight: bold;">￥ '+data.order.money.chargeMoney+'</span></div><button class="comfirmProduct"  onclick="comfirmProduct()">确认收货</button></div>');
                                            }
                                        }
                                    }else{
                                        console.log('虚拟')
                                        $('.wrap').empty().append('<div class="my_product" style="margin:10px 0;"><div class="my_productmessage"><div class="my_productmessage_left"><img src="../../../theme/basic/images/bookone.png" alt=""><span>'+data.order.goodsDesc+'</span></div><div class="my_productmessage_right"><span>￥ '+data.order.money.chargeMoney+'</span> <span>'+data.order.totalProduct+'</span></div></div><div class="my_productmoney">商品总价：<span>'+data.order.money.chargeMoney+'</span> 您已支付：<span style="font-size:23px;color:red;font-weight: bold;">￥ '+data.order.money.chargeMoney+'</span></div></div>');
                                        return;
                                    }
                                },  
                                error : function(message) {  
                                    alert(message.message.message);
                                }  
                            });
                        },  
                        error : function(data) {  
                            alert(data.message.message);
                        }  
                    }); 
            })
        })


        // 确认收货
        function comfirmProduct(){
            $.ajax({  
                type : "post",  
                url : "/order/confirmDelivery/"+transactionId+".json",  
                dataType:"json",
                data:{
                    cartId:cartId
                }, 
                success : function(productmessage) { 
                    console.log(productmessage);
                    alert(productmessage.message.message);
                },  
                error : function(productmessage) {  
                    alert(productmessage.message.message);
                }  
            });
        }


        // 隐藏登录框
        function hideImg(){
            $('#loginForm').css('display','none');
        }
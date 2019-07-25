// 修改资料
$(function(){
        if(jQuery.cookie('qixingid')== null || jQuery.cookie('qixingpassword') == null){
            alert('请登陆！');
    }else{
        var name = jQuery.cookie('qixingid');
            $.ajax({
                type: "post",
                url: '/user.json',
                data: $("#loginform").serialize(),
                dataType: 'json',
                async: false,
                success: function(data) {
                    if (data.message.operateCode == 500016) {
                    $('#user-name').text('你好,'+name);
                    $('#user-img').append('<img src="../../../theme/basic/images/touxiang.png" />');
                    $('#user-email').append('<img src="../../../theme/basic/images/user-email.png" />');
                    $("#top-bar ul li").eq(3).empty().html('<a title="" id="profile-messages" class="dropdown-toggle" ><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">管理</span><b class="caret"></b></a><ul class="dropdown-menu" id="dropdown-menu" style="width:100px;"><li><a href="/content/user/20170515102939.shtml"><img src="../../../theme/basic/images/moneygl.png" style="margin-right:15px;position:relative;left:2px;" />账户余额</a></li><li><a href="/content/friend/upload/index.shtml"><img src="../../../theme/basic/images/productgl.png" style="margin-right:13px;width:12px;position:relative;left:1px;"/>我的作品</a></li><li><a href="/content/user/20170515123310.shtml"><img src="../../../theme/basic/images/diamgl.png" style="margin-right:13px;position:relative;left:1px;"/>我的收藏</a></li><li><a href="/content/user/20170515125420.shtml"><img src="../../../theme/basic/images/shopgl.png" style="margin-right:10px;"/>已购教学</a></li><li><a href="/content/user/20170515130841.shtml"><img src="../../../theme/basic/images/giftgl.png" style="margin-right:14px;position:relative;left:2px;"/>VIP订购</a></li><li><a href="/content/user/20170515133906.shtml"><img src="../../../theme/basic/images/messagegl.png" style="margin-right:16px;position:relative;left:3px;"/>站内信息</a></li><li><a href="/content/user/20170515141440.shtml"><img src="../../../theme/basic/images/setgl.png" style="margin-right:12px;position:relative;left:2px;width:13px;"/>修改资料</a></li></ul>');
                    $("#user-register").empty().html('<a href="/user/login.shtml" title="登出" rel="nofollow"><img src="../../../theme/basic/images/login.png" alt="" class="register" style="margin-bottom: 2px;"></i><span class="text">登出</span></a>');


                    $('#user-name').css('display','block');
                    $('#user-img').css('display','block');
                    $('#user-email').css('display','block'); 

                } else {
                        alert(data.message.message);
                    }
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                },
            }, 'json');
        }
})
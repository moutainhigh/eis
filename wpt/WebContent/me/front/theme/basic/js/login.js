
        $(function() {
                //   提交订单商品总计
                var total = 0;
                var allMon = 0;
                $('.money').each(function(){
                    var nub = parseFloat($(this).siblings('li').eq(2).text().substring('1'));
                    var mon = parseInt($(this).siblings('li').eq(3).text());
                    if(nub*mon.toString().indexOf('.') > -1){
                        $(this).text((nub*mon).toFixed(2));
                    }else{
                        $(this).text(nub*mon+'.00');
                    }
                    var explam = $(this).text();
                    // var couponMoney = parseFloat($('#couponMoney').text());
                    // if(explam.indexOf('.00') > -1){
                    //     total += parseFloat(explam);
                    //     // $('#totalMoney').text(total-couponMoney+".00");
                    //     $('#totalMoney').text(total+".00");
                    //     $('#productMoney').text(total+".00");
                    // }else{
                        total += parseFloat(explam);
                        // $('#totalMoney').text(total-couponMoney);
                        $('#totalMoney').text(total.toFixed(2));
                        $('#productMoney').text(total.toFixed(2));
                    // }

                    allMon += mon;
                })
                // 计算总份数
                $('#productMoneyNum').text(allMon);

                // 遍历时限
                var currentTime = new  
                $('.li1 span').each(function(){
                      var month = '';
                      var day = '';
                      var year = '';
                      var val = '';
                    //   var buyTime = $(this).text().substring(0,$('.buyTime').text().indexOf(' '));
                    //   var time = $(this).text().substring($('.buyTime').text().indexOf(' '));
                      var d=new Date();
                      if($(this).text() == 'vipyear'){
                          month=d.getMonth()+1;   
                          day = d.getDate(); 
                          year = d.getFullYear() + 1;
                          val =year+"-"+month+"-"+day;
                          $(this).parent().siblings().eq(2).text(val); 
                      }else if($(this).text() == '月度会员'){
                        //   month=d.getMonth()+2;   
                        //   day = d.getDate(); 
                        //   year = d.getFullYear();
                        //   val =year+"-"+month+"-"+day;
                          var dd = getNowFormatDate();
                          console.log(dd);
                          $(this).parent().siblings().eq(2).text(GetNextMonthDay(dd,1)); 
                      }else if($(this).text() == '季度会员'){
                        //   month=d.getMonth()+4;   
                        //   day = d.getDate(); 
                        //   year = d.getFullYear();
                        //   val =year+"-"+month+"-"+day;
                          var dd = getNowFormatDate();
                          console.log(dd);
                          $(this).parent().siblings().eq(2).text(GetNextMonthDay(dd,4)); 
                      }else{
                          $(this).parent().siblings().eq(2).text('永久观看');
                      }
                })

                // 遍历名称检查是否能勾选vip
                // var productName = '';
                $('.li1').each(function(){
                    if($(this).text().indexOf('季度会员') > -1 || $(this).text().indexOf('月度会员') > -1 || $(this).text().indexOf('vipyear') > -1 ){
                        $('#vipRemove').empty();
                    }else{

                    }
                })

                // 选中时候的价格
                var checkCurrent = 0;
                var currentState = true;
                // 提交订单页面的选择vip商品信息总计
                $('.vip').each(function(a,b){
                    $(this).click(function(){
                        var vipMoney = parseFloat($(this).siblings().val());
                        var currentTotal =parseFloat($('#totalMoney').text());
                        var num = parseFloat($('#productMoneyNum').text());
                        // 判断当前的用户是否是vip
                        if(jQuery.cookie('frontVipLevel') == 2){
                            alert('您已经是vip用户了！');
                            return false;
                        }else{
                            // 当前vip的价格
                            for(var i = 0;i< 2;i++){
                                if($(this).parent().siblings()[i].children[0].checked){
                                        // console.log($(this).parent().siblings()[i].children[1].value);
                                        checkCurrent = parseFloat($(this).parent().siblings()[i].children[1].value);
                                        if(checkCurrent == 0){
                                            // console.log('其他里面没有checked');
                                            // console.log('再次点击消失');
                                        }else{
                                            // console.log('其他里面有checked');
                                            $('#totalMoney').text((currentTotal - checkCurrent + vipMoney).toFixed(2));
                                            $('#productMoney').text((currentTotal - checkCurrent + vipMoney).toFixed(2));
                                            // $('#productMoneyNum').text(num + 1);
                                            $(this).parent().siblings()[i].children[0].checked = false;
                                        }
                                        $(this).attr('checked',true);
                                        return;
                                }else{
                                        if($(this).parent().siblings()[0].children[0].checked == false && $(this).parent().siblings()[1].children[0].checked == true){
                                            // console.log('当第一个没有的时候而第二个有的时候');
                                            continue;
                                        }else if($(this).parent().siblings()[0].children[0].checked == false && $(this).parent().siblings()[1].children[0].checked == false){
                                            // console.log('都没有checked执行');
                                            // console.log(checkCurrent);
                                            // console.log(currentState);
                                            if(currentState == true){
                                                $('#totalMoney').text((currentTotal + vipMoney).toFixed(2));
                                                $('#productMoney').text((currentTotal + vipMoney).toFixed(2));
                                                $('#productMoneyNum').text(num + 1);
                                                $(this).attr('checked',true);
                                                currentState = false;
                                                return;
                                            }else{
                                                $('#totalMoney').text((currentTotal - vipMoney).toFixed(2));
                                                $('#productMoney').text((currentTotal - vipMoney).toFixed(2));
                                                $('#productMoneyNum').text(num - 1);
                                                $(this).attr('checked',false);
                                                currentState = true;
                                                return;
                                            }
                                        }else{
                                            // console.log('其他');
                                        }
                                }
                            }
                        }
                    })
                })

                // 代金劵点击
                $('#isdaijinquan').click(function(){
                    if($(this)[0].checked == true){
                        $('#box_daijinquan').slideDown();
                    }else{
                        $('#box_daijinquan').slideUp();
                    }
                })

                // 开具发票
                $('#isfapiao').click(function(){
                    if($(this)[0].checked == true){
                        $("#box_fapiao").slideDown();
                    }else{
                        $("#box_fapiao").slideUp();
                    }
                })

                // 添加代金券
                $('#addCoupon').click(function(){
                    $('.box_coupon').append('<div class="voucher martop15"><span class="daijinquan">代金券验证码 :</span><input type="text" class="code" id="giftCardNumber" maxlength="19" placeholder="请输入16位数字或大写字母"><input type="submit" value="确认添加" class="confirm_btn"></div>');
                })
  
                // 导航菜单点击隐藏显示
                $('.visible-phone').click(function(){
                    $('.visible-phone').siblings('ul').toggle();
                })

                $('.submenu').each(function(){
                    $(this).click(function(){
                        $(this).find('ul').toggle();
                    })
                })

                $('.addVip').css({padding:0,border:0});

                $('#user-name').css('display','none');
                $('#user-img').css('display','none');
                $('#user-email').css('display','none');

                // 我的作品里的功能
                $(".wpuf__check_pu_8544").click(function(){
                   $("#user-right .p-pu").toggle();
                });


                // 点击录音播放
                var toggleVoice = true;
                $('.app-voice-state-bg').each(function(){
                    $(this).click(function(){
                        $('.audio').each(function(a,b){
                               b.pause();
                               $(this).siblings('div').eq(0).children().removeClass('app-voice-you');
                               $(this).siblings('div').eq(0).children().addClass('app-voice-he');
                        })
                         if(toggleVoice == true ){
                            $(this).siblings('audio')[0].play();
                            toggleVoice = false;
                            $(this).children().removeClass('app-voice-he');
                            $(this).children().addClass('app-voice-you');
                            // 监听音频播放完
                            $(this).siblings('audio').bind('ended',function (){
                                $(this).siblings('div').eq(0).children().removeClass('app-voice-you');
                                $(this).siblings('div').eq(0).children().addClass('app-voice-he');
                            });
                         }else{
                            $(this).siblings('audio')[0].pause();
                            toggleVoice = true;
                            $(this).children().removeClass('app-voice-you');
                            $(this).children().addClass('app-voice-he');
                         }
                    })
                })

                // 录音时间显示
                $('.audio').each(function(){
                    $(this)[0].oncanplay = function(){
                        var time = parseInt($(this)[0].duration);
                        $(this).siblings('div').eq(1).text(time+"'");
                    } 
                })

                if(jQuery.cookie('eis_username') == null){
                     // 评论显示表情
                    $('.text').each(function(i,j){
                        var comments = j.innerText;
                        var comment = '';
                        comments = comments.replace(/:arrow:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />');
                        comments = comments.replace(/:love:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" class="lazy" />');
                        comments = comments.replace(/:!:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif" class="lazy" />');
                        comments = comments.replace(/:question:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" class="lazy" />');
                        comments = comments.replace(/:cool:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" class="lazy" />');
                        comments = comments.replace(/:roll:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif" class="lazy" />');
                        comments = comments.replace(/:eek:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" class="lazy" />');
                        comments = comments.replace(/:evil:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif" class="lazy" />');
                        comments = comments.replace(/:razz:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif" class="lazy" />');
                        comments = comments.replace(/:mrgreen:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif" class="lazy" />');
                        comments = comments.replace(/:smile:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif" class="lazy" />');
                        comments = comments.replace(/:chijing:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif" class="lazy" />');
                        comments = comments.replace(/:lol:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif" class="lazy" />');
                        comments = comments.replace(/:mad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif" class="lazy" />');
                        comments = comments.replace(/:twisted:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif" class="lazy" />');
                        comments = comments.replace(/:wink:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif" class="lazy" />');
                        comments = comments.replace(/:idea:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif" class="lazy" />');
                        comments = comments.replace(/:cry:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif" class="lazy" />');
                        comments = comments.replace(/:shock:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif" class="lazy" />');
                        comments = comments.replace(/:neutral:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif" class="lazy" />');
                        comments = comments.replace(/:sad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif" class="lazy" />');
                        comment = comments.replace(/:confused:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif" class="lazy" />');
                        $(this).empty().append(comment);
                    })
                }else{
                    // var userImg = $('.userImg').val();
                    var userImg = jQuery.cookie('eis_userheadpic');
                    var name = jQuery.cookie('eis_username');
                    $('.myname p').text(name);
                    // $('#user-name').text('你好,'+name);
                    if(userImg == 0){
                        // $('.myname img').attr('src',"../../../theme/basic/images/touxiang.png");
                        $('#user-img').append('<img src="../../../theme/basic/images/touxiang.png" />');
                    }else{ 
                        // $('.myname img').attr('src',userImg);   
                        $('#user-img').append('<img style="width:20px;height:20px;border-radius:10px;" src="/file/client/'+userImg+'" />');
                    }
                    $('#user-email a').append('<img src="../../../theme/basic/images/user-email.png" />');
                    
                    // <li><a href="http://me.mo4u.cn/content/user/20170515102939.shtml"><img src="../../../theme/basic/images/moneygl.png" style="margin-right:15px;position:relative;left:2px;" />账户余额</a></li>
                    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  
                        $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li><li style="float:left;"><a href="/cart.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的购物车</a></li></ul>');  
                    } else if (/(Android)/i.test(navigator.userAgent)) {  
                        $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li><li style="float:left;"><a href="/cart.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的购物车</a></li></ul>');
                    } else {  
                        $("#top-bar ul li").eq(3).empty().html('<a title=""  class="dropdown-toggle" href="javascript:void(0)"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(225,225,225,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="left:180px;background:#F58B3C;height:60px;width:1920px;overflow:hidden;"><li style="float:left;"><a href="/" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">首页</a></li><li style="float:left;"><a href="/user/myUploadProducts.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的作品</a></li><li style="float:left;"><a href="/user/myFavoriteList.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">我的收藏</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=product" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">已购教学</a></li><li style="float:left;"><a href="/user/purchasedList.shtml?flag=vip" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">VIP订购</a></li><li style="float:left;"><a href="/user/bbsInformation.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">站内信息</a></li><li style="float:left;"><a href="/user/modifyPersonalInfo.shtml" style="line-height:54px;color:#ffffff;text-align: center;width: 80px;font-size:16px;">修改资料</a></li></ul>');
                    }
                     // <a title=""  class="dropdown-toggle"><img src="../../../theme/basic/images/eight.png" alt="" class="login" ><span class="text" style="position:relative;bottom:2px;">我的</span><b class="caret"></b></a><div style="height:22px;background:rgba(0,0,0,0);"></div><ul class="dropdown-menu" id="dropdown-menu" style="width:100px;"><li><a href="/content/friend/upload/index.shtml"><img src="../../../theme/basic/images/productgl.png" style="margin-right:13px;width:12px;position:relative;left:1px;"/>我的作品</a></li><li><a href="/content/user/20170515123310.shtml"><img src="../../../theme/basic/images/diamgl.png" style="margin-right:13px;position:relative;left:1px;"/>我的收藏</a></li><li><a href="/content/user/20170515125420.shtml?vPro=purchaseList"><img src="../../../theme/basic/images/shopgl.png" style="margin-right:10px;"/>已购教学</a></li><li><a href="/content/user/20170515130841.shtml?vPro=purchaseList"><img src="../../../theme/basic/images/giftgl.png" style="margin-right:14px;position:relative;left:2px;"/>VIP订购</a></li><li><a href="/content/user/20170515133906.shtml"><img src="../../../theme/basic/images/messagegl.png" style="margin-right:16px;position:relative;left:3px;"/>站内信息</a></li><li><a href="/content/user/20170515141440.shtml"><img src="../../../theme/basic/images/setgl.png" style="margin-right:12px;position:relative;left:2px;width:13px;"/>修改资料</a></li></ul>
                    $("#user-register").empty().html('<a onclick="quit()"><img src="../../../theme/basic/images/login.png" alt="" class="register" style="margin-bottom: 2px;"></i><span class="text">登出</span></a>');

                    // pc端加载完我的后的
                    var toggles = true;
                    $('#menu').mouseenter(function() {
                        if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  
                            $('.dropdown-menu').css({'display':'block','height':'489px'});   
                        } else if (/(Android)/i.test(navigator.userAgent)) {  
                            $('.dropdown-menu').css({'display':'block','height':'489px'});    
                        } else {  
                            $('.dropdown-menu').css({'display':'block','height':'60px'});
                        }; 
                        // if(toggles == true && $(this).attr('class') == 'dropdown'){
                        
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

                    $('#user-name').css('display','block');
                    $('#user-img').css('display','block');
                    $('#user-email').css('display','block'); 


                    $('.reply').empty().html('<a class="comment-reply-link">[回复]</a>');

                    var a = $('#productType').val();
                    $('#respond p').empty().html('<form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input type="button" id="submit" class="submit" value="发表评论" onclick="publishSubmit('+"'"+a+"'"+')"> <input type="hidden" name="objectId" value="1451"  ><input type="hidden" name="objectType"  value="document"></p></form>');
                    
                    // 点击开启回复功能
                    $('.reply').each(function(){
                        $(this).click(function(){
                            var name = jQuery.cookie('eis_username');
                            var people = $(this).siblings('div').find('a').eq(0).text();
                            var commentId = $(this).siblings('input').val();
                            var relatedName = $(this).siblings('div').eq(0).find('a').eq(0).text();
                            if($(this).parent("div").siblings().text() == ''){ 
                                    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  
                                        $('#respond p').empty();
                                        $('#respond').remove();
                                        $(this).parents(".comment").append('<div id="respond" class="comment-respond"><form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input  type="button" id="submit" class="submit" value="发表评论" onclick="authorSubmit('+"'"+people+"'"+','+commentId+','+"'"+relatedName+"'"+','+"'"+a+"'"+')"><input type="hidden" name="objectId" value="1451"  ><input type="hidden" name="objectType"  value="document"></p></form><span class="progress"></span></div>');  

                                        $('#comment').focus(); 
                                    } else if (/(Android)/i.test(navigator.userAgent)) {  
                                        $('#respond p').empty();
                                        $('#respond').remove();
                                        $(this).parents(".comment").append('<div id="respond" class="comment-respond"><form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input  type="button" id="submit" class="submit" value="发表评论" onclick="authorSubmit('+"'"+people+"'"+','+commentId+','+"'"+relatedName+"'"+','+"'"+a+"'"+')"><input type="hidden" name="objectId" value="1451"  ><input type="hidden" name="objectType"  value="document"></p></form><span class="progress"></span></div>');  

                                        $('#comment').focus(); 
                                    } else {  
                                        $('#respond p').empty();
                                        $('#respond').remove();
                                        $(this).parents(".comment").append('<div id="respond" class="comment-respond"><form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input  type="button" id="submit" class="submit" value="发表评论" onclick="authorSubmit('+"'"+people+"'"+','+commentId+','+"'"+relatedName+"'"+','+"'"+a+"'"+')"><input type="hidden" name="objectId" value="1451"  ><input type="hidden" name="objectType"  value="document"></p></form><audio controls autoplay class="audioOnly"></audio><input style="margin-left: 10px;" onclick="startRecording('+"'"+people+"'"+')" type="button" value="录音" /><input onclick="stopRecording('+"'"+people+"'"+')" type="button" value="停止" /><input onclick="playRecording('+"'"+people+"'"+')" type="button" value="播放" /><input onclick="uploadAudio('+commentId+','+"'"+relatedName+"'"+','+"'"+a+"'"+')" type="button" value="提交" /><span class="progress"></span></div>');  

                                        $('#comment').focus(); 
                                    }; 
                            }else{
                                return ;
                            }
                        })
                    })

                    // 删除收藏
                    $('.wpfp-link').each(function(){
                        $(this).click(function(){
                            var userRelationId = $(this).siblings('input').val();
                            $.ajax({
                                type:"POST",
                                url:"/userRelation/delete.json",
                                data:{  
                                    userRelationId:userRelationId
                                },           
                                async:false,
                                success: function(data) {
                                    if(data.message.operateCode == 102008){
                                        alert(data.message.message);
                                        document.location.reload();
                                    }else{
                                        alert(data.message.message);
                                        return false;
                                    }
                                    
                                },
                                error: function(XMLResponse) {
                                    alert("操作失败:" + XMLResponse.responseText);
                                }
                            })
                        })
                    })
                    // 详情页购买
                    $('#dltz').remove();
                    $('#and').remove();
                    
                    // 评论显示表情
                    $('.text').each(function(i,j){
                        var comments = j.innerText;
                        var comment = '';
                        comments = comments.replace(/:arrow:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />');
                        comments = comments.replace(/:love:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" class="lazy" />');
                        comments = comments.replace(/:!:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif" class="lazy" />');
                        comments = comments.replace(/:question:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" class="lazy" />');
                        comments = comments.replace(/:cool:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" class="lazy" />');
                        comments = comments.replace(/:roll:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif" class="lazy" />');
                        comments = comments.replace(/:eek:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" class="lazy" />');
                        comments = comments.replace(/:evil:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif" class="lazy" />');
                        comments = comments.replace(/:razz:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif" class="lazy" />');
                        comments = comments.replace(/:mrgreen:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif" class="lazy" />');
                        comments = comments.replace(/:smile:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif" class="lazy" />');
                        comments = comments.replace(/:chijing:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif" class="lazy" />');
                        comments = comments.replace(/:lol:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif" class="lazy" />');
                        comments = comments.replace(/:mad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif" class="lazy" />');
                        comments = comments.replace(/:twisted:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif" class="lazy" />');
                        comments = comments.replace(/:wink:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif" class="lazy" />');
                        comments = comments.replace(/:idea:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif" class="lazy" />');
                        comments = comments.replace(/:cry:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif" class="lazy" />');
                        comments = comments.replace(/:shock:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif" class="lazy" />');
                        comments = comments.replace(/:neutral:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif" class="lazy" />');
                        comments = comments.replace(/:sad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif" class="lazy" />');
                        comment = comments.replace(/:confused:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif" class="lazy" />');
                        $(this).empty().append(comment);
                    })
                }
                // vip购买页面调整状态和到期时间
                $('.buyTime').each(function(index,f){
                      var month = '';
                      var day = '';
                      var year = '';
                      var val = '';
                      var buyTime = $(this).text().substring(0,$('.buyTime').text().indexOf(' '));
                      var time = $(this).text().substring($('.buyTime').text().indexOf(' '));
                      var d=new Date(buyTime);
                      if($(this).siblings().eq(0).text() == 'vipyear'){
                          month=d.getMonth()+1;   
                          day = d.getDate(); 
                          year = d.getFullYear() + 1;
                          val =year+"-"+month+"-"+day+time;
                          $(this).siblings().eq(2).text(val); 
                      }
                      if($(this).siblings().eq(0).text() == '月度会员'){
                        //   month=d.getMonth()+2;   
                        //   day = d.getDate(); 
                        //   year = d.getFullYear();
                        //   val =year+"-"+month+"-"+day+time;
                          var dd = getNowFormatDate();
                          $(this).siblings().eq(2).text(GetNextMonthDay(dd,1)); 
                      }
                      if($(this).siblings().eq(0).text() == '季度会员'){
                        //   month=d.getMonth()+4;   
                        //   day = d.getDate(); 
                        //   year = d.getFullYear();
                        //   val =year+"-"+month+"-"+day+time;
                          var dd = getNowFormatDate();
                          $(this).siblings().eq(2).text(GetNextMonthDay(dd,4)); 
                      }
                    //   年度会员
                    //   console.log(d.getFullYear())
                    //   季度会员
                    //   console.log(d.getMonth()+4);
                    //   月度会员
                    //   console.log(d.getMonth()+2);
                       if(index == 0){
                            var startTime = $(this).siblings().eq(2).text();
                            var start=new Date(startTime.replace("-", "/").replace("-", "/")); 
                            var date = new Date(); 
                            var seperator1 = "-";
                            var seperator2 = ":";
                            var year = date.getFullYear();
                            var month = date.getMonth() + 1;
                            var strDate = date.getDate();  
                            // var endTime = mydate.toLocaleString();
                            // var end=new Date(endTime.replace("-", "/").replace("-", "/"));
                            if (month >= 1 && month <= 9) {
                                month = "0" + month;
                            }
                            if (strDate >= 0 && strDate <= 9) {
                                strDate = "0" + strDate;
                            }
                            var currentdate = year + seperator1 + month + seperator1 + strDate
                                    + " " + date.getHours() + seperator2 + date.getMinutes()
                                    + seperator2 + date.getSeconds();
                            var end = new Date(currentdate.replace("-", "/").replace("-", "/"));
                            if(end<start){ 
                                $(this).siblings().eq(3).text('未过期');  
                            }   
                       }
                })

                // 判断反馈意见和站内信息用户默认值
                var url=location.search;
                var name =  getQueryString("defaultReceiver"); 
                if(url.indexOf("defaultReceiver") > -1){
                   $('.from form input[name="message_to"]').val(name);
                   $('.from form input[name="message_to"]').attr("disabled",true);
                }else{
                    return;
                }
            })
    

            //将URL中的UTF-8字符串转成中文字符串  
            function getQueryString(name) {   
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
                var l = decodeURI(window.location.search);  
                var r = l.substr(1).match(reg);  
                if (r != null) return unescape(r[2]);  
                return null;  
            } 

            // 算下一个月
            function GetNextMonthDay(date, monthNum)
                {
                    console.log(date)
                    var dateArr = date.split('-');
                    var year = dateArr[0]; //获取当前日期的年份
                    var month = dateArr[1]; //获取当前日期的月份
                    var day = dateArr[2]; //获取当前日期的日
                    var days = new Date(year, month, 0);
                    days = days.getDate(); //获取当前日期中的月的天数
                    var year2 = year;
                    var month2 = parseInt(month) + parseInt(monthNum);
                    if (month2 >12) {
                        year2 = parseInt(year2) + parseInt((parseInt(month2) / 12 == 0 ? 1 : parseInt(month2) / 12));
                        month2 = parseInt(month2) % 12;
                    }
                    var day2 = day;
                    var days2 = new Date(year2, month2, 0);
                    days2 = days2.getDate();
                    if (day2 > days2) {
                        day2 = days2;
                    }
                    if (month2 < 10) {
                        month2 = '0' + month2;
                    }
            
                    var t2 = year2 + '-' + month2 + '-' + day2;
                    return t2;
                }

                // 格式化日期
                function getNowFormatDate() {
                    var date = new Date();
                    var seperator1 = "-";
                    var month = date.getMonth() + 1;
                    var strDate = date.getDate();
                    if (month >= 1 && month <= 9) {
                        month = "0" + month;
                    }
                    if (strDate >= 0 && strDate <= 9) {
                        strDate = "0" + strDate;
                    }
                    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
                    return currentdate;
                } 
                            

      
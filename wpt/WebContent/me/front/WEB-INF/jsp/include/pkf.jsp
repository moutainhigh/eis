        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <style>
            .ptfcart{
                width:280px;
                height: 500px;
                border: 1px solid #F58B3C;
                position: fixed;   
                bottom: 5%;
                right: 0;
                border-right-color: #EDEDED;
                background: #fff;
                display: none;
            }
            .ptfcarts{
                width:41px;
                height: 170px;
                background: #c9c9c9;
                margin-bottom: 5px;
            }
            .ptfcarts p {
                color: #fff;
                width: 5px;
                margin-left: 15px;
            }
            .ptfcarts img {
                width: 20px;
                margin-left: 11px;
                margin-top: 15px;
                margin-bottom: 5px;
            }
            .ptfhead{
                width:100%;
                height: 40px;
                color: #F58B3C;
                margin: 10px;
            }
            .ptfhead p{
                float: left;
            }
            .cartLists{
                width:100%;
                height: 340px;
                padding: 0 10px;
                overflow-x:hidden;
                overflow-y:auto;
                /*display: flex;*/
                white-space: nowrap;
            }
            .ptfcart button{
                width:256px;
                height: 40px;
                background: red;
                color: #fff;
                margin: 45px auto;
                text-align: center;
                line-height: 40px;
                border: 0;
                border-radius: 5px;
                display: block;
                cursor: pointer;
            }
            .ptfImg{
                margin: 0 auto;
                display: block;
                width: 20px;
            }
            .ptfTotal{
                float: left;
                margin-left: 10px;
                margin-top: 10px;
            }
            .usercart{
                margin: 10px 0;
            }
            .ptftitle{
                display: inline-block;
                width: 100px;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
            }
            .usercart input{
                margin-top: -2px;
            }
            .usercart span{
                margin-left: 7px; 
            }
        </style>
               
            <div class="asid_share" id="asid_share" style="right:20px;"  >
                <div class="asid_book">
                        <a href="/content/about/qixingbook.shtml?preview=8BB3964B4484B2DAA8711264649F0F8027088E4DB53F8F436C96E26A24876AF6"><img src="../../../theme/${theme}/images/book.gif" alt="" style="width:100%;height:auto;"></a>  
                </div>
                <div  onmouseenter="mouseOver()" onmouseleave="mouseOut()">
                <!--<div id="mycarts">-->
                    <div class="ptfcarts">
                        <img src="../../../theme/${theme}/images/whitecart.png" alt="">
                        <p>我的购物车</p>
                        <p style="font-weight:bold;height: 18px;background: #fff;width: 18px;border-radius: 50%;margin-top: 5px;color: #c9c9c9;font-size: 12px;text-align: center;line-height: 18px;margin-left: 14px;">0</p>
                    </div>
                    <!--购物车栏目-->
                    <div class="ptfcart">
                        <div class="ptfhead">
                           <p>我的购物车 (<font> 0 </font>)</p>
                           <div style="float: right;margin-right: 10px;">
                               <img data-lazy-src="../../../theme/${theme}/images/ptfleft.png" alt="" style="width:20px;float: left;">
                               <p>1/1</p>
                               <img data-lazy-src="../../../theme/${theme}/images/ptfright.png" alt="" style="width:20px;margin-right: 12px;">
                           </div>    
                        </div>
                        <div class="cartLists">
                            <img data-lazy-src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" class="ptfImg">
                        </div>
                        <p class="ptfTotal">金额总计：<b>0</b></p>
                        <button onclick="submitOrder()">提交订单</button>
                    </div>
                </div>
                <div>
                    <a href="#"><img alt="返回顶部" title="返回顶部" class="adid_icon" src="../../../theme/${theme}/images/top.png"></a>
                </div>
                <div id='kefu' style='margin-top:10px;height:40px;'>
                    <a href="#" class='hovera'>
                        <p>
                            <img alt="客服中心" title="客服中心" class="adid_icon" src="../../../theme/${theme}/images/kefu.png">
                        </p>
                    </a>

                    <div class="asid_sha_layer kefu" id='box'>
                        <div class="asid_share_triangle">
                            <em class="border_sj">&#9670;</em>
                            <span class="con_sj">&#9670;</span>
                        </div>
                        <!--<p>淘宝店铺店长https://amos.alicdn.com/getcid.aw?v=3&groupid=0&s=1&charset=utf-8&uid=%E7%82%AB%E8%93%9D%E7%9A%84%E7%90%B4%E5%BC%A6&site=cntaobao&groupid=0&s=1&fromid=cntaobao
                            <a target="_blank" href="http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&siteid=cntaobao&status=1&charset=utf-8"><img border="0" src="http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&site=cntaobao&s=1&charset=utf-8" alt="" /></a>
                        </p>
                        <p>吉他购买咨询
                            <a target="_blank" href="http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&siteid=cntaobao&status=1&charset=utf-8"><img border="0" src="http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&site=cntaobao&s=1&charset=utf-8" alt="" /></a>
                        </p>
                        <p>网站充值咨询
                            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1837363883&site=qq&menu=yes"><img style="vertical-align:middle;width:77px;height:22px;" border="0" src="http://wpa.qq.com/pa?p=2:426900148:41" alt="" title=""></a>
                        </p>
                        <p>付费教学咨询
                            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=426900148&site=qq&menu=yes"><img style="vertical-align:middle;width:77px;height:22px;" border="0" src="http://wpa.qq.com/pa?p=2:426900148:41" alt="" title=""></a>
                        </p>-->
                        <p>在线咨询
                            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3268551923&site=qq&menu=yes"><img style="vertical-align:middle;width:77px;height:22px;" border="0" src="http://wpa.qq.com/pa?p=2:1664083554:41" alt="" title=""></a>
                        </p>
                    </div>
                </div>
            </div>

            <script src='../../../theme/${theme}/js/EasyLazyload.min.js'></script>
            <script>
                 function mouseOver(){
                    // $('#mycarts').mouseenter(function(e){
                        // $('.ptfImg').css('display','none')
                        // $('.cartLists p').empty();
                        $('.ptfcart').show(0,function(){
                            $('#asid_share').css('right','282px');
                            $.ajax({
                                type:"GET",
                                url: '/cart.json',
                                data:{},
                                dataType:'json',
                                success:function (data) {
                                    //    if(data.message.operateCode == 500031){
                                    //        $('.ptfcart ul').empty();
                                    //        $('.ptfcart ul').append('<li>您还未登录！</li>');
                                    //    }else{
                                            $('.ptfImg').css('display','none')
                                            var itemMapArray=[];
                                            for(var j in data.itemMap){
                                                itemMapArray.push(j);
                                            }
                                            $('.ptfhead p font').text( itemMapArray.length );
                                            if(itemMapArray.length == 0){
                                                $('.cartLists').append('<p>您还没加入商品到购物车！</p>');
                                            }else{
                                                for(var i=0;i<itemMapArray.length;i++){
                                                    var list = '<p class="usercart">';
                                                    list += '<input type="checkbox" name="tids" checked="checked" value="'+data.itemMap[itemMapArray[i]].transactionId+'">';
                                                    list += '<input type="hidden" name="cartsId" value="'+data.itemMap[itemMapArray[i]].cartId+'">';
                                                    list += '<span class="ptftitle" style="position:relative;top:3px;">'+data.itemMap[itemMapArray[i]].name+'</span>';
                                                    list += '<span>数量:'+data.itemMap[itemMapArray[i]].count+'</span>';
                                                    list += '<span>￥'+data.itemMap[itemMapArray[i]].price.money+'</span>';
                                                    list += '<span class="ptfdetele" style="cursor:pointer; color:#F58B3C;">删除</span>'
                                                    list += '</p>';
                                                    $('.cartLists').append(list);
                                                }
                                            }
                                        
                                        // 算出总价格
                                        var totalMoney = 0;
                                        $('.usercart').each(function(){
                                            totalMoney += parseFloat($(this).children('span').eq(1).text().substring(3))*parseFloat($(this).children('span').eq(2).text().substring(1))
                                        })
                                        if($('.cartLists').text() == '您还没加入商品到购物车！'){
                                            $('.ptfTotal b').text('0');
                                        }else{
                                            $('.ptfTotal b').text(totalMoney.toFixed(2));
                                        }

                                        // 点击选择商品变换价格
                                        $('input[name="tids"]').each(function (i, d) {
                                            $(this).click(function(){
                                              if($(this).is(':checked')){
                                                totalMoney = totalMoney + parseFloat($(this).siblings('span').eq(1).text().substring(3))*parseFloat($(this).siblings('span').eq(2).text().substring(1));
                                                $('.ptfTotal b').text(totalMoney.toFixed(2));
                                              }else{
                                                totalMoney = totalMoney-parseFloat($(this).siblings('span').eq(1).text().substring(3))*parseFloat($(this).siblings('span').eq(2).text().substring(1));
                                                $('.ptfTotal b').text(totalMoney.toFixed(2));
                                              }
                                            })
                                        })

                                            //  删除按钮
                                            $('.ptfdetele').each(function(){
                                                $(this).click(function(){
                                                    var strIds = $(this).siblings('input[name="tids"]').val();
                                                    var that = $(this);
                                                    if(confirm("是否要删除该商品")){
                                                        totalMoney = totalMoney - parseFloat($(this).siblings('span').eq(1).text().substring(3))*parseFloat($(this).siblings('span').eq(2).text().substring(1));
                                                        $.ajax({
                                                            type:"POST",
                                                            url:'/cart/delete.json',
                                                            data:{
                                                                transactionIds:strIds
                                                            },
                                                            dataType:'json',
                                                            success:function (data) {
                                                                // window.location.reload();
                                                                // 左侧购物车减1
                                                                // var num = $('.ptfcarts p').eq(1).text();
                                                                // $('.ptfcarts p').eq(1).text(parseFloat(num) - 1);
                                                                // 总价格减去当前删除的价格
                                                                $('.ptfTotal b').text(totalMoney);
                                                                // 减去商品数
                                                                $('.ptfcarts p').eq(1).text( itemMapArray.length-1 );
                                                                $('.ptfhead p font').text( itemMapArray.length-1 );
                                                                if($('.ptfcarts p').eq(1).text() == 0){
                                                                    that.parent('p').empty().append('<p>您还没加入商品到购物车！</p>');
                                                                }else{
                                                                    that.parent('p').remove();
                                                                }
                                                            },
                                                            error:function (data) {
                                                                alert("系统繁忙,请稍后再试");
                                                                return false;
                                                            }
                                                        }); 
                                                    }
                                                })
                                            })
                                    }
                            })
                        });
                    // })
                 }

                 function mouseOut(){  
                    // $('#mycarts').mouseleave(function(){
                        $('.ptfcart').hide(0,function(){
                            $('#asid_share').css('right','20px');
                        });

                        $('.cartLists p').remove();
                        $('.ptfImg').css('display','block');
                    // })
                 }
        

                    //    提交订单按钮
                   function submitOrder(){
                       if($('#profile-messages').text().indexOf('登录')>-1){
                            alert("请登录！");
                        }else if($('input[name="tids"]:checked').length != 0){
                                var a ="";
                                $('input[name="tids"]:checked').each(function (i, d) {
                                    if (d.checked) {
                                        a+='tid='+d.value+'&';
                                    }
                                });
                                a=a.substring(0,a.length-1);
                                var cartId = $('input[name="cartsId"]').val();
                                location.href='/buy/delivery.shtml?'+a+"&orderId="+cartId;
                            }else{
                                alert('您还未选择商品！')
                        }
                   }

                    // pc客服悬停出现
                    $('#kefu').mouseover(function() {
                        $('#box').css('display', 'block');
                        $('#kefu a').first().find('img').remove();
                        $('#kefu a').first().html('<p style="text-align:center;width:40px;height:38px;background:#C9C9C9;color:#ffffff;">客服中心</p>');

                    })

                    $('#kefu').mouseout(function() {
                        $('#box').css('display', 'none');
                        $('#kefu a').first().find('span').remove();
                        $('#kefu a').first().html('<img alt="客服中心" tilt="客服中心" class="adid_icon" src="../../../theme/basic/images/kefu.png">');
                    })

                    // 图片懒加载   
                    lazyLoadInit();   
            </script>
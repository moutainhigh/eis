
                    // 购物车商品数量
                      var number = $('#rongQi li').length;
                      $('.po_ab').text(number);

                    // 小计
                    $('#rongQi li .sumtotal').each(function(){
                        var nub = parseInt($(this).parent('div').siblings().find('span').text().substring('3'));
                        var mon = parseFloat($(this).parents('li').find('p').eq(1).text().substring('1'));
                        if(nub*mon.toString().indexOf('.') > -1){
                            $(this).text(nub*mon);
                        }else{
                            $(this).text(nub*mon+'.00');
                        }
                    })


                    //  购物车勾选商品
                      $('.choose').each(function(){
                          $(this).click(function(){
                              var productNum = $(this).siblings('div').find('div[class="spinner"] span').text().substring(3);
                              var money = parseFloat($("#zong1").text());
                              var indexs = parseFloat($('.box_container_left span').text());
                              if($(this).is(':checked')){
                                indexs++;
                                var sumtotal = parseFloat($(this).siblings('p').eq(1).text().substring(1));
                                sumtotal = sumtotal*productNum;
                                money = (money + sumtotal).toFixed(2);
                                $('.box_container_left span').text(indexs);
                                if(money.toString().indexOf('.')>-1){
                                    $('#zong1').text(money);
                                }else{
                                    $('#zong1').text(money+".00");
                                }
                                $('#balance').css({"background-color":""});
                              }else{ 
                                indexs--;
                                var sumtotal = parseFloat($(this).siblings('p').eq(1).text().substring(1));
                                sumtotal = sumtotal*productNum;
                                money = (money - sumtotal).toFixed(2);
                                $('.box_container_left span').text(indexs);
                                if(money.toString().indexOf('.')>-1){
                                    $('#zong1').text(money);
                                }else{
                                    $('#zong1').text(money+".00");
                                }
                                $('.quanxuan')[0].checked = false;
                                if($('.box_container_left span').text() == 0){
                                    $('#balance').css({"background-color":"rgb(221, 221, 221)"});
                                }
                              }
                          })
                      })

                    //    购物车全选
                    $('#quanxuan').click(function(){
                            if($('input[name="tid"]:checked').length==$('input[name="tid"]').length){
                                var num = 0;
                                if($('.quanxuan').is(":checked")){
                                    $('.choose').each(function(index){
                                        var productNum = $(this).siblings('div').find('div[class="spinner"] span').text().substring(3);
                                        $('.choose')[index].checked = true;
                                        var sumtotal = parseFloat($(this).siblings('p').eq(1).text().substring(1));
                                        num += sumtotal*productNum;
                                        $('.box_container_left span').text($('.choose').length);
                                        if(num.toString().indexOf('.')>-1){
                                            $('#zong1').text(num);
                                        }else{
                                            $('#zong1').text(num+".00");
                                        }
                                        $('#balance').css({"background-color":""});
                                    }) 
                                }else{
                                    $('.choose').each(function(index){
                                        $('.choose')[index].checked = false;
                                        $('.box_container_left span').text('0');
                                        $('#zong1').text('0'+".00");
                                        $('#balance').css({"background-color":"rgb(221, 221, 221)"});
                                    })
                                }
                            }else{
                                var num = 0;
                                $('.choose').each(function(index){
                                    var productNum = $(this).siblings('div').find('div[class="spinner"] span').text().substring(3);
                                    $('.choose')[index].checked = true;
                                    num +=parseFloat($(this).siblings('p').eq(1).text().substring(1))*productNum;
                                    $('.box_container_left span').text($('.choose').length);
                                    if(num.toString().indexOf('.')>-1){
                                            $('#zong1').text(num);
                                        }else{
                                            $('#zong1').text(num+".00");
                                        }
                                    $('#balance').css({"background-color":""});
                                })
                                
                            }
                    })

                    //    结算按钮
                    $("#balance").click(function() {
                        if($('#profile-messages').text().indexOf('登录')>-1){
                            alert("请登录！");
                        }else if($('input[name="tid"]:checked').length != 0){
                                var a ="";
                                $('input[name="tid"]:checked').each(function (i, d) {
                                    if (d.checked) {
                                        a+='tid='+d.value+'&';
                                    }
                                });
                                a=a.substring(0,a.length-1);
                                var cartId = $('.cartId').val();
                                location.href='/buy/delivery.shtml?'+a+"&orderId="+cartId;

                            }else{
                                alert('您还未选择商品！')
                            }
                        })

                //    删除按钮
                    $("#delgwc").click(function() {
                            //var s='';
                            var strIds = "";//声明一个存放id的数组
                            $('input[name="tid"]:checked').each(function (i, d) {
                                if (d.checked) {
                                    //strIds.push(d.value);
                                    var str=","+d.value;
                                    strIds+=str;
                                }
                                //s+=$(this).val()+',';
                            });
                            if(strIds.length<1){
                                alert("请选择要删除的商品");
                                return;
                            }
                            if(confirm("是否要删除该商品")){
                                strIds=strIds.substr(1);
                                $.ajax({
                                    type:"POST",
                                    url:'/cart/delete.json',
                                    data:{
                                        transactionIds:strIds
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        window.location.reload();

                                    },
                                    error:function (data) {
                                        alert("系统繁忙,请稍后再试");
                                        return false;
                                    }
                                }); 
                            }
                        });
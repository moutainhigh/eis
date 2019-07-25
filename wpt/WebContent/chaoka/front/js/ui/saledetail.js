//消费购买详细页
//ajax多次调用函数
var tryTime = 1;
function reajax(orderId) {

    $.ajax({
        type:"GET",
         url:userHost + '/sale/manual/query/'+orderId+'.json',

        dataType:'json',

        success:function (data) {

            if (data.message.operateCode == transSuccess) {

                //jAlert('交易成功', '提示信息');


                jAlert('交易成功!', '提示信息', function (r) {

                    hideloading('buyForm');
                    tryTime = 1;

                });

                return false;

            } else {
                if (tryTime > 8) {

                    jAlert('系统正在为您处理中,请稍后查询!', '提示信息', function (r) {

                        hideloading('buyForm');

                        tryTime = 1;

                    });
                    return false;

                }

                window.setTimeout(function () {
                    reajax(orderId);
                }, tryTime * 1000);

                tryTime = ++tryTime;

            }

        },
        error:function (data) {

            jAlert("系统繁忙,请稍后再试", '提示信息');
            return false;
        }
    });

}

function productSale(productCode) {


 if (!$("#minBuyMoney").val()) {
        jAlert('最低出售价格！', '提示信息');

        return false;
    }
    if (!$("#productSerialNumber").val()) {
        jAlert('请输入卡号！', '提示信息');

        return false;
    }
      if (!$("#productPassword").val()) {
        jAlert('请输入密码！', '提示信息');

        return false;
    }
	

    $.ajax({
        type:"GET",
        url:userHost + '/user.json',
        dataType:'json',
        async:false,
        success:function (data) {

            if (data.frontUser == "" || data.frontUser == null) {

                Boxy.load(userHost + "/content/user/loginMini.shtml", //确认提示信息

                    {
                        title:"请先登录",
                        closeText:"关闭",
                        draggable:true,
                        afterShow:function (e) {
                            $('#Useral input[id=username]').focus();
                        },
                        afterHide:function (e) {
                            location.reload();
                        },
                        modal:true
                    });

            } else {

                jConfirm('<div>你确定卖掉产品' + $(".productwelcome").text() + '吗?</div>',
                    '确认对话框', function (r) {

                        if (r == true) {

                            $.ajax({
                                type:"POST",
                                url:userHost + '/sale/manual/submit.json',
                                data:{

                                    productCode:productCode,
                                    buyMoney:$("#buyMoney").val(),
                                    minBuyMoney:$("#minBuyMoney").val(),
									 productSerialNumber:$("#productSerialNumber").val(),
                                    productPassword:$("#productPassword").val()

                                },
                                dataType:'json',

                                success:function (data) {

                                    var orderId = data.message.content;

                                    //账户资金不足的情况下
                                    if (data.message.operateCode == cardPasswordError) {

                                        jAlert(data.message, '提示信息');

                                        return false;

                                    }
                                    if (data.message.operateCode == operateResultAccept) {

                                        //正在处理中
                                        // $(".ol_loading_mask null_mask").remove();

                                        $.ajax({
                                            type:"GET",
                                            url:userHost + '/sale/manual/query/'+orderId+'.json',

                                            dataType:'json',

                                            success:function (data) {

                                                if (data.message.operateCode == transSuccess) {

                                                    jAlert('交易成功!', '提示信息', function (r) {

                                                        hideloading('buyForm');
                                                        tryTime = 1;

                                                    });

                                                    return false;


                                                }
                                                else {

                                                    showloading('buyForm');
                                                    reajax(orderId);
                                                }

                                            },
                                            error:function (data) {

                                                jAlert("系统繁忙,请稍后再试", '提示信息');
                                                return false;
                                            }
                                        });

                                    }}
                                    ,
                                    error : function (data) {

                                        jAlert("系统繁忙,请稍后再试", '提示信息');
                                        return false;
                                    }
                                })

                        }


                    })
            }

            },


                error: function (data) {

                    jAlert("系统繁忙,请稍后再试", '提示信息');
                    return false;
                }
            }
            )
            ;

        }
 function selectCity(data)
    {

        var option2 = '';
        var selectedIndex = $("#productRegion :selected").text();

        $("#productServer").empty();

        for (var i = 0; i < data.length; i++) {

            if (data[i].regionName == selectedIndex) {
                if (data[i].serverList != null) {


                    for (var j = 0; j < data[i].serverList.length; j++) {


                        $("#productServer_ui").show();
                        if ($("#productRegion :selected").attr("value") == -1) {

                            $("#productServer_ui").hide();
                        }
                        var serverNames = data[i].serverList[j].serverName;
                        var serverIDs = data[i].serverList[j].serverId;
                        option2 += "<option value=" + serverIDs + ">"
                            + serverNames + "</option>";

                    }

                } else {

                    $("#productServer_ui").hide();

                }

            }

        }

        $("#productServer").append(option2);

    }

  $(document).ready(function () {

      $("#productRegion").change(function(){

           if($("#productRegion").val()==0){


               jAlert("请选择充值区域", '提示信息');
               $(".serverChoose").hide();
               return false;



           }
          $productServerList = $("#productServerListJson").html(); 

          var jsonData = eval("(" + $productServerList + ")");

          var option1 = '';

          $.each(jsonData, function (index, indexItems) {

              if($("#productRegion").val()==indexItems.regionId){

                  option1 += "<option value=" + indexItems.productServerId + ">"
                      + indexItems.serverName + "</option>";

              }


          });

          if(option1!=null&&option1!=""){

              $(".serverChoose").show();

          }else{

              $(".serverChoose").hide();
          }

          $("#productServer option").remove();

          $("#productServer").append(option1);

      });

        //可购买数量等于-1
        $sl = $("#shuliang").text();
        if ($sl == '-1') {

            $("#shuliang").text("无限制");
        }




        //读取用户资金信息
        $.ajax({
            type:"GET",
            url:userHost + '/user/money.json',

            dataType:'json',

            success:function (data) {
            },
            error:function (data) {

                jAlert("系统繁忙,请稍后再试", '提示信息');
                return false;
            }
        });
        $.ajax({
            type:"GET",
            url:userHost + '/user.json',
            async:false,
            dataType:'json',

            success:function (data) {

                if (data.frontUser == null || data.frontUser == undefined) { //成功
                    $('#no_login').show();
                    $('#loginres').hide();
                } else {

                    $('#no_login').hide();
                    $('#loginres').show();

                    var html = '';
                    var htmlzh = '';
                    //账户首页
                    if (data.money == null || data.money == undefined) {

                        $('#chargeMoney').text("0");

                    } else {

                        $('#chargeMoney').text(data.money.chargeMoney);
                        $('#incomingMoney').text(data.money.incomingMoney);
                        $('#frozenMoney').text(data.money.frozenMoney);
                        $('#marginMoney').text(data.money.marginMoney);
                        $('#coin').text(data.money.coin);
                        $('#point').text(data.money.point);

                    }
                    if (data.frontUser.nickName != null || data.frontUser.nickName != undefined) {
                        htmlzh += '欢迎回来,' + data.frontUser.nickName;
                    } else {
                        htmlzh += '欢迎回来,' + data.frontUser.username;
                    }
                    $('#usernamezh').text(htmlzh);
                    if (data.frontUser.nickName != null) {
                        html += '您好,' + data.frontUser.nickName;
                    } else {
                        html += '您好,' + data.frontUser.username;
                    }
                    $('#info').text(html);

                }
                //编辑用户资料


            },
            error:function (data) {

                jAlert("系统繁忙,请稍后再试", '提示信息');
                return false;
            }


    });
  });
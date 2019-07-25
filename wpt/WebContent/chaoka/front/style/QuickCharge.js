var ajaxCallback = function(params, fun) {
    $.ajax({
        url: "Service/AjaxProductList.aspx",
        data: params,
        dataType: "json",
        cache: false,
        success: function(data) {
            fun(data);
        }
    })
};

//手机快充处理部分开始
$(document).ready(function() {

    var mobileTemp = "";

    //获取面值列表
    var GetParPriceList = function() {
        var mobile = GetRealMobile($("#txtMobile").val());
        $.ajax({
            url: "Service/AjaxProductList.aspx",
            cache: false,
            dataType: "json",
            data: {
                doType: "mobile",
                mobile: mobile,
                chargeType: 1  //充值类型，1=15分钟快充,2=48小时慢充,3=96小时慢充
            },
            success: function(data) {
                if (data.HasError) {
                    alert(data.Message);
                    return;
                }
                var $MobileParPriceList = $("#MobileParPriceList").empty();
                $("#MobileSupplierName").text(data.DicData.mobileSupplier);
                var $mobileParPrice = $("<select name='mobileParPrice' id='mobileParPrice'></select>").appendTo($MobileParPriceList);
                $.each(data.Data, function(entryIndex, entry) {
                    $mobileParPrice.append("<option value='" + entry + "'>" + entry + "元</option>");
                })
                $mobileParPrice.val(100);

            }
        });
    }

    $("#txtMobile").blur(function() {

        var mobile = GetRealMobile($(this).val());

        //客户端检查号码
        if (mobile.length > 0 && mobile.length < 11) {
            alert("输入的号码不合法");
            return;
        }

        $("#MobileSupplierName").text(GetMobileSupplierName(mobile));
        //        if (mobile != mobileTemp) {
        //            GetParPriceList();
        //        }

        mobile = mobile.substring(0, 3) + " " + mobile.substring(3, 7) + " " + mobile.substring(7, 11);
        $(this).val(mobile);
    }).focus(function() {
        var mobile = GetRealMobile($(this).val());
        $(this).val(mobile);
        mobileTemp = mobile;
    });

    $("#btnMobileCharge").click(function() {
        var mobile = GetRealMobile($("#txtMobile").val());
        var parPrice = $("#cboMobileParPrice").val();

        if (mobile.length > 0 && mobile.length < 11) {
            alert("输入的号码不合法");
            return;
        }

        window.open("Charge/PhoneCharge.aspx?phone=" + mobile + "&parPrice=" + parPrice);
    });
});

function GetMobileSupplierName(mobile) {

    var supplier = mobile.substr(0, 3);

    var arrYD = [134, 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 182, 187, 188];
    var arrLT = [130, 131, 132, 155, 156, 185, 186];
    var arrDX = [133, 153, 180, 189];

    for (var i = 0; i < arrYD.length; i++) {
        if (supplier == arrYD[i]) {
            return "中国移动";
        }
    }

    for (var j = 0; j < arrLT.length; j++) {
        if (supplier == arrLT[j]) {
            return "中国联通";
        }
    }

    for (var k = 0; k < arrDX.length; k++) {
        if (supplier == arrDX[k]) {
            return "中国电信";
        }
    }

    return "未知";
}

function GetRealMobile(mobile) {
    var reg = new RegExp(" ", "g");
    mobile = mobile.replace(reg, "");
    return mobile;
}
//手机快充处理部分结束

//以下为QQ及游戏快充处理部分
$(document).ready(function() {

    //QQ产品提交按钮
    $("#btnQQSubmit").click(function() {
        var qQNum = $("#txtQQNum").val();
        var qQProduct = $("#cboQQList").val();
        var qQPrice = $("#cboQQPriceList").val();
        if (qQNum == "") {
            alert("请填写QQ号码");
            return false;
        }
        if (qQProduct == "") {
            alert("请选择QQ产品类型");
            return false;
        }
        if (qQPrice == "") {
            alert("请选择产品面值");
            return false;
        }
        if (qQProduct == "qb") {
            window.open("Charge/QBCharge.aspx?qq=" + qQNum + "&parPrice=" + qQPrice);
        }
        else {
            window.open("Charge/QQCzTransfer.aspx?type=quick&qq=" + qQNum + "&code=" + qQProduct + "&parPrice=" + qQPrice);
        }
    });
    //游戏快充提交按钮
    $("#btnGameSubmit").click(function() {
        var product = $("#prices").val();
        var gameid = $("#cboGameList").val();
        var category = product.substr(0, 10);
        if (product == "") {
            alert("选择游戏产品");
            return false;
        }
        var prodType = $("#prodTypes").find(":radio:checked").val();
        if (prodType == "1") {
            window.open("Fetch/UCardFetchCard.aspx?gameid=" + gameid + "&category=" + category + "&product=" + product);
        }
        else if (prodType == "3") {
            window.open("Charge/UCardDirectCharge.aspx?gameid=" + gameid + "&category=" + category + "&product=" + product);
        }
        else if (prodType == "5") {
            window.open("Charge/UCardManualCharge.aspx?gameid=" + gameid + "&category=" + category + "&product=" + product);
        }
        else {
            alert("未知的充值类型");
            return false;
        }
    });
});

var mQQListJson = null;
var mGameListJson = null;

function SetQuickBuyDropDownList() {
    var isGetFromJsFile = true; //AJAX服务或JS文件数据来源开关
    if ($("#cboQQList").find("option").length > 1 || $("#cboGameList").find("option").length > 1) {
        return;
    }
    if (mQQListJson == null || mGameListJson == null) {
        if (isGetFromJsFile) {
            $.getScript("JS/ProductData.js", function() {
                mQQListJson = mQQListAjax;
                mGameListJson = mGameListAjax;
                SetQQDropDownList();
                SetGameDropDownList();
            });
        }
        else {
            ajaxCallback("doType=qq", GetQQListData);
            ajaxCallback("doType=gamelist", GetGameListData);
        }
    }
    else {
        SetQQDropDownList();
        SetGameDropDownList();
    }
}

function GetQQListData(data) {
    mQQListJson = data.ComData.items;
    SetQQDropDownList();
}

function SetQQDropDownList() {
    var $cboQQList = $("#cboQQList");
    $.each(mQQListJson, function(entryIndex, entry) {
        $cboQQList.append("<option value='" + entry["code"] + "'>" + entry["name"] + "</option>");
    })
    $cboQQList.change(function() {
        var curCode = $(this).val();
        if (curCode != "") {
            var $cboQQPriceList = $("#cboQQPriceList").empty().append("<option value=''>请选择面值</option>");
            $.each(mQQListJson, function(entryIndex, entry) {
                if (entry["code"] == curCode) {
                    var arrPrices = entry["prices"].split(",");
                    var arrUnit = entry["unit"].split(",");
                    for (var i = 0; i < arrPrices.length; i++) {
                        var unit = arrPrices[i] * arrUnit[0];
                        unit = unit == 0 ? "" : unit;
                        $cboQQPriceList.append("<option value='" + arrPrices[i] + "'>" + arrPrices[i] + "元 " + unit + arrUnit[1] + "</option>");
                    }
                }
            })
        }
    });
}

function GetGameListData(data) {
    mGameListJson = data.ComData.list;
    SetGameDropDownList();
}

function SetGameDropDownList() {
    var $cboGameList = $("#cboGameList").empty().append("<option value=''>-请选择游戏-</option>");
    $.each(mGameListJson, function(entryIndex, entry) {
        $cboGameList.append("<option value=\"" + entry["id"] + "\">" + entry["letters"].toUpperCase() + "-" + entry["name"] + "</option>");
    });
    $cboGameList.change(function() {
        ajaxCallback("doType=chargeconfig&gameID=" + $(this).val(), getGameChargeConfig);
    });
}

//获取某游戏配置信息
var gameChargeData;

function getGameChargeConfig(data) {
    if (data.HasError) {
        //alert(data.Message); //不用显示提示信息
        return;
    }
    gameChargeData = data.ComData;
    var $prodTypes = $("#prodTypes").empty();
    var prodTypesCount = 0;
    $("#prices").empty().append("<option value=''>-请选择-</option>");
    if (gameChargeData.prodTypes.cardPasswordProdCount != 0) {
        $prodTypes.append("<input type='radio' onclick='setProdList(1)' value='1' name='prodTypes'  id='prodType1'><label for='prodType1'>卡密</label>");
        prodTypesCount++;
    }
    if (gameChargeData.prodTypes.directChargeProdCount != 0) {
        $prodTypes.append("<input type='radio' onclick='setProdList(3)' value='3' name='prodTypes'  id='prodType3'><label for='prodType3'>直充</label>");
        prodTypesCount++;
    }
    if (gameChargeData.prodTypes.manualChargeProdCount != 0) {
        $prodTypes.append("<input type='radio' onclick='setProdList(5)' value='5' name='prodTypes'  id='prodType5'><label for='prodType5'>代充</label>");
        prodTypesCount++;
    }
    if (prodTypesCount <= 1) {
        $prodTypes.find("input").click().attr("visible", "false");
    }
}

//设置产品下拉框
function setProdList(prodType) {
    var $prices = $("#prices").empty().append("<option value=''>-请选择-</option>");
    $.each(gameChargeData.prices, function(entryIndex, entry) {
        if (entry["type"] == prodType) {
            $prices.append("<option price=" + entry["price"] + " jpoint=" + entry["jpoint"] + " value='" + entry["code"] + "'>" + entry["name"] + "</option>");
        }
    })
}
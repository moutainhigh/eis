
var baseURL ="https://www.maicard.com/";


var mProductListJson = null;

function SetLeftTreeList(obj) {
    if (mProductListJson == null) {
        GetProductListData(obj, "left");
        return;
    }
    BindLeftTreeListData(obj);
}

function GetProductListData(obj, position) {

    var isGetFromJsFile = true; //AJAX服务或JS文件数据来源开关
	

    if (isGetFromJsFile){
    
          if(position == "letter") {
                BindIndexLettersListData(obj);
            }
       
    }else {
        $.ajax({
            url: baseURL + "Service/AjaxProductList.aspx",
            cache: false,
            dataType: "json",
            data: "doType=product",
            async: true,
            success: function(data) {
                mProductListJson = data.ComData;
                if (position == "left") {
                    BindLeftTreeListData(obj);
                }
                else if (position == "letter") {
					
                    BindIndexLettersListData(obj);
                }
            }
        });
    }
}

function BindLeftTreeListData(obj) {
    var $curUl = $(obj).find("ul").empty();
    var curType = $(obj).attr("type");
    $.each(mProductListJson, function(entryIndex, entry) {
        if (curType == entry["t"]) {
            $curUl.append(getPayUrl(entry["h"], entry["r"], entry["i"], entry["n"], entry["p"]));
        }
    });
	
    $(obj).addClass("selected");
    $(obj).children("a:eq(0)").addClass("h2-tit");
    $(obj).children("ul").show();
    $(obj).siblings().removeClass("selected");
    $(obj).siblings().children("a:eq(0)").removeClass("h2-tit");
    $(obj).siblings().children("ul").hide();
}

function SetIndexLettersList(obj) {

    if (mProductListJson == null) {
        GetProductListData(obj, "letter");
        return;
    }
    BindIndexLettersListData(obj);
}
function BindIndexLettersListData(obj) {
	

    var curLetter = $(obj).find("span").text();
	//alert(curLetter);

    var $gameList = $(obj).find("ul");
	
    var count = 0;
  //  $.each(mProductListJson, function(entryIndex, entry) {
//        if (curLetter == entry["l"].toUpperCase()) {
//            $gameList.append(getPayUrl(entry["h"], entry["r"], entry["i"], entry["n"], entry["p"]));//读取数据孙
//            count++;
//        }
//    });
   // if (count == 0) {
//        $gameList.append("<li>该字母下暂无游戏</li>");
//    }
    $gameList.show();
}

function getPayUrl(isHot, payType, categoryCode, categoryName, prompt) {

    var hot = "<img src='" + baseURL + "Images/Hot.gif' />";
    if (isHot == "0") {
        hot = "";
    }

    var reDirUrl = "";
    var target = "target='_blank'";
    if (payType == "0") {//充值
        reDirUrl = "Charge/UCardDirectCharge.aspx?category=" + categoryCode;
    }
    else if (payType == "1") {//卡密
        reDirUrl = "Fetch/UCardFetchCard.aspx?category=" + categoryCode;
    }
    else {//卡密+充值
        reDirUrl = "Product/ProductList.aspx?con=key&mode=and&value=" + categoryCode;
        target = "";
    }

    return "<li onclick='hideMyParent(this)'><a " + target + " href=\"" + baseURL + reDirUrl + "\" title=\"" + prompt + "\" >" + categoryName + "</a>" + hot + "</li>";
}

function hideMyParent(obj) {
    $(obj).parent("ul").hide();
}
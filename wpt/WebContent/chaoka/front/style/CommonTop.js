$(document).ready(function() {
    $('#FloatTop_Menu li span').parent().hover(function() {
        $(this).addClass('hover').find('div').show();
    },
             function() {
                 $(this).removeClass('hover').find('div').hide();
             });

    $('#backSiteURL').hover(function() {
        $(this).addClass('hover').find('ul').show();
    },
             function() {
                 $(this).removeClass('hover').find('div').hide();
             });

    //$("#FloatTop").floatdiv("middletop");
});

function addToFavorite() {
    var d = "http://www.jcard.cn/";
    var c = "汇元网-骏卡-数字产品通用充值卡";
    if (document.all) {
        window.external.AddFavorite(d, c);
    }
    else {
        if (window.sidebar) {
            window.sidebar.addPanel(c, d, "");
        }
        else {
            alert("对不起，您的浏览器不支持此操作!\n请您使用菜单栏或Ctrl+D收藏本站。");
        }
    }
}
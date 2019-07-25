function CreateLoginForm(successFun, closeFun) {
    var formContent = "";
    formContent += "<div id='login' style='display:none;'>";
    formContent += "    <table>";
    formContent += "       <thead>";
    formContent += "            <tr>";
    formContent += "                <th colspan='2' id='errInfo'>";
    formContent += "                </th>";
    formContent += "            </tr>";
    formContent += "        </thead>";
    formContent += "        <tbody>";
    formContent += "            <tr>";
    formContent += "                <th>";
    formContent += "                    用户名：";
    formContent += "                </th>";
    formContent += "                <td>";
    formContent += "                    <input id='txtUserName' type='text' class='chanpininput' onkeydown='javascript:return TradeLoginEnter(event);' />";
    formContent += "                </td>";
    formContent += "            </tr>";
    formContent += "            <tr>";
    formContent += "                <th>";
    formContent += "                    密&nbsp;&nbsp;码：";
    formContent += "                </th>";
    formContent += "                <td>";
    formContent += "                    <input id='txtPassword' type='password' class='chanpininput' onkeydown='javascript:return TradeLoginEnter(event);' />";
    formContent += "                </td>";
    formContent += "            </tr>";
    formContent += "            <tr>";
    formContent += "                <th>";
    formContent += "                    验证码：";
    formContent += "                </th>";
    formContent += "                <td>";
    formContent += "                    <input id='txtExtCode' type='text' class='chanpininput' style='width: 80px;' onkeydown='javascript:return TradeLoginEnter(event);' />";
    formContent += "                    <a id='reLoadExtCode' href='#'>";
    formContent += "                        <img id='imgCode' src='' /></a>";
    formContent += "                </td>";
    formContent += "           </tr>";
    formContent += "        </tbody>";
    formContent += "        <tfoot>";
    formContent += "            <tr>";
    formContent += "                <td colspan='2' align='center'>";
    formContent += "                    <input type='image' id='btnLogin' src='" + baseURL + "Images/login-bottom.gif' align='middle' />";
    formContent += "                    &nbsp;&nbsp;<a href='http://passport.jcard.cn/Account/UCardRegister.aspx?from=http://www.jcard.cn/' style='color: Red'>新用户注册</a>";
    formContent += "                </td>";
    formContent += "            </tr>";
    formContent += "        </tfoot>";
    formContent += "    </table>";
    formContent += "</div>";

    $("#login").remove();
    $("body").append(formContent);

    $.ajax({
        type: "POST",
        dataType: "json",
        cache: false,
        url: baseURL + "Service/AjaxLoginService.aspx",
        data: "type=checklogin",
        success: function(data) {
            if (!data.HasError) {
                successFun(data);
                return;
            }
            $("#login").zxxbox({
                title: "登录",
                onclose: function() {
                    if (closeFun != null) {
                        closeFun();
                    }
                    //                    $("#ctl00_rblSaleType_0").attr("checked", "checked");
                }
            });
            $("#errInfo").text("");
            $("#txtUserName").val("").focus();
            $("#txtPassword").val("");
            $("#txtExtCode").val("");

            $("#imgCode").attr("src", baseURL + "Modules/RandomImage.aspx?colorf=ff0000&colorb=F0F0F0&preKey=login");
            $("#reLoadExtCode").click(function() {
                reLoadExtCode();
            })

            $("#btnLogin").click(function() {

                var userName = $("#txtUserName").val();
                var password = $("#txtPassword").val();
                var extCode = $("#txtExtCode").val();

                $.ajax({
                    type: "POST",
                    dataType: "json",
                    cache: false,
                    url: baseURL + "Service/AjaxLoginService.aspx",
                    data: "type=login&userName=" + userName + "&password=" + password + "&extCode=" + extCode,
                    success: function(data) {
                        if (data.HasError) {
                            $("#errInfo").text(data.Message);
                            reLoadExtCode();
                            return;
                        }
                        successFun(data);
                        $.zxxbox.hide();
                        $("#login").remove();
                    }
                });
            });
        }
    });
    return false;
}

function reLoadExtCode() {
    var ran = Math.round((Math.random()) * 100000000);
    $("#imgCode").attr("src", baseURL + "Modules/RandomImage.aspx?colorf=ff0000&colorb=F0F0F0&preKey=login&ran=" + ran);
}

function TradeLoginEnter(e) {
    var key = window.event ? e.keyCode : e.which;
    var obj = document.getElementById("btnLogin");
    if (key == 13) {
        obj.click();
        return false;
    }
}

function TopLogin() {
    CreateLoginForm(SetUCardTopLogin, null);
}

function SetUCardTopLogin(data) {
    $("#" + ucardTopRealName).html("<b>" + data.DicData.RealName + "</b> 您好，欢迎光临");
    $("#" + ucardTopSpanLogin).hide();
    $("#logout").show();
}
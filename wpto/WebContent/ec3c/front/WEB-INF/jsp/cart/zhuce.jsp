<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>



<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /><title>
	友邦商城
</title>
	<link href="/theme/ec1/style/com.css" rel="stylesheet" />
	<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
	<link href="/theme/ec1/style/web.css" rel="stylesheet" />
	
	<link href="/theme/ec1/style/InputForm.css" rel="stylesheet" />
</head>
<body>
    <form name="form1" method="post" action="Register.aspx" id="form1">
<div>
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwULLTE0MDYwNDU3MTAPFgQeDlJlZ2lzdGVyU3lzdGVtCylmSnVubmV0LkpOZXQuSk5ldENvbnN0YW50K1N1YlN5c3RlbSwgSnVubmV0LkpOZXQsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxsAx4TQ3VycmVudEludHJvZHVjZXJJRAL/////D2Rkr+zSzJIHDFQbUPU9P/wICM8Di+A=" />
</div>

<div>

	<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="0F36C6EC" />
</div>
        <div class="Top">
            <div class="Top_Left">
                <a href="Index.aspx" class="Logo"></a><span class="Top_Title">欢迎注册</span>
            </div>
        </div>
        <div class="Wrap1000">
            <div class="InputWrap">
                <span class="InputWrap_Tip">我已经注册，现在就&nbsp;&nbsp;&nbsp;<a href="Login.aspx">登录</a></span>
                <div class="InputForm">
                    <table>
                        <tr>
                            <th>手机号：<input type="hidden" name="hidCheckCode" id="hidCheckCode" />
                            </th>
                            <td>
                                <input name="txtMobile" type="text" maxlength="11" id="txtMobile" class="InputWidth270" PlaceHolder="请填写手机号" /></td>
                        </tr>
                        <tr>
                            <th>附加码：</th>
                            <td>
                                <input name="txtExtCode" type="text" maxlength="5" id="txtExtCode" class="InputWidth180" />
                                <img id="imgCode" title="点击更换验证码" class="imgExtCode" onclick="reLoadExtCode()" src="../Modules/RandomImage.aspx" align="absmiddle" style="height:35px;width:60px;border-width:0px;" />
                            </td>
                        </tr>
                        <tr>
                            <th>验证码：</th>
                            <td>
                                <input name="txtCheckCode" type="text" maxlength="6" id="txtCheckCode" class="InputWidth180" PlaceHolder="输入短信验证码" />
                                <input type="button" id="btnSendExtCode" class="btnExtCode" onclick="checkCode()" value="获取验证码" /></td>
                        </tr>
                        <tr>
                            <th>密码：</th>
                            <td>
                                <input name="txtLoginPassword" type="password" maxlength="30" id="txtLoginPassword" class="InputWidth270" PlaceHolder="设置登录密码" /></td>
                        </tr>
                        <tr>
                            <th>收货人：</th>
                            <td>
                                <input name="txtUser" type="text" maxlength="10" id="txtUser" class="InputWidth270" PlaceHolder="请输入收货人姓名" /></td>
                        </tr>
                        <tr>
                            <th style="vertical-align: text-top;">配送地址：</th>
                            <td>
                                <textarea name="txtAddress" rows="2" cols="20" id="txtAddress" class="MulText"></textarea></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <span id="lblNote" style="color: gray; display: none;">验证码已发出，请注意查收短信。如果再<span style="color: Red;" id="spTime">120</span>秒内没有收到验证码，请重新获取</span>

                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td>
                                <input type="submit" name="btnRegister" value="立即注册" onclick="return Validate();" id="btnRegister" class="btnRed" class="btnRed" /></td>
                        </tr>
                    </table>
                </div>
            </div>
            
<div class="Footer">
    友邦电商是由第三方供货商提供商品和物流的电商平台，所售商品由供货商提供售后，友邦电商提供7*24小时客服。<br />
    <br />
    Copyright&copy;2015  
                北京驰汇友邦科技有限公司&nbsp;&nbsp;&nbsp;&nbsp;京ICP备16014097号
</div>

        </div>
        <script src="/theme/ec1/Js/JQuery.js" type="text/javascript"></script>
        <script type="text/javascript">

            function Validate() {
                var txtMobile = $("#txtMobile");
                var txtCheckCode = $("#txtCheckCode");
                var txtLoginPassword = $("#txtLoginPassword");
                var txtUser = $("#txtUser");
                var txtAddress = $("#txtAddress");
                if (txtMobile.val() == "") {
                    alert("请输入手机号");
                    return false;
                }
                if (txtCheckCode.val() == "") {
                    alert("请输入短信验证码");
                    return false;
                }
                if (txtLoginPassword.val() == "") {
                    alert("请输入登录密码");
                    return false;
                }
                if (txtUser.val() == "") {
                    alert("收货人不能为空");
                    return false;
                }
                if (txtAddress.val() == "") {
                    alert("配送地址不能为空");
                    return false;
                }
                return true;
            }

            function checkCode() {
                var mobile = $("#txtMobile").val();
                if (!mobile) {
                    alert("请输入手机号");
                    return false;
                }
                if (!/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/.test(mobile)) {
                    alert("手机格式错误");
                    return false;
                }

                var extCode = $("#txtExtCode").val();
                if (!extCode) {
                    alert("请输入页面验证码");
                    return false;
                }

                var down;
                var time = 120;

                $.ajax({
                    url: "../MSite/Service/AjaxSendCodeService.aspx",
                    data: "doType=sendextcode&mobile=" + mobile + "&IsLoginAccount=true&extCode=" + extCode,
                    cache: false,
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        if (data.HasError) {
                            reLoadExtCode();
                            alert(data.Message);
                            return;
                        }

                        down = setInterval(function () {
                            DownTime();
                        }, 1000);
                        $("#lblNote").show();
                    }
                });

                function DownTime() {
                    time = time - 1;
                    if (parseInt(time) < 0) {
                        clearInterval(down);
                        time = 120;
                        $("#spTime").text("120");
                        $("#btnSendExtCode").removeAttr('disabled');
                        $("#btnSendExtCode").removeClass().addClass("btnExtCode");
                        $("#lblNote").hide();
                    } else {
                        $("#spTime").html(time);
                        $("#btnSendExtCode").attr('disabled', 'true');
                        $("#btnSendExtCode").removeClass().addClass("btnGray");
                        $("#lblNote").show();
                    }
                }
                return false;
            }

            function reLoadExtCode() {
                var ran = Math.round((Math.random()) * 100000000);
                $("#imgCode").attr("src", "../Modules/RandomImage.aspx?ran=" + ran);
            }
        </script>
    </form>
</body>
</html>

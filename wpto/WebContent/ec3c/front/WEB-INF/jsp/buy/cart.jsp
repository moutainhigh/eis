
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /><title>
	七彩商城
</title>
	<link href="/theme/ec1/style/com.css" rel="stylesheet" />
	<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
	<link href="/theme/ec1/style/web.css" rel="stylesheet" />
	
	<link href="/theme/ec1/style/InputForm.css" rel="stylesheet" />
    <style>
        .OrderTitle {
            font-size: 16px;
            line-height: 42px;
            color: #666;
            position: absolute;
            left: 0;
            top: -40px;
        }

        .ConfirmOrder {
            width: 960px;
            margin-top: 30px;
            padding: 0 20px 20px;
            border: 1px solid #f0f0f0;
        }

            .ConfirmOrder .InputForm {
                width: 100%;
                padding-top: 0;
            }

            .ConfirmOrder h1 {
                color: #333;
                font-size: 16px;
                height: 40px;
                line-height: 40px;
                font-weight: normal;
                margin-bottom: 10px;
                position: relative;
            }

                .ConfirmOrder h1 span {
                    font-size: 14px;
                    position: absolute;
                    right: 10px;
                    color: #aaaaaa;
                }

        .hr {
            width: 100%;
            height: 0;
            border-top: 1px solid #e6e6e6;
        }

        .tbOrder {
            border-collapse: collapse;
        }

            .tbOrder th {
                text-align: center;
                color: #999;
                width: 105px;
                font-size: 12px;
                font-family: Arial,"宋体";
                height: 35px;
                line-height: 35px;
                border-bottom: 3px solid #b2d1ff;
            }

            .tbOrder td {
                height: 80px;
                line-height: 80px;
                text-align: center;
                position: relative;
            }

                .tbOrder td img {
                    position: absolute;
                    left: 8px;
                    top: 11px;
                }

        .Total {
            font-size: 14px;
            color: #666;
            font-family: Arial,"宋体";
            text-align: right;
        }

        .Total_Price {
            color: #e4393c;
            font-size: 20px;
            font-weight: 700;
        }
    </style>
</head>
<body>
    <form name="form1" method="post" action="ConfirmOrder.aspx" id="form1">
<div>
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUKLTYwMjkzMDM0Mw8WCh4GQmlsbE5vBRBQMTYwNjE3MDAzMTg1OTNEHg5DdXJQcm9kdWN0Q29kZQUPQUFXWFNDRlNYQjE2OUZWHg5DdXJQcm9kdWN0TmFtZQU/56i76I2J5Lq6TWV4aWNhbiDnlJznvo7mt5HlpbPonbTonbbnu5Pkuq7oibLmi4npk77plb/mrL5fMTY55YWDHg1DdXJQcm9kdWN0TnVtAgEeEUN1clByb2R1Y3RTYWxlQW10KClbU3lzdGVtLkRlY2ltYWwsIG1zY29ybGliLCBWZXJzaW9uPTIuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OQgxNjkuMDAwMBYCAgMPZBYMAgMPDxYCHghJbWFnZVVybGRkZAIEDw8WAh4EVGV4dAU/56i76I2J5Lq6TWV4aWNhbiDnlJznvo7mt5HlpbPonbTonbbnu5Pkuq7oibLmi4npk77plb/mrL5fMTY55YWDZGQCBQ8PFgIfBgUGMTY5LjAwZGQCBg8PFgIfBgUBMWRkAgcPDxYCHwYFBjE2OS4wMGRkAggPDxYCHwYFBjE2OS4wMGRkZOe3Y0xbYSMStPxO3WipX3knRE5U" />
</div>

<div>

	<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="82008B30" />
</div>
        <div class="Top">
            <div class="Top_Left">
                <a href="Index.aspx" class="Logo"></a><span class="Top_Title">确认订单</span>
            </div>
        </div>
        <div class="SpaceBlockHorizontal20"></div>
        <div class="Wrap1000">
            <div class="ConfirmOrder">
                <span class="OrderTitle">填写并核对订单信息</span>
                <div class="InputForm">
                    <h1>收货信息</h1>
                    <table>
                        <tr>
                            <th>收货人：</th>
                            <td>
                                <input name="txtUser" type="text" value="18064029219" maxlength="10" id="txtUser" class="InputWidth270" /></td>
                        </tr>
                        <tr>
                            <th>联系电话：</th>
                            <td>
                                <input name="txtMobile" type="text" value="18064029219" maxlength="11" id="txtMobile" class="InputWidth270" /></td>
                        </tr>
                        <tr>
                            <th>配送地址：</th>
                            <td>
                                <textarea name="txtAddress" rows="2" cols="20" id="txtAddress" class="MulText">湖北省武汉市洪山区</textarea></td>
                        </tr>
                    </table>
                    <div class="hr"></div>
                    <div class="SpaceBlockHorizontal"></div>
                    <h1>支付方式<span>财付通支付</span></h1>
                    <div class="hr"></div>
                    <h1>确认订单信息</h1>
                    <table class="tbOrder">
                        <tr>
                            <th style="text-align: left;">商品</th>
                            <th>单价（元）</th>
                            <th>数量</th>
                            <th>优惠（元）</th>
                            <th>小计（元）</th>
                            <th>配送方式</th>
                        </tr>
                        <tr>
                            <td style="width: 335px; text-align: left; padding-left: 78px;">
                                <img id="imgProduct" src="" style="height:60px;width:60px;border-width:0px;" />
                                <span id="lblProductName">稻草人Mexican 甜美淑女蝴蝶结亮色拉链长款_169元</span>
                            </td>
                            <td>
                                <span id="lblPrice">169.00</span>
                            </td>
                            <td>
                                <span id="lblNum">1</span></td>
                            <td>0.00</td>
                            <td>
                                <span id="lblPrices">169.00</span></td>
                            <td>快递&nbsp;免费</td>
                        </tr>
                    </table>
                    <div class="hr"></div>
                    <div class="SpaceBlockHorizontal"></div>
                    <div class="Total">
                        应付总额：&nbsp;<span class="Total_Price">&yen;<span id="lblSalePrice">169.00</span></span>&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               <input type="submit" name="btnSubmit" value="提交订单" onclick="return Validate();" id="btnSubmit" class="btnRed" />
                    </div>
                </div>
            </div>
            
<div class="Footer">
    ${systemName}是由第三方供货商提供商品和物流的电商平台，所售商品由供货商提供售后，${systemName}提供7*24小时客服。<br />
    <br />
    ${commonFooter}
</div>

        </div>
        <script src="/theme/ec1/js/JQuery.js"></script>
        <script>
            function Validate() {
                var txtUser = $("#txtUser");
                var txtMobile = $("#txtMobile");
                var txtAddress = $("#txtAddress");

                if (txtUser.val() == "") {
                    alert("收货人不能为空");
                    return false;
                }

                if (txtMobile.val() == "") {
                    alert("联系电话不能为空");
                    return false;
                }

                if (txtAddress.val() == "") {
                    alert("请输入配送地址");
                    return false;
                }
                return true;
            }
        </script>
    </form>
</body>
</html>

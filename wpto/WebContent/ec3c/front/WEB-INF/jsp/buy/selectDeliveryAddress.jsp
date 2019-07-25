
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
	
	<link href="/theme/ec1/style/InputFrom.css" rel="stylesheet" />
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
                   float: right;
                    right: 10px;
                    margin-left: 15px;
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
    <form name="form1" action="/buy/settleUp.shtml" id="form1" onSubmit="return Validate();">


        <div class="Top">
            <div class="Top_Left">
                <a href="" class="Logo"></a><span class="Top_Title">填写地址信息</span>
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
                                <input name="txtUser" type="text" value="" maxlength="10" id="txtUser" class="InputWidth270" /></td>
                        </tr>
                        <tr>
                            <th>联系电话：</th>
                            <td>
                                <input name="txtMobile" type="text" value="" maxlength="11" id="txtMobile" class="InputWidth270" /></td>
                        </tr>
                        <tr>
                            <th>配送地址：</th>
                            <td>
                                <textarea name="txtAddress" rows="2" cols="20" id="txtAddress" class="MulText"></textarea></td>
                        </tr>
                    </table>
                    <div class="hr"></div>
                    <div class="SpaceBlockHorizontal"></div>
                    <h1>支付方式<c:forEach var="i" items="${payTypeList}">
                    			
                    				<span><input type="radio" name="payType" value="${i.payTypeId}" />${i.name}</span>
                    	
                    			</c:forEach></h1>
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
                        <c:set var="totalMoney" value="0"/>
                        <c:forEach var="it" items="${cart}">
                        	
                        	
    						<tr>
	                        	<c:set var="totalMoney" value="${totalMoney + it.value.requestMoney}"/>
    							<input type="hidden" name="tid" value="${it.value.transactionId}" id="tid"/>
	                            <td style="width: 335px; text-align: left; padding-left: 78px;">
	                                <img id="imgProduct" src="${it.value.itemDataMap.get('productSmallImage').dataValue}" style="height:60px;width:60px;border-width:0px;" />
	                                <span id="lblProductName">${it.value.name}</span>
	                            </td>
	                            <td>
	                                <span id="lblPrice">${it.value.labelMoney}</span>
	                            </td>
	                            <td>
	                                <span id="lblNum">${it.value.count}</span></td>
	                            <td>${it.value.labelMoney*it.value.count-it.value.requestMoney}</td>
	                            <td>
	                                <span id="lblPrices">${it.value.requestMoney}</span></td>
	                            <td>快递&nbsp;免费</td>
	                        </tr>
                       </c:forEach>
                       <input type="hidden" name="orderId" value="${orderId}"> 
                    </table>
                    <div class="hr"></div>
                    <div class="SpaceBlockHorizontal"></div>
                    <div class="Total">
                        应付总额：&nbsp;<span class="Total_Price">&yen;<span id="lblSalePrice">${totalMoney}</span></span>&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               <input type="submit" value="提交订单" id="btnSubmit" class="btnRed" />
                    </div>
                </div>
            </div>
            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>
       
       <script src="/theme/ec1/js/JQuery.js"></script>
        <script src="/theme/ec1/js/jquery.form.3.5.1.js"></script>
        <script>
          function Validate() {
              var txtUser = $("#txtUser");
              var txtMobile = $("#txtMobile");
              var txtAddress = $("#txtAddress");

              if (txtUser.val() == "") {
                  alert("收货人不能为空");
                  return false;
              }

             else if (txtMobile.val() == "") {
                  alert("联系电话不能为空");
                  return false;
             }

             else if (txtAddress.val() == "") {
                  alert("请输入配送地址");
                 return false;
                }
             else{
             	return true;
             }
                  		
       }
            
//         $("form").submit(function(e){
//         			var btn=Validate();
//         			
//				    e.preventDefault();
//				    if(btn){
//				    	$(this).ajaxSubmit({
//				        type : "POST",
//				        url :  '/buy/settleUp.json',
//				        async:false,
//				        dataType : 'json',
//				        success : function (data) { 
//				        	alert(JSON.stringify(data));
//				        	
//				        	
//				      	}
//				    	})
//				    }
//					
//				});	
				
		
        </script>
     </form>
     
</body>
</html>


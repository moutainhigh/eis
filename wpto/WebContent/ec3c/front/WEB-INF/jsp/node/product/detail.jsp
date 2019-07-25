<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html>

<html >

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
	<title>七彩商城</title>
	<link href="/theme/ec1/style/com.css" rel="stylesheet" />
	<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
	<link href="/theme/ec1/style/web.css" rel="stylesheet" />
	<link href="/theme/ec1/style/ProductDetail.css" rel="stylesheet" />
</head>
<body>
	<%@include file="/WEB-INF/jsp/include/head.jsp" %>
    <form name="form1" method="post" action='/buy/add.json' id="form1" onSubmit="return false;">
        <!--<input type="hidden" name="hidCategoryCode" id="hidCategoryCode" value="AAWXSCCPGM" />
        <input type="hidden" name="hidProductCode" id="hidProductCode" value="AAWXSCCPGM2K6FV" />
        <div class="SpaceBlockHorizontal20"></div>-->
        <div class="Wrap1000">
            <div class="ProductDetial1">
                <div class="ProductDetial1_Left">
                   <img id="imgProduct" src="${document.documentDataMap.get('productSmallImage').dataValue}" style="height:160px;width:160px;border-width:0px;" />
                </div>
                <div class="ProductDetial1_Right">
                    <div class="Title1"><span id="lblProductName">${document.title}</span></div>
                    
                    <div class="Price">
                        价格：
                        <strong>
                        <span class='Yen'>&yen;</span><span id="lblPrice">${fn:replace(document.documentDataMap.get("productBuyMoney"). dataValue,"money:","")}</span></strong>
                    </div>
                    <div class="Num">数量：
                          <select name="cboNum" id="cboNum" class="InputWidth100">
	<option selected="selected" value="1">1</option>
	<option value="2">2</option>
	<option value="3">3</option>
	<option value="4">4</option>
	<option value="5">5</option>
	<option value="6">6</option>
	<option value="7">7</option>
	<option value="8">8</option>
	<option value="9">9</option>
	<option value="10">10</option>

</select>
                    </div>
                     <div class="Price">
                        总价：
                        <strong>
                        <span class='Yen'>&yen;</span><span id="lblPriceer">${fn:replace(document.documentDataMap.get("productBuyMoney"). dataValue,"money:","")}</span></strong>
                    </div>
                    
			   			
                    <div class="Btn">
                    	<input type="hidden" value="${document.documentDataMap.get('productCode').dataValue}" id="productCode"/>
                        <input type="submit" name="btnSubmit" value="立即购买" id="btnSubmit" class="btnRed""/>
                    </div>
                </div>
            </div>
            <div class="cf"></div>
            <div class="ProductDetial2">
                <div class="ProductDetial2_Title">商品介绍</div>
                <div class="ProductDetial2_Detial">
                    <span id="lblDesc"></span></div>
                <div class="Wrap750">
                	${document.content}
                    <!--<iframe id="iframepage" src="" width="750" marginheight="0" marginwidth="0" frameborder="0" scrolling="no"" onLoad="iFrameHeight()"></iframe>-->
                    </div>
            </div>
            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>

        

        <script src="/theme/ec1/js/jquery-1.3.2.min.js"></script>
            <script src="/theme/ec1/js/commonTop.js"></script>
            <script src="/theme/ec1/js/com.js"></script>

        <script type="text/javascript">
//          $(function () {
//              var categoryCode = $("#hidCategoryCode").val();
//              var productCode = $("#hidProductCode").val();
//              $("#iframepage").attr("src", "ProductDetail/" + categoryCode + "/" + productCode + ".html");
//
//          })
//          function iFrameHeight() {
//              var ifm = document.getElementById("iframepage");
//              var subWeb = document.frames ? document.frames["iframepage"].document :
//              ifm.contentDocument;
//              if (ifm != null && subWeb != null) {
//                  ifm.height = subWeb.body.scrollHeight;
//              }
//          }
            
            
             //模拟下拉列表框

         

        $(document).ready(function(){ 
        	var money=$('#lblPriceer').text();
			$('#cboNum').change(function(){ 
			//alert($(this).children('option:selected').val()); 
			
			var p1=$(this).children('option:selected').val();//这就是selected的值 
			//var p2=$('#lblPrice').css("color","blue");//获取本页面其他标签的值 
			$('#lblPriceer').text(money*p1+".0");
			//alert(mo*p1);
			
			//window.location.href="xx.php?param1="+p1+"¶m2="+p2+"";//页面跳转并传参 
			}) 
		}) 
		
		//立即购买
		
		var $buynow= $('#btnSubmit');
		$buynow.click(function(){
           //alert("/cart.shtml?directBuy=true&productCode="+$("#productCode").val()+"&count="+$("#productcount").val());
          // location.href="/cart.shtml?directBuy=true&productCode="+$("#productCode").val()+"&count="+$("#productcount").val();

            $.ajax({
            type:"POST",
            url:'/buy/add.json',//'/buy/add.json',///buy/settleUp.json
            data:{
                productCode:$("#productCode").val(),
                count:$("#cboNum").val(),
                directBuy:true,
            },
            dataType:'json',
           
            success:function (data) {
            	//alert(JSON.stringify(data));
            	//alert(data.message.message);
            	
                switch (data.message.operateCode) {    
                    case operateResultSuccess://102008;	//成功;6;操作结果
                       // location.href="/WEB-INF/jsp/buy/ordergoods.";
                      location.href='/cart.shtml?cartId='+data.item.cartId;
//                     
                        break;
                    case errorUserNotFoundInRequest://500042; //在Session中没找到用户;18;错误码
                        alert(data.message.message);
                       location.href='/content/user/login.shtml?url='+location.href;
                       //location.href=getRootPath()+"/"+unescape(data.redirectUrl);
                        break;
                    case  operateResultFailed://失败;6;操作结果102007
                        alert(datas.message.message);
                        break;
                }
                //alert(data.message.operateCode)
            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });

     });
        </script>
    </form>
</body>
</html>

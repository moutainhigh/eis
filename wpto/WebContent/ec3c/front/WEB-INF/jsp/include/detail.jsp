<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
    <input type="hidden" name="uuid" value="${frontUser.uuid}">
    <input type="hidden" name="objectId" value="${document.udid}"  id="udid">
     <div class="textInfo">
     <form action="javascript:addToCart(21)" method="post" name="ECS_FORMBUY" id="ECS_FORMBUY" >
    <h1 class="clearfix" >${document.title}</h1> 
    
    <ul class="ul2 clearfix">
        <li class="clearfix" style="width:100%">
       <dd>
 
       
       <strong>本店售价：</strong><font class="shop" id="ECS_SHOPPRICE">￥
        <em class="moneynumber" style="font-style: normal;">${document.documentDataMap.productBuyMoney.dataValue}</em>元</font> 
   
     
            <font class="market">${document.documentDataMap.productMarketPrice.dataValue} </font> 
              </dd>
       </li>
      
      
      
      
             <li class="clearfix">
       <dd>
       <strong>商品货号：</strong><em id="documentCode" style="font-style: normal;">${document.documentDataMap.productCode.dataValue}</em>    
       </dd> 
       </li> 
                      <li class="clearfix">
       <dd>
      
                  <strong>商品库存：</strong>
         ${document.documentDataMap.availableCount.dataValue}           
       </dd>
       </li>  
                             <li class="clearfix">
       <dd>
       
       <strong>商品重量：</strong>${document.documentDataMap.goodsSpec.dataValue}     
       </dd>
      </li>
        
             <li class="clearfix">
       <dd>
    
      <strong>上架时间：</strong>
      <fmt:formatDate value="${document.publishTime}" type="both" />  
       </dd>
       </li>
              <li class="clearfix">
       <dd>
       
       <strong>商品点击数：</strong>${readCount}</dd>
      </li>
    </ul>
    
    
    <ul>
     
        
      
        
  
  <li class="clearfix">
       <dd >
  
              注册用户：<font class="f1" id="ECS_RANKPRICE_1" style=" padding-right:10px;">￥<em class="moneynumber" style="font-style: normal;">${document.documentDataMap.productBuyMoney.dataValue}</em>元</font> 
              
        <a href="#a" onclick="collect()">收藏</a> |  
              <a href="#a" onclick="recommend()">推荐</a>
         </dd>
      </li>
  
               
      
       
        <li class="clearfix">
       <dd>
       <strong>购买此商品可使用：</strong><font class="f4">${fn:substringAfter(document.documentDataMap.PRICE_PRICE_STANDARD.dataValue,"score=")}积分</font>
       </dd>
      </li>
                            </ul>
         <ul class="bnt_ul">
     
      
            
      
           <li class="clearfix">
       <dd>
       <strong>购买数量：</strong>
        <input name="number" type="text" id="number" value="1" size="4"  style="border:1px solid #ccc; "/> <strong>商品总价：</strong><font id="ECS_GOODS_AMOUNT" class="f1"></font>
       </dd>
       </li>
      <!-- 加入购物车 -->
      <li class="padd">
      <img src="/theme/${theme}/images/goumai2.gif" />
     
      </li>
     
      </ul>
      </form>
     </div>
   </div>

   <script>
        // 收藏按钮
        function  collect(){
                $.ajax({
                    type:"POST",
                    url:"/userRelation/add.json",
                    data:{  
                        uuid:$('input[name="uuid"]').val(),
                        objectType:'document',
                        objectId:$('input[name="objectId"]').val(),
                        relationType:'favorite',
                    },           
                    async:false,
                    success:function(data){
                        switch (data.message.operateCode) {
                            case 500031:
                                alert(data.message.message);
                                location.href="/user/login.shtml";
                                break;
                            case 102008:
                                alert(data.message.message);
                                window.reload();
                                break;
                            case 500018:
                                alert(data.message.message);
                                window.reload();
                                break;
                        }			
                    }
                })
        }

        // 推荐按钮
        function  recommend(){
                $.ajax({
                    type:"POST",
                    url:"/userRelation/add.json",
                    data:{  
                        uuid:$('input[name="uuid"]').val(),
                        objectType:'document',
                        objectId:$('input[name="objectId"]').val(),
                        relationType:'groom',
                    },           
                    async:false,
                    success:function(data){
                        switch (data.message.operateCode) {
                            case 500031:
                                alert(data.message.message);
                                location.href="/user/login.shtml";
                                break;
                            case 102008:
                                alert(data.message.message);
                                window.reload();
                                break;
                            case 500018:
                                alert(data.message.message);
                                window.reload();
                                break;
                        }			
                    }
                })
        }
   </script>
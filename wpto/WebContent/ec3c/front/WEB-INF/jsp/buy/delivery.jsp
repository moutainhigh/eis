<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit">
<title>${systemName}-结算</title>
<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />
<script  type="text/javascript" src="/theme/ec1/js/jquery-1.7.1.js"></script>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="respond" src="/theme/ec1/js/respond.src.js"></script>
<script  type="text/javascript" src="/theme/ec1/js/common.js"></script>
<script  type="text/javascript" src="/theme/${theme}/js/selectCity.js"></script>

<style>
	.wrap1000{
		margin:0 auto;
		width:1000px;
	}
	.center{
		text-align:center;
	}
	*{
		font-family:"Microsoft Yahei";
	}
	.head{
		margin-top:120px;
	}
	h1{
		font-size:30px;
		font-weight:bold;
		color:#FF6922;
	}
	body{
		text-align:center;
	}
  .site{
    position: fixed;
    top: 0;left: 0;width: 100%;height: 100%;
    background-color: rgba(0,0,0,0.5);
        z-index: 9999999;
  }
  .sitebox{
    position: absolute;
    top: 50%;
    left: 50%;
    width: 520px;
    height: 415px;
    background-color: #F9F9F9;
    margin-top: -207px;
    margin-left: -273px;
    border: 13px solid rgba(0, 0, 0, 0.14);

}
.sitebox h3{
        text-align: center;
    font-size: 18px;
    line-height: 40px;
    color: #CE4444;
    background-color: transparent;
    border-bottom: 1px solid #333;
    height: 40px;
}
.sitebox .sitetext{
  text-align: left;
padding: 20px;
}
.sitebox .sitetext p{
  margin-bottom: 15px;
}
.sitebox .sitetext p .title{
      width: 97px;
    display: inline-block;
    text-align: right;
    font-size: 14px;
}
.sitebox .sitetext textarea{
    vertical-align: top;
    width: 340px;
    height: 50px;
}
.sitebox .sitetext input[type='text'],.sitebox .sitetext textarea{
  width: 340px;
  padding: 2px 5px;
}
.sitebox .sitetext input[type='button']{
      padding: 4px 19px;
    background-color: #272727;
    color: #fff;
    border: none;
    box-shadow: 0 0 2px #000;
    margin: 0 10px;
    cursor: pointer;
}
.handle span{
  cursor: pointer;
}
.trnone{
  display: none;
}
.shouqispan,.qitaspan{
  cursor: pointer;
  text-align: left;
  display: inline-block;
  width: 100%;
      color: #D42F09;
}
</style>
<script>
	
</script>
</head>

<body>
	
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
    <!--<div class="head center">
	   <h1>${message.operateCode}</h1>
    </div>
    <div class="content center">
		<p>${message.message}</p>
		<p>${message.content}</p>
    </div>-->
	  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/">首页</a> <code>&gt;</code> 购物流程 
</div>
</div>
<div class="blank"></div>
 
<div class="block table">   
    <form action="" method="post" name="theForm" id="theForm">
        <div class="flowBox">
        <h6><span>商品列表</span></h6>
        <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
            <tr>
              <th bgcolor="#ffffff">商品名称</th>
              <th bgcolor="#ffffff">属性</th>
                            <th bgcolor="#ffffff">市场价</th>
                            <th bgcolor="#ffffff">本店价</th>
              <th bgcolor="#ffffff">购买数量</th>
              <th bgcolor="#ffffff">小计</th>
            </tr>
            
          <c:forEach items="${cart}" var="a"> 
            <input type="hidden" value="${a.value.transactionId}" class="tid">
            <input type="hidden" value="${a.value.cartId}" class="cartId">
              <tr>
                <td bgcolor="#ffffff">
                        <a href="${a.value.itemDataMap.refUrl.dataValue}" target="_blank" class="f6">${a.value.name}</a>
                                                    </td>

                <td bgcolor="#ffffff"></td>
                              <td bgcolor="#ffffff">￥${a.value.price.marketPrice}元</td>
                              <td bgcolor="#ffffff" >￥${a.value.price.money}元</td>
                <td bgcolor="#ffffff"  class="number">${a.value.count}</td>
                <td bgcolor="#ffffff" >￥<span class="total">${a.value.price.money*a.value.count}</span>元</td>
          </c:forEach>
              </tr>
                                      <tr>
                <td bgcolor="#ffffff" colspan="7">
                              购物金额小计 ￥<span id="totalCart" ></span>元，比市场价 ￥<span id="pulicCart"></span>元 节省了 ￥<span id="cart"></span>元               </td>
              </tr>
                      </table>
      </div>
      <div class="blank"></div>
      <div class="flowBox">
      <h6><span>收货人信息</span><span class="f6" id="addsite" style="float: right;cursor: pointer;">新增收货地址</span></h6>
      <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd" id="addresstable">
            <c:choose>
              <c:when test="${empty addressBookList}">
                  <tr>您还未填写收货地址！</tr>
              </c:when>
              <c:otherwise>
                  <tr><th>收货人</th><th>所在地区</th><th>详细地址</th><th>邮政编码</th><th>手机</th><th>电话</th><th>操作</th></tr> 
                  <c:forEach items="${addressBookList}" var="a" varStatus='status'>
                      <c:choose>
                      <c:when test="${a.currentStatus=='100003'}">
                      <tr>
                        <td bgcolor="#ffffff" style="width:10%;"><span class="name">${a.contact}</span></td>
                        <td bgcolor="#ffffff" style="width:15%;"><span class="prov">${a.province}</span><span class="city">${a.city}</span><span class="dist">${a.district}</span></td>
                        <td bgcolor="#ffffff" style="width:20%;"><span class="address">${a.address}</span></td>
                        <td bgcolor="#ffffff" style="width:10%;"><span class="zipcode">${a.postcode}</span></td>
                        <td bgcolor="#ffffff" style="width:10%;"><span class="mobile">${a.mobile}</span></td>
                        <td bgcolor="#ffffff" style="width:10%;"><span class="tel">${a.phone}</span></td>
                        <td bgcolor="#ffffff" class="handle">
                          <span style="margin-right: 15px;">
                            <input type="radio" name="addressBookId" class="addressBookId" value="${a.addressBookId}" checked/><span style=" color: #D42F09;">配送至该地址</span>
                            <input type="radio" onclick="one(${a.addressBookId})" class="setDefaultAdd" name="default_address" checked /><span style=" color: #D42F09;">默认地址</span>
                          </span>
                          <span class="modification" dataaddressBookId="${a.addressBookId}">修改</span> | <span class="delete" onclick="deletes(${a.addressBookId})">删除</span>
                        </td>
                      </tr>
                      </c:when>
                      <c:otherwise>
                         <tr>
                        <td bgcolor="#ffffff"><span class="name">${a.contact}</span></td>
                        <td bgcolor="#ffffff"><span class="prov">${a.province}</span><span class="city">${a.city}</span><span class="dist">${a.district}</span></td>
                        <td bgcolor="#ffffff"><span class="address">${a.address}</span></td>
                        <td bgcolor="#ffffff"><span class="zipcode">${a.postcode}</span></td>
                        <td bgcolor="#ffffff"><span class="mobile">${a.mobile}</span></td>
                        <td bgcolor="#ffffff"><span class="tel">${a.phone}</span></td>
                        <td bgcolor="#ffffff" class="handle">
                          <span style="margin-right: 15px;">
                            <input type="radio" name="addressBookId" class="addressBookId" value="${a.addressBookId}"/><span>配送至该地址</span>
                            <input type="radio" name="default_address" onclick="one(${a.addressBookId})"class="setDefaultAdd"/>设为默认
                          </span>
                          <span class="modification" dataaddressBookId="${a.addressBookId}">修改</span> | <span class="delete" onclick="deletes(${a.addressBookId})">删除</span>
                        </td>
                      </tr>
                      </c:otherwise>
                      </c:choose>
                  </c:forEach>
              </c:otherwise>
            </c:choose>
                      </table>
      </div>
     <div class="blank"></div>
        <!--<div class="flowBox">
    <h6><span>配送方式</span></h6>
    <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd" id="shippingTable">
            <tr>
              <th bgcolor="#ffffff" width="5%">&nbsp;</th>
              <th bgcolor="#ffffff" width="25%">名称</th>
              <th bgcolor="#ffffff">订购描述</th>
              <th bgcolor="#ffffff" width="15%">费用</th>
              <th bgcolor="#ffffff" width="15%">免费额度</th>
              <th bgcolor="#ffffff" width="15%">保价费用</th>
            </tr>
                        <tr>
              <td bgcolor="#ffffff" valign="top"><input name="shipping" type="radio" value="1"  supportCod="1" insure="0" onclick="selectShipping(this)" />
              </td>
              <td bgcolor="#ffffff" valign="top"><strong>圆通速递</strong></td>
              <td bgcolor="#ffffff" valign="top">上海圆通物流（速递）有限公司经过多年的网络快速发展，在中国速递行业中一直处于领先地位。为了能更好的发展国际快件市场，加快与国际市场的接轨，强化圆通的整体实力，圆通已在东南亚、欧美、中东、北美洲、非洲等许多城市运作国际快件业务</td>
              <td bgcolor="#ffffff" align="right" valign="top">￥5.00元</td>
              <td bgcolor="#ffffff" align="right" valign="top">￥0.00元</td>
              <td bgcolor="#ffffff" align="right" valign="top">不支持保价</td>
            </tr>
                        <tr>
              <td bgcolor="#ffffff" valign="top"><input name="shipping" type="radio" value="2"  supportCod="0" insure="0" onclick="selectShipping(this)" />
              </td>
              <td bgcolor="#ffffff" valign="top"><strong>运费到付</strong></td>
              <td bgcolor="#ffffff" valign="top">所购商品到达即付运费</td>
              <td bgcolor="#ffffff" align="right" valign="top">￥0.00元</td>
              <td bgcolor="#ffffff" align="right" valign="top">￥0.00元</td>
              <td bgcolor="#ffffff" align="right" valign="top">不支持保价</td>
            </tr>
                        <tr>
              <td colspan="6" bgcolor="#ffffff" align="right"><label for="ECS_NEEDINSURE">
                <input name="need_insure" id="ECS_NEEDINSURE" type="checkbox"  onclick="selectInsure(this.checked)" value="1"  disabled="true"  />
                配送是否需要保价 </label></td>
            </tr>
          </table>
    </div>-->
    <div class="blank"></div>
                <div class="flowBox">
    <h6><span>支付方式</span></h6>
    <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd" id="paymentTable">
            <tr>
              <th width="5%" bgcolor="#ffffff">&nbsp;</th>
              <th width="20%" bgcolor="#ffffff">名称</th>
              <th bgcolor="#ffffff">订购描述</th>
              <!--<th bgcolor="#ffffff" width="15%">手续费</th>-->
            </tr>
            <c:forEach items="${payTypeList}" var="a">          
                <tr>
                  <td valign="top" bgcolor="#ffffff"><input type="radio" name="payment" value="${a.payTypeId}"  isCod="0" /></td>
                  <td valign="top" bgcolor="#ffffff"><strong>${a.name}</strong></td>
                  <td valign="top" bgcolor="#ffffff">${a.description}</td>
                  <!--<td align="right" bgcolor="#ffffff" valign="top">￥0.00元</td>-->
                </tr>
            </c:forEach>
                      </table>
    </div>
        <div class="blank"></div>
          
          

      <!--<div class="flowBox">
    <h6><span>其它信息</span></h6>
      <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
                                                <tr>
              <td bgcolor="#ffffff"><strong>使用红包:</strong></td>
              <td bgcolor="#ffffff">
                选择已有红包                <select name="bonus" onchange="changeBonus(this.value)" id="ECS_BONUS" style="border:1px solid #ccc;">
                  <option value="0" selected>请选择</option>
                                  </select>

                或者输入红包序列号               <input name="bonus_sn" type="text" class="inputBg" size="15" value=""/> 

                <input name="validate_bonus" type="button" class="bnt_blue_1" value="验证红包" onclick="validateBonus(document.forms['theForm'].elements['bonus_sn'].value)"  />
              </td>
            </tr>
                                    <tr>
              <td valign="top" bgcolor="#ffffff"><strong>订单附言:</strong></td>
              <td bgcolor="#ffffff"><textarea name="postscript" cols="80" rows="3" id="postscript" style="border:1px solid #ccc;"></textarea></td>
            </tr>
                        <tr>
              <td bgcolor="#ffffff"><strong>缺货处理:</strong></td>
              <td bgcolor="#ffffff">                <label>
                <input name="how_oos" type="radio" value="0" checked onclick="changeOOS(this)" />
                等待所有商品备齐后再发</label>
                                <label>
                <input name="how_oos" type="radio" value="1"  onclick="changeOOS(this)" />
                取消订单</label>
                                <label>
                <input name="how_oos" type="radio" value="2"  onclick="changeOOS(this)" />
                与店主协商</label>
                              </td>
            </tr>
                      </table>
    </div>-->
    <div class="blank"></div>
    <div class="flowBox">
    <h6><span>费用总计</span></h6>
          <div id="ECS_ORDERTOTAL">
<table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
    <tr>
    <!--<td align="right" bgcolor="#ffffff">    该订单完成后，您将获得            <font class="f4_b">3499</font> 积分            ，以及价值               <font class="f4_b">￥0.00元</font>的红包。
          </td>-->
</tr>
    <tr>
    <td align="right" bgcolor="#ffffff">
      商品总价: <font class="f4_b">￥<span id="totalPrice"></span>元</font>
                                              </td>
  </tr>
    <tr>
    <td align="right" bgcolor="#ffffff"> 应付款金额: <font class="f4_b">￥<span id="nowPrice"></span>元</font>
    	</td>
  </tr>
</table>
</div>           <div align="center" style="margin:8px auto;">
            <!--<input type="image" src="themes/ecmoban_haier2015/images/bnt_subOrder.gif" onclick="submitPay()"/>-->
            <input type="button" value="提交订单" onclick="submitPay()">
            <input type="hidden" name="step" value="done" />
            </div>
    </div>
<!-- 新增地址弹窗 -->
<div class="site" style="display:none;">
  <div class="sitebox">
  <h3>新增收货地址</h3>
  <div class="sitetext">
    <p>
      <span  class="title">所在地区<em style="color:red;font-style: normal;">*</em>：</span>
      <span id="citys">
          <select class="prov" id="prov" style="border:1px solid #ccc;"></select>
          <select class="city" id="city" style="border:1px solid #ccc;"></select>
          <select class="dist" id="dist" style="border:1px solid #ccc;"></select>
      </span>
    </p>
    <p><span class="title">详细地址<em style="color:red;font-style: normal;">*</em>：</span><textarea name=""  id="address"  cols="30" rows="10"></textarea></p>
    <p><span class="title">邮政编码<em style="color:red;font-style: normal;">*</em>：</span><input type='text'id="zipcode" /></p>
    <p><span class="title">收货人姓名<em style="color:red;font-style: normal;">*</em>：</span><input type='text' id="name"/></p>
    <p><span class="title">手机号<em style="color:red;font-style: normal;">*</em>：</span><input type='text' id="mobile"/></p>
    <p><span class="title">电话 ：</span><input id="tel" type="text"/></p>
    <p><span class="title"></span><input type="checkbox" id="setDefaultAdd" datavalue="N"/><span>设置为默认收货地址</span></p>
    <p style="text-align:center;"><input type="button" class="modificationbtn" value="修 改" style="display:none;"><input type="button" class="confirm" value="新 增" onclick="createAddress()"><input type="button" class="close" value="取 消" style="background-color:#D0D0D0;color: #151515;"></p>
  </div>
  </div>
</div>
    </form>
      <script>        
        var flow_no_payment = "您必须选定一个支付方式。";
        var flow_no_shipping = "您必须选定一个配送方式。";

        //  检验是否登录
        $(function(){
        

        // 市场价总计
        var num = 0;
        // 本店价总计
        var nub = 0;
        $('.number').each(function(){
            num += parseFloat($(this).siblings().eq(2).text().substring(1,$(this).siblings().eq(2).text().length-1))*parseInt($(this).text());
            nub += parseFloat($(this).siblings().eq(3).text().substring(1,$(this).siblings().eq(3).text().length-1))*parseInt($(this).text());
        })
        $('#pulicCart').text(num.toFixed(2));
        $('#totalCart').text(nub.toFixed(2));

        // 节省金额
        $('#cart').text((num-nub).toFixed(2))

        // 商品总价
        $('#totalPrice').text(nub.toFixed(2));

        // 应付金额
        $('#nowPrice').text(nub.toFixed(2));
      })

        // 实现对数字保留两位小数效果
        function returnFloat(value){
              var value=Math.round(parseFloat(value)*100)/100;
              var xsd=value.toString().split(".");
              if(xsd.length==1){
                value=value.toString()+".00";
                return value;
              }
              if(xsd.length>1){
                if(xsd[1].length<2){
                  value=value.toString()+"0";
                }
                return value;
            }
        }
         

         $('.total').each(function(){
           $(this).text(returnFloat($(this).text()))
         })

        // 登出按钮
       function secede(){
          $.ajax( {  
                type : "get",  
                url : "/user/logout.json",  
                dataType:"json",
                data:'', 
                success : function(data) {  
                  if (data.message.operateCode=='102008') {
                  window.location.href = "http://me.mo4u.cn:8093";
                  }else{
                    alert(data.message.message);
                  }
                },  
                error : function(data) {  
                    alert(data.message.message);
                }  
            }); 
       }
        
    
          function getCookie(name){
              var arr = document.cookie.split("; ");
              for(var i=0,len=arr.length;i<len;i++){
                var  item = arr[i].split("=");
              if(item[0]==name){
                return item[1];
                  }
                }
                return "";
              }

$('.addressBookId').on('click',function(){
  $('.addressBookId').each(function(){
    $(this).removeAttr('checked');
    $(this).next('span').css('color','#333');
  })
  $(this).attr('checked','checked');
  $(this).next('span').css('color','#D42F09');
})
            // 提交订单
            function submitPay(){
              if($("input[name=payment]").is(':checked') == false){
                    alert(flow_no_payment);
              }else{
                var tid ='';
                $('.tid').each(function(d,f){
                    tid+= 'tid='+f.value+'&';
                });
                tid = tid.substring(0,tid.length-1);
                var orderId = $('.cartId').val();
                var addressBookId ='';
                $('.addressBookId').each(function(){
                  if ($(this).is(':checked')) {
                     addressBookId= $(this).val();
                  };
                })
                //console.log(addressBookId);
                // console.log("/buy/settleUp.shtml?addressBookId="+addressBookId+'&'+tid+"&payTypeId="+$("input[name=payment]:checked").val()+"&orderId="+ orderId)
				        location.href="/buy/settleUp.shtml?addressBookId="+addressBookId+'&'+tid+"&payTypeId="+$("input[name=payment]:checked").val()+"&orderId="+ orderId;
                // 1.addressBookId 收货地址id
                // 2.openIDStr  tid
                // 3.payTypeId  支付类型
                // 4.orderId    购物车idsd
              }
            }
// 选择城市插件
$("#citys").citySelect({
    url:"/theme/${theme}/js/city.min.js",  
    prov:provs, //省份 
    city:citys, //城市 
    dist:dist, //区县 
    nodata:"none" //当子集无数据时，隐藏select 
});
$('#setDefaultAdd').on('click',function(){
  if ($('#setDefaultAdd').is(':checked')) {
        $('#setDefaultAdd').attr('datavalue','Y');
      }else{
        $('#setDefaultAdd').attr('datavalue','N');
      };
})
$('#addsite').on('click',function(){
  $('.site').css('display','block');
  $('.sitebox h3').text('新增收货地址');
  $('.modificationbtn').css('display','none');
  $('.confirm').css('display','inline-block');
  $('.site input[type="text"]').each(function(){
    $(this).attr('value','');
  });
  $('.site input[type="checkbox"]').removeAttr('checked');
  $('.site textarea').attr('value','');
}) 
$('.close').on('click',function(){
  $('.site').css('display','none');
});

// 新增收货地址
function createAddress(){
  var mobile = $('#mobile').val();
  var zipcode = $('#zipcode').val();
  if ($('#address').val().length == 0) {
    alert('请填写详细地址！');
  }else if(!(/^[1-9][0-9]{5}$/.test(zipcode))){
    alert('请填写正确的邮编地址（开头不能为0，由6位数字组成）！');
  }else if($('#name').val().length == 0){
    alert('请填写收货人姓名！');
  }else if(!(/^1(3|4|5|7|8)\d{9}$/.test(mobile))){
    alert('请填写正确格式的手机号码！');
  }else{
      $.ajax({     
          type : "post",  
          url : "/addressBook/create.json",  
          dataType:"json",
          data:{
              province:$("#prov  option:selected").text(),
              city:$("#city  option:selected").text(),
              district:$("#dist  option:selected").text(),
              address:$('#address').val(),
              contact:$('#name').val(),
              phone:$('#tel').val(),
              mobile:$('#mobile').val(),
              postcode:$('#zipcode').val(),
              setDefaultAdd:$('#setDefaultAdd').attr('datavalue')
          }, 
          success : function(data) { 
              if (data.message.operateCode=='102008') {
                  alert(data.message.message);
                  location.reload(); 
              }else{
                  alert(data.message.message);
                  return false;    
                  }
          },  
          error : function(data) {  
              alert(data.message.message);
          }  
      });  
  }
}

// 设置默认地址
function one(a){
    // console.log(a);
    $.ajax({  
        type : "get",  
        url : "/addressBook/setDefault/"+a+".json",  
        dataType:"json",
        data:{}, 
        success : function(data) {
            if (data.message.operateCode=='102008') {
                alert(data.message.message);
                location.reload(); 
            }else{
                alert(data.message.message);
                return false;    
                }
        },  
        error : function(data) {  
            alert(data.message.message);
        }  
    });
}
// 删除地址记录
function deletes(a){
  if (confirm('确定删除该条收货地址信息吗？')) {
     $.ajax({  
      type : "post",  
      url : "/addressBook/delete/"+a+".json",  
      dataType:"json",
      data:{}, 
      success : function(data) { 
          if (data.message.operateCode=='102008') {
              alert(data.message.message);
              location.reload(); 
          }else{
              alert(data.message.message);
              return false;    
              }
      },  
      error : function(data) {  
          alert(data.message.message);
      }  
  }); 
  } 
}

// 修改地址
$('.modification').on('click',function(){
  var dataaddressBookId = $(this).attr('dataaddressBookId');
  $('.sitebox h3').text('修改收货地址');
  $('.sitebox h3').attr('dataaddressBookId',dataaddressBookId);
  $('.site').css('display','block');
  $('.modificationbtn').css('display','inline-block');
  $('.confirm').css('display','none');
  var spantext = $(this).parents('tr').find('td span'); 
  var citygh = [];
  spantext.each(function(){
    var classN = $(this).attr('class');
    // console.log(classN);
    var text = $(this).text();
    var idName = $('.sitetext').find('#'+classN);
    // console.log(idName);
    idName.attr('value',text);
    if (classN=='prov'||classN=='city'||classN=='dist') {
      citygh.push(text);
      // console.log(text);
    };
  })
  // console.log(citygh);

  cityselects(citygh[0],citygh[1],citygh[2]);
  if ($(this).parent().find('.setDefaultAdd').is(':checked')) {
    $('.sitetext').find('#setDefaultAdd').attr('checked','checked');
  }else{
    $('.sitetext').find('#setDefaultAdd').removeAttr('checked');
  }

})
var provs ='北京';
var citys = '东城区';
var dists = '';
// 选择城市插件
function cityselects(provs,citys,dists){
  $("#citys").citySelect({
    url:"/theme/${theme}/js/city.min.js",  
    prov:provs, //省份 
    city:citys, //城市 
    dist:dists, //区县 
    nodata:"none" //当子集无数据时，隐藏select 
});
}
cityselects(provs,citys,dists);


$('.modificationbtn').on('click',function(){
  var mobile = $('#mobile').val();
  var zipcode = $('#zipcode').val();
  if ($('#address').val().length == 0) {
    alert('请填写详细地址！');
  }else if(!(/^[1-9][0-9]{5}$/.test(zipcode))){
    alert('请填写正确的邮编地址（开头不能为0，由6位数字组成）！');
  }else if($('#name').val().length == 0){
    alert('请填写收货人姓名！');
  }else if(!(/^1(3|4|5|7|8)\d{9}$/.test(mobile))){
    alert('请填写正确格式的手机号码！');
  }else{
      var id = $('.sitebox h3').attr('dataaddressBookId');
      // console.log(id);
      $.ajax({  
          type : "post",  
          url : "/addressBook/update.json",  
          dataType:"json",
          data:{
              province:$("#prov  option:selected").text(),
              city:$("#city  option:selected").text(),
              district:$("#dist  option:selected").text(),
              address:$('#address').val(),
              contact:$('#name').val(),
              phone:$('#tel').val(),
              mobile:$('#mobile').val(),
              postcode:$('#zipcode').val(),
              setDefaultAdd:$('#setDefaultAdd').attr('datavalue'),
              addressBookId:id
          }, 
          success : function(data) { 
              if (data.message.operateCode=='102008') {
                  alert(data.message.message);
                  //location.reload(); 
              }else{
                  alert(data.message.message);
                  return false;    
                  }
          },  
          error : function(data) {  
              alert(data.message.message);
          }  
      });  
  } 
})   
var trLength = $('#addresstable tr').length;
// console.log(trLength-1);
if (trLength>3) {
  $('#addresstable tr').slice(4).addClass('trnone');
  $('#addresstable').after('<p><span class="qitaspan">↓ 使用其他地址</span></p>');
  $('#addresstable').after('<p><span class="shouqispan" style="display:none;">↑ 收起地址</span></p>');
}else{
  $('#addresstable tr').removeClass('trnone');
  $('.shouqispan').css('display','none');$('.qitaspan').css('display','none');
};
$('.qitaspan').on('click',function(){
  $('#addresstable tr').removeClass('trnone');
  $('.shouqispan').css('display','block');
  $(this).css('display','none');
});
$('.shouqispan').on('click',function(){
  $('#addresstable tr').slice(4).addClass('trnone');
  $('.qitaspan').css('display','block');
  $(this).css('display','none');

});


    </script>  
                



</div>

            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

</body>
</html>

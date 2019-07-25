<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>${systemName}-购物车</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="../../../../theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../../../../theme/${theme}/js/common.js" ></script><script type="text/javascript" src="../../../../theme/${theme}/js/shopping_flow.js" ></script></head>
<style type="text/css">
  .inputBg{
    height: auto!important;
    width: 50px;
  }
</style>
<body>
 <script type="text/javascript" src="../../../../theme/${theme}/js/jquery-1.11.3.min.js" ></script><script type="text/javascript" src="../../../../theme/${theme}/js/jquery.json.js" ></script> <script type="text/javascript" src="../../../../theme/${theme}/js/transport_jquery.js"></script><script type="text/javascript" src="../../../../theme/${theme}/js/utils.js" ></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 

  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/" >首页</a> <code>&gt;</code> 购物流程 
</div>
</div>
<div class="blank"></div>
 
<div class="block table">
  
  
  
<script type="text/javascript">
      var user_name_empty = "请输入您的用户名！";
      var email_address_empty = "请输入您的电子邮件地址！";
      var email_address_error = "您输入的电子邮件地址格式不正确！";
      var new_password_empty = "请输入您的新密码！";
      var confirm_password_empty = "请输入您的确认密码！";
      var both_password_error = "您两次输入的密码不一致！";
      var show_div_text = "请点击更新购物车按钮";
      var show_div_exit = "关闭";
    </script>
  <div class="flowBox">
    <h6><span>商品列表</span></h6>
        <form id="formCart" name="formCart" method="post" action="http://www.ktwwg.top/flow.php">
           <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
            <tr>
              <th bgcolor="#ffffff" ><span style="float: left;"><input type="checkbox" class="allselect" style="vertical-align: middle;"/>全选</span>商品名称</th>
                    <th bgcolor="#ffffff">属性</th>
                                          <th bgcolor="#ffffff">市场价</th>
                            <th bgcolor="#ffffff">本店价</th>
              <th bgcolor="#ffffff">购买数量</th>
              <th bgcolor="#ffffff">小计</th>
              <th bgcolor="#ffffff">操作</th>
            </tr>
            <c:if test="${empty itemMap}">
              <tr style="background-color: #fff;">
                <td colspan="7" style="padding: 27px 10px;">您还没有添加任何商品！</td>
              </tr>
            </c:if>
            <c:forEach items="${itemMap}" var="a">
                                 <tr>
                                   <input type="hidden" value="${a.value.transactionId}" class="tid">
                                   <input type="hidden" value="${a.value.cartId}" class="cartId">
              <td bgcolor="#ffffff" align="center" style="width:300px;">
                <input type="checkbox" style="vertical-align: top;margin-right:20px;"/>
                  <a href="${a.value.itemDataMap.refUrl.dataValue}" target="_blank"><img style="width:80px; height:80px;" src="/file/${a.value.itemDataMap.productSmallImage.dataValue}" border="0" title=""></a><br>
                  <a href="${a.value.itemDataMap.refUrl.dataValue}" target="_blank" class="f6">${a.value.name}</a>
              </td>
              <td bgcolor="#ffffff"></td>
              <td align="center" bgcolor="#ffffff">￥<span>${a.value.price.marketPrice}</span>元</td>
              <td align="center" bgcolor="#ffffff">￥<span>${a.value.price.money}</span>元</td>
              <td align="center" bgcolor="#ffffff">
                <input type="number" name="number" id="" value="${a.value.count}" size="4" class="inputBg" style="text-align:center "min="1">
              </td>
              <td align="center" bgcolor="#ffffff">￥<span class="total">${a.value.price.money*a.value.count}</span>元</td>
              <td align="center" bgcolor="#ffffff">
                <span style="cursor: pointer;" class="remove" itemId="${a.value.transactionId}">删除</span>
                            </td>
            </tr>
            </c:forEach>
            </table>
          <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
           
            <tr>
              <td bgcolor="#ffffff">
                            结算金额小计 ￥<span id="totalCart" >0</span>元，比市场价 ￥<span id="pulicCart">0</span>元 节省了 ￥<span id="cart">0</span>元               </td>
              <td align="right" bgcolor="#ffffff">
                <input type="button" value="清空购物车" class="bnt_blue_1  empty" />
                <a href="/cart.shtml" class="bnt_blue_1">更新购物车</a>
              </td>
            </tr> 
          </table>
          <input type="hidden" name="step" value="update_cart" />
        </form>
        <table width="99%" align="center" border="0" cellpadding="5" cellspacing="0" bgcolor="#dddddd">
          <tr>
            <td bgcolor="#ffffff"><a href="index.htm" ><img src="../../../../theme/${theme}/images/continue.gif" alt="continue" /></a></td>
            <td bgcolor="#ffffff" align="right"><a href="#"><img src="../../../../theme/${theme}/images/checkout.gif"  alt="checkout" id="balance" /></a></td>
          </tr>    
        </table>
       </div>
       <script>
        //  检验是否登录
          $(function(){
         $('input[type="checkbox"]').change(function(){
          if ($(this).hasClass('allselect')) {
            if ($(this).is(':checked')) {
              $('input[type="checkbox"]').each(function(){
                $(this).prop('checked',true);
              })
            }else{
              $('input[type="checkbox"]').each(function(){
                $(this).prop('checked',false);
              })
            }
          }else{
            if ($(this).is(':checked')){
              
            }else{
              $('.allselect').prop('checked',false);
            }
          };
           $('input[type="checkbox"]').each(function(){
                if ($(this).is(':checked')) {
                  if ($(this).hasClass('allselect')) {
                    $(this).removeClass('xzsel');
                  }else{
                    $(this).addClass('xzsel');
                  }
                
              }else{
                $(this).removeClass('xzsel');
              };
           })
          
          var num = 0;var nub = 0;
          $('.xzsel').each(function(){
            num += returnFloat($(this).parents('tr').find('td').eq(2).find('span').text())*$(this).parents('tr').find('input[type="number"]').val();
            nub += returnFloat($(this).parents('tr').find('.total').text())*1;
          })
          $('#pulicCart').text(num);
          $('#totalCart').text(nub);
           // 节省金额
            $('#cart').text(returnFloat(num-nub));
         })
            
            function price_calculation(){
            // 市场价总计
            var num = 0;

            $('input[type="number"]').each(function(){
              if ($(this).parents('tr').find('td').eq(0).find('input[type="checkbox"]').is(':checked')){
                  num += returnFloat($(this).parent().siblings().eq(4).text().substring(1,$(this).parent().siblings().eq(4).text().length-1))*$(this).val();
              }else{
                return false;
              };

                 
            })
            $('#pulicCart').text(num);

            // 本店价总计
            var nub = 0;
            $('.total').each(function(){
              if ($(this).parents('tr').find('td').eq(0).find('input[type="checkbox"]').is(':checked')){
                nub += returnFloat($(this).text())*1;
              }
               
            })
            $('#totalCart').text(nub);


            // 节省金额
            $('#cart').text(returnFloat(num-nub));
          }
          price_calculation();
          
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
        
        // 登出按钮
       function secede(){
          $.ajax({  
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


          //    结算按钮
          $("#balance").click(function() {
              if(getCookie('eis_username') == ''){
                  alert("请先登录！");
                  return false;
              }else if($('#formCart table').eq(0).find('tr').length == 1){
                  alert('您没有任何商品！');
                  return false;
              }else{
                  var a ="";
                  if ($('.xzsel').length>0) {
                  $('.xzsel').parents('tr').find('.tid').each(function (i, d) {
                          a+='tid='+d.value+'&';
                  });
                  a=a.substring(0,a.length-1);
                  var cartId = $('.cartId').val();
                  location.href='/buy/delivery.shtml?'+a;
                  }else{
                    alert('您没有选择任何结算商品！');
                  }
                };
              })
          // 删除
          $('span.remove').on('click',function(){
            var itemId = $(this).attr('itemId');
            if (confirm("确定要删除商品吗？")) {
              $.ajax({
              type:'post',
              url:'cart/delete.json',
              dataType:'json',
              data:{
                transactionIds:itemId
              },
              success:function(data){
                window.location.reload();
              },
              error:function(data){
                alert(data.message.message);
              }
            })
            }
            
          })
          // 清空
          $('.empty').on('click',function(){
            var transactionIds = '';
            $('span.remove').each(function(){
              var ids = $(this).attr('itemId');
              transactionIds += ids+',';
            })
            
            if (confirm("确定要清空购物车吗？")) {
              $.ajax({
              type:'post',
              url:'cart/clear.json',
              dataType:'json',
              data:{
                transactionIds:transactionIds
              },
              success:function(data){
                window.location.reload();
              },
              error:function(data){
                alert(data.message.message);
              }
            })
            }
          })

          $('input[type="number"]').each(function(){
            var nums = $(this).val();
            var price = $(this).parent().prev('td').find('span').text();
            $(this).parent().next('td').find('span').text(price*nums);
          })
          $('input[type="number"]').change(function(){
            var nums = $(this).val();
            var price = $(this).parent().prev('td').find('span').text();
            $(this).parent().next('td').find('span').text(price*nums);
            price_calculation();
          })
          });
          // 商品数量变化
          $('input[type="number"]').change(function(){
            var num = $(this).val();
            var transactionId = $('.tid').attr('value');
            $.ajax({
              type:'post',
              url:'cart/update.json',
              dataType:'json',
              data:{
                transactionId:transactionId,
                count:num
              },
              success:function(data){
                //price_calculation();
              },
              error:function(data){
                alert(data.message.message);
              }
            })
          })

       </script>
    <div class="blank"></div>
   
    <div class="blank5"></div>
  
  
  

        
        
                



</div>


<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    


</body>
<script type="text/javascript">
var process_request = "正在处理您的请求...";
var username_empty = "- 用户名不能为空。";
var username_shorter = "- 用户名长度不能少于 3 个字符。";
var username_invalid = "- 用户名只能是由字母数字以及下划线组成。";
var password_empty = "- 登录密码不能为空。";
var password_shorter = "- 登录密码不能少于 6 个字符。";
var confirm_password_invalid = "- 两次输入密码不一致";
var email_empty = "- Email 为空";
var email_invalid = "- Email 不是合法的地址";
var agreement = "- 您没有接受协议";
var msn_invalid = "- msn地址不是一个有效的邮件地址";
var qq_invalid = "- QQ号码不是一个有效的号码";
var home_phone_invalid = "- 家庭电话不是一个有效号码";
var office_phone_invalid = "- 办公电话不是一个有效号码";
var mobile_phone_invalid = "- 手机号码不是一个有效号码";
var msg_un_blank = "* 用户名不能为空";
var msg_un_length = "* 用户名最长不得超过7个汉字";
var msg_un_format = "* 用户名含有非法字符";
var msg_un_registered = "* 用户名已经存在,请重新输入";
var msg_can_rg = "* 可以注册";
var msg_email_blank = "* 邮件地址不能为空";
var msg_email_registered = "* 邮箱已存在,请重新输入";
var msg_email_format = "* 邮件地址不合法";
var msg_blank = "不能为空";
var no_select_question = "- 您没有完成密码提示问题的操作";
var passwd_balnk = "- 密码中不能包含空格";
var username_exist = "用户名 %s 已经存在";
var compare_no_goods = "您没有选定任何需要比较的商品或者比较的商品数少于 2 个。";
var btn_buy = "购买";
var is_cancel = "取消";
var select_spe = "请选择商品属性";


</script>

</html>

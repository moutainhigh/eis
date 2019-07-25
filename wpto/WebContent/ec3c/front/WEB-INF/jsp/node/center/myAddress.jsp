<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>${systemName}-用户中心</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" src="js/user.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>
<script  type="text/javascript" src="/theme/${theme}/js/selectCity.js"></script>
</head>
<body>


<style>
    .userCenterBox table tbody tr{
        background: #fff;
    }
    .userCenterBox table tbody tr:nth-child(even) {
        background: #F8F8F8;
    }
    td {
    text-align: center;
}
/*弹窗*/
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
 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
<div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/">首页</a> <code>&gt;</code> 用户中心 
</div>
</div>
<div class="blank"></div>

<div class="block clearfix">
  
  <div class="AreaL">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox">
        <div class="userMenu">
<a href="/content/center/center.shtml"><img src="/theme/${theme}/images/u1.gif"> 欢迎页</a>
<a href="/content/center/userMessage.shtml"><img src="/theme/${theme}/images/u2.gif"> 用户信息</a>
<a href="/content/center/myOrders.shtml"><img src="/theme/${theme}/images/u3.gif"> 我的订单</a>
<a href="/content/center/myAddress.shtml" class="curs"><img src="/theme/${theme}/images/u4.gif"> 收货地址</a>
<a href="/content/center/myLove.shtml"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
<a href="/content/center/myWords.shtml"><img src="/theme/${theme}/images/u6.gif"> 我的留言</a>
<a href="/content/center/myLabel.shtml"><img src="/theme/${theme}/images/u7.gif"> 我的标签</a>
<a href="/content/center/shortsSupply.shtml"><img src="/theme/${theme}/images/u8.gif"> 缺货登记</a>
<a href="/content/center/myPacket.shtml"><img src="/theme/${theme}/images/u9.gif"> 我的红包</a>
<a href="/content/center/myRecommend.shtml"><img src="/theme/${theme}/images/u10.gif"> 我的推荐</a>
<a href="/content/center/myComments.shtml"><img src="/theme/${theme}/images/u11.gif"> 我的评论</a>
<!--<a href="user.php?act=group_buy">我的团购</a>-->
<a href="/content/center/trackingPackages.shtml"><img src="/theme/${theme}/images/u12.gif"> 跟踪包裹</a>
<a href="/content/center/fundManagement.shtml"><img src="/theme/${theme}/images/u13.gif"> 资金管理</a>
<a href="/" style="background:none; text-align:right; margin-right:10px;"><img src="/theme/${theme}/images/bnt_sign.gif" onclick="secede()"></a>
</div>      </div>
     </div>
    </div>
  </div>
  <div class="AreaR">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox boxCenterList clearfix" style="_height:1%;" id="notLogin">
            <h5><span>收货人信息</span><span class="f6" id="addsite" style="float: right;cursor: pointer;">新增收货地址</span></h5>
            <div class="blank"></div>

         <div class="table">
            <input type="hidden" value="${frontUser.userConfigMap.userBindMailBox.dataValue}" id="emails">
            <form action="" method="post" name="theForm" id="theForm" onsubmit="return checkConsignee(this)">
            <div class="flowBox">
                <table width="99%" align="center" border="0" cellpadding="4" cellspacing="1" bgcolor="#dddddd"> 
                    <tr style="background-color: #eee; line-height: 26px;"><th>收货人</th><th>所在地区</th><th>详细地址</th><th>邮政编码</th><th>手机</th><th>电话</th><th>操作</th></tr>
                    <tbody id="tbody">
                        
                    </tbody>
                </table>
            </div>        
        </form>

        </div>       


    </div>
      </div>
     </div>
    </div>
  </div>
<div class="blank"></div>
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
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    


</body>
<script type="text/javascript">         
var bonus_sn_empty = "请输入您要添加的红包号码！";
var bonus_sn_error = "您输入的红包号码格式不正确！";
var email_empty = "请输入您的电子邮件地址！";
var email_error = "您输入的电子邮件地址格式不正确！";
var phone_error = "您输入的手机号格式不正确！";
var old_password_empty = "请输入您的原密码！";
var new_password_empty = "请输入您的新密码！";
var confirm_password_empty = "请输入您的确认密码！";
var both_password_error = "您现两次输入的密码不一致！";
var msg_blank = "不能为空";
var no_select_question = "- 您没有完成密码提示问题的操作";
var status = "[{'currentStatus':'710001','content':'需要处理'},{'currentStatus':'710002','content':'接受但还未成功'},{'currentStatus':'710003','content':'部分成功'},{'currentStatus':'710004','content':'已关闭'},{'currentStatus':'710005','content':'等待竞拍结果'},{'currentStatus':'710007','content':'竞拍成功'},{'currentStatus':'710010','content':'交易成功'},{'currentStatus':'710011','content':'交易失败'},{'currentStatus':'710013','content':'交易处理中'},{'currentStatus':'710017','content':'交易超时'},{'currentStatus':'710018', 'content':'暂存在购物车'},{'currentStatus':'710019','content':'交易等待付款'},{'currentStatus':'710021','content':'新订单'},{'currentStatus':'710022', 'content':'未开放'},{'currentStatus':'710023','content':'资金更新失败'},{'currentStatus':'710024','content':'系统异常'},{'currentStatus':'710025','content':'需返回原状态'},{'currentStatus':'710026','content':'等待异步处理结果'},{'currentStatus':'710027','content':'不需要进行处理'},{'currentStatus':'710028','content':'部分完成'},{'currentStatus':'710029','content':'需再次验证'},{'currentStatus':'710030','content':'强行关闭'},{'currentStatus':'710031','content':'成功总额大于标签价格'},{'currentStatus':'710032','content':'强制成功'},{'currentStatus':'710033','content':'需要回滚之前的交易'},{'currentStatus':'710034','content':'由失败转为成功'},{'currentStatus':'710037','content':'已完成所有验证'},{'currentStatus':'710038','content':'需要最后一次匹配'},{'currentStatus':'710039','content':'等待验证结果'},{'currentStatus':'710040','content':'等待其他返回结果'},{'currentStatus':'710041','content':'需要匹配补单'},{'currentStatus':'710042','content':'强行将冻结金额改为请求金额'},{'currentStatus':'710043','content':'只可用于宽松匹配'},{'currentStatus':'710044', 'content':'释放并暂停使用'},{'currentStatus':'710045','content':'只可用于同区域匹配'},{'currentStatus':'710046','content':'需要备份节点处理'},{'currentStatus':'710047','content':'已确认收货' },{'currentStatus':'710048','content':'延迟处理'},{'currentStatus':'710049','content':'预定成功'},{'currentStatus':'710050','content':'发货中'},{'currentStatus':'710051','content':'准备发货'},{'currentStatus':'710052','content':'待评价'},{'currentStatus':'710053','content':'评价已完成'},{'currentStatus':'710054','content':'退款中'},{'currentStatus':'710055','content':'已退款'},{'currentStatus':'710056','content':'等待扣款'},{'currentStatus':'710057','content':'等待匹配'}]";
 


//   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     }
     
 })


        var i = -5;
        var nub = 0;
        // 页面加载开始更新配送地址页面
      		$(function(){ 
                $('#tbody').empty();

                // 选择城市插件
                // $("#citys").citySelect({
                //     url:"/theme/${theme}/js/city.min.js",  
                //     prov:"北京", //省份 
                //     city:"东城区", //城市 
                //     dist:"", //区县 
                //     nodata:"none" //当子集无数据时，隐藏select 
                // }); 
                $.ajax({  
                    type : "get",  
                    url : "/addressBook/index.json",  
                    dataType:"json",
                    data:'', 
                    success : function(data) { 
                            var addressBooks = data.addressBook.map(function(a){
                                i = i+5;
                                if (a.currentStatus=='100003') {
                                    return ('<tr><td bgcolor="#ffffff" style="width:10%;"><span class="name">'+a.contact+'</span></td><td bgcolor="#ffffff" style="width:15%;"><span class="prov">'+a.province+'</span><span class="city">'+a.city+'</span><span class="dist">'+a.district+'</span></td><td bgcolor="#ffffff" style="width:20%;"><span class="address">'+a.address+'</span></td><td bgcolor="#ffffff" style="width:10%;"><span class="zipcode">'+a.postcode+'</span></td><td bgcolor="#ffffff" style="width:10%;"><span class="mobile">'+a.mobile+'</span></td><td bgcolor="#ffffff" style="width:10%;"><span class="tel">'+a.phone+'</span></td><td bgcolor="#ffffff" class="handle"><span style="margin-right: 15px;"><input type="radio" onclick="one('+a.addressBookId+')" class="setDefaultAdd" name="default_address" checked /><span style=" color: #D42F09;">默认地址</span></span><span class="modification" dataaddressBookId="'+a.addressBookId+'">修改</span> | <span class="delete" onclick="deletes('+a.addressBookId+')">删除</span></td></tr>');
                                }else{
                                    return ('<tr><td bgcolor="#ffffff" style="width:10%;"><span class="name">'+a.contact+'</span></td><td bgcolor="#ffffff" style="width:15%;"><span class="prov">'+a.province+'</span><span class="city">'+a.city+'</span><span class="dist">'+a.district+'</span></td><td bgcolor="#ffffff" style="width:20%;"><span class="address">'+a.address+'</span></td><td bgcolor="#ffffff" style="width:10%;"><span class="zipcode">'+a.postcode+'</span></td><td bgcolor="#ffffff" style="width:10%;"><span class="mobile">'+a.mobile+'</span></td><td bgcolor="#ffffff" style="width:10%;"><span class="tel">'+a.phone+'</span></td><td bgcolor="#ffffff" class="handle"><span style="margin-right: 15px;"><input type="radio" onclick="one('+a.addressBookId+')" class="setDefaultAdd" name="default_address" /><span style=" color: #D42F09;">设为默认</span></span><span class="modification" dataaddressBookId="'+a.addressBookId+'">修改</span> | <span class="delete" onclick="deletes('+a.addressBookId+')">删除</span></td></tr>');
                                }
                                
                                     
                            });
                            $('#tbody').append(addressBooks);
                            // 修改地址

                            
                     
                        
                    },  
                    error : function(data) {  
                        alert(data.message.message);
                    }  
                });  
			});
$(window).load(function(){
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
    idName.val(text);
    if (classN=='prov'||classN=='city'||classN=='dist') {
      citygh.push(text);
      
    };console.log(text);
  })
  //console.log(text);

  cityselects(citygh[0],citygh[1],citygh[2]);
  if ($(this).parent().find('.setDefaultAdd').is(':checked')) {
    $('.sitetext').find('#setDefaultAdd').attr('checked','checked');
  }else{
    $('.sitetext').find('#setDefaultAdd').removeAttr('checked');
  }

});
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
      console.log(id);
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
})  
});
            
            

function cityselects(provs,citys,dists){
  $("#citys").citySelect({
    url:"/theme/${theme}/js/city.min.js",  
    prov:provs, //省份 
    city:citys, //城市 
    dist:dists, //区县 
    nodata:"none" //当子集无数据时，隐藏select 
    });
};
$("#citys").citySelect({
    url:"/theme/${theme}/js/city.min.js",  
    prov:'北京', //省份 
    city:'东城区', //城市 
    dist:'', //区县 
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

// 新增配送地址按钮
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

            // 设置默认地址
            function one(a){
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
            </script>

</html>









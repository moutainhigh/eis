<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>用户中心_${systemName}</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" src="js/user.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>
</head>
<body>


<style>
    .userCenterBox table tbody tr{
        background: #fff;
    }
    .userCenterBox table tbody tr:nth-child(even) {
        background: #fff;
    }
    .order_status span{
        display: inline-block;
        padding: 2px 9px;
        font-size: 13px;

    }
    .order_status span.show{
        color: #B92020;
        border-bottom: 2px solid;
    }
    .none{
        display: none;
    }
    td{
        text-align: center;
    }
    .products p{
        text-align: left;
        line-height: 25px;
        cursor: pointer;
    }
    .payment{
        display: inline-block;
        padding: 2px 6px;
        color: #F10F0F;
        border-radius: 4px;
        cursor: pointer;
        /*border: 1px solid rgba(241, 15, 15, 0.52);*/

    }
    .evaluate{
        display: inline-block;
        padding: 2px 6px;
        color: #FF6700;
        border-radius: 4px;
        cursor: pointer;
        /*border:1px solid rgba(255, 127, 15, 0.63);*/
    }
    /*.delete{
        display: inline-block;
        padding: 2px 6px;
        color: #333;
        border-radius: 4px;
        cursor: pointer;
        float: right;
        border: 1px solid rgba(51, 51, 51, 0.55);
    }*/
    /*.caozuo p span{
        margin: 0 2px;
    }*/
    /*.caozuo>p{
        display: inline-block;
        text-align: left;
        width: 65%;
    }*/
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
<a href="/content/center/myOrders.shtml" class="curs"><img src="/theme/${theme}/images/u3.gif"> 我的订单</a>
<a href="/content/center/myAddress.shtml"><img src="/theme/${theme}/images/u4.gif"> 收货地址</a>
<a href="/content/center/myLove.shtml"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
<a href="/content/center/myWords.shtml" ><img src="/theme/${theme}/images/u6.gif"> 我的留言</a>
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
             <h5><span>我的订单</span></h5>
                <div class="blank"></div>
                <div class="order_status">
                    <span class="show all" onclick="findCart()">所有订单</span>
                    <!-- <span class="obligation">待付款</span>
                    <span class="overhang">待发货</span>
                    <span class="receiving">待收货</span>
                    <span class="evaluated">待评价</span> -->
                </div>
                <div class="blank"></div>
                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
                    <thead>
                       <th bgcolor="#eee">商品</th>
                       <th bgcolor="#eee">订单号</th>
                       <th bgcolor="#eee">下单时间</th>
                       <th bgcolor="#eee">订单金额</th>
                       <th bgcolor="#eee">订单状态</th>
                       <th bgcolor="#eee">操作</th>
                    </thead>
                    <!--<tr align="center">
                        <td bgcolor="#ffffff">订单号</td>
                        <td bgcolor="#ffffff">下单时间</td>
                        <td bgcolor="#ffffff">订单总金额</td>
                        <td bgcolor="#ffffff">订单状态</td>
                        <td bgcolor="#ffffff">操作</td>
                    </tr>-->
                    <tbody id="List">
                        
                    </tbody>
                </table>
                    <div class="blank5"></div>
                    <div id="pager" class="pagebar">
                            <span class="f_l " style="margin-right:10px;">总共 <b>0</b>  页</span> <span class="f_l"> 当前第 <span class="currentpage"></span> 页 </span>
                            <span> 
                                <a onclick="gotoPage()" style="cursor: pointer;">  首页</a> 
                                <a onclick="goOldPage()" style="cursor: pointer;">上一页</a> 
                                <a onclick="goNextPage()" style="cursor: pointer;">下一页</a> 
                                <a onclick="goLastPage()" style="cursor: pointer;">尾页</a> 
                            </span>
                    </div>  


    </div>
      </div>
     </div>
    </div>
  </div>
<div class="blank"></div>

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

var status = '{"list":[' +
	'{"currentStatus":"710001","content":"需要处理" },' +
	'{"currentStatus":"710002","content":"接受但还未成功" },' +
	'{"currentStatus":"710003","content":"部分成功" },' +
	'{"currentStatus":"710004","content":"已关闭" },' +
	'{"currentStatus":"710005","content":"等待竞拍结果" },' +
	'{"currentStatus":"710007","content":"竞拍成功" },' +
	'{"currentStatus":"710010","content":"交易成功" },' +
	'{"currentStatus":"710011","content":"交易失败" },' +
	'{"currentStatus":"710013","content":"交易处理中" },' +
	'{"currentStatus":"710017","content":"交易超时" },' +
	'{"currentStatus":"710018","content":"暂存在购物车" },' +
	'{"currentStatus":"710019","content":"交易等待付款" },' +
	'{"currentStatus":"710021","content":"新订单" },' +
	'{"currentStatus":"710022","content":"未开放" },' +
	'{"currentStatus":"710023","content":"资金更新失败" },' +
	'{"currentStatus":"710024","content":"系统异常" },' +
	'{"currentStatus":"710025","content":"需返回原状态" },' +
	'{"currentStatus":"710026","content":"等待异步处理结果" },' +
	'{"currentStatus":"710027","content":"不需要进行处理" },' +
	'{"currentStatus":"710028","content":"部分完成" },' +
	'{"currentStatus":"710029","content":"需再次验证" },' +
	'{"currentStatus":"710030","content":"强行关闭" },' +
	'{"currentStatus":"710031","content":"成功总额大于标签价格" },' +
	'{"currentStatus":"710032","content":"强制成功" },' +
	'{"currentStatus":"710033","content":"需要回滚之前的交易" },' +
	'{"currentStatus":"710034","content":"由失败转为成功" },' +
	'{"currentStatus":"710037","content":"已完成所有验证" },' +
	'{"currentStatus":"710038","content":"需要最后一次匹配" },' +
	'{"currentStatus":"710039","content":"等待验证结果" },' +
	'{"currentStatus":"710040","content":"等待其他返回结果" },' +
	'{"currentStatus":"710041","content":"需要匹配补单" },' +
	'{"currentStatus":"710042","content":"强行将冻结金额改为请求金额" },' +
	'{"currentStatus":"710043","content":"只可用于宽松匹配" },' +
	'{"currentStatus":"710044","content":"释放并暂停使用" },' +
	'{"currentStatus":"710045","content":"只可用于同区域匹配" },' +
	'{"currentStatus":"710046","content":"需要备份节点处理" },' +
	'{"currentStatus":"710047","content":"已确认收货" },' +
	'{"currentStatus":"710048","content":"延迟处理" },' +
	'{"currentStatus":"710049","content":"预定成功" },' +
	'{"currentStatus":"710050","content":"发货中" },' +
	'{"currentStatus":"710051","content":"准备发货" },' +
	'{"currentStatus":"710052","content":"待评价" },' +
	'{"currentStatus":"710053","content":"评价已完成" },' +
	'{"currentStatus":"710054","content":"退款中" },' +
	'{"currentStatus":"710055","content":"已退款" },' +
	'{"currentStatus":"710056","content":"等待扣款" },' +
	'{"currentStatus":"710057","content":"等待匹配" }]}';
var lists = JSON.parse(status);

// 总页数
var totalPage;
// 辨识当前加载的是哪一个订单
var url;
// 当前页数
var currentPages;
//   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     }
     
 })
function transid(){
    $.ajax({
        type:"POST",
        url:"/order/index.json",
        data:{},           
        async:false,
        success: function(data) {
            for(var ii = 0; ii < data.orderList.length;ii++){
                for(var a = 0; a < ii.transactionIds.length;a++){
                    $('.products').eq(ii).attr(a,ii.transactionIds[a]);
                }
                
            }
        },
        error: function(XMLResponse) {
            alert("操作失败:" + XMLResponse.responseText);
        }
})};

//  订单查询
 function findCart(){
     url = '/order/index.json';
          $.ajax({
                type:"POST",
                url:"/order/index.json",
                data:{},           
                async:false,
                success: function(data) {
                    $('#List').empty();
                    if(data.orderList == undefined){
                        alert('您还没有订单！');
                        return false;
                    }else{
                        var orderList = data.orderList.map(function(x){
                            for(var i = 0; i < lists.list.length;i++){
                                if(lists.list[i].currentStatus == x.currentStatus){
                                    var transactionIdshtml = '';
                                    for(var a = 0; a < x.transactionIds.length;a++){
                                       transactionIdshtml += "<p>"+x.transactionIds[a]+"</p>";
                                    }
                                    //console.log(transactionIdshtml);
                                    return '<tr><td class="products">'+x.goodsDesc+'</td><td>'+x.cartId+'</td><td>'+x.lastAccessTime+'</td><td>'+x.money.chargeMoney+'</td><td class="currentStatustd">'+lists.list[i].content+'</td><td class="caozuo"></td><td style="display:none;">'+transactionIdshtml+'</td></tr>';
                                    
                                }

                            }});
                        $('#List').append(orderList);
                        
                        
                        $('.f_l b').text(data.paging.totalPage);
                        totalPage = data.paging.totalPage;
                        currentPages = data.paging.currentPage;
                        $('.currentpage').text(currentPages);
                        //findProduct();
                    }
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                }
            })  
 }
findCart();
//  商品查询
function findProduct(){
          url = '/order/item/index.json';
          $.ajax({
                type:"POST",
                url:"/order/item/index.json",
                data:{},           
                async:false,
                success: function(data) {
                    // $('#List').empty();
                    // if(data.orderList == undefined){
                    //     alert('您还没有订单！');
                    //     return false;
                    // }else{
                    //     var orderList = data.orderList.map(function(x){
                    //         for(var i = 0; i < lists.list.length;i++){
                    //             if(lists.list[i].currentStatus == x.currentStatus){
                    //                 return '<tr><td>'+x.transactionId+'</td><td>'+x.enterTime+'</td><td>'+x.price.money+'</td><td>'+lists.list[i].content+'</td></tr>';
                    //         }
                    //     }})
                    //     $('#List').append(orderList);
                    //     $('.f_l b').text(data.paging.totalPage);
                    //     totalPage = data.paging.totalPage;
                    //     currentPages = data.paging.currentPage;
                    //     $('.currentpage').text(currentPages);
                    // }
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                }
         })
}

// 第一页跳转
function gotoPage(){ 
    if($('.f_l b').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else{
        if(currentPages == 1){
            return false;
        }else{
            $.ajax({
                type:"POST",
                url:"/order/index.json",
                data:{
                    rows:10,
                    page:1
                },           
                async:false,
                success: function(data) {
                    $('#List').empty();
                    var orderList = data.orderList.map(function(x){
                    for(var i = 0; i < lists.list.length;i++){
                        if(lists.list[i].currentStatus == x.currentStatus){
                            var transactionIdshtml = '';
                            for(var a = 0; a < x.transactionIds.length;a++){
                               transactionIdshtml += "<p>"+x.transactionIds[a]+"</p>"
                            }
                            return '<tr><td class="products">'+x.goodsDesc+'</td><td>'+x.cartId+'</td><td>'+x.lastAccessTime+'</td><td>'+x.money.chargeMoney+'</td><td class="currentStatustd">'+lists.list[i].content+'</td><td class="caozuo"></td><td style="display:none;">'+transactionIdshtml+'</td></tr>';
                        }
                    }})
                    $('#List').append(orderList);
                    $('.f_l b').text(data.paging.totalPage);
                    totalPage = data.paging.totalPage;
                    currentPages = data.paging.currentPage;
                    $('.currentpage').text(currentPages);
                    productname();
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                }
            })
        }
    }    
}

// 下一页跳转
function goNextPage(){ 
    if($('.f_l b').text() == 0){
        $('#pager').css('display','none');
        return false;
    }else{
        if(currentPages == totalPage){
            return false;
        }else{
            $.ajax({
                type:"POST",
                url:"/order/index.json",
                data:{
                    rows:10,
                    page:currentPages+1
                },           
                async:false,
                success: function(data) {
                    $('#List').empty();
                    var orderList = data.orderList.map(function(x){
                        for(var i = 0; i < lists.list.length;i++){
                            if(lists.list[i].currentStatus == x.currentStatus){
                                var transactionIdshtml = '';
                                    for(var a = 0; a < x.transactionIds.length;a++){
                                       transactionIdshtml += "<p>"+x.transactionIds[a]+"</p>"
                                    }
                                    return '<tr><td class="products">'+x.goodsDesc+'</td><td>'+x.cartId+'</td><td>'+x.lastAccessTime+'</td><td>'+x.money.chargeMoney+'</td><td class="currentStatustd">'+lists.list[i].content+'</td><td class="caozuo"></td><td style="display:none;">'+transactionIdshtml+'</td></tr>';
                            }
                    }})
                    $('#List').append(orderList);
                    $('.f_l b').text(data.paging.totalPage);
                    totalPage = data.paging.totalPage;
                    currentPages = data.paging.currentPage;
                    $('.currentpage').text(currentPages);
                    productname();
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                }
            })
        }
    }    
}

// 上一页跳转
function goOldPage(){ 
    if($('.f_l b').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else{
        if(currentPages == 1){
            return false;
        }else{
            $.ajax({
                type:"POST",
                url:"/order/index.json",
                data:{
                    rows:10,
                    page:currentPages-1
                },           
                async:false,
                success: function(data) {
                    $('#List').empty();
                    var orderList = data.orderList.map(function(x){
                        for(var i = 0; i < lists.list.length;i++){
                            if(lists.list[i].currentStatus == x.currentStatus){
                                var transactionIdshtml = '';
                                    for(var a = 0; a < x.transactionIds.length;a++){
                                       transactionIdshtml += "<p>"+x.transactionIds[a]+"</p>"
                                    }
                                    return '<tr><td class="products">'+x.goodsDesc+'</td><td>'+x.cartId+'</td><td>'+x.lastAccessTime+'</td><td>'+x.money.chargeMoney+'</td><td class="currentStatustd">'+lists.list[i].content+'</td><td class="caozuo"></td><td style="display:none;">'+transactionIdshtml+'</td></tr>';
                            }       
                    }})
                    $('#List').append(orderList);
                    $('.f_l b').text(data.paging.totalPage);
                    totalPage = data.paging.totalPage;
                    currentPages = data.paging.currentPage;
                    $('.currentpage').text(currentPages);
                    productname();
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                }
            })
        }
    }    
}

// 最后一页跳转
function goLastPage(){ 
    if($('.f_l b').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else{
        $.ajax({
            type:"POST",
            url:"/order/index.json",
            data:{
                rows:10,
                page:totalPage
            },           
            async:false,
            success: function(data) {
                $('#List').empty();
                var orderList = data.orderList.map(function(x){
                    for(var i = 0; i < lists.list.length;i++){
                        if(lists.list[i].currentStatus == x.currentStatus){
                            var transactionIdshtml = '';
                                    for(var a = 0; a < x.transactionIds.length;a++){
                                       transactionIdshtml += "<p>"+x.transactionIds[a]+"</p>"
                                    }
                                    return '<tr><td class="products">'+x.goodsDesc+'</td><td>'+x.cartId+'</td><td>'+x.lastAccessTime+'</td><td>'+x.money.chargeMoney+'</td><td class="currentStatustd">'+lists.list[i].content+'</td><td class="caozuo"></td><td style="display:none;">'+transactionIdshtml+'</td></tr>';
                        }    
                }})
                $('#List').append(orderList);
                $('.f_l b').text(data.paging.totalPage);
                totalPage = data.paging.totalPage;
                currentPages = data.paging.currentPage;
                $('.currentpage').text(currentPages);
                    productname();
            },
            error: function(XMLResponse) {
                alert("操作失败:" + XMLResponse.responseText);
            }
        })
    }    
}
// 遍历产品名称
function productname(){
$('.products').each(function(){
    var text = $(this).text();
    var texts = text.split(',');
    $(this).empty();
    for(var i=0;i<texts.length;i++){
        var xx = parseInt(i)+1;
        $(this).append("<p>"+xx+'.'+texts[i]+"</p>");
    }
});
$('.currentStatustd').each(function(){
    var curtext = $(this).text();
    if (curtext=='待评价') {
        var props = $(this).parent().find('.products p');
        for(var i=0;i<props.length;i++){
            var titlet = props.eq(i).text();
            $(this).next('.caozuo').append('<p><span class="evaluate">✎去评价</span></p>');
            $(this).next('.caozuo').find('.evaluate').eq(i).attr('title',titlet);
        }
        // $(this).next('.caozuo').find('p').append('<span class="delete">✘删除订单</span>');
    };
    if (curtext=='评价已完成') {
        var props = $(this).parent().find('.products p');
        for(var i=0;i<props.length;i++){
            $(this).next('.caozuo').append('<p><span class="evaluate">✎再次评价</span></p>');
            // $(this).next('.caozuo').find('p').append('<span class="delete">✘删除订单</span>');
        }
    };
    if (curtext=='交易等待付款') {
        $(this).next('.caozuo').html('<p><span class="payment">↗去付款</span></p>');
        // $(this).next('.caozuo').find('p').append('<span class="delete">✘删除订单</span>')
    };
    if (curtext=='交易成功') {
        // $(this).next('.caozuo').append('<p><span class="delete">✘删除订单</span></p>');
    };
})  
} 
productname();

// 去评价
$('body').on('click','.evaluate',function(){
    var tidsindex = $(this).index();
    var tids = $(this).parents('td').next('td').find('p').eq(tidsindex).text();
    //console.log(tids);
    window.location.href='/order/addComment.shtml?tid='+tids+'';
})
// 去付款
$('body').on('click','.payment',function(){
    var $tidp = $(this).parents('td').next('td').find('p');
    var ordert = $(this).parents('tr').find('td').eq(1).text();
    var lianj = '';
    $tidp.each(function(){
        var tist = $(this).text();
        lianj+='tid='+tist+'&';
    })
    // console.log(lianj);
    window.location.href='/buy/delivery.shtml?'+lianj+'orderId='+ordert+'';
})

</script>
</html>


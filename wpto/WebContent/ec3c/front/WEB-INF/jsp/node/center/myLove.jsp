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
</head>
<style>
.deleteLove{
  position: absolute;top: 1px;right: 0;
  padding:3px 6px;
  display: inline-block;
  background-color: rgba(0,0,0,0.7);
  color: #fff!important;
  opacity: 0;
}
.shoucang ul li:hover .deleteLove{
  opacity: 1;
}
.shoucang ul li:hover .product{
  color: #333;
  text-decoration: underline;
}
.shoucang ul li{
  position: relative;
}
.shoucang ul li{
  display: inline-block;
  width: 135px!important;
  margin: 10px 13px 20px 14px;
}
  .shoucang img{
    width: 100%;
    border: 1px solid #eee;
    height: 135px;
  }
</style>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>

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
<a href="/content/center/myAddress.shtml"><img src="/theme/${theme}/images/u4.gif"> 收货地址</a>
<a href="/content/center/myLove.shtml" class="curs"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
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

 <h5><span>我的收藏</span></h5>
    <div class="blank"></div>
    <div class="shoucang">
      <ul class="clearfix">
        <c:forEach var="item" items="${userFavoriteList}" varStatus="status">
        <li><a href="${item.data.refUrl}" class="product">
          <img src="${item.data.refImage}">
          <p>${item.data.refTitle}</p>
          </a>
          <a href="#a" class="f6 deleteLove" dataid='${item.userRelationId}'>删除</a>
        </li>
        </c:forEach> 
      </ul>
    </div>
  <!--    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd">
         <thead>
            <th width="35%" bgcolor="#ffffff">商品名称</th>
            <th width="30%" bgcolor="#ffffff">价格</th>
            <th width="35%" bgcolor="#ffffff">操作</th> 
         </thead>
         <tbody>
            <c:forEach var="item" items="${userFavoriteList}" varStatus="status">
                <tr>
                    <td bgcolor="#ffffff"><a href="${item.data.refUrl}" class="f6">${item.data.refTitle}</a></td>
                    <td bgcolor="#ffffff">          本店售价：<span class="goods-price">￥899元</span></td>
                    <td align="center" bgcolor="#ffffff">
                        <a href="#a" class="f6" onclick="cart()">加入购物车</a>
                        <a href="#a" class="f6 deleteLove">删除</a>
                        <input type="hidden" value="${item.userRelationId}">
                    </td>
                </tr> 
            </c:forEach>          
         </tbody>
    </table>
     -->


    <div id="pager" class="pagebar">
        <span class="f_l " style="margin-right:10px;">总计 <b>${fn:length(userFavoriteList)}</b>  个记录</span>  
    </div>


                     
                    <!--<div id="pager" class="pagebar">
                            <span class="f_l " style="margin-right:10px;">总共 <b id="totalPage">0</b>  页</span> 
                            <span> 
                                <a onclick="gotoPage()" style="cursor: pointer;">第一页</a> 
                                <a onclick="goOldPage()" style="cursor: pointer;">上一页</a> 
                                <a onclick="goNextPage()" style="cursor: pointer;">下一页</a> 
                                <a onclick="goLastPage()" style="cursor: pointer;">最末页</a> 
                            </span>
                    </div>   -->
        <div class="blank5"></div>
      </div>
     </div>
    </div>
  </div>
  
</div>

<div class="blank"></div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    


</body>
<script type="text/javascript">

//   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     } 
 })
 // 取单个图片
function imgsrc(){
  $('.shoucang ul li').each(function(){
    var src = $(this).find('img').attr('src');
    var arry = {};
    arry = src.split(',');
    $(this).find('img').attr('src','/file/'+arry[0]);
  })
}
imgsrc();



// 删除收藏
$('.deleteLove').each(function(){
    $(this).click(function(){
        var userRelationId = $(this).attr('dataid');
        $.ajax({
            type:"POST",
            url:"/userRelation/delete.json",
            data:{  
                userRelationId:userRelationId
            },           
            async:false,
            success: function(data) {
                if(data.message.operateCode == 102008){
                    alert(data.message.message);
                    document.location.reload();
                }else{
                    alert(data.message.message);
                    return false;
                }
                
            },
            error: function(XMLResponse) {
                alert("操作失败:" + XMLResponse.responseText);
            }
        })
    })
})

</script>
</html>

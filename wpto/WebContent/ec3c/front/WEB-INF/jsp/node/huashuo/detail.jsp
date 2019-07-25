<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8">
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<title>产品详情</title>
<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/action.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/mzp-packed-me.js"></script>


<script type="text/javascript">
function $id(element) {
  return document.getElementById(element);
}
//切屏--是按钮，_v是内容平台，_h是内容库
function reg(str){
  var bt=$id(str+"_b").getElementsByTagName("h2");
  for(var i=0;i<bt.length;i++){
    bt[i].subj=str;
    bt[i].pai=i;
    bt[i].style.cursor="pointer";
    bt[i].onclick=function(){
      $id(this.subj+"_v").innerHTML=$id(this.subj+"_h").getElementsByTagName("blockquote")[this.pai].innerHTML;
      for(var j=0;j<$id(this.subj+"_b").getElementsByTagName("h2").length;j++){
        var _bt=$id(this.subj+"_b").getElementsByTagName("h2")[j];
        var ison=j==this.pai;
        _bt.className=(ison?"":"h2bg");
      }
    }
  }
  $id(str+"_h").className="none";
  $id(str+"_v").innerHTML=$id(str+"_h").getElementsByTagName("blockquote")[0].innerHTML;
}
</script>
</head>
<body>
 
<script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js" ></script>
  <!--<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>-->
  <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>
<script type="text/javascript">
$(function() {
window.__Object_toJSONString = Object.prototype.toJSONString;
delete Object.prototype.toJSONString;
});
</script>
 <%@include file="/WEB-INF/jsp/include/head.jsp" %> 

  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/" >首页</a> <code>&gt;</code> <a href="../../content/${node.alias}/index.shtml" >${node.name}</a> <code>&gt;</code>${pageTitle}
</div>
</div>
<div class="blank"></div>
<div class="block clearfix">
  
  <div class="AreaL">
    
  <div id="category_tree">
  <div class="tit">所有商品分类</div>
  <dl class="clearfix" style=" overflow:hidden;" >

<%@include file="/WEB-INF/jsp/include/productList.jsp" %> 
<div style="clear:both"></div>  
</div>  
 
<div class="blank"></div>
<script type="text/javascript">
obj_h4 = document.getElementById("cate").getElementsByTagName("h4")
obj_ul = document.getElementById("cate").getElementsByTagName("ul")
obj_img = document.getElementById("cate").getElementsByTagName("img")
function tab(id)
{ 
    if(obj_ul.item(id).style.display == "block")
    {
      obj_ul.item(id).style.display = "none"
      obj_img.item(id).src = "/theme/${theme}/images/btn_fold.gif"/*tpa=http://www.ktwwg.top//theme/${theme}/images/btn_fold.gif*/
      return false;
    }
    else(obj_ul.item(id).style.display == "none")
    {
      obj_ul.item(id).style.display = "block"
      obj_img.item(id).src = "/theme/${theme}/images/btn_unfold.gif"/*tpa=http://www.ktwwg.top//theme/${theme}/images/btn_unfold.gif*/
    }
}
</script>
    
    <div class="box" id='history_div'> <div class="box_1">
 <h3><span>浏览历史</span></h3>
 
  <div class="boxCenterList clearfix" id='history_list'>
       <ul class="clearfix">
        <c:forEach items="${browseHistoryList}" var="item" varStatus="status">
          <li class="goodsimg"><a href="${item.doc.viewUrl}" target="_blank"><img src="/file/${item.doc.documentDataMap.productSmallImage.dataValue}"  alt="" class="B_blue" /></a></li>
          <li><a href="${item.doc.viewUrl}" target="_blank" title="${item.doc.title}">${item.doc.title}</a><br />本店售价：<font class="f1">￥${item.buyMoney}元</font><br /></li></ul><ul class="clearfix">
        </c:forEach>
      </ul>
    </div>
 </div>
</div>
<div class="blank5"></div>
<script type="text/javascript">
if (document.getElementById('history_list').innerHTML.replace(/\s/g,'').length<1)
{
    document.getElementById('history_div').style.display='none';
}
else
{
    document.getElementById('history_div').style.display='block';
}
// function clear_history()
// {
// Ajax.call('user.php.htm'/*tpa=http://www.ktwwg.top/user.php*/, 'act=clear_history',clear_history_Response, 'GET', 'TEXT',1,1);
// }
function clear_history_Response(res)
{
document.getElementById('history_list').innerHTML = '您已清空最近浏览过的商品';
}
</script>  </div>
  
  
  <div class="AreaR">
   
   <div id="goodsInfo" class="clearfix goodsInfo">
    
     
     <div class="imgInfo">
     <a href="" id="zoom1" class="MagicZoom MagicThumb" title="">
      <img src="${document.documentDataMap.productSmallImage.dataValue}" alt="" width="360px;" height="360px"/>
     </a>
     <div class="blank5"></div>
    <!-- <div style="text-align:center; position:relative; width:100%;">
       <a href="goods.php-id=22.htm" tppabs="http://www.ktwwg.top/goods.php?id=22"><img style="position: absolute; left:0;" alt="prev" src="/theme/${theme}/images/up.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/up.gif" /></a>
       <a href="javascript:;" onclick="window.open('http://www.ktwwg.top/gallery.php?id=21'); return false;"><img alt="zoom" src="/theme/${theme}/images/zoom.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/zoom.gif" /></a>
    </div> -->
      
 
         <div class="blank"></div>
           
      <div class="picture" id="imglist" imgsrc="${document.documentDataMap.productSmallImage.dataValue}">
           
</div>
 
<script type="text/javascript">
  mypicBg();
</script>     
     </div>    

<%@include file="/WEB-INF/jsp/include/detail.jsp" %>
   <div class="blank"></div>
   
   
     <div class="box">
 
      <div style="padding:0 0px;">
        <div id="com_b" class="history clearfix">
        <h2>商品描述</h2>
        <h2 class="h2bg">商品属性</h2>
         <h2 class="h2bg">商品标签</h2>
           <h2 class="h2bg">相关商品</h2>
                </div>
      </div>    <div class="box_1">
      <div id="com_v" class="  " style="padding:6px;"></div>
      <div id="com_h">
       <blockquote>
        <div class="content ke-post" style="height: auto;" imgsrc="${document.documentDataMap.productGallery.dataValue}"> 
          <!--<img src="${document.documentDataMap.productGallery.dataValue}">-->
<!--       <img class="desc_anchor img-ks-lazyload" id="desc-module-1" src="../assets.alicdn.com/kissy/1.0.0/build/imglazyload/spaceball.gif" tppabs="https://assets.alicdn.com/kissy/1.0.0/build/imglazyload/spaceball.gif" alt="" /> -->
${document.content}
     </div>       
 </blockquote> 
       
       <blockquote>
      <table class="table" width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#dddddd">
              </table>
     </blockquote>
     <blockquote>
   
     <div class="box">
     <div class="box_1">
      <h3><span class="text">商品标签</span></h3>
      <div class="boxCenterList clearfix ie6">
       <form name="tagForm" action="javascript:;" onSubmit="return submitTag(this)" id="tagForm">
        <p id="ECS_TAGS" style="margin-bottom:5px;">
                  </p>
        <p>
          <input type="text" name="tag" id="tag" class="inputBg" size="35" />
          <input type="submit" value="添 加" class="bnt_blue" style="border:none;" />
          <input type="hidden" name="goods_id" value="21"  />
        </p>
                <script type="text/javascript">
                //<![CDATA[
                
                /**
                 * 用户添加标记的处理函数
                 */
                // function submitTag(frm)
                // {
                //   try
                //   {
                //     var tag = frm.elements['tag'].value;
                //     var idx = frm.elements['goods_id'].value;
                //     if (tag.length > 0 && parseInt(idx) > 0)
                //     {
                //       Ajax.call('user.php-act=add_tag.htm'/*tpa=http://www.ktwwg.top/user.php?act=add_tag*/, "id=" + idx + "&tag=" + tag, submitTagResponse, "POST", "JSON");
                //     }
                //   }
                //   catch (e) {alert(e);}
                //   return false;
                // }
                function submitTagResponse(result)
                {
                  var div = document.getElementById('ECS_TAGS');
                  if (result.error > 0)
                  {
                    alert(result.message);
                  }
                  else
                  {
                    try
                    {
                      div.innerHTML = '';
                      var tags = result.content;
                      for (i = 0; i < tags.length; i++)
                      {
                        div.innerHTML += '<a href="http://www.ktwwg.top/search.php?encode=YToyOntzOjg6ImtleXdvcmRzIjtzOjE2OiInIHRhZ3NbaV0ud29yZCAnIjtzOjE4OiJzZWFyY2hfZW5jb2RlX3RpbWUiO2k6MTUwMTczODA0Nzt9" style="color:#006ace; text-decoration:none; margin-right:5px;">' +tags[i].word + '[' + tags[i].count + ']<\/a>&nbsp;&nbsp; ';
                      }
                    }
                    catch (e) {alert(e);}
                  }
                }
                
                //]]>
                </script>
              </form>
      </div>
     </div>
    </div>
    <div class="blank5"></div>
     </blockquote>
     
     
    
          
     
     <blockquote>
   
 
     </blockquote>
     
      </div>
     </div>
    </div>
    <script type="text/javascript">
    <!--
    reg("com");
    //-->
    </script>
  <div class="blank"></div>
  

<%@include file="/WEB-INF/jsp/include/comment.jsp" %>
    <div class="blank5"></div>
  
</div>
  </div>
  
  <div class="addcarttooltip">
    <h3></h3>
    <p><a href="/cart.shtml">查看购物车</a><span>继续购物</span></p>
  </div>
  <script>


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

// 用户名加载
$(function () {
  if (getCookie('eis_username') == '') {
    $('.userName').text("匿名用户");
    return false;
  } else {
    $('.userName').text(decodeURIComponent(getCookie('eis_username')));
  }
})
//<![CDATA[
var cmt_empty_username = "请输入您的用户名称";
var cmt_empty_email = "请输入您的电子邮件地址";
var cmt_error_email = "电子邮件地址格式不正确";
var cmt_empty_content = "您没有输入评论的内容";
var captcha_not_null = "验证码不能为空!";
var cmt_invalid_comments = "无效的评论内容!";

/**
 * 提交评论信息
*/
function comments()
{ 
  if(!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ .test($('input[name="email"]').val()))){
        alert(cmt_empty_email);
        return false;
  }else if($('textarea[name="content"]').val().length == 0){
        alert(cmt_invalid_comments);
        return false;
  }
  var rank;
  $('input[name="comment_rank"]').each(function(){
    if($(this).is(":checked")){
      rank = $(this).val();
    }
  })
  $.ajax({
        type:"POST",
        url: '/comment/submit.json',
        data:{
          email:$('input[name="email"]').val(),
          objectType:$('input[name="objectType"]').val(),
          objectId:$('input[name="objectId"]').val(),
          content:$('textarea[name="content"]').val(),
          title:$('input[name="title"]').val(),
          rank:rank
        },
        dataType:'json',
        success:function (data) {
            if(data.message.operateCode == 500031){
                    alert(data.message.message);  
                }else{
                    alert(data.message.message);
                    window.location.reload();
                }  
            },
            error:function(XMLResponse){
                    alert("操作失败:" + XMLResponse.responseText);
                    },
        }); 
}

/**
 * 处理提交评论的反馈信息
*/
  function commentResponse(result)
  {
    if (result.message)
    {
      alert(result.message);
    }

    if (result.error == 0)
    {
      var layer = document.getElementById('ECS_COMMENT');

      if (layer)
      {
        layer.innerHTML = result.content;
      }
    }
  }

//]]>
</script>
</div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    
</body>
<script type="text/javascript">
var goods_id = 21;
var goodsattr_style = 1;
var gmt_end_time = 0;
var day = "天";
var hour = "小时";
var minute = "分钟";
var second = "秒";
var end = "结束";
var goodsId = 21;
var now_time = 1501737965;
// onload = function(){
//   changePrice();
//   fixpng();
//   try {onload_leftTime();}
//   catch (e) {}
// }
/**
 * 点选可选属性或改变数量时修改商品价格的函数
 */
// function changePrice()
// {
//   var attr = getSelectedAttributes(document.forms['ECS_FORMBUY']);
//   var qty = document.forms['ECS_FORMBUY'].elements['number'].value;
//   Ajax.call('index.htm'/*tpa=http://www.ktwwg.top/goods.php*/, 'act=price&id=' + goodsId + '&attr=' + attr + '&number=' + qty, changePriceResponse, 'GET', 'JSON');
// }
/**
 * 接收返回的信息
 */
function changePriceResponse(res)
{
  if (res.err_msg.length > 0)
  {
    alert(res.err_msg);
  }
  else
  {
    document.forms['ECS_FORMBUY'].elements['number'].value = res.qty;
    if (document.getElementById('ECS_GOODS_AMOUNT'))
      document.getElementById('ECS_GOODS_AMOUNT').innerHTML = res.result;
  }
}
$('.addcarttooltip span').on('click',function(){
  $('.addcarttooltip').css('display','none');
})
// 立即购买按钮
$('.padd img').on('click',function(){
        var documentCode = $('#documentCode').text();
        $.ajax({
          type : "POST",  
              url : "/buy/add.json",  
              dataType:"json",
              data:{
                productCode:documentCode,
                count:$('input[name="number"]').val(),
                directBuy:false
              }, 
              success : function(data) {  
                if(data.message.operateCode == 102008){
                  $('.addcarttooltip').css('display','block');
                   $('.addcarttooltip h3').text(data.message.message);
                }else if(data.message.operateCode == 500031){
                  alert(data.message.message);
                  window.location.href = "/user/login.shtml";              
                }else{
                  alert(data.message.message);
                }
              },  
              error : function() {  
                $('.addcarttooltip').css('display','none');
                  alert(data.message.message)
              }  
        }); 
})

// 分字段
$('.moneynumber').each(function () {
  var neirongzhi = $(this).text();
  var arry = {};
  arry = neirongzhi.split(';'); 
  if (arry.length>0) {
    var num = arry[0].replace(/[^\d.]/g,"");
  };
  $(this).text(num);
})

function productSmallImage(){
  var src = $('#imglist').attr('imgsrc');
  var arry = {};   
  arry = src.split(',');
  var htmls = '';
  for (var i = 0; i < arry.length; i++) {
    var html = '<a  href="/file/'+arry[i]+'" rel="zoom1"  rev="/file/'+arry[i]+'" title="${document.title}"><img src="/file/'+arry[i]+'"  alt="${document.title}" class="onbg" /></a>';
    htmls +=html
  };
  $('#imglist').append(htmls);
}
productSmallImage();
function imgsrc(){
  $('#zoom1').each(function(){
    var src = $(this).find('img').attr('src');
    var arry = {};
    arry = src.split(',');
    $(this).find('img').attr('src','/file/'+arry[0]);
  })
}
imgsrc();
function productGallery(){
  var src = $('.ke-post').attr('imgsrc');
  var arry = {};
  arry = src.split(',');
  var htmls = '';
  for (var i = 0; i < arry.length; i++) {
    var html = '<img class="desc_anchor img-ks-lazyload" src="/file/'+arry[i]+'"  alt="" />';
    htmls +=html
  };
  $('.ke-post').append(htmls);
}
// productGallery();
</script>
</html>

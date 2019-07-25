<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>公司简介_${systemName}</title>

<link rel="shortcut icon" href="favicon.ico" />
<link rel="icon" href="animated_favicon.gif" type="image/gif" />
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/theme/${theme}/js/common.js" src="js/user.js" ></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
</head>
<body>
 <script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js" ></script>
 <script type="text/javascript" src="/theme/${theme}/js/jquery.json.js"></script>
 <script type="text/javascript" src="/theme/${theme}/js/utils.js"></script>

 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
<div class="block box">
<div class="blank"></div>
 <div id="ur_here">
	当前位置: <a href="/">首页</a><code>&gt;</code> <a href="/content/about/about.shtml">关于我们</a> <code>&gt;</code> 退换货原则 
</div>
</div>
<div class="blank"></div>

<div class="block clearfix">
  
  <div class="AreaL">
   <div class="left_help clearfix">
<dl>
  <dt> <img src="/theme/${theme}/images/left_help_biao.gif"> <a href="article_cat.php?id=6" title="新手上路">新手上路</a></dt>
    <dd><a href="/content/about/AfterSalesProcess .shtml" title="售后流程">售后流程</a></dd>
    <dd><a href="/content/about/AfterBuyProcess.shtml" title="购物流程">购物流程</a></dd>
  </dl>
<dl>
  <dt> <img src="/theme/${theme}/images/left_help_biao.gif"> <a href="article_cat.php?id=7" title="配送与支付">配送与支付</a></dt>
    <dd><a href="/content/about/CashOnDelivery.shtml" title="货到付款区域">货到付款区域</a></dd>
    <dd><a href="/content/about/IntelligentInquiry.shtml" title="配送支付智能查询 ">配送支付智能查询</a></dd>
  </dl>
<dl>
  <dt> <img src="/theme/${theme}/images/left_help_biao.gif"> <a href="article_cat.php?id=9" title="服务保证">服务保证</a></dt>
    <dd><a href="/content/about/serviceGuarantee.shtml" title="售后服务保证">售后服务保证</a></dd>
    <dd><a href="/content/about/ReturnPrinciple.shtml" title="退换货原则">退换货原则</a></dd>
  </dl>
<dl>
  <dt> <img src="/theme/${theme}/images/left_help_biao.gif"> <a href="article_cat.php?id=4" title="关于我们">关于我们</a></dt>
    <dd><a href="/content/about/contact.shtml" title="联系我们">联系我们</a></dd>
    <dd><a href="/content/about/about.shtml" title="公司简介">公司简介</a></dd>
  </dl>
  </div>
<div class="blank"></div>
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
  </div>
  <div class="AreaR">
    <div class="box">
     <div class="box_1">
      <div class="userCenterBox boxCenterList clearfix" style="_height:1%;" id="notLogin">
         
                  <div class="tc" style="padding:8px;">
         <font class="f5 f6" style="font-size:18px; ">退换货原则</font><br />
         </div>
                   <p>&nbsp;<span style="color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif, 宋体; line-height: 18px;">承诺符合以下情况，自客户收到商品之日起7日（含）内可以退换货，15日（含）内可以换货，客户可在线提交返修申请办理退换货事宜。具体退换货标准如下：</span></p>
<p style="margin: 0px; padding: 0px; border: none; list-style-type: none; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif, 宋体; line-height: 18px;">商品退换类别 具体说明 是否支持7天<br />
（含）内退换货 是否支持15天<br />
（含）内换货 是否收取返回运费 备注<br />
国家三包政策范围内所规定要求的：<br />
1.功能性故障<br />
2.商品外观质量问题 1.由生产厂家指定或官方售后服务中心检测确认并出具检测报告<br />
2.由售后确认属于商品质量问题 是 是 否 若当地无检测条件的可联系售后服务中心处理<br />
1.由物流或与合作的第三方物流导致的商品损坏、丢件、缺件等<br />
2.由于网站本身提供的商品描述信息与实际商品不符等的情况<br />
3.其他原因 1.物流损指在运输过程中造成的损坏、漏液、破碎、或由于运输不慎引发的性能故障<br />
2.缺件指商品原装或配件缺失<br />
以上情况经售后人员核查属实的情况 是 是 否 为了使售后人员更快速解决客户的问题，在处理售后期间可能需快递人员证明或需客户提供实物照片等<br />
1.个人原因<br />
2.其他原因 如个人原因导致的退换货，在商品完好的前提下 是 否 是 需客户承担商品返回的运费标准<br />
7天无理由退货说明：<br />
自客户签收商品之日起7日内，在商品完好的情况下，可享受无理由退货。由于商品的特殊性质，以下商品不享有7天无理由退货：<br />
1.个人定作类商品；<br />
2.鲜活易腐类商品；<br />
3.在线下载或者您拆封的音像制品，计算机软件等数字化商品；<br />
4.交付的报纸期刊类商品；<br />
5.其他根据商品性质不适宜退货，经您在购买时确认不宜退货的商品；<br />
退换货说明：<br />
1.网销售商品均为正品行货，可按国家三包和厂商售后政策，享受相应的退货、更换、维修和保养服务；<br />
2.因厂家检测站政策不一，网代为送检的商品，如检测结果为&ldquo;无故障&rdquo;，可能检测站无法提供检测报告；如客户对我司反馈的&ldquo;无故障&rdquo;结果有质疑，可联系品牌厂商进行确认；<br />
3.网会为所有客户开具发票作为客户的质保凭证，请客户妥善保留原件作为后续质保之凭证；<br />
4.换货的定义是结合国家三包和具体厂商售后政策而定，可能是更换&ldquo;新品&rdquo;，也可能是更换&ldquo;良品&rdquo;（&ldquo;良品&rdquo;即拆封或使用过，但没有任何质量问题的同型号商品）；<br />
5.选择退货的客户必须提供对应商品的发票，并保证商品的完好，商品完好是指退回商品需保持和签收时包装内的所有物品数量一致，且均无人为损害，包括原来的包装物、 用户手册（如果有）和其他由厂商或提供的一切附件、赠品。如果某一部分丢失或损坏，可能会影响您的退货；<br />
6.由于商品的不同，各厂商服务政策的差异，在实施细则上，如保修操作、售后服务、技术支持等方面为客户提供服务可能会有所差异；所有保修规定均以国家《三包规定》为准则，如本说明有和《三包规定》相冲突之处，以《三包规定》为准。<br />
小易温馨提示，以下情况不在国家相关《三包规定》之列，不予办理退换货：<br />
1任何非网出售的商品；<br />
2.超过三包有效期的；<br />
3.未经授权的维修、误用、碰撞、疏忽、滥用、进液、事故、改动、不正确的安装所造成的商品质量问题，或撕毁、涂改标贴、机器序号、防伪标记；<br />
4.无法提供商品的发票、保修卡等三包凭证或者三包凭证信息与商品不符及被涂改的；<br />
5.三包凭证上的商品型号或编号与商品实物不相符合的；</p>                           <div style="padding:8px; margin-top:15px; text-align:left; border-top:1px solid #ccc;">
         
                    
                      上一篇:<a href="/content/about/serviceGuarantee.shtml" class="f6">售后服务保证</a>
                   </div>
    
        <div class="blank5"></div>
   
      </div>
     </div>
    </div>
    <c:choose>
    <c:when test="${empty document.documentDataMap.get('productCode')}">
          <input type="hidden" value="document" id="productType">
    </c:when>
    <c:otherwise>
        <input type="hidden" value="product" id="productType">
    </c:otherwise>
</c:choose>
<div id="ECS_COMMENT"> <div class="box">
     <div class="box_1">
      <h3><span class="text">用户评论</span>(共<font class="f1">
        <c:choose>
          <c:when test="${empty comment}">
              0
          </c:when>
          <c:otherwise>
              ${document.documentDataMap.get('commentCount').dataValue}
          </c:otherwise>
        </c:choose>
      </font>条评论)</h3>
      <div class="boxCenterList clearfix" style="height:1%;">
       <ul class="comments">
         <c:choose>
           <c:when test="${empty comment}">
               <li>暂时还没有任何用户评论</li>
           </c:when>
           <c:otherwise> 
                <c:forEach var="itema" items="${comment}" varStatus="status">
                    <c:forEach var="item" items="${itema}" varStatus="status">
                        <li>
                          <span>${item.data.userRealName}</span>
                          <span><fmt:formatDate value="${item.createTime}" type="both" /></span>
                          <p style="text-overflow: ellipsis;-o-text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" title="${item.content}">${item.content}</p>
                          <input type="hidden" value="${item.objectId}" id="objectId">
                          <input type="hidden" value="${item.commentId}" id="comment">
                        </li>
                    </c:forEach>
                </c:forEach>
           </c:otherwise>
         </c:choose>
       </ul>
       
       <div id="pagebar" class="f_r">
                <div id="pager">
                  共 <b  id="totalPage">${document.documentDataMap.commentCount.dataValue}</b> 页。 <span> <a href="#a" onclick="gotoPage()">第一页</a> <a href="#a" onclick="goOldPage()">上一页</a> <a href="#a" onclick="goNextPage()">下一页</a> <a href="#a" onclick="goLastPage()">最末页</a> </span>
                </div>
   
      </div>
      
      <div class="blank5"></div>
      
      <div class="commentsList">
      <form action="" method="post" name="commentForm" id="commentForm">
       <table width="710" border="0" cellspacing="5" cellpadding="0">
        <tr>
          <td width="64" align="right">用户名：</td>
          <td width="631" class="userName">匿名用户</td>
        </tr>
        <tr>
          <td align="right">E-mail：</td>
          <td>
            <input type="text" name="email" id="email"  maxlength="100" value="${frontUser.userConfigMap.userBindMailBox.dataValue}" class="inputBorder"/>
          </td>
        </tr>
        <tr>
          <td align="right">评价等级：</td>
          <td>
            <input name="comment_rank" type="radio" value="1" id="comment_rank1" /> <img src="/theme/${theme}/images/stars1.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/stars1.gif" />
            <input name="comment_rank" type="radio" value="2" id="comment_rank2" /> <img src="/theme/${theme}/images/stars2.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/stars2.gif" />
            <input name="comment_rank" type="radio" value="3" id="comment_rank3" /> <img src="/theme/${theme}/images/stars3.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/stars3.gif" />
            <input name="comment_rank" type="radio" value="4" id="comment_rank4" /> <img src="/theme/${theme}/images/stars4.gif" tppabs="http://www.ktwwg.top//theme/${theme}/images/stars4.gif" />
            <input name="comment_rank" type="radio" value="5" checked="checked" id="comment_rank5" /> <img src="/theme/${theme}/images/stars5.gif" tppabs="/theme/${theme}/images/stars5.gif" />
          </td>
        </tr>
        <tr>
          <td align="right" valign="top">评论内容：</td>
          <td>
            <textarea name="content" class="inputBorder" style="height:50px; width:620px;"></textarea>
            <!--<input type="hidden" name="cmt_type" value="0" />-->
            <c:choose>
                <c:when test="${empty document.documentDataMap.get('productCode')}">
                      <input type="hidden" value="document" name="objectType">
                </c:when>
                <c:otherwise>
                    <input type="hidden" value="product" name="objectType">
                </c:otherwise>
            </c:choose>
            <input type="hidden" name="objectId" value="${document.udid}" />
            <input type="hidden" name="title" value="${document.title}">
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <!--<div style="padding-left:15px; text-align:left; float:left;">
              验证码：<input type="text" name="captcha"  class="inputBorder" style="width:50px; margin-left:5px;"/>
              <img src="captcha.php-827197488.png" alt="captcha" onClick="this.src='captcha.php?'+Math.random()" class="captcha">
            </div>-->
            <input name="" type="button"  value="评论咨询" class="f_r bnt_blue_1" style=" margin-right:8px;" onclick="comments()" />
          </td>
        </tr>
      </table>
      </form>
      </div>
      <input type="hidden" value="${document.documentDataMap.commentCount.dataValue}" id="totalPages">
      </div>
     </div>
    </div>
    <div class="blank5"></div>
  </div>
  
</div>
<div class="blank"></div>
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    


</body>
<script type="text/javascript">
var cmt_empty_username = "请输入您的用户名称";
var cmt_empty_email = "请输入您的电子邮件地址";
var cmt_error_email = "电子邮件地址格式不正确";
var cmt_empty_content = "您没有输入评论的内容";
var captcha_not_null = "验证码不能为空!";
var cmt_invalid_comments = "无效的评论内容!";
// 总页数
var totalPage = $('#totalPages').val();
// 当前页数
var currentPages = 1;


//   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     } 
  // 用户名加载
    if (getCookie('eis_username') == '') {
      $('.userName').text("匿名用户");
      return false;
    } else {
      $('.userName').text(decodeURIComponent(getCookie('eis_username')));
    }
 })

// 评论
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

// 第一页跳转
function gotoPage(){ 
    if($('#totalPage').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else{
        if(currentPages == 1){
            return false;
        }else{
            $.ajax({
                type:"get",
                url:'/comment/commentListOnPage.json',
                data:{
                    rows:5,
                    page:1,
                    reload:true,
                    object_id:$('#objectId').val(),
                    object_type:$('#productType').val(),
                    commentId:$('#comment').val()
                },           
                async:false,
                success: function(data) {
                    $('.comments').empty();
                    var orderList = data.commentList.map(function(x){
                            return '<li><span>'+x.data.userRealName+'</span><span>'+x.createTime+'</span><p style="text-overflow: ellipsis;-o-text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" title="'+x.content+'">'+x.content+'</p><input type="hidden" value="'+x.objectId+'" id="objectId"><input type="hidden" value="'+x.commentId+'" id="comment"></li>';
                    })

                    $('.comments').append(orderList);
                    currentPages = data.paging.currentPage;
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
    if($('#totalPage').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else{
        if(currentPages == totalPage){
            return false;
        }else{
            $.ajax({
                type:"get",
                url:'/comment/commentListOnPage.json',
                data:{
                    rows:5,
                    page:currentPages+1,
                    reload:true,
                    object_id:$('#objectId').val(),
                    object_type:$('#productType').val(),
                    commentId:$('#comment').val()
                },           
                async:false,
                success: function(data) {
                    $('.comments').empty();
                    var orderList = data.commentList.map(function(x){
                            return '<li><span>'+x.data.userRealName+'</span><span>'+x.createTime+'</span><p style="text-overflow: ellipsis;-o-text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" title="'+x.content+'">'+x.content+'</p><input type="hidden" value="'+x.objectId+'" id="objectId"><input type="hidden" value="'+x.commentId+'" id="comment"></li>';
                    })
                    $('.comments').append(orderList);
                    currentPages = data.paging.currentPage;
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
    if($('#totalPage').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else{
        if(currentPages == 1){
            return false;
        }else{
            $.ajax({
                type:"get",
                url:'/comment/commentListOnPage.json',
                data:{
                    rows:5,
                    page:currentPages-1,
                    reload:true,
                    object_id:$('#objectId').val(),
                    object_type:$('#productType').val(),
                    commentId:$('#comment').val()
                },           
                async:false,
                success: function(data) {
                    $('.comments').empty();
                    var orderList = data.commentList.map(function(x){
                            return '<li><span>'+x.data.userRealName+'</span><span>'+x.createTime+'</span><p style="text-overflow: ellipsis;-o-text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" title="'+x.content+'">'+x.content+'</p><input type="hidden" value="'+x.objectId+'" id="objectId"><input type="hidden" value="'+x.commentId+'" id="comment"></li>';
                    })
                    $('.comments').append(orderList);
                    currentPages = data.paging.currentPage;
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
    if($('#totalPage').text() == 0){
        alert('请点击左上角查询！');
        return false;
    }else if(totalPage == 1){
        return false;
    }else{
        $.ajax({
            type:"get", 
            url:'/comment/commentListOnPage.json',
            data:{
                rows:5,
                page:totalPage,
                reload:true,
                object_id:$('#objectId').val(),
                object_type:$('#productType').val(),
                commentId:$('#comment').val()
            },           
            async:false,
            success: function(data) {
                $('.comments').empty();
                var orderList = data.commentList.map(function(x){
                        return '<li><span>'+x.data.userRealName+'</span><span>'+x.createTime+'</span><p style="text-overflow: ellipsis;-o-text-overflow: ellipsis;overflow: hidden;white-space: nowrap;" title="'+x.content+'">'+x.content+'</p><input type="hidden" value="'+x.objectId+'" id="objectId"><input type="hidden" value="'+x.commentId+'" id="comment"></li>';
                })
                $('.comments').append(orderList);
                currentPages = data.paging.currentPage;
            },
            error: function(XMLResponse) {
                alert("操作失败:" + XMLResponse.responseText);
            }
        })
    }    
}
</script>
</html>

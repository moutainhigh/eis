<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="ECSHOP v2.7.3" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />

<title>公司简介_${systemName}1</title>

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
	当前位置: <a href="/">首页</a><code>&gt;</code> <a href="/content/about/about.shtml">关于我们</a> <code>&gt;</code> 公司简介 
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
          ${document.content}
                   <%--<div class="tc" style="padding:8px;">--%>
         <%--<font class="f5 f6" style="font-size:18px; ">公司简介</font><br><font class="f3">深圳市驰悦芯科技有限公司 / 2015-12-19</font>--%>
         <%--</div>--%>
                   <%--<p>深圳市驰悦芯科技有限公司&nbsp;<font color="#353536" face="Simsun"><span style="font-size: 13px; line-height: 26px;">成立于1994年，经历和见证了湖南IT行业的发展历程，一直是湖南省IT行业中知名度最高、影响最大、实力最强的大型综合性电脑公司之一。</span></font></p>--%>
<%--<div style="color: rgb(53, 53, 54); font-family: Simsun; font-size: 13px; line-height: 26px;">&nbsp;&nbsp;&nbsp;&nbsp;目前公司的主要业务范围为DIY电脑配件产品、品牌电脑、外设、网络产品的渠道分销与零售，电脑技术服务，行业客户的系统集成和网络工程项目等。</div>--%>
<%--<div style="color: rgb(53, 53, 54); font-family: Simsun; font-size: 13px; line-height: 26px;">&nbsp;&nbsp;&nbsp; 良好的商誉、雄厚的实力、强大的渠道销售能力源自于十多年来不断的公司文化建设、人才队伍成长、与及和众多国内外著名IT厂商合作的沉淀积累。</div>--%>
<%--<div style="color: rgb(53, 53, 54); font-family: Simsun; font-size: 13px; line-height: 26px;">&nbsp;&nbsp;&nbsp; 目前独家或核心代理的DIY配件产品有：飞利浦、长城、新境界显示器，技嘉、蓝宝石主板，影驰、蓝宝石、技嘉、丽台专业显卡，AMD、Intel的原装CPU，希捷、西数硬盘，金士顿内存，先锋、三星光存储;航嘉机箱电源，影驰、安钛克电源，飞利浦音箱，金士顿、影驰固态硬盘，海联达无线设备等。核心代理的品牌电脑有：戴尔、宏碁笔记本及台式机,商用及家用系列产品。是金士顿在湖南省唯一指定的售后服务中心。并且是省直、市直、县直、政府协议供货指定单位。</div>--%>
<%--<div style="color: rgb(53, 53, 54); font-family: Simsun; font-size: 13px; line-height: 26px;">&nbsp;&nbsp;&nbsp; 秉承客户至上、品质至上、诚信负责、高效服务、专业专注、合作互赢、务实创新的经营理念，公司在竞争激烈的IT市场中，不断成长，一直在行业内保持良性发展势头。优秀的企业文化，优秀的代理产品，严谨务实、专业高效的人才队伍和锐意进取、富于远见的领导层，是蓝威核心竞争力的保证。</div>--%>
<%--<div style="color: rgb(53, 53, 54); font-family: Simsun; font-size: 13px; line-height: 26px;">&nbsp;&nbsp;&nbsp; 一流的产品、一流的团队、一流的服务，蓝威人正在一直不断地实践和履行自己的使命：为客户和社会创造价值，为员工提供良好的发展机会和工作平台，为公司创造利润，为繁荣湖南的IT产业做出更大的贡献。</div>--%>
<%--<div style="color: rgb(53, 53, 54); font-family: Simsun; font-size: 13px; line-height: 26px;">&nbsp;</div>                   --%>

              <div style="padding:8px; margin-top:15px; text-align:left; border-top:1px solid #ccc;">
         
                    
                      上一篇:<a href="/content/about/contact.shtml" class="f6">联系我们</a>
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

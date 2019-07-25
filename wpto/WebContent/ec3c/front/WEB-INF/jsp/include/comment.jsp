<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
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
          <c:when test="${empty commentList}">
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
           <c:when test="${empty commentList}">
               <li>暂时还没有任何用户评论</li>
           </c:when>
           <c:otherwise> 
                <c:forEach var="itema" items="${commentList}" varStatus="status">
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
                  共 <b  id="totalPage">${totalPages}</b> 页。 <span> <a href="#a" onclick="gotoPage()">第一页</a> <a href="#a" onclick="goOldPage()">上一页</a> <a href="#a" onclick="goNextPage()">下一页</a> <a href="#a" onclick="goLastPage()">最末页</a> </span>
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
      <input type="hidden" value="${totalPages}" id="totalPages">
      </div>
     </div>
    </div>

    <script>
// 总页数
var totalPage = $('#totalPages').val();
// 当前页数
var currentPages = 1;


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
 
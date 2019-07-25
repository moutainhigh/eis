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
<link href="/theme/${theme}/css/zoomify.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.js"  ></script>

<script type="text/javascript" src="/theme/${theme}/js/common.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/transport_jquery.js" ></script>
</head>
<style>
    .content .title{
        vertical-align: top;
    }
    .productGalleryimg{
            width: 60px;
    border: 1px solid #C1C1C1;
    }
    .content{
            background-color: #F6F6F6;
    padding: 6px;
    margin: 17px 0;
    position: relative;
    }
    .content>p{
        padding: 5px;
    }
    
    .refimg{
        width: 60px;height: 60px;
    }
    table{
        width: 100%;
    }
    table th{
        text-align: left;
        border-bottom: 1px solid #ddd;
            padding: 9px 15px;
    }
    #comments td{
        border-bottom: 1px solid #ddd;
            padding: 9px 15px;
            vertical-align: top;
    }
    #comments .text{
        padding: 0 0 7px 0;
    }
    #comments .time{
        padding: 7px 0 0 0;
        color: #AFAFAF;
    }
    #comments img.products{
        width: 50px;
            border: 1px solid;
    }
</style>
<body>
 
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
<a href="/content/center/myLove.shtml"><img src="/theme/${theme}/images/u5.gif"> 我的收藏</a>
<a href="/content/center/myWords.shtml"><img src="/theme/${theme}/images/u6.gif"> 我的留言</a>
<a href="/content/center/myLabel.shtml"><img src="/theme/${theme}/images/u7.gif"> 我的标签</a>
<a href="/content/center/shortsSupply.shtml"><img src="/theme/${theme}/images/u8.gif"> 缺货登记</a>
<a href="/content/center/myPacket.shtml"><img src="/theme/${theme}/images/u9.gif"> 我的红包</a>
<a href="/content/center/myRecommend.shtml"><img src="/theme/${theme}/images/u10.gif"> 我的推荐</a>
<a href="/content/center/myComments.shtml" class="curs"><img src="/theme/${theme}/images/u11.gif"> 我的评论</a>
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
                    <h5><span>我的评论</span></h5>
                    <div class="blank"></div>

                    <div>
                        <table>
                            <tbody>
                            <tr><th>评价</th><th width="60%;">评论</th><th width="20%;">商品信息</th><th>操作</th></tr></tbody>
                            <tbody  id="comments">
                                
                            </tbody>
                        </table>
                    </div>
                     
                    <div id="pager" class="pagebar">
                            <span class="f_l " style="margin-right:10px;">总共 <b id="totalPage">0</b>  页</span> 
                            <span> 
                                <a onclick="gotoPage()" style="cursor: pointer;">第一页</a> 
                                <a onclick="goOldPage()" style="cursor: pointer;">上一页</a> 
                                <a onclick="goNextPage()" style="cursor: pointer;">下一页</a> 
                                <a onclick="goLastPage()" style="cursor: pointer;">最末页</a> 
                            </span>
                    </div>   
        <div class="blank5"></div>
      </div>
     </div>
    </div>
  </div>
  
</div>

<div class="blank"></div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
    


</body>
<script src="http://www.jq22.com/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript" src="/theme/${theme}/js/zoomify.js" ></script>
<script type="text/javascript">
// 总页数
var totalPage;
// 当前页数
var currentPages;

//   判断用户是否登录
 $(function(){
     if(getCookie('eis_username') == ''){
         $('#notLogin').empty().append('<h2>您还未登录，请先<a href="/user/login.shtml" style="color:#f60;">登录</a>！</h2>')
     }else{

     } 

     $.ajax({
        type:"get",
        url:'/comment/index.json',
        data:{
            rows:10,
            page:1
        },           
        async:false,
        success: function(data) {
            var orderList = data.commentList.map(function(x){
                var imghtml = '';
                var shifou = x.data.productGallery;
                var rank = x.rank;
                if (rank==1) {
                  var ranks='好评';
                };
                if (rank==2) {
                  var ranks='中评';
                };
                if (rank==3) {
                  var ranks='差评';
                };
                // console.log(ranks);
                if (shifou==undefined) {
                    imghtml = '';
                    
                }else{
                    console.log(x.data.productGallery)
                    imghtml +='<img class="productGalleryimg " src="/file/open/'+x.data.productGallery+'"/>';
                };
                    
                return '<tr><td>'+ranks+'</td><td><p class="text">'+x.content+'</p>'+imghtml+'<p class="time">'+x.createTime+'</p></td><td><img class="products" src="/file/'+x.data.refImage+'"/><p>'+x.title+'</p></td><td><a href="#" title="删除" onclick="deleteComment('+"'"+x.commentId+"'"+')" class="f6 deletea">删除</a></td></tr>';
              
             })
            $('#comments').append(orderList);
            $('#totalPage').text(data.paging.totalPage);
            totalPage = data.paging.totalPage;
            currentPages = data.paging.currentPage;
            // 图片点击放大
            $('.productGalleryimg').zoomify();
        },
        error: function(XMLResponse) {
            alert("操作失败:" + XMLResponse.responseText);
        }
    }) 
 })

// 删除按钮
function deleteComment(index){
    if (confirm('您确定要删除此评论？')==true) {
        $.ajax({
            type:"POST",
            url:"/comment/delete.json",
            data:{  
                commentId:index
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
        }else{
            return false;
        }
        

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
                url:'/comment/index.json',
                data:{
                    rows:10,
                    page:1
                },           
                async:false,
                success: function(data) {
                    $('#comments').empty();
                    var orderList = data.commentList.map(function(x){
                           var imghtml = '';
                            var shifou = x.data.productGallery;
                            var rank = x.rank;
                            if (rank==1) {
                              var ranks='好评';
                            };
                            if (rank==2) {
                              var ranks='中评';
                            };
                            if (rank==3) {
                              var ranks='差评';
                            };
                            console.log(ranks);
                            if (shifou==undefined) {
                                imghtml = '';
                                
                            }else{
                                imghtml='<img class="productGalleryimg " src="/file/open/'+x.data.productGallery+'"/>';
                            };
                                
                            return '<tr><td>'+ranks+'</td><td><p class="text">'+x.content+'</p>'+imghtml+'<p class="time">'+x.createTime+'</p></td><td><img class="products" src="/file/'+x.data.refImage+'"/><p>'+x.title+'</p></td><td><a href="#" title="删除" onclick="deleteComment('+"'"+x.commentId+"'"+')" class="f6 deletea">删除</a></td></tr>';
                    })
                    $('#comments').append(orderList);
                    currentPages = data.paging.currentPage;
                    // 图片点击放大
                    $('.productGalleryimg').zoomify();
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
                url:'/comment/index.json',
                data:{
                    rows:10,
                    page:currentPages+1
                },           
                async:false,
                success: function(data) {
                    $('#comments').empty();
                    var orderList = data.commentList.map(function(x){
                            var imghtml = '';
                            var shifou = x.data.productGallery;
                            var rank = x.rank;
                            if (rank==1) {
                              var ranks='好评';
                            };
                            if (rank==2) {
                              var ranks='中评';
                            };
                            if (rank==3) {
                              var ranks='差评';
                            };
                            console.log(ranks);
                            if (shifou==undefined) {
                                imghtml = '';
                                
                            }else{
                                imghtml='<img class="productGalleryimg " src="/file/open/'+x.data.productGallery+'"/>';
                            };
                                
                            return '<tr><td>'+ranks+'</td><td><p class="text">'+x.content+'</p>'+imghtml+'<p class="time">'+x.createTime+'</p></td><td><img class="products" src="/file/'+x.data.refImage+'"/><p>'+x.title+'</p></td><td><a href="#" title="删除" onclick="deleteComment('+"'"+x.commentId+"'"+')" class="f6 deletea">删除</a></td></tr>';
                    })
                    $('#comments').append(orderList);
                    currentPages = data.paging.currentPage;
                    // 图片点击放大
                    $('.productGalleryimg').zoomify();
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
                url:'/comment/index.json',
                data:{
                    rows:10,
                    page:currentPages-1
                },           
                async:false,
                success: function(data) {
                    $('#comments').empty();
                    var orderList = data.commentList.map(function(x){
                            var imghtml = '';
                            var shifou = x.data.productGallery;
                            var rank = x.rank;
                            if (rank==1) {
                              var ranks='好评';
                            };
                            if (rank==2) {
                              var ranks='中评';
                            };
                            if (rank==3) {
                              var ranks='差评';
                            };
                            console.log(ranks);
                            if (shifou==undefined) {
                                imghtml = '';
                                
                            }else{
                                imghtml='<img class="productGalleryimg " src="/file/open/'+x.data.productGallery+'"/>';
                            };
                                
                            return '<tr><td>'+ranks+'</td><td><p class="text">'+x.content+'</p>'+imghtml+'<p class="time">'+x.createTime+'</p></td><td><img class="products" src="/file/'+x.data.refImage+'"/><p>'+x.title+'</p></td><td><a href="#" title="删除" onclick="deleteComment('+"'"+x.commentId+"'"+')" class="f6 deletea">删除</a></td></tr>';
                    })
                    $('#comments').append(orderList);
                    currentPages = data.paging.currentPage;
                    // 图片点击放大
                    $('.productGalleryimg').zoomify();
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
            url:'/comment/index.json',
            data:{
                rows:10,
                page:totalPage
            },           
            async:false,
            success: function(data) {
                $('#comments').empty();
                var orderList = data.commentList.map(function(x){
                        var imghtml = '';
                            var shifou = x.data.productGallery;
                            var rank = x.rank;
                            if (rank==1) {
                              var ranks='好评';
                            };
                            if (rank==2) {
                              var ranks='中评';
                            };
                            if (rank==3) {
                              var ranks='差评';
                            };
                            console.log(ranks);
                            if (shifou==undefined) {
                                imghtml = '';
                                
                            }else{
                                imghtml='<img class="productGalleryimg " src="/file/open/'+x.data.productGallery+'"/>';
                            };
                                
                            return '<tr><td>'+ranks+'</td><td><p class="text">'+x.content+'</p>'+imghtml+'<p class="time">'+x.createTime+'</p></td><td><img class="products" src="/file/'+x.data.refImage+'"/><p>'+x.title+'</p></td><td><a href="#" title="删除" onclick="deleteComment('+"'"+x.commentId+"'"+')" class="f6 deletea">删除</a></td></tr>';
                })
                $('#comments').append(orderList);
                currentPages = data.paging.currentPage;
                // 图片点击放大
                $('.productGalleryimg').zoomify();
            },
            error: function(XMLResponse) {
                alert("操作失败:" + XMLResponse.responseText);
            }
        })
    }    
}

</script>
</html>

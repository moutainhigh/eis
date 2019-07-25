<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-我的评论</TITLE>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!--<meta http-equiv="Refresh" content="0; url=/content/node/index.shtml" />-->
            <META name=description content=有宝>
            <META name=keywords content=有宝>
            <LINK rel="Shortcut Icon" href="/favicon.ico">
            <LINK rel=Bookmark href="/favicon.ico">


            <link rel="stylesheet" href="../../../theme/${theme}/css/swiper.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <!--<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="1106128779" data-redirecturi="http://me.mo4u.cn" charset="utf-8"></script>-->
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>


            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
                 $(function(){  
                     if(jQuery.cookie('eis_username') == null){
                           $('.entry').empty().append('<div class="wpuf-info" style="margin:23px;">该页面只允许已登录用户访问，请登录/注册后访问。</div>')
                     }else{
                         var img = jQuery.cookie('eis_userheadpic');
                        if(img == ''){
                            $('#headImg').attr('src','../../../theme/basic/images/touxiang.png');
                         }else{
                            $('#headImg').attr('src','/file/client/'+img);
                         }
                        //  兼容字体溢出
                        // $('.widefat tbody tr').children('td').css('width','20px');
                        // 点击分页
                        var totalPages = $('input[name="totalPage"]').val();
                        var currentPage = 1;
                        // 点击首页
                        $('.pages ul li').eq(2).click(function(){
                            if(currentPage == 1){
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/comment/commentListOnPage.json',
                                    data:{
                                        // rows:  行数   
                                        page:1
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td align="center" style="cursor:pointer;">'+a.content+'</td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                                currentPage = 1;
                            }
                        })
                        // 点击尾页
                        $('.pages ul li').eq(1).click(function(){
                            if(currentPage == totalPages ){
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/comment/commentListOnPage.json',
                                    data:{
                                        // rows:  行数   
                                        page:totalPages
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td align="center" style="cursor:pointer;">'+a.content+'</td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                                currentPage = totalPages;
                            }
                        })
                        // 点击下一页
                        $('.pages ul li').eq(0).click(function(){                            
                            currentPage++;
                            console.log(currentPage);
                            if(currentPage > totalPages){
                                alert('已经到了最后一页了！');
                                currentPage = totalPages;
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/comment/commentListOnPage.json',
                                    data:{
                                        // rows:  行数   
                                        page:currentPage
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td align="center" style="cursor:pointer;">'+a.content+'</td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                            }
                        })
                        // 点击上一页
                        $('.pages ul li').eq(3).click(function(){
                            currentPage--;
                            console.log(currentPage);
                            if(currentPage == 0){
                                alert('已经到了第一页了！');
                                currentPage = 1;
                                return;
                            }else{
                                $.ajax({
                                    type:"GET",
                                    url: '/comment/commentListOnPage.json',
                                    data:{
                                        // rows:  行数   
                                        page:currentPage
                                    },
                                    dataType:'json',
                                    success:function (data) {
                                        var commentList = data.commentList.map(function(a,b){
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td align="center" style="cursor:pointer;">'+a.content+'</td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                            }
                        })	
                     }
                  })
                // IE Evitar seleccion de texto
                document.onselectstart = function() {
                    if (event.srcElement.type != "text" && event.srcElement.type != "textarea" && event.srcElement.type != "password")
                        return false
                    else return true;
                };

                // FIREFOX Evitar seleccion de texto
                if (window.sidebar) {
                    document.onmousedown = function(e) {
                        var obj = e.target;
                        if (obj.tagName.toUpperCase() == "INPUT" || obj.tagName.toUpperCase() == "TEXTAREA" || obj.tagName.toUpperCase() == "PASSWORD")
                            return true;
                        /*else if (obj.tagName=="BUTTON"){
                        return true;
                        }*/
                        else
                            return false;
                    }
                }
                // End -->
            </script>

        </head>
        <style>
            .pages{
                width: 95%;
                margin: 0 auto;
            }
            .pages ul li{
                float: right;
                cursor: pointer;    
                background: #eee;
                padding: 0 10px;
                border-radius: 3px;
            }
        </style>
        <body id="top" class="home blog chrome"> 
            <input type="hidden" name="totalPage" value="${paging.totalPage}">
	        <%@include file="/WEB-INF/jsp/include/phead.jsp" %>
            <div id="main-content">
                <div id="content-header">
                    <div itemscope itemtype="http://schema.org/WebPage" id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">用户中心</span>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">已购教学</span></div>
                </div>
                <div class="container-fluid">
                    <div class="row-fluid gtop">
                        <div class="span6">
                            <a href="#" title="全站免费教学资源集合" target="_blank" rel="nofollow">
                                <img src="../../../theme/${theme}/images/banner1.png" alt="全站资源索引">
                            </a>
                        </div>
                        <div class="span6">
                            <a href="#" title="新人快快来报道" target="_blank" rel="nofollow">
                                <img src="../../../theme/${theme}/images/banner2.png" alt="新人报道">
                            </a>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <header class="archive-head jieshao">
                            <h1 itemprop="headline" class='title-ro' style='padding: 20px 0px;'>课程概述
                                <a class="rss-cat-icon" title="订阅这个分类" href="#">
                                    <p class="icon-rss"></p>
                                </a>
                            </h1>

                            <p style='padding: 15px 15px 15px 28px;'>${node.desc}</p>
                        </header>
                        <div class="span12">

                            <div class="widget-box user-center">
                                <div id="user-left">
                                    <div class="user-avatar">
                                        <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" id="headImg" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li><a href="/user/myUploadProducts.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li>
                                            <a href="/user/myFavoriteList.shtml?preview=E92C831F108E3E3D5F74A844C418F52B92EAA6FCAF3B004E66354683006A957C"><span class="icon-heart"></span>我的收藏</a></li>
                                        <li class="current-menu-item"><a href="/content/user/20170515125420.shtml?preview=525940543BC2641DD422CF13501B4C719DA3DCEE9232775527AB4BF88D22B1E8&vPro=purchaseList" style='background: url("../../../theme/${theme}/images/greenshop.png") no-repeat; 
                background-position-y: 4px;
                background-size: 12px;'><span class="icon-shopping-cart"></span>已购教学</a><img  class="down_list" src="../../../theme/${theme}/images/shop-right.png" alt="">
                <ul class="comment_down_list">
                    <li>
                        <img style="left: 12px;" src="../../../theme/${theme}/images/shop-right.png" alt="">
                        <span style="margin-left: 7px;"><a style="background: #fff;color:#31a030;" href="/comment/commentListOnPage.shtml">我的评论</a></span>
                    </li>
                    <li>
                        <img style="left: 25px;" src="../../../theme/${theme}/images/shop-righta.png" alt="">    
                        <span style="margin-left: 21px;"><a style="background: #fff;color:gray;" href="/order/item.shtml?status=710052">待评论商品</a></span>
                    </li>
                </ul>
                </li>
                                        <li><a href="/user/purchasedList.shtml?flag=vip"><span class="icon-gift"></span>VIP订购</a></li>
                                        <li><a href="/user/bbsInformation.shtml"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/user/modifyPersonalInfo.shtml"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope>
                                    <div id="post-header">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">我的评论</h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody">
                                        <div class="wrap">
                                            <table class="widefat" style="margin-top:10px;">
                                                <thead>
                                                    <tr>
                                                        <!--<th width="15%">订单编号</th>-->
                                                        <th width="15%">购买时间</th>
                                                        <th width="25%">商品名称</th>
                                                        <!-- <th width="10%">分类</th> -->
                                                        <!--<th width="12%">作者</th>-->
                                                        <!--<th width="8%">价格</th>-->
                                                        <th width="15%">操作</th>
                                                        <th width="15%" style="border-right: 1px solid #eee;">评论内容</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <c:choose>
                                                            <c:when test="${empty commentList}">
                                                                <tr>
                                                                    <td colspan="7" align="center">您目前还没有待评价商品！！</td>
                                                                </tr> 
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach var="item" items="${commentList}" varStatus="status">
                                                                    <tr>
                                                                        <td  align="center"><fmt:formatDate value="${item.createTime}" type="both"/></td>
                                                                        <c:choose>
                                                                            <c:when test="${empty item.data.refTitle}">
                                                                                    <td  align="center">${item.title}</td>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                    <td  align="center">${item.data.refTitle}</td>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <td  align="center"><a href="${item.data.refUrl}">继续阅读</a></td>
                                                                        <td  align="center" style="cursor:pointer;">${item.content}</td>
                                                                        <!--<td  align="center">待评价</td>-->
                                                                    </tr> 
                                                                </c:forEach> 
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                </tbody>
                                            </table>
                                            <div class="pages">
                                                <ul>
                                                    <li>下一页</li>
                                                    <li>尾页</li>
                                                    <li>首页</li>
                                                    <li>上一页</li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                              <div class="clear"></div>
                          </div>
                      </div>
                  </div>
              </div>
          </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>            
          <script src='../../../theme/${theme}/js/login.js'></script>
          <script src='../../../theme/${theme}/js/common.js'></script>
          <script src='../../../theme/${theme}/js/comment.js'></script>
        </body>
        </html>
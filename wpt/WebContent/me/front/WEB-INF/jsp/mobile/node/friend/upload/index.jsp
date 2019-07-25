<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-我的作品</TITLE>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!--<meta http-equiv="Refresh" content="0; url=/content/node/index.shtml" />-->
            <META name=description content=有宝>
            <META name=keywords content=有宝>
            <LINK rel="Shortcut Icon" href="/favicon.ico">
            <LINK rel=Bookmark href="/favicon.ico">


            <link rel="stylesheet" href="../../../theme/${theme}/css/swiper.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/fontAwesomeCss.css">
            <link rel="stylesheet" href="../../../theme/${theme}/css/styleb.css">
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script src='../../../theme/${theme}/js/jquery.form.js'></script>
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>

            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
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
                        else
                            return false;
                    }
                }
                // End -->
            </script>
        </head>

        <body id="top" class="home blog chrome">
	        <%@include file="/WEB-INF/jsp/include/phead.jsp" %>  
            <div id="main-content">
                <div id="content-header">
                    <div itemscope itemtype="http://schema.org/WebPage" id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">用户中心</span>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">我的作品</span></div>
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

                            <p style='    padding: 15px 15px 15px 28px;'>${node.desc}</p>
                        </header>
                        <div class="span12">

                            <div class="widget-box user-center">
                                <div id="user-left">
                                    <div class="user-avatar">
                                        <c:choose>
                                            <c:when test="${empty frontUser.userConfigMap.userHeadPic.dataValue}">
                                                <img data-original="#" src="../../../theme/${theme}/images/touxiang.png" class="avatar avatar-200" height="200" width="200">
                                            </c:when>
                                            <c:otherwise>
                                                <img data-original="#" src="/file/client/${frontUser.userConfigMap.userHeadPic.dataValue}" class="avatar avatar-200" height="200" width="200">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li class="current-menu-item"><a href="/content/friend/upload/index.shtml" style='background: url("../../../theme/${theme}/images/greenproduct.png") no-repeat;background-position-y: 4px;
    background-size: 11px;'><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li><a href="/content/user/20170515123310.shtml?preview=E92C831F108E3E3D5F74A844C418F52B92EAA6FCAF3B004E66354683006A957C"><span class="icon-heart"></span>我的收藏</a></li>
                                        <li><a href="/content/user/20170515125420.shtml?preview=525940543BC2641DD422CF13501B4C719DA3DCEE9232775527AB4BF88D22B1E8&vPro=purchaseList"><span class="icon-shopping-cart"></span>已购教学</a></li>
                                        <li><a href="/content/user/myOrders.shtml"><span class="icon-play-circle"></span>我的订单</a></li>
                                        <li><a href="/content/user/20170515130841.shtml?preview=8522EDBB6A0A0CA2A9B67FC2577D40F50A85647ECD689EE49012C6DD73057B20&vPro=purchaseList"><span class="icon-gift"></span>VIP订购</a></li>
                                        <li><a href="/content/user/20170515133906.shtml?preview=7991CF14C46EED8461848DB054D7ABEC9B7A9BDB109B333D5E2640B2950E1C95"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/content/user/20170515141440.shtml?preview=FA5F1259136D6FA8994C28370FDC834C232C6A10B52F6FD05056CFC0C721EB65"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope itemtype="http://schema.org/Article">
                                    <div id="post-header" style="margin: 0 15px;">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">我的作品</h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody">
                                        <c:choose>
                                            <c:when test="${empty frontUser}">
                                                <h2 style='border-bottom:1px solid #f2f2f2;padding-bottom: 10px;'>发布作品
                                                    <span style='font-size:12px;padding-left:30px;color:red;'>该页面只允许已登录用户访问，请登录/注册后访问。</span>
                                                </h2>
                                            </c:when>
                                            <c:otherwise>
                                                <h2 style='border-bottom:1px solid #f2f2f2;padding-bottom: 10px;'>发布作品</h2>
                                                <form class="wpuf-form-add" action="/document/create.json" method="post" enctype='multipart/form-data' id="submitForm">
                                                    <ul class="wpuf-form">
                                                        <li class="wpuf-el post_title p-title">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-post_title">视频标题 <span class="required">*</span></label>
                                                            </div>
                                                            <div class="wpuf-fields">
                                                                <input class="textfield wpuf_post_title_8544" id="post_title" type="text" data-required="yes" data-type="text" name="title" placeholder="为自己的作品起个名字吧，标题不要超过15个字" value="" size="40"  />
                                                                <span class="wpuf-help"></span>
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el post_content p-link">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-post_content">视频地址 <span class="required">*</span></label>
                                                            </div>
                                                            <div class="wpuf-fields">
                                                                <textarea class="textareafield  wpuf_post_content_8544" id="post_content" name="redirectTo" data-required="yes" data-type="textarea" placeholder="请先将自己的作品上传到优酷或土豆网，在此框内填写视频的浏览器地址" rows="1" cols="25"></textarea>
                                                                <span class="wpuf-help"></span>
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el v_dsc p-content">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-v_dsc">作品简介 <span class="required">*</span></label>
                                                            </div>
                                                            <div class="wpuf-fields">
                                                                <textarea class="textareafield  wpuf_v_dsc_8544" id="v_dsc" name="documentBrief" data-required="yes" data-type="textarea" placeholder="关于这个作品，有啥想说的没？" rows="3" cols="25"></textarea>
                                                                <span class="wpuf-help"></span>
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el category p-category">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-category">作品分类 <span class="required">*</span></label>
                                                            </div>
                                                            <div class="wpuf-fields wpuf_category_select_48212_8544">
                                                                    <select data-required="yes" data-type="select" name='defaultNodeId' id='category' class='category wpuf_category_8544' >
                                                                        <option value='-1'>&#8212; 选择 &#8212;</option>
                                                                        <option class="level-0" value="160527">所有作品</option>
                                                                        <option class="level-0" value="160520">原创作品</option>
                                                                        <option class="level-0" value="160517">弹唱作品</option>
                                                                        <option class="level-0" value="160519">指弹作品</option>
                                                                    </select>
                                                                <span class="wpuf-help"></span>
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el _check_pu p-check">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-_check_pu"> </label>
                                                            </div>
                                                            <div class="wpuf-fields" data-required="no" data-type="radio">
                                                                <label>
                                                                    <input type="checkbox" class="wpuf__check_pu_8544" name="_check_pu[]" value="向网友共享你用的吉他谱（可以是任何形式的吉他谱）" />
                                                                    向网友共享你用的吉他谱（可以是任何形式的吉他谱）                    
                                                                </label>
                                                                <div class="wpuf-fields">
                                                                    <span class="wpuf-help"></span>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el">        
                                                            <div class="wpuf-fields  wpuf_custom_html_8544" style="margin: 0;">
                                                                <h3 class="p-pu" style="padding: 0;">曲谱信息</h3>        
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el tags p-pu">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-tags">原唱歌手 <span class="required">*</span></label>
                                                            </div>
                                                            <div class="wpuf-fields">
                                                                <input class="textfield wpuf_tags_8544" id="tags" type="text" data-required="no" data-type="text" name="author" placeholder="多个歌手名，请用英文逗号,隔开" value="" size="40"  />
                                                                <span class="wpuf-help"></span>
                                                            </div>
                                                        </li>
                                                        <li class="wpuf-el _share_pu p-pu">        
                                                            <div class="wpuf-label">
                                                                <label for="wpuf-_share_pu">上传曲谱 <span class="required">*</span></label>
                                                            </div>
                                                            <div class="wpuf-fields">
                                                                <div id="wpuf-_share_pu-upload-container">
                                                                    <div class="wpuf-attachment-upload-filelist" data-type="file" data-required="no">
                                                                        <!--<a id="wpuf-_share_pu-pickfiles" class="button file-selector  wpuf__share_pu_8544 file" href="#" style="left:-17px;top: 6px;padding-left: 20px;color: #ffffff;">选择文件-->
                                                                            <input type="file" name="productSmallImage" id="files">
                                                                        <!--</a>-->
                                                                        <div class="comments-loading"><img src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;"> 正在上传请稍候 ...</div>
                                                                        <ul class="wpuf-attachment-list thumbnails"></ul>
                                                                    </div>
                                                                </div><!-- .container -->
                                                                <span class="wpuf-help">附件只支持压缩文件格式（zip、rar）。<br />
                                                    必须是常见图片或文本格式。<br />
                                                    谱子要和你上传的版本对应。如果谱子是手绘拍照的，请保证图片清晰。<br />
                                                    为尊重版权，请不要上传商业曲谱。</span>
                                                            </div> <!-- .wpuf-fields -->
                                                        </li>       
                                                        <li class="wpuf-submit">
                                                            <input type="hidden" value="171006" name="documentTypeId">
                                                            <input type="hidden" value="176004" name="displayTypeId">
                                                            <input type="submit" name="submit" value="提交作品" onclick="return submitProduct(this.form)"/>                                                           
                                                        </li>
                                                    </ul>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                        <!--<script>
                                            $(function(){
                                                $('#submitForm').ajaxForm(function(){ 
                                                    alert('提交成功');   
                                                });   
                                            })
                                        </script>-->
                                        <h2 style='border-bottom:1px solid #f2f2f2;padding-bottom: 10px;'>我的上传</h2>
                                        <table class="wpuf-table widefat" cellpadding="0" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>作品标题</th>
                                                    <th>曲谱状态</th>
                                                    <th>发布状态</th>
                                                    <!--<th>浏览次数</th>
                                                    <th>人气指数</th>-->
                                                </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <c:when test="${empty frontUser}">
                                                    <tr>
                                                        <td>请先登录...</td>
                                                    </tr>
                                                </c:when>
                                                <c:when test="${empty myUploadList}">
                                                    <tr>
                                                        <td>您还未上传任何作品！</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${myUploadList}" varStatus="status">
                                                        <tr>
                                                            <td><a href="${item.redirectTo}" title="${item.title}" >${item.title}</a></td>
                                                            <td><a href="#">共享曲谱</a></td>
                                                            <td><span><span class="icon-meh">
                                                                </span> 
                                                                    <c:choose>
                                                                        <c:when test="${item.currentStatus == 130002}">
                                                                            审核中
                                                                        </c:when>
                                                                        <c:when test="${item.currentStatus == 130005}">
                                                                            已发布
                                                                        </c:when>
                                                                    </c:choose>
                                                                </span>
                                                            </td>
                                                            <!--<td><span class="icon-eye-open"></span> 0</td>
                                                            <td><span class="icon-thumbs-up-alt"></span> 0</td>-->
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <!--<div class="page-nav nav-fix" style='padding-left:15px;'>
                                            <span class="pages " style='padding: 0 11px;'>1 of 139</span>
                                            <span class='page-numbers current ' style='padding: 0 11px;'>1</span>
                                            <a class='page-numbers ' href='#' style='padding: 0 11px;'>2</a>
                                            <a class='page-numbers ' href='#' style='padding: 0 11px;'>3</a>
                                            <span class="page-numbers dots " style='padding: 0 11px;'>&hellip;</span>
                                            <a class='page-numbers ' href='#' style='padding: 0 11px;'>139</a>
                                            <a class="next page-numbers " href="#" style='padding: 0 11px;'>&raquo;</a>
                                        </div>-->
                                    </div>
                                </div>
                                <div class='clear'></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkfMobile.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-${pageTitle}</TITLE>
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
            <script>                
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
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">站内信息</span></div>
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
                                    <div class="user-avatar myname">
                                        <c:choose>
                                        <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li><a href="/user/myUploadProducts.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li>
                                            <a href="/user/myFavoriteList.shtml?preview=E92C831F108E3E3D5F74A844C418F52B92EAA6FCAF3B004E66354683006A957C"><span class="icon-heart"></span>我的收藏</a></li>
                                        <li><a href="/user/purchasedList.shtml?flag=vip"><span class="icon-shopping-cart"></span>已购教学</a></li>
                                        <li><a href="/content/user/20170515130841.shtml?preview=8522EDBB6A0A0CA2A9B67FC2577D40F50A85647ECD689EE49012C6DD73057B20&vPro=purchaseList"><span class="icon-gift"></span>VIP订购</a></li>
                                        <li class="current-menu-item"><a href="/content/user/20170515133906.shtml?preview=7991CF14C46EED8461848DB054D7ABEC9B7A9BDB109B333D5E2640B2950E1C95" style='background: url("../../../theme/${theme}/images/greenmessage.png") no-repeat;
                background-position-y: 6px;
                background-position-x: 2px;
                background-size: 11px;'><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/content/user/20170515141440.shtml?preview=FA5F1259136D6FA8994C28370FDC834C232C6A10B52F6FD05056CFC0C721EB65"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <input type="hidden" value="${frontUser.uuid}" id="uuid">
                                <div class="widget-content single-post" id="user-right" itemscope itemtype="http://schema.org/Article">
                                    <div id="post-header">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">站内信息</h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody" style='    margin-left: 20px;'>
                                        <div id='fep-wrapper'>
                                            <div id='fep-menu'><a class='fep-button' href='#' style='background: #31a030;color:#ffffff;' id='post_message' class=''>发信息</a><a class='fep-button' href='#' id='email-box'>信箱 </a><a class='fep-button' href='#' id='public-tell'>公告 </a>
                                                <a class='fep-button' href='#' id='set-box'>设置</a>
                                            </div>
                                            <div id='fep-content'>
                                                <div class="comments-loading"><img src="../../../theme/${theme}/images/07093129-1-24Q58.gif" alt="" style="width:12px;position: relative;bottom: -12px;"> 邮箱信息正在加载 ...</div>
                                                <!--发信息-->
                                                <div class="from" style="display: block">
                                                    <p><strong>创建新信息:</strong></p>
                                                    <form name='message' action='http://www.daweijita.com/user/pm?fepaction=checkmessage' method='post' enctype='multipart/form-data'>收件人
                                                        <font color='red'>*</font>: <br/><input type='text' name='message_to' placeholder='收件人名字' autocomplete='off' value='' size='80' style="height:35px;border:1px solid #e1e1e1;padding-left:10px" /><br/>主题
                                                        <font color='red'>*</font>:<br/>
                                                        <input type='text' name='message_title' placeholder='主题' maxlength='65' value='' size='80' style="height:35px;border:1px solid #e1e1e1;padding-left:10px" /><br/>信息
                                                        <font color='red'>*</font>:<br/>
                                                        <a title="粗体" href="javascript:void(0);" onclick='b()'><img src="../../../theme/${theme}/images/ba.png" /></a>
                                                        <a title="斜体" href="javascript:void(0);" onclick='i()'><img src="../../../theme/${theme}/images/ia.png" /></a>
                                                        <a title="下划线" href="javascript:void(0);" onclick='u()'><img src="../../../theme/${theme}/images/ua.png" /></a>
                                                        <a title="删除线" href="javascript:void(0);" onclick='s()'><img src="../../../theme/${theme}/images/sa.png" /></a>
                                                        <a title="代码" href="javascript:void(0);" onclick='code()'><img src="../../../theme/${theme}/images/code.png" /></a>
                                                        <a title="引用" href="javascript:void(0);" onclick='quote()'><img src="../../../theme/${theme}/images/quote.png" /></a>
                                                        <a title="表单" href="javascript:void(0);" onclick='list()'><img src="../../../theme/${theme}/images/list.png" /></a>
                                                        <a title="表单项" href="javascript:void(0);" onclick='lover()'><img src="../../../theme/${theme}/images/li.png" /></a>
                                                        <a title="链接" href="javascript:void(0);" onclick='url()'><img src="../../../theme/${theme}/images/url.png" /></a>
                                                        <a title="图像" href="javascript:void(0);" onclick='img()'><img src="../../../theme/${theme}/images/img.png" /></a>
                                                        <a title="邮箱" href="javascript:void(0);" onclick='email()'><img src="../../../theme/${theme}/images/email.png" /></a>
                                                        <a title="添加16进制颜色" href="javascript:void(0);" onclick='color()'><img src="../../../theme/${theme}/images/color.png" /></a>
                                                        <a title="嵌入" href="javascript:void(0);" onclick='embed()'><img src="../../../theme/${theme}/images/embed.png" /></a><br/>
                                                        <textarea name='message_content' placeholder='信息内容' style='width:800px;height: 200px;border:1px solid #e1e1e1;padding:10px'></textarea><br />
                                                        <input type='button' id='submit' value='发送信息' class='postmessage' onclick="send()" />
                                                    </form>
                                                </div>
                                                <!--没有信箱-->
                                                <div class="messagebox" style="display: none"><span>您的信箱为空！</span></div>
                                                <!--有信箱-->
                                                <div class="messageboxhave" style="display: none">
                                                    <p><strong>你的信息: (<span id="totalSize">0</span>)</strong></p>
                                                    <table>
                                                        <thead>
                                                            <tr class='head'>
                                                                <td width='20%'>发起人</td>
                                                                <td width='20%'>收件人</td>
                                                                <td width='30%'>主题</td>
                                                                <td width='20%'>最后回复人</td>
                                                                <td width='10%'>删除</td>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <!--回复邮件-->
                                                <div class="replyMessage" style="display: none;">
                                                    <p><strong>信息流:</strong></p>
                                                    <table>
                                                        <tbody>
                                                            <tr>
                                                                <th width="15%">发信人</th>
                                                                <th width="85%">信息</th>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <a href="#" class="sendName">qixing</a><br><small>20小时 前</small><br>
                                                                    <a href="#" class="highslide-image">
                                                                        <img src="http://cn.gravatar.com/avatar/dbe699840302c4f59afef7973df481f4?s=120" class="avatar avatar-120" height="120" width="120" style="display: inline;">
                                                                    </a>
                                                                </td>
                                                                <td class="pmtext">
                                                                    <strong>主题: </strong><span>qixing</span><hr><p>qixing</p>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                    <p><strong>回复:</strong></p>
                                                    <form name='message' action='' method='post' enctype='multipart/form-data'>
                                                        <a title="粗体" href="javascript:void(0);" onclick='b()'><img src="../../../theme/${theme}/images/ba.png" /></a>
                                                        <a title="斜体" href="javascript:void(0);" onclick='i()'><img src="../../../theme/${theme}/images/ia.png" /></a>
                                                        <a title="下划线" href="javascript:void(0);" onclick='u()'><img src="../../../theme/${theme}/images/ua.png" /></a>
                                                        <a title="删除线" href="javascript:void(0);" onclick='s()'><img src="../../../theme/${theme}/images/sa.png" /></a>
                                                        <a title="代码" href="javascript:void(0);" onclick='code()'><img src="../../../theme/${theme}/images/code.png" /></a>
                                                        <a title="引用" href="javascript:void(0);" onclick='quote()'><img src="../../../theme/${theme}/images/quote.png" /></a>
                                                        <a title="表单" href="javascript:void(0);" onclick='list()'><img src="../../../theme/${theme}/images/list.png" /></a>
                                                        <a title="表单项" href="javascript:void(0);" onclick='lover()'><img src="../../../theme/${theme}/images/li.png" /></a>
                                                        <a title="链接" href="javascript:void(0);" onclick='url()'><img src="../../../theme/${theme}/images/url.png" /></a>
                                                        <a title="图像" href="javascript:void(0);" onclick='img()'><img src="../../../theme/${theme}/images/img.png" /></a>
                                                        <a title="邮箱" href="javascript:void(0);" onclick='email()'><img src="../../../theme/${theme}/images/email.png" /></a>
                                                        <a title="添加16进制颜色" href="javascript:void(0);" onclick='color()'><img src="../../../theme/${theme}/images/color.png" /></a>
                                                        <a title="嵌入" href="javascript:void(0);" onclick='embed()'><img src="../../../theme/${theme}/images/embed.png" /></a><br/>
                                                        <textarea name='message_content' placeholder='信息内容' style='width:800px;height: 200px;border:1px solid #e1e1e1;padding:10px'></textarea><input type='hidden' name='message_from' value='110296' />
                                                        <input type='hidden' name='parent_id' value='0' />
                                                        <input type='hidden' name='token' value='9caf3b60ef-1494826904' /><br/>
                                                        <input type='button' id='submit' value='发送信息' class='postmessage' onclick="reply()" />
                                                    </form>
                                                </div>
                                                 <!--公告-->
                                                <div class="public"style="display: none"><p><strong>公告:</strong></p><span style="color:red;">没有公告</span></div>
                                                <!--保存设置-->
                                                <div class="oneLove" style="display: none">
                                                    <p><strong>设置您的偏好：:</strong></p>
                                                    <ul>
                                                        <li style="margin-left:-10px">
                                                            <c:choose>
                                                                <c:when test="${frontUser.userConfigMap.allowOtherToSelf.dataValue == 'Y'}">
                                                                        <input type="checkbox" checked/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                        <input type="checkbox" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <span>允许其他人给我发送信息</span> 
                                                        </li>
                                                        <!--<li style="margin-left:-10px">
                                                            <input type="checkbox" /><span>当我收到新信息时邮件通知我</span> 
                                                        </li>
                                                        <li style="margin-left:-10px">
                                                            <input type="checkbox" /><span>当有新公告时邮件通知我</span> 
                                                        </li>-->
                                                        <li style="margin-left:-10px">
                                                            <input type="button" value="保存设置" class="story" onclick="set()"/>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
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
            <script src='../../../theme/${theme}/js/message.js'></script>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>
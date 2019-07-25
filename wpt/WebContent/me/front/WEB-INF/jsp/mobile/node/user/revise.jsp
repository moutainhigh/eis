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
            <link rel="stylesheet" href="../../../theme/${theme}/css/register.css">

            
            <!--<link rel="stylesheet" href="../../../theme/${theme}/css/ssi-uploader.min.css">-->
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
            <script src='../../../theme/${theme}/js/jqmeter.min.js'></script>

            
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
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">修改资料</span></div>
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
                                        <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li><a href="/user/myUploadProducts.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li><a href="/user/myFavoriteList.shtml"><span class="icon-heart"></span>我的收藏</a></li>
                                        <li><a href="/content/user/20170515125420.shtml?preview=525940543BC2641DD422CF13501B4C719DA3DCEE9232775527AB4BF88D22B1E8&vPro=purchaseList"><span class="icon-shopping-cart"></span>已购教学</a></li>
                                        <li><a href="/content/user/20170515130841.shtml?preview=8522EDBB6A0A0CA2A9B67FC2577D40F50A85647ECD689EE49012C6DD73057B20&vPro=purchaseList"><span class="icon-gift"></span>VIP订购</a></li>
                                        <li><a href="/content/user/20170515133906.shtml?preview=7991CF14C46EED8461848DB054D7ABEC9B7A9BDB109B333D5E2640B2950E1C95"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li class="current-menu-item"><a href="/content/user/20170515141440.shtml?preview=FA5F1259136D6FA8994C28370FDC834C232C6A10B52F6FD05056CFC0C721EB65" style='background: url("../../../theme/${theme}/images/greenset.png") no-repeat;
                background-position-y: 4px;
                background-position-x: 2px;
                background-size: 11px;'><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope itemtype="http://schema.org/Article">
                                    <div id="post-header">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">修改资料</h1>
                                    </div>
                                    <div class="entry" itemprop="articleBody">
                                        <form class="wpuf-form-add" action=""  enctype="multipart/form-data" method="post" name="fileinfo" id="registerForm">
                                            <ul class="wpuf-form">
                                                <li class="wpuf-el user_login username">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_login">用户名 <span class="required"></span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input class="textfield wpuf_user_login_5046" id="userName" type="text"  name="user_login" placeholder="用户名" value="" size="40" style="background:#f8f8f8;"/>
                                                        <span class="wpuf-help">不要超过6位数以及出现空格符号，否则有可能会被网站系统认定为广告ID</span>
                                                    </div>
                                                </li>                                               
                                                <li class="wpuf-submit">
                                                    <div class="wpuf-label">
                                                        &nbsp;
                                                    </div>
                                                    <div class="wpuf-fields">
                                                        <input type="button"  value="修改用户名" style='background:#1ba0e1;' onclick='reviseUsername()' />
                                                    </div>                                              
                                                </li>
                                                <li class="wpuf-el user_email email">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_email">找回密码 </label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <a href="/content/user/revisepassword.shtml" style="background: #1ba0e1;padding: 8px 30px;color: #fff;border-radius: 5px;">找回密码</a>
                                                    </div>
                                                </li> 
                                                <!--<li class="wpuf-el user_email email">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_email">手机号 <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="wpuf-user_email" type="email" class="email  wpuf_user_email_5046"  name="user_email" placeholder="非常重要！请填写您的常用手机号" value="" size="40" />
                                                        <span class="wpuf-help" id='email_text'></span>
                                                    </div>

                                                </li>
                                                <li class="wpuf-el">
                                                    <div class="wpuf-label">
                                                        <label >验证码 <span class="required">*</span></label>
                                                    </div>
                                                    <div class="wpuf-fields">
                                                        <input type="text" placeholder="请输入验证码" id="smsRegisterSign" name="smsRegisterSign" class="passwd" maxlength="6" autocomplete="off">
                                                        <input type="button" class="btn_sendCode" id="btnSendCode" value="发送验证码" onclick="sendMessage();" style="padding: 5px;">
                                                    </div>
                                                </li>-->
                                                <!--<li class="wpuf-el password password">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-password">密码</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="userPassword" type="password" class="password  wpuf_password_5046" data-required="no" data-type="text" name="pass1" placeholder="至少7个字符" value="" size="40" />
                                                        <span class="wpuf-help" id='password_text'>可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>
                                                    </div>

                                                </li>
                                                <li>
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-pass2">确认密码</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="userPassword2" type="password" class="password  wpuf_password_5046" data-required="no" data-type="text" name="pass2" value="" size="40" />
                                                        <span class="wpuf-help">如果要设置新密码，请重新输入</span>
                                                    </div>

                                                </li>
                                                <li>
                                                    <div class="wpuf-label">
                                                        &nbsp;
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <div id="pass-strength-result" class='pass-strength-result'>强度评估</div>


                                                </li>-->

                                                <li class="wpuf-el avatar user-avatar">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-avatar">修改头像</label>
                                                    </div>
                                                    <div class="wpuf-fields">
                                                            <a href="javascript:;" class="file" style="color: #fff;">选择文件
                                                                        <input type="file" name="image" id="fileupload" required onchange="previewImage(this)">
                                                            </a>
                                                            <!--<input type="file" name="image" required  onchange="previewImage(this)" />-->
                                                            <input type="submit" value="上传" class="upload" />
                                                        <div id="preview" style="margin:10px 0;"></div>
                                                        <div id="jqmeter-container"></div>
                                                        <!-- .container -->
                                                        <span class="wpuf-help">
                                                            <!--<strong><font color="red">修改头像，请先删除之前的头像。</font></strong>-->
                                                            <font color="red">图片文件名后缀只能是jpg、png、gif！</font>尺寸为 100-200像素的正方形 jpg、png 图片为佳，不能超过 2M,建议使用IE、FireFox、Chrome浏览器。
                                                        </span>
                                                    </div>
                                                </li>
                                                <!--<li class="wpuf-el description description">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-description">个人简介</label>
                                                    </div>

                                                    <div class="wpuf-fields">


                                                        <textarea class="textareafield  wpuf_description_5046" id="description" name="description" data-required="no" data-type="textarea" placeholder="用简短的话语，描述最具个性的你！20-50字为宜。" rows="3" cols="25"></textarea>
                                                        <span class="wpuf-help"></span>
                                                    </div>

                                                </li>
                                                <li class="wpuf-el user_url website">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_url">个人网站</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="wpuf-user_url" type="url" class="url  wpuf_user_url_5046" data-required="no" data-type="text" name="user_url" placeholder="例如 http://www.qixing.com" value="" size="40" />
                                                        <span class="wpuf-help"></span>
                                                    </div>
                                                </li>
                                                <li class="wpuf-el qq_weibo qq-weibo">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-qq_weibo">腾讯微博</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input class="textfield wpuf_qq_weibo_5046" id="qq_weibo" type="text" data-required="no" data-type="text" name="qq_weibo" placeholder="例如 http://t.qq.com/hcm602" value="" size="40" />
                                                        <span class="wpuf-help"></span>

                                                    </div>
                                                </li>
                                                <li class="wpuf-el sina_weibo sina-weibo">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-sina_weibo">新浪微博</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input class="textfield wpuf_sina_weibo_5046" id="sina_weibo" type="text" data-required="no" data-type="text" name="sina_weibo" placeholder="例如 http://weibo.com/davidjita" value="" size="40" />
                                                        <span class="wpuf-help"></span>

                                                    </div>
                                                </li>-->
                                                <!--<li class="wpuf-submit">
                                                    <div class="wpuf-label">
                                                        &nbsp;
                                                    </div>
                                                    <div class="wpuf-fields">
                                                        <input type="hidden" id="_wpnonce" name="_wpnonce" value="7d83c8b5b7" /><input type="hidden" name="_wp_http_referer" value="/user/profile" /> <input type="hidden" name="form_id" value="5046">
                                                        <input type="hidden" name="page_id" value="5068">

                                                        <input type="hidden" name="action" value="wpuf_update_profile">
                                                        <input type="button" name="submit" value="更新个人资料" style='background:#1ba0e1;' onclick='updata()' />
                                                    </div>                                              
                                                </li>-->
                                            </ul>
                                        </form>
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
            <script>
                // 将本地图片上传后台              
                var form = document.forms.namedItem("fileinfo");
                form.addEventListener('submit', function(ev) {

                // var oOutput = document.querySelector("#preview"),
                oData = new FormData(form);

                // oData.append("CustomField", "This is some extra data");

                var oReq = new XMLHttpRequest();
                oReq.open("POST", "/user/changeHeadPic.json", true);
                oReq.onload = function(oEvent) {
                if (oReq.status == 200) {
                        $('#jqmeter-container').jQMeter({
                            goal:'$1,000',
                            raised:'$1000',
                            orientation:'vertical',
                            width:'500px',
                            height:'40px'
                        });
                        location.reload();
                    } else {
                        $('#jqmeter-container').jQMeter({
                            goal:'$1,000',
                            raised:'$800',
                            orientation:'vertical',
                            width:'500px',
                            height:'40px'
                        });
                        $('#preview').append('<p>"Error ":' + oReq.status + ' 上传文件失败.'+'</p>');
                        }
                    };
                    oReq.send(oData);
                    ev.preventDefault();
                }, false);
            </script>
            <script src='../../../theme/${theme}/js/register.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>
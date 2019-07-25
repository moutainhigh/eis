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
            <script src='../../../theme/${theme}/js/jquery-1.11.3.js'></script>
            <script src='../../../theme/${theme}/js/jquery.cookie.js'></script>
            <script src='../../../theme/${theme}/js/jquery.base64.js'></script>
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
                    <div itemscope id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">注册账号</span></div>
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
                        <div class="span12">
                            <div class="widget-box" style='height: 80%;'>
                                <article class="widget-content single-post" itemscope itemtype="http://schema.org/Article">
                                    <header id="post-header">
                                        <h1 class="page-title" itemprop="headline">找回密码</h1>
                                    </header>
                                    <div class="entry" itemprop="articleBody" style="display:flex;flex-direction: row;-ms-flex-direction:row;justify-content: center;display:-webkit-flex;" onkeydown="keyLogin();">
                                        <div class="login" id="wpuf-login-form" style="border-right:1px solid #ededed;margin: 50px;">
                                            <form  class="wpuf-login-form" id="loginform" action="#" method="post">
                                                <p>
                                                    <label for="wpuf-user_login" style="width:135px;">请输入你的手机号：</label>
                                                    <input type="text" name="log" id="wpuf-user_email" class="input" value="" size="20" />
                                                </p>
                                                <p>
                                                    <label for="wpuf-user_pass" style="width:135px;">验证码：</label>
                                                    <input type="text" placeholder="请输入验证码" id="smsRegisterSign" name="smsRegisterSign" class="passwd" maxlength="6" autocomplete="off">
                                                    <input type="button" class="btn_sendCode" id="btnSendCode" value="发送验证码" onclick="sendMessage();" style="padding: 5px;background: #31a030;color:#fff;">
                                                </p>


                                                <p>
                                                    <label for="wpuf-user_pass" style="width:135px;">新密码：</label>
                                                    <input type="password" name="pwd" id="userPassword" class="input" value="" size="20" />
                                                </p>
                                                <p>
                                                    <label for="wpuf-user_pass" style="width:135px;">确认密码：</label>
                                                    <input type="password" name="pwd" id="userPassword" class="input" value="" size="20" />
                                                </p>

                                                <p class="submit">
                                                    <input type="button" name="wp-submit" id="wp-submit" value="提交" onclick="findpassword()" style="margin: 35px;"/>
                                                </p>
                                            </form>
                                        </div>
                                        <div class="leftImg">
                                            <img src="../../../theme/basic/images/wxlogin.png" alt=""  onclick="wxLogin()" style="border:0;cursor:pointer;width:30px;">
                                            <img src="../../../theme/basic/images/qqlogin.png" alt=""   id="qq" onclick="qqLogin()" style="border:0;cursor: pointer;width:30px;">
                                        </div>
                                    </div>
                                </article>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/register.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>
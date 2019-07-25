<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-联系我们</TITLE>
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
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                <!-- Begin
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
                        /*else if (obj.tagName=="BUTTON"){
                        return true;
                        }*/
                        else
                            return false;
                    }
                }
                // End -->
            </script>


            <!-- EVITAR IFRAME-->
            <script>
                $(function() {
                    $('#kefu').mouseover(function() {
                        $('#box').css('display', 'block');
                        $('#kefu a').first().find('img').remove();
                        $('#kefu a').first().html('<p style="text-align:center;width:40px;height:38px;background:#C9C9C9;color:#ffffff;">客服中心</p>');

                    })

                    $('#kefu').mouseout(function() {
                        $('#box').css('display', 'none');
                        $('#kefu a').first().find('span').remove();
                        $('#kefu a').first().html('<img alt="客服中心" tilt="客服中心" class="adid_icon" src="../../../theme/${theme}/images/kefu.png">');
                    })
                })
                window.onload = function() {
                    document.getElementById("profile-messages").onclick = function() {
                        var classVal = document.getElementById("profile-messages").getAttribute("class");
                        if (classVal == 'dropdown-toggle') {
                            document.getElementById("profile-messages").setAttribute("class", 'ula');
                            document.getElementById("dropdown-menu").setAttribute("class", 'openul dropdown-menu');
                        } else {
                            document.getElementById("dropdown-menu").setAttribute("class", 'dropdown-menu');
                            document.getElementById("profile-messages").setAttribute("class", 'dropdown-toggle');
                        }
                    }
                }
            </script>


            <!--<link rel="shortcut icon" href="http://img.daweijita.com/2014/07/favicon.png" title="Favicon" />-->
            <!--[if lt IE 9]>
  <script src="http://www.daweijita.com/wp-content/themes/wpdx/js/html5.js"></script>
  <script src="http://www.daweijita.com/wp-content/themes/wpdx/js/css3-mediaqueries.js"></script>
  <![endif]-->
            <!--[if IE 8]>
  <link rel="stylesheet" href="http://www.daweijita.com/wp-content/themes/wpdx/css/ie8.css">
  <![endif]-->
            <!--[if IE 7]>
  <link rel="stylesheet" href="http://www.daweijita.com/wp-content/themes/wpdx/font-awesome/css/font-awesome-ie7.min.css">
  <link rel="stylesheet" href="http://www.daweijita.com/wp-content/themes/wpdx/css/ie7.css">
  <![endif]-->
            <!--[if IE 6]>
  <script src="//letskillie6.googlecode.com/svn/trunk/2/zh_CN.js"></script>
  <![endif]-->
        </head>

        <body id="top" class="home blog chrome">
            <div id="top-part">
                <div id="top-bar" class="navbar navbar-inverse">
                    <div id="logo">
                        <img src="../../../theme/${theme}/images/logo.png" alt="" class='logo'>
                    </div>
                    <ul class="nav">
                        <li class="dropdown" style='background:red;'>
                            <a title="" id="profile-messages" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle"><img src="../../../theme/${theme}/images/login.png" alt="" class='login'><span class="text">登录</span><b class="caret"></b></a>
                            <ul class="dropdown-menu" id='dropdown-menu'>
                                <form class="user-login" name="loginform" action="#" method="post">
                                    <li><img src="../../../theme/basic/images/eight.png" alt="" class='user'><input class="ipt" placeholder="用户名" type="text" name="log" value="" size="18"></li>
                                    <li><img src="../../../theme/basic/images/six.png" alt="" class='password'></i><input class="ipt" placeholder="密码" type="password" name="pwd" value="" size="18"></li>
                                    <li><input name="rememberme" id="rememberme" type="checkbox" checked="checked" value="forever" /> 记住我的登录信息</li>
                                    <li class="btn"><input class="login-btn" type="submit" name="submit" value="登录"></li>
                                    <li>
                                        <a class="pw-reset" rel="nofollow" href="#"><img src="../../../theme/basic/images/password.png" alt="" class='forget'> 忘记密码？</a>
                                    </li>
                                    <input type="hidden" name="redirect_to" value="" />
                                    <input type="hidden" name="wpuf_login" value="true" />
                                    <input type="hidden" name="action" value="login" />
                                    <input type="hidden" id="_wpnonce" name="_wpnonce" value="19d19ff553" /><input type="hidden" name="_wp_http_referer" value="/" />
                                </form>
                                <li>
                                    <p>——— 第三方登录 ———</p>
                                </li>
                                <li>
                                    <div class='all'>
                                        <span><img src="../../../theme/basic/images/wxlogin.png" alt=""></span>
                                        <span><img src="../../../theme/basic/images/qqlogin.png" alt=""></span>

                                    </div>
                                </li>
                            </ul>
                        </li>
                        <li class="user-btn user-reg">
                            <a href="/user/register.shtml" title="注册" rel="nofollow"><img src="../../../theme/${theme}/images/yaos.png" alt="" class='register'></i><span class="text">注册</span></a>
                        </li>

                        <li class="wx"><img src="../../../theme/${theme}/images/wx.png" title="扫描微信二维码" alt="扫描微信二维码"><span class="weixin" style='width:128px;background:#ffffff;'><img src="../../../theme/${theme}/images/tubiao.png" alt="微信二维码"></span></li>

                        <li id="swb" class="other-nav">
                            <!--<wb:follow-button uid="1720882337" type="red_2" height="24"></wb:follow-button>-->
                            <img src="../../../theme/${theme}/images/xl.png" alt="">
                            <span class='nub'>1.2万</span>
                        </li>
                    </ul>
                    <div id="search">
                        <form class="search_form" method="GET" action="#">
                            <input class="left" type="text" name="s" id="search-txt" value="搜索 ..." onfocus="if (this.value == '搜索 ...') {this.value = '';}" onblur="if (this.value == '') {this.value = '搜索 ...';}" x-webkit-speech />
                            <button type="submit" class="tip-bottom" title="搜索"><img src="../../../theme/${theme}/images/sousuo.png" alt="" class='suosou'></button>
                        </form>
                    </div>
                </div>
                <header id="header" role="banner">
                    <nav id="main-nav" role="navigation" itemscope itemtype="http://schema.org/SiteNavigationElement"> <a href="#" class="visible-phone"><i class="icon-align-justify"></i>导航菜单</a>
                        <ul>
                            <li class="submenu">
                                <a href="/"><img src="../../../theme/${theme}/images/first.png" alt="" class='first'>网站首页</a>
                            </li>
                            <li>
                                <a target="_blank" href="https://qxmusic.taobao.com/?qq-pf-to=pcqq.c2c"><img src="../../../theme/${theme}/images/two.png" alt="" class='first'>官方商城</a>
                            </li>
                            <li class=" submenu">
                                <a href="/content/newbie/index.shtml#"><img src="../../../theme/${theme}/images/three.png" alt="" class='first'>入门自学20课</a>
                            </li>
                            <li class=" submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/five.png" alt="" class='first'>指弹教学 <span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li><a target="_blank" href="#">全部教学资源</a></li>
                                    <li><a target="_blank" href="#">初级指弹曲</a></li>
                                    <li><a target="_blank" href="#">中级指弹曲</a></li>
                                    <li><a target="_blank" href="#">高级指弹曲</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="/content/vip/index.shtml"><img src="../../../theme/${theme}/images/six.png" alt="" class='first'>VIP曲谱教学</a>
                            </li>
                            <li class=" submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/tang.png" alt="" class='first'>指弹提高 <span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li><a href="#">歌曲前奏</a></li>
                                    <li><a href="#">Riff练习</a></li>
                                    <li><a href="#">双吉他教学</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="/content/study/index.shtml"><img src="../../../theme/${theme}/images/ku.png" alt="" class='first'>学习文章</a>
                            </li>
                            <li class=" submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/eight.png" alt="" class='first'>老师专栏 <span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li>
                                        <a target="_blank" href="#"><img src="../../../theme/${theme}/images/nan.png" alt="" class='two'><span>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;伟</span></a>
                                    </li>
                                    <li>
                                        <a target="_blank" href="#"><img src="../../../theme/${theme}/images/nan.png" alt="" class='two'>杨猛（阿坤）</a>
                                    </li>
                                </ul>
                            </li>
                            <li class=" submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/nine.png" alt="" class='first'>琴友作品<span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li><a target="_blank" href="/content/friend/index.shtml" title="琴友作品">所有作品</a></li>
                                    <li><a target="_blank" href="#">我要上传</a></li>
                                    <li><a href="#">弹唱作品</a></li>
                                    <li><a href="#">指弹作品</a></li>
                                    <li><a href="#">原创作品</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="/content/ring/index.shtml" title="指弹铃音"><img src="../../../theme/${theme}/images/musics.png" alt="" class='first' style='width: 15px;'>指弹铃音 </a>

                            </li>
                            <li>
                                <a href="/content/chord/index.shtml"><img src="../../../theme/${theme}/images/eleven.png" alt="" class='first'>在线调弦</a>
                            </li>
                            <li>
                                <a href="/content/help/index.shtml"><img src="../../../theme/${theme}/images/twelve.png" alt="" class='first'>帮助中心</a>
                            </li>
                        </ul>
                        <div class="clear"></div>
                    </nav>
                </header>
            </div>
            <div id="main-content">
                <div class='userour'>
                    <div class='userour_a'>
                        <h1>联系我们</h1>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>客户服务（支付问题、功能故障、投诉建议、使用帮助 ）： </h3>
                            </li>
                            <li>电子邮件：<a href="#">sasddy@service.netease.com</a></li>
                            <li>联系电话：0571-8888888（工作日周一至周五10:00-12:00,14:00-18:00）</li>
                            <li> 客服提醒：为了更快帮您解决问题，请在邮件中留下电话、吉他教学账号、昵称、订单号、截图等信息，并尽可能详细的描述问题。课程相关服务请在课程页面联系课程提供机构。</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>机构、讲师商务合作： </h3>
                            </li>
                            <li>电子邮件：<a href="#">bdsasdady@163.com</a></li>
                            <li>联系电话：0571-88888888、0571-99999999</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>企业、学校在线培训服务、课程团购及销售代理合作： </h3>
                            </li>
                            <li>电子邮件：<a href="#">kcxsasdatudy@163.com</a></li>
                            <li>联系电话：0571-88888888、 0571-999999999</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>广告销售：</h3>
                            </li>
                            <li>电子邮件：<a href="#">bdsasdady@163.com</a></li>
                            <li>联系电话：0571-888888888、0571-99999999</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>市场合作（媒体、公关）：</h3>
                            </li>
                            <li>电子邮件：<a href="#">bdstsday@163.com</a></li>
                            <li>联系电话：0571-99999999</li>
                        </ul>
                    </div>
                    <div class='userour_a font_height'>
                        <ul>
                            <li>
                                <h3>联系地址： </h3>
                            </li>
                            <li>公司地址：浙江省杭州市某某599号</li>
                            <li>邮政编码：430025</li>
                        </ul>
                    </div>

                </div>

            </div>
            <footer id="footer" class="row-fluid" role="contentinfo" itemscope itemtype="http://schema.org/WPFooter">
                <div class="span12 footer-nav">
                    <ul>
                        <li><a target="_blank" href="/content/about/20160629163203.shtml">关于我们</a></li>
                        <!--<li><a target="_blank" href="http://www.daweijita.com/8439.html">招聘信息</a></li>
                        <li><a target="_blank" href="http://www.daweijita.com/8366.html">商务合作</a></li>
                        <li><a target="_blank" href="http://www.daweijita.com/8706.html">联系我们</a></li>-->
                    </ul>
                </div>
                <div class="span12 footer-info">
                    ${commonFooter}
                    <script type="text/javascript">
                        var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
                        document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F612bedbdc93b0cf91a59fa09da7b034f' type='text/javascript'%3E%3C/script%3E"));
                    </script>
                </div>
            </footer>
            <!-- Swiper JS -->
            <script src="../../../theme/${theme}/js/swiper.js"></script>

            <!-- Initialize Swiper -->
            <script>
                var swiper = new Swiper('.swiper-container', {
                    pagination: '.swiper-pagination',
                    nextButton: '.swiper-button-next',
                    prevButton: '.swiper-button-prev',
                    paginationClickable: true,
                    spaceBetween: 30,
                    centeredSlides: true,
                    autoplay: 2500,
                    autoplayDisableOnInteraction: false
                });
            </script>

            <script>
                var right_1 = 1,
                    right_2 = 2;
            </script>
            <div class="asid_share" id="asid_share">
                <div>
                    <a href="#"><img alt="返回顶部" title="返回顶部" class="adid_icon" src="../../../theme/${theme}/images/top.png"></a>
                </div>
                <div id='kefu' style='margin-top:10px;height:40px;'>
                    <a href="#" class='hovera'><img alt="客服中心" title="客服中心" class="adid_icon" src="../../../theme/${theme}/images/kefu.png"></a>

                    <div class="asid_sha_layer kefu" id='box'>
                        <div class="asid_share_triangle">
                            <em class="border_sj">&#9670;</em>
                            <span class="con_sj">&#9670;</span>
                        </div>
                        <p>淘宝店铺店长
                            <a target="_blank" href="http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&siteid=cntaobao&status=1&charset=utf-8"><img border="0" src="http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&site=cntaobao&s=1&charset=utf-8" alt="" /></a>
                        </p>
                        <p>吉他购买咨询
                            <a target="_blank" href="http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&siteid=cntaobao&status=1&charset=utf-8"><img border="0" src="http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%A4%A7%E4%BC%9F%E5%90%89%E4%BB%96%E6%95%99%E5%AE%A4&site=cntaobao&s=1&charset=utf-8" alt="" /></a>
                        </p>
                        <p>网站充值咨询
                            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=426900148&site=qq&menu=yes"><img style="vertical-align:middle;width:77px;height:22px;" border="0" src="http://wpa.qq.com/pa?p=2:426900148:41" alt="" title=""></a>
                        </p>
                        <p>付费教学咨询
                            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=426900148&site=qq&menu=yes"><img style="vertical-align:middle;width:77px;height:22px;" border="0" src="http://wpa.qq.com/pa?p=2:426900148:41" alt="" title=""></a>
                        </p>
                        <p>老师招聘咨询
                            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1847037538&site=qq&menu=yes"><img style="vertical-align:middle;width:77px;height:22px;" border="0" src="http://wpa.qq.com/pa?p=2:1847037538:41" alt="" title=""></a>
                        </p>
                    </div>
                </div>
            </div>
        </body>

        </html>
        <!-- hyper cache: 987f9798f08bbda9b55ccc52c51605a3 17-04-25 11:40:00 -->
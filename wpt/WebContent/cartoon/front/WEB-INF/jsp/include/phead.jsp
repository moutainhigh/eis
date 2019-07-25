        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
            <!--<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="1106128779" charset="utf-8"></script>-->
            <div id="top-part">
                <input type="hidden" id="pagetitlenode" value="${node.name}">
                <div id="top-bar" class="navbar navbar-inverse">
                    <div id="logo">
                        <a href="/">
                            <strong style="color: #ddd;font-size: 23px;font-style: italic;">七星指弹教室</strong>
                            <img src="../../../theme/${theme}/images/LOGO.png" alt="" class='logo'>
                        </a>
                    </div>
                    <ul class="nav">
                        <li id='user-name' style='color:#ffffff;padding-top:4px;'>您好，${frontUser.username}</li>
                        <li id='user-img' style='width:23px;height: 10px;'></li>
                        <input type="hidden" class="userImg" value="${frontUser.userConfigMap.userHeadPic.dataValue}">
                        <li id='user-email' style='margin-top:19px;'><a href='/user/bbsInformation.shtml' style="padding:0;background: rgba(0,0,0,0);">
                        <c:choose>
                            <c:when test="${unreadMessage == 0 || empty unreadMessage}">

                            </c:when>
                            <c:otherwise>
                                <img src="../../../theme/${theme}/images/userMessage.png" alt="" style="position: absolute;top: -15px;left: -2px;">
                            </c:otherwise>
                        </c:choose>
                        </a></li>
                        <li class="dropdown" id="menu">
                            <a title="" id="profile-messages" href="javascript:void(0)" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle"><img src="../../../theme/${theme}/images/login.png" alt="" class='login'><span class="text">登录</span><b class="caret"></b></a>
                            <ul class="dropdown-menu" id='dropdown-menu'>
                            </ul>
                        </li>
                        <form class="user-login" id='loginForm' method="post" style="display:none"  onkeydown="keyLogin();">
                            <li><img src="../../../theme/basic/images/eight.png" alt="" class='user'><input class="ipt" placeholder="用户名" type="text" name="username" value="" size="18" id='user_login'></li>
                            <li><img src="../../../theme/basic/images/six.png" alt="" class='password'></i><input class="ipt" placeholder="密码" type="password" name="userPassword" value="" size="18" id="pass1"></li>
                            <li><input  id="rememberme" type="checkbox" checked="checked" value="forever" /> 记住我的登录信息</li>
                            <li class="btn"><input class="login-btn" type="button"  value="登录" onclick="login()"></li>
                            <li>
                                <a class="pw-reset" rel="nofollow" href='/content/user/revisepassword.shtml'><img src="../../../theme/basic/images/password.png" alt="" class='forget'> 忘记密码？</a>
                            </li>
                        </form>
                        <li class="user-btn user-reg" id='user-register' style="z-index:0;">
                            <a href='/user/register.shtml' title="注册" rel="nofollow"><img src="../../../theme/${theme}/images/yaos.png" alt="" class='register'></i><span class="text">注册</span></a>
                        </li>

                        <li class="wx"><img src="../../../theme/${theme}/images/wx.png" title="扫描微信二维码" alt="扫描微信二维码"><span class="weixin" style='width:128px;background:#ffffff;'><img src="../../../theme/${theme}/images/tubiao.jpg" alt="微信二维码"></span></li>

                        <li id="swb" class="other-nav">
                            <!--<wb:follow-button uid="1720882337" type="red_2" height="24"></wb:follow-button>-->
                            <img src="../../../theme/${theme}/images/xl.png" alt="">
                            <span class='nub' style="padding-left:4px;">2.2万</span>
                        </li>
                        <li class="mycart">
                            <a href='/cart.shtml' >
                                <span class="mycart_icon1"></span>
                                <span class="text_icon">我的购物车</span>
                                <img src="../../../theme/${theme}/images/right.png" alt="" class="arrow_right_icon">
                            </a>
                        </li>
                    </ul>
                    <div id="search">
                        <!--<form class="search_form" method="GET" action="#">-->
                            <input class="left" type="text" name="s" id="search-txt" value="搜索 ..."  onkeydown="keySearch()" />
                            <button class="tip-bottom search" title="搜索" onclick="search()"><img src="../../../theme/${theme}/images/sousuo.png" alt="" class='suosou'></button>
                        <!--</form>-->
                    </div>
                </div>
                <header id="header" role="banner">
                    <nav id="main-nav" role="navigation" itemscope> <a href="#" class="visible-phone"><i class="icon-align-justify"></i><span>导航菜单</span></a>
                        <ul>
                            <li >
                                <a href='/'><img src="../../../theme/${theme}/images/first.png" alt="" class='first'>网站首页</a>
                            </li>
                            <li>
                                <a target="_blank" href="/"><img src="../../../theme/${theme}/images/two.png" alt="" class='first'>官方商城</a>
                            </li>
                            <li class="">
                                <a href='/content/newbie/index.shtml' ><img src="../../../theme/${theme}/images/three.png" alt="" class='first'>入门自学20课</a>
                            </li>
                            <li class="submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/five.png" alt="" class='first'>指弹教学 <span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li><a target="_blank" href='/content/finger/index.shtml'>全部教学资源</a></li>
                                    <li><a target="_blank" href='/content/finger/primary/index.shtml'>初级指弹曲</a></li>
                                    <li><a target="_blank" href='/content/finger/intermediate/index.shtml'>中级指弹曲</a></li>
                                    <li><a target="_blank" href='/content/finger/senior/index.shtml'>高级指弹曲</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href='/content/vip/index.shtml'><img src="../../../theme/${theme}/images/six.png" alt="" class='first'>付费曲谱教学</a>
                            </li>
                            <li>
                                <a href='/content/onlyvip/index.shtml'><img src="../../../theme/${theme}/images/whitevip.png" alt="" class='first' style="width:15px;height: auto;position: relative;top: 2px;">vip专区</a>
                            </li>
                            <li class="submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/tang.png" alt="" class='first'>指弹提高 <span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li><a href='/content/highfinger/song/index.shtml'>歌曲前奏</a></li>
                                    <li><a href='/content/highfinger/riff/index.shtml'>Riff练习</a></li>
                                    <li><a href='/content/highfinger/guitar/index.shtml'>双吉他教学</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href='/content/study/index.shtml'><img src="../../../theme/${theme}/images/ku.png" alt="" class='first'>学习文章</a>
                            </li>
                            <li class="submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/eight.png" alt="" class='first'>老师专栏 <span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li>
                                        <a target="_blank" href='/content/teacher/jinwei/index.shtml'><img src="../../../theme/${theme}/images/nan.png" alt="" class='two'><span>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;伟</span></a>
                                    </li>
                                    <li>
                                        <a target="_blank" href='/content/teacher/yangmeng/index.shtml'><img src="../../../theme/${theme}/images/nan.png" alt="" class='two'><span>杨猛（阿坤）</span></a>
                                    </li>
                                </ul>
                            </li>
                            <li class="submenu">
                                <a href="#"><img src="../../../theme/${theme}/images/nine.png" alt="" class='first'>琴友作品<span class="caret"></span></a>
                                <ul class="sub-menu">
                                    <li><a target="_blank" href='/content/friend/all/index.shtml' title="琴友作品">所有作品</a></li>
                                    <li><a target="_blank" href='/user/myUploadProducts.shtml'>我要上传</a></li>
                                    <li><a href='/content/friend/sing/index.shtml'>弹唱作品</a></li>
                                    <li><a href='/content/friend/thrum/index.shtml'>指弹作品</a></li>
                                    <li><a href='/content/friend/original/index.shtml'>原创作品</a></li>
                                </ul>
                            </li>
                            <!--<li>
                                <a href='/content/ring/index.shtml' title="指弹铃音"><img src="../../../theme/${theme}/images/musics.png" alt="" class='first' style='width: 15px;'>指弹铃音 </a>
                            </li>-->
                            <li>
                                <a href='/content/chord/index.shtml'><img src="../../../theme/${theme}/images/eleven.png" alt="" class='first'>在线调弦</a>
                            </li>
                            <li>
                                <a href='/content/help/index.shtml'><img src="../../../theme/${theme}/images/twelve.png" alt="" class='first'>帮助中心</a>
                            </li>
                        </ul>
                        <div class="clear"></div>
                    </nav>
                </header>
            </div>
            <script>
                 $(function(){     
                      var title = '${node.name}';
                      if(title.indexOf('首页')>-1){
                        $('#header li').eq(0).addClass('current-menu-item');               
                      }else if(title.indexOf('入门自学20课')>-1){
                        $('#header li').eq(2).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹教学')>-1||title.indexOf('初级指弹曲')>-1||title.indexOf('中级指弹曲')>-1||title.indexOf('高级指弹曲')>-1){
                        $('#header li').eq(3).addClass('current-menu-item');               
                      }else if(title.indexOf('付费曲谱教学')>-1){
                        $('#header li').eq(8).addClass('current-menu-item');               
                      }else if(title.indexOf('vip专区')>-1){
                        $('#header li').eq(9).addClass('current-menu-item');               
                      }else if(title.indexOf('歌曲前奏')>-1 ||title.indexOf('Riff练习')>-1 || title.indexOf('双吉他教学')>-1 ){
                        $('#header li').eq(10).addClass('current-menu-item');               
                      }else if(title.indexOf('学习文章')>-1){
                        $('#header li').eq(14).addClass('current-menu-item');               
                      }else if(title.indexOf('金伟')>-1 || title.indexOf('杨猛')>-1 ){
                        $('#header li').eq(15).addClass('current-menu-item');               
                      }else if(title.indexOf('所有作品')>-1 || title.indexOf('我要上传')>-1 ||title.indexOf('弹唱作品')>-1||title.indexOf('指弹作品')>-1||title.indexOf('原创作品')>-1){
                        $('#header li').eq(18).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹铃音')>-1){
                        $('#header li').eq(23).addClass('current-menu-item');               
                      }else if(title.indexOf('在线调弦')>-1){
                        $('#header li').eq(24).addClass('current-menu-item');               
                      }else if(title.indexOf('帮助中心')>-1){
                        $('#header li').eq(25).addClass('current-menu-item');               
                      }

                     if(jQuery.cookie('frontVipLevel') == 2){
                         $('#user-img').append('<img src="../../../theme/${theme}/images/headvip.png" style="position: absolute;top: -3px;left: -6px;width: 16px;">');
                     }else{

                     }

                    //  请求购物车接口显示购物车数量
                    $.ajax({
                        type:"GET",
                        url: '/cart.json',
                        data:{},
                        dataType:'json',
                        success:function (data) {
                            var itemMapArray=[];
                            for(var j in data.itemMap){
                                itemMapArray.push(j);
                            }
                            $('.ptfcarts p').eq(1).text( itemMapArray.length );
                        }
                    })
                 })
        </script>
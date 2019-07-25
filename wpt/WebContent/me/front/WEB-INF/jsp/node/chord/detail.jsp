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
                      var title = '${node.name}';
                      if(title.indexOf('首页')>-1){
                        $('#header li').eq(0).addClass('current-menu-item');               
                      }else if(title.indexOf('入门自学20课')>-1){
                        $('#header li').eq(2).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹教学')>-1||title.indexOf('初级指弹曲')>-1||title.indexOf('中级指弹曲')>-1||title.indexOf('高级指弹曲')>-1){
                        $('#header li').eq(3).addClass('current-menu-item');               
                      }else if(title.indexOf('VIP曲谱*教学')>-1){
                        $('#header li').eq(8).addClass('current-menu-item');               
                      }else if(title.indexOf('歌曲前奏')>-1 ||title.indexOf('Riff练习')>-1 || title.indexOf('双吉他教学')>-1 ){
                        $('#header li').eq(9).addClass('current-menu-item');               
                      }else if(title.indexOf('学习文章')>-1){
                        $('#header li').eq(13).addClass('current-menu-item');               
                      }else if(title.indexOf('金伟')>-1 || title.indexOf('杨猛')>-1 ){
                        $('#header li').eq(14).addClass('current-menu-item');               
                      }else if(title.indexOf('所有作品')>-1 || title.indexOf('我要上传')>-1 ||title.indexOf('弹唱作品')>-1||title.indexOf('指弹作品')>-1||title.indexOf('原创作品')>-1){
                        $('#header li').eq(17).addClass('current-menu-item');               
                      }else if(title.indexOf('指弹铃音')>-1){
                        $('#header li').eq(22).addClass('current-menu-item');               
                      }else if(title.indexOf('在线调弦')>-1){
                        $('#header li').eq(23).addClass('current-menu-item');               
                      }else if(title.indexOf('帮助中心')>-1){
                        $('#header li').eq(24).addClass('current-menu-item');               
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
                    <div itemscope id="breadcrumb">
                        <a itemprop="breadcrumb" href="/" title="返回首页" class="tip-bottom"><img src="../../../theme/${theme}/images/souye.png" alt="" class='souye'>首页</a>
                        <a href="/content/study/index.shtml"><img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">学习文章</span></a>
                        <img src="../../../theme/${theme}/images/right.png" alt=""> <span class="current">正文</span></div>
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

                        <div class="span8">
                            <div class="widget-box">
                                <article id="post-7315" class="widget-content single-post" itemscope style='padding: 0;'>

                                    <header id="post-header" style='width:100%;background: #f8f8f8;'>
                                        <div class='wpfp-span'><img src='../../../theme/${theme}/images/lovera.png' alt='Loading' title='Loading' class='wpfp-hide wpfp-img' /><span title="只有已登录用户才能收藏文章，请先登录/注册。"><i class="icon-heart add"></i> <div class="poptip"><span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span>55</span>
                                        </div>
                                        </span>
                            </div>
                            <h1 class="post-title" itemprop="headline" style='width:80%;padding-left:15px;'>学习文章01【吉他的发声原理与如何调音】</h1>
                            <div class="clear"></div>
                            <p class="post-meta" style='margin-left:15px;'>
                                <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="#" title="">金伟</a></span>
                                <span class="time"><img src="../../../theme/${theme}/images/lixian.png" alt="" class='lixian' style='padding-left:5px;'>20天前</span>
                                <span class="cat"><img src="../../../theme/${theme}/images/excrse.png" alt="" class='ku'><a href="#" rel="category tag" style='padding-left:5px;'>学习文章</a></span>
                                <span class="eye"><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>193,491</span>
                                <span class="comm"><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="#">75</a></span>
                            </p>
                            <div class="clear"></div>
                            </header>
                            <c:choose>
                                <c:when test="${empty document.content}">
                                       <div class="entry" itemprop="articleBody">
                                            <p>
                                                <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                            </p>
                                            <p>
                                                <video src="" style='width:96%;padding:11px;'></video>
                                            </p>
                                            <p>
                                                <a href="../../../theme/${theme}/images/paino-one.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-medium wp-image-7141" alt="paino-one" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/paino-one.png" width="1000" height="1358" /></a>
                                                <a href="../../../theme/${theme}/images/paino-one.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-medium wp-image-7142" alt="paino-two" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/paino-two.png" width="1000" height="1358" /></a>
                                            </p>
                                            <noscript><img data-tag="bdimg" class="alignnone size-medium wp-image-7141" alt="Rex_02" src="http://cdn.daweijita.com/2014/07/Rex_02.jpg" width="1000" height="1358" /></a> <a href="http://cdn.daweijita.com/2014/07/Rex_03.jpg" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-medium wp-image-7142" alt="Rex_03" src="http://cdn.daweijita.com/2014/07/Rex_03.jpg" width="1000" height="1358" /></a></p></noscript>



                                            <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #31a030;'>七星教育</a> 的吉他谱和教学视频！</div>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="entry" itemprop="articleBody">
                                        <p>
                                            <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                        </p>
                                        <div style="padding:0 15px 0 15px;">${document.content}</div>
                                        
                                        <div style="text-align:center;border:1px dashed #FF9A9A;padding:8px;margin:30px 15px;color:#FF6666;" id="dltz">
                                            <div style="width:100px; text-align:center; margin-top:-22px; background:#fff;color:#FF6666;">收费内容</div>
                                            亲，您必须 <a href="/user/login.shtml">登录</a> 且 <a href="#paydown">购买</a> 后才可以阅读收费内容！
                                        </div>
                                        <div class="paydown" id="paydown">
                                            <div class="down-title">资<br>源<br>购<br>买</div>
                                            <div class="down-detail"><p class="down-price">价格：<span>
                                                ${fn:substringAfter(document.documentDataMap.productBuyMoney.dataValue,":")}
                                                </span>&nbsp;元</p><p class="down-ordinary">您需要在网站顶部登录或注册后，才能购买资源</p><p class="down-tip">提示：此非实物交易，购买后不退款，请考虑好再购买 | <a target="_blank" href="#">点此查看购买指南</a></p><p class="down-tip">要联系作者，请 <a target="_blank" rel="nofollow" href="/content/user/20170515133906.shtml?fepaction=newmessage&amp;to=大伟">点此给TA发站内信</a></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #31a030;'>七星教育</a> 的吉他谱和教学视频！</div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <footer class="entry-meta">
                                <div class='wpfp-span'><img src='../../../theme/${theme}/images/lovera.png' alt='Loading' title='Loading' class='wpfp-hide wpfp-img' /><span title="只有已登录用户才能收藏文章，请先登录/注册。"><i class="icon-heart add"></i> <div class="poptip"><span class="poptip-arrow poptip-arrow-left"><em>◆</em><i>◆</i></span>55</span>
                                </div>
                                </span>
                        </div>
                        <div class="bdsharebuttonbox post-share">
                            <a href="#" data-cmd="qzone" title="分享到QQ空间">
                                <div class="bds_qzone"></div>
                            </a>
                            <a href="#" data-cmd="tsina" title="分享到新浪微博">
                                <div class="bds_tsina"></div>
                            </a>
                            <a href="#" data-cmd="tqq" title="分享到腾讯微博">
                                <div class="bds_tqq"></div>
                            </a>
                            <a href="#" data-cmd="renren" title="分享到人人网">
                                <div class="bds_renren"></div>
                            </a>
                            <a href="#" data-cmd="weixin" title="分享到微信">
                                <div class="bds_weixin"></div>
                            </a>
                            <a href="#" data-cmd="more">
                                <div class="bds_more"></div>
                            </a>
                            <a data-cmd="count">
                                <div class="bds_count"></div>
                            </a>
                        </div>
                        <div class="gpost-below">
                            <a href="https://qxmusic.taobao.com/?qq-pf-to=pcqq.c2c" rel="nofollow" title="官方淘宝店" target="_blank">
                                <img src="../../../theme/${theme}/images/bannerb.png" alt="官方淘宝店" style='width: 96%;margin:0 15px;' />
                            </a>
                        </div>
                        <div id="author-box">
                            <h3>作者： 大伟</h3>
                            <div class="author-info">
                                <div class="author-avatar">
                                    <img src="http://img.daweijita.com/2014/09/头像2-avatar.jpg" alt="" height="64" width="64" class="avatar"> </div>
                                <div class="author-description">
                                    <p>该用户很懒，还没有介绍自己。</p>
                                    <ul class="author-social follows nb">
                                        <li class="archive">
                                            <a target="_blank" href="http://www.daweijita.com/author/%e5%a4%a7%e4%bc%9f" title="阅读 大伟 的其他文章">阅读 大伟 的其他文章</a>
                                        </li>
                                        <li class="email">
                                            <a target="_blank" rel="nofollow" href="http://www.daweijita.com/user/pm?fepaction=newmessage&amp;to=大伟" title="给 大伟 发送站内信">给 大伟 发送站内信</a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        </footer>
                        </article>
                    </div>
                    <div class="row-fluid" style='background: #ffffff;'>
                        <section class="span4 home-recent related">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <h3>随便看看</h3>
                                </div>
                                <div class="widget-content">
                                    <ul class="news-list">
                                        <li>
                                            <a href="http://www.daweijita.com/79571.html" title="链接到  【殷鹏】农夫渔夫" rel="bookmark" target="_blank">【殷鹏】农夫渔夫</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/46116.html" title="链接到  【琴友】吉他指弹 《后会无期》（音频）" rel="bookmark" target="_blank">【琴友】吉他指弹 《后会无期》（音频）</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/48475.html" title="链接到  【琴友】街头吉他弹唱《人家》现实的歌曲，真实的愿望。（视频）" rel="bookmark" target="_blank">【琴友】街头吉他弹唱《人家》现实的歌曲，真实的愿望。（视频）</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/%e5%b0%8f%e5%bc%a0%e5%90%89%e4%bb%96%e5%bc%b9%e5%94%b1%e3%80%8a%e8%93%9d%e8%8e%b2%e8%8a%b1%e3%80%8b.html" title="链接到  小张吉他弹唱《蓝莲花》" rel="bookmark" target="_blank">小张吉他弹唱《蓝莲花》</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/62006.html" title="链接到  逝去的爱" rel="bookmark" target="_blank">逝去的爱</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/61546.html" title="链接到  《玫瑰》无间奏" rel="bookmark" target="_blank">《玫瑰》无间奏</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/47566.html" title="链接到  【琴友】寂寞的季节 吉他弹唱（音频）" rel="bookmark" target="_blank">【琴友】寂寞的季节 吉他弹唱（音频）</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/64822.html" title="链接到  理想三旬" rel="bookmark" target="_blank">理想三旬</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/75990.html" title="链接到  一路向北 魔天伦演唱会版" rel="bookmark" target="_blank">一路向北 魔天伦演唱会版</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/69160.html" title="链接到  安静" rel="bookmark" target="_blank">安静</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </section>



                        <section class="span4 home-recent related">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <h3>小编推荐</h3>
                                </div>
                                <div class="widget-content">
                                    <ul class="news-list">
                                        <li>
                                            <a href="http://www.daweijita.com/75334.html" title="链接到  鹿先森乐队《春风十里》吉他弹唱" rel="bookmark" target="_blank">鹿先森乐队《春风十里》吉他弹唱</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/70352.html" title="链接到  谢春花《借我》男声版吉他弹唱" rel="bookmark" target="_blank">谢春花《借我》男声版吉他弹唱</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/67118.html" title="链接到  莫文蔚《盛夏的果实》男声版吉他弹唱教学" rel="bookmark" target="_blank">莫文蔚《盛夏的果实》男声版吉他弹唱教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/64098.html" title="链接到  田馥甄《你就不要想起我》吉他教学" rel="bookmark" target="_blank">田馥甄《你就不要想起我》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/62871.html" title="链接到  陈鸿宇《理想三旬》吉他教学" rel="bookmark" target="_blank">陈鸿宇《理想三旬》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/61268.html" title="链接到  贰佰《玫瑰》吉他教学" rel="bookmark" target="_blank">贰佰《玫瑰》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/59166.html" title="链接到  郑伊健《友情岁月》 吉他弹唱教学" rel="bookmark" target="_blank">郑伊健《友情岁月》 吉他弹唱教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/53290.html" title="链接到  赵雷《吉姆餐厅》吉他教学" rel="bookmark" target="_blank">赵雷《吉姆餐厅》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/51863.html" title="链接到  陈奕迅《孤独患者》吉他教学" rel="bookmark" target="_blank">陈奕迅《孤独患者》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/49660.html" title="链接到  赵雷《三十岁的女人》吉他教学" rel="bookmark" target="_blank">赵雷《三十岁的女人》吉他教学</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </section>

                        <section class="span4 home-recent related">
                            <div class="widget-box">
                                <div class="widget-title">
                                    <h3>精品付费教学</h3>
                                </div>
                                <div class="widget-content">
                                    <ul class="news-list">
                                        <li>
                                            <a href="http://www.daweijita.com/67487.html" title="链接到  【V】周杰伦《告白气球》吉他弹唱教学" rel="bookmark" target="_blank">【V】周杰伦《告白气球》吉他弹唱教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/68542.html" title="链接到  【V】动力火车《那就这样吧》吉他弹唱教学" rel="bookmark" target="_blank">【V】动力火车《那就这样吧》吉他弹唱教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/67580.html" title="链接到  【V】薛之谦《刚刚好》吉他弹唱教学" rel="bookmark" target="_blank">【V】薛之谦《刚刚好》吉他弹唱教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/64320.html" title="链接到  【V】《未闻花名》主题曲 secret base .君がくれたもの 指弹" rel="bookmark" target="_blank">【V】《未闻花名》主题曲 secret base .君がくれたもの 指弹</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/62506.html" title="链接到  【V】薛之谦《绅士》吉他教学" rel="bookmark" target="_blank">【V】薛之谦《绅士》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/59492.html" title="链接到  【V】BigBang《If You》" rel="bookmark" target="_blank">【V】BigBang《If You》</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/58505.html" title="链接到  【V】宋冬野《莉莉安》吉他教学" rel="bookmark" target="_blank">【V】宋冬野《莉莉安》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/54858.html" title="链接到  【V】田馥甄《小幸运》吉他教学" rel="bookmark" target="_blank">【V】田馥甄《小幸运》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/53999.html" title="链接到  【V】陈奕迅《陪你度过漫长岁月》吉他教学" rel="bookmark" target="_blank">【V】陈奕迅《陪你度过漫长岁月》吉他教学</a>
                                        </li>
                                        <li>
                                            <a href="http://www.daweijita.com/51994.html" title="链接到  【V】杨宗纬《一次就好》吉他教学" rel="bookmark" target="_blank">【V】杨宗纬《一次就好》吉他教学</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </section>
                    </div>

                    <div class="widget-box">
                        <section class="widget-content" style='padding: 0;'>
                            <!-- You can start editing here. -->
                            <div id="comments">
                                <h3>75 条评论</h3>
                            </div>
                            <div class="comments-loading"><i class="icon-spinner icon-spin icon-large"></i> 评论正在加载 ...</div>
                            <ol class="commentlist">
                                <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='width:92%;margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;'>
                                    <div id="div-comment-39615" class="comment-body">
                                        <div class="comment-author vcard ">
                                            <img src="http://cdn.daweijita.com/2017/02/1477791063850-avatar.jpg" alt="꧁墨⃢凝꧂" height="40" width="40" class="avatar">
                                            <div class="floor">
                                                34# </div>
                                            <span class="c-user"><a href='http://user.qzone.qq.com/3229907373' rel='external nofollow' class='url'>꧁墨⃢凝꧂</a></span>: </div>
                                        <p>我有一个电脑能用的节拍器,只要电脑运算速度够快就是准确的,刚刚对比了下,发现那个节拍器慢了,还是去买个节拍器吧,省事</p>
                                        <div class="clear"></div>
                                        <span class="datetime">2017-02-23 12:38 </span>
                                        <span class="reply"><a rel="nofollow" class="comment-reply-login" href="http://www.daweijita.com/login?redirect_to=http%3A%2F%2Fwww.daweijita.com%2F7315.html">登录以回复</a></span>
                                    </div>
                                </li>
                                <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='width:92%;margin:10px 15px;'>
                                    <div id="div-comment-39615" class="comment-body">
                                        <div class="comment-author vcard ">
                                            <img src="http://cdn.daweijita.com/2017/02/1477791063850-avatar.jpg" alt="꧁墨⃢凝꧂" height="40" width="40" class="avatar">
                                            <div class="floor">
                                                34# </div>
                                            <span class="c-user"><a href='http://user.qzone.qq.com/3229907373' rel='external nofollow' class='url'>꧁墨⃢凝꧂</a></span>: </div>
                                        <p>我有一个电脑能用的节拍器,只要电脑运算速度够快就是准确的,刚刚对比了下,发现那个节拍器慢了,还是去买个节拍器吧,省事</p>
                                        <div class="clear"></div>
                                        <span class="datetime">2017-02-23 12:38 </span>
                                        <span class="reply"><a rel="nofollow" class="comment-reply-login" href="http://www.daweijita.com/login?redirect_to=http%3A%2F%2Fwww.daweijita.com%2F7315.html">登录以回复</a></span>
                                    </div>
                                </li>
                                <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='width:92%;margin:10px 15px;'>
                                    <div id="div-comment-39615" class="comment-body">
                                        <div class="comment-author vcard ">
                                            <img src="http://cdn.daweijita.com/2017/02/1477791063850-avatar.jpg" alt="꧁墨⃢凝꧂" height="40" width="40" class="avatar">
                                            <div class="floor">
                                                34# </div>
                                            <span class="c-user"><a href='http://user.qzone.qq.com/3229907373' rel='external nofollow' class='url'>꧁墨⃢凝꧂</a></span>: </div>
                                        <p>我有一个电脑能用的节拍器,只要电脑运算速度够快就是准确的,刚刚对比了下,发现那个节拍器慢了,还是去买个节拍器吧,省事</p>
                                        <div class="clear"></div>
                                        <span class="datetime">2017-02-23 12:38 </span>
                                        <span class="reply"><a rel="nofollow" class="comment-reply-login" href="http://www.daweijita.com/login?redirect_to=http%3A%2F%2Fwww.daweijita.com%2F7315.html">登录以回复</a></span>
                                    </div>
                                </li>
                                <li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style='width:92%;margin:10px 15px;'>
                                    <div id="div-comment-39615" class="comment-body">
                                        <div class="comment-author vcard ">
                                            <img src="http://cdn.daweijita.com/2017/02/1477791063850-avatar.jpg" alt="꧁墨⃢凝꧂" height="40" width="40" class="avatar">
                                            <div class="floor">
                                                34# </div>
                                            <span class="c-user"><a href='http://user.qzone.qq.com/3229907373' rel='external nofollow' class='url'>꧁墨⃢凝꧂</a></span>: </div>
                                        <p>我有一个电脑能用的节拍器,只要电脑运算速度够快就是准确的,刚刚对比了下,发现那个节拍器慢了,还是去买个节拍器吧,省事</p>
                                        <div class="clear"></div>
                                        <span class="datetime">2017-02-23 12:38 </span>
                                        <span class="reply"><a rel="nofollow" class="comment-reply-login" href="http://www.daweijita.com/login?redirect_to=http%3A%2F%2Fwww.daweijita.com%2F7315.html">登录以回复</a></span>
                                    </div>
                                </li>
                            </ol>
                            <div class="page-nav comment-nav">
                                <a class='page-numbers' style='margin-left:15px;' href='#'>1</a>
                                <span class='page-numbers current'>2</span>
                                <a class='page-numbers' href='#'>3</a>
                                <a class="prev page-numbers" href="#">下一页  &raquo;</a></div>
                            <div class="clear"></div>
                            <div id="respond" class="comment-respond">
                                <h3 id="reply-title" class="comment-reply-title">发表评论 <small><a rel="nofollow" id="cancel-comment-reply-link" href="/7315.html#respond" style="display:none;">取消回复</a></small></h3>
                                <p class="must-log-in">要发表评论，您必须先<a href="http://www.daweijita.com/login?redirect_to=http%3A%2F%2Fwww.daweijita.com%2F7315.html">登录</a>。</p>
                            </div>
                            <!-- #respond -->
                        </section>
                    </div>

                </div>


                <aside class="span4 sidebar-right hide-sidebar" role="complementary" itemscope itemtype="http://schema.org/WPSideBar">
                    <div class="widget-box widget ">
                        <div class="widget-content">
                            <a href="http://www.daweijita.com/?p=68044" target="_blank" title="dove新款D系列">
                                <img class="gg430 mt10" src="../../../theme/${theme}/images/a.png" alt="dove新款D系列">
                            </a>
                            <a href="http://www.daweijita.com/?p=79417" target="_blank" title="拿火吉他">
                                <img class="gg430 mt10" src="../../../theme/${theme}/images/b.png" alt="拿火吉他">
                            </a>
                            <a href="http://www.daweijita.com/?p=76247" target="_blank" title="大伟独家原创教材">
                                <img class="gg430 mt10" src="../../../theme/${theme}/images/c.png" alt="大伟独家原创教材">
                            </a>
                        </div>
                    </div>
                    <div id="news-pic-widget-3" class="widget-box widget widget-pic">
                        <div class="widget-title"><span class="icon"><i class="icon-list"></i></span>
                            <h3>最新教学 </h3>
                        </div>
                        <div class="widget-content">
                            <div class="new-pic">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/83007.html" title="链接到  李健《十点半的地铁》吉他弹唱教学" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/lijian_shidianbandeditie_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="李健《十点半的地铁》吉他弹唱教学" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/lijian_shidianbandeditie_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="李健《十点半的地铁》吉他弹唱教学" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/83007.html" title="链接到  李健《十点半的地铁》吉他弹唱教学">
                                    </a>李健《十点半的地铁》吉他弹唱教学</p>
                            </div>
                            <div class="new-pic pic-even">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/82813.html" title="链接到  李建《异乡人》吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/lijian_yixiangren_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="李建《异乡人》吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/lijian_yixiangren_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="李建《异乡人》吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/82813.html" title="链接到  李建《异乡人》吉他弹唱">
                                    </a>李建《异乡人》吉他弹唱</p>
                            </div>
                            <div class="new-pic">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/82665.html" title="链接到  温岚《夏天的风》吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/wenlan_xiatiandefeng_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="温岚《夏天的风》吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/wenlan_xiatiandefeng_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="温岚《夏天的风》吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/82665.html" title="链接到  温岚《夏天的风》吉他弹唱">
                                    </a>温岚《夏天的风》吉他弹唱</p>
                            </div>
                            <div class="new-pic pic-even">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/82554.html" title="链接到  李健/许飞《父亲写的散文诗》吉他教学" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/lijian_fuqinxiedesanwenshi_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="李健/许飞《父亲写的散文诗》吉他教学" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/lijian_fuqinxiedesanwenshi_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="李健/许飞《父亲写的散文诗》吉他教学" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/82554.html" title="链接到  李健/许飞《父亲写的散文诗》吉他教学">
                                    </a>李健/许飞《父亲写的散文诗》吉他教学</p>
                            </div>
                            <div class="new-pic">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/81823.html" title="链接到  邓丽君《夜来香》吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/denglijun_yelaixiang_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="邓丽君《夜来香》吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/04/denglijun_yelaixiang_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="邓丽君《夜来香》吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/81823.html" title="链接到  邓丽君《夜来香》吉他弹唱">
                                    </a>邓丽君《夜来香》吉他弹唱</p>
                            </div>
                            <div class="new-pic pic-even">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/81593.html" title="链接到  李玉刚《刚好遇见你》女声版吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/liyugang_ganghaoyujianni_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="李玉刚《刚好遇见你》女声版吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/liyugang_ganghaoyujianni_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="李玉刚《刚好遇见你》女声版吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/81593.html" title="链接到  李玉刚《刚好遇见你》女声版吉他弹唱">
                                    </a>李玉刚《刚好遇见你》女声版吉他弹唱</p>
                            </div>
                            <div class="new-pic">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/80915.html" title="链接到  赵雷《理想》吉他弹唱教学" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/zhaolei_lixiang_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="赵雷《理想》吉他弹唱教学" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/zhaolei_lixiang_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="赵雷《理想》吉他弹唱教学" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/80915.html" title="链接到  赵雷《理想》吉他弹唱教学">
                                    </a>赵雷《理想》吉他弹唱教学</p>
                            </div>
                            <div class="new-pic pic-even">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/80909.html" title="链接到  陈奕迅/鹿晗《让我留在你身边》吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/chenyixun_rangwoliuzainishenbian_guitar1.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="陈奕迅/鹿晗《让我留在你身边》吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/chenyixun_rangwoliuzainishenbian_guitar1.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="陈奕迅/鹿晗《让我留在你身边》吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/80909.html" title="链接到  陈奕迅/鹿晗《让我留在你身边》吉他弹唱">
                                    </a>陈奕迅/鹿晗《让我留在你身边》吉他弹唱</p>
                            </div>
                            <div class="new-pic">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/80615.html" title="链接到  薛之谦《方圆几里》吉他弹唱教学" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/xuezhiqian_fangyuanjili_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="薛之谦《方圆几里》吉他弹唱教学" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/03/xuezhiqian_fangyuanjili_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="薛之谦《方圆几里》吉他弹唱教学" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/80615.html" title="链接到  薛之谦《方圆几里》吉他弹唱教学">
                                    </a>薛之谦《方圆几里》吉他弹唱教学</p>
                            </div>
                            <div class="new-pic pic-even">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/79792.html" title="链接到  萧敬腾《怎么说我不爱你》吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/02/xiaojingteng_zenmeshuowobuaini_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="萧敬腾《怎么说我不爱你》吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/02/xiaojingteng_zenmeshuowobuaini_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="萧敬腾《怎么说我不爱你》吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/79792.html" title="链接到  萧敬腾《怎么说我不爱你》吉他弹唱">
                                    </a>萧敬腾《怎么说我不爱你》吉他弹唱</p>
                            </div>
                            <div class="new-pic">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/79722.html" title="链接到  王若琳《三个人的晚餐》吉他弹唱" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/02/wangruol_sangerendewancan_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="王若琳《三个人的晚餐》吉他弹唱" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/02/wangruol_sangerendewancan_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="王若琳《三个人的晚餐》吉他弹唱" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/79722.html" title="链接到  王若琳《三个人的晚餐》吉他弹唱">
                                    </a>王若琳《三个人的晚餐》吉他弹唱</p>
                            </div>
                            <div class="new-pic pic-even">
                                <p>
                                    <a class="post-thumbnail" href="http://www.daweijita.com/79274.html" title="链接到  何中华友情出演吉他翻唱《华山论剑》主题曲《世间始终你好》" rel="bookmark">
                                        <img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" data-original="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/02/luowenzhenni_shijianshizhongnihao_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1"
                                            alt="何中华友情出演吉他翻唱《华山论剑》主题曲《世间始终你好》" width="210" height="120" />
                                        <noscript><img src="http://www.daweijita.com/wp-content/themes/wpdx/timthumb.php?src=http://cdn.daweijita.com/2017/02/luowenzhenni_shijianshizhongnihao_guitar.jpg&w=210&h=120&zc=1&q=90&ct=1" alt="何中华友情出演吉他翻唱《华山论剑》主题曲《世间始终你好》" width="210" height="120" /></noscript>
                                    </a>
                                </p>
                                <p class="pic-t">
                                    <a href="http://www.daweijita.com/79274.html" title="链接到  何中华友情出演吉他翻唱《华山论剑》主题曲《世间始终你好》">
                                    </a>何中华友情出演吉他翻唱《华山论剑》主题曲《世间始终你好》</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                    </div>
                    <div id="wpfp-most_favorited_posts" class="widget-box widget wpfp_widget_view">
                        <div class="widget-title"><span class="icona"></span>
                            <h3>最受关注教学</h3>
                        </div>
                        <div class="widget-content">
                            <ul>
                                <li class="most-favorited" style='list-style:disc;'><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 374</span><a href='http://www.daweijita.com/72753.html' title='【V】赵雷《成都》吉他弹唱教学'>【V】赵雷《成都》吉他弹唱教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 288</span><a href='http://www.daweijita.com/42615.html' title='马頔 《南山南》吉他教学'>马頔 《南山南》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 286</span><a href='http://www.daweijita.com/9359.html' title='宋冬野《斑马斑马》吉他教学'>宋冬野《斑马斑马》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 220</span><a href='http://www.daweijita.com/3658.html' title='宋冬野/左立《董小姐》吉他教学'>宋冬野/左立《董小姐》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 214</span><a href='http://www.daweijita.com/46367.html' title='【V】薛之谦《演员》吉他教学'>【V】薛之谦《演员》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 213</span><a href='http://www.daweijita.com/10752.html' title='李克勤《月半小夜曲》吉他教学'>李克勤《月半小夜曲》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 205</span><a href='http://www.daweijita.com/7433.html' title='朴树《平凡之路》吉他教学'>朴树《平凡之路》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 197</span><a href='http://www.daweijita.com/54858.html' title='【V】田馥甄《小幸运》吉他教学'>【V】田馥甄《小幸运》吉他教学</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 184</span><a href='http://www.daweijita.com/4191.html' title='逃跑计划乐队/张恒远《夜空中最亮的星》'>逃跑计划乐队/张恒远《夜空中最亮的星》</a></li>
                                <li class="most-favorited"><span class="fav-num"><img src="../../../theme/${theme}/images/love.png" alt="" class='love'> 163</span><a href='http://www.daweijita.com/2441.html' title='全站教学资源列表（一直在更新，永远不间断……）'>全站教学资源列表（一直在更新，永远不间断……）</a></li>
                            </ul>
                        </div>
                    </div>
                    <div id="category-posts-widget-3" class="widget-box widget category-posts">
                        <div class="widget-title"><span class="iconb"></span>
                            <h3>最新琴友上传 </h3>
                        </div>
                        <div class="widget-content">
                            <ul>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/-15425" title=""><img data-original="http://cn.gravatar.com/avatar/0a51860d58d360e568ae11901759ce27?s=90" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" class="avatar avatar-90" height="90" width="90">
                                        <noscript><img src="http://cn.gravatar.com/avatar/0a51860d58d360e568ae11901759ce27?s=90" class="avatar avatar-90" height="90" width="90"></noscript> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82993.html">宋冬野《斑马斑马》吉他弹唱</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/-15425" title="">篱夜秋晨</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>204</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82993.html#respond">0</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/-21626" title=""><img src="http://cdn.daweijita.com/2017/03/捕获-avatar.jpg" alt="" height="45" width="45" class="avatar"> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82996.html">邓丽君《我只在乎你》-侠客鑫</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/-21626" title="">侠客鑫韵</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>82</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82996.html#respond">0</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/5878" title=""><img src="http://cdn.daweijita.com/2017/04/1480351211755_75a09313-425f-4f6d-8547-ab746fd6cb7-avatar.jpg" alt="" height="45" width="45" class="avatar"> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82881.html">周杰伦《告白气球》双吉他版</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/5878" title="">少年此间</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>337</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82881.html#comments">1</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/-9759" title=""><img src="http://cdn.daweijita.com/2016/06/IMG_20160515_092742_副本-avatar.jpg" alt="" height="45" width="45" class="avatar"> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82845.html">汪峰《像一只小鸟》–音乐云鹤</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/-9759" title="">音乐云鹤</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>117</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82845.html#respond">0</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/wuxiongcai" title=""><img data-original="http://cn.gravatar.com/avatar/d6dc361e8cc362afdb7842a974def939?s=90" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" class="avatar avatar-90" height="90" width="90">
                                        <noscript><img src="http://cn.gravatar.com/avatar/d6dc361e8cc362afdb7842a974def939?s=90" class="avatar avatar-90" height="90" width="90"></noscript> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82753.html">赵雷《成都》-吉他翻唱</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/wuxiongcai" title="">wuxiongcai</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>493</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82753.html#comments">1</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/8972-2" title=""><img src="http://cdn.daweijita.com/2016/06/hdImg_c9661230fca937e08c2390437ffe98041462580356107-avatar.jpg" alt="" height="45" width="45" class="avatar"> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82780.html">贰佰《玫瑰》-隔壁老曲</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/8972-2" title="">隔壁老曲</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>161</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82780.html#respond">0</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/weizhichao30" title=""><img data-original="http://cn.gravatar.com/avatar/6217d6182e02df926da928f840efc413?s=90" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" class="avatar avatar-90" height="90" width="90">
                                        <noscript><img src="http://cn.gravatar.com/avatar/6217d6182e02df926da928f840efc413?s=90" class="avatar avatar-90" height="90" width="90"></noscript> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82659.html">《Someone Like You》——Adele（阿祁翻弹）</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/weizhichao30" title="">weizhichao30</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>339</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82659.html#respond">0</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/d%e7%9a%84%e6%84%8f%e5%bf%97" title=""><img data-original="http://cn.gravatar.com/avatar/1dd69da3c7ec31b941440b922023739f?s=90" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" class="avatar avatar-90" height="90" width="90">
                                        <noscript><img src="http://cn.gravatar.com/avatar/1dd69da3c7ec31b941440b922023739f?s=90" class="avatar avatar-90" height="90" width="90"></noscript> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82663.html">《去大理》郝云——吉他弹唱</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/d%e7%9a%84%e6%84%8f%e5%bf%97" title="">D的意志</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>286</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82663.html#respond">0</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/ypmike" title=""><img src="http://cdn.daweijita.com/2016/05/头像1_副本-avatar.jpg" alt="" height="45" width="45" class="avatar"> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82678.html">《凉凉》张碧晨/杨宗纬—殷鹏</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/ypmike" title="">ypmike</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>321</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82678.html#comments">3</a></span>
                                    </p>
                                </li>
                                <li class="widget-thumb posts-cat">
                                    <a class="post-thumbnail" href="http://www.daweijita.com/author/d%e7%9a%84%e6%84%8f%e5%bf%97" title=""><img data-original="http://cn.gravatar.com/avatar/1dd69da3c7ec31b941440b922023739f?s=90" src="http://www.daweijita.com/wp-content/themes/wpdx/images/grey.gif" class="avatar avatar-90" height="90" width="90">
                                        <noscript><img src="http://cn.gravatar.com/avatar/1dd69da3c7ec31b941440b922023739f?s=90" class="avatar avatar-90" height="90" width="90"></noscript> </a>
                                    <p class="post-t">
                                        <a href="http://www.daweijita.com/82582.html">《傲寒》马頔——吉他弹唱</a>
                                    </p>
                                    <p class="post-meta">
                                        <span><img src="../../../theme/${theme}/images/person.png" alt="" class='person'><a href="http://www.daweijita.com/author/d%e7%9a%84%e6%84%8f%e5%bf%97" title="">D的意志</a></span>
                                        <span><img src="../../../theme/${theme}/images/video.png" alt="" class='video'>400</span>
                                        <span><img src="../../../theme/${theme}/images/pinglun.png" alt="" class='pinglun'><a href="http://www.daweijita.com/82582.html#comments">1</a></span>
                                    </p>
                                </li>
                            </ul>
                            <div class="clear"></div>
                        </div>
                    </div>
                </aside>
            </div>
            </div>
            </div>
  
	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/login.js'></script>
            <script src='../../../theme/${theme}/js/common.js'></script>
        </body>

        </html>
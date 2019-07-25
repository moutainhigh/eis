<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-注册账号</TITLE>
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
            <!--<script src='../../../theme/${theme}/js/ssi-uploader.js'></script>-->
            <!-- EVITAR CLICK DERECHO-->
            <script language="Javascript">
                document.oncontextmenu = function() {
                        return false
                    }
                    // End -->
            </script>

            <!-- SELECCION DE TEXTO-->
            <script type="text/javascript">
            // $(function(){
                // 上传图片
            //     $('#ssi-upload').ssi_uploader({
            //         url:'/user/changeHeadPic.json',
            //         locale: "zh_CN",
            //         maxFileSize:6,
            //         allowed:['jpg','gif','png','pdf'],
            //         responseValidation:{
            //             validationKey: 'type',
            //             resultKey: 'data',
            //             success: 'success',
            //             error: 'error'
            //         },
            //         onUpload:function(){
            //             console.log('文件上传完毕！');
            //         }
            //     });
            // })
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

                            <div class="widget-box">
                                <article class="widget-content single-post" itemscope style='padding: 0;'>
                                    <header id="post-header">
                                        <h1 class="page-title" itemprop="headline">注册账号</h1>
                                    </header>
                                    <div class="entry" itemprop="articleBody">
                                        <form class="wpuf-form-add" method='post' id='registerForm'>
                                            <ul class="wpuf-form">
                                                <li class="wpuf-el ">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-toc">协议条款 <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields  wpuf_toc_5046">
                                                        <textarea rows="8" cols="40" disabled="disabled" name="toc">用户注册协议

一、注册协议条款的确认和接受
七星指弹教室(以下简称”本网站“)同意按照本协议的规定及其不定时发布的操作规则提供基于互联网和移动网的相关服务(以下称“网络服务”)。
为获得网络服务, 申请人应当认真阅读、充分理解本《协议》中各条款。审慎阅读并选择接受或不接受本《协议》(未成年人应在法定监护人陪同下阅读)。

同意接受本协议的全部条款的, 申请人应当按照页面上的提示完成全部的注册程序, 并在注册程序过程中在”已阅读并同意协议条款“选项卡打钩, 否则视为不接受本《协议》全部条款, 申请人应当终止并退出申请。

本《协议》可由商丘七星文化传播有限公司随时更新, 更新后的协议条款一旦公布即代替原来的协议条款, 恕不再另行通知, 用户可在本网站查阅最新版协议条款。在本网站修改《协议》条款后, 如果用户不接受修改后的条款, 请立即停止使用本网站提供的网络服务, 继续使用的用户将被视为已接受了修改后的协议。

二、服务内容
1、网络服务的具体内容由本网站根据实际情况提供, 例如乐器教育视频、音频、教学文章、谱例、指弹铃音、问答中心、等相关应用服务。

2、本网站提供的部分网络服务为收费的网络服务, 用户使用收费网络服务需要向本网站支付一定的费用。对于收费的网络服务, 本网站会在用户使用之前给予用户明确的提示, 只有用户根据提示确认其愿意支付相关费用, 用户才能使用该等收费网络服务。如用户未支付相关费用, 则本网站有权不向用户提供该等收费网络服务。

3、本网站仅提供相关的网络服务, 除此之外与相关网络服务有关的设备(如个人电脑、手机、及其他与接入互联网或移动网有关的装置)及所需的费用(如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费)均应由用户自行负担。

三、用户账号
1、经本网站注册系统完成注册程序并通过身份认证的用户即为正式用户。 
2、如发现用户账号中含有不雅文字或不恰当名称的, 本网站保留注销其用户账号的权利。 
3、用户账号的所有权归本网站, 用户完成申请注册手续后, 用户享有使用权。 
4、用户有义务保证密码和账号的安全, 用户利用该账号所进行的一切活动引起的任何损失或损害, 由用户自行承担全部责任, 本网站不承担任何责任。如用户发现账号遭到未授权的使用或发生其他任何安全问题, 应立即修改账号密码并妥善保管。因黑客行为或用户的保管疏忽导致账号非法使用, 本网站不承担任何责任。 
5、用户账号独占使用权仅属于初始申请注册人。为避免因账号问题产生纠纷, 用户不应私下有偿、无偿转让或合用账号, 用户应当自行承担因违反此要求而遭致的任何损失。本网站保留对交易和共享（合用）账户行为进行调查，并停止该账户功能甚至注销该账号的权利。
6、用户账号在丢失、遗忘密码及因合用产生使用权归属纠纷后, 须遵照本网站的找回账号流程找回，或者联系本网站客服人工找回。用户可以凭初始注册资料向本网站申请找回账号。本网站的账户恢复机制仅负责识别申请用户所提资料与系统记录资料是否一致, 而无法识别申诉人是否系账号的真正使用权人。对用户因被他人冒名申请而致的任何损失, 本网站不承担任何责任, 用户知晓账号及密码保管责任在于用户, 本网站并不承诺账号丢失或遗忘密码后用户一定能通过申诉找回账号。用户应当谨慎填写初始注册邮箱作为确认接收争议账号的指定邮箱。

四、付费商品购买
1、用户在购买前必须对付费的条款和方式进行充分的了解之后再进行操作。
2、本网站出售的非实物类商品均视为为虚拟商品（包含VIP账号特权、付费曲谱，教学视频、音频、谱例、在线教学），一旦交易不可退款。请用户在购买付费服务前对索要购买服务的内容、价格、以及相关条款充分知悉后，再决定是否付费购买。
4、本网站拥有对所发布内容的定价权（包括但不限于是否收费、何时开始或者结束收费、是否对VIP用户收费、以及定价等权利）以上权利由本网站根据运营需要自行决定。

五、VIP账号和服务
1、VIP服务是用户在普通账号（在本站注册后获得的账号）的基础上通过付费获得的升级账号权限的服务。VIP权限包含但不限于对部分付费内容的免费获得以及打折购买。
2、VIP账号属于包时服务，VIP在有效期结束后即停止服务。用户可以通过继续购买的方式获得一个新的周期的VIP服务。VIP服务不可人工终止，不可退款、也不可按已经使用的时间折现或退款。用户在购买任何一种包时VIP服务前，应该对服务内容做充分的了解。
3、本网站无法对VIP用户提供的特权内容提供任何承诺，包括为VIP用户提供何种内容、内容的数量，内容更新等。关于VIP用户可获得的内容本网站将根据实际的运营情况自行决定。

六、不得利用本站危害国家安全、泄露国家秘密，不得侵犯国家社会集体的和公民的合法权益，不得利用本站制作、复制和传播下列信息： 

1、煽动抗拒、破坏宪法和法律、行政法规实施的；
2、煽动颠覆国家政权，推翻社会主义制度的；
3、煽动分裂国家、破坏国家统一的；
4、煽动民族仇恨、民族歧视，破坏民族团结的；
5、捏造或者歪曲事实，散布谣言，扰乱社会秩序的；
6、公然侮辱他人或者捏造事实诽谤他人的，或者进行其他恶意攻击的；
7、损害国家机关信誉的；
8、其他违反宪法和法律行政法规的；
9、进行商业广告行为的。

七、互相尊重，对自己的言论和行为负责。

八、版权声明
本网站提供的网络服务中包含的任何文本、图片、图形、音频和/或视频资料均受版权、商标和/或其它财产所有权法律的保护, 未经相关权利人同意, 上述资料均不得在任何媒体直接或间接发布、播放、出于播放或发布目的而改写或再发行, 或者被用于其他任何商业目的。所有以上资料或资料的任何部分仅可作为私人和非商业用途保存。本网站不就由上述资料产生或在传送或递交全部或部分上述资料过程中产生的延误、不准确、错误和遗漏或从中产生或由此产生的任何损害赔偿, 以任何形式, 向用户或任何第三方负责。

九、隐私保护
保护用户隐私是本网站的一项基本政策, 本网站保证不对外公开或向第三方提供用户的注册资料及用户在使用网络服务时存储在本网站内的非公开内容, 但下列情况除外:

1、事先获得用户的书面明确授权; 
2、根据有关的法律法规要求; 
3、按照相关政府主管部门的要求; 
4、为维护社会公众的利益; 
5、为维护本网站的合法权益;

十、法律解释
本网站隶属于商丘七星文化传播有限公司，任何有关本网站的法律事务代由商丘七星文化传播有限公司处理。4、为维护社会公众的利益; 
5、为维护本网站的合法权益;</textarea>
                                                        <label>
            <span data-required="yes" data-type="radio">
                <input type="checkbox" checked="checked" id='checkout' /> 已阅读并同意协议条款            </span>
            </label>
                                                    </div>
                                                </li>
                                                <li class="wpuf-el user_login username">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_login">用户名 <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input class="textfield wpuf_user_login_5046" id="userName" type="text" name="username" value='' placeholder="不能含有空格" size="40" />
                                                        <span class="wpuf-help" id='user_text'>请输入2至20个字符的用户名，个性也是一种态度！</span>

                                                    </div>

                                                </li>
                                                <li class="wpuf-el user_email email">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_email">手机号： <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="wpuf-user_email" type="email" class="email  wpuf_user_email_5046" data-required="yes" data-type="text" name="email" placeholder="非常重要！请填写您的常用手机号" value="" size="40" />
                                                        <span class="wpuf-help" id='email_text'></span>
                                                    </div>

                                                </li>
                                                <li class="wpuf-el user_email email">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user_email">验证码 <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input type="text" placeholder="请输入验证码" id="smsRegisterSign" name="smsRegisterSign" class="passwd" maxlength="6" autocomplete="off">
                                                        <input type="button" class="btn_sendCode" id="btnSendCode" value="发送验证码" onclick="sendMessage();" style="padding: 5px;">
                                                        <span class="wpuf-help" id='email_text'></span>
                                                    </div>

                                                </li>
                                                <li class="wpuf-el password password">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-password">密码 <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="userPassword" type="password" class="password  wpuf_password_5046" data-required="yes" data-type="text" name="userPassword" placeholder="至少7个字符" value="" size="40" />
                                                        <span class="wpuf-help" id='password_text'>(至少7个字符，可用大小写字母、数字和符号，例如! " ? $ % ^ & )</span>
                                                    </div>

                                                </li>
                                                <li>
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-pass2">确认密码 <span class="required">*</span></label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input id="userPassword2" type="password" class="password  wpuf_password_5046" data-required="yes" data-type="text" name="userPassword2" value="" size="40" />
                                                    </div>

                                                </li>
                                                <li>
                                                    <div class="wpuf-label">
                                                        &nbsp;
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <div id="pass-strength-result">强度评估</div>
                                                    </div>
                                                </li>
                                                <!--<li class="wpuf-el avatar user-avatar">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-avatar">上传头像</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <input type="file" multiple id="ssi-upload" name="fileupload"/>
                                                            </div>
                                                        </div>
                                                        <span class="wpuf-help">
                                                            <strong><font color="red">修改头像，请先删除之前的头像。</font></strong>
                                                            <font color="red">图片文件名后缀只能是jpg、png、gif！</font>尺寸为 100-200像素的正方形 jpg、png 图片为佳，不能超过 2M,建议使用IE、FireFox、Chrome浏览器。
                                                        </span>
                                                    </div>
                                                </li>-->
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
                                                        <input id="wpuf-user_url" type="url" class="url  wpuf_user_url_5046" data-required="no" data-type="text" name="user_url" placeholder="例如 http://www.qixingyueqi.com" value="" size="40" />
                                                        <span class="wpuf-help"></span>
                                                    </div>
                                                </li>
                                                <li class="wpuf-el qq_weibo qq-weibo">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-qq_weibo">腾讯微博</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input class="textfield wpuf_qq_weibo_5046" id="qq_weibo" type="text" data-required="no" data-type="text" name="qq_weibo" placeholder="例如 http://t.qq.com/qixingyueqi" value="" size="40" />
                                                        <span class="wpuf-help"></span>

                                                    </div>
                                                </li>
                                                <li class="wpuf-el sina_weibo sina-weibo">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-sina_weibo">新浪微博</label>
                                                    </div>

                                                    <div class="wpuf-fields">
                                                        <input class="textfield wpuf_sina_weibo_5046" id="sina_weibo" type="text" data-required="no" data-type="text" name="sina_weibo" placeholder="例如 http://weibo.com/qixingyueqi" value="" size="40" />
                                                        <span class="wpuf-help"></span>

                                                    </div>

                                                </li>
                                                <li class="wpuf-el user-proof">
                                                    <div class="wpuf-label">
                                                        <label for="wpuf-user-proof">验证问题 <span class="required">*</span></label>
                                                    </div>
                                                    <div class="wpuf-fields">
                                                        <input class="textfield" type="text" id="user_proof" data-required="yes" data-type="text" name="user_proof" required="required" placeholder="本站的域名是？" value="" size="40" />
                                                        <span class="wpuf-help" id="wz_text">请输入【www.qixingyueqi.com】，不含方括号【】</span>
                                                    </div>
                                                </li>-->
                                                <li class="wpuf-submit">
                                                    <div class="wpuf-label">
                                                        &nbsp;
                                                    </div>
                                                    <div class="wpuf-fields">
                                                            <input type="button" onclick='send()' value="注册" />
                                                    </div>
                                                </li>
                                                
                                            </ul>
                                        </form>
                                    </div>
                                </article>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


	        <%@include file="/WEB-INF/jsp/include/pfooter.jsp" %>
	        <%@include file="/WEB-INF/jsp/include/pkf.jsp" %>
            <script src='../../../theme/${theme}/js/common.js'></script>
            <script src='../../../theme/${theme}/js/register.js'></script>
        </body>

        </html>
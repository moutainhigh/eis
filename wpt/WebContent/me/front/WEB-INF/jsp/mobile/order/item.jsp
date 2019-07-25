<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@include file="/WEB-INF/jsp/include/tags.jsp" %>
        <HTML xmlns="http://www.w3.org/1999/xhtml">

        <HEAD>
            <TITLE>${systemName}-待评论商品</TITLE>
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
                    // 提交评论
                  function startComment(a,title){ 
                      if($("#review_text").val().length == 0 || $("#review_text").val().length > 150){
                          alert('输入不能为空或超过150字了');
                      }else{
                            $.ajax({
                                type:"POST",
                                url: '/order/submitComment.json',
                                data:{
                                    tid:a,
                                    content:$("#review_text").val(),
                                    title:title
                                },
                                dataType:'json',
                                success:function (data) {
                                    console.log(data);
                                    //   if(data.message.operateCode == 500031){
                                    //       alert(data.message.message);	
                                    //   }else{
                                    alert(data.message.message);
                                    window.location.reload();
                                    //   } 
                                },
                                error:function(XMLResponse){
                                    alert("操作失败:" + XMLResponse.responseText);
                                },
                            });
                        }
                    }

                //  关闭评论
                 function closeComment(){
                    $('.box div').empty();
                    $('.box').css('display','none');
                 }

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
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="'+a.data.transactionId+'" name="tid"><input type="hidden" value="'+a.data.refTitle+'" name="title"><span class="commentOrder">待评价</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                        // 点击评价弹出评论界面 
                                        $('.commentOrder').each(function(){
                                            $(this).click(function(){
                                                var a = $(this).siblings('input[name="tid"]').val();
                                                var title = $(this).siblings('input[name="title"]').val();
                                                $('.box div').append('<form  method="post" id="commentform" class="comment-form"><div id="smilies" style="margin:0;display:inline-block;"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><img src="../../../theme/${theme}/images/close.png" style="width: 10px;float: right;cursor:pointer;" onclick="closeComment()" /><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input type="button" id="submit" class="submit" value="发表评论" onclick="startComment('+"'"+a+"'"+","+"'"+title+"'"+')"></p></form>');
                                                $('.box').css('display','block');
                                            })
                                        })
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
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="'+a.data.transactionId+'" name="tid"><input type="hidden" value="'+a.data.refTitle+'" name="title"><span class="commentOrder">待评价</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                        // 点击评价弹出评论界面 
                                        $('.commentOrder').each(function(){
                                            $(this).click(function(){
                                                var a = $(this).siblings('input[name="tid"]').val();
                                                var title = $(this).siblings('input[name="title"]').val();
                                                $('.box div').append('<form  method="post" id="commentform" class="comment-form"><div id="smilies" style="margin:0;display:inline-block;"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><img src="../../../theme/${theme}/images/close.png" style="width: 10px;float: right;cursor:pointer;" onclick="closeComment()" /><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input type="button" id="submit" class="submit" value="发表评论" onclick="startComment('+"'"+a+"'"+","+"'"+title+"'"+')"></p></form>');
                                                $('.box').css('display','block');
                                            })
                                        })
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
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="'+a.data.transactionId+'" name="tid"><input type="hidden" value="'+a.data.refTitle+'" name="title"><span class="commentOrder">待评价</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                        // 点击评价弹出评论界面 
                                        $('.commentOrder').each(function(){
                                            $(this).click(function(){
                                                var a = $(this).siblings('input[name="tid"]').val();
                                                var title = $(this).siblings('input[name="title"]').val();
                                                $('.box div').append('<form  method="post" id="commentform" class="comment-form"><div id="smilies" style="margin:0;display:inline-block;"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><img src="../../../theme/${theme}/images/close.png" style="width: 10px;float: right;cursor:pointer;" onclick="closeComment()" /><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input type="button" id="submit" class="submit" value="发表评论" onclick="startComment('+"'"+a+"'"+","+"'"+title+"'"+')"></p></form>');
                                                $('.box').css('display','block');
                                            })
                                        })
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
                                            return '<tr><td align="center">'+a.createTime+'</td><td align="center">'+a.data.refTitle+'</td><td align="center"><a href="'+a.data.refUrl+'">继续阅读</a></td><td  align="center" style="cursor:pointer;"><input type="hidden" value="'+a.data.transactionId+'" name="tid"><input type="hidden" value="'+a.data.refTitle+'" name="title"><span class="commentOrder">待评价</span></td></tr>'
                                        })
                                        $('.widefat tbody').empty().append(commentList);
                                        // 点击评价弹出评论界面 
                                        $('.commentOrder').each(function(){
                                            $(this).click(function(){
                                                var a = $(this).siblings('input[name="tid"]').val();
                                                var title = $(this).siblings('input[name="title"]').val();
                                                $('.box div').append('<form  method="post" id="commentform" class="comment-form"><div id="smilies" style="margin:0;display:inline-block;"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><img src="../../../theme/${theme}/images/close.png" style="width: 10px;float: right;cursor:pointer;" onclick="closeComment()" /><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input type="button" id="submit" class="submit" value="发表评论" onclick="startComment('+"'"+a+"'"+","+"'"+title+"'"+')"></p></form>');
                                                $('.box').css('display','block');
                                            })
                                        })
                                    },
                                    error:function(XMLResponse){
                                        alert("操作失败:" + XMLResponse.responseText);
                                    }
                                })
                            }
                        })
                        	
                        // 点击评价弹出评论界面 
                        $('.commentOrder').each(function(){
                            $(this).click(function(){
                                var a = $(this).siblings('input[name="tid"]').val();
                                var title = $(this).siblings('input[name="title"]').val();
                                $('.box div').append('<form  method="post" id="commentform" class="comment-form"><div id="smilies" style="margin:0;display:inline-block;"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><img src="../../../theme/${theme}/images/close.png" style="width: 10px;float: right;cursor:pointer;" onclick="closeComment()" /><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input type="button" id="submit" class="submit" value="发表评论" onclick="startComment('+"'"+a+"'"+","+"'"+title+"'"+')"></p></form>');
                                $('.box').css('display','block');
                            })
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
            #smilies img{
                padding:0;
                margin: 0;
                border: 0;
            }
            .box{
                background: rgba(0,0,0,0.5);
                position: fixed;
                top: 0;
                bottom: 0;
                left: 0;
                right: 0;
                z-index: 99999;
            }
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
            <div class="box" style="display: none">
                <div style="margin: 100px auto;width:50%;"></div>    
            </div> 
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

                            <p style='    padding: 15px 15px 15px 28px;'>${node.desc}</p>
                        </header>
                        <div class="span12">

                            <div class="widget-box user-center">
                                <div id="user-left">
                                    <div class="user-avatar myname">
                                        <img data-original="#" src="../../../theme/basic/images/touxiang.png" class="avatar avatar-200" id="headImg" height="200" width="200">
                                        <p>${frontUser.username}</p>
                                    </div>
                                    <ul id="user-menu">
                                        <!--<li><a href="/content/user/20170515102939.shtml?preview=2DFA076BB87470636F4E7E80C12C586365D3AEDB4C81F872C0DE8A99FE9FA42D"><span class="icon-jpy"></span>账户余额</a></li>-->
                                        <li><a href="/content/friend/upload/index.shtml"><span class="icon-play-circle"></span>我的作品</a></li>
                                        <li>
                                            <a href="/content/user/20170515123310.shtml?preview=E92C831F108E3E3D5F74A844C418F52B92EAA6FCAF3B004E66354683006A957C"><span class="icon-heart"></span>我的收藏</a></li>
                                        <li class="current-menu-item"><a href="/content/user/20170515125420.shtml?preview=525940543BC2641DD422CF13501B4C719DA3DCEE9232775527AB4BF88D22B1E8&vPro=purchaseList" style='background: url("../../../theme/${theme}/images/greenshop.png") no-repeat; 
                background-position-y: 4px;
                background-size: 12px;'><span class="icon-shopping-cart"></span>已购教学</a><img  class="down_list" src="../../../theme/${theme}/images/shop-right.png" alt="">
                <ul class="comment_down_list">
                    <li>
                        <img style="left: 12px;" src="../../../theme/${theme}/images/shop-righta.png" alt="">
                        <span style="margin-left: 7px;"><a style="background: #fff;color:gray;" href="/comment/commentListOnPage.shtml?flag=my">我的评论</a></span>
                    </li>
                    <li>
                        <img style="left: 25px;" src="../../../theme/${theme}/images/shop-right.png" alt="">    
                        <span style="margin-left: 21px;"><a style="background: #fff;color:#31a030;" href="/order/item.shtml?status=710052">待评论商品</a></span>
                    </li>
                </ul>
                </li>
                                        <li><a href="/content/user/20170515130841.shtml?preview=8522EDBB6A0A0CA2A9B67FC2577D40F50A85647ECD689EE49012C6DD73057B20&vPro=purchaseList"><span class="icon-gift"></span>VIP订购</a></li>
                                        <li><a href="/content/user/20170515133906.shtml?preview=7991CF14C46EED8461848DB054D7ABEC9B7A9BDB109B333D5E2640B2950E1C95"><span class="icon-envelope"></span>站内信息</a></li>
                                        <li><a href="/content/user/20170515141440.shtml?preview=FA5F1259136D6FA8994C28370FDC834C232C6A10B52F6FD05056CFC0C721EB65"><span class="icon-cog"></span>修改资料</a></li>
                                    </ul>
                                </div>
                                <div class="widget-content single-post" id="user-right" itemscope>
                                    <div id="post-header">
                                        <div class="feedback"><a href="/content/user/20170515133906.shtml?defaultReceiver=七星乐器"><i class="icon-pencil"></i> 反馈建议</a></div>
                                        <h1 class="page-title" itemprop="headline">待评价商品</h1>
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
                                                        <th width="15%" style="border-right: 1px solid #eee;">订单状态</th>
                                                        <!--<th width="15%">订单状态</th>-->
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <c:choose>
                                                            <c:when test="${empty orderList}">
                                                                <tr>
                                                                    <td colspan="7" align="center">您目前还没有待评价商品！！</td>
                                                                </tr> 
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach var="item" items="${orderList}" varStatus="status">
                                                                    <tr>
                                                                        <!--<c:choose>
                                                                            <c:when test="${item.currentStatus == '710053'}">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <td  align="center">${item.transactionId}</td>
                                                                            </c:otherwise>
                                                                        </c:choose>-->
                                                                        <td  align="center"><fmt:formatDate value="${item.enterTime}" type="both"/></td>
                                                                        <td  align="center">${item.name}</td>
                                                                        <!--<td  align="center">${item.price.marketPrice}</td>-->
                                                                        <td  align="center"><a href="${item.itemDataMap.refUrl.dataValue}">继续阅读</a></td>
                                                                        <td  align="center" style="cursor:pointer;"><input type="hidden" value="${item.transactionId}" name="tid"><input type="hidden" value="${item.name}" name="title"><span class="commentOrder">待评价</span></td>  
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
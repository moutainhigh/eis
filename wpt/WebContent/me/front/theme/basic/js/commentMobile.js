        // 产品标题
        var title = $('.post-title').text();
        var name = jQuery.cookie('eis_username');
        // 评论表情点击
            function arrow(){
                //  var str = '<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />';
                var font = ' :arrow: ';
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function biggrin(){
                var font = ' :love: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function exclaim(){
                var font = ' :!: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

            function question(){
                var font = ' :question: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function cool(){
                var font = ' :cool: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function rolleyes(){
                var font = ' :roll: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function eek(){
                var font = ' :eek: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function evil(){
                var font = ' :evil: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function razz(){
                var font = ' :razz: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function mrgreen(){
                var font = ' :mrgreen: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function smile(){
                var font = ' :smile: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function redface(){
                var font = ' :chijing: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function lol(){
                var font = ' :lol: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function mad(){
                var font = ' :mad: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function twisted(){
                var font = ' :twisted: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

            function wink(){
                var font = ' :wink: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

            function idea(){
                var font = ' :idea: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

            function cry(){
                var font = ' :cry: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function surprised(){
                var font = ' :shock: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

            function neutral(){
                var font = ' :neutral: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

            function sad(){
                var font = ' :sad: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }

           function confused(){
                var font = ' :confused: '; 
                var arr = $('#review_text').val();
                $('#review_text').val(arr+font);
            }



            //  发表评论
            function publishSubmit(a){
                var name = jQuery.cookie('eis_username');
                var str = new Date();
                var time = str.getFullYear()+'-';
                time += str.getMonth() +'-';
                time += str.getDate();
                time += ' ' + str.getHours() + ':' + str.getSeconds() + ' ';
            if( $('#review_text').val() == ''){
                alert('评论不能为空！');
            }else if($('#review_text').val().length > 150){
                alert('评论字数不得超过150字！');
            }else{
                $.ajax({
                    type:"POST",
                    url: '/comment/submit.json',
                    data:{
                        objectType:a,
                        objectId:$("#udid").val(),
                        content:$("#review_text").val(),
                        title:title
                    },
                    dataType:'json',
                    success:function (data) {
                        // var comments = $('.text').val();
                        // var comment = '';
                        // comments = comments.replace(/:arrow:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />');
                        // comments = comments.replace(/:grin:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" class="lazy" />');
                        // comments = comments.replace(/:!:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif" class="lazy" />');
                        // comments = comments.replace(/:question:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" class="lazy" />');
                        // comments = comments.replace(/:cool:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" class="lazy" />');
                        // comments = comments.replace(/:roll:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif" class="lazy" />');
                        // comments = comments.replace(/:eek:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" class="lazy" />');
                        // comments = comments.replace(/:evil:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif" class="lazy" />');
                        // comments = comments.replace(/:razz:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif" class="lazy" />');
                        // comments = comments.replace(/:mrgreen:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif" class="lazy" />');
                        // comments = comments.replace(/:smile:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif" class="lazy" />');
                        // comments = comments.replace(/:oops:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif" class="lazy" />');
                        // comments = comments.replace(/:lol:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif" class="lazy" />');
                        // comments = comments.replace(/:mad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif" class="lazy" />');
                        // comments = comments.replace(/:twisted:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif" class="lazy" />');
                        // comments = comments.replace(/:wink:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif" class="lazy" />');
                        // comments = comments.replace(/:idea:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif" class="lazy" />');
                        // comments = comments.replace(/:cry:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif" class="lazy" />');
                        // comments = comments.replace(/:shock:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif" class="lazy" />');
                        // comments = comments.replace(/:neutral:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif" class="lazy" />');
                        // comments = comments.replace(/:sad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif" class="lazy" />');
                        // comment = comments.replace(/:confused:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif" class="lazy" />');
                        if(data.message.operateCode == 500031){
                                alert(data.message.message);	
                            }else{
                                alert(data.message.message);
                                window.location.reload();
                            }  
                        },
                        error:function(XMLResponse){
                                alert("操作失败:" + XMLResponse.responseText);
                                },
                    });	
                }
            }	


      // 回复评论
      function authorSubmit(a,b,c,d){
                var name = jQuery.cookie('eis_username');
                var str = new Date();
                var time = str.getFullYear()+'-';
                time += str.getMonth() +'-';
                time += str.getDate();
                time += ' ' + str.getHours() + ':' + str.getSeconds() + ' ';
                if(a == name ){
                    alert('不能评论自己！');
                }else if($('#review_text').val() == ''){
                    alert('评论不能为空！');
                }else if($('#review_text').val().length >150){
                    alert('评论字数不得超过150字！');
                }else{   
                    $.ajax({
                        type:"POST",
                        url: '/comment/submit.json',
                        data:{ 
                            objectType:d,
                            objectId:$("#udid").val(),
                            content:$("#review_text").val(),
                            relatedCommentId:b,
                            replyTo:c,
                            title:title
                        },
                        dataType:'json',
                        success:function (data) { 
                            // var comments = $('#review_text').val();
                            // var comment = '';
                            // comments = comments.replace(/:arrow:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />');
                            // comments = comments.replace(/:grin:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" class="lazy" />');
                            // comments = comments.replace(/:!:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif" class="lazy" />');
                            // comments = comments.replace(/:question:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" class="lazy" />');
                            // comments = comments.replace(/:cool:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" class="lazy" />');
                            // comments = comments.replace(/:roll:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif" class="lazy" />');
                            // comments = comments.replace(/:eek:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" class="lazy" />');
                            // comments = comments.replace(/:evil:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif" class="lazy" />');
                            // comments = comments.replace(/:razz:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif" class="lazy" />');
                            // comments = comments.replace(/:mrgreen:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif" class="lazy" />');
                            // comments = comments.replace(/:smile:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif" class="lazy" />');
                            // comments = comments.replace(/:oops:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif" class="lazy" />');
                            // comments = comments.replace(/:lol:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif" class="lazy" />');
                            // comments = comments.replace(/:mad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif" class="lazy" />');
                            // comments = comments.replace(/:twisted:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif" class="lazy" />');
                            // comments = comments.replace(/:wink:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif" class="lazy" />');
                            // comments = comments.replace(/:idea:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif" class="lazy" />');
                            // comments = comments.replace(/:cry:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif" class="lazy" />');
                            // comments = comments.replace(/:shock:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif" class="lazy" />');
                            // comments = comments.replace(/:neutral:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif" class="lazy" />');
                            // comments = comments.replace(/:sad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif" class="lazy" />');
                            // comment = comments.replace(/:confused:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif" class="lazy" />');
                            if(data.message.operateCode == 500031){
                                alert("回复成功");
                                $('.commentlist').append('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid#E1E1E1padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png"  height="40" width="40" class="avatar"><div class="floor">34# </div><span class="c-user"><a href="#" rel="external nofollow" class="url" >'+name+'</a>回复<a class="url">'+a+'</a></span>: </div><p>'+comment+'</p><div class="clear"></div><span class="datetime">'+time+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span></div></li>');
                                
                                $("html,body").animate({"scrollTop":$('#top').height()});

                                // 点击开启回复功能
                                    $('.reply').each(function(){
                                        $(this).click(function(){
                                        var people = $(this).siblings('div').find('a').eq(0).text();
                                        var commentId = $(this).siblings('input').val();
                                        var relatedName = $(this).siblings('div').eq(0).find('a').eq(0).text();
                                            if($(this).parent("div").siblings().text() == ''){
                                                $('#respond p').empty();
                                                $('#respond').remove();
                                                $(this).parents(".comment").append('<div id="respond" class="comment-respond"><form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea required="" id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input  type="button" id="submit" class="submit" value="发表评论" onclick="authorSubmit('+"'"+people+"'"+"'"+','+commentId+','+"'"+relatedName+"'"+','+"'"+a+"'"+')"> <input type="hidden" name="comment_post_ID" value="1337" id="comment_post_ID"><input type="hidden" name="comment_parent" id="comment_parent" value="0"></p></form></div>');   
                                                $('#review_text').focus();
                                            }else{
                                                return ;
                                            }
                                        })
                                    })	
                            }else{
                                alert(data.message.message);
                                window.location.reload();
                            }  
                        },
                        error:function(XMLResponse){
                                alert("操作失败:" + XMLResponse.responseText);
                            },
                    });	
                    }	
                }


        // 指定页数跳转
        function goPage(c){
            // 当前页数
            var page = parseFloat($('.current').eq(2).text());
            // objectId值
            var objectId = $('#objectId').val();
            // objectType值
            var objectType = $('#productType').val();
            // commentId值
            var commentId = $('#commentId').val();
            if(page == c){
                return;
            }else{
                $('.page-numbers').removeClass('current').eq(c-1).addClass('current');
                $('.commentlist').empty();
                $('.comments-loading').css('display','block');
                $.ajax({
                    type:"GET",
                    url: '/comment/commentListOnPage.json',
                    data:{ 
                        rows:5,
                        page:c,
                        reload:true,
                        object_id:objectId,
                        object_type:objectType,
                        commentId:commentId
                    },
                    dataType:'json',
                    success:function (data) {
                        var a = data.commentList[0].objectId;
                        var b = data.commentList[0].commentId;
                        $('.comments-loading').css('display','none');
                        if(jQuery.cookie('eis_username') == undefined){
                            var commentList =data.commentList.map(function(list){
                                    if(list.relatedCommentId==undefined){
                                            if(list.data.userHeadPic == null){
                                                if(list.data.productGallery == null){
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p  class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }else{
                                                if(list.data.productGallery == null){    
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p  class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }
                                        }else{
                                            if(list.data.userHeadPic == null){
                                                if(list.data.productGallery == null){
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }else{
                                                if(list.data.productGallery == null){
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }
                                        }  
                                })
                                $('.commentlist').append(commentList); 
                        }else{
                            var commentList =data.commentList.map(function(list){
                                    if(list.relatedCommentId==undefined){
                                        if(list.data.userHeadPic == null){
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }else{
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }
                                    }else{
                                        if(list.data.userHeadPic == null){
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }else{
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }
                                    }
                            })
                            $('.commentlist').append(commentList); 
                        }

                         // 点击开启回复功能
                        if($('.reply').text().indexOf('登录以回复')>-1){
       
                        }else{
                            $('.reply').each(function(){
                                $(this).click(function(){
                                    var people = $(this).siblings('div').find('a').eq(0).text();
                                    // var commentId = $(this).siblings('input').val();
                                    var relatedName = $(this).siblings('div').eq(0).find('a').eq(0).text();
                                    if($(this).parent("div").siblings().text() == ''){
                                    $('#respond p').empty();
                                    $('#respond').remove();
                                    $(this).parents(".comment").append('<div id="respond" class="comment-respond"><form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input  type="button" id="submit" class="submit" value="发表评论" onclick="authorSubmit('+"'"+people+"'"+','+commentId+','+"'"+relatedName+"'"+','+"'"+objectType+"'"+')"><input type="hidden" name="objectId" value="1451"  ><input type="hidden" name="objectType"  value="document"></p></form></div>');  
                                    $('#comment').focus();  
                                    }else{
                                        return ;
                                    }
                                })
                            })
                        }

                        // 评论显示表情
                        $('.text').each(function(i,j){
                            var comments = j.innerText;
                            var comment = '';
                            comments = comments.replace(/:arrow:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />');
                            comments = comments.replace(/:love:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" class="lazy" />');
                            comments = comments.replace(/:!:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif" class="lazy" />');
                            comments = comments.replace(/:question:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" class="lazy" />');
                            comments = comments.replace(/:cool:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" class="lazy" />');
                            comments = comments.replace(/:roll:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif" class="lazy" />');
                            comments = comments.replace(/:eek:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" class="lazy" />');
                            comments = comments.replace(/:evil:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif" class="lazy" />');
                            comments = comments.replace(/:razz:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif" class="lazy" />');
                            comments = comments.replace(/:mrgreen:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif" class="lazy" />');
                            comments = comments.replace(/:smile:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif" class="lazy" />');
                            comments = comments.replace(/:chijing:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif" class="lazy" />');
                            comments = comments.replace(/:lol:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif" class="lazy" />');
                            comments = comments.replace(/:mad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif" class="lazy" />');
                            comments = comments.replace(/:twisted:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif" class="lazy" />');
                            comments = comments.replace(/:wink:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif" class="lazy" />');
                            comments = comments.replace(/:idea:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif" class="lazy" />');
                            comments = comments.replace(/:cry:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif" class="lazy" />');
                            comments = comments.replace(/:shock:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif" class="lazy" />');
                            comments = comments.replace(/:neutral:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif" class="lazy" />');
                            comments = comments.replace(/:sad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif" class="lazy" />');
                            comment = comments.replace(/:confused:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif" class="lazy" />');
                            $(this).empty().append(comment);
                        })

                        // 点击录音播放
                        var toggleVoice = true;
                        $('.app-voice-state-bg').each(function(){
                            $(this).click(function(){
                                $('.audio').each(function(a,b){
                                    b.pause();
                                    $(this).siblings('div').eq(0).children().removeClass('app-voice-you');
                                    $(this).siblings('div').eq(0).children().addClass('app-voice-he');
                                })
                                if(toggleVoice == true ){
                                    $(this).siblings('audio')[0].play();
                                    toggleVoice = false;
                                    $(this).children().removeClass('app-voice-he');
                                    $(this).children().addClass('app-voice-you');
                                    // 监听音频播放完
                                    $(this).siblings('audio').bind('ended',function (){
                                        $(this).siblings('div').eq(0).children().removeClass('app-voice-you');
                                        $(this).siblings('div').eq(0).children().addClass('app-voice-he');
                                    });
                                }else{
                                    $(this).siblings('audio')[0].pause();
                                    toggleVoice = true;
                                    $(this).children().removeClass('app-voice-you');
                                    $(this).children().addClass('app-voice-he');
                                }
                            })
                        }) 


                        // 录音时间显示
                        $('.audio').each(function(){
                            $(this)[0].oncanplay = function(){
                                var time = parseInt($(this)[0].duration);
                                $(this).siblings('div').eq(1).text(time+"'");
                            } 
                        }) 
                        
                    },
                    error:function (XMLResponse){
                        alert("操作失败:" + XMLResponse.responseText);
                    }
                })
            }
        }

        // 下一页跳转
        function nextPage(){
            // 总页数
            var a = $('.page-numbers').length-2;
            var pages =$('.page-numbers').eq(a).text();
            // 当前页数
            var page = parseFloat($('.current').eq(2).text());
            // objectId值
            var objectId = $('#objectId').val();
            // objectType值
            var objectType = $('#productType').val();
            // commentId值
            var commentId = $('#commentId').val();
            if(page < pages){
                $('.page-numbers').removeClass('current').eq(page).addClass('current');
                page++;
                $('.commentlist').empty();
                $('.comments-loading').css('display','block');
                $.ajax({
                    type:"GET",
                    url: '/comment/commentListOnPage.json',
                    data:{ 
                        rows:5,
                        page:page,
                        reload:true,
                        object_id:objectId,
                        object_type:objectType,
                        commentId:commentId
                    },
                    dataType:'json',
                    success:function (data) {
                        var a = data.commentList[0].objectId;
                        var b = data.commentList[0].commentId;
                        $('.comments-loading').css('display','none');
                        if(jQuery.cookie('eis_username') == undefined){
                            var commentList =data.commentList.map(function(list){
                                        if(list.relatedCommentId==undefined){
                                            if(list.data.userHeadPic == null){
                                                if(list.data.productGallery == null){
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p  class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }else{
                                                if(list.data.productGallery == null){    
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p  class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }
                                        }else{
                                            if(list.data.userHeadPic == null){
                                                if(list.data.productGallery == null){
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }else{
                                                if(list.data.productGallery == null){
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }else{
                                                    return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a rel="nofollow" class="comment-reply-login" href="/user/login.shtml">登录以回复</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                                }
                                            }
                                        }  
                                })
                                $('.commentlist').append(commentList); 
                        }else{
                            var commentList =data.commentList.map(function(list){
                                    if(list.relatedCommentId==undefined){
                                        if(list.data.userHeadPic == null){
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }else{
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }
                                    }else{
                                        if(list.data.userHeadPic == null){
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="../../../theme/basic/images/touxiang.png" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a>回复<a class="url">'+list.data.replyTo+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }else{
                                            if(list.data.productGallery == null){
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><p class="text">'+list.content+'</p><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }else{
                                                return  ('<li class="comment byuser comment-author-19853 even thread-even depth-1" id="comment-39615" style="margin:10px 15px;border-bottom: 1px solid #E1E1E1;padding-bottom: 15px;"><div id="div-comment-39615" class="comment-body"><div class="comment-author vcard "><img src="/file/client/'+list.data.userHeadPic+'" alt="'+list.data.userRealName+'" height="40" width="40" class="avatar"><div class="floor"></div><span class="c-user"><a href="" rel="external nofollow" class="url">'+list.data.userRealName+'</a></span>: </div><div class="app-voice-main"><div style=" width: 180px;"><audio class="audio"><source src="/file/'+list.data.productGallery+'" type=""></audio><div class="app-voice-state-bg"><div class="app-voice-he"></div></div><div class="voiceTime"></div></div></div><div class="clear"></div><span class="datetime">'+list.createTime+'</span><span class="reply"><a class="comment-reply-link">[回复]</a></span><input type="hidden" value="${item.commentId}"><input type="hidden" value="'+a+'" id="objectId"><input type="hidden" value="'+b+'" id="commentId"></li>')
                                            }
                                        }
                                    }
                            })
                            $('.commentlist').append(commentList); 
                        }

                        // 点击开启回复功能
                        if($('.reply').text().indexOf('登录以回复')>-1){
                            
                        }else{
                            $('.reply').each(function(){
                                $(this).click(function(){
                                    var people = $(this).siblings('div').find('a').eq(0).text();
                                    // var commentId = $(this).siblings('input').val();
                                    var name = jQuery.cookie('eis_username');
                                    var relatedName = $(this).siblings('div').eq(0).find('a').eq(0).text();
                                    if($(this).parent("div").siblings().text() == ''){
                                    $('#respond p').empty();
                                    $('#respond').remove();
                                    $(this).parents(".comment").append('<div id="respond" class="comment-respond"><form  method="post" id="commentform" class="comment-form"><p class="logged-in-as">以<a href="/content/friend/upload/index.shtml">'+name+'</a>的身份登录！</p><div id="smilies"><a      onclick="arrow()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif"  alt="arrow" style="display: inline;"></a><a onclick="biggrin()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" alt="grin&quot;&quot;" style="display: inline;"></a><a onclick="exclaim()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif"  alt="!" style="display: inline;"></a><a      onclick="question()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" alt="?" style="display: inline;"></a><a onclick="cool()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" alt="cool" style="display: inline;"></a><a onclick="rolleyes()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif"  alt="roll" style="display: inline;"></a><a onclick="eek()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" alt="eek" style="display: inline;"></a><a onclick="evil()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif"  alt="evil" style="display: inline;"></a><a onclick="razz()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif"  alt="razz" style="display: inline;"></a><a onclick="mrgreen()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif"  alt="mrgreen" style="display: inline;"></a><a onclick="smile()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif"  alt="smile" style="display: inline;"></a><a onclick="redface()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif"  alt="oops" style="display: inline;"></a><a onclick="lol()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif"  alt="lol" style="display: inline;"></a><a onclick="mad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif"  alt="mad" style="display: inline;"></a><a onclick="twisted()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif"  alt="twisted" style="display: inline;"></a><a onclick="wink()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif"  alt="wink" style="display: inline;"></a><a onclick="idea()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif"  alt="idea" style="display: inline;"></a><a onclick="cry()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif"  alt="cry" style="display: inline;"></a><a onclick="surprised()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif"  alt="shock" style="display: inline;"></a><a onclick="neutral()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif"  alt="neutral" style="display: inline;"></a><a onclick="sad()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif"  alt="sad" style="display: inline;"></a><a onclick="confused()"><img class="lazy" src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif"  alt="???" style="display: inline;"></a></div><textarea  id="review_text" name="content" cols="45" rows="8" aria-required="true" placeholder="字数不能超过150字..."></textarea><div id="loading" style="display: none;"><img src="http://www.daweijita.com/wp-admin/images/wpspin_light.gif" style="vertical-align:middle;" alt="">正在提交，请稍后 ...</div><div id="error" style="display: none;">#</div></p><p class="form-allowed-tags" id="form-allowed-tags">您可以使用这些<abbr title="HyperText Markup Language">HTML</abbr>标签和属性： <code>&lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;cite&gt; &lt;code&gt; &lt;del datetime=""&gt; &lt;em&gt; &lt;i&gt; &lt;q cite=""&gt; &lt;s&gt; &lt;strike&gt; &lt;strong&gt; </code></p><p class="form-submit"><input  type="button" id="submit" class="submit" value="发表评论" onclick="authorSubmit('+"'"+people+"'"+','+commentId+','+"'"+relatedName+"'"+','+"'"+objectType+"'"+')"><input type="hidden" name="objectId" value="1451"  ><input type="hidden" name="objectType"  value="document"></p></form></div>');  

                                    $('#comment').focus();


                                    }else{
                                        return ;
                                    }
                                })
                            })
                        }

                        // 评论显示表情
                        $('.text').each(function(i,j){
                            var comments = j.innerText;
                            var comment = '';
                            comments = comments.replace(/:arrow:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_arrow.gif" class="lazy" />');
                            comments = comments.replace(/:love:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_biggrin.gif" class="lazy" />');
                            comments = comments.replace(/:!:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_exclaim.gif" class="lazy" />');
                            comments = comments.replace(/:question:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_question.gif" class="lazy" />');
                            comments = comments.replace(/:cool:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cool.gif" class="lazy" />');
                            comments = comments.replace(/:roll:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_rolleyes.gif" class="lazy" />');
                            comments = comments.replace(/:eek:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_eek.gif" class="lazy" />');
                            comments = comments.replace(/:evil:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_evil.gif" class="lazy" />');
                            comments = comments.replace(/:razz:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_razz.gif" class="lazy" />');
                            comments = comments.replace(/:mrgreen:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mrgreen.gif" class="lazy" />');
                            comments = comments.replace(/:smile:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_smile.gif" class="lazy" />');
                            comments = comments.replace(/:chijing:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_redface.gif" class="lazy" />');
                            comments = comments.replace(/:lol:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_lol.gif" class="lazy" />');
                            comments = comments.replace(/:mad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_mad.gif" class="lazy" />');
                            comments = comments.replace(/:twisted:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_twisted.gif" class="lazy" />');
                            comments = comments.replace(/:wink:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_wink.gif" class="lazy" />');
                            comments = comments.replace(/:idea:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_idea.gif" class="lazy" />');
                            comments = comments.replace(/:cry:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_cry.gif" class="lazy" />');
                            comments = comments.replace(/:shock:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_surprised.gif" class="lazy" />');
                            comments = comments.replace(/:neutral:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_neutral.gif" class="lazy" />');
                            comments = comments.replace(/:sad:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_sad.gif" class="lazy" />');
                            comment = comments.replace(/:confused:/g,'<img src="http://www.daweijita.com/wp-content/themes/wpdx/images/smilies/icon_confused.gif" class="lazy" />');
                            $(this).empty().append(comment);
                        }) 


                        // 点击录音播放
                        var toggleVoice = true;
                        $('.app-voice-state-bg').each(function(){
                            $(this).click(function(){
                                $('.audio').each(function(a,b){
                                    b.pause();
                                    $(this).siblings('div').eq(0).children().removeClass('app-voice-you');
                                    $(this).siblings('div').eq(0).children().addClass('app-voice-he');
                                })
                                if(toggleVoice == true ){
                                    $(this).siblings('audio')[0].play();
                                    toggleVoice = false;
                                    $(this).children().removeClass('app-voice-he');
                                    $(this).children().addClass('app-voice-you');
                                    // 监听音频播放完
                                    $(this).siblings('audio').bind('ended',function (){
                                        $(this).siblings('div').eq(0).children().removeClass('app-voice-you');
                                        $(this).siblings('div').eq(0).children().addClass('app-voice-he');
                                    });
                                }else{
                                    $(this).siblings('audio')[0].pause();
                                    toggleVoice = true;
                                    $(this).children().removeClass('app-voice-you');
                                    $(this).children().addClass('app-voice-he');
                                }
                            })
                        }) 


                        // 录音时间显示
                        $('.audio').each(function(){
                            $(this)[0].oncanplay = function(){
                                var time = parseInt($(this)[0].duration);
                                $(this).siblings('div').eq(1).text(time+"'");
                            } 
                        })  
                    },
                    error:function (XMLResponse){
                        alert("操作失败:" + XMLResponse.responseText);
                    }
                }) 
            }else{
                alert('已经是最后一页了');
            }
        }

    //    录音函数
        var recorder;

        
        function startRecording(a) {
            if(a == name){
                alert('对不起,不能语音回复自己！');
            }else if(name == '七星乐器' || name == '阿坤' || name == '金伟'){
                HZRecorder.get(function (rec) {
                    recorder = rec;
                    recorder.start();
                });
            }else{
                alert("对不起,此功能暂时只对本站管理员开放！");
            }
        }

        function stopRecording() {
            if(a == name){
                alert('对不起,不能语音回复自己！');
            }else if(name == '七星乐器' || name == '阿坤' || name == '金伟'){
                recorder.stop();
            }else{
                alert("对不起,此功能暂时只对本站管理员开放！");
            }
        }

        function playRecording() {
            // var audio = document.querySelector('audio');
            if(a == name){
                alert('对不起,不能语音回复自己！');
            }else if(name == '七星乐器' || name == '阿坤' || name == '金伟'){
                recorder.play($(".audioOnly")[0]);
            }else{     
                alert("对不起,此功能暂时只对本站管理员开放！");  
            }
        }

        function uploadAudio(b,c,d) {
                recorder.upload(b,c,d,"/comment/submit.json", function (state, e) {
                    switch (state) {
                        case 'uploading':
                            // 获取上传文件进度
                            // var percentComplete = Math.round(e.loaded * 100 / e.total) + '%';
                            // console.log(percentComplete);
                            // $('.progress').text(percentComplete);
                            break;
                        case 'ok':
                            // alert(e.target.responseText);
                            alert("上传成功");
                            break;
                        case 'error':
                            alert("上传失败");
                            break;
                        case 'cancel':
                            alert("上传被取消");
                            break;
                    }
                });
        }
        

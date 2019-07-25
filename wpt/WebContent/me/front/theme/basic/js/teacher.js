
                 // 获取url地址的类别
var test = window.location.href;
var url = test.substring(34);
var urlName = url.indexOf('/');
var value = url.substring(0,urlName);
// 跳转指定页面
function goPage(c){
    // 当前页数
    var page = $('.current').eq(1).text();
    // 总页数
    var a = $('.page-numbers').length-2;
    var pages =$('.page-numbers').eq(a).text();
    if(page == c){
        return;
    }else{
        if(c == pages){
            $('.page-numbers').removeClass('current').eq(2).addClass('current');
        }else{
            $('.page-numbers').removeClass('current').eq(c-1).addClass('current');
        }
        $('.commentlists').empty();
        $('.comments-loading').css('display','block');
        $.ajax({
            type:"GET",
            url: '/content/teacher/'+value+'/index_20_'+c+'.json',
            data:{},
            dataType:'json',
            success:function (data) {
                $('.comments-loading').css('display','none');
                var commentList =data.newsList.map(function(list){
                    if(list.documentDataMap == undefined){
                        return ('<li class="archive-simple" style="border-bottom: 1px solid #eee;"><h2><a href="'+list.viewUrl+'" title="链接到  '+list.title+'" rel="bookmark" target="_blank"><img src="../../../theme/basic/images/greenyuan.png" alt="" class="right">'+list.title+'</a></h2><p class="post-meta"><span><img src="../../../theme/basic/images/lixian.png" alt="" class="lixian">'+list.publishTime+'</span><span><img src="../../../theme/basic/images/video.png" alt="" class="video"></i>0</span></p></li>')            
                    }else if(list.documentDataMap.readCount == undefined){
                        return ('<li class="archive-simple" style="border-bottom: 1px solid #eee;"><h2><a href="'+list.viewUrl+'" title="链接到  '+list.title+'" rel="bookmark" target="_blank"><img src="../../../theme/basic/images/greenyuan.png" alt="" class="right">'+list.title+'</a></h2><p class="post-meta"><span><img src="../../../theme/basic/images/lixian.png" alt="" class="lixian">'+list.publishTime+'</span><span><img src="../../../theme/basic/images/video.png" alt="" class="video"></i>0</span></p></li>')            
                    }else{
                        return ('<li class="archive-simple" style="border-bottom: 1px solid #eee;"><h2><a href="'+list.viewUrl+'" title="链接到  '+list.title+'" rel="bookmark" target="_blank"><img src="../../../theme/basic/images/greenyuan.png" alt="" class="right">'+list.title+'</a></h2><p class="post-meta"><span><img src="../../../theme/basic/images/lixian.png" alt="" class="lixian">'+list.publishTime+'</span><span><img src="../../../theme/basic/images/video.png" alt="" class="video"></i>'+list.documentDataMap.readCount.dataValue+'</span></p></li>')                                  
                    }
                })
                $('.commentlists').append(commentList);

                //  文章数量
                // $('.archive-simple').each(function(x,y){
                //                 $('.post-count').find('span').text(x+1);
                // })
            }
        })
    }
}


// 下一页跳转    
// 总页数
var a = $('.page-numbers').length-2;
var pages =$('.page-numbers').eq(a).text();
// 当前页数
var page = parseFloat($('.current').eq(1).text());
var i = page;
function nextPage(){
    if(i < pages){
                if(i>3){
                     i++;
                }else{
                    $('.page-numbers').removeClass('current').eq(i).addClass('current');
                    i++;
                }
                $('.commentlists').empty();
                $('.comments-loading').css('display','block');
                $.ajax({
                    type:"GET",
                    url: '/content/teacher/'+value+'/index_20_'+i+'.json',
                    data:{},
                    dataType:'json',
                    success:function (data) {
                        $('.comments-loading').css('display','none');
                        var commentList =data.newsList.map(function(list){
                            if(list.documentDataMap == undefined){
                                return ('<li class="archive-simple" style="border-bottom: 1px solid #eee;"><h2><a href="'+list.viewUrl+'" title="链接到  '+list.title+'" rel="bookmark" target="_blank"><img src="../../../theme/basic/images/greenyuan.png" alt="" class="right">'+list.title+'</a></h2><p class="post-meta"><span><img src="../../../theme/basic/images/lixian.png" alt="" class="lixian">'+list.publishTime+'</span><span><img src="../../../theme/basic/images/video.png" alt="" class="video"></i>0</span></p></li>')            
                            }else if(list.documentDataMap.readCount == undefined){
                                return ('<li class="archive-simple" style="border-bottom: 1px solid #eee;"><h2><a href="'+list.viewUrl+'" title="链接到  '+list.title+'" rel="bookmark" target="_blank"><img src="../../../theme/basic/images/greenyuan.png" alt="" class="right">'+list.title+'</a></h2><p class="post-meta"><span><img src="../../../theme/basic/images/lixian.png" alt="" class="lixian">'+list.publishTime+'</span><span><img src="../../../theme/basic/images/video.png" alt="" class="video"></i>0</span></p></li>')            
                            }else{
                                return ('<li class="archive-simple" style="border-bottom: 1px solid #eee;"><h2><a href="'+list.viewUrl+'" title="链接到  '+list.title+'" rel="bookmark" target="_blank"><img src="../../../theme/basic/images/greenyuan.png" alt="" class="right">'+list.title+'</a></h2><p class="post-meta"><span><img src="../../../theme/basic/images/lixian.png" alt="" class="lixian">'+list.publishTime+'</span><span><img src="../../../theme/basic/images/video.png" alt="" class="video"></i>'+list.documentDataMap.readCount.dataValue+'</span></p></li>')                                  
                            }
                        })
                        $('.commentlists').append(commentList);
                        
                        //  文章数量
                        // $('.archive-simple').each(function(x,y){
                        //                 $('.post-count').find('span').text(x+1);
                        // })
                    }
                })
    }else{
            if(pages == 1){
                $('.page-numbers').removeClass('current').eq(0).addClass('current');
            }else if(pages == 2){
                $('.page-numbers').removeClass('current').eq(1).addClass('current');
            }else if( pages == 3){
                $('.page-numbers').removeClass('current').eq(2).addClass('current');
            }else{
                $('.page-numbers').removeClass('current').eq(4).addClass('current');
            }
            alert('已经是最后一页了');
    } 
}   
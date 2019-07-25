// 加载时间按钮
window.onload = function (){
        $(".audio_btn").each(function(){
            var music = $(this).parents('li').find('audio')[0];
            var times = parseFloat(music.duration);
            var total = times.toFixed(0);
            var mint = (total/60).toFixed(0).toString();
            if(mint.length == 1){
            mint = '0' + mint;
            }else{
            mint;
            }
            var miao = ((total/60) - mint)*60;
            var miaos = parseFloat(miao).toFixed(0).toString();        
            if(miaos.length == 1){
            miaos = '0' + miaos;
            }else{
            miaos;
            }
            $(this).parents('div[class="shoucana"]').siblings('div[class="times"]').text(mint+':'+miaos);
        })
}

// 播放按钮
$(".audio_btn").each(function(){
    $(this).click(function(){ 
        var music = $(this).parents('li').find('audio')[0];
        if(music.paused){ 
            music.play(); 
            $(this).attr("src","../../../theme/basic/images/play.png"); 
            $(this).css({'width':'14px','height':'14px'});
        }else{ 
            music.pause(); 
            $(this).attr("src","../../../theme/basic/images/pause.png"); 
            $(this).css({'width':'15px','height':'15px'});
        } 
    });
})


// 下载按钮 
$('.onload').each(function(){
    $(this).click(function(){
        if($('input[name="vipLevel"]').val() != 2){
            alert('您还不是vip会员！');
        }else{
            var mp3 = $(this).siblings('input').val();
            var a = document.createElement('a'); 
            a.href = "#";  
            a.download = "/file/" + mp3;    
            a.click(); 
        } 
    })
})

// 收藏按钮
function  collectMusic(){
        $.ajax({
            type:"POST",
            url:"/userRelation/add.json",
            data:{  
                uuid:$('input[name="uuid"]').val(),
                objectType:$('input[name="objectType"]').val(),
                objectId:$('input[name="objectId"]').val(),
                relationType:'favorite',
            },           
            async:false,
            success:function(data){
                switch (data.message.operateCode) {
                    case 500031:
                        alert(data.message.message);
                        location.href="/user/login.shtml";
                        break;
                    case 102008:
                        alert(data.message.message);
                        break;
                    case 500018:
                        alert(data.message.message);
                        break;
                }			
            }
        })
}

// 批量下载操作
function moreLoad(){
    if($('#onload').text().indexOf('取消') > -1){
        $('#onload').text('批量下载');
        $('.content-input').css('display','none');
    }else{
        $('#onload').text('取消批量下载');
        $('.content-input').css('display','block');
    }
}

// 全选
function allCheck(){
    if($('#allCheck')[0].checked == true){
        $('.content-input').each(function(index){
            $('.content-input')[index].checked = true;      
        })
        $('.content-input').css('display','block');
    }else{
        $('.content-input').each(function(index){
            $('.content-input')[index].checked = false;      
        })
        $('.content-input').css('display','none');
    }
}

// 全部下载
function allLoad(){
        if($('.content-input').is(':checked') == false){
            alert('您还未勾选音乐！')
        }else if($('input[name="vipLevel"]').val() != 2){
            alert('您还不是vip会员！');
        }else{
            $('.content-input').each(function(index){
                if($(this).is(':checked') == true){
                    var mp3 = $(this).parent().siblings('div[class="shoucana"]').children().eq(2).find('input').val();
                    var a = document.createElement('a'); 
                    a.href = "#";  
                    a.download = "/file/" + mp3;    
                    a.click();
                }
            })
        }
}


// 跳转指定页面
function goPage(c){
    // 当前页数
    var page = $('.current').eq(1).text();
    if(page == c){
        return;
    }else{
        $('.page-numbers').removeClass('current').eq(c-1).addClass('current');
        $('.commentlist').empty();
        $('.comments-loading').css('display','block');
        $.ajax({
            type:"GET",
            url: '/content/ring/index_1_'+c+'.json',
            data:{},
            dataType:'json',
            success:function (data) {
                    $('.comments-loading').css('display','none');

                    var commentList =data.newsList.map(function(list){
                        return ('<li class="archive-head-three-odd"><audio class="music"><source src=/file/'+list.documentDataMap.subscribeContent.dataValue+'></source></audio><div class="qumua"><input type="checkbox" class="content-input"><span class="content-author">'+list.title+'</span></div><div class="authora">'+list.author+'</div><div class="times"></div><div class="shoucana"><span><img src="../../../theme/basic/images/love.png" alt="" style="margin-left:15px;" onclick="collectMusic()"></span><span><img style="width: 14px;height: 14px;" src="../../../theme/basic/images/pause.png" alt="" class="audio_btn"></span><span><input type="hidden" value="'+list.documentDataMap.subscribeContent.dataValue+'"><img src="../../../theme/basic/images/onload.png" alt="" style="margin-right: 15px;" class="onload"></span></div></li>');
                    })
                    $('.commentlist').append(commentList);

                    //  加载时长
                    var audio = document.getElementsByClassName('music');
                    audio.addEventListener("canplay", function(){
                            console.log(parseInt(audio.duration));
                    });
                    $(".audio_btn").each(function(){
                        var music = $(this).parents('li').find('audio')[0];
                        console.log(music.duration);
                        console.log(parseInt(music.duration));
                        var times = parseFloat(music.duration);
                        var total = times.toFixed(0);
                        console.log(total);
                        var mint = (total/60).toFixed(0).toString();
                        if(mint.length == 1){
                        mint = '0' + mint;
                        }else{
                        mint;
                        }
                        var miao = ((total/60) - mint)*60;
                        var miaos = parseFloat(miao).toFixed(0).toString();        
                        if(miaos.length == 1){
                        miaos = '0' + miaos;
                        }else{
                        miaos;
                        }
                        $(this).parents('div[class="shoucana"]').siblings('div[class="times"]').text(mint+':'+miaos);
                    })
                    
                    // 播放按钮
                    $(".audio_btn").each(function(){
                        $(this).click(function(){ 
                            var music = $(this).parents('li').find('audio')[0];
                            if(music.paused){ 
                                music.play(); 
                                $(this).attr("src","../../../theme/basic/images/play.png"); 
                                $(this).css({'width':'14px','height':'14px'});
                            }else{ 
                                music.pause(); 
                                $(this).attr("src","../../../theme/basic/images/pause.png"); 
                                $(this).css({'width':'15px','height':'15px'});
                            } 
                        });
                    })


                    // 下载按钮 
                    $('.onload').each(function(){
                        $(this).click(function(){
                            if($('input[name="vipLevel"]').val() != 2){
                                alert('您还不是vip会员！');
                            }else{
                                var mp3 = $(this).siblings('input').val();
                                var a = document.createElement('a'); 
                                a.href = "#";  
                                a.download = "/file/" + mp3;    
                                a.click(); 
                            } 
                        })
                    })
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
    var page = parseFloat($('.current').eq(1).text());
    if(page < pages){
                $('.page-numbers').removeClass('current').eq(page).addClass('current');
                page++;
                $('.commentlist').empty();
                $('.comments-loading').css('display','block');
                $.ajax({
                    type:"GET",
                    url: '/content/ring/index_1_'+page+'.json',
                    data:{},
                    dataType:'json',
                    success:function (data) {
                        $('.comments-loading').css('display','none');
                        console.log(data);
                        $('.comments-loading').css('display','none');
                        var commentList =data.newsList.map(function(list){
                            return ('<li class="archive-head-three-odd"><audio class="music"><source src=/file/'+list.documentDataMap.subscribeContent.dataValue+'></source></audio><div class="qumua"><input type="checkbox" class="content-input"><span class="content-author">'+list.title+'</span></div><div class="authora">'+list.author+'</div><div class="times"></div><div class="shoucana"><span><img src="../../../theme/basic/images/love.png" alt="" style="margin-left:15px;" onclick="collectMusic()"></span><span><img style="width: 14px;height: 14px;" src="../../../theme/basic/images/pause.png" alt="" class="audio_btn"></span><span><input type="hidden" value="'+list.documentDataMap.subscribeContent.dataValue+'"><img src="../../../theme/basic/images/onload.png" alt="" style="margin-right: 15px;" class="onload"></span></div></li>');
                        })
                        $('.commentlist').append(commentList);

                        var audio = document.getElementsByClassName('music');
                        audio.addEventListener("canplay", function(){
                            console.log(parseInt(audio.duration));
                        });
                        //  加载时长
                            $(".audio_btn").each(function(){
                                var music = $(this).parents('li').find('audio')[0];
                                console.log(music.duration);
                                console.log(parseInt(music.duration));
                                var times = parseFloat(music.duration);
                                var total = times.toFixed(0);
                                console.log(total);
                                var mint = (total/60).toFixed(0).toString();
                                if(mint.length == 1){
                                mint = '0' + mint;
                                }else{
                                mint;
                                }
                                var miao = ((total/60) - mint)*60;
                                var miaos = parseFloat(miao).toFixed(0).toString();        
                                if(miaos.length == 1){
                                miaos = '0' + miaos;
                                }else{
                                miaos;
                                }
                                $(this).parents('div[class="shoucana"]').siblings('div[class="times"]').text(mint+':'+miaos);
                            })

                        // 播放按钮
                        $(".audio_btn").each(function(){
                            $(this).click(function(){ 
                                var music = $(this).parents('li').find('audio')[0];
                                if(music.paused){ 
                                    music.play(); 
                                    $(this).attr("src","../../../theme/basic/images/play.png"); 
                                    $(this).css({'width':'14px','height':'14px'});
                                }else{ 
                                    music.pause(); 
                                    $(this).attr("src","../../../theme/basic/images/pause.png"); 
                                    $(this).css({'width':'15px','height':'15px'});
                                } 
                            });
                        })


                        // 下载按钮 
                        $('.onload').each(function(){
                            $(this).click(function(){
                                if($('input[name="vipLevel"]').val() != 2){
                                    alert('您还不是vip会员！');
                                }else{
                                    var mp3 = $(this).siblings('input').val();
                                    var a = document.createElement('a'); 
                                    a.href = "#";  
                                    a.download = "/file/" + mp3;    
                                    a.click(); 
                                } 
                            })
                        })
                    }
                })
    }else{
        alert('已经是最后一页了');
    } 
}   
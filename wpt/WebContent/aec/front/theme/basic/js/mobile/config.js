$(function(){
	$('#share').on('click',function(){
		$.ajax({
        url: 'XXXX.do',
        data:{
            shareUrl:encodeURI(location.href)
        },
        success:function(data){
            wx.config({
                debug: false,
                appId: Const.APP_ID,
                timestamp: data.timestamp,
                nonceStr: data.nonceStr,
                signature: data.signature,
                jsApiList: [
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage',
                    'hideMenuItems',
                    'showMenuItems'
                ]
            });
            wx.ready(function(){  
            	// 分享到朋友圈
		        wx.onMenuShareTimeline({  
		            title: $('#productTitle').text(),  
		            link: window.location.href,  
		            imgUrl: $('.swiper-wrapper').find('.swiper-slide').eq(0).find('img').attr('src'),  
		            success: function () {   
		                // 用户确认分享后执行的回调函数  
		                 alert('分享到朋友圈成功');  
		            },  
		            cancel: function () {   
		                // 用户取消分享后执行的回调函数  
		                 alert('你没有分享到朋友圈');  
		            }  
		        });  
		        // 分享给朋友
		        wx.onMenuShareAppMessage({  
		              title: $('#productTitle').text(),  
		              desc: $('#productprice').text(),  
		              link: window.location.href,  
		              imgUrl: $('.swiper-wrapper').find('.swiper-slide').eq(0).find('img').attr('src'),  
		              trigger: function (res) {  
		                // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回  
		              },  
		              success: function (res) {  
		                  alert('分享给朋友成功');  
		              },  
		              cancel: function (res) {  
		                alert('你没有分享给朋友');  
		              },  
		              fail: function (res) {  
		                alert(JSON.stringify(res));  
		              }  
		            });  
		    });  
        },
        error:function(data){
            console.error('req '+ Const.CHECK_WECHAT + " error "+data);
        }
    });
	})
	
})
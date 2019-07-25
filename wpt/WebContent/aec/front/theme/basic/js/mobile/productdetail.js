
$(function() {
    var $buynow= $('#buyNow');
    $buynow.click(function(){
		var mLoading=new MLoading();
        $.ajax({
            type:"POST",
            url:"/buy/add.json",
            data:{
                productCode:$("#productCode").val(),
                count:1
            },
            dataType:'json',
            success:function (data) {
				mLoading.beRemoved();
                switch (data.message.operateCode) {                 
                    case operateResultSuccess:
                        malert(data.message.message+"",1500);
						location.reload();
						break
                    case errorUserNotFoundInRequest:
					case errorUserNotFoundInSession:
                        malert(data.message.message+"",1500);
                        location.href='/content/user/login.shtml';
                        break;
					default:
						malert(data.message.message+"",1500);
                }				
            },
            error:function (data) {
				mLoading.beRemoved();
                malert("系统繁忙,请稍后再试",1500);
                return false;
            }
        });
    })
});
    // 收藏按钮 
    function collectproduct(){
        $.ajax({
            type:"POST",
            url:"/userRelation/add.json",
            data:{  
                uuid:$('input[name="uuid"]').val(),
                objectType:$('#productType').val(),
                objectId:$('#udid').val(),
                relationType:'favorite',
            },           
            async:false,
            success:function(data){
                switch (data.message.operateCode) {
                    case 500031:
                        alert(data.message.message);
                        location.href="/content/user/login.shtml";
                        break;
                    case 102008:
                        alert(data.message.message);
                        // window.location.reload();
                        break;
                    case 500018:
                        alert(data.message.message);
                        window.location.reload();
                        break;
                }			
            }
        })
    }

// 评论按钮
function comments()
    {   
        if(Cookie.getCookie("eis_username")==null){
            location.href = "/content/user/login.shtml";
        }else if($('textarea[name="content"]').val().length == 0){
                alert('内容不能为空！');
                return false;
        }else if($('textarea[name="content"]').val().length > 5000){
                alert('内容不能多于5000字！');
                return false;
        }else{
            $.ajax({
                    type:"POST",
                    url: '/comment/submit.json',
                    data:{
                        objectType:$('#productType').val(),
                        objectId:$("#udid").val(),
                        content:$('textarea[name="content"]').val(),
                        title:$("#title").val()
                    },
                    dataType:'json',
                    success:function (data) {
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

function changevalue(t,transactionId){
	if(t==null||t==""){
		malert("请输入数量");
		return false;
	}
	var mLoading=new MLoading();
    $.ajax({
        type:"POST",
        url:'/cart/update.json',
        data:{
            transactionId:transactionId,
            count:t
        },
        dataType:'json',
        success:function (data) {
			mLoading.beRemoved();
			$(t).attr("value",t)
            window.location.reload();
        },
        error:function (data) {
			mLoading.beRemoved();
            alert("系统繁忙,请稍后再试");
            return false;
        }
    });	
}
function increase(t,transactionId){
	var mLoading=new MLoading();
    var addcount = parseInt($(t).prev().val())+1;
    $.ajax({
        type:"POST",
        url:'/cart/update.json',
        data:{
            transactionId:transactionId,
            count:addcount
        },
        dataType:'json',
        success:function (data) {
			mLoading.beRemoved();
			if(data.message){
				malert(data.message.message+"",1500)
			}
            $(t).prev().val(addcount);
			location.reload();
        },
        error:function (data) {
			mLoading.beRemoved();
            malert("系统繁忙,请稍后再试",1500);
            return false;
        }
    });
}
function decrease(t,transactionId){
	var mLoading=new MLoading();
	var jiancount = parseInt($(t).next().val())-1;
	var url='/cart/update.json';
	if(jiancount<0){
		return false;
	}
    $.ajax({
        type:"POST",
        url:url,
        data:{
            transactionId:transactionId,
            count:jiancount
        },
        dataType:'json',
        success:function (data) {
			mLoading.beRemoved();
			if(data.message){
				malert(data.message.message+"",1500);
			}
            $(t).next().val(jiancount);	
			location.reload();			
        },
        error:function (data) {
			mLoading.beRemoved();
            malert("系统繁忙,请稍后再试",1500);
            return false;
        }
    });
}




$(function(){
	    $.ajax({
			   type:"GET",
                url:'/user.json',
                data:{
                  
                },
                dataType:'json',

                success:function (data) {
					 //$(".username").val(data.frontUser.username);
					 //$(".myName").val(data.frontUser.nickName);
					if(data.frontUser.gender==0){
						$("input[name=sex]")[0].checked=true;
					}
					else{
						$("input[name=sex]")[1].checked=true;
					}
					//$(".headerImg").attr("src","/static/userUploadDir/"+data.frontUser.userConfigMap.userHeadPic.dataValue);


                },
                error:function (data) {

                    alert("系统繁忙,请稍后再试");
                    return false;
                }
            });
		jQuery.validator.addMethod("mobile", function (value, element) {
             var mobile = /^1[3|4|5|7|8]\d{9}$/;
                return this.optional(element) || (mobile.test(value));
         }, "手机格式不对!");  
        $(".box_changePw").validate({

        rules:{
            oldPw:{
                required:true,
                minlength:6,
                maxlength:16
            },
			newPw:{
				 required:true,
                minlength:6,
                maxlength:16
			},
			newPw1:{
				required:true,
                minlength:6,
                maxlength:16,
				equalTo:".box_changePw #newPw"
			}
			
        },
        messages:{
            oldPw:{
                required:"请输入密码",
                minlength:"不小于6个字符",
                maxlength:"不大于16个字符"
            },
            newPw:{
                required:"请输入密码",
                minlength:"不小于6个字符",
                maxlength:"不大于16个字符"
            },
            newPw1:{
                required:"请输入密码",
                minlength:"不小于6个字符",
                maxlength:"不大于16个字符",
				equalTo: "两次输入密码不一致"
            }
        },
        errorPlacement: function(error, element) {
            error.insertAfter(element);
        },
        success: function(label) {
            label.addClass("valid").text("")
        },
        submitHandler: function(form) {


             $.ajax({  
                type:"POST",
                url:'/user/changePassword.json',
                data:{
					oldPassword:$(".box_changePw input[name=oldPw]").val(),
                    password1:$(".box_changePw input[name=newPw]").val(),
					password2:$(".box_changePw input[name=newPw1]").val()
                },
                dataType:'json',

                success:function (data) {
                    if(data.message.operateCode != '102008'){
                       alert(data.message.message);
                        return false;
                    }
                    if (data.message.operateCode == '102008') { //成功
					    alert(data.message.message);
					    location.href="/content/user/login.shtml";
                    }

                },
                error:function (data) {

                    alert("系统繁忙,请稍后再试");
                    return false;
                }
            }); 
        }

    });
	$(".box_changeMobile").validate({
        rules:{
            userPw:{
                required:true,
                minlength:6,
                maxlength:16
            },
			userPhone:{
				required:true,
                mobile:true
			},
			userPhone1:{
				required:true,
                mobile:true,
				equalTo:".box_changeMobile #userPhone"
			}	
        },
        messages:{
            userPw:{
                required:"请输入密码",
                minlength:"不小于6个字符",
                maxlength:"不大于16个字符"
            },
            userPhone:{
               required:"请输入手机号"
            },
            userPhone1:{
               required:"请输入手机号",
			   equalTo: "两次输入手机号不一致"
            }
        },
        errorPlacement: function(error, element) {
            error.insertAfter(element);
        },
        success: function(label) {
            label.addClass("valid").text("")
        },
        submitHandler: function(form) {
            $.ajax({  
                type:"POST",
                url:'/user/changeUserName.json',
                data:{
                    password:$(".box_changeMobile input[name=userPw]").val(),
                    username1:$(".box_changeMobile input[name=userPhone]").val(),
					username2:$(".box_changeMobile input[name=userPhone1]").val()
                },
                dataType:'json',
                success:function (data) {
					alert(data.message.message);
                    if(data.message.operateCode != '102008'){
                       alert(data.message.message);
                        return false;
                    }
                    if (data.message.operateCode == '102008') { //成功
					  alert(data.message.message);
					    location.href="/content/user/login.shtml";
                    }

                },
                error:function (data) {

                    alert("系统繁忙,请稍后再试");
                    return false;
                }
            });
        }

    });
	$(".btnUpload").click(function(){
		$("#fileDragArea").css("display","block");
	})
	$("input[name=sex]").click(function(){
		if($(this).prop('checked')){
			$.ajax({
            type:"POST",
            url: '/user/submitChangeOther.json',
            data:{
               gender:$(this).val()
            },
            dataType:'json',

            success:function (data) {
                alert("更改成功!");
            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		}
	})
	$("#changePwBtn").click(function(){
		$(".box_changePw").css("display","block");
	})
    $("#changeMobileBtn").click(function(){
		$(".box_changeMobile").css("display","block");
	})
	$(".btnCon").click(function(){
		if($(".myName").val()==""||$(".myName").val()==null){
			alert("请输入昵称");
			return;
		}

		$.ajax({
            type:"POST",
            url: '/user/submitChangeOther.json',
            data:{
               nickName:$(".myName").val()
            },
            dataType:'json',
            async: !1,
            success:function(e){
				return e.message.message==undefined?(alert(e.message),void(location.href="/content/user/pcenter.shtml?favPage=1&favRows=8")):(alert(e.message),!1);
            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
	})
})
$(document).ready(function () {
    jQuery.validator.addMethod("mobile", function (value, element) {
        var mobile = /^1[3|4|5|7|8]\d{9}$/;
        return this.optional(element) || (mobile.test(value));
    }, "手机格式不对!");  
	
    $("#loginForm").validate({
        rules:{
            username:{
                required:true,
                mobile:true

            },

            userPassword:{
                required:true,
                minlength:6,
                maxlength:16
            }
        },
        messages:{

            username:{

                required:"请输入手机号"
            },


            userPassword:{
                required:"请输入密码",
                minlength:"不小于6个字符",
                maxlength:"不大于16个字符"
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
                url:'/user/login.json?randID='+parseInt(1000000*Math.random()),
                data:{
                    username:$("#loginForm input[id=username]").val(),
                    userPassword:$("#loginForm input[id=userPassword]").val(),
                    rememberUserName:true,
					url:$("#loginForm input[id=loginurl]").val()
                },
                dataType:'json',

                success:function (data) {
                	
                    if(data.message.operateCode != '102008'){
                       alert(data.message.message);
                        return false;
                    }
                    if (data.message.operateCode == '102008') { //成功 
						if(data.redirectUrl){
							 location.href=UrlDecode(data.redirectUrl)+"";
							 return false;
						}
						location.href="/content/user/pcenter.shtml?favPage=1&favRows=8";
                      
                    }

                },
                error:function (data) {

                    alert("系统繁忙,请稍后再试");
                    return false;
                }
            });
        }

    });
      $(function(){
	
	$("#btnLogin1").click(function(e){
		if($("#"+$(this).parent().parent().attr("id")).css("zIndex")=="3"){
			$("#divLogin1").animate({zIndex:"3",right:"25%",top:"56%"});
			$("#divLogin2").animate({zIndex:"2",right:"0%",top:"49%"});
			$("#divLogin1").animate({zIndex:"3",right:"17%",top:"56%"});
			$("#divLogin2").animate({zIndex:"2",right:"15%",top:"49%"});
		}  
	})
	$("#btnLogin2").click(function(e){
		
		if($("#"+$(this).parent().parent().attr("id")).css("zIndex")=="3"){
			$("#divLogin1").animate({zIndex:"3",right:"25%",top:"56%"});
			$("#divLogin2").animate({zIndex:"2",right:"0%",top:"49%"});
			$("#divLogin1").animate({zIndex:"3",right:"17%",top:"56%"});
			$("#divLogin2").animate({zIndex:"2",right:"15%",top:"49%"});
		}  
	})
	/*$("#btnChangePsw").click(function(){
		
		$("#divLogin1").animate({zIndex:"3",right:"25%",top:"56%"});
		$("#divLogin2").animate({zIndex:"2",right:"0%",top:"49%"});
		$("#divLogin1").animate({zIndex:"3",right:"17%",top:"56%"});
		$("#divLogin2").animate({zIndex:"2",right:"15%",top:"49%"});
	})*/
	$("#btnRegister").click(function(){
      if($("#"+$(this).parent().attr("id")).css("zIndex")=="3"){
		$("#divLogin2 #member_forgetForm").css("display","none");
		$("#divLogin2 #member_regForm").css("display","block");
		$("#divLogin2").animate({zIndex:"3",right:"25%",top:"56%"});
		$("#divLogin1").animate({zIndex:"2",right:"0%",top:"49%"});
		$("#divLogin2").animate({zIndex:"3",right:"17%",top:"56%"});
		$("#divLogin1").animate({zIndex:"2",right:"15%",top:"49%"});
	  }
	})
	$("#btnForPsw").click(function(){
		
		$("#divLogin2 #member_forgetForm").css("display","block");
		$("#divLogin2 #member_regForm ").css("display","none");
		$("#divLogin2").animate({zIndex:"3",right:"25%",top:"56%"});
		$("#divLogin1").animate({zIndex:"2",right:"0%",top:"49%"});
		$("#divLogin2").animate({zIndex:"3",right:"17%",top:"56%"});
		$("#divLogin1").animate({zIndex:"2",right:"15%",top:"49%"});
		
	})
})


});
function UrlDecode(str){ 
	var ret=""; 
	for(var i=0;i<str.length;i++){ 
		var chr = str.charAt(i); 
		if(chr == "+"){ 
			ret+=" "; 
		}else if(chr=="%"){ 
			var asc = str.substring(i+1,i+3); 
			if(parseInt("0x"+asc)>0x7f){ 
				ret+=asc2str(parseInt("0x"+asc+str.substring(i+4,i+6))); 
				i+=5; 
			}else{ 
				ret+=asc2str(parseInt("0x"+asc)); 
				i+=2; 
			} 
		}else{ 
				ret+= chr; 
		} 
	} 
		return ret; 
	} 
function str2asc(str){ 
	return str.charCodeAt(0).toString(16); 
} 
function asc2str(str){ 
	return String.fromCharCode(str); 
} 
  









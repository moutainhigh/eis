var options=
		{
			rules: {
				
				receiveCode: {
					required: true,
					minlength: 8,
					maxlength: 8
				},
			},
			messages: {
				receiveCode: {
					required: "请输入领取码",
					
					minlength: "领取码不能小于8个字符",
					maxlength: "领取码不能大于8个字符"
				},
				
			},
			errorPlacement: function (error, element) {
				error.insertAfter(element.next());
			},
			success: function (label) {
				label.removeClass("error");
			},
			submitHandler: function (form) {
				$.ajax({
                cache: true,
                type: "POST",
                url:"/coupon/get.json" ,
                data:{
                	receiveCode:$("#receiveCode").val(),
                },
                async:false,
                error:function(request) {
                    alert("Connection error");
                },
                 success: function(data) {
                	
                  //console.log(dataStr);
                  if(data.message){
                  	//$("#consss").html(dataStr);
                  	swal(data.message.message);
                  }else{
                  	console.log("success错误");
                  	swal(data.message.message);
                  }
                
                  //$("#consss").html(datas.message);//然后调用对象值

                   
                }
            });
			}
		}
	$(function(){		
		
		$("#yourformid").validate(options);		
		$("input").on("input",function(){  
			$(this).removeData("previousValue").valid(); 
		}); 
	})
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
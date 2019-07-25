
    $(document).ready(function () {
		$(".selects").citySelect({
		          nodata:"none",
		           required:false				  
	    });		
		$("input[type=radio]").change(function(){
			$("input[type=radio]").each(function (i,d){
				d.checked=false;
			});
			$(this).attr("checked","checked");
		})	 
	   jQuery.validator.addMethod("mobile", function (value, element) {
           var mobile = /^1[3|4|5|7|8]\d{9}$/;
           return this.optional(element) || (mobile.test(value));
       }, "手机格式不对");
	   /*jQuery.validator.addMethod("postcode", function(value, element) {  
			var postcode = /^[0-9]{6}$/;
			return this.optional(element) || (postcode.test(value));
		}, "请正确填写您的邮政编码");*/
        $(".abc").validate({
            rules: {
				//收货人
                contact: {
                    required: true,
                    maxlength: 20
                },
				//联系方式
                mobile: {
                    required: true,
					 mobile: true
                },
				//省
                s_province: {
                required: true

                },
				//市
                s_city: {
                    required: true

                },
				//详细地址
                address: {
                    required: true

                }
				
                /*postcode: {
                    required: true,
                    postcode: true
                }*/

            },
            messages: {

                contact: {

                    required: "请输入"
                },
				mobile: {

                    required: "请输入"
                },
               s_province: {

                    required: "请输入"
                },
                s_city: {
                    required: "请输入"
                },
				 s_county: {
                    required: "请输入"
                },
                address: {
                    required: "请输入"
                },                
			/*
                postcode: {
                    required: "请输入",
                   maxlength: "邮政编码不能大于6个字符"
                } */

            },			
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            },
            success: function (label) {
                label.addClass("valid").text("");
            },
            submitHandler: function (form) {
                if(document.getElementById("checkboxgroup").checked){
                    $("#currentStatus").attr("value","100003");
                }
                else{
                    $("#currentStatus").attr("value","100001");
                }
                $.ajax({
                    url:"/addressBook/create.json",
                    data:$(".abc").serialize(),
                    type:"post",
                    dataType:'json',
                    success:function(data){//ajax返回的数据
                        if(data.message.operateCode != operateResultSuccess){
							alert(data.message.message);
                        }
                        if (data.message.operateCode == operateResultSuccess) { //成功
                            alert("添加成功!");
							if(document.referrer.indexOf('?')!=-1){
								location.href=addUrl(document.referrer,data.addressBook.addressBookId); 
							}else{
								location.href=document.referrer+"?selectedAddressBookId="+data.addressBook.addressBookId;
							}                             
						}
					}					
                });
            }
        });     
    });
	function addUrl(url,txt)
		{
			var newUrl='';
			var hasAddress=false;
			var str = url.split("?")[1]; 
			strs = str.split("&"); 
			var key=new Array(strs.length);
			var value=new Array(strs.length);
			newUrl+=url.split("?")[0]+"?";
			for(i=0;i<strs.length;i++) 
			{ 
				 key[i]=strs[i].split("=")[0]
				 value[i]=unescape(strs[i].split("=")[1]); 
				 if(key[i]=="selectedAddressBookId"){
					value[i]=txt;
					hasAddress=true;
				 }
			}
			if(strs.length>1){
				for(var i=0;i<strs.length-1;i++){
					newUrl+=key[i]+'='+value[i]+'&';
				}
				newUrl+=key[i]+'='+value[i];
			}else{
				newUrl+=key[0]+"="+value[0];
			}
			if(!hasAddress){
				newUrl+="&selectedAddressBookId="+txt;
			}		
			return newUrl;
		}
    function del(id){
        $.ajax({
            url:"/addressBook/delete/"+id+".json",
            type:"post",
            dataType:'json',
            success:function(data){
            //ajax返回的数据
                if(data.message.operateCode != operateResultSuccess){
                    alert(data.message.message);
                }
                if (data.message.operateCode == operateResultSuccess) { //成功
                    alert("删除成功!");
                    window.reload();
                }
            }
        });
    }

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<title>${systemName}</title>
	<link href="/theme/ec1/style/com.css" rel="stylesheet" />
<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
<link href="/theme/ec1/style/web.css" rel="stylesheet" />
<link href="/theme/ec1/style/Account.css" rel="stylesheet" />
	<link href="/theme/ec1/style/InputFrom.css" rel="stylesheet" /></head>
<body>
    <form name="form1" id="form1">
<div>


    <%@include file="/WEB-INF/jsp/include/headcenter.jsp" %>

            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>

        <script src="/theme/ec1/js/JQuery.js"></script>
        <script src="/theme/ec1/js/com.js"></script>
        <script>
            function ValidateInput() {
                var contact = $("#contact").val();
                var mobile = $("#mobile").val();
                var address = $("#address").val();
			
                if (!contact) {
                    alert("收货人不能为空");
                    return false;
                }

                if (!mobile) {
                    alert("手机号不能为空");
                    return false;
                }

                if (!address) {
                    alert("请输入收货地址");
                    return false;
                }
            	//ar con = $("#addaress table :input").serialize();//addaress =
            	//alert(con);
                 $.ajax({
                 	type:"POST",
                    url:"/addressBook/add.json",
                    data:$("#addaress table :input").serialize(),
                    dataType:'json',
                    async:true,
                    success:function(data){//ajax返回的数据
                         //location.href="/buy/settleUp.shtml";
						//alert(JSON.stringify(data));
						
                        if(data.message.operateCode != operateResultSuccess){
                            alert(data.message.message);
                        }
                        if (data.message.operateCode == operateResultSuccess) { //成功
                            alert("添加成功!");
							
                           location.href='/content/user/pcenter.shtml';
                                

                        }


                    }
                });
                 return false;
              
            }
            
  
        </script>
    </form>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<title>${systemName}heh</title>
	<link href="/theme/ec1/style/com.css" rel="stylesheet" />
	<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
	<link href="/theme/ec1/style/web.css" rel="stylesheet" />
	<link href="/theme/ec1/style/Account.css" rel="stylesheet" />
	<link href="/theme/ec1/style/InputFrom.css" rel="stylesheet" />
	<script type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.min.js"></script>
<style>
	.tbl-main{border: 1px solid #e7e7e7;width: 100%;margin: 0 auto;}
	.thead-tbl-grade th{height: 40px;padding: 0 5px;border-top: 1px solid #e7e7e7;border-bottom: 1px solid 

#e7e7e7;background: #f2f2f2;text-align: center;
						border-right: 1px solid #e7e7e7;}
	.thead-tbl-address td{text-align: center;border: 1px solid #e7e7e7;}
</style>
</head>
<body>
    <form name="form1" id="form1">
<div>


    <%@include file="/WEB-INF/jsp/include/headcenter.jsp" %>
        <div class="AccountTop">
            <div class="Wrap1000">
                <div class="AccountTop_Left">sad您好，<span id="lblLoginAccount">${frontUser.username}</span></div>
                <ul class="AccountTop_Nav">
                    <li><a href="/">首页</a></li>
                    <li class="Cur"><a href="javascript:;">地址管理</a></li>
                    <li><a href="/content/user/addressbook.shtml">我的订单</a></li>
                </ul>
            </div>
        </div>
        <div class="SpaceBlockHorizontal20"></div>
        <div class="Wrap1000">
            <div class="Account">
                <div class="Account_Title">账户设置</div>
                <div class="InputForm">

                	
               <table border="0" cellspacing="0" cellpadding="0" class="tbl-main">

               <table border="0" cellspacing="0" cellpadding="0" class="tbl-main">
	
               <table id="address" border="0" cellspacing="0" cellpadding="0" class="tbl-main">

					<colgroup>
						<col class="col-man">
						<col class="col-area">
						<col class="col-address">
						<col class="col-postcode">
						<col class="col-phone">
						<col class="col-actions">
					</colgroup>
					<thead>
					<tr class="thead-tbl-grade">
						<th>收货人</th>
						<!--<th>所在地区</th>-->
						<th>收货地址</th>
						<th>电话/手机</th>
						<th>操作</th>
						<th></th>
					</tr>
					</thead>
				<tbody>
					
				</tbody>
				</table>
				 <div class="InputFormMore">
				<table>
					  <tr>
                            <th></th>
                            <td>
                               <!-- <input type="submit" name="btnSubmit" value="新增地址" onclick="return ValidateInput();" 

id="btnSubmit" class="btnRed"/>-->
                                <a href="/content/user/addressadd.shtml" class="btnReder">新增地址</a>
                            </td>
                        </tr>
                      
                </table>
                </div>
                </div>
            </div>
            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>

        <script src="/theme/ec1/js/JQuery.js"></script>
    	<script>
    		$(document).ready(function(){
    			$.ajax({
    				type : 'get',
    				url:'/content/user/pcenter.json',
    				success:function(data){
    					
    					var shdz = "<tr class='thead-tbl-address  need-update'><td>"
    						+data.addressBook.contact+
    						"</td><td>"
    						+data.addressBook.address+
    						"</td><td>"
    						+data.addressBook.mobile+
    						"</td><td><a href='/addressBook/update/${address.addressBookId}.shtml'>修改</a>|<a href='javascript:;' class='del' onclick=\"del(\${editdetail.addressBookId}"
    						+data.addressBook.index+
    						")\">删除</a></td><td class='thead-tbl-status'><a class='note'>默认地址</a></td></tr>"
								$(".InputForm #address tbody").append(shdz);
								
//							for(var i=0;i<data.favoriteList.length;i++){ //ata.favoriteList.length data下面的favoritelist这个对象数组的总数组长度
//								var qtdz ="<tr class='thead-tbl-address  need-update'>\
//									<td>"+data.favoriteList.contact+"</td>\
//									<td>"+data.favoriteList.address+"</td>\
//									<td>"+data.favoriteList.mobile+"</td>\
//									<td><a href='/content/user/addressadd.shtml'>修改</a>\
//									|\
//									<a href='' class='del'>删除</a>\
//								</td>\
//								<td class='thead-tbl-status'><a href='' class='note'>设为默认</a></td></tr>"
//								$(".InputForm #address tbody").append(qtdz);
//							}
						}	
    			})		
    		})
    		/*删除地址 */
    		
    function del(id){ //id 需要传一个需要删除的ID号  在数据库不可重复的数据 一般都用id  ID=3
        $.ajax({
            url:"/addressBook/delete/"+id+".json",
            type:"post",
            dataType:'json',
            success:function(data){
            //ajax返回的数据
			alert(JSON.stringify(data));
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

    	</script>    
    </form>
</body>
</html>
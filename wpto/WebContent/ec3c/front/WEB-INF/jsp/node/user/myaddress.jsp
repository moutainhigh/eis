 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<title>${systemName}</title>
	<!--<link href="/theme/ec1/style/com.css" rel="stylesheet" />
<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
<link href="/theme/ec1/style/web.css" rel="stylesheet" />
<link href="/theme/ec1/style/Account.css" rel="stylesheet" />
<link href="/theme/ec1/style/InputFrom.css" rel="stylesheet" />-->
<link href="/theme/${theme}/css/style.css"  rel="stylesheet" type="text/css" />
<!--<script  type="text/javascript" src="/theme/${theme}/js/jquery.js"></script>-->
<script  type="text/javascript" src="/theme/${theme}/js/jquery-1.11.3.js"></script>
<script  type="text/javascript" src="/theme/${theme}/js/selectCity.js"></script>
    </head>
<body>
    <form name="form1" id="form1">
<div>


 <%@include file="/WEB-INF/jsp/include/head.jsp" %>
 
 
  <div class="block box">
<div class="blank"></div>
 <div id="ur_here">
当前位置: <a href="/">首页</a> <code>&gt;</code> 购物流程 
</div>
</div>
<div class="blank"></div>
 
<div class="block table">
    <input type="hidden" value="${frontUser.userConfigMap.userBindMailBox.dataValue}" id="emails">
    <form action="" method="post" name="theForm" id="theForm" onsubmit="return checkConsignee(this)">
    <div class="flowBox">
        <h6><span>收货人信息</span></h6>
        <table width="99%" align="center" border="0" cellpadding="5" cellspacing="1" bgcolor="#dddddd"> 
            <tbody id="tbody">
                
            </tbody>
            <tr>
                <td bgcolor="#ffffff">配送区域:</td>
                <td colspan="3" bgcolor="#ffffff">
                    <span id="citys">
                        <select class="prov" id="prov" style="border:1px solid #ccc;"></select>
                        <select class="city" id="city" style="border:1px solid #ccc;"></select>
                        <select class="dist" id="dist" style="border:1px solid #ccc;"></select>
                    </span>(必填) 
                </td>
            </tr>
            <tr>
                <td bgcolor="#ffffff">收货人姓名:</td>
                <td bgcolor="#ffffff">
                    <input name="consignee" type="text" class="inputBg" id="name" value="" />(必填) 
                </td>
                <td bgcolor="#ffffff">电子邮件地址:</td>
                <td bgcolor="#ffffff"><input name="email" type="text" class="inputBg email"  id="email_1" value="" disabled/>
                </td>
            </tr>
            <tr>
                <td bgcolor="#ffffff">详细地址:</td>
                <td bgcolor="#ffffff">
                    <input name="address" type="text" class="inputBg"  id="address" value="" />(必填)
                </td>
                <td bgcolor="#ffffff">邮政编码:</td>
                <td bgcolor="#ffffff">
                    <input name="zipcode" type="text" class="inputBg"  id="zipcode" value="" />
                </td>
            </tr>
            <tr>
                <td bgcolor="#ffffff">电话:</td>
                <td bgcolor="#ffffff">
                    <input name="tel" type="text" class="inputBg"  id="tel" value="" />
                </td>
                <td bgcolor="#ffffff">手机:</td>
                <td bgcolor="#ffffff"><input name="mobile" type="text" class="inputBg"  id="mobile" value="" />(必填)
                </td>
            </tr>
            <tr>
                <td colspan="4" align="center" bgcolor="#ffffff">
                    <input type="button" name="Submit" class="bnt_blue_2" value="新增地址" onclick="createAddress(this)" />
                </td>
            </tr>   
        </table>
    </div>        
</form>


        <script type="text/javascript">
        var i = -5;
        var nub = 0;
        // 页面加载开始更新配送地址页面
      		$(function(){ 
                $('#tbody').empty();

                // 选择城市插件
                $("#citys").citySelect({
                    url:"/theme/${theme}/js/city.min.js",  
                    prov:"北京", //省份 
                    city:"东城区", //城市 
                    dist:"", //区县 
                    nodata:"none" //当子集无数据时，隐藏select 
                }); 
                $.ajax({  
                    type : "get",  
                    url : "/addressBook/index.json",  
                    dataType:"json",
                    data:'', 
                    success : function(data) { 
                            var addressBooks = data.addressBook.map(function(a){
                                i = i+5;
                                return ('<tr><td bgcolor="#ffffff">配送区域:</td><td colspan="3" bgcolor="#ffffff"><span id="citysa"><select class="prov" style="border:1px solid #ccc;" disabled="disabled"><option value="'+a.province+'">'+a.province+'</option></select><select class="city" style="border:1px solid #ccc;" disabled="disabled"><option value="'+a.city+'">'+a.city+'</option></select><select class="dist" style="border:1px solid #ccc;" disabled="disabled"><option value="'+a.district+'">'+a.district+'</option></select></span>(必填) </td></tr><tr><td bgcolor="#ffffff">收货人姓名:</td><td bgcolor="#ffffff"><input name="consignee" type="text" class="inputBg" id="consignee_1" value="'+a.contact+'" />(必填) </td><td bgcolor="#ffffff">电子邮件地址:</td><td bgcolor="#ffffff"><input name="email" type="text" class="inputBg email"  id="email_1" value="" disabled/></td></tr><tr><td bgcolor="#ffffff">详细地址:</td><td bgcolor="#ffffff"><input name="address" type="text" class="inputBg"  id="address_1" value="'+a.address+'" />(必填)</td><td bgcolor="#ffffff">邮政编码:</td><td bgcolor="#ffffff"><input name="zipcode" type="text" class="inputBg"  id="zipcode_1" value="'+a.postcode+'" /></td></tr><tr><td bgcolor="#ffffff">电话:</td><td bgcolor="#ffffff"><input name="tel" type="text" class="inputBg"  id="tel_1" value="'+a.phone+'" /></td><td bgcolor="#ffffff">手机:</td><td bgcolor="#ffffff"><input name="mobile" type="text" class="inputBg"  id="mobile_1" value="'+a.mobile+'" />(必填)</td></tr><tr><td colspan="4" align="center" bgcolor="#ffffff"><input type="hidden" value="'+i+'"><input type="button" name="Submit" class="bnt_blue_2 bnt_blue_5" value="更新地址"  /><input name="button" type="button" onclick="deletes(this)"  class="bnt_blue" value="删除" style="margin-left: 3px;" /><input type="hidden" value='+a.addressBookId+' name="addressBookId" /><input name="button" type="button" onclick="one(this)"  class="one" value="设置默认地址" /></td></tr>');
                            })
                            $('#tbody').append(addressBooks);

                            // email电子邮件
                            $('.email').val($('#emails').val());

                            
                            // 更新配送地址按钮
                            $('.bnt_blue_5').each(function(){
                                $(this).click(function(){
                                    nub = parseInt($(this).siblings().eq(0).val());

                                    $.ajax({  
                                        type : "post",  
                                        url : "/addressBook/update.json",  
                                        dataType:"json",
                                        data:{
                                            province:$(this).parents('tr').siblings().eq(nub).find('select').eq(0).val(),
                                            city:$(this).parents('tr').siblings().eq(nub).find('select').eq(1).val(),
                                            district:$(this).parents('tr').siblings().eq(nub).find('select').eq(2).val(),
                                            address:$(this).parents('tr').siblings().eq(nub+2).find('input[name="address"]').val(),
                                            contact:$(this).parents('tr').siblings().eq(nub+1).find('input[name="consignee"]').val(),
                                            phone:$(this).parents('tr').siblings().eq(nub+3).find('input[name="tel"]').val(),
                                            mobile:$(this).parents('tr').siblings().eq(nub+3).find('input[name="mobile"]').val(),
                                            postcode:$(this).parents('tr').siblings().eq(nub+2).find('input[name="zipcode"]').val(),
                                            addressBookId:$(this).siblings().eq(2).val()
                                        }, 
                                        success : function(data) { 
                                            if (data.message.operateCode=='102008') {
                                                alert(data.message.message);
                                                document.location.reload();
                                            }else{
                                                alert(data.message.message);  
                                                return false;      
                                            }
                                        },  
                                        error : function(data) {  
                                            alert(data.message.message);
                                        }  
                                    });
                                })
                            }) 
                        // }
                    },  
                    error : function(data) {  
                        alert(data.message.message);
                    }  
                });  
			});


           
            // 新增配送地址按钮
            function createAddress(){
                    if($('#name').val().length == 0 || $('#address').val().length == 0 || $('#mobile').val().length == 0){
                           alert('请填写完必选项信息！');
                    }else{
                        $.ajax({  
                            type : "post",  
                            url : "/addressBook/create.json",  
                            dataType:"json",
                            data:{
                                province:$("#prov  option:selected").text(),
                                city:$("#city  option:selected").text(),
                                district:$("#dist  option:selected").text(),
                                address:$('#address').val(),
                                contact:$('#name').val(),
                                phone:$('#tel').val(),
                                mobile:$('#mobile').val(),
                                postcode:$('#zipcode').val()
                            }, 
                            success : function(data) { 
                                if (data.message.operateCode=='102008') {
                                    alert(data.message.message);
                                    location.reload(); 
                                }else{
                                    alert(data.message.message);
                                    return false;    
                                    }
                            },  
                            error : function(data) {  
                                alert(data.message.message);
                            }  
                        });  
                    }
            }

            // 删除地址记录
            function deletes(a){
                var id = a.nextSibling.value;
                 $.ajax({  
                        type : "post",  
                        url : "/addressBook/delete/"+id+".json",  
                        dataType:"json",
                        data:{}, 
                        success : function(data) { 
                            if (data.message.operateCode=='102008') {
                                alert(data.message.message);
                                location.reload(); 
                            }else{
                                alert(data.message.message);
                                return false;    
                                }
                        },  
                        error : function(data) {  
                            alert(data.message.message);
                        }  
                    });  
            }

            // 设置默认地址
            function one(a){
                var id = a.previousSibling.value;
                $.ajax({  
                    type : "get",  
                    url : "/addressBook/setDefault/"+id+".json",  
                    dataType:"json",
                    data:{}, 
                    success : function(data) {
                        if (data.message.operateCode=='102008') {
                            alert(data.message.message);
                            location.reload(); 
                        }else{
                            alert(data.message.message);
                            return false;    
                            }
                    },  
                    error : function(data) {  
                        alert(data.message.message);
                    }  
                });
            }
		</script>

</div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
        </div>
</body>
</html>

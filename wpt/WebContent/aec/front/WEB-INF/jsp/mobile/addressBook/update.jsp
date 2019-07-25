<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<title>编辑收货地址</title>
<link href="../../../theme/${theme}/css/mobile/head.css" rel="stylesheet" type="text/css">
<link href="../../../theme/${theme}/css/mobile/addressadd.css" rel="stylesheet" type="text/css">
<script src="../../../theme/${theme}/js/mobile/jquery-1.11.3.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.form.js"></script>
<script src="../../../theme/${theme}/js/mobile/jquery.validate.min.js"></script>
<script src="../../../theme/${theme}/js/mobile/common.min.js"></script>
<script type="text/javascript" src="../../../theme/${theme}/js/mobile/jquery.cityselect.js"></script>
<script src="../../../theme/${theme}/js/jsupaddress.js" type="text/javascript"></script>
<script  type="text/javascript" src="../../../theme/${theme}/js/mobile/lazyload.js"></script>
<script type="text/javascript">
$(function(){

//	$(".box_right").citySelect({
//   	    prov:"${editdetail.province}",
//    	city:"${editdetail.city}"
//	});
    $(".selects").citySelect({
        nodata:"none",
        required:false
    })
    $.ajax({
        type:"GET",
        url:"/addressBook/update/"+${editdetail.addressBookId}+".json",
        dataType:"json",
        success:(function(data){
            console.log(data);
            $(data.editdetail).each(function(i,m) {
                console.log(m);
                console.log(m.city);
                $("#city").html("<option>"+m.city+"</option>");
                $("#province").val(m.province);
            })
        }),
        error:function(data) {
            alert("系统繁忙,请稍后再试");
            return false;
        }
    })
});	
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
                    location.href='/addressBook/index.shtml';


                }



            }
        });
    }
  </script>
<style type="text/css">
  *{
    font-size: 16px;
  }
</style>
</head>
<body>
  <div class="header" id="header">
    <a class="back" href="javascript:history.go(-1);"></a><span>修改地址</span><!--<a class="home" href="/">-->
 </div>
<!--  <%@include file="/WEB-INF/jsp/include/commonhead.jsp" %> -->
 
 <form  method="POST"  name="member_xgForm" id="member_xgForm"   onsubmit="return false;">
 <ul>
    <li><span class="box_left">收货人</span><input type="text" value="${editdetail.contact}" id="contact"  name="contact" class="box_right"/></li>
    <li><span class="box_left">区域选择</span>
         <div class="selects">
                <select class="prov" id="province" name="province"></select>
                 <select class="city" id="city" name="city"></select>
         </div>
    </li>
    <li><span class="box_left">详细地址</span><input type="text" value="${editdetail.address}" id="address"  name="address" class="box_right"/></li>
    <li><span class="box_left">联系方式</span><input type="text" value="${editdetail.mobile}" id="mobile"  name="mobile"  class="box_right"/></li>
    <!--<li><span class="box_left">邮政编码</span><input type="text" value="${editdetail.postcode}" id="postcode"  name="postcode"  class="box_right"/>
    </li>-->
    <input type="hidden" value="" id="currentStatus"  name="currentStatus"  class="box_right"/>
    <input type="hidden" value="${editdetail.addressBookId}" id="addressBookId"  name="addressBookId"  class="box_right"/>
    <li>
       <div class="checkBox">
  		<input type="checkbox" value="1" id="checkboxInput"  name="" checked/>
	  	<label for="checkboxInput" id="checkBoxLabel"></label>
  	   </div>
       <span class="setfault">设为默认地址</span>
    </li>
 </ul>
 <div class="btn_groups">
 <div class="btn_delete"><input type="button" class="orangebutton" value="删除"  onclick='del(${editdetail.addressBookId})'/></div>
 <div class="btn_save"><input type="submit" class="bluebutton" id="adddistrict" name="adddistrict" value="保存" /></div>
 </div>
 
 </form>
  <script type="text/javascript">
      var Gid  = document.getElementById ;
      var showArea = function(){
          Gid('show').innerHTML = "<h3>省" + Gid('s_province').value + " - 市" +
                  Gid('s_city').value + " - 县/区" +
                  Gid('s_county').value + "</h3>"
      }


  </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${systemName}-支付方式</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	  <link rel="stylesheet" href="/theme/${theme}/style/font-awesome/css/font-awesome.min.css">	
    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
	<script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	
  </head>
  <style type="text/css">
 .payul li{
  list-style-type: none;
  float: left;
}
/* Fixes incorrect placement of numbers in ol’s in IE6/7 */ 
ol { margin-left:2em; } 
/* == clearfix == */ 
/* new clearfix */
.clearfix:after {
visibility: hidden;
display: block;
font-size: 0;
content: " ";
clear: both;
height: 0;
}
* html .clearfix             { zoom: 1; } /* IE6 */
*:first-child+html .clearfix { zoom: 1; } /* IE7 */
  .payul{
    margin-top: 35px;
  }
  .payul li>span{
    line-height: 22px;
    float: left;
  }
  .payul li>span.name{
    font-size: 16px;
  }
  .payul li{
    margin-right: 40px;
        margin-bottom: 30px;
  }
  .payul li .switch-button{
    display: inline-block;
    width: 70px;
    height: 22px;
    border-radius: 20px;
    text-align: center;
    position: relative;
        margin-left: 10px;
  }
  .switch-button span{
    position: absolute;top: 0;
    display: inline-block;
    width: 77%;text-align: center;
    line-height: 22px;
    color: #fff;
    font-size: 12px;
  }
  .switch-button em{
    position: absolute;top: 0px;
    width: 22px;
    height: 22px;
    border-radius: 20px;
    background-color: #D4D4D4;
    box-shadow: 2px 1px 3px rgba(51, 51, 51, 0.43);
    transition:all ease 0.3s;
    -webkit-transition:all ease 0.3s;
    -moz-transition:all ease 0.3s;
  }
  .switch-button input[type="checkbox"]{
    width: 100%;
    height: 100%;
    position: absolute;top: 0;left: 0;
    opacity: 0;
    cursor: pointer;
  }
  .switch-button.on{
    background-color:#77AF1D;
  }
  .switch-button.on span{
    right:0;
  }
  .switch-button.on em{
    left: 0px;
  }
  .switch-button.off{
    background-color:rgb(191, 58, 58);
  }
  .switch-button.off span{
    left:0;
  }
  .switch-button.off em{
    left: 69%;
    margin-left: 0px;
  }
  .table-responsive.xiugaibox {
    min-height: 200px;
    background-color: #eee;
    border-radius: 5px;
    padding: 20px;
}
  .xiugaibox h3{
    font-size: 18px;
    border-bottom: 3px solid #5C6F87;
    padding-bottom: 10px;
    margin-bottom: 24px;
    margin-top: 10px;
    /* font-weight: bold; */
    color: #37485D;
  }
  /*弹窗*/
    .Popup{
    position: fixed;top: 0;left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 99999;
  } 
  .Popup.none{
    display: none;
  }
  .Popup .box{
    position: absolute;
    top: 50%;
    width: 290px;
    height: 168px;
    left: 50%;
    margin-left: -145px;
    margin-top: -140px;
    background-color: #eee;
    border-radius: 5px;
    padding: 20px;
  }
  .Popup .box h3{
    font-size: 20px;
    text-align: center;
    margin-bottom: 36px;
    color: #A52222;
    margin-top: 29px;
  }
  .Popup .box p{
    text-align: center;
  }
  .Popup .box input[type="button"]{
   width: 32%;
    padding: 6px;
    background-color: #51749E;
    color: #fff;
    border-radius: 5px;
    display: inline-block;
    border: none;
  }
  .Popup .none{
      display: none;
  }
  .Popup .closespan{
    display: inline-block;
    width: 120px;
    padding: 6px;
    background-color: #b1b1b1;
    color: #fff;
    border-radius: 5px;
    cursor: pointer;
  }
</style>
  <body>
   <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
	  
	     
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span><spring:message code="支付方式" /></span></h2><!-- ConfigCategory.${ct} -->
			<div class="table-responsive">
				<div class="table-responsive xiugaibox">
         <h3><b>${partner.nickName}</b>用户支付方式配置</h3>
         <ul class="payul clearfix" data-uuid="${partner.uuid}">
          <c:forEach var="r" items="${payType}" varStatus="varStatus">
           <li class="clearfix">
             <span class="name">${varStatus.index+1}.${r.name} ：</span><span class="Immediate" style="font-size:16px;"></span>
             <span class="switch-button on">
               <span class="on">OFF</span>
             <em></em>
               <input type="checkbox" checked currentStatus="${r.currentStatus}" data-id="${r.payTypeId}" data-name="${r.name}"/>
             </span>
           </li>
           </c:forEach>
         </ul>
        </div>


			</div>
        </div>
        
       
        
      </div>
    </div>
    <!-- 弹窗 -->
    <div class="Popup none">
      <div class="box">
      <h3>微信支付已关闭！</h3>
      <p><input type="button" class="closespan" value="确 认"></p>
      </div>
    </div>
    
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <!--<script src="/theme/${theme}/js/vendor/holder.min.js"></script>-->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
<script type="text/javascript">
  var uuid = $('.payul').attr('data-uuid');
  $("input[type='checkbox']").each(function () {
    if($(this).attr('currentStatus')=='0'){
      $(this).removeAttr('checked');
    }else{
      $(this).attr('checked','checked');
    }
    if($(this).is(':checked')){
      $(this).parent().addClass('on');
      $(this).parent().removeClass('off');
      $(this).parent().find('span').text('OFF');
      $(this).parents('li').find('.Immediate').text('已开启');
    }else{
      $(this).parent().removeClass('on');
      $(this).parent().addClass('off');
      $(this).parent().find('span').text('ON');
      $(this).parents('li').find('.Immediate').text('已关闭');
    }
  });
  $("input[type='checkbox']").on('click',function(){
    var id = $(this).attr("data-id");
    var value = $(this).attr('currentStatus');
    var kuozhan = 'updateMode';
    if($(this).is(':checked')){
      $(this).parent().addClass('on');
      $(this).parent().removeClass('off');
      $(this).parent().find('span').text('OFF');
      $(this).parents('li').find('.Immediate').text('已开启');
      $(this).attr('currentStatus','100001');
    }else{
      $(this).parent().removeClass('on');
      $(this).parent().addClass('off');
      $(this).parent().find('span').text('ON');
      $(this).parents('li').find('.Immediate').text('已关闭');
      $(this).attr('currentStatus','0');
    }
    var name = $(this).attr('data-name');
    ajaxfunction(uuid,id,$(this).attr('currentStatus'),name);
  })

//弹框点击事件
$('.Popup .closespan').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  
});





  function ajaxfunction(uuid,id,value,name){
    var dataarry = {'uuid':uuid,'payTypeId':id,'currentStatus':value};
     console.log(dataarry);
    $.ajax( {  
           type : "POST",  
           url : "/partnerPayTypeRelation/update.json ",  
           dataType:"json",
           data:dataarry, 
           success : function(data) {  
            if(value=='0'){
              var tishi='已关闭！';
              $('.Popup h3').text(name+tishi);
              $('.Popup').removeClass('none');

            }else{
              var tishi='已开启！';
              $('.Popup h3').text(name+tishi);
              $('.Popup').removeClass('none');
            };
            
           },  
           error : function() { 
              var tishi = '修改失败！';
              $('.Popup h3').text(name+tishi);
              $('.Popup').removeClass('none');
           }  
          });  
  }
</script>




  </body>
</html>

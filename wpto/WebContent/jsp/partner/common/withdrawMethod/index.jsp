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

    <title>${systemName}-批付通道管理</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/theme/${theme}/style/dashboard.css" rel="stylesheet">
  <link href="/theme/${theme}/style/pageJSTL.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="/theme/${theme}/js/ie-emulation-modes-warning.js"></script>
  <script src="/theme/${theme}/js/My97DatePicker/WdatePicker.js"></script>
  <script src="/theme/${theme}/js/jquery-1.8.3.min.js"></script>
  <script src="/theme/${theme}/js/pageQuery.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  <script src="/theme/${theme}/js/sweetalert.min.js"></script>
    <script>
          //关闭功能
          function closePresent(i,c){
              if(c=="100001"){
                  swal({
                      title: "您确定要关闭吗？",
                      type: "warning",
                      showCancelButton: true,
                      closeOnConfirm: false,
                      confirmButtonText: "确认",
                      confirmButtonColor: "#ec6c62"
                  }, function() {
                      $.ajax({
                          type:"POST",
                          url:"/payMethod/update.json?withdrawMethodId="+i+"&currentStatus="+100002,
                          dataType:"json",
                          success:function(data){
                              swal({
                                  title: data.message.message
                              },function(){
                                  window.location.reload();
                              });
                          }
                      });
                  });
        }else if(c=="100002"){
                  swal({
                      title: "您确定要打开吗？",
                      type: "success",
                      showCancelButton: true,
                      closeOnConfirm: false,
                      confirmButtonText: "确认",
                      confirmButtonColor: "#ec6c62"
                  }, function() {
                      $.ajax({
                          type:"POST",
                          url:"/payMethod/update.json?withdrawMethodId="+i+"&currentStatus="+100001,
                          dataType:"json",
                          success:function(data){
                              swal({
                                  title: data.message.message
                              },function(){
                                  window.location.reload();
                              });
                          }
                      });
                  });
        }

          }
    </script>
    <link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
  <style>
    @media (max-width:768px){
      .search_form td{
        display:block !important;
      }
    }
  
    .table-responsive td{
      display:table-cell;
      vertical-align:middle;
      line-height:100%;
    }
.switch-button{
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
  </style>
  </head>
  <body>
  <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
    <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      <h2 class="sub-header"><span>批付通道管理</span></h2>
    
      <p><a href="<c:url value='/withdrawMethod/create'/>" class="addmoban">+ 新增批付通道</a></p>
      <div class="table-responsive">      
         <table class="table table-striped">
                  <thead>
                   <tr>
                      <th>批付通道ID</th>
                      <th>绑定ID</th>
                      <th>虚拟钱包UUID</th>
                      <th>名称</th>
                      <th>对应批付类型</th>
 		              <th>配置简述</th>
                      <th>状态</th>
                      <th>开关操作</th>
                      <th>操作</th>
                    </tr>
                  </thead>
          <tbody>
          <c:forEach var="i" items="${rows}">
            <tr>
                      <td>${i.withdrawMethodId}</td>
                      <td>${i.channelId}</td>
                      <td><a href="/merchant/get/${i.referUuid}.shtml">${i.referUuid}</a></td>
                      <td>${i.name}</td>
                      <td>${i.operate.withdrawTypeName}</td>
  	                  <td>${i.data.accountId}/${i.data.accountName}</td>
                      <td>
                        <font
                        <c:choose>
                          <c:when test="${i.currentStatus==100001}">
                             color="#006600"                          
                          </c:when>
                          <c:otherwise>
                            color="#000000"
                          </c:otherwise>
                        </c:choose>
                        >
                        <spring:message code="Status.${i.currentStatus}" /></font></td>
                        <td>
                          <span class="Immediate">${i.currentStatus==100001?'已开启':'已关闭'}</span>
                          <span class="switch-button on">
                         <span class="on">OFF</span>
                         <em></em>
                         <input type="checkbox" checked currentStatus="${i.currentStatus}" data-id="${i.withdrawMethodId}" data-name="${i.name}"/>
                      </span>
                        </td>
                      <td style="position:relative;">
              <span class="tools" style="right:5px;cursor: pointer;"><img src="/theme/basic/images/tools.png"></span>
              <ul style="position:absolute; width: 58px; padding-right: 10px; padding-left: 10px; margin-left: -28px; top:40px;left:50%;line-height: 30px; list-style: none; z-index: 100; border: 1px solid rgb(221, 221, 221); border-radius: 5px; display: none; background-color: rgb(255, 255, 255);" class="toolbtns">
                <li><a href="<c:url value='/withdrawMethod/get/${i.withdrawMethodId}.shtml'/>">查看</a></li>
                <%--<c:if test="${i.operate.update != null }">
                    <c:set var="c" value="${i.currentStatus}"/>
                    <li><a href="<c:url value='/withdrawMethod/get/${i.withdrawMethodId}.shtml'/>">查看</a></li>
                </c:if>--%>
                
              </ul>
                      </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <div class="Pagination" style="text-align:center;width:100%;background:#fff;">  
        <%@include file="/WEB-INF/jsp/common/include/paging/default.jsp"%>
      </div>
        </div>    
      </div>
    
    </div>
    <div id="jurisdiction" style="display:none;">
      <span>
        <c:forEach items="${listTag}" var="a">
        ${a.tagName},
        </c:forEach>
      </span>
    </div>
  <%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
  <script>
if ($('#jurisdiction>span').length && $('#jurisdiction>span').length>0) {
var jurisdictiontext = $('#jurisdiction>span').text();
if (jurisdictiontext.indexOf('edit')>-1) {
  $('.switch-button').css('display','inline-block');
  $('table tr').each(function(){
    $(this).find('td').last().css('display','block');
    $(this).find('th').last().css('display','block');
  })
}else if (jurisdictiontext.indexOf('browse')>-1) {
  $('.switch-button').css('display','none');
  $('table tr').each(function(){
    $(this).find('td').last().css('display','block');
    $(this).find('th').last().css('display','block');
  })
}else if (jurisdictiontext.indexOf('switch')>-1) {
  $('.switch-button').css('display','inline-block');
  $('table tr').each(function(){
    $(this).find('td').last().css('display','block');
    $(this).find('th').last().css('display','block');
  })
}}else{
     $('.switch-button').css('display','none');
      $('table tr').each(function(){
        $(this).find('td').last().css('display','none');
        $(this).find('th').last().css('display','none');
      })
  }



  //var uuid = $('.payul').attr('data-uuid');
  $("input[type='checkbox']").each(function () {
    if($(this).attr('currentStatus')=='100001'){
      $(this).attr('checked','checked');
    }else{
      $(this).removeAttr('checked');
    }
    if($(this).is(':checked')){
      $(this).parent().addClass('on');
      $(this).parent().removeClass('off');
      $(this).parent().find('span').text('OFF');
      //$(this).parents('li').find('.Immediate').text('已开启');
    }else{
      $(this).parent().removeClass('on');
      $(this).parent().addClass('off');
      $(this).parent().find('span').text('ON');
      //$(this).parents('li').find('.Immediate').text('已关闭');
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
      $(this).parents('td').find('.Immediate').text('已开启');
      $(this).attr('currentStatus','100001');
    }else{
      $(this).parent().removeClass('on'); 
      $(this).parent().addClass('off');
      $(this).parent().find('span').text('ON');
      $(this).parents('td').find('.Immediate').text('已关闭');
      $(this).attr('currentStatus','100002');
    }
    var name = $(this).attr('data-name');
    ajaxfunction(id,$(this).attr('currentStatus'),name);
  })


  function ajaxfunction(id,value,name){
    var dataarry = {'withdrawMethodId':id,'currentStatus':value,'updateCurrentStatus':'updateCurrentStatus'};
     console.log(dataarry);
    $.ajax( {  
           type : "POST",  
           url : "/withdrawMethod/update.json ",  
           dataType:"json",
           data:dataarry, 
           success : function(data) {  
            if(value=='100001'){
              var tishi='已开启！';
              swal(name+tishi);
              //$('.Popup').removeClass('none');

            }else{
              var tishi='已关闭！';
              swal(name+tishi);
              //$('.Popup h3').text(name+tishi);
              //$('.Popup').removeClass('none');
            };
            
           },  
           error : function() { 
              var tishi = '修改失败！';
              swal(name+tishi);
              //$('.Popup h3').text(name+tishi);
              //$('.Popup').removeClass('none');
           }  
          });  
  }







    //重发通知
    function retransmission(a) {
      $.ajax({
        type:"GET",
        url:"/pay/notify/" + a + ".json",
        dataType:"json",
        success:function(data){
          swal(data.message.message);
        }
      })
    }
  </script>
  <script>
  $(function(){
    var len=$(".table-responsive table td ").find("a").length;
    for(var i=0;i<len;i++){
      $(".table-responsive table td a")[i].ondragstart=dragstart;
      
    }




  })
  function dragstart(){return false;}
  </script>
  <script>
  $(".tools").click(function(){
    $(this).parent().parent().siblings().find(".toolbtns").hide();
    $(this).siblings(".toolbtns").toggle();
  })
  </script>
  </body>
</html>

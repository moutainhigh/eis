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

    <title>${systemName}-账号详情</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this workflow -->
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
    td{
      text-align: left;
    }
  	input[type='text']{
  		border:none;
  		background-color: #ddd;
  		text-align: left;
      border-radius: 5px;
      padding: 3px 5px;
      min-width: 70%;
  	}
	.inputclass{
		padding: 2px 5px;
    	border: 1px solid #A9A9A9;
    	border-radius: 5px;
    	background-color:#fff;
	}
  .hide{
    display: none;
  }
  .a-updata{
    margin-right: 10px;
  }
    /*弹窗*/
    .Popup{
    position: fixed;top: 60px;left: 0;
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
    min-height: 168px;
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
  select{
    border-radius: 5px;
    width: 35%;
  }
  .ztspan{
        width: 35%;
    display: inline-block;
    background-color: #ddd;
    border-radius: 5px;
    padding: 3px 5px;
  }
  </style>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>扩展字段详情</span></h2>
			<div class="table-responsive">	
        <form action="" id='productTypeForm' method="post">
		<table class="table table-striped" data-uuid="${dataDefine.dataDefineId}">
		<colgroup width="800">
			<col width="100"/>
			<col width="*"/>
		</colgroup>
		<tr class="header tabheader">
			<td  align="left" colspan="3">${dataDefine.dataDefineId}#扩展字段详情</td>
		</tr>	
		<tr>
			<th style="min-width:450px;">扩展数据ID</th>
			<td>
				<c:out value="${dataDefine.dataDefineId}"/>
			</td>
			<td></td>

		</tr>
		<tr>
			<th><span>数据代码</span></th>
			<td>
				<input type="text" name="dataCode" id="dataCode" readonly value="${dataDefine.dataCode}"/>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>数据类型</span></th>
			<td>
        <span class='ztspan'>${dataDefine.dataType}</span>
        <select name="dataType" id="dataType" class="hide">
          <c:forEach items="${dataType}" var="i">
            <option value="${i}" >${i}</option>
          </c:forEach> 
        </select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>数据名称</span></th>
			<td>
				<input type="text" name="dataName" id="dataName" readonly value="${dataDefine.dataName}"/>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>数据说明</span></th>
			<td>
				<input type="text" name="dataDescription" id="dataDescription" readonly value="${dataDefine.dataDescription}"/>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>允许在哪个级别显示</span></th>
			<td>
				
        <span class='ztspan'>${dataDefine.displayLevel}</span>
        <select name="displayLevel" id="displayLevel" class="hide">
          <c:forEach items="${displayLevel}" var="i">
            <option value="${i}" >${i}</option>
          </c:forEach> 
        </select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>用户端的输入方式</span></th>
			<td>
				<input type="text" name="inputMethod" id="inputMethod" readonly value="${dataDefine.inputMethod}"/>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>允许哪个级别输入</span></th>
			<td>
        <span class='ztspan'>${dataDefine.inputLevel}</span>
        <select name="inputLevel" id="inputLevel" class="hide">
          <c:forEach items="${inputLevel}" var="i">
            <option value="${i}" >${i}</option>
          </c:forEach> 
        </select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr id="objectType">
			<th><span>数据定义对应的类型</span></th>
			<td>
				
        <span class='ztspan'>${dataDefine.objectType}</span>
        <select name="objectType" id="objectType" class="hide">
          <c:forEach items="${objectType}" var="i">
            <option value="${i}" >${i}</option>
          </c:forEach> 
        </select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr id="objectIdp">
			<th><span>数据定义Id</span></th>
			<td>
			
        <span class='ztspan'>${dataDefine.objectId}</span>
        <select name="objectId" id="objectId" class="hide">
          <c:choose>
          <c:when test="${dataDefine.objectType=='user'}">
          <c:forEach items="${userType}" var="i">
            <option value="${i.userTypeId}" >${i.userTypeName}</option>
          </c:forEach> 
          </c:when>
          <c:when test="${dataDefine.objectType=='document'}">
          <c:forEach items="${documentType}" var="i">
            <option value="${i.documentTypeId}" >${i.documentTypeName}</option>
          </c:forEach>  
          </c:when>
          <c:when test="${dataDefine.objectType=='product'}">
          <c:forEach items="${productType}" var="i">
            <option value="${i.productTypeId}" >${i.productTypeName}</option>
          </c:forEach> 
        </c:when>
        <c:otherwise>
          <option value="0"></option> 
        </c:otherwise>
        </c:choose>
        </select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>比较时的条件操作符</span></th>
			<td>
				<input type="text" name="compareMode" id="compareMode" readonly value="${dataDefine.compareMode}"/>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>状态</span></th>
			<td>
				<span class='ztspan'><spring:message code="Status.${dataDefine.currentStatus}" /></span>
				<select name="currentStatus" id="currentStatus" class="hide">
					<c:forEach items="${statusList}" var="i">
            <option value="${i}" ><spring:message code="Status.${i}" /></option>
          </c:forEach> 
				</select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
   



	</table>
  
	</form>
	
	</div>
        </div>
      </div>
    </div>

<!-- 弹窗 -->
    <div class="Popup none">
      <div class="box">
      <h3></h3>
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
    <script src="/theme/${theme}/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/theme/${theme}/js/ie10-viewport-bug-workaround.js"></script>
	<script type="text/javascript">
//弹框点击事件
$('.Popup .closespan').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  window.location.reload(); 
});
$('.ztspan').each(function(){
  var optionL = $(this).parents('tr').find('select option');
  for (var i = 0; i < optionL.length; i++) {
    if ($(this).text()==optionL.eq(i).attr('value')) {
      optionL.eq(i).attr('selected','selected');
    };
  };
})
var objecttext = $('#objectType .ztspan').text();
if(objecttext=='business' || objecttext=='activity'||objecttext=='payMethod'||objecttext=='withdrawMethod'){
  $('#objectIdp').css('display','none');
  //$('#objectIdp select').find('option').eq(0).attr('selected','selected');
}


  //保存配置
  var uuid = $('.table-striped').attr('data-uuid');
		$('.tools').on('click',function(){
              var tishititle = $(this).parent().parent().find('th span').text();
              var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
              if(typeof($inputText.attr("readonly"))=="undefined"){
                $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                 $(this).find('img').attr('title','保存配置');
              	$inputText.attr('readonly','readonly');
              	$inputText.removeClass('inputclass');
                var key = $inputText.attr('name');
                var value = $inputText.val();
                console.log(key,value);
                
                if($(this).parent().parent().hasClass('kzztr')){
                  if($inputText.attr('data-required')=='true' && $.trim(value)==''){
                    var jinggao = $(this).parents('tr').find('th input').val();
                    alert(jinggao+'不能为空！');
                  }else{
                    var classname = 'updateDataCode';
                    ajaxfunction(key,value,uuid,classname,tishititle);
                  }
                }else{
                  var classname = 'updateMode';
                  ajaxfunction(key,value,uuid,classname,tishititle);
                }

              }else{
                $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
                $(this).find('img').attr('title','修改配置');
              	$inputText.removeAttr('readonly');
              	$inputText.addClass('inputclass');
              	$inputText.focus();
              }
        });





        //状态选择
        $('.cxztxz').on('click',function(){
          var tishititle = $(this).parent().parent().find('th span').text();
          if($(this).parents('tr').find('td>select').hasClass('hide')){
            $(this).parents('tr').find('td>select').removeClass('hide');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
            $(this).find('img').attr('title','修改配置');
          }else{
            $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
            $(this).find('img').attr('title','保存配置');
            $(this).parents('tr').find('td>select').addClass('hide');
            var zxtext = $(this).parents('tr').find('select option:selected').text();
            var optionid = $(this).parents('tr').find('select option:selected').attr('value')
            $(this).parents('tr').find('.ztspan').text(zxtext);
            var key = $(this).parent().parent().find('select').attr('name');
            var classname = 'updateMode';
            ajaxfunction(key,optionid,uuid,classname,tishititle);
          }
        })


//提交
function ajaxfunction(key,value,uuid,kuozhan,tishititle){
var dataarry = {'dataDefineId':uuid};
dataarry[kuozhan] = key;
dataarry[key] = value;
 //var dataarry = [key,value];
  console.log(dataarry);
  $.ajax( {  
         type : "POST",  
         url : "/dataDefine/update.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) { 
            $('.Popup').removeClass('none');
            $('.Popup h3').text(tishititle+'修改成功！'); 
              
         },  
         error : function() {  
            $('.Popup').removeClass('none');
            $('.Popup h3').text(tishititle+'修改失败！');
         }  
        });  
};

</script>





  </body>
</html>

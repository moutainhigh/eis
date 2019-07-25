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
  </style>
  <body>
	<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>账号详情</span></h2>
			<div class="table-responsive">	
        <form action="" id='productTypeForm' method="post">
		<table class="table table-striped" data-uuid="${partner.uuid}">
		<colgroup width="800">
			<col width="100"/>
			<col width="*"/>
		</colgroup>
		<tr class="header tabheader">
			<td  align="left" colspan="3">${partner.uuid}#系统帐号详情</td>
		</tr>	
		<tr>
			<th style="min-width:450px;">登录名</th>
			<td>
				<c:out value="${partner.username}"/>
        
			</td>
			<td></td>

		</tr>
		<tr>
			<th><span>昵称</span></th>
			<td>
				<input type="text" name="nickName" id="nickName" readonly value="${partner.nickName}"/>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>状态</span></th>
			<td>
				<span class='ztspan'><spring:message code="Status.${partner.currentStatus}" /></span>
				<select name="currentStatus" id="currentStatus" class="hide">
					<c:forEach items="${statusCodeList}" var="i">
            <option value="${i.id}" <c:if test="${i.id == 120001}"> selected </c:if> ><spring:message code="Status.${i.id}" /></option>
          </c:forEach> 
					
				</select>
			</td>
			<td><span class='cxztxz' style='right:5px;cursor: pointer;'><img style='width:22px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>本用户关联的角色</span></th>
			<td>
        <span id="byhgljs">
  				<c:forEach var="r" items="${partner.relatedRoleList}">
  					<a class="a-updata" dataid='${r.roleId }' href="/partnerRole/get/${r.roleId }.shtml">
              ${r.roleId}.${r.roleName} </a>
  				  </c:forEach>
        </span>
        <div class="jiaose hide">
  				<c:forEach var="a" items="${partnerRoleList}">
  				<input type="checkbox" name="roleId"  value="${a.roleId }"><span style="display:inline-block;background-size: contain;background-repeat: no-repeat;">${a.roleId}.${a.roleName}</span>&nbsp;&nbsp;&nbsp;&nbsp;
  				</c:forEach>
        </div>			
			</td>
			<td><span class='jsgls save' style='right:5px;cursor: pointer;'><img style='width:20px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>密码</span></th>
			<td>
				<input type="password" name="userPassword" id="userPassword" readonly value="******"/>
				<span class="makePassword" name="common" style="display:none;cursor:pointer;text-align:center;height: 24px;line-height: 25px;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
					生成密码
				</span>
			</td>
			<td><span class='tools save' style='right:5px;cursor: pointer;'><img style='width:22px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>	
		<tr>
			<th><span>二级密码</span></th>
			<td>
				<input type="password"  name="authKey" id="authKey" readonly value="******"/>
				<span class="makePassword" name="common" style="display:none;cursor:pointer;text-align:center;height: 24px;line-height: 25px;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
					生成密码
				</span>
			</td>
			<td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>
		</tr>		
    <!-- 新增 -->
            <c:forEach var="a" items="${partner.userConfigMap}" varStatus="varStatus">
            <tr class="kzztr">
            
            <th style="text-align:left;min-width:450px;">
              <span style="border:none;background-color:transparent; text-align:left;width:100%;" title="${a.value.dataDescription}" >${a.value.dataName}</span>[${a.value.dataCode}]
            </th>
            <c:choose>
              <c:when test="${a.value.dataType=='boolean'}">
                <td class="tdradio" style='text-align:left;' data-readonly="${a.value.readonly}" data-required="${a.value.required}">
                  <span class="kz-updata">${a.value.dataValue}</span>
                  <span class="truefalse hide">
                  <input type="radio" name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" value="true" /><span style="margin-right:6px;">true</span>
                  <input type="radio" name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" value="false"/><span>false</span></span>
                </td>
              </c:when>
              <c:when test="${!empty a.value.dataValue}">
                <td class="kzinputtd" style='text-align:left;'>
                  <input name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" value="${a.value.dataValue}" readonly type="text"  data-readonly="${a.value.readonly}" data-required="${a.value.required}"/>
	                <c:if test="${a.value.dataCode == 'supplierLoginKey'}">
							<span class="makePassword" name="MD5" style="display:none;cursor:pointer;text-align:center;height: 24px;line-height: 25px;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
								生成密码
							</span>
					</c:if>
					<c:if test="${a.value.dataCode == 'supplierChargeKey'}">
						<span class="makePassword" name="3DES" style="display:none;cursor:pointer;text-align:center;height: 24px;line-height: 25px;width: 90px;background-color: gray;border-radius: 3px;color: wheat;">
							生成密码
						</span>
					</c:if>	
                </td>
              </c:when>
                           
              
              <c:otherwise>
                <td class="kzinputtd" style='text-align:left;' >
                  <input name="data.${a.value.dataCode}" id="data.${a.value.dataCode}" readonly  type="text"  value="" data-readonly="${a.value.readonly}" data-required="${a.value.required}"/>
                </td>
              </c:otherwise>
            </c:choose>
            
            </tr> 
            </c:forEach>
            <!-- 结束 -->





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
    <script src="/theme/${theme}/all/js/GenerationKey.js" type="text/javascript"></script>
    <script type="text/javascript">
//弹框点击事件
$('.Popup .closespan').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  
});
// 角色选中
   $('#byhgljs>a').each(function(){
  var index = $(this).index();
      for (var i = 0; i < $('.jiaose>input').length; i++) {
        if($(this).attr('dataid')==$('.jiaose>input').eq(i).attr('value')){
          $('.jiaose>input').eq(i).attr('checked','checked');
        }
      };
  })
     
// ture  false 选中
$('.kz-updata').each(function(){
  for (var i = 0; i < $(this).parent().find('span').length; i++) {
    if($(this).text()==$(this).parent().find('span').eq(i).text()){
      $(this).parent().find('input').eq(i).attr('checked','checked');
    }
  };
});
        //扩展数据
        $('.kzinputtd').each(function(){
        console.log($(this).find("input[type='text']").attr('data-required'));
        if($(this).find("input[type='text']").attr('data-readonly') == 'false'){
          $(this).parent().append("<td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>")
          
        }else{
          $(this).parent().append("<td></td>")
          
        }
        
      })
    $('.tdradio').each(function(){
      if($(this).attr('data-readonly')=='false'){
        $(this).parent().append("<td><span class='radiobtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='保存配置' src='/theme/basic/images/Preservation2.png'></span></td>")
        //$(this).addClass('required');
      }else{
        $(this).parent().append("<td></td>");
      }
    })




  //保存配置
  var uuid = $('.table-striped').attr('data-uuid');
		$('.tools').on('click',function(){
              var tishititle = $(this).parent().parent().find('th span').text();
              var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
              if(typeof($inputText.attr("readonly"))=="undefined"){
                $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
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
                var id = $inputText.attr("id");
              	if(id == "userPassword" || id == "authKey" 
              			|| id == "data.supplierLoginKey" || id == "data.supplierChargeKey"){
              		$inputText.next().css("display","none");
              		if(id == "userPassword" || id == "authKey"){
              			$inputText.attr("type","password");
              		}
              	}
              }else{
                $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
              	$inputText.removeAttr('readonly');
              	$inputText.addClass('inputclass');
              	var id = $inputText.attr("id");
              	if(id == "userPassword" || id == "authKey" 
              			|| id == "data.supplierLoginKey" || id == "data.supplierChargeKey"){
              		$inputText.next().css("display","inline-block");
              		if(id == "userPassword" || id == "authKey"){
              			$inputText.attr("type","text");
              		}
              	}
              	$inputText.focus();
              }
        });

        //true false选择
        $('.radiobtn').on('click',function(){
          var tishititle = $(this).parent().parent().find('th span').text();
        	var $ycspan = $(this).parent().parent().find($('.truefalse'));
        	if($ycspan.hasClass('hide')){
        		$ycspan.removeClass('hide');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
        	}else{
            $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
            $ycspan.addClass('hide');
        		var jiaosetext = '';
            var $input = $ycspan.find("input[type='radio']");
        		$input.each(function(){
        			if($(this).is(':checked')){
        				jiaosetext = $(this).next('span').text();
                $(this).parent().parent().find('span.kz-updata').text(jiaosetext);
        			}

        		})
            var classname = 'updateDataCode';
            var key = $(this).parent().parent().find("input[type='radio']").eq(0).attr('name');
            ajaxfunction(key,$(this).parent().parent().find('span.kz-updata').text(),uuid,classname,tishititle);
        	}
        });

        //角色关联选择  
        $('.jsgls').on('click',function(){
          var tishititle = $(this).parent().parent().find('th span').text();
          if($('.jiaose').hasClass('hide')){
            $('.jiaose').removeClass('hide');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
          }else{
            $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
            $('.jiaose').addClass('hide');
            var jiaosetext = '';
            $('.jiaose input').each(function(){
              if($(this).is(':checked')){
                $(this).addClass('checkedbox');
              }else{
                $(this).removeClass('checkedbox');
              }
            })
            var checketext = '';
            var jiaoseid = ''; 
            var juesehtmladd = '';
            var key = $(this).parent().parent().find('input').attr('name');
            if($('.checkedbox').length>0){
              $('.checkedbox').each(function(){
                var indextext = $(this).attr('value');//角色id
                checketext = $(this).next('span').text();//角色name
                var juesehtml = "<a class='a-updata' dataid='${r.roleId }' href='/partnerRole/get/"+ indextext+".shtml'>"+ checketext+"</a>"
                juesehtmladd +=juesehtml;
                $(this).parent().parent().find('#byhgljs').empty();
                $(this).parent().parent().find('#byhgljs').append(juesehtmladd);
                jiaoseid+=indextext+',';
              });
              //$('.a-updata').text(jiaosetext);
              //$(this).parent().parent().find('span.kz-updata').text(checketext);
              
            }else{
              $(this).parent().parent().find('#byhgljs').empty();
              $(this).parent().parent().find('#byhgljs').append(juesehtmladd);
             //jiaoseid = $(this).parents('tr').find('.a-updata').attr('dataid');
            }
            var classname = 'updateMode';
           ajaxfunction(key,jiaoseid,uuid,classname,tishititle);
            
          }
        });



        //状态选择
        $('.cxztxz').on('click',function(){
          var tishititle = $(this).parent().parent().find('th span').text();
          if($('#currentStatus').hasClass('hide')){
            $('#currentStatus').removeClass('hide');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
          }else{
            $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
            $('#currentStatus').addClass('hide');
            var zxtext = $('#currentStatus').find('option:selected').text();
            var optionid = $('#currentStatus').find('option:selected').attr('value')
            $('.ztspan').text(zxtext);
            var key = $(this).parent().parent().find('select').attr('name');
            var classname = 'updateMode';
            ajaxfunction(key,optionid,uuid,classname,tishititle);
          }
        })

		$(".makePassword").click(function(){
			var type = $(this).attr("name");
			var str = "";
			if(type == "common"){
				str = makeCommonKey(8);
			}else if(type == "MD5"){
				str = makeMD5Key(8);
			}else if(type == "3DES"){
				str = make3DesKey();
			}
			$(this).parent().find("input").val(str);
		});
//提交
function ajaxfunction(key,value,uuid,kuozhan,tishititle){
var dataarry = {'uuid':uuid};
dataarry[kuozhan] = key;
dataarry[key] = value;
 //var dataarry = [key,value];
  console.log(dataarry);
  $.ajax( {  
         type : "POST",  
         url : "/merchant/update.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) { 
            $('.Popup').removeClass('none');
            $('.Popup h3').text(tishititle+data.message.message);    
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

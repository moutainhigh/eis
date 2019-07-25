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

    <title>${systemName}-分成配置详情</title>

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
	<script src="/theme/${theme}/js/sweetalert.min.js"></script>
	<link href="/theme/${theme}/style/sweetalert.css" rel="stylesheet" type="text/css"/>
  </head>
  <style type="text/css">
  input[type='text']{
    width: 95%;
  }
  input{
    
    border:none;
    background-color: transparent;
  }
  .Tinput{
    border-radius: 5px;
    width: 80%;
    border:none;
    padding: 3px 5px;
    background-color: transparent;
    text-align: center;
  }
  td{
    padding: 7px 0;
  }
  .TinputBorder{
    border:1px solid #ddd;
  }
  .styleinput{
        border-radius: 5px;
    border: 1px solid #989898;
    padding: 1px 3px;
    background-color: #E6E6E6;
  }
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
    width: 300px;
    height: 182px;
    left: 50%;
    margin-left: -150px;
    margin-top: -140px;
    background-color: #fff;
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
    text-align: center;    margin-top: 18px;
  }
  .Popup .box p.tsp{
    font-size: 12px;
    color: red;
    margin: 0;
    text-align: left;
    margin-top: 5px;
  }
  .Popup .box p.tsp.none{
    display: none;
  }
  .Popup .box input[type="text"]{
    background-color: #eee;
    border:1px solid #ddd;
    border-radius: 5px;
    padding: 3px 5px;
  }
  .Popup .box input[type="button"]{
        width: 97px;
    padding: 9px;
    background-color: #708AA9;
    color: #fff;
    border-radius: 5px;
    margin-right: 15px;
    display: inline-block;
    font-size: 16px;
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
  select.none{
    display: none!important;
  }
  .jiaose.none{
    display: none;
  }
  .xzroles>span{
    margin-right: 14px;
  }
  .jiaose>span{
    margin-right: 14px;
  }
  .product.none{
  	display: none;
  }
  </style>
  <body>
   <%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
      <div class="row">
	  <%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
	  
	     
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span><spring:message code="分成配置详情" /></span></h2><!-- ConfigCategory.${ct} -->
			<div class="table-responsive">
				<div class="table-responsive">
         <table class="table table-striped" style="margin-top:15px;" data-shareConfigId="${shareConfig.shareConfigId}">
          <thead>
            <tr>
              <th>配置</th>
              <th>属性</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>分成配置ID</th>
              <td style="text-align:left;"><span>${shareConfig.shareConfigId}</span></td>
              <td></td>
            </tr>
            <tr>
              <th>分成名称</th>
                <td style="text-align:left;"><input type="text"  name="shareConfigName" id="shareConfigName"  value="${shareConfig.shareConfigName}" class="styleinput daempty" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                
                </td>
            </tr>
            <tr>
              <th>分成对象类型</th>
              <td style="text-align:left;">
                <input type="text" name="objectType" objectType="${shareConfig.objectType}" id="objectType" class="styleinput" readonly style="width:50%;" value="<spring:message code='ObjectType.${shareConfig.objectType}'/>"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${objectType}">
                    <option value="${a}">
                    	<spring:message code='ObjectType.${a}'/>
                    </option>
                  </c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              </td>
            </tr>
            <tr>
              <th>分成对象</th>
              <td style="text-align:left;">
                <input type="text" name="shareUuid" id="shareUuid" class="styleinput" readonly style="width:50%;" value="${shareConfig.operate.shareUser}"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <%-- <c:forEach var="a" items="${subPartnerList}">
                    <option value="${a.uuid}">${a.username}</option>
                  </c:forEach> --%>
                  <c:forEach var="a" items="${pay}">
						<option name="pay" value="${a.uuid}" style="display:none;">
							${a.username}
						</option>
				  </c:forEach>
				 <c:forEach var="a" items="${product}">
										<option name="product" value="${a.uuid}" style="display:none;">
											${a.username}
										</option>
									</c:forEach>
									<c:forEach var="a" items="${channel}">
										<option name="channel" value="${a.payMethodId}" style="display:none;">
											${a.name}
										</option>
									</c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              
              </td>
            </tr>
            
            <tr class="product">
              <th>分成产品</th>
              <td style="text-align:left;">
                <input type="text" name="objectId" id="objectId" class="styleinput" readonly style="width:50%;" value="${shareConfig.operate.productName}"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${productList}">
                    <option value="${a.productId}">${a.productName}</option>
                  </c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              
              </td>
            </tr>
            <tr>
              <th>分成比例</th>
                <td style="text-align:left;"><input type="text" name="sharePercent" id="sharePercent"  value="<fmt:formatNumber  value='${shareConfig.sharePercent*100}' />" class="styleinput daempty numLimit" readonly/>%</td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
                
                </td>
            </tr>
            
       		<tr>
              <th>支付卡类型</th>
              <td style="text-align:left;">
                <input type="text" name="payCardType" id="payCardType" class="styleinput" readonly style="width:50%;" value="<spring:message code='PayCardType.${shareConfig.payCardType}'/>"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
					<option value="UN">
						<spring:message code='PayCardType.UN'/>
					</option>
					<option value="DE">
						<spring:message code='PayCardType.DE'/>
					</option>
					<option value="CR">
						<spring:message code='PayCardType.CR'/>
					</option>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              
              </td>
            </tr>
            <tr>
              <th>区间的结算起始金额</th>
                <td style="text-align:left;"><input type="text" name="beginMoney" id="beginMoney" value="${shareConfig.beginMoney}" class="styleinput daempty accountMoney" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
               
                </td>
            </tr>
            <tr>
              <th>区间的结算结束金额</th>
                <td style="text-align:left;"><input type="text" name="endMoney" id="endMoney" value="<fmt:formatNumber value='${shareConfig.endMoney}' pattern='0.0' />" class="styleinput daempty accountMoney" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
               
                </td>
            </tr>
            <tr>
              <th data-name="moneyDirect">资金方向</th>
              <td style="text-align:left;">
                <div class="xzroles">
                    <span dataId="${shareConfig.moneyDirect}">
                    	<c:if test="${shareConfig.moneyDirect=='plus'}" >增加资金</c:if>
                    	<c:if test="${shareConfig.moneyDirect=='minus'}" >减少资金</c:if>
                    </span>
                </div>
                <div class="jiaose none">
                    <input type="radio" value="plus" name="moneyDirect" /><span>增加资金</span>
                    <input type="radio" value="minus" name="moneyDirect"/><span>减少资金</span>
                </div>
              </td>
              <td><span class='jsgls' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              </td>
            </tr>
     		   <tr>
              <th data-name="chargeType">付费类型用户</th>
              <td style="text-align:left;">
                <div class="xzroles">
                <span dataId="${shareConfig.chargeType}">
                	<c:if test="${shareConfig.chargeType==0}" >预付费</c:if>
                    <c:if test="${shareConfig.chargeType==1}" >后付费</c:if>
                </span>
                </div>
                <div class="jiaose none">
                    <input type="radio" value="0" name="chargeType" /><span>预付费</span>
                    <input type="radio" value="1" name="chargeType"/><span>后付费</span>
                </div>
              </td>
              <td><span class='jsgls' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              </td>
            </tr>
             <tr>
              <th>最大重试次数</th>
                <td style="text-align:left;"><input type="text" name="maxRetry" id="maxRetry" value="${shareConfig.maxRetry}" class="styleinput daempty accountMoney" readonly/></td>

                <td><span class='tools' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
               
                </td>
            </tr>
            <tr>
              <th>当前状态</th>
              <td style="text-align:left;">
                <input type="text" name="currentStatus" id="currentStatus" class="styleinput"  readonly style="width:50%;" value="<spring:message code='Status.${shareConfig.currentStatus}'/>"/>
                <select id="leixing" class="none" style="width:44.5%;padding: 1px;border-radius: 5px;">
                  <c:forEach var="a" items="${statusList}">
                    <option value="${a}"><spring:message code="Status.${a}"/></option>
                  </c:forEach>
                </select>
              </td>
              <td><span class='selectbtn' style='right:5px;cursor: pointer;'><img style='width:22px;' title='修改配置' src='/theme/basic/images/Preservation2.png'></span>
              
              </td>
            </tr>
      
          </tbody>
        </table>  
      </div>


			</div>
        </div>
        
       
        
      </div>
    </div>
<!-- 弹窗 -->
    <div class="Popup outpopup none">
      <div class="box">
      <h3></h3>
      <p><input type="button" class="btnclose" value="确 认"></p>
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
	 <script> 
	 
	//执行展示分成对象代码
		function changeObjectInfo(){
			var objectType = $("#objectType").attr("objectType");
			$("#shareUuid").next().find("option").css("display","none");
			$("#shareUuid").next().find("option[name='"+objectType+"']").css("display","block");
			$("#shareUuid").find("option").attr("selected",false);
			//默认选中切换后得第一个
			$("#shareUuid").find("option[name='"+objectType+"']").eq(0).attr("selected",true);	
		}
		changeObjectInfo();
// 产品是否出现
if($('#objectType').val()=='pay' || $('#objectType').val()=='channel'){
	$('.product').addClass('none');
}else{
	$('.product').removeClass('none');
}
  
//弹框点击事件
$('.Popup .btnclose').on('click',function(){
  $(this).parent().parent().parent().addClass('none');
  
});
var payMethodId = $('.table').attr('data-shareConfigId');
    	$('.tools').on('click',function(){
              var $inputText = $(this).parent().parent().find('td').eq(0).find('input');
              if(typeof($inputText.attr("readonly"))=="undefined"){
            	//验证输入框合法性
            	//先判断空
            	if($inputText.hasClass('daempty')){
            		if($inputText.val().trim() == ""){
            			var alertV = $inputText.parent().prev().text(); 
            			alert(alertV+"不能为空!");
            			return;
            		}
            	}
            	//验证数值合法性
            	if($inputText.hasClass('numLimit')){
            		var reg = /^((\d|[123456789]\d)(\.\d+)?|100)$/;//0到100
            		if(!reg.test($inputText.val().trim())){
            			var alertV = $inputText.parent().prev().text(); 
            			alert(alertV+"不能在0-100范围外！");
            			return;
            		}
            	}
            	
            	//下面判断不能小于0得情况
            	if($inputText.hasClass('accountMoney')){
            		if($inputText.val() < 0){
            			var alertV = $inputText.parent().prev().text();
            			alert(alertV+"不能小于0！");
            			return;
            		}
            	}
            	//下面特殊判断 分成结束金额不能低于起始金额
            	if($inputText.attr("id") == "endMoney"){
            		var beginMoney = $("#beginMoney").val().trim();
            		var endMoney = $("#endMoney").val().trim();
            		if(endMoney <= beginMoney){
            			alert("结算结束金额不能小于等于结算起始金额！");
            			return;
            		}
            	}
            	
                $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                $(this).find('img').attr('title','修改配置');
                $inputText.removeClass('inputclass');
                var key = $inputText.attr('name');
                $inputText.attr('readonly','readonly');

                console.log(key,value);
                //var payMethodId = $inputText.attr('payMethodId');
                //$inputText.removeClass('styleinput');
                    if($(this).parents('tr').hasClass('tabody')){
                      
                      var tishititle = $(this).parent().parent().find('th').text();
                      var value = $inputText.val();
                      var classname = 'updateDataCode';
                      ajaxfunction(key,value,payMethodId,classname,tishititle);
                    }else{
                      var tishititle = $(this).parent().parent().find('th').text();
                      var value = $inputText.val();
                      var classname = 'updateMode';
                      ajaxfunction(key,value,payMethodId,classname,tishititle);
                    }
                     
               
                }
             else{
                $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
                $(this).find('img').attr('title','保存配置');
                $inputText.removeAttr('readonly');
                $inputText.addClass('inputclass');
                //$inputText.addClass('styleinput');
                $inputText.focus();
                $(this).parent().parent().find('td').eq(0).find('select').css('display','block');
              }


        });
  $('.selectbtn').on('click',function(){
              var $input = $(this).parent().parent().find('td').eq(0).find('input');
              var key = $input.attr('name');
              var $select = $(this).parent().parent().find('td').eq(0).find('select');
              if($select.hasClass('none')){
                  $select.removeClass('none');
                  $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
                  $(this).find('img').attr('title','保存配置');
                  $(this).parent().parent().find('td').eq(0).find('select').css('display','inline-block');
                  //执行切换select值
                  if($input.attr("id") == "shareUuid"){
                	  changeObjectInfo();
                  }
                  
                }else{
                  $select.addClass('none');
                  $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
                  $(this).find('img').attr('title','修改配置');
                  $(this).parent().parent().find('td').eq(0).find('select').css('display','none');
                  
                  //var payMethodId = $input.attr('payMethodId');
                     var tishititle = $(this).parent().parent().find('th').text();
                      var value1 = $(this).parent().parent().find('td').eq(0).find('select option:selected').text();
                      $input.val(value1.trim());
                      var value = $(this).parent().parent().find('td').eq(0).find('select option:selected').attr('value');
                      var classname = 'updateMode';
                      ajaxfunction(key,value,payMethodId,classname,tishititle);

              }


        });

$('.xzroles>span').each(function(){
  var input = $(this).parents('td').find($("input[type='radio']"));
      for (var i = 0; i < input.length; i++) {
        if($(this).attr('dataId')==input.eq(i).attr('value')){
          input.eq(i).attr('checked','checked');
        }
      };
  })
$('.jsgls').on('click',function(){
          var tishititle = $(this).parent().parent().find('th').text();
          var jiaose = $(this).parents('tr').find('.jiaose');
          var jiaoseinput = jiaose.find('input');
          if(jiaose.hasClass('none')){
            jiaose.removeClass('none');
            $(this).find('img').attr('src','/theme/basic/images/Preservation.png');
          }else{
            
            var jiaosetext = '';
            jiaoseinput.each(function(){
              if($(this).is(':checked')){
                $(this).addClass('checkedbox');
              }else{
                $(this).removeClass('checkedbox');
              }
            })
            var checketext = '';
            var value = ''; 
            var juesehtmladd = '';
            var key = $(this).parent().parent().find('th').attr('data-name');
            var checkedbox = $(this).parents('tr').find($('.checkedbox'));
            if(checkedbox.length>0){
              $(this).find('img').attr('src','/theme/basic/images/Preservation2.png');
              jiaose.addClass('none');
              checkedbox.each(function(){
                var indextext = $(this).attr('value');//角色id
                checketext = $(this).next('span').text();//角色name
                var juesehtml = "<span>"+checketext+"</span>"
                juesehtmladd +=juesehtml;
                $(this).parent().parent().find('.xzroles').empty();
                $(this).parent().parent().find('.xzroles').append(juesehtmladd);
                value+=indextext+',';
              });
              var classname = 'updateMode';
               ajaxfunction(key,value,payMethodId,classname,tishititle);
            }else{
              alert(tishititle+'不能为空！')
            }
              //$('.a-updata').text(jiaosetext);
              //$(this).parent().parent().find('span.kz-updata').text(checketext);
            
            
          }
        });

// 关联角色不能为空




//弹框点击事件
 $('.Popup .closespan').on('click',function(){
    $(this).parent().parent().parent().addClass('none');
  });




//提交
function ajaxfunction(key,value,payMethodId,classname,tishititle){
  var dataarry = {'shareConfigId':payMethodId};
  dataarry[classname] = key;
  dataarry[key] = value;
  console.log(dataarry);
  $.ajax( {  
         type : "POST",  
         url : "/shareConfig/update.json",  
         dataType:"json",
         data:dataarry, 
         success : function(data) {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'修改成功！') ;
            if($('#objectType').val()=='pay' || $('#objectType').val()=='channel'){
                  $('.product').addClass('none');
            }else{
                  $('.product').removeClass('none');
            }   
         },  
         error : function() {  
            $('.outpopup').removeClass('none');
            $('.outpopup h3').text(tishititle+'修改失败！')   
         }  
        });  
}


  </script>
  </body>
</html>

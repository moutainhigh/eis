<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 





<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /><title>
	${systemName}
</title>
<link href="/theme/ec1/style/com.css" rel="stylesheet" />
<link href="/theme/ec1/style/layout.css" rel="stylesheet" />
<link href="/theme/ec1/style/web.css" rel="stylesheet" />
<link href="/theme/ec1/style/Account.css" rel="stylesheet" />
    <style>
        .tbOrder {
            border-collapse: collapse;
            border: 1px solid #E5E5E5;
            width: 100%;
        }

            .tbOrder th {
                text-align: center;
                color: #999;
                font-size: 12px;
                font-family: Arial,"宋体";
                height: 35px;
                line-height: 35px;
                background-color: #F5F5F5;
                font-weight: normal;
            }

            .tbOrder td {
                height: 80px;
                text-align: center;
                position: relative;
                color: #aaa;
                border-right: 1px solid #E5E5E5;
            }

                .tbOrder td a {
                    color: #333;
                    cursor: pointer;
                }

                    .tbOrder td a:hover {
                        color: #e4393c;
                    }

                .tbOrder td img {
                    position: absolute;
                    left: 8px;
                    top: 11px;
                }

        .Total {
            font-size: 14px;
            color: #666;
            font-family: Arial,"宋体";
            text-align: right;
        }

        .Total_Price {
            color: #e4393c;
            font-size: 20px;
            font-weight: 700;
        }

        a.btn-again {
            background-color: #f5f5f5;
            border: 1px solid #ddd;
            border-radius: 2px;
            color: #333;
            display: block;
            text-align: center;
            width: 85px;
            height: 27px;
            line-height: 27px;
            margin: 0 auto;
        }

            a.btn-again:hover {
                border-color: #e4393c;
                color: #e4393c;
            }

        .hr {
            width: 80%;
            height: 0;
            display: block;
            border-top: 1px solid #E5E5E5;
            margin: 5px auto;
        }
    </style>
</head>
<body>
    <form name="form1" method="post" action="/user.json" id="form1">


<script type="text/javascript">
//<![CDATA[
var theForm = document.forms['form1'];
if (!theForm) {
    theForm = document.form1;
}
function __doPostBack(eventTarget, eventArgument) {
    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {
        theForm.__EVENTTARGET.value = eventTarget;
        theForm.__EVENTARGUMENT.value = eventArgument;
        theForm.submit();
    }
}
//]]>
</script>



        
        <%@include file="/WEB-INF/jsp/include/headcenter.jsp" %>

        <div class="AccountTop">
            <div class="Wrap1000">
                <div class="AccountTop_Left">您好，<span id="lblLoginAccount">${frontUser.username}</span></div>
                <ul class="AccountTop_Nav">
                    <li><a href="/">首页</a></li>
                    <li><a href="/content/user/pcenter.shtml">地址管理</a></li>
                    <li class="Cur"><a href="javascrpt:;">我的订单</a></li>
                </ul>
            </div>
        </div>
        <div class="SpaceBlockHorizontal20"></div>
        <div class="Wrap1000">
            <div class="Account">
                <div class="Account_Title">我的订单</div>
                <div>
                    <div>
	<table cellspacing="0" rules="all" border="1" id="gvList" style="width:100%;border-collapse:collapse;">
		
         
         	
		<tr>
			<th scope="col">&nbsp;</th>
		</tr><tr>
			<td>
                                    <table class="tbOrder">
                                   		 
                                    	
                                        <tr>
                                        
                                            <th style="text-align: left; padding-left: 10px;">
                                                <span id="gvList_ctl02_lblTime">${frontUser.createTime}</span></th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                            <th colspan="2">订单号：<span id="gvList_ctl02_lblBillNo">P16062200328693D</span></th>
                                            <th></th>
                                        </tr>
                                        
                                        <tr>
                                        	
                                        	
                                            <td style="width: 270px; text-align: left; padding-left: 8px; border-right: none;">
                                                <a id="gvList_ctl02_hyTitle" href="/content/node/product/pdetail.shtml">雅芳小黑裙香体乳150g_35元</a>
                                            </td>
                                           
                                            <td style="width: 80px;">x
                                                <span id="gvList_ctl02_lblNum">${it.value.count}</span></td>
                                            <td style="width: 80px;">优惠：0.00</td>
                                            <td style="width: 100px;">总额：<span id="gvList_ctl02_lblPrice">35.00</span><span class="hr"></span>
                                                财付通支付
                                            </td>
                                            <td style="width: 100px;">
                                                <span id="gvList_ctl02_lblPerson"></span>
                                            </td>
                                            <td style="width: 100px;">
                                                <span id="gvList_ctl02_lblStatus">已完成</span></span><span class="hr"></span>
                                                <a id="gvList_ctl02_hyBillDetail" href="/content/node/product/detail.shtml">订单详情</a>
                                            </td>
                                            <td style="width: 130px;">
                                                <a id="gvList_ctl02_hyOrderAgain" class="btn-again" href="ProductDetail.aspx?BillNo=P16062200328693D">立即购买</a>
                                            </td>
                                        </tr>
                                        
                                    </table>
                                    <div class="SpaceBlockHorizontal"></div>
                                </td>
		</tr>
		
	</table>
</div>
 
                </div>
            </div>
            
<%@include file="/WEB-INF/jsp/include/footer.jsp" %>

        </div>
    </form>
</body>
</html>

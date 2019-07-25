<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>
<style>
#serverChooseDialog {}
.dialogServerLi {display:block; float:left; }
.dialogServerLi a {background:url(/style/fire.png) no-repeat 0px -95px; display:block; text-align:center; width:180px; margin-right:10px; margin-top:5px; height: 83px; font-size:12px; padding-top:10px; color:#ffffff; }
.dialogServerLi .hot {background:url(/style/fire.png)  no-repeat 0px 0px; display:block; text-align:center; width:180px; margin-right:10px; margin-top:5px; height: 83px; font-size:12px; padding-top:10px; color:#ffffff; }
.dialogServerLi .normal {background:url(/style/fire.png)  no-repeat 0px -95px; display:block; text-align:center; width:180px; margin-right:10px; margin-top:5px; height: 83px; font-size:12px; padding-top:10px; color:#333333; }
.dialogServerLi .idle {background:url(/style/fire.png)  no-repeat 0px -185px; display:block; text-align:center; width:180px; margin-right:10px; margin-top:5px; height: 83px; font-size:12px; padding-top:10px; color:#ffffff; }
.dialogServerLi .disable {background:url(/style/fire.png)  no-repeat 0px -285px; display:block; text-align:center; width:180px; margin-right:10px; margin-top:5px; height: 83px; font-size:12px; padding-top:10px; color:#ffffff; }
</style>
<div  id="serverChooseDialog" name="serverChooseDialog" style="display:none; " title="请选择服务器">	
	
</div>


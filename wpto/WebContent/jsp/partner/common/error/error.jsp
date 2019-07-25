<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	 <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<title>ERROR</title>
</head>
<style>
	body{
		background-color: #62d5ff;
		font-family: 'Microsoft YaHei';
	}
	body >div{
		max-width:510px;
		width: 100%;
		height: 240px;
		position: absolute;top: 50%;left: 50%;
		margin-left: -255px;
		margin-top:-190px;
	}
	body >div img{
		width: 100%;
	}
	body >div >div{
		color: #fff;
		position: relative;
		padding-left: 37px
	}
	body >div >div:before{
		display: inline-block;
		content: "";
		position: absolute;
		top: 0;left: 0;
		height: 100%;
		width: 30px;
		background: -webkit-linear-gradient(left, rgba(255,255,255,0) , rgba(255,255,255,1)); /* Safari 5.1 - 6.0 */
	    background: -o-linear-gradient(right, rgba(255,255,255,1), rgba(255,255,255,0)); /* Opera 11.1 - 12.0 */
	    background: -moz-linear-gradient(right, rgba(255,255,255,1), rgba(255,255,255,0)); /* Firefox 3.6 - 15 */
	    background: linear-gradient(to right,  rgba(255,255,255,0) , rgba(255,255,255,1)); /* 标准的语法 */
	}
	h2{
		font-size: 50px;
		margin-bottom: 0;
	}
	h4{
		padding-left: 40px;
		line-height: 28px;
	    margin-top: 20px;
	    color: #0077A2;
	    font-weight: normal;
	    margin:0;
	    font-weight: bold;
	    font-size: 15px;
	}
	h3{
		text-transform: uppercase;
		margin-top: 0;
		font-weight: normal;
		font-size: 24px;
		letter-spacing: 4px;
		margin-bottom: 8px;
		padding-left: 3px;

	}
	h1{
		margin-top: 0;
		font-size: 48px;
		letter-spacing: 2px;
		margin-bottom: 10px;
	}
	@media(max-width: 678px){
		body>div{
			width: 100%;
			left: 0;margin-left: 0;
			margin-top: -130px;
		}
		body> div img{
			width: 100%;
		}
		h4{
			padding-left: 38px;
			font-size: 12px;
		}
		h2 {
		    font-size: 30px;
		    margin-bottom: 0;
		}
		h1 {
		    margin-top: 10px!important;
		    font-size: 26px;
		    letter-spacing: 2px;
		    margin-bottom: 10px;
		}
		h3{
			font-size: 14px;
		}
		
	}
</style>
<body>  
	<div>
		<div>
			<h2>LOOK! </h2>
			<h3>page information prompt</h3>
			<h1>信息提示码：<span style="color:#D66045;">500 </span></h1>
		</div>
		<h4>抱歉！服务器遇到错误，无法完成请求！</h4>
	</div>
</body>
</html>
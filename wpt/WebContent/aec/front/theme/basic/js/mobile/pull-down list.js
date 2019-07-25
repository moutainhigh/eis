// JavaScript Document
function mouseOver(over){
	over.className="yiShang";
	over.style.cursor="default";
}
//鼠标移开
function mouseOut(out){
	out.className="";
}
//选中
function selectInput(selected){
	document.getElementById("ulId").style.display="none";  //使列表不显示
	document.getElementById("shuRu2").value = selected.innerHTML;	//设置输入框为选中的值  
}
var xmlhttp;
	function doAjax(){
		var projName =  document.getElementById("projectName").value;
		//alert("当前工程名:" + projName );
		var shuRuNeiRong = document.getElementById("shuRu2").value;
		xmlhttp = new XMLHttpRequest();
		//设置回调
		xmlhttp.onreadystatechange = function(){
			if(xmlhttp.status==200 && xmlhttp.readyState==4){
				//alert(xmlhttp.responseText);
				document.getElementById("ulId").innerHTML = xmlhttp.responseText;
			}
		}
		xmlhttp.open("post", projName+"/search/selects.shtml", true);
		xmlhttp.setRequestHeader("Content-type",
		"application/x-www-form-urlencoded");
		xmlhttp.send("searchValue="+shuRuNeiRong);
	}
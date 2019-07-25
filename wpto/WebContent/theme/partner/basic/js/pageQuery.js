function query(){
	var url = document.location.href.split('?')[0];
	url= url + $("#queryForm").serialize();  		
}
function query_num() {
	var formurl = $("#queryForm").serialize(); 
	var url = document.location.href.split('?')[0];
	console.log(formurl);
	var arry = [];
	arry = formurl.split('&');
	console.log(arry);
	var arry2 = [];
	arry2 = arry[2].split('=');
	console.log(arry2);
	if (arry2[1]=='') {
		arry.pop();
	}
	var urlcx = '';
	for (var i = 0; i < arry.length-1; i++) {
		urlcx += arry[i]+'&';
	}
	urlcx += arry[arry.length-1]
	console.log(urlcx+' / '+url)
	//url= '?'+url + urlcx;  
	window.location.href= url+'?'+urlcx;
}

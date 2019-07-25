		<script type="text/javascript">
			var dom=document.getElementsByTagName('form')[0];
			dom.onsubmit=function(){
			//return false;
			}
			var xhr=new XMLHttpRequest();
			var fd;
			var des=document.getElementById('load');
			var num=document.getElementById('upimg');
			var file;
			const LENGTH=10*1024*1024;
			var start;
			var end;
			var blob;
			var pecent;
			var filename;
		//var pending;
		//var clock;
		function upfile(){
			start=0;
			end=LENGTH+start;
			//pending=false;
			
			file=document.getElementsByName('mof')[0].files[0];
			//filename = file.name;
			console.log(document.getElementsByName('mof')[0].files[0]);
			if(!file){
			alert('请选择文件');
			return;
			}
			//clock=setInterval('up()',1000);
			up();
		
		}
		
		function up(){
				/*
				if(pending){
				return;
				}
				*/
				if(start<file.size){
				xhr.open('POST','/user/bigUpload.json',true);
				//xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
				xhr.onreadystatechange=function(){
					if(this.readyState==4){
					if(this.status>=200&&this.status<300){
					if(this.responseText.indexOf('failed') >= 0){
					//alert(this.responseText);
					alert('文件发送失败，请重新发送');
					des.style.width='0%';
					//num.innerHTML='';
					//clearInterval(clock);
					}else{
					//alert(this.responseText)
					// pending=false;
					start=end;
					end=start+LENGTH;
					setTimeout('up()',1000);
					}
				
				}
			}
		}
		xhr.upload.onprogress=function(ev){
			if(ev.lengthComputable){
			pecent=100*(ev.loaded+start)/file.size;
			if(pecent>100){
			pecent=100;
			}
			//num.innerHTML=parseInt(pecent)+'%';
			des.style.width=pecent+'%';
			des.innerHTML = parseInt(pecent)+'%'
			}
		}
		　　　　　　　
		　　　　　　　//分割文件核心部分slice
		blob=file.slice(start,end);
		fd=new FormData();
		fd.append('mof',blob);
		fd.append('test',file.name);
		//console.log(fd);
		//pending=true;
		xhr.send(fd);
		}else{
		//clearInterval(clock);
		}
		}
		
		function change(){
		des.style.width='0%';
		}
		
		</script>



        														
																		<style>
																		#test{
																		width: 200px;
																		height: 100px;
																		border: 1px solid green;
																		display: none;
																		}
																		#img{
																		width: 50px;
																		height: 50px;
																		display: none;
																		}
																		#upimg{
																		text-align: center;
																		font: 8px/10px '微软雅黑','黑体',sans-serif;
																		width: 300px;
																		height: 10px;
																		border: 1px solid green;
																		}
																		#load{
																		width: 0%;
																		height: 100%;
																		background: green;
																		text-align: center;
																		}
																		</style>
																		<div id="upimg">
																			<div id="load"></div>
																		</div>
																		<input type="file" name="mof" multiple="multiple"/>
																		<input type="button" value="uploadfile" onclick="upfile();" />


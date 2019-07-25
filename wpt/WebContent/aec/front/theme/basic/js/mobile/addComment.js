var arrFiles=[];
var MaxImgLength;
$(function(){
	MaxImgLength=$("#gallerySize").val();
	$("#upload").submit(function(e){
		e.preventDefault();
		$("#box_input").children().last().remove();
		$("#box_input").children().last().css("display","inline-block");
		$(this).ajaxSubmit({
			type:"POST",
			url:"/order/submitComment.json",
				dataType:"json",
				data:$("#upload").serialize(),				
				success:function (data) {
					alert(data.message.message);			
					//location.href="/";
					},
					error:function (data) {
						alert("系统繁忙,请稍后再试");
						return false;
					}
					
		});
	})
})
var currentImgLength=0;
function dropHandler(files){
  if(currentImgLength>=MaxImgLength){
	  alert("最多可评论"+MaxImgLength+"张图片！");
		  return false;
	} 
  $('#pageContent').className='';
  for(var f=0;f<files.length;f++){
	 arrFiles.push(files[f]);
   }
   $('#pageContent').html("");
   currentImgLength=0;
   for(var i=0; i<arrFiles.length;i++){
     arrFiles[i].index=parseInt(Math.random() * 9000000);
	 viewfile(arrFiles[i]);
	 currentImgLength++;
   }
   var html1='<a class="btn_addPic" href="javascript:void(0);"><span>添加图片</span> <input type="file"  class="filePrew"  name="productGallery'+currentImgLength+'" onchange="dropHandler(this.files)" accept="image/gif,image/jpeg,image/png,image/bmp" class="blue" /></a>';
	$("#box_input a").css("display","none");
	$("#box_input").append(html1);    
} 
function viewfile(file){
	if(window.webkitURL){  
		var imgsrc=window.webkitURL.createObjectURL(file)
		createView(file,imgsrc)  
	}
	else if(window.URL){  
		var imgsrc=window.URL.createObjectURL(file) 
		createView(file,imgsrc)
	}
	else{ 
		var reader = new FileReader();
	　 	reader.onload = function(e) {	 
			var imgsrc=e.target.result;     
			createView(file,imgsrc)
　 		}
		reader.readAsDataURL(file);
	}
}
 //创建预览dom
 function createView(file,imgsrc){
    var html = "";
    html += '<li id="imgViewList'+file.index+'">';
    html += '<div class="img_and_info">';
    html += '<img  border="0" src="'+imgsrc+'">';
    html += '</div>';
    html += '<span onclick="del(\'imgViewList'+file.index+'\')" class="upload_delete" title="删除">删除</span>';             
    html += '</li>';
	document.getElementById("pageContent").innerHTML+=html;    
  }
  Array.prototype.del=function(n) {　//n表示第几项，从0开始算起。
　    if(n<0){　//如果n<0，则不进行任何操作。
　　    return this;}
　  else{
　　   return this.slice(0,n).concat(this.slice(n+1,this.length));
      }
}
 //删除预览
  function del(obj){
    var n=parseInt(obj.substring(11));//获取数组索引
    for(var s=0;s<arrFiles.length; s++){
     if(arrFiles[s].index==n){
	   $("#box_input").children()[s].remove();
	    currentImgLength--;
		arrFiles.splice(s,1);
		alert("删除过后还剩"+arrFiles.length+"个");
      }
     }
	 for(var i=0;i<currentImgLength+2;i++){
		 $("#box_input input").each(function(i,ele){
			 $(this).attr("name","productGallery"+i);
		 })
	 }
      removeElement(document.getElementById(obj+""));
    }
	function removeElement(_element){
         var _parentElement = _element.parentNode;
         if(_parentElement){
                _parentElement.removeChild(_element);
         }
    }
var arrFiles=[];
var MaxImgLength=parseInt($("#maxImgLength").val());
var currentImgLength=0;
function uploadApprise(){
        $("#box_input").children().last().remove();
		$("#box_input").children().last().css("display","block");
		//$('input[type="file"]').attr('name','productGallery');
		var data=new FormData($('#upload')[0]);
		$.ajax({
            type:"POST",
            url: '/order/submitComment.json',
            data:data,
            dataType:'json',
			async: false,  
            cache: false,  
            contentType: false,  
            processData: false,  
            success:function (data) {
               alert(data.message.message);
               //window.location.href='/content/center/myComments.shtml';
			
            },
            error:function (data) {

                alert("系统繁忙,请稍后再试");
                return false;
            }
        });
		
		 
			
}
 // function videodom(){
 // 	$('#box_input_subscribeGallery').prepend('<a class="btn_addPic videodom" href="javascript:void(0);"><span>添加视频</span> <input type="file"  class="filePrew" id="subscribeGallery0"  name="subscribeGallery0" class="blue" data-name="subscribeGallery" multiple="multiple"/></a>');
 // }
 // videodom();
 $(document).on('change','.filePrew',function(){
 	var files = $(this)[0].files;
 	var columnName = $(this).attr('data-name');
 	var filepath = $(this).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart, filepath.length).toUpperCase();//获取上传文件类型
    console.log(files);
    var leixing = '';
    if (ext =='.MP3'|| ext =='.MP4' || ext =='.MPG' || ext =='.wmv') {
		leixing='video'
    }else{
    	leixing='img'
    }



   if(currentImgLength>=MaxImgLength){
		alert("最多可评论"+MaxImgLength+"张图片！");
		return false;
   } 
  $('#pageContent_'+columnName).className='';
  /*for(var f=0;f<files.length;f++){
	 arrFiles.push(files[f]);
   }*/
   ///$('#pageContent_'+columnName).html("");
   //currentImgLength=0;
   /*for(var i=0; i<arrFiles.length;i++){
     arrFiles[i].index=parseInt(Math.random() * 9000000);
	 viewfile(arrFiles[i], columnName);
	 currentImgLength++;
   }*/
   // if(columnName != "productGallery"){
		
		if ($(this).parents('a').hasClass('videodom')) {
			return;
		}else{
			var array1=[];
			for(var f=0;f<files.length;f++){
				array1.push(files[f]);
			}
			for(var i=0; i<array1.length;i++){
				array1[i].index=parseInt(Math.random() * 9000000);
				viewfile(array1[i], columnName, array1,leixing);
				currentImgLength++;
			}
			console.log(array1);
			console.log(currentImgLength);
			if(currentImgLength >= 1){
			var html1='<a class="btn_addPic inputa" href="javascript:void(0);"><span>晒照片</span> <input type="file"  class="filePrew" id="'+columnName+currentImgLength+'"  name="'+columnName+currentImgLength+'" class="blue" data-name="'+columnName+'" /></a>';
			$("#box_input_"+columnName+" a.inputa").css("display","none");
			$("#box_input_"+columnName+" a.inputa").addClass('videodom');
			$("#box_input_"+columnName).append(html1);
			//$("#box_input_"+columnName).find("input[name="+columnName+1+"]").attr("type","file");
		}
		};
		
   // }
  //  else{
		// for(var f=0;f<files.length;f++){
		// 	arrFiles.push(files[f]);
		// }
		// for(var i=0; i<arrFiles.length;i++){
		// 	arrFiles[i].index=parseInt(Math.random() * 9000000);
		// 	viewfile(arrFiles[i], columnName, arrFiles);
		// 	currentImgLength++;
		// }
		// var html2='<a class="btn_addPic" href="javascript:void(0);"><span>添加44图片</span> <input type="file"  class="filePrew" id="'+columnName+currentImgLength+'" name="productGallery'+currentImgLength+'" class="blue" data-name="'+columnName+'" multiple="multiple"/></a>';
		// $("#box_input_"+columnName+" a").css("display","none");
		// $("#box_input_"+columnName).append(html2);
  //  }
	    
   
    
} )
function viewfile(file, a, array1,leixing){
	if(window.URL){
		var imgsrc=window.URL.createObjectURL(file)
		createView(file,imgsrc, a, array1,leixing)
	} 
	else if (window.URL){
		var imgsrc=window.URL.createObjectURL(file)
		createView(file,imgsrc, a, array1,leixing)
	}
	else{
		var reader = new FileReader();
	　 	reader.onload = function(e) {
			var imgsrc=e.target.result;
			createView(file,imgsrc, a, array1,leixing)
		}
		reader.readAsDataURL(file);
	}
 }
 //创建预览dom
 function createView(file, imgsrc, a, array1,leixing){
	var arrayObj = new Array();
	for(var x=0;x<array1.length;x++){
		arrayObj[x]=array1[x].index;
	}
    var html = "";
    html += '<li id="imgViewList'+file.index+'">';
    html += '<div class="img_and_info">';
    html += '<'+leixing+'  border="0" src="'+imgsrc+'">';
    html += '</div>';
    html += '<span onclick="del(\'imgViewList'+file.index+'\', \''+a+'\', \['+arrayObj+'\])" class="upload_delete" title="删除">删除</span>';
    html += '</li>';
	document.getElementById("pageContent_"+a).innerHTML+=html;
  }
  Array.prototype.del=function(n) {　//n表示第几项，从0开始算起。
　    if(n<0){　//如果n<0，则不进行任何操作。
　　    return this;}
　  else{
　　   return this.slice(0,n).concat(this.slice(n+1,this.length));
      }
   }
 
//删除预览
	function del(obj, col, array1){
		var n=parseInt(obj.substring(11));//获取数组索引
		/*for(var s=0;s<arrFiles.length; s++){
			if(arrFiles[s].index==n){
				$('#box_input_'+col).children()[s].remove();
				currentImgLength--;
				arrFiles.splice(s,1);
				alert("删除过后还剩"+arrFiles.length+"个");
			}
		}*/
		for(var s=0;s<array1.length; s++){
			if(array1[s]==n){
				$('#box_input_'+col).children()[s].remove();
				currentImgLength--;
				array1.splice(s,1);
				//alert("1张图删除过后还剩"+array1.length+"个");
			}
		}
		for(var i=0;i<currentImgLength+2;i++){
			$('#box_input_'+col+' input').each(function(i,ele){
				$(this).attr("name","'"+col+i+"'");
				$(this).attr("type","file");
			})
		}
		removeElement(document.getElementById(obj+""));
		console.log('目前数组arrFiles为：'+arrFiles);
		console.log('目前数组array1为：'+array1);

    }
	function removeElement(_element){
         var _parentElement = _element.parentNode;
         if(_parentElement){
                _parentElement.removeChild(_element);
         }
    }
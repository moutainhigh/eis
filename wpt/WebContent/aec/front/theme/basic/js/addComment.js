var arrFiles = [];
var MaxImgLength = parseInt($("#maxImgLength").val());
var currentImgLength = 0;

function uploadApprise() {
	$("#box_input").children().last().remove();
	$("#box_input").children().last().css("display", "block");
	var data = new FormData($('#upload')[0]);
	$.ajax({
		type: "POST",
		url: '/order/submitComment.json',
		data: data,
		dataType: 'json',
		async: false,
		cache: false,
		contentType: false,
		processData: false,
		success: function(data) {
			alert(data.message.message);
			window.location.href = "/comment/index.shtml?page=1&rows=10";
		},
		error: function(data) {
			alert("系统繁忙,请稍后再试");
			return false;
		}
	})
}
function dropHandler(files) {
	if (currentImgLength >= MaxImgLength) {
		alert("最多可评论" + MaxImgLength + "张图片！");
		return false;
	}
	$('#pageContent').className = '';
	for (var f = 0; f < files.length; f++) {
		arrFiles.push(files[f]);
	}
	$('#pageContent').html("");
	currentImgLength = 0;
	for (var i = 0; i < arrFiles.length; i++) {
		arrFiles[i].index = parseInt(Math.random() * 9000000);
		viewfile(arrFiles[i]);
		currentImgLength++;
	}
	var html1 = '<a class="btn_addPic" href="javascript:void(0);"><span>添加图片</span> <input type="file"  class="filePrew"  name="productGallery' + currentImgLength + '" onchange="dropHandler(this.files)" accept="image/gif,image/jpeg,image/png,image/bmp" class="blue" /></a>';
	$("#box_input a").css("display", "none");
	$("#box_input").append(html1);
}
function viewfile(file) {
	if (window.webkitURL) {
		var imgsrc = window.webkitURL.createObjectURL(file);
		createView(file, imgsrc);
	} else if (window.URL) {
		var imgsrc = window.URL.createObjectURL(file);
		createView(file, imgsrc);
	} else {
		var reader = new FileReader();　reader.onload = function(e) {
			var imgsrc = e.target.result;
			createView(file, imgsrc);
		}
		reader.readAsDataURL(file);
	}
}
function createView(file, imgsrc) {
	var html = "";
	html += '<li id="imgViewList' + file.index + '">';
	html += '<div class="img_and_info">';
	html += '<img  border="0" src="' + imgsrc + '">';
	html += '</div>';
	html += '<span onclick="del(\'imgViewList' + file.index + '\')" class="upload_delete" title="删除">删除</span>';
	html += '</li>';
	document.getElementById("pageContent").innerHTML += html;
}
Array.prototype.del = function(n) {　　
	if (n < 0) {　　　
		return this;
	}　
	else {　　
		return this.slice(0, n).concat(this.slice(n + 1, this.length));
	}
}
function del(obj) {
	var n = parseInt(obj.substring(11));
	for (var s = 0; s < arrFiles.length; s++) {
		if (arrFiles[s].index == n) {
			$("#box_input").children()[s].remove();
			currentImgLength--;
			arrFiles.splice(s, 1);
			alert("删除过后还剩" + arrFiles.length + "个");
		}
	}
	for (var i = 0; i < currentImgLength + 2; i++) {
		$("#box_input input").each(function(i, ele) {
			$(this).attr("name", "productGallery" + i);
		})
	}
	removeElement(document.getElementById(obj + ""));
	console.log('目前数组为：' + arrFiles);
}
function removeElement(_element) {
	var _parentElement = _element.parentNode;
	if (_parentElement) {
		_parentElement.removeChild(_element);
	}
}
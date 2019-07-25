//秘钥生成js
//普通字符集合 （普通密码、MD5key）
var $commonChars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
var $complexChars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678!@#$%^&*';

//生成字符串
function randomString(len,$chars) {
	len = len || 32;
	var maxPos = $chars.length;
	var key = '';
	for (i = 0; i < len; i++) {
		key += $chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return key;
}

//生成3des秘钥
function make3DesKey(){
	return randomString(24,$complexChars);
}

//生成MD5秘钥
function makeMD5Key(len){
	return randomString(len,$commonChars);
}

//生成普通密码 
function makeCommonKey(len){
	return randomString(len,$commonChars);
}

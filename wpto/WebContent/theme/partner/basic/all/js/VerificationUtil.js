/* 
* 判断object是否空，未定义或null 
* @param object  
* @return 
*/  
jQuery.mIsNull = function (obj) {  
    if (obj == "" || typeof (obj) == "undefined" || obj == null || obj == "null") {  
        return true;  
    }  
    else {  
        return false;  
    }  
};

/* 
* 正则验证 
* @param s 验证字符串 
* @param type 验证类型 money,china,mobile等  
* @return 
*/  
jQuery.mCheck = function (s, type) {  
    var objexp = "";  
    switch (type) {  
        case 'money': //金额格式,格式定义为带小数的正数，小数点后最多三位  
            objexp = "^[0-9]+[\.][0-9]{0,3}$";  
            break;  
        case 'numletter_': //英文字母和数字和下划线组成     
            objexp = "^[0-9a-zA-Z\_]+$";  
            break;  
        case 'numletter': //英文字母和数字组成  
            objexp = "^[0-9a-zA-Z]+$";  
            break;  
        case 'numletterchina': //汉字、字母、数字组成   
            objexp = "^[0-9a-zA-Z\u4e00-\u9fa5]+$";  
            break;  
        case 'email': //邮件地址格式   
            objexp = "^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$";  
            break;  
        case 'tel': //固话格式   
            objexp = "^((\d2,3)|(\d{3}\-))?(0\d2,3|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$";  
            break;  
        case 'mobile': //手机号码   
            objexp = "^[1][0-9][0-9]{9}$";  //^(13[0-9]|15[0-9]|18[0-9])([0-9]{8})$  
            break;  
        case 'decimal': //浮点数   
            objexp = "^[0-9]+([.][0-9]+)?$";  
            break;  
        case 'url': //网址   
            objexp = "(http://|https://){0,1}[\w\/\.\?\&\=]+";  
            break;  
        case 'date': //日期 YYYY-MM-DD格式  
            objexp = "^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$";  
            break;  
        case 'int': //整数   
            objexp = "^[0-9]*[1-9][0-9]*$";  
            break;  
        case 'int+': //正整数包含0  
            objexp = "^\\d+$";  
            break;  
        case 'int-': //负整数包含0  
            objexp = "^((-\\d+)|(0+))$";  
            break;  
        case 'china': //中文   
            objexp = "^[\u0391-\uFFE5]+$";  //  /^[\u4E00-\u9FA5]{0,6}$/ 0至6位字符的中文  
            break;  
        case 'english':  
            objexp = "[a-zA-Z]";  
    }  
    var re = new RegExp(objexp);  
    if (re.test(s)) {  
        return true;  
    }  
    else {  
        return false;  
    }  
};

/**
 * 验证身份证号是否合法
 */
jQuery.isIdCard = function(idCard){
	var flag = false;
	//15位和18位身份证号码的正则表达式
	var regIdCard=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
	//如果通过该验证，说明身份证格式正确，但准确性还需计算
	if(regIdCard.test(idCard)){
		if(idCard.length==18){
		    var idCardWi=new Array( 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ); //将前17位加权因子保存在数组里
		    var idCardY=new Array( 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ); //这是除以11后，可能产生的11位余数、验证码，也保存成数组
		    var idCardWiSum=0; //用来保存前17位各自乖以加权因子后的总和
		    for(var i=0;i<17;i++){
		    	idCardWiSum+=idCard.substring(i,i+1)*idCardWi[i];
		    }
		    var idCardMod=idCardWiSum%11;//计算出校验码所在数组的位置
		    var idCardLast=idCard.substring(17);//得到最后一位身份证号码
		    //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
		    if(idCardMod==2){
		    	if(idCardLast=="X"||idCardLast=="x"){
		    		flag = true;
		    	}else{
		    		flag = false;
		    	}
		    }else{
		    	//用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
			    if(idCardLast==idCardY[idCardMod]){
			    	flag = true;
			    }else{
			    	flag = false;
			    }
		   }
		} 
	}else{
		flag = false;
	}
	return flag;
};


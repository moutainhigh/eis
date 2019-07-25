/**
 * 将时间转为日期来显示
 * */
function formatDate(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        var newTime = new Date(value);
        var pattern = "yyyy-MM-dd hh:mm:ss";
        return newTime.format(pattern);
    }
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

gridHeight = function () {
    var winHeight;
    if (parent != self) {//如果在框架中
        //减30去掉上下边框
        winHeight = parent.document.documentElement.clientHeight - $('header', parent.parent.document).height() - $('nav.iconrept', parent.parent.document).height() - 30;
    } else {
        winHeight = document.documentElement.clientHeight;
    }
    winHeight = winHeight - 40;//减40，去掉上下border
    //winHeight = winHeight - $('.ui-action').height();
    return winHeight;
}

function trim(s) {
    return  s.replace(/(^\s*)|(\s*$)/g,"");
}

<!--清除选中项-->
function clearSelection() {
    $('#grid').omGrid('setSelections', []);
}

<!--会员属性-->
function getUserProp(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'PVT') {
            return '个人用户';
        } else if (value == 'PUC') {
            return '企业用户';
        } else {
            return value;
        }
    }
}

<!--账户属性-->
function getAccountType(colValue) {
    if (colValue == null || colValue == '' || colValue == 'undefined') {
        return "";
    } else {
        if (colValue == 'P') {
            return '对私业务';
        } else if (colValue == 'C') {
            return '对公业务';
        } else {
            return colValue;
        }

    }
}

<!--调整后状态-->
function getAdjustStatus(colValue) {
    if (colValue == null || colValue == '' || colValue == 'undefined') {
        return "";
    } else {
        if (colValue == 'INIT') {
            return '初始化';
        } else if (colValue == 'FAILURE') {
            return '失败';
        } else {
            return colValue;
        }
    }
}

<!--交易状态-->
function getTradeStatus(colValue) {
    if (colValue == null || colValue == '' || colValue == 'undefined') {
        return "";
    } else {
        if (colValue == 'INIT') {
            return '初始化';
        } else if (colValue == 'PROCESSING') {
            return '处理中';
        } else if (colValue == 'SUCCESS') {
            return '成功';
        } else if (colValue == 'FAILURE') {
            return '失败';
        } else {
            return colValue;
        }
    }
}
<!--渠道状态-->
function getBankStatus(colValue) {
    if (colValue == null || colValue == '' || colValue == 'undefined') {
        return "";
    } else {
        if (colValue == 'INIT') {
            return '初始化';
        } else if (colValue == 'PROCESSING') {
            return '处理中';
        } else if (colValue == 'SUCCESS') {
            return '交易成功';
        } else if (colValue == 'FAILURE') {
            return '交易失败';
        } else if (colValue == 'REFUND') {
            return '退票';
        } else {
            return colValue;
        }
    }
}
<!--账务状态-->
function getAccountStatus(colValue) {
    if (colValue == null || colValue == '' || colValue == 'undefined') {
        return "";
    } else {
        if (colValue == 'INIT') {
            return '初始化';
        } else if (colValue == 'PROCESSING') {
            return '处理中';
        } else if (colValue == 'SUCCESS') {
            return '成功';
        } else if (colValue == 'FAILURE') {
            return '失败';
        } else {
            return colValue;
        }
    }
}
<!--退票状态-->
function getRefundStatus(colValue) {
    if (colValue == null || colValue == '' || colValue == 'undefined') {
        return "";
    } else {
        if (colValue == 'INIT') {
            return '初始化';
        } else if (colValue == 'PROCESSING') {
            return '处理中';
        } else if (colValue == 'SUCCESS') {
            return '成功';
        } else if (colValue == 'FAILURE') {
            return '失败';
        } else {
            return colValue;
        }
    }
}

//账户签约填充商户信息导致有特殊字符，页面显示不正常
function resplactSpecialCharacter(str) {
    if (!(typeof(str) != 'undefined' && str != null && str != '' && str != 'null')) {
       return "";
    }
    return str.replace(/"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
}
//对于后台查询的特殊字符，需要正确展示与resplactSpecialCharacter相反
function resplactCharacter(str){
    if (!(typeof(str) != 'undefined' && str != null && str != '' && str != 'null')) {
        return "";
    }
    return  str.replace(/\&quot\;/g,"\"").replace(/\&lt\;/g,"<").replace(/\&gt\;/g,">");
}
function checkArgument(str) {
    if (!(typeof(str) != 'undefined' && str != null && str != '' && str != 'null')) {
        return "";
    }else{
        return str;
    }
}
/**
 * 获取当前时间的前一天
 */
getYesterdayFormat = function (date) {
    var yesterdayMillons = date.getTime() - 1000 * 60 * 60 * 24;
    var yesterday = new Date();
    yesterday.setTime(yesterdayMillons);
    var yearStr = yesterday.getFullYear();
    var dayStr = yesterday.getDate();
    var monthStr = yesterday.getMonth() + 1;
    if (monthStr < 10) {
        monthStr = "0" + monthStr;
    }
    if (dayStr < 10) {
        dayStr = "0" + dayStr;
    }
    var resultData = yearStr + "-" + monthStr + "-" + dayStr;
    return resultData;
};

/**
 * 单位分 整数
 * @param amount
 * @param len
 * @returns {*}
 */
var amountFormat = function (amount) {
	if(isNaN(amount)){
		return '';
	}
    var num = Math.round(amount) / 100;
    var symbol = "";
    if(num<0){
        symbol = "-"
    }
    num = num.toString();
    if (/^0+/) //清除字符串开头的0
        num = num.replace(/^0+/, '');
    if (!/\./.test(num)) //为整数字符串在末尾添加.00
        num += '.00';
    if (/^\./.test(num)) //字符以.开头时,在开头添加0
        num = '0' + num;
    num += '00';
    num = num.match(/\d+\.\d{2}/)[0];
    var part = num.toString().split(".");
    var formatAmount = symbol+insertComma(part[0], 3) + "." + part[1];
    return formatAmount;
};

/**
 * 将数字分割 例 1000 =  1,000
 * @param num
 * @param len
 * @returns {*}
 */
var insertComma = function (num, len) {
    var len = len || 3;
    if (num == undefined) {
        return "";
    }
    var num = num.toString();
    var length = num.length, num2 = '', max = Math.floor((length - 1) / len);
    for (var i = 0; i < max; i++) {
        var s = num.slice(length - len, length);
        num = num.substr(0, length - len);
        num2 = (',' + s) + num2;
        length = num.length;
    }
    num += num2;
    return num;
};

//获取账户类型
function getAccountType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == '0801') {
            return '商户基本账户';
        } else if (value == '0811') {
            return '商户待清算账户';
        } else if (value == '8001') {
            return '渠道交易应付账户';
        } else if (value == '8002') {
            return '渠道交易应收账户';
        } else if (value == '8003') {
            return '渠道资金应付账户';
        } else if (value == '8004') {
            return '渠道资金应收账户';
        } else {
            return value;
        }
    }
}

//获取资金类型
function getFundType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'AVAL') {
            return '可用余额';
        } else if (value == 'FREE') {
            return '冻结金额';
        } else if (value == 'RISK') {
            return '风险金额';
        } else {
            return value;
        }
    }
}

//获取交易状态
function getTradeStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'BUID') {
            return '交易建立';
        } else if (value == 'WPAR') {
            return '等待支付结果';
        } else if (value == 'CLOS') {
            return '交易关闭';
        } else if (value == 'FINI') {
            return '交易成功';
        } else if (value == 'REFU') {
            return '交易退款';
        } else if (value == 'ACCE') {
            return '交易受理';
        } else if (value == 'ACSU') {
            return '受理成功';
        } else if (value == 'BPFI') {
            return '买家已付款';
        } else if (value == 'WICY') {
            return '确认中';
        } else if (value == 'CONF') {
            return '交易已确认';
        } else if (value == 'RETU') {
            return '退票';
        } else if (value == 'CLWT') {
            return '交易待关闭';
        } else {
            return value;
        }
    }
}

//获取交易状态
function getAccountCategory(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'FEE_CHARGE_IN') {
            return '手续费收入';
        } else if (value == 'FEE_CHARGE_OUT') {
            return '手续费支出';
        } else if (value == 'PAY_INCOME') {
            return '交易收入';
        } else if (value == 'BANK_RECEV') {
            return '渠道交易应收';
        } else if (value == 'SETTLE_OUT') {
            return '结算支出';
        } else if (value == 'SETTLE_IN') {
            return '结算收入';
        } else if (value == 'TRANS_OUT') {
            return '转账支出';
        } else if (value == 'BANK_PAYMENT') {
            return '渠道交易应付';
        } else {
            return value;
        }
    }
}


//获取操作类型
function getFundOperateType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'IN') {
            return '收入';
        } else if (value == 'OUT') {
            return '支出';
        } else {
            return value;
        }
    }
}

//获取会员类型
function getMemberType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == '00') {
            return '个人会员';
        } else if (value == '08') {
            return '企业会员';
        } else if (value == '80') {
            return '渠道内部会员';
        } else if (value == '88') {
            return '通用内部会员';
        } else {
            return value;
        }
    }
}
/**
 * 清算状态
 */
function getClearStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'WAIT_CLEAR') {
            return '待清算';
        } else if (value == 'SUC_CLEAR') {
            return '已清算';
        } else {
            return value;
        }
    }
}

//获取会员状态
function getMemberStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'WACT') {
            return '待激活';
        } else if (value == 'NORM') {
            return '帐户正常';
        } else if (value == 'SLEEP') {
            return '休眠';
        } else if (value == 'CANCEL') {
            return '注销';
        } else {
            return value;
        }
    }
}

//获取支付卡类型
function getCardType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'UN') {
            return '默认';
        } else if (value == 'CR') {
            return '信用卡';
        } else if (value == 'DE') {
            return '储蓄卡';
        } else {
            return value;
        }
    }
}

//获取支付卡类型
function getBatchStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'INIT') {
            return '对账操作初始化';
        } else if (value == 'SUCCESS') {
            return '对账操作成功';
        } else if (value == 'FAIL') {
            return '对账操作失败';
        } else if (value == 'NOBILL') {
            return '渠道没有订单信息';
        } else if (value == 'ERROR') {
            return '渠道返回错误信息';
        } else {
            return value;
        }
    }
}

//获取对账差错类型
function getMistakeType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'BANK_MISS') {
            return '渠道漏单';
        } else if (value == 'PLATFORM_MISS') {
            return '平台漏单';
        } else if (value == 'PLATFORM_SHORT_STATUS_MISMATCH') {
            return '平台短款，状态不符';
        } else if (value == 'PLATFORM_SHORT_CASH_MISMATCH') {
            return '平台短款，金额不符';
        } else if (value == 'PLATFORM_OVER_CASH_MISMATCH') {
            return '平台长款,金额不符';
        } else if (value == 'PLATFORM_OVER_STATUS_MISMATCH') {
            return '平台长款,状态不符';
        } else if (value == 'FEE_MISMATCH') {
            return '手续费不匹配';
        } else {
            return value;
        }
    }
}

//获取对账差错类型
function getMistakeHandleStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'HANDLED') {
            return '已处理';
        } else if (value == 'NOHANDLE') {
            return '未处理';
        } else {
            return value;
        }
    }
}

//获取账户状态
function getAccountStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'OPEN') {
            return '帐户开立';
        } else if (value == 'NORM') {
            return '帐户正常';
        } else if (value == 'S_PAY') {
            return '帐户止付';
        } else if (value == 'FREZ') {
            return '帐户冻结';
        } else if (value == 'CLOS') {
            return '帐户关闭';
        } else {
            return value;
        }
    }
}

//获取证件类型
function getCertificateType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
    	if (value == 'ID') {
            return '身份证';
        } else if (value == 'PA') {
            return '护照';
        } else if (value == 'HO') {
            return '回乡证';
        } else if (value == 'TW') {
            return '台胞证';
        } else if (value == 'CE') {
            return '军官证';
        } else if (value == 'OF') {
            return '警官证';
        } else if (value == 'SO') {
            return '士兵证';
        } else if (value == 'FPA') {
            return '外国护照';
        } else if (value == 'TID') {
            return '临时身份证';
        } else if (value == 'HR') {
            return '户口本';
        } else if (value == 'DID') {
            return '外交人员身份证';
        } else if (value == 'FRP') {
            return '外国人居留许可证';
        } else if (value == 'BAP') {
            return '边民出入境通行证';
        } else {
            return value;
        }
    }
}

//获取申请单状态
function getApplyFlowStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'NO_SUB') {
            return '未提交审核';
        } else if (value == 'WAIT') {
            return '等待审核中';
        } else if (value == 'NO_PASS') {
            return '审核退回';
        } else if (value == 'INVALID') {
            return '审核未通过';
        } else if (value == 'PASS') {
            return '审核通过';
        } else {
            return value;
        }
    }
}

//获取申请单编辑状态
function getAuditStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'CAN_AUD') {
            return '可编辑';
        } else if (value == 'NO_AUD') {
            return '不可编辑';
        } else {
            return value;
        }
    }
}

//获取认证方式
function getCertificateType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'UN_SOCIAL_CRE') {
            return '统一社会信用代码类';
        } else if (value == 'ORDINARY_FIVE') {
            return '普通五证类';
        } else if (value == 'MULTIPLE_TO_ONE') {
            return '多证合一类';
        } else {
            return value;
        }
    }
}

//获取申请单编辑状态
function getEnterpriseType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'ENTERPRISE') {
            return '企业';
        } else if (value == 'SOCIAL_GROUPS') {
            return '社会团体';
        } else if (value == 'NATURAL_PERSON') {
            return '自然人';
        } else if (value == 'FOUNDATION') {
            return '基金会';
        } else if (value == 'INDIVIDUAL_ENTERPRISE') {
            return '个体工商户';
        } else if (value == 'GOVERNMENT_ORGANS') {
            return '党政国家机关';
        } else if (value == 'INSTITUTION') {
            return '事业单位';
        } else {
            return value;
        }
    }
}

//获取认证方式
function getIdType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'ID') {
            return '身份证';
        } else if (value == 'PA') {
            return '护照';
        } else if (value == 'HO') {
            return '回乡证';
        } else if (value == 'TW') {
            return '台胞证';
        } else if (value == 'CE') {
            return '军官证';
        } else if (value == 'OF') {
            return '警官证';
        } else if (value == 'SO') {
            return '士兵证';
        } else if (value == 'FPA') {
            return '外国护照';
        } else if (value == 'TID') {
            return '临时身份证';
        } else if (value == 'HR') {
            return '户口本';
        } else if (value == 'DID') {
            return '外交人员身份证';
        } else if (value == 'FRP') {
            return '外国人居留许可证';
        } else if (value == 'BAP') {
            return '边民出入境通行证';
        } else {
            return value;
        }
    }
}

//获取申请单编辑状态
function getTermType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'LONG') {
            return '长期';
        } else if (value == 'FIXED') {
            return '固定期限';
        } else {
            return value;
        }
    }
}

//获取秘钥状态
function getSecretKeyStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'DISABLE') {
            return '禁用';
        } else if (value == 'ENABLE') {
            return '可用';
        } else {
            return value;
        }
    }
}

//获取秘钥类型
function getSecretKeyType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'DES3') {
            return '3DES';
        } else if (value == 'SHA-256') {
            return 'SHA-256';
        } else if (value == 'MD5') {
            return 'MD5';
        } else {
            return value;
        }
    }
}

//获取产品类型
function getProductType(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == 'TRADE') {
            return '交易';
        } else if (value == 'WITHDRAW') {
            return '提现';
        } else {
            return value;
        }
    }
}

//获取产品类型
function getOldTradeStatus(value) {
    if (value == null || value == '' || value == 'undefined') {
        return "";
    } else {
        if (value == '500036') {
            return '数据错误';
        } else if (value == '500091') {
            return '网络错误';
        } else if (value == '500152') {
            return '银行卡号错误';
        } else if (value == '500153') {
            return '客户银行卡绑定错误';
        } else if (value == '500159') {
            return '通道余额不足';
        } else if (value == '500160') {
            return '出款通道账户超限';
        } else if (value == '500161') {
            return '出款通道不匹配';
        } else if (value == '710010') {
            return '交易成功';
        } else if (value == '710011') {
            return '交易失败';
        } else if (value == '710013') {
            return '交易处理中';
        } else if (value == '710028') {
            return '部分完成';
        } else if (value == '710021') {
            return '新订单';
        }else {
            return value;
        }
    }
}
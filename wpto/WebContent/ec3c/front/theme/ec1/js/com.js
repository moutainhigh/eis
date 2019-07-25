var errorNoRelatedNode		= 500001; //没有找到起码一个关联节点;18;错误码
var errorNoRelatedNode2		= 500001; //没有找到起码一个关联节点;18;错
var errorNoWorkflow		= 500002; //没有找到起码一个工作流;18;错误码
var errorObjectNotEditable	= 500003; //对象属于不可编辑状态;18;错误码
var errorObjectIsNull		= 500004; //需要查找的对象是空;18;错误码
var errorAuthFailed		= 500005; //认证失败;18;错误码
var errorAccessDenied		= 500006; //无权限;18;错误码
var errorUnsupportedFileName	= 500008; //不被支持的文件名;18;错误码
var errorBillAlreadyExist		= 500009; //订单已存在或已处理过;18;错误码
var errorChargeToAccountNotExist	= 500010; //被充值账户不存在;18;错误码
var errorBillUpdateFailed		= 500011; //订单更新失败;18;错误码
var errorMoneyUpdateFailed	= 500012; //资金更新失败;18;错误码
var errorDataUpdateFailed		= 500013; //数据更新失败;18;错误码
var errorVersionUnSupported	= 500014; //版本不支持;18;错误码
var errorUnknownError		= 500015; //未知错误;18;错误码
var errorVerifyError		= 500016; //校验失败;18;错误码
var errorCanNotParseObject	= 500017; //无法解析需要的对象;18;错误码
var errorObjectAlreadyExist	= 500018; //要创建的对象已存在;18;错误码
var errorAccountLocked		= 500019; //账户被锁定;18;错误码
var errorParentAccountLocked	= 500020; //父账户被锁定;18;错误码
var errorBillCreateFailed		= 500021; //无法创建订单;18;错误码
var errorBillNotExist		= 500022; //订单不存在;18;错误码
var errorDataFormatError		= 500023; //错误的数据格式;18;错误码
var errorAccountError		= 500024; //帐号错误;18;错误码
var errorAccountNotExist		= 500025; //帐号不存在;18;错误码
var errorMoneyRangeError		= 500026; //金额超范围;18;错误码
var errorActiveSignNotFound	= 500027; //没找到对应的激活码;18;错误码
var errorMailBindSignNotFound	= 500028; //没找到对应的邮箱绑定签名;18;错误码
var errorPhoneBindSignNotFound	= 500029; //没找到对应的手机绑定签名;18;错误码
var errorFindPasswordSignNotFound		= 500030; //没找到对应的找回密码签名;18;错误码
var errorUserNotFoundInSession	= 500031; //在Session中没找到用户;18;错误码
var errorRequiredDataNotFound	= 500032; //必须的数据没有提供;18;错误码
var errorSystemInMaintenance	= 500033; //系统维护中;18;错误码
var errorSystemUpgrade		= 500034; //系统例行升级中;18;错误码
var errorBillDuplicate		= 500035; //订单重复;18;错误码
var errorDataError		= 500036; //数据错误;18;错误码
var errorNoMatchedPrivilege	= 500037; //无匹配的权限;18;错误码
var errorFlowWorkRouteCountError	= 500038; //业务流异常，该业务流工作步骤数量错误;18;错误码
var errorChargeToPeerError	= 500039; //充值到对方异常;18;错误码
var errorPaySuccessButChargeToPeerError	= 500040; //支付完成但充值到对方异常;18;错误码
var errorMailAlreadyBinded	= 500041; //邮箱已绑定;18;错误码
var errorUserNotFoundInRequest	= 500042; //在请求中没找到用户;18;错误码
var errorMailBindSignNotFoundInRequest	= 500043; //请求中没找到对应的绑定码;18;错误码
var errorMailBindSignNotFoundInSystem	= 500044; //系统中没找到对应的绑定码;18;错误码
var errorMailBindSignNotMatch	= 500045; //绑定签名不一致;18;错误码
var errorDataUniqueConflict	= 500046; //数据唯一性错误;18;错误码
var errorDataGlobalUniqueConflict	= 500047; //数据全局唯一性错误;18;错误码
var errorFindPasswordSignNotFoundInSystem = 500048; //没找到对应的找回密码签名;18;错误码
var errorTransactionObjectNotMatch	= 500049; //交易对象不匹配;18;错误码
var errorMoneyAccountNotExist		= 500050;	//用户的资金帐号不存在;18;错误码
var errorMoneyNotEnough			= 500051;	//用户资金账号不足;18;错误码
var errorCartIsNull			= 500052;	//购物车为空;18;错误码
var errorCartIsEmpty			= 500053;	//购物车没有物品;18;错误码
var errorTransactionProcessorNotFound	= 500053;	//找不到对应的交易处理器;18;错误码
var errorAddToCartFail			= 500054;	//向购物车添加物品失败;18;错误码
var errorPayMethodIsNull			= 500055;	//关联的支付方式为空;18;错误码
var errorPayProcessorIsNull		= 500056;	//指定的支付处理器不存在;18;错误码
var errorSupplierNotFound			= 500057;	//找不到供应商;18;错误码
var errorUserNotFoundInSystem		= 500058;	//系统中找不到对应的用户;18;错误码
var errorDataVerifyFail			= 500059;	//数据校验失败;18;错误码
var activityClosed			= 500072;	//活动已结束;18;错误码
var activityMissedShot			= 500073;	//活动未命中;18;错误码
var activityLimited			= 500074;	//活动受限;18;错误码
var captchaTimeOut	        = 500086;	//验证码超时

var transSuccess				= 710010;	//交易成功;16;交易状态态
var transFailed				= 710011;	//交易失败;16;交易状态
var transCompleted			= 710012;	//交易已处理;16;交易状态	
var transInProcess			= 710013;	//交易处理中;16;交易状态
var transCompletedPaySuccessChargeFailed	= 710014;	//支付成功但充值失败;16;交易状态
var transCompletedButUpdateFailed		= 710015;	//支付充值成功但数据更新失败;16;交易状态
var transCompletedPaySuccessChargeNotProcess	= 710016;	//支付成功但尚未进行充值;16;交易状态
var transTimeout	= 710017;	//交易超时;16;交易状态
var transInCart				= 710018;	//交易暂存在购物车;16;交易状态
var transWaitingPay	= 710019;	//交易等待付款;16;交易状态
var transCartHalfComplete	= 710020;	//购物车中的部分交易成功
var transNewOrder		 = 710021;	//新的订单;16;交易状态
//操作结果
var operateResultAccept		= 102005;	//允许;6;操作结果
var operateResultDeny		= 102006;	//拒绝;6;操作结果
var operateResultFailed		= 102007;	//失败;6;操作结果
var operateResultSuccess		= 102008;	//成功;6;操作结果
var operateStatusNew		= 102009;	//新;6;操作状态
var operateJump			= 102124;	//跳转到指定地址;7;操作代码
var authSuccess			= 175001;	//认证成功;17;认证及授权状态
var userFromSuggest		= 120005; 	//来自意见簿创建的用户;12;用户状态
var systemName = "以先";
var loginUrl = "/user/login";
var registerSubmitUrl = "/user/register";
var userHost=""; //用户相关的主机
var payHost=""; //
var apiHost=""; //其他默认请求主机
var suffix = '.shtml';//后缀
/**
 面）
 * <li>如果没有前一页历史，则直接关闭当前页面</li>
 */



 

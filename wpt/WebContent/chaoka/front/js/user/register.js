require.config({
    paths: {
        jquery: 'jquery-1.8.3.min',
		userLib: 'userLib',
		constants: 'constants'
    }
});

requirejs(['userLib'], function(userLib){
		$('#registerSubmit').click(function(){
			$.post(registerSubmitUrl + ".json", function result(response){
			if(response.message != null){
				if(response.message.operateCode == errorDataError){
					alert("请提交正确的数据");
				} else {
					alert(response.message.message + "[" + response.message.operateCode + "]");
				}
			} else {
				alert("系统异常，请稍后再试。");
			}
		});
			
			
			
			
			
			
			
		});
	}
);

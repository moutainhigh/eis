define('requirejs(['userLib'], function(userLib){
	var redirect = true;
	userLib.showUserInformation(redirect);
}
);',['jquery','constants'], function (){	
	var showUserInformation = function(redirect){
		alert(redirect);
		$.get("/user.json", function result(response){
			if(response.frontUser != null){
				$('#username').html(response.frontUser.username);				
			} else {
				if(redirect){
					//window.location.href=loginUrl;
				}
			}
			if(response.money != null){
				$('#chargeMoney').html(response.money.chargeMoney + "麦粒");
				$('#incomingMoney').html(response.money.incomingMoney + "麦粒");
				$('#frozenMoney').html(response.money.frozenMoney + "麦粒");
				$('#marginMoney').html(response.money.marginMoney + "麦粒");
			}
		});
	};
	
	
	return {
		showUserInformation: showUserInformation
	};
	
});
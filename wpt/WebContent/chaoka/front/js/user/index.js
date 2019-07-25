require.config({
    paths: {
        jquery: '/js/lib/jquery-1.8.3.min',
		constants: '/js/lib/constants',
		userLib: '/js/user/lib'
    }
});

requirejs(['userLib'], function(userLib){
	var redirect = true;
	userLib.showUserInformation(redirect);
}
);

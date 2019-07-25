require.config({
	baseUrl: "/js/lib",
    paths: {
        'jquery': 'jquery-1.8.3.min',
		'jquery.form':'jquery.form',
		'buyLib': '/js/buy/lib',
		'constants': 'constants'
    }
});

requirejs(['buyLib', 'jquery','jquery.form'], function(buyLib, $){
		$('#buySubmitButton').click(function(){
			var submitOptions = {
					url: $('#buyForm').attr('action') + ".json",
					success: buySuccess,
					error: buyError
			};
				
			$('#buyForm').ajaxSubmit(submitOptions);
		});
}
);

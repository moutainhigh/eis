$(function() {
	 var $scxy = $('#scxy');
     var $jxsp = $('#jxsp');
     var $jcsk = $('#jcsk');

      $scxy.click(function(){
        alert(123);
        $jxsp.removeClass('current');
        $jcsk.removeClass('current');
        $scxy.addClass('current');
        $('.jxsp').css('display','none');
        $('.jcsk').css('display','none');
        $('.scxy').css('display','block');
    });
    $jxsp.click(function(){

        $scxy.removeClass('current');
        $jcsk.removeClass('current');
        $jxsp.addClass('current');
        $('.jxsp').css('display','block');
        $('.scxy').css('display','none');
        $('.jcsk').css('display','none');
    });

     $jcsk.click(function(){

        $scxy.removeClass('current');
        $jxsp.removeClass('current');
        $jcsk.addClass('current');
        $('.scxy').css('display','none');
        $('.jxsp').css('display','none');
        $('.jcsk').css('display','block');

    });



})

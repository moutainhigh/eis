$(function() {
    var $block = $('#CarouselAD'),
		$slides = $('ul.list', $block),
		_width = $block.width(),
		$li = $('li', $slides),
		_animateSpeed = 600,
		timer, _showSpeed = 3000, _stop = false;

    var _str = '';
    for (var i = 0, j = $li.length; i < j; i++) {
        _str += '<li class="playerControl_' + (i + 1) + '"></li>';
    }

    var $playerControl = $('<ul class="playerControl"></ul>').html(_str).appendTo($slides.parent()).css('left', function() {
        return (_width - $(this).width()) / 2;
    });

    var $playerControlLi = $playerControl.find('li').mouseover(function() {
        var $this = $(this);
        $this.addClass('curAD').siblings('.curAD').removeClass('curAD');

        clearTimeout(timer);
        $slides.stop().animate({
            left: _width * $this.index() * -1
        }, _animateSpeed, function() {
            if (!_stop) timer = setTimeout(move, _showSpeed);
        });

        return false;
    }).eq(0).mouseover().end();

    $block.hover(function() {
        _stop = true;
        clearTimeout(timer);
    }, function() {
        _stop = false;
        timer = setTimeout(move, _showSpeed);
    });

    function move() {
        var _index = $('.curAD').index();
        $playerControlLi.eq((_index + 1) % $playerControlLi.length).mouseover();
    }
});
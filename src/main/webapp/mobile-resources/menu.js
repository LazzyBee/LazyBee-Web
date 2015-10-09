$(function() {
	$('html').click(function(e) {
		$('#menu').css({
			'display' : 'none'
		});
		menuVisible = false;
		return;
	})
	var menuVisible = false;
	$('#menuBtn').click(function(e) {
		e.stopPropagation();
		if (menuVisible) {
			$('#menu').css({
				'display' : 'none'
			});
			menuVisible = false;
			return;
		}
		$('#menu').css({
			'display' : 'block'
		});
		menuVisible = true;
	});
	$('#menu').click(function() {
		$(this).css({
			'display' : 'none'
		});
		menuVisible = false;
	});
});
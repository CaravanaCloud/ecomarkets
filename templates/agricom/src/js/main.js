/// <reference path='lib/isotope.pkgd.js' />
/// <reference path='lib/owl.carousel.js' />
/// <reference path='lib/jquery.countTo.js' />
/// <reference path='lib/jquery.appear.min.js' />
/// <reference path='lib/jquery.easypiechart.js' />
/// <reference path='lib/jarallax.min.js' />
/// <reference path='lib/jquery.fs.boxer.min.js' />

var _fixed_menu;

(function($) {

	/*-- Strict mode enabled --*/
	'use strict';

	var nHtmlNode = document.documentElement,
		jWindow = $(window),
		animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';

	/* fixed menu
	================================================== */
	_fixed_menu = function _fixed_menu ()
	{
		var nTopBar       = document.getElementById('top-bar'),
			jTopBar       = $(nTopBar),
			iTop          = jTopBar.next('header').innerHeight() - 80,
			bHeaderSticky = false;

		window.onscroll = function() {
			if ( (window.pageYOffset || document.documentElement.scrollTop) >= iTop ) {

				if ( !bHeaderSticky )
				{
					jTopBar
						.off(animationEnd)
						.addClass('fixed in')
						.one(animationEnd, function(e){
							jTopBar.removeClass('in');
						});

					bHeaderSticky = !bHeaderSticky;
				};

			} else if ( bHeaderSticky ) {

				jTopBar
					.addClass('out')
					.off(animationEnd)
					.one(animationEnd, function(e){
						jTopBar.removeClass('fixed out');
					});

					bHeaderSticky = !bHeaderSticky;
			};
		};
	};

	/* main menu
	================================================== */
	function _main_menu ()
	{
		var nTopBar      = document.getElementById('top-bar'),
			nMenuToggler = document.getElementById('top-bar__navigation-toggler'),
			nNav         = document.getElementById('top-bar__navigation'),

			jTopBar      = $(nTopBar),
			jMenuToggler = $(nMenuToggler),
			jNav         = $(nNav),

			jLink        = jNav.find('li a'),
			jSubMenu     = jNav.find('.submenu'),
			bMenuOpen    = false,
			TopBarHeight = 0;

		if ( jSubMenu.length ) { jSubMenu.parents('li').addClass('has-children'); };

		TopBarHeight = jMenuToggler.is(':visible') ? 70 : 80;

		jLink.on('touchend click', function (e) {

			var $this = $(this),
				$parent = $this.parent();

			if ( location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname )
			{
				var target = $(this.hash);

				target = target.length ? target : $('[name=' + this.hash.slice(1) +']');

				if ( target.length )
				{
					$('html,body').stop().animate({
						scrollTop: target.offset().top - TopBarHeight
					}, 1000);
				}

				if ( jMenuToggler.is(':visible') )
				{
					jTopBar.removeClass('expanded');
					jMenuToggler.removeClass('active');
				};

				return false;
			};

			if ( jMenuToggler.is(':visible') && $this.next(jSubMenu).length )
			{
				if ( $this.next().is(':visible') )
				{
					$parent.removeClass('drop_active');
					$this.next().slideUp('fast');

				} else {

					$this.closest('ul').find('li').removeClass('drop_active');
					$this.closest('ul').find('.submenu').slideUp('fast');
					$parent.addClass('drop_active');
					$this.next().slideDown('fast');
				};

				return false;
			};
		});

		jMenuToggler.on('touchend click', function (e) {
			e.preventDefault();

			var $this = $(this);

			if ( bMenuOpen )
			{
				$this.removeClass('active');
				jTopBar.removeClass('expanded');
				nHtmlNode.style.overflow = '';
				bMenuOpen = !bMenuOpen;
			}
			else
			{
				$this.addClass('active');
				jTopBar.addClass('expanded');
				nHtmlNode.style.overflow = 'hidden';
				bMenuOpen = !bMenuOpen;
			}

			return false;
		});

		jWindow.on('resize', debounce(function () {

			if ( window.innerWidth > 767 )
			{
				jTopBar.removeClass('expanded');
				jMenuToggler.removeClass('active');
				jSubMenu.removeAttr('style');
				nHtmlNode.style.overflow = '';
				bMenuOpen = false;
			}
		}, 100));
	};

	/* owl carousel
	================================================== */
	function _owl_carousel ()
	{
		var fSlider = $('.feedbacks--slider');

		if ( fSlider.length > 0 )
		{
			fSlider.children('.owl-carousel').owlCarousel({
				loop: true,
				nav: false,
				dots: true,
				autoplay: true,
				autoplayTimeout: 6000,
				autoplayHoverPause: true,
				autoHeight: true,
				smartSpeed: 1000,
				margin: 30,
				navText: [
					'<i class="fa fa-angle-left"></i>',
					'<i class="fa fa-angle-right"></i>'
				],
				responsive: {
					0:{
						items:1
					},
					992:{
						items:1
					}
				}
			});
		};
	};

	/* isotope sorting
	================================================== */
	function _isotope_sorting ()
	{
		var nOptionSets = document.getElementById('gallery-set'),
			jOptionSets = $(nOptionSets);

		if ( jOptionSets.length > 0 )
		{
			var jIsoContainer = $('.js-isotope'),
				jOptionLinks = jOptionSets.find('a');

			jOptionLinks.on('click', function(e) {
				var $this = $(this),
					currentOption = $this.data('cat');

				jOptionSets.find('.selected').removeClass('selected');
				$this.addClass('selected');

				if (currentOption !== '*') {
					currentOption = '.' + currentOption;
				}

				jIsoContainer.isotope({filter : currentOption});

				return false;
			});
		};
	};

	/* chart
	================================================== */
	function _chart ()
	{
		$('.skill__item').appear(function() {
			var _self = $(this);

			setTimeout(function() {
				_chartInit(_self);
			}, 200);
		});

		function _chartInit(el)
		{
			$('.js-chart',el).each(function () {
				$(this).easyPieChart({
					easing: 'easeOutElastic',
					delay: 3000,
					barColor: '#369670',
					trackColor: '',
					scaleColor: false,
					lineWidth: 12,
					trackWidth: 12,
					size: 175,
					lineCap: 'butt',
					onStep: function(from, to, percent) {
						this.el.children[0].innerHTML = Math.round(percent);
					}
				});
			});
		};
	};

	/* counters
	================================================== */
	function _count()
	{
		$('.counter__item').appear(function() {
			var _self = $(this);

			setTimeout(function() {
				_countInit(_self);
			}, 200);
		});

		function _countInit(el)
		{
			$('.js-count',el).each(function() {
				if( !$(this).hasClass('animate') )
				{
					$(this).countTo({
						from: 0,
						speed: 2000,
						refreshInterval: 100,
						onComplete: function() {
							$(this).addClass('animate');
						}
					});
				}
			});
		};
	};

	/* google map
	================================================== */
	function _g_map ()
	{
		var maps = $('.g_map');

		if ( maps.length > 0 )
		{
			var apiKey = maps.attr('data-api-key'),
				apiURL;

			if (apiKey)
			{
				apiURL = 'http://maps.google.com/maps/api/js?key='+ apiKey +' &sensor=false';
			}
			else
			{
				apiURL = 'http://maps.google.com/maps/api/js?sensor=false';
			}

			$.getScript( apiURL , function( data, textStatus, jqxhr ) {

				maps.each(function() {
					var current_map = $(this),
						latlng = new google.maps.LatLng(current_map.attr('data-longitude'), current_map.attr('data-latitude')),
						point = current_map.attr('data-marker'),

						myOptions = {
							zoom: 14,
							center: latlng,
							mapTypeId: google.maps.MapTypeId.ROADMAP,
							mapTypeControl: false,
							scrollwheel: false,
							draggable: true,
							panControl: false,
							zoomControl: false,
							disableDefaultUI: true
						},

						stylez = [
							{
								featureType: "all",
								elementType: "all",
								stylers: [
									{ saturation: -100 } // <-- THIS
								]
							}
						];

					var map = new google.maps.Map(current_map[0], myOptions);

					var mapType = new google.maps.StyledMapType(stylez, { name:"Grayscale" });
					map.mapTypes.set('Grayscale', mapType);
					map.setMapTypeId('Grayscale');

					var marker = new google.maps.Marker({
						map: map,
						icon: {
							size: new google.maps.Size(59,69),
							origin: new google.maps.Point(0,0),
							anchor: new google.maps.Point(0,69),
							url: point
						},
						position: latlng
					});

					google.maps.event.addDomListener(window, "resize", function() {
						var center = map.getCenter();
						google.maps.event.trigger(map, "resize");
						map.setCenter(center);
					});
				});
			});
		};
	};

	/* parallax
	================================================== */
	function _parallax ()
	{
		var nJarallax = document.querySelectorAll('.jarallax');

		if ( device.desktop() && nJarallax.length > 0 )
		{
			jarallax(nJarallax, {
				type: 'scroll', // scroll, scale, opacity, scroll-opacity, scale-opacity
				zIndex: -20
			});
		};
	};

	/* scroll to top
	================================================== */
	function _scrollTop ()
	{
		var	nBtnToTopWrap = document.getElementById('btn-to-top-wrap'),
			jBtnToTopWrap = $(nBtnToTopWrap);

		if ( jBtnToTopWrap.length > 0 )
		{
			var nBtnToTop = document.getElementById('btn-to-top'),
				jBtnToTop = $(nBtnToTop),
				iOffset   = jBtnToTop.data('visible-offset');

			jBtnToTop.on('click', function (e) {
				e.preventDefault();

				$('body,html').stop().animate({ scrollTop: 0 } , 1500);

				return false;
			});

			jWindow.on('scroll', throttle(function(e) {

				if ( jWindow.scrollTop() > iOffset )
				{
					if ( jBtnToTopWrap.is(":hidden") )
					{
						jBtnToTopWrap.fadeIn();
					};

				}
				else
				{
					if ( jBtnToTopWrap.is(":visible") )
					{
						jBtnToTopWrap.fadeOut();
					};
				};

			}, 400)).scroll();
		};
	};

	/* boxer gall
	================================================== */
	function _gall ()
	{
		var galleryElement = $("a[data-gallery]");

		if ( galleryElement.length > 0 )
		{
			galleryElement.boxer({
				fixed: true,
				videoWidth: 1000
			});
		};
	};

	/* contact form
	================================================== */
	function _contactForm ()
	{
		var jForm = $('.js-contact-form');

		if ( jForm.length > 0 )
		{
			jForm.each(function ( i, form ) {
				var $this = $( form );

				$this.on('submit', function() {
					var $this = $(this),
						str = $this.serialize(),
						note = $this.find('.form__note');

					$.ajax({
						type: "POST",
						url: "send_mail/contact_process.php",
						data: str,
						success: function(msg) {

							var result = '<span style="color: green"><br/>Your message has been sent. Thank you!</span>';

							note.html(result);

							$this.get(0).reset();

							setTimeout(function() { note.html('') }, 3000);
						},
						error: function(err) {
							var result = '<span style="color: red"><br/>Your message not sent! Error: "'+err.responseJSON.message+'"</span>';

							note.html(result);
						},
						complete: function() {
						}
					});

					return false;
				});
			});
		};
	};

	$(document).ready(function() {

		/* fixed menu
		================================================== */
		_fixed_menu();

		/* main menu
		================================================== */
		_main_menu();

		/* owl carousel
		================================================== */
		_owl_carousel();

		/* isotopeSort
		================================================== */
		_isotope_sorting();

		/* chart
		================================================== */
		_chart();

		/* counters
		================================================== */
		_count();

		/* parallax
		================================================== */
		_parallax();

		/* scroll to top
		================================================== */
		_scrollTop();

		/* boxer gall
		================================================== */
		_gall();

		/* contact form
		================================================== */
		_contactForm();
	});

	jWindow.on('load', function () {

		var jIsotope = $('.js-isotope');

		if ( jIsotope.length )
		{
			jIsotope.isotope('layout');
		};

		/* google map
		================================================== */
		_g_map();
	});

	// Create a safe reference to the Underscore object for use below.
	function now() {
		return new Date().getTime();
	};

	function throttle(func, wait, options)
	{
		var timeout, context, args, result;
		var previous = 0;

		if (!options) options = {};

		var later = function later()
		{
			previous = options.leading === false ? 0 : now();
			timeout = null;
			result = func.apply(context, args);
			if (!timeout) context = args = null;
		};

		var throttled = function throttled()
		{
			var at = now();
			if (!previous && options.leading === false) previous = at;
			var remaining = wait - (at - previous);
			context = this;
			args = arguments;
			if (remaining <= 0 || remaining > wait)
			{
				if (timeout)
				{
						clearTimeout(timeout);
						timeout = null;
				}
				previous = at;
				result = func.apply(context, args);
				if (!timeout) context = args = null;
			}
			else if (!timeout && options.trailing !== false)
			{
				timeout = setTimeout(later, remaining);
			}
			return result;
		};

		throttled.cancel = function ()
		{
			clearTimeout(timeout);
			previous = 0;
			timeout = context = args = null;
		};

		return throttled;
	};

	//  Pure js debounce function to optimize resize method
	function debounce(func, wait, immediate)
	{
		var timeout;

		return function()
		{
			var context = this,
				args = arguments;

			clearTimeout(timeout);

			timeout = setTimeout(function() {
				timeout = null;

				if (!immediate) func.apply(context, args);
			}, wait);

			if (immediate && !timeout) func.apply(context, args);
		};
	};
}(jQuery));
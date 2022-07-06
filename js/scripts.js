$(".mobile-nav-hamburger svg").click(function() {
    $('#main-navigation').toggleClass('open');

});

$(window).on('resize', function() {

    if ($(window).width() <= 600) { 

        $('#main-navigation').removeClass('open');

    } 
});
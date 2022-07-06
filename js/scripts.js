// Mobile Menu onClick open
$(".mobile-nav-hamburger svg").click(function() {

    $('#main-navigation').toggleClass('open');

});

// Closes mobile navigation on resize
$(window).on('resize', function() {

    if ($(window).width() <= 600) { 
        
        $('#main-navigation').removeClass('open');

    } 
});
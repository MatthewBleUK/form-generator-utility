import "./jquery-3.6.0.min.js";
import "./jquery.modal-0.9.1.min.js";
import "./jquery-validate-1.19.5.min.js";

// Mobile Menu onClick open
$(".mobile-nav-hamburger svg").click(function () {

	$('#main-navigation').toggleClass('open');

});

// Closes mobile navigation on resize
$(window).on('resize', function () {

	if ($(window).width() <= 600) {

		$('#main-navigation').removeClass('open');

	}
});

// Validation for sign up form
$("#sign-up-form").validate({

	rules: {

		firstName: {

			required: true,
			minlength: 2
		},

		lastName: {

			required: true,
			minlength: 2
		},

		email: {

			required: true,
			email: true
		},

		password: {

			required: true,
			pwcheck: true,

		},

		matchingPassword: {

			required: true,
			equalTo: "#password"
		}
	},

	messages: {

		firstName: {

			required: "Please enter a first name",
			minlength: "Please enter at least 2 characters"
		},

		lastName: {

			required: "Please enter a last name"
		},

		email: {

			required: "Please enter an email",
			email: "Your email address must be in the format of name@domain.com"
		},

		password: {

			required: "Please enter a password",
		},

		matchingPassword: {

			required: "Please enter a confirm password",
			equalTo: "The confirm password must be the same as the password"
		}
	},

	submitHandler: function () {

		if(checkGoogleRecaptcha()) {
			return true;
		}

	}
});

// Validation for log in form
$("#login-form").validate({

	rules: {

		email: {

			required: true,
			email: true
		},

		password: {

			required: true
		}
	},

	messages: {

		email: {

			required: "Please enter a email",
			email: "Your email address must be in the format of name@domain.com"
		},

		password: {

			required: "Please enter a password"
		}

	},

	submitHandler: function () {

		if(checkGoogleRecaptcha()) {
			return true;
		}

	},

});

// Checks the strength of the password
$.validator.addMethod("pwcheck", function (value, element) {

	// Handles password value validation

	let password = value;

	if (!(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%&!£$%^&*?_+=-])(.{8,20}$)/.test(password))) {
		return false;
	}

	return true;

}, function (value, element) {

	// Handles error message label

	let password = $(element).val();

	if (!(/^(.{8,20}$)/.test(password))) {

		return 'Password must be between 8 to 20 characters long.';

	} else if (!(/^(?=.*[A-Z])/.test(password))) {

		return 'Password must contain at least one uppercase.';

	} else if (!(/^(?=.*[a-z])/.test(password))) {

		return 'Password must contain at least one lowercase.';

	} else if (!(/^(?=.*[0-9])/.test(password))) {

		return 'Password must contain at least one digit.';

	} else if (!(/^(?=.*[@#$%&!£$%^&*?_+=-])/.test(password))) {

		return "Password must contain special character";   // Special characters: @#$%&!£$%^&*?
	}

	return false;
});

// Validation for reset password form
$("#reset-password-form").validate({

	rules: {

		recoveryEmail: {

			required: true,
			email: true
		}
	},

	messages: {

		recoveryEmail: {

			required: "Please enter a email",
			email: "Your email address must be in the format of name@domain.com"
		}

	},

	submitHandler: function () {

	}
});

// Validation for change password form
$("#change-password-form").validate({

	rules: {

		new_password: {

			required: true,
			pwcheck: true
		},

		confirm_password: {

			required: true,
			equalTo: "#new-password"
		}
	},

	messages: {

		new_password: {

			required: "Please enter a password"
		},

		confirm_password: {

			required: "Please enter a confirm password",
			equalTo: "The confirm password must be the same as the password"
		}

	},

	submitHandler: function () {

	}
});

// Validation for sign up form
$("#add-new-form").validate({

	rules: {

		formName: {

			required: true,
			minlength: 2
		}

	},

	messages: {

		formName: {

			required: "Please enter a form name",
		}
	},

	submitHandler: function (event) {


    	addNewFormtoDom();
	}
});

// function addNewFormtoDom() {

//     let newFormTitle = $('#add-new-form #form-name').val();
// 	// let afterElement = $('.user-forms');

// 	console.log(newFormTitle);

// 	let afterEl2 = document.querySelector('.user-forms');

//     console.log(afterEl2);

//     let div = `
// 			<div class="form-entry">
// 				<h3 class="form-title">${newFormTitle}</h3>

// 				<p>Submissions: <span>0</span></p>

// 				<p><a href="#set-up-guide-modal" rel="modal:open">Set up guide</a></p>

// 				<div class="entry-dropdown">
// 					<i class="settings-gear-svg"></i>

// 					<div class="entry-dropdown-content">
// 						<a href="#">Delete</a>
// 					</div>
// 				</div>
// 			</div>
//     	`;

// 		console.log(div);

// 		afterEl2.insertAdjacentHTML("afterbegin", div);

// }

function checkGoogleRecaptcha() {

    var response = grecaptcha.getResponse();
	
    if(response.length === 0) {
        //reCaptcha not verified

		let error = document.querySelector('#recaptcha-error p');
		error.innerHTML = 'Please solve the recaptcha';

        return false;
    } 

	//reCaptch verified
	return true;

 }

 /* Changing modal url on click - for backend handling */

 // on open modal

 $('#personal-modal-btn').click(function(event) {

     changeURL('personal-details');

 });

$('#password-modal-btn').click(function(event) {

   changeURL('change-password');

});

$('#two-factor-modal-btn').click(function(event) {

   changeURL('two-factor-setup');

});

// on close modal

$('#personal-details-modal').on($.modal.CLOSE, function(event, modal) {

   changeURL('settings');

});

$('#password-modal').on($.modal.CLOSE, function(event, modal) {

   changeURL('settings');

});

$('#two-factor-modal').on($.modal.CLOSE, function(event, modal) {

   changeURL('settings');

});

function changeURL(url) {

    const state = {};
    const title = ''

    history.pushState(state, title, url);

}


// Homepage header nav scroll
$(window).scroll(function() {

	var sticky = $('#homepage header'),
		container = $('#homepage .header-container'),
		scroll = $(window).scrollTop();

	if (scroll >= 100) {

		sticky.css("box-shadow", "0 0 1.875rem 0 rgb(26 46 68 / 10%)");
		sticky.css("background", "#ffffff");

		container.css("height", "75px");

		$('#homepage header #desktop-navigation .active').css("border-bottom", "0px");

		//sticky.slideDown("slow");
	} else {

		sticky.css("box-shadow", "");
		sticky.css("background", "#f9f9f7");

		container.css("height", "85px");

		$('#homepage header #desktop-navigation .active').css("border-bottom", "4px solid");

	}

});

// Close mobile navigation on resize
window.addEventListener('resize', function(event) {
	$(".mobile-nav-hamburger").removeClass('opened');
	$("header").removeClass( "active");
	$("#main-content").removeClass("active");
}, true);

// Close mobile navigation on mobile menu click
$("#mobile-navigation a").click(function () {
	$(".mobile-nav-hamburger").removeClass('opened');
	$("header").removeClass( "active");
	$("#main-content").removeClass("active");
})

// Mobile navigation button toggle
$(".mobile-nav-hamburger").click(function() {
	this.classList.toggle('opened');
	$("header").toggleClass( "active");
	$("#main-content").toggleClass("active");
});

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

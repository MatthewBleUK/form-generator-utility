// Validation for sign up form
$("#sign-up-form").validate({

	rules: {

		fname: {

			required: true,
			minlength: 2
		},

		lname: {

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
			minlength: 8
		},

		confirm_password: {

			required: true,
			equalTo: "#password"
		}
	},

	messages: {

		fname: {

			required: "Please enter a first name",
			minlength: "Please enter at least 2 characters"
		},

		lname: {

			required: "Please enter a last name"
		},

		email: {

			required: "Please enter a email",
			email: "Your email address must be in the format of name@domain.com"
		},

		password: {

			required: "Please enter a password",
			minlength: "Please enter at least 2 characters"
		},

		confirm_password: {

			required: "Please enter a confirm password",
			equalTo: "The confirm password must be the same as the password"
		}
	},

	submitHandler: function () {

		$.ajax({

			type: "POST",
			url: '',   // Post url
			data: $('#sign-up-form').serialize(),

			success: function (response) {

				var jsonData = JSON.parse(response);

				if (jsonData.success == "1") {

					// success

				} else {

					// fail
				}
			}
		});
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

		$.ajax({

			type: "POST",
			url: '',   // Post url
			data: $('#login-form').serialize(),

			success: function (response) {

				var jsonData = JSON.parse(response);

				if (jsonData.success == "1") {

					// success

				} else {

					// fail
				}
			}
		});
	},

});

// Checks the strength of the password
$.validator.addMethod("pwcheck", function (value, element) {

	// Handles password value validation

	let password = value;

	if (!(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%&])(.{8,20}$)/.test(password))) {
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

	} else if (!(/^(?=.*[@#$%&])/.test(password))) {
		
		return "Password must contain special characters from @#$%&.";
	}

	return false;
});

// Validation for reset password form
$("#reset-password-form").validate({

	rules: {

		recover_email: {

			required: true,
			email: true
		}
	},

	messages: {

		recover_email: {

			required: "Please enter a email",
			email: "Your email address must be in the format of name@domain.com"
		}

	},

	submitHandler: function () {

		$.ajax({

			type: "POST",
			url: '',   // Post url
			data: $('#reset-password-form').serialize(),

			success: function (response) {

				var jsonData = JSON.parse(response);

				if (jsonData.success == "1") {

					// success

				} else {

					// fail
				}
			}
		});
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

		$.ajax({

			type: "POST",
			url: '',   // Post url
			data: $('#change-password-form').serialize(),

			success: function (response) {

				var jsonData = JSON.parse(response);

				if (jsonData.success == "1") {

					// success

				} else {

					// fail
				}
			}
		});
	}
});
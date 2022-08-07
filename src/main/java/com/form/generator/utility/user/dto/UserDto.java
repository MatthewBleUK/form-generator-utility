package com.form.generator.utility.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserDto {

	@NotNull
	@NotEmpty
	private String firstName;

	@NotNull
	@NotEmpty
	private String lastName;

	@NotNull
	@NotEmpty
	private String password;

	@NotNull
	@NotEmpty
	private String matchingPassword;

	@NotNull
	@NotEmpty
	private String email;
}


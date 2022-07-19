package com.form.generator.utility.user;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {

	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String rememberMe;
}
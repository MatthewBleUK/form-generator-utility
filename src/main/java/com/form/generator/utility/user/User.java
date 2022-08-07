package com.form.generator.utility.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity()
@Table(name = "USERS")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

	@Id
	@Column(name = "USERNAME", nullable = false)
	@EqualsAndHashCode.Include
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	// y or n
	@Column(name = "ENABLED")
	private String enabled;

	public User() {

		super();
		this.enabled="n";
	}
}
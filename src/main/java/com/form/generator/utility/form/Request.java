package com.form.generator.utility.form;

import javax.persistence.*;

import com.form.generator.utility.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity()
@Table(name = "request")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Request {

	@Id
	@Column(name = "id", nullable = false)
	private final String id;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(nullable = false, name = "username")
	private final User user;
}

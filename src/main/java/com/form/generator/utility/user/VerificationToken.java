package com.form.generator.utility.user;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "VERIFICATION_TOKEN")
@NoArgsConstructor
public class VerificationToken {

	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@EqualsAndHashCode.Include
	@Column(name = "id", updatable = true, nullable = false)
	private String id;

	@Column(name = "token")
	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "username")
	private User user;

	@Column(name = "expiry_date")
	private Date expiryDate;

	public VerificationToken(User user, String token) {

		super();
		expiryDate = calculateExpiryDate();
		this.user = user;
		this.token = token;
	}

	private Date calculateExpiryDate() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, EXPIRATION);

		return new Date(cal.getTime().getTime());
	}
}
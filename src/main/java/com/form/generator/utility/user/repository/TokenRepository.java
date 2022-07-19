package com.form.generator.utility.user.repository;

import java.util.List;

import com.form.generator.utility.user.VerificationToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository {

	private final SessionFactory sessionFactory;

	public TokenRepository(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	public void createToken(VerificationToken newToken) {

		try (Session session = sessionFactory.openSession()) {

			session.beginTransaction();
			session.save(newToken);
			session.getTransaction().commit();
		}
	}

	public List<VerificationToken> getVerificationToken(String token) {

		try (Session session = sessionFactory.openSession()) {

			Query<VerificationToken> query =
					session.createQuery("from VerificationToken where token = :token", VerificationToken.class);

			query.setParameter("token", token);

			return query.list();
		}
	}
}

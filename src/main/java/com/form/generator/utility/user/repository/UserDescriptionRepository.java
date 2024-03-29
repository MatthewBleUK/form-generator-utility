package com.form.generator.utility.user.repository;

import java.util.List;

import com.form.generator.utility.user.User;
import com.form.generator.utility.user.UserDescription;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDescriptionRepository {

	private final SessionFactory sessionFactory;

	@Autowired
	public UserDescriptionRepository(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	public void create(UserDescription userDescription) {

		try (Session session = sessionFactory.openSession()) {

			session.beginTransaction();
			session.save(userDescription);
			session.getTransaction().commit();
		}
	}

	public List<UserDescription> getUserDescriptionByEmail(String email) {

		try (Session session = sessionFactory.openSession()) {

			Query<UserDescription> query =
					session.createQuery("from UserDescription where user.email = :email", UserDescription.class);

			query.setParameter("email", email);

			return query.list();
		}
	}
}
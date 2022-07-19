package com.form.generator.utility.user.repository;

import java.util.List;

import com.form.generator.utility.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private final SessionFactory sessionFactory;

	@Autowired
	public UserRepository(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	public void create(User user) {

		try (Session session = sessionFactory.openSession()) {

			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		}
	}

	public List<User> getUserByEmail(String email) {

		try (Session session = sessionFactory.openSession()) {

			Query<User> query = session.createQuery("from User where email = :email", User.class);
			query.setParameter("email", email);

			return query.list();
		}
	}

	public void update(User user) {

		try (Session session = sessionFactory.openSession()) {

			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		}
	}
}

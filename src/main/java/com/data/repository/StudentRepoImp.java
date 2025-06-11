package com.data.repository;

import com.data.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepoImp implements StudentRepo{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public boolean existsByUsername(String username) {
        String hql = "SELECT COUNT(s) FROM Student s WHERE s.username = :username";
        try (Session session = sessionFactory.openSession()) {
            Long count = (Long) session.createQuery(hql)
                    .setParameter("username", username)
                    .uniqueResult();
            return count > 0;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String hql = "SELECT COUNT(s) FROM Student s WHERE s.email = :email";
        try (Session session = sessionFactory.openSession()) {
            Long count = (Long) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
            return count > 0;
        }
    }

    @Override
    public void save(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(student);
            tx.commit();
        }
    }

    @Override
    public boolean existsByPhone(String phone) {
        String hql = "SELECT COUNT(s) FROM Student s WHERE s.phone = :phone";
        try (Session session = sessionFactory.openSession()) {
            Long count = (Long) session.createQuery(hql)
                    .setParameter("phone", phone)
                    .uniqueResult();
            return count > 0;
        }
    }

    @Override
    public Student findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Student WHERE email = :email";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }
}

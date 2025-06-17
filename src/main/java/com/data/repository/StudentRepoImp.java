package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<Student> getStudentByPage(int pageNo, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            query.setFirstResult((pageNo - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public long countTotalStudent() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Student", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public void blockUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                student.setRole(false);
                session.update(student);
            }
            tx.commit();
        }
    }

    @Override
    public void unblockUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                student.setRole(true);
                session.update(student);
            }
            tx.commit();
        }
    }

//    @Override
//    public List<Student> searchByName(String name, String email, int id, int offset, int limit) {
//        Session session = sessionFactory.openSession();
//
//        String hql = """
//        FROM Student s
//        WHERE
//            (:id = 0 OR s.id = :id)
//            OR LOWER(s.name) LIKE CONCAT('%', :name, '%')
//            OR LOWER(s.email) LIKE CONCAT('%', :email, '%')
//    """;
//
//        Query<Student> query = session.createQuery(hql, Student.class);
//        query.setParameter("id", id);
//        query.setParameter("name", name.toLowerCase());
//        query.setParameter("email", email.toLowerCase());
//        query.setFirstResult((offset - 1) * limit);
//        query.setMaxResults(limit);
//
//        return query.getResultList();
//    }

}
